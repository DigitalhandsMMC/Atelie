package com.example.clothesshop.controller;

import com.example.clothesshop.reponse.UserResponse;
import com.example.clothesshop.request.UserLoginRequest;
import com.example.clothesshop.request.UserSignUpRequest;
import com.example.clothesshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getAllUsers")
    ResponseEntity<List<UserResponse>> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @PostMapping("/signup")
    ResponseEntity<?> signup(@Valid @RequestBody UserSignUpRequest userSignUpRequest){
        return userService.signup(userSignUpRequest);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/user-login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest) {
        return userService.login(userLoginRequest);
    }

//    @GetMapping("/loginWithGoogle")
//    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        String email = principal.getAttribute("email");
//        User user = userRepository.findByEmail(email).orElse(null);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(user);
//    }


//    @GetMapping("/googleUserInfo")
//    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
//        Map<String, Object> userDetails = new HashMap<>();
//
//        if (principal != null) {
//            userDetails.put("name", principal.getAttribute("name"));
//            userDetails.put("email", principal.getAttribute("email"));
//        }
//
//        return userDetails;
//    }

}
