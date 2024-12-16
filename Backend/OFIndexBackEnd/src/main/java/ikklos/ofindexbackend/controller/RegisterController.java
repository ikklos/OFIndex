package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.response.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/register",produces = "application/json")
public class RegisterController {

    public static class RegisterRequest{
        public String phoneNumber;
        public String username;
        public String passwd;
    }

    public static class RegisterResponse extends UniversalResponse {
        public Integer id;
    }

    private final UserRepository repository;

    public RegisterController(@Autowired UserRepository repository){
        this.repository=repository;
    }

    @GetMapping
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
        RegisterResponse response=new RegisterResponse();
        response.result=true;
        response.message="Register success";
        response.id=id;
        return response;
    }

}
