package com.example.farmaser.security;

import com.example.farmaser.security.filter.JwtAuthenticationFilter;
import com.example.farmaser.security.filter.JwtAuthorizationFilter;
import com.example.farmaser.security.jwt.JwtUtils;
import com.example.farmaser.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthorizationFilter authorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/register"); //esto es por si queremos cambiarle la ruta en la que se loguea, por defecto es /login

        return httpSecurity
                .csrf(config -> config.disable()) //para trabajar con formularios, como no lo hacemos, se desact
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/register").permitAll();
                    auth.requestMatchers("/api/v1/users/**","/api/v1/admin/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /*@Bean
    UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("santiago")
                .password("1234")
                .roles()
                .build());

        return manager;
    }*/

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            HttpSecurity httpSecurity,
            PasswordEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return builder.build();
    }
}
//esto da error en el .build() pero asi da el loco el video, ni idea
   /*  @Bean
AuthenticationManager authenticationManager(
        HttpSecurity httpSecurity,
        PasswordEncoder passwordEncoder) throws Exception {

    return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .build(); // Eliminamos el .and()
} */