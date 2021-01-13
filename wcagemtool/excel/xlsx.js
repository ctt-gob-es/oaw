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

app.listen(9002, function () {
    console.log('**** Generador de XLSX escuchando en el puero 9002 ****');
});

app.post('/xlsx', function (request, response) {

    const sourceFile = path.resolve(__dirname) + '/xlsx/IRA.xlsx';



    // Load an existing workbook
    xlsxPopulate.fromFileAsync(sourceFile)
        .then(workbook => {
            // Modify the workbook.
 
            //Json on body
            var report = request.body;

            var techMap = new Map();
            techMap.set("HTML5", 'C9');
            techMap.set("XHTML 1.0", 'C10');
            techMap.set("HTML 4.01", 'C11');
            techMap.set("CSS", 'C12');
            techMap.set("WAI-ARIA", 'C13');
            techMap.set("ECMAScript 3", 'F9');
            techMap.set("ECMAScript 5",'F10');
            techMap.set("DOM", 'F11');
            techMap.set("Flash", 'F12');
            techMap.set("Silverlight", 'F13');
            techMap.set("OOXML", 'I9');
            techMap.set("ODF 1.2", 'I10');
            techMap.set("SVG", 'I11');
            techMap.set("OTRAS",'I12');

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
                } else {
                    techCells.push(techMap.get("OTRAS"));

                    for (var k = 0; k < otherTech && k < 12; k++) {

                        var nameColumn = 'K';
                        var descriptionColumn = 'L';
                        workbook.sheet("02.Tecnologías").cell(nameColumn+""+cellRow).value(technologies[i].title);
                        workbook.sheet("02.Tecnologías").cell(descriptionColumn+""+cellRow).value(technologies[i].id);
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
                workbook.sheet("03.Muestra").cell("C"+row).value(webpages[i].title);
                workbook.sheet("03.Muestra").cell("E"+row).value(webpages[i].source ? webpages[i].source : webpages[i].description);
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
                            workbook.sheet("P1.Perceptible").cell("D"+cell[1]).value("N/T");
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    workbook.sheet("P1.Perceptible").cell("D"+cell[1]).value("Pasa");
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    workbook.sheet("P1.Perceptible").cell("D"+cell[1]).value("Falla");
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    workbook.sheet("P1.Perceptible").cell("D"+cell[1]).value("N/D");
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    workbook.sheet("P1.Perceptible").cell("D"+cell[1]).value("N/A");
                                } else {
                                    workbook.sheet("P1.Perceptible").cell("D"+cell[1]).value("N/T");
                                }
                            } else {
                                workbook.sheet("P1.Perceptible").cell("D"+cell[1]).value("N/T");
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
                            workbook.sheet("P2.Operable").cell("D"+cell[1]).value("N/T");
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    workbook.sheet("P2.Operable").cell("D"+cell[1]).value("Pasa");
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    workbook.sheet("P2.Operable").cell("D"+cell[1]).value("Falla");
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    workbook.sheet("P2.Operable").cell("D"+cell[1]).value("N/D");
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    workbook.sheet("P2.Operable").cell("D"+cell[1]).value("N/A");
                                } else {
                                    workbook.sheet("P2.Operable").cell("D"+cell[1]).value("N/T");
                                }
                            } else {
                                workbook.sheet("P2.Operable").cell("D"+cell[1]).value("N/T");
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
                            workbook.sheet("P3.Comprensible").cell("D"+cell[1]).value("N/T");
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    workbook.sheet("P3.Comprensible").cell("D"+cell[1]).value("Pasa");
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    workbook.sheet("P3.Comprensible").cell("D"+cell[1]).value("Falla");
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    workbook.sheet("P3.Comprensible").cell("D"+cell[1]).value("N/D");
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    workbook.sheet("P3.Comprensible").cell("D"+cell[1]).value("N/A");
                                } else {
                                    workbook.sheet("P3.Comprensible").cell("D"+cell[1]).value("N/T");
                                }
                            } else {
                                workbook.sheet("P3.Comprensible").cell("D"+cell[1]).value("N/T");
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
                            workbook.sheet("P4.Robusto").cell("D"+cell[1]).value("N/T");
                            cellRow = cellRow + 1;
                        }

                    } else {

                        for (var j = 0; j < webpages.length && j < 35; j++) {
                            var cell = [4, cellRow];

                            if (resultsByType[j]) {

                                if (resultsByType[j].result.outcome == "earl:passed") {
                                    workbook.sheet("P4.Robusto").cell("D"+cell[1]).value("Pasa");
                                } else if (resultsByType[j].result.outcome == "earl:failed") {
                                    workbook.sheet("P4.Robusto").cell("D"+cell[1]).value("Falla");
                                } else if (resultsByType[j].result.outcome == "earl:cantTell") {
                                    workbook.sheet("P4.Robusto").cell("D"+cell[1]).value("N/D");
                                } else if (resultsByType[j].result.outcome == "earl:inapplicable") {
                                    workbook.sheet("P4.Robusto").cell("D"+cell[1]).value("N/A");
                                } else {
                                    workbook.sheet("P4.Robusto").cell("D"+cell[1]).value("N/T");
                                }
                            } else {
                                workbook.sheet("P4.Robusto").cell("D"+cell[1]).value("N/T");
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
