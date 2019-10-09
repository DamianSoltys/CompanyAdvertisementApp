package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "categories")
@NoArgsConstructor
@Indexed
public class Category extends FullTimestampingAudit implements IEntity<Short> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id = 0;

    @Field
    @Column(nullable = false, length = 30)
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
