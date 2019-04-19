package local.project.Inzynierka.persistence.model;

import java.io.Serializable;

interface IEntity extends Serializable {

    long getId();
    void setId(long id);
}
