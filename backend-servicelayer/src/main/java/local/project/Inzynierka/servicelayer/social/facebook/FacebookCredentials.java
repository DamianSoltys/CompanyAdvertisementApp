package local.project.Inzynierka.servicelayer.social.facebook;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:social.properties")
@ConfigurationProperties(prefix = "credentials.social.facebook")
@Data
public class FacebookCredentials {
    private String appSecret;
    private String appId;
    private String appToken;
}
