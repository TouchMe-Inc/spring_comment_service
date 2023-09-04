package by.touchme.commentservice.handler;

import by.touchme.commentservice.exception.CommentNotFoundException;
import by.touchme.commentservice.record.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller that catches all thrown exceptions.
 */
@RestControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * The method expects a NotFound error and generates a response for the request.
     *
     * @param e NewsNotFoundException or CommentNotFoundException
     * @return Object with error message
     */
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundException(Exception e, WebRequest request) {
        return new ErrorMessage(new Date(), e.getMessage(), request.getDescription(false));
    }

    /**
     * The method expects a MethodArgumentNotValidException error and generates a response for the request.
     *
     * @param e MethodArgumentNotValidException
     * @return Object with error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage argumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return new ErrorMessage(new Date(), errors.toString(), request.getDescription(false));
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage anyException(Exception e, WebRequest request) {
        logger.error("Unexpected error", e);
        return new ErrorMessage(new Date(), e.getMessage(), request.getDescription(false));
    }
}
