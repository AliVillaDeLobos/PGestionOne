package com.gestion.system.exceptions;

import com.gestion.system.exceptions.BusinessRulesExceptions.DuplicateAssignmentException;
import com.gestion.system.exceptions.BusinessRulesExceptions.DuplicateResourceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    private ResponseEntity<ErrorDetails> json(
            HttpStatus status, String message, Map<String, String> fields){
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorDetails.of(status, message, fields));
    }

    private ResponseEntity<ErrorDetails> json(HttpStatus status, String message){
        return json(status, message, null);
    }

    //     400 BAD REQUEST
    @ExceptionHandler(HttpMessageNotReadableException.class) // JSON mal formado
    public ResponseEntity<ErrorDetails> handlerMessageNotReadable(
            HttpMessageNotReadableException ex
    ){
        return json(HttpStatus.BAD_REQUEST, "Invalid JSON format.");
    }

    @ExceptionHandler(InvalidDeletionUserException.class)
    public ResponseEntity<ErrorDetails> handleInvalidDeletionUser(InvalidDeletionUserException ex) {
        return json(HttpStatus.BAD_REQUEST, "User haven't enough permissions.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Invalid type for '" + ex.getName() + "'. Expected: " +
                (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown");
        return json(HttpStatus.BAD_REQUEST, msg);
    }

    // 401 No autorizado
    @ExceptionHandler(PasswordInvalidateException.class)
    public ResponseEntity<ErrorDetails> handlePasswordInvalid(PasswordInvalidateException ex) {
        return json(HttpStatus.UNAUTHORIZED, "Unauthorize. Password Invalid.");
    }

    // 403 Prohibido/Forbidden
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ErrorDetails> handleUnauthorizeOperation(UnauthorizedOperationException ex) {
        return json(HttpStatus.FORBIDDEN, "Don't have enought permissions.");
    }

    //    404 Recurso no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handlerNotFound(
            ResourceNotFoundException ex
    ){
        return json(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //    409  Violaciones de integridad (FK, unique, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrity(DataIntegrityViolationException ex) {
        return json(HttpStatus.CONFLICT, "Data Integrity violation.");
    }

    @ExceptionHandler(DuplicateAssignmentException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateAssignment(DuplicateAssignmentException ex) {
        return json(HttpStatus.CONFLICT, "Duplicate Assignment.");
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateResource(DuplicateResourceException ex) {
        return json(HttpStatus.CONFLICT, "Resource Duplicate.");
    }

    @ExceptionHandler(InvalidResourceStateException.class)
    public ResponseEntity<ErrorDetails> handleInvalidResource(InvalidResourceStateException ex) {
        return json(HttpStatus.CONFLICT, "Resource Invalid State.");
    }

    //    500 Error generico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handlerGeneric(Exception ex){
        return json(HttpStatus.INTERNAL_SERVER_ERROR, "Server Internal Error ");
    }

    //    Errores de @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        return json(HttpStatus.BAD_REQUEST, "Fields Validation Error", fieldErrors);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handlerIllegalArgument(
            IllegalArgumentException ex
    ){
        return json(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    //    Validaciones de parámetros
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handlerConstraintViolation(
            ConstraintViolationException ex
    ){
        Map<String, String> violation = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), v -> v.getMessage()));
        return json(HttpStatus.BAD_REQUEST, "Invalid Parameters.", violation);
    }

    public record ErrorDetails(
            int status,
            String error,
            String message,
            String timestamp,
            Map<String, String> fields
    ) {
        public static ErrorDetails of(HttpStatus status, String message, Map<String, String> fields) {
            return new ErrorDetails(
                    status.value(),
                    status.getReasonPhrase(),
                    message,
                    Instant.now().toString(),
                    fields
            );
        }
    }
}

