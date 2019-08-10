package local.project.Inzynierka.persistence.entity;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "promotion_items")
public class PromotionItem implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_item_id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "promoting_company_id", nullable = false, foreignKey = @ForeignKey(name = "promoting_company_FK"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "promotion_type_id", nullable = false,  foreignKey = @ForeignKey(name = "promotion_type_FK"))
    private PromotionItemType promotionItemType;

    @Column(name = "valid")
    private boolean valid;

    @Column(nullable = false, name = "valid_from", columnDefinition = "TIMESTAMP")
    private Timestamp validFrom;

    @Column(nullable = false, name = "valid_to", columnDefinition = "TIMESTAMP")
    private Timestamp validTo;

    @Column(nullable = false, name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false, name = "modified_at", columnDefinition = "TIMESTAMP")
    private Timestamp modifiedAt;
}
