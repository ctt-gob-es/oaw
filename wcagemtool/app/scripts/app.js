'use strict';
angular.module('wcagReporter', [
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'ngAnimate',
  'pascalprecht.translate',
  'wert-templates',
  'textAngular',
  'ui.bootstrap'
])
  .config(function ($routeProvider, $compileProvider, $provide) {
    function createTable(tableParams) {
      if (angular.isNumber(tableParams.row) && angular.isNumber(tableParams.col)
        && tableParams.row > 0 && tableParams.col > 0) {
        var table = "<table class='w-table no-border "
          + (tableParams.style ? "border-" + tableParams.style : '')
          + "'>";

        var colWidth = 100 / tableParams.col;
        for (var idxRow = 0; idxRow < tableParams.row; idxRow++) {
          var row = "<tr>";
          for (var idxCol = 0; idxCol < tableParams.col; idxCol++) {
            row += "<td"
              + (idxRow == 0 ? ' style="width: ' + colWidth + '%;"' : '')
              + ">&nbsp;</td>";
          }
          table += row + "</tr>";
        }
        return table + "</table>";
      }
    }

    $provide.decorator('taOptions', ['taRegisterTool', '$delegate', '$uibModal', '$rootScope', function (taRegisterTool, taOptions, $uibModal, $rootScope) {
      taRegisterTool('insertTable', {
        iconclass: 'fa fa-table',
        tooltiptext: 'Insert table',
        action: function (promise, restoreSelection) {
          var that = this;

          var modalInstance = $uibModal.open({
            templateUrl: 'views/table.html',
            windowClass: 'modal-window-sm',
            backdrop: 'static',
            keyboard: false,
            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
              $scope.newtable = {};
              $scope.tablestyles = [
                { name: 'Dotted', value: 'dotted' },
                { name: 'Dashed', value: 'dashed' },
                { name: 'Solid', value: 'solid' },
                { name: 'Double', value: 'double' },
                { name: 'Groove', value: 'groove' },
                { name: 'Ridge', value: 'ridge' },
                { name: 'Inset', value: 'inset' },
                { name: 'Outset', value: 'outset' }];

              $scope.tblInsert = function () {
                $uibModalInstance.close($scope.newtable);
              };

              $scope.tblCancel = function () {
                $uibModalInstance.dismiss("cancel");
              };
            }],
            size: 'sm'
          });
          //define result modal , when user complete result information 
          modalInstance.result.then(function (result) {
            if (result) {
              restoreSelection();
              var html = createTable(result)
              promise.resolve();
              return that.$editor().wrapSelection('insertHtml', html);
            }
          });


          //define result modal , when user complete result information 
          // modalInstance.result.then(function (result) {
          //   if (result) {
          //     insertIntoTextEditor(createTable(result));
          //     restoreSelection();
          //     promise.resolve();
          //   }
          // });
          return false;
        }
      });
      // add the button to the default toolbar definition
      taOptions.toolbar[1].push('insertTable');
      return taOptions;
    }])

    angular.lowercase = angular.$$lowercase;

    $compileProvider
      .aHrefSanitizationWhitelist(/^\s*(https?|data|blob):/);

    $routeProvider
      .when('/', {
        templateUrl: 'views/start.html',
        controller: 'StartCtrl'
      })
      .when('/evaluation/scope', {
        templateUrl: 'views/evaluation/scope.html',
        controller: 'EvalScopeCtrl'
      })
      .when('/evaluation/explore', {
        templateUrl: 'views/evaluation/explore.html',
        controller: 'EvalExploreCtrl'
      })
      .when('/evaluation/sample', {
        templateUrl: 'views/evaluation/sample.html',
        controller: 'EvalSampleCtrl'
      })
      .when('/evaluation/audit', {
        templateUrl: 'views/evaluation/audit.html',
        controller: 'EvalAuditCtrl'
      })
      .when('/evaluation/report', {
        templateUrl: 'views/evaluation/report.html',
        controller: 'EvalReportCtrl'
      })
      .when('/view_report', {
        templateUrl: 'views/viewReport.html',
        controller: 'ViewReportCtrl'
      })
      .when('/open', {
        templateUrl: 'views/open.html',
        controller: 'OpenCtrl'
      })
      .when('/save', {
        templateUrl: 'views/save.html',
        controller: 'SaveCtrl'
      })
      .when('/error', {
        templateUrl: 'views/error.html'
      })
      .when('/import', {
        templateUrl: 'views/import.html',
        controller: 'ImportCtrl'
      })
      .otherwise({
        redirectTo: '/error'
      });

  });
