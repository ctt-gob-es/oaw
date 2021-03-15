DROP TABLE IF EXISTS `observatorio_extra_configuration`;
CREATE TABLE `observatorio_extra_configuration` ( 
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL ,
	`key` VARCHAR(255) NOT NULL , 
	`value` VARCHAR(255) NOT NULL , 
    PRIMARY KEY(`id`),
	UNIQUE (`key`)
);

INSERT INTO `observatorio_extra_configuration` (`name`, `key`, `value`) VALUES ('observatory.extra.config.theshold', 'umbral', '30');
INSERT INTO `observatorio_extra_configuration` (`name`, `key`, `value`) VALUES ('observatory.extra.config.realunch.timeout','timemout', '60');
INSERT INTO `observatorio_extra_configuration` (`name`, `key`, `value`) VALUES ('observatory.extra.config.width','width', '10');
INSERT INTO `observatorio_extra_configuration` (`name`, `key`, `value`) VALUES ('observatory.extra.config.depth','depth', '10');
INSERT INTO `observatorio_extra_configuration` (`name`, `key`, `value`) VALUES ('observatory.extra.config.autorelaunch','autorelaunch', '0');
--INSERT INTO `observatorio_extra_configuration` (`name`, `key`, `value`) VALUES ('observatory.extra.config.first.classification.threshold','firstclassthreshold', '0.5');
--INSERT INTO `observatorio_extra_configuration` (`name`, `key`, `value`) VALUES ('observatory.extra.config.second.classification.threshold','secondclassthreshold', '2');

ALTER TABLE `tanalisis_accesibilidad` ADD `COD_FUENTE` MEDIUMTEXT;

ALTER TABLE `observatorios_realizados` ADD `tags` TEXT NULL;

UPDATE observatorios_realizados obr SET obr.tags = (SELECT ob.tags from observatorio ob where ob.id_observatorio = obr.id_observatorio);

-- uras

ALTER TABLE `dependencia` ADD `emails` TEXT NULL;
ALTER TABLE `dependencia` ADD `id_ambit` INT NULL;
ALTER TABLE `dependencia` ADD `send_auto` INT NULL;
ALTER TABLE `dependencia` ADD `official` INT NULL;
ALTER TABLE `dependencia` ADD `id_tag` INT NULL;


CREATE TABLE `observatorio_range` ( 
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL ,
	`min_value` float(4,2) NOT NULL , 
	`max_value` float(4,2) ,
	`min_value_operator` VARCHAR(255) NOT NULL ,
	`max_value_operator` VARCHAR(255), 
    PRIMARY KEY(`id`)
);

CREATE TABLE `observatorio_template_range` ( 
	`id` INT NOT NULL AUTO_INCREMENT,
	`id_observatory_execution` INT NOT NULL,
	`name` VARCHAR(255) NOT NULL ,
	`min_value` float(4,2) NOT NULL , 
	`max_value` float(4,2) ,
	`min_value_operator` VARCHAR(255) NOT NULL ,
	`max_value_operator` VARCHAR(255), 
	`template` mediumtext NOT NULL,
    PRIMARY KEY(`id`)
);

CREATE TABLE `observatorio_ura_send_results` ( 
	`id` INT NOT NULL AUTO_INCREMENT,
	`id_observatory_execution` INT NOT NULL,
	`id_ura` INT NOT NULL,
	`id_range` INT NOT NULL,
	`range_value` float(4,2) ,
	`custom_text` mediumtext NOT NULL,
	`send` int(11) NOT NULL DEFAULT '0',
    PRIMARY KEY(`id`)
);
ALTER TABLE observatorio_ura_send_results ADD UNIQUE unique_index(id_observatory_execution, id_ura);

CREATE TABLE `observatorio_send_configuration` ( 
	`id` INT NOT NULL AUTO_INCREMENT,
	`id_observatory_execution` INT NOT NULL,
	`ids_observatory_execution_evolution` VARCHAR(255),
	`has_custom_texts` INT NOT NULL DEFAULT 0,
    PRIMARY KEY(`id`)
);

CREATE TABLE `observatorio_send_configuration_comparision` ( 
	`id` INT NOT NULL AUTO_INCREMENT,
	`id_observatory_execution` INT NOT NULL,
	`id_tag` INT NOT NULL,
	`date_first` VARCHAR(255),
	`date_previous` VARCHAR(255),
    PRIMARY KEY(`id`)
);

-- only DES
RENAME TABLE `observatorio_template_custom_text_ura` TO `observatorio_ura_send_results`;
-- /only DES

ALTER TABLE `observatorio_plantillas` ADD `type` VARCHAR(10) NULL;
UPDATE `observatorio_plantillas` SET `type`= 'odt';



