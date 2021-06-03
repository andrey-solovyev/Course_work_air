package org.example.rest.controllers;

import org.example.data.dto.RegistrationDto;
import org.example.rest.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller

public class RegistrationController {

    @Autowired
    private PersonService personService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute RegistrationDto registrationDto,
            Model model
    ) {
        if (!personService.addUser(registrationDto)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }


}
