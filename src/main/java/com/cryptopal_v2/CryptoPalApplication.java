package com.cryptopal_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.cryptopal_v2.model")
@EnableJpaRepositories("com.cryptopal_v2.repository")
public class CryptoPalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CryptoPalApplication.class, args);
    }
} 