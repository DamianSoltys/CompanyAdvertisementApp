package local.project.Inzynierka.servicelayer.company.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchLogoAddedEvent {
    private String branchUUID;
}
