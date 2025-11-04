# 📋 Plan de Desarrollo - Sistema de Gestión de Farmacia (FarmaSer)

## 📊 Estado Actual del Proyecto

### ✅ **Ya Implementado (FASE 1 COMPLETA + FASE 5.1):**

- ✅ Autenticación JWT (login/register)
- ✅ Sistema de usuarios con roles granulares (SUPER_ADMIN, MANAGER, CASHIER, WAREHOUSE, VIEWER)
- ✅ CRUD completo de usuarios
- ✅ **CRUD completo de productos** (ProductService + ProductController)
- ✅ **Sistema de categorías completo** (CategoryEntity, Service, Controller)
- ✅ **Control de stock completo** (StockMovementEntity, Service, Controller)
- ✅ Historial de movimientos de stock
- ✅ Búsqueda y paginación de productos
- ✅ Detección de productos con stock bajo
- ✅ Seguridad configurada con Spring Security
- ✅ Mappers con MapStruct
- ✅ Manejo de excepciones global
- ✅ Base de datos MySQL configurada

### ❌ **Falta Implementar (Próximas Fases):**

- ❌ Sistema de ventas/transacciones (FASE 2.2)
- ❌ Sistema de clientes (FASE 2.1)
- ❌ Sistema de reservas (FASE 3)
- ❌ Proveedores (FASE 4)
- ❌ Sistema de compras (FASE 4)
- ❌ Notificaciones/alertas (FASE 7)
- ❌ Reportes y estadísticas (FASE 6)
- ✅ Ampliación de roles (FASE 5.1) - **COMPLETADO**
- ❌ Auditoría (FASE 5.2) - **PENDIENTE**

---

## 🎯 Fases de Desarrollo

---

## **FASE 1: Fundación del Sistema (MVP Core)**

_Prioridad: ALTA | Tiempo estimado: 2-3 semanas_

### **1.1 Completar Gestión de Productos** ⭐

**Objetivo:** Sistema completo CRUD de productos funcional

**Tareas:**

- [ ] Implementar `ProductService` con toda la lógica de negocio
- [ ] Crear `ProductController` con endpoints REST
- [ ] Agregar validaciones de negocio:
  - [ ] Validar que el stock no sea negativo
  - [ ] Validar que el precio sea positivo
  - [ ] Validar formato de código de barras (si aplica)
- [ ] Implementar búsqueda de productos:
  - [ ] Por código de barras
  - [ ] Por nombre (búsqueda parcial)
  - [ ] Por categoría (futuro)
  - [ ] Filtros combinados
- [ ] Agregar campos faltantes a `ProductEntity`:
  - [ ] `categoryId` (Foreign Key a CategoryEntity)
  - [ ] `supplierId` (Foreign Key a SupplierEntity)
  - [ ] `minimumStock` (stock mínimo antes de alerta)
  - [ ] `active` (boolean para soft delete)
  - [ ] `lastModifiedDate` (auditoría)
- [ ] Implementar paginación en listado de productos
- [ ] Crear endpoints:
  - [ ] `GET /api/v1/products` - Listar con paginación
  - [ ] `GET /api/v1/products/{barcode}` - Buscar por código
  - [ ] `GET /api/v1/products/search?name={name}` - Búsqueda
  - [ ] `POST /api/v1/products` - Crear
  - [ ] `PUT /api/v1/products/{barcode}` - Actualizar
  - [ ] `DELETE /api/v1/products/{barcode}` - Eliminar (soft delete)
  - [ ] `PATCH /api/v1/products/{barcode}/stock` - Actualizar solo stock

**Archivos a crear/modificar:**

- `ProductService.java` (implementación)
- `ProductController.java` (nuevo)
- `ProductEntity.java` (agregar campos)
- `ProductRequestDto.java` (agregar campos)
- `ProductResponseDto.java` (agregar campos)

---

### **1.2 Sistema de Categorías de Productos**

**Objetivo:** Clasificar productos por categorías (Medicamentos, Cosméticos, Higiene, etc.)

**Tareas:**

