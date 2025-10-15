# 🏪 InventPro - Sistema de Gestión de Inventario

<div align="center">

![InventPro Logo](https://img.shields.io/badge/InventPro-Sistema%20de%20Inventario-blue?style=for-the-badge&logo=shopping-cart)

**Sistema completo de gestión de inventario con Spring Boot y Angular**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.0-brightgreen?style=flat&logo=spring)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17-red?style=flat&logo=angular)](https://angular.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat&logo=mysql)](https://www.mysql.com/)
[![Java](https://img.shields.io/badge/Java-17-orange?style=flat&logo=java)](https://www.oracle.com/java/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0-blue?style=flat&logo=typescript)](https://www.typescriptlang.org/)

[🚀 Demo](#-demo) • [📋 Características](#-características) • [🛠️ Instalación](#️-instalación) • [📖 Uso](#-uso) • [🤝 Contribuir](#-contribuir)

</div>

---

## 📋 Características

### 🎯 **Funcionalidades Principales**
- ✅ **Gestión de Productos** - CRUD completo con categorías
- ✅ **Gestión de Categorías** - Organización de productos
- ✅ **Sistema de Ventas** - Registro y seguimiento de ventas
- ✅ **Sistema de Compras** - Control de stock e inventario
- ✅ **Dashboard Dinámico** - Estadísticas en tiempo real
- ✅ **Reportes Avanzados** - Análisis de ventas e inventario
- ✅ **Autenticación JWT** - Seguridad robusta
- ✅ **Interfaz Responsiva** - Diseño moderno y adaptable

### 🏗️ **Arquitectura**
- **Backend**: Spring Boot con Spring Security
- **Frontend**: Angular 17 con TypeScript
- **Base de Datos**: MySQL con JPA/Hibernate
- **Autenticación**: JWT (JSON Web Tokens)
- **API**: RESTful con validación completa

---

## 🚀 Demo

### 📱 **Capturas de Pantalla**

| Dashboard | Productos | Ventas |
|-----------|-----------|---------|
| ![Dashboard](https://via.placeholder.com/300x200/007bff/ffffff?text=Dashboard) | ![Productos](https://via.placeholder.com/300x200/28a745/ffffff?text=Productos) | ![Ventas](https://via.placeholder.com/300x200/dc3545/ffffff?text=Ventas) |

### 🎬 **Funcionalidades en Acción**
- **Dashboard Interactivo** con métricas en tiempo real
- **Gestión de Stock** con alertas automáticas
- **Reportes Visuales** con gráficos dinámicos
- **Interfaz Intuitiva** para máxima productividad

---

## 🛠️ Instalación

### 📋 **Prerrequisitos**

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| ☕ Java | 17+ | Runtime para Spring Boot |
| 🟢 Node.js | 18+ | Runtime para Angular |
| 🐬 MySQL | 8.0+ | Base de datos |
| 📦 Maven | 3.6+ | Gestión de dependencias |
| 🔧 Angular CLI | 17+ | Herramientas de desarrollo |

### 🚀 **Instalación Rápida**

#### 1️⃣ **Clonar el Repositorio**
```bash
git clone https://github.com/tu-usuario/inventpro.git
cd inventpro
```

#### 2️⃣ **Configurar Base de Datos**
```sql
-- Crear base de datos
CREATE DATABASE taller4_avanzada;

-- Crear usuario (opcional)
CREATE USER 'inventpro'@'localhost' IDENTIFIED BY 'tu_password';
GRANT ALL PRIVILEGES ON taller4_avanzada.* TO 'inventpro'@'localhost';
FLUSH PRIVILEGES;
```

#### 3️⃣ **Configurar Backend**
```bash
# Editar configuración
nano src/main/resources/application.properties
```

```properties
# Configuración de base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/taller4_avanzada
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# JWT Configuration
jwt.secret=tu_clave_secreta_muy_larga_y_segura_de_al_menos_32_caracteres
jwt.expiration=86400000

# Server
server.port=8080
```

#### 4️⃣ **Ejecutar Backend**
```bash
# Compilar y ejecutar
mvn clean install
mvn spring-boot:run
```

#### 5️⃣ **Ejecutar Frontend**
```bash
# Navegar al directorio frontend
cd inventpro-frontend

# Instalar dependencias
npm install

# Ejecutar servidor de desarrollo
ng serve
```

### 🌐 **Acceso a la Aplicación**
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

---

## 📖 Uso

### 🔐 **Primer Acceso**

1. **Registrar Usuario**
   - Ve a http://localhost:4200
   - Haz clic en "Registrarse"
   - Completa el formulario con tus datos

2. **Iniciar Sesión**
   - Usa las credenciales creadas
   - El sistema te redirigirá al Dashboard

### 📊 **Dashboard Principal**

El Dashboard incluye:
- **Métricas en Tiempo Real**: Productos, ventas, categorías
- **Reportes de Ventas**: Por día, mes y productos más vendidos
- **Análisis de Inventario**: Stock bajo, distribución, valor total
- **Alertas Automáticas**: Notificaciones de stock crítico

### 🛍️ **Gestión de Productos**

- **Agregar Productos**: Nombre, precio, stock, categoría
- **Editar Productos**: Modificar información existente
- **Eliminar Productos**: Soft delete (marcar como inactivo)
- **Buscar Productos**: Filtros por nombre y categoría

### 🏷️ **Gestión de Categorías**

- **Crear Categorías**: Organizar productos por tipo
- **Editar Categorías**: Modificar descripción y nombre
- **Eliminar Categorías**: Solo si no tienen productos asociados

### 💰 **Sistema de Ventas**

- **Crear Venta**: Seleccionar productos y cantidades
- **Calcular Total**: Automático con impuestos
- **Actualizar Stock**: Reducción automática del inventario
- **Historial de Ventas**: Consultar ventas anteriores

### 📦 **Sistema de Compras**

- **Registrar Compra**: Aumentar stock de productos
- **Actualizar Precios**: Modificar precios de compra
- **Control de Costos**: Seguimiento de gastos

---

## 🏗️ Estructura del Proyecto

```
📁 inventpro/
├── 📁 src/main/java/com/inventpro/
│   ├── 📁 entity/              # Entidades JPA
│   │   ├── Usuario.java
│   │   ├── Producto.java
│   │   ├── Categoria.java
│   │   └── Venta.java
│   ├── 📁 repository/          # Repositorios de datos
│   ├── 📁 service/             # Lógica de negocio
│   ├── 📁 controller/          # Controladores REST
│   ├── 📁 dto/                 # Objetos de transferencia
│   ├── 📁 security/            # Configuración de seguridad
│   └── 📁 config/              # Configuraciones
├── 📁 inventpro-frontend/
│   ├── 📁 src/app/
│   │   ├── 📁 components/      # Componentes Angular
│   │   │   ├── dashboard/
│   │   │   ├── productos/
│   │   │   ├── categorias/
│   │   │   ├── ventas/
│   │   │   └── compras/
│   │   ├── 📁 services/        # Servicios Angular
│   │   ├── 📁 models/          # Interfaces TypeScript
│   │   └── 📁 guards/          # Guards de autenticación
├── 📄 README.md
└── 📄 pom.xml
```

---

## 🔌 API Endpoints

### 🔐 **Autenticación**
```http
POST /api/auth/login          # Iniciar sesión
POST /api/auth/register       # Registrar usuario
GET  /api/auth/profile        # Obtener perfil
```

### 📦 **Productos**
```http
GET    /api/productos         # Listar productos
POST   /api/productos         # Crear producto
PUT    /api/productos/{id}    # Actualizar producto
DELETE /api/productos/{id}    # Eliminar producto
GET    /api/productos/buscar  # Buscar productos
```

### 🏷️ **Categorías**
```http
GET    /api/categorias        # Listar categorías
POST   /api/categorias        # Crear categoría
PUT    /api/categorias/{id}   # Actualizar categoría
DELETE /api/categorias/{id}   # Eliminar categoría
```

### 💰 **Ventas**
```http
GET    /api/ventas            # Listar ventas
POST   /api/ventas            # Crear venta
PUT    /api/ventas/{id}       # Actualizar venta
DELETE /api/ventas/{id}       # Eliminar venta
```

### 📊 **Estadísticas**
```http
GET /api/estadisticas/dashboard    # Datos del dashboard
GET /api/estadisticas/ventas       # Reportes de ventas
GET /api/estadisticas/productos    # Reportes de productos
GET /api/estadisticas/inventario   # Reportes de inventario
```

---

## 🎨 Tecnologías Utilizadas

### 🔧 **Backend**
- **Spring Boot 2.7.0** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **MySQL Connector** - Conexión a base de datos
- **JWT** - Tokens de autenticación
- **BCrypt** - Encriptación de contraseñas
- **Validation** - Validación de datos

### 🎯 **Frontend**
- **Angular 17** - Framework principal
- **TypeScript** - Lenguaje de programación
- **RxJS** - Programación reactiva
- **Angular Material** - Componentes UI
- **FontAwesome** - Iconografía
- **SweetAlert2** - Alertas y confirmaciones

### 🗄️ **Base de Datos**
- **MySQL 8.0** - Sistema de gestión de base de datos
- **JPA/Hibernate** - ORM para Java
- **Soft Delete** - Eliminación lógica de registros

---

## 🚨 Solución de Problemas

### ❌ **Error de Conexión a Base de Datos**
```bash
# Verificar que MySQL esté ejecutándose
sudo systemctl status mysql

# Verificar conexión
mysql -u root -p -e "SHOW DATABASES;"
```

### ❌ **Error de CORS**
```java
// Verificar configuración en SecurityConfig.java
@CrossOrigin(origins = "http://localhost:4200")
```

### ❌ **Error de Compilación**
```bash
# Limpiar y recompilar
mvn clean install
cd inventpro-frontend && npm install
```

### ❌ **Error de JWT**
```properties
# Verificar que jwt.secret tenga al menos 32 caracteres
jwt.secret=tu_clave_secreta_muy_larga_y_segura_de_al_menos_32_caracteres
```

---

## 🔧 Configuración Avanzada

### 🌍 **Variables de Entorno**
```bash
# Crear archivo .env
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=taller4_avanzada
export DB_USER=tu_usuario
export DB_PASSWORD=tu_password
export JWT_SECRET=tu_clave_secreta_muy_larga
```

### 🐳 **Docker (Opcional)**
```dockerfile
# Dockerfile para backend
FROM openjdk:17-jdk-slim
COPY target/inventpro-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 📊 **Monitoreo**
- **Actuator** - Endpoints de monitoreo
- **Logs** - Registro de actividades
- **Métricas** - Rendimiento de la aplicación

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! 🎉

### 📝 **Proceso de Contribución**

1. **Fork** el proyecto
2. **Crea** una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abre** un Pull Request

### 🐛 **Reportar Bugs**
- Usa el sistema de [Issues](https://github.com/tu-usuario/inventpro/issues)
- Incluye pasos para reproducir el error
- Adjunta capturas de pantalla si es necesario

### 💡 **Sugerir Mejoras**
- Abre un [Issue](https://github.com/tu-usuario/inventpro/issues) con la etiqueta "enhancement"
- Describe la funcionalidad que te gustaría ver
- Explica por qué sería útil

---

## 📄 Licencia

Este proyecto está bajo la **Licencia MIT**. Ver el archivo [LICENSE](LICENSE) para más detalles.

```
MIT License

Copyright (c) 2024 InventPro

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 👥 Autores

- **Tu Nombre** - *Desarrollo inicial* - [@tu-usuario](https://github.com/tu-usuario)

---

## 🙏 Agradecimientos

- [Spring Boot](https://spring.io/projects/spring-boot) - Framework backend
- [Angular](https://angular.io/) - Framework frontend
- [MySQL](https://www.mysql.com/) - Base de datos
- [FontAwesome](https://fontawesome.com/) - Iconos
- [SweetAlert2](https://sweetalert2.github.io/) - Alertas

---

<div align="center">

**⭐ Si te gusta este proyecto, ¡dale una estrella! ⭐**

[![GitHub stars](https://img.shields.io/github/stars/tu-usuario/inventpro?style=social)](https://github.com/tu-usuario/inventpro)
[![GitHub forks](https://img.shields.io/github/forks/tu-usuario/inventpro?style=social)](https://github.com/tu-usuario/inventpro)
[![GitHub issues](https://img.shields.io/github/issues/tu-usuario/inventpro)](https://github.com/tu-usuario/inventpro/issues)

</div>