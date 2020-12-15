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
  .config(["$routeProvider", "$compileProvider", "$provide", function ($routeProvider, $compileProvider, $provide) {
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

  }]);

'use strict';

angular.module('wcagReporter')
  .value('supportedLanguages', [
    {
      code: 'es',
      localName: 'Spanish'
    },
    {
      code: 'en',
      localName: 'English'
    }
    // {
    //   code: 'nl',
    //   localName: 'Nederlands'
    // }
  ])
  .config(["$translateProvider", "wcag2specProvider", function ($translateProvider, wcag2specProvider) {
    var lang;
    function createCookie(name, value) {
      document.cookie = name + '=' + value + '; path=/';
    }

    function readCookie(name) {
      var nameEQ = name + '=';
      var ca = document.cookie.split(';');
      for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
      }
      return null;
    }

    try {
      lang = readCookie('wcagReporter-lang');
      if (!lang) {
        lang = jQuery('*[ng-app="wcagReporter"]')
          .attr('lang') || 'es';
        lang = lang.substr(0, 2);
        createCookie('wcagReporter-lang', lang);
      }
    } catch (e) {
      lang = 'es';
    }

    $translateProvider.useSanitizeValueStrategy(null);
    $translateProvider.useStaticFilesLoader({
      prefix: 'locale/',
      suffix: '.json'
    });

    wcag2specProvider.setSpecPath('wcag2spec/wcag2-${lang}.json');
    wcag2specProvider.loadLanguage(lang);
    $translateProvider.preferredLanguage(lang);
    $translateProvider.usePostCompiling(true);
  }])
  .run(["$rootScope", "$rootElement", "translateFilter", function ($rootScope, $rootElement, translateFilter) {
    $rootScope.translate = translateFilter;

    $rootElement.addClass('app-loading');
    $rootScope.$on('$translateChangeSuccess', function (e, change) {
      // Update the lang data
      $rootElement.attr('lang', change.language);
      $rootScope.lang = change.language;
      $rootElement.removeClass('app-loading');
    });
  }]);

'use strict';

angular.module('wcagReporter')
  .run(["$rootScope", "$document", "$rootElement", "evalScopeModel", function ($rootScope, $document, $rootElement, evalScopeModel) {
    var titleElm = $document.find('title');
    var prefix = titleElm.text()
      .trim();
    var routeChanged;

    if (prefix) {
      prefix = titleElm.text() + ', ';
    }

    /*
    Few workaround to make screen readers behave.
    We move the focus to the h1, but since angular has no ready
    event for route loading we've moved this to a delayed call
    in the setTile method above.

    JAWS will always read the updated title. To have NVDA comply
    we'll blur when the route changes so SR's won't try to read
    all the changes to the DOM. Then once setTitle indicates the
    view is loaded, we'll wait another half second for it to compile
    and then move focus to the h1.
     */
    $rootScope.focusH1 = function focusH1() {
      var h1 = $rootElement.find('h1:first()')
        .attr({
          tabindex: -1,
          // This is a bug workaround for NVDA + IE, which
          // seems to think our <h1> is an input field
          role: 'heading'
        });
      setTimeout(function () {
        h1.focus();
      }, 10);
    };

    $rootScope.setTitle = function (title) {
      var sitename = '';
      if (evalScopeModel.website && evalScopeModel.website.title) {
        sitename = evalScopeModel.website.title + ' - ';
      }

      titleElm.text(prefix + sitename + title);

      // Move focus to h1 when the title is set after the initial load
      if (routeChanged === true) {
        routeChanged = false;
        // Wait for the template to compile, then focus to h1
        setTimeout($rootScope.focusH1, 750);
      }
      return title;
    };

    /*
    Set the next title change up to move the focus to h1
    This little workaround is to ensure
     */
    $rootScope.$on('$routeChangeSuccess', function () {
      // Move focus, starting at the second route change
      routeChanged = (routeChanged !== undefined);

      if (document.activeElement) {
        document.activeElement.blur();
      }
    });

    // Setup root scope
  }])
  .run(["$rootScope", "appState", "$location", function ($rootScope, appState, $location) {
    $rootScope.rootHide = {};

    appState.init();

    $rootScope.setEvalLocation = function () {
      appState.setDirtyState();
      $location.path('/evaluation/scope');
    };
    $rootScope.support = {
      hasSupport: appState.hasBrowserSupport(),
      hideMessage: false
    };

    // ctrl+s || cmd+s
  }])
  .run(["$rootElement", "wcagReporterExport", "showSave", "appState", function ($rootElement, wcagReporterExport, showSave, appState) {
    if (showSave) {
      $rootElement.on('keydown', function (e) {
        if (e.keyCode === 83 && (e.ctrlKey || e.metaKey)) {
          showSave(
            wcagReporterExport.getString(),
            wcagReporterExport.getFileName(),
            'application/json'
          );

          appState.setPrestineState();
          e.preventDefault();
        }
      });
    }

    // Ensure all external links open in a new window
    var reg = new RegExp('/' + window.location.host + '/');
    $rootElement.on('click', 'a[href]:not(.local)', function (e) {
      if (!reg.test(this.href) && this.href.substr(0, 4) === 'http') {
        e.preventDefault();
        e.stopPropagation();
        window.open(this.href, '_blank');
      }
    });

    // Setup automatic import/export based on attributes of the root element
  }])
  .run(["wcagReporterImport", "wcagReporterExport", "$rootElement", function (wcagReporterImport, wcagReporterExport, $rootElement) {
    wcagReporterExport.storage.init({
      autosave: (typeof $rootElement.attr('autosave') === 'string'),
      url: $rootElement.attr('url'),
      saveDelay: ($rootElement.attr('save-delay') || 1500)
    });

    if ($rootElement.attr('url')) {
      wcagReporterImport.getFromUrl();
    }
  }]);

'use strict';

angular.module('wcagReporter')
  .value('pkgData', {
    name: '<%= pkg.name =%>',
    version: '<%= pkg.version =%>',
    buildDate: '<%= pkg.buildDate =%>'
  });

'use strict';

angular.module('wcagReporter')
  .value('toggleCriterionText', function toggleCriterionText (elm) {
    var expandState = [
      true,
      'true'
    ].indexOf(elm.getAttribute('aria-expanded')) !== -1;
    elm.setAttribute('aria-expanded', !expandState);

    var section = elm.parentNode.parentNode.nextSibling;
    section.classList.toggle('collapsed');
  });

'use strict';

/**
 * Allow developers to create plugins for a directive
 *
 * Fow now, only additional link functions are supported
 */
angular.module('wcagReporter')
  .factory('directivePlugin', function () {
    var func = function (directive) {
      var link = directive.link || function () {};

    	if (!directive.plugins) {
    		directive.plugins = [];
    	}

      directive.link = function () {
        	var args = arguments;
        	link.apply(undefined, args);

        	directive.plugins.forEach(function (plugin) {
        		if (plugin.link) {
        			plugin.link.apply(undefined, args);
        		}
        	});
      };

    	return directive;
    };
    return func;
  });

'use strict';

angular.module('wcagReporter')
  .service('changeLanguage', ["$translate", "wcag2spec", "$rootScope", function ($translate, wcag2spec, $rootScope) {
    function createCookie (name, value) {
      document.cookie = name + '=' + value + '; path=/';
    }

    return function changeLanguage (lang) {
      if ($rootScope.lang === lang) {
        return;
      }

      if (document) {
        createCookie('wcagReporter-lang', lang);
      }
      $translate.use(lang);
      wcag2spec.loadLanguage(lang);
    };
  }]);

'use strict';

angular.module('wcagReporter')
  .constant('evalContextV1', {
    '@vocab': 'http://www.w3.org/TR/WCAG-EM/#',
    wcag20: 'http://www.w3.org/TR/WCAG20/#',
    earl: 'http://www.w3.org/ns/earl#',
    dct: 'http://purl.org/dc/terms/',
    reporter: 'https://github.com/w3c/wcag-em-report-tool/blob/master/dataformat.md#',
    conformanceTarget: { id: 'step1b', type: 'id' },
    evaluationScope: { id: 'step1' },
    accessibilitySupportBaseline: { id: 'step1c' },
    additionalEvalRequirement: { id: 'step1d' },
    siteScope: { id: 'step1a' },
    commonPages: { id: 'step2a' },
    essentialFunctionality: { id: 'step2b' },
    pageTypeVariety: { id: 'step2c' },
    otherRelevantPages: { id: 'step2e' },
    structuredSample: { id: 'step3a' },
    randomSample: { id: 'step3b' },
    specifics: { id: 'step5b' },
    auditResult: { id: 'step4' },
    outcome: { type: 'id' },
    subject: { type: 'id' },
    assertedBy: { type: 'id' },
    testRequirement: { type: 'id' },
    creator: { type: 'id' },
    handle: 'reporter:handle',
    description: 'reporter:description',
    tested: 'reporter:tested',
    id: '@id',
    type: '@type',
    title: 'dct:title',
    hasPart: 'dct:hasPart',
    specs: '@id',
    reliedUponTechnology: 'wcag20:reliedupondef'
  });

'use strict';

angular.module('wcagReporter')
  .constant('evalContextV2', {
    // Current namespace
    '@vocab': 'http://www.w3.org/TR/WCAG-EM/#',

    // Namespaces
    reporter: 'https://github.com/w3c/wcag-em-report-tool/',
    wcagem: 'http://www.w3.org/TR/WCAG-EM/#',
    WCAG2: 'http://www.w3.org/TR/WCAG20/#',
    earl: 'http://www.w3.org/ns/earl#',
    dct: 'http://purl.org/dc/terms/',
    wai: 'http://www.w3.org/WAI/',
    sch: 'http://schema.org/',

    // Classes
    Evaluation: 'wcagem:Evaluation',
    EvaluationScope: 'wcagem:EvaluationScope',
    TestSubject: 'earl:TestSubject',
    WebSite: 'sch:WebSite',
    Sample: 'wcagem:Sample',
    WebPage: 'sch:WebPage',
    Technology: 'WCAG2:technologydef',
    Assertion: 'earl:Assertion',
    Assertor: 'earl:Assertor',
    TestResult: 'earl:TestResult',

    // Evaluation class properties
    title: 'dct:title',
    summary: 'dct:summary',
    creator: {
      '@id': 'dct:creator',
      '@type': '@id'
    },
    date: 'dct:date',
    commissioner: 'wcagem:commissioner',
    reliedUponTechnology: 'WCAG2:reliedupondef',
    evaluationScope: 'step1',
    commonPages: 'step2a',
    essentialFunctionality: 'step2b',
    pageTypeVariety: 'step2c',
    otherRelevantPages: 'step2e',
    structuredSample: 'step3a',
    randomSample: 'step3b',
    auditResult: 'step4',
    specifics: 'step5b',
    publisher: {
      '@id': 'dct:publisher',
      '@type': '@id'
    },

    // EvaluationScope class properties
    conformanceTarget: {
      '@id': 'step1b',
      '@type': '@id'
    },
    accessibilitySupportBaseline: 'step1c',
    additionalEvalRequirement: 'step1d',
    website: 'WCAG2:set-of-web-pagesdef',

    // sch:WebSite class properties
    siteScope: 'step1a',
    siteName: 'sch:name',

    // Sample class properties
    webpage: 'WCAG2:webpagedef',

    // sch:WebPage class properties
    description: 'dct:description',
    // 'title': 'dct:title',  -- As in the Evaluation class
    source: {
      '@id': 'dct:source',
      '@type': '@id'
    },
    tested: 'reporter:blob/master/docs/EARL%2BJSON-LD.md#tested',

    // WCAG2:technologydef class properties
    // 'title': 'dct:title',  -- As in the Evaluation class

    // earl:Assertion class properties
    test: {
      '@id': 'earl:test',
      '@type': '@id'
    },
    assertedBy: {
      '@id': 'earl:assertedBy',
      '@type': '@id'
    },
    subject: {
      '@id': 'earl:subject',
      '@type': '@id'
    },
    result: 'earl:result',
    mode: {
      '@id': 'earl:mode',
      '@type': '@id'
    },
    hasPart: 'dct:hasPart',

    // earl:TestResult class properties
    // 'description': 'dct:description',  -- As in the sch:WebPage class
    outcome: {
      '@id': 'earl:outcome',
      '@type': '@id'
    },

    // shorthand, because @ can't be used in dot notation
    id: '@id',
    type: '@type',
    lang: '@language'
  });

'use strict';

angular
  .module('wcagReporter')
  .constant('evalContextV3', {
    // Current namespace
    '@vocab': 'http://www.w3.org/TR/WCAG-EM/#',

    // Namespaces
    reporter: 'https://github.com/w3c/wcag-em-report-tool/',
    wcagem: 'http://www.w3.org/TR/WCAG-EM/#',
    WCAG2: 'http://www.w3.org/TR/WCAG21/#',
    earl: 'http://www.w3.org/ns/earl#',
    dct: 'http://purl.org/dc/terms/',
    wai: 'http://www.w3.org/WAI/',
    sch: 'http://schema.org/',

    // Classes
    Evaluation: 'wcagem:Evaluation',
    EvaluationScope: 'wcagem:EvaluationScope',
    TestSubject: 'earl:TestSubject',
    WebSite: 'sch:WebSite',
    Sample: 'wcagem:Sample',
    WebPage: 'sch:WebPage',
    Technology: 'WCAG2:dfn-technologies',
    Assertion: 'earl:Assertion',
    Assertor: 'earl:Assertor',
    TestResult: 'earl:TestResult',

    // Evaluation class properties
    title: 'dct:title',
    summary: 'dct:summary',
    creator: {
      '@id': 'dct:creator',
      '@type': '@id'
    },
    date: 'dct:date',
    commissioner: 'wcagem:commissioner',
    reliedUponTechnology: 'WCAG2:dfn-relied-upon',
    evaluationScope: 'step1',
    commonPages: 'step2a',
    essentialFunctionality: 'step2b',
    pageTypeVariety: 'step2c',
    otherRelevantPages: 'step2e',
    structuredSample: 'step3a',
    randomSample: 'step3b',
    auditResult: 'step4',
    specifics: 'step5b',
    publisher: {
      '@id': 'dct:publisher',
      '@type': '@id'
    },

    // EvaluationScope class properties
    conformanceTarget: {
      '@id': 'step1b',
      '@type': '@id'
    },
    accessibilitySupportBaseline: 'step1c',
    additionalEvalRequirement: 'step1d',
    website: 'WCAG2:dfn-set-of-web-pages',

    // sch:WebSite class properties
    siteScope: 'step1a',
    siteName: 'sch:name',

    // Sample class properties
    // sch:WebPage class properties
    webpage: 'WCAG2:dfn-web-page-s',
    description: 'dct:description',
    source: {
      '@id': 'dct:source',
      '@type': '@id'
    },
    tested: 'reporter:blob/master/docs/EARL%2BJSON-LD.md#tested',

    // earl:Assertion class properties
    test: {
      '@id': 'earl:test',
      '@type': '@id'
    },
    assertedBy: {
      '@id': 'earl:assertedBy',
      '@type': '@id'
    },
    subject: {
      '@id': 'earl:subject',
      '@type': '@id'
    },
    result: 'earl:result',
    mode: {
      '@id': 'earl:mode',
      '@type': '@id'
    },
    hasPart: 'dct:hasPart',

    // earl:TestResult class properties
    outcome: {
      '@id': 'earl:outcome',
      '@type': '@id'
    },

    // shorthand, because @ can't be used in dot notation
    id: '@id',
    type: '@type',
    lang: '@language'
  });

'use strict';

angular.module('wcagReporter')
  .constant('knownTech', [
    { title: 'HTML5', id: 'http://www.w3.org/TR/html5/', type: 'Technology' },
    { title: 'XHTML 1.0', id: 'http://www.w3.org/TR/xhtml1/', type: 'Technology' },
    { title: 'HTML 4.01', id: 'http://www.w3.org/TR/html401/', type: 'Technology' },
    { title: 'CSS', id: 'http://www.w3.org/Style/CSS/specs/', type: 'Technology' },
    { title: 'WAI-ARIA', id: 'http://www.w3.org/TR/wai-aria/', type: 'Technology' },
    { title: 'ECMAScript 3', id: 'http://www.ecma-international.org/publications/standards/Ecma-327.htm', type: 'Technology' },
    { title: 'ECMAScript 5', id: 'http://www.ecma-international.org/publications/standards/Ecma-262.htm', type: 'Technology' },
    { title: 'DOM', id: 'http://www.w3.org/DOM/', type: 'Technology' },
    { title: 'Flash', id: 'http://get.adobe.com/nl/flashplayer/', type: 'Technology' },
    { title: 'Silverlight', id: 'http://www.microsoft.com/silverlight/', type: 'Technology' },
    { title: 'OOXML', id: 'http://www.ecma-international.org/publications/standards/Ecma-376.htm', type: 'Technology' },
    { title: 'ODF 1.2', id: 'https://www.oasis-open.org/standards#opendocumentv1.2', type: 'Technology' },
    { title: 'SVG', id: 'http://www.w3.org/TR/SVG/', type: 'Technology' }
  ]);

