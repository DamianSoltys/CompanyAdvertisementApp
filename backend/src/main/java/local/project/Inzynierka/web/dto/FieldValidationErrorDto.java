package local.project.Inzynierka.web.dto;

import java.util.ArrayList;
import java.util.List;

public class FieldValidationErrorDto {

    private List<FieldErrorDto> fieldErrors = new ArrayList<>();

    public FieldValidationErrorDto(){}

    public void addFieldError(String path, String message) {
        FieldErrorDto error = new FieldErrorDto(path, message);
        fieldErrors.add(error);
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }
}
