Attribute VB_Name = "Módulo1"
Sub Sub_OAW_0()
'
' Sub_OAW_0 Macro
'

'
    Sheets("00.Info").Select
    Range("B9:N9").Select
    ActiveCell.FormulaR1C1 = _
        "Objeto" & Chr(10) & "" & Chr(10) & "El documento sirve como soporte para la revisión de accesibilidad en profundidad, es decir, permite anotar los resultados del análisis automático y manual realizado sobre un sitio web o aplicación para dispositivo móvil. " & Chr(10) & "" & Chr(10) & "El documento sigue la estructura de la WCAG-EM (Evaluation Methodology) para generar un informe estructurado a partir de los datos que us" & _
        "ted proporcione. Para su aplicación, las personas encargadas deberán conocer la aplicación de las Pautas de Accesibilidad para el Contenido Web (WCAG). El documento no sirve para realizar ninguna comprobación de accesibilidad." & _
        ""
    Range("B11:N11").Select
    ActiveCell.FormulaR1C1 = _
        "Cómo utilizar el documento" & Chr(10) & "" & Chr(10) & "El orden recomendado para cumplimentar la información es el siguiente:" & Chr(10) & "" & Chr(10) & "1. Rellenar en primer lugar la información de las pestañas ""01.Definición de ámbito"" y ""02. Tecnologías"""
    Range("B13:N13").Select
    ActiveCell.FormulaR1C1 = _
        "2. En la pestaña ""03. Muestra"" deberán indicarse las páginas que forman parte de la muestra. " & Chr(10) & "Para su selección deberá tenerse en cuenta lo indicado en el apdo. 3.2 de la Decisión de Ejecución (UE) 2018/1524. De acuerdo con la Decisión, por «página» se entenderá una página web o una pantalla de una aplicación para dispositivos móviles." & Chr(10) & "" & Chr(10) & "- Entre las páginas  selecc" & _
        "ionadas, en caso de existir, deberán figurar de forma obligatoria las siguientes: Página de inicio, Inicio de sesión, Mapa del sitio, Contacto, Ayuda, Información legal y página de la Declaración de accesibilidad. " & Chr(10) & "" & Chr(10) & "- Por cada página seleccionada en la muestra deberá indicarse: Nombre corto, Tipo de página, URL de la página o Nombre de la pantalla y Direccionamiento" & _
        " alternativo (útil en los sitios web con URIs dinámicas así como en las aplicaciones para dispositivos móviles)." & Chr(10) & "" & Chr(10) & "- Cuando la muestra sea menor de 30 URLs, las URLs restantes se dejarán vacías. " & Chr(10) & "" & Chr(10) & "- También debe tenerse en cuenta que al menos un 10% de la muestra debe ser aleatorio e indicarse en al columna tipo." & _
        ""
    Range("B15:N15").Select
    ActiveCell.FormulaR1C1 = _
        "3. En las pestañas ""P1. Perceptible"", ""P2. Operable"", ""P3. Comprensible"" y ""P4. Robusto"" deberá cumplimentarse, para todos los criterios de éxito, y dentro de estos, para cada una de las páginas de la muestra, el resultado de dicha comprobación indicándolo en la columna resultado." & Chr(10) & "" & Chr(10) & " Para realizar la evaluación de la accesibilidad puede servir de ayuda el lis" & _
        "tado de herramientas automáticas y manuales disponibles en: " & Chr(10) & "" & Chr(10) & "- Para sitios web: listado de herramientas disponibles en el Anexo de la Guía de validación de accesibilidad web" & Chr(10) & "" & Chr(10) & "- Para aplicaciones para dispositivos móviles: listado de herramientas disponibles en el Anexo IV de la Guía de accesibilidad de aplicaciones móviles" & _
        ""
    Range("B17:N17").Select
    ActiveCell.FormulaR1C1 = _
        "4. Los resultados agregados del proceso de validación serán consolidados automáticamente en la pestaña ""RESULTADOS""."
    Range("B18").Select
    ActiveWindow.SmallScroll Down:=-33
    Range("A1").Select
    Sheets("01.Definicción de ámbito").Select
    ActiveWindow.SmallScroll Down:=-12
    Range("A1").Select
    Sheets("02.Tecnologías").Select
    Range("B7:L7").Select
    ActiveCell.FormulaR1C1 = _
        "Para añadir otras tecnologías, seleccionar ""Otras"" y rellenar los campos ""Nombre Tecnología"" y ""URL de la Especificación o Descripción""." & Chr(10) & "El campo ""URL de la especificación"" debe identificar la especificación de la tecnología utilizada."
    Range("B8").Select
    ActiveWindow.SmallScroll Down:=-18
    Range("A1").Select
    Sheets("03.Muestra").Select
    ActiveWindow.SmallScroll Down:=24
    Range("D40").Select
    ActiveCell.FormulaR1C1 = "=30-COUNTIF(R[-32]C:R[-3]C,"""")"
    Range("D42").Select
    ActiveCell.FormulaR1C1 = "=R[-2]C-R[2]C"
    Range("D44").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-36]C:R[-7]C,""Aleatoria"")"
    Range("D47").Select
    ActiveCell.FormulaR1C1 = "=IF(AND(R[-3]C>=0.1*R[-5]C,R[-3]C>0),""SI"",""NO"")"
    Range("D49").Select
    ActiveWindow.SmallScroll Down:=-45
    Range("A1").Select
    ActiveWindow.SmallScroll Down:=9
    Range("A1").Select
    Sheets("00.Info").Select
End Sub
Sub Sub_OAW_1()
'
' Sub_OAW_1 Macro
'

    Sheets("P4.Robusto").Select
    Range("B19").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B20").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B49").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B52").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B53").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B54").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B55").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B85").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B86").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B87").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B88").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B89").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B90").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B91").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B92").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B93").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B94").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B95").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B96").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B97").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B98").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B99").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B100").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B101").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B102").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B103").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B104").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B105").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B106").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B107").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B108").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B109").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B110").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B111").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B112").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B113").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B114").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B115").Select
    ActiveWindow.SmallScroll Down:=-87
    Range("C19").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C20").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C49").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C50").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C52").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C53").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C54").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C55").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C82").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C85").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C86").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C87").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C88").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C89").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C90").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C91").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C92").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C93").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C94").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C95").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C96").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C97").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C98").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C99").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C100").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C101").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C102").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C103").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C104").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C105").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C106").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C107").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C108").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C109").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C110").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C111").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C112").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C113").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C114").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C115").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C116").Select
    ActiveWindow.SmallScroll Down:=-117
    Range("G12").Select
    ActiveCell.FormulaR1C1 = "=R[8]C[3]+R[41]C[3]+R[74]C[3]"
    Range("G13").Select
    ActiveCell.FormulaR1C1 = "=R[7]C[2]+R[40]C[2]+R[73]C[2]"
    Range("G14").Select
    ActiveCell.FormulaR1C1 = "=R[6]C[1]+R[39]C[1]+R[72]C[1]"
    Range("G15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("G16").Select
    ActiveWindow.LargeScroll Down:=1
    Range("G33").Select
    ActiveWindow.SmallScroll Down:=-36
    Range("H12").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H13").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H14").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("K12").Select
    ActiveCell.FormulaR1C1 = "=R[8]C[-4]+R[41]C[-4]+R[74]C[-4]"
    Range("K13").Select
    ActiveCell.FormulaR1C1 = "=R[7]C[-5]+R[40]C[-5]+R[73]C[-5]"
    Range("K14").Select
    ActiveCell.FormulaR1C1 = "=R[6]C+R[39]C+R[72]C"
    Range("K15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("L12").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L13").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L14").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("L16").Select
    ActiveWindow.SmallScroll Down:=15
    Range("F20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K21").Select
    ActiveWindow.SmallScroll Down:=24
    Range("F53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J54").Select
    ActiveCell.FormulaR1C1 = ""
    Range("J53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K54").Select
    ActiveWindow.SmallScroll Down:=33
    Range("F86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],"""")"
    Range("I86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K87").Select
    ActiveWindow.SmallScroll Down:=-84
    ActiveWindow.ScrollColumn = 1
    Range("A1").Select
    Sheets("00.Info").Select
End Sub
Sub Sub_OAW_2()
'
' Sub_OAW_2 Macro
'

