package by.touchme.commentservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * The comment object contains main details about Comment.
 */
@Data
@Table(name = "comments")
@Entity
public class Comment {

    /**
     * Unique identifier of the comment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Author of the comment.
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * Message of the comment.
     */
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;


    /**
     * Time of creation of the news.
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;

    /**
     * The identifier of the news the comment is associated with.
     */
    @Column(name = "news_id", nullable = false)
    private Long newsId;
}
