package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Entity
@Table(name="shelf")
public class ShelfModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shelfid")
    private Integer shelfId;

    @Column(name="userid")
    @Setter
    private Integer userId;

    @Column(name="shelfidx")
    @Setter
    private Integer index;

    @Column(name="name")
    @Setter
    private String name;


}
