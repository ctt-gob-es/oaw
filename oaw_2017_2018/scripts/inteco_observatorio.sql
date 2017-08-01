--
-- Base de datos: `inteco_observatorio`
--

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

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
