'use strict';

angular.module('wcagReporter')
  .controller(
    'EvalScopeCtrl',
    function (
      $scope,
      appState,
      evalScopeModel,
      evalReportModel,
      $timeout,
      $filter
    ) {
      $scope.state = appState.moveToState('scope');
      $scope.scopeModel = evalScopeModel;

      $scope.wcagVersionOptions = evalScopeModel.wcagVersionOptions
        .reduce(function (keys, key) {
          var translateKey = 'SCOPE.' + key;
          keys[key] = $filter('translate')(translateKey);
          return keys;
        }, {});

      $scope.typologyOptions = evalScopeModel.typologyOptions
        .reduce(function (keys, key) {
          var translateKey = 'SCOPE.' + key;
          keys[key] = $filter('translate')(translateKey);
          return keys;
        }, {});

      $scope.territorialScopeOptions = evalScopeModel.territorialScopeOptions
        .reduce(function (keys, key) {
          var translateKey = 'SCOPE.' + key;
          keys[key] = $filter('translate')(translateKey);
          return keys;
        }, {});

      $scope.thematicScopes = angular.copy(evalScopeModel.thematicScopes);

      $scope.changeThematic = function (thematic) {
        if (thematic.checked) {
          var newThematic = angular.extend({}, thematic);
          delete newThematic.checked;
          evalScopeModel.reliedUponThematic.push(newThematic);
        } else {
          evalScopeModel.reliedUponThematic = evalScopeModel.reliedUponThematic
            .filter(function (item) {
              return item.title !== thematic.title && item.id !== thematic.id;
            });
        }
      };

      $scope.conformanceOptions = evalScopeModel.conformanceOptions
        .reduce(function (tgt, lvl) {
          tgt[lvl] = $filter('rdfToLabel')(lvl);
          return tgt;
        }, {});

      // Give the report a default title
      // (won't if one is already set)
      $scope.$on('$routeChangeStart', function () {
        if (evalScopeModel.website.siteName) {
          var translate = $filter('translate');
          var siteName = translate('REPORT.TITLE_PREFIX') + ' ' +
            evalScopeModel.website.siteName;
          evalReportModel.setDefaultTitle(siteName);
        }
      });

      $timeout(function () {
        // set relied upon technologies in the right field
        $scope.scopeModel.reliedUponThematic
          .forEach(function (thematic) {
            var index = $scope.thematicScopes
              // Find exact matching index in thematic of reliedUponThematic
              // it will be an user defined technology otherwise
              .reduce(function (index, currThematic, currIndex) {
                if (currThematic.id === thematic.id && currThematic.title === thematic.title) {
                  return currIndex;
                }
                return index;
              }, -1);

            // Set checkboxes for known fields
            if (index !== -1) {
              $scope.thematicScopes[index].checked = true;
            }
          });
      },1000);
    }
  );