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
@Table(name="forummsg")
public class ForumMessageModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue
    @Column(name="messageid")
    private Integer messageId;

    @Column(name="receiver")
    @Setter
    private Integer receiverId;

    @Column(name="sender")
    @Setter
    private Integer senderId;

    @Column(name="type")
    @Setter
    private Integer type;

    @Column(name="text")
    @Setter
    private String text;

    @Column(name="isRead")
    @Setter
    private Integer isRead;

    @Column(name="timestamp")
    @Setter
    private LocalDateTime timeStamp;

}
