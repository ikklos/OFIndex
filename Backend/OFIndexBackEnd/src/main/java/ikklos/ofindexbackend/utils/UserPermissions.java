package ikklos.ofindexbackend.utils;

import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.UserRepository;

public class UserPermissions {

    public static boolean noPermission(UserRepository userRepository,
                                       Integer userId,
                                       int level){
        var userO=userRepository.findById(userId);
        return userO.filter(userModel -> userModel.getLevel() < level).isEmpty();
    }

    public static boolean isPermissionEnough(UserModel userModel,int level){
        return userModel.getLevel()>=level;
    }

}
