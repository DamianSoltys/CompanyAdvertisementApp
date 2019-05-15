package local.project.Inzynierka.domain.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Category {
    private long id;
    private String name;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
}
