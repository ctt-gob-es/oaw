'use strict';

angular.module('wcagReporter')
  .service('evalScopeModel', function (
    thematicScopes,
    evalSampleModel
  ) {
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
        siteScope: '',
        uraName: '',
        uraDIR3: '',
        uraScope: '',
        scopeDIR3: '',
        responsibleEntity: '',
        responsibleEntityDIR3: '',
        responsiblePerson: '',
        typology: '',
        basicFunctionality: '',
        revisionDate: '',
        territorialScope: '',
        url: '',
        evaluationType: '',
        evaluationCompany: '',
        observations: ''
      },
      thematicScopes: thematicScopes,
      accessibilitySupportBaseline: '',
      reliedUponThematic: []
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
          siteScope: scopeModel.website.siteScope,
          uraName: scopeModel.website.uraName,
          uraDIR3: scopeModel.website.uraDIR3,
          uraScope: scopeModel.website.uraScope,
          scopeDIR3: scopeModel.website.scopeDIR3,
          responsibleEntity: scopeModel.website.responsibleEntity,
          responsibleEntityDIR3: scopeModel.website.responsibleEntityDIR3,
          responsiblePerson: scopeModel.website.responsiblePerson,
          typology: scopeModel.website.typology,
          basicFunctionality: scopeModel.website.basicFunctionality,
          revisionDate: scopeModel.website.revisionDate,
          territorialScope: scopeModel.website.territorialScope,
          url: scopeModel.website.url,
          evaluationType: scopeModel.website.evaluationType,
          evaluationCompany: scopeModel.website.evaluationCompany,
          observations: scopeModel.website.observations,
        },
        accessibilitySupportBaseline: scopeModel.accessibilitySupportBaseline,
        reliedUponThematic: scopeModel.reliedUponThematic
      };
    };

    scopeModel.importData = function (evalData) {
      scopeModel.reliedUponThematic = evalData.evaluationScope.reliedUponThematic;
    };

    scopeModel.wcagVersionOptions = [
      'WCAG21',
      'WCAG20'
    ];

    scopeModel.typologyOptions = [
      'TYPO1',
      'TYPO2'
    ];


    scopeModel.territorialScopeOptions = [
      'TERRITORIAL_AGE',
      'TERRITORIAL_CCAA',
      'TERRITORIAL_EELL',
      'TERRITORIAL_OTHERS'
    ];


    scopeModel.conformanceOptions = [
      'wai:WCAG2A-Conformance',
      'wai:WCAG2AA-Conformance',
      'wai:WCAG2AAA-Conformance'
    ];

    scopeModel.evaluationTypeOptions = [
      'EVALUATION_TYPE_1',
      'EVALUATION_TYPE_2',
      'EVALUATION_TYPE_3'
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
