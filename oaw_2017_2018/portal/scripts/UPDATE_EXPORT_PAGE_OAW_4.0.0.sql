-- Unificar la longitud de las urls de con la tabla tanalisis
ALTER TABLE export_page MODIFY COLUMN url VARCHAR(2050);