'
    Sheets("P3.Comprensible").Select
    Range("B19").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B20").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B52").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B53").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B54").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B55").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B85").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B86").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B87").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B88").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B89").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B90").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B91").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B92").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B93").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B94").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B95").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B96").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B97").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B98").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B99").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B100").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B101").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B102").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B103").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B104").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B105").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B106").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B107").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B108").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B109").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B110").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B111").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B112").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B113").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B114").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B118").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B119").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B120").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B121").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B122").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B123").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B124").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B125").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B126").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B127").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B128").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B129").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B130").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B131").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B132").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B133").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B134").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B135").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B136").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B137").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B138").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B139").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B140").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B141").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B142").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B143").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B144").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B145").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B146").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B147").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B148").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B151").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B152").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B153").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B154").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B155").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B156").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B157").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B158").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B159").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B160").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B161").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B162").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B163").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B164").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B165").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B166").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B167").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B168").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B169").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B170").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B171").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B172").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B173").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B174").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B175").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B176").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B177").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B178").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B179").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B180").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B184").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B185").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B186").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B187").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B188").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B189").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B190").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B191").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B192").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B193").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B194").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B195").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B196").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B197").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B198").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B199").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B200").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B201").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B202").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B203").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B204").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B205").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B206").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B207").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B208").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B209").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B210").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B211").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B212").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B213").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B217").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B218").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B219").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B220").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B221").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B222").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B223").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B224").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B225").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B226").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B227").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B228").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B229").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B230").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B231").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B232").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B233").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B234").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B235").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B236").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B237").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B238").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B239").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B240").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B241").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B242").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B243").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B244").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B245").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B246").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B247").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B248").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B249").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B250").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B251").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B252").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B253").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B254").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B255").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B256").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B257").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B258").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B259").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B260").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B261").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B262").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B263").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B264").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B265").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B266").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B267").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B268").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B269").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B270").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B271").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B272").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B273").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B274").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B275").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B276").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B277").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B278").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B279").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B280").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B283").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B284").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B285").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B286").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B287").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B288").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B289").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B290").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B291").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B292").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B293").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B294").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B295").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B296").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B297").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B298").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B299").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B300").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B301").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B302").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B303").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B304").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B305").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B306").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B307").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B308").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B309").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B310").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B311").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B312").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B316").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B317").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B318").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B319").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B320").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B321").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B322").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B323").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B324").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B325").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B326").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B327").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B328").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B329").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B330").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B331").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B332").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B333").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B334").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B335").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B336").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B337").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B338").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B339").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B340").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B341").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B342").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B343").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B344").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B345").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B346").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B347").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B348").Select
    ActiveWindow.SmallScroll Down:=-45
    ActiveWindow.ScrollRow = 235
    ActiveWindow.ScrollRow = 203
    ActiveWindow.ScrollRow = 161
    ActiveWindow.ScrollRow = 152
    ActiveWindow.ScrollRow = 134
    ActiveWindow.ScrollRow = 127
    ActiveWindow.ScrollRow = 122
    ActiveWindow.ScrollRow = 115
    ActiveWindow.ScrollRow = 109
    ActiveWindow.ScrollRow = 105
    ActiveWindow.ScrollRow = 99
    ActiveWindow.ScrollRow = 94
    ActiveWindow.ScrollRow = 88
    ActiveWindow.ScrollRow = 83
    ActiveWindow.ScrollRow = 79
    ActiveWindow.ScrollRow = 75
    ActiveWindow.ScrollRow = 70
    ActiveWindow.ScrollRow = 67
    ActiveWindow.ScrollRow = 63
    ActiveWindow.ScrollRow = 60
    ActiveWindow.ScrollRow = 56
    ActiveWindow.ScrollRow = 52
    ActiveWindow.ScrollRow = 50
    ActiveWindow.ScrollRow = 47
    ActiveWindow.ScrollRow = 46
    ActiveWindow.ScrollRow = 44
    ActiveWindow.ScrollRow = 43
    ActiveWindow.ScrollRow = 42
    ActiveWindow.ScrollRow = 41
    ActiveWindow.ScrollRow = 40
    ActiveWindow.ScrollRow = 39
    ActiveWindow.ScrollRow = 38
    ActiveWindow.ScrollRow = 37
    ActiveWindow.ScrollRow = 35
    ActiveWindow.ScrollRow = 33
    ActiveWindow.ScrollRow = 30
    ActiveWindow.ScrollRow = 28
    ActiveWindow.ScrollRow = 26
    ActiveWindow.ScrollRow = 25
    ActiveWindow.ScrollRow = 22
    ActiveWindow.ScrollRow = 20
    ActiveWindow.ScrollRow = 19
    ActiveWindow.ScrollRow = 18
    ActiveWindow.ScrollRow = 17
    ActiveWindow.ScrollRow = 16
    ActiveWindow.ScrollRow = 15
    ActiveWindow.ScrollRow = 14
    ActiveWindow.ScrollRow = 13
    ActiveWindow.ScrollRow = 14
    ActiveWindow.SmallScroll Down:=-6
    Range("C19").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C20").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C49").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C52").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C53").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C54").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C55").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C85").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C86").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C87").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C88").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C89").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C90").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C91").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C92").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C93").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C94").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C95").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C96").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C97").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C98").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C99").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C100").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C101").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C102").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C103").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C104").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C105").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C106").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C107").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C108").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C109").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C110").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C111").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C112").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C113").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C114").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C115").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C118").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C119").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C120").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C121").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C122").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C123").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C124").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C125").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C126").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C127").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C128").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C129").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C130").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C131").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C132").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C133").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C134").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C135").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C136").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C137").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C138").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C139").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C140").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C141").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C142").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C143").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C144").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C145").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C146").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C147").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C148").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C151").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C152").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C153").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C154").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C155").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C156").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C157").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C158").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C159").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C160").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C161").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C162").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C163").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C164").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C165").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C166").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C167").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C168").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C169").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C170").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C171").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C172").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C173").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C174").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C175").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C176").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C177").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C178").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C179").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C180").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C181").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C184").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C185").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C186").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C187").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C188").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C189").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C190").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C191").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C192").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C193").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C194").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C195").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C196").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C197").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C198").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C199").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C200").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C201").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C202").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C203").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C204").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C205").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C206").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C207").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C208").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C209").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C210").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C211").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C212").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C213").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C214").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C217").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C218").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C219").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C220").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C221").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C222").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C223").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C224").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C225").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C226").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C227").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C228").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C229").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C230").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C231").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C232").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C233").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C234").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C235").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C236").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C237").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C238").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C239").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C240").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C241").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C242").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C243").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C244").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C245").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C246").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C247").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C250").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C251").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C252").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C253").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C254").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C255").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C256").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C257").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C258").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C259").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C260").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C261").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C262").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C263").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C264").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C265").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C266").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C267").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C268").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C269").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C270").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C271").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C272").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C273").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C274").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C275").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C276").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C277").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C278").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C279").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C280").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C283").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C284").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C285").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C286").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C287").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C288").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C289").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C290").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C291").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C292").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C293").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C294").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C295").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C296").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C297").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C298").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C299").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C300").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C301").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C302").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C303").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C304").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C305").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C306").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C307").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C308").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C309").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C310").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C311").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C312").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C313").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C316").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C317").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C318").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C319").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C320").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C321").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C322").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C323").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C324").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C325").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C326").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C327").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C328").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C329").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C330").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C331").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C332").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C333").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C334").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C335").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C336").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C337").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C338").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C339").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C340").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C341").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C342").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C343").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C344").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C345").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C346").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C347").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C348").Select
    ActiveWindow.SmallScroll Down:=-6
    ActiveWindow.ScrollRow = 321
    ActiveWindow.ScrollRow = 320
    ActiveWindow.ScrollRow = 319
    ActiveWindow.ScrollRow = 318
    ActiveWindow.ScrollRow = 316
    ActiveWindow.ScrollRow = 315
    ActiveWindow.ScrollRow = 312
    ActiveWindow.ScrollRow = 310
    ActiveWindow.ScrollRow = 307
    ActiveWindow.ScrollRow = 305
    ActiveWindow.ScrollRow = 300
    ActiveWindow.ScrollRow = 296
    ActiveWindow.ScrollRow = 293
    ActiveWindow.ScrollRow = 288
    ActiveWindow.ScrollRow = 283
    ActiveWindow.ScrollRow = 277
    ActiveWindow.ScrollRow = 274
    ActiveWindow.ScrollRow = 269
    ActiveWindow.ScrollRow = 264
    ActiveWindow.ScrollRow = 260
    ActiveWindow.ScrollRow = 256
    ActiveWindow.ScrollRow = 253
    ActiveWindow.ScrollRow = 246
    ActiveWindow.ScrollRow = 243
    ActiveWindow.ScrollRow = 238
    ActiveWindow.ScrollRow = 234
    ActiveWindow.ScrollRow = 229
    ActiveWindow.ScrollRow = 224
    ActiveWindow.ScrollRow = 219
    ActiveWindow.ScrollRow = 216
    ActiveWindow.ScrollRow = 211
    ActiveWindow.ScrollRow = 202
    ActiveWindow.ScrollRow = 199
    ActiveWindow.ScrollRow = 195
    ActiveWindow.ScrollRow = 192
    ActiveWindow.ScrollRow = 189
    ActiveWindow.ScrollRow = 187
    ActiveWindow.ScrollRow = 183
    ActiveWindow.ScrollRow = 181
    ActiveWindow.ScrollRow = 178
    ActiveWindow.ScrollRow = 176
    ActiveWindow.ScrollRow = 173
    ActiveWindow.ScrollRow = 170
    ActiveWindow.ScrollRow = 168
    ActiveWindow.ScrollRow = 165
    ActiveWindow.ScrollRow = 163
    ActiveWindow.ScrollRow = 160
    ActiveWindow.ScrollRow = 158
    ActiveWindow.ScrollRow = 153
    ActiveWindow.ScrollRow = 151
    ActiveWindow.ScrollRow = 148
    ActiveWindow.ScrollRow = 142
    ActiveWindow.ScrollRow = 137
    ActiveWindow.ScrollRow = 132
    ActiveWindow.ScrollRow = 127
    ActiveWindow.ScrollRow = 121
    ActiveWindow.ScrollRow = 117
    ActiveWindow.ScrollRow = 111
    ActiveWindow.ScrollRow = 105
    ActiveWindow.ScrollRow = 100
    ActiveWindow.ScrollRow = 93
    ActiveWindow.ScrollRow = 89
    ActiveWindow.ScrollRow = 83
    ActiveWindow.ScrollRow = 78
    ActiveWindow.ScrollRow = 71
    ActiveWindow.ScrollRow = 67
    ActiveWindow.ScrollRow = 62
    ActiveWindow.ScrollRow = 58
    ActiveWindow.ScrollRow = 54
    ActiveWindow.ScrollRow = 51
    ActiveWindow.ScrollRow = 47
    ActiveWindow.ScrollRow = 44
    ActiveWindow.ScrollRow = 42
    ActiveWindow.ScrollRow = 39
    ActiveWindow.ScrollRow = 37
    ActiveWindow.ScrollRow = 34
    ActiveWindow.ScrollRow = 33
    ActiveWindow.ScrollRow = 29
    ActiveWindow.ScrollRow = 27
    ActiveWindow.ScrollRow = 25
    ActiveWindow.ScrollRow = 22
    ActiveWindow.ScrollRow = 19
    ActiveWindow.ScrollRow = 15
    ActiveWindow.ScrollRow = 12
    ActiveWindow.ScrollRow = 8
    ActiveWindow.ScrollRow = 6
    ActiveWindow.ScrollRow = 4
    ActiveWindow.ScrollRow = 3
    ActiveWindow.ScrollRow = 2
    ActiveWindow.ScrollRow = 1
    Range("G12").Select
    ActiveCell.FormulaR1C1 = _
        "=R[8]C[3]+R[41]C[3]+R[74]C[3]+R[107]C[3]+R[140]C[3]+R[173]C[3]+R[206]C[3]+R[239]C[3]+R[272]C[3]+R[305]C[3]"
    Range("G13").Select
    ActiveCell.FormulaR1C1 = _
        "=R[7]C[2]+R[40]C[2]+R[73]C[2]+R[106]C[2]+R[139]C[2]+R[172]C[2]+R[205]C[2]+R[238]C[2]+R[271]C[2]+R[304]C[2]"
    Range("G14").Select
    ActiveCell.FormulaR1C1 = _
        "=R[6]C[1]+R[39]C[1]+R[72]C[1]+R[105]C[1]+R[138]C[1]+R[171]C[1]+R[204]C[1]+R[237]C[1]+R[270]C[1]+R[303]C[1]"
    Range("G15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("H12").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H13").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H14").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("K12").Select
    ActiveCell.FormulaR1C1 = _
        "=R[8]C[-4]+R[41]C[-4]+R[74]C[-4]+R[107]C[-4]+R[140]C[-4]+R[173]C[-4]+R[206]C[-4]+R[239]C[-4]+R[272]C[-4]+R[305]C[-4]"
    Range("K13").Select
    ActiveCell.FormulaR1C1 = _
        "=R[7]C[-5]+R[40]C[-5]+R[73]C[-5]+R[106]C[-5]+R[139]C[-5]+R[172]C[-5]+R[205]C[-5]+R[238]C[-5]+R[271]C[-5]+R[304]C[-5]"
    Range("K14").Select
    ActiveCell.FormulaR1C1 = _
        "=R[6]C+R[39]C+R[72]C+R[105]C+R[138]C+R[171]C+R[204]C+R[237]C+R[270]C+R[303]C"
    Range("K15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("L12").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L13").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L14").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("L16").Select
    ActiveWindow.SmallScroll Down:=5
    Range("F20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K21").Select
    ActiveWindow.SmallScroll Down:=28
    Range("F53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K54").Select
    ActiveWindow.SmallScroll Down:=35
    Range("F86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K87").Select
    ActiveWindow.SmallScroll Down:=36
    Range("F119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K120").Select
    ActiveWindow.SmallScroll Down:=30
    Range("F152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K153").Select
    ActiveWindow.SmallScroll Down:=36
    Range("F185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K186").Select
    ActiveWindow.SmallScroll Down:=31
    Range("F218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K219").Select
    ActiveWindow.SmallScroll Down:=30
    Range("F251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K252").Select
    ActiveWindow.SmallScroll Down:=36
    Range("F284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K285").Select
    ActiveWindow.SmallScroll Down:=34
    Range("F317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],"""")"
    Range("I317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K318").Select
    ActiveWindow.SmallScroll Down:=31
    ActiveWindow.ScrollRow = 332
    ActiveWindow.ScrollRow = 331
    ActiveWindow.ScrollRow = 330
    ActiveWindow.ScrollRow = 327
    ActiveWindow.ScrollRow = 325
    ActiveWindow.ScrollRow = 324
    ActiveWindow.ScrollRow = 321
    ActiveWindow.ScrollRow = 320
    ActiveWindow.ScrollRow = 315
    ActiveWindow.ScrollRow = 313
    ActiveWindow.ScrollRow = 308
    ActiveWindow.ScrollRow = 299
    ActiveWindow.ScrollRow = 292
    ActiveWindow.ScrollRow = 282
    ActiveWindow.ScrollRow = 260
    ActiveWindow.ScrollRow = 238
    ActiveWindow.ScrollRow = 228
    ActiveWindow.ScrollRow = 196
    ActiveWindow.ScrollRow = 186
    ActiveWindow.ScrollRow = 166
    ActiveWindow.ScrollRow = 159
    ActiveWindow.ScrollRow = 135
    ActiveWindow.ScrollRow = 127
    ActiveWindow.ScrollRow = 115
    ActiveWindow.ScrollRow = 105
    ActiveWindow.ScrollRow = 97
    ActiveWindow.ScrollRow = 95
    ActiveWindow.ScrollRow = 91
    ActiveWindow.ScrollRow = 89
    ActiveWindow.ScrollRow = 87
    ActiveWindow.ScrollRow = 84
    ActiveWindow.ScrollRow = 83
    ActiveWindow.ScrollRow = 79
    ActiveWindow.ScrollRow = 78
    ActiveWindow.ScrollRow = 76
    ActiveWindow.ScrollRow = 75
    ActiveWindow.ScrollRow = 73
    ActiveWindow.ScrollRow = 71
    ActiveWindow.ScrollRow = 70
    ActiveWindow.ScrollRow = 68
    ActiveWindow.ScrollRow = 66
    ActiveWindow.ScrollRow = 65
    ActiveWindow.ScrollRow = 63
    ActiveWindow.ScrollRow = 61
    ActiveWindow.ScrollRow = 59
    ActiveWindow.ScrollRow = 58
    ActiveWindow.ScrollRow = 56
    ActiveWindow.ScrollRow = 55
    ActiveWindow.ScrollRow = 54
    ActiveWindow.ScrollRow = 53
    ActiveWindow.ScrollRow = 52
    ActiveWindow.ScrollRow = 50
    ActiveWindow.ScrollRow = 48
    ActiveWindow.ScrollRow = 46
    ActiveWindow.ScrollRow = 45
    ActiveWindow.ScrollRow = 43
    ActiveWindow.ScrollRow = 41
    ActiveWindow.ScrollRow = 38
    ActiveWindow.ScrollRow = 35
    ActiveWindow.ScrollRow = 33
    ActiveWindow.ScrollRow = 31
    ActiveWindow.ScrollRow = 28
    ActiveWindow.ScrollRow = 26
    ActiveWindow.ScrollRow = 23
    ActiveWindow.ScrollRow = 21
    ActiveWindow.ScrollRow = 20
    ActiveWindow.ScrollRow = 18
    ActiveWindow.ScrollRow = 17
    ActiveWindow.ScrollRow = 15
    ActiveWindow.ScrollRow = 14
    ActiveWindow.ScrollRow = 13
    ActiveWindow.ScrollRow = 12
    ActiveWindow.ScrollRow = 11
    ActiveWindow.ScrollRow = 10
    ActiveWindow.ScrollRow = 9
    ActiveWindow.ScrollRow = 8
    ActiveWindow.ScrollRow = 7
    ActiveWindow.ScrollRow = 6
    ActiveWindow.ScrollRow = 5
    ActiveWindow.ScrollRow = 4
    ActiveWindow.ScrollRow = 2
    ActiveWindow.ScrollRow = 1
    Range("A1").Select
    Sheets("00.Info").Select
End Sub
Sub Sub_OAW_3_1()
'
' Sub_OAW_3_1 Macro
'

'
    Sheets("RESULTADOS").Select
    ActiveWindow.SmallScroll Down:=7
    Range("C20").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C21").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C50").Select
    ActiveCell.FormulaR1C1 = "SITIO WEB"
    Range("B20").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B21").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("D20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[-1]C),,P1.Perceptible!R[-1]C)"
    Range("D50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("E20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("E49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[32]C[-1]),,P1.Perceptible!R[32]C[-1])"
    Range("F20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-2]),,P1.Perceptible!R[98]C[-2])"
    Range("F50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("G20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[98]C[-3]),,P1.Perceptible!R[98]C[-3])"
    Range("G50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("G51").Select
    ActiveCell.FormulaR1C1 = ""
    Range("H20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[197]C[-4]),,P1.Perceptible!R[197]C[-4])"
    Range("H50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("I20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[230]C[-5]),,P1.Perceptible!R[230]C[-5])"
    Range("I50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("J20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[263]C[-6]),,P1.Perceptible!R[263]C[-6])"
    Range("J50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("K20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[362]C[-7]),,P1.Perceptible!R[362]C[-7])"
    Range("K50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("L20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P1.Perceptible!R[395]C[-8]),,P1.Perceptible!R[395]C[-8])"
    Range("L50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("M20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[-1]C[-9]),,P2.Operable!R[-1]C[-9])"
    Range("M50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("N20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[32]C[-10]),,P2.Operable!R[32]C[-10])"
    Range("N50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("O20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[65]C[-11]),,P2.Operable!R[65]C[-11])"
    Range("O50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("O51").Select
    ActiveCell.FormulaR1C1 = ""
    Range("P20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[98]C[-12]),,P2.Operable!R[98]C[-12])"
    Range("P50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("Q20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[131]C[-13]),,P2.Operable!R[131]C[-13])"
    Range("Q50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("R20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[164]C[-14]),,P2.Operable!R[164]C[-14])"
    Range("R50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("S20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[197]C[-15]),,P2.Operable!R[197]C[-15])"
    Range("S50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("T20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[230]C[-16]),,P2.Operable!R[230]C[-16])"
    Range("T50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("U20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[263]C[-17]),,P2.Operable!R[263]C[-17])"
    Range("U50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("V20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[296]C[-18]),,P2.Operable!R[296]C[-18])"
    Range("V50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("V51").Select
    ActiveCell.FormulaR1C1 = ""
    Range("V52").Select
    ActiveCell.FormulaR1C1 = ""
    Range("W20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[428]C[-19]),,P2.Operable!R[428]C[-19])"
    Range("W50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("X20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[461]C[-20]),,P2.Operable!R[461]C[-20])"
    Range("X50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("Y20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[494]C[-21]),,P2.Operable!R[494]C[-21])"
    Range("Y50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("Z20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P2.Operable!R[527]C[-22]),,P2.Operable!R[527]C[-22])"
    Range("Z50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("AE20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[230]C[-27]),,P3.Comprensible!R[230]C[-27])"
    Range("AE50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("AA20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[-1]C[-23]),,P3.Comprensible!R[-1]C[-23])"
    Range("AA50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("AB20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[65]C[-24]),,P3.Comprensible!R[65]C[-24])"
    Range("AB50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("AB51").Select
    ActiveCell.FormulaR1C1 = ""
    Range("AC20").Select
    Application.CommandBars("Help").Visible = False
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
End Sub
Sub Sub_OAW_3_2()
'
' Sub_OAW_3_2 Macro
'
    Sheets("RESULTADOS").Select
    Range("AC21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[98]C[-25]),,P3.Comprensible!R[98]C[-25])"
    Range("AC50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("AD20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P3.Comprensible!R[197]C[-26]),,P3.Comprensible!R[197]C[-26])"
    Range("AD50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("AF20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[-1]C[-28]),,P4.Robusto!R[-1]C[-28])"
    Range("AF50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("AG20").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG49").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[32]C[-29]),,P4.Robusto!R[32]C[-29])"
    Range("AG50").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("A51").Select
    ActiveWindow.SmallScroll Down:=19
    Range("B56").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B57").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B86").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B87").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C56").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C57").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C86").Select
    ActiveCell.FormulaR1C1 = "SITIO WEB"
    Range("D56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[95]C),,P1.Perceptible!R[95]C)"
    Range("D86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("D87").Select
    ActiveCell.FormulaR1C1 = ""
    Range("E56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[128]C[-1]),,P1.Perceptible!R[128]C[-1])"
    Range("E86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("F56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[260]C[-2]),,P1.Perceptible!R[260]C[-2])"
    Range("F86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("G56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[293]C[-3]),,P1.Perceptible!R[293]C[-3])"
    Range("G86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("H56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[392]C[-4]),,P1.Perceptible!R[392]C[-4])"
    Range("H86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("I56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[425]C[-5]),,P1.Perceptible!R[425]C[-5])"
    Range("I86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("J56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[458]C[-6]),,P1.Perceptible!R[458]C[-6])"
    Range("J86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("K56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("K85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[491]C[-7]),,P1.Perceptible!R[491]C[-7])"
    Range("L56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[524]C[-8]),,P1.Perceptible!R[524]C[-8])"
    Range("L86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("M56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[557]C[-9]),,P1.Perceptible!R[557]C[-9])"
    Range("M86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("N56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P1.Perceptible!R[590]C[-10]),,P1.Perceptible!R[590]C[-10])"
    Range("N86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("O56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[293]C[-11]),,P2.Operable!R[293]C[-11])"
    Range("O86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("P56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[326]C[-12]),,P2.Operable!R[326]C[-12])"
    Range("P86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("Q56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P2.Operable!R[359]C[-13]),,P2.Operable!R[359]C[-13])"
    Range("Q86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("R56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[-4]C[-14]),,P3.Comprensible!R[-4]C[-14])"
    Range("R86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("S56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[95]C[-15]),,P3.Comprensible!R[95]C[-15])"
    Range("S86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("T56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[128]C[-16]),,P3.Comprensible!R[128]C[-16])"
    Range("T86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("U56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[227]C[-17]),,P3.Comprensible!R[227]C[-17])"
    Range("U86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("V56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK(P3.Comprensible!R[260]C[-18]),,P3.Comprensible!R[260]C[-18])"
    Range("V86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("W56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W82").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W83").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W84").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W85").Select
    ActiveCell.FormulaR1C1 = _
        "=IF(ISBLANK(P4.Robusto!R[260]C[-19]),,P4.Robusto!R[260]C[-19])"
    Range("W86").Select
    ActiveCell.FormulaR1C1 = "N/A"
    Range("W87").Select
        Range("D8").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[42]C:R[42]C[29],""CONFORME"")"
    Range("D9").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[77]C:R[77]C[19],""CONFORME"")"
    Range("D10").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-2]C:R[-1]C)"
    Range("E8").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[42]C[-1]:R[42]C[28],""NO CONFORME"")"
    Range("E9").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[77]C[-1]:R[77]C[18],""NO CONFORME"")"
    Range("E10").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-2]C:R[-1]C)"
    Range("F8").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[42]C[-2]:R[42]C[27],""N/A"")"
    Range("F9").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[77]C[-2]:R[77]C[17],""N/A"")"
    Range("F10").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-2]C:R[-1]C)"
    Range("G8").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[42]C[-3]:R[42]C[26],""ERROR"")"
    Range("G9").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[77]C[-3]:R[77]C[16],""ERROR"")"
    Range("G10").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-2]C:R[-1]C)"
    Range("H8").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[42]C[-4]:R[42]C[25],""EN CURSO"")"
    Range("H9").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[77]C[-4]:R[77]C[15],""EN CURSO"")"
    Range("H10").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-2]C:R[-1]C)"
    Range("K8").Select
    ActiveCell.FormulaR1C1 = _
        "=COUNTIF( R20C4:INDIRECT(""$AG$"" &  SUM(20,'03.Muestra'!R40C4)),""Pasa"")+ COUNTIF(R56C4:INDIRECT(""$W$"" &  SUM(56,'03.Muestra'!R40C4)),""Pasa"")"
    Range("K9").Select
    ActiveCell.FormulaR1C1 = _
        "=COUNTIF( R20C4:INDIRECT(""$AG$"" &  SUM(20,'03.Muestra'!R40C4)),""Falla"")+ COUNTIF(R56C4:INDIRECT(""$W$"" &  SUM(56,'03.Muestra'!R40C4)),""Falla"")"
    Range("K10").Select
    ActiveCell.FormulaR1C1 = _
        "=COUNTIF( R20C4:INDIRECT(""$AG$"" &  SUM(20,'03.Muestra'!R40C4)),""N/A"")+ COUNTIF(R56C4:INDIRECT(""$W$"" &  SUM(56,'03.Muestra'!R40C4)),""N/A"")"
    Range("K11").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("L8").Select
    ActiveCell.FormulaR1C1 = "=IF((R11C11+R11C15)=0,0,RC[-1]/(R11C11+R11C15))"
    Range("L9").Select
    ActiveCell.FormulaR1C1 = "=IF((R11C11+R11C15)=0,0,RC[-1]/(R11C11+R11C15))"
    Range("L10").Select
    ActiveCell.FormulaR1C1 = "=IF((R11C11+R11C15)=0,0,RC[-1]/(R11C11+R11C15))"
    Range("L11").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("O8").Select
    ActiveCell.FormulaR1C1 = _
        "=COUNTIF( R20C4:INDIRECT(""$AG$"" &  SUM(20,'03.Muestra'!R40C4)),""N/D"")+ COUNTIF(R56C4:INDIRECT(""$W$"" &  SUM(56,'03.Muestra'!R40C4)),""N/D"")"
    Range("O9").Select
    ActiveCell.FormulaR1C1 = _
        "=COUNTIF( R20C4:INDIRECT(""$AG$"" &  SUM(20,'03.Muestra'!R40C4)),""N/T"")+ COUNTIF(R56C4:INDIRECT(""$W$"" &  SUM(56,'03.Muestra'!R40C4)),""N/T"")"
    Range("O10").Select
    ActiveCell.FormulaR1C1 = _
        "=IF('03.Muestra'!R40C4=0,0,SUM(COUNTBLANK(R20C4:INDIRECT(""$AG$"" &  SUM(20,'03.Muestra'!R40C4)-1)),COUNTBLANK(R56C4:INDIRECT(""$W$"" &  SUM(56,'03.Muestra'!R40C4)-1))))"
    Range("O11").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("P8").Select
    ActiveCell.FormulaR1C1 = "=IF((R11C11+R11C15)=0,0,RC[-1]/(R11C11+R11C15))"
    Range("P9").Select
    ActiveCell.FormulaR1C1 = "=IF((R11C11+R11C15)=0,0,RC[-1]/(R11C11+R11C15))"
    Range("P10").Select
    ActiveCell.FormulaR1C1 = "=IF((R11C11+R11C15)=0,0,RC[-1]/(R11C11+R11C15))"
    Range("P11").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("P12").Select
    Range("A1").Select
    Sheets("00.Info").Select
'
End Sub
Sub OAW()
'
' OAW Macro
'
Call Sub_OAW_0
Call Sub_OAW_1
Call Sub_OAW_2
Call Sub_OAW_3_1
Call Sub_OAW_3_2
Call Sub_OAW_4_1
Call Sub_OAW_4_2
Call Sub_OAW_4_3
Call Sub_OAW_4_4
Call Sub_OAW_5_1
Call Sub_OAW_5_2
Call Sub_OAW_5_3
Call Sub_OAW_5_4
'
End Sub
Sub Sub_OAW_4_1()
'
' Sub_OAW_4_1 Macro
'
    Sheets("P1.Perceptible").Select
    ActiveWindow.SmallScroll Down:=3
    ActiveCell.Offset(18, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "A"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(2, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "AA"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "AA"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "A"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(2, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(4, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select

    
'
End Sub
Sub Sub_OAW_4_2()
'
' Sub_OAW_4_2 Macro
'
    Sheets("P1.Perceptible").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(4, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "AA"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
'
End Sub
Sub Sub_OAW_4_3()
'
' Sub_OAW_4_3 Macro
'
        Sheets("P1.Perceptible").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=6
    ActiveWindow.ScrollRow = 662
    ActiveWindow.ScrollRow = 660
    ActiveWindow.ScrollRow = 659
    ActiveWindow.ScrollRow = 657
    ActiveWindow.ScrollRow = 656
    ActiveWindow.ScrollRow = 654
    ActiveWindow.ScrollRow = 651
    ActiveWindow.ScrollRow = 649
    ActiveWindow.ScrollRow = 646
    ActiveWindow.ScrollRow = 643
    ActiveWindow.ScrollRow = 642
    ActiveWindow.ScrollRow = 640
    ActiveWindow.ScrollRow = 635
    ActiveWindow.ScrollRow = 634
    ActiveWindow.ScrollRow = 632
    ActiveWindow.ScrollRow = 631
    ActiveWindow.ScrollRow = 627
    ActiveWindow.ScrollRow = 626
    ActiveWindow.ScrollRow = 624
    ActiveWindow.ScrollRow = 621
    ActiveWindow.ScrollRow = 620
    ActiveWindow.ScrollRow = 616
    ActiveWindow.ScrollRow = 615
    ActiveWindow.ScrollRow = 610
    ActiveWindow.ScrollRow = 609
    ActiveWindow.ScrollRow = 606
    ActiveWindow.ScrollRow = 604
    ActiveWindow.ScrollRow = 602
    ActiveWindow.ScrollRow = 599
    ActiveWindow.ScrollRow = 596
    ActiveWindow.ScrollRow = 595
    ActiveWindow.ScrollRow = 593
    ActiveWindow.ScrollRow = 591
    ActiveWindow.ScrollRow = 590
    ActiveWindow.ScrollRow = 588
    ActiveWindow.ScrollRow = 587
    ActiveWindow.ScrollRow = 582
    ActiveWindow.ScrollRow = 579
    ActiveWindow.ScrollRow = 574
    ActiveWindow.ScrollRow = 571
    ActiveWindow.ScrollRow = 566
    ActiveWindow.ScrollRow = 562
    ActiveWindow.ScrollRow = 558
    ActiveWindow.ScrollRow = 552
    ActiveWindow.ScrollRow = 548
    ActiveWindow.ScrollRow = 540
    ActiveWindow.ScrollRow = 535
    ActiveWindow.ScrollRow = 529
    ActiveWindow.ScrollRow = 524
    ActiveWindow.ScrollRow = 518
    ActiveWindow.ScrollRow = 511
    ActiveWindow.ScrollRow = 504
    ActiveWindow.ScrollRow = 496
    ActiveWindow.ScrollRow = 491
    ActiveWindow.ScrollRow = 482
    ActiveWindow.ScrollRow = 469
    ActiveWindow.ScrollRow = 460
    ActiveWindow.ScrollRow = 447
    ActiveWindow.ScrollRow = 436
    ActiveWindow.ScrollRow = 430
    ActiveWindow.ScrollRow = 413
    ActiveWindow.ScrollRow = 395
    ActiveWindow.ScrollRow = 385
    ActiveWindow.ScrollRow = 372
    ActiveWindow.ScrollRow = 359
    ActiveWindow.ScrollRow = 345
    ActiveWindow.ScrollRow = 331
    ActiveWindow.ScrollRow = 325
    ActiveWindow.ScrollRow = 319
    ActiveWindow.ScrollRow = 308
    ActiveWindow.ScrollRow = 301
    ActiveWindow.ScrollRow = 279
    ActiveWindow.ScrollRow = 270
    ActiveWindow.ScrollRow = 251
    ActiveWindow.ScrollRow = 237
    ActiveWindow.ScrollRow = 229
    ActiveWindow.ScrollRow = 212
    ActiveWindow.ScrollRow = 206
    ActiveWindow.ScrollRow = 196
    ActiveWindow.ScrollRow = 181
    ActiveWindow.ScrollRow = 176
    ActiveWindow.ScrollRow = 170
    ActiveWindow.ScrollRow = 165
    ActiveWindow.ScrollRow = 162
    ActiveWindow.ScrollRow = 157
    ActiveWindow.ScrollRow = 154
    ActiveWindow.ScrollRow = 151
    ActiveWindow.ScrollRow = 146
    ActiveWindow.ScrollRow = 143
    ActiveWindow.ScrollRow = 140
    ActiveWindow.ScrollRow = 135
    ActiveWindow.ScrollRow = 132
    ActiveWindow.ScrollRow = 129
    ActiveWindow.ScrollRow = 127
    ActiveWindow.ScrollRow = 124
    ActiveWindow.ScrollRow = 121
    ActiveWindow.ScrollRow = 116
    ActiveWindow.ScrollRow = 115
    ActiveWindow.ScrollRow = 112
    ActiveWindow.ScrollRow = 109
    ActiveWindow.ScrollRow = 104
    ActiveWindow.ScrollRow = 101
    ActiveWindow.ScrollRow = 98
    ActiveWindow.ScrollRow = 91
    ActiveWindow.ScrollRow = 88
    ActiveWindow.ScrollRow = 84
    ActiveWindow.ScrollRow = 80
    ActiveWindow.ScrollRow = 76
    ActiveWindow.ScrollRow = 73
    ActiveWindow.ScrollRow = 68
    ActiveWindow.ScrollRow = 63
    ActiveWindow.ScrollRow = 60
    ActiveWindow.ScrollRow = 57
    ActiveWindow.ScrollRow = 54
    ActiveWindow.ScrollRow = 49
    ActiveWindow.ScrollRow = 46
    ActiveWindow.ScrollRow = 43
    ActiveWindow.ScrollRow = 40
    ActiveWindow.ScrollRow = 37
    ActiveWindow.ScrollRow = 35
    ActiveWindow.ScrollRow = 33
    ActiveWindow.ScrollRow = 30
    ActiveWindow.ScrollRow = 29
    ActiveWindow.ScrollRow = 26
    ActiveWindow.ScrollRow = 22
    ActiveWindow.ScrollRow = 19
    ActiveWindow.ScrollRow = 18
    ActiveWindow.ScrollRow = 16
    ActiveWindow.ScrollRow = 15
    ActiveWindow.ScrollRow = 13
    ActiveWindow.ScrollRow = 11
    ActiveWindow.ScrollRow = 10
    ActiveWindow.ScrollRow = 8
    ActiveWindow.ScrollRow = 7
    ActiveWindow.ScrollRow = 5
    ActiveWindow.ScrollRow = 4
    ActiveWindow.SmallScroll Down:=4
    ActiveCell.Offset(-659, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(4, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(4, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(4, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(2, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select

'
End Sub
Sub Sub_OAW_4_4()
'
' Sub_OAW_4_4 Macro
'
        Sheets("P1.Perceptible").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(2, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "1.4.5 Imágenes de texto"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(2, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = ""
    ActiveCell.Offset(3, 0).Range("A1").Select
    ActiveWindow.ScrollRow = 657
    ActiveWindow.ScrollRow = 655
    ActiveWindow.ScrollRow = 654
    ActiveWindow.ScrollRow = 652
    ActiveWindow.ScrollRow = 651
    ActiveWindow.ScrollRow = 649
    ActiveWindow.ScrollRow = 648
    ActiveWindow.ScrollRow = 646
    ActiveWindow.ScrollRow = 645
    ActiveWindow.ScrollRow = 643
    ActiveWindow.ScrollRow = 641
    ActiveWindow.ScrollRow = 640
    ActiveWindow.ScrollRow = 638
    ActiveWindow.ScrollRow = 637
    ActiveWindow.ScrollRow = 635
    ActiveWindow.ScrollRow = 634
    ActiveWindow.ScrollRow = 632
    ActiveWindow.ScrollRow = 631
    ActiveWindow.ScrollRow = 629
    ActiveWindow.ScrollRow = 627
    ActiveWindow.ScrollRow = 626
    ActiveWindow.ScrollRow = 624
    ActiveWindow.ScrollRow = 621
    ActiveWindow.ScrollRow = 620
    ActiveWindow.ScrollRow = 617
    ActiveWindow.ScrollRow = 612
    ActiveWindow.ScrollRow = 609
    ActiveWindow.ScrollRow = 603
    ActiveWindow.ScrollRow = 596
    ActiveWindow.ScrollRow = 585
    ActiveWindow.ScrollRow = 575
    ActiveWindow.ScrollRow = 547
    ActiveWindow.ScrollRow = 514
    ActiveWindow.ScrollRow = 475
    ActiveWindow.ScrollRow = 414
    ActiveWindow.ScrollRow = 371
    ActiveWindow.ScrollRow = 312
    ActiveWindow.ScrollRow = 274
    ActiveWindow.ScrollRow = 226
    ActiveWindow.ScrollRow = 218
    ActiveWindow.ScrollRow = 183
    ActiveWindow.ScrollRow = 176
    ActiveWindow.ScrollRow = 165
    ActiveWindow.ScrollRow = 156
    ActiveWindow.ScrollRow = 150
    ActiveWindow.ScrollRow = 147
    ActiveWindow.ScrollRow = 144
    ActiveWindow.ScrollRow = 141
    ActiveWindow.ScrollRow = 136
    ActiveWindow.ScrollRow = 133
    ActiveWindow.ScrollRow = 128
    ActiveWindow.ScrollRow = 127
    ActiveWindow.ScrollRow = 123
    ActiveWindow.ScrollRow = 120
    ActiveWindow.ScrollRow = 119
    ActiveWindow.ScrollRow = 116
    ActiveWindow.ScrollRow = 114
    ActiveWindow.ScrollRow = 113
    ActiveWindow.ScrollRow = 111
    ActiveWindow.ScrollRow = 109
    ActiveWindow.ScrollRow = 108
    ActiveWindow.ScrollRow = 106
    ActiveWindow.ScrollRow = 102
    ActiveWindow.ScrollRow = 99
    ActiveWindow.ScrollRow = 95
    ActiveWindow.ScrollRow = 92
    ActiveWindow.ScrollRow = 89
    ActiveWindow.ScrollRow = 86
    ActiveWindow.ScrollRow = 83
    ActiveWindow.ScrollRow = 78
    ActiveWindow.ScrollRow = 77
    ActiveWindow.ScrollRow = 74
    ActiveWindow.ScrollRow = 71
    ActiveWindow.ScrollRow = 69
    ActiveWindow.ScrollRow = 66
    ActiveWindow.ScrollRow = 63
    ActiveWindow.ScrollRow = 61
    ActiveWindow.ScrollRow = 60
    ActiveWindow.ScrollRow = 57
    ActiveWindow.ScrollRow = 55
    ActiveWindow.ScrollRow = 53
    ActiveWindow.ScrollRow = 50
    ActiveWindow.ScrollRow = 49
    ActiveWindow.ScrollRow = 47
    ActiveWindow.ScrollRow = 44
    ActiveWindow.ScrollRow = 41
    ActiveWindow.ScrollRow = 39
    ActiveWindow.ScrollRow = 38
    ActiveWindow.ScrollRow = 35
    ActiveWindow.ScrollRow = 30
    ActiveWindow.ScrollRow = 27
    ActiveWindow.ScrollRow = 24
    ActiveWindow.ScrollRow = 21
    ActiveWindow.ScrollRow = 18
    ActiveWindow.ScrollRow = 13
    ActiveWindow.ScrollRow = 11
    ActiveWindow.ScrollRow = 10
    ActiveWindow.ScrollRow = 8
    ActiveWindow.ScrollRow = 7
    ActiveWindow.ScrollRow = 5
    ActiveWindow.ScrollRow = 4
    ActiveWindow.ScrollRow = 2
    ActiveWindow.ScrollRow = 1
    ActiveWindow.SmallScroll Down:=1
    ActiveCell.Offset(-667, 4).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=R[8]C[3]+R[41]C[3]+R[74]C[3]+R[107]C[3]+R[140]C[3]+R[173]C[3]+R[206]C[3]+R[239]C[3]+R[272]C[3]+R[305]C[3]+R[338]C[3]+R[371]C[3]+R[404]C[3]+R[437]C[3]+R[470]C[3]+R[503]C[3]+R[536]C[3]"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=R[7]C[2]+R[40]C[2]+R[73]C[2]+R[106]C[2]+R[139]C[2]+R[172]C[2]+R[205]C[2]+R[238]C[2]+R[271]C[2]+R[304]C[2]+R[337]C[2]+R[370]C[2]+R[403]C[2]+R[436]C[2]+R[469]C[2]+R[502]C[2]+R[535]C[2]"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=R[6]C[1]+R[39]C[1]+R[72]C[1]+R[105]C[1]+R[138]C[1]+R[171]C[1]+R[204]C[1]+R[237]C[1]+R[270]C[1]+R[303]C[1]+R[336]C[1]+R[369]C[1]+R[402]C[1]+R[435]C[1]+R[468]C[1]+R[501]C[1]+R[534]C[1]"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    ActiveCell.Offset(-3, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    ActiveCell.Offset(-3, 3).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=R[8]C[-4]+R[41]C[-4]+R[74]C[-4]+R[107]C[-4]+R[140]C[-4]+R[173]C[-4]+R[206]C[-4]+R[239]C[-4]+R[272]C[-4]+R[305]C[-4]+R[338]C[-4]+R[371]C[-4]+R[404]C[-4]+R[437]C[-4]+R[470]C[-4]+R[503]C[-4]+R[536]C[-4]"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=R[7]C[-5]+R[40]C[-5]+R[73]C[-5]+R[106]C[-5]+R[139]C[-5]+R[172]C[-5]+R[205]C[-5]+R[238]C[-5]+R[271]C[-5]+R[304]C[-5]+R[337]C[-5]+R[370]C[-5]+R[403]C[-5]+R[436]C[-5]+R[469]C[-5]+R[502]C[-5]+R[535]C[-5]"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = _
        "=R[6]C+R[39]C+R[72]C+R[105]C+R[138]C+R[171]C+R[204]C+R[237]C+R[270]C+R[303]C+R[336]C+R[369]C+R[402]C+R[435]C+R[468]C+R[501]C+R[534]C"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    ActiveCell.Offset(-3, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=8
    ActiveCell.Offset(4, -6).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=27
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=35
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=32
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=36
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=30
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=33
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=36
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=30
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=34
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=34
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveWindow.SmallScroll Down:=2
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=34
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=33
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=34
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=34
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=33
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=29
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=33
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=34
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=34
    ActiveCell.Offset(32, -5).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],"""")"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    ActiveCell.Offset(0, 1).Range("A1").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    ActiveCell.Offset(1, 0).Range("A1").Select
    ActiveWindow.SmallScroll Down:=3
    ActiveWindow.ScrollRow = 639
    ActiveWindow.ScrollRow = 637
    ActiveWindow.ScrollRow = 636
    ActiveWindow.ScrollRow = 633
    ActiveWindow.ScrollRow = 630
    ActiveWindow.ScrollRow = 625
    ActiveWindow.ScrollRow = 619
    ActiveWindow.ScrollRow = 613
    ActiveWindow.ScrollRow = 600
    ActiveWindow.ScrollRow = 571
    ActiveWindow.ScrollRow = 551
    ActiveWindow.ScrollRow = 461
    ActiveWindow.ScrollRow = 438
    ActiveWindow.ScrollRow = 313
    ActiveWindow.ScrollRow = 291
    ActiveWindow.ScrollRow = 229
    ActiveWindow.ScrollRow = 217
    ActiveWindow.ScrollRow = 183
    ActiveWindow.ScrollRow = 174
    ActiveWindow.ScrollRow = 157
    ActiveWindow.ScrollRow = 154
    ActiveWindow.ScrollRow = 143
    ActiveWindow.ScrollRow = 137
    ActiveWindow.ScrollRow = 133
    ActiveWindow.ScrollRow = 130
    ActiveWindow.ScrollRow = 127
    ActiveWindow.ScrollRow = 123
    ActiveWindow.ScrollRow = 118
    ActiveWindow.ScrollRow = 113
    ActiveWindow.ScrollRow = 110
    ActiveWindow.ScrollRow = 104
    ActiveWindow.ScrollRow = 101
    ActiveWindow.ScrollRow = 95
    ActiveWindow.ScrollRow = 90
    ActiveWindow.ScrollRow = 82
    ActiveWindow.ScrollRow = 75
    ActiveWindow.ScrollRow = 69
    ActiveWindow.ScrollRow = 62
    ActiveWindow.ScrollRow = 56
    ActiveWindow.ScrollRow = 47
    ActiveWindow.ScrollRow = 35
    ActiveWindow.ScrollRow = 22
    ActiveWindow.ScrollRow = 14
    ActiveWindow.ScrollRow = 13
    ActiveWindow.ScrollRow = 10
    ActiveWindow.ScrollRow = 8
    ActiveWindow.ScrollRow = 7
    ActiveWindow.ScrollRow = 5
    ActiveWindow.ScrollRow = 4
    ActiveWindow.ScrollRow = 2
    ActiveWindow.ScrollRow = 1
    Range("A1").Select
    Sheets("00.Info").Select
'
End Sub
Sub Sub_OAW_5_1()
'
' Sub_OAW_5_1 Macro
'
    Sheets("P2.Operable").Select
    ActiveWindow.SmallScroll Down:=15
    Range("B19").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B20").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B45").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B49").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B50").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B51").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B52").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B53").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B54").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B55").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B82").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B83").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B84").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B85").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B86").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B87").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B88").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B89").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B90").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B91").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B92").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B93").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B94").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B95").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B96").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B97").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B98").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B99").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B100").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B101").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B102").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B103").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B104").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B105").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B106").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B107").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B108").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B109").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B110").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B111").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B112").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B113").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B114").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B115").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B116").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B117").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B118").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B119").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B120").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B121").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B122").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B123").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B124").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B125").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B126").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B127").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B128").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B129").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B130").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B131").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B132").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B133").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B134").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B135").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B136").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B137").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B138").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B139").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B140").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B141").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B142").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B143").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B144").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B145").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B146").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B147").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B148").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B149").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B150").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B151").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B152").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B153").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B154").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B155").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B156").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B157").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B158").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B159").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B160").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B161").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B162").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B163").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B164").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B165").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B166").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B167").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B168").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B169").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B170").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B171").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B172").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B173").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B174").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B175").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B176").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B177").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B178").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B179").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B180").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B181").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B182").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B183").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B184").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B185").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B186").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B187").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B188").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B189").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B190").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B191").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B192").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B193").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B194").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B195").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B196").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B197").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B198").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B199").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B200").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B201").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B202").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B203").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B204").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B205").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B206").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B207").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B208").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B209").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B210").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B211").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B212").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B213").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B214").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B215").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B216").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B217").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B218").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B219").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B220").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B221").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B222").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B223").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B224").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B225").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B226").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B227").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B228").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B229").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B230").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B231").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B232").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B233").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B234").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B235").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B236").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B237").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B238").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B239").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B240").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B241").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B242").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B243").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B244").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B245").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B246").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B247").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B250").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B251").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B252").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B253").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B254").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B255").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B256").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B257").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B258").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B259").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B260").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B261").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B262").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B263").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B264").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B265").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B266").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B267").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B268").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B269").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B270").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B271").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B272").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B273").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B274").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B275").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B276").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B277").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B278").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B279").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B280").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B283").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B284").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B285").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B286").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B287").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B288").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B289").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B290").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B291").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B292").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B293").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B294").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B295").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B296").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B297").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B298").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B299").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B300").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B301").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B302").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B303").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B304").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B305").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B306").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B307").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B308").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B309").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B310").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"

