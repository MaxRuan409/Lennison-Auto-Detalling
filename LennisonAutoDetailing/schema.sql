-- =============================================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS: LENNISON AUTO DETAILING
-- (Versión limpia sin sentencias DROP para instalación desde cero)
-- =============================================================================
-- 1. Crear el usuario (cambia 'tu_contrasena_aqui' por la contraseña que tengas en tu código Java)
CREATE USER IF NOT EXISTS 'lennison_app'@'localhost' IDENTIFIED BY '123456';

-- 2. Darle todos los permisos sobre la base de datos lennison_db
GRANT ALL PRIVILEGES ON lennison_db.* TO 'lennison_app'@'localhost';

-- 3. Aplicar los cambios
FLUSH PRIVILEGES;

-- 1. Crear base de datos y seleccionarla
CREATE DATABASE IF NOT EXISTS lennison_db;
USE lennison_db;

-- 2. Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(64) NOT NULL, -- Hash SHA-256 de la contraseña
    telefono VARCHAR(20) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'USUARIO', -- 'ADMIN' o 'USUARIO'
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla de Productos
CREATE TABLE IF NOT EXISTS productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    imagen_url VARCHAR(255)
);

-- 4. Tabla de Servicios
CREATE TABLE IF NOT EXISTS servicios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    duracion_estimada_minutos INT NOT NULL
);

-- 5. Tabla de Pedidos / Órdenes
CREATE TABLE IF NOT EXISTS pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_servicio TIMESTAMP NULL, -- Fecha programada del servicio de detallado
    total DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE', -- 'PENDIENTE', 'COMPLETADO', 'CANCELADO'
    vehiculo_marca VARCHAR(50),
    vehiculo_modelo VARCHAR(50),
    vehiculo_placa VARCHAR(20),
    vehiculo_color VARCHAR(30),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- 6. Tabla Detalle de Pedido
CREATE TABLE IF NOT EXISTS detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- 'PRODUCTO' o 'SERVICIO'
    id_referencia INT NOT NULL, -- ID del producto o del servicio
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id) ON DELETE CASCADE
);

-- =============================================================================
-- INSERCIONES DE PRUEBA (DATA INICIAL)
-- =============================================================================

-- Contraseñas hasheadas en SHA-256 para el valor plano: "123456"
-- Hash: 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92

-- Insertar Cuentas por Defecto
INSERT IGNORE INTO usuarios (nombre, correo, contrasena_hash, telefono, rol) VALUES 
('Administrador Lennison', 'admin@lennison.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '555-0199', 'ADMIN'),
('Juan Perez (Cliente)', 'usuario@lennison.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '555-0123', 'USUARIO');

-- Insertar Productos en Catálogo
INSERT IGNORE INTO productos (id, nombre, descripcion, precio, stock, imagen_url) VALUES
(1, 'Shampoo con Cera Carnauba (500ml)', 'Shampoo automotriz de PH neutro con cera carnauba para limpieza profunda y brillo premium sin dañar el sellador del auto.', 15.50, 15, 'https://images.unsplash.com/photo-1563720223185-11003d516935?w=500&auto=format&fit=crop&q=60'),
(2, 'Limpia Rines Descontaminante (250ml)', 'Remueve el polvo de balatas, la grasa de carretera y las impurezas ferrosas acumuladas en los rines dejándolos como nuevos.', 12.00, 8, 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=500&auto=format&fit=crop&q=60'),
(3, 'Cera Rapida Liquida Spray (450ml)', 'Cera protectora de fácil aplicación. Pulverizar, frotar con microfibra y obtén una repelencia al agua instantánea de larga duración.', 18.00, 20, 'https://images.unsplash.com/photo-1598978213499-6d84ff0d22c9?w=500&auto=format&fit=crop&q=60'),
(4, 'Toallas Microfibra Premium (Kit 3 uds)', 'Microfibras ultra suaves de 400 GSM ideales para remoción de cera, lavado en seco y secado rápido sin rayar la laca.', 9.99, 25, '');

-- Insertar Servicios de Detallado
INSERT IGNORE INTO servicios (id, nombre, descripcion, precio, duracion_estimada_minutos) VALUES
(1, 'Lavado Detallado Exterior', 'Lavado premium a dos cubetas, limpieza de arcos de rueda, descontaminado rápido de laca y aplicación de cera spray.', 30.00, 90),
(2, 'Detallado Completo de Interiores', 'Aspirado profundo, limpieza a vapor de alfombras, hidratación de plásticos, acondicionamiento de asientos de piel y sanitización con ozono.', 75.00, 180),
(3, 'Pulido Correccion de Pintura (1 Paso)', 'Eliminación del 60-70% de micro-rayas (swirls), restauración del brillo y profundidad del color, sellado acrílico protector por 6 meses.', 120.00, 240),
(4, 'Tratamiento Ceramico Nanotecnologico', 'Aplicación de recubrimiento cerámico 9H en carrocería por 2 años, repelencia extrema, protección contra rayos UV y brillo cristal.', 250.00, 360);