'use strict';

angular.module('wcagReporter')
  .service('reportStorage', ["$http", "$rootScope", "appState", "$timeout", function ($http, $rootScope, appState, $timeout) {
    var reportStorage; var autosave; var loading;
    var failedSaveAttempts = 0;
    var settings = {
      url: undefined,
      revisionId: undefined,
      autosave: false,
      saveDelay: 3000
    };

    function startAutosave () {
      if (!settings.url) {
        settings.autosave = false;
        return;
      }
      autosave = $timeout(function autosaveFunc () {
        reportStorage.exportModel.saveToUrl()
          .then(function () {
            failedSaveAttempts = 0;
          }, function () {
            failedSaveAttempts += 1;
            if (settings.autosave) {
              autosave = $timeout(
                autosaveFunc,
                settings.saveDelay * failedSaveAttempts,
                0,
                false
              );
            }
          });
      }, settings.saveDelay, 0, false);
    }

    $rootScope.$on('appstate:prestine', function () {
      if (autosave) {
        $timeout.cancel(autosave);
        autosave = undefined;
      }
    });

    $rootScope.$on('appstate:dirty', function () {
      if (settings.autosave && autosave === undefined) {
        startAutosave();
      }
    });

    reportStorage = {
      settings: settings,

      exportModel: undefined,

      init: function (obj) {
        reportStorage.updateSettings(obj);
      },

      updateSettings: function (obj) {
        if (obj) {
          angular.extend(settings, obj);
        }

        if (settings.autosave && loading) {
          loading.then(startAutosave);
        } else if (settings.autosave) {
          startAutosave();
        }
      },

      post: function (json) {
        if (settings.revisionId) {
          json._rev = settings.revisionId;
        }
        return $http.put(settings.url, json, {}, {
          withCredentials: true
        })
          .then(function (response) {
            if (response && response.data && response.data.rev) {
              settings.revisionId = response.data.rev;
            }
            appState.setPrestineState();
            return response.data;
          });
      },

      get: function () {
        $timeout.cancel(autosave);
        loading = $http.get(settings.url, {}, {
          withCredentials: true
        })
          .then(function (response) {
            if (response.data._rev) {
              settings.revisionId = response.data._rev;
            }
            appState.setPrestineState();
            return response.data;
          });
        return loading;
      }
    };

    return reportStorage;
  }]);

'use strict';

angular.module('wcagReporter')
  .service('evalLoader', ["evalWindow", "appState", "fileReader", "wcagReporterImport", "$q", function (evalWindow, appState, fileReader, wcagReporterImport, $q) {
    function loadFactory (promiseGen) {
      var importTarget;

      return function () {
        var promise = promiseGen.apply(null, arguments);
        var defer = $q.defer();

        function reject (e) {
          defer.reject(e);
          if (importTarget) {
            importTarget.abort();
          }
        }

        if (!appState.empty) {
          try {
            importTarget = evalWindow.openEmptyWindow();
          } catch (e) {
            reject('Popup blocker detected. Please allow popups so the evaluation can open in a new window.');
          }
        } else {
          importTarget = evalWindow;
        }

        promise.then(function (data) {
          try {
            importTarget.loadJson(data);
            appState.setDirtyState();
            defer.resolve();
          } catch (e) {
            reject(e);
          }
        }, reject);

        return defer.promise;
      };
    }

    return {
      openFromFile: loadFactory(function (file) {
        return fileReader.readAsText(file);
      }),

      openFromUrl: loadFactory(function () {
        return wcagReporterImport.getFromUrl();
      })
    };
  }]);

'use strict';

angular.module('wcagReporter')
  .service('evalWindow', ["$http", "wcagReporterImport", "$rootScope", function ($http, wcagReporterImport, $rootScope) {
    var curWindow;
    var waitingForEvaluation = 'waitingForEvaluation';
    var loadEvaluationData = 'loadEvaluationData';
    var evaluateDataWhenReady = 'evaluateDataWhenReady';

    function EvalWindow () {}

    EvalWindow.prototype = {
      openEmptyWindow: function () {
        var newRunner = new EvalWindow();
        var newWindow = window.open(window.location.href, '_blank');

        newWindow[waitingForEvaluation] = true;

        // open a window
        newRunner.abort = function () {
          newWindow.close();
        };

        newRunner.loadJson = function (data) {
          if (newWindow[loadEvaluationData]) {
            newWindow[loadEvaluationData](data);
          } else {
            newWindow[evaluateDataWhenReady] = data;
          }
          // Tell the new window where to look
        };

        return newRunner;
      },

      loadJson: function (data) {
        wcagReporterImport.fromJson(data);
      },

      abort: angular.noop
    };

    function processPageData (data) {
    	curWindow.loadJson(data);
      $rootScope.setEvalLocation();
    }

    curWindow = new EvalWindow();

    if (window[waitingForEvaluation]) {
      window[waitingForEvaluation] = undefined;

      if (window[evaluateDataWhenReady]) {
        processPageData(window[evaluateDataWhenReady]);
    	} else {
    		window[loadEvaluationData] = processPageData;
    	}
    }

    return curWindow;
  }]);

/**
  * Original module by K. Scott Allen
 * http://odetocode.com/blogs/scott/archive/2013/07/03/building-a-filereader-service-for-angularjs-the-service.aspx
 */
(function (module) {
  'use strict';

  var fileReader = function ($q, $rootScope) {
    var onLoad = function (reader, deferred, scope) {
      return function () {
        scope.$apply(function () {
          deferred.resolve(reader.result);
        });
      };
    };

    var onError = function (reader, deferred, scope) {
      return function () {
        scope.$apply(function () {
          deferred.reject(reader.result);
        });
      };
    };

    var onProgress = function (reader, scope) {
      return function (event) {
        scope.$broadcast(
          'fileProgress',
          {
            total: event.total,
            loaded: event.loaded
          }
        );
      };
    };

    var getReader = function (deferred, scope) {
      var reader = new FileReader();
      reader.onload = onLoad(reader, deferred, scope);
      reader.onerror = onError(reader, deferred, scope);
      reader.onprogress = onProgress(reader, scope);
      return reader;
    };

    var readAsText = function (file, scope) {
      scope = scope || $rootScope;
      var deferred = $q.defer();
      var reader = getReader(deferred, scope);

      scope.$evalAsync(function () {
        try {
          reader.readAsText(file);
        } catch (e) {
          deferred.reject(e);
        }
      });

      return deferred.promise;
    };

    var readAsDataURL = function (file, scope) {
      scope = scope || $rootScope;
      var deferred = $q.defer();
      var reader = getReader(deferred, scope);

      scope.$evalAsync(function () {
        try {
          reader.readAsDataURL(file);
        } catch (e) {
          deferred.reject(e);
        }
      });

      return deferred.promise;
    };

    return {
      readAsDataUrl: readAsDataURL,
      readAsText: readAsText
    };
  };

  module.factory('fileReader', [
    '$q',
    '$rootScope',
    fileReader
  ]);
}(angular.module('wcagReporter')));

'use strict';

/**
 *  Source: http://hackworthy.blogspot.nl/2012/05/savedownload-data-generated-in.html
 *  By: Mathias Panzenböck
 *  A small example using the sowSave function:
 *  function saveData () {
 *      if (!showSave) {
 *          alert("Your browser does not support any method of saving JavaScript gnerated data to files.");
 *          return;
 *      }
 *
 *      showSave(
 *          document.getElementById("data").value,
 *          document.getElementById("filename").value,
 *          document.getElementById("mimeType").value);
 *  }
 */
angular.module('wcagReporter')
  .service('showSave', function () {
    var showSave;
    var octetStream = 'application/octet-stream';

    // Feature test: Does this browser support the download attribute on anchor tags? (currently only Chrome)
    var DownloadAttributeSupport = 'download' in document.createElement('a');

    // Use any available BlobBuilder/URL implementation:
    var Blob = window.Blob;
    var URL = window.URL || window.webkitURL || window.mozURL || window.msURL;

    // IE 10 has a handy navigator.msSaveBlob method. Maybe other browsers will emulate that interface?
    // See: http://msdn.microsoft.com/en-us/library/windows/apps/hh441122.aspx
    navigator.saveBlob = navigator.saveBlob || navigator.msSaveBlob || navigator.mozSaveBlob || navigator.webkitSaveBlob;

    // Anyway, HMTL5 defines a very similar but more powerful window.saveAs function:
    // http://www.w3.org/TR/file-writer-api/#the-filesaver-interface
    window.saveAs = window.saveAs || window.webkitSaveAs || window.mozSaveAs || window.msSaveAs;
    // However, this is not supported by any browser yet. But there is a compatibility library that
    // adds this function to browsers that support Blobs (except Internet Exlorer):
    // http://eligrey.com/blog/post/saving-generated-files-on-the-client-side
    // https://github.com/eligrey/FileSaver.js

    // mime types that (potentially) don't trigger a download when opened in a browser:
    var BrowserSupportedmimeTypes = {
      'image/jpeg': true,
      'image/png': true,
      'image/gif': true,
      'image/svg+xml': true,
      'image/bmp': true,
      'image/x-windows-bmp': true,
      'image/webp': true,
      'audio/wav': true,
      'audio/mpeg': true,
      'audio/webm': true,
      'audio/ogg': true,
      'video/mpeg': true,
      'video/webm': true,
      'video/ogg': true,
      'text/plain': true,
      'text/html': true,
      'text/xml': true,
      'application/xhtml+xml': true,
      'application/json': true
    };

    // Blobs and saveAs (or saveBlob)   :
    if (Blob && (window.saveAs || navigator.saveBlob)) {
      // Currently only IE 10 supports this, but I hope other browsers will also implement the saveAs/saveBlob method eventually.
      showSave = function (data, name, mimeType) {
        var blob = new Blob([data], { type: mimeType || octetStream });
        name = name || 'Download.bin';

        // I don't assign saveAs to navigator.saveBlob (or the other way around)
        // because I cannot know at this point whether future implementations
        // require these methods to be called with 'this' assigned to window (or
        // naviagator) in order to work. E.g. console.log won't work when not called
        // with this === console.
        if (window.saveAs) {
          window.saveAs(blob, name);
        } else {
          navigator.saveBlob(blob, name);
        }
      };
    }

    // Blobs and object URLs:
    else if (Blob && URL) {
      // Currently WebKit and Gecko support BlobBuilder and object URLs.
      showSave = function (data, name, mimeType) {
        var url, blob;

        if (DownloadAttributeSupport) {
          blob = new Blob([data], { type: mimeType || octetStream });
          url = URL.createObjectURL(blob);
          // Currently only Chrome (since 14-dot-something) supports the download attribute for anchor elements.
          var link = document.createElement('a');
          link.setAttribute('href', url);
          link.setAttribute('download', name || 'Download.bin');
          // Now I need to simulate a click on the link.
          // IE 10 has the better msSaveBlob method and older IE versions do not support the BlobBuilder interface
          // and object URLs, so we don't need the MS way to build an event object here.
          var event = document.createEvent('MouseEvents');
          event.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
          link.dispatchEvent(event);
        } else {
          // In other browsers I open a new window with the object URL.
          // In order to trigger a download I have to use the generic binary data mime type
          // "application/octet-stream" for mime types that browsers would display otherwise.
          // Of course the browser won't show a nice file name here.
          if (BrowserSupportedmimeTypes[mimeType.split(';')[0]] === true) {
            mimeType = octetStream;
          }

          blob = new Blob([data], { type: mimeType || octetStream });
          url = URL.createObjectURL(blob);
          window.open(url, '_blank', '');
        }
        // The timeout is probably not necessary, but just in case that some browser handle the click/window.open
        // asynchronously I don't revoke the object URL immediately.
        setTimeout(function () {
          URL.revokeObjectURL(url);
        }, 250);

        // Using the filesystem API (http://www.w3.org/TR/file-system-api/) you could do something very similar.
        // However, I think this is only supported by Chrome right now and it is much more complicated than this
        // solution. And chrome supports the download attribute anyway.
      };
    }

    // data:-URLs:
    else if (!/\bMSIE\b/.test(navigator.userAgent)) {
      // IE does not support URLs longer than 2048 characters (actually bytes), so it is useless for data:-URLs.
      // Also it seems not to support window.open in combination with data:-URLs at all.
      showSave = function (data, name, mimeType) {
        if (!mimeType) {
          mimeType = octetStream;
        }
        // Again I need to filter the mime type so a download is forced.
        if (BrowserSupportedmimeTypes[mimeType.split(';')[0]] === true) {
          mimeType = octetStream;
        }

        // Note that encodeURIComponent produces UTF-8 encoded text. The mime type should contain
        // the charset=UTF-8 parameter. In case you don't want the data to be encoded as UTF-8
        // you could use escape(data) instead.
        window.open('data:' + mimeType + ',' + encodeURIComponent(data), '_blank', '');
      };
    }

    // Internet Explorer before version 10 does not support any of the methods above.
    // If it is text data you could show it in an textarea and tell the user to copy it into a text file.

    return showSave;
  });

'use strict';

angular
  .module('wcagReporter')
  /**
   * wcagSpecIdMapping
   * ---
   * Necessary version management for WCAG#IDs
   * Contains a 2-dimensional array, call it a tupple,
   * of successcriterion id versions.
   * Starting on index 0 with first id version WCAG 2.0
   * Callable like wcagSpecIdMap[SC][IDVersion].
   *
   * Current versions: [
   *  [0] => WCAG2.0,
   *  [1] => WCAG2.1
   * ]
   *
   * An empty ID version means the criterion did not exist for that version,
   * e.g.: ['', 'orientation'] means the criterion id orientation
   * was added since WCAG version 2.1
   * and ['text-equiv-all', 'non-text-content'] means the criterion id changed over time.
   * ---
   * @return Array[Array[...scId]]
   */
  .constant('wcagSpecIdMap', [
    // ids here grouped sc per array
    // From first to newest specification like: 2.0, 2.1, 2.2, 3.0?
    [
      'text-equiv-all',
      'non-text-content'
    ],
    [
      'media-equiv-av-only-alt',
      'audio-only-and-video-only-prerecorded'
    ],
    [
      'media-equiv-captions',
      'captions-prerecorded'
    ],
    [
      'media-equiv-audio-desc',
      'audio-description-or-media-alternative-prerecorded'
    ],
    [
      'media-equiv-real-time-captions',
      'captions-live'
    ],
    [
      'media-equiv-audio-desc-only',
      'audio-description-prerecorded'
    ],
    [
      'media-equiv-sign',
      'sign-language-prerecorded'
    ],
    [
      'media-equiv-extended-ad',
      'extended-audio-description-prerecorded'
    ],
    [
      'media-equiv-text-doc',
      'media-alternative-prerecorded'
    ],
    [
      'media-equiv-live-audio-only',
      'audio-only-live'
    ],
    [
      'content-structure-separation-programmatic',
      'info-and-relationships'
    ],
    [
      'content-structure-separation-sequence',
      'meaningful-sequence'
    ],
    [
      'content-structure-separation-understanding',
      'sensory-characteristics'
    ],
    [
      '',
      'orientation'
    ],
    [
      '',
      'identify-input-purpose'
    ],
    [
      '',
      'identify-purpose'
    ],
    [
      'visual-audio-contrast-without-color',
      'use-of-color'
    ],
    [
      'visual-audio-contrast-dis-audio',
      'audio-control'
    ],
    [
      'visual-audio-contrast-contrast',
      'contrast-minimum'
    ],
    [
      'visual-audio-contrast-scale',
      'resize-text'
    ],
    [
      'visual-audio-contrast-text-presentation',
      'images-of-text'
    ],
    [
      'visual-audio-contrast7',
      'contrast-enhanced'
    ],
    [
      'visual-audio-contrast-noaudio',
      'low-or-no-background-audio'
    ],
    [
      'visual-audio-contrast-visual-presentation',
      'visual-presentation'
    ],
    [
      'visual-audio-contrast-text-images',
      'images-of-text-no-exception'
    ],
    [
      '',
      'reflow'
    ],
    [
      '',
      'non-text-contrast'
    ],
    [
      '',
      'text-spacing'
    ],
    [
      '',
      'content-on-hover-or-focus'
    ],
    [
      'keyboard-operation-keyboard-operable',
      'keyboard'
    ],
    [
      'keyboard-operation-trapping',
      'no-keyboard-trap'
    ],
    [
      'keyboard-operation-all-funcs',
      'keyboard-no-exception'
    ],
    [
      '',
      'character-key-shortcuts'
    ],
    [
      'time-limits-required-behaviors',
      'timing-adjustable'
    ],
    [
      'time-limits-pause',
      'pause-stop-hide'
    ],
    [
      'time-limits-no-exceptions',
      'no-timing'
    ],
    [
      'time-limits-postponed',
      'interruptions'
    ],
    [
      'time-limits-server-timeout',
      're-authenticating'
    ],
    [
      '',
      'timeouts'
    ],
    [
      'seizure-does-not-violate',
      'three-flashes-or-below-threshold'
    ],
    [
      'seizure-three-times',
      'three-flashes'
    ],
    [
      '',
      'animation-from-interactions'
    ],
    [
      'navigation-mechanisms-skip',
      'bypass-blocks'
    ],
    [
      'navigation-mechanisms-title',
      'page-titled'
    ],
    [
      'navigation-mechanisms-focus-order',
      'focus-order'
    ],
    [
      'navigation-mechanisms-refs',
      'link-purpose-in-context'
    ],
    [
      'navigation-mechanisms-mult-loc',
      'multiple-ways'
    ],
    [
      'navigation-mechanisms-descriptive',
      'headings-and-labels'
    ],
    [
      'navigation-mechanisms-focus-visible',
      'focus-visible'
    ],
    [
      'navigation-mechanisms-location',
      'location'
    ],
    [
      'navigation-mechanisms-link',
      'link-purpose-link-only'
    ],
    [
      'navigation-mechanisms-headings',
      'section-headings'
    ],
    [
      '',
      'pointer-gestures'
    ],
    [
      '',
      'pointer-cancellation'
    ],
    [
      '',
      'label-in-name'
    ],
    [
      '',
      'motion-actuation'
    ],
    [
      '',
      'target-size'
    ],
    [
      '',
      'concurrent-input-mechanisms'
    ],
    [
      'meaning-doc-lang-id',
      'language-of-page'
    ],
    [
      'meaning-other-lang-id',
      'language-of-parts'
    ],
    [
      'meaning-idioms',
      'unusual-words'
    ],
    [
      'meaning-located',
      'abbreviations'
    ],
    [
      'meaning-supplements',
      'reading-level'
    ],
    [
      'meaning-pronunciation',
      'pronunciation'
    ],
    [
      'consistent-behavior-receive-focus',
      'on-focus'
    ],
    [
      'consistent-behavior-unpredictable-change',
      'on-input'
    ],
    [
      'consistent-behavior-consistent-locations',
      'consistent-navigation'
    ],
    [
      'consistent-behavior-consistent-functionality',
      'consistent-identification'
    ],
    [
      'consistent-behavior-no-extreme-changes-context',
      'change-on-request'
    ],
    [
      'minimize-error-identified',
      'error-identification'
    ],
    [
      'minimize-error-cues',
      'labels-or-instructions'
    ],
    [
      'minimize-error-suggestions',
      'error-suggestion'
    ],
    [
      'minimize-error-reversible',
      'error-prevention-legal-financial-data'
    ],
    [
      'minimize-error-context-help',
      'help'
    ],
    [
      'minimize-error-reversible-all',
      'error-prevention-all'
    ],
    [
      'ensure-compat-parses',
      'parsing'
    ],
    [
      'ensure-compat-rsv',
      'name-role-value'
    ],
    [
      '',
      'status-messages'
    ]
  ]);

