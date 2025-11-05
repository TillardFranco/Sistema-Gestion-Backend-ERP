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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
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
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login"); // Ruta para el login/autenticación

        return httpSecurity
                .csrf(config -> config.disable()) //para trabajar con formularios, como no lo hacemos, se desact
                .authorizeHttpRequests(auth -> {
                    // Público
                    auth.requestMatchers("/api/v1/login").permitAll();
                    
                    // Solo SUPER_ADMIN
                    auth.requestMatchers("/api/v1/users/**", "/api/v1/admin/**")
                        .hasRole("SUPER_ADMIN");
                    
                    // SUPER_ADMIN + MANAGER (reportes futuros)
                    // auth.requestMatchers("/api/v1/reports/**")
                    //     .hasAnyRole("SUPER_ADMIN", "MANAGER");
                    
                    // SUPER_ADMIN + MANAGER + CASHIER (ventas y clientes)
                    auth.requestMatchers("/api/v1/sales/**", "/api/v1/customers/**")
                        .hasAnyRole("SUPER_ADMIN", "MANAGER", "CASHIER");
                    
                    // SUPER_ADMIN + MANAGER + WAREHOUSE (productos y stock)
                    // Los permisos específicos se manejan con @PreAuthorize en controladores
                    auth.requestMatchers("/api/v1/products/**", "/api/v1/categories/**", "/api/v1/stock/**")
                        .hasAnyRole("SUPER_ADMIN", "MANAGER", "WAREHOUSE", "CASHIER", "VIEWER");
                    
                    // Reservas: SUPER_ADMIN + MANAGER + CASHIER
                    auth.requestMatchers("/api/v1/reservations/**")
                        .hasAnyRole("SUPER_ADMIN", "MANAGER", "CASHIER", "VIEWER");
                    
                    // Alertas: todos autenticados
                    auth.requestMatchers("/api/v1/alerts/**").authenticated();
                    
                    // Auditoría: Solo SUPER_ADMIN y MANAGER
                    auth.requestMatchers("/api/v1/audit/**")
                        .hasAnyRole("SUPER_ADMIN", "MANAGER");
                    
                    // Por defecto, requiere autenticación (los permisos específicos se manejan con @PreAuthorize)
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