'
End Sub
Sub Sub_OAW_5_2()
'
' Sub_OAW_5_2 Macro
'
    Range("B311").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B312").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B313").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B316").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B317").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B318").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B319").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B320").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B321").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B322").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B323").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B324").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B325").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B326").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B327").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B328").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B329").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B330").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B331").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B332").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B333").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B334").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B335").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B336").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B337").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B338").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B339").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B340").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B341").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B342").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B343").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B344").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B345").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B346").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B349").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B350").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B351").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B352").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B353").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B354").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B355").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B356").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B357").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B358").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B359").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B360").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B361").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B362").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B363").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B364").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B365").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B366").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B367").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B368").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B369").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B370").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B371").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B372").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B373").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B374").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B375").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B376").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B377").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B378").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B382").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B383").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B384").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B385").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B386").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B387").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B388").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B389").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B390").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B391").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B392").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B393").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B394").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B395").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B396").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B397").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B398").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B399").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B400").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B401").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B402").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B403").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B404").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B405").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B406").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B407").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B408").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B409").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B410").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B411").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B412").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B415").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B416").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B417").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B418").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B419").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B420").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B421").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B422").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B423").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B424").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B425").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B426").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B427").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B428").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B429").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B430").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B431").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B432").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B433").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B434").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B435").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B436").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B437").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B438").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B439").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B440").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B441").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B442").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B443").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B444").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B445").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B446").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B447").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B449").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B448").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B449").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B450").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B451").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B452").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B453").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B454").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B455").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B456").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B457").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B458").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B459").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B460").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B461").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B462").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B463").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B464").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B465").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B466").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B467").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B468").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B469").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B470").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B471").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B472").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B473").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B474").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B475").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B476").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B477").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B478").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B479").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B480").Select
    ActiveCell.FormulaR1C1 = "A"
    Range("B481").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B482").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B483").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B484").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B485").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B486").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B487").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B488").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B489").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B490").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B491").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B492").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B493").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B494").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B495").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B496").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B497").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B498").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B499").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B500").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B501").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B502").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B503").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B504").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B505").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B506").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B507").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B508").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B509").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B510").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B511").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B514").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B515").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B516").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B517").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B518").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B519").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B520").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B521").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B522").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B523").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B524").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B525").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B526").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B527").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B528").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B529").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B530").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B531").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B532").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B533").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B534").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B535").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B536").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B537").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B538").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B539").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B540").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B541").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B542").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B543").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B544").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B547").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C3),,'03.Muestra'!R8C3)"
    Range("B548").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C3),,'03.Muestra'!R9C3)"
    Range("B549").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C3),,'03.Muestra'!R10C3)"
    Range("B550").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C3),,'03.Muestra'!R11C3)"
    Range("B551").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C3),,'03.Muestra'!R12C3)"
    Range("B552").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C3),,'03.Muestra'!R13C3)"
    Range("B553").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C3),,'03.Muestra'!R14C3)"
    Range("B554").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C3),,'03.Muestra'!R15C3)"
    Range("B555").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C3),,'03.Muestra'!R16C3)"
    Range("B556").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C3),,'03.Muestra'!R17C3)"
    Range("B557").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C3),,'03.Muestra'!R18C3)"
    Range("B558").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C3),,'03.Muestra'!R19C3)"
    Range("B559").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C3),,'03.Muestra'!R20C3)"
    Range("B560").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C3),,'03.Muestra'!R21C3)"
    Range("B561").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C3),,'03.Muestra'!R22C3)"
    Range("B562").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C3),,'03.Muestra'!R23C3)"
    Range("B563").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C3),,'03.Muestra'!R24C3)"
    Range("B564").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C3),,'03.Muestra'!R25C3)"
    Range("B565").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C3),,'03.Muestra'!R26C3)"
    Range("B566").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C3),,'03.Muestra'!R27C3)"
    Range("B567").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C3),,'03.Muestra'!R28C3)"
    Range("B568").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C3),,'03.Muestra'!R29C3)"
    Range("B569").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C3),,'03.Muestra'!R30C3)"
    Range("B570").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C3),,'03.Muestra'!R31C3)"
    Range("B571").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C3),,'03.Muestra'!R32C3)"
    Range("B572").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C3),,'03.Muestra'!R33C3)"
    Range("B573").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C3),,'03.Muestra'!R34C3)"
    Range("B574").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C3),,'03.Muestra'!R35C3)"
    Range("B575").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C3),,'03.Muestra'!R36C3)"
    Range("B576").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C3),,'03.Muestra'!R37C3)"
    Range("B577").Select
    ActiveCell.FormulaR1C1 = ""
    Range("B580").Select
    ActiveWindow.ScrollRow = 557
    ActiveWindow.ScrollRow = 554
    ActiveWindow.ScrollRow = 552
    ActiveWindow.ScrollRow = 548
    ActiveWindow.ScrollRow = 536
    ActiveWindow.ScrollRow = 530
    ActiveWindow.ScrollRow = 519
    ActiveWindow.ScrollRow = 493
    ActiveWindow.ScrollRow = 483
    ActiveWindow.ScrollRow = 452
    ActiveWindow.ScrollRow = 433
    ActiveWindow.ScrollRow = 384
    ActiveWindow.ScrollRow = 368
    ActiveWindow.ScrollRow = 334
    ActiveWindow.ScrollRow = 315
    ActiveWindow.ScrollRow = 271
    ActiveWindow.ScrollRow = 260
    ActiveWindow.ScrollRow = 220
    ActiveWindow.ScrollRow = 211
    ActiveWindow.ScrollRow = 187
    ActiveWindow.ScrollRow = 174
    ActiveWindow.ScrollRow = 163
    ActiveWindow.ScrollRow = 146
    ActiveWindow.ScrollRow = 138
    ActiveWindow.ScrollRow = 131
    ActiveWindow.ScrollRow = 122
    ActiveWindow.ScrollRow = 118
    ActiveWindow.ScrollRow = 113
    ActiveWindow.ScrollRow = 110
    ActiveWindow.ScrollRow = 108
    ActiveWindow.ScrollRow = 104
    ActiveWindow.ScrollRow = 101
    ActiveWindow.ScrollRow = 98
    ActiveWindow.ScrollRow = 97
    ActiveWindow.ScrollRow = 96
    ActiveWindow.ScrollRow = 94
    ActiveWindow.ScrollRow = 93
    ActiveWindow.ScrollRow = 92
    ActiveWindow.ScrollRow = 90
    ActiveWindow.ScrollRow = 89
    ActiveWindow.ScrollRow = 86
    ActiveWindow.ScrollRow = 84
    ActiveWindow.ScrollRow = 81
    ActiveWindow.ScrollRow = 78
    ActiveWindow.ScrollRow = 77
    ActiveWindow.ScrollRow = 75
    ActiveWindow.ScrollRow = 73
    ActiveWindow.ScrollRow = 71
    ActiveWindow.ScrollRow = 69
    ActiveWindow.ScrollRow = 68
    ActiveWindow.ScrollRow = 64
    ActiveWindow.ScrollRow = 63
    ActiveWindow.ScrollRow = 60
    ActiveWindow.ScrollRow = 57
    ActiveWindow.ScrollRow = 55
    ActiveWindow.ScrollRow = 52
    ActiveWindow.ScrollRow = 51
    ActiveWindow.ScrollRow = 48
    ActiveWindow.ScrollRow = 45
    ActiveWindow.ScrollRow = 41
    ActiveWindow.ScrollRow = 39
    ActiveWindow.ScrollRow = 35
    ActiveWindow.ScrollRow = 32
    ActiveWindow.ScrollRow = 30
    ActiveWindow.ScrollRow = 28
    ActiveWindow.ScrollRow = 26
    ActiveWindow.ScrollRow = 23
    ActiveWindow.ScrollRow = 20
    ActiveWindow.ScrollRow = 18
    ActiveWindow.ScrollRow = 16
    ActiveWindow.ScrollRow = 12
    ActiveWindow.ScrollRow = 11
    ActiveWindow.ScrollRow = 10
    ActiveWindow.ScrollRow = 7
    ActiveWindow.ScrollRow = 6
    ActiveWindow.ScrollRow = 3
    ActiveWindow.ScrollRow = 1
    ActiveWindow.SmallScroll Down:=12
    Range("C19").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C20").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C21").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C22").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C23").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C24").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C25").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C26").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C27").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C28").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C29").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C30").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C31").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C32").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C33").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C34").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C35").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C36").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C37").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C38").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C39").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C40").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C41").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C42").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C43").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C44").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C45").Select
        Sheets("P2.Operable").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C46").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C47").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C48").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C49").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C50").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C52").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C53").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C54").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C55").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C56").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"

