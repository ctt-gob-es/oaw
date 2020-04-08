 -- from istyf@github
 
INSERT INTO usuario(Usuario, Password,  Nombre, Apellidos, Departamento, Email) VALUES ('admin', md5('admin'), 'Test', 'User', 'Test Department', 'test.user@email.net');
INSERT INTO usuario_rol(Usuario, id_rol) VALUES (LAST_INSERT_ID(), 1);
INSERT INTO roles(id_rol, rol, id_tipo) VALUES (1, 'Admin', 1);

-- Create test values for periodicity

INSERT INTO periodicidad(nombre, dias, cronExpression) VALUES ('Every Minute', 0, '0 * * * * ?');
INSERT INTO periodicidad(nombre, dias, cronExpression) VALUES ('Hourly', 0, '0 0 * * * ?');
INSERT INTO periodicidad(nombre, dias, cronExpression) VALUES ('Daily', 1, '0 0 0 * * ?');