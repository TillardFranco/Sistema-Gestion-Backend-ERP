package com.example.farmaser.config;

import com.example.farmaser.service.IReservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ReservationScheduler.class);

    @Autowired
    private IReservation reservationService;

    /**
     * Ejecuta la expiración de reservas cada hora
     * Cron: segundo, minuto, hora, día del mes, mes, día de la semana
     * "0 0 * * * ?" = cada hora
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void expireReservations() {
        logger.info("Ejecutando expiración automática de reservas...");
        try {
            reservationService.expireReservations();
            logger.info("Expiración de reservas completada exitosamente");
        } catch (Exception e) {
            logger.error("Error al expirar reservas automáticamente", e);
        }
    }
}

