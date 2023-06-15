package com.ha.facesdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ha","com.jni"})
public class FacesdkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacesdkApplication.class, args);
    }

}
