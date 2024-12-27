package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="comment")
public class CommentModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="commentid")
    private Integer commentId;

    @Column(name="postid")
    @Setter
    private Integer postId;

    @Column(name="userid")
    @Setter
    private Integer userId;

    @Column(name="text",columnDefinition="TEXT")
    @Setter
    private String text;

    @Column(name="parent")
    @Setter
    private Integer parentComment;

    @Column(name="timestamp")
    @Setter
    private LocalDateTime timeStamp;

}
