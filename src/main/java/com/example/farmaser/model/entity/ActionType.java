package com.example.farmaser.model.entity;

public enum ActionType {
    CREATE,     // Crear entidad
    UPDATE,     // Actualizar entidad
    DELETE,     // Eliminar entidad (soft delete)
    LOGIN,      // Inicio de sesión
    LOGOUT,     // Cierre de sesión
    CANCEL,     // Cancelar operación (venta, reserva, etc.)
    COMPLETE,   // Completar operación
    CONFIRM,    // Confirmar operación
    EXPIRE      // Expirar (reservas, etc.)
}

