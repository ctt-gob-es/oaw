-- SOPORTE PARA SEMILLAS CON MULTIDEPENDENCIA
USE oaw_pruebas_rollback;

-- TABLA DEPENDENCIA 
CREATE TABLE IF NOT EXISTS dependencia (
  id_dependencia bigint(20) NOT NULL AUTO_INCREMENT,
  nombre varchar(200) CHARACTER SET latin1 COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (id_dependencia),
  UNIQUE KEY nombre (nombre),
  KEY id_dependencia (id_dependencia)
);

-- INSERTAR LAS DEPENENCIAS SACADAS DE LA TABLA LISTA
INSERT INTO dependencia(nombre) SELECT DISTINCT(dependencia) FROM lista WHERE dependencia IS NOT NULL ORDER BY dependencia ASC;


-- ACTUALIZAMOS LAS DEPENDENCIA QUE HAN CAMBIADO DE NOMBRE
-- UPDATE dependencia SET nombre ='Ministerio de Energía, Turismo y Agenda Digital' WHERE nombre ='Ministerio de Industria, Energía y Turismo';
-- UPDATE dependencia SET nombre ='Ministerio de Hacienda y Función Pública' WHERE nombre ='Ministerio de Hacienda y Administraciones Públicas';
-- UPDATE dependencia SET nombre ='Ministerio de Presidencia' WHERE nombre ='Ministerio de la Presidencia y para las Administraciones Territoriales';
-- UPDATE dependencia SET nombre ='Ministerio de Economía y Competitividad' WHERE nombre ='Ministerio de Economía, Industria y Competitividad';
-- UPDATE dependencia SET nombre ='Ministerio de Agricultura, Alimentación y Medio Ambiente' WHERE nombre ='Ministerio de Agricultura y Pesca, Alimentación y Medio Ambiente';
 
-- TABLA SEMILLA_DEPENDENCIA
CREATE TABLE semilla_dependencia (
	id_lista bigint(20), 
	id_dependencia bigint(20), 
	PRIMARY KEY pk_semilla_dependencia (id_lista,id_dependencia));
-- foreign key (id_lista) references lista (id_lista), foreign key (id_dependencia) references dependencia (id_dependencia) <-- lista esta MyISAM que no permite FK

-- RELLENAMOS LA TABLA ANTERIOR CON  LA TABLA DE RELACION CON LAS DEPENDENCIAS DE LA NUEVA TABLA
INSERT INTO semilla_dependencia(id_lista, id_dependencia) SELECT l.id_lista, d.id_dependencia FROM lista l, dependencia d WHERE l.dependencia=d.nombre;

-- ELIMINAR LA COLUMNA ANTERIOR DE DEPENDENCIA
ALTER TABLE lista DROP COLUMN dependencia;

-- Revisar aquellas semillas que teniendo dependencia "string" no estén relacionadas con ninguna en semilla_dependencia o que no aparezcan en esa tabla
-- SELECT * FROM lista l WHERE l.id_lista not in (select sd.id_lista from semilla_dependencia sd) order by l.id_lista;
-- SELECT * FROM lista l WHERE l.dependencia is not null and l.id_lista not in (select sd.id_lista from semilla_dependencia sd) order by l.id_lista;
