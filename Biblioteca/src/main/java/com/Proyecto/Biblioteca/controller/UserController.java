package com.Proyecto.Biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user/perfil")
    public String userProfile() {
        return "user/perfil";
    }
}