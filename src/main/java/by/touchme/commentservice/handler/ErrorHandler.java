package by.touchme.commentservice.handler;

import by.touchme.commentservice.exception.CommentNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller that catches all thrown exceptions.
 */
@RestControllerAdvice
public class ErrorHandler {

    /**
     * The method expects a NotFound error and generates a response for the request.
     *
     * @param ex CommentNotFoundException
     * @return Object with error message
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> notFoundException(Exception ex, WebRequest request) {
        return prepareErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    /**
     * Validation via @Valid annotation failed.
     *
     * @param ex MethodArgumentNotValidException
     * @return Object with error message
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleNotValidException(Exception ex, WebRequest request) {
        return prepareErrorMessage(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Unauthorized access.
     *
     * @param ex AccessDeniedException
     * @return Object with error message
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenyException(Exception ex, WebRequest request) {
        return prepareErrorMessage(ex, request, HttpStatus.FORBIDDEN);
    }

    /**
     * Expired Jwt token.
     *
     * @param ex ExpiredJwtException
     * @return Object with error message
     */
    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(Exception ex, WebRequest request) {
        return prepareErrorMessage(ex, request, HttpStatus.FORBIDDEN);
    }

    /**
     * Malformed Jwt token.
     *
     * @param ex MalformedJwtException
     * @return Object with error message
     */
    @ExceptionHandler(value = {MalformedJwtException.class})
    public ResponseEntity<Object> handleMalformedJwtException(Exception ex, WebRequest request) {
        return prepareErrorMessage(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Any unhandled exceptions.
     *
     * @param ex Exception
     * @return Object with error message
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        return prepareErrorMessage(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> prepareErrorMessage(Exception ex, WebRequest request, HttpStatus status) {
        String requestUri = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), requestUri);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), status);
    }
}
