CREATE TABLE `dependencia_ambito` ( 
	`id` INT NOT NULL AUTO_INCREMENT , 
	`id_dependencia` INT NOT NULL , 
	`id_ambito` INT NOT NULL , 
	PRIMARY KEY (`id`)
);

ALTER TABLE `dependencia` DROP `id_ambit`;