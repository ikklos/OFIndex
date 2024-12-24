package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="post")
public class PostModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue
    @Column(name="postid")
    private Integer postId;

    @Column(name="userid")
    @Setter
    private Integer userId;

    @Column(name="bookid")
    @Setter
    private Integer bookId;

    @Column(name="packid")
    @Setter
    private Integer packId;

    @Column(name="tags")
    @Setter
    private String tags;

    @Column(name="title")
    @Setter
    private String title;

    @Column(name="text",columnDefinition="TEXT")
    @Setter
    private String text;

    @Column(name="imageurls")
    @Setter
    private String imageurls;

    @Column(name="likes")
    @Setter
    private Integer likes;

    @Column(name="timestamp")
    @Setter
    private LocalDateTime timeStamp;

}
