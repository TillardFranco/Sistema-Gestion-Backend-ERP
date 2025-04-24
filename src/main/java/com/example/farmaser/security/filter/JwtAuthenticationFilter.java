package com.example.farmaser.security.filter;

import com.example.farmaser.model.entity.UserEntity;
import com.example.farmaser.security.jwt.JwtUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtUtils jwtUtils;

    // al gordo del video le salia error con el autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        UserEntity userEntity = null;
        String email = "";
        String password = "";
        try {
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            email = userEntity.getEmail();
            password = userEntity.getPassword();
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        String token = jwtUtils.generateAccesToken(user.getUsername());

        // Obtener el rol del usuario (asumiendo que el primer authority es el rol)
        String role = user.getAuthorities().stream().findFirst().get().getAuthority();

        // Configurar la cookie para el token JWT
        Cookie tokenCookie = new Cookie("JWT_TOKEN", token);
        tokenCookie.setHttpOnly(true); // Importante para seguridad
        tokenCookie.setSecure(true); // En producción debería ser true (HTTPS)
        tokenCookie.setPath("/"); // Accesible en todo el dominio
        tokenCookie.setMaxAge(86400); // Tiempo de vida en segundos (ej. 24h)

        // Configurar la cookie para el rol
        Cookie roleCookie = new Cookie("USER_ROLE", role);
        roleCookie.setHttpOnly(false); // Puede ser accedido por JS si es necesario
        roleCookie.setSecure(true);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(86400);

        // Añadir las cookies a la respuesta
        response.addCookie(tokenCookie);
        response.addCookie(roleCookie);

        // También puedes mantener el token en el header por si acaso
        response.addHeader("Authorization", "Bearer " + token);

        // Respuesta JSON opcional
        Map<String, Object> httpResponse = new HashMap<>();
        httpResponse.put("Token", token);
        httpResponse.put("Message", "Autenticación Correcta");
        httpResponse.put("email", user.getUsername());
        httpResponse.put("role", role);

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}

