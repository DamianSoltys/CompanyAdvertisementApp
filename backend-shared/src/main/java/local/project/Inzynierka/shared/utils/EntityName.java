package local.project.Inzynierka.shared.utils;

public enum EntityName {
    BRANCH("branch"),
    COMPANY("company");

    private String entityName;

    EntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }
}
