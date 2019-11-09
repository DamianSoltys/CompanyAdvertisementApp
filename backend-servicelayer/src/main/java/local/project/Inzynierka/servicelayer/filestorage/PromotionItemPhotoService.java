package local.project.Inzynierka.servicelayer.filestorage;

import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemPhotoAddedEvent;
import local.project.Inzynierka.shared.utils.EntityName;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class PromotionItemPhotoService {

    private final PhotoFileService photoFileService;
    private final ApplicationEventPublisher publisher;

    public PromotionItemPhotoService(PhotoFileService photoFileService, ApplicationEventPublisher publisher) {
        this.photoFileService = photoFileService;
        this.publisher = publisher;
    }

    public void savePhotos(String promotionItemUUID, Map<String, MultipartFile> filesMap) {
        var iterator = filesMap.entrySet().iterator();
        while (iterator.hasNext()) {
            var nextMapping = iterator.next();
            String photoUUID = nextMapping.getKey();
            MultipartFile file = nextMapping.getValue();
            photoFileService.validateLogoFile(file);
            photoFileService.saveEntityLogo(promotionItemUUID, photoUUID, file, EntityName.PROMOTION_ITEM);
            publisher.publishEvent(PromotionItemPhotoAddedEvent.builder()
                                           .photoUUID(photoUUID)
                                           .promotionItemUUID(promotionItemUUID)
                                           .build());
        }
    }

}
