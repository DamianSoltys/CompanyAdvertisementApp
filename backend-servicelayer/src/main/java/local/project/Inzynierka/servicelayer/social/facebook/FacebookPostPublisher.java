package local.project.Inzynierka.servicelayer.social.facebook;

import local.project.Inzynierka.persistence.entity.FacebookToken;
import local.project.Inzynierka.persistence.entity.SocialProfile;
import local.project.Inzynierka.persistence.repository.FacebookTokenRepository;
import local.project.Inzynierka.persistence.repository.SocialProfileRepository;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStatus;
import local.project.Inzynierka.servicelayer.dto.social.SocialPlatform;
import local.project.Inzynierka.servicelayer.promotionitem.event.SendingEvent;
import local.project.Inzynierka.servicelayer.social.SocialMediaConnectionService;
import local.project.Inzynierka.servicelayer.social.facebook.event.FacebookPostWithPhotosEvent;
import local.project.Inzynierka.servicelayer.social.facebook.event.SimpleFacebookPostEvent;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.SuccessfulPagePost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FacebookPostPublisher {

    private final FacebookTokenRepository facebookTokenRepository;
    private final SocialProfileRepository socialProfileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FacebookTemplate facebookTemplate;

    private final SocialMediaConnectionService socialMediaConnectionService;


    public FacebookPostPublisher(FacebookTokenRepository facebookTokenRepository, SocialProfileRepository socialProfileRepository, ApplicationEventPublisher applicationEventPublisher, FacebookTemplate facebookTemplate, SocialMediaConnectionService socialMediaConnectionService) {
        this.facebookTokenRepository = facebookTokenRepository;
        this.socialProfileRepository = socialProfileRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.facebookTemplate = facebookTemplate;
        this.socialMediaConnectionService = socialMediaConnectionService;
    }

    @Async
    @EventListener
    @Transactional
    public void publishSimpleFacebookPost(SimpleFacebookPostEvent postEvent) throws IOException {

        if (!socialMediaConnectionService.existActiveFacebookConnection(postEvent.getCompanyId())) {
            applicationEventPublisher.publishEvent(getFacebookFailedConnectionEvent(postEvent));
        } else {
            FacebookToken pageToken = getPageAccessToken(postEvent);
            ResponseEntity<?> postResult = facebookTemplate.exchangeForEntity(getPostToPageUri(pageToken, postEvent), HttpMethod.POST, String.class);
            checkPostStatus(postEvent, postResult);
        }
    }

    private void checkPostStatus(SimpleFacebookPostEvent postEvent, ResponseEntity<?> postResult) throws IOException {
        if (postResult.getStatusCode().is2xxSuccessful()) {
            SuccessfulPagePost successfulPagePost = facebookTemplate.readValue((String) postResult.getBody(), SuccessfulPagePost.class);
            applicationEventPublisher.publishEvent(getPostToPageSuccessEvent(successfulPagePost, postEvent));
        } else {
            applicationEventPublisher.publishEvent(getPostToPageFailureEvent(postEvent));
        }
    }

    private FacebookToken getPageAccessToken(SimpleFacebookPostEvent postEvent) {
        SocialProfile socialProfile =
                socialProfileRepository.findByCompany_IdAndAndSocialMediaPlatform_SocialMediaPlatform(postEvent.getCompanyId(),
                                                                                                      SocialPlatform.FACEBOOK.toString());
        return facebookTokenRepository.findByFacebookSocialProfile_SocialProfileAndType(socialProfile, "PAGE");
    }

    @EventListener
    @Async
    @Transactional
    public void publishFacebookPostWithPhotos(FacebookPostWithPhotosEvent postWithPhotosEvent) throws IOException {

        if (!socialMediaConnectionService.existActiveFacebookConnection(postWithPhotosEvent.getSimpleFacebookPostEvent().getCompanyId())) {
            applicationEventPublisher.publishEvent(getFacebookFailedConnectionEvent(postWithPhotosEvent.getSimpleFacebookPostEvent()));
        } else {
            FacebookToken pageAccessToken = getPageAccessToken(postWithPhotosEvent.getSimpleFacebookPostEvent());
            List<Object> photoIDs = getUploadedPhotosIDs(postWithPhotosEvent, pageAccessToken);

            if (uploadedSuccessfullyAllPhotos(postWithPhotosEvent, photoIDs)) {
                var postResult = facebookTemplate.postForEntity(getPostFeedUri(pageAccessToken),
                                                                getPostWithPhotoData(postWithPhotosEvent, pageAccessToken, photoIDs),
                                                                String.class);
                log.info(postResult.getBody());
                checkPostStatus(postWithPhotosEvent.getSimpleFacebookPostEvent(), postResult);
            } else {
                applicationEventPublisher.publishEvent(getFailedToUploadPhotosEvent(postWithPhotosEvent));
            }
        }

    }

    private UriComponents getPostFeedUri(FacebookToken pageAccessToken) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("graph.facebook.com")
                .path(String.valueOf(pageAccessToken.getFacebookSocialProfile().getPageId()))
                .path("/feed")
                .build();
    }

    private boolean uploadedSuccessfullyAllPhotos(FacebookPostWithPhotosEvent postWithPhotosEvent, List<Object> photoIDs) {
        return photoIDs.size() == postWithPhotosEvent.getPhotoURLs().size();
    }

    private List<Object> getUploadedPhotosIDs(FacebookPostWithPhotosEvent postWithPhotosEvent, FacebookToken pageAccessToken) {
        return postWithPhotosEvent.getPhotoURLs()
                .stream()
                .map(photoURL -> {
                    MultiValueMap<String, Object> parts = getPhotoPostData(pageAccessToken, photoURL);
                    return facebookTemplate.postForObject(getPostPhotoUri(pageAccessToken), parts);
                })
                .map(response -> response.get("id"))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private MultiValueMap<String, Object> getPhotoPostData(FacebookToken pageAccessToken, String photoURL) {
        Resource photoResource = new FileSystemResource(photoURL);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("source", photoResource);
        parts.add("access_token", pageAccessToken.getAccessToken());
        parts.add("published", "false");
        return parts;
    }

    private MultiValueMap<String, Object> getPostWithPhotoData(FacebookPostWithPhotosEvent postWithPhotosEvent, FacebookToken pageAccessToken, List<Object> photosIDs) {
        MultiValueMap<String, Object> postData = new LinkedMultiValueMap<>();
        postData.add("message", postWithPhotosEvent.getSimpleFacebookPostEvent().getContent());
        postData.add("access_token", pageAccessToken.getAccessToken());
        int photoIDIndex = 0;
        for (var photo : photosIDs) {
            postData.add(String.format("attached_media[%d]", photoIDIndex),
                         String.format("{\"media_fbid\":\"%s\"}", photo));
            photoIDIndex++;
        }
        return postData;
    }

    private SendingEvent getFailedToUploadPhotosEvent(FacebookPostWithPhotosEvent postWithPhotosEvent) {
        return SendingEvent.builder()
                .destination(postWithPhotosEvent.getSimpleFacebookPostEvent().getDestination())
                .promotionItemUUUID(postWithPhotosEvent.getSimpleFacebookPostEvent().getPromotionItemUUID())
                .sendingStatus(SendingStatus.FAILED)
                .detail("Failed to upload photos.")
                .build();
    }

    private UriComponents getPostPhotoUri(FacebookToken pageToken) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("graph.facebook.com")
                .path(String.valueOf(pageToken.getFacebookSocialProfile().getPageId()))
                .path("/photos")
                .build();
    }

    private SendingEvent getPostToPageSuccessEvent(SuccessfulPagePost postResult, SimpleFacebookPostEvent postEvent) {
        return SendingEvent.builder()
                .destination(Destination.FB)
                .sendingStatus(SendingStatus.SENT)
                .promotionItemUUUID(postEvent.getPromotionItemUUID())
                .detail(String.format("Post resource: https://facebook.com/%s", postResult.getId()))
                .build();
    }

    private SendingEvent getPostToPageFailureEvent(SimpleFacebookPostEvent postEvent) {
        return SendingEvent.builder().sendingStatus(SendingStatus.FAILED)
                .destination(Destination.FB)
                .promotionItemUUUID(postEvent.getPromotionItemUUID())
                .build();
    }

    private UriComponents getPostToPageUri(FacebookToken pageToken, SimpleFacebookPostEvent postEvent) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("graph.facebook.com")
                .path(String.valueOf(pageToken.getFacebookSocialProfile().getPageId()))
                .path("/feed")
                .queryParam("message", postEvent.getContent())
                .queryParam("access_token", pageToken.getAccessToken())
                .build();
    }

    private SendingEvent getFacebookFailedConnectionEvent(SimpleFacebookPostEvent postEvent) {
        return SendingEvent.builder()
                .detail(String.format("Company %d is not connected to facebook", postEvent.getCompanyId()))
                .promotionItemUUUID(postEvent.getPromotionItemUUID())
                .sendingStatus(SendingStatus.FAILED)
                .destination(Destination.FB)
                .build();
    }
}
