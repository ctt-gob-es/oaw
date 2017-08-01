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
-- Base de datos: `inteco_lenox_2`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SEXISTA_CATEGORIA`
--

CREATE TABLE IF NOT EXISTS `SEXISTA_CATEGORIA` (
  `ID_CATEGORIA` int(10) NOT NULL AUTO_INCREMENT,
  `CATEGORIA` varchar(250) DEFAULT NULL,
  `DESCRIPCION` varchar(250) DEFAULT NULL,
  `A` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID_CATEGORIA`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SEXISTA_EXCEPCIONES1`
--

CREATE TABLE IF NOT EXISTS `SEXISTA_EXCEPCIONES1` (
  `ID_EXCEPCION` int(10) NOT NULL DEFAULT '0',
  `EXCEPCION` varchar(250) DEFAULT NULL,
  `D` varchar(250) DEFAULT NULL,
  `POSICION` smallint(5) DEFAULT NULL,
  `ID_TERMINO` int(10) DEFAULT NULL,
  `F6` double(15,5) DEFAULT NULL,
  PRIMARY KEY (`ID_EXCEPCION`),
  KEY `T1E1` (`ID_TERMINO`),
  KEY `TERMINO` (`ID_TERMINO`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SEXISTA_EXCEPCIONES2`
--

CREATE TABLE IF NOT EXISTS `SEXISTA_EXCEPCIONES2` (
  `ID_EXCEPCION` int(10) NOT NULL DEFAULT '0',
  `EXCEPCION` varchar(250) DEFAULT NULL,
  `D` varchar(250) DEFAULT NULL,
  `POSICION` smallint(5) DEFAULT NULL,
  PRIMARY KEY (`ID_EXCEPCION`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SEXISTA_EXCEPCIONES3`
--

CREATE TABLE IF NOT EXISTS `SEXISTA_EXCEPCIONES3` (
  `ID_EXCEPCION` int(10) NOT NULL DEFAULT '0',
  `EXCEPCION` varchar(250) DEFAULT NULL,
  `D` varchar(250) DEFAULT NULL,
  `POSICION` smallint(5) DEFAULT NULL,
  `ID_CATEGORIA` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID_EXCEPCION`),
  KEY `Y1E3` (`ID_CATEGORIA`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SEXISTA_RASTREOS`
--

CREATE TABLE IF NOT EXISTS `SEXISTA_RASTREOS` (
  `ID_RASTREO` varchar(45) NOT NULL,
  `FECHA` datetime NOT NULL,
  `USUARIO` varchar(45) NOT NULL DEFAULT '0',
  `TIEMPO` decimal(10,0) NOT NULL,
  PRIMARY KEY (`ID_RASTREO`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SEXISTA_RESULTADOS`
--

CREATE TABLE IF NOT EXISTS `SEXISTA_RESULTADOS` (
  `ID_RESULTADO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_RASTREO` varchar(200) NOT NULL,
  `ID_TERMINO` decimal(10,0) DEFAULT '0',
  `CONTEXTO` varchar(200) DEFAULT NULL,
  `URL_TERMINO` varchar(200) NOT NULL,
  `EN_SINGULAR` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID_RESULTADO`),
  KEY `FK_RESULTADO_RASTREOS` (`ID_RASTREO`),
  KEY `FK_RESULTADO_TERMINOS` (`ID_TERMINO`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SEXISTA_SUGERENCIAS`
--

CREATE TABLE IF NOT EXISTS `SEXISTA_SUGERENCIAS` (
  `ID_SUGERENCIA` int(10) NOT NULL AUTO_INCREMENT,
  `ALTERNATIVA` varchar(250) DEFAULT NULL,
  `DESCRIPCION` varchar(250) DEFAULT NULL,
  `R` tinyint(1) NOT NULL,
  `ID_TERMINO` int(10) DEFAULT NULL,
  `GRAVEDAD` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID_SUGERENCIA`),
  KEY `T1S1` (`ID_TERMINO`),
  KEY `TERMINO` (`ID_TERMINO`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SEXISTA_TERMINOS`
--

CREATE TABLE IF NOT EXISTS `SEXISTA_TERMINOS` (
  `ID_TERMINO` int(10) NOT NULL DEFAULT '0',
  `TERMINO` varchar(250) DEFAULT NULL,
  `DESCRIPCION` varchar(250) DEFAULT NULL,
  `USO_GLOBAL` tinyint(1) NOT NULL,
  `FEMENINO` varchar(250) DEFAULT NULL,
  `PLURAL` varchar(250) DEFAULT NULL,
  `FEMENINO_PLURAL` varchar(250) DEFAULT NULL,
  `FU` tinyint(1) NOT NULL,
  `ID_CATEGORIA` int(10) DEFAULT NULL,
  `PS` int(10) DEFAULT NULL,
  `PP` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID_TERMINO`),
  KEY `Y1T1` (`ID_CATEGORIA`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
