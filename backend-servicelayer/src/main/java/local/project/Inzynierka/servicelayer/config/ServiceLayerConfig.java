package local.project.Inzynierka.servicelayer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.project.Inzynierka.servicelayer.social.facebook.FacebookCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceLayerConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
