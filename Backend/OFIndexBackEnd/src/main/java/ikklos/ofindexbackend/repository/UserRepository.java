package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel,Integer> {

    UserModel findUserModelByPhonenum(String phonenum);

    boolean existsUserModelByPhonenum(String phonenum);



}
