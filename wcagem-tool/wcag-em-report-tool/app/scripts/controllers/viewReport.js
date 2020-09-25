'use strict';

angular.module('wcagReporter')

  .controller('ViewReportCtrl', function (
    $scope,
    $location,
    $document,
    $http,
    wcag2spec,
    evalModel,
    appState,
    wcagReporterExport,
    toggleCriterionText
  ) {
    var htmlBlob;

    $scope.state = appState.moveToState('viewReport');
    $scope.scope = evalModel.scopeModel;
    $scope.explore = evalModel.exploreModel;


    //-----------------------------------------------------------------------------------------
    //to generate the xlsx file
    //for some reason it makes the column width wpx * 1.145
    //and the row heigth hpx * 1.145

    //map with technologies to select them later
    $scope.saveAsXlsx = function () {
      try {

        $scope.loading = true;

        //console.log(doc);
        //console.log($scope);
        //Load JSON


        $http.get($scope.exportJsonUrl, {}).then(function onSuccess(response) {
          //console.log(response.data);
          $http({
            // url: 'http://localhost:9001/ods',
            url: $location.protocol() + "://" + $location.host() + ':' + $location.port() + "/ods",
            method: "POST",
            data: response,
            responseType: 'blob'
          }).then(function (response) {
            var data = response.data;
            var a = document.createElement("a");
            document.body.appendChild(a);

            var file = new Blob([data], { type: 'application/vnd.oasis.opendocument.spreadsheet' });
            var fileURL = window.URL.createObjectURL(file);
            a.href = fileURL;
            a.download = 'Informe_Revision_Profunidad_v1.ods';
            a.click();

            $scope.loading = false;
          }).catch(function (response) {
            $scope.loading = false;
            console.log('Unable to download the file')
          });

        });

      } catch (e) { if (typeof console != 'undefined') console.log(e, $scope.wbout); $scope.loading = false; }
      return $scope.wbout;
    };

    function s2ab(s) {
      if (typeof ArrayBuffer !== 'undefined') {
        var buf = new ArrayBuffer(s.length);
        var view = new Uint8Array(buf);
        for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
        return buf;
      } else {
        var buf = new Array(s.length);
        for (var i = 0; i != s.length; ++i) buf[i] = s.charCodeAt(i) & 0xFF;
        return buf;
      }
    }
    //-----------------------------------------------------------------------------------------


    $scope.filledPages = function () {
      return evalModel.sampleModel.getFilledPages();
    };

    $scope.wcag2specReady = wcag2spec.isLoaded();
    $scope.$on('wcag2spec:langChange', function () {
      $scope.wcag2specReady = true;
    });

    $scope.report = evalModel.reportModel;
    var tpl = [
      '<!DOCTYPE html><html lang="en"><head>' +
      '<meta charset="utf-8">' +
      '<title>' + evalModel.reportModel.title + '</title>' +
      '<script>' + toggleCriterionText.toString()
        .replace('function (a)', 'function toggleCriterionText(a)') + '</script>' +
      '<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />' +
      '<link rel="stylesheet" href="report.css" />' +
      '</head><body><div class="container reporter-view">',
      '</div></body></html>'
    ];

    $scope.$on('reportReady', function (e, data) {
      var html = tpl[0] + data.html() + tpl[1];

      htmlBlob = wcagReporterExport.getBlob(html, 'text/html;charset=utf-8');
      $document.find('#html_download_link')
        .attr(
          'href',
          wcagReporterExport.getBlobUrl(htmlBlob)
        );
    });

    $scope.downloadJsonStart = function () {
      wcagReporterExport.saveBlobIE();
      appState.setPrestineState();
    };

    $scope.saveHtmlBlobIE = function () {
      if (htmlBlob) {
        wcagReporterExport.saveBlobIE(htmlBlob, $scope.exportHtmlFile);
      }
    };

    $scope.exportHtmlFile = wcagReporterExport.getFileName('html');
    $scope.exportJsonUrl = wcagReporterExport.getBlobUrl();
    $scope.exportJsonFile = wcagReporterExport.getFileName();
  });
