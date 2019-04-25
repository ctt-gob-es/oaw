-- Actualizar los códigos fuente de base de datos a base 64
UPDATE tanalisis SET cod_fuente = TO_BASE64(cod_fuente);
UPDATE tanalisis_css SET codigo = TO_BASE64(codigo)