package com.example.demo.config;


import com.example.demo.Security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http 
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> SessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth 
                // Public endpoints 
                .requestMatchers("/api/auth/**").permitAll()

                // Admin only 
                .requestMatchers("/api/users/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/comapnies/**").hasRole("ADMIN")
                .requestMatchers("/api/branches/**").hasRole("ADMIN")
                .requestMatchers("/api/dashboard/admin/**").hasRole("ADMIN")

                //Branch Manager
                .requestMatchers("/api/users/manager/**").hasRole("BRANCH_MANAGER")
                .requestMatchers("/api/tokens/verify").hasRole("BRANCH_MANAGER")
                .requestMatchers("/api/completion-reports/*/review").hasRole("BRANCH_MANAGER")
                .requestMatchers("/api/dashboard/manager/**").hasRole("BRANCH_MANAGER")

                //Techncian
                .requestMatchers("/api/requests").hasRole("TECHNICIAN")
                .requestMatchers("/api/tokens/technician/**").hasRole("TECHNICAN")
                .requestMatchers("/api/dashboard/technician/**").hasRole("TECHNICIAN")

                //Activity logs - each role sees own logs 
                .requestMatchers("/api/activity-logs/**").authenticated()

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private Object SessionCreationPolicy(SessionCreationPolicy stateless) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'SessionCreationPolicy'");
    }

    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
