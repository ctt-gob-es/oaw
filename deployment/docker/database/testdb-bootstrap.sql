
CREATE SCHEMA `OAW` DEFAULT CHARACTER SET utf8 ;
use OAW;

--
-- 000_INIT_DATABASE.sql
--

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
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=39 ;


CREATE TABLE IF NOT EXISTS `cartucho` (
  `id_cartucho` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `instalado` tinyint(1) NOT NULL,
  `aplicacion` varchar(100) COLLATE utf8_bin NOT NULL,
  `numrastreos` int(11) NOT NULL,
  `numhilos` int(11) NOT NULL,
  `id_guideline` int(11) NOT NULL,
  PRIMARY KEY (`id_cartucho`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=9 ;



CREATE TABLE IF NOT EXISTS `cartucho_rastreo` (
  `id_cartucho` bigint(20) NOT NULL,
  `id_rastreo` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cartucho`,`id_rastreo`),
  KEY `id_rastreo` (`id_rastreo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoria` varchar(50) COLLATE utf8_bin NOT NULL,
  `umbral` double NOT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;



CREATE TABLE IF NOT EXISTS `categorias_lista` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8_bin NOT NULL,
  `orden` int(11) NOT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=9 ;



CREATE TABLE IF NOT EXISTS `categoria_termino` (
  `id_categoria` bigint(20) NOT NULL,
  `id_termino` bigint(20) NOT NULL,
  `porcentaje` double NOT NULL,
  `porcentaje_normalizado` double NOT NULL,
  PRIMARY KEY (`id_categoria`,`id_termino`),
  KEY `id_termino` (`id_termino`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=2 ;



CREATE TABLE IF NOT EXISTS `cuenta_cliente_cartucho` (
  `id_cuenta` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cuenta`,`id_cartucho`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `cuenta_cliente_usuario` (
  `id_cuenta` bigint(20) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cuenta`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `enlaces_rotos` (
  `id_rastreo_realizado` bigint(20) NOT NULL,
  `url` varchar(255) COLLATE utf8_bin NOT NULL,
  `num_enlaces` int(11) NOT NULL,
  PRIMARY KEY (`id_rastreo_realizado`,`url`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;



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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;



CREATE TABLE IF NOT EXISTS `export_observatory` (
  `idExecution` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `numA` int(11) NOT NULL,
  `numAA` int(11) NOT NULL,
  `numNV` int(11) NOT NULL,
  PRIMARY KEY (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;


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
  PRIMARY KEY (`id`),
  KEY `FK8323B4F2D83469BD` (`idCategory`),
  KEY `FK8323B4F2BBF2859D` (`idCategory`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;



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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;


CREATE TABLE IF NOT EXISTS `export_verification_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `modality` varchar(255) COLLATE utf8_bin NOT NULL,
  `value` int(11) DEFAULT NULL,
  `verification` varchar(255) COLLATE utf8_bin NOT NULL,
  `idPage` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK724BD668833FBD5F` (`idPage`),
  KEY `FK724BD668B26C93F` (`idPage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;



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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;


CREATE TABLE IF NOT EXISTS `languages` (
  `id_language` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(45) COLLATE utf8_bin NOT NULL,
  `codice` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_language`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=3 ;



CREATE TABLE IF NOT EXISTS `lista` (
  `id_lista` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_tipo_lista` bigint(20) NOT NULL,
  `nombre` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `lista` text COLLATE utf8_bin NOT NULL,
  `id_categoria` bigint(20) DEFAULT NULL,
  `acronimo` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `dependencia` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `activa` tinyint(1) DEFAULT '1',
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_lista`),
  UNIQUE KEY `id_lista` (`id_lista`),
  KEY `id_tipo_lista` (`id_tipo_lista`),
  KEY `id_categoria` (`id_categoria`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=535 ;


CREATE TABLE IF NOT EXISTS `oaw_dashboard` (
`id` bigint(20)
,`usr` varchar(250)
,`url` varchar(2048)
,`email` varchar(100)
,`depth` int(11)
,`width` int(11)
,`report` varchar(30)
,`date` datetime
,`status` varchar(25)
,`analysis_type` varchar(20)
,`in_directory` tinyint(1)
);


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
  PRIMARY KEY (`id_observatorio`),
  KEY `id_periodicidad` (`id_periodicidad`),
  KEY `id_language` (`id_language`),
  KEY `id_cartucho` (`id_cartucho`),
  KEY `id_tipo` (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=6 ;


CREATE TABLE IF NOT EXISTS `observatorios_realizados` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_observatorio` bigint(20) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `id_cartucho` bigint(20) DEFAULT NULL,
  `estado` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id_observatorio` (`id_observatorio`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=9 ;


CREATE TABLE IF NOT EXISTS `observatorio_categoria` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_categoria` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_categoria`),
  KEY `id_categoria` (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `observatorio_lista` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_lista` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_lista`),
  KEY `id_lista` (`id_lista`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `observatorio_metodologia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_obs_realizado` bigint(20) DEFAULT NULL,
  `metodologia` mediumtext COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  KEY `id_obs_realizado` (`id_obs_realizado`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=9 ;


CREATE TABLE IF NOT EXISTS `observatorio_tipo` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=4 ;


CREATE TABLE IF NOT EXISTS `observatorio_usuario` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `periodicidad` (
  `id_periodicidad` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(15) COLLATE utf8_bin NOT NULL,
  `dias` int(11) DEFAULT NULL,
  `cronExpression` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_periodicidad`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=8 ;


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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1069 ;


CREATE TABLE IF NOT EXISTS `rastreos_realizados` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_rastreo` bigint(20) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `id_usuario` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  `id_obs_realizado` bigint(20) DEFAULT NULL,
  `id_lista` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_rastreo` (`id_rastreo`),
  KEY `id_cartucho` (`id_cartucho`),
  KEY `id_obs_realizado` (`id_obs_realizado`),
  KEY `id_lista` (`id_lista`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=278 ;


CREATE TABLE IF NOT EXISTS `roles` (
  `id_rol` bigint(20) NOT NULL AUTO_INCREMENT,
  `rol` varchar(50) COLLATE utf8_bin NOT NULL,
  `id_tipo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  KEY `id_tipo` (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=7 ;


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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2309 ;


CREATE TABLE IF NOT EXISTS `tanalisis_css` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(2050) DEFAULT NULL,
  `codigo` mediumtext,
  `cod_analisis` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `cod_analisis` (`cod_analisis`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=28818 ;


CREATE TABLE IF NOT EXISTS `termino` (
  `id_termino` bigint(20) NOT NULL AUTO_INCREMENT,
  `termino` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_termino`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;


CREATE TABLE IF NOT EXISTS `tguidelines` (
  `cod_guideline` bigint(20) NOT NULL AUTO_INCREMENT,
  `des_guideline` varchar(50) NOT NULL,
  PRIMARY KEY (`cod_guideline`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;


CREATE TABLE IF NOT EXISTS `tincidencia` (
  `cod_incidencia` bigint(20) NOT NULL DEFAULT '0',
  `cod_comprobacion` bigint(20) NOT NULL,
  `cod_analisis` bigint(20) NOT NULL,
  `cod_linea_fuente` bigint(20) NOT NULL,
  `cod_columna_fuente` bigint(20) NOT NULL,
  `des_fuente` text,
  KEY `cod_analisis` (`cod_analisis`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `tipo_lista` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=5 ;


CREATE TABLE IF NOT EXISTS `tipo_rol` (
  `id_tipo` bigint(20) NOT NULL,
  `nombre` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) COLLATE utf8_bin NOT NULL,
  `password` varchar(100) COLLATE utf8_bin NOT NULL,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `apellidos` varchar(150) COLLATE utf8_bin NOT NULL,
  `departamento` varchar(100) COLLATE utf8_bin NOT NULL,
  `email` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=5 ;


CREATE TABLE IF NOT EXISTS `usuario_cartucho` (
  `id_usuario` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_cartucho`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `usuario_rol` (
  `usuario` bigint(20) NOT NULL,
  `id_rol` bigint(20) NOT NULL,
  PRIMARY KEY (`usuario`,`id_rol`),
  KEY `id_rol` (`id_rol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `oaw_dashboard`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `oaw_dashboard` AS select `basic_service`.`id` AS `id`,`basic_service`.`usr` AS `usr`,`basic_service`.`domain` AS `url`,`basic_service`.`email` AS `email`,`basic_service`.`depth` AS `depth`,`basic_service`.`width` AS `width`,`basic_service`.`report` AS `report`,`basic_service`.`date` AS `date`,`basic_service`.`status` AS `status`,`basic_service`.`analysis_type` AS `analysis_type`,`basic_service`.`in_directory` AS `in_directory` from `basic_service`;


--
-- 001_UPDATE_TABLES_OAW_4.0.0.sql
--

-- Unificar la longitud de las urls de con la tabla tanalisis
ALTER TABLE export_page MODIFY COLUMN url VARCHAR(2050);

-- Default order
ALTER TABLE categorias_lista ORDER BY nombre ASC;

-- Email admin
UPDATE usuario SET email ='observ.accesibilidad@correo.gob.es' WHERE usuario='admin';


--
-- 002_CREATE_TABLES_OAW_4.0.0.sql
--

-- SOPORTE PARA SEMILLAS CON MULTIDEPENDENCIA
-- USE oaw_pruebas_rollback;

-- TABLA DEPENDENCIA 
CREATE TABLE IF NOT EXISTS dependencia (
  id_dependencia bigint(20) NOT NULL AUTO_INCREMENT,
  nombre varchar(200) CHARACTER SET latin1 COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (id_dependencia),
  UNIQUE KEY nombre (nombre),
  KEY id_dependencia (id_dependencia)
);

-- INSERTAR LAS DEPENENCIAS SACADAS DE LA TABLA LISTA
-- INSERT INTO dependencia(nombre) SELECT DISTINCT(dependencia) FROM lista WHERE dependencia IS NOT NULL ORDER BY dependencia ASC;
INSERT INTO dependencia(nombre) SELECT DISTINCT(dependencia) FROM lista WHERE dependencia IS NOT NULL ORDER BY dependencia ASC ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);


-- ACTUALIZAMOS LAS DEPENDENCIA QUE HAN CAMBIADO DE NOMBRE
-- UPDATE dependencia SET nombre ='Ministerio de Energía, Turismo y Agenda Digital' WHERE nombre ='Ministerio de Industria, Energía y Turismo';
-- UPDATE dependencia SET nombre ='Ministerio de Hacienda y Función Pública' WHERE nombre ='Ministerio de Hacienda y Administraciones Públicas';
-- UPDATE dependencia SET nombre ='Ministerio de Presidencia' WHERE nombre ='Ministerio de la Presidencia y para las Administraciones Territoriales';
-- UPDATE dependencia SET nombre ='Ministerio de Economía y Competitividad' WHERE nombre ='Ministerio de Economía, Industria y Competitividad';
-- UPDATE dependencia SET nombre ='Ministerio de Agricultura, Alimentación y Medio Ambiente' WHERE nombre ='Ministerio de Agricultura y Pesca, Alimentación y Medio Ambiente';
 
-- TABLA SEMILLA_DEPENDENCIA
CREATE TABLE semilla_dependencia (
	id_lista bigint(20), 
	id_dependencia bigint(20), 
	PRIMARY KEY pk_semilla_dependencia (id_lista,id_dependencia));
-- foreign key (id_lista) references lista (id_lista), foreign key (id_dependencia) references dependencia (id_dependencia) <-- lista esta MyISAM que no permite FK

-- RELLENAMOS LA TABLA ANTERIOR CON  LA TABLA DE RELACION CON LAS DEPENDENCIAS DE LA NUEVA TABLA
-- INSERT INTO semilla_dependencia(id_lista, id_dependencia) SELECT l.id_lista, d.id_dependencia FROM lista l, dependencia d WHERE l.dependencia=d.nombre;
INSERT INTO semilla_dependencia(id_lista, id_dependencia) SELECT l.id_lista, d.id_dependencia FROM lista l, dependencia d  WHERE l.dependencia collate utf8_general_ci like d.nombre collate latin1_spanish_ci;


-- ELIMINAR LA COLUMNA ANTERIOR DE DEPENDENCIA
ALTER TABLE lista DROP COLUMN dependencia;

-- Revisar aquellas semillas que teniendo dependencia "string" no estén relacionadas con ninguna en semilla_dependencia o que no aparezcan en esa tabla
-- SELECT * FROM lista l WHERE l.id_lista not in (select sd.id_lista from semilla_dependencia sd) order by l.id_lista;
-- SELECT * FROM lista l WHERE l.dependencia is not null and l.id_lista not in (select sd.id_lista from semilla_dependencia sd) order by l.id_lista;


--
-- INSERT_TABLES_OAW_4.1.0.sql 
--

INSERT INTO tguidelines (cod_guideline, des_guideline) VALUES (8, 'observatorio-une-2012-b.xml');
INSERT INTO cartucho (id_cartucho, nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES (8, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-2012-B', 15, 50, 8);
INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES(1, 8);


--
-- CREATE_TABLES_OAW_4.2.0.sql 
--

DROP TABLE  IF EXISTS observatorio_estado;

CREATE TABLE observatorio_estado (
  id int(11) NOT NULL COMMENT 'Identificador autoincrementable',
  id_observatorio int(11) NOT NULL COMMENT 'Identificador del observatorio',
  id_ejecucion_observatorio int(11) NOT NULL COMMENT 'Identificador de la ejecución del observatorio',
  nombre varchar(256) NOT NULL COMMENT 'Nombre de la semilla',
  url varchar(256) NOT NULL COMMENT 'URL de la semilla',
  total_url int(11) NOT NULL COMMENT 'Total del URLs que se analizarán',
  total_url_analizadas int(11) DEFAULT '0',
  ultima_url varchar(256) NOT NULL COMMENT 'Última URL analizada',
  fecha_ultima_url datetime DEFAULT NULL COMMENT 'Fecha del fin de la última URL analizada',
  actual_url varchar(256) NOT NULL COMMENT 'URL que se está analizando',
  tiempo_medio int(11) DEFAULT NULL COMMENT 'Tiempo medio de análisis',
  tiempo_acumulado int(11) DEFAULT NULL
);

ALTER TABLE observatorio_estado
  ADD PRIMARY KEY (id),
  ADD UNIQUE KEY id_observatorio (id_observatorio,id_ejecucion_observatorio);


ALTER TABLE observatorio_estado
  MODIFY id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador autoincrementable';


--
-- UPDATE_DATABASE_OAW_4.2.0.sql 
--

-- Actualizar los códigos fuente de base de datos a base 64
UPDATE tanalisis SET cod_fuente = TO_BASE64(cod_fuente);
UPDATE tanalisis_css SET codigo = TO_BASE64(codigo);


--
-- ALTER_TABLE_OBSERVATORIO_ESTADO_OAW_4.2.1.sql
--

ALTER TABLE `observatorio_estado` CHANGE `ultima_url` `ultima_url` VARCHAR(8000);
ALTER TABLE `observatorio_estado` CHANGE `actual_url` `actual_url` VARCHAR(8000);


--
-- CREATE_TABLE_OBSERVATORIO_PROXY_OAW_4.3.0.sql 
--

CREATE TABLE observatorio_proxy (
  status tinyint(1) NOT NULL,
  url varchar(64) NOT NULL,
  port varchar(5) NOT NULL
);

INSERT INTO observatorio_proxy (status, url, port) VALUES(1, '127.0.0.1', '18088');


--
-- INSERT_TABLES_OAW_5.0.0.sql 
--

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


--
-- CREATE_TABLES_OAW_5.0.2.sql 
--

CREATE TABLE tanalisis_accesibilidad ( 
	id INT NOT NULL AUTO_INCREMENT , 
	id_analisis INT NOT NULL , 
	urls VARCHAR(2048) NOT NULL , 
	PRIMARY KEY (id)
);

ALTER TABLE basic_service ADD filename VARCHAR(1024) NULL;
ALTER TABLE etiqueta ADD UNIQUE(nombre);
ALTER TABLE categoria CHANGE categoria categoria VARCHAR(256);

INSERT INTO clasificacion_etiqueta (id_clasificacion, nombre) VALUES ('4', 'Otros');


--
-- ALTER_OAW_5.0.3.sql 
--

ALTER TABLE ambitos_lista ADD descripcion VARCHAR(1024) NULL;

UPDATE ambitos_lista SET descripcion = 'Administración General del Estado' WHERE id_ambito = 1;
UPDATE ambitos_lista SET descripcion = 'Comunidades Autónomas' WHERE id_ambito = 2;
UPDATE ambitos_lista SET descripcion = 'Entidades Locales' WHERE id_ambito = 3;
UPDATE ambitos_lista SET descripcion = 'Otros' WHERE id_ambito = 4;

CREATE INDEX tanalisis_cod_rastreo ON tanalisis (cod_rastreo);


ALTER TABLE ambitos_lista ROW_FORMAT=COMPRESSED;
ALTER TABLE basic_service ROW_FORMAT=COMPRESSED;
ALTER TABLE cartucho ROW_FORMAT=COMPRESSED;
ALTER TABLE cartucho_rastreo ROW_FORMAT=COMPRESSED;
ALTER TABLE categoria ROW_FORMAT=COMPRESSED;
ALTER TABLE categorias_lista ROW_FORMAT=COMPRESSED;
ALTER TABLE categoria_termino ROW_FORMAT=COMPRESSED;
ALTER TABLE clasificacion_etiqueta ROW_FORMAT=COMPRESSED;
ALTER TABLE complejidades_lista ROW_FORMAT=COMPRESSED;
ALTER TABLE cuenta_cliente ROW_FORMAT=COMPRESSED;
ALTER TABLE cuenta_cliente_cartucho ROW_FORMAT=COMPRESSED;
ALTER TABLE cuenta_cliente_usuario ROW_FORMAT=COMPRESSED;
ALTER TABLE dependencia ROW_FORMAT=COMPRESSED;
ALTER TABLE enlaces_rotos ROW_FORMAT=COMPRESSED;
ALTER TABLE etiqueta ROW_FORMAT=COMPRESSED;
ALTER TABLE export_aspect_score ROW_FORMAT=COMPRESSED;
ALTER TABLE export_category ROW_FORMAT=COMPRESSED;
ALTER TABLE export_observatory ROW_FORMAT=COMPRESSED;
ALTER TABLE export_page ROW_FORMAT=COMPRESSED;
ALTER TABLE export_site ROW_FORMAT=COMPRESSED;
ALTER TABLE export_verification_modality ROW_FORMAT=COMPRESSED;
ALTER TABLE export_verification_page ROW_FORMAT=COMPRESSED;
ALTER TABLE export_verification_score ROW_FORMAT=COMPRESSED;
ALTER TABLE languages ROW_FORMAT=COMPRESSED;
ALTER TABLE lista ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorios_realizados ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_ambito ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_categoria ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_complejidad ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_estado ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_lista ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_metodologia ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_plantillas ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_proxy ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_tipo ROW_FORMAT=COMPRESSED;
ALTER TABLE observatorio_usuario ROW_FORMAT=COMPRESSED;
ALTER TABLE periodicidad ROW_FORMAT=COMPRESSED;
ALTER TABLE rastreo ROW_FORMAT=COMPRESSED;
ALTER TABLE rastreos_realizados ROW_FORMAT=COMPRESSED;
ALTER TABLE roles ROW_FORMAT=COMPRESSED;
ALTER TABLE semilla_dependencia ROW_FORMAT=COMPRESSED;
ALTER TABLE semilla_etiqueta ROW_FORMAT=COMPRESSED;
ALTER TABLE tanalisis ROW_FORMAT=COMPRESSED;
ALTER TABLE tanalisis_css ROW_FORMAT=COMPRESSED;
ALTER TABLE termino ROW_FORMAT=COMPRESSED;
ALTER TABLE tguidelines ROW_FORMAT=COMPRESSED;
ALTER TABLE tincidencia ROW_FORMAT=COMPRESSED;
ALTER TABLE tipo_lista ROW_FORMAT=COMPRESSED;
ALTER TABLE tipo_rol ROW_FORMAT=COMPRESSED;
ALTER TABLE usuario ROW_FORMAT=COMPRESSED;
ALTER TABLE usuario_cartucho ROW_FORMAT=COMPRESSED;
ALTER TABLE usuario_rol ROW_FORMAT=COMPRESSED;


--
-- Seed required data into the database to be able to start and use the application
--

-- Create an admin:admin user for test purposes

INSERT INTO usuario(Usuario, Password,  Nombre, Apellidos, Departamento, Email) VALUES ('admin', md5('admin'), 'Test', 'User', 'Test Department', 'test.user@email.net');
INSERT INTO usuario_rol(Usuario, id_rol) VALUES (LAST_INSERT_ID(), 1);
INSERT INTO roles(id_rol, rol, id_tipo) VALUES (1, 'Admin', 1);

-- Create test values for periodicity

INSERT INTO periodicidad(nombre, dias, cronExpression) VALUES ('Every Minute', 0, '0 * * * * ?');
INSERT INTO periodicidad(nombre, dias, cronExpression) VALUES ('Hourly', 0, '0 0 * * * ?');
INSERT INTO periodicidad(nombre, dias, cronExpression) VALUES ('Daily', 1, '0 0 0 * * ?');
