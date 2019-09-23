package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.PreventAnyUpdate;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@EntityListeners(PreventAnyUpdate.class)
@Table(name = "voivodeships")
public class Voivoideship implements IEntity<Short> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voivodeship_id")
    private Short id = 0;

    @Column(nullable = false, unique = true, length = 25)
    private String name;

    public Voivoideship(String name) {
        this.name = name;
    }

    public Voivoideship() {
    }
}
