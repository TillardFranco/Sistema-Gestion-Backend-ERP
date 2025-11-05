# 📊 Estado Actual del Proyecto Sistema-Gestion-Backend-(ERP)

## 🎯 Resumen Ejecutivo

**ERP** es un sistema de gestión backend desarrollado en **Spring Boot 3.4.4** con **Java 17**, similar a plataformas como Farmacity.

**Módulos implementados:**

- ✅ Gestión de usuarios y autenticación JWT
- ✅ Gestión de productos y categorías
- ✅ Control de stock e inventario
- ✅ Gestión de clientes
- ✅ Sistema de ventas completo
- ✅ Sistema de reservas con expiración automática
- ✅ Sistema de alertas de productos próximos a vencer
- ✅ Sistema de roles granulares con permisos específicos
- ✅ Sistema de auditoría y logs completo con integración automática en servicios principales

El sistema permite registrar productos, categorizarlos, controlar su stock, gestionar clientes, procesar ventas con actualización automática de inventario, manejar reservas de productos con expiración automática y conversión en ventas, recibir alertas automáticas cuando productos están próximos a vencer, gestionar usuarios con roles granulares que permiten control fino de permisos por funcionalidad, y registrar automáticamente todas las acciones críticas del sistema (productos, ventas, clientes, reservas) para auditoría y trazabilidad completa.

---

## ✅ Fase 1 - COMPLETADA

### 1.1 Gestión de Productos ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ CRUD completo de productos
- ✅ Búsqueda por código de barras
- ✅ Búsqueda por nombre (con y sin paginación)
- ✅ Paginación en listados
- ✅ Soft delete (marcar productos como inactivos)
- ✅ Actualización individual de stock
- ✅ Detección de productos con stock bajo
- ✅ Validaciones de negocio (precio positivo, stock no negativo)

**Endpoints disponibles:**

- `GET /api/v1/products` - Listado con paginación
- `GET /api/v1/products/all` - Listado sin paginación
- `GET /api/v1/products/{barcode}` - Buscar por código
- `GET /api/v1/products/search` - Búsqueda con paginación
- `GET /api/v1/products/search/all` - Búsqueda sin paginación
- `POST /api/v1/products` - Crear producto
- `PUT /api/v1/products/{barcode}` - Actualizar producto
- `PATCH /api/v1/products/{barcode}/stock` - Actualizar stock
- `DELETE /api/v1/products/{barcode}` - Soft delete
- `GET /api/v1/products/low-stock` - Productos con stock bajo

**Entidad ProductEntity:**

- Campos: `id`, `barcode`, `name`, `description`, `price`, `imageUrl`, `stock`, `minimumStock`, `active`, `creationDate`, `lastModifiedDate`, `expirationDate`
- Relación ManyToOne con CategoryEntity

---

### 1.2 Sistema de Categorías ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ CRUD completo de categorías
- ✅ Validación de nombres únicos
- ✅ Soft delete

**Endpoints disponibles:**

- `GET /api/v1/categories` - Listar todas
- `GET /api/v1/categories/{id}` - Obtener por ID
- `POST /api/v1/categories` - Crear categoría
- `PUT /api/v1/categories/{id}` - Actualizar categoría
- `DELETE /api/v1/categories/{id}` - Soft delete

**Entidad CategoryEntity:**

- Campos: `id`, `name`, `description`, `active`, `creationDate`, `lastModifiedDate`
- Relación OneToMany con ProductEntity

---

### 1.3 Control de Stock ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ Registro de ingresos de stock (IN)
- ✅ Registro de egresos de stock (OUT)
- ✅ Ajustes de inventario (ADJUSTMENT)
- ✅ Devoluciones (RETURN)
- ✅ Actualización automática de stock en productos
- ✅ Validación de stock disponible antes de egresos
- ✅ Historial de movimientos por producto
- ✅ Historial general de movimientos

**Endpoints disponibles:**

- `POST /api/v1/stock/entry` - Registrar ingreso
- `POST /api/v1/stock/exit` - Registrar egreso
- `GET /api/v1/stock/movements/{productId}` - Historial por producto
- `GET /api/v1/stock/movements` - Historial general
- `GET /api/v1/stock/low-stock` - Productos con stock bajo

**Entidad StockMovementEntity:**

- Campos: `id`, `product`, `type`, `quantity`, `reason`, `user`, `date`, `notes`
- Relaciones con ProductEntity y UserEntity

**Enum MovementType:**

- `IN` - Ingreso de stock
- `OUT` - Egreso de stock
- `ADJUSTMENT` - Ajuste de inventario
- `RETURN` - Devolución

---

### Sistema Base ✅

**Ya implementado previamente:**

- ✅ Autenticación JWT (login/register)
- ✅ Sistema de usuarios con roles (ADMIN, USER)
- ✅ CRUD completo de usuarios
- ✅ Seguridad configurada con Spring Security
- ✅ Mappers con MapStruct
- ✅ Manejo de excepciones global
- ✅ Base de datos MySQL configurada

---

## ✅ Fase 2 - COMPLETADA

### 2.1 Gestión de Clientes ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ CRUD completo de clientes
- ✅ Búsqueda por DNI (único)
- ✅ Búsqueda por nombre y apellido (con y sin paginación)
- ✅ Búsqueda por email (único)
- ✅ Paginación en listados
- ✅ Soft delete (marcar clientes como inactivos)
- ✅ Validaciones de negocio:
  - DNI único obligatorio
  - Email único (opcional pero si se proporciona debe ser único)
  - Validación de formato de email
  - Validación de tamaños máximos de campos

**Endpoints disponibles:**

- `GET /api/v1/customers` - Listado con paginación
- `GET /api/v1/customers/all` - Listado sin paginación
- `GET /api/v1/customers/{dni}` - Buscar por DNI
- `GET /api/v1/customers/search?name={term}` - Búsqueda por nombre con paginación
- `GET /api/v1/customers/search/all?name={term}` - Búsqueda por nombre sin paginación
- `GET /api/v1/customers/search/email?email={term}` - Búsqueda por email con paginación
- `GET /api/v1/customers/search/email/all?email={term}` - Búsqueda por email sin paginación
- `POST /api/v1/customers` - Crear cliente
- `PUT /api/v1/customers/{id}` - Actualizar cliente
- `DELETE /api/v1/customers/{id}` - Soft delete

**Entidad CustomerEntity:**

- Campos: `id`, `dni` (unique, not null), `name`, `lastname`, `email` (unique, opcional), `phone`, `address`, `city`, `active`, `creationDate`
- Sin relaciones con otras entidades (independiente)

**Repositorio CustomerRepository:**

