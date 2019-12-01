package local.project.Inzynierka.servicelayer.social.facebook.fbapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    private Integer code;
    private String message;
    @JsonProperty(value = "subcode")
    private Integer subCode;
}
