ALTER TABLE categorias_lista ADD clave VARCHAR(1024) NULL;
ALTER TABLE lista ADD observaciones VARCHAR(1024) NULL;
ALTER table export_site ADD COLUMN compliance VARCHAR(32) NULL;


TRUNCATE tanalisis_accesibilidad;
ALTER TABLE tanalisis_accesibilidad CHANGE COLUMN urls url VARCHAR(256) NULL DEFAULT NULL ;
ALTER TABLE tanalisis_accesibilidad ADD UNIQUE INDEX id_analisis_UNIQUE (id_analisis,url);
ALTER TABLE tanalisis_accesibilidad ADD COLUMN checks_ok INT NULL DEFAULT 0;
