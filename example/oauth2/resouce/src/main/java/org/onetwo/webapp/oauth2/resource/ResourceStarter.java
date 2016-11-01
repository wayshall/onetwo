package org.onetwo.webapp.oauth2.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
public class ResourceStarter extends SpringBootServletInitializer {
	
	@RequestMapping("/")
    String home() {
        return "index";
    }
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application;
	}
	
    public static void main(String[] args) {
        SpringApplication.run(ResourceStarter.class, args);
    }
}
