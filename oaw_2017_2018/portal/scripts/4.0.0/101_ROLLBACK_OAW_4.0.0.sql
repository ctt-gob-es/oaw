-- ROLLBACK DE LA VERSIÓN 4.0 A LA 3.5 DE OAW
USE oaw_pruebas_rollback;

ALTER TABLE lista ADD COLUMN dependencia varchar(512);

-- Restauramos las dependencias (se supone que ninguna puede tener más de una en este punto)
-- SELECT l.id_lista,l.dependencia,d.nombre FROM lista l, semilla_dependencia sd, dependencia d WHERE l.id_lista=sd.id_lista AND sd.id_dependencia=d.id_dependencia
UPDATE lista l SET dependencia = (SELECT d.nombre FROM dependencia d, semilla_dependencia sd WHERE d.id_dependencia = sd.id_dependencia AND sd.id_lista = l.id_lista);

-- Borrado de las tablas de multidependencia
DROP TABLE semilla_dependencia;
DROP TABLE dependencia;


