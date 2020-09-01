package com.safe.campus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SafeCampusApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SafeCampusApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
