package ikklos.ofindexbackend.schedule;

import ikklos.ofindexbackend.utils.JwtUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtKeyScheduleTask {


    @Scheduled(fixedRate = JwtUtils.KeyDuration)
    public void refreshAllJwtKey(){

        JwtUtils.refreshJwtKey();

    }

}
