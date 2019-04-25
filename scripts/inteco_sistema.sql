-- phpMyAdmin SQL Dump
-- version 4.0.8
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 03-09-2014 a las 09:47:10
-- Versión del servidor: 5.5.34-0ubuntu0.13.04.1
-- Versión de PHP: 5.4.9-4ubuntu2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `inteco_sistema`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `basic_service`
--

CREATE TABLE IF NOT EXISTS `basic_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usr` varchar(250) DEFAULT NULL,
  `language` varchar(10) DEFAULT NULL,
  `domain` varchar(1024) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `depth` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `report` varchar(30) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `status` varchar(25) NOT NULL DEFAULT 'launched',
  `send_date` datetime DEFAULT NULL,
  `scheduling_date` datetime DEFAULT NULL,
  `analysis_type` varchar(20) NOT NULL,
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cartucho`
--

CREATE TABLE IF NOT EXISTS `cartucho` (
  `id_cartucho` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `instalado` tinyint(1) NOT NULL,
  `aplicacion` varchar(100) NOT NULL,
  `numrastreos` int(11) NOT NULL,
  `numhilos` int(11) NOT NULL,
  PRIMARY KEY (`id_cartucho`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=6 ;

--
-- Volcado de datos para la tabla `cartucho`
--

INSERT INTO `cartucho` (`id_cartucho`, `nombre`, `instalado`, `aplicacion`, `numrastreos`, `numhilos`) VALUES
(2, 'es.inteco.cartuchoUrlMaliciosa.CartuchoWebMaliciosas', 1, 'Malware', 6, 10),
(3, 'es.inteco.crawler.sexista.modules.analisis.service.CartuchoSexista', 1, 'Lenox', 6, 10),
(4, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'Intav', 30, 100),
(5, 'es.inteco.plugin.MultilanguagePlugin', 1, 'Multilingüismo', 30, 100);

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
  `categoria` varchar(50) NOT NULL,
  `umbral` double NOT NULL,
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
-- Estructura de tabla para la tabla `categorias_lista`
--

CREATE TABLE IF NOT EXISTS `categorias_lista` (
  `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta_cliente`
--

CREATE TABLE IF NOT EXISTS `cuenta_cliente` (
  `id_cuenta` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

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
  `url` varchar(255) NOT NULL,
  `num_enlaces` int(11) NOT NULL,
  PRIMARY KEY (`id_rastreo_realizado`,`url`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `languages`
--

CREATE TABLE IF NOT EXISTS `languages` (
  `id_language` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(45) NOT NULL,
  `codice` varchar(45) NOT NULL,
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
  `nombre` varchar(255) DEFAULT NULL,
  `lista` text NOT NULL,
  `id_categoria` bigint(20) DEFAULT NULL,
  `acronimo` varchar(25) DEFAULT NULL,
  `dependencia` varchar(255) DEFAULT NULL,
  `activa` tinyint(1) DEFAULT '1',
  `in_directory` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_lista`),
  KEY `id_tipo_lista` (`id_tipo_lista`),
  KEY `id_categoria` (`id_categoria`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorio`
--

CREATE TABLE IF NOT EXISTS `observatorio` (
  `id_observatorio` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
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
  `metodologia` mediumtext,
  PRIMARY KEY (`id`),
  KEY `id_obs_realizado` (`id_obs_realizado`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observatorio_tipo`
--

CREATE TABLE IF NOT EXISTS `observatorio_tipo` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `observatorio_tipo`
--

INSERT INTO `observatorio_tipo` (`id_tipo`, `name`) VALUES
(1, 'AGE'),
(2, 'CCAA'),
(3, 'EELL'),
(4, 'PRENSA');

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `periodicidad`
--

CREATE TABLE IF NOT EXISTS `periodicidad` (
  `id_periodicidad` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(15) NOT NULL,
  `dias` int(11) DEFAULT NULL,
  `cronExpression` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id_periodicidad`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=8;

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
  `nombre_rastreo` varchar(255) DEFAULT NULL,
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE IF NOT EXISTS `roles` (
  `id_rol` bigint(20) NOT NULL AUTO_INCREMENT,
  `rol` varchar(50) NOT NULL,
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
-- Estructura de tabla para la tabla `termino`
--

CREATE TABLE IF NOT EXISTS `termino` (
  `id_termino` bigint(20) NOT NULL AUTO_INCREMENT,
  `termino` varchar(50) NOT NULL,
  PRIMARY KEY (`id_termino`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_lista`
--

CREATE TABLE IF NOT EXISTS `tipo_lista` (
  `id_tipo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
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
  `nombre` varchar(50) NOT NULL,
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
  `usuario` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(150) NOT NULL,
  `departamento` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `usuario`, `password`, `nombre`, `apellidos`, `departamento`, `email`) VALUES
(1, 'Programado', '4500d1ab0296081648c636b096619efd', 'Miguel', '', '', ''),
(2, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Miguel', 'García Nicieza', 'CTIC', 'miguel.garcia@fundacionctic.org');

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
(2, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
