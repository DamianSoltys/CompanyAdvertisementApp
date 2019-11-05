package local.project.Inzynierka.servicelayer.dto.promotionitem;

public enum PromotionItemType {
    PROMOTION("promotion"),
    INFORMATION("information"),
    PRODUCT("product");

    private String type;
    PromotionItemType(String type) {
        this.type = type;
    }
}
