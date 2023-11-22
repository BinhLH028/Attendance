package com.example.AttendanceApplication.Auth;

import com.example.AttendanceApplication.Auth.Token.ConfirmationToken;
import com.example.AttendanceApplication.Auth.Token.ConfirmationTokenService;
import com.example.AttendanceApplication.Auth.Token.Token;
import com.example.AttendanceApplication.Auth.Token.TokenRepository;
import com.example.AttendanceApplication.Common.Const;
import com.example.AttendanceApplication.DTO.AppUserDTO;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Enum.TokenType;
import com.example.AttendanceApplication.Model.AppUser;
import com.example.AttendanceApplication.Repository.AppUserRepository;
import com.example.AttendanceApplication.Service.AppUserService;
import com.example.AttendanceApplication.Service.JwtService;
import com.example.AttendanceApplication.Validator.EmailValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private final AppUserRepository appUserRepository;

    @Autowired
    private final TokenRepository tokenRepository;

    @Autowired
    private AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final EmailValidator emailValidator;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    public String RegistrationToken;

    public String register(RegisterRequest request) {

        boolean isValidEmail = emailValidator.patternMatches(request.getEmail(), Const.REGEX_PATTERN);
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        if((request.getRole()) == null || (request.getRole()).toString() == "") {
            request.setRole(Role.USER);
        }

        var user = new AppUser(request.getFirstname() + request.getLastname(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),request.getRole());

        RegistrationToken = appUserService.registerNewAccount(user);
        System.out.println(RegistrationToken);
        return "";
//        var jwtToken = jwtService.generateToken(appUser);
//        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public ResponseEntity authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
            )
        );
        AppUser appUser = appUserRepository.findByEmail(request.getEmail()).orElseThrow();
        System.out.println(appUser);

        if (appUser.isEnabled()) {
            var jwtToken = jwtService.generateToken(appUser);
            var refreshToken = jwtService.generateRefreshToken(appUser);
            revokeAllUserTokens(appUser);
            saveUserToken(appUser,jwtToken);
            AppUserDTO userDTO = this.modelMapper.map(appUser, AppUserDTO.class);
            return new ResponseEntity<>(new AuthenticationResponse(jwtToken,refreshToken,userDTO), HttpStatus.OK) ;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public String confirmToken(String token) {
        //TODO update exeption
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());

        return messageSource.getMessage("04", new String[]{}, Locale.getDefault());
    }

    private void saveUserToken(AppUser user, String jwtToken) {
        var token = Token.builder()
                .appUser(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(AppUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(refreshToken);
        if (userEmail != null) {
            AppUser appUser = this.appUserRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, appUser)) {
                var accessToken = jwtService.generateToken(appUser);
                revokeAllUserTokens(appUser);
                saveUserToken(appUser, accessToken);
                AppUserDTO userDTO = this.modelMapper.map(appUser, AppUserDTO.class);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .user(userDTO)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
