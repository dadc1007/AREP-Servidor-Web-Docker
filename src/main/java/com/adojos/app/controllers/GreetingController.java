package com.adojos.app.controllers;

import com.adojos.app.annotations.GetMapping;
import com.adojos.app.annotations.RequestParam;
import com.adojos.app.annotations.RestController;

@RestController
public class GreetingController {
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}