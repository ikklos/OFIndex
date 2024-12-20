package ikklos.ofindexbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OfIndexBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfIndexBackEndApplication.class, args);
    }

}
