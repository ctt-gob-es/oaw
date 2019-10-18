INSERT INTO tguidelines (cod_guideline, des_guideline) VALUES (9, 'observatorio-une-en2019.xml');
INSERT INTO cartucho (id_cartucho, nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES (9, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-EN2019', 15, 50, 9);
INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES(1, 9);

ALTER TABLE `lista` ADD `eliminar` TINYINT(1) NOT NULL DEFAULT '0' AFTER `in_directory`;

CREATE TABLE `oaw_js`.`ambito_lista` ( `id_ambito` BIGINT(20) NULL AUTO_INCREMENT , `nombre` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL , PRIMARY KEY (`id_ambito`)) ENGINE = InnoDB;
INSERT INTO `ambito_lista` (`id_ambito`, `nombre`) VALUES ('1', 'AGE'), ('2', 'CCAA'), ('3', 'EELL'), ('4', 'Otros');
ALTER TABLE `lista` ADD `id_ambito` BIGINT(20) NULL DEFAULT NULL AFTER `id_categoria`;
ALTER TABLE `lista`  ADD KEY `id_ambito` (`id_ambito`);

CREATE TABLE `oaw_js`.`observatorio_ambito` ( `id_observatorio` BIGINT(20) NOT NULL , `id_ambito` BIGINT(20) NOT NULL ) ENGINE = InnoDB;