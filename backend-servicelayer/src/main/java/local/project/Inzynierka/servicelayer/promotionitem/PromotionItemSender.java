package local.project.Inzynierka.servicelayer.promotionitem;

public interface PromotionItemSender {
    void send(Sendable sendable);
    void schedule(Sendable sendable);
    void cancel();
}
