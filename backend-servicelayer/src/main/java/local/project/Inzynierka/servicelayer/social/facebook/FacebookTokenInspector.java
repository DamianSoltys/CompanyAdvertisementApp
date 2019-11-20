package local.project.Inzynierka.servicelayer.social.facebook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.Response;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenInspection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

@Service
@Slf4j
public class FacebookTokenInspector {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final FacebookCredentials facebookCredentials;

    public FacebookTokenInspector(ObjectMapper objectMapper, RestTemplate restTemplate, FacebookCredentials facebookCredentials) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.facebookCredentials = facebookCredentials;
    }

    public TokenInspection inspectToken(String token) throws IOException {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://graph.facebook.com/v5.0/debug_token")
                .queryParam("input_token", token)
                .queryParam("access_token", facebookCredentials.getAppToken());
        var result = restTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, getDecoratedHttpEntity(), String.class);

        log.info(String.valueOf(result));
        log.info(String.valueOf(result.getBody()));
        Response<TokenInspection> tokenInspectionResponse = objectMapper.readValue(result.getBody(), new TypeReference<Response<TokenInspection>>() {});
        return tokenInspectionResponse.getData();
    }

    private HttpEntity getDecoratedHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(headers);
    }
}
