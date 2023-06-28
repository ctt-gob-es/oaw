# OAW - Rastreador Docker
Este repositorio contiene todo lo necesario para desplegar el viejo rastreador utilizando `docker-compose`.

## Despliegue
Actualmente el despliegue se realiza a través del script `deploy.sh` que se encuentra en el directorio raíz.
`deploy.sh` se encarga de descomprimir `/mysql/database.tar.gz` que contiene el script sql necesario para inicializar la base de datos.

El archivo se encuentra comprimido debido a su gran tamaño y a las limitaciones de github respecto a los LFS.

Una vez descomprimido ejecutará el comando `docker compose up -d` y se montarán los contenedores.

**Recuerda** que antes de realizar el despligue es necesario incluir un war del rastreador en la carpeta `/tomcat` para lanzar la aplicación.

Realizado el despliegue, la aplicación estará disponible en `http://localhost:18081/oaw`