- [ ] Crear `CategoryEntity`:
  - `id`, `name`, `description`, `active`, `creationDate`
- [ ] Crear `CategoryRepository`
- [ ] Crear DTOs: `CategoryRequestDto`, `CategoryResponseDto`
- [ ] Crear Mappers: `CategoryMapper`
- [ ] Crear `ICategory` (interfaz)
- [ ] Crear `CategoryService` (implementación)
- [ ] Crear `CategoryController`
- [ ] Agregar relación ManyToOne en `ProductEntity` → `CategoryEntity`
- [ ] Endpoints:
  - [ ] `GET /api/v1/categories` - Listar categorías
  - [ ] `GET /api/v1/categories/{id}` - Obtener categoría
  - [ ] `POST /api/v1/categories` - Crear categoría
  - [ ] `PUT /api/v1/categories/{id}` - Actualizar
  - [ ] `DELETE /api/v1/categories/{id}` - Eliminar

**Archivos a crear:**

- `CategoryEntity.java`
- `CategoryRepository.java`
- `CategoryRequestDto.java`, `CategoryResponseDto.java`
- `CategoryMapper.java`
- `ICategory.java`
- `CategoryService.java`
- `CategoryController.java`

---

### **1.3 Control de Stock Básico**

**Objetivo:** Sistema para registrar movimientos de stock (ingresos/egresos)

**Tareas:**

- [ ] Crear `StockMovementEntity`:
  - `id`, `productId`, `type` (IN/OUT), `quantity`, `reason`, `userId`, `date`, `notes`
- [ ] Crear enum `MovementType` (IN, OUT, ADJUSTMENT, RETURN)
- [ ] Crear `StockMovementRepository`
- [ ] Crear DTOs: `StockMovementRequestDto`, `StockMovementResponseDto`
- [ ] Crear `IStockMovement` (interfaz)
- [ ] Crear `StockMovementService`:
  - [ ] Actualizar stock del producto automáticamente
  - [ ] Validar que no se pueda hacer egreso si no hay stock suficiente
  - [ ] Generar alerta si stock baja del mínimo
- [ ] Crear `StockMovementController`
- [ ] Endpoints:
  - [ ] `POST /api/v1/stock/entry` - Registrar ingreso
  - [ ] `POST /api/v1/stock/exit` - Registrar egreso
  - [ ] `GET /api/v1/stock/movements/{productId}` - Historial de movimientos
  - [ ] `GET /api/v1/stock/low-stock` - Productos con stock bajo

**Archivos a crear:**

- `StockMovementEntity.java`
- `MovementType.java` (enum)
- `StockMovementRepository.java`
- `StockMovementRequestDto.java`, `StockMovementResponseDto.java`
- `StockMovementMapper.java`
- `IStockMovement.java`
- `StockMovementService.java`
- `StockMovementController.java`

---

## **FASE 2: Sistema de Ventas y Clientes**

_Prioridad: ALTA | Tiempo estimado: 3-4 semanas_

### **2.1 Gestión de Clientes**

**Objetivo:** Sistema completo de clientes

**Tareas:**

- [ ] Crear `CustomerEntity`:
  - `id`, `dni` (unique), `name`, `lastname`, `email`, `phone`, `address`, `city`, `active`, `creationDate`
- [ ] Crear `CustomerRepository`
- [ ] Crear DTOs y Mappers
- [ ] Crear `ICustomer` (interfaz)
- [ ] Crear `CustomerService`
- [ ] Crear `CustomerController`
- [ ] Endpoints CRUD completos
- [ ] Búsqueda por DNI, nombre, email

**Archivos a crear:**

- `CustomerEntity.java`
- `CustomerRepository.java`
- `CustomerRequestDto.java`, `CustomerResponseDto.java`
- `CustomerMapper.java`
- `ICustomer.java`
- `CustomerService.java`
- `CustomerController.java`

---

### **2.2 Sistema de Ventas**

**Objetivo:** Procesar ventas de productos

**Tareas:**

