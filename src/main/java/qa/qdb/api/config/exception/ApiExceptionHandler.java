package qa.qdb.api.config.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.UncheckedIOException;
import java.security.AccessControlException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {NoSuchElementException.class, EmptyResultDataAccessException.class})
    protected ResponseEntity<Object> handleNotFoundElement(
            RuntimeException ex, WebRequest request) {
        ApiError bodyOfResponse = new ApiError("Object not found");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = UncheckedIOException.class)
    protected ResponseEntity<Object> handleIOException(
            RuntimeException ex, WebRequest request) {
        ApiError bodyOfResponse = new ApiError("File not saved");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = AccessControlException.class)
    protected ResponseEntity<Object> handleAccessControlException(
            RuntimeException ex, WebRequest request) {
        ApiError bodyOfResponse = new ApiError(ex.getMessage());
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}
