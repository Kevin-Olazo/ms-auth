-- ms-auth: base de datos de seguridad + datos de prueba (roles y 2 usuarios).
-- Ejecutar una vez antes de arrancar ms-auth:  mysql -u root -p < script-seguridad.sql
-- Se puede volver a ejecutar sin problema (INSERT IGNORE no duplica ni pisa lo que ya existe).

CREATE DATABASE IF NOT EXISTS seguridadfacturador;
USE seguridadfacturador;

CREATE TABLE IF NOT EXISTS usuario (
	idusuario INT auto_increment NOT NULL,
	nomusuario varchar(100) NULL,
	email varchar(200) NULL,
	password varchar(300) NULL,
	nombres varchar(100) NULL,
	apellidos varchar(100) NULL,
	activo BOOL NULL,
	CONSTRAINT users_pk PRIMARY KEY (idusuario)
);

CREATE TABLE IF NOT EXISTS rol (
	idrol INT auto_increment NOT NULL,
	nomrol varchar(300) NULL,
	CONSTRAINT roles_pk PRIMARY KEY (idrol)
);

CREATE TABLE IF NOT EXISTS usuario_rol (
	idusuario INT NOT NULL,
	idrol INT NOT NULL,
	CONSTRAINT user_role_pk PRIMARY KEY (idusuario, idrol),
	CONSTRAINT user_role_FK FOREIGN KEY (idusuario) REFERENCES usuario(idusuario),
	CONSTRAINT user_role_FK_1 FOREIGN KEY (idrol) REFERENCES rol(idrol)
);

-- ---------------------------------------------------------------------------
-- Roles. Los nombres ADMIN y VENDEDOR los usa el api-gateway en su SecurityConfig.
-- ---------------------------------------------------------------------------
INSERT IGNORE INTO rol (idrol, nomrol) VALUES
	(1, 'ADMIN'),
	(2, 'VENDEDOR');

-- ---------------------------------------------------------------------------
-- Usuarios de PRUEBA (solo desarrollo). La columna password guarda el hash BCrypt
-- (lo que Spring Security compara al hacer login), nunca la clave en texto plano.
--   admin     / Adm-39678786dd   -> rol ADMIN
--   vendedor  / Ven-33aedb08e3    -> rol VENDEDOR
-- Para crear mas usuarios usa POST /api/v1/usuarios (ms-auth encripta la clave solo).
-- ---------------------------------------------------------------------------
INSERT IGNORE INTO usuario (idusuario, nomusuario, email, password, nombres, apellidos, activo) VALUES
	(1, 'admin',    'admin@gases-industriales.local',    '$2a$10$wLP2S83p2FwCS2mR6QSZp.WoAWmQPL1C3GEkkeiprAUZe29t7MSNe', 'Administrador', 'Sistema', 1),
	(2, 'vendedor', 'vendedor@gases-industriales.local', '$2a$10$2mYf/Kai1OuOE2Luw9VnqOUL6FGjaC8ESDrTyWh3MWaLzHyX8mB7e', 'Usuario',       'Vendedor', 1);

INSERT IGNORE INTO usuario_rol (idusuario, idrol) VALUES
	(1, 1),
	(2, 2);
