package local.project.Inzynierka.domain.model;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Comment {
    private long id;
    private String comment;
    private User user;
    private Branch branch;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
}
