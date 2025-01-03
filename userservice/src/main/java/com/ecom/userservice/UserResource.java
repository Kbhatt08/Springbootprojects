package com.ecom.userservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.userservice.entity.RegisterApiResponse;
import com.ecom.userservice.entity.LoginResponse;
import com.ecom.userservice.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/users")
public class UserResource {
    @GetMapping("/{id}")
    public ResponseEntity<User>  getUserbyid(@RequestParam String param) {
        return null;
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterApiResponse> registerUser(@RequestBody String entity) {
        RegisterApiResponse response = new RegisterApiResponse();
        response.setStatus("success");
        response.setMessage("User registered successfully");
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody User entity) { 
        return null;
    }
    
}
