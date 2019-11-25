package local.project.Inzynierka.servicelayer.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendationDto {
    private Long companyId;
    private String name;
    private Long branchId;
    private Float averageRating;
    private Double geoX;
    private Double geoY;
    private String getLogoURL;
    private Integer currentUserRating;
    private String category;
}
