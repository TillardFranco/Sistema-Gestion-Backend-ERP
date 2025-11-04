# 🎭 Sistema de Roles y Permisos - Diseño Genérico

## 📋 ¿Qué significa "Roles Granulares"?

**Roles granulares** se refiere a tener múltiples roles con permisos específicos, en lugar de solo dos roles básicos (ADMIN/USER).

### Situación Actual (Básica):

- **ADMIN**: Acceso total a todo
- **USER**: Acceso básico a todo

**Problema**: No hay control fino. Un usuario básico puede hacer lo mismo que un admin, o un admin debe hacer todo manualmente.

### Situación con Roles Granulares:

- **MANAGER**: Acceso completo + reportes
- **CASHIER**: Solo ventas y clientes
- **WAREHOUSE**: Solo gestión de stock
- **VIEWER**: Solo lectura
- **ADMIN**: Configuración del sistema

**Ventaja**: Control preciso de quién puede hacer qué, mejor seguridad y organización.

---

## 🏪 ¿Por qué Roles Genéricos?

Como tu aplicación será para **múltiples tipos de comercios** (farmacias, tiendas, supermercados, etc.), necesitas roles que funcionen para todos, no específicos de un rubro.

### ❌ Roles Específicos de Farmacia (NO recomendado):

- `PHARMACIST` - Solo tiene sentido en farmacias
- `DISPENSER` - Específico de farmacia
- `PHARMACY_TECH` - Muy específico

### ✅ Roles Genéricos (Recomendado):

- `MANAGER` - Gerente (funciona en cualquier negocio)
- `CASHIER` - Cajero/Vendedor (universal)
- `WAREHOUSE` - Depósito/Inventario (universal)
- `VIEWER` - Solo lectura (universal)

---

## 💡 Recomendación: Sistema de Roles Genéricos

### **Roles Propuestos (5 roles):**

#### 1. **SUPER_ADMIN** (Nuevo)

- **Propósito**: Configuración del sistema, usuarios, roles
- **Permisos**:
  - ✅ Gestionar usuarios y roles
  - ✅ Configuración del sistema
  - ✅ Acceso completo a todo
  - ❌ Solo 1-2 usuarios deberían tenerlo

#### 2. **MANAGER** (Gerente)

- **Propósito**: Administración diaria del negocio
- **Permisos**:
  - ✅ Ver reportes y analytics
  - ✅ Crear/editar/eliminar productos
  - ✅ Gestionar categorías
  - ✅ Ver todas las ventas
  - ✅ Cancelar ventas
  - ✅ Gestionar clientes
  - ✅ Gestionar reservas
  - ✅ Ver alertas y notificaciones
  - ❌ No puede gestionar usuarios ni configurar el sistema

#### 3. **CASHIER** (Cajero/Vendedor)

- **Propósito**: Operaciones de venta al público
- **Permisos**:
  - ✅ Ver productos (solo lectura)
  - ✅ Crear ventas
  - ✅ Ver sus propias ventas
  - ✅ Crear/editar clientes
  - ✅ Crear reservas
  - ✅ Ver alertas propias
  - ✅ Cancelar sus propias ventas (con restricciones)
  - ❌ No puede editar productos
  - ❌ No puede ver reportes completos
  - ❌ No puede gestionar stock directamente

#### 4. **WAREHOUSE** (Depósito/Inventario)

- **Propósito**: Gestión de inventario y stock
- **Permisos**:
  - ✅ Ver productos
  - ✅ Crear/editar productos
  - ✅ Gestionar stock (entradas, salidas, ajustes)
  - ✅ Ver movimientos de stock
  - ✅ Ver alertas de stock bajo
  - ✅ Gestionar categorías
  - ✅ Ver compras (cuando se implemente)
  - ❌ No puede crear ventas
  - ❌ No puede ver reportes financieros
  - ❌ No puede gestionar clientes

#### 5. **VIEWER** (Solo Lectura)

