package com.project.sushi_website.config;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

public class CustomBanner implements Banner {
    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println("" +
                "███╗░░░██╗██╗░██████╗░░██████╗░███████╗██████╗\n" +
                "████╗░░██║██║██╔════╝░██╔════╝░██╔════╝██╔══██╗\n" +
                "██╔██╗░██║██║██║░░███╗██║░░███╗█████╗░░██████╔╝\n" +
                "██║╚██╗██║██║██║░░░██║██║░░░██║██╔══╝░░██╔══██╗\n" +
                "██║░╚████║██║╚██████╔╝╚██████╔╝███████╗██║░░██║\n" +
                "╚═╝░░╚═══╝╚═╝░╚═════╝░░╚═════╝░╚══════╝╚═╝░░");
    }
}