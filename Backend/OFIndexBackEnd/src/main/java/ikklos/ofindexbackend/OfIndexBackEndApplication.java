package ikklos.ofindexbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScans(value = {
        @ComponentScan("ikklos.ofindexbackend.controller"),
        @ComponentScan("ikklos.ofindexbackend.repository"),
        @ComponentScan("ikklos.ofindexbackend.filesystem")
})
public class OfIndexBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfIndexBackEndApplication.class, args);
    }

}
