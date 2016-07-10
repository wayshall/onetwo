package com.yooyo.zhiyetong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication(exclude={VelocityAutoConfiguration.class})
//@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
//@SpringBootApplication
public class ApplicationStarter {
	
	@RequestMapping("/")
    String home() {
        return "index";
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }
}
