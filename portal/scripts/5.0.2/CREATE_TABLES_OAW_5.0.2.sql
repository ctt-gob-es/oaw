CREATE TABLE tanalisis_accesibilidad ( 
	id INT NOT NULL AUTO_INCREMENT , 
	id_analisis INT NOT NULL , 
	urls VARCHAR(2048) NOT NULL , 
	PRIMARY KEY (id)
);

ALTER TABLE basic_service ADD filename VARCHAR(1024) NULL;