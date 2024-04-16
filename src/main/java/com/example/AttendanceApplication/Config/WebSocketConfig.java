package com.example.AttendanceApplication.Config;

import com.example.AttendanceApplication.Model.AppUser;
import com.example.AttendanceApplication.Repository.AppUserRepository;
import com.example.AttendanceApplication.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser appUser;

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic","user");
        registry.setApplicationDestinationPrefixes("/ws");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/our-websocket")
                .setAllowedOriginPatterns("*")
//                .setHandshakeHandler(new UserHandshakeHandler(appUser))
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
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
                                accessor.setUser(new UsernamePasswordAuthenticationToken(
                                        userEmail,
                                        null,
                                        Collections.singleton((GrantedAuthority) () -> "USER")));
                            }
                        }
                    }
//                    Authentication user = ... ; // access authentication header(s)
//                    accessor.setUser(user);
                    System.out.println(accessor.getNativeHeader("Authorization"));
                    System.out.println(SecurityContextHolder.getContext().getAuthentication());
                    System.out.println(accessor.getUser());
                }

                return message;
            }

        });

    }
}