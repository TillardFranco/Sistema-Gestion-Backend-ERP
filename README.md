# 🏥 Sistema de Gestión Backend (ERP)

Sistema de gestión backend (ERP) genérico y modular desarrollado en **Spring Boot 3.4.4** con **Java 17**. Diseñado para ser fácilmente adaptable a diferentes tipos de comercios (farmacias, tiendas, supermercados, etc.).

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Módulos Implementados](#-módulos-implementados)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [API Endpoints](#-api-endpoints)
- [Sistema de Roles](#-sistema-de-roles)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Contribuir](#-contribuir)
- [Licencia](#-licencia)

## ✨ Características

### 🔐 Seguridad y Autenticación

- Autenticación JWT (JSON Web Tokens)
- Sistema de roles granulares (5 roles: SUPER_ADMIN, MANAGER, CASHIER, WAREHOUSE, VIEWER)
- Control de acceso basado en roles (RBAC)
- Sistema completo de auditoría y logs

### 📦 Gestión de Inventario

- CRUD completo de productos con validaciones
- Sistema de categorías
- Control de stock con movimientos (ingresos, egresos, ajustes, devoluciones)
- Alertas automáticas de productos próximos a vencer
- Detección de stock bajo
- Historial completo de movimientos de stock

### 💰 Sistema de Ventas

- Procesamiento de ventas con múltiples items
- Cálculo automático de subtotales, impuestos (IVA 21%) y totales
- Actualización automática de stock al completar venta
- Reversión de stock al cancelar venta
- Generación de números de venta únicos
- Filtros por estado y búsqueda por número de venta
- Reportes de ventas por rango de fechas

### 👥 Gestión de Clientes

- CRUD completo de clientes
- Validaciones de DNI único y email único
- Búsqueda avanzada por DNI, nombre, email
- Paginación en listados

### 📅 Sistema de Reservas

- Reserva de productos con validación de stock
- Reserva automática de stock (descuenta stock al crear)
- Expiración automática de reservas (7 días, ejecutado cada hora)
- Conversión de reserva en venta
- Cancelación con liberación de stock

### 📊 Reportes y Analytics

- Reportes de ventas (resumen, top productos, por vendedor, diarios)
- Reportes de stock
- Optimizaciones con índices en base de datos
- Cache de categorías para mejor performance

### 🔍 Auditoría

- Registro automático de todas las acciones críticas
- Historial completo de cambios con valores anteriores y nuevos
- Consultas avanzadas por múltiples filtros
- Registro de IP address y timestamps

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.4.4**
  - Spring Data JPA
  - Spring Security
  - Spring Web
  - Spring Validation
- **MySQL** (Base de datos)
- **MapStruct** (Mappers)
- **Lombok** (Reducción de código boilerplate)
- **JWT** (JSON Web Tokens para autenticación)
- **Maven** (Gestión de dependencias)

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas (Layered Architecture) con separación clara de responsabilidades:

```
Controller Layer (REST APIs)
    ↓
Service Layer (Lógica de Negocio)
    ↓
Repository Layer (Acceso a Datos)
    ↓
Entity Layer (Modelo de Datos)
```

### Patrones de Diseño Utilizados

- **Repository Pattern**: Para acceso a datos
- **Service Layer Pattern**: Para lógica de negocio
- **DTO Pattern**: Para transferencia de datos
- **Mapper Pattern**: Para conversión entre entidades y DTOs
- **Singleton Pattern**: Para configuración de seguridad
- **Strategy Pattern**: Para diferentes tipos de movimientos de stock

## 📦 Módulos Implementados

### ✅ Módulos Completos

1. **Autenticación y Autorización**

   - Login/Registro de usuarios
   - Generación y validación de JWT
   - Sistema de roles granulares

2. **Gestión de Productos**

   - CRUD completo
   - Búsqueda avanzada
   - Gestión de categorías
   - Control de stock

3. **Sistema de Ventas**

   - Procesamiento de ventas
   - Gestión de items de venta
   - Cancelación de ventas
   - Reportes

4. **Gestión de Clientes**

   - CRUD completo
   - Búsqueda avanzada
   - Validaciones

5. **Sistema de Reservas**

   - Creación y gestión
   - Expiración automática
   - Conversión a ventas

6. **Sistema de Alertas**

   - Alertas de productos próximos a vencer
   - Alertas de stock bajo
   - Notificaciones automáticas

7. **Auditoría y Logs**

   - Registro de acciones
   - Historial de cambios
   - Consultas avanzadas

8. **Reportes y Analytics**
   - Reportes de ventas
   - Reportes de stock
   - Métricas y estadísticas

## 📋 Requisitos

- **Java 17** o superior
- **Maven 3.6+**
- **MySQL 8.0+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code con extensiones Java)

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/Sistema-Gestion-Backend-ERP.git
cd Sistema-Gestion-Backend-ERP
```

### 2. Crear base de datos MySQL

```sql
CREATE DATABASE farmaser CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar aplicación

1. Copiar el archivo de ejemplo:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

2. Editar `src/main/resources/application.properties` con tus credenciales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/farmaser
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 4. Compilar y ejecutar

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

O desde tu IDE, ejecuta la clase `FarmaserApplication.java`.

La aplicación estará disponible en: `http://localhost:8080`

## ⚙️ Configuración

### Usuario Administrador por Defecto

El sistema crea automáticamente un usuario administrador al iniciar:

- **Email**: `admin@farmaser.com`
- **Password**: `1234`

⚠️ **IMPORTANTE**: Cambia estas credenciales en producción.

### Configuración de JWT

```properties
jwt.secret.key=tu_clave_secreta_aqui
jwt.time.expiration=86400000  # 24 horas en milisegundos
```

### Configuración de Base de Datos

```properties
spring.jpa.hibernate.ddl-auto=update  # Para desarrollo
# Usar 'validate' o 'none' en producción
```

## 📖 Uso

### Autenticación

1. **Registro de Usuario** (requiere autenticación de SUPER_ADMIN):

```http
POST /api/v1/users/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123",
  "roles": ["CASHIER"]
}
```

2. **Login**:

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}
```

3. **Usar el token** en las siguientes peticiones:

```http
Authorization: Bearer tu_token_jwt_aqui
```

### Ejemplo: Crear un Producto

```http
POST /api/v1/products
Authorization: Bearer tu_token_jwt
Content-Type: application/json

{
  "barcode": "1234567890123",
  "name": "Paracetamol 500mg",
  "description": "Analgésico y antipirético",
  "price": 150.00,
  "stock": 100,
  "minimumStock": 20,
  "categoryId": 1,
  "expirationDate": "2025-12-31"
}
```

## 🔌 API Endpoints

### Autenticación

- `POST /api/v1/auth/login` - Iniciar sesión
- `POST /api/v1/auth/register` - Registrar usuario (SUPER_ADMIN)

### Productos

- `GET /api/v1/products` - Listar productos (paginado)
- `GET /api/v1/products/{barcode}` - Obtener por código de barras
- `GET /api/v1/products/search?name={nombre}` - Buscar por nombre
- `POST /api/v1/products` - Crear producto
- `PUT /api/v1/products/{barcode}` - Actualizar producto
- `PATCH /api/v1/products/{barcode}/stock` - Actualizar stock
- `DELETE /api/v1/products/{barcode}` - Eliminar producto (soft delete)
- `GET /api/v1/products/low-stock` - Productos con stock bajo

### Categorías

- `GET /api/v1/categories` - Listar categorías
- `GET /api/v1/categories/{id}` - Obtener categoría
- `POST /api/v1/categories` - Crear categoría
- `PUT /api/v1/categories/{id}` - Actualizar categoría
- `DELETE /api/v1/categories/{id}` - Eliminar categoría

### Clientes

- `GET /api/v1/customers` - Listar clientes (paginado)
- `GET /api/v1/customers/{dni}` - Obtener por DNI
- `GET /api/v1/customers/search?name={nombre}` - Buscar por nombre
- `POST /api/v1/customers` - Crear cliente
- `PUT /api/v1/customers/{id}` - Actualizar cliente
- `DELETE /api/v1/customers/{id}` - Eliminar cliente

### Ventas

- `POST /api/v1/sales` - Crear venta
- `GET /api/v1/sales` - Listar ventas (paginado)
- `GET /api/v1/sales/status/{status}` - Filtrar por estado
- `GET /api/v1/sales/search/by-sale-number/{saleNumber}` - Buscar por número
- `PATCH /api/v1/sales/{id}/cancel` - Cancelar venta
- `GET /api/v1/sales/reports/by-date-range?start={date}&end={date}` - Reporte por fechas

### Reservas

- `POST /api/v1/reservations` - Crear reserva
- `GET /api/v1/reservations` - Listar reservas
- `PATCH /api/v1/reservations/{id}/confirm` - Confirmar reserva
- `PATCH /api/v1/reservations/{id}/complete` - Completar reserva (convertir en venta)
- `PATCH /api/v1/reservations/{id}/cancel` - Cancelar reserva

### Stock

- `POST /api/v1/stock/entry` - Registrar ingreso de stock
- `POST /api/v1/stock/exit` - Registrar egreso de stock
- `GET /api/v1/stock/movements/{productId}` - Historial por producto
- `GET /api/v1/stock/movements` - Historial general

### Alertas

- `GET /api/v1/alerts` - Listar alertas del usuario
- `GET /api/v1/alerts/unread` - Alertas no leídas
- `PATCH /api/v1/alerts/{id}/read` - Marcar como leída
- `PATCH /api/v1/alerts/read-all` - Marcar todas como leídas

### Reportes

- `GET /api/v1/reports/sales/summary?start={date}&end={date}` - Resumen de ventas
- `GET /api/v1/reports/sales/top-products?start={date}&end={date}&limit=10` - Top productos
- `GET /api/v1/reports/sales/by-seller?start={date}&end={date}` - Ventas por vendedor
- `GET /api/v1/reports/sales/daily?start={date}&end={date}` - Serie diaria de ventas

### Auditoría

- `GET /api/v1/audit` - Listar todos los logs
- `GET /api/v1/audit/entity-type/{entityType}` - Logs por tipo de entidad
- `GET /api/v1/audit/entity/{entityType}/{entityId}` - Logs de una entidad
- `GET /api/v1/audit/user/{userId}` - Logs de un usuario
- `GET /api/v1/audit/date-range?start={date}&end={date}` - Logs por rango de fechas

## 👥 Sistema de Roles

El sistema implementa 5 roles granulares para control de acceso:

### SUPER_ADMIN

- Configuración del sistema
- Gestión de usuarios y roles
- Acceso completo a todas las funcionalidades

### MANAGER

- Administración diaria del negocio
- Ver reportes y analytics
- Gestionar productos, categorías, ventas
- Ver todas las ventas y cancelar ventas
- Gestionar clientes y reservas

### CASHIER

- Operaciones de venta al público
- Crear ventas
- Ver productos (solo lectura)
- Crear/editar clientes
- Crear reservas
- Ver solo sus propias ventas

### WAREHOUSE

- Gestión de inventario y stock
- Crear/editar productos
- Gestionar stock (entradas, salidas, ajustes)
- Gestionar categorías
- Ver movimientos de stock

### VIEWER

- Solo lectura en todas las funcionalidades
- Ver productos, ventas, clientes, reservas
- Ver reportes básicos
- No puede crear, editar ni eliminar nada

## 📁 Estructura del Proyecto

```
src/main/java/com/example/farmaser/
├── config/              # Configuraciones (Cache, Schedulers, etc.)
├── controller/          # Controladores REST
├── exceptions/          # Manejo de excepciones global
├── mapper/              # Mappers MapStruct
├── model/
│   ├── dto/            # Data Transfer Objects
│   ├── entity/         # Entidades JPA
│   └── repository/     # Repositorios Spring Data
├── security/           # Configuración de seguridad y JWT
└── service/            # Lógica de negocio
    └── impl/           # Implementaciones de servicios
```

## 🔧 Personalización

Este sistema está diseñado para ser fácilmente adaptable a diferentes tipos de comercios:

1. **Cambiar el dominio**: Reemplaza "farmacia" por tu dominio específico
2. **Agregar campos**: Extiende las entidades según tus necesidades
3. **Modificar validaciones**: Ajusta las reglas de negocio en los servicios
4. **Agregar módulos**: Sigue el patrón establecido para nuevos módulos

## 🧪 Testing

Para ejecutar los tests:

```bash
mvn test
```

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

Desarrollado como proyecto de portafolio para demostrar habilidades en desarrollo backend con Spring Boot.

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📞 Contacto

Para preguntas o sugerencias, puedes abrir un issue en el repositorio.

---

⭐ Si este proyecto te resulta útil, considera darle una estrella en GitHub.