- Extiende `PagingAndSortingRepository` y `CrudRepository`
- Métodos: `findByDni`, `existsByDni`, `existsByEmail`, `existsByEmailAndIdNot`, `findByActiveTrue`, búsquedas por nombre y email

---

### 2.2 Sistema de Ventas ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ Creación de ventas con múltiples items
- ✅ Validación de stock disponible antes de vender
- ✅ Cálculo automático de subtotales, impuestos (IVA 21%) y totales
- ✅ Actualización automática de stock al completar venta
- ✅ Reversión de stock al cancelar venta
- ✅ Generación de número de venta único (formato: S-XXXXXXXX)
- ✅ Paginación en listados de ventas
- ✅ Filtros por estado de venta (PENDING, COMPLETED, CANCELLED)
- ✅ Búsqueda por número de venta
- ✅ Reporte por rango de fechas
- ✅ Cancelación de ventas con validaciones

**Endpoints disponibles:**

- `POST /api/v1/sales` - Crear venta
- `GET /api/v1/sales` - Listar ventas con paginación
- `GET /api/v1/sales/status/{status}` - Filtrar ventas por estado
- `GET /api/v1/sales/search/by-sale-number/{saleNumber}` - Buscar por número de venta
- `GET /api/v1/sales/reports/by-date-range?start={date}&end={date}` - Reporte por rango de fechas
- `PATCH /api/v1/sales/{id}/cancel` - Cancelar venta

**Entidades:**

**SaleEntity:**

- Campos: `id`, `saleNumber` (unique, auto-generado), `customer` (ManyToOne), `user` (ManyToOne), `date`, `subtotal`, `tax` (IVA 21%), `total`, `paymentMethod`, `status`, `items` (OneToMany)
- Relaciones: CustomerEntity, UserEntity, List<SaleItemEntity>

**SaleItemEntity:**

- Campos: `id`, `sale` (ManyToOne), `product` (ManyToOne), `quantity`, `unitPrice`, `subtotal`
- Relaciones: SaleEntity, ProductEntity

**Enums:**

**PaymentMethod:**

- `CASH` - Efectivo
- `CARD` - Tarjeta
- `TRANSFER` - Transferencia

**SaleStatus:**

- `PENDING` - Pendiente
- `COMPLETED` - Completada
- `CANCELLED` - Cancelada

**Repositorios:**

**SaleRepository:**

- Extiende `PagingAndSortingRepository` y `CrudRepository`
- Métodos: `findBySaleNumber`, `findByStatus`, `findByDateBetween`

**SaleItemRepository:**

- Extiende `CrudRepository`
- Métodos: `findBySale`

**Lógica de Negocio (SaleService):**

1. **Creación de venta:**

   - Valida que existan items en la venta
   - Valida que el cliente exista
   - Valida que el usuario (vendedor) exista
   - Valida stock disponible para cada producto
   - Calcula subtotales de cada item
   - Calcula subtotal general
   - Calcula impuestos (IVA 21%)
   - Calcula total final
   - Genera número de venta único
   - Guarda la venta con status COMPLETED
   - Crea los items de venta
   - Actualiza stock de cada producto (reduce stock)
   - Retorna venta completa con items

2. **Cancelación de venta:**

   - Valida que la venta exista
   - Valida que no esté ya cancelada
   - Recupera todos los items de la venta
   - Revierte el stock de cada producto (aumenta stock)
   - Marca la venta como CANCELLED
   - Retorna venta actualizada

3. **Búsquedas y filtros:**
   - Listado paginado de todas las ventas
   - Filtrado por estado con paginación
   - Búsqueda por número de venta
   - Reporte por rango de fechas con paginación

---

## ✅ Fase 3 - COMPLETADA

### 3.1 Sistema de Reservas ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ Creación de reservas con validación de stock
- ✅ Reserva automática de stock (descuenta stock al crear reserva)
- ✅ Expiración automática de reservas (7 días, ejecutado cada hora)
- ✅ Conversión de reserva en venta
- ✅ Cancelación de reservas con liberación de stock
- ✅ Confirmación de reservas
- ✅ Búsqueda por número de reserva
- ✅ Filtrado por estado
- ✅ Paginación en listados

**Endpoints disponibles:**

- `POST /api/v1/reservations` - Crear reserva
- `GET /api/v1/reservations` - Listar reservas con paginación
- `GET /api/v1/reservations/status/{status}` - Filtrar por estado
- `GET /api/v1/reservations/{id}` - Obtener reserva por ID
- `GET /api/v1/reservations/search/by-reservation-number/{number}` - Buscar por número
- `PATCH /api/v1/reservations/{id}/confirm` - Confirmar reserva
- `PATCH /api/v1/reservations/{id}/complete` - Completar reserva (convertir en venta)
- `PATCH /api/v1/reservations/{id}/cancel` - Cancelar reserva
- `DELETE /api/v1/reservations/expired` - Expirar reservas manualmente (job programado)

**Entidad ReservationEntity:**

- Campos: `id`, `reservationNumber` (unique, auto-generado), `customer` (ManyToOne), `product` (ManyToOne), `quantity`, `status`, `reservationDate`, `expirationDate` (7 días desde creación), `notes`
- Relaciones: CustomerEntity, ProductEntity

**Enum ReservationStatus:**

- `PENDING` - Pendiente
- `CONFIRMED` - Confirmada
- `COMPLETED` - Completada (convertida en venta)
- `CANCELLED` - Cancelada
- `EXPIRED` - Expirada

**Repositorio ReservationRepository:**

- Extiende `PagingAndSortingRepository` y `CrudRepository`
- Métodos: `findByReservationNumber`, `findByStatus`, `findByCustomerId`, `findByProductId`, `findByExpirationDateBeforeAndStatus`

**Lógica de Negocio (ReservationService):**

1. **Creación de reserva:**

   - Valida que el cliente exista
   - Valida que el producto exista
   - Valida cantidad mayor a cero
   - Valida stock disponible suficiente
   - **Reserva stock automáticamente** (descuenta del stock disponible del producto)
   - Calcula fecha de expiración (7 días desde la creación)
   - Genera número de reserva único (formato: R-XXXXXXXX)
   - Crea la reserva con status PENDING
   - Retorna reserva completa

2. **Cancelación de reserva:**

   - Valida que la reserva exista
   - Valida que no esté completada o cancelada
   - **Libera stock reservado** (devuelve stock al producto)
   - Marca la reserva como CANCELLED
   - Retorna reserva actualizada

3. **Confirmación de reserva:**

   - Valida que la reserva esté en estado PENDING
   - Valida que no haya expirado
   - Cambia status a CONFIRMED
   - Retorna reserva actualizada

