package com.example.AttendanceApplication.Config;

import com.example.AttendanceApplication.Model.AppUser;
import com.example.AttendanceApplication.Repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class WebSocketAuthenticatorService {
    // This method MUST return a UsernamePasswordAuthenticationToken instance, the spring security chain is testing it with 'instanceof' later on. So don't use a subclass of it or any other class
    @Autowired
    AppUserRepository appUserRepository;

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String  userEmail) throws AuthenticationException {
        if (userEmail == null || userEmail.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        // Add your own logic for retrieving user in fetchUserFromDb()

        if (appUserRepository.findByEmail(userEmail) == null) {
            throw new BadCredentialsException("Bad credentials for user " + userEmail);
        }

//        AppUser appUser = appUserRepository.findByEmail(userEmail)

        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                userEmail,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }
}
