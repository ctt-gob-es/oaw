'use strict';

angular.module('wcagReporter')
  .directive('thematicSelect', function () {
    var knownThematic = [
      { title: 'THEMATIC_1', id: 'THEMATIC_1'},
      { title: 'THEMATIC_2', id: 'THEMATIC_2'},
      { title: 'THEMATIC_3', id: 'THEMATIC_3'},
      { title: 'THEMATIC_4', id: 'THEMATIC_4'},
      { title: 'THEMATIC_5', id: 'THEMATIC_5'},
      { title: 'THEMATIC_6', id: 'THEMATIC_6'},
      { title: 'THEMATIC_7', id: 'THEMATIC_7'},
      { title: 'THEMATIC_8', id: 'THEMATIC_8'},
      { title: 'THEMATIC_9', id: 'THEMATIC_9'},
      { title: 'THEMATIC_10', id: 'THEMATIC_10'},
      { title: 'THEMATIC_11', id: 'THEMATIC_11' }
    ];

    function updateThematic (reliedUponThematic, prop1, prop2) {
      knownThematic.forEach(function (thematic) {
        if (reliedUponThematic[prop1] === thematic[prop1]) {
          reliedUponThematic[prop2] = thematic[prop2];
        }
      });
    }

    return {
      restrict: 'E',
      replace: true,
      scope: { selected: '=' },
      link: function (scope) {
        	scope.thematic = knownThematic;
        	scope.updateTitle = function (select) {
        		updateThematic(select, 'specs', 'title');
        	};
        	scope.updateSpec = function (select) {
        		updateThematic(select, 'title', 'specs');
        	};
      },
      templateUrl: 'views/directives/evaluate/thematicSelect.html'
    };
  });
