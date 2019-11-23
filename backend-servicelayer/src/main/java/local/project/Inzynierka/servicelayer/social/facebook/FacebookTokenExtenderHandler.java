package local.project.Inzynierka.servicelayer.social.facebook;

import local.project.Inzynierka.servicelayer.social.facebook.event.FacebookTokenInstalledEvent;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.FacebookLogin;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenExtension;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenInspection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@Slf4j
public class FacebookTokenExtenderHandler {

    private final FacebookCredentials facebookCredentials;
    private final FacebookTokenInspector facebookTokenInspector;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FacebookTemplate facebookTemplate;

    public FacebookTokenExtenderHandler(FacebookCredentials facebookCredentials, FacebookTokenInspector facebookTokenInspector, ApplicationEventPublisher applicationEventPublisher, FacebookTemplate facebookTemplate) {
        this.facebookCredentials = facebookCredentials;
        this.facebookTokenInspector = facebookTokenInspector;
        this.applicationEventPublisher = applicationEventPublisher;
        this.facebookTemplate = facebookTemplate;
    }

    @Async
    @EventListener
    public void handleTokenExtension(FacebookLogin facebookLogin) throws IOException {

        var result = facebookTemplate.exchangeForEntity(
                getUriComponentsBuilder(facebookLogin), HttpMethod.GET, String.class);

        log.info(String.valueOf(result));
        String tokenToInspect;
        boolean tokenExtended = tokenExtendedSuccessfully(result);
        if (tokenExtended) {
            TokenExtension tokenExtension = facebookTemplate.readValue(result.getBody(), TokenExtension.class);
            log.info(String.valueOf(tokenExtension));
            tokenToInspect = tokenExtension.getAccess_token();
        } else {
            tokenToInspect = facebookLogin.getAuthResponse().getAccessToken();
        }

        TokenInspection tokenInspection = facebookTokenInspector.inspectToken(tokenToInspect);
        log.info(String.valueOf(tokenInspection));
        var event = FacebookTokenInstalledEvent.builder()
                .facebookLogin(facebookLogin)
                .tokenInspection(tokenInspection)
                .inspectedToken(tokenToInspect)
                .build();
        log.info(String.valueOf(event));

        publishInstallTokenEvent(event);

    }

    private void publishInstallTokenEvent(FacebookTokenInstalledEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    private UriComponents getUriComponentsBuilder(FacebookLogin facebookLogin) {
        return UriComponentsBuilder.fromUriString("https://graph.facebook.com/oauth/access_token")
                .queryParam("client_id", facebookCredentials.getAppId())
                .queryParam("client_secret", facebookCredentials.getAppSecret())
                .queryParam("grant_type", "fb_exchange_token")
                .queryParam("fb_exchange_token", facebookLogin.getAuthResponse().getAccessToken())
                .queryParam("access_token", facebookLogin.getAuthResponse().getAccessToken()).build();
    }

    private boolean tokenExtendedSuccessfully(ResponseEntity<String> result) {
        return result.getStatusCode().is2xxSuccessful();
    }
}
