package local.project.Inzynierka.servicelayer.social.facebook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class FacebookTemplate {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public FacebookTemplate(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> T exchange(UriComponents uriComponents, HttpMethod httpMethod, Class<T> tClass) throws IOException {
        var result = restTemplate.exchange(uriComponents.toUriString(), httpMethod, getDecoratedHttpEntity(), String.class);
        return objectMapper.readValue(result.getBody(), tClass);
    }

    public <T> T exchange(UriComponents uriComponents, HttpMethod httpMethod, TypeReference<T> responseType) throws IOException {
        ResponseEntity<String> exchange = restTemplate.exchange(uriComponents.toUriString(), httpMethod, getDecoratedHttpEntity(), String.class);
        return objectMapper.readValue(exchange.getBody(), responseType);
    }

    public <T> ResponseEntity<T> exchangeForEntity(UriComponents uriComponents, HttpMethod httpMethod, Class<T> tClass) {
        return restTemplate.exchange(uriComponents.toUriString(), httpMethod, getDecoratedHttpEntity(), tClass);
    }

    public <T> T readValue(String value, Class<T> tClass) throws IOException {
        return objectMapper.readValue(value, tClass);
    }

    private HttpEntity getDecoratedHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(headers);
    }

    public Map postForObject(UriComponents uriComponents, Object data) {
        return restTemplate.postForObject(uriComponents.toUriString(), data, Map.class);
    }

    public <T> ResponseEntity<T> postForEntity(UriComponents uriComponents, Object data, Class<T> responseType) {
        return restTemplate.postForEntity(uriComponents.toUriString(), data, responseType);
    }
}
