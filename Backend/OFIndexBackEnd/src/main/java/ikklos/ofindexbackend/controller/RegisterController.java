package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.ShelfModel;
import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.ShelfRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/register",produces = "application/json")
public class RegisterController {

    public static class RegisterRequest{
        public String phoneNumber;
        public String username;
        public String passwd;
    }

    public static class RegisterResponse extends UniversalResponse {
        public String id;
    }

    private final UserRepository userRepository;
    private final ShelfRepository shelfRepository;

    @Autowired
    public RegisterController(ShelfRepository shelfRepository,
                              UserRepository userRepository){
        this.userRepository =userRepository;
        this.shelfRepository=shelfRepository;
    }

    @PostMapping
    public RegisterResponse tryRegister(@RequestBody RegisterRequest registerRequest) throws UniversalBadReqException {
        if(registerRequest.phoneNumber!=null&& userRepository.existsUserModelByPhonenum(registerRequest.phoneNumber)) {
            throw new UniversalBadReqException("Exist Phone Number");
        }
        UserModel user=new UserModel();
        user.setPasswd(registerRequest.passwd);
        user.setUsername(registerRequest.username);
        user.setLevel(0);
        if(registerRequest.phoneNumber!=null)
            user.setPhonenum(registerRequest.phoneNumber);

        userRepository.save(user);
        Integer id=user.getUserid();
        try{

            ShelfModel history=new ShelfModel();
            history.setUserId(id);
            history.setIndex(0);
            history.setName("history");

            ShelfModel defaultShelf=new ShelfModel();
            defaultShelf.setUserId(id);
            defaultShelf.setIndex(1);
            defaultShelf.setName("Default");

            shelfRepository.save(history);
            shelfRepository.save(defaultShelf);
        }catch (DataIntegrityViolationException ex){
            userRepository.delete(user);
            throw ex;
        }

        RegisterResponse response=new RegisterResponse();
        response.message="Register success";
        response.id=id.toString();
        return response;
    }

}
