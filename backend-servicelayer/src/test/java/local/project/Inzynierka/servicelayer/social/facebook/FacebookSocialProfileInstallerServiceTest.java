package local.project.Inzynierka.servicelayer.social.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.project.Inzynierka.servicelayer.config.ServiceLayerConfig;
import local.project.Inzynierka.servicelayer.social.facebook.fbapi.QueriedPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceLayerConfig.class)
@Slf4j
public class FacebookSocialProfileInstallerServiceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldDeserializePageData() throws IOException {

        String json = "{\"data\":[{\"access_token\":\"EAAFZC73l3LmcBAG6iLWFhbcfwTafjqLjkY9n4O3PHmNrqNO8G7a9Xu7z8Qolez7ovl7NVib0SuDaAJi3Nr05Ql39caPdYZBXGE3AIosLBpEFDJ9UCAYWmRZC363gz3PcVClXDVeWuWMHLZC0MYkecBA7Sw0BiWLESTqZCmZA2HrvwZBf2JLgsxK8c6WXbgRkbwZD\",\"category\":\"Patio\\/ogr\\u00f3d\",\"category_list\":[{\"id\":\"2222\",\"name\":\"Patio\\/ogr\\u00f3d\"}],\"name\":\"Moja strona\",\"id\":\"106000967536283\",\"tasks\":[\"ANALYZE\",\"ADVERTISE\",\"MODERATE\",\"CREATE_CONTENT\",\"MANAGE\"]}]}";

        QueriedPageInfo actual = objectMapper.readValue(json, QueriedPageInfo.class);

        QueriedPageInfo expected = QueriedPageInfo.builder()
                .data(new QueriedPageInfo.Data[]{
                        new QueriedPageInfo.Data("EAAFZC73l3LmcBAG6iLWFhbcfwTafjqLjkY9n4O3PHmNrqNO8G7a9Xu7z8Qolez7ovl7NVib0SuDaAJi3Nr05Ql39caPdYZBXGE3AIosLBpEFDJ9UCAYWmRZC363gz3PcVClXDVeWuWMHLZC0MYkecBA7Sw0BiWLESTqZCmZA2HrvwZBf2JLgsxK8c6WXbgRkbwZD", "106000967536283")})
                .build();




        Assert.assertEquals(expected, actual);
    }
}
