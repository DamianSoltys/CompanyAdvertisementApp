package local.project.Inzynierka.servicelayer.social.facebook.fbapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueriedPageInfo {

    private Data[] data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String access_token;
        private String id;
    }

    public Data getData(){
        return data[0];
    }

}
