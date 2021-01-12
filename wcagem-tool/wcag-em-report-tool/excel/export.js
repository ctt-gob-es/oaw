var xpath = require('xpath');
var path = require('path');
var dom = require('xmldom').DOMParser;
var serializer = new (require('xmldom')).XMLSerializer;
var express = require('express');
var app = express();
var fs = require('fs');
var child_process = require('child_process');
var bodyParser = require('body-parser');
var cors = require('cors');
var { v4: uuidv4 } = require('uuid');
const xlsxPopulate = require('xlsx-populate');


app.use(cors());
app.use(bodyParser.json({ limit: '50mb' }));
app.use(bodyParser.urlencoded({ limit: '50mb', extended: true, parameterLimit: 50000 }));



app.get('/', function (req, res) {
    res.send('Hello World!');
});

app.listen(9001, function () {
    console.log('**** Generador de ODS escuchando en el puero 9001 ****');
});

app.post('/ods', function (request, response) {

    const sourceFile = path.resolve(__dirname) + '/ods/original/content.xml';
    const outputFile = path.resolve(__dirname) + '/ods/modified/content.xml';

    console.log('**** Inicio del proceso de conversión de JSON a ODS ****');
    console.log("Procesando JSON de entrada...");

    fs.readFile(sourceFile, 'utf8', function (error, xml) {
        if (!error) {

            console.log("Procesando ODS de entrada...");

            var doc = new dom().parseFromString(xml)
            var select = xpath.useNamespaces(
                {
                    "office": "urn:oasis:names:tc:opendocument:xmlns:office:1.0",
                    "table": "urn:oasis:names:tc:opendocument:xmlns:table:1.0",
                    "text": "urn:oasis:names:tc:opendocument:xmlns:text:1.0"
                }
            );
            //Json on body
            var report = request.body;

            var techMap = new Map();
            techMap.set("HTML5", [3, 9]);
            techMap.set("XHTML 1.0", [3, 10]);
            techMap.set("HTML 4.01", [3, 11]);
            techMap.set("CSS", [3, 12]);
            techMap.set("WAI-ARIA", [3, 13]);
            techMap.set("ECMAScript 3", [6, 9]);
            techMap.set("ECMAScript 5", [6, 10]);
            techMap.set("DOM", [6, 11]);
            techMap.set("Flash", [6, 12]);
            techMap.set("Silverlight", [6, 13]);
            techMap.set("OOXML", [9, 9]);
            techMap.set("ODF 1.2", [9, 10]);
            techMap.set("SVG", [9, 11]);
            techMap.set("OTRAS", [9, 12]);

            var perceptibleTablesData = new Map();
            perceptibleTablesData.set(0, 0);
            perceptibleTablesData.set(1, 1);
            perceptibleTablesData.set(2, 2);
            perceptibleTablesData.set(3, 3);
            perceptibleTablesData.set(4, 4);
            perceptibleTablesData.set(5, 5);
            perceptibleTablesData.set(6, 10);
            perceptibleTablesData.set(7, 11);
            perceptibleTablesData.set(8, 12);
            perceptibleTablesData.set(9, 13);
            perceptibleTablesData.set(10, 14);
            perceptibleTablesData.set(11, 16);
            perceptibleTablesData.set(12, 17);
            perceptibleTablesData.set(13, 18);
            perceptibleTablesData.set(14, 19);
            perceptibleTablesData.set(15, 20);
            perceptibleTablesData.set(16, 25);
            perceptibleTablesData.set(17, 26);
            perceptibleTablesData.set(18, 27);
            perceptibleTablesData.set(19, 28);

            var operableTablesData = new Map();
            operableTablesData.set(0, 29);
            operableTablesData.set(1, 30);
            operableTablesData.set(2, 32);
            operableTablesData.set(3, 33);
            operableTablesData.set(4, 34);
            operableTablesData.set(5, 39);
            operableTablesData.set(6, 42);
            operableTablesData.set(7, 43);
            operableTablesData.set(8, 44);
            operableTablesData.set(9, 45);
            operableTablesData.set(10, 46);
            operableTablesData.set(11, 47);
            operableTablesData.set(12, 48);
            operableTablesData.set(13, 52);
            operableTablesData.set(14, 53);
            operableTablesData.set(15, 54);
            operableTablesData.set(16, 55);


            var comprensibleTablesData = new Map();
            comprensibleTablesData.set(0, 58);
            comprensibleTablesData.set(1, 59);
            comprensibleTablesData.set(2, 64);
            comprensibleTablesData.set(3, 65);
            comprensibleTablesData.set(4, 66);
            comprensibleTablesData.set(5, 67);
            comprensibleTablesData.set(6, 69);
            comprensibleTablesData.set(7, 70);
            comprensibleTablesData.set(8, 71);
            comprensibleTablesData.set(9, 72);

            var robustoTablesData = new Map();
            robustoTablesData.set(0, 75);
            robustoTablesData.set(1, 76);
            robustoTablesData.set(2, 77);


            //process ambit [01]

            var website = report.data['@graph'][0].evaluationScope.website;

            var titleElm = doc.createElement("text:p");
            var text = doc.createTextNode(website.uraName?website.uraName:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[7]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.uraDIR3?website.uraDIR3:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[9]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.uraScope?website.uraScope:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[11]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.scopeDIR3?website.scopeDIR3:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[13]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.responsibleEntity?website.responsibleEntity:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[17]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.responsibleEntityDIR3?website.responsibleEntityDIR3:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[19]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.responsiblePerson?website.responsiblePerson:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[21]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");


            var typologyText="";
            switch (website.typology) {
                case "TYPO1": typologyText = "Mayoritariamente estáctico"; break;
                case "TYPO2": typologyText = "Servicio electrónico"; break;
            }


            text = doc.createTextNode(typologyText);
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[25]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.siteName?website.siteName:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[27]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.url?website.url:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[29]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.siteScope?website.siteScope:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[31]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.basicFunctionality?website.basicFunctionality:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[33]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.revisionDate?website.revisionDate:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[35]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            var accessibilitySupportBaseline = report.data['@graph'][0].evaluationScope.accessibilitySupportBaseline;

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(accessibilitySupportBaseline?accessibilitySupportBaseline:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[41]/table:table-cell[3]`, doc)[0].appendChild(titleElm);



            var territorialScopeText = "";
            switch (website.territorialScope) {
                case "TERRITORIAL_AGE": territorialScopeText = "AGE"; break;
                case "TERRITORIAL_CCAA": territorialScopeText = "CCAA"; break;
                case "TERRITORIAL_EELL": territorialScopeText = "EELL"; break;
                case "TERRITORIAL_OTHER": territorialScopeText = "Otros"; break;
            }

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(territorialScopeText);
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[45]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            var thematicScopes = report.data['@graph'][0].evaluationScope.reliedUponThematic;

            for (var i = 0; i < thematicScopes.length; i++) {
                var currentThematic = thematicScopes[i];
                var cellRow;
                switch (currentThematic.id) {
                    case "THEMATIC_1": cellRow = 49; break;
                    case "THEMATIC_2": cellRow = 50; break;
                    case "THEMATIC_3": cellRow = 51; break;
                    case "THEMATIC_4": cellRow = 52; break;
                    case "THEMATIC_5": cellRow = 53; break;
                    case "THEMATIC_6": cellRow = 54; break;
                    case "THEMATIC_7": cellRow = 55; break;
                    case "THEMATIC_8": cellRow = 56; break;
                    case "THEMATIC_9": cellRow = 57; break;
                    case "THEMATIC_10": cellRow = 58; break;
                    case "THEMATIC_11": cellRow = 59; break;
                }

                titleElm = doc.createElement("text:p");
                text = doc.createTextNode("Sí");
                titleElm.appendChild(text);
                var oldNode = select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[${cellRow}]/table:table-cell[4]/text:p`, doc)[0];
                select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[${cellRow}]/table:table-cell[3]`, doc)[0].replaceChild(titleElm, oldNode);
            }



            var evaluationTypeText = "";
            switch (website.evaluationType) {
                case "EVALUATION_TYPE_1": evaluationTypeText = "Autoevaluación con recursos propios"; break;
                case "EVALUATION_TYPE_2": evaluationTypeText = "Autoevaluación con recursos externos"; break;
                case "EVALUATION_TYPE_3": evaluationTypeText = "Inspección acreditada por ENAC"; break;
            }

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(evaluationTypeText);
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[61]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.evaluationCompany?website.evaluationCompany:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[63]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            titleElm = doc.createElement("text:p");
            text = doc.createTextNode(website.observations?website.observations:"");
            titleElm.appendChild(text);
            select(`//office:spreadsheet/table:table[@table:name='01.Definición de ámbito']/table:table-row[65]/table:table-cell[3]`, doc)[0].appendChild(titleElm);

            //checking technologies [02]
            console.log("Procesando tecnologías...");
            var technologies = report.data['@graph'][0].reliedUponTechnology;

            var techCells = [];

            var cellRow = 13;

            for (var i = 0; i < technologies.length; i++) {
                if (techMap.has(technologies[i].title)) {
                    techCells.push(techMap.get(technologies[i].title));
                } else {
                    techCells.push(techMap.get("OTRAS"));

                    if (cellRow < 23) {

                        var repeat = 10;
                        var nameColumn = 1;
                        var descriptionColumn = 2;

                        if (cellRow == 13) {
                            nameColumn = 7;
                            descriptionColumn = 8;
                            var repeat = 4;
                        }

                        try {

                            var row = select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${cellRow}]`, doc)[0];

                            if (row == undefined) {
                                row = doc.createElement("table:table-row");
                                row.setAttribute("table:number-rows-repeated", 0);
                                select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']`, doc)[0].appendChild(row);
                            } else {
                                row.setAttribute("table:number-rows-repeated", 0);
                            }

                            //Remove node repeat all line
                            var oldNode = select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${cellRow}]/table:table-cell[${nameColumn}]`, doc)[0];
                            var tableCell = doc.createElement("table:table-cell");
                            tableCell.setAttribute("table:number-columns-repeated", repeat);
                            row.replaceChild(tableCell, oldNode);

                            // Tech title
                            var titleTech = doc.createElement("text:p");
                            var text = doc.createTextNode(technologies[i].title);
                            titleTech.appendChild(text);
                            var tableCellTitle = doc.createElement("table:table-cell");
                            tableCellTitle.appendChild(titleTech);
                            row.appendChild(tableCellTitle);

                            //Tech URL
                            var urlTech = doc.createElement("text:p");
                            var url = doc.createTextNode(technologies[i].id);
                            urlTech.appendChild(url);

                            var tableCellDesc = doc.createElement("table:table-cell");
                            tableCellDesc.appendChild(urlTech);

                            row.appendChild(tableCellDesc);

                        } catch (error) {
                            console.log("Error al exportar : " + technologies[i].title)
                        }

                        cellRow++;
                    }
                }
            }


            try {
                if (cellRow >= 14 && cellRow < 23) {
                    var row = select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${cellRow}]`, doc)[0];
                    if (row == undefined) {
                        row = doc.createElement("table:table-row");
                        row.setAttribute("table:number-rows-repeated", 24 - cellRow);
                        select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']`, doc)[0].appendChild(row);
                    } else {
                        row.setAttribute("table:number-rows-repeated", 24 - cellRow);
                    }

                }

            } catch (error) {

            }


            try {
                for (var i = 0; i < techCells.length; i++) {


                    var cellRow = techCells[i][1];
                    var cellColumn = techCells[i][0];


                    titleElm = doc.createElement("text:p");
                    text = doc.createTextNode("Sí");
                    titleElm.appendChild(text);
                    var oldNode = select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${cellRow}]/table:table-cell[${cellColumn}]/text:p`, doc)[0];
                    select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${cellRow}]/table:table-cell[${cellColumn}]`, doc)[0].replaceChild(titleElm, oldNode);


                }

            } catch (error) {

            }


            //checking samples [03]

            var webpagesStructured = report.data['@graph'][0].structuredSample.webpage;
            var webpagesRandom = report.data['@graph'][0].randomSample.webpage;

            var webpages = webpagesStructured.concat(webpagesRandom);
            var row = 8;

            console.log("Procesando muestra de páginas...");

            for (var i = 0; i < webpages.length && i < 35; i++) {
                //Page ttile
                var titleElm = doc.createElement("text:p");
                var text = doc.createTextNode(webpages[i].title);
                titleElm.appendChild(text);
                //Page type
                var pageTypeOption = "";
                switch (webpages[i].pageType) {
                    case "PAGE_TYPE_1": pageTypeOption = "Página inicio"; break;
                    case "PAGE_TYPE_2": pageTypeOption = "Inicio de sesión"; break;
                    case "PAGE_TYPE_3": pageTypeOption = "Mapa web"; break;
                    case "PAGE_TYPE_4": pageTypeOption = "Contacto"; break;
                    case "PAGE_TYPE_5": pageTypeOption = "Ayuda"; break;
                    case "PAGE_TYPE_6": pageTypeOption = "Legal"; break;
                    case "PAGE_TYPE_7": pageTypeOption = "Servicio / Proceso"; break;
                    case "PAGE_TYPE_8": pageTypeOption = "Búsqueda"; break;
                    case "PAGE_TYPE_9": pageTypeOption = "Declaración accesibilidad"; break;
                    case "PAGE_TYPE_10": pageTypeOption = "Mecanismo de comunicación"; break;
                    case "PAGE_TYPE_11": pageTypeOption = "Pagina tipo"; break;
                    case "PAGE_TYPE_12": pageTypeOption = "Otras páginas"; break;
                    case "PAGE_TYPE_13": pageTypeOption = "Documento descargable"; break;
                    case "PAGE_TYPE_14": pageTypeOption = "Aleatoria"; break;
                }
                var pageTypeElm = doc.createElement("text:p");
                var pageTypeText = doc.createTextNode(pageTypeOption);
                pageTypeElm.appendChild(pageTypeText);


                //Page URL
                var urlElm = doc.createElement("text:p");
                var url = doc.createTextNode(webpages[i].source ? webpages[i].source : (webpages[i].description?webpages[i].description:""));
                urlElm.appendChild(url);

                //breadcrumb
                var breadcrumbElm = doc.createElement("text:p");
                var breadcrumbUrl = doc.createTextNode(webpages[i].breadcrumb ? webpages[i].breadcrumb : "");
                breadcrumbElm.appendChild(breadcrumbUrl);

                //elements
                var elementsElm = doc.createElement("text:p");
                var elementsText = doc.createTextNode(webpages[i].elements ? webpages[i].elements : "");
                elementsElm.appendChild(elementsText);

                select(`//office:spreadsheet/table:table[@table:name='03.Muestra']/table:table-row[${row}]/table:table-cell[3]`, doc)[0].appendChild(titleElm);
                select(`//office:spreadsheet/table:table[@table:name='03.Muestra']/table:table-row[${row}]/table:table-cell[4]`, doc)[0].appendChild(pageTypeElm);
                select(`//office:spreadsheet/table:table[@table:name='03.Muestra']/table:table-row[${row}]/table:table-cell[5]`, doc)[0].appendChild(urlElm);
                select(`//office:spreadsheet/table:table[@table:name='03.Muestra']/table:table-row[${row}]/table:table-cell[6]`, doc)[0].appendChild(breadcrumbElm);
                select(`//office:spreadsheet/table:table[@table:name='03.Muestra']/table:table-row[${row}]/table:table-cell[7]`, doc)[0].appendChild(elementsElm);
                row++;
            }

            //check results

            var FIRSTROW = 19;
            //checking results [P1]
            console.log("Procesando resultados de Perceptible...");

            for (var i = 0; i < 20; i++) {
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (perceptibleTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[perceptibleTablesData.get(i)].hasPart;

                    if (resultsByType.length == 0) {
                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];
                            var type = doc.createElement("text:p");
                            var text = doc.createTextNode("N/T");
                            type.appendChild(text);
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("Pasa");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("Falla");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/D");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/A");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }
                                else {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/T");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }
                            } else {
                                var type = doc.createElement("text:p");
                                var text = doc.createTextNode("N/T");
                                type.appendChild(text);
                                select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                            }

                            cellRow = cellRow + 1;
                        }
                    }
                }
            }


            //checking results [P2]
            console.log("Procesando resultados de Operable...");
            for (var i = 0; i < 17; i++) {
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (operableTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[operableTablesData.get(i)].hasPart;

                    if (resultsByType.length == 0) {
                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];
                            var type = doc.createElement("text:p");
                            var text = doc.createTextNode("N/T");
                            type.appendChild(text);
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("Pasa");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("Falla");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/D");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }
                                else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/A");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }
                                else {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/T");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }

                            } else {
                                var type = doc.createElement("text:p");
                                var text = doc.createTextNode("N/A");
                                type.appendChild(text);
                                select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);


                            }
                            cellRow = cellRow + 1;
                        }
                    }
                }
            }

            //checking results [P3]
            console.log("Procesando resultados de Comprensible...");
            for (var i = 0; i < 10; i++) {
                //console.log(i);
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (comprensibleTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[comprensibleTablesData.get(i)].hasPart;

                    if (resultsByType.length == 0) {
                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];
                            var type = doc.createElement("text:p");
                            var text = doc.createTextNode("N/T");
                            type.appendChild(text);
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {

                            cell = [4, cellRow];
                            if (resultsByType[j]) {
                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("Pasa");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("Falla");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/D");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/A");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }
                                else {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/T");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }
                            } else {
                                var type = doc.createElement("text:p");
                                var text = doc.createTextNode("N/A");
                                type.appendChild(text);
                                select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                            }


                            cellRow = cellRow + 1;
                        }
                    }
                }
            }

            //checking results [P4]
            console.log("Procesando resultados de Robusto...");
            for (var i = 0; i < 3; i++) {
                //console.log(i);
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (robustoTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[robustoTablesData.get(i)].hasPart;

                    if (resultsByType.length == 0) {
                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];
                            var type = doc.createElement("text:p");
                            var text = doc.createTextNode("N/T");
                            type.appendChild(text);
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                            cellRow = cellRow + 1;
                        }

                    } else {
                        for (var j = 0; j < resultsByType.length && j < webpages.length && j < 35; j++) {

                            cell = [4, cellRow];

                            if (resultsByType[j]) {
                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("Pasa");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("Falla");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/D");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/A");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }
                                else {
                                    var type = doc.createElement("text:p");
                                    var text = doc.createTextNode("N/T");
                                    type.appendChild(text);
                                    select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                                }
                            } else {
                                var type = doc.createElement("text:p");
                                var text = doc.createTextNode("N/T");
                                type.appendChild(text);
                                select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].appendChild(type);
                            }

                            cellRow = cellRow + 1;
                        }
                    }
                }
            }

            //Save the file

            fs.writeFile(
                outputFile,
                serializer.serializeToString(doc),
                function (error) {
                    if (error) {
                        console.log(error);
                    } else {

                        const cwdFolder = path.resolve(__dirname) + '/ods/modified/';

                        const odsName = uuidv4() + '.ods';

                        const finalOds = path.resolve(__dirname) + '/ods/' + odsName;

                        child_process.exec(`zip -0 -X ${finalOds} mimetype`, { cwd: cwdFolder }, (error, stdout, stderr) => {
                            if (error) {
                                console.error(`exec error: ${error}`);
                                return;
                            }

                            child_process.exec(`zip -r ${finalOds} * -x mimetype`, { cwd: cwdFolder }, (error, stdout, stderr) => {
                                if (error) {
                                    console.error(`exec error: ${error}`);
                                    return;
                                }

                                console.log('Fichero ODS generado con éxito');
                                // zip archive of your folder is ready to download and download on success
                                response.sendFile(finalOds, function (err) {
                                    if (err) {
                                        next(err);
                                    } else {
                                        console.log('**** Finalizado del proceso de conversión de JSON a ODS ****');
                                        try {
                                            fs.unlink(finalOds, function (err) {
                                                if (err) {
                                                    next(err);
                                                }
                                            });
                                        } catch (e) {
                                            console.log("error removing ", filename);
                                        }
                                    }
                                });
                            });
                        });
                    }
                }
            );

        } else {
            console.log(`Error al leer el fichero ODS ${error}`);
        }
    });
});

