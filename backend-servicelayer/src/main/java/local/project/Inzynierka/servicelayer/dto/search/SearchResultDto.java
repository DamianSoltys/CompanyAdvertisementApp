package local.project.Inzynierka.servicelayer.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultDto {
    private List<Object> result;
    private Integer companiesNumber;
    private Integer branchesNumber;
}
