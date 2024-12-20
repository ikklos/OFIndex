package ikklos.ofindexbackend.schedule;

import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtKeyScheduleTask {

    private final UserRepository userRepository;

    public JwtKeyScheduleTask(@Autowired UserRepository repository){
        userRepository=repository;
    }


    @Scheduled(fixedRate = 60*1000)
    public void refreshAllJwtKey(){

        var factory=new JwtUtils.JwtkeyFactory();

        for(var user:userRepository.findAll()){
            user.setJwtKey(factory.generateJwtKey());
            userRepository.save(user);
        }

    }

}
