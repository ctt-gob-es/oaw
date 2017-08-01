-- phpMyAdmin SQL Dump
-- version 4.0.8
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 15-09-2014 a las 15:38:28
-- Versión del servidor: 5.5.34-0ubuntu0.13.04.1
-- Versión de PHP: 5.4.9-4ubuntu2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `OAW`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `basic_service`
--

CREATE TABLE IF NOT EXISTS `basic_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usr` varchar(250) COLLATE utf8_bin DEFAULT NULL,
  `language` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `domain` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
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
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cartucho`
--

CREATE TABLE IF NOT EXISTS `cartucho` (
  `id_cartucho` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `instalado` tinyint(1) NOT NULL,
  `aplicacion` varchar(100) COLLATE utf8_bin NOT NULL,
  `numrastreos` int(11) NOT NULL,
  `numhilos` int(11) NOT NULL,
  `id_guideline` int(11) NOT NULL,
  PRIMARY KEY (`id_cartucho`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `cartucho`
--

INSERT INTO `cartucho` (`id_cartucho`, `nombre`, `instalado`, `aplicacion`, `numrastreos`, `numhilos`, `id_guideline`) VALUES
(1, 'es.inteco.cartuchoUrlMaliciosa.CartuchoWebMaliciosas', 1, 'Malware', 6, 10, -1),
(2, 'es.inteco.crawler.sexista.modules.analisis.service.CartuchoSexista', 1, 'Lenox', 6, 10, -1),
(3, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-2004', 15, 50, 4),
(4, 'es.inteco.plugin.MultilanguagePlugin', 1, 'Multilingüismo', 15, 50, -1),
(5, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-2012', 15,	50, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cartucho_rastreo`
--

