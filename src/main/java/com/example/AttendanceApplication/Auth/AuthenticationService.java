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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    AppUserRepository appUserRepository;

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

    public String msg = "";

    private List<String> resultMsg = new ArrayList<>();

    public ResponseEntity register(RegisterRequest request) {
        resultMsg.clear();
        request.setEmail(request.getEmail().toLowerCase());
        if (validateRequest(request)) {

            var user = new AppUser(request.getUsername(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getEmail(),request.getRole());

            RegistrationToken = appUserService.registerNewAccount(user, request.getUserCode());
            msg = messageSource.getMessage("U01", new String[]{request.getEmail()}, Locale.getDefault());
            return new ResponseEntity<>(msg,HttpStatus.OK);
        }

        System.out.println(RegistrationToken);

        return new ResponseEntity<>(resultMsg,HttpStatus.BAD_REQUEST);
    }

    private boolean validateRequest(RegisterRequest request) {
        boolean isValid = true;

        // check form of email
        boolean isValidEmail = emailValidator.patternMatches(request.getEmail(), Const.REGEX_PATTERN);
        if (!isValidEmail) {
            isValid = false;
            msg = messageSource.getMessage("U02", new String[]{}, Locale.getDefault());
            resultMsg.add(msg);
        }

        if((request.getRole()) == null || (request.getRole()).toString() == "") {
            request.setRole(Role.USER);
        }

        // check email has been used
        Optional<AppUser> temp = appUserRepository.findByEmail(request.getEmail());

        if (temp.isPresent()) {
            isValid = false;
            msg = messageSource.getMessage("U03", new String[]{request.getEmail()}, Locale.getDefault());
            resultMsg.add(msg);
        }


        return isValid;
    }

    public ResponseEntity authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail().toLowerCase(), request.getPassword()
            )
        );
        AppUser appUser = appUserRepository.findByEmail(request.getEmail().toLowerCase()).orElseThrow();

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
        //TODO create email again ??

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());

        return messageSource.getMessage("U04", new String[]{}, Locale.getDefault());
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

        //TODO:
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
