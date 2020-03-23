ALTER TABLE categorias_lista ADD clave VARCHAR(1024) NULL;
UPDATE categorias_lista SET clave = nombre;
ALTER TABLE categorias_lista CHANGE clave clave VARCHAR(1024) NOT NULL;


ALTER TABLE lista ADD observaciones VARCHAR(1024) NULL;
