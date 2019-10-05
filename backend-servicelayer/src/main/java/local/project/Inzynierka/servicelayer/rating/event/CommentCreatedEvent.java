package local.project.Inzynierka.servicelayer.rating.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class CommentCreatedEvent {
    private String commentContent;
    private String userName;
    private Long branchId;
}
