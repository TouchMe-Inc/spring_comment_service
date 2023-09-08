package by.touchme.commentservice.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorMessage {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String path;

    public ErrorMessage() {
        timestamp = LocalDateTime.now();
    }

    public ErrorMessage(String message, String path) {
        this();
        this.message = message;
        this.path = path;
    }
}