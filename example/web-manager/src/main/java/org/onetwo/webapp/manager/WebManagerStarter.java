package org.onetwo.webapp.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
//@SpringBootApplication(exclude={VelocityAutoConfiguration.class})
public class WebManagerStarter extends SpringBootServletInitializer {
	
	/*@RequestMapping("/")
    String home() {
        return "index";
    }*/
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application;
	}
	
    public static void main(String[] args) {
        SpringApplication.run(WebManagerStarter.class, args);
    }
}
