package local.project.Inzynierka.servicelayer.social;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import local.project.Inzynierka.servicelayer.config.ServiceLayerConfig;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.Error;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.Response;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenInspection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceLayerConfig.class)
public class FacebookAuthServiceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldDeserializeTokenInspection() throws IOException {
        Response<TokenInspection> actual = objectMapper.readValue("{\"data\":{\"app_id\":\"422141488475751\"," +
                                                                "\"type\":\"USER\"," +
                                                                "\"application\":\"ProjectCompanies\"," +
                                                                "\"data_access_expires_at\":1581713719," +
                                                                "\"error\":" +
                                                                "{\"code\":190," +
                                                                "\"message\":\"Error validating access token: Session has expired on Saturday, " +
                                                                "16-Nov-19 13:00:00 PST. The current time is Saturday, 16-Nov-19 13:13:49 PST.\"," +
                                                                "\"subcode\":463}," +
                                                                "\"expires_at\":1573938000," +
                                                                "\"is_valid\":false," +
                                                                "\"scopes\":" +
                                                                "[\"manage_pages\"," +
                                                                "\"pages_show_list\"," +
                                                                "\"public_profile\"]," +
                                                                "\"granular_scopes\":" +
                                                                "[{\"scope\":\"manage_pages\"}," +
                                                                "{\"scope\":\"pages_show_list\"}]," +
                                                                "\"user_id\":\"116363453141375\"}}", new TypeReference<Response<TokenInspection>>(){});


        TokenInspection expected = TokenInspection.builder()
                .app_id("422141488475751")
                .type("USER")
                .application("ProjectCompanies")
                .data_access_expires_at(1581713719L)
                .error(Error.builder()
                               .code(190)
                               .message("Error validating access token: Session has expired on Saturday, 16-Nov-19 13:00:00 PST. The current time is Saturday, 16-Nov-19 13:13:49 PST.")
                               .subCode(463)
                               .build())
                .expires_at(1573938000L)
                .is_valid(false)
                .scopes(Arrays.asList("manage_pages", "pages_show_list", "public_profile"))
                .granular_scopes(Arrays.asList(Map.of("scope", "manage_pages"), Map.of("scope", "pages_show_list")))
                .user_id(116363453141375L)
                .build();
        Assert.assertEquals(actual.getData(), expected);
    }
}
