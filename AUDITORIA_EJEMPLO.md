# 📋 Ejemplo de Integración de Auditoría

Este documento muestra cómo integrar el sistema de auditoría en los servicios existentes.

## 📝 Ejemplo: ProductService con Auditoría

```java
@Service
public class ProductService implements IProduct {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuditHelper auditHelper; // Agregar esto

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @Autowired
    private ProductRequestMapper productRequestMapper;

    @Transactional
    @Override
    public ProductResponseDto save(ProductRequestDto productRequestDto) {
        // Validaciones existentes...

        ProductEntity productEntity = productRequestMapper.productRequestDtoToProductEntity(productRequestDto);
        ProductEntity savedProduct = productRepository.save(productEntity);

        // Registrar auditoría
        auditHelper.log(
            "Product",                    // Tipo de entidad
            savedProduct.getId(),         // ID de la entidad
            ActionType.CREATE,           // Acción
            null,                        // Valor anterior (null para creación)
            auditHelper.toJsonString(savedProduct), // Valor nuevo
            "Producto creado: " + savedProduct.getName(),
            null                         // HttpServletRequest (opcional, puede ser null)
        );

        return productResponseMapper.productEntityToProductDto(savedProduct);
    }

    @Transactional
    @Override
    public ProductResponseDto update(String barcode, ProductRequestDto productRequestDto) {
        ProductEntity existingProduct = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        // Guardar valores anteriores para auditoría
        String oldValue = auditHelper.toJsonString(existingProduct);

        // Actualizar campos...
        ProductEntity updatedEntity = productRequestMapper.productRequestDtoToProductEntity(productRequestDto);
        updatedEntity.setId(existingProduct.getId());
        // ... resto de la lógica

        ProductEntity savedProduct = productRepository.save(updatedEntity);

        // Registrar auditoría
        Map<String, Object> changes = new HashMap<>();
        if (!Objects.equals(existingProduct.getName(), savedProduct.getName())) {
            changes.put("name", existingProduct.getName() + " -> " + savedProduct.getName());
        }
        if (!Objects.equals(existingProduct.getPrice(), savedProduct.getPrice())) {
            changes.put("price", existingProduct.getPrice() + " -> " + savedProduct.getPrice());
        }
        // Agregar más campos según sea necesario...

        auditHelper.log(
            "Product",
            savedProduct.getId(),
            ActionType.UPDATE,
            oldValue,
            auditHelper.toJsonString(savedProduct),
            auditHelper.createChangeDescription(changes),
            null
        );

        return productResponseMapper.productEntityToProductDto(savedProduct);
    }

    @Transactional
    @Override
    public void delete(String barcode) {
        ProductEntity product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        product.setActive(false);
        productRepository.save(product);

        // Registrar auditoría
        auditHelper.log(
            "Product",
            product.getId(),
            ActionType.DELETE,
            auditHelper.toJsonString(product),
            null,
            "Producto desactivado: " + product.getName(),
            null
        );
    }
}
```

## 📝 Ejemplo: SaleService con Auditoría

```java
@Service
public class SaleService implements ISale {

    @Autowired
    private AuditHelper auditHelper;

    @Transactional
    @Override
    public SaleResponseDto create(SaleRequestDto requestDto, String userEmail) {
        // Lógica existente de creación de venta...

        SaleEntity sale = saleRepository.save(saleEntity);

        // Registrar auditoría
        auditHelper.log(
            "Sale",
            sale.getId(),
            ActionType.CREATE,
            null,
            auditHelper.toJsonString(sale),
            "Venta creada: " + sale.getSaleNumber() + " - Total: $" + sale.getTotal(),
            null
        );

        return saleResponseMapper.toDto(sale);
    }

    @Transactional
    @Override
    public SaleResponseDto cancel(Long id) {
        SaleEntity sale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        String oldValue = auditHelper.toJsonString(sale);
        sale.setStatus(SaleStatus.CANCELLED);
        SaleEntity updatedSale = saleRepository.save(sale);

        // Registrar auditoría
        auditHelper.log(
            "Sale",
            sale.getId(),
            ActionType.CANCEL,
            oldValue,
            auditHelper.toJsonString(updatedSale),
            "Venta cancelada: " + sale.getSaleNumber(),
            null
        );

        return saleResponseMapper.toDto(updatedSale);
    }
}
```

## 📝 Uso en Controladores (con HttpServletRequest)

```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private IProduct productService;

    @Autowired
    private AuditHelper auditHelper;

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
            @Valid @RequestBody ProductRequestDto productRequestDto,
            HttpServletRequest request) {  // Agregar HttpServletRequest

        ProductResponseDto created = productService.save(productRequestDto);

        // Registrar auditoría con IP
        auditHelper.log(
            "Product",
            created.getId(),
            ActionType.CREATE,
            null,
            auditHelper.toJsonString(created),
            "Producto creado desde API",
            request  // Pasar request para obtener IP
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

## 🔍 Consultar Logs de Auditoría

### Ejemplos de uso de los endpoints:

1. **Ver todos los logs:**

   ```
   GET /api/v1/audit?page=0&size=20
   ```

2. **Ver logs de un tipo de entidad:**

   ```
   GET /api/v1/audit/entity-type/Product
   ```

3. **Ver historial de una entidad específica:**

   ```
   GET /api/v1/audit/entity/Product/123/history
   ```

4. **Ver logs de un usuario:**

   ```
   GET /api/v1/audit/user/5
   ```

5. **Ver logs por acción:**

   ```
   GET /api/v1/audit/action/CREATE
   ```

6. **Ver logs por rango de fechas:**

   ```
   GET /api/v1/audit/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59
   ```

7. **Ver logs de un tipo de entidad en un rango de fechas:**
   ```
   GET /api/v1/audit/entity-type/Product/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59
   ```

## 📊 Acciones de Auditoría Disponibles

- `CREATE` - Crear entidad
- `UPDATE` - Actualizar entidad
- `DELETE` - Eliminar entidad (soft delete)
- `LOGIN` - Inicio de sesión
- `LOGOUT` - Cierre de sesión
- `CANCEL` - Cancelar operación
- `COMPLETE` - Completar operación
- `CONFIRM` - Confirmar operación
- `EXPIRE` - Expirar (reservas, etc.)

## ⚠️ Notas Importantes

1. **El auditoría es asíncrona y no bloquea operaciones:** Si falla, no interrumpe el flujo principal.

2. **IP Address:** Solo se registra si se pasa `HttpServletRequest` al método `log()`.

3. **JSON Values:** Los valores `oldValue` y `newValue` se almacenan como JSON strings para facilitar la comparación.

4. **Permisos:** Solo `SUPER_ADMIN` y `MANAGER` pueden consultar los logs de auditoría.

5. **Performance:** Los índices en la tabla `audit_log` optimizan las consultas por entidad, usuario, acción y fecha.
