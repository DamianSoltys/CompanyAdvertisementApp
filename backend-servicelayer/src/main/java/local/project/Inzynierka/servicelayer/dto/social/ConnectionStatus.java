package local.project.Inzynierka.servicelayer.dto.social;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectionStatus {
    private Status status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;
}
