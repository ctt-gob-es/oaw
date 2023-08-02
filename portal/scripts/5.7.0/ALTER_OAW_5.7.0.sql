ALTER TABLE observatorio_template_range
ADD COLUMN min_position_value FLOAT(4,2) NULL AFTER max_value_operator,
ADD COLUMN max_position_value FLOAT(4,2) NULL AFTER min_position_value,
ADD COLUMN min_position_value_operator VARCHAR(255) NULL AFTER max_position_value,
ADD COLUMN max_position_value_operator VARCHAR(255) NULL AFTER min_position_value_operator;

ALTER TABLE observatorio_send_historic_ranges
ADD COLUMN min_position_value FLOAT(4,2) NULL AFTER max_value_operator,
ADD COLUMN max_position_value FLOAT(4,2) NULL AFTER min_position_value,
ADD COLUMN min_position_value_operator VARCHAR(255) NULL AFTER max_position_value,
ADD COLUMN max_position_value_operator VARCHAR(255) NULL AFTER min_position_value_operator;

ALTER TABLE observatorio_ura_send_results
ADD COLUMN mid_previous_score FLOAT(4,2) NULL DEFAULT NULL;

ALTER TABLE observatorio_send_historic_results
ADD COLUMN mid_previous_score FLOAT(4,2) NULL DEFAULT NULL;

CREATE TABLE apiKey (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  nombre varchar(100) COLLATE utf8_bin NOT NULL,
  descripcion varchar(200) COLLATE utf8_bin DEFAULT NULL,
  apiKey varchar(255) COLLATE utf8_bin DEFAULT NULL,
  activa bit(1) DEFAULT b'0',
  PRIMARY KEY (id),
  KEY id (id)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


