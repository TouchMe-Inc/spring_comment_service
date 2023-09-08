package by.touchme.commentservice.handler;

import by.touchme.commentservice.exception.CommentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller that catches all thrown exceptions.
 */
@RestControllerAdvice
public class ErrorHandler {

    /**
     * The method expects a NotFound error and generates a response for the request.
     *
     * @param ex NewsNotFoundException or CommentNotFoundException
     * @return Object with error message
     */
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> notFoundException(Exception ex, WebRequest request) {
        return prepareErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    /**
     * Any unhandled exceptions.
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        System.out.println(ex.getClass());
        return prepareErrorMessage(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> prepareErrorMessage(Exception ex, WebRequest request, HttpStatus status) {
        String requestUri = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), requestUri);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), status);
    }
}
