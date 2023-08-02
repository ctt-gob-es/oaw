SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `OAW` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `OAW`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ambitos_lista` (
  `id_ambito` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id_ambito`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `basic_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usr` varchar(250) COLLATE utf8_bin DEFAULT NULL,
  `language` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `domain` varchar(12000) CHARACTER SET latin1 DEFAULT NULL,
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
  `complexity` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `filename` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `depthReport` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `basic_service_backup` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `usr` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `language` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `domain` varchar(12000) CHARACTER SET latin1 DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `depth` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `report` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `status` varchar(25) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'launched',
  `send_date` datetime DEFAULT NULL,
  `scheduling_date` datetime DEFAULT NULL,
  `analysis_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  `register_result` tinyint(1) NOT NULL DEFAULT '0',
  `complexity` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `filename` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `depthReport` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cartucho` (
  `id_cartucho` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `instalado` tinyint(1) NOT NULL,
  `aplicacion` varchar(100) COLLATE utf8_bin NOT NULL,
  `numrastreos` int(11) NOT NULL,
  `numhilos` int(11) NOT NULL,
  `id_guideline` int(11) NOT NULL,
  PRIMARY KEY (`id_cartucho`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cartucho_rastreo` (
  `id_cartucho` bigint(20) NOT NULL,
  `id_rastreo` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cartucho`,`id_rastreo`),
  KEY `id_rastreo` (`id_rastreo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoria` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoria` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `umbral` double NOT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoria_termino` (
  `id_categoria` bigint(20) NOT NULL,
  `id_termino` bigint(20) NOT NULL,
  `porcentaje` double NOT NULL,
  `porcentaje_normalizado` double NOT NULL,
  PRIMARY KEY (`id_categoria`,`id_termino`),
  KEY `id_termino` (`id_termino`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categorias_lista` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8_bin NOT NULL,
  `orden` int(11) NOT NULL,
  `clave` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `principal` int(11) DEFAULT '0',
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clasificacion_etiqueta` (
  `id_clasificacion` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id_clasificacion`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `complejidades_lista` (
  `id_complejidad` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `profundidad` bigint(20) NOT NULL,
  `amplitud` bigint(20) NOT NULL,
  PRIMARY KEY (`id_complejidad`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cuenta_cliente` (
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
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cuenta_cliente_cartucho` (
  `id_cuenta` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cuenta`,`id_cartucho`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cuenta_cliente_usuario` (
  `id_cuenta` bigint(20) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cuenta`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dependencia` (
  `id_dependencia` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) CHARACTER SET latin1 COLLATE latin1_spanish_ci DEFAULT NULL,
  `emails` text COLLATE utf8_bin,
  `send_auto` int(11) DEFAULT NULL,
  `official` int(11) DEFAULT NULL,
  `id_tag` int(11) DEFAULT NULL,
  `acronym` text COLLATE utf8_bin,
  PRIMARY KEY (`id_dependencia`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `id_dependencia` (`id_dependencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dependencia_ambito` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_dependencia` int(11) NOT NULL,
  `id_ambito` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enlaces_rotos` (
  `id_rastreo_realizado` bigint(20) NOT NULL,
  `url` varchar(255) COLLATE utf8_bin NOT NULL,
  `num_enlaces` int(11) NOT NULL,
  PRIMARY KEY (`id_rastreo_realizado`,`url`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etiqueta` (
  `id_etiqueta` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `id_clasificacion` bigint(20) NOT NULL,
  PRIMARY KEY (`id_etiqueta`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `FKADC57F94AF7028F6` (`id_clasificacion`),
  CONSTRAINT `FKADC57F94AF7028F6` FOREIGN KEY (`id_clasificacion`) REFERENCES `clasificacion_etiqueta` (`id_clasificacion`)
) ENGINE=InnoDB AUTO_INCREMENT=18833 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_aspect_score` (
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
) ENGINE=InnoDB AUTO_INCREMENT=135141 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_category` (
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
) ENGINE=InnoDB AUTO_INCREMENT=556 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_observatory` (
  `idExecution` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `numA` int(11) NOT NULL,
  `numAA` int(11) NOT NULL,
  `numNV` int(11) NOT NULL,
  PRIMARY KEY (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_page` (
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
) ENGINE=InnoDB AUTO_INCREMENT=24762 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_site` (
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
  `compliance` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8323B4F2D83469BD` (`idCategory`),
  KEY `FK8323B4F2BBF2859D` (`idCategory`)
) ENGINE=InnoDB AUTO_INCREMENT=1165 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_verification_modality` (
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
) ENGINE=InnoDB AUTO_INCREMENT=51162 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_verification_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `modality` varchar(255) COLLATE utf8_bin NOT NULL,
  `value` int(11) DEFAULT NULL,
  `verification` varchar(255) COLLATE utf8_bin NOT NULL,
  `idPage` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK724BD668833FBD5F` (`idPage`),
  KEY `FK724BD668B26C93F` (`idPage`)
) ENGINE=InnoDB AUTO_INCREMENT=495221 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_verification_score` (
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
) ENGINE=InnoDB AUTO_INCREMENT=45341 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `languages` (
  `id_language` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(45) COLLATE utf8_bin NOT NULL,
  `codice` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_language`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lista` (
  `id_lista` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_tipo_lista` bigint(20) NOT NULL,
  `nombre` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `lista` text COLLATE utf8_bin NOT NULL,
  `id_categoria` bigint(20) DEFAULT NULL,
  `acronimo` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `dependencia` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `activa` tinyint(1) DEFAULT '1',
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  `eliminar` bigint(20) NOT NULL DEFAULT '0',
  `id_complejidad` bigint(20) DEFAULT NULL,
  `id_ambito` bigint(20) DEFAULT NULL,
  `observaciones` varchar(5000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_lista`),
  UNIQUE KEY `id_lista` (`id_lista`),
  KEY `id_tipo_lista` (`id_tipo_lista`),
  KEY `id_categoria` (`id_categoria`),
  KEY `id_ambito` (`id_ambito`),
  KEY `FK6236383178C109A` (`id_categoria`),
  KEY `FK6236383E4BE25A2` (`id_complejidad`),
  KEY `FK6236383BD8D2820` (`id_ambito`),
  KEY `FK623638314AE8A4F` (`id_tipo_lista`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `oaw_dashboard` AS SELECT 
 1 AS `id`,
 1 AS `usr`,
 1 AS `url`,
 1 AS `email`,
 1 AS `depth`,
 1 AS `width`,
 1 AS `report`,
 1 AS `date`,
 1 AS `status`,
 1 AS `analysis_type`,
 1 AS `in_directory`*/;
SET character_set_client = @saved_cs_client;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio` (
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
  `id_ambito` bigint(20) DEFAULT NULL,
  `tags` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_observatorio`),
  KEY `id_periodicidad` (`id_periodicidad`),
  KEY `id_language` (`id_language`),
  KEY `id_cartucho` (`id_cartucho`),
  KEY `id_tipo` (`id_tipo`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_ambito` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_ambito` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_categoria` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_categoria` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_categoria`),
  KEY `id_categoria` (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_complejidad` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_complejidad` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_estado` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador autoincrementable',
  `id_observatorio` int(11) NOT NULL COMMENT 'Identificador del observatorio',
  `id_ejecucion_observatorio` int(11) NOT NULL COMMENT 'Identificador de la ejecuciÃ³n del observatorio',
  `nombre` varchar(256) COLLATE utf8_bin NOT NULL COMMENT 'Nombre de la semilla',
  `url` varchar(256) COLLATE utf8_bin NOT NULL COMMENT 'URL de la semilla',
  `total_url` int(11) NOT NULL COMMENT 'Total del URLs que se analizarÃ¡n',
  `total_url_analizadas` int(11) DEFAULT '0',
  `ultima_url` varchar(8000) COLLATE utf8_bin DEFAULT NULL,
  `fecha_ultima_url` datetime DEFAULT NULL COMMENT 'Fecha del fin de la Ãºltima URL analizada',
  `actual_url` varchar(8000) COLLATE utf8_bin DEFAULT NULL,
  `tiempo_medio` int(11) DEFAULT NULL COMMENT 'Tiempo medio de anÃ¡lisis',
  `tiempo_acumulado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_observatorio` (`id_observatorio`,`id_ejecucion_observatorio`)
) ENGINE=InnoDB AUTO_INCREMENT=538 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_extra_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `key` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_lista` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_lista` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_lista`),
  KEY `id_lista` (`id_lista`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_metodologia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_obs_realizado` bigint(20) DEFAULT NULL,
  `metodologia` mediumtext COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  KEY `id_obs_realizado` (`id_obs_realizado`)
) ENGINE=MyISAM AUTO_INCREMENT=546 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_plantillas` (
  `id_plantilla` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1024) NOT NULL,
  `documento` longblob NOT NULL,
  `type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id_plantilla`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_proxy` (
  `status` tinyint(1) NOT NULL,
  `url` varchar(64) NOT NULL,
  `port` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_range` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `min_value` float(4,2) DEFAULT NULL,
  `max_value` float(4,2) DEFAULT NULL,
  `min_value_operator` varchar(255) DEFAULT NULL,
  `max_value_operator` varchar(255) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  `color` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_send_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_observatory_execution` int(11) NOT NULL,
  `ids_observatory_execution_evolution` varchar(255) DEFAULT NULL,
  `has_custom_texts` int(11) NOT NULL DEFAULT '0',
  `subject` varchar(255) DEFAULT NULL,
  `cco` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_send_configuration_comparision` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_observatory_execution` int(11) NOT NULL,
  `id_tag` int(11) NOT NULL,
  `date_first` varchar(255) DEFAULT NULL,
  `date_previous` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_send_historic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_observatory_execution` int(11) NOT NULL,
  `cco` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `ids_observatory_execution_evol` varchar(255) DEFAULT NULL,
  `send_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_send_historic_comparision` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_send_historic` int(11) NOT NULL,
  `id_tag` int(11) NOT NULL,
  `date_first` varchar(255) DEFAULT NULL,
  `date_previous` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_send_historic_ranges` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_send_historic` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `min_value` float(4,2) NOT NULL,
  `max_value` float(4,2) DEFAULT NULL,
  `min_value_operator` varchar(255) NOT NULL,
  `max_value_operator` varchar(255) DEFAULT NULL,
  `template` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_send_historic_results` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_send_historic` int(11) NOT NULL,
  `id_ura` int(11) NOT NULL,
  `range_name` varchar(255) NOT NULL,
  `range_value` float(4,2) DEFAULT NULL,
  `custom_text` mediumtext NOT NULL,
  `send` int(11) NOT NULL DEFAULT '0',
  `mail` mediumtext NOT NULL,
  `send_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `send_error` varchar(255) DEFAULT NULL,
  `file_link` varchar(1024) DEFAULT NULL,
  `file_pass` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_template_range` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_observatory_execution` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `min_value` float(4,2) NOT NULL,
  `max_value` float(4,2) DEFAULT NULL,
  `min_value_operator` varchar(255) NOT NULL,
  `max_value_operator` varchar(255) DEFAULT NULL,
  `template` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_tipo` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_ura_send_results` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_observatory_execution` int(11) NOT NULL,
  `id_ura` int(11) NOT NULL,
  `id_range` int(11) NOT NULL,
  `range_value` float(4,2) DEFAULT NULL,
  `has_custom_text` int(11) NOT NULL DEFAULT '0',
  `custom_text` mediumtext NOT NULL,
  `send` int(11) NOT NULL DEFAULT '0',
  `send_date` datetime DEFAULT NULL,
  `send_error` varchar(255) DEFAULT NULL,
  `file_link` varchar(1024) DEFAULT NULL,
  `file_pass` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_index` (`id_observatory_execution`,`id_ura`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorio_usuario` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observatorios_realizados` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_observatorio` bigint(20) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `id_cartucho` bigint(20) DEFAULT NULL,
  `estado` int(10) unsigned NOT NULL DEFAULT '0',
  `tags` text COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  KEY `id_observatorio` (`id_observatorio`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM AUTO_INCREMENT=557 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `periodicidad` (
  `id_periodicidad` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(15) COLLATE utf8_bin NOT NULL,
  `dias` int(11) DEFAULT NULL,
  `cronExpression` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_periodicidad`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rastreo` (
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
) ENGINE=MyISAM AUTO_INCREMENT=1182 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rastreos_realizados` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_rastreo` bigint(20) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `id_usuario` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  `id_obs_realizado` bigint(20) DEFAULT NULL,
  `id_lista` bigint(20) DEFAULT NULL,
  `level` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `score` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_rastreo` (`id_rastreo`),
  KEY `id_cartucho` (`id_cartucho`),
  KEY `id_obs_realizado` (`id_obs_realizado`),
  KEY `id_lista` (`id_lista`)
) ENGINE=MyISAM AUTO_INCREMENT=1053 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id_rol` bigint(20) NOT NULL AUTO_INCREMENT,
  `rol` varchar(50) COLLATE utf8_bin NOT NULL,
  `id_tipo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  KEY `id_tipo` (`id_tipo`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `semilla_dependencia` (
  `id_lista` bigint(20) NOT NULL DEFAULT '0',
  `id_dependencia` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_lista`,`id_dependencia`),
  KEY `semilla_dependencia_ibfk_1` (`id_dependencia`),
  KEY `FKA5EF2FABF38120E` (`id_dependencia`),
  CONSTRAINT `FKA5EF2FABF38120E` FOREIGN KEY (`id_dependencia`) REFERENCES `dependencia` (`id_dependencia`),
  CONSTRAINT `semilla_dependencia_ibfk_1` FOREIGN KEY (`id_dependencia`) REFERENCES `dependencia` (`id_dependencia`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `semilla_etiqueta` (
  `id_lista` bigint(20) NOT NULL DEFAULT '0',
  `id_etiqueta` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_lista`,`id_etiqueta`),
  KEY `semilla_etiqueta_ibfk_1` (`id_etiqueta`),
  KEY `FK19AFD5A076FE5584` (`id_etiqueta`),
  CONSTRAINT `FK19AFD5A076FE5584` FOREIGN KEY (`id_etiqueta`) REFERENCES `etiqueta` (`id_etiqueta`),
  CONSTRAINT `semilla_etiqueta_ibfk_1` FOREIGN KEY (`id_etiqueta`) REFERENCES `etiqueta` (`id_etiqueta`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tanalisis` (
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
  KEY `basic_service` (`nom_entidad`,`cod_rastreo`) USING BTREE,
  KEY `tanalisis_cod_rastreo` (`cod_rastreo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tanalisis_accesibilidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_analisis` int(11) NOT NULL,
  `url` varchar(256) DEFAULT NULL,
  `checks_ok` int(11) DEFAULT '0',
  `COD_FUENTE` mediumtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_analisis_UNIQUE` (`id_analisis`,`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tanalisis_accesibilidad_backup` (
  `id` int(11) NOT NULL DEFAULT '0',
  `id_analisis` int(11) NOT NULL,
  `url` varchar(256) DEFAULT NULL,
  `checks_ok` int(11) DEFAULT '0',
  `COD_FUENTE` mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tanalisis_backup` (
  `cod_analisis` bigint(20) NOT NULL DEFAULT '0',
  `fec_analisis` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cod_url` varchar(2050) DEFAULT NULL,
  `num_duracion` bigint(20) DEFAULT NULL,
  `nom_entidad` varchar(100) NOT NULL,
  `cod_rastreo` bigint(20) NOT NULL,
  `cod_guideline` bigint(20) NOT NULL,
  `checks_ejecutados` text,
  `estado` int(11) NOT NULL,
  `cod_fuente` mediumtext,
  `ref_cod_fuente` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tanalisis_css` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(2050) DEFAULT NULL,
  `codigo` mediumtext,
  `cod_analisis` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `cod_analisis` (`cod_analisis`)
) ENGINE=InnoDB AUTO_INCREMENT=1145 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tanalisis_css_backup` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `url` varchar(2050) DEFAULT NULL,
  `codigo` mediumtext,
  `cod_analisis` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `termino` (
  `id_termino` bigint(20) NOT NULL AUTO_INCREMENT,
  `termino` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_termino`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tguidelines` (
  `cod_guideline` bigint(20) NOT NULL AUTO_INCREMENT,
  `des_guideline` varchar(50) NOT NULL,
  PRIMARY KEY (`cod_guideline`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tincidencia` (
  `cod_incidencia` bigint(20) NOT NULL DEFAULT '0',
  `cod_comprobacion` bigint(20) NOT NULL,
  `cod_analisis` bigint(20) NOT NULL,
  `cod_linea_fuente` bigint(20) NOT NULL,
  `cod_columna_fuente` bigint(20) NOT NULL,
  `des_fuente` text,
  KEY `cod_analisis` (`cod_analisis`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_lista` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_rol` (
  `id_tipo` bigint(20) NOT NULL,
  `nombre` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) COLLATE utf8_bin NOT NULL,
  `password` varchar(100) COLLATE utf8_bin NOT NULL,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `apellidos` varchar(150) COLLATE utf8_bin NOT NULL,
  `departamento` varchar(100) COLLATE utf8_bin NOT NULL,
  `email` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_cartucho` (
  `id_usuario` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_cartucho`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_rol` (
  `usuario` bigint(20) NOT NULL,
  `id_rol` bigint(20) NOT NULL,
  PRIMARY KEY (`usuario`,`id_rol`),
  KEY `id_rol` (`id_rol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apiKey`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `apiKey` varchar(300) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(200),
  `activa` tinyint (1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*!40101 SET character_set_client = @saved_cs_client */;
/*!50001 DROP VIEW IF EXISTS `oaw_dashboard`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `oaw_dashboard` AS select `basic_service`.`id` AS `id`,`basic_service`.`usr` AS `usr`,`basic_service`.`domain` AS `url`,`basic_service`.`email` AS `email`,`basic_service`.`depth` AS `depth`,`basic_service`.`width` AS `width`,`basic_service`.`report` AS `report`,`basic_service`.`date` AS `date`,`basic_service`.`status` AS `status`,`basic_service`.`analysis_type` AS `analysis_type`,`basic_service`.`in_directory` AS `in_directory` from `basic_service` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
