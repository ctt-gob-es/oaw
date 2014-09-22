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
-- Base de datos: `inteco_intav`
--

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
-- Estructura de tabla para la tabla `tguidelines`
--

CREATE TABLE IF NOT EXISTS `tguidelines` (
  `cod_guideline` bigint(20) NOT NULL AUTO_INCREMENT,
  `des_guideline` varchar(50) NOT NULL,
  PRIMARY KEY (`cod_guideline`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Volcado de datos para la tabla `tguidelines`
--

INSERT INTO `tguidelines` (`cod_guideline`, `des_guideline`) VALUES
(1, 'une-139803.xml'),
(2, 'wcag-1-0.xml'),
(3, 'wcag-2-0.xml'),
(4, 'observatorio-inteco-1-0.xml'),
(5, 'uneER-139803.xml'),
(6, 'wcagER-1-0.xml');

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

--
-- Restricciones para tablas volcadas
--

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
