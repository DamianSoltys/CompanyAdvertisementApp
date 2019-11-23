package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.promotionitem.event.PromotionItemAddedEvent;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringBase64Encoded implements ConstraintValidator<HtmlContentBase64Encoded, PromotionItemAddedEvent> {

   public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
      return StringUtils.isEmpty(value.getHTMLContent())  ||
              (!StringUtils.isEmpty(value.getHTMLContent()) && Base64.isBase64(value.getHTMLContent()));
   }
}
