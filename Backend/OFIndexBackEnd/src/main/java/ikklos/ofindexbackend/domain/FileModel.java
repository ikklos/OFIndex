package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="file")
public class FileModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fileid")
    private Integer fileId;

    @Column(name="userid")
    private Integer userId;

    @Column(name="name")
    private String name;

    @Column(name="size")
    private Long size;

    @Column(name="shared")
    private Integer shared;
}
