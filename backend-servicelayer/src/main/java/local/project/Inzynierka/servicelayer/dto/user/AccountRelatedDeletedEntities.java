package local.project.Inzynierka.servicelayer.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AccountRelatedDeletedEntities {

    private Long userId;
    private Long verificationTokenId;
    private Long emailId;
    private PersonRelatedDeletedEntities personRelatedDeletedEntities;

    private List<Long> ratingIds;
    private List<Long> commentsIds;
    private List<Long> subscriptionIds;
}