- [ ] Crear `SaleEntity`:
  - `id`, `saleNumber` (unique, auto-generado), `customerId`, `userId`, `date`, `subtotal`, `tax`, `total`, `paymentMethod`, `status`
- [ ] Crear `SaleItemEntity`:
  - `id`, `saleId`, `productId`, `quantity`, `unitPrice`, `subtotal`
- [ ] Crear enum `PaymentMethod` (CASH, CARD, TRANSFER)
- [ ] Crear enum `SaleStatus` (PENDING, COMPLETED, CANCELLED)
- [ ] Crear `SaleRepository`, `SaleItemRepository`
- [ ] Crear DTOs:
  - `SaleRequestDto` (con lista de items)
  - `SaleResponseDto`
  - `SaleItemRequestDto`, `SaleItemResponseDto`
- [ ] Crear `ISale` (interfaz)
- [ ] Crear `SaleService`:
  - [ ] Validar stock disponible antes de vender
  - [ ] Calcular totales automáticamente
  - [ ] Actualizar stock de productos al completar venta
  - [ ] Generar número de venta único
  - [ ] Generar recibo/ticket
- [ ] Crear `SaleController`
- [ ] Endpoints:
  - [ ] `POST /api/v1/sales` - Crear venta
  - [ ] `GET /api/v1/sales` - Listar ventas (con filtros y paginación)
  - [ ] `GET /api/v1/sales/{id}` - Obtener venta con items
  - [ ] `GET /api/v1/sales/{id}/receipt` - Generar recibo PDF
  - [ ] `PATCH /api/v1/sales/{id}/cancel` - Cancelar venta
  - [ ] `GET /api/v1/sales/reports/daily` - Reporte diario
  - [ ] `GET /api/v1/sales/reports/by-date-range` - Reporte por rango

**Archivos a crear:**

- `SaleEntity.java`
- `SaleItemEntity.java`
- `PaymentMethod.java` (enum)
- `SaleStatus.java` (enum)
- `SaleRepository.java`, `SaleItemRepository.java`
- `SaleRequestDto.java`, `SaleResponseDto.java`
- `SaleItemRequestDto.java`, `SaleItemResponseDto.java`
- `SaleMapper.java`
- `ISale.java`
- `SaleService.java`
- `SaleController.java`

---

## **FASE 3: Sistema de Reservas**

_Prioridad: MEDIA | Tiempo estimado: 2 semanas_

### **3.1 Gestión de Reservas**

**Objetivo:** Permitir a clientes reservar productos

**Tareas:**

- [ ] Crear `ReservationEntity`:
  - `id`, `reservationNumber`, `customerId`, `productId`, `quantity`, `status`, `reservationDate`, `expirationDate`, `notes`
- [ ] Crear enum `ReservationStatus` (PENDING, CONFIRMED, COMPLETED, CANCELLED, EXPIRED)
- [ ] Crear `ReservationRepository`
- [ ] Crear DTOs y Mappers
- [ ] Crear `IReservation` (interfacio)
- [ ] Crear `ReservationService`:
  - [ ] Validar stock disponible al crear reserva
  - [ ] Reservar stock automáticamente (restar del disponible)
  - [ ] Expirar reservas automáticamente después de X días
  - [ ] Convertir reserva en venta
  - [ ] Cancelar reserva y liberar stock
- [ ] Crear `ReservationController`
- [ ] Crear Job programado para expirar reservas
- [ ] Endpoints:
  - [ ] `POST /api/v1/reservations` - Crear reserva
  - [ ] `GET /api/v1/reservations` - Listar reservas
  - [ ] `GET /api/v1/reservations/{id}` - Obtener reserva
  - [ ] `PATCH /api/v1/reservations/{id}/confirm` - Confirmar
  - [ ] `PATCH /api/v1/reservations/{id}/complete` - Completar (convertir en venta)
  - [ ] `PATCH /api/v1/reservations/{id}/cancel` - Cancelar
  - [ ] `DELETE /api/v1/reservations/expired` - Eliminar expiradas (job)

**Archivos a crear:**

