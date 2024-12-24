package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/user",produces = "application/json")
public class UserController {

    public static class UserInfoResponse extends UniversalResponse{
        public Integer userId;
        public String userName;
        public String avatar;
        public Integer level;
    }

    public static class ModifyUserInfoRequest{
        public String name;
        public String avatar;
        public String password;
        public String phoneNumber;
    }

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @GetMapping
    public UserInfoResponse getUserOwnInfo(@RequestHeader("Authorization") String token) throws UniversalBadReqException {
        Integer userId= JwtUtils.getUserIdJWT(token);

        return getUserInfoResponse(userId);
    }

    @GetMapping("/{userid}")
    public UserInfoResponse getUserInfo(@RequestHeader("Authorization") String token,
                                        @PathVariable("userid") Integer userid) throws UniversalBadReqException {
        JwtUtils.getUserIdJWT(token);

        return getUserInfoResponse(userid);
    }

    @PostMapping("/modify")
    public UserInfoResponse modifyUserInfo(@RequestHeader("Authorization") String token,
                                           @RequestBody ModifyUserInfoRequest request) throws UniversalBadReqException {
        Integer userId= JwtUtils.getUserIdJWT(token);
        var userO=userRepository.findById(userId);
        if(userO.isEmpty()){
            throw new UniversalBadReqException("No such user");
        }

        UserModel userModel=userO.get();

        if(request.avatar!=null)userModel.setAvatar(request.avatar);
        if(request.name!=null)userModel.setUsername(request.name);
        if(request.password!=null)userModel.setPasswd(request.password);
        if(request.phoneNumber!=null)userModel.setPhonenum(request.phoneNumber);
        userRepository.save(userModel);

        UserInfoResponse response=new UserInfoResponse();
        response.avatar=userModel.getAvatar();
        response.userId=userModel.getUserid();
        response.userName=userModel.getUsername();
        return response;
    }

    private UserInfoResponse getUserInfoResponse(@PathVariable Integer userid) throws UniversalBadReqException {
        var userO=userRepository.findById(userid);
        if(userO.isEmpty()){
            throw new UniversalBadReqException("No such user");
        }

        UserModel userModel=userO.get();

        UserInfoResponse response=new UserInfoResponse();
        response.avatar=userModel.getAvatar();
        response.userId=userModel.getUserid();
        response.userName=userModel.getUsername();
        response.level=userModel.getLevel();
        return response;
    }

}
