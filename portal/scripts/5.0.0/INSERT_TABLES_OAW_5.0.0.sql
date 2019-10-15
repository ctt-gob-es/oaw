
INSERT INTO tguidelines (cod_guideline, des_guideline) VALUES (9, 'observatorio-une-en2019.xml');
INSERT INTO tguidelines (cod_guideline, des_guideline) VALUES (10, 'observatorio-accesibilidad.xml');
INSERT INTO cartucho (id_cartucho, nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES (9, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'UNE-EN2019', 15, 50, 9);
INSERT INTO cartucho (id_cartucho, nombre, instalado, aplicacion, numrastreos, numhilos, id_guideline) VALUES (10, 'es.inteco.accesibilidad.CartuchoAccesibilidad', 1, 'Accesibilidad', 15, 50, 10);
INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES(1, 9);
INSERT INTO usuario_cartucho (id_usuario, id_cartucho) VALUES(1, 10);
INSERT INTO observatorio_tipo (id_tipo, name) VALUES ('4', 'OTROS');
ALTER TABLE lista ADD eliminar TINYINT(1) NULL;