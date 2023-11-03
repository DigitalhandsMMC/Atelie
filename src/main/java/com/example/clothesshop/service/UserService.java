package com.example.clothesshop.service;

import com.example.clothesshop.dao.entity.User;
import com.example.clothesshop.dao.repository.CustomerRepository;
import com.example.clothesshop.dao.repository.UserRepository;
import com.example.clothesshop.enums.UserRole;
import com.example.clothesshop.reponse.AuthenticationResponse;
import com.example.clothesshop.reponse.UserResponse;
import com.example.clothesshop.request.UserLoginRequest;
import com.example.clothesshop.request.UserSignUpRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    private final JwtService jwtService;
    private EncryptionService encryptionService;

    public UserService(UserRepository userRepository, CustomerRepository customerRepository, ModelMapper modelMapper, JwtService jwtService, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.encryptionService = encryptionService;
    }

    public ResponseEntity<?> signup(UserSignUpRequest userSignUpRequest) {
        User firstByEmail = userRepository.findFirstByEmail(userSignUpRequest.getEmail());
        if (Objects.isNull(firstByEmail)) {
            User user = modelMapper.map(userSignUpRequest, User.class);
            user.setPassword(encryptionService.encryptPassword(userSignUpRequest.getPassword()));
            user.setUserRole(UserRole.USER);
            User saved = userRepository.save(user);
            return ResponseEntity.ok(modelMapper.map(saved, UserResponse.class));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
    }

    public ResponseEntity<?> login(UserLoginRequest userLoginRequest) {
        Optional<User> userOptional = userRepository.findUserByEmail(userLoginRequest.getEmail());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();

        if (!encryptionService.verifyPassword(userLoginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtService.generateToken(userLoginRequest.getEmail());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setUserId(user.getId());
        authenticationResponse.setJwtToken(token);

        return ResponseEntity.ok(authenticationResponse);
    }

    public ResponseEntity<?> getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.nonNull(user)) {
            return ResponseEntity.ok(modelMapper.map(user, UserResponse.class));
        }
        return ResponseEntity.status(NOT_FOUND).body("User not found");
    }

    public ResponseEntity<List<UserResponse>> getAllUsers(Pageable pageable) {
        try {
            Page<User> usersPage = userRepository.findAll(pageable);
            List<User> users = usersPage.getContent();
            List<UserResponse> userResponses = users.parallelStream()
                    .map(user -> modelMapper.map(user, UserResponse.class))
                    .toList();
            return new ResponseEntity<>(userResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
