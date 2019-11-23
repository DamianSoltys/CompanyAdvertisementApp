package local.project.Inzynierka.servicelayer.social.facebook;

import com.fasterxml.jackson.core.type.TypeReference;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.Response;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenInspection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@Slf4j
public class FacebookTokenInspector {

    private final FacebookCredentials facebookCredentials;
    private final FacebookTemplate facebookTemplate;

    public FacebookTokenInspector(FacebookCredentials facebookCredentials, FacebookTemplate facebookTemplate) {
        this.facebookCredentials = facebookCredentials;
        this.facebookTemplate = facebookTemplate;
    }

    public TokenInspection inspectToken(String token) throws IOException {

        var result = facebookTemplate.exchange(
                getInspectTokenUri(token), HttpMethod.GET, new TypeReference<Response<TokenInspection>>(){});

        log.info(String.valueOf(result));
        return result.getData();
    }

    private UriComponents getInspectTokenUri(String token) {
        return UriComponentsBuilder.fromUriString("https://graph.facebook.com/v5.0/debug_token")
                .queryParam("input_token", token)
                .queryParam("access_token", facebookCredentials.getAppToken())
                .build();
    }

}
