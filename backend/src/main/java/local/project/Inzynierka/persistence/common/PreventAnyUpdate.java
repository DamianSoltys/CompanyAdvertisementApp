package local.project.Inzynierka.persistence.common;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class PreventAnyUpdate {

    @PrePersist
    void onPersist(Object o) {
        throw new VoivodeshipUpdateException("Cannot add new voivodeships.");
    }
    @PreUpdate
    void onUpdate(Object o) {
        throw new VoivodeshipUpdateException("Cannot update any existing voivodeships.");
    }
    @PreRemove
    void onRemove(Object o) {
        throw new VoivodeshipUpdateException("Cannot remove any existing voivodeships.");
    }

}