4. **Completar reserva (convertir en venta):**

   - Valida que la reserva exista y no esté completada/cancelada/expirada
   - Restaura temporalmente el stock del producto reservado
   - Crea la venta usando SaleService (que validará y descontará el stock nuevamente)
   - Marca la reserva como COMPLETED
   - Retorna reserva actualizada

5. **Expiración automática:**
   - Job programado ejecutado cada hora (`@Scheduled(cron = "0 0 * * * ?")`)
   - Busca reservas PENDING o CONFIRMED que hayan pasado su fecha de expiración
   - **Libera stock de todas las reservas expiradas** (devuelve stock a productos)
   - Marca reservas como EXPIRED
   - Logs de ejecución para auditoría

**Scheduler (ReservationScheduler):**

- Componente Spring con `@Scheduled`
- Ejecuta `expireReservations()` cada hora automáticamente
- Manejo de errores con logging
- Habilitado con `@EnableScheduling` en `FarmaserApplication`

**Flujo de Trabajo de Reservas:**

1. **Cliente reserva producto:**

   - Stock se descuenta inmediatamente
   - Reserva válida por 7 días
   - Status: PENDING

2. **Confirmar reserva (opcional):**

   - Status cambia a CONFIRMED
   - Stock sigue reservado

3. **Completar reserva (convertir en venta):**

   - Se crea la venta normal
   - Stock que ya estaba descontado se maneja correctamente
   - Status: COMPLETED

4. **Cancelar o expirar:**
   - Stock se libera y vuelve al producto
   - Status: CANCELLED o EXPIRED

---

## ✅ Sistema de Alertas de Vencimiento - COMPLETADO

### Funcionalidad: Alertas de Productos Próximos a Vencer ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ Detección automática de productos próximos a vencer:
  - ✅ Alertas con 1 mes de anticipación (28-31 días antes)
  - ✅ Alertas con 1 semana de anticipación (5-7 días antes)
  - ✅ Alertas el mismo día de vencimiento (0 días)
- ✅ Generación automática de alertas diaria (cada día a las 8:00 AM)
- ✅ Creación de alertas para todos los usuarios del sistema
- ✅ Prevención de duplicados (no crea alertas si ya existe una no leída del mismo tipo)
- ✅ Sistema de lectura de alertas (marcar como leída, marcar todas como leídas)
- ✅ Paginación en listados de alertas
- ✅ Filtrado de alertas no leídas

**Endpoints disponibles:**

- `GET /api/v1/alerts` - Listar alertas del usuario (paginado)
- `GET /api/v1/alerts/all` - Listar todas las alertas del usuario
- `GET /api/v1/alerts/unread` - Listar alertas no leídas (paginado)
- `GET /api/v1/alerts/unread/all` - Listar todas las alertas no leídas
- `PATCH /api/v1/alerts/{id}/read` - Marcar alerta como leída
- `PATCH /api/v1/alerts/read-all` - Marcar todas las alertas como leídas

**Entidad AlertEntity:**

- Campos: `id`, `type` (AlertType enum), `message`, `read` (boolean), `user` (ManyToOne), `product` (ManyToOne), `date`, `expirationDate`
- Relaciones: UserEntity, ProductEntity

**Enum AlertType:**

- `LOW_STOCK` - Stock bajo (preparado para futuras implementaciones)
- `EXPIRING_PRODUCT_1_MONTH` - Producto vence en 1 mes
- `EXPIRING_PRODUCT_1_WEEK` - Producto vence en 1 semana
- `EXPIRING_PRODUCT_TODAY` - Producto vence hoy
- `RESERVATION_EXPIRING` - Reserva próxima a expirar (preparado para futuras implementaciones)

**Repositorio AlertRepository:**

- Extiende `PagingAndSortingRepository` y `CrudRepository`
- Métodos: `findByUserId`, `findByUserIdAndReadFalse`, `findByTypeAndReadFalse`, `existsByProductIdAndTypeAndReadFalse`

**Lógica de Negocio (AlertService):**

1. **Generación de alertas de vencimiento:**

   - Normaliza fechas a medianoche para comparaciones precisas
   - Calcula días hasta vencimiento para cada producto activo con fecha de vencimiento
   - **Alertas de 1 mes:** Detecta productos que vencen en 28-31 días
   - **Alertas de 1 semana:** Detecta productos que vencen en 5-7 días
   - **Alertas de hoy:** Detecta productos que vencen el mismo día (0 días)
   - Verifica que no exista una alerta no leída del mismo tipo para evitar duplicados
   - Crea alertas para todos los usuarios del sistema
   - Mensajes descriptivos con información del producto y fecha de vencimiento

2. **Gestión de alertas:**
   - Listado de alertas del usuario con paginación
   - Filtrado de alertas no leídas
   - Marcado individual de alertas como leídas
   - Marcado masivo de todas las alertas como leídas

**Scheduler (ProductExpirationScheduler):**

- Componente Spring con `@Scheduled`
- Ejecuta `generateExpirationAlerts()` cada día a las 8:00 AM
- Cron: `"0 0 8 * * ?"` (segundo, minuto, hora, día del mes, mes, día de la semana)
- Manejo de errores con logging detallado
- Logs informan cantidad de alertas creadas

**Ejemplo de Mensajes de Alerta:**

- **1 mes:** `"⚠️ PRODUCTO VENCE EN 1 MES: Paracetamol 500mg (Vence: 15/02/2025 - Faltan 30 días)"`
- **1 semana:** `"⚠️ PRODUCTO VENCE EN 1 SEMANA: Ibuprofeno 400mg (Vence: 20/01/2025 - Faltan 7 días)"`
- **Hoy:** `"🚨 PRODUCTO VENCE HOY: Aspirina 100mg (Vence: 13/01/2025)"`

**Características Importantes:**

- **Prevención de duplicados:** Solo crea una alerta no leída por producto y tipo
- **Distribución universal:** Crea alertas para todos los usuarios (vendedores y dueños)
- **Alerta temprana:** Permite planificar acciones antes de que los productos venzan
- **Priorización visual:** Emojis diferentes (⚠️ para anticipación, 🚨 para urgencia)
- **Información completa:** Incluye nombre del producto, fecha de vencimiento y días restantes

---

## ✅ Fase 5.1 - Sistema de Roles Granulares - COMPLETADA

