package com.websocket.demo.controller;

import com.websocket.demo.request.AddFriendRequest;
import com.websocket.demo.request.CreateUserRequest;
import com.websocket.demo.request.LoginRequest;
import com.websocket.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return "login";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute CreateUserRequest request){
        try {
            if (userService.create(request)) return "login";
        } catch (RuntimeException e){
            return "createUser";
        }
        return "createUser";
    }

    @PostMapping("/friend")
    public String newFriend(@ModelAttribute AddFriendRequest request, @SessionAttribute(name = "user", required = false) LoginRequest userInfo){
        if(userService.addFriend(request, userInfo.getNickname())) return "redirect:/";
        return "addFriend";
    }

    @GetMapping("/create")
    public String createUserPage(){
        return "createUser";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().setMaxInactiveInterval(0);
        return "login";
    }

    @GetMapping("/friend")
    public String addFriendPage() {
        return "addFriend";
    }
}

