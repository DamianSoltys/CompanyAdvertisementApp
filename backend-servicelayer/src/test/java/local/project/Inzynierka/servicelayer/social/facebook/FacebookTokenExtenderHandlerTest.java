package local.project.Inzynierka.servicelayer.social.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.project.Inzynierka.servicelayer.config.ServiceLayerConfig;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.TokenExtension;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceLayerConfig.class)
public class FacebookTokenExtenderHandlerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldDeserializeTokenExtension() throws IOException {
        TokenExtension actual = objectMapper.readValue("{\n" +
                                                               "  \"access_token\": \"EAAFZC73l5LmcBADsB7NvcoI7mZC7Bc9j8zbyQyzxn5FYZB5inTp4ZB6PJgUM6CQJAeyLRXMfOJX3dkNnidZAmNUrVAta1aIWAJZB7keUN9N8uiSGZCEvDBYNwwYEQdbRVUrMaSCeRohrlZC8AwOPo2FSHVCQUZCkGeNxiDzQlD5BPhe0I74hlwwPM\",\n" +
                                                               "  \"token_type\": \"bearer\",\n" +
                                                               "  \"expires_in\": 5183999\n" +
                                                               "}",  TokenExtension.class);

        TokenExtension expected = TokenExtension.builder()
                .access_token("EAAFZC73l5LmcBADsB7NvcoI7mZC7Bc9j8zbyQyzxn5FYZB5inTp4ZB6PJgUM6CQJAeyLRXMfOJX3dkNnidZAmNUrVAta1aIWAJZB7keUN9N8uiSGZCEvDBYNwwYEQdbRVUrMaSCeRohrlZC8AwOPo2FSHVCQUZCkGeNxiDzQlD5BPhe0I74hlwwPM")
                .expires_in(5183999)
                .token_type("bearer")
                .build();

        Assert.assertEquals(expected, actual);
    }
}
