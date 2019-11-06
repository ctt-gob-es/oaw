
INSERT INTO tguidelines (cod_guideline, des_guideline) VALUES (9, 'observatorio-une-en2019.xml');
INSERT INTO tguidelines (cod_guideline, des_guideline) VALUES (10, 'observatorio-accesibilidad.xml');
INSERT INTO cartucho (id_cartucho, nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES (9, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-EN2019', 15, 50, 9);
INSERT INTO cartucho (id_cartucho, nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES (10, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'Accesibilidad', 15, 50, 10);

INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES(1, 9);
INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES(1, 10);
INSERT INTO observatorio_tipo (id_tipo, name) VALUES ('4', 'OTROS');


ALTER TABLE lista ADD eliminar BIGINT(20) NOT NULL DEFAULT '0' AFTER in_directory;

CREATE TABLE ambito_lista (
	id_ambito BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50)  NOT NULL , 
	PRIMARY KEY (id_ambito)
) ;

INSERT INTO ambitos_lista (id_ambito, nombre) VALUES ('1', 'AGE'), ('2', 'CCAA'), ('3', 'EELL'), ('4', 'Otros');

CREATE TABLE `oaw_js`.`complejidades_lista` ( `id_complejidad` BIGINT(20) NOT NULL AUTO_INCREMENT , `nombre` VARCHAR(50) NOT NULL , `profundidad` BIGINT(20) NOT NULL , `amplitud` BIGINT(20) NOT NULL , PRIMARY KEY (`id_complejidad`));
CREATE TABLE `oaw_js`.`observatorio_complejidad` ( `id_observatorio` BIGINT(20) NOT NULL , `id_complejidad` INT(20) NOT NULL ) ;
ALTER TABLE `lista` ADD `id_complejidad` BIGINT(20);
INSERT INTO `complejidades_lista` (`id_complejidad`, `nombre`, `profundidad`, `amplitud`) VALUES ('1', 'Baja', '4', '4'), ('2', 'Media', '4', '8'), ('3', 'Alta', '4', '11')

ALTER TABLE lista ADD id_ambito BIGINT(20) NULL DEFAULT NULL AFTER id_categoria;
ALTER TABLE lista  ADD KEY id_ambito (id_ambito);

CREATE TABLE observatorio_ambito ( 
	id_observatorio BIGINT(20) NOT NULL , 
	id_ambito BIGINT(20) NOT NULL 
);

ALTER TABLE observatorio ADD id_ambito BIGINT(20);

CREATE TABLE `oaw_js`.`etiqueta` ( `id_etiqueta` BIGINT(20) NOT NULL AUTO_INCREMENT , `nombre` VARCHAR(50) NOT NULL , `id_clasificacion` BIGINT(20) NOT NULL , PRIMARY KEY (`id_etiqueta`));

CREATE TABLE `oaw_js`.`clasificacion_etiqueta` ( `id_clasificacion` BIGINT(20) NOT NULL AUTO_INCREMENT , `nombre` VARCHAR(50) NOT NULL , PRIMARY KEY (`id_clasificacion`));
INSERT INTO `clasificacion_etiqueta` (`id_clasificacion`, `nombre`) VALUES ('1', 'Temática'), ('2', 'Distribución'), ('3', 'Recurrencia');

