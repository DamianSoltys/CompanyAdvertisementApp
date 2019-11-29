package local.project.Inzynierka.servicelayer.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultDto {
    private Page<Object> result;
    private Integer companiesNumber;
    private Integer branchesNumber;
}
