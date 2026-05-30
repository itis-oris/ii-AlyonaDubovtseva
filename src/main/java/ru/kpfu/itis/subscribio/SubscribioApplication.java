package ru.kpfu.itis.subscribio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SubscribioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscribioApplication.class, args);
    }
}