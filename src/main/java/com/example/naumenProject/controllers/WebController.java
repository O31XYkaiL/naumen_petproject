package com.example.naumenProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.naumenProject.services.ProjectService;

import org.springframework.ui.Model;
import com.example.naumenProject.models.User;
import com.example.naumenProject.repositories.UserRepository;

import org.springframework.security.core.Authentication;

@Controller
public class WebController
{
    private final UserRepository userRepository;

    @Autowired
    public WebController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/")
    public String getMainPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        model.addAttribute("user", currentUser);

        return "index";
    }
}