'
End Sub
Sub Sub_OAW_5_3()
'
' Sub_OAW_5_3 Macro
'
    Range("C57").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C58").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C59").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C60").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C61").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C62").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C63").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C64").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C65").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C66").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C67").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C68").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C69").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C70").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C71").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C72").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C73").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C74").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C75").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C76").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C77").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C78").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C79").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C80").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C81").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C82").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C83").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C85").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C86").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C87").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C88").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C89").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C90").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C91").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C92").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C93").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C94").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C95").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C96").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C97").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C98").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C99").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C100").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C101").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C102").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C103").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C104").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C105").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C106").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C107").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C108").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C109").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C110").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C111").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C112").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C113").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C114").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C115").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C116").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C118").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C119").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C120").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C121").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C122").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C123").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C124").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C125").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C126").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C127").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C128").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C129").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C130").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C131").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C132").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C133").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C134").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C135").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C136").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C137").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C138").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C139").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C140").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C141").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C142").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C143").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C144").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C145").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C146").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C147").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C148").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C149").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C151").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C152").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C153").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C154").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C155").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C156").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C157").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C158").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C159").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C160").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C161").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C162").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C163").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C164").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C165").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C166").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C167").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C168").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C169").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C170").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C171").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C172").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C173").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C174").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C175").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C176").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C177").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C178").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C179").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C180").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C181").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C184").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C185").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C186").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C187").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C188").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C189").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C190").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C191").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C192").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C193").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C194").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C195").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C196").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C197").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C198").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C199").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C200").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C201").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C202").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C203").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C204").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C205").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C206").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C207").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C208").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C209").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C210").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C211").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C212").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C213").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C214").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C217").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C218").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C219").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C220").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C221").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C222").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C223").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C224").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C225").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C226").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C227").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C228").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C229").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C230").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C231").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C232").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C233").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C234").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C235").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C236").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C237").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C238").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C239").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C240").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C241").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C242").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C243").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C244").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C245").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C246").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C247").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C250").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C251").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C252").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C253").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C254").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C255").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C256").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C257").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C258").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C259").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C260").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C261").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C262").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C263").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C264").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C265").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C266").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C267").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C268").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C269").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C270").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C271").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C272").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C273").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C274").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C275").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C276").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C277").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C278").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C279").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C283").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C284").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C285").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C286").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C287").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C288").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C289").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C290").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C291").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C292").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C293").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C294").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C295").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C296").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C297").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C298").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C299").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C300").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C301").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C302").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C303").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C304").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C305").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C306").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C307").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C308").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C309").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C310").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C311").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C312").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C313").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C314").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C316").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C317").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C318").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C319").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C320").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C321").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C322").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C323").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C324").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C325").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C326").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C327").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C328").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C329").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C330").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C331").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C332").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C333").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C334").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C335").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C336").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C337").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C338").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C339").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C340").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C341").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C342").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C343").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C344").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C345").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C346").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C349").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C350").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C351").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C352").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C353").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C354").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C355").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C356").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C357").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C358").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C359").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C360").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C361").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C362").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C363").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C364").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C365").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C366").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C367").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C368").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C369").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C370").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C371").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C372").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C373").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C374").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C375").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C376").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C377").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C378").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C379").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C382").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C383").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C384").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C385").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C386").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C387").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C388").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C389").Select
        Sheets("P2.Operable").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C390").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C391").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C392").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C393").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C394").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C395").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C396").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C397").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C398").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
