package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

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

    @Column(name="name")
    @Setter
    private String name;

    @Column(name="description")
    @Setter
    private String description;

    @Column(name="like")
    @Setter
    private Integer likeCount;

}
