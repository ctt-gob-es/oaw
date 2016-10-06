USE OAW;

--
-- Estructura de tabla para la tabla `tanalisis_css`
--

CREATE TABLE IF NOT EXISTS `tanalisis_css` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` mediumtext,
  `cod_analisis` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  FOREIGN KEY (cod_analisis) REFERENCES tanalisis (cod_analisis) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
