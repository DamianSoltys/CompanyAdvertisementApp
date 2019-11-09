package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "promotion_item_photos")
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PromotionItemPhoto extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_item_photo_id")
    private Long id;

    @Column(name = "photo_uuid", unique = true, nullable = false)
    private String photoUUID;

    @Column(name = "was_added", nullable = false)
    private Boolean wasAdded;

    @ManyToOne
    @JoinColumn(name = "promotion_item_id", foreignKey = @ForeignKey(name = "promotion_item_photo_FK"))
    private PromotionItem promotionItem;
}
