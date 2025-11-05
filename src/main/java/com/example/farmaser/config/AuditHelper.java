package com.example.farmaser.config;

import com.example.farmaser.model.entity.ActionType;
import com.example.farmaser.model.repository.UserRepository;
import com.example.farmaser.service.IAudit;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Helper class para facilitar el registro de auditoría desde los servicios y controladores
 */
@Component
public class AuditHelper {

    @Autowired
    private IAudit auditService;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Registra una acción de auditoría
     */
    public void log(String entityType, Long entityId, ActionType action, String oldValue, String newValue, String description, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId();
            String ipAddress = request != null ? getClientIpAddress(request) : null;
            auditService.log(entityType, entityId, action, userId, oldValue, newValue, description, ipAddress);
        } catch (Exception e) {
            // Log error pero no interrumpir el flujo principal
            System.err.println("Error al registrar auditoría: " + e.getMessage());
        }
    }

    /**
     * Registra una acción de auditoría sin request (para uso interno)
     */
    public void log(String entityType, Long entityId, ActionType action, String oldValue, String newValue, String description) {
        log(entityType, entityId, action, oldValue, newValue, description, null);
    }

    /**
     * Convierte un objeto a JSON string para almacenar en oldValue/newValue
     */
    public String toJsonString(Object obj) {
        try {
            if (obj == null) {
                return null;
            }
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj != null ? obj.toString() : null;
        }
    }

    /**
     * Crea un mapa resumido de cambios para descripción
     */
    public String createChangeDescription(Map<String, Object> changes) {
        if (changes == null || changes.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        changes.forEach((key, value) -> {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(key).append(": ").append(value);
        });
        return sb.toString();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                    .getId();
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}

