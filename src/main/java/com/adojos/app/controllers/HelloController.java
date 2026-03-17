package com.adojos.app.controllers;

import com.adojos.app.annotations.GetMapping;
import com.adojos.app.annotations.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public static String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/pi")
    public static String wmPI() {
        return "PI = " + Math.PI;
    }

    @GetMapping("/hello")
    public static String wmHello() {
        return "Hello world";
    }
}