'use strict';

angular
  .module('wcagReporter')
  .constant('types', {
    EARL: {
      type: 'earl',
      MODE: {
        type: 'earl:TestMode',
        MANUAL: 'earl:manual'
      },
      OUTCOME: {
        type: 'earl:OutcomeValue',
        CANNOT_TELL: 'earl:CannotTell',
        CANT_TELL: 'earl:cantTell',
        INAPPLICABLE: 'earl:inapplicable',
        FAIL: 'earl:Fail',
        FAILED: 'earl:failed',
        NOT_APPLICABLE: 'earl:NotApplicable',
        NOT_TESTED: 'earl:NotTested',
        PASS: 'earl:Pass',
        PASSED: 'earl:passed',
        UNTESTED: 'earl:untested'
      },
      RESULT: {
        class: 'TestResult',
        type: 'earl:TestResult'
      }
    }
  });

angular
  .module('wcagReporter')
  .service('isObjectLiteral', function () {
    /**
     * isObjectLiteral tests if parameter is an object literal like:
     * {
     *  key1: value1,
     *  …,
     *  keyN: valueN
     * }
     * @param  {any}  test [parameter to test]
     * @return {Boolean}
     */
    function isObjectLiteral (test) {
      if (
        typeof test === 'object' &&
        Object.prototype.toString.call(test) === '[object Object]'
      ) {
        return true;
      }

      return false;
    }

    return isObjectLiteral;
  });

