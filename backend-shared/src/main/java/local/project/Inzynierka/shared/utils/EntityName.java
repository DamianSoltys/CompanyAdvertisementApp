package local.project.Inzynierka.shared.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum EntityName {
    BRANCH("branch"),
    COMPANY("company"),
    PROMOTION_ITEM("pi");

    private String entityName;

    EntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    @JsonCreator
    public static EntityName fromEntityName(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(EntityName.values())
                .filter(v -> v.getEntityName().equals(value.toLowerCase()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @JsonValue
    @Override
    public String toString() {
        return super.toString();
    }
}
