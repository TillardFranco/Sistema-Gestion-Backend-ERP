package com.example.farmaser.config;

import com.example.farmaser.service.IAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductExpirationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ProductExpirationScheduler.class);

    @Autowired
    private IAlert alertService;

    /**
     * Ejecuta la generación de alertas de productos próximos a vencer cada día a las 8:00 AM
     * Cron: segundo, minuto, hora, día del mes, mes, día de la semana
     * "0 0 8 * * ?" = cada día a las 8:00 AM
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void generateExpirationAlerts() {
        logger.info("Ejecutando generación automática de alertas de productos próximos a vencer...");
        try {
            alertService.generateExpirationAlerts();
            logger.info("Generación de alertas de vencimiento completada exitosamente");
        } catch (Exception e) {
            logger.error("Error al generar alertas de productos próximos a vencer", e);
        }
    }
}