### Funcionalidad: Roles y Permisos Genéricos ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ Sistema de 5 roles genéricos (SUPER_ADMIN, MANAGER, CASHIER, WAREHOUSE, VIEWER)
- ✅ Permisos granulares por funcionalidad y operación
- ✅ Control de acceso con `@PreAuthorize` en todos los controladores
- ✅ Configuración de seguridad por roles en `SecurityConfig`
- ✅ Migración automática de usuarios existentes (ADMIN→SUPER_ADMIN, USER→CASHIER)
- ✅ Roles legacy (USER, ADMIN) mantenidos para compatibilidad

**Roles implementados:**

1. **SUPER_ADMIN** - Configuración del sistema

   - Gestión de usuarios y roles
   - Acceso completo a todo
   - Solo 1-2 usuarios deberían tenerlo

2. **MANAGER** - Administración diaria del negocio

   - Ver reportes y analytics
   - Crear/editar/eliminar productos
   - Gestionar categorías
   - Ver todas las ventas y cancelar ventas
   - Gestionar clientes y reservas
   - Ver alertas
   - No puede gestionar usuarios

3. **CASHIER** - Operaciones de venta al público

   - Ver productos (solo lectura)
   - Crear ventas
   - Ver sus propias ventas
   - Crear/editar clientes
   - Crear reservas
   - Ver alertas propias
   - Cancelar sus propias ventas
   - No puede editar productos ni gestionar stock

4. **WAREHOUSE** - Gestión de inventario y stock

   - Ver productos
   - Crear/editar productos
   - Gestionar stock (entradas, salidas, ajustes)
   - Ver movimientos de stock
   - Ver alertas de stock bajo
   - Gestionar categorías
   - No puede crear ventas ni gestionar clientes

5. **VIEWER** - Solo lectura (consultas)
   - Ver productos, ventas, clientes, reservas (solo lectura)
   - Ver reportes básicos (solo lectura)
   - Ver alertas
   - No puede crear, editar ni eliminar nada

**Matriz de Permisos Implementada:**

| Funcionalidad                 | SUPER_ADMIN | MANAGER  | CASHIER         | WAREHOUSE      | VIEWER         |
| ----------------------------- | ----------- | -------- | --------------- | -------------- | -------------- |
| Usuarios y Roles              | ✅ Total    | ❌ No    | ❌ No           | ❌ No          | ❌ No          |
| Productos - Ver               | ✅ Sí       | ✅ Sí    | ✅ Sí           | ✅ Sí          | ✅ Sí          |
| Productos - Crear/Editar      | ✅ Sí       | ✅ Sí    | ❌ No           | ✅ Sí          | ❌ No          |
| Productos - Eliminar          | ✅ Sí       | ✅ Sí    | ❌ No           | ❌ No          | ❌ No          |
| Categorías - Ver              | ✅ Sí       | ✅ Sí    | ✅ Sí           | ✅ Sí          | ✅ Sí          |
| Categorías - Gestionar        | ✅ Sí       | ✅ Sí    | ❌ No           | ✅ Sí          | ❌ No          |
| Stock - Ver                   | ✅ Sí       | ✅ Sí    | ✅ Sí           | ✅ Sí          | ✅ Sí          |
| Stock - Gestionar             | ✅ Sí       | ✅ Sí    | ❌ No           | ✅ Sí          | ❌ No          |
| Ventas - Crear                | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ❌ No          |
| Ventas - Ver todas            | ✅ Sí       | ✅ Sí    | ❌ Solo propias | ✅ Sí          | ✅ Sí          |
| Ventas - Cancelar             | ✅ Sí       | ✅ Sí    | ✅ Solo propias | ❌ No          | ❌ No          |
| Clientes - Ver                | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ✅ Sí          |
| Clientes - Crear/Editar       | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ❌ No          |
| Reservas - Crear              | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ❌ No          |
| Reservas - Ver                | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ✅ Sí          |
| Reservas - Confirmar          | ✅ Sí       | ✅ Sí    | ❌ No           | ❌ No          | ❌ No          |
| Reservas - Completar/Cancelar | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ❌ No          |
| Alertas                       | ✅ Total    | ✅ Total | ✅ Ver propias  | ✅ Ver propias | ✅ Ver propias |
| Reportes                      | ✅ Total    | ✅ Total | ❌ No           | ✅ Stock       | ✅ Básicos     |

**Configuración de Seguridad:**

- `SecurityConfig` actualizado con `@EnableMethodSecurity` (reemplazando el deprecado `@EnableGlobalMethodSecurity`)
- Endpoints `/api/v1/users/**` y `/api/v1/admin/**` restringidos a SUPER_ADMIN
- Endpoints de ventas y clientes accesibles para SUPER_ADMIN, MANAGER, CASHIER
- Endpoints de productos y stock accesibles para SUPER_ADMIN, MANAGER, WAREHOUSE
- Permisos específicos por método con `@PreAuthorize` en todos los controladores

**Controladores Actualizados:**

- ✅ `ProductController` - Permisos por operación (ver/crear/editar/eliminar)
- ✅ `SaleController` - Permisos diferenciados para crear/ver/cancelar
- ✅ `CustomerController` - Permisos para ver/crear/editar/eliminar
- ✅ `StockMovementController` - Permisos para gestionar stock
- ✅ `ReservationController` - Permisos para crear/ver/confirmar/completar/cancelar
- ✅ `CategoryController` - Permisos para ver/gestionar
- ✅ `AdminController` - Solo SUPER_ADMIN
- ✅ `UserController` - Solo SUPER_ADMIN
- ✅ `AlertController` - Todos los usuarios autenticados

**Migración de Datos:**

- `DataInitializer` crea automáticamente todos los roles nuevos
- Migra usuarios ADMIN existentes a SUPER_ADMIN
- Migra usuarios USER existentes a CASHIER
- Usuario admin por defecto (`admin@farmaser.com`) ahora tiene rol SUPER_ADMIN

**Ventajas del Sistema:**

- ✅ Genérico: Funciona para farmacias, tiendas, supermercados, etc.
- ✅ Escalable: Fácil agregar más roles en el futuro
- ✅ Seguro: Control fino de permisos por funcionalidad
- ✅ Flexible: Cada negocio asigna roles según su necesidad
- ✅ Profesional: Estándar en software comercial

---

## ✅ Fase 5.2 - Sistema de Auditoría y Logs - COMPLETADA

### Funcionalidad: Registro de Auditoría Completo ✅

**Estado:** Completo e implementado

**Funcionalidades implementadas:**

- ✅ Sistema completo de registro de auditoría
- ✅ Registro de acciones: CREATE, UPDATE, DELETE, LOGIN, LOGOUT, CANCEL, COMPLETE, CONFIRM, EXPIRE
- ✅ Almacenamiento de valores anteriores y nuevos (JSON)
- ✅ Registro de IP address del usuario
- ✅ Consultas avanzadas por múltiples filtros
- ✅ Historial completo de entidades
- ✅ Permisos: Solo SUPER_ADMIN y MANAGER pueden consultar logs
- ✅ Optimización con índices en base de datos

