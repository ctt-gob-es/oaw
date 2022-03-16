UPDATE observatorio_plantillas set documento = LOAD_FILE('[RUTA]/Informe Revision Accesibilidad - Sitios web - v2.0.0.xlsx') WHERE nombre = 'IRA (XLSX)';
