package by.touchme.commentservice.record;

import java.util.Date;

public record ErrorMessage(Date timestamp, String message, String details) {
}
