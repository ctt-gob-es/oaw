UPDATE observatorio_plantillas set documento = LOAD_FILE('[RUTA]/Informe_Revision_Profundidad_v1.xlsx') WHERE nombre = 'IRA (XLSX)';
