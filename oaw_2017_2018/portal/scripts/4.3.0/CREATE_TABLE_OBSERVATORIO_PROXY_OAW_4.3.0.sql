CREATE TABLE observatorio_proxy (
  status tinyint(1) NOT NULL,
  url varchar(64) NOT NULL,
  port varchar(5) NOT NULL
);

INSERT INTO observatorio_proxy (status, url, port) VALUES(1, '127.0.0.1', '18088');