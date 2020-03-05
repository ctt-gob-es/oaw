-- Nuevas metodologías
INSERT INTO tguidelines (cod_guideline, des_guideline) VALUES (9, 'observatorio-une-en2019.xml');
INSERT INTO tguidelines (cod_guideline, des_guideline) VALUES (10, 'observatorio-accesibilidad.xml');
INSERT INTO cartucho (id_cartucho, nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES (9, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-EN301549:2019 (beta)', 15, 50, 9);
INSERT INTO cartucho (id_cartucho, nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES (10, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'Accesibilidad', 15, 50, 10);
INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES(1, 9);
INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES(1, 10);

-- Nuevo tipo observatorio
INSERT INTO observatorio_tipo (id_tipo, name) VALUES ('4', 'OTROS');

-- Campo borrado en smillas
ALTER TABLE lista ADD eliminar BIGINT(20) NOT NULL DEFAULT '0';

-- Ämbitos
CREATE TABLE ambitos_lista (
	id_ambito BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50)  NOT NULL , 
	PRIMARY KEY (id_ambito)
) ;
INSERT INTO ambitos_lista (id_ambito, nombre) VALUES ('1', 'AGE'), ('2', 'CCAA'), ('3', 'EELL'), ('4', 'Otros');

CREATE TABLE observatorio_ambito ( 
	id_observatorio BIGINT(20) NOT NULL , 
	id_ambito BIGINT(20) NOT NULL 
);

ALTER TABLE observatorio ADD id_ambito BIGINT(20);

-- Complejidades
CREATE TABLE complejidades_lista ( 
	id_complejidad BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50) NOT NULL , 
	profundidad BIGINT(20) NOT NULL , 
	amplitud BIGINT(20) NOT NULL , 
	PRIMARY KEY (id_complejidad)
);

INSERT INTO complejidades_lista (id_complejidad, nombre, profundidad, amplitud) VALUES(1, 'Baja', 2, 2);
INSERT INTO complejidades_lista (id_complejidad, nombre, profundidad, amplitud) VALUES(2, 'Media', 4, 8);
INSERT INTO complejidades_lista (id_complejidad, nombre, profundidad, amplitud) VALUES(3, 'Alta', 4, 11);

CREATE TABLE observatorio_complejidad ( id_observatorio BIGINT(20) NOT NULL , id_complejidad INT(20) NOT NULL ) ;

ALTER TABLE lista ADD id_complejidad BIGINT(20);
ALTER TABLE lista ADD id_ambito BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE lista ADD KEY id_ambito (id_ambito);

-- Por defecto todas media
UPDATE lista SET id_complejidad=2;

CREATE TABLE etiqueta (
	id_etiqueta BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50) NOT NULL , 
	id_clasificacion BIGINT(20) NOT NULL , 
	PRIMARY KEY (id_etiqueta)
);

CREATE TABLE clasificacion_etiqueta (
	id_clasificacion BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50) NOT NULL , 
	PRIMARY KEY (id_clasificacion)
);

INSERT INTO clasificacion_etiqueta (id_clasificacion, nombre) VALUES ('1', 'Temática'), ('2', 'Distribución'), ('3', 'Recurrencia');


CREATE TABLE semilla_etiqueta (
  id_lista bigint(20) NOT NULL DEFAULT 0,
  id_etiqueta bigint(20) NOT NULL DEFAULT 0
);

ALTER TABLE semilla_etiqueta ADD PRIMARY KEY (id_lista,id_etiqueta), ADD KEY semilla_etiqueta_ibfk_1 (id_etiqueta);
ALTER TABLE semilla_etiqueta ADD CONSTRAINT semilla_etiqueta_ibfk_1 FOREIGN KEY (id_etiqueta) REFERENCES etiqueta (id_etiqueta) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE rastreos_realizados ADD level VARCHAR(128),  ADD score VARCHAR(32) ;


CREATE TABLE observatorio_plantillas (
  id_plantilla int(11)  NOT NULL AUTO_INCREMENT,
  nombre varchar(1024) NOT NULL,
  documento LONGBLOB NOT NULL,
  PRIMARY KEY (id_plantilla)
);

ALTER TABLE basic_service ADD complexity VARCHAR(128) NULL;

ALTER TABLE observatorio ADD tags VARCHAR(1024) NULL;