- `ReservationEntity.java`
- `ReservationStatus.java` (enum)
- `ReservationRepository.java`
- `ReservationRequestDto.java`, `ReservationResponseDto.java`
- `ReservationMapper.java`
- `IReservation.java`
- `ReservationService.java`
- `ReservationController.java`
- `ReservationScheduler.java` (Job programado)

---

## **FASE 4: Sistema de Compras y Proveedores**

_Prioridad: MEDIA | Tiempo estimado: 2-3 semanas_

### **4.1 Gestión de Proveedores**

**Objetivo:** Administrar proveedores de productos

**Tareas:**

- [ ] Crear `SupplierEntity`:
  - `id`, `name`, `cuit` (unique), `email`, `phone`, `address`, `city`, `active`, `creationDate`
- [ ] Crear `SupplierRepository`
- [ ] Crear DTOs y Mappers
- [ ] Crear `ISupplier` (interfaz)
- [ ] Crear `SupplierService`
- [ ] Crear `SupplierController`
- [ ] Endpoints CRUD completos

**Archivos a crear:**

- `SupplierEntity.java`
- `SupplierRepository.java`
- `SupplierRequestDto.java`, `SupplierResponseDto.java`
- `SupplierMapper.java`
- `ISupplier.java`
- `SupplierService.java`
- `SupplierController.java`

---

### **4.2 Sistema de Compras**

**Objetivo:** Registrar compras de productos a proveedores

**Tareas:**

- [ ] Crear `PurchaseEntity`:
  - `id`, `purchaseNumber`, `supplierId`, `userId`, `date`, `total`, `status`, `invoiceNumber`
- [ ] Crear `PurchaseItemEntity`:
  - `id`, `purchaseId`, `productId`, `quantity`, `unitPrice`, `subtotal`
- [ ] Crear enum `PurchaseStatus` (PENDING, COMPLETED, CANCELLED)
- [ ] Crear `PurchaseRepository`, `PurchaseItemRepository`
- [ ] Crear DTOs y Mappers
- [ ] Crear `IPurchase` (interfaz)
- [ ] Crear `PurchaseService`:
  - [ ] Crear productos nuevos si no existen
  - [ ] Actualizar stock automáticamente
  - [ ] Actualizar precio de compra del producto
  - [ ] Generar número de compra único
- [ ] Crear `PurchaseController`
- [ ] Endpoints:
  - [ ] `POST /api/v1/purchases` - Crear compra
  - [ ] `GET /api/v1/purchases` - Listar compras
  - [ ] `GET /api/v1/purchases/{id}` - Obtener compra
  - [ ] `PATCH /api/v1/purchases/{id}/complete` - Completar compra
  - [ ] `GET /api/v1/purchases/reports` - Reportes de compras

**Archivos a crear:**

- `PurchaseEntity.java`
- `PurchaseItemEntity.java`
- `PurchaseStatus.java` (enum)
- `PurchaseRepository.java`, `PurchaseItemRepository.java`
- `PurchaseRequestDto.java`, `PurchaseResponseDto.java`
- `PurchaseItemRequestDto.java`, `PurchaseItemResponseDto.java`
- `PurchaseMapper.java`
- `IPurchase.java`
- `PurchaseService.java`
- `PurchaseController.java`

---

## **FASE 5: Mejoras de Seguridad y Roles**

_Prioridad: ALTA | Tiempo estimado: 1-2 semanas_

### **5.1 Ampliar Sistema de Roles** ✅ COMPLETADO

**Objetivo:** Roles más granulares genéricos (funcionan para cualquier tipo de comercio)

**Tareas:**

- [x] Extender `ERole`:
  - [x] `SUPER_ADMIN` (Configuración del sistema)
  - [x] `MANAGER` (Administración diaria)
  - [x] `CASHIER` (Operaciones de venta)
  - [x] `WAREHOUSE` (Gestión de inventario)
  - [x] `VIEWER` (Solo lectura)
  - [x] Mantener roles legacy (USER, ADMIN) para compatibilidad
