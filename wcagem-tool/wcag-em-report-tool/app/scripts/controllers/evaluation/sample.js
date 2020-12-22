'use strict';

angular.module('wcagReporter')
  .controller(
    'EvalSampleCtrl',
    function (
      $scope,
      appState,
      evalExploreModel,
      evalSampleModel,
      evalAuditModel
    ) {
      $scope.state = appState.moveToState('sample');

      $scope.structuredSample = evalSampleModel.structuredSample;
      $scope.randomSample = evalSampleModel.randomSample;

      $scope.exploreModel = evalExploreModel;

      if ($scope.structuredSample &&
        $scope.structuredSample.webpage.length === 0) {
        var strPage = evalSampleModel.addNewStructuredPage();
        evalAuditModel.addPageForAsserts(strPage);

        if ($scope.randomSample &&
          $scope.randomSample.webpage.length === 0) {
          var rndPage = evalSampleModel.addNewRandomPage();
          evalAuditModel.addPageForAsserts(rndPage);
        }
      }

      $scope.getPageAdder = function (sample) {
        return function () {
          var strPage = evalSampleModel.addNewPage(sample);
          evalAuditModel.addPageForAsserts(strPage);
          var strSize = $scope.structuredSample.webpage.length;

          // Add a random page if it's one off
          var randomSampleSize = Math.ceil(strSize / 10);
          if ($scope.randomSample.webpage.length + 1 === randomSampleSize &&
            strSize % 10 === 1) {
            var rndPage = evalSampleModel
              .addNewPage($scope.randomSample);
            evalAuditModel.addPageForAsserts(rndPage);
          }

          return strPage;
        };
      };

      $scope.getPageRemover = function (sample) {
        return function (index) {
          var page = evalSampleModel.removePage(sample, index);
          evalAuditModel.removePageFromAsserts(page);
        };
      };

      $scope.getPageUpdater = function (originSample, destinySample) {
        return function (index) {
          //remove page from current sample
          var originPage = evalSampleModel.removePage(originSample, index);
          console.log("Origin page: " + originPage.id);

          //add page to destiny sample
          var destinyPage = evalSampleModel.insertPage(destinySample,originPage);
          console.log("Destiny page: " + originPage.id);

          //update audit references
          evalAuditModel.updatePageFromAsserts(originPage, destinyPage.id);
        };
      };

      $scope.randPageCount = function () {
        return Math
          .ceil($scope.structuredSample.webpage.length / 10);
      };
    }
  );
