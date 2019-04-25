-- Unificar la longitud de las urls de con la tabla tanalisis
ALTER TABLE export_page MODIFY COLUMN url VARCHAR(2050);

-- Default order
ALTER TABLE categorias_lista ORDER BY nombre ASC;

-- Email admin
UPDATE usuario SET email ='observ.accesibilidad@correo.gob.es' WHERE usuario='admin';