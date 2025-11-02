# 🔄 Flujo de Trabajo con IntelliJ IDEA y Cursor

## 📋 Estrategia Recomendada: Ramas Git

### Opción 1: Rama separada para Cursor (Recomendado) ⭐

Esta es la mejor opción porque:

- Mantienes el código estable en `main`/`develop`
- Puedes experimentar libremente en Cursor
- IntelliJ puede probar los cambios antes de integrarlos

#### Configuración Inicial:

```bash
# Desde IntelliJ o terminal en la raíz del proyecto

# 1. Crear rama para Cursor
git checkout -b cursor-dev

# 2. Subir la rama al remoto
git push -u origin cursor-dev

# 3. Trabajar normalmente en Cursor en esta rama
```

#### Flujo de Trabajo Diario:

**En Cursor:**

```bash
# 1. Asegúrate de estar en la rama cursor-dev
git checkout cursor-dev

# 2. Trabajas normalmente, haces commits
git add .
git commit -m "feat: Nueva funcionalidad desde Cursor"
git push origin cursor-dev
```

**En IntelliJ IDEA:**

1. **Obtener cambios de Cursor:**

```bash
# Opción A: Pull directo de la rama cursor-dev
git checkout cursor-dev
git pull origin cursor-dev

# Opción B: Fetch para ver cambios sin cambiar de rama
git fetch origin cursor-dev
```

2. **Probar en IntelliJ:**

    - Abre el proyecto
    - IntelliJ detectará los cambios automáticamente
    - Puedes ejecutar, hacer debug, pruebas, etc.

3. **Fusionar cambios a main cuando estén listos:**

```bash
# Cambiar a main
git checkout main

# Fusionar cambios de cursor-dev
git merge cursor-dev

# O usar merge con mensaje
git merge cursor-dev -m "Merge: Integrar cambios desarrollados en Cursor"

# Push a main
git push origin main
```

---

### Opción 2: Trabajar en la misma rama (más simple, pero requiere cuidado)

Si prefieres trabajar en la misma rama:

**En Cursor:**

```bash
git checkout main
# Trabajas y haces commits
git push origin main
```

**En IntelliJ:**

```bash
git pull origin main
# Probar y ejecutar
```

⚠️ **Precaución:** Asegúrate de hacer `pull` antes de trabajar en cada IDE para evitar conflictos.

---

### Opción 3: Rama de desarrollo compartida (Ideal para equipos)

```bash
# Crear rama develop
git checkout -b develop
git push -u origin develop

# Cursor trabaja en cursor-dev
# Cuando está listo, hacer merge a develop
# IntelliJ prueba desde develop
# Cuando está estable, merge develop → main
```

---

## 🛠️ Configuración Paso a Paso

### 1. Configuración Inicial (Hacer una vez)

**En terminal (o Git Bash):**

```bash
# Ver ramas actuales
git branch

# Crear y cambiar a rama cursor-dev
git checkout -b cursor-dev

# Subir al remoto
git push -u origin cursor-dev

# Verificar
git branch -a
```

### 2. Configuración en Cursor

