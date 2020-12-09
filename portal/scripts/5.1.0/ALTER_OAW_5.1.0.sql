ALTER TABLE basic_service ADD depthReport VARCHAR(128) NULL;
INSERT INTO clasificacion_etiqueta (id_clasificacion, nombre) VALUES ('4', 'Otros');

ALTER TABLE categorias_lista ADD principal INT NULL DEFAULT 0