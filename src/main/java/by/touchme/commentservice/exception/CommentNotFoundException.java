package by.touchme.commentservice.exception;

/**
 * The class {@code CommentNotFoundException} and its subclasses
 * are a form of {@code RuntimeException} that indicates conditions
 * that a reasonable application might want to catch.
 */
public class CommentNotFoundException extends RuntimeException {

    /**
     * Unique identifier of the comment.
     */
    private final Long commentId;

    /**
     * Constructs a new runtime exception with the comment identifier.
     *
     * @param   commentId   the comment identifier. The comment identifier is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public CommentNotFoundException(Long commentId) {
        this.commentId = commentId;
    }

    @Override
    public String getMessage() {
        return "Comment with id = " + commentId + " not found";
    }
}
