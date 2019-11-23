package local.project.Inzynierka.servicelayer.social.facebook.fbapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenInspection {
    private String app_id;
    private String type;
    private String application;
    private Long data_access_expires_at;
    private Error error;
    private Long expires_at;
    private Boolean is_valid;
    private Long issued_at;
    private List<String> scopes;
    private List<Map<String,String>> granular_scopes;
    private Long user_id;

}
