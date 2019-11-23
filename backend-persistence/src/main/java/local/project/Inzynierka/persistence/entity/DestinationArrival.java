package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.CreationTimestampingAudit;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "destination_arrivals")
public class DestinationArrival extends CreationTimestampingAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_arrival_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "promotion_item_destination_id", foreignKey = @ForeignKey(name = "sending_status_FK"))
    private PromotionItemDestination promotionItemDestination;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "detail")
    private String detail;

    @Override
    public Date getCreatedDate() {
        return super.getCreatedDate();
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        super.setCreatedDate(createdDate);
    }
}
