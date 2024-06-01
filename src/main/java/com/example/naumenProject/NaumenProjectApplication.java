package com.example.naumenProject;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NaumenProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaumenProjectApplication.class, args);
	}

	@Configuration
    public class MvcConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String userDir = System.getProperty("user.dir");
            String uploadDir = userDir + File.separator + "uploads" + File.separator;

            registry.addResourceHandler("/games/**")
                    .addResourceLocations("file:" + uploadDir);
        }
    }

}