'
End Sub
Sub Sub_OAW_5_4()
'
' Sub_OAW_5_4 Macro
'
        Sheets("P2.Operable").Select
    Range("C399").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C400").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C401").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C402").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C403").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C404").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C405").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C406").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C407").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C408").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C409").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C410").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C411").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C415").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C416").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C417").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C418").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C419").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C420").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C421").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C422").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C423").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C424").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C425").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C426").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C427").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C428").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C429").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C430").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C431").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C432").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C433").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C434").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C435").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C436").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C437").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C438").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C439").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C440").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C441").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C442").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C443").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C444").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C448").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C449").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C450").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C451").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C452").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C453").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C454").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C455").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C456").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C457").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C458").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C459").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C460").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C461").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C462").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C463").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C464").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C465").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C466").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C467").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C468").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C469").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C470").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C471").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C472").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C473").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C474").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C475").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C476").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C477").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C478").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C481").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C482").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C483").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C484").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C485").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C486").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C487").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C488").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C489").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C490").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C491").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C492").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C493").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C494").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C495").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C496").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C497").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C498").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C499").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C500").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C501").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C502").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C503").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C504").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C505").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C506").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C507").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C508").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C509").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C510").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C511").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C512").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C513").Select
    ActiveCell.FormulaR1C1 = "2.5.3 Etiqueta en el nombre"
    Range("C514").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C515").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C516").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C517").Select
    ActiveWindow.SmallScroll Down:=9
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C518").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C519").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C520").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C521").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C522").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C523").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C524").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C525").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C526").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C527").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C528").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C529").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C530").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C531").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C532").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C533").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C534").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C535").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C536").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C537").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C538").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C539").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C540").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C541").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C542").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C543").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C544").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C545").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C547").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R8C5),,'03.Muestra'!R8C5)"
    Range("C548").Select
    ActiveCell.FormulaR1C1 = "=IF( ISBLANK('03.Muestra'!R9C5),,'03.Muestra'!R9C5)"
    Range("C549").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R10C5),,'03.Muestra'!R10C5)"
    Range("C550").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R11C5),,'03.Muestra'!R11C5)"
    Range("C551").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R12C5),,'03.Muestra'!R12C5)"
    Range("C552").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R13C5),,'03.Muestra'!R13C5)"
    Range("C553").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R14C5),,'03.Muestra'!R14C5)"
    Range("C554").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R15C5),,'03.Muestra'!R15C5)"
    Range("C555").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R16C5),,'03.Muestra'!R16C5)"
    Range("C556").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R17C5),,'03.Muestra'!R17C5)"
    Range("C557").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R18C5),,'03.Muestra'!R18C5)"
    Range("C558").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R19C5),,'03.Muestra'!R19C5)"
    Range("C559").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R20C5),,'03.Muestra'!R20C5)"
    Range("C560").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R21C5),,'03.Muestra'!R21C5)"
    Range("C561").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R22C5),,'03.Muestra'!R22C5)"
    Range("C562").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R23C5),,'03.Muestra'!R23C5)"
    Range("C563").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R24C5),,'03.Muestra'!R24C5)"
    Range("C564").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R25C5),,'03.Muestra'!R25C5)"
    Range("C565").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R26C5),,'03.Muestra'!R26C5)"
    Range("C566").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R27C5),,'03.Muestra'!R27C5)"
    Range("C567").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R28C5),,'03.Muestra'!R28C5)"
    Range("C568").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R29C5),,'03.Muestra'!R29C5)"
    Range("C569").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R30C5),,'03.Muestra'!R30C5)"
    Range("C570").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R31C5),,'03.Muestra'!R31C5)"
    Range("C571").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R32C5),,'03.Muestra'!R32C5)"
    Range("C572").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R33C5),,'03.Muestra'!R33C5)"
    Range("C573").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R34C5),,'03.Muestra'!R34C5)"
    Range("C574").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R35C5),,'03.Muestra'!R35C5)"
    Range("C575").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R36C5),,'03.Muestra'!R36C5)"
    Range("C576").Select
    ActiveCell.FormulaR1C1 = _
        "=IF( ISBLANK('03.Muestra'!R37C5),,'03.Muestra'!R37C5)"
    Range("C577").Select
    ActiveCell.FormulaR1C1 = ""
    Range("C578").Select
    ActiveWindow.ScrollRow = 555
    ActiveWindow.ScrollRow = 552
    ActiveWindow.ScrollRow = 540
    ActiveWindow.ScrollRow = 486
    ActiveWindow.ScrollRow = 438
    ActiveWindow.ScrollRow = 411
    ActiveWindow.ScrollRow = 344
    ActiveWindow.ScrollRow = 277
    ActiveWindow.ScrollRow = 176
    ActiveWindow.ScrollRow = 151
    ActiveWindow.ScrollRow = 89
    ActiveWindow.ScrollRow = 77
    ActiveWindow.ScrollRow = 53
    ActiveWindow.ScrollRow = 44
    ActiveWindow.ScrollRow = 26
    ActiveWindow.ScrollRow = 20
    ActiveWindow.ScrollRow = 12
    ActiveWindow.ScrollRow = 3
    ActiveWindow.ScrollRow = 1
    ActiveWindow.SmallScroll Down:=3
    Range("G12").Select
    ActiveCell.FormulaR1C1 = _
        "=R[8]C[3]+R[41]C[3]+R[74]C[3]+R[107]C[3]+R[140]C[3]+R[173]C[3]+R[206]C[3]+R[239]C[3]+R[272]C[3]+R[305]C[3]+R[338]C[3]+R[371]C[3]+R[404]C[3]+R[437]C[3]+R[470]C[3]+R[503]C[3]+R[536]C[3]+R[569]C[3]+R[602]C[3]+R[636]C[3]"
    Range("G13").Select
    ActiveCell.FormulaR1C1 = _
        "=R[7]C[2]+R[40]C[2]+R[73]C[2]+R[106]C[2]+R[139]C[2]+R[172]C[2]+R[205]C[2]+R[238]C[2]+R[271]C[2]+R[304]C[2]+R[337]C[2]+R[370]C[2]+R[403]C[2]+R[436]C[2]+R[469]C[2]+R[502]C[2]+R[535]C[2]+R[568]C[2]+R[601]C[2]+R[635]C[2]"
    Range("G14").Select
    ActiveCell.FormulaR1C1 = _
        "=R[6]C[1]+R[39]C[1]+R[72]C[1]+R[105]C[1]+R[138]C[1]+R[171]C[1]+R[204]C[1]+R[237]C[1]+R[270]C[1]+R[303]C[1]+R[336]C[1]+R[369]C[1]+R[402]C[1]+R[435]C[1]+R[468]C[1]+R[501]C[1]+R[534]C[1]+R[567]C[1]+R[600]C[1]+R[634]C[1]"
    Range("G15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("H12").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H13").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H14").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("H15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("K12").Select
    ActiveCell.FormulaR1C1 = _
        "=R[8]C[-4]+R[41]C[-4]+R[74]C[-4]+R[107]C[-4]+R[140]C[-4]+R[173]C[-4]+R[206]C[-4]+R[239]C[-4]+R[272]C[-4]+R[305]C[-4]+R[338]C[-4]+R[371]C[-4]+R[404]C[-4]+R[437]C[-4]+R[470]C[-4]+R[503]C[-4]+R[536]C[-4]+R[569]C[-4]+R[602]C[-4]+R[636]C[-4]"
    Range("K13").Select
    ActiveCell.FormulaR1C1 = _
        "=R[7]C[-5]+R[40]C[-5]+R[73]C[-5]+R[106]C[-5]+R[139]C[-5]+R[172]C[-5]+R[205]C[-5]+R[238]C[-5]+R[271]C[-5]+R[304]C[-5]+R[337]C[-5]+R[370]C[-5]+R[403]C[-5]+R[436]C[-5]+R[469]C[-5]+R[502]C[-5]+R[535]C[-5]+R[568]C[-5]+R[601]C[-5]+R[635]C[-5]"
    Range("K14").Select
    ActiveCell.FormulaR1C1 = _
        "=R[6]C+R[39]C+R[72]C+R[105]C+R[138]C+R[171]C+R[204]C+R[237]C+R[270]C+R[303]C+R[336]C+R[369]C+R[402]C+R[435]C+R[468]C+R[501]C+R[534]C+R[567]C+R[600]C+R[634]C"
    Range("K15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("L12").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L13").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L14").Select
    ActiveCell.FormulaR1C1 = "=IF((R15C7+R15C11)=0,0,RC[-1]/(R15C7+R15C11))"
    Range("L15").Select
    ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)"
    Range("F20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K20").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K21").Select
    ActiveWindow.SmallScroll Down:=33
    Range("F53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K53").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K54").Select
    ActiveWindow.SmallScroll Down:=33
    Range("F86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K86").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K87").Select
    ActiveWindow.SmallScroll Down:=39
    Range("F119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K119").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K120").Select
    ActiveWindow.SmallScroll Down:=36
    Range("F152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K152").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K153").Select
    ActiveWindow.SmallScroll Down:=36
    Range("F185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K185").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K186").Select
    ActiveWindow.SmallScroll Down:=21
    Range("F218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K218").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K219").Select
    ActiveWindow.SmallScroll Down:=45
    Range("F251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K251").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K252").Select
    ActiveWindow.SmallScroll Down:=33
    Range("F284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K284").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K285").Select
    ActiveWindow.SmallScroll Down:=33
    Range("F317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K317").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K318").Select
    ActiveWindow.SmallScroll Down:=27
    Range("F350").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G350").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H350").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I350").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J350").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K350").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K351").Select
    ActiveWindow.SmallScroll Down:=27
    Range("F383").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G383").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H383").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I383").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J383").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K383").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K384").Select
    ActiveWindow.SmallScroll Down:=39
    Range("F416").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G416").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H416").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I416").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J416").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K416").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K417").Select
    ActiveWindow.SmallScroll Down:=30
    Range("F449").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G449").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H449").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I449").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J449").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K449").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K450").Select
    ActiveWindow.SmallScroll Down:=36
    Range("F482").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G482").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H482").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I482").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J482").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K482").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K483").Select
    ActiveWindow.SmallScroll Down:=33
    Range("F515").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G515").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H515").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],R[-1]C)"
    Range("I515").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J515").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K515").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K516").Select
    ActiveWindow.SmallScroll Down:=33
    Range("F548").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-2]:R[28]C[-2],R[-1]C)"
    Range("G548").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-3]:R[28]C[-3],R[-1]C)"
    Range("H548").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-4]:R[28]C[-4],"""")"
    Range("I548").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-5]:R[28]C[-5],R[-1]C)"
    Range("J548").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-6]:R[28]C[-6],R[-1]C)"
    Range("K548").Select
    ActiveCell.FormulaR1C1 = "=COUNTIF(R[-1]C[-7]:R[28]C[-7],"""")"
    Range("K549").Select
    ActiveWindow.SmallScroll Down:=21
    ActiveWindow.ScrollRow = 556
    ActiveWindow.ScrollRow = 552
    ActiveWindow.ScrollRow = 543
    ActiveWindow.ScrollRow = 516
    ActiveWindow.ScrollRow = 498
    ActiveWindow.ScrollRow = 481
    ActiveWindow.ScrollRow = 457
    ActiveWindow.ScrollRow = 442
    ActiveWindow.ScrollRow = 419
    ActiveWindow.ScrollRow = 394
    ActiveWindow.ScrollRow = 386
    ActiveWindow.ScrollRow = 373
    ActiveWindow.ScrollRow = 362
    ActiveWindow.ScrollRow = 345
    ActiveWindow.ScrollRow = 338
    ActiveWindow.ScrollRow = 333
    ActiveWindow.ScrollRow = 329
    ActiveWindow.ScrollRow = 324
    ActiveWindow.ScrollRow = 320
    ActiveWindow.ScrollRow = 313
    ActiveWindow.ScrollRow = 308
    ActiveWindow.ScrollRow = 304
    ActiveWindow.ScrollRow = 300
    ActiveWindow.ScrollRow = 295
    ActiveWindow.ScrollRow = 292
    ActiveWindow.ScrollRow = 289
    ActiveWindow.ScrollRow = 284
    ActiveWindow.ScrollRow = 281
    ActiveWindow.ScrollRow = 276
    ActiveWindow.ScrollRow = 274
    ActiveWindow.ScrollRow = 270
    ActiveWindow.ScrollRow = 267
    ActiveWindow.ScrollRow = 263
    ActiveWindow.ScrollRow = 259
    ActiveWindow.ScrollRow = 256
    ActiveWindow.ScrollRow = 251
    ActiveWindow.ScrollRow = 249
    ActiveWindow.ScrollRow = 245
    ActiveWindow.ScrollRow = 242
    ActiveWindow.ScrollRow = 238
    ActiveWindow.ScrollRow = 235
    ActiveWindow.ScrollRow = 231
    ActiveWindow.ScrollRow = 229
    ActiveWindow.ScrollRow = 226
    ActiveWindow.ScrollRow = 223
    ActiveWindow.ScrollRow = 221
    ActiveWindow.ScrollRow = 217
    ActiveWindow.ScrollRow = 214
    ActiveWindow.ScrollRow = 210
    ActiveWindow.ScrollRow = 208
    ActiveWindow.ScrollRow = 202
    ActiveWindow.ScrollRow = 201
    ActiveWindow.ScrollRow = 198
    ActiveWindow.ScrollRow = 190
    ActiveWindow.ScrollRow = 185
    ActiveWindow.ScrollRow = 180
    ActiveWindow.ScrollRow = 177
    ActiveWindow.ScrollRow = 172
    ActiveWindow.ScrollRow = 168
    ActiveWindow.ScrollRow = 163
    ActiveWindow.ScrollRow = 159
    ActiveWindow.ScrollRow = 154
    ActiveWindow.ScrollRow = 148
    ActiveWindow.ScrollRow = 142
    ActiveWindow.ScrollRow = 136
    ActiveWindow.ScrollRow = 130
    ActiveWindow.ScrollRow = 125
    ActiveWindow.ScrollRow = 121
    ActiveWindow.ScrollRow = 113
    ActiveWindow.ScrollRow = 109
    ActiveWindow.ScrollRow = 103
    ActiveWindow.ScrollRow = 95
    ActiveWindow.ScrollRow = 90
    ActiveWindow.ScrollRow = 82
    ActiveWindow.ScrollRow = 78
    ActiveWindow.ScrollRow = 70
    ActiveWindow.ScrollRow = 68
    ActiveWindow.ScrollRow = 61
    ActiveWindow.ScrollRow = 59
    ActiveWindow.ScrollRow = 55
    ActiveWindow.ScrollRow = 51
    ActiveWindow.ScrollRow = 48
    ActiveWindow.ScrollRow = 45
    ActiveWindow.ScrollRow = 43
    ActiveWindow.ScrollRow = 40
    ActiveWindow.ScrollRow = 39
    ActiveWindow.ScrollRow = 37
    ActiveWindow.ScrollRow = 35
    ActiveWindow.ScrollRow = 33
    ActiveWindow.ScrollRow = 32
    ActiveWindow.ScrollRow = 31
    ActiveWindow.ScrollRow = 30
    ActiveWindow.ScrollRow = 27
    ActiveWindow.ScrollRow = 23
    ActiveWindow.ScrollRow = 20
    ActiveWindow.ScrollRow = 19
    ActiveWindow.ScrollRow = 18
    ActiveWindow.ScrollRow = 16
    ActiveWindow.ScrollRow = 15
    ActiveWindow.ScrollRow = 14
    ActiveWindow.ScrollRow = 12
    ActiveWindow.ScrollRow = 11
    ActiveWindow.ScrollRow = 10
    ActiveWindow.ScrollRow = 8
    ActiveWindow.ScrollRow = 7
    ActiveWindow.ScrollRow = 6
    ActiveWindow.ScrollRow = 4
    ActiveWindow.ScrollRow = 3
    ActiveWindow.ScrollRow = 1
    ActiveWindow.ScrollColumn = 2
    ActiveWindow.ScrollColumn = 1
    Range("A1").Select
    Sheets("01.Definicción de ámbito").Select
    ActiveWindow.ScrollWorkbookTabs Sheets:=-1
    Sheets("00.Info").Select
    Range("A1").Select
'
End Sub


