# Mensaje de Commit - Fase 1

## Título del Commit:

```
feat: Implementar Fase 1 - Gestión de productos, categorías y control de stock
```

## Título del Push (si usas una plataforma que lo requiera):

```
Implementar Fase 1: Gestión de productos, categorías y control de stock
```

## Descripción completa del commit:

```
feat: Implementar Fase 1 - Gestión de productos, categorías y control de stock

Implementación completa de la Fase 1 del sistema de gestión de farmacia:

## 1. Gestión de Productos (Fase 1.1)

- Actualizar ProductEntity con campos adicionales:
  * minimumStock: stock mínimo antes de alerta
  * active: flag para soft delete
  * lastModifiedDate: auditoría de modificaciones
  * Relación ManyToOne con CategoryEntity

- Agregar validaciones en ProductRequestDto:
  * Validación de precio positivo
  * Validación de stock no negativo
  * Validaciones de tamaño y formato

- Implementar ProductService completo:
  * CRUD completo con validaciones de negocio
  * Búsqueda por nombre (con paginación)
  * Soft delete (marcar como inactivo)
  * Actualización individual de stock
  * Detección de productos con stock bajo

- Crear ProductController con endpoints REST:
  * GET /api/v1/products (listado con paginación)
  * GET /api/v1/products/all (listado sin paginación)
  * GET /api/v1/products/{barcode} (buscar por código)
  * GET /api/v1/products/search (búsqueda con paginación)
  * GET /api/v1/products/search/all (búsqueda sin paginación)
  * POST /api/v1/products (crear producto)
  * PUT /api/v1/products/{barcode} (actualizar producto)
  * PATCH /api/v1/products/{barcode}/stock (actualizar stock)
  * DELETE /api/v1/products/{barcode} (soft delete)
  * GET /api/v1/products/low-stock (productos con stock bajo)

- Actualizar ProductRepository con:
  * Métodos de búsqueda por nombre
  * Soporte para paginación (PagingAndSortingRepository)
  * Filtros por productos activos

## 2. Sistema de Categorías (Fase 1.2)

- Crear CategoryEntity:
  * Campos: id, name, description, active, creationDate, lastModifiedDate
  * Relación OneToMany con ProductEntity

- Implementar CategoryService:
  * CRUD completo
  * Validación de nombres únicos
  * Soft delete

- Crear CategoryController con endpoints:
  * GET /api/v1/categories (listar todas)
  * GET /api/v1/categories/{id} (obtener por ID)
  * POST /api/v1/categories (crear)
  * PUT /api/v1/categories/{id} (actualizar)
  * DELETE /api/v1/categories/{id} (soft delete)

- Crear DTOs y Mappers:
  * CategoryRequestDto con validaciones
  * CategoryResponseDto
  * CategoryMapper con MapStruct

## 3. Control de Stock (Fase 1.3)

- Crear enum MovementType:
  * IN: Ingreso de stock
  * OUT: Egreso de stock
  * ADJUSTMENT: Ajuste de inventario
  * RETURN: Devolución

- Crear StockMovementEntity:
  * Relación con ProductEntity y UserEntity
  * Campos: type, quantity, reason, notes, date

- Implementar StockMovementService:
  * Registro de ingresos (entry)
  * Registro de egresos (exit) con validación de stock disponible
  * Actualización automática de stock en productos
  * Historial de movimientos por producto
  * Historial general de movimientos

- Crear StockMovementController:
  * POST /api/v1/stock/entry (registrar ingreso)
  * POST /api/v1/stock/exit (registrar egreso)
  * GET /api/v1/stock/movements/{productId} (historial por producto)
  * GET /api/v1/stock/movements (historial general)
  * GET /api/v1/stock/low-stock (productos con stock bajo)

- Crear DTOs y Mappers:
  * StockMovementRequestDto con validaciones
  * StockMovementResponseDto con información expandida
  * StockMovementMapper con mapeo de relaciones

## 4. Seguridad

- Actualizar SecurityConfig:
  * Agregar permisos para /api/v1/products/**
  * Agregar permisos para /api/v1/categories/**
  * Agregar permisos para /api/v1/stock/**
  * Todos requieren autenticación

## Archivos creados/modificados

### Nuevos archivos:
- ProductService.java (implementación completa)
- ProductController.java
- CategoryEntity.java
- CategoryRepository.java
- CategoryService.java
- CategoryController.java
- CategoryMapper.java
- CategoryRequestDto.java / CategoryResponseDto.java
- MovementType.java
- StockMovementEntity.java
- StockMovementRepository.java
- StockMovementService.java
- StockMovementController.java
- StockMovementMapper.java
- StockMovementRequestDto.java / StockMovementResponseDto.java

### Archivos modificados:
- ProductEntity.java (campos adicionales y relación con CategoryEntity)
- ProductRequestDto.java / ProductResponseDto.java (validaciones y campos)
- ProductRepository.java (métodos de búsqueda y paginación)
- IProduct.java (nuevos métodos)
- SecurityConfig.java (permisos actualizados)
```
