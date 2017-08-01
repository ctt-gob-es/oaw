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
-- Base de datos: `inteco_multilanguage`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `analysis`
--

CREATE TABLE IF NOT EXISTS `analysis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `id_crawling` bigint(20) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `url` varchar(750) NOT NULL,
  `cod_fuente` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_category`
--

CREATE TABLE IF NOT EXISTS `export_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_category` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `id_observatory` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA67C529764B8E58` (`id_observatory`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_observatory`
--

CREATE TABLE IF NOT EXISTS `export_observatory` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_page`
--

CREATE TABLE IF NOT EXISTS `export_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_page` bigint(20) NOT NULL,
  `name` varchar(750) NOT NULL,
  `id_site` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8322383AAE442EEA` (`id_site`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_page_result`
--

CREATE TABLE IF NOT EXISTS `export_page_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `id_txt_language` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `translation` tinyint(1) DEFAULT NULL,
  `id_page` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4730B7C2AE41357A` (`id_page`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_percentage`
--

CREATE TABLE IF NOT EXISTS `export_percentage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ko_translation_pages` decimal(19,2) DEFAULT NULL,
  `no_translation` decimal(19,2) DEFAULT NULL,
  `no_translation_pages` decimal(19,2) DEFAULT NULL,
  `ok_translation_pages` decimal(19,2) DEFAULT NULL,
  `translation` decimal(19,2) DEFAULT NULL,
  `with_code` decimal(19,2) DEFAULT NULL,
  `with_translation_txt` decimal(19,2) DEFAULT NULL,
  `without_code` decimal(19,2) DEFAULT NULL,
  `without_translation_txt` decimal(19,2) DEFAULT NULL,
  `id_language` bigint(20) DEFAULT NULL,
  `id_category` bigint(20) DEFAULT NULL,
  `id_site` bigint(20) DEFAULT NULL,
  `id_observatory` bigint(20) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8B519C0537365C58` (`id_category`),
  KEY `FK8B519C0570D6090C` (`id_language`),
  KEY `FK8B519C05AE442EEA` (`id_site`),
  KEY `FK8B519C05764B8E58` (`id_observatory`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `export_site`
--

CREATE TABLE IF NOT EXISTS `export_site` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_site` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `id_category` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8323B4F237365C58` (`id_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `languages`
--

CREATE TABLE IF NOT EXISTS `languages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `text` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `to_analyze` tinyint(1) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Volcado de datos para la tabla `languages`
--

INSERT INTO `languages` (`id`, `name`, `text`, `code`, `to_analyze`, `title`) VALUES
(1, 'Español', '^[\\W_]*(Bienvenid[oa]s{0,1}|Castellano|Espa.{1,2}ol|Espa&ntilde;ol)( *\\(.*\\)){0,1}[\\W_]*$', '^es(-es){0,1}$', 1, '.*(Castellano|Espa.{1,2}ol|Espa&ntilde;ol).*'),
(2, 'Inglés', '^[\\W_]*(Welcome|English(\\s+version){0,1}|Ingl.{1,2}s|Ingl&eacute;s)( *\\(.*\\)){0,1}[\\W_]*$', '^en(-gb|-us){0,1}$', 1, '.*(English( version){0,1}|Ingl.{1,2}s|Ingl&eacute;s).*'),
(3, 'Francés', '^[\\W_]*(Bienvenues{0,1}|Bienvenus{0,1}|Fran.{1,2}ais|Fran&ccedil;ais|Franc.{1,2}s|Franc&eacute;s)( *\\(.*\\)){0,1}[\\W_]*$', '^fr(-fr){0,1}$', 1, '.*(Fran.{1,2}ais|Fran&ccedil;ais|Franc.{1,2}s|Franc&eacute;s).*'),
(4, 'Gallego', '^[\\W_]*(Be[nm]vidos{0,1}|Galego|Gallego)( *\\(.*\\)){0,1}[\\W_]*$', '^gl(-es){0,1}$', 1, '.*(Galego|Gallego).*'),
(5, 'Euskera', '^[\\W_]*(Ongi\\s+etorri|Eusk[ae]ra|Eusquera|Vasco)( *\\(.*\\)){0,1}[\\W_]*$', '^eu(-es){0,1}$', 1, '.*(Eusk[ae]ra|Eusquera|Vasco).*'),
(6, 'Catalán', '^[\\W_]*(Benvin{0,1}guts{0,1}|Catal.{1,2}|Catal&agrave;|Catal.{1,2}n|Catal&aacute;n)( *\\(.*\\)){0,1}[\\W_]*$', '^ca(-es){0,1}$', 1, '.*(Catal.{1,2}|Catal&agrave;|Catal.{1,2}n|Catal&aacute;n).*'),
(7, 'Valenciano', '^[\\W_]*(Benvin{0,1}guts{0,1}|Valenci[^a]{1,2}|Valenci&agrave;|Valenciano)( *\\(.*\\)){0,1}[\\W_]*$', '^ca(-es|-valencia){0,1}$', 1, '.*(Valenci.{1,2}|Valenci&agrave;|Valenciano).*'),
(8, 'Portugués', '^[\\W_]*(Boas-vindas|Portugu.{1,2}s|Portugu&ecirc;s{0,1})( *\\(.*\\)){0,1}[\\W_]*$', '^pt(-pt){0,1}$', 0, '.*(Portugu.{1,2}s|Portugu&ecirc;s{0,1}).*'),
(9, 'Alemán', '^[\\W_]*(Willkommen|Deutsch|Alem.{1,2}n|Alem&aacute;n)( *\\(.*\\)){0,1}[\\W_]*$', '^de(-de){0,1}$', 0, '.*(Deutsch|Alem.{1,2}n|Alem&aacute;n).*');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `languages_found`
--

CREATE TABLE IF NOT EXISTS `languages_found` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_analysis` bigint(20) NOT NULL,
  `id_language` bigint(20) DEFAULT NULL,
  `id_sus_language` bigint(20) DEFAULT NULL,
  `href` varchar(750) NOT NULL,
  `cod_fuente` mediumtext,
  `declaration_lang` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_analysis` (`id_analysis`),
  KEY `id_language` (`id_language`),
  KEY `id_sus_language` (`id_sus_language`),
  KEY `FKBD74125EB7114814` (`id_analysis`),
  KEY `FKBD74125EC8EA153A` (`id_sus_language`),
  KEY `FKBD74125E70D6090C` (`id_language`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `export_category`
--
ALTER TABLE `export_category`
  ADD CONSTRAINT `export_category_ibfk_1` FOREIGN KEY (`id_observatory`) REFERENCES `export_observatory` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_page`
--
ALTER TABLE `export_page`
  ADD CONSTRAINT `export_page_ibfk_1` FOREIGN KEY (`id_site`) REFERENCES `export_site` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_page_result`
--
ALTER TABLE `export_page_result`
  ADD CONSTRAINT `export_page_result_ibfk_1` FOREIGN KEY (`id_page`) REFERENCES `export_page` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_percentage`
--
ALTER TABLE `export_percentage`
  ADD CONSTRAINT `export_percentage_ibfk_1` FOREIGN KEY (`id_observatory`) REFERENCES `export_observatory` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `export_percentage_ibfk_2` FOREIGN KEY (`id_category`) REFERENCES `export_category` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `export_percentage_ibfk_3` FOREIGN KEY (`id_site`) REFERENCES `export_site` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `export_percentage_ibfk_4` FOREIGN KEY (`id_language`) REFERENCES `languages` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `export_site`
--
ALTER TABLE `export_site`
  ADD CONSTRAINT `export_site_ibfk_1` FOREIGN KEY (`id_category`) REFERENCES `export_category` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `languages_found`
--
ALTER TABLE `languages_found`
  ADD CONSTRAINT `FKBD74125E70D6090C` FOREIGN KEY (`id_language`) REFERENCES `languages` (`id`),
  ADD CONSTRAINT `FKBD74125EB7114814` FOREIGN KEY (`id_analysis`) REFERENCES `analysis` (`id`),
  ADD CONSTRAINT `FKBD74125EC8EA153A` FOREIGN KEY (`id_sus_language`) REFERENCES `languages` (`id`),
  ADD CONSTRAINT `languages_found_ibfk_1` FOREIGN KEY (`id_analysis`) REFERENCES `analysis` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `languages_found_ibfk_2` FOREIGN KEY (`id_language`) REFERENCES `languages` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `languages_found_ibfk_3` FOREIGN KEY (`id_sus_language`) REFERENCES `languages` (`id`) ON DELETE CASCADE;
