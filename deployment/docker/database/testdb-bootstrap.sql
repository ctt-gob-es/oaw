
CREATE SCHEMA `oaw` DEFAULT CHARACTER SET utf8;
use oaw;

CREATE TABLE IF NOT EXISTS `ambitos_lista` (
	id_ambito BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50)  NOT NULL , 
  descripcion VARCHAR(1024) NULL,
	PRIMARY KEY (id_ambito)
) ROW_FORMAT=COMPRESSED;

INSERT INTO ambitos_lista (nombre, descripcion) VALUES ('AGE', 'General State Administration'), ('CCAA', 'Autonomous Communities'), ('EELL', 'Local Entities'), ('Other', 'Other');


CREATE TABLE IF NOT EXISTS `basic_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usr` varchar(250) COLLATE utf8_bin DEFAULT NULL,
  `language` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `domain` varchar(2048) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `depth` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `report` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `status` varchar(25) COLLATE utf8_bin NOT NULL DEFAULT 'launched',
  `send_date` datetime DEFAULT NULL,
  `scheduling_date` datetime DEFAULT NULL,
  `analysis_type` varchar(20) COLLATE utf8_bin NOT NULL,
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  `register_result` tinyint(1) NOT NULL DEFAULT '0',
  `complexity` VARCHAR(128) NULL,
  `filename` VARCHAR(1024) NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `cartucho` (
  `id_cartucho` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `instalado` tinyint(1) NOT NULL,
  `aplicacion` varchar(100) COLLATE utf8_bin NOT NULL,
  `numrastreos` int(11) NOT NULL,
  `numhilos` int(11) NOT NULL,
  `id_guideline` int(11) NOT NULL,
  PRIMARY KEY (`id_cartucho`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;

INSERT INTO cartucho (nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES ('es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-2004', 15, 50, 1), ('es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-2012', 15, 50, 2), ('es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-2012-B', 15, 50, 3), ('es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-EN301549:2019 (beta)', 15, 50, 4), ('es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'Accesibilidad', 15, 50, 5);


CREATE TABLE IF NOT EXISTS `cartucho_rastreo` (
  `id_cartucho` bigint(20) NOT NULL,
  `id_rastreo` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cartucho`,`id_rastreo`),
  KEY `id_rastreo` (`id_rastreo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoria` varchar(256) NOT NULL,
  `umbral` double NOT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `categorias_lista` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8_bin NOT NULL,
  `orden` int(11) NOT NULL,
  `clave` VARCHAR(1024) NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `categoria_termino` (
  `id_categoria` bigint(20) NOT NULL,
  `id_termino` bigint(20) NOT NULL,
  `porcentaje` double NOT NULL,
  `porcentaje_normalizado` double NOT NULL,
  PRIMARY KEY (`id_categoria`,`id_termino`),
  KEY `id_termino` (`id_termino`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `clasificacion_etiqueta` (
	id_clasificacion BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50) NOT NULL , 
	PRIMARY KEY (id_clasificacion)
) ROW_FORMAT=COMPRESSED;

INSERT INTO clasificacion_etiqueta (id_clasificacion, nombre) VALUES ('1', 'Thematic'), ('2', 'Distribution'), ('3', 'Recurrence'), ('4', 'Other');


CREATE TABLE IF NOT EXISTS `complejidades_lista` ( 
	id_complejidad BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50) NOT NULL , 
	profundidad BIGINT(20) NOT NULL , 
	amplitud BIGINT(20) NOT NULL , 
	PRIMARY KEY (id_complejidad)
) ROW_FORMAT=COMPRESSED;

INSERT INTO complejidades_lista (nombre, profundidad, amplitud) VALUES ('Low', 2, 2), ('Medium', 4, 8), ('High', 4, 11);


CREATE TABLE IF NOT EXISTS `cuenta_cliente` (
  `id_cuenta` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `dominio` bigint(20) NOT NULL,
  `id_periodicidad` bigint(20) NOT NULL,
  `profundidad` int(11) NOT NULL DEFAULT '0',
  `amplitud` int(11) NOT NULL DEFAULT '0',
  `fecha_inicio` datetime DEFAULT NULL,
  `lista_rastreable` bigint(20) DEFAULT NULL,
  `lista_no_rastreable` bigint(20) DEFAULT NULL,
  `id_guideline` bigint(20) DEFAULT NULL,
  `pseudoaleatorio` tinyint(1) DEFAULT NULL,
  `id_language` bigint(20) NOT NULL,
  `activo` tinyint(1) NOT NULL,
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_cuenta`),
  KEY `id_periodicidad` (`id_periodicidad`),
  KEY `dominio` (`dominio`),
  KEY `lista_rastreable` (`lista_rastreable`),
  KEY `lista_no_rastreable` (`lista_no_rastreable`),
  KEY `id_language` (`id_language`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `cuenta_cliente_cartucho` (
  `id_cuenta` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cuenta`,`id_cartucho`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `cuenta_cliente_usuario` (
  `id_cuenta` bigint(20) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cuenta`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `dependencia` (
  id_dependencia bigint(20) NOT NULL AUTO_INCREMENT,
  nombre varchar(200) CHARACTER SET latin1 COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (id_dependencia),
  UNIQUE KEY nombre (nombre),
  KEY id_dependencia (id_dependencia)
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `enlaces_rotos` (
  `id_rastreo_realizado` bigint(20) NOT NULL,
  `url` varchar(255) COLLATE utf8_bin NOT NULL,
  `num_enlaces` int(11) NOT NULL,
  PRIMARY KEY (`id_rastreo_realizado`,`url`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `etiqueta` (
	id_etiqueta BIGINT(20) NOT NULL AUTO_INCREMENT , 
	nombre VARCHAR(50) NOT NULL , 
	id_clasificacion BIGINT(20) NOT NULL , 
	PRIMARY KEY (id_etiqueta),
  UNIQUE KEY nombre (nombre)
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `export_aspect_score` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aspect` varchar(255) COLLATE utf8_bin NOT NULL,
  `score` decimal(19,2) NOT NULL,
  `idCategory` bigint(20) DEFAULT NULL,
  `idExecution` bigint(20) DEFAULT NULL,
  `idPage` bigint(20) DEFAULT NULL,
  `idSite` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK39330E96833FBD5F` (`idPage`),
  KEY `FK39330E96D83469BD` (`idCategory`),
  KEY `FK39330E968342B6CF` (`idSite`),
  KEY `FK39330E96B3ADA411` (`idExecution`),
  KEY `FK39330E96B26C93F` (`idPage`),
  KEY `FK39330E96BBF2859D` (`idCategory`),
  KEY `FK39330E96B29C2AF` (`idSite`),
  KEY `FK39330E965BDB7C31` (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `export_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idCrawlerCategory` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `numA` int(11) NOT NULL,
  `numAA` int(11) NOT NULL,
  `numNV` int(11) NOT NULL,
  `scoreA` decimal(19,2) NOT NULL,
  `scoreAA` decimal(19,2) NOT NULL,
  `scoreNV` decimal(19,2) NOT NULL,
  `idExecution` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA67C529B3ADA411` (`idExecution`),
  KEY `FKA67C5295BDB7C31` (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `export_observatory` (
  `idExecution` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `numA` int(11) NOT NULL,
  `numAA` int(11) NOT NULL,
  `numNV` int(11) NOT NULL,
  PRIMARY KEY (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `export_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` varchar(255) COLLATE utf8_bin NOT NULL,
  `score` decimal(19,2) NOT NULL,
  `scoreLevel1` decimal(19,2) NOT NULL,
  `scoreLevel2` decimal(19,2) NOT NULL,
  `url` varchar(2050) COLLATE utf8_bin DEFAULT NULL,
  `idSite` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8322383A8342B6CF` (`idSite`),
  KEY `FK8322383AB29C2AF` (`idSite`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `export_site` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idCrawlerSeed` bigint(20) DEFAULT NULL,
  `level` varchar(255) COLLATE utf8_bin NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `numA` int(11) NOT NULL,
  `numAA` int(11) NOT NULL,
  `numNV` int(11) NOT NULL,
  `score` decimal(19,2) DEFAULT NULL,
  `scoreLevel1` decimal(19,2) DEFAULT NULL,
  `scoreLevel2` decimal(19,2) DEFAULT NULL,
  `idCategory` bigint(20) NOT NULL,
  `compliance` VARCHAR(32) NULL,
  PRIMARY KEY (`id`),
  KEY `FK8323B4F2D83469BD` (`idCategory`),
  KEY `FK8323B4F2BBF2859D` (`idCategory`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `export_verification_modality` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `failPercentage` decimal(19,2) NOT NULL,
  `passPercentage` decimal(19,2) NOT NULL,
  `verification` varchar(255) COLLATE utf8_bin NOT NULL,
  `idCategory` bigint(20) DEFAULT NULL,
  `idExecution` bigint(20) DEFAULT NULL,
  `idSite` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC9F9E23AD83469BD` (`idCategory`),
  KEY `FKC9F9E23A8342B6CF` (`idSite`),
  KEY `FKC9F9E23AB3ADA411` (`idExecution`),
  KEY `FKC9F9E23ABBF2859D` (`idCategory`),
  KEY `FKC9F9E23AB29C2AF` (`idSite`),
  KEY `FKC9F9E23A5BDB7C31` (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `export_verification_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `modality` varchar(255) COLLATE utf8_bin NOT NULL,
  `value` int(11) DEFAULT NULL,
  `verification` varchar(255) COLLATE utf8_bin NOT NULL,
  `idPage` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK724BD668833FBD5F` (`idPage`),
  KEY `FK724BD668B26C93F` (`idPage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `export_verification_score` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `score` decimal(19,2) DEFAULT NULL,
  `verification` varchar(255) COLLATE utf8_bin NOT NULL,
  `idCategory` bigint(20) DEFAULT NULL,
  `idExecution` bigint(20) DEFAULT NULL,
  `idSite` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD75A45D9D83469BD` (`idCategory`),
  KEY `FKD75A45D98342B6CF` (`idSite`),
  KEY `FKD75A45D9B3ADA411` (`idExecution`),
  KEY `FKD75A45D9BBF2859D` (`idCategory`),
  KEY `FKD75A45D9B29C2AF` (`idSite`),
  KEY `FKD75A45D95BDB7C31` (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `languages` (
  `id_language` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(45) COLLATE utf8_bin NOT NULL,
  `codice` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_language`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;

INSERT INTO `languages` (`key_name`, `codice`) VALUES ('idioma.espanol', 'es'), ('idioma.ingles', 'en');


CREATE TABLE IF NOT EXISTS `lista` (
  `id_lista` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_tipo_lista` bigint(20) NOT NULL,
  `nombre` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `lista` text COLLATE utf8_bin NOT NULL,
  `id_categoria` bigint(20) DEFAULT NULL,
  `acronimo` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `activa` tinyint(1) DEFAULT '1',
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  `eliminar` BIGINT(20) NOT NULL DEFAULT '0',
  `id_complejidad` BIGINT(20),
  `id_ambito` BIGINT(20) NULL DEFAULT NULL,
  `observaciones` VARCHAR(1024) NULL,
  PRIMARY KEY (`id_lista`),
  UNIQUE KEY `id_lista` (`id_lista`),
  KEY `id_tipo_lista` (`id_tipo_lista`),
  KEY `id_categoria` (`id_categoria`),
  KEY `id_ambito` (`id_ambito`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio` (
  `id_observatorio` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `id_periodicidad` bigint(20) NOT NULL,
  `profundidad` int(11) NOT NULL DEFAULT '0',
  `amplitud` int(11) NOT NULL DEFAULT '0',
  `fecha_inicio` datetime DEFAULT NULL,
  `id_guideline` bigint(20) DEFAULT NULL,
  `pseudoaleatorio` tinyint(1) DEFAULT NULL,
  `id_language` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  `activo` tinyint(1) NOT NULL,
  `id_tipo` bigint(20) DEFAULT NULL,
  `id_ambito` BIGINT(20),
  `tags` VARCHAR(1024) NULL,
  PRIMARY KEY (`id_observatorio`),
  KEY `id_periodicidad` (`id_periodicidad`),
  KEY `id_language` (`id_language`),
  KEY `id_cartucho` (`id_cartucho`),
  KEY `id_tipo` (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio_ambito` ( 
	id_observatorio BIGINT(20) NOT NULL , 
	id_ambito BIGINT(20) NOT NULL 
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio_categoria` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_categoria` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_categoria`),
  KEY `id_categoria` (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio_complejidad` (
  id_observatorio BIGINT(20) NOT NULL,
  id_complejidad INT(20) NOT NULL
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio_estado` (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador autoincrementable',
  id_observatorio int(11) NOT NULL COMMENT 'Identificador del observatorio',
  id_ejecucion_observatorio int(11) NOT NULL COMMENT 'Identificador de la ejecución del observatorio',
  nombre varchar(256) NOT NULL COMMENT 'Nombre de la semilla',
  url varchar(256) NOT NULL COMMENT 'URL de la semilla',
  total_url int(11) NOT NULL COMMENT 'Total del URLs que se analizarán',
  total_url_analizadas int(11) DEFAULT '0',
  ultima_url varchar(8000) COMMENT 'Última URL analizada',
  fecha_ultima_url datetime DEFAULT NULL COMMENT 'Fecha del fin de la última URL analizada',
  actual_url varchar(8000) COMMENT 'URL que se está analizando',
  tiempo_medio int(11) DEFAULT NULL COMMENT 'Tiempo medio de análisis',
  tiempo_acumulado int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_observatorio (id_observatorio,id_ejecucion_observatorio)
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio_lista` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_lista` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_lista`),
  KEY `id_lista` (`id_lista`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio_metodologia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_obs_realizado` bigint(20) DEFAULT NULL,
  `metodologia` mediumtext COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  KEY `id_obs_realizado` (`id_obs_realizado`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio_plantillas` (
  id_plantilla int(11)  NOT NULL AUTO_INCREMENT,
  nombre varchar(1024) NOT NULL,
  documento LONGBLOB NOT NULL,
  PRIMARY KEY (id_plantilla)
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorio_proxy` (
  status tinyint(1) NOT NULL,
  url varchar(64) NOT NULL,
  port varchar(5) NOT NULL
) ROW_FORMAT=COMPRESSED;

INSERT INTO observatorio_proxy (status, url, port) VALUES(1, '127.0.0.1', '18088');


CREATE TABLE IF NOT EXISTS `observatorio_tipo` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;

INSERT INTO observatorio_tipo (name) VALUES ('AGE'), ('CCAA'), ('EELL'), ('OTHER');


CREATE TABLE IF NOT EXISTS `observatorio_usuario` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `observatorios_realizados` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_observatorio` bigint(20) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `id_cartucho` bigint(20) DEFAULT NULL,
  `estado` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id_observatorio` (`id_observatorio`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `periodicidad` (
  `id_periodicidad` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(15) COLLATE utf8_bin NOT NULL,
  `dias` int(11) DEFAULT NULL,
  `cronExpression` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_periodicidad`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;

INSERT INTO `periodicidad` (`nombre`, `dias`, `cronExpression`) VALUES ('Daily', 1, NULL), ('Weekly', 7, NULL), ('Every 15 days', 15, NULL), ('Monthly', NULL, '0 min hour daymonth month/1 ? year/1'), ('Quarterly', NULL, '0 min hour daymonth month/3 ? year/1'), ('6 Months', NULL, '0 min hour daymonth month/6 ? year/1'), ('Yearly', NULL, '0 min hour daymonth month ? year/1');


CREATE TABLE IF NOT EXISTS `rastreo` (
  `id_rastreo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre_rastreo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `profundidad` int(11) NOT NULL,
  `topn` int(11) NOT NULL,
  `semillas` bigint(20) NOT NULL,
  `fecha_lanzado` datetime DEFAULT NULL,
  `lista_no_rastreable` bigint(20) DEFAULT NULL,
  `lista_rastreable` bigint(20) DEFAULT NULL,
  `estado` int(11) NOT NULL,
  `id_cuenta` bigint(20) DEFAULT NULL,
  `id_observatorio` bigint(20) DEFAULT NULL,
  `id_guideline` bigint(20) DEFAULT NULL,
  `automatico` tinyint(1) NOT NULL DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `pseudoaleatorio` tinyint(1) DEFAULT NULL,
  `id_language` bigint(20) NOT NULL,
  `exhaustive` tinyint(1) NOT NULL,
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_rastreo`),
  KEY `id_cuenta` (`id_cuenta`),
  KEY `id_observatorio` (`id_observatorio`),
  KEY `semillas` (`semillas`),
  KEY `id_language` (`id_language`),
  KEY `rastreo_ibfk_4` (`lista_rastreable`),
  KEY `rastreo_ibfk_5` (`lista_no_rastreable`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `rastreos_realizados` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_rastreo` bigint(20) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `id_usuario` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  `id_obs_realizado` bigint(20) DEFAULT NULL,
  `id_lista` bigint(20) DEFAULT NULL,
  `level` VARCHAR(128),
  `score` VARCHAR(32),
  PRIMARY KEY (`id`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_rastreo` (`id_rastreo`),
  KEY `id_cartucho` (`id_cartucho`),
  KEY `id_obs_realizado` (`id_obs_realizado`),
  KEY `id_lista` (`id_lista`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `roles` (
  `id_rol` bigint(20) NOT NULL AUTO_INCREMENT,
  `rol` varchar(50) COLLATE utf8_bin NOT NULL,
  `id_tipo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  KEY `id_tipo` (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;

INSERT INTO `roles` (`rol`, `id_tipo`) VALUES ('Administrador', 1), ('Configurador', 1), ('Visualizador', 1), ('Responsable cliente', 2), ('Visualizador cliente', 2), ('Observatorio', 3);


CREATE TABLE IF NOT EXISTS `semilla_dependencia` (
	id_lista bigint(20), 
	id_dependencia bigint(20), 
	PRIMARY KEY pk_semilla_dependencia (id_lista,id_dependencia)
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `semilla_etiqueta` (
  id_lista bigint(20) NOT NULL DEFAULT 0,
  id_etiqueta bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (id_lista,id_etiqueta),
  KEY semilla_etiqueta_ibfk_1 (id_etiqueta),
  CONSTRAINT semilla_etiqueta_ibfk_1 FOREIGN KEY (id_etiqueta) REFERENCES etiqueta (id_etiqueta) ON DELETE CASCADE ON UPDATE CASCADE
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `tanalisis` (
  `cod_analisis` bigint(20) NOT NULL AUTO_INCREMENT,
  `fec_analisis` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cod_url` varchar(2050) DEFAULT NULL,
  `num_duracion` bigint(20) DEFAULT NULL,
  `nom_entidad` varchar(100) NOT NULL,
  `cod_rastreo` bigint(20) NOT NULL,
  `cod_guideline` bigint(20) NOT NULL,
  `checks_ejecutados` text,
  `estado` int(11) NOT NULL,
  `cod_fuente` mediumtext,
  `ref_cod_fuente` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cod_analisis`),
  KEY `cod_guideline` (`cod_guideline`),
  KEY `basic_service` (`nom_entidad`,`cod_rastreo`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

CREATE INDEX tanalisis_cod_rastreo ON tanalisis (cod_rastreo);


CREATE TABLE IF NOT EXISTS `tanalisis_accesibilidad` (
	id INT NOT NULL AUTO_INCREMENT,
	id_analisis INT NOT NULL,
	urls VARCHAR(2048) NOT NULL,
	PRIMARY KEY (id)
) ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `tanalisis_css` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(2050) DEFAULT NULL,
  `codigo` mediumtext,
  `cod_analisis` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `cod_analisis` (`cod_analisis`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `termino` (
  `id_termino` bigint(20) NOT NULL AUTO_INCREMENT,
  `termino` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_termino`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `tguidelines` (
  `cod_guideline` bigint(20) NOT NULL AUTO_INCREMENT,
  `des_guideline` varchar(50) NOT NULL,
  PRIMARY KEY (`cod_guideline`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

INSERT INTO tguidelines (des_guideline) VALUES ('observatorio-inteco-1-0.xml'), ('observatorio-une-2012.xml'), ('observatorio-une-2012-b.xml'), ('observatorio-une-en2019.xml'), ('observatorio-accesibilidad.xml');


CREATE TABLE IF NOT EXISTS `tincidencia` (
  `cod_incidencia` bigint(20) NOT NULL DEFAULT '0',
  `cod_comprobacion` bigint(20) NOT NULL,
  `cod_analisis` bigint(20) NOT NULL,
  `cod_linea_fuente` bigint(20) NOT NULL,
  `cod_columna_fuente` bigint(20) NOT NULL,
  `des_fuente` text,
  KEY `cod_analisis` (`cod_analisis`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `tipo_lista` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `tipo_rol` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;

INSERT INTO `tipo_rol` (`nombre`) VALUES ('Normal'), ('Client'), ('Observatory');


CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) COLLATE utf8_bin NOT NULL,
  `password` varchar(100) COLLATE utf8_bin NOT NULL,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `apellidos` varchar(150) COLLATE utf8_bin NOT NULL,
  `departamento` varchar(100) COLLATE utf8_bin NOT NULL,
  `email` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `usuario_cartucho` (
  `id_usuario` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_cartucho`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;


CREATE TABLE IF NOT EXISTS `usuario_rol` (
  `usuario` bigint(20) NOT NULL,
  `id_rol` bigint(20) NOT NULL,
  PRIMARY KEY (`usuario`,`id_rol`),
  KEY `id_rol` (`id_rol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPRESSED;

INSERT INTO `usuario` (`usuario`, `password`, `nombre`, `apellidos`, `departamento`, `email`) VALUES ('username', md5('password'), 'Nombre', 'Apellidos', 'Departamento', 'email@mail.com');

INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5);
INSERT INTO usuario_rol (usuario, id_rol) VALUES (1, 1);


CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `oaw_dashboard` AS select `basic_service`.`id` AS `id`,`basic_service`.`usr` AS `usr`,`basic_service`.`domain` AS `url`,`basic_service`.`email` AS `email`,`basic_service`.`depth` AS `depth`,`basic_service`.`width` AS `width`,`basic_service`.`report` AS `report`,`basic_service`.`date` AS `date`,`basic_service`.`status` AS `status`,`basic_service`.`analysis_type` AS `analysis_type`,`basic_service`.`in_directory` AS `in_directory` from `basic_service`;


--
-- Seed some more data into the database to be able to test the application easier
--

INSERT INTO `periodicidad` (`nombre`, `dias`, `cronExpression`) VALUES('Every 5 Minutes', NULL, '0 0/5 * * * ?');
