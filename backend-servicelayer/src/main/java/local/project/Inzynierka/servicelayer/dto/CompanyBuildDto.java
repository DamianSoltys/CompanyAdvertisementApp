package local.project.Inzynierka.servicelayer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CompanyBuildDto {
    private Long id;
    private String logoFilePath;
}
