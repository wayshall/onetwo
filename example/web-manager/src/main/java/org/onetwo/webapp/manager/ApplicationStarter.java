package org.onetwo.webapp.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;

import de.codecentric.boot.admin.config.EnableAdminServer;

@Controller
@SpringBootApplication
@EnableAdminServer
//@SpringBootApplication(exclude={VelocityAutoConfiguration.class})
public class ApplicationStarter extends SpringBootServletInitializer {
	
	/*@RequestMapping("/")
    String home() {
        return "index";
    }*/
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application;
	}
	
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }
}
