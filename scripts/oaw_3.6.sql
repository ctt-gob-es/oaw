ALTER TABLE  `export_page` CHANGE  `url`  `url` VARCHAR( 2050 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ;

-- 1. Crear nueva tabla de depndencias

--create table dependencia (id_dependencia bigint(20) AUTO_INCREMENT, nombre varchar(1024), primary key pk_dependencia (id_dependencia));

CREATE TABLE IF NOT EXISTS dependencia (
  id_dependencia bigint(20) NOT NULL AUTO_INCREMENT,
  nombre varchar(200) CHARACTER SET latin1 COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (id_dependencia),
  UNIQUE KEY nombre (nombre),
  KEY id_dependencia (id_dependencia)
);


-- 2. Insertar las dependencias generadas y normalizadas

insert into dependencia(nombre) select distinct(dependencia) from lista where dependencia is not null order by dependencia asc;

-- 2.1. Actualizar los nombres que han cambiado

update dependencia set nombre ='Ministerio de Energía, Turismo y Agenda Digital' where nombre ='Ministerio de Industria, Energía y Turismo';

update dependencia set nombre ='Ministerio de Hacienda y Función Pública' where nombre ='Ministerio de Hacienda y Administraciones Públicas';

update dependencia set nombre ='Ministerio de Presidencia' where nombre ='Ministerio de la Presidencia y para las Administraciones Territoriales';

update dependencia set nombre ='Ministerio de Economía y Competitividad' where nombre ='Ministerio de Economía, Industria y Competitividad';

update dependencia set nombre ='Ministerio de Agricultura, Alimentación y Medio Ambiente' where nombre ='Ministerio de Agricultura y Pesca, Alimentación y Medio Ambiente';
 

-- 2.2 Revisar y normalizar <-- manual

-- 3. Crear la tabla de relacion semilla-dependencia
create table semilla_dependencia (id_lista bigint(20), id_dependencia bigint(20), primary key pk_semilla_dependencia (id_lista,id_dependencia));--, foreign key (id_lista) references lista (id_lista), foreign key (id_dependencia) references dependencia (id_dependencia) <-- lista esta MyISAM que no permite FK

-- 4. Rellenar la tabla de relacion con todas aquellas que sea posible depues de insertar en depencia
-- update lista l set l.dependencia_id=(select d.id from dependencia d where d.nombre=l.dependencia)
insert into semilla_dependencia(id_lista, id_dependencia) SELECT l.id_lista, d.id_dependencia FROM lista l, dependencia d where l.dependencia=d.nombre;

-- 5. Revisar aquellas semillas que teniendo dependencia "string" no estén relacionadas con ninguna en semilla_dependencia o que no aparezcan en esa tabla
SELECT * FROM lista l WHERE l.id_lista not in (select sd.id_lista from semilla_dependencia sd) order by l.id_lista;
SELECT * FROM lista l WHERE l.dependencia is not null and l.id_lista not in (select sd.id_lista from semilla_dependencia sd) order by l.id_lista;


-- 6. Eliminar la colimna dependencia de la tabla lista *** OPCIONAL ***
alter table lista drop column dependencia;

