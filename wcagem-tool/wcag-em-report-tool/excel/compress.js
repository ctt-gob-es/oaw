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

            //if the JSON is not always the same the map should be done with test names like the following and change the loops
            //perceptibleTablesData.set("WCAG2:non-text-content",0);


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


            //checking technologies [02]
            console.log("Procesando tecnologías...");
            var technologies = report.data['@graph'][0].reliedUponTechnology;

            var techCells = [];
            var otherTech = 1;

            var cellRow = 13;

            for (var i = 0; i < technologies.length; i++) {
                if (techMap.has(technologies[i].title)) {
                    techCells.push(techMap.get(technologies[i].title));
                    //console.log(`--> Tecnología ${i + 1} : ` + technologies[i].title);
                } else {
                    techCells.push(techMap.get("OTRAS"));

                    
                
                    console.log("# other techs : " + otherTech);

                    for (var k = 0; k < otherTech && k < 12; k++) {

                        var nameColumn = 2;
                        var descriptionColumn = 3;

                        if(cellRow == 13){
                            nameColumn = 8;
                            descriptionColumn = 9;
                        }

                        // Tech ttile
                        var titleTech = doc.createElement("text:p");
                        var text = doc.createTextNode(technologies[i].title);
                        titleTech.appendChild(text);


                        var oldNode = doc.createElement("text:p");

                        select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${cellRow}]/table:table-cell[${nameColumn}]`, doc)[0].replaceChild(titleTech, oldNode);
                        

                        //Tech URL
                        var urlTech = doc.createElement("text:p");
                        var url = doc.createTextNode(technologies[i].id);
                        urlTech.appendChild(url);

                        oldNode = doc.createElement("text:p");

                        select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${cellRow}]/table:table-cell[${descriptionColumn}]`, doc)[0].replaceChild(urlTech, oldNode);

                        cellRow++;

                    }

                }
            }

            //clean other techs
            if(cellRow<23){

                for(var c=cellRow;c<=23;c++){

                    var nameColumn = 2;
                    var descriptionColumn = 3;

                    if(cellRow == 13){
                        nameColumn = 8;
                        descriptionColumn = 9;
                    }

                    // Tech ttile
                    var titleTech = doc.createElement("text:p");
                    var text = doc.createTextNode("");
                    titleTech.appendChild(text);


                    var oldNode = doc.createElement("text:p");

                    select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${c}]/table:table-cell[${nameColumn}]`, doc)[0].replaceChild(titleTech, oldNode);
                    

                    //Tech URL
                    var urlTech = doc.createElement("text:p");
                    var url = doc.createTextNode("");
                    urlTech.appendChild(url);

                    oldNode = doc.createElement("text:p");

                    select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${c}]/table:table-cell[${descriptionColumn}]`, doc)[0].replaceChild(urlTech, oldNode);
                }
            }
            


            for (var i = 0; i < techCells.length; i++) {
                //Technology used
                var techUsed = doc.createElement("text:p");
                var text = doc.createTextNode("Sí");
                techUsed.appendChild(text);

                var oldNode = doc.createElement("text:p");

                select(`//office:spreadsheet/table:table[@table:name='02.Tecnologías']/table:table-row[${techCells[i][1]}]/table:table-cell[${techCells[i][0]}]`, doc)[0].replaceChild(techUsed, oldNode);

            }

            //checking samples [03]

            var webpagesStructured = report.data['@graph'][0].structuredSample.webpage;
            var webpagesRandom = report.data['@graph'][0].randomSample.webpage;

            var webpages = webpagesStructured.concat(webpagesRandom);
            var row = 8;

            console.log("Procesando muestra de páginas...");

            for (var i = 0; i < webpages.length && i < 35; i++) {
                //console.log(`--> Página ${i + 1} : ` + webpages[i].title);
                //Page ttile
                var titleElm = doc.createElement("text:p");
                var text = doc.createTextNode(webpages[i].title);
                titleElm.appendChild(text);
                //Page URL
                var urlElm = doc.createElement("text:p");
                var url = doc.createTextNode(webpages[i].source ? webpages[i].source : webpages[i].description);
                urlElm.appendChild(url);

                //Page ytpe
                // var typeElm = doc.createElement("text:p");
                // var type = doc.createTextNode(webpages[i].type[1]);
                // typeElm.appendChild(type);

                select(`//office:spreadsheet/table:table[@table:name='03.Muestra']/table:table-row[${row}]/table:table-cell[3]`, doc)[0].appendChild(titleElm);
                //select(`//office:spreadsheet/table:table[@table:name='03.Muestra']/table:table-row[${row}]/table:table-cell[4]`, doc)[0].appendChild(typeElm);
                select(`//office:spreadsheet/table:table[@table:name='03.Muestra']/table:table-row[${row}]/table:table-cell[5]`, doc)[0].appendChild(urlElm);
                row++;
            }

            //check results

            var FIRSTROW = 19;
            //checking results [P1]
            console.log("Procesando resultados de Perceptible...");
            for (var i = 0; i < 20; i++) {
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (i == 19) { cellRow++ }; //last table is one row lower   
                if (perceptibleTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[perceptibleTablesData.get(i)].hasPart;
                    for (var j = 0; j < resultsByType.length && j < webpages.length && j < 35; j++) {
                        var cell = [4, cellRow];
                        if (resultsByType[j].result.outcome == "earl:passed") {
                            var type = doc.createTextNode("Pasa");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:failed") {
                            var type = doc.createTextNode("Falla");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                            var type = doc.createTextNode("N/D");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                            var type = doc.createTextNode("N/A");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P1.Perceptible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        }

                        cellRow = cellRow + 1;
                    }
                }
            }


            //checking results [P2]
            console.log("Procesando resultados de Operable...");
            for (var i = 0; i < 17; i++) {
                //console.log(i);
                var cellRow = FIRSTROW;
                cellRow = cellRow + (38 * i);
                if (operableTablesData.has(i)) {
                    var resultsByType = report.data["@graph"][0].auditResult[operableTablesData.get(i)].hasPart;
                    for (var j = 0; j < resultsByType.length && j < webpages.length && j < 35; j++) {
                        cell = [4, cellRow];
                        if (resultsByType[j].result.outcome == "earl:passed") {
                            var type = doc.createTextNode("Pasa");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:failed") {
                            var type = doc.createTextNode("Falla");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                            var type = doc.createTextNode("N/D");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        }
                        else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                            var type = doc.createTextNode("N/A");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P2.Operable']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        }


                        cellRow = cellRow + 1;
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
                    for (var j = 0; j < resultsByType.length && j < webpages.length && j < 35; j++) {
                        cell = [4, cellRow];
                        if (resultsByType[j].result.outcome == "earl:passed") {
                            var type = doc.createTextNode("Pasa");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:failed") {
                            var type = doc.createTextNode("Falla");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                            var type = doc.createTextNode("N/D");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                            var type = doc.createTextNode("N/A");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P3.Comprensible']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        }


                        cellRow = cellRow + 1;
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
                    for (var j = 0; j < resultsByType.length && j < webpages.length && j < 35; j++) {
                        cell = [4, cellRow];
                        if (resultsByType[j].result.outcome == "earl:passed") {
                            var type = doc.createTextNode("Pasa");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:failed") {
                            var type = doc.createTextNode("Falla");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                            var type = doc.createTextNode("N/D");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                            var type = doc.createTextNode("N/A");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("office:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].removeAttribute("table:formula");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]`, doc)[0].setAttribute("calcext:value-type", "string");
                            select(`//office:spreadsheet/table:table[@table:name='P4.Robusto']/table:table-row[${cell[1]}]/table:table-cell[${cell[0]}]/text:p`, doc)[0].appendChild(type);
                        }


                        cellRow = cellRow + 1;
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
