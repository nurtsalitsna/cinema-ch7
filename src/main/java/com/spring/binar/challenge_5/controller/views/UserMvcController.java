package com.spring.binar.challenge_5.controller.views;

import com.spring.binar.challenge_5.dto.AuthenticationRequestDTO;
import com.spring.binar.challenge_5.dto.UserRegisterDTO;
import com.spring.binar.challenge_5.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/web-public/auth")
public class UserMvcController {
    
    private final UserService userService;

    @Autowired
    public UserMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login-form")
    public String loginPublic(Model model){

        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        model.addAttribute("user", authenticationRequestDTO);

        return "user/login";
    }

    @GetMapping("/register-form")
    public String registerPublic(Model model){

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        model.addAttribute("user", userRegisterDTO);

        return "user/register";
    }

    @PostMapping("/login-form/save")
    public String loginPublicSave(@ModelAttribute("user") AuthenticationRequestDTO authUser, HttpSession session){
        return userService.authentication(authUser, session);
    }

    @PostMapping("/register-form/save")
    public String registerPublicSave(@Valid @ModelAttribute("user") UserRegisterDTO userRegisterDTO){
        userService.register(userRegisterDTO);
        return "redirect:/user/login";
    }

}