app.post('/xlsx', function (request, response) {

    const sourceFile = path.resolve(__dirname) + '/xlsx/Informe_Revision_Profunidad_v1.xlsx';

    // Load an existing workbook
    xlsxPopulate.fromFileAsync(sourceFile)
        .then(workbook => {
            // Modify the workbook.
            console.log('**** Inicio del proceso de conversión de JSON a XLSX ****');
            //Json on body
            var report = request.body;

            var techMap = new Map();
            techMap.set("HTML5", 'C9');
            techMap.set("XHTML 1.0", 'C10');
            techMap.set("HTML 4.01", 'C11');
            techMap.set("CSS", 'C12');
            techMap.set("WAI-ARIA", 'C13');
            techMap.set("ECMAScript 3", 'F9');
            techMap.set("ECMAScript 5", 'F10');
            techMap.set("DOM", 'F11');
            techMap.set("Flash", 'F12');
            techMap.set("Silverlight", 'F13');
            techMap.set("OOXML", 'I9');
            techMap.set("ODF 1.2", 'I10');
            techMap.set("SVG", 'I11');
            techMap.set("OTRAS", 'I12');

            var perceptibleTablesData = new Map();
            perceptibleTablesData.set(0, 0);
            perceptibleTablesData.set(1, 1);
            perceptibleTablesData.set(2, 2);
            perceptibleTablesData.set(3, 3);
            perceptibleTablesData.set(4, 4);
            perceptibleTablesData.set(5, 5);
            perceptibleTablesData.set(6, 10);
            perceptibleTablesData.set(7, 11);
            perceptibleTablesData.set(8, 12);
            perceptibleTablesData.set(9, 13);
            perceptibleTablesData.set(10, 14);
            perceptibleTablesData.set(11, 16);
            perceptibleTablesData.set(12, 17);
            perceptibleTablesData.set(13, 18);
            perceptibleTablesData.set(14, 19);
            perceptibleTablesData.set(15, 20);
            perceptibleTablesData.set(16, 25);
            perceptibleTablesData.set(17, 26);
            perceptibleTablesData.set(18, 27);
            perceptibleTablesData.set(19, 28);

            var operableTablesData = new Map();
            operableTablesData.set(0, 29);
            operableTablesData.set(1, 30);
            operableTablesData.set(2, 32);
            operableTablesData.set(3, 33);
            operableTablesData.set(4, 34);
            operableTablesData.set(5, 39);
            operableTablesData.set(6, 42);
            operableTablesData.set(7, 43);
            operableTablesData.set(8, 44);
            operableTablesData.set(9, 45);
            operableTablesData.set(10, 46);
            operableTablesData.set(11, 47);
            operableTablesData.set(12, 48);
            operableTablesData.set(13, 52);
            operableTablesData.set(14, 53);
            operableTablesData.set(15, 54);
            operableTablesData.set(16, 55);


            var comprensibleTablesData = new Map();
            comprensibleTablesData.set(0, 58);
            comprensibleTablesData.set(1, 59);
            comprensibleTablesData.set(2, 64);
            comprensibleTablesData.set(3, 65);
            comprensibleTablesData.set(4, 66);
            comprensibleTablesData.set(5, 67);
            comprensibleTablesData.set(6, 69);
            comprensibleTablesData.set(7, 70);
            comprensibleTablesData.set(8, 71);
            comprensibleTablesData.set(9, 72);

            var robustoTablesData = new Map();
            robustoTablesData.set(0, 75);
            robustoTablesData.set(1, 76);
            robustoTablesData.set(2, 77);



            //process ambit [01]

            var website = report.data['@graph'][0].evaluationScope.website;

            workbook.sheet("01.Definición de ámbito").cell("C7").value(website.uraName);
            workbook.sheet("01.Definición de ámbito").cell("C9").value(website.uraDIR3);
            workbook.sheet("01.Definición de ámbito").cell("C11").value(website.uraScope);
            workbook.sheet("01.Definición de ámbito").cell("C13").value(website.scopeDIR3);
            workbook.sheet("01.Definición de ámbito").cell("C17").value(website.responsibleEntity);
            workbook.sheet("01.Definición de ámbito").cell("C19").value(website.responsibleEntityDIR3);
            workbook.sheet("01.Definición de ámbito").cell("C21").value(website.responsiblePerson);


            var typologyText;
            switch (website.typology) {
                case "TYPO1": typologyText = "Mayoritariamente estáctico"; break;
                case "TYPO2": typologyText = "Servicio electrónico"; break;
            }

            workbook.sheet("01.Definición de ámbito").cell("C25").value(typologyText);


            workbook.sheet("01.Definición de ámbito").cell("C27").value(website.siteName);
            workbook.sheet("01.Definición de ámbito").cell("C29").value(website.url);
            workbook.sheet("01.Definición de ámbito").cell("C31").value(website.siteScope);
            workbook.sheet("01.Definición de ámbito").cell("C33").value(website.basicFunctionality);
            workbook.sheet("01.Definición de ámbito").cell("C35").value(website.revisionDate);

            var accessibilitySupportBaseline = report.data['@graph'][0].evaluationScope.accessibilitySupportBaseline;
            workbook.sheet("01.Definición de ámbito").cell("C41").value(accessibilitySupportBaseline);

            var territorialScopeText;
            switch (website.territorialScope) {
                case "TERRITORIAL_AGE": territorialScopeText = "AGE"; break;
                case "TERRITORIAL_CCAA": territorialScopeText = "CCAA"; break;
                case "TERRITORIAL_EELL": territorialScopeText = "EELL"; break;
                case "TERRITORIAL_OTHER": territorialScopeText = "Otros"; break;
            }

            workbook.sheet("01.Definición de ámbito").cell("C45").value(territorialScopeText);

            var thematicScopes = report.data['@graph'][0].evaluationScope.reliedUponThematic;

            for (var i = 0; i < thematicScopes.length; i++) {
                var currentThematic = thematicScopes[i];
                var cellRow;
                switch (currentThematic.id) {
                    case "THEMATIC_1": cellRow = 49; break;
                    case "THEMATIC_2": cellRow = 50; break;
                    case "THEMATIC_3": cellRow = 51; break;
                    case "THEMATIC_4": cellRow = 52; break;
                    case "THEMATIC_5": cellRow = 53; break;
                    case "THEMATIC_6": cellRow = 54; break;
                    case "THEMATIC_7": cellRow = 55; break;
                    case "THEMATIC_8": cellRow = 56; break;
                    case "THEMATIC_9": cellRow = 57; break;
                    case "THEMATIC_10": cellRow = 58; break;
                    case "THEMATIC_11": cellRow = 59; break;
                }
                // exists one line less in xlsx vs ods
                workbook.sheet("01.Definición de ámbito").cell("D" + (cellRow - 1)).value("Sí");
            }


            var evaluationTypeText;
            switch (website.evaluationType) {
                case "EVALUATION_TYPE_1": evaluationTypeText = "Autoevaluación con recursos propios"; break;
                case "EVALUATION_TYPE_2": evaluationTypeText = "Autoevaluación con recursos externos"; break;
                case "EVALUATION_TYPE_3": evaluationTypeText = "Inspección acreditada por ENAC"; break;
            }
            workbook.sheet("01.Definición de ámbito").cell("C60").value(evaluationTypeText);
            workbook.sheet("01.Definición de ámbito").cell("C62").value(website.evaluationCompany);
            workbook.sheet("01.Definición de ámbito").cell("C64").value(website.observations);




            //checking technologies [02]
            console.log("Procesando tecnologías...");
            var technologies = report.data['@graph'][0].reliedUponTechnology;

            var techCells = [];
            var otherTech = 1;

            var cellRow = 13;

            for (var i = 0; i < technologies.length; i++) {
                if (techMap.has(technologies[i].title)) {
                    techCells.push(techMap.get(technologies[i].title));
                } else {
                    techCells.push(techMap.get("OTRAS"));

                    for (var k = 0; k < otherTech && k < 12; k++) {

                        var nameColumn = 'K';
                        var descriptionColumn = 'L';
                        workbook.sheet("02.Tecnologías").cell(nameColumn + "" + cellRow).value(technologies[i].title);
                        workbook.sheet("02.Tecnologías").cell(descriptionColumn + "" + cellRow).value(technologies[i].id);
                        cellRow++;
                    }

                }
            }

            for (var i = 0; i < techCells.length; i++) {

                workbook.sheet("02.Tecnologías").cell(techCells[i]).value("Sí");

            }

            //checking samples [03]

            var webpagesStructured = report.data['@graph'][0].structuredSample.webpage;
            var webpagesRandom = report.data['@graph'][0].randomSample.webpage;

            var webpages = webpagesStructured.concat(webpagesRandom);
            var row = 8;

            console.log("Procesando muestra de páginas...");

            for (var i = 0; i < webpages.length && i < 35; i++) {
                workbook.sheet("03.Muestra").cell("C" + row).value(webpages[i].title);

                var pageTypeOption = "";
                switch (webpages[i].pageType) {
                    case "PAGE_TYPE_1": pageTypeOption = "Página inicio"; break;
                    case "PAGE_TYPE_2": pageTypeOption = "Inicio de sesión"; break;
                    case "PAGE_TYPE_3": pageTypeOption = "Mapa web"; break;
                    case "PAGE_TYPE_4": pageTypeOption = "Contacto"; break;
                    case "PAGE_TYPE_5": pageTypeOption = "Ayuda"; break;
                    case "PAGE_TYPE_6": pageTypeOption = "Legal"; break;
                    case "PAGE_TYPE_7": pageTypeOption = "Servicio / Proceso"; break;
                    case "PAGE_TYPE_8": pageTypeOption = "Búsqueda"; break;
                    case "PAGE_TYPE_9": pageTypeOption = "Declaración accesibilidad"; break;
                    case "PAGE_TYPE_10": pageTypeOption = "Mecanismo de comunicación"; break;
                    case "PAGE_TYPE_11": pageTypeOption = "Pagina tipo"; break;
                    case "PAGE_TYPE_12": pageTypeOption = "Otras páginas"; break;
                    case "PAGE_TYPE_13": pageTypeOption = "Documento descargable"; break;
                    case "PAGE_TYPE_14": pageTypeOption = "Aleatoria"; break;
                }

                workbook.sheet("03.Muestra").cell("D" + row).value(pageTypeOption);
                workbook.sheet("03.Muestra").cell("E" + row).value(webpages[i].source ? webpages[i].source : webpages[i].description);
                workbook.sheet("03.Muestra").cell("F" + row).value(webpages[i].breadcrumb ? webpages[i].breadcrumb : "");
                workbook.sheet("03.Muestra").cell("G" + row).value(webpages[i].elements ? webpages[i].elements : "");
                row++;
            }

            //check results

            var FIRSTROW = 19;
            //checking results [P1]
            console.log("Procesando resultados de Perceptible...");

            for (var i = 0; i < 20; i++) {
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (perceptibleTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[perceptibleTablesData.get(i)].hasPart;

                    if (resultsByType.length == 0) {
                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];
                            workbook.sheet("P1.Perceptible").cell("D" + cell[1]).value("N/T");
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    workbook.sheet("P1.Perceptible").cell("D" + cell[1]).value("Pasa");
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    workbook.sheet("P1.Perceptible").cell("D" + cell[1]).value("Falla");
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    workbook.sheet("P1.Perceptible").cell("D" + cell[1]).value("N/D");
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    workbook.sheet("P1.Perceptible").cell("D" + cell[1]).value("N/A");
                                } else {
                                    workbook.sheet("P1.Perceptible").cell("D" + cell[1]).value("N/T");
                                }
                            } else {
                                workbook.sheet("P1.Perceptible").cell("D" + cell[1]).value("N/T");
                            }

                            cellRow = cellRow + 1;
                        }
                    }
                }
            }

            console.log("Procesando resultados de Operable...");

            for (var i = 0; i < 17; i++) {
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (operableTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[operableTablesData.get(i)].hasPart;

                    if (resultsByType.length == 0) {
                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];
                            workbook.sheet("P2.Operable").cell("D" + cell[1]).value("N/T");
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    workbook.sheet("P2.Operable").cell("D" + cell[1]).value("Pasa");
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    workbook.sheet("P2.Operable").cell("D" + cell[1]).value("Falla");
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    workbook.sheet("P2.Operable").cell("D" + cell[1]).value("N/D");
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    workbook.sheet("P2.Operable").cell("D" + cell[1]).value("N/A");
                                } else {
                                    workbook.sheet("P2.Operable").cell("D" + cell[1]).value("N/T");
                                }
                            } else {
                                workbook.sheet("P2.Operable").cell("D" + cell[1]).value("N/T");
                            }

                            cellRow = cellRow + 1;
                        }
                    }
                }
            }


            console.log("Procesando resultados de Comprensible...");

            for (var i = 0; i < 10; i++) {
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (comprensibleTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[comprensibleTablesData.get(i)].hasPart;

                    if (resultsByType.length == 0) {
                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];
                            workbook.sheet("P3.Comprensible").cell("D" + cell[1]).value("N/T");
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    workbook.sheet("P3.Comprensible").cell("D" + cell[1]).value("Pasa");
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    workbook.sheet("P3.Comprensible").cell("D" + cell[1]).value("Falla");
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    workbook.sheet("P3.Comprensible").cell("D" + cell[1]).value("N/D");
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    workbook.sheet("P3.Comprensible").cell("D" + cell[1]).value("N/A");
                                } else {
                                    workbook.sheet("P3.Comprensible").cell("D" + cell[1]).value("N/T");
                                }
                            } else {
                                workbook.sheet("P3.Comprensible").cell("D" + cell[1]).value("N/T");
                            }

                            cellRow = cellRow + 1;
                        }
                    }
                }
            }


            console.log("Procesando resultados de Robusto...");

            for (var i = 0; i < 3; i++) {
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (robustoTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[robustoTablesData.get(i)].hasPart;

                    if (resultsByType.length == 0) {
                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];
                            workbook.sheet("P4.Robusto").cell("D" + cell[1]).value("N/T");
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    workbook.sheet("P4.Robusto").cell("D" + cell[1]).value("Pasa");
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    workbook.sheet("P4.Robusto").cell("D" + cell[1]).value("Falla");
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    workbook.sheet("P4.Robusto").cell("D" + cell[1]).value("N/D");
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    workbook.sheet("P4.Robusto").cell("D" + cell[1]).value("N/A");
                                } else {
                                    workbook.sheet("P4.Robusto").cell("D" + cell[1]).value("N/T");
                                }
                            } else {
                                workbook.sheet("P4.Robusto").cell("D" + cell[1]).value("N/T");
                            }

                            cellRow = cellRow + 1;
                        }
                    }
                }
            }

            const finalOds = path.resolve(__dirname) + '/xlsx/' + uuidv4() + '.xlsx';

            // Write to file.
            workbook.toFileAsync(finalOds).then(function () {
                response.sendFile(finalOds, function (err) {
                    console.log('**** Finalizado del proceso de conversión de JSON a XLSX ****');
                    try {
                        fs.unlink(finalOds, function (err) {
                            if (err) {
                                console.log('**** Err ' + err);
                            }
                        });
                    } catch (e) {
                        console.log("error removing ", filename);
                    }

                });
            });
        });
});
