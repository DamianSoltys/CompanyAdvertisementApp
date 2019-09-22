package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "promotion_items")
public class PromotionItem extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_item_id")
    private Long id = 0L;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "promoting_company_id", nullable = false, foreignKey = @ForeignKey(name = "promoting_company_FK"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "promotion_type_id", nullable = false, foreignKey = @ForeignKey(name = "promotion_type_FK"))
    private PromotionItemType promotionItemType;

    @Column(name = "was_sent")
    private boolean wasSent;

    @Column(nullable = false, name = "valid_from", columnDefinition = "TIMESTAMP")
    private Timestamp validFrom;

    @Column(nullable = false, name = "valid_to", columnDefinition = "TIMESTAMP")
    private Timestamp validTo;

}
