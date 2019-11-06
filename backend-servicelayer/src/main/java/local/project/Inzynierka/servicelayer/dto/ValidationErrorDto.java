package local.project.Inzynierka.servicelayer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationErrorDto {

    private List<FieldErrorDto> fieldErrors = new ArrayList<>();
    private List<TypeError> typeErrors = new ArrayList<>();

    public ValidationErrorDto() {}

    public void addFieldError(String path, String message) {
        FieldErrorDto error = new FieldErrorDto(path, message);
        fieldErrors.add(error);
    }

    public void addTypeError(String message) {
        typeErrors.add(new TypeError(message));
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }

    public List<TypeError> getTypeErrors() {return typeErrors;}
}
