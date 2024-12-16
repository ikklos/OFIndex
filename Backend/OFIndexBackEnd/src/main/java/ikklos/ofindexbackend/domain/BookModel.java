package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Entity
@Table(name="book")
public class BookModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue
    @Column(name="bookid")
    private Integer bookId;

    @Column(name="name")
    @Setter
    private String name;

    @Column(name="author")
    @Setter
    private String author;

    @Column(name="description")
    @Setter
    private String description;

    @Column(name="cover")
    @Setter
    private String cover;

    @Column(name="tags")
    @Setter
    private String tags;

    @Column(name="isbn")
    @Setter
    private String isbn;

    @Column(name="class")
    @Setter
    private Integer bookClass;

}
