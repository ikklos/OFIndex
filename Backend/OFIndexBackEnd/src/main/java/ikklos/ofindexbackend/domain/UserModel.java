package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="user")
@Getter
public class UserModel implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue
    @Column(name="userid")
    @Getter
    private Integer userid;

    @Column(name="username")
    @Getter
    @Setter
    private String username;

    @Column(name="passwd")
    private String passwd;

    @Column(name="phonenum")
    @Getter
    @Setter
    private String phonenum;

    public boolean checkPassword(String password){
        return passwd.equals(password);
    }

}