**Entidad AuditLogEntity:**

- Campos: `id`, `entityType` (String), `entityId` (Long), `action` (ActionType enum), `user` (ManyToOne), `oldValue` (TEXT), `newValue` (TEXT), `description` (TEXT), `ipAddress`, `timestamp`
- Relación ManyToOne con UserEntity
- Índices optimizados para búsquedas por: entityType+entityId, userId, action, timestamp

**Enum ActionType:**

- `CREATE` - Crear entidad
- `UPDATE` - Actualizar entidad
- `DELETE` - Eliminar entidad (soft delete)
- `LOGIN` - Inicio de sesión
- `LOGOUT` - Cierre de sesión
- `CANCEL` - Cancelar operación
- `COMPLETE` - Completar operación
- `CONFIRM` - Confirmar operación
- `EXPIRE` - Expirar (reservas, etc.)

**Repositorio AuditLogRepository:**

- Extiende `PagingAndSortingRepository` y `CrudRepository`
- Métodos: `findByEntityType`, `findByEntityTypeAndEntityId`, `findByUserId`, `findByAction`, `findByTimestampBetween`, `findByEntityTypeAndTimestampBetween`, `findByUserIdAndTimestampBetween`, `findByEntityTypeAndEntityIdOrderByTimestampDesc`

**Endpoints disponibles:**

- `GET /api/v1/audit` - Listar todos los logs (paginado)
- `GET /api/v1/audit/entity-type/{entityType}` - Logs por tipo de entidad
- `GET /api/v1/audit/entity/{entityType}/{entityId}` - Logs de una entidad específica
- `GET /api/v1/audit/entity/{entityType}/{entityId}/history` - Historial completo de una entidad
- `GET /api/v1/audit/user/{userId}` - Logs de un usuario
- `GET /api/v1/audit/action/{action}` - Logs por tipo de acción
- `GET /api/v1/audit/date-range?start={date}&end={date}` - Logs por rango de fechas
- `GET /api/v1/audit/entity-type/{entityType}/date-range` - Combinación tipo + fechas
- `GET /api/v1/audit/user/{userId}/date-range` - Combinación usuario + fechas

**Lógica de Negocio (AuditService):**

1. **Registro de auditoría:**

   - Recibe tipo de entidad, ID, acción, usuario, valores anteriores/nuevos, descripción, IP
   - Valida que el usuario exista
   - Almacena valores como JSON strings para facilitar comparación
   - Registra timestamp automáticamente
   - Manejo de errores sin interrumpir flujo principal

2. **Consultas de auditoría:**
   - Listado paginado de todos los logs
   - Filtrado por tipo de entidad
   - Filtrado por entidad específica (tipo + ID)
   - Filtrado por usuario
   - Filtrado por acción
   - Filtrado por rango de fechas
   - Combinaciones de filtros (tipo + fechas, usuario + fechas)
   - Historial completo ordenado por fecha descendente

**AuditHelper (Componente Helper):**

- Clase helper para facilitar el registro desde servicios y controladores
- Métodos:
  - `log()` - Registra auditoría con HttpServletRequest (obtiene IP automáticamente)
  - `log()` - Registra auditoría sin request
  - `toJsonString()` - Convierte objetos a JSON string
  - `createChangeDescription()` - Crea descripción resumida de cambios

**Configuración de Seguridad:**

- Endpoints `/api/v1/audit/**` restringidos a SUPER_ADMIN y MANAGER
- Todos los endpoints requieren autenticación

**Integración:**

- ✅ **Integración completa en servicios principales:**
  - ✅ **ProductService:** Registra CREATE, UPDATE, DELETE de productos
  - ✅ **SaleService:** Registra CREATE y CANCEL de ventas
  - ✅ **CustomerService:** Registra CREATE, UPDATE, DELETE de clientes
  - ✅ **ReservationService:** Registra CREATE, CONFIRM, COMPLETE, CANCEL y EXPIRE de reservas
- ✅ Todos los servicios usan `AuditHelper` para registro automático
- ✅ Obtiene automáticamente el usuario desde SecurityContextHolder
- ✅ Guarda valores anteriores y nuevos (JSON) para trazabilidad completa
- ✅ No interrumpe el flujo principal si falla la auditoría (try-catch interno)
- Ver `AUDITORIA_EJEMPLO.md` para ejemplos de uso adicionales
- No requiere cambios en entidades existentes (opcional agregar campos createdBy/modifiedBy)

**Características Importantes:**

- **No intrusivo:** El registro de auditoría no bloquea operaciones principales
- **Flexible:** Permite registrar cualquier tipo de entidad y acción
- **Trazabilidad completa:** Registra quién, qué, cuándo y desde dónde
- **Optimizado:** Índices en BD para consultas rápidas
- **Escalable:** Paginación en todas las consultas
- **Seguro:** Solo roles autorizados pueden consultar logs

**Ejemplo de Uso (Ya implementado en servicios):**

```java
// En ProductService.save() - Ya implementado ✅
auditHelper.log("Product", savedProduct.getId(), ActionType.CREATE, null,
    auditHelper.toJsonString(savedProduct), "Producto creado: " + savedProduct.getName());

// En ProductService.update() - Ya implementado ✅
String oldValue = auditHelper.toJsonString(existingProduct);
auditHelper.log("Product", savedProduct.getId(), ActionType.UPDATE, oldValue,
    auditHelper.toJsonString(savedProduct), "Producto actualizado: " + savedProduct.getName());
```

**Nota:** Los servicios ProductService, SaleService, CustomerService y ReservationService ya tienen esta integración automática. El `AuditHelper` obtiene el usuario desde `SecurityContextHolder` automáticamente.

---

### FASE 4: Sistema de Compras y Proveedores (Prioridad MEDIA)

- Gestión de proveedores
- Registro de compras
- Actualización automática de stock desde compras

### FASE 5: Mejoras de Seguridad y Roles (Prioridad ALTA)

- ✅ Roles más granulares (SUPER_ADMIN, MANAGER, CASHIER, WAREHOUSE, VIEWER) - **COMPLETADO**
- ✅ Sistema de auditoría - **COMPLETADO**

### ✅ FASE 6: Reportes y Analytics (Primera entrega) – COMPLETADA

**Nuevos Endpoints (requieren SUPER_ADMIN o MANAGER):**

- `GET /api/v1/reports/sales/summary?start={ISO_DATE}&end={ISO_DATE}`
  - Resumen de ventas: total de ventas, items vendidos, subtotal, IVA, total