- [x] Actualizar `SecurityConfig` con permisos por rol:
  - [x] SUPER_ADMIN: gestión de usuarios y acceso completo
  - [x] MANAGER: administración diaria, reportes, productos, ventas
  - [x] CASHIER: ventas, clientes, reservas (sin editar productos)
  - [x] WAREHOUSE: productos, stock, categorías (sin ventas)
  - [x] VIEWER: solo lectura en todo
- [x] Agregar anotaciones `@PreAuthorize` en todos los controladores
- [x] Actualizar `DataInitializer` con roles iniciales y migración automática
- [x] Actualizar `@EnableMethodSecurity` (reemplazando deprecado `@EnableGlobalMethodSecurity`)

**Archivos modificados:**

- `ERole.java` - Enum actualizado con 5 roles nuevos
- `SecurityConfig.java` - Permisos por rol y rutas protegidas
- `DataInitializer.java` - Creación de roles y migración de usuarios
- Todos los controladores - `@PreAuthorize` en cada endpoint según matriz de permisos

**Documentación:**

- `ROLES_Y_PERMISOS.md` - Documentación completa del sistema de roles

---

### **5.2 Auditoría y Logs**

**Objetivo:** Registrar quién hace qué y cuándo

**Tareas:**

- [ ] Agregar campos de auditoría a entidades críticas:
  - [ ] `createdBy`, `modifiedBy`, `createdDate`, `modifiedDate`
- [ ] Crear entidad `AuditLog`:
  - `id`, `entityType`, `entityId`, `action`, `userId`, `oldValue`, `newValue`, `timestamp`
- [ ] Crear servicio de auditoría
- [ ] Implementar listeners JPA para auditoría automática
- [ ] Crear endpoints para consultar logs de auditoría

---

## **FASE 6: Reportes y Analytics**

_Prioridad: MEDIA | Tiempo estimado: 2-3 semanas_

### **6.1 Reportes de Ventas**

**Tareas:**

- [ ] Reporte diario de ventas
- [ ] Reporte mensual/anual
- [ ] Ventas por vendedor
- [ ] Productos más vendidos
- [ ] Ventas por categoría
- [ ] Gráficos de tendencias

**Endpoints:**

- [ ] `GET /api/v1/reports/sales/daily`
- [ ] `GET /api/v1/reports/sales/monthly`
- [ ] `GET /api/v1/reports/sales/by-seller`
- [ ] `GET /api/v1/reports/sales/top-products`
- [ ] `GET /api/v1/reports/sales/by-category`

---

### **6.2 Reportes de Stock**

**Tareas:**

- [ ] Stock actual por categoría
- [ ] Productos próximos a vencer
- [ ] Productos con stock bajo
- [ ] Historial de movimientos
- [ ] Rotación de inventario

**Endpoints:**

- [ ] `GET /api/v1/reports/stock/current`
- [ ] `GET /api/v1/reports/stock/expiring-soon`
- [ ] `GET /api/v1/reports/stock/low-stock`
- [ ] `GET /api/v1/reports/stock/rotation`

---

### **6.3 Dashboard**

**Tareas:**

- [ ] Crear endpoint de dashboard con métricas principales:
  - [ ] Ventas del día
  - [ ] Productos con stock bajo
  - [ ] Reservas pendientes
  - [ ] Top productos vendidos
  - [ ] Ingresos del mes
- [ ] Endpoint: `GET /api/v1/dashboard`

---

## **FASE 7: Notificaciones y Alertas**

_Prioridad: MEDIA | Tiempo estimado: 1-2 semanas_

### **7.1 Sistema de Alertas**

**Tareas:**

- [ ] Crear entidad `AlertEntity`:
  - `id`, `type`, `message`, `read`, `userId`, `date`
- [ ] Crear enum `AlertType` (LOW_STOCK, EXPIRING_PRODUCT, RESERVATION_EXPIRING, etc.)
- [ ] Crear servicio de alertas
- [ ] Generar alertas automáticas:
  - [ ] Stock bajo (job programado)
  - [ ] Productos próximos a vencer (job programado)
  - [ ] Reservas próximas a expirar
