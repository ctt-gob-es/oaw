# Installation

* Download full repository
* Build and run with docker-compose:
    ```
    cd docker
    docker-compose build
    docker-compose up -d 
    ```
* Connect to database and change user data in `usuario` table.
* Go to http://localhost:8080 and login.
* From the menu, go to "Plantillas" and create the following:
  * ODS: portal/src/main/webapp/WEB-INF/templatesODT/Informe_Revision_Profunidad_v1.ods
  * XLSX: portal/src/main/webapp/WEB-INF/templatesODT/Informe_Revision_Profunidad_v1.xlsx
* Go to "Servicio diagnóstico" and analyze the desired website.
* Go to http://localhost:8025 and download the report.
