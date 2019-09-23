package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.CreationTimestampingAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion_item_types")
public class PromotionItemType extends CreationTimestampingAudit implements IEntity<Short> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_item_type_id")
    private Short id = 0;

    @Column(name = "type", length = 30, nullable = false)
    private String type;
}
