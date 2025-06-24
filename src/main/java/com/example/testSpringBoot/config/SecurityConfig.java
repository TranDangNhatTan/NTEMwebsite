package com.example.testSpringBoot.config;

import com.example.testSpringBoot.config.handlers.CustomAuthenticationFailureHandler;
import com.example.testSpringBoot.config.handlers.CustomAuthenticationSuccessHandler;
import com.example.testSpringBoot.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler; // Tiêm handler thành công

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler; // Tiêm handler thất bại

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/vendor/**").permitAll()
                        .requestMatchers("/", "/index", "/register", "/courses", "/events", "/achievements").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        // .defaultSuccessUrl("/index", true) // Bỏ dòng này
                        // .failureUrl("/login?error=true") // Bỏ dòng này
                        .successHandler(authenticationSuccessHandler) // <-- SỬ DỤNG HANDLER MỚI
                        .failureHandler(authenticationFailureHandler) // <-- SỬ DỤNG HANDLER MỚI
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/login")
                        .permitAll());

        return http.build();
    }
}