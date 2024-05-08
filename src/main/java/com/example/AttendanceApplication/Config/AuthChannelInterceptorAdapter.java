package com.example.AttendanceApplication.Config;

import com.example.AttendanceApplication.Model.AppUser;
import com.example.AttendanceApplication.Repository.AppUserRepository;
import com.example.AttendanceApplication.Service.JwtService;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser appUser;

    private final WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Inject
    public AuthChannelInterceptorAdapter(final WebSocketAuthenticatorService webSocketAuthenticatorService) {
        this.webSocketAuthenticatorService = webSocketAuthenticatorService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String authorizationHeader = accessor.getNativeHeader("Authorization").get(0);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwtToken = authorizationHeader.substring(7); // Extract the token excluding "Bearer "
                //
                final String userEmail;
                System.out.println(jwtToken);
                //

                userEmail = jwtService.extractEmail(jwtToken);
                if (userEmail != null) {
                    appUser = appUserRepository.findByEmail(userEmail)
                            .orElseThrow();
                    if (jwtService.isTokenValid(jwtToken, appUser)) {
                        final UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAuthenticatedOrFail(userEmail);
                        accessor.setUser(user);
                    }
                }
            }
        }
        return message;
    }
}
