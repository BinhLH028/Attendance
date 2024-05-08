package com.example.AttendanceApplication.Config;

import com.example.AttendanceApplication.Auth.LogoutService;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Secutity.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutService logoutHandler;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/user/**",
            "/api/v1/management/**",
            "/api/v1/section/**",
            "/our-websocket/**",
            "/user/**",
            "/topic/**",
            "/api/v1/user/**",
            "/api/v1/topic/**",
            // for Swagger UI v2
            "/v2/api-docs",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            // for Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private static final String[] AUTH_ADMIN = {
            "/api/v1/user/update/{id}",
            "/api/v1/user/upload-teachers",
            "/api/v1/user/upload-students",
            "/api/v1/run-batch-job",
            "/api/v1/course_section/create",
            "/api/v1/course_section/upload",
            "/api/v1/course_section/delete",
//            "/api/v1/management",
            "/api/v1/section/add",
            "/api/v1/student",
            "/api/v1/student_enrolled/new",
            "/api/v1/student_enrolled/update",
            "/api/v1/student_enrolled/upload",
            "/api/v1/teacher/all",
            "/api/v1/teacher_teach/new",
            "/api/v1/teacher_teach/update",
    };

    private static final String[] AUTH_TEACHER = {
            "/api/v1/attendance/save",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf()
                .disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(AUTH_ADMIN).hasAnyAuthority("ADMIN")
                        .requestMatchers(AUTH_TEACHER).hasAnyAuthority("TEACHER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/user/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        return http.build();
    }
}
