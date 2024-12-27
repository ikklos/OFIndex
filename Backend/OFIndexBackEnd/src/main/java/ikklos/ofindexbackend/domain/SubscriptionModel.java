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
@Table(name="subscription")
public class SubscriptionModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="follower")
    @Setter
    private Integer followerId;

    @Column(name="following")
    @Setter
    private Integer followingId;

    @Column(name="notification")
    @Setter
    private Integer notification;

    @Column(name="timestamp")
    @Setter
    private LocalDateTime timeStamp;

}
