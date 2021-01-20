'use strict';

angular.module('wcagReporter')
  .controller(
    'EvalReportCtrl',
    function ($scope, appState, evalReportModel,evalAuditModel) {
      $scope.state = appState.moveToState('report');
      $scope.reportModel = evalReportModel;

      // $scope.criteria = evalAuditModel.getCriteriaSorted();

      // $scope.allVerifications = function () {
      //   console.log($scope.criteria.length);


      //   for (var i = 0; i < $scope.criteria.length; i++) {
      //     for (var j = 0; j < $scope.criteria[i].hasPart.length; j++) {
      //       if($scope.criteria[i].hasPart[j].result.outcome == "earl:cantTell"
      //       || $scope.criteria[i].hasPart[j].result.outcome == "earl:untested" )
      //       return false;
      //     }
      //   }

      //   return true;
      // }

    }
  );
