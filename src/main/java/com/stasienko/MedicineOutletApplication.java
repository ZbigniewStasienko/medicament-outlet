package com.stasienko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedicineOutletApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicineOutletApplication.class, args);
    }

}
