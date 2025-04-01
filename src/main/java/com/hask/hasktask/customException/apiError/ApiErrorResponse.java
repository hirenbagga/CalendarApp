package com.hask.hasktask.customException.apiError;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.ConstraintViolation;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
public class ApiErrorResponse {

    private HttpStatus status;

    // Renamed to timestamp to indicate when the error occurred
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")

    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiError> subErrors;

    private ApiErrorResponse() {
        timestamp = LocalDateTime.now();
    }

    // Constructor for setting HTTP status only
    public ApiErrorResponse(HttpStatus status) {
        this();
        this.status = status;
    }

    // Constructor for setting HTTP status and exception details
    public ApiErrorResponse(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error:-> ";
        this.debugMessage = ex.getLocalizedMessage();
    }

    // Constructor for setting HTTP status, message, and exception details
    public ApiErrorResponse(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    // Utility method for adding a sub-error to the list
    private void addSubError(ApiError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    // Utility method for adding validation error details
    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    // Utility method for adding validation error with just the object and message
    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }

    // Utility method for adding a FieldError to the response
    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    // Adds a list of FieldErrors to the response
    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    // Utility method for adding a general ObjectError to the response
    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    // Adds a list of ObjectErrors to the response
    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    // Utility method for adding error from ConstraintViolation (e.g., when @Validated validation fails)
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    // Adds a list of ConstraintViolations to the response
    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }
}

