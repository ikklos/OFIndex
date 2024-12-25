package ikklos.ofindexbackend.domain;

import ikklos.ofindexbackend.repository.ForumMessageRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name="text",columnDefinition="TEXT")
    @Setter
    private String text;

    @Column(name="is_read")
    @Setter
    private Integer isRead;

    @Column(name="timestamp")
    @Setter
    private LocalDateTime timeStamp;

    public static ForumMessageModel addForumMessage(ForumMessageRepository repository,
                                Integer senderId,Integer receiverId,Integer type,
                                String text,boolean read){
        ForumMessageModel model=new ForumMessageModel();
        model.setSenderId(senderId);
        model.setReceiverId(receiverId);
        model.setType(type);
        model.setText(text);
        model.setIsRead(0);
        model.setTimeStamp(LocalDateTime.now());
        model.setIsRead(read?1:0);
        repository.save(model);
        return model;
    }

}
