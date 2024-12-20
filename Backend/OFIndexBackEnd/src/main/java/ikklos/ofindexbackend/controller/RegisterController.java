package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.response.UniversalResponse;
import ikklos.ofindexbackend.utils.JwtUtils;
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

    public RegisterController(@Autowired UserRepository repository){
        this.repository=repository;
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

        var keyFactory=new JwtUtils.JwtkeyFactory();
        user.setJwtKey(keyFactory.generateJwtKey());
        //TODO Jwt key interval refresh

        repository.save(user);

        //TODO create shelf

        Integer id=user.getUserid();
        RegisterResponse response=new RegisterResponse();
        response.result=true;
        response.message="Register success";
        response.id=id.toString();
        return response;
    }

}
