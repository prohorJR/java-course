package com.example.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("Запуск банкомата...");
        SpringApplication.run(JavaApplication.class, args);
        System.out.println("Приложение запущено!");
    }
}