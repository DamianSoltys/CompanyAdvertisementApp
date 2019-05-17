package local.project.Inzynierka.persistence.entity;

import java.io.Serializable;

interface IEntity<T> extends Serializable {

    T getId();
    void setId(T id);
}
