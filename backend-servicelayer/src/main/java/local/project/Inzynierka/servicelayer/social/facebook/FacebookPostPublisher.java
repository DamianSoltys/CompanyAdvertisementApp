package local.project.Inzynierka.servicelayer.social.facebook;

import local.project.Inzynierka.persistence.entity.FacebookToken;
import local.project.Inzynierka.persistence.entity.SocialProfile;
import local.project.Inzynierka.persistence.repository.FacebookTokenRepository;
import local.project.Inzynierka.persistence.repository.SocialProfileRepository;
import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStatus;
import local.project.Inzynierka.servicelayer.dto.social.SocialPlatform;
import local.project.Inzynierka.servicelayer.dto.social.Status;
import local.project.Inzynierka.servicelayer.promotionitem.event.SendingEvent;
import local.project.Inzynierka.servicelayer.social.SocialMediaConnectionService;
import local.project.Inzynierka.servicelayer.social.facebook.event.SimpleFacebookPostEvent;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.SuccessfulPagePost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

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

        var connections = socialMediaConnectionService.getSocialProfileConnections(postEvent.getCompanyId())
                .stream().filter(connection -> connection.getSocialPlatform().equals(SocialPlatform.FACEBOOK) &&
                        connection.getConnectionStatus().getStatus().equals(Status.CONNECTED)).findAny();
        if (connections.isEmpty()) {
            var failEvent = getFacebookFailedSendingEvent(postEvent);
            applicationEventPublisher.publishEvent(failEvent);
        } else {
            SocialProfile socialProfile =
                    socialProfileRepository.findByCompany_IdAndAndSocialMediaPlatform_SocialMediaPlatform(postEvent.getCompanyId(),
                                                                                                          SocialPlatform.FACEBOOK.toString());
            FacebookToken pageToken = facebookTokenRepository.findByFacebookSocialProfile_SocialProfileAndType(socialProfile, "PAGE");
            ResponseEntity<?> postResult = facebookTemplate.exchangeForEntity(getPostToPageUri(pageToken, postEvent), HttpMethod.POST, String.class);

            if (postResult.getStatusCode().is2xxSuccessful()) {
                SuccessfulPagePost successfulPagePost = facebookTemplate.readValue((String) postResult.getBody(), SuccessfulPagePost.class);
                var successEvent = getPostToPageSuccessEvent(successfulPagePost, postEvent);
                applicationEventPublisher.publishEvent(successEvent);
            } else {
                var failEvent = getPostToPageFailureEvent(postEvent);
                applicationEventPublisher.publishEvent(failEvent);
            }
        }
    }

    private SendingEvent getPostToPageSuccessEvent(SuccessfulPagePost postResult, SimpleFacebookPostEvent postEvent) {
        return SendingEvent.builder()
                .destination(Destination.FB)
                .sendingStatus(SendingStatus.SENT)
                .promotionItemUUUID(postEvent.getPromotionItemUUID())
                .detail(String.format("Post resource: https://graph.facebook.com/%s", postResult.getId()))
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

    private SendingEvent getFacebookFailedSendingEvent(SimpleFacebookPostEvent postEvent) {
        return SendingEvent.builder()
                .detail(String.format("Company %d is not connected to facebook", postEvent.getCompanyId()))
                .promotionItemUUUID(postEvent.getPromotionItemUUID())
                .sendingStatus(SendingStatus.FAILED)
                .destination(Destination.FB)
                .build();
    }
}
