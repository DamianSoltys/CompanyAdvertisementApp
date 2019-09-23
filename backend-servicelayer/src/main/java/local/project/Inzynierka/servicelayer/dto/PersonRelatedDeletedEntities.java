package local.project.Inzynierka.servicelayer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PersonRelatedDeletedEntities {

    private Long personId;
    private Long addressId;
    private List<Long> companiesIds;
}
