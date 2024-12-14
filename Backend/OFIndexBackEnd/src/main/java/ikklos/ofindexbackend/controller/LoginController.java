package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.response.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
        public int token;
    }

    private final UserRepository repository;

    public  LoginController(@Autowired UserRepository repository){
        this.repository=repository;
    }

    @GetMapping
    public LoginResponse tryLogin(@RequestBody LoginRequest loginRequest){
        if(loginRequest.userid!=null){
            var data=repository.findById(loginRequest.userid);
            if(data.isPresent()) {
                if (data.get().checkPassword(loginRequest.passwd)) {
                    LoginResponse response = new LoginResponse();
                    response.result = true;
                    response.message = "Login success!";
                    response.token=-1;
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

    @GetMapping("/phone")
    public LoginResponse TryPhoneLogin(@RequestBody PhoneLoginRequest loginRequest){
        if(loginRequest.phoneNumber!=null&&repository.existsUserModelByPhonenum(loginRequest.phoneNumber)){
            var data=repository.findUserModelByPhonenum(loginRequest.phoneNumber);
                if(data.checkPassword(loginRequest.passwd)){
                    LoginResponse response=new LoginResponse();
                    response.result=true;
                    response.message="Login success!";
                    response.token=-1;
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
}