CREATE TABLE IF NOT EXISTS `cartucho_rastreo` (
  `id_cartucho` bigint(20) NOT NULL,
  `id_rastreo` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cartucho`,`id_rastreo`),
  KEY `id_rastreo` (`id_rastreo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoria` varchar(50) COLLATE utf8_bin NOT NULL,
  `umbral` double NOT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias_lista`
--

CREATE TABLE IF NOT EXISTS `categorias_lista` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8_bin NOT NULL,
  `orden` int(11) NOT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_termino`
--

CREATE TABLE IF NOT EXISTS `categoria_termino` (
  `id_categoria` bigint(20) NOT NULL,
  `id_termino` bigint(20) NOT NULL,
  `porcentaje` double NOT NULL,
  `porcentaje_normalizado` double NOT NULL,
  PRIMARY KEY (`id_categoria`,`id_termino`),
  KEY `id_termino` (`id_termino`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta_cliente`
--

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta_cliente_cartucho`
--

CREATE TABLE IF NOT EXISTS `cuenta_cliente_cartucho` (
  `id_cuenta` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cuenta`,`id_cartucho`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta_cliente_usuario`
--

CREATE TABLE IF NOT EXISTS `cuenta_cliente_usuario` (
  `id_cuenta` bigint(20) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id_cuenta`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enlaces_rotos`
--

CREATE TABLE IF NOT EXISTS `enlaces_rotos` (
  `id_rastreo_realizado` bigint(20) NOT NULL,
  `url` varchar(255) COLLATE utf8_bin NOT NULL,
  `num_enlaces` int(11) NOT NULL,
  PRIMARY KEY (`id_rastreo_realizado`,`url`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_aspect_score`
--

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
  KEY `FK39330E96B3ADA411` (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_category`
--

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
  KEY `FKA67C529B3ADA411` (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_observatory`
--

CREATE TABLE IF NOT EXISTS `export_observatory` (
  `idExecution` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `numA` int(11) NOT NULL,
  `numAA` int(11) NOT NULL,
  `numNV` int(11) NOT NULL,
  PRIMARY KEY (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_page`
--

CREATE TABLE IF NOT EXISTS `export_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` varchar(255) COLLATE utf8_bin NOT NULL,
  `score` decimal(19,2) NOT NULL,
  `scoreLevel1` decimal(19,2) NOT NULL,
  `scoreLevel2` decimal(19,2) NOT NULL,
  `url` varchar(500) COLLATE utf8_bin NOT NULL,
  `idSite` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8322383A8342B6CF` (`idSite`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_site`
--

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
  KEY `FK8323B4F2D83469BD` (`idCategory`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_verification_modality`
--

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
  KEY `FKC9F9E23AB3ADA411` (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_verification_page`
--

CREATE TABLE IF NOT EXISTS `export_verification_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `modality` varchar(255) COLLATE utf8_bin NOT NULL,
  `value` int(11) DEFAULT NULL,
  `verification` varchar(255) COLLATE utf8_bin NOT NULL,
  `idPage` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK724BD668833FBD5F` (`idPage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_verification_score`
--

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
  KEY `FKD75A45D9B3ADA411` (`idExecution`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `languages`
--

CREATE TABLE IF NOT EXISTS `languages` (
  `id_language` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(45) COLLATE utf8_bin NOT NULL,
  `codice` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_language`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `languages`
--

INSERT INTO `languages` (`id_language`, `key_name`, `codice`) VALUES
(1, 'idioma.espanol', 'es'),
(2, 'idioma.ingles', 'en');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lista`
--

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
  KEY `id_tipo_lista` (`id_tipo_lista`),
  KEY `id_categoria` (`id_categoria`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorio`
--

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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorios_realizados`
--

CREATE TABLE IF NOT EXISTS `observatorios_realizados` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_observatorio` bigint(20) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `id_cartucho` bigint(20) DEFAULT NULL,
  `estado` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id_observatorio` (`id_observatorio`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorio_categoria`
--

CREATE TABLE IF NOT EXISTS `observatorio_categoria` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_categoria` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_categoria`),
  KEY `id_categoria` (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorio_lista`
--

CREATE TABLE IF NOT EXISTS `observatorio_lista` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_lista` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_lista`),
  KEY `id_lista` (`id_lista`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorio_metodologia`
--

CREATE TABLE IF NOT EXISTS `observatorio_metodologia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_obs_realizado` bigint(20) DEFAULT NULL,
  `metodologia` mediumtext COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  KEY `id_obs_realizado` (`id_obs_realizado`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorio_tipo`
--

CREATE TABLE IF NOT EXISTS `observatorio_tipo` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=4 ;

--
-- Volcado de datos para la tabla `observatorio_tipo`
--

INSERT INTO `observatorio_tipo` (`id_tipo`, `name`) VALUES
(1, 'AGE'),
(2, 'CCAA'),
(3, 'EELL');


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorio_usuario`
--

CREATE TABLE IF NOT EXISTS `observatorio_usuario` (
  `id_observatorio` bigint(20) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id_observatorio`,`id_usuario`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `periodicidad`
--

CREATE TABLE IF NOT EXISTS `periodicidad` (
  `id_periodicidad` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(15) COLLATE utf8_bin NOT NULL,
  `dias` int(11) DEFAULT NULL,
  `cronExpression` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_periodicidad`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=8 ;

--
-- Volcado de datos para la tabla `periodicidad`
--

INSERT INTO `periodicidad` (`id_periodicidad`, `nombre`, `dias`, `cronExpression`) VALUES
(1, 'Diario', 1, NULL),
(2, 'Semanal', 7, NULL),
(3, 'Quincenal', 15, NULL),
(4, 'Mensual', NULL, '0 min hour daymonth month/1 ? year/1'),
(5, 'Trimestral', NULL, '0 min hour daymonth month/3 ? year/1'),
(6, 'Semestral', NULL, '0 min hour daymonth month/6 ? year/1'),
(7, 'Anual', NULL, '0 min hour daymonth month ? year/1');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rastreo`
--

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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rastreos_realizados`
--

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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE IF NOT EXISTS `roles` (
  `id_rol` bigint(20) NOT NULL AUTO_INCREMENT,
  `rol` varchar(50) COLLATE utf8_bin NOT NULL,
  `id_tipo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  KEY `id_tipo` (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=7 ;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_rol`, `rol`, `id_tipo`) VALUES
(1, 'Administrador', 1),
(2, 'Configurador', 1),
(3, 'Visualizador', 1),
(4, 'Responsable cliente', 2),
(5, 'Visualizador cliente', 2),
(6, 'Observatorio', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tanalisis`
--

CREATE TABLE IF NOT EXISTS `tanalisis` (
  `cod_analisis` bigint(20) NOT NULL AUTO_INCREMENT,
  `fec_analisis` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cod_url` varchar(500) DEFAULT NULL,
  `num_duracion` bigint(20) DEFAULT NULL,
  `nom_entidad` varchar(100) NOT NULL,
  `cod_rastreo` bigint(20) NOT NULL,
  `cod_guideline` bigint(20) NOT NULL,
  `checks_ejecutados` text,
  `estado` int(11) NOT NULL,
  `cod_fuente` mediumtext,
  `ref_cod_fuente` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cod_analisis`),
  KEY `cod_guideline` (`cod_guideline`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `termino`
--

CREATE TABLE IF NOT EXISTS `termino` (
  `id_termino` bigint(20) NOT NULL AUTO_INCREMENT,
  `termino` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_termino`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tguidelines`
--

CREATE TABLE IF NOT EXISTS `tguidelines` (
  `cod_guideline` bigint(20) NOT NULL AUTO_INCREMENT,
  `des_guideline` varchar(50) NOT NULL,
  PRIMARY KEY (`cod_guideline`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Volcado de datos para la tabla `tguidelines`
--

INSERT INTO `tguidelines` (`cod_guideline`, `des_guideline`) VALUES
(1, 'une-139803.xml'),
(2, 'wcag-1-0.xml'),
(3, 'wcag-2-0.xml'),
(4, 'observatorio-inteco-1-0.xml'),
(5, 'uneER-139803.xml'),
(6, 'wcagER-1-0.xml'),
(7, 'observatorio-une-2012.xml');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tincidencia`
--

CREATE TABLE IF NOT EXISTS `tincidencia` (
  `cod_incidencia` bigint(20) NOT NULL DEFAULT '0',
  `cod_comprobacion` bigint(20) NOT NULL,
  `cod_analisis` bigint(20) NOT NULL,
  `cod_linea_fuente` bigint(20) NOT NULL,
  `cod_columna_fuente` bigint(20) NOT NULL,
  `des_fuente` text,
  KEY `cod_analisis` (`cod_analisis`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_lista`
--

CREATE TABLE IF NOT EXISTS `tipo_lista` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `tipo_lista`
--

INSERT INTO `tipo_lista` (`id_tipo`, `nombre`) VALUES
(1, 'semilla'),
(2, 'lista_rastreable'),
(3, 'lista_no_rastreable'),
(4, 'semilla_observatorio');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_rol`
--

CREATE TABLE IF NOT EXISTS `tipo_rol` (
  `id_tipo` bigint(20) NOT NULL,
  `nombre` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `tipo_rol`
--

INSERT INTO `tipo_rol` (`id_tipo`, `nombre`) VALUES
(1, 'Normal'),
(2, 'Cliente'),
(3, 'Observatorio');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) COLLATE utf8_bin NOT NULL,
  `password` varchar(100) COLLATE utf8_bin NOT NULL,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `apellidos` varchar(150) COLLATE utf8_bin NOT NULL,
  `departamento` varchar(100) COLLATE utf8_bin NOT NULL,
  `email` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `usuario`, `password`, `nombre`, `apellidos`, `departamento`, `email`) VALUES
(1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Miguel', 'García Nicieza', 'CTIC', 'miguel.garcia@fundacionctic.org'),
(2, 'Programado', '21232f297a57a5a743894a0e4a801fc3', '', '', '', 'miguel.garcia@fundacionctic.org');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_cartucho`
--

CREATE TABLE IF NOT EXISTS `usuario_cartucho` (
  `id_usuario` bigint(20) NOT NULL,
  `id_cartucho` bigint(20) NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_cartucho`),
  KEY `id_cartucho` (`id_cartucho`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `usuario_cartucho`
--

INSERT INTO `usuario_cartucho` (`id_usuario`, `id_cartucho`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_rol`
--

CREATE TABLE IF NOT EXISTS `usuario_rol` (
  `usuario` bigint(20) NOT NULL,
  `id_rol` bigint(20) NOT NULL,
  PRIMARY KEY (`usuario`,`id_rol`),
  KEY `id_rol` (`id_rol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `usuario_rol`
--

INSERT INTO `usuario_rol` (`usuario`, `id_rol`) VALUES
(1, 1),
(2, 1);

-- --------------------------------------------------------

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `export_aspect_score`
--
ALTER TABLE `export_aspect_score`
  ADD CONSTRAINT `FK39330E96833FBD5F` FOREIGN KEY (`idPage`) REFERENCES `export_page` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK39330E968342B6CF` FOREIGN KEY (`idSite`) REFERENCES `export_site` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK39330E96B3ADA411` FOREIGN KEY (`idExecution`) REFERENCES `export_observatory` (`idExecution`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK39330E96D83469BD` FOREIGN KEY (`idCategory`) REFERENCES `export_category` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_category`
--
ALTER TABLE `export_category`
  ADD CONSTRAINT `FKA67C529B3ADA411` FOREIGN KEY (`idExecution`) REFERENCES `export_observatory` (`idExecution`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_page`
--
ALTER TABLE `export_page`
  ADD CONSTRAINT `FK8322383A8342B6CF` FOREIGN KEY (`idSite`) REFERENCES `export_site` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_site`
--
ALTER TABLE `export_site`
  ADD CONSTRAINT `FK8323B4F2D83469BD` FOREIGN KEY (`idCategory`) REFERENCES `export_category` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_verification_modality`
--
ALTER TABLE `export_verification_modality`
  ADD CONSTRAINT `FKC9F9E23A8342B6CF` FOREIGN KEY (`idSite`) REFERENCES `export_site` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKC9F9E23AB3ADA411` FOREIGN KEY (`idExecution`) REFERENCES `export_observatory` (`idExecution`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKC9F9E23AD83469BD` FOREIGN KEY (`idCategory`) REFERENCES `export_category` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_verification_page`
--
ALTER TABLE `export_verification_page`
  ADD CONSTRAINT `FK724BD668833FBD5F` FOREIGN KEY (`idPage`) REFERENCES `export_page` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_verification_score`
--
ALTER TABLE `export_verification_score`
  ADD CONSTRAINT `FKD75A45D98342B6CF` FOREIGN KEY (`idSite`) REFERENCES `export_site` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKD75A45D9B3ADA411` FOREIGN KEY (`idExecution`) REFERENCES `export_observatory` (`idExecution`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKD75A45D9D83469BD` FOREIGN KEY (`idCategory`) REFERENCES `export_category` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `tanalisis`
--
ALTER TABLE `tanalisis`
  ADD CONSTRAINT `tanalisis_ibfk_1` FOREIGN KEY (`cod_guideline`) REFERENCES `tguidelines` (`cod_guideline`) ON DELETE CASCADE;

--
-- Filtros para la tabla `tincidencia`
--
ALTER TABLE `tincidencia`
  ADD CONSTRAINT `tincidencia_ibfk_1` FOREIGN KEY (`cod_analisis`) REFERENCES `tanalisis` (`cod_analisis`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
