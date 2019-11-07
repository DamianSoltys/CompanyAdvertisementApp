package local.project.Inzynierka.persistence.entity;

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

@Data
@Entity
@Table(name = "promotion_item_destinations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionItemDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_item_destination_id")
    private Long id;

    @Column(name = "destination", nullable = false)
    private String destination;

    @ManyToOne
    @JoinColumn(name = "promotion_item_id", nullable = false, foreignKey = @ForeignKey(name = "promotion_item_destination_FK"))
    private PromotionItem promotionItem;
}
