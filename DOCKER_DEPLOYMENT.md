# 🐳 Despliegue con Docker - InventPro

## 📋 Requisitos Previos

- Docker instalado
- Docker Compose instalado
- Git instalado

## 🚀 Instrucciones de Despliegue

### 1. Clonar el Repositorio
```bash
git clone https://github.com/BrianZuto/Taller-4---Avanzada.git
cd Taller-4---Avanzada
```

### 2. Configurar Variables de Entorno
```bash
# Copiar el archivo de ejemplo
cp docker.env .env

# Editar las variables según sea necesario
nano .env
```

### 3. Construir y Desplegar
```bash
# Construir todas las imágenes
docker-compose build --no-cache

# Desplegar todos los servicios
docker-compose up -d

# Verificar el estado de los contenedores
docker-compose ps
```

### 4. Verificar el Despliegue
```bash
# Ver logs del backend
docker-compose logs backend

# Ver logs del frontend
docker-compose logs frontend

# Ver logs de MySQL
docker-compose logs mysql
```

## 🌐 Acceso a la Aplicación

- **Frontend (Interfaz Web)**: http://13.61.142.123
- **Backend API**: http://13.61.142.123:8080/api
- **Base de Datos**: 13.61.142.123:3306

## 👤 Credenciales por Defecto

- **Usuario**: admin
- **Contraseña**: admin123

## 🔧 Comandos Útiles

### Gestión de Contenedores
```bash
# Detener todos los servicios
docker-compose down

# Reiniciar un servicio específico
docker-compose restart backend

# Ver logs en tiempo real
docker-compose logs -f

# Detener y eliminar volúmenes
docker-compose down -v
```

### Limpieza
```bash
# Eliminar contenedores
docker container prune -f

# Eliminar imágenes
docker image prune -a -f

# Eliminar volúmenes
docker volume prune -f

# Eliminar redes
docker network prune -f
```

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   MySQL         │
│   (Nginx)       │◄──►│   (Spring Boot) │◄──►│   (Database)    │
│   Port: 80      │    │   Port: 8080    │    │   Port: 3306    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🔍 Solución de Problemas

### Error de Permisos de Docker
```bash
sudo usermod -aG docker $USER
newgrp docker
```

### Error de Conexión a la Base de Datos
```bash
# Verificar que MySQL esté ejecutándose
docker-compose logs mysql

# Reiniciar MySQL
docker-compose restart mysql
```

### Error de Construcción del Frontend
```bash
# Limpiar caché de npm
docker-compose build --no-cache frontend
```

## 📝 Notas Importantes

1. **CORS**: Configurado para permitir conexiones desde localhost y la IP del VPS
2. **Base de Datos**: Se inicializa automáticamente con datos de ejemplo
3. **Proxy Reverso**: Nginx maneja el enrutamiento entre frontend y backend
4. **Variables de Entorno**: Todas las configuraciones sensibles están en variables de entorno
5. **Restart Policy**: Los contenedores se reinician automáticamente si fallan
