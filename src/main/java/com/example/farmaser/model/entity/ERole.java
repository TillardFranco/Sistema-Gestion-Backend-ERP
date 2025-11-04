package com.example.farmaser.model.entity;

public enum ERole {
    SUPER_ADMIN,    // Configuración del sistema, usuarios, roles
    MANAGER,        // Administración diaria del negocio
    CASHIER,        // Operaciones de venta al público
    WAREHOUSE,      // Gestión de inventario y stock
    VIEWER,         // Solo lectura (consultas)
    
    // Roles legacy (mantener para compatibilidad con datos existentes)
    USER,           // @Deprecated - Usar CASHIER en su lugar
    ADMIN           // @Deprecated - Usar SUPER_ADMIN o MANAGER según corresponda
}
