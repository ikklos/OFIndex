package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="shelfbook")
public class ShelfBookModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column(name="shelfid")
    @Setter
    private Integer shelfId;

    @Column(name="bookid")
    @Setter
    private Integer bookId;

    @Column(name="timestamp")
    @Setter
    private LocalDateTime timeStamp;

}
