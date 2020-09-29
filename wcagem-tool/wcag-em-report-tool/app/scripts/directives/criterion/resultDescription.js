'use strict';
angular.module('wcagReporter')
  .directive('resultDescription', function (directivePlugin,$translate, $filter) {
    return directivePlugin({
      restrict: 'E',
      replace: true,
      scope: {
        value: '=',
        updateMetadata: '='
      },
      link:function(scope){
        scope.value= $filter('translate')(scope.value);
      },
      templateUrl: 'views/directives/criterion/resultDescription.html'
    });
  });
