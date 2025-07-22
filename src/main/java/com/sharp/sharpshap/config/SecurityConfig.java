package com.sharp.sharpshap.config;

import com.sharp.sharpshap.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyUserDetailsService myUserDetailsService;//предоставляет информацию о пользователе
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // разрешаем iframe с того же домена
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(myUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/auth/**").permitAll()
                        .requestMatchers("api/hello").permitAll()
                        .requestMatchers("/h2-console/**", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("api/**").hasAnyRole("USER")
                        .requestMatchers("/actuator/**").permitAll() // если используешь actuator
                        .requestMatchers("/webjars/**", "/css/**", "/js/**", "/images/**").permitAll() // статика H2
                        .anyRequest().authenticated()//остальные требуется аутентификация
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/h2-console/**", "/webjars/**", "/css/**", "/js/**", "/images/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).
                userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}
