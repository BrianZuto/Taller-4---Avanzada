-- Script de inicialización de la base de datos
CREATE DATABASE IF NOT EXISTS taller4_avanzada;
USE taller4_avanzada;

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Categorías
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Productos
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    category_id BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Tabla de Ventas
CREATE TABLE IF NOT EXISTS sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla de Detalles de Venta
CREATE TABLE IF NOT EXISTS sale_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_id BIGINT,
    product_id BIGINT,
    quantity INT NOT NULL,
    price_at_sale DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Insertar usuario administrador por defecto
-- Contraseña: admin123 (hasheada con BCrypt)
INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@inventpro.com', 'ADMIN')
ON DUPLICATE KEY UPDATE username=username;

-- Insertar categorías de ejemplo
INSERT INTO categories (name, description) VALUES
('Electrónicos', 'Dispositivos electrónicos y tecnología'),
('Ropa', 'Vestimenta y accesorios'),
('Hogar', 'Artículos para el hogar'),
('Deportes', 'Equipos y accesorios deportivos'),
('Libros', 'Libros y material educativo')
ON DUPLICATE KEY UPDATE name=name;

-- Insertar productos de ejemplo
INSERT INTO products (name, description, price, stock, category_id) VALUES
('Laptop HP Pavilion', 'Laptop para uso general y oficina', 899.99, 10, 1),
('Smartphone Samsung Galaxy', 'Teléfono inteligente con Android', 699.99, 15, 1),
('Camiseta Nike', 'Camiseta deportiva de algodón', 29.99, 50, 2),
('Sofá 3 plazas', 'Sofá cómodo para sala', 599.99, 5, 3),
('Balón de Fútbol', 'Balón oficial de fútbol', 24.99, 20, 4),
('Libro de Programación', 'Guía completa de programación', 49.99, 25, 5)
ON DUPLICATE KEY UPDATE name=name;
