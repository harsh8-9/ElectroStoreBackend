package com.harry.electro.store.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
@author :-
        Harshal Bafna
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String testing() {
        return "<h2>Welcome to</h2> <h1>Electro-Store</h1>";
    }
}
