package com.example.farmaser.config;

/**
 * Constantes para paginación global
 * Define límites máximos y valores por defecto para evitar consultas masivas
 */
public class PaginationConstants {
    
    // Tamaños por defecto
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_MAX_PAGE_SIZE = 100;
    
    // Tamaños máximos por tipo de recurso
    public static final int MAX_PAGE_SIZE_PRODUCTS = 100;
    public static final int MAX_PAGE_SIZE_CUSTOMERS = 100;
    public static final int MAX_PAGE_SIZE_SALES = 50;
    public static final int MAX_PAGE_SIZE_RESERVATIONS = 50;
    public static final int MAX_PAGE_SIZE_AUDIT = 100;
    
    // Utilidad para validar y limitar el tamaño de página
    public static int validateAndLimitPageSize(int size, int maxSize) {
        if (size < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, maxSize);
    }
    
    private PaginationConstants() {
        // Clase de utilidad, no instanciable
    }
}

