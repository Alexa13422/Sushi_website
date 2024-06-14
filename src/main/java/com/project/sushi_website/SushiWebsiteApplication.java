package com.project.sushi_website;

import com.project.sushi_website.config.CustomBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SushiWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SushiWebsiteApplication.class);
        app.setBanner(new CustomBanner());
        app.run(args);
    }

}
