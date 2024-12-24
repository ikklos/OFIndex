package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="pack")
public class PackModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue
    @Column(name="packid")
    private Integer packId;

    @Column(name="bookid")
    @Setter
    private Integer bookId;

    @Column(name="authorid")
    @Setter
    private Integer authorId;

    @Column(name="ownerid")
    @Setter
    private Integer ownerId;

    @Column(name="name")
    @Setter
    private String name;

    @Column(name="description",columnDefinition="TEXT")
    @Setter
    private String description;

    @Column(name="likes")
    @Setter
    private Integer likeCount;

    @Column(name="shared")
    @Setter
    private Integer shared;

    @Column(name="content",columnDefinition="TEXT")
    @Setter
    private String content;

    @Column(name="updatetime")
    @Setter
    private LocalDateTime updateTime;

}
