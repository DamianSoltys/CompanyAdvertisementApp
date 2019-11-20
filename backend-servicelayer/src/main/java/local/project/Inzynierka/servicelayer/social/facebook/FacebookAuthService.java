package local.project.Inzynierka.servicelayer.social.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.FacebookLogin;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.QueriedPageInfo;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@RestController
@Slf4j
public class FacebookAuthService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final RestTemplate restTemplate;
    private final FacebookCredentials facebookCredentials;

    public FacebookAuthService(ApplicationEventPublisher applicationEventPublisher, @Qualifier("fbRestTemplate") RestTemplate restTemplate, ObjectMapper objectMapper, FacebookCredentials facebookCredentials) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.restTemplate = restTemplate;
        this.facebookCredentials = facebookCredentials;
    }

    private HttpEntity getDecoratedHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(headers);
    }

    @GetMapping("/api/fb/generate_app_access_token")
    public void generateAppAccessToken() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://graph.facebook.com/oauth/access_token")
                .queryParam("client_id", facebookCredentials.getAppId())
                .queryParam("client_secret", facebookCredentials.getAppSecret())
                .queryParam("grant_type","client_credentials");
        var result = restTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, getDecoratedHttpEntity(), String.class).getBody();
        log.info(String.valueOf(result));
    }

    @PostMapping("/api/fb/login_in")
    public ResponseEntity<?> login(@RequestBody FacebookLogin facebookLogin) {

        applicationEventPublisher.publishEvent(facebookLogin);

        return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson("OK"));
    }

}
