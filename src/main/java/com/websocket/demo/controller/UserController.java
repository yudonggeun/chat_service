package com.websocket.demo.controller;

import com.websocket.demo.request.CreateUserRequest;
import com.websocket.demo.request.LoginRequest;
import com.websocket.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginRequest request, HttpServletRequest servletRequest){
        if (userService.login(request)) {
            servletRequest.getSession(true).setAttribute("user", request);
            return "redirect:/";
        }
        return "createUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute CreateUserRequest request, HttpServletRequest servletRequest){
        if(userService.create(request)){
            return "createUser";
        }
        return "redirect:/";
    }

    @GetMapping("/create")
    public String createUserPage(){
        return "createUser";
    }
}
