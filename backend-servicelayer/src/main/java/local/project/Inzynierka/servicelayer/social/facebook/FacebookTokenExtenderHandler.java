package local.project.Inzynierka.servicelayer.social.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.project.Inzynierka.servicelayer.social.facebook.event.FacebookTokenInstalledEvent;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.FacebookLogin;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenExtension;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenInspection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

@Service
@Slf4j
public class FacebookTokenExtenderHandler {

    private final RestTemplate restTemplate;
    private final FacebookCredentials facebookCredentials;
    private final ObjectMapper objectMapper;
    private final FacebookTokenInspector facebookTokenInspector;
    private final ApplicationEventPublisher applicationEventPublisher;

    public FacebookTokenExtenderHandler(@Qualifier("fbRestTemplate") RestTemplate restTemplate, FacebookCredentials facebookCredentials, ObjectMapper objectMapper, FacebookTokenInspector facebookTokenInspector, ApplicationEventPublisher applicationEventPublisher) {
        this.restTemplate = restTemplate;
        this.facebookCredentials = facebookCredentials;
        this.objectMapper = objectMapper;
        this.facebookTokenInspector = facebookTokenInspector;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Async
    @EventListener
    public void handleTokenExtension(FacebookLogin facebookLogin) throws IOException {

        UriComponentsBuilder extendTokenUriBuilder = getUriComponentsBuilder(facebookLogin);
        var result = restTemplate.exchange(
                extendTokenUriBuilder.toUriString(), HttpMethod.GET, getDecoratedHttpEntity(), String.class);

        log.info(String.valueOf(result));
        log.info(String.valueOf(result.getBody()));
        String tokenToInspect;
        boolean tokenExtended = tokenExtendedSuccessfully(result);
        if (tokenExtended) {
            TokenExtension tokenExtension = objectMapper.readValue(result.getBody(), TokenExtension.class);
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
        installToken(event);

    }

    private void installToken(FacebookTokenInstalledEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    private UriComponentsBuilder getUriComponentsBuilder(FacebookLogin facebookLogin) {
        return UriComponentsBuilder.fromUriString("https://graph.facebook.com/oauth/access_token")
                .queryParam("client_id", facebookCredentials.getAppId())
                .queryParam("client_secret", facebookCredentials.getAppSecret())
                .queryParam("grant_type", "fb_exchange_token")
                .queryParam("fb_exchange_token", facebookLogin.getAuthResponse().getAccessToken())
                .queryParam("access_token", facebookLogin.getAuthResponse().getAccessToken());
    }

    private boolean tokenExtendedSuccessfully(ResponseEntity<String> result) {
        return result.getStatusCode().is2xxSuccessful();
    }

    private HttpEntity getDecoratedHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(headers);
    }
}
