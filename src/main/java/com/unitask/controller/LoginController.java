package com.unitask.controller;

import com.unitask.constant.Enum.UserRole;
import com.unitask.constant.error.AuthErrorConstant;
import com.unitask.dto.user.LoginRequest;
import com.unitask.dto.user.LoginResponse;
import com.unitask.dto.user.OtpRequest;
import com.unitask.dto.user.SignUpRequest;
import com.unitask.exception.ServiceAppException;
import com.unitask.security.JwtUtils;
import com.unitask.service.ContextService;
import com.unitask.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(path = "auth")
public class LoginController extends ContextService {

    @Autowired
    private JwtUtils utils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login/lecturer")
    public ResponseEntity<?> loginLecturer(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (!isLecturer()) {
            throw new ServiceAppException(HttpStatus.UNAUTHORIZED, AuthErrorConstant.BAD_CREDENTIAL);
        }
        LoginResponse loginResponse = utils.generateJwtToken(authentication);

        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/login/student")
    public ResponseEntity<?> loginStudent(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (!isStudent()) {
            throw new ServiceAppException(HttpStatus.UNAUTHORIZED, AuthErrorConstant.BAD_CREDENTIAL);
        }
        LoginResponse loginResponse = utils.generateJwtToken(authentication);

        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/signUp/lecturer")
    public ResponseEntity<?> singUpLecturer(@RequestBody SignUpRequest signUpRequest) {
        userService.addUser(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getName(), UserRole.LECTURER);
        return ResponseEntity.ok("SIGN UP");
    }

    @PostMapping("/signUp/student")
    public ResponseEntity<?> singUpStudent(@RequestBody SignUpRequest signUpRequest) {
        userService.addUser(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getName(), UserRole.STUDENT);
        return ResponseEntity.ok("SIGN UP");
    }


    @GetMapping("/getOTP")
    public ResponseEntity<?> getOTP(@RequestParam String email) throws MessagingException {
        userService.getOtp(email);
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/validateOTP")
    public ResponseEntity<?> validateOTP(@RequestBody OtpRequest otpRequest) {
        return ResponseEntity.ok(userService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp()));
    }

    //TODO oauth
}
