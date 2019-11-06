package local.project.Inzynierka.web.advices;

import local.project.Inzynierka.servicelayer.dto.ValidationErrorDto;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
@NoArgsConstructor
public class ControllerErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDto processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ObjectError> typeErrors = result.getGlobalErrors();

        return processErrors(fieldErrors, typeErrors);
    }

    private ValidationErrorDto processErrors(List<FieldError> fieldErrors, List<ObjectError> objectErrors) {
        ValidationErrorDto dto = new ValidationErrorDto();

        for (FieldError fieldError: fieldErrors) {
            dto.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        for(var objectError: objectErrors) {
            dto.addTypeError(objectError.getDefaultMessage());
        }

        return dto;
    }
}
