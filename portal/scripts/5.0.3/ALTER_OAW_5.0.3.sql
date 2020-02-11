ALTER TABLE ambitos_lista ADD descripcion VARCHAR(1024) NULL;

UPDATE ambitos_lista SET descripcion = 'Administración General del Estado' WHERE id_ambito = 1;
UPDATE ambitos_lista SET descripcion = 'Comunidades Autónomas' WHERE id_ambito = 2;
UPDATE ambitos_lista SET descripcion = 'Entidades Locales' WHERE id_ambito = 3;
UPDATE ambitos_lista SET descripcion = 'Otros' WHERE id_ambito = 4;