- **Propósito**: Personal que solo necesita consultar información
- **Permisos**:
  - ✅ Ver productos (solo lectura)
  - ✅ Ver ventas (solo lectura)
  - ✅ Ver clientes (solo lectura)
  - ✅ Ver reportes básicos (solo lectura)
  - ✅ Ver alertas
  - ❌ No puede crear, editar ni eliminar nada

---

## 🔐 Matriz de Permisos Detallada

| Funcionalidad                | SUPER_ADMIN | MANAGER  | CASHIER         | WAREHOUSE      | VIEWER         |
| ---------------------------- | ----------- | -------- | --------------- | -------------- | -------------- |
| **Usuarios y Roles**         | ✅ Total    | ❌ No    | ❌ No           | ❌ No          | ❌ No          |
| **Productos - Ver**          | ✅ Sí       | ✅ Sí    | ✅ Sí           | ✅ Sí          | ✅ Sí          |
| **Productos - Crear/Editar** | ✅ Sí       | ✅ Sí    | ❌ No           | ✅ Sí          | ❌ No          |
| **Productos - Eliminar**     | ✅ Sí       | ✅ Sí    | ❌ No           | ✅ Sí          | ❌ No          |
| **Categorías**               | ✅ Total    | ✅ Total | ❌ No           | ✅ Total       | ✅ Ver         |
| **Stock - Ver**              | ✅ Sí       | ✅ Sí    | ✅ Sí           | ✅ Sí          | ✅ Sí          |
| **Stock - Gestionar**        | ✅ Sí       | ✅ Sí    | ❌ No           | ✅ Sí          | ❌ No          |
| **Ventas - Crear**           | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ❌ No          |
| **Ventas - Ver todas**       | ✅ Sí       | ✅ Sí    | ✅ Solo propias | ✅ Sí          | ✅ Sí          |
| **Ventas - Cancelar**        | ✅ Sí       | ✅ Sí    | ✅ Solo propias | ❌ No          | ❌ No          |
| **Clientes - Crear/Editar**  | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ❌ No          |
| **Clientes - Ver**           | ✅ Sí       | ✅ Sí    | ✅ Sí           | ❌ No          | ✅ Sí          |
| **Reservas**                 | ✅ Total    | ✅ Total | ✅ Crear/Ver    | ❌ No          | ✅ Ver         |
| **Alertas**                  | ✅ Total    | ✅ Total | ✅ Ver propias  | ✅ Ver propias | ✅ Ver propias |
| **Reportes**                 | ✅ Total    | ✅ Total | ❌ No           | ✅ Stock       | ✅ Básicos     |
| **Compras** (Futuro)         | ✅ Total    | ✅ Total | ❌ No           | ✅ Total       | ✅ Ver         |

---

## 🏗️ Implementación Técnica

### 1. **Actualizar ERole Enum**

```java
public enum ERole {
    SUPER_ADMIN,    // Nuevo
    MANAGER,        // Reemplaza ADMIN para operaciones diarias
    CASHIER,        // Nuevo
    WAREHOUSE,      // Nuevo
    VIEWER          // Nuevo
    // USER se puede eliminar o mantener como legacy
}
```

### 2. **Actualizar SecurityConfig**

```java
.authorizeHttpRequests(auth -> {
    // Público
    auth.requestMatchers("/api/v1/login").permitAll();

    // Solo SUPER_ADMIN
    auth.requestMatchers("/api/v1/users/**", "/api/v1/admin/**")
        .hasRole("SUPER_ADMIN");

    // SUPER_ADMIN + MANAGER
    auth.requestMatchers("/api/v1/reports/**")
        .hasAnyRole("SUPER_ADMIN", "MANAGER");

    // SUPER_ADMIN + MANAGER + CASHIER
    auth.requestMatchers("/api/v1/sales/**", "/api/v1/customers/**")
        .hasAnyRole("SUPER_ADMIN", "MANAGER", "CASHIER");

    // SUPER_ADMIN + MANAGER + WAREHOUSE
    auth.requestMatchers("/api/v1/stock/**", "/api/v1/products/**")
        .hasAnyRole("SUPER_ADMIN", "MANAGER", "WAREHOUSE");

    // Todos autenticados
    auth.requestMatchers("/api/v1/alerts/**")
        .authenticated();

    // VIEWER solo lectura (usar @PreAuthorize en métodos específicos)
    auth.anyRequest().authenticated();
})
```

