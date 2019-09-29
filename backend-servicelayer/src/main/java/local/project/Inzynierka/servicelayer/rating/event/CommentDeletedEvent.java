package local.project.Inzynierka.servicelayer.rating.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDeletedEvent {
    Long commentId;
}
