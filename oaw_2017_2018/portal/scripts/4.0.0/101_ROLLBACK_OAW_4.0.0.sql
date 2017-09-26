-- ROLLBACK DE LA VERSIÓN 4.0 A LA 3.5 DE OAW
USE oaw_pruebas_rollback;

ALTER TABLE lista ADD COLUMN dependencia varchar(512);

-- Restauramos las dependencias (se supone que ninguna puede tener más de una en este punto)
UPDATE lista l SET dependencia = (SELECT d.nombre FROM dependencia d, semilla_dependencia sd WHERE d.id_dependencia = sd.id_dependencia AND sd.id_lista = l.id_lista LIMIT 1);

-- Borrado de las tablas de multidependencia
DROP TABLE semilla_dependencia;
DROP TABLE dependencia;