### 3. **Usar @PreAuthorize en Controladores**

```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'CASHIER', 'VIEWER')")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(...) {
        // Todos pueden ver
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE')")
    public ResponseEntity<ProductResponseDto> createProduct(...) {
        // Solo estos roles pueden crear
    }

    @DeleteMapping("/{barcode}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteProduct(...) {
        // Solo estos roles pueden eliminar
    }
}
```

### 4. **Migración de Roles Existentes**

```java
// En DataInitializer o un script de migración
// Convertir usuarios ADMIN existentes a MANAGER
// Convertir usuarios USER existentes a CASHIER (o según criterio)
```

---

## 📊 Ventajas de Este Sistema

### ✅ **Ventajas:**

1. **Genérico**: Funciona para farmacias, tiendas, supermercados, etc.
2. **Escalable**: Fácil agregar más roles en el futuro
3. **Seguro**: Control fino de permisos
4. **Flexible**: Cada negocio asigna roles según su necesidad
5. **Profesional**: Estándar en software comercial

### ⚠️ **Consideraciones:**

1. **Migración**: Necesitas migrar usuarios existentes
2. **Testing**: Probar todos los permisos por rol
3. **Documentación**: Documentar qué puede hacer cada rol
4. **Flexibilidad futura**: Considerar sistema de permisos más avanzado (ACL) si crece mucho

---

## 🚀 Plan de Implementación Recomendado

### **Fase 5.1: Roles Granulares (Genéricos)**

1. **Semana 1:**

   - [ ] Actualizar `ERole` con los 5 roles nuevos
   - [ ] Crear migración de datos (convertir ADMIN→MANAGER, USER→CASHIER)
   - [ ] Actualizar `DataInitializer` con todos los roles
   - [ ] Actualizar `SecurityConfig` con permisos básicos

2. **Semana 2:**
   - [ ] Agregar `@PreAuthorize` en todos los controladores
   - [ ] Crear tests de permisos por rol
   - [ ] Documentar matriz de permisos
   - [ ] Testing completo de seguridad

### **Fase 5.2: Sistema de Auditoría** (como está planificado)

---

## 💼 Casos de Uso por Tipo de Negocio

### **Farmacia:**

- **SUPER_ADMIN**: Dueño/Farmacéutico titular
- **MANAGER**: Farmacéutico de turno
- **CASHIER**: Vendedor/Cajero
- **WAREHOUSE**: Personal de depósito
- **VIEWER**: Contador (solo consultas)

### **Tienda de Ropa:**

- **SUPER_ADMIN**: Dueño
- **MANAGER**: Gerente de tienda
- **CASHIER**: Vendedor
- **WAREHOUSE**: Personal de almacén
- **VIEWER**: Auditor externo

### **Supermercado:**

- **SUPER_ADMIN**: Director
- **MANAGER**: Gerente de sucursal
- **CASHIER**: Cajero
- **WAREHOUSE**: Repositor/Depósito
- **VIEWER**: Analista de datos

---

## 🎯 Recomendación Final

**Implementa los 5 roles genéricos propuestos:**

1. `SUPER_ADMIN` - Configuración del sistema
2. `MANAGER` - Administración diaria
3. `CASHIER` - Ventas y atención al cliente
4. `WAREHOUSE` - Inventario y stock
5. `VIEWER` - Solo consultas

**Esto te dará:**

- ✅ Flexibilidad para cualquier tipo de comercio
- ✅ Control de seguridad adecuado
- ✅ Escalabilidad para el futuro
- ✅ Producto profesional y comercializable

---

**¿Quieres que implemente esta estructura de roles genéricos ahora o prefieres ajustar algo antes?**