- `GET /api/v1/reports/sales/top-products?start={ISO_DATE}&end={ISO_DATE}&limit=10`
  - Top productos por cantidad y facturación
- `GET /api/v1/reports/sales/by-seller?start={ISO_DATE}&end={ISO_DATE}`
  - Rendimiento por vendedor: ventas, items, facturación
- `GET /api/v1/reports/sales/daily?start={ISO_DATE}&end={ISO_DATE}`
  - Serie diaria de ventas (para gráficos)

**DTOs nuevos:**

- `SalesSummaryDto`
- `TopProductDto`
- `SellerPerformanceDto`
- `DailySalesPointDto`

**Componentes:**

- `IReport` y `ReportService` con agregaciones en memoria (sobre ventas filtradas por fecha)
- `ReportController` con seguridad por roles
- `SecurityConfig` actualizado para permitir `/api/v1/reports/**`

**Notas:**

_IMPORTANTE_

- Primera entrega enfocada en APIs; dashboard UI y exportación a PDF se abordarán luego
- Los cálculos consideran ventas en estado actual y usan los totales de cada venta

### ✅ FASE 7: Optimizaciones y Performance – COMPLETADA

#### Entrega 1: Optimizaciones de Base de Datos

**Cambios aplicados:**

- ✅ Reportes sin N+1: consultas optimizadas con `JOIN FETCH` (ventas + items)
- ✅ Índices JPA:
  - `sale`: `date`, `status`, `customer_id`, `user_id`
  - `sale_item`: `sale_id`, `product_id`
- ✅ Validación de parámetros en reportes (límite con tope 100, fechas requeridas)

**Impacto:**

- Menos queries y menor latencia en endpoints de reportes
- Mejor tiempo de respuesta en rangos de fechas amplios
- Base de datos preparada para escalar consultas analíticas

#### Entrega 2: Optimizaciones de Aplicación

**Cambios aplicados:**

- ✅ **Paginación global con límites:**
  - Clase `PaginationConstants` con límites máximos por tipo de recurso
  - Productos: máximo 100 por página
  - Clientes: máximo 100 por página
  - Ventas: máximo 50 por página
  - Validación automática en todos los controladores principales
- ✅ **Cache de categorías:**
  - Implementado con Spring Cache (`@Cacheable`, `@CacheEvict`)
  - Cache en memoria para listado y búsqueda por ID
  - Invalidación automática al crear/actualizar/eliminar categorías
  - Configuración centralizada en `CacheConfig`
- ✅ **Validaciones de fechas en reportes:**
  - Validación de rango máximo (1 año)
  - Validación de orden (start < end)
  - Validación de fechas requeridas

**Impacto:**

- Protección contra consultas masivas que pueden sobrecargar el servidor
- Mejor performance en endpoints de categorías (datos cacheados)
- Prevención de errores por rangos de fechas inválidos o excesivos
- Mejor experiencia de usuario con respuestas más rápidas

**Componentes creados:**

- `PaginationConstants.java` - Constantes y utilidades para paginación
- `CacheConfig.java` - Configuración de cache de Spring
- Actualización de `CategoryService` con anotaciones de cache
- Actualización de controladores: `ProductController`, `CustomerController`, `SaleController`, `ReportController`

### FASE 8-9: Features Avanzadas (Prioridad MEDIA/BAJA)

- Generación de PDFs
- Testing
- Features avanzadas (promociones, fidelidad, etc.)

---

## 🏗️ Arquitectura Actual

### Stack Tecnológico

- **Framework:** Spring Boot 3.4.4
- **Java:** 17
- **Base de Datos:** MySQL
- **ORM:** JPA/Hibernate
- **Seguridad:** Spring Security + JWT
- **Mappers:** MapStruct
- **Validación:** Jakarta Validation (Bean Validation)

### Estructura del Proyecto