1. Abre el proyecto en Cursor
2. Abre la terminal integrada (Ctrl + `)
3. Verifica la rama:

```bash
git branch
# Debería mostrar * cursor-dev
```

Si estás en `main`:

```bash
git checkout cursor-dev
```

### 3. Configuración en IntelliJ IDEA

1. Abre el proyecto en IntelliJ
2. Ve a: `VCS` → `Git` → `Branches`
3. Selecciona `cursor-dev` → `Checkout`
4. O usa el terminal de IntelliJ:

```bash
git checkout cursor-dev
git pull origin cursor-dev
```

---

## 📝 Flujo de Trabajo Recomendado (Día a Día)

### Escenario 1: Desarrollar nueva funcionalidad en Cursor

```bash
# En Cursor:
git checkout cursor-dev
git pull origin cursor-dev  # Por si hay cambios

# Desarrollas con IA en Cursor
# Haces commits
git add .
git commit -m "feat: Nueva funcionalidad X"
git push origin cursor-dev
```

### Escenario 2: Probar en IntelliJ

```bash
# En IntelliJ (terminal o interfaz):
git checkout cursor-dev
git pull origin cursor-dev

# Ahora puedes:
# - Ejecutar la aplicación (Run)
# - Ejecutar tests
# - Debug
# - Verificar que todo compile
```

### Escenario 3: Aprobar y fusionar a main

```bash
# En IntelliJ o terminal:
git checkout main
git pull origin main  # Asegurar que main está actualizado
git merge cursor-dev

# Resolver conflictos si los hay
# Probar una vez más

git push origin main
```

### Escenario 4: Continuar desarrollo después de merge

```bash
# Actualizar cursor-dev con los cambios de main
git checkout cursor-dev
git merge main
git push origin cursor-dev
```

---

## 🔧 Comandos Útiles

### Ver diferencias entre ramas:

```bash
git diff main..cursor-dev
```

### Ver commits que están en cursor-dev pero no en main:

```bash
git log main..cursor-dev
```

### Crear rama temporal para experimentar:

```bash
git checkout cursor-dev
git checkout -b feature/nueva-funcionalidad
# Trabajas
git checkout cursor-dev
git merge feature/nueva-funcionalidad
```

---

## ⚠️ Mejores Prácticas

1. **Siempre hacer pull antes de trabajar:**

   ```bash
   git pull origin cursor-dev
   ```

2. **Commits frecuentes y descriptivos:**

    - Facilita identificar cambios
    - Permite rollback si algo falla

3. **Probar en IntelliJ antes de merge a main:**

    - IntelliJ es mejor para ejecutar y debuggear
    - Verifica que compile y funcione

4. **Mantener main estable:**

    - Solo mergear cuando esté probado
    - main debe estar siempre funcional

5. **Sincronizar regularmente:**
    - Actualiza cursor-dev con main periódicamente
    - Evita divergencias grandes

---

## 🎯 Ejemplo de Workflow Semanal

**Lunes - Miércoles: Desarrollo en Cursor**

```bash
# En Cursor
git checkout cursor-dev
# Desarrollas features con IA
git commit -m "feat: Feature X"
git push origin cursor-dev
```

**Jueves: Pruebas en IntelliJ**

```bash
# En IntelliJ
git checkout cursor-dev
git pull origin cursor-dev
# Ejecutas, pruebas, debuggeas
# Corriges bugs si los hay
git commit -m "fix: Corregir bug Y"
git push origin cursor-dev
```

**Viernes: Merge a main**

```bash
# En IntelliJ o terminal
git checkout main
git pull origin main
git merge cursor-dev
# Pruebas finales
git push origin main
```

---

## 🚀 Comandos Rápidos (Alias opcionales)

Puedes crear alias en Git para facilitar el trabajo:

```bash
# Agregar a ~/.gitconfig o ejecutar:
git config --global alias.cursor "checkout cursor-dev"
git config --global alias.main "checkout main"
git config --global alias.sync "!git fetch && git pull"

# Uso:
git cursor    # Cambia a cursor-dev
git main      # Cambia a main
git sync      # Sincroniza con remoto
```

---

## 💡 Ventajas de este Flujo

✅ **Separación clara:** Cursor para desarrollo, IntelliJ para pruebas  
✅ **Seguridad:** main siempre estable  
✅ **Flexibilidad:** Puedes experimentar sin miedo  
✅ **Trazabilidad:** Cada cambio está documentado  
✅ **Colaboración:** Si trabajas en equipo, otros pueden ver cursor-dev

---

## 🔍 Verificar Estado Actual

```bash
# Ver en qué rama estás
git branch

# Ver estado de archivos
git status

# Ver últimas ramas
git branch -a

# Ver diferencias entre ramas
git diff main cursor-dev
```