- [ ] Endpoints:
  - [ ] `GET /api/v1/alerts` - Obtener alertas del usuario
  - [ ] `PATCH /api/v1/alerts/{id}/read` - Marcar como leída

---

## **FASE 8: Mejoras y Optimizaciones**

_Prioridad: BAJA | Tiempo estimado: 2-3 semanas_

### **8.1 Validaciones Avanzadas**

**Tareas:**

- [ ] Validar DNI/CUIT con algoritmo
- [ ] Validar códigos de barras
- [ ] Validar emails únicos
- [ ] Validar precios mínimos/máximos
- [ ] Validar fechas (no vencidas, etc.)

---

### **8.2 Mejoras de Performance**

**Tareas:**

- [ ] Implementar caché (Redis) para:
  - [ ] Lista de productos
  - [ ] Categorías
  - [ ] Stock actual
- [ ] Optimizar consultas con índices en BD
- [ ] Paginación en todos los listados
- [ ] Lazy loading donde corresponda

---

### **8.3 Generación de Comprobantes**

**Tareas:**

- [ ] Integrar librería para generar PDFs (iText, Apache PDFBox)
- [ ] Generar tickets de venta en PDF
- [ ] Generar facturas
- [ ] Generar remitos de compra

---

### **8.4 Testing**

**Tareas:**

- [ ] Tests unitarios para servicios
- [ ] Tests de integración para controladores
- [ ] Tests de seguridad
- [ ] Coverage mínimo 70%

---

## **FASE 9: Features Avanzadas (Futuro)**

_Prioridad: BAJA | Tiempo estimado: Variable_

### **9.1 Sistema de Descuentos y Promociones**

- [ ] Entidad `PromotionEntity`
- [ ] Aplicar descuentos a productos/categorías
- [ ] Cupones de descuento
- [ ] Promociones por cantidad

### **9.2 Plan de Fidelidad**

- [ ] Puntos por compras
- [ ] Canje de puntos por descuentos

### **9.3 Integración con APIs Externas**

- [ ] API de AFIP (Argentina)
- [ ] Consulta de precios de proveedores
- [ ] Envío de emails

### **9.4 Múltiples Sucursales**

- [ ] Entidad `BranchEntity`
- [ ] Transferencias entre sucursales
- [ ] Stock por sucursal

---

## 📦 Dependencias Adicionales Necesarias

```xml
<!-- Para jobs programados -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>

<!-- Para generación de PDFs -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
</dependency>

<!-- Para caché (opcional) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>

<!-- Para validaciones avanzadas -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```

---

## 🎯 Priorización Recomendada

### **Sprint 1-2 (MVP Mínimo):**

1. Completar CRUD de productos
2. Sistema de categorías
3. Control básico de stock
4. Sistema de clientes

### **Sprint 3-4 (Funcionalidad Core):**

1. Sistema de ventas completo
2. Ampliar roles y permisos
3. Reportes básicos

### **Sprint 5-6 (Features Avanzadas):**

1. Sistema de reservas
2. Sistema de compras y proveedores
3. Alertas y notificaciones

### **Sprint 7+ (Mejoras):**

1. Reportes avanzados
2. Optimizaciones
3. Testing
4. Features futuras

---

## 📝 Notas Importantes

1. **Base de Datos:** Considerar crear scripts de migración con Flyway o Liquibase
2. **Documentación API:** Implementar Swagger/OpenAPI
3. **Logging:** Configurar logging apropiado (Logback)
4. **Ambientes:** Separar configuraciones para dev, test, prod
5. **Códigos de Barras:** Considerar generación automática si no existe
6. **Imágenes:** Planear almacenamiento de imágenes de productos (AWS S3, local, etc.)

---

## ✅ Checklist de Inicio de Cada Fase

Antes de empezar cada fase, verificar:

- [ ] Tests de fase anterior pasando
- [ ] Documentación actualizada
- [ ] Código revisado y limpio
- [ ] Base de datos con migraciones aplicadas
- [ ] Backup de BD antes de cambios importantes
