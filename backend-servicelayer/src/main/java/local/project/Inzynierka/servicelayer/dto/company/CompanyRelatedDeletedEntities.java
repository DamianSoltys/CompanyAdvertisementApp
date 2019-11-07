package local.project.Inzynierka.servicelayer.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CompanyRelatedDeletedEntities {

    private Long companyId;
    private List<Long> branchesIds;
    private List<Long> addressesIds;
    private List<Long> commentsIds;
    private List<Long> ratingsIds;
    private List<List<Long>> favouriteBranchesIds;
    private List<Long> newsletterSubscriptions;
    private List<Long> promotionItemsIds;
    private List<String> socialProfileUrls;
}
