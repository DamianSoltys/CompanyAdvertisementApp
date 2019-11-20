package local.project.Inzynierka.servicelayer.social.facebook;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.FacebookSocialProfile;
import local.project.Inzynierka.persistence.entity.FacebookToken;
import local.project.Inzynierka.persistence.entity.FacebookTokenScope;
import local.project.Inzynierka.persistence.entity.SocialProfile;
import local.project.Inzynierka.persistence.entity.TokenScopeType;
import local.project.Inzynierka.persistence.repository.FacebookSocialProfileRepository;
import local.project.Inzynierka.persistence.repository.FacebookTokenRepository;
import local.project.Inzynierka.persistence.repository.FacebookTokenScopesRepository;
import local.project.Inzynierka.persistence.repository.SocialPlatformRepository;
import local.project.Inzynierka.persistence.repository.SocialProfileRepository;
import local.project.Inzynierka.servicelayer.social.facebook.event.FacebookTokenInstalledEvent;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.EmptyData;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.FacebookLogin;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.PageData;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.QueriedPageInfo;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenInspection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FacebookSocialProfileInstallerService {

    private final SocialProfileRepository socialProfileRepository;
    private final SocialPlatformRepository socialPlatformRepository;
    private final FacebookTokenInspector facebookTokenInspector;
    private final FacebookTemplate facebookTemplate;
    private final FacebookSocialProfileRepository facebookSocialProfileRepository;
    private final FacebookTokenRepository facebookTokenRepository;
    private final FacebookTokenScopesRepository facebookTokenScopesRepository;

    public FacebookSocialProfileInstallerService(SocialProfileRepository socialProfileRepository, SocialPlatformRepository socialPlatformRepository,
                                                 FacebookTokenInspector facebookTokenInspector, FacebookTemplate facebookTemplate, FacebookSocialProfileRepository facebookSocialProfileRepository, FacebookTokenRepository facebookTokenRepository, FacebookTokenScopesRepository facebookTokenScopesRepository) {
        this.socialProfileRepository = socialProfileRepository;
        this.socialPlatformRepository = socialPlatformRepository;
        this.facebookTemplate = facebookTemplate;
        this.facebookTokenInspector = facebookTokenInspector;
        this.facebookSocialProfileRepository = facebookSocialProfileRepository;
        this.facebookTokenRepository = facebookTokenRepository;
        this.facebookTokenScopesRepository = facebookTokenScopesRepository;
    }

    @EventListener
    @Async
    public void handleTokenInstallation(FacebookTokenInstalledEvent event) throws IOException {

        FacebookLogin facebookLogin = event.getFacebookLogin();
        TokenInspection tokenInspection = event.getTokenInspection();

        var userAccounts = facebookTemplate.exchangeForEntity(getCheckForPagesUri(event, tokenInspection), HttpMethod.GET, String.class);
        if (hasNoPage(userAccounts.getBody())) {
            log.info(String.format("User %s has no page registered. ", facebookLogin.getAuthResponse().getUserID()));
            installTokens(event, facebookLogin, tokenInspection, null, null, null);
            return;
        }

        QueriedPageInfo page = facebookTemplate.readValue(userAccounts.getBody(), QueriedPageInfo.class);
        TokenInspection pageTokenInspection = facebookTokenInspector.inspectToken(page.getData().getAccess_token());

        PageData pageData = facebookTemplate.exchange(getPageUri(page), HttpMethod.GET, PageData.class);
        log.info(String.valueOf(pageData));

        installTokens(event, facebookLogin, tokenInspection, page, pageTokenInspection, pageData);
    }

    private void installTokens(FacebookTokenInstalledEvent event, FacebookLogin facebookLogin, TokenInspection tokenInspection, QueriedPageInfo page, TokenInspection pageTokenInspection, PageData pageData) {
        SocialProfile socialProfile = socialProfileRepository.save(buildSocialProfile(facebookLogin, pageData));
        FacebookSocialProfile facebookSocialProfile = facebookSocialProfileRepository.save(buildFacebookSocialProfile(facebookLogin, pageData, socialProfile));

        FacebookToken userToken = facebookTokenRepository.save(buildUserToken(event, tokenInspection, facebookSocialProfile));
        if(page != null ) {
            facebookTokenRepository.save(buildPageToken(page, pageTokenInspection, facebookSocialProfile));
        }

        List<FacebookTokenScope> scopes = tokenInspection.getScopes().stream()
                .map(scope -> FacebookTokenScope.builder()
                        .facebookToken(userToken)
                        .scope(scope)
                        .tokenScopeType(TokenScopeType.NORMAL)
                        .build())
                .collect(Collectors.toList());
        facebookTokenScopesRepository.saveAll(scopes);
    }

    private FacebookToken buildPageToken(QueriedPageInfo pageInfo, TokenInspection pageTokenInspection, FacebookSocialProfile facebookSocialProfile) {
        return FacebookToken.builder()
                .accessToken(pageInfo.getData().getAccess_token())
                .type(pageTokenInspection.getType())
                .isValid(pageTokenInspection.getIs_valid())
                .issuedAt(pageTokenInspection.getIssued_at())
                .facebookSocialProfile(facebookSocialProfile)
                .expiresAt(pageTokenInspection.getExpires_at())
                .dataAccessExpiresAt(pageTokenInspection.getData_access_expires_at())
                .build();
    }

    private FacebookToken buildUserToken(FacebookTokenInstalledEvent event, TokenInspection tokenInspection, FacebookSocialProfile facebookSocialProfile) {
        return FacebookToken.builder()
                .accessToken(event.getInspectedToken())
                .dataAccessExpiresAt(tokenInspection.getData_access_expires_at())
                .expiresAt(tokenInspection.getExpires_at())
                .isValid(tokenInspection.getIs_valid())
                .facebookSocialProfile(facebookSocialProfile)
                .issuedAt(tokenInspection.getIssued_at())
                .type(tokenInspection.getType())
                .build();
    }

    private FacebookSocialProfile buildFacebookSocialProfile(FacebookLogin facebookLogin, PageData pageData, SocialProfile socialProfile) {

       Long pageId = pageData == null ? null : pageData.getId();

        return FacebookSocialProfile.builder()
                .pageId(pageId)
                .socialProfile(socialProfile)
                .userId(facebookLogin.getAuthResponse().getUserID())
                .userName("")
                .build();
    }

    private SocialProfile buildSocialProfile(FacebookLogin facebookLogin, PageData pageData) {

        var socialMediaPlatform = socialPlatformRepository.findBySocialMediaPlatform("facebook");
        String socialURL = pageData == null ? "" : String.format("https://facebook.com/%s-%s", pageData.getName()
                .replace(" ", "-"), pageData.getId());

        return SocialProfile.builder()
                .id(SocialProfile.PK.builder()
                            .companyId(facebookLogin.getCompanyId())
                            .platformId(socialMediaPlatform.getId())
                            .build())
                .socialMediaPlatform(socialMediaPlatform)
                .company(Company.builder().id(facebookLogin.getCompanyId()).build())
                .URL(socialURL)
                .build();
    }

    private UriComponents getPageUri(QueriedPageInfo page) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("graph.facebook.com")
                .path(String.valueOf(page.getData().getId()))
                .queryParam("access_token", page.getData().getAccess_token())
                .build();
    }

    private UriComponents getCheckForPagesUri(FacebookTokenInstalledEvent event, TokenInspection tokenInspection) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("graph.facebook.com")
                .path(String.valueOf(tokenInspection.getUser_id()))
                .path("/accounts")
                .queryParam("access_token", event.getInspectedToken())
                .build();
    }

    private boolean hasNoPage(String accounts) {
        return EmptyData.EMPTY_DATA.toString().equals(accounts);
    }

}
