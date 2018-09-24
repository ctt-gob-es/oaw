DROP TABLE  IF EXISTS observatorio_estado;

CREATE TABLE observatorio_estado (
  id int(11) NOT NULL COMMENT 'Identificador autoincrementable',
  id_observatorio int(11) NOT NULL COMMENT 'Identificador del observatorio',
  id_ejecucion_observatorio int(11) NOT NULL COMMENT 'Identificador de la ejecución del observatorio',
  nombre varchar(256) NOT NULL COMMENT 'Nombre de la semilla',
  url varchar(256) NOT NULL COMMENT 'URL de la semilla',
  total_url int(11) NOT NULL COMMENT 'Total del URLs que se analizarán',
  total_url_analizadas int(11) DEFAULT '0',
  ultima_url varchar(256) NOT NULL COMMENT 'Última URL analizada',
  fecha_ultima_url datetime DEFAULT NULL COMMENT 'Fecha del fin de la última URL analizada',
  actual_url varchar(256) NOT NULL COMMENT 'URL que se está analizando',
  tiempo_medio int(11) DEFAULT NULL COMMENT 'Tiempo medio de análisis',
  tiempo_acumulado int(11) DEFAULT NULL
);

ALTER TABLE observatorio_estado
  ADD PRIMARY KEY (id),
  ADD UNIQUE KEY id_observatorio (id_observatorio,id_ejecucion_observatorio);


ALTER TABLE observatorio_estado
  MODIFY id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador autoincrementable';