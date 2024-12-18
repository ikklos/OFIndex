package ikklos.ofindexbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="account")
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
    @Setter
    private String passwd;

    @Column(name="phonenum")
    @Getter
    @Setter
    private String phonenum;

    @Column(name="avatar")
    @Getter
    @Setter
    private String avatar;

    @Column(name="jwtkey")
    @Getter
    @Setter
    private String jwtKey;


    public boolean checkPassword(String password){
        return passwd.equals(password);
    }

}