```
src/main/java/com/example/farmaser/
├── config/
│   ├── AuditHelper.java              ✅ Fase 5.2
│   ├── DataInitializer.java
│   ├── ProductExpirationScheduler.java ✅ Alertas
│   └── ReservationScheduler.java     ✅ Fase 3
├── controller/
│   ├── AdminController.java
│   ├── AuditController.java          ✅ Fase 5.2
│   ├── CategoryController.java
│   ├── CustomerController.java      ✅ Fase 2.1
│   ├── AlertController.java         ✅ Alertas
│   ├── ProductController.java
│   ├── ReservationController.java   ✅ Fase 3
│   ├── SaleController.java          ✅ Fase 2.2
│   ├── StockMovementController.java
│   └── UserController.java
├── exceptions/
│   └── (manejo global de excepciones)
├── mapper/
│   ├── alertMapper/                  ✅ Alertas
│   │   └── AlertResponseMapper.java
│   ├── auditMapper/                  ✅ Fase 5.2
│   │   └── AuditLogResponseMapper.java
│   ├── categoryMapper/
│   ├── customerMapper/               ✅ Fase 2.1
│   │   ├── CustomerRequestMapper.java
│   │   ├── CustomerResponseMapper.java
│   │   └── CustomerListMapper.java
│   ├── productMapper/
│   ├── reservationMapper/            ✅ Fase 3
│   │   ├── ReservationRequestMapper.java
│   │   └── ReservationResponseMapper.java
│   ├── saleMapper/                   ✅ Fase 2.2
│   │   ├── SaleItemRequestMapper.java
│   │   ├── SaleItemResponseMapper.java
│   │   └── SaleResponseMapper.java
│   ├── stockMapper/
│   └── userMapper/
├── model/
│   ├── dto/
│   │   ├── alertDto/                 ✅ Alertas
│   │   │   └── AlertResponseDto.java
│   │   ├── auditDto/                ✅ Fase 5.2
│   │   │   └── AuditLogResponseDto.java
│   │   ├── categoryDto/
│   │   ├── customerDto/              ✅ Fase 2.1
│   │   │   ├── CustomerRequestDto.java
│   │   │   └── CustomerResponseDto.java
│   │   ├── productDto/
│   │   ├── reservationDto/           ✅ Fase 3
│   │   │   ├── ReservationRequestDto.java
│   │   │   └── ReservationResponseDto.java
│   │   ├── saleDto/                  ✅ Fase 2.2
│   │   │   ├── SaleRequestDto.java
│   │   │   ├── SaleResponseDto.java
│   │   │   ├── SaleItemRequestDto.java
│   │   │   └── SaleItemResponseDto.java
│   │   ├── stockDto/
│   │   └── userDto/
│   ├── entity/
│   │   ├── ActionType.java           ✅ Fase 5.2
│   │   ├── AlertEntity.java          ✅ Alertas
│   │   ├── AlertType.java            ✅ Alertas
│   │   ├── AuditLogEntity.java       ✅ Fase 5.2
│   │   ├── CategoryEntity.java
│   │   ├── CustomerEntity.java       ✅ Fase 2.1
│   │   ├── MovementType.java
│   │   ├── PaymentMethod.java        ✅ Fase 2.2
│   │   ├── ProductEntity.java
│   │   ├── ReservationEntity.java    ✅ Fase 3
│   │   ├── ReservationStatus.java    ✅ Fase 3
│   │   ├── RoleEntity.java
│   │   ├── SaleEntity.java           ✅ Fase 2.2
│   │   ├── SaleItemEntity.java       ✅ Fase 2.2
│   │   ├── SaleStatus.java           ✅ Fase 2.2
│   │   ├── StockMovementEntity.java
│   │   └── UserEntity.java
│   ├── payload/
│   └── repository/
│       ├── AlertRepository.java      ✅ Alertas
│       ├── AuditLogRepository.java   ✅ Fase 5.2
│       ├── CategoryRepository.java
│       ├── CustomerRepository.java   ✅ Fase 2.1
│       ├── ProductRepository.java
│       ├── ReservationRepository.java ✅ Fase 3
│       ├── RoleRepository.java
│       ├── SaleItemRepository.java    ✅ Fase 2.2
│       ├── SaleRepository.java        ✅ Fase 2.2
│       ├── StockMovementRepository.java
│       └── UserRepository.java
├── security/
│   ├── filter/
│   ├── jwt/
│   └── SecurityConfig.java (actualizado para /api/v1/customers/**, /api/v1/sales/**, /api/v1/reservations/**, /api/v1/alerts/** y /api/v1/audit/**)
└── service/
    ├── IAlert.java                    ✅ Alertas
    ├── IAudit.java                    ✅ Fase 5.2
    ├── ICustomer.java                 ✅ Fase 2.1
    ├── IProduct.java
    ├── IReservation.java              ✅ Fase 3
    ├── ISale.java                     ✅ Fase 2.2
    ├── IStockMovement.java
    ├── IUser.java
    └── impl/
        ├── AlertService.java          ✅ Alertas
        ├── AuditService.java           ✅ Fase 5.2
        ├── CategoryService.java
        ├── CustomerService.java       ✅ Fase 2.1 (con auditoría integrada)
        ├── ProductService.java       ✅ (con auditoría integrada)
        ├── ReservationService.java   ✅ Fase 3 (con auditoría integrada)
        ├── SaleService.java           ✅ Fase 2.2 (con auditoría integrada)
        ├── StockMovementService.java
        ├── UserDetailsServiceImpl.java
        └── UserService.java
```

---

## 📝 Próximos Pasos Recomendados

### Inmediatos (Sprint Actual):

1. **Testing de Fases 2 y 3 (Ventas, Clientes y Reservas)**
   - Probar todos los endpoints de clientes
   - Probar flujo completo de ventas
   - Probar flujo completo de reservas
   - Validar integración con stock
   - Validar expiración automática de reservas
   - Tiempo estimado: 3-5 días

### Corto Plazo (Próximo Sprint):

3. ✅ **Ampliar Roles (Fase 5.1)** - **COMPLETADO**

   - ✅ Roles granulares implementados: SUPER_ADMIN, MANAGER, CASHIER, WAREHOUSE, VIEWER
   - ✅ Permisos actualizados en SecurityConfig

4. ✅ **Sistema de Auditoría (Fase 5.2)** - **COMPLETADO**

   - ✅ Sistema de auditoría completo implementado
   - ✅ Integración automática en ProductService, SaleService, CustomerService, ReservationService
   - ✅ Registro automático de todas las acciones críticas

5. **Reportes Básicos (Fase 6)**
   - Reportes de ventas diarios/mensuales
   - Dashboard básico con métricas
   - Integración con logs de auditoría para mostrar actividad de usuarios

### Medio Plazo:

5. Sistema de Reservas
6. Sistema de Compras y Proveedores
7. Sistema de Alertas

---

## 🔧 Configuración Actual

**Base de Datos:**

- URL: `jdbc:mysql://localhost:3306/farmaser`
- Usuario: `root`
- DDL: `update` (auto-creación de tablas)

**JWT:**

- Expiración: 86400000 ms (24 horas)

**Admin por defecto:**

- Email: `admin@farmaser.com`
- Password: `1234`

---

## 📚 Archivos Clave para Revisar

1. **PLAN_DESARROLLO.md** - Plan completo con todas las fases
2. **COMMIT_MESSAGE.md** - Detalle de la implementación de la Fase 1
3. **FLUJO_TRABAJO.md** - Guía de trabajo con Git e IntelliJ

---

## ⚠️ Notas Importantes

1. ✅ **Fase 3 completada:** Sistema de Reservas totalmente funcional con expiración automática
2. ✅ **Fase 2 completada:** Sistema de Clientes y Ventas totalmente funcional
3. ✅ **Fase 1 completamente funcional:** Productos, Categorías y Control de Stock
4. **Flujo de Ventas:**
   - Las ventas se crean automáticamente con status COMPLETED
   - El stock se actualiza automáticamente al crear una venta
   - Al cancelar una venta, el stock se revierte automáticamente
   - El IVA está configurado en 21% (ajustable en SaleService)
5. **Flujo de Reservas:**
   - Las reservas descuentan stock automáticamente al crearse
   - Expiración automática cada hora (reservas con más de 7 días)
   - Conversión de reservas en ventas
   - Cancelación y expiración liberan stock automáticamente
6. **Validaciones implementadas:**
   - DNI y Email únicos en clientes
   - Validación de stock antes de vender o reservar
   - Validación de cantidades positivas
   - Validación de fechas de expiración
7. **Sistema de Alertas de Vencimiento:**
   - Detección automática de productos próximos a vencer (1 mes, 1 semana, hoy)
   - Generación automática de alertas diaria a las 8:00 AM
   - Alertas distribuidas a todos los usuarios del sistema
   - Prevención de duplicados
   - Sistema de lectura de alertas
8. **Sistema de Auditoría:**
   - ✅ **Integración completa en servicios principales:**
     - ProductService: CREATE, UPDATE, DELETE
     - SaleService: CREATE, CANCEL
     - CustomerService: CREATE, UPDATE, DELETE
     - ReservationService: CREATE, CONFIRM, COMPLETE, CANCEL, EXPIRE
   - Registro automático de todas las acciones críticas
   - Consultas avanzadas por múltiples filtros
   - Historial completo de entidades con valores anteriores/nuevos (JSON)
   - Solo SUPER_ADMIN y MANAGER pueden consultar logs
   - Obtiene usuario automáticamente desde SecurityContextHolder
   - No interrumpe el flujo principal si falla la auditoría