'use strict';
angular.module('wcagReporter')
  .directive('criterionBody', ["directivePlugin", "evalSampleModel", "selectedCasesOnlyFilter", function (directivePlugin, evalSampleModel, selectedCasesOnlyFilter) {
    function singlePageAssert (scope) {
      var pages = evalSampleModel.getFilledPages();
      var asserts = scope.criterion.getSinglePageAsserts();

      if (scope.opt.editable) {
        return selectedCasesOnlyFilter(asserts)
          .sort(function (assertA, assertB) {
            return pages.indexOf(assertA.subject[0]) - pages.indexOf(assertB.subject[0]);
          });
      } else {
        return asserts.filter(function (assert) {
          return assert.isDefined();
        })
          .sort(function (assertA, assertB) {
            return pages.indexOf(assertA.subject[0]) - pages.indexOf(assertB.subject[0]);
          });
      }
    }

    return directivePlugin({
      restrict: 'E',
      replace: true,
      transclude: true,
      scope: {
        criterion: '=assert',
        opt: '=options'
      },

      controller: [
        '$scope',
        function ($scope) {
          if ($scope.opt.editable) {
            $scope.criterion.setCaseForEachPage();
          }

          $scope.$on('audit:sample-change', function () {
            $scope.multiPageAsserts = $scope.criterion.getMultiPageAsserts();
            $scope.singlePageAsserts = singlePageAssert($scope);
          });
        }
      ],

      link: function (scope) {
        scope.showBody = function () {
          return scope.multiPageAsserts.length > 0 ||
                        scope.singlePageAsserts.length > 0 ||
                        scope.opt.editable;
        };

        scope.multiPageAsserts = scope.criterion.getMultiPageAsserts();
        scope.singlePageAsserts = singlePageAssert(scope);

        scope.hasMultipage = false;
        scope.addMultiPage = function () {
          scope.criterion.addTestCaseAssertion({
            multiPage: true,
            subject: evalSampleModel.getSelectedPages()
          });
          scope.multiPageAsserts = scope.criterion.getMultiPageAsserts();
        };
      },
      templateUrl: 'views/directives/criterion/criterionBody.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive(
    'earlAssert',
    ["$filter", "directivePlugin", "CriterionAssert", "$rootScope", function ($filter, directivePlugin, CriterionAssert, $rootScope) {
      function getOutcomes() {
        return [
          'earl:untested',
          'earl:passed',
          'earl:failed',
          'earl:inapplicable',
          'earl:cantTell'
        ]
          .map(function (rdfId) {
            return {
              id: rdfId,
              name: $filter('rdfToLabel')(rdfId)
            };
          });
      }

      return directivePlugin({
        restrict: 'E',
        replace: true,
        transclude: true,
        scope: {
          opt: '=options',
          assert: '='
        },
        link: function (scope) {
          scope.result = scope.assert.result;
          scope.outcomes = getOutcomes();
          scope.updateMetadata = function () {
            CriterionAssert.updateMetadata(scope.assert);
          };

          scope.getStaticHtmlResult = function (text) {
            text = ('' + text).trim() || '–';
            return '<p><strong>' + $filter('translate')('HTML_REPORT.LABEL_DESCR') + ':</strong> ' +
              $filter('txtToHtml')(text)
                .substr(3);
          };
          scope.htmlResult = scope.getStaticHtmlResult(scope.result.description);
        },
        templateUrl: 'views/directives/criterion/earlAssert.html'
      });
    }]
  );

'use strict';
angular.module('wcagReporter')
  .directive('macroResults', ["directivePlugin", function (directivePlugin) {
    return directivePlugin({
      restrict: 'E',
      replace: true,
      scope: {
        criterion: '=value',
        asserts: '=',
        opt: '=options'
      },

      link: function (scope) {
        scope.removeAssert = function (assert) {
          var index = scope.criterion.hasPart.indexOf(assert);
          if (index >= 0) {
            scope.criterion.hasPart.splice(index, 1);
          }
          index = scope.asserts.indexOf(assert);
          if (index >= 0) {
            scope.asserts.splice(index, 1);
          }
        };

        scope.transferMacroData = function (macroAssert) {
          // Get all single page asserts where a tag is part of this macro assert
          scope.criterion.transferMacroData(macroAssert);
          scope.removeAssert(macroAssert);
        };

        scope.getAllTitles = function (assert) {
          return assert.subject.map(function (page) {
            return page.displayTitle();
          })
            .join(', ');
        };
      },
      templateUrl: 'views/directives/criterion/macroResults.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('pageResults', ["directivePlugin", function (directivePlugin) {
    return directivePlugin({
      restrict: 'E',
      replace: true,
      scope: {
        criterion: '=value',
        asserts: '=',
        opt: '=options'
      },
      link: function (scope) {
        scope.createMacro = function (assert) {
          scope.criterion.addTestCaseAssertion({
            result: {
              description: assert.result.description,
              outcome: assert.result.outcome
            },
            multiPage: true
          });
        };
      },
      templateUrl: 'views/directives/criterion/pageResults.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('pageSelect', ["directivePlugin", "evalSampleModel", function (directivePlugin, evalSampleModel) {
    var sample;
    return directivePlugin({
      restrict: 'E',
      replace: true,
      scope: {
        pages: '='
      },
      controller: [
        '$scope',
        function ($scope) {
          $scope.updateSampleList = function () {
            $scope.unselectedPages = sample.filter(function (page) {
              return $scope.pages.indexOf(page) === -1;
            });
          };

          $scope.removePage = function (page) {
            var index = $scope.pages.indexOf(page);
            if (index >= 0) {
              $scope.pages.splice(index, 1);
              $scope.updateSampleList();
            }
          };

          $scope.addPageToAssert = function () {
            var page = evalSampleModel.getPageByTitle($scope.newPage);

            if (page && $scope.pages.indexOf(page) === -1) {
              $scope.unselectedPages.splice($scope.unselectedPages.indexOf(page), 1);
              $scope.pages.push(page);
              $scope.newPage = '';
            }
          };

          sample = evalSampleModel.getPages();
          $scope.updateSampleList();
        }
      ],
      templateUrl: 'views/directives/criterion/pageSelect.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('resultDescription', ["directivePlugin", "$translate", "$filter", function (directivePlugin,$translate, $filter) {
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
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('infoButton', ["directivePlugin", function (directivePlugin) {
    return directivePlugin({
      restrict: 'E',
      scope: {
        label: '@',
        target: '@'
      },
      link: function (scope, elm) {
        var tgt;

        elm.on('click', function () {
          if (!tgt) {
            if (typeof scope.target === 'undefined') {
              tgt = elm.next();
            } else {
              tgt = angular.element('#' + scope.target);
            }
            tgt.find('.close')
              .on(
                'click',
                elm.attr.bind(elm, 'aria-expanded', false)
              );
          }
          tgt.toggle(200, function () {
            tgt.focus();
            elm.attr('aria-expanded', tgt.is(':visible'));
          });
        });
      },
      replace: true,
      templateUrl: 'views/directives/evaluate/infoButton.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('infoField', ["directivePlugin", "$document", function (directivePlugin, $document) {
    /**
     * Get the next visible element that can receive focus outside the .alert-info
     */
    function getNextFocusElement (elm) {
      var selector = ':input:visible, a[href]:visible';
      var focusable = $document.find(selector);

      elm = angular.element(elm)
        .closest('.alert-info')
        .find(selector)
        .last();
      return focusable[focusable.index(elm) + 1];
    }

    return directivePlugin({
      restrict: 'E',
      scope: {
        ref: '@',
        button: '@',
        exitto: '@'
      },
      link: function (scope, elm) {
        elm.hide(0);
        scope.close = function ($event) {
          var nextElm;

          if ($event.type === 'keyup' &&
                ($event.keyCode !== 13 && $event.keyCode !== 27)) {
            return;
          }
          if (scope.exitto) {
            nextElm = angular.element('#' + scope.exitto);
          }
          if (!nextElm || nextElm.length === 0) {
            nextElm = getNextFocusElement($event.target);
          }
          nextElm.focus();
          elm.hide(200);
        };
      },
      replace: true,
      transclude: true,
      templateUrl: 'views/directives/evaluate/infoField.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('inputPages', ["directivePlugin", "$timeout", "Page", function (directivePlugin, $timeout, Page) {
    return directivePlugin({
      restrict: 'E',
      replace: true,
      scope: {
        pages: '=',
        addPage: '&',
        removePage: '&'
      },
      link: function (scope) {
        var addPageFunc = scope.addPage();
        var removePageFunc = scope.removePage();
        scope.addPage = function ($event) {
          var button = angular.element($event.delegateTarget);
          addPageFunc();

          $timeout(function () {
            var inputs = button.prev()
              .find('input');
            inputs[inputs.length - 2].select();
          }, 100);
        };

        scope.processPage = function (page) {
          Page.prependProtocol(page);
          Page.updateSource(page);
        };

        scope.removePage = function ($index, $event) {
          removePageFunc($index);
          // We need this timeout to prevent Angular UI from throwing an error
          $timeout(function () {
            angular.element($event.delegateTarget)
              .closest('fieldset')
              .parent()
              .children()
              .last()
              .focus();
          });
        };
      },
      templateUrl: 'views/directives/evaluate/inputPages.html'
    });
  }]);

'use strict';

angular.module('wcagReporter')
  .directive('techSelect', function () {
    var knownTechnologies = [
      { title: 'HTML5', specs: 'http://www.w3.org/TR/html5/' },
      { title: 'CSS', specs: 'http://www.w3.org/Style/CSS/specs/' },
      { title: 'HTML 4.01', specs: 'http://www.w3.org/TR/html401/' }
    ];

    function updateTech (reliedUponTech, prop1, prop2) {
      knownTechnologies.forEach(function (tech) {
        if (reliedUponTech[prop1] === tech[prop1]) {
          reliedUponTech[prop2] = tech[prop2];
        }
      });
    }

    return {
      restrict: 'E',
      replace: true,
      scope: { selected: '=' },
      link: function (scope) {
        	scope.technolgies = knownTechnologies;
        	scope.updateTitle = function (select) {
        		updateTech(select, 'specs', 'title');
        	};
        	scope.updateSpec = function (select) {
        		updateTech(select, 'title', 'specs');
        	};
      },
      templateUrl: 'views/directives/evaluate/techSelect.html'
    };
  });

'use strict';

angular.module('wcagReporter')
  .directive('iconButton', ["directivePlugin", function (directivePlugin) {
    return directivePlugin({
      restrict: 'E',
      replace: true,
      scope: {
        label: '=',
        icon: '=',
        click: '&'
      },
      templateUrl: 'views/directives/evaluate/iconButton.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('buttonCollapse', ["directivePlugin", function (directivePlugin) {
    return directivePlugin({
      restrict: 'E',
      replace: true,
      transclude: true,
      scope: {
        	obj: '=target',
        	prop: '@property'
      },
      link: function (scope, elm, attr) {
        // If collapsed and the property is not defined, set the default to collapse
        if (attr.collapsed !== undefined && scope.obj[scope.prop] === undefined) {
          scope.obj[scope.prop] = true;
        }
      },
      templateUrl: 'views/directives/buttonCollapse.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('fullReport', ["directivePlugin", "$interval", function (directivePlugin, $interval) {
    function testFilter (element) {
      return function (query) {
        return element.find(query).length === 0;
      };
    }

    return directivePlugin({
      restrict: 'E',
      replace: true,
      link: function (scope, element) {
        var tests = [
          '.panel-heading',
          '.sample_narrow',
          '.score-total'
        ];
        var stop = $interval(function () {
          tests = tests.filter(testFilter(element));
          if (tests.length === 0) {
            scope.$emit('reportReady', element);
            $interval.cancel(stop);
          }
        }, 200);
      },
      templateUrl: 'views/directives/fullReport.html'
    });
  }]);

'use strict';

angular.module('wcagReporter')
  .directive('successCriterion', ["directivePlugin", "$rootScope", "toggleCriterionText", function (directivePlugin, $rootScope, toggleCriterionText) {
    var className = {
      'earl:untested': 'untested',
      'earl:passed': 'passed',
      'earl:failed': 'failed',
      'earl:inapplicable': 'inapplicable',
      'earl:cantTell': 'canttell'
    };

    return directivePlugin({
      restrict: 'E',
      replace: true,
      scope: {
        assert: '=assertion',
        spec: '=requirement',
        opt: '=options'
      },
      link: function (scope) {
        window.toggleCriterionText = toggleCriterionText;
        // scope.outcomes = outcomes;
        scope.rootHide = $rootScope.rootHide;
        scope.critHide = scope.spec.id + '-cb';
        scope.getClassName = function (state) {
          return className[state];
        };
      },
      toggleCriterionText: toggleCriterionText,
      templateUrl: 'views/directives/successCriterion.html'
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('autoResize', ["directivePlugin", function (directivePlugin) {
    return directivePlugin({
      restrict: 'A',
      link: function (scope, element) {
        scope.$evalAsync(function () {
            	angular.element(element)
            .textareaAutoSize();
        });
      }
    });
  }]);

'use strict';
angular.module('wcagReporter')
  .directive('shyPlaceholder', ["directivePlugin", function (directivePlugin) {
    var ph = 'placeholder';

    return directivePlugin({
      restrict: 'A',
      scope: {
        shyPlaceholder: '='
      },
      compile: function (elm, attrs) {
        return function (scope, elm) {
          var phValue = scope.$eval(attrs.shyPlaceholder);

          elm.attr(ph, phValue)
            .bind({
              focus: elm.attr.bind(elm, ph, ''),
              blur: elm.attr.bind(elm, ph, phValue)
            });
        };
      }
    });
  }]);

// Based on the work from the great guys at bootstrap UI.
'use strict';
angular.module('wcagReporter')
  .directive('collapse', [
    '$animate',
    function ($animate) {
      return {
        link: function (scope, element, attrs) {
          var initialAnimSkip = true;
          var currentTransition;

          function doTransition (change) {
            var fromState = {};
            Object.keys(change)
              .forEach(function (cssProp) {
                fromState[cssProp] = element.css(cssProp);
              });

            var newTransition = $animate.animate(element, fromState, change);
            if (currentTransition) {
              $animate.cancel(currentTransition);
            }

            currentTransition = newTransition;
            newTransition.then(newTransitionDone, newTransitionDone);
            return newTransition;

            function newTransitionDone () {
              // Make sure it's this transition, otherwise, leave it alone.
              if (currentTransition === newTransition) {
                currentTransition = undefined;
              }
            }
          }

          function expand () {
            if (initialAnimSkip) {
              initialAnimSkip = false;
              expandDone();
            } else {
              element.removeClass('collapse')
                .addClass('collapsing');
              doTransition({ height: element[0].scrollHeight + 'px' })
                .then(expandDone);
            }
          }

          function expandDone () {
            element.removeClass('collapsing');
            element.addClass('collapse in');
            element.css({ height: 'auto' });
          }

          function collapse () {
            if (initialAnimSkip) {
              initialAnimSkip = false;
              collapseDone();
              element.css({ height: 0 });
            } else {
              // CSS transitions don't work with height: auto, so we have to manually change the height to a specific value
              element.css({ height: element[0].scrollHeight + 'px' });
              // trigger reflow so a browser realizes that height was updated from auto to a specific value
              var x = element[0].offsetWidth;

              element.removeClass('collapse in')
                .addClass('collapsing');

              doTransition({ height: 0 })
                .then(collapseDone);
            }
          }

          function collapseDone () {
            element.removeClass('collapsing');
            element.addClass('collapse');
          }

          scope.$watch(attrs.collapse, function (shouldCollapse) {
            if (shouldCollapse) {
              collapse();
            } else {
              expand();
            }
          });
        }
      };
    }
  ]);

// Based on the work from the great guys at bootstrap UI.
angular.module('wcagReporter')

  .constant('dropdownConfig', {
    openClass: 'open'
  })
  .service('dropdownService', ["$document", function ($document) {
    var openScope = null;

    this.open = function (dropdownScope) {
      if (!openScope) {
        $document.bind('click', closeDropdown);
        $document.bind('keydown', escapeKeyBind);
      }

      if (openScope && openScope !== dropdownScope) {
        openScope.isOpen = false;
      }

      openScope = dropdownScope;
    };

    this.close = function (dropdownScope) {
      if (openScope === dropdownScope) {
        openScope = null;
        $document.unbind('click', closeDropdown);
        $document.unbind('keydown', escapeKeyBind);
      }
    };

    var closeDropdown = function (evt) {
      // This method may still be called during the same mouse event that
      // unbound this event handler. So check openScope before proceeding.
      if (!openScope) {
        return;
      }

      var toggleElement = openScope.getToggleElement();
      if (evt && toggleElement && toggleElement[0].contains(evt.target)) {
        return;
      }

      openScope.$apply(function () {
        openScope.isOpen = false;
      });
    };

    var escapeKeyBind = function (evt) {
      if (evt.which === 27) {
        openScope.focusToggleElement();
        closeDropdown();
      }
    };
  }])
  .controller(
    'DropdownController',
    ["$scope", "$attrs", "$parse", "dropdownConfig", "dropdownService", "$animate", function ($scope, $attrs, $parse, dropdownConfig, dropdownService, $animate) {
      var self = this;
      var scope = $scope.$new(); // create a child scope so we are not polluting original one
      var openClass = dropdownConfig.openClass;
      var getIsOpen;
      var setIsOpen = angular.noop;
      var toggleInvoker = $attrs.onToggle ? $parse($attrs.onToggle) : angular.noop;

      this.init = function (element) {
        self.$element = element;

        if ($attrs.isOpen) {
          getIsOpen = $parse($attrs.isOpen);
          setIsOpen = getIsOpen.assign;

          $scope.$watch(getIsOpen, function (value) {
            scope.isOpen = !!value;
          });
        }
      };

      this.toggle = function (open) {
        return scope.isOpen = arguments.length ? !!open : !scope.isOpen;
      };

      // Allow other directives to watch status
      this.isOpen = function () {
        return scope.isOpen;
      };

      scope.getToggleElement = function () {
        return self.toggleElement;
      };

      scope.focusToggleElement = function () {
        if (self.toggleElement) {
          self.toggleElement[0].focus();
        }
      };

      scope.$watch('isOpen', function (isOpen, wasOpen) {
        $animate[isOpen ? 'addClass' : 'removeClass'](self.$element, openClass);

        if (isOpen) {
          scope.focusToggleElement();
          dropdownService.open(scope);
        } else {
          dropdownService.close(scope);
        }

        setIsOpen($scope, isOpen);
        if (angular.isDefined(isOpen) && isOpen !== wasOpen) {
          toggleInvoker($scope, {
            open: !!isOpen
          });
        }
      });

      $scope.$on('$locationChangeSuccess', function () {
        scope.isOpen = false;
      });

      $scope.$on('$destroy', function () {
        scope.$destroy();
      });
    }]
  )
  .directive('dropdown', function () {
    return {
      controller: 'DropdownController',
      link: function (scope, element, attrs, dropdownCtrl) {
        dropdownCtrl.init(element);
      }
    };
  })
  .directive('dropdownToggle', function () {
    return {
      require: '?^dropdown',
      link: function (scope, element, attrs, dropdownCtrl) {
        if (!dropdownCtrl) {
          return;
        }

        dropdownCtrl.toggleElement = element;

        var toggleDropdown = function (event) {
          event.preventDefault();

          if (!element.hasClass('disabled') && !attrs.disabled) {
            scope.$apply(function () {
              dropdownCtrl.toggle();
            });
          }
        };

        element.bind('click', toggleDropdown);

        // WAI-ARIA
        element.attr({
          'aria-haspopup': true,
          'aria-expanded': false
        });
        scope.$watch(dropdownCtrl.isOpen, function (isOpen) {
          element.attr('aria-expanded', !!isOpen);
        });

        scope.$on('$destroy', function () {
          element.unbind('click', toggleDropdown);
        });
      }
    };
  });

'use strict';
angular.module('wcagReporter')
  .filter('getUrl', function () {
    var linkReg = /((https?):\/\/)([\da-z\.-]+)\.([a-z\.]{2,6})([\-\w\d@:%_\+.~#?,&\/\/=]+)/g;

    return function (text) {
    	var match;
    	if (typeof text === 'string') {
        	match = text.match(linkReg);
    	}
      if (match) {
        return match[0];
      } else {
        	return false;
      }
    };
  });

'use strict';

angular.module('wcagReporter')
  .filter('selectedCasesOnly', function () {
    function critHasSelectedPages (criterion) {
      for (var i = 0; i < criterion.subject.length; i++) {
        var page = criterion.subject[i];
        if (page.selected && (page.title || page.description)) {
          return true;
        }
      }
      return false;
    }

    return function (criterion) {
      if (criterion) {
        return criterion.filter(critHasSelectedPages);
      }
    };
  });

'use strict';

angular.module('wcagReporter')
  .filter('rdfToLabel', ["$filter", function ($filter) {
    var rdfToLabel;
    var keymap = {
      'earl:passed': 'PASSED',
      'earl:failed': 'FAILED',
      'earl:cantTell': 'CANT_TELL',
      'earl:inapplicable': 'NOT_PRESENT',
      'earl:untested': 'NOT_CHECKED',
      A: 'LEVEL_A',
      AA: 'LEVEL_AA',
      AAA: 'LEVEL_AAA',
      'wai:WCAG2A-Conformance': 'LEVEL_A',
      'wai:WCAG2AA-Conformance': 'LEVEL_AA',
      'wai:WCAG2AAA-Conformance': 'LEVEL_AAA'
    };

    rdfToLabel = function (earl) {
      return $filter('translate')('EARL.' + keymap[earl]);
    };
    rdfToLabel.keymap = keymap;
    rdfToLabel.$stateful = true;

    return rdfToLabel;
  }]);

'use strict';

angular.module('wcagReporter')
  .filter('txtToHtml', ["$filter", function ($filter) {
    return function (text) {
      if (typeof text !== 'string') {
        return '';
      }
      return text.split('\n')
        .reduce(function (cur, line) {
          if (line.trim() === '') {
            return cur + '</p><p>';
          } else {
            line = $filter('linky')(line, '_blank');
            return cur + (cur.substr(-3) === '<p>' ? '' : '<br />') + line;
          }
        }, '<p>') + '</p>';
    };
  }]);

'use strict';

angular.module('wcagReporter')
  .service('appState', ["$rootScope", "$document", "translateFilter", function ($rootScope, $document, translateFilter) {
    var appState;
    var warningTxt = translateFilter('COMMON.WARNING_BEFORE_UNLOAD');
    var jqWin = window.jQuery(window);

    appState = {
      pristine: true,
      empty: true,
      currentStateIndex: 0,
      maxStateIndex: 0,
      stateList: [
        {
          name: 'start',
          route: '/',
          copmlete: false
        },
        {
          name: 'scope',
          route: '/evaluation/scope',
          copmlete: false
        },
        {
          name: 'explore',
          route: '/evaluation/explore',
          copmlete: false
        },
        {
          name: 'sample',
          route: '/evaluation/sample',
          copmlete: false
        },
        {
          name: 'audit',
          route: '/evaluation/audit',
          copmlete: false
        },
        {
          name: 'report',
          route: '/evaluation/report',
          copmlete: false
        },
        {
          name: 'viewReport',
          route: '/view_report',
          copmlete: false
        }
      ]
    };

    function warningFn () {
      return warningTxt;
    }

    /**
     * Set the current state and if it's higher then the max state, increase the max
     * @param  {[string]} state name
     * @return {[object]} Self
     */
    appState.moveToState = function (newState) {
    	var i, newIndex;
    	for (i = 0; i < this.stateList.length; i += 1) {
        if (appState.stateList[i].name === newState) {
          newIndex = i;
          break;
        }
      }
    	appState.currentStateIndex = newIndex;
    	if (appState.maxStateIndex < newIndex) {
	  		appState.maxStateIndex = newIndex;
	  	}
    	return appState;
    };

    appState.getPreviousState = function () {
      return appState.stateList[appState.currentStateIndex - 1];
    };

    appState.getNextState = function () {
      return appState.stateList[appState.currentStateIndex + 1];
    };

    appState.setPrestineState = function () {
      appState.pristine = true;
      jqWin.off('beforeunload', warningFn);
      $document.one('change', 'input[type=text], textarea, select', appState.setDirtyState);
      $rootScope.$emit('appstate:prestine');
    };

    appState.setDirtyState = function setDirtyState () {
      if (appState.pristine) {
        $rootScope.$emit('appstate:dirty');
        jqWin.on('beforeunload', warningFn);
      }
      appState.pristine = false;
      appState.empty = false;
    };

    appState.init = function () {
      $document.one('change', 'input[type=text], textarea, select', appState.setDirtyState);
    };

    /**
     * Check if the browser supports all required features
     * @return {Boolean}
     */
    appState.hasBrowserSupport = function () {
      var res = true;
      if (!window.Blob) {
        console.error('Blob not supported in this browser.');
        res = false;
      }
      if (!window.FileReader) {
        console.error('FileReader not supported in this browser.');
        res = false;
      }
      if (!window.navigator.msSaveOrOpenBlob &&
            !((window.URL || window.webkitURL) &&
              (window.URL || window.webkitURL).createObjectURL)) {
        console.error('msSaveOrOpenBlob or createObjectURL not supported in this browser.');
        res = false;
      }
      return res;
    };

    return appState;
  }]);

'use strict';

/**
 *
 */
angular.module('wcagReporter')
  .provider('wcag2spec', function () {
    var specPath;
    var guidelines;
    var criteria;
    var currentSpec;
    var broadcast = angular.noop;
    var criteriaObj = {};
    var specs = {};

    function pluck (prop) {
      return function (a, b) {
        if (!angular.isArray(a)) {
          a = a[prop];
        }
        return a.concat(b[prop]);
      };
    }

    var wcag2 = {
      addSpec: function (lang, spec) {
        lang = lang.toLowerCase();
        specs[lang] = spec;
        if (!currentSpec) {
          wcag2.useLanguage(lang);
        }
      },

      loadLanguage: function (lang) {
        lang = lang.toLowerCase();
        if (typeof specPath !== 'string') {
          throw new Error('specPath must be defined first');
        }
        if (specs[lang]) {
          return wcag2.useLanguage(lang);
        }

        var path = specPath.replace('${lang}', lang.toLowerCase());
        $.getJSON(path)
          .done(function (data) {
            specs[lang] = data;
            wcag2.useLanguage(lang);
            broadcast('wcag2spec:load', lang);
          })
          .fail(console.error.bind(console));
      },

      useLanguage: function (lang) {
        lang = lang.toLowerCase();
        if (!specs[lang]) {
          throw new Error('Spec for lang ' + lang + ' not defined.');
        }
        currentSpec = specs[lang];
        // Concat all guidelines arrays of each principle
        guidelines = currentSpec.principles
          .reduce(pluck('guidelines'), []);

        // Concat all criteria arrays of each guideline
        criteria = guidelines.reduce(pluck('successcriteria'), []);

        // Make an object of the criteria array with uri as keys
        criteria.forEach(function (criterion) {
          var levels = [
            'A',
            'AA',
            'AAA'
          ];

          if (levels.indexOf(criterion.level) !== -1) {
            criterion.level = 'wai:WCAG2' + criterion.level + '-Conformance';
            criteriaObj[criterion.id] = criterion;
          }

          // Versions are 2.0 or 2.1 but need to be json-ld wich is WCAG20 and WCAG21
          if (Object.prototype.hasOwnProperty.call(criterion, 'versions')) {
            criterion.versions = criterion.versions
              .map(function (version) {
                return 'WCAG' + version.replace('.', '');
              });
          }
        });

        broadcast('wcag2spec:langChange', lang);
      },

      getGuidelines: function () {
        return guidelines;
      },
      getCriteria: function () {
        return criteria;
      },
      getCriterion: function (id) {
        return criteriaObj[id];
      },
      getPrinciples: function () {
        return currentSpec.principles;
      },
      isLoaded: function () {
        return (typeof currentSpec !== 'undefined');
      },
      onLangChange: angular.noop
    };

    this.setSpecPath = function (path) {
      specPath = path;
    };

    this.loadLanguage = wcag2.loadLanguage;

    this.$get = [
      '$rootScope',
      function ($rootScope) {
        broadcast = function (a, b, c) {
          $rootScope.$broadcast(a, b, c);
        };

        wcag2.onLangChange = function (cb) {
          $rootScope.$on('wcag2spec:langChange', cb);
        };

        return angular.extend({}, wcag2);
      }
    ];
  });

'use strict';

angular.module('wcagReporter')
  .service('CriterionAssert', ["types", "evalSampleModel", "$filter", "TestCaseAssert", "wcag2spec", "currentUser", function (
    types,
    evalSampleModel,
    $filter,
    TestCaseAssert,
    wcag2spec,
    currentUser
  ) {
    function CriterionAssert (idref) {
      var self = this;

      // Copy prototype onto the object - prevents problems with JSON.stringify()
      for (var key in CriterionAssert.prototype) {
        if (!this.hasOwnProperty(key)) {
          this[key] = CriterionAssert.prototype[key];
        }
      }

      this.test = idref;
      this.mode = types.EARL.MODE.MANUAL;
      this.hasPart = [];
      this.result = {
        type: types.EARL.RESULT.class,
        outcome: types.EARL.OUTCOME.UNTESTED,
        description: ''
      };

      this.getSinglePageAsserts = function () {
        return self.hasPart.filter(function (page) {
          return page.multiPage !== true;
        });
      };

      this.getMultiPageAsserts = function () {
        return self.hasPart.filter(function (page) {
          return page.multiPage === true;
        });
      };

      this.addPage = function (page) {
        this.addTestCaseAssertion({
          subject: [page]
        });
      };

      this.removePage = function (page) {
        var parts = this.hasPart;
        var x = parts.map(function (assert) {
          return assert.subject[0].id;
        });
        parts.forEach(function (assert, partIndex) {
          var subjIndex = assert.subject.indexOf(page);
          if (subjIndex !== -1) {
            if (assert.multiPage) {
              parts.subject.splice(subjIndex, 1);
            } else {
              parts.splice(partIndex, 1);
            }
          }
        });
      };
    }

    CriterionAssert.prototype = {
      type: 'Assertion',
      test: undefined,
      assertedBy: undefined,
      subject: '_:website',
      result: undefined,
      mode: undefined,
      hasPart: undefined,
      getSinglePageAsserts: undefined,
      getMultiPageAsserts: undefined,

      addTestCaseAssertion: function (obj) {
        var key;
        var tc = new TestCaseAssert();
        tc.testcase = this.test;
        this.hasPart.push(tc);
        if (!obj) {
          return;
        }
        for (key in obj) {
          if (key === 'subject') {
            tc.setSubject(obj.subject);
          } else {
            tc[key] = obj[key];
          }
        }
      },

      transferMacroData: function (macroAssert) {
        this.getSinglePageAsserts()
          .filter(function (assert) {
            return macroAssert.subject.indexOf(assert.subject[0]) !== -1;

          // Append the current result
          })
          .forEach(function (assert) {
            if (typeof assert.result.description !== 'string') {
              assert.result.description = '';
            }

            if (assert.result.description.trim() === '') {
              assert.result.description = macroAssert.result.description.trim();
            } else {
              assert.result.description = (macroAssert.result.description +
                        '\n\n' + assert.result.description).trim();
            }
            assert.result.outcome = macroAssert.result.outcome;
          });
      },

      /**
         * For each page in the sample, create a
         * test case if none exists already
         */
      setCaseForEachPage: function () {
        var singlePageCases;
        var self = this;

        // Find all test cases with a single page
        singlePageCases = this.hasPart
          .filter(function (assert) {
            return (angular.isArray(assert.subject) &&
                       assert.subject.length === 1);
            // Put all pages from them in singlePageCases
          })
          .map(function (assert) {
            return assert.subject[0];
          });

        // Select all pages, filter those not singlePageCases
        evalSampleModel.getPages()
          .filter(function (page) {
            return singlePageCases.indexOf(page) === -1;

            // Then add a test case assertion with that page
          })
          .forEach(function (page) {
            self.addTestCaseAssertion({
              subject: [page]
            });
          });
      },

      getSpec: function () {
        return wcag2spec.getCriterion(this.test);
      }
    };

    // Checks if an assert is empty
    CriterionAssert.isDefined = function (critAssert) {
      var hasPart = critAssert.hasPart
        .reduce(function (hasPart, tcAssert) {
          if (hasPart || tcAssert.isDefined()) {
            return true;
          } else {
            return false;
          }
        }, false);

      return hasPart || !!critAssert.result.description ||
               critAssert.result.outcome !== types.EARL.OUTCOME.UNTESTED;
    };

    CriterionAssert.updateMetadata = function (critAssert) {
      critAssert.assertedBy = currentUser.id;
      critAssert.mode = types.EARL.MODE.MANUAL;
      critAssert.result.date = $filter('date')(Date.now(), 'yyyy-MM-dd HH:mm:ss Z');
    };

    return CriterionAssert;
  }]);

'use strict';

angular
  .module('wcagReporter')
  .service('TestCaseAssert', ["types", "evalSampleModel", "currentUser", function (
    types,
    evalSampleModel,
    currentUser
  ) {
    var protoResult = {
      type: types.EARL.RESULT.class,
      description: '',
      outcome: types.EARL.OUTCOME.UNTESTED
    };

    function TestCaseAssert () {
      // Copy prototype onto the object - prevents problems with JSON.stringify()
      for (var key in TestCaseAssert.prototype) {
        if (!this.hasOwnProperty(key)) {
          this[key] = TestCaseAssert.prototype[key];
        }
      }

      this.subject = [];
      this.result = angular.copy(protoResult);
    }

    TestCaseAssert.isDefined = function (tc) {
      var hasPage = false;
      tc.subject.forEach(function (page) {
        hasPage = (hasPage || page.title || page.description);
      });
      return ((tc.result.description || tc.result.outcome !== protoResult.outcome) && hasPage);
    };

    TestCaseAssert.prototype = {
      type: 'Assertion',
      assertedBy: currentUser.id,
      subject: undefined,
      testCase: undefined,
      result: undefined,
      multiPage: false,
      mode: types.EARL.MODE.MANUAL,
      isDefined: function () {
        return TestCaseAssert.isDefined(this);
      },

      addNewPage: function (page) {
        this.subject.push(page);
      },

      removePage: function (i) {
        this.subject.splice(i, 1);
      },

      setSubject: function (pages) {
        var subject = [];
        this.subject = subject;
        if (pages && !angular.isArray(pages)) {
          pages = [pages];
        }
        pages.forEach(function (page) {
          if (typeof page === 'string') {
            page = evalSampleModel.getPageById(page);
          }
          if (typeof page === 'object') {
            subject.push(page);
          }
        });
      }
    };

    return TestCaseAssert;
  }]);

'use strict';

angular.module('wcagReporter')
  .service('Page', ["$filter", function ($filter) {
    var translateFilter = $filter('translate');

    function Page () {
      this.type = [
        'TestSubject',
        'WebPage'
      ];
    }

    Page.updateSource = function (page) {
      var source = $filter('getUrl')(page.description);
      if (source) {
        page.source = source;
      } else {
        delete page.source;
      }
      return source;
    };

    Page.prependProtocol = function (page) {
      if (page.description && page.description.match(/^([\da-z\.-]+)\.([a-z\.]{2,6})/)) {
        page.description = 'http://' + page.description;
      }
    };

    Page.openInWindow = function (page, target) {
      target = target || '_blank';
      if (page.source) {
        window.open(page.source, target);
      }
    };

    Page.prototype = {
      type: [
        'TestSubject',
        'WebPage'
      ],
      id: '',
      description: undefined,
      title: '',
      tested: false,
      selected: false,
      displayTitle: function () {
        var num = 0;
        if (this.title.trim()) {
          return this.title;
        } else if (this.id.substr(0, 9) === '_:struct_') {
          num = +this.id.substr(9);
          return translateFilter('SAMPLE.STRUCTURED_PAGE') + ' ' + (num + 1);
        } else if (this.id.substr(0, 7) === '_:rand_') {
          num = +this.id.substr(7);
          return translateFilter('SAMPLE.RANDOM_PAGE') + ' ' + (num + 1);
        } else {
          return translateFilter('SAMPLE.SAMPLE_PAGE');
        }
      }
    };

    return Page;
  }]);

'use strict';

angular.module('wcagReporter')
  .service('evalScopeModel', function () {
    var scopeModel = {
      type: 'EvaluationScope',
      wcagVersion: 'WCAG21',
      conformanceTarget: 'wai:WCAG2AA-Conformance',
      additionalEvalRequirement: '',
      website: {
        type: [
          'TestSubject',
          'WebSite'
        ],
        id: '_:website',
        siteName: '',
        siteScope: ''
      },
      accessibilitySupportBaseline: ''
    };

    scopeModel.exportData = function () {
      return {
        type: scopeModel.type,
        conformanceTarget: scopeModel.conformanceTarget,
        additionalEvalRequirement: scopeModel.additionalEvalRequirement,
        website: {
          type: scopeModel.website.type,
          id: scopeModel.website.id,
          siteName: scopeModel.website.siteName,
          siteScope: scopeModel.website.siteScope
        },
        accessibilitySupportBaseline: scopeModel.accessibilitySupportBaseline
      };
    };

    scopeModel.wcagVersionOptions = [
      'WCAG21',
      'WCAG20'
    ];

    scopeModel.conformanceOptions = [
      'wai:WCAG2A-Conformance',
      'wai:WCAG2AA-Conformance',
      'wai:WCAG2AAA-Conformance'
    ];

    /**
     * Returns an array of errors indicating which (if any) properties are invalid
     */
    scopeModel.validate = function () {
      return [];
    };

    scopeModel.matchConformTarget = function (level) {
      return scopeModel.conformanceTarget.length >= level.length;
    };

    // Lock up the object, for a little more dev security
    Object.preventExtensions(scopeModel.website);
    Object.preventExtensions(scopeModel);

    return scopeModel;
  });

'use strict';

angular.module('wcagReporter')
  .service('currentUser', function () {
    return {
      '@context': {
        '@vocab': 'http://xmlns.com/foaf/0.1/',
        id: '@id',
        type: '@type'
      },
      id: '_:evaluator',
      type: 'Person',
      name: ''
    };
  });

'use strict';

angular
  .module('wcagReporter')
  .service('evalExploreModel', ["knownTech", "evalSampleModel", function (
    knownTech,
    evalSampleModel
  ) {
    var exploreModel = {
      knownTech: knownTech
    };
    var basicProps = [
      'reliedUponTechnology',
      'essentialFunctionality',
      'pageTypeVariety',
      'commonPages',
      'otherRelevantPages'
    ];

    // add all properties to this
    basicProps
      .forEach(function (prop) {
        exploreModel[prop] = undefined;
      });

    exploreModel.reliedUponTechnology = [];

    exploreModel.importData = function (evalData) {
      if (!angular.isArray(evalData.reliedUponTechnology)) {
        evalData.reliedUponTechnology = [evalData.reliedUponTechnology];
      }

      basicProps
        .forEach(function (prop) {
          if (evalData[prop]) {
            exploreModel[prop] = evalData[prop];
          }
        });
    };

    exploreModel.exportData = function () {
      var exportData = {};

      basicProps
        .forEach(function (prop) {
          exportData[prop] = exploreModel[prop];
        });

      return exportData;
    };

    /**
     * Returns an array of errors indicating which (if any) properties are invalid
     */
    exploreModel.validate = function () {
      return [];
    };

    // Lock up the object, for a little more dev security
    Object.preventExtensions(exploreModel);

    return exploreModel;
  }]);

'use strict';

angular.module('wcagReporter')
  .service('evalSampleModel', ["Page", function (Page) {
    var ng = angular;
    var sampleModel = {};
    var randomPages = [];
    var structuredPages = [];

    /**
     * Get the next available page number for a given sample
     * @param  {object} sample
     * @return {int}    pageNum
     */
    function getAvailablePageNum (sample) {
      var name, lastId;
      if (!ng.isArray(sample.webpage) || sample.webpage.length === 0) {
        return 0;
      }

      name = (sample === sampleModel.randomSample ? '_:rand_' : '_:struct_');
      lastId = sample.webpage.map(function (page) {
        return +page.id.substr(name.length);
      })
        .sort(function (a, b) {
          return a - b;
        })
        .pop();

      return lastId + 1;
    }

    sampleModel.structuredSample = {
      webpage: randomPages
    };

    sampleModel.randomSample = {
      webpage: structuredPages
    };

    sampleModel.removePage = function (sample, pageNum) {
      var page;
      if (!ng.isNumber(pageNum)) {
        pageNum = sample.webpage.indexOf(pageNum);
      }

      if (ng.isNumber(pageNum) && pageNum >= 0) {
        page = sample.webpage.splice(pageNum, 1)[0];
      }
      return page;
    };

    sampleModel.addNewStructuredPage = function () {
      var sample = sampleModel.structuredSample;
      var page = new Page();
      var num = getAvailablePageNum(sample);

      sample.webpage.push(page);
      page.id = '_:struct_' + num;
      page.title = '';
      return page;
    };

    sampleModel.addNewRandomPage = function () {
      var page = new Page();
      var num = getAvailablePageNum(sampleModel.randomSample);

      sampleModel.randomSample.webpage.push(page);
      page.id = '_:rand_' + num;
      page.title = '';
      return page;
    };

    sampleModel.addNewPage = function (sample) {
      if (sample === sampleModel.randomSample) {
        return sampleModel.addNewRandomPage();
      } else {
        return sampleModel.addNewStructuredPage();
      }
    };

    sampleModel.getPageByTitle = function (title) {
      var res;
      sampleModel.getPages()
        .forEach(function (page) {
          if (page.title === title) {
            res = page;
          }
        });
      return res;
    };

    sampleModel.getPages = function () {
      return sampleModel.structuredSample.webpage
        .concat(sampleModel.randomSample.webpage);
    };

    sampleModel.getFilledPages = function () {
      return sampleModel.getPages()
        .filter(function (page) {
          return (page.description || page.title);
        });
    };

    sampleModel.getSelectedPages = function () {
      return sampleModel.getPages()
        .reduce(function (arr, page) {
          if (page.selected) {
            arr.push(page);
          }
          return arr;
        }, []);
    };

    /**
     * Returns an array of errors indicating which (if any) properties are invalid
     */
    sampleModel.validate = function () {
      return [];
    };

    /**
     * Clean up the data so it can be exported
     */
    sampleModel.exportData = function () {
      var samples;

      // Only export the following properties
      var props = [
        'type',
        'id',
        'description',
        'source',
        'title',
        'tested'
      ];

      // For both samples
      samples = [
        sampleModel.structuredSample.webpage,
        sampleModel.randomSample.webpage
      ]
        .map(function (webpages) {
          return webpages.map(function (page) {
            // create a copy of a page with only the permitted properties
            var newPage = {};
            props.forEach(function (prop) {
              newPage[prop] = page[prop];
            });
            return newPage;
          });
        });
      // and return the samples
      return {
        structuredSample: { webpage: samples[0] },
        randomSample: { webpage: samples[1] }
      };
    };

    sampleModel.importData = function (data) {
      [
        'structuredSample',
        'randomSample'
      ].forEach(function (prop) {
        sampleModel[prop] = data[prop];
        if (typeof data[prop] !== 'object') {
          sampleModel[prop] = {};
        }

        if (typeof sampleModel[prop].webpage === 'undefined') {
          sampleModel[prop].webpage = [];
        } else if (!ng.isArray(sampleModel[prop].webpage)) {
          sampleModel[prop].webpage = [sampleModel[prop].webpage];
        }
        sampleModel[prop].webpage.forEach(function (pageData) {
          pageData.displayTitle = Page.prototype.displayTitle;
        });
      });
    };

    sampleModel.getPageById = function (id) {
      var pages = sampleModel.getPages();
      for (var i = 0; i < pages.length; i++) {
        if (pages[i].id === id) {
          return pages[i];
        }
      }
    };

    // Lock up the object, for a little more dev security
    Object.preventExtensions(sampleModel);

    return sampleModel;
  }]);

'use strict';

angular.module('wcagReporter')
  .service('evalAuditModel', ["TestCaseAssert", "evalScopeModel", "wcag2spec", "CriterionAssert", "types", "$filter", function (
    TestCaseAssert,
    evalScopeModel,
    wcag2spec,
    CriterionAssert,
    types,
    $filter
  ) {
    var auditModel;
    var criteria = {};

    wcag2spec.onLangChange(function () {
      wcag2spec.getCriteria()
        .forEach(function (spec) {
          if (typeof criteria[spec.id] === 'undefined') {
            auditModel.addCritAssert({
              test: spec.id
            });
          }
        });
    });

    function updateAssertion (assertion, update) {
      var testResult = update.result;

      function composeImportResult (result) {
        var composed = '\n\n';
        composed += '*Imported finding*';
        composed += '\noutcome: ' + $filter('rdfToLabel')(result.outcome);
        if (result.description) {
          composed += '\n' + result.description;
        }

        return composed;
      }

      assertion.result.description += composeImportResult(testResult);

      // Remove empty lines at start of description
      assertion.result.description = assertion.result.description.replace(/^\s+/, '');

      // Decide what outcome should be set.
      // Set Failed if imported result is Failed
      // This forces the evaluator to check the import and this is the only outcome
      // that can be set with certainty by automatic assertors.
      if (
        // Dont try to modify if it already has failed outcome
        assertion.result.outcome !== types.EARL.OUTCOME.FAILED &&
        testResult.outcome === types.EARL.OUTCOME.FAILED
      ) {
        assertion.result.outcome = types.EARL.OUTCOME.FAILED;
      }
    }

    auditModel = {
      criteria: criteria,

      exportData: function () {
        // Deep copy:
        var criteria = angular.copy(auditModel.getCriteriaSorted());
        criteria.reduce(function (list, criterion) {
          // Remove all empty test case asserts
          criterion.hasPart = criterion.hasPart
            .filter(function (testcase) {
              return TestCaseAssert.isDefined(testcase);
            });

          // Delete any methods from the output object
          Object.keys(criterion)
            .forEach(function (key) {
              if (typeof criterion[key] === 'function') {
                delete criterion[key];
              }
            });

          // get all hasPart
          list.push.apply(list, criterion.hasPart);
          return list;
        }, [])
          .forEach(function (testcase) {
            // replace the page object with it's id
            testcase.subject = testcase.subject.map(function (page) {
              return page.id;
            });
          });

        return criteria;
      },

      importData: function (evalData) {
        if (evalData.auditResult) {
          if (!angular.isArray(evalData.auditResult)) {
            evalData.auditResult = [evalData.auditResult];
          }
          // NOTE: Why was this done? (Reset criteria to imported criteria)
          // criteria = {};
          // auditModel.criteria = criteria;

          evalData.auditResult.forEach(auditModel.addCritAssert);
        }
      },

      getCritAssert: function (idref) {
        if (typeof criteria[idref] !== 'object') {
          throw new Error('Unknown criterion of id ' + idref);
        }
        return criteria[idref];
      },

      getCriteriaSorted: function () {
        if (!wcag2spec.isLoaded()) {
          return [];
        }
        var critSpec = wcag2spec.getCriteria();
        return critSpec.map(function (criterion) {
          return criteria[criterion.id];
        })
          .filter(angular.isDefined);
      },

      addCritAssert: function (result) {
        var prop;
        var newCrit = Object.create(CriterionAssert.prototype);
        CriterionAssert.apply(newCrit);

        for (prop in result) {
          if (prop === 'hasPart') {
            // Make sure hasPart is an array:
            if (!angular.isArray(result.hasPart)) {
              result.hasPart = [result.hasPart];
            }
            result.hasPart.forEach(newCrit.addTestCaseAssertion, newCrit);
          } else {
            newCrit[prop] = result[prop];
          }
        }
        criteria[newCrit.test] = newCrit;
      },

      updateCritAssert: function updateCritAssert (id, data) {
        if (data === undefined) {
          return;
        } else if (typeof data !== 'object') {
          return;
        }

        // First try to get a matching criteria before anything else
        var criterion = auditModel.getCritAssert(id);

        if (data.result) {
          updateAssertion(criterion, data);
        }
      },

      addPageForAsserts: function (page) {
        Object.keys(criteria)
          .forEach(function (critName) {
            criteria[critName].addPage(page);
          });
      },

      removePageFromAsserts: function (page) {
        Object.keys(criteria)
          .forEach(function (critName) {
            criteria[critName].removePage(page);
          });
      }
    };
    return auditModel;
  }]);

'use strict';

angular.module('wcagReporter')
  .service('evalReportModel', ["$filter", "currentUser", function ($filter, currentUser) {
    var protoModel = {
      creator: currentUser,
      title: '',
      summary: '',
      specifics: '',
      commissioner: ''
    };
    var reportModel = Object.create(protoModel);
    protoModel.date = $filter('date')(new Date(), 'dd/MM/yyyy');

    reportModel.exportData = function () {
      var res = angular.copy(reportModel);
      res.creator = res.creator.id;
      return res;
    };

    reportModel.importData = function (evalData) {
      Object.keys(protoModel)
        .forEach(function (key) {
          if (angular.isDefined(evalData[key])) {
            reportModel[key] = evalData[key];
          }
        });
    };

    reportModel.setDefaultTitle = function (title) {
      if (!reportModel.title) {
        reportModel.title = title;
      }
    };

    return reportModel;
  }]);

'use strict';

angular
  .module('wcagReporter')
  .factory('evalModel', ["evalScopeModel", "evalExploreModel", "evalSampleModel", "evalAuditModel", "evalReportModel", "evalContextV3", "currentUser", function (
    evalScopeModel,
    evalExploreModel,
    evalSampleModel,
    evalAuditModel,
    evalReportModel,
    evalContextV3,
    currentUser
  ) {
    var evalModel = {
      id: undefined,
      type: 'Evaluation',
      context: evalContextV3,
      scopeModel: evalScopeModel,
      exploreModel: evalExploreModel,
      sampleModel: evalSampleModel,
      auditModel: evalAuditModel,
      reportModel: evalReportModel,
      // This array collects data that is outside the evaluation
      // For example the author and external rdf data
      otherData: [currentUser]
    };

    return evalModel;
  }]);

'use strict';

angular
  .module('wcagReporter')
  .factory('wcagReporterImport', ["$rootScope", "evalModel", "currentUser", "reportStorage", "importV1", "changeLanguage", function (
    $rootScope,
    evalModel,
    currentUser,
    reportStorage,
    importV1,
    changeLanguage
  ) {
    var jsonld = window.jsonld;

    /**
     * OBJECT MODIFIER
     * Add to or replace object 1's keys with object 2's keys
     * @param  {Object} obj1 Object that needs to be updated
     * @param  {Object} obj2 Object with keys that need to replace or be added to obj1
     * @return {undefined}      Modifies object 1 with object 2 keys
     */
    function objectCollide (obj1, obj2) {
      Object.keys(obj1)
        .forEach(function (prop) {
          if (
            typeof obj1[prop] !== 'function' &&
            typeof obj2[prop] !== 'undefined'
          ) {
            obj1[prop] = obj2[prop];
          }
        });
    }

    function compactEach (callback) {
      var results = [];
      var calls = 0;
      var evalType = evalModel.context['@vocab'] + evalModel.type;
      var personType = currentUser['@context']['@vocab'] + currentUser.type;

      function testCallback (err, compacted) {
        if (err) {
          // Something json-ldish is not ok here, exit.
          // This should not be the case anytime since the data should have
          // been checked before importing.
          console.error(err);
          return;
        }

        results.push(compacted);

        if (results.length === calls) {
          callback(results);
        }
      }

      return function (evalObj) {
        calls += 1;

        if (
          evalObj['@type'] &&
          evalObj['@type'].indexOf(evalType) !== -1
        ) {
          // Compact with the evaluation context
          jsonld.compact(
            evalObj,
            evalModel.context,
            testCallback
          );
        } else if (
          evalObj['@type'] &&
          evalObj['@type'].indexOf(personType) !== -1
        ) {
          // Compact with the FOAF context
          jsonld.compact(
            evalObj,
            currentUser['@context'],
            testCallback
          );
        } else {
          results.push(evalObj);
        }
      };
    }

    /**
     * Inject evaluation data into the reporter
     * @param {[Object]} evalData
     */
    function updateEvalModel (evalData) {
      if (evalData.evaluationScope) {
        objectCollide(evalModel.scopeModel, evalData.evaluationScope);
      }

      evalModel.id = evalData.id;
      evalModel.type = evalData.type;

      evalModel.sampleModel.importData(evalData);
      evalModel.reportModel.importData(evalData);
      evalModel.auditModel.importData(evalData);
      evalModel.exploreModel.importData(evalData);
      evalModel.otherData = evalData.otherData;
    }

    var importModel = {

      storage: reportStorage,

      /**
       * Import an evaluation from a JSON string
       * @param  {string} json Evaluation
       * @return {undefined}
       */
      fromJson: function (json) {
        importModel.fromObject(angular.fromJson(json));
      },

      getFromUrl: function () {
        return reportStorage.get()
          .then(function (data) {
            importModel.fromJson(data);
            return data;
          });
      },

      fromObject: function (evalData) {
        // Check if an old format needs to be converted:
        var graphData = evalData['@graph'] || null;

        if (
          angular.isArray(graphData) &&
          !importV1.isLatestVersion(graphData)
        ) {
          // Fix an older import format
          evalData['@graph'] = importV1(graphData);
        }

        jsonld.expand(evalData, function (err, expanded) {
          if (err) {
            console.error(err);
          }

          importModel.fromExpanded(expanded);
        });
      },

      fromExpanded: function (evalData) {
        evalData
          .forEach(
            compactEach(function (results) {
              var evaluation = results
                .reduce(function (result, data) {
                  if (data.type === 'Evaluation') {
                    if (typeof result !== 'undefined') {
                      throw new Error('Only one evaluation object allowed in JSON data');
                    }

                    return data;
                  }

                  return result;
                }, undefined);

              if (!evaluation) {
                throw new Error('No evaluation found in data');
              }

              // If the creator has an id, give that id to the current user
              if (typeof evaluation.creator === 'string' &&
                evaluation.creator.indexOf('_:') === 0) {
                currentUser.id = evaluation.creator;
              }

              evaluation.creator = currentUser;

              var foundUser = false;

              // Find the first Person that matches the ID of the current user
              results
                .forEach(function (data) {
                  if (
                    !foundUser &&
                    data.type === 'Person' &&
                    data.id === currentUser.id
                  ) {
                  // overwrite the current user with the new data
                    angular.extend(currentUser, data);
                    foundUser = true;
                  }
                });

              // Take all data that isn't the evaluation or the current user
              evaluation.otherData = results
                .reduce(function (otherData, data) {
                  if (data !== evaluation && data.id !== currentUser.id) {
                    otherData.push(data);
                  }

                  return otherData;
                }, [currentUser]);

              if (evaluation.lang) {
                // This is a workaround for what seems to be a bug in the
                // JSON-LD lib. It outputs ['e', 'n'] instead of 'en', so we
                // join to fix this.
                if (angular.isArray(evaluation.lang)) {
                  evaluation.lang = evaluation.lang.join('');
                }

                changeLanguage(evaluation.lang);
              }

              // Put the evaluation as the first on the list
              $rootScope.$apply(function () {
                updateEvalModel(evaluation);
              });
            })
          );
      }
    };

    return importModel;
  }]);

'use strict';

/**
 * ImportV1; imports and migrates jsonld data
 * TODO: Use JSONLD API
 */

angular
  .module('wcagReporter')
  .factory('importV1', ["types", "wcagSpecIdMap", "evalContextV1", "evalContextV2", "evalContextV3", "$filter", function (
    types,
    wcagSpecIdMap,
    evalContextV1,
    evalContextV2,
    evalContextV3,
    $filter
  ) {
    var getUrl = $filter('getUrl');
    var isLatestVersion = isV3Evaluation;
    /**
     * Converts json-ld @graph contents
     * @param  {Array} '@graph'-contents
     * @return {Array} new updated '@graph'-contents
     */
    function convertor(importArray) {
      return importArray
        .map(function (importObj) {
          // upgrade from v1 to v2
          if (isV1Evaluation(importObj)) {
            importObj = upgradeToV2(importObj);
          }

          if (isV2Evaluation(importObj)) {
            importObj = upgradeToV3(importObj);
          }

          // Correct the foaf namespace
          if (
            typeof importObj === 'object' &&
            typeof importObj['@context'] === 'object' &&
            importObj['@context']['@vocab'] === 'http://xmlns.com/foaf/spec/#'
          ) {
            importObj['@context']['@vocab'] = 'http://xmlns.com/foaf/0.1/';
          }

          return importObj;
        });
    }

    /**
     * Updates the test WCAG ID to latest version.
     * The test ID exist of 2 parts: [WCAG2, <ID>]
     * @param  {String}      test string
     * @return {String}      new test ID string
     */
    function updateTestId(test) {
      var testId = test.split(':');
      var criterionIdSet = wcagSpecIdMap
        .filter(function (idSet) {
          return idSet.indexOf(testId[1]) >= 0;
        })[0];
      var latestId = criterionIdSet.length - 1;
      testId[1] = criterionIdSet[latestId].toString();

      return testId.join(':');
    }

    /**
     * Check if an Evaluation object is of v1
     */
    function isV1Evaluation(data) {
      if (typeof data !== 'object') {
        throw new TypeError('Expected data to be of type object but is ' + typeof data + ' instead.');
      }

      var dataContext = data['@context'];

      // Skip if the context isn't there
      if (typeof dataContext !== 'object') {
        return false;
      }

      // Check if full context V1 is represented in dataContext
      return _atLeastEqualTo(dataContext, evalContextV1);
    }

    /** Upgrade Page to v2 */
    function fixPage(page) {
      if (page.type === 'webpage') {
        page.type = [
          'TestSubject',
          'WebPage'
        ];
      }

      page.title = page.handle;
      delete page.handle;

      var source = getUrl(page.description);
      if (source) {
        page.source = source;
      }
    }

    function upgradeToV2(evaluation) {
      // Initiate update to prevent side-effect alteration of evaluation
      var update = angular.copy(evaluation);
      update['@context'] = evalContextV2;
      update.type = 'Evaluation';

      // Update the EvaluationScope object
      var evalScope = update.evaluationScope;
      evalScope.type = evalScope.type || 'EvaluationScope';
      evalScope.website.type = evalScope.website.type || [
        'TestSubject',
        'WebSite'
      ];

      update.reliedUponTechnology.forEach(function (tech) {
        tech.type = tech.type || 'Technology';
      });

      // Change conformanceTarget to "wai:WCAG2X-Conformance" where X is A{1,3}
      if (evalScope.conformanceTarget.substr(0, 13) === 'wcag20:level_') {
        evalScope.conformanceTarget = 'wai:WCAG2' + (
          evalScope.conformanceTarget
            .replace('wcag20:level_', '')
            .toUpperCase()
        ) + '-Conformance';
      }

      // website.title > website.siteName
      if (evalScope.website.title) {
        evalScope.website.siteName = evalScope.website.title;
        delete evalScope.website.title;
      }

      // Update the structured and random sample
      if (!angular.isArray(update.structuredSample.webpage)) {
        update.structuredSample.webpage = [update.structuredSample.webpage];
      }
      update.structuredSample.type = update.structuredSample.type || 'Sample';
      update.structuredSample.webpage.forEach(fixPage);

      if (!angular.isArray(update.randomSample.webpage)) {
        update.randomSample.webpage = [update.randomSample.webpage];
      }
      update.randomSample.type = update.randomSample.type || 'Sample';
      update.randomSample.webpage.forEach(fixPage);

      // Update assertions
      update.auditResult.forEach(function updateAsserts(assertion) {
        assertion.type = assertion.type.replace('earl:assertion', 'Assertion');

        if (assertion.testRequirement) {
          assertion.test = assertion.testRequirement.replace('wcag20:', 'WCAG2:');
          delete assertion.testRequirement;
        } else if (assertion.testcase) {
          assertion.test = assertion.testcase.replace('wcag20:', 'WCAG2:');
          delete assertion.testcase;
        }

        if (assertion.result.type !== types.EARL.RESULT.class) {
          assertion.result.type = types.EARL.RESULT.class;
        }

        if (assertion.mode !== types.EARL.MODE.MANUAL) {
          assertion.mode = types.EARL.MODE.MANUAL;
        }
        if (assertion.hasPart) {
          assertion.hasPart.forEach(updateAsserts);
        }
      });

      return update;
    }

    function isV2Evaluation(data) {
      if (typeof data !== 'object') {
        throw new TypeError('Expected object but got ' + typeof data);
      }

      if (!Object.prototype.hasOwnProperty.call(data, '@context')) {
        return false;
      }

      if (data['@context'] === evalContextV2) {
        return true;
      }

      return _atLeastEqualTo(data['@context'], evalContextV2);
    }

    function upgradeToV3(evaluation) {
      var update = angular.copy(evaluation);

      // update context to v3
      update['@context'] = evalContextV3;

      // Update successcriteria ids
      update.auditResult
        .forEach(function (assertion) {
          if (assertion.test) {
            assertion.test = updateTestId(assertion.test);
          }

          if (
            assertion.hasPart &&
            assertion.hasPart.length
          ) {
            assertion.hasPart
              .forEach(function (subAssertion) {
                if (subAssertion.test) {
                  subAssertion.test = updateTestId(subAssertion.test);
                }
              });
          }
        });

      return update;
    }

    function isV3Evaluation(data) {
      if (typeof data !== 'object') {
        throw new TypeError('Expected object but got ' + typeof data);
      }

      if (!Object.prototype.hasOwnProperty.call(data, '@context')) {
        return false;
      }

      if (data['@context'] === evalContextV3) {
        return true;
      }

      return false;
    }

    function _atLeastEqualTo(object1, object2) {
      var result = true;

      for (var property in object2) {
        if (!Object.prototype.hasOwnProperty.call(object1, property)) {
          result = false;
          break;
        }

        if (
          typeof object1[property] === 'object' &&
          !_atLeastEqualTo(object1[property], object2[property])
        ) {
          result = false;
          break;
        }

        if (
          typeof object1[property] === 'string' &&
          object1[property] !== object2[property]
        ) {
          result = false;
          break;
        }
      }

      return result;
    }

    // Compatibility check
    convertor.isLatestVersion = isLatestVersion;

    // Expose methods for testing
    convertor.isV1Evaluation = isV1Evaluation;
    convertor.isV2Evaluation = isV2Evaluation;
    convertor.isV3Evaluation = isV3Evaluation;
    convertor.upgradeToV2 = upgradeToV2;
    convertor.upgradeToV3 = upgradeToV3;

    return convertor;
  }]);

'use strict';
/**
 *
 */
angular.module('wcagReporter')
  .factory('wcagReporterExport', ["evalModel", "reportStorage", "pkgData", "$rootScope", function (evalModel, reportStorage, pkgData, $rootScope) {
    function getJsonLd () {
      var jsonLd = {
        '@context': evalModel.context,
        type: evalModel.type,
        id: evalModel.id,
        publisher: 'reporter:releases/tag/' + pkgData.version,
        lang: $rootScope.lang
      };

      jsonLd.evaluationScope = evalModel.scopeModel.exportData();
      jsonLd.auditResult = evalModel.auditModel.exportData();

      angular.extend(
        jsonLd,
        evalModel.reportModel.exportData(),
        evalModel.sampleModel.exportData(),
        evalModel.exploreModel.exportData()
      );

      return jsonLd;
    }

    var exportModel = {

      storage: reportStorage,

      saveToUrl: function () {
        return reportStorage.post(exportModel.getJson());
      },

      getJson: function () {
        return {
          '@graph': [getJsonLd()].concat(evalModel.otherData)
        };
      },

      getString: function () {
        return angular.toJson(exportModel.getJson(), true);
      },

      getBlobUrl: function (blob) {
        try {
          blob = blob || exportModel.getBlob();
          return (window.URL || window.webkitURL).createObjectURL(blob);
        } catch (e) {
          console.error(e);
        }
      },

      saveBlobIE: function (blob, filename) {
        blob = blob || exportModel.getBlob();
        filename = filename || exportModel.getFileName();

        if (window.navigator.msSaveOrOpenBlob) {
	            window.navigator.msSaveBlob(blob, filename);
	        }
      },

      getBlob: function (data, type) {
        data = data || exportModel.getString();
        type = type || 'application/json;charset=utf-8';
        return new Blob([data], { type: type });
      },

      getFileName: function (ext) {
        var title = (evalModel.scopeModel.website.siteName +
			' evaluation report');
        ext = ext || 'json';
        title = title.trim();

        return title.replace(/(^\-+|[^a-zA-Z0-9\/_| -]+|\-+$)/g, '')
          .toLowerCase()
          .replace(/[\/_| -]+/g, '-') + '.' + ext;
      }
    };

    reportStorage.exportModel = exportModel;

    return exportModel;
  }]);

'use strict';

angular.module('wcagReporter')
  .controller(
    'FooterCtrl',
    ["$scope", "pkgData", function ($scope, pkgData) {
      $scope.pkg = pkgData;
    }]
  );

'use strict';

angular.module('wcagReporter')
  .controller(
    'NavigationCtrl',
    ["$scope", "$rootScope", "supportedLanguages", "changeLanguage", function ($scope, $rootScope, supportedLanguages, changeLanguage) {
      $scope.languages = supportedLanguages;
      $scope.currentLang = $rootScope.lang;

      $rootScope.$on('$translateChangeSuccess', function (e, change) {
        $scope.currentLang = change.language.toLowerCase();
      });

      $scope.changeLanguage = changeLanguage;
    }]
  );

'use strict';

angular.module('wcagReporter')
  .controller(
    'StartCtrl',
    ["$scope", "$location", "appState", "$timeout", "$rootScope", function ($scope, $location, appState, $timeout, $rootScope) {
  	$scope.state = appState.moveToState('start');

      if (typeof $rootScope.rootHide.start1 === 'undefined') {
        $scope.initial = 'hidden';
        $timeout(function () {
          $scope.initial = '';
        }, 500);
        $timeout(function () {
          $rootScope.rootHide.start1 = false;
        }, 700);
      }

      $scope.nextStep = function () {
        $location.path('/evaluation/scope');
      };

      $scope.nextStepName = 'STEP_SCOPE';
    }]
  );

'use strict';

angular.module('wcagReporter')
  .controller(
    'StepButtonsCtrl',
    ["$scope", "$location", "appState", function ($scope, $location, appState) {
      var previous = appState.getPreviousState();
      var next = appState.getNextState();

      if (next) {
        $scope.nextStep = function () {
          $location.path(next.route);
        };
        $scope.nextStepName = 'STEP_' + (next.name).toUpperCase();
      }

      if (previous) {
        $scope.previousStep = function () {
          $location.path(previous.route);
        };
        $scope.previousStepName = 'STEP_' + (previous.name).toUpperCase();
      }
    }]
  );

'use strict';

angular.module('wcagReporter')
  .controller(
    'EvalScopeCtrl',
    ["$scope", "appState", "evalScopeModel", "evalReportModel", "$filter", function (
      $scope,
      appState,
      evalScopeModel,
      evalReportModel,
      $filter
    ) {
      $scope.state = appState.moveToState('scope');
      $scope.scopeModel = evalScopeModel;

      $scope.wcagVersionOptions = evalScopeModel.wcagVersionOptions
        .reduce(function (versions, version) {
          var translateKey = 'SCOPE.' + version;

          versions[version] = $filter('translate')(translateKey);

          return versions;
        }, {});

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
    }]
  );

'use strict';

angular
  .module('wcagReporter')
  .controller('EvalExploreCtrl', ["$scope", "appState", "$timeout", "evalExploreModel", function (
    $scope,
    appState,
    $timeout,
    evalExploreModel
  ) {
    $scope.state = appState.moveToState('explore');
    $scope.exploreModel = evalExploreModel;

    $scope.updateSpec = function (tech) {
      if (techMap[tech.title]) {
        tech.id = techMap[tech.title];
      }
    };

    $scope.knownTech = angular.copy(evalExploreModel.knownTech);
    $scope.otherTech = [];

    // set relied upon technologies in the right field
    evalExploreModel.reliedUponTechnology
      .forEach(function (tech) {
        var index = $scope.knownTech
          // Find exact matching index in knownTech of reliedUponTechnology
          // it will be an user defined technology otherwise
          .reduce(function (index, currTech, currIndex) {
            if (currTech.id === tech.id && currTech.title === tech.title) {
              return currIndex;
            }
            return index;
          }, -1);

        // Set checkboxes for known fields
        if (index !== -1) {
          $scope.knownTech[index].checked = true;
        } else {
        // Push the tech to the other tech field (it is user defined)
          $scope.otherTech.push(tech);
        }
      });

    // Add an empty field by default
    if ($scope.otherTech.length === 0) {
      $scope.otherTech.push({ type: 'Technology' });
    } else {
      $scope.rootHide.OtherTech = $scope.rootHide.OtherTech || true;
    }

    $scope.changeTech = function (tech) {
      if (tech.checked) {
        var newTech = angular.extend({}, tech);
        delete newTech.checked;
        evalExploreModel.reliedUponTechnology.push(newTech);
      } else {
        evalExploreModel.reliedUponTechnology = evalExploreModel.reliedUponTechnology
          .filter(function (item) {
            return item.title !== tech.title && item.id !== tech.id;
          });
      }
    };

    $scope.updateOtherTech = function (tech) {
      var index = evalExploreModel.reliedUponTechnology.indexOf(tech);
      var isEmpty = !tech.title && !tech.id;
      if (index === -1 && !isEmpty) {
        evalExploreModel.reliedUponTechnology.push(tech);
      } else if (index !== -1 && isEmpty) {
        evalExploreModel.reliedUponTechnology.splice(index, 1);
      }
    };

    $scope.addTechnology = function ($event) {
      $scope.otherTech.push({ type: 'Technology' });

      // evalExploreModel.addReliedUponTech();
      if ($event) {
        var button = angular.element($event.delegateTarget);

        $timeout(function () {
          var inputs = button.prev()
            .find('input');

          if (inputs.length > 0) {
            inputs[0].select();
          }
        });
      }
    };

    $scope.removeTechnology = function ($index, $event) {
      var tech = $scope.otherTech[$index];
      var index = evalExploreModel.reliedUponTechnology.indexOf(tech);
      evalExploreModel.reliedUponTechnology.splice(index, 1);
      $scope.otherTech.splice($index, 1);

      // evalExploreModel.reliedUponTechnology.splice($index,1);
      // We need this timeout to prevent Angular UI from throwing an error
      if ($event) {
        $timeout(function () {
          angular.element($event.delegateTarget)
            .closest('fieldset')
            .parent()
            .children()
            .last()
            .focus();
        });
      }
    };

    var techMap = {};
    $scope.knownTech.forEach(function (knownTech) {
      techMap[knownTech.title] = knownTech.id;
    });
  }]);

'use strict';

angular.module('wcagReporter')
  .controller(
    'EvalSampleCtrl',
    ["$scope", "appState", "evalExploreModel", "evalSampleModel", "evalAuditModel", function (
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

      $scope.randPageCount = function () {
        return Math
          .ceil($scope.structuredSample.webpage.length / 10);
      };
    }]
  );

'use strict';

angular.module('wcagReporter')
  .controller('EvalAuditCtrl', ["$scope", "appState", function ($scope, appState) {
    $scope.state = appState.moveToState('audit');
  }]);

'use strict';

angular.module('wcagReporter')
  .controller('AuditCriteriaCtrl', ["$scope", "evalAuditModel", "evalScopeModel", "wcag2spec", "$rootElement", "$anchorScroll", "$filter", "$rootScope", "$timeout", function (
    $scope,
    evalAuditModel,
    evalScopeModel,
    wcag2spec,
    $rootElement,
    $anchorScroll,
    $filter,
    $rootScope,
    $timeout
  ) {
    var principlesOrigin = [];
    var activeFilters = [];

    $scope.criteria = evalAuditModel.getCriteriaSorted();

    $scope.getCritAssert = evalAuditModel.getCritAssert;

    function buildPrinciples (target, origin) {
      var tgtPrinciple = origin[target.length];
      target.push(tgtPrinciple);

      if (target.length !== origin.length) {
        $timeout(function () {
          buildPrinciples(target, origin);
        }, 50);
      }
    }

    // Read from critFilter
    function getActiveFilters () {
      var filters = $scope.critFilter;
      var activatedFilters = [];

      for (var filter in filters) {
        // levels is an object with level key value boolean
        if (
          Object.prototype.hasOwnProperty.call($scope.critFilter, filter) &&
          typeof filters[filter] === 'object'
        ) {
          for (var filterOption in filters[filter]) {
            if (
              Object.prototype.hasOwnProperty.call(filters[filter], filterOption) &&
              filters[filter][filterOption] === true
            ) {
              activatedFilters.push(filterOption);
            }
          }
        }

        // version is a string; WCAG21, WCAG20 or WCAG20 WCAG21
        if (
          Object.prototype.hasOwnProperty.call($scope.critFilter, filter) &&
          typeof filters[filter] === 'string'
        ) {
          filters[filter].split(' ')
            .forEach(function (filterOption) {
              activatedFilters.push(filterOption);
            });
        }
      }

      return activatedFilters.slice();
    }

    function setActiveFilters () {
      activeFilters = getActiveFilters();
    }

    function filteredByLevel () {
      var levelFilter = $scope.critFilter.levels;

      for (var level in levelFilter) {
        if (
          Object.prototype.hasOwnProperty.call(levelFilter, level) &&
          levelFilter[level] === true
        ) {
          return true;
        }
      }

      return false;
    }

    function criterionMatchFilter (criterion) {
      var versionActive = (activeFilters.indexOf(criterion.versions[0]) !== -1);
      var levelActive = (activeFilters.indexOf(criterion.level) !== -1);

      if (
        versionActive &&
        levelActive
      ) {
        return true;
      }

      // Version filtering is always on so if no level is filtered
      // show criteria based on version occurence alone
      if (
        versionActive &&
        !filteredByLevel()
      ) {
        return true;
      }

      return false;
    }

    $scope.principles = [];

    if (wcag2spec.isLoaded()) {
      principlesOrigin = wcag2spec.getPrinciples();
      buildPrinciples($scope.principles, principlesOrigin);
    }

    $scope.$on('wcag2spec:langChange', function () {
      principlesOrigin = wcag2spec.getPrinciples();
      $scope.principles = [];
      buildPrinciples($scope.principles, principlesOrigin);
    });

    $scope.handleFilterChange = function handleFilterChange () {
      setActiveFilters();
    };

    if ($rootScope.rootHide.criteria) {
      $scope.critFilter = $rootScope.rootHide.criteria;
    } else {
      $scope.critFilter = {
        version: evalScopeModel.wcagVersion === 'WCAG21'
          ? 'WCAG21 WCAG20'
          : 'WCAG20',
        levels: {
          'wai:WCAG2A-Conformance': evalScopeModel.matchConformTarget('wai:WCAG2A-Conformance'),
          'wai:WCAG2AA-Conformance': evalScopeModel.matchConformTarget('wai:WCAG2AA-Conformance'),
          'wai:WCAG2AAA-Conformance': evalScopeModel.matchConformTarget('wai:WCAG2AAA-Conformance')
        }
      };

      $rootScope.rootHide.criteria = $scope.critFilter;
    }
    setActiveFilters();

    $scope.isCriterionVisible = function (critSpec) {
      // Check if the level of this criterion should be shown
      if (!criterionMatchFilter(critSpec)) {
        return false;
      }

      return true;
    };

    $scope.isGuidelineVisible = function (guideline) {
      return guideline.successcriteria.some(function (criterion) {
        return $scope.isCriterionVisible(criterion);
      });
    };

    $scope.isPrincipleVisible = function (principle) {
      return principle.guidelines.some(function (guideline) {
        return $scope.isGuidelineVisible(guideline);
      });
    };

    var untested = [
      'earl:untested',
      'earl:cantTell'
    ];
    $scope.criteriaUntested = function (guideline) {
      var count = 0;
      guideline.successcriteria.forEach(function (critSpec) {
        var critAssert = evalAuditModel.getCritAssert(critSpec.id);
        if (untested.indexOf(critAssert.result.outcome) !== -1) {
          count += 1;
        }
      });
      return count;
    };

    // Scroll to the top, then move focus to the h1
    $scope.toTop = function () {
      $('html, body')
        .animate({
          scrollTop: $rootElement.offset().top
        }, 200, $rootElement.focusH1);
    };
  }]);

'use strict';

angular.module('wcagReporter')
  .controller(
    'AuditSamplePagesCtrl',
    ["$scope", "evalSampleModel", "Page", "$rootScope", function ($scope, evalSampleModel, Page, $rootScope) {
      var getSelected = evalSampleModel.getSelectedPages;
      var getPages = evalSampleModel.getPages;

      $scope.structuredSample = evalSampleModel.structuredSample;
      $scope.randomSample = evalSampleModel.randomSample;

      $scope.filledPages = function () {
        return evalSampleModel.getFilledPages();
      };

      $scope.auditSize = getSelected.length;
      $scope.anySelect = $scope.auditSize !== 0;

      $scope.openSelected = function () {
        getSelected()
          .forEach(function (page) {
            Page.openInWindow(page);
          });
      };

      $scope.openPage = Page.openInWindow;

      $scope.changeAll = function () {
        var pages = getPages();
        pages.forEach(function (page) {
          page.selected = $scope.anySelect;
        });
        $scope.sampleChange();
      };

      var previousSelection;
      $scope.multiSelect = function (index, event) {
        if (event.toElement.nodeName.toLowerCase() !== 'input') {
          return;
        }

        if (typeof previousSelection !== 'undefined' && event.shiftKey) {
          var pages = evalSampleModel.getFilledPages();
          var start = Math.min(previousSelection, index);
          var end = Math.max(previousSelection, index);
          var state = pages[index].selected;

          for (var i = start; i <= end; i++) {
            pages[i].selected = state;
          }
          $scope.sampleChange();
        }
        previousSelection = index;
      };

      $scope.sampleChange = function () {
    	var selected = getSelected().length;
    	$scope.auditSize = selected;
    	$scope.anySelect = selected > 0;
        $rootScope.$broadcast('audit:sample-change');
      };

      $scope.completePages = function () {
    	getSelected()
          .forEach(function (page) {
            page.tested = true;
          });
      };

      $scope.uncompletePages = function () {
    	getSelected()
          .forEach(function (page) {
            page.tested = false;
          });
      };
    }]
  );

'use strict';

angular.module('wcagReporter')
  .controller(
    'EvalReportCtrl',
    ["$scope", "appState", "evalReportModel", function ($scope, appState, evalReportModel) {
      $scope.state = appState.moveToState('report');
      $scope.reportModel = evalReportModel;
    }]
  );

'use strict';

angular.module('wcagReporter')

  .controller('ViewReportCtrl', ["$scope", "$location", "$document", "$http", "wcag2spec", "evalModel", "appState", "wcagReporterExport", "toggleCriterionText", function (
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

    $scope.saveAsXlsx = function () {
      try {

        $scope.loading = true;
        $scope.clickDownload = true;
      
        $http.get($scope.exportJsonUrl, {}).then(function onSuccess(response) {
          $http({
            //url: 'http://localhost:9001/ods',
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



    $scope.saveAsXlsx2 = function () {
      try {

        $scope.loadingxlsx = true;

        //console.log(doc);
        //console.log($scope);
        //Load JSON


        $http.get($scope.exportJsonUrl, {}).then(function onSuccess(response) {
          //console.log(response.data);
          $http({
            //url: 'http://localhost:9001/xlsx',
            url: $location.protocol() + "://" + $location.host() + ':' + $location.port() + "/xlsx",
            method: "POST",
            data: response,
            responseType: 'blob'
          }).then(function (response) {
            var data = response.data;
            var a = document.createElement("a");
            document.body.appendChild(a);

            var file = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
            var fileURL = window.URL.createObjectURL(file);
            a.href = fileURL;
            a.download = 'Informe_Revision_Profunidad_v1.xlsx';
            a.click();

            $scope.loadingxlsx = false;
          }).catch(function (response) {
            $scope.loadingxlsx = false;
            console.log('Unable to download the file')
          });

        });

      } catch (e) { if (typeof console != 'undefined') console.log(e, $scope.wbout); $scope.loading = false; }
      return $scope.wbout;
    };


    $scope.exportHtmlFile = wcagReporterExport.getFileName('html');
    $scope.exportJsonUrl = wcagReporterExport.getBlobUrl();
    $scope.exportJsonFile = wcagReporterExport.getFileName();
  }]);

'use strict';

angular.module('wcagReporter')
  .controller('OpenCtrl', ["$scope", "reportStorage", "evalLoader", "$rootScope", function ($scope, reportStorage, evalLoader, $rootScope) {
    $scope.postSettings = reportStorage.settings;
    $scope.fileFeedback = {
      posted: false, failures: false
    };
    $scope.evalFile = '';

    $scope.urlFeedback = {
      posted: false, failures: false
    };

    function handleLoad(defer, feedback) {
      feedback.posted = true;
      feedback.failure = false;

      defer.then(function success() {
        feedback.posted = false;
        $rootScope.setEvalLocation();
      }, function error(e) {
        feedback.posted = false;
        if (e.message) {
          feedback.failure = e.message;
        } else {
          feedback.failure = e;
        }
      });
    }

    $scope.loadFile = function (filePath) {
      var uploadResponse = evalLoader.openFromFile(filePath);
      handleLoad(uploadResponse, $scope.fileFeedback);
    };

    $scope.loadUrl = function () {
      handleLoad(evalLoader.openFromUrl($scope.postSettings.url), $scope.urlFeedback);
    };

    $scope.updateSettings = function () {
      reportStorage.updateSettings();
    };
  }]);

'use strict';

angular
  .module('wcagReporter')
  .controller('ImportCtrl', ["fileReader", "$scope", "$rootScope", "evalContextV3", "evalModel", "types", "isObjectLiteral", "wcagSpecIdMap", function (
    fileReader,
    $scope,
    $rootScope,
    evalContextV3,
    evalModel,
    types,
    isObjectLiteral,
    wcagSpecIdMap
  ) {
    var JSONLD = window.jsonld;
    var FEEDBACK = {
      ERROR: {
        type: 'error',
        class: 'danger'
      },
      PENDING: {
        type: 'pending',
        class: 'info'
      },
      SUCCESS: {
        type: 'success',
        class: 'success'
      }
    };

    $scope.assertionImport = [];

    $scope.allowedMime = [
      'application/json',
      'application/ld+json'
    ].join(',');

    $scope.feedback = false;
    $scope.importFile = undefined;
    $scope.importConfirmed = undefined;

    /**
     * Assertions that get imported need to be validated against
     * 1. test: should be directly known / related to WCAG
     * 2. subject should be related to one of the samples
     * 3. result: being an earl:TestResult
     * 4. assertedBy: Nice to know who / what made this assertion
     * @param  {earl:Assertion} assertion [description]
     * @return {boolean}           validity
     */
    function isValidAssertion (assertion) {
      function hasRequiredKeys (_assertion) {
        var assertionKeys = Object.keys(_assertion);
        var requiredKeys = [
          'test',
          'subject',
          'result',
          'assertedBy'
        ];

        var key;

        for (key in requiredKeys) {
          if (assertionKeys.indexOf(requiredKeys[key]) === -1) {
            return false;
          }
        }

        return true;
      }

      function isSampleRelated (subject) {
        var sampleUrls = evalModel.sampleModel.getPages()
          .map(function getUrls (page) {
            var pageUrl;

            if (page.source !== undefined) {
              try {
                pageUrl = new URL(page.source);
              } catch (e) {
                console.error(e);

                return page.source;
              }

              return pageUrl.href;
            }
          });

        var subjectUrl = '';

        if (typeof subject === 'string') {
          try {
            subjectUrl = new URL(subject).href;
          } catch (e) {
            console.error('Expected valid url in import assertion subject.');

            return false;
          }
        }

        if (
          isObjectLiteral(subject) &&
          subject.source !== undefined
        ) {
          try {
            subjectUrl = new URL(subject.source).href;
          } catch (e) {
            console.error('Expected valid url in import assertion subject.');

            return false;
          }
        }

        return (sampleUrls.indexOf(subjectUrl) >= 0);
      }

      function isWcagRelated (assertionTest) {
        if (
          typeof assertionTest === 'string' &&
          isWcagId(assertionTest)
        ) {
          return true;
        }

        if (
          isObjectLiteral(assertionTest) &&
          assertionTest.id !== undefined &&
          isWcagId(assertionTest.id)
        ) {
          setWcagId(assertion, assertionTest.id);
          return true;
        }

        if (
          isObjectLiteral(assertionTest) &&
          assertionTest.isPartOf !== undefined &&
          typeof assertionTest.isPartOf === 'string' &&
          isWcagId(assertionTest.isPartOf)
        ) {
          setWcagId(assertion, assertionTest.isPartOf);
          return true;
        }

        return false;
      }

      function hasResult (_assertion) {
        var result = _assertion.result;

        function hasOutcomeValue (_result) {
          var earlOutcome = types.EARL.OUTCOME;
          var outcomeValues = [
            earlOutcome.PASSED,
            earlOutcome.FAILED,
            earlOutcome.CANT_TELL,
            earlOutcome.INAPPLICABLE,
            earlOutcome.UNTESTED
          ];
          var outcomeClasses = [
            earlOutcome.PASS,
            earlOutcome.FAIL,
            earlOutcome.CANNOT_TELL,
            earlOutcome.NOT_APPLICABLE,
            earlOutcome.NOT_TESTED
          ];

          if (_result.outcome === undefined) {
            return false;
          }

          if (
            typeof _result.outcome === 'string' &&
            outcomeValues.indexOf(_result.outcome) >= 0
          ) {
            return true;
          }

          if (
            isObjectLiteral(_result.outcome) &&
            _result.outcome['@type'] !== undefined &&
            outcomeClasses.indexOf(_result.outcome['@type']) >= 0
          ) {
            return true;
          }
        }

        if (!hasOutcomeValue(result)) {
          return false;
        }

        return true;
      }

      if (!hasRequiredKeys(assertion)) {
        return false;
      }

      if (!isSampleRelated(assertion.subject)) {
        return false;
      }

      if (!isWcagRelated(assertion.test)) {
        return false;
      }

      if (!hasResult(assertion)) {
        return false;
      }

      return true;
    }

    function isWcagId (testId) {
      var _id = testId.split(':')[1];

      // Find existing wcag id
      return wcagSpecIdMap.some(function (wcagIdSet) {
        return wcagIdSet.indexOf(_id) >= 0;
      });
    }

    function upgradeWcagId (wcagId) {
      var _id = wcagId.split(':')[1];
      var wcagIdSet = wcagSpecIdMap.filter(function (idSet) {
        return idSet.indexOf(_id) >= 0;
      })[0];
      var idCount = wcagIdSet.length;

      return 'WCAG2:' + wcagIdSet[idCount - 1];
    }

    function setWcagId (assertion, wcagId) {
      var wcagVersion = wcagId.split(':')[0];

      if (wcagVersion !== 'WCAG2') {
        wcagId = upgradeWcagId(wcagId);
      }
      assertion.wcagId = wcagId;
    }

    function getWcagId (assertion) {
      if (typeof assertion.test === 'string') {
        return assertion.test;
      }

      return assertion.wcagId || false;
    }

    /**
     * Tries to insert all found assertions from the import
     * into the auditModel specific criteria
     */
    function insertAssertions () {
      var assertions = $scope.assertionImport;
      var assertionsCount = assertions.length;
      var assertion, wcagId;

      for (var i = 0; i < assertionsCount; i++) {
        assertion = assertions[i];
        wcagId = getWcagId(assertion);

        if (wcagId) {
          evalModel.auditModel.updateCritAssert(wcagId, assertion);
        }
      }

      $scope.feedback = FEEDBACK.SUCCESS;
      $scope.feedback.message = 'Import successfull! Imported ' + assertionsCount + ' assertions.';
    }

    function resetImport () {
      $scope.feedback = false;
      $scope.importFile = undefined;
      $scope.importConfirmed = undefined;
      $scope.assertionImport.length = 0;
    }

    function handleLoad (defer, feedback) {
      defer.then(
        function success (result) {
          var resultJson = JSON.parse(result);
          var context = angular.copy(evalContextV3);
          context.WCAG20 = 'https://www.w3.org/TR/WCAG20/#';
          context.isPartOf = {
            '@id': 'dct:isPartOf',
            '@type': '@id'
          };

          JSONLD.frame(
            resultJson,
            {
              '@context': context,
              '@graph': [
                {
                  '@type': 'Assertion'
                }
              ]
            },
            function (error, framed) {
              if (error) {
                feedback = FEEDBACK.ERROR;
                feedback.message = error.message;
                return;
              }

              var graph = framed['@graph'];
              var graphSize = graph.length;
              var currentAssertion;

              for (var i = 0; i < graphSize; i++) {
                currentAssertion = graph[i];

                if (isValidAssertion(currentAssertion)) {
                  $scope.assertionImport.push(currentAssertion);
                }
              }

              if ($scope.assertionImport.length > 0) {
                $scope.feedback = FEEDBACK.PENDING;
                $scope.feedback.message = 'Ready to import ' + $scope.assertionImport.length + ' assertions.';
              } else {
                $scope.feedback = FEEDBACK.ERROR;
                $scope.feedback.message = 'No Assertions found in file “' + $scope.importFile.name + '”';

                $scope.importFile = null;
              }

              $scope.$apply();
            }
          );
        },
        function error (e) {
          feedback = FEEDBACK.ERROR;
          if (e.message) {
            feedback.message = e.message;
          } else {
            feedback.message = e;
          }
        }
      );
    }

    function isJson (file) {
      if ($scope.allowedMime.indexOf(file.type) >= 0) {
        return true;
      }

      return false;
    }

    $scope.loadFile = function loadFile (source) {
      $scope.feedback = FEEDBACK.PENDING;

      if (!isJson(source)) {
        $scope.feedback = FEEDBACK.ERROR;
        $scope.feedback.message = 'Expected to open a json-file, the filename must end with either “.json” or “.jsonld”.';
        $scope.$apply();

        return;
      }

      $scope.importFile = {
        name: source.name
      };

      handleLoad(fileReader.readAsText(source, $scope), $scope.feedback);
    };

    $scope.handleConfirmation = function handleConfirmation (confirmed) {
      if (confirmed === undefined) {
        confirmed = false;
      }

      if (confirmed) {
        $scope.feedback = FEEDBACK.PENDING;
        $scope.feedback.message = 'Inserting ' + $scope.assertionImport.length + ' assertions from “' + $scope.importFile.name + '”';

        insertAssertions();

        $scope.importConfirmed = confirmed;
      } else {
        resetImport();
        $scope.feedback = FEEDBACK.PENDING;
        $scope.feedback.message = 'Import aborted. Choose another file or go back to the evaluation.';
      }
    };

    $scope.handleDoneClick = function handleDoneClick () {
      $rootScope.setEvalLocation();
    };
  }]);

'use strict';

angular.module('wcagReporter')
  .controller('SaveCtrl', ["$scope", "wcagReporterExport", "appState", function ($scope, wcagReporterExport, appState) {
    $scope.exportUrl = wcagReporterExport.getBlobUrl();
    $scope.exportFile = wcagReporterExport.getFileName();
    $scope.postSettings = wcagReporterExport.storage.settings;
    $scope.posted = false;
    $scope.failure = false;
    $scope.success = false;

    $scope.postJson = function () {
      $scope.posted = true;
      $scope.failure = false;

      wcagReporterExport.saveToUrl()
        .then(function () {
          $scope.success = true;
          $scope.posted = false;
        }, function (data) {
          $scope.failure = (data || true);
          $scope.posted = false;
        });
    };

    $scope.downloadStart = function () {
      wcagReporterExport.saveBlobIE();
      appState.setPrestineState();
    };

    $scope.updateSettings = function () {
      wcagReporterExport.storage.updateSettings();
    };
  }]);

'use strict';

angular.module('wcagReporter')
  .controller('ReportScoreCtrl', ["$scope", "wcag2spec", "evalAuditModel", function ($scope, wcag2spec, evalAuditModel) {
    $scope.principles = wcag2spec.getPrinciples();
    var totals = {
      'earl:passed': 0,
      'earl:failed': 0,
      'earl:inapplicable': 0,
      'earl:untested': 0,
      'earl:cantTell': 0,
      level_a: { pass: 0, total: 0 },
      level_aa: { pass: 0, total: 0 },
      level_aaa: { pass: 0, total: 0 }
    };
    $scope.totals = totals;

    $scope.getScores = function () {
      return wcag2spec.getPrinciples()
        .map(function (p) {
          var result = {
            name: p.num + '. ' + p.handle,
            'earl:passed': 0,
            'earl:failed': 0,
            'earl:inapplicable': 0,
            'earl:untested': 0,
            'earl:cantTell': 0,
            tested: 0,
            level_a: { pass: 0, total: 0 },
            level_aa: { pass: 0, total: 0 },
            level_aaa: { pass: 0, total: 0 }
          };

          // Get all criteria of this principle:
          p.guidelines.reduce(function (list, guide) {
            list.push.apply(list, guide.successcriteria);
            return list;
          }, [])
            .forEach(function (crit) {
              // For each, set the result
              var critResult = evalAuditModel.getCritAssert(crit.id);
              if (critResult) {
                var outcome = critResult.result.outcome;

                var level = crit.level
                  .replace('wai:WCAG2', '')
                  .replace('-Conformance', '')
                  .toLowerCase();

                result[outcome] += 1;
                $scope.totals[outcome] += 1;
                if (outcome === 'earl:passed' ||
                        outcome === 'earl:inapplicable') {
                  result['level_' + level].pass += 1;
                  totals['level_' + level].pass += 1;
                }
                if (outcome !== 'earl:untested') {
                  result.tested += 1;
                  result['level_' + level].total += 1;
                  totals['level_' + level].total += 1;
                }
              }
            });
          return result;
        });
    };

    $scope.scores = $scope.getScores();
    // Update the score name when the language changes
    $scope.$on('wcag2spec:langChange', function () {
      $scope.scores = $scope.getScores();
    });
  }]);

'use strict';

angular.module('wcagReporter')
  .controller(
    'ReportFindingsCtrl',
    ["$scope", "wcag2spec", "evalAuditModel", "evalScopeModel", "CriterionAssert", function ($scope, wcag2spec, evalAuditModel, evalScopeModel, CriterionAssert) {
      if (wcag2spec.isLoaded()) {
        $scope.principles = wcag2spec.getPrinciples();
      } else {
        $scope.principles = [];
        wcag2spec.onLangChange(function () {
          $scope.principles = wcag2spec.getPrinciples();
        });
      }

      $scope.auditModel = evalAuditModel;
      $scope.critOpt = {
        editable: false,
        collapsed: false,
        showallpages: false,
        hideCollapseBtn: true
      };

      $scope.getCritAssert = evalAuditModel.getCritAssert;
      $scope.shouldCritRender = function (critSpec) {
        if (evalScopeModel.matchConformTarget(critSpec.level)) {
          return true;
        } else {
          var critAssert = $scope.getCritAssert(critSpec.id);
          return CriterionAssert.isDefined(critAssert);
        }
      };
    }]
  );
