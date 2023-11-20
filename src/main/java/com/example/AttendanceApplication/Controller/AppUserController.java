package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Auth.AuthenticationRequest;
import com.example.AttendanceApplication.Auth.AuthenticationResponse;
import com.example.AttendanceApplication.Auth.AuthenticationService;
import com.example.AttendanceApplication.Auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Locale;

@CrossOrigin
@RestController
@RequestMapping(value = "/API/user")
public class AppUserController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    MessageSource messageSource;
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {
        try {
            return new ResponseEntity(authenticationService.register(request), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(messageSource.getMessage("03", new String[]{request.getEmail()}, Locale.getDefault()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }


    @PostMapping(value = "/confirm")
    public ResponseEntity<AuthenticationResponse> ConfirmRegistration(
            @RequestParam("token") String token) {
        return new ResponseEntity(authenticationService.confirmToken(token), HttpStatus.OK);
    }
}