9. **Tareas programadas:**
   - Expiración automática de reservas cada hora (ReservationScheduler)
   - Generación de alertas de vencimiento cada día a las 8:00 AM (ProductExpirationScheduler)
   - Habilitado con @EnableScheduling en FarmaserApplication
10. **Consideraciones futuras:**

- Implementar pruebas unitarias e integración
- Evaluar agregar Swagger/OpenAPI para documentación de API
- Considerar migraciones de BD con Flyway/Liquibase para producción
- Generación de recibos/tickets en PDF (pendiente)

---

## 🔄 Flujo de Trabajo Típico

### Escenario: Realizar una Venta

1. **Crear/Verificar Cliente:**

   - `POST /api/v1/customers` o `GET /api/v1/customers/{dni}`

2. **Verificar Productos Disponibles:**

   - `GET /api/v1/products` o `GET /api/v1/products/{barcode}`

3. **Crear Venta:**

   - `POST /api/v1/sales` con:
     - `customerId`: ID del cliente
     - `paymentMethod`: CASH, CARD o TRANSFER
     - `items`: Array con `productId`, `quantity`, `unitPrice`

4. **La venta automáticamente:**

   - Valida stock disponible
   - Calcula subtotales y totales
   - Actualiza stock de productos
   - Genera número de venta único
   - Retorna venta completa con todos los datos

5. **Consultar Venta:**

   - `GET /api/v1/sales/search/by-sale-number/{saleNumber}`

6. **Si es necesario cancelar:**
   - `PATCH /api/v1/sales/{id}/cancel`
   - El stock se revierte automáticamente

### Escenario: Reservar un Producto

1. **Crear/Verificar Cliente:**

   - `POST /api/v1/customers` o `GET /api/v1/customers/{dni}`

2. **Verificar Producto Disponible:**

   - `GET /api/v1/products/{barcode}` o `GET /api/v1/products/search?name={name}`

3. **Crear Reserva:**

   - `POST /api/v1/reservations` con:
     - `customerId`: ID del cliente
     - `productId`: ID del producto
     - `quantity`: Cantidad a reservar
     - `notes`: Notas opcionales
   - **El stock se descuenta automáticamente al crear la reserva**
   - La reserva expira en 7 días automáticamente

4. **Confirmar Reserva (opcional):**

   - `PATCH /api/v1/reservations/{id}/confirm`
   - Cambia status de PENDING a CONFIRMED

5. **Completar Reserva (convertir en venta):**

   - `PATCH /api/v1/reservations/{id}/complete` con:
     - `paymentMethod`: CASH, CARD o TRANSFER
     - `items`: Array de items de la venta (puede incluir más productos además del reservado)
   - Se crea la venta automáticamente
   - El stock ya estaba descontado, se maneja correctamente
   - Status de reserva cambia a COMPLETED

6. **Si se cancela la reserva:**

   - `PATCH /api/v1/reservations/{id}/cancel`
   - El stock se libera automáticamente y vuelve al producto

7. **Expiración automática:**
   - Las reservas expiran automáticamente después de 7 días
   - Job programado ejecuta cada hora
   - Stock se libera automáticamente de reservas expiradas

### Escenario: Consultar Alertas de Productos Próximos a Vencer

1. **Ver alertas no leídas:**

   - `GET /api/v1/alerts/unread/all` - Ver todas las alertas no leídas
   - `GET /api/v1/alerts/unread` - Ver alertas no leídas con paginación

2. **Tipos de alertas de vencimiento:**

   - **1 mes antes:** Productos que vencen en 28-31 días
   - **1 semana antes:** Productos que vencen en 5-7 días
   - **Hoy:** Productos que vencen el mismo día

3. **Marcar alertas como leídas:**

   - `PATCH /api/v1/alerts/{id}/read` - Marcar una alerta específica
   - `PATCH /api/v1/alerts/read-all` - Marcar todas las alertas como leídas

4. **Generación automática:**
   - El sistema genera alertas automáticamente cada día a las 8:00 AM
   - Analiza todos los productos activos con fecha de vencimiento
   - Crea alertas para todos los usuarios del sistema

### Escenario: Consultar Logs de Auditoría

**Nota:** La auditoría se registra automáticamente en todas las operaciones críticas:

- ✅ Crear/Actualizar/Eliminar productos
- ✅ Crear/Cancelar ventas
- ✅ Crear/Actualizar/Eliminar clientes
- ✅ Crear/Confirmar/Completar/Cancelar/Expirar reservas

1. **Ver todos los logs:**

   - `GET /api/v1/audit?page=0&size=20` - Listar todos los logs (paginado)
   - Ordenados por fecha descendente (más recientes primero)

2. **Ver historial de una entidad específica:**

   - `GET /api/v1/audit/entity/Product/123/history` - Ver todos los cambios de un producto específico
   - `GET /api/v1/audit/entity/Sale/456/history` - Ver todos los cambios de una venta específica

3. **Filtrar por tipo de entidad:**

   - `GET /api/v1/audit/entity-type/Product` - Ver todos los logs relacionados con productos
   - `GET /api/v1/audit/entity-type/Sale` - Ver todos los logs relacionados con ventas

4. **Filtrar por usuario:**

   - `GET /api/v1/audit/user/5` - Ver todas las acciones de un usuario específico

5. **Filtrar por acción:**

   - `GET /api/v1/audit/action/CREATE` - Ver todas las creaciones
   - `GET /api/v1/audit/action/DELETE` - Ver todas las eliminaciones

6. **Filtrar por rango de fechas:**

   - `GET /api/v1/audit/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59` - Logs de un período específico

7. **Combinar filtros:**
   - `GET /api/v1/audit/entity-type/Product/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59` - Productos modificados en un período
   - `GET /api/v1/audit/user/5/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59` - Acciones de un usuario en un período

**Permisos requeridos:**

- Solo usuarios con rol `SUPER_ADMIN` o `MANAGER` pueden consultar logs de auditoría

---

**Última actualización:** Diciembre 2024
**Estado general:** ✅ Fase 1 completa | ✅ Fase 2 completa | ✅ Fase 3 completa | ✅ Sistema de Alertas completo | ✅ Fase 5 completa (Roles Granulares + Auditoría) | ⏳ Listo para Fase 4 (Compras y Proveedores) o Fase 6 (Reportes y Analytics)
