package cat.itacademy.fruitapih2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FruitNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFound(FruitNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FruitAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleAlreadyExist(FruitAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneralError(Exception ex) {
        return buildResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDetails> buildResponse(String message, HttpStatus status) {
        ErrorDetails details = new ErrorDetails(LocalDateTime.now(), status.value(), message);
        return new ResponseEntity<>(details, status);
    }

    public record ErrorDetails(LocalDateTime timestamp, int status, String message) {}
}