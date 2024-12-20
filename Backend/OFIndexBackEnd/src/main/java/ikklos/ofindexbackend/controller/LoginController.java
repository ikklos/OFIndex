package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.response.UniversalResponse;
import ikklos.ofindexbackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/login",produces = "application/json")
public class LoginController {

    public static class LoginRequest{
        public Integer userid;
        public String passwd;
    }

    public static class PhoneLoginRequest{
        public String phoneNumber;
        public String passwd;
    }

    public static class LoginResponse extends UniversalResponse {
        public String token;
    }

    private final UserRepository repository;

    public  LoginController(@Autowired UserRepository repository){
        this.repository=repository;
    }

    @PostMapping
    public LoginResponse tryLogin(@RequestBody LoginRequest loginRequest){
        if(loginRequest.userid!=null){
            var data=repository.findById(loginRequest.userid);
            if(data.isPresent()) {
                if (data.get().checkPassword(loginRequest.passwd)) {
                    LoginResponse response = new LoginResponse();
                    response.result = true;
                    response.message = "Login success!";
                    response.token=generateLoginJWT(data.get());
                    return response;
                }else{
                    LoginResponse response = new LoginResponse();
                    response.result = false;
                    response.message = "Wrong password!";
                    return response;
                }
            }
        }
        LoginResponse response=new LoginResponse();
        response.result=false;
        response.message="No such user!";
        return response;
    }

    @PostMapping("/phone")
    public LoginResponse TryPhoneLogin(@RequestBody PhoneLoginRequest loginRequest){
        if(loginRequest.phoneNumber!=null&&repository.existsUserModelByPhonenum(loginRequest.phoneNumber)){
            var data=repository.findUserModelByPhonenum(loginRequest.phoneNumber);
                if(data.checkPassword(loginRequest.passwd)){
                    LoginResponse response=new LoginResponse();
                    response.result=true;
                    response.message="Login success!";
                    response.token=generateLoginJWT(data);
                    return response;
                }else{
                    LoginResponse response = new LoginResponse();
                    response.result = false;
                    response.message = "Wrong password!";
                    return response;
                }
        }
        LoginResponse response=new LoginResponse();
        response.result=false;
        response.message="No such user!";
        return response;
    }

    private String generateLoginJWT(UserModel userModel){
        return JwtUtils.createUserIdJWT(userModel.getUserid());
    }

}

