package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.ShelfModel;
import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.ShelfRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.response.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserRepository repository;
    private final ShelfRepository shelfRepository;

    public RegisterController(@Autowired UserRepository repository,@Autowired ShelfRepository shelfRepository){
        this.repository=repository;
        this.shelfRepository=shelfRepository;
    }

    @PostMapping
    public RegisterResponse tryRegister(@RequestBody RegisterRequest registerRequest){
        if(registerRequest.phoneNumber!=null&&repository.existsUserModelByPhonenum(registerRequest.phoneNumber)) {
            RegisterResponse response = new RegisterResponse();
            response.result = false;
            response.message = "Exist Phone Number";
            return response;
        }
        UserModel user=new UserModel();
        user.setPasswd(registerRequest.passwd);
        user.setUsername(registerRequest.username);
        if(registerRequest.phoneNumber!=null)
            user.setPhonenum(registerRequest.phoneNumber);

        repository.save(user);
        Integer id=user.getUserid();

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

        RegisterResponse response=new RegisterResponse();
        response.result=true;
        response.message="Register success";
        response.id=id.toString();
        return response;
    }

}
