package com.example.AttendanceApplication.WebSocket;

import com.example.AttendanceApplication.Model.AppUser;
import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);

    private AppUser appUser;

    public UserHandshakeHandler(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//        final String randomId = UUID.randomUUID().toString();
//        String jwtToken = (String) attributes.get("jwtToken");
//        LOG.info("token ", jwtToken);
//        LOG.info("User with ID '{}' opened the page", randomId);
//        LOG.info(" '{}' ", attributes);
//        LOG.info("User with ID '{}' ", request.getHeaders());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        AppUser userDetails = (AppUser) authentication.getPrincipal();
//        LOG.info("User with email '{}' opened the page", authentication.getName());
        return new UserPrincipal(appUser.getEmail());
    }
}