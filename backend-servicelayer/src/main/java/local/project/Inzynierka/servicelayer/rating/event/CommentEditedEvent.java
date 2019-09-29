package local.project.Inzynierka.servicelayer.rating.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CommentEditedEvent {
    private Long commentId;
    private String newCommentContent;
}
