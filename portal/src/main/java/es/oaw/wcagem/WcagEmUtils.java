package es.oaw.wcagem;

import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.util.MessageResources;

import es.gob.oaw.rastreador2.observatorio.ObservatoryManager;
import es.gob.oaw.rastreador2.pdf.utils.CheckDescriptionsManager;
import es.inteco.common.Constants;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.intav.form.ProblemForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.oaw.wcagem.enums.WcagEmPointKey;
import es.oaw.wcagem.util.ValidationDetails;
import es.oaw.wcagem.util.ValidationResult;

/**
 * The Class WcagEmUtils.
 * 
 * This class generates {@link WcagEmReport} with a {@link ObservatoryEvaluationForm} data to generate a JSON
 */
public final class WcagEmUtils {
	/** The Constant _1_6. */
	public static final String _1_6 = "1.6";
	/** The Constant _1_5. */
	public static final String _1_5 = "1.5";
	/** The Constant _1_4. */
	public static final String _1_4 = "1.4";
	/** The Constant _1_3. */
	public static final String _1_3 = "1.3";
	/** The Constant _1_2. */
	public static final String _1_2 = "1.2";
	/** The Constant _1_10. */
	public static final String _1_10 = "1.10";
	/** The Constant _2_6. */
	public static final String _2_6 = "2.6";
	/** The Constant _1_13. */
	public static final String _1_13 = "1.13";
	/** The Constant _1_14. */
	public static final String _1_14 = "1.14";
	/** The Constant _2_1. */
	public static final String _2_1 = "2.1";
	/** The Constant _1_7. */
	public static final String _1_7 = "1.7";
	/** The Constant _1_9. */
	public static final String _1_9 = "1.9";
	/** The Constant _2_4. */
	public static final String _2_4 = "2.4";
	/** The Constant _1_12. */
	public static final String _1_12 = "1.12";
	/** The Constant _1_11. */
	public static final String _1_11 = "1.11";
	/** The Constant _1_8. */
	public static final String _1_8 = "1.8";
	/** The Constant _2_2. */
	public static final String _2_2 = "2.2";
	/** The Constant _2_3. */
	public static final String _2_3 = "2.3";
	/** The Constant _2_5. */
	public static final String _2_5 = "2.5";
	/** The Constant _1_1. */
	public static final String _1_1 = "1.1";
	/** The Constant EARL_INAPPLICABLE. */
	public static final String EARL_INAPPLICABLE = "earl:inapplicable";
	/** The Constant EARL_FAILED. */
	public static final String EARL_FAILED = "earl:failed";
	/** The Constant EARL_PASSED. */
	public static final String EARL_PASSED = "earl:passed";
	/** The Constant EARL_CANNOT_TELL. */
	public static final String EARL_CANNOT_TELL = "earl:cantTell";
	/** The Constant EARL_UNTESTED. */
	public static final String EARL_UNTESTED = "earl:untested";

	/**
	 * Generate report.
	 *
	 * @param messageResources the message resources
	 * @param pdfBuilder       the pdf builder
	 * @param siteUrl          the site url
	 * @param analisisId       the analisis id
	 * @return the wcag em report
	 * @throws Exception the exception
	 */
	public static WcagEmReport generateReport(final MessageResources messageResources, final AnonymousResultExportPdf pdfBuilder, final String siteUrl, Long analisisId) throws Exception {
		WcagEmReport wcagEmReport = new WcagEmReport();
		// Get scores
		final ObservatoryManager observatoryManager = new ObservatoryManager();
		final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), analisisId);
		// Pagaes
		List<ObservatoryEvaluationForm> currentEvaluationPageList = new ArrayList<>();
		final List<ObservatoryEvaluationForm> tmp = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
		if (tmp.size() > 35) {
			currentEvaluationPageList.addAll(tmp.subList(0, 35));
		} else {
			currentEvaluationPageList.addAll(tmp);
		}
		// This map store, the url and a map with everi wcag automatic validation an result
		Map<String, Map<String, ValidationDetails>> wcagCompliance = generateEquivalenceMap(currentEvaluationPageList);
		// Generate analysis info
		Graph graph = new Graph();
		{
			Context context = new Context();
			context.setVocab("http://www.w3.org/TR/WCAG-EM/#");
			context.setReporter("https://github.com/w3c/wcag-em-report-tool/");
			context.setWcagem("http://www.w3.org/TR/WCAG-EM/#");
			context.setWCAG2("http://www.w3.org/TR/WCAG21/#");
			context.setEarl("http://www.w3.org/ns/earl#");
			context.setDct("http://purl.org/dc/terms/");
			context.setWai("http://www.w3.org/WAI/");
			context.setSch("http://schema.org/");
			context.setEvaluation("wcagem:Evaluation");
			context.setEvaluationScope("wcagem:EvaluationScope");
			context.setTestSubject("earl:TestSubject");
			context.setWebSite("sch:WebSite");
			context.setSample("wcagem:Sample");
			context.setWebPage("sch:WebPage");
			context.setTechnology("WCAG2:dfn-technologies");
			context.setAssertion("earl:Assertion");
			context.setAssertor("earl:Assertor");
			context.setTestResult("earl:TestResult");
			context.setTitle("dct:title");
			context.setSummary("dct:summary");
			{
				Creator creator = new Creator();
				creator.setId("dct:creator");
				creator.setType("@id");
				context.setCreator(creator);
			}
			context.setDate("dct:date");
			context.setCommissioner("wcagem:commissioner");
			context.setReliedUponTechnology("WCAG2:dfn-relied-upon");
			context.setEvaluationScope2("step1");
			context.setCommonPages("step2a");
			context.setEssentialFunctionality("step2b");
			context.setPageTypeVariety("step2c");
			context.setOtherRelevantPages("step2e");
			context.setStructuredSample("step3a");
			context.setRandomSample("step3b");
			context.setAuditResult("step4");
			context.setSpecifics("step5b");
			{
				Publisher publisher = new Publisher();
				publisher.setId("dct:publisher");
				publisher.setType("@id");
				context.setPublisher(publisher);
			}
			{
				ConformanceTarget conformanceTarget = new ConformanceTarget();
				conformanceTarget.setId("step1b");
				conformanceTarget.setType("@id");
				context.setConformanceTarget(conformanceTarget);
			}
			context.setAccessibilitySupportBaseline("step1c");
			context.setAdditionalEvalRequirement("step1d");
			context.setWebsite("WCAG2:dfn-set-of-web-pages");
			context.setSiteScope("step1a");
			context.setSiteName("sch:name");
			context.setWebpage("WCAG2:dfn-web-page-s");
			context.setDescription("dct:description");
			{
				Source source = new Source();
				source.setId("dct:source");
				source.setType("@id");
				context.setSource(source);
			}
			context.setTested("reporter:blob/master/docs/EARL%2BJSON-LD.md#tested");
			{
				Test test = new Test();
				test.setId("earl:test");
				test.setType("@id");
				context.setTest(test);
			}
			{
				AssertedBy assertedBy = new AssertedBy();
				assertedBy.setId("earl:assertedBy");
				assertedBy.setType("@id");
				context.setAssertedBy(assertedBy);
			}
			{
				Subject subject = new Subject();
				subject.setId("earl:subject");
				subject.setType("@id");
				context.setSubject(subject);
			}
			context.setResult("earl:result");
			{
				Mode mode = new Mode();
				mode.setId("earl:mode");
				mode.setType("@id");
				context.setMode(mode);
			}
			context.setHasPart("dct:hasPart");
			{
				Outcome outcome = new Outcome();
				outcome.setId("earl:outcome");
				outcome.setType("@id");
				context.setOutcome(outcome);
			}
			context.setId("@id");
			context.setType("@type");
			context.setLang("@language");
			graph.setContext(context);
		}
		graph.setType("Evaluation");
		graph.setPublisher("reporter:releases/tag/<%= pkg.version =%>");
		graph.setLang("es");
		{
			EvaluationScope evaluationScope = new EvaluationScope();
			evaluationScope.setType("EvaluationScope");
			evaluationScope.setConformanceTarget("wai:WCAG2AA-Conformance");
			evaluationScope.setAdditionalEvalRequirement("");
			{
				Website website = new Website();
				website.setType(Arrays.asList(new String[] { "TestSubject", "WebSite" }));
				website.setId("_:website");
				// website.setSiteName(siteUrl);
				website.setSiteName("");
				website.setSiteScope("");
				evaluationScope.setWebsite(website);
			}
			evaluationScope.setAccessibilitySupportBaseline("");
			graph.setEvaluationScope(evaluationScope);
		}
		{
			List<AuditResult> auditResults = new ArrayList<>();
			for (WcagEmPointKey wcagEmPointKey : WcagEmPointKey.values()) {
				AuditResult auditResult = new AuditResult();
				auditResult.setType("Assertion");
				auditResult.setTest(wcagEmPointKey.getWcagEmId());
				auditResult.setAssertedBy("_:evaluator");
				auditResult.setSubject("_:website");
				{
					Result result = new Result();
					DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyy");
					result.setDate(fmt.format(OffsetDateTime.now(ZoneId.of("Europe/Madrid"))));
					result.setDescription("");
					result.setType("TestResult");
					// By default, we mark all as cannot tell because this an automatic analisys that not cover all WCAG verfification point
					result.setOutcome(EARL_UNTESTED);
					auditResult.setResult(result);
				}
				auditResult.setMode("earl:automatic");
				{
					List<HasPart> hasParts = new ArrayList<HasPart>();
					// Iterate WCAG Points
					if (wcagCompliance != null && !wcagCompliance.isEmpty()) {
						int pageCounter = 0;
						// Iterate evl list to preserve order
						for (ObservatoryEvaluationForm eval : currentEvaluationPageList) {
							Map<String, ValidationDetails> result = wcagCompliance.get(eval.getUrl());
							// if cointain current wcag rule
							if (result.containsKey(wcagEmPointKey.getWcagEmId())) {
								HasPart hasPart = new HasPart();
								hasPart.setType("Assertion");
								hasPart.setAssertedBy("_:evaluator");
								hasPart.setSubject(Arrays.asList(new String[] { "_:struct_" + pageCounter }));
								{
									Result_ resultP = new Result_();
									resultP.setType("TestResult");
									resultP.setDate(OffsetDateTime.now(ZoneId.of("Europe/Madrid")).toString());
									// Result from Details
									final List<ValidationResult> results = result.get(wcagEmPointKey.getWcagEmId()).getResults();
									if (results != null && !results.isEmpty()) {
										resultP.setDescription(printProblemsAsTable(results, messageResources));
									} else {
										resultP.setDescription("");
									}
									final String validationResult = result.get(wcagEmPointKey.getWcagEmId()).getResult();
									resultP.setOutcome(validationResult);
									hasPart.setResult(resultP);
									// if one of this has earl:failed, all result marked as failed
									// Modified, by default preservate EARL_UNTESTED
//									if (EARL_FAILED.equals(validationResult)) {
//										auditResult.getResult().setOutcome(EARL_FAILED);
//									} else {
//										auditResult.getResult().setOutcome(EARL_CANNOT_TELL);
//									}
								}
								hasPart.setMultiPage(false);
								hasPart.setMode("earl:automatic");
								hasPart.setTestcase(wcagEmPointKey.getWcagEmId());
								hasParts.add(hasPart);
								pageCounter++;
							}
						}
					}
					auditResult.setHasPart(hasParts);
				}
				auditResults.add(auditResult);
			}
			graph.setAuditResult(auditResults);
		}
		graph.setCreator("_:evaluator");
		if (currentEvaluationPageList != null && currentEvaluationPageList.size() > 0) {
			graph.setTitle("Informe de revisión de accesibilidad - " + currentEvaluationPageList.get(0).getEntity());
		} else {
			graph.setTitle("Informe de revisión de accesibilidad");
		}
		graph.setCommissioner("");
		{
			StructuredSample structuredSample = new StructuredSample();
			List<Webpage> webpages = new ArrayList<>();
			List<NoWebpage> noWebpages = new ArrayList<>();
			int randCounter = 0;
			int randCounterNoWeb = 0;
			// Iterate currentEvaluationPageList to preserve order
			for (ObservatoryEvaluationForm eval : currentEvaluationPageList) {
				Webpage webpage = new Webpage();
				webpage.setType(Arrays.asList(new String[] { "TestSubject", "WebPage" }));
				webpage.setId("_:struct_" + randCounter);
				webpage.setDescription(eval.getUrl());
				webpage.setSource(eval.getUrl());
				webpage.setTitle(BasicServiceUtils.getTitleDocFromContent(eval.getSource(), false));
				webpage.setTested(false);// false to mark as incomplete un report step
				webpages.add(webpage);
				randCounter++;
			}
			structuredSample.setWebpage(webpages);
			graph.setStructuredSample(structuredSample);
		}
		{
			RandomSample randomSample = new RandomSample();
			List<Webpage_> webpages = new ArrayList<>();
			randomSample.setWebpage(webpages);
			graph.setRandomSample(randomSample);
		}
		{
			List<ReliedUponTechnology> reliedUponTechnologies = new ArrayList<>();
			ReliedUponTechnology html5 = new ReliedUponTechnology();
			html5.setId("http://www.w3.org/TR/html5/");
			html5.setType("Technology");
			html5.setTitle("HTML5");
			reliedUponTechnologies.add(html5);
			ReliedUponTechnology css = new ReliedUponTechnology();
			css.setId("http://www.w3.org/Style/CSS/specs/");
			css.setType("Technology");
			css.setTitle("CSS");
			reliedUponTechnologies.add(css);
			ReliedUponTechnology wai = new ReliedUponTechnology();
			wai.setId("http://www.w3.org/TR/wai-aria/");
			wai.setType("Technology");
			wai.setTitle("WAI-ARIA");
			reliedUponTechnologies.add(wai);
			graph.setReliedUponTechnology(reliedUponTechnologies);
		}
		List<Graph> graphs = new ArrayList<>();
		graphs.add(graph);
		Graph graph2 = new Graph();
		Context context2 = new Context();
		context2.setVocab("http://xmlns.com/foaf/0.1/");
		context2.setId("@id");
		context2.setType("@type");
		graph2.setContext(context2);
		graph2.setId("_:evaluator");
		graph2.setType("Software");
		graph2.setName("Observatorio de Accesibilidad Web (OAW)");
		graphs.add(graph2);
		wcagEmReport.setGraph(graphs);
		return wcagEmReport;
	}

	/**
	 * Generate equivalence map.
	 *
	 * @param currentEvaluationPageList the current evaluation page list
	 * @return the map
	 */
	public static Map<String, Map<String, ValidationDetails>> generateEquivalenceMap(final List<ObservatoryEvaluationForm> currentEvaluationPageList) {
		Map<String, Map<String, ValidationDetails>> wcagCompliance = new TreeMap<>();
		for (ObservatoryEvaluationForm observatoryEvaluationForm : currentEvaluationPageList) {
			String url = observatoryEvaluationForm.getUrl();
			Map<String, ObservatorySubgroupForm> tmp = new TreeMap<>();
			// Process groups
			for (ObservatorySuitabilityForm suitabilityForm : observatoryEvaluationForm.getGroups().get(0).getSuitabilityGroups()) {
				for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
					final String name = subgroupForm.getDescription()
							.substring(subgroupForm.getDescription().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length());
					tmp.put(name, subgroupForm);
				}
			}
			for (ObservatorySuitabilityForm suitabilityForm : observatoryEvaluationForm.getGroups().get(1).getSuitabilityGroups()) {
				for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
					final String name = subgroupForm.getDescription()
							.substring(subgroupForm.getDescription().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length());
					tmp.put(name, subgroupForm);
				}
			}
			Map<String, ValidationDetails> tmpWcag = new TreeMap<>();
			// Match oaw validations with wcag (this url)
			/*
			 * This OAW verification points only matchs with a single WCAG verfication
			 */
			// Check 1.1.1
			processSimpleVerification(tmpWcag, tmp.get(_1_1), WcagEmPointKey.WCAG_1_1_1.getWcagEmId(), false);
			processSimpleVerification(tmpWcag, tmp.get(_2_3), WcagEmPointKey.WCAG_1_4_10.getWcagEmId(), false);
			// Check 2.4.4
			processSimpleVerification(tmpWcag, tmp.get(_1_12), WcagEmPointKey.WCAG_2_4_4.getWcagEmId(), false);
			// Check 2.4.5
			processSimpleVerification(tmpWcag, tmp.get(_2_4), WcagEmPointKey.WCAG_2_4_5.getWcagEmId(), false);
			// Check 3.1.1
			processSimpleVerification(tmpWcag, tmp.get(_1_7), WcagEmPointKey.WCAG_3_1_1.getWcagEmId(), false);
			// Check 3.1.2
			processSimpleVerification(tmpWcag, tmp.get(_2_1), WcagEmPointKey.WCAG_3_1_2.getWcagEmId(), false);
			// Check 3.2.3
			processSimpleVerification(tmpWcag, tmp.get(_2_6), WcagEmPointKey.WCAG_3_2_3.getWcagEmId(), false);
			// Check 1.1.1
			processSimpleVerification(tmpWcag, tmp.get(_1_14), WcagEmPointKey.WCAG_4_1_1.getWcagEmId(), false);
			/*
			 * This OAW verification points only matchs partial WCAG verfication
			 */
			// This verifications cannot assign directy to wcag, depends on check failed
			// Check 2.1.1
			processSimpleVerification(tmpWcag, tmp.get(_1_8), WcagEmPointKey.WCAG_2_1_1.getWcagEmId(), true);
			// Check 2.2.1
			processSimpleVerification(tmpWcag, tmp.get(_1_8), WcagEmPointKey.WCAG_2_2_1.getWcagEmId(), true);
			// Check 2.2.2
			processSimpleVerification(tmpWcag, tmp.get(_1_8), WcagEmPointKey.WCAG_2_2_2.getWcagEmId(), true);
			// Check 3.3.2
			processSimpleVerification(tmpWcag, tmp.get(_1_9), WcagEmPointKey.WCAG_3_3_2.getWcagEmId(), true);
			// Check 2.5.3
			processSimpleVerification(tmpWcag, tmp.get(_1_9), WcagEmPointKey.WCAG_2_5_3.getWcagEmId(), true);
			// Check 2.4.1
			processSimpleVerification(tmpWcag, tmp.get(_1_11), WcagEmPointKey.WCAG_2_4_1.getWcagEmId(), true);
			// Check 2.4.2
			processSimpleVerification(tmpWcag, tmp.get(_1_11), WcagEmPointKey.WCAG_2_4_2.getWcagEmId(), true);
			// Check 3.2.1
			processSimpleVerification(tmpWcag, tmp.get(_1_13), WcagEmPointKey.WCAG_3_2_1.getWcagEmId(), true);
			// Check 3.2.2
			processSimpleVerification(tmpWcag, tmp.get(_1_13), WcagEmPointKey.WCAG_3_2_2.getWcagEmId(), true);
			// Check 1.4.12
			processSimpleVerification(tmpWcag, tmp.get(_2_2), WcagEmPointKey.WCAG_1_4_12.getWcagEmId(), true);
			// Check 1.4.3
			processSimpleVerification(tmpWcag, tmp.get(_2_2), WcagEmPointKey.WCAG_1_4_3.getWcagEmId(), true);
			// Check 1.3.4
			processSimpleVerification(tmpWcag, tmp.get(_2_5), WcagEmPointKey.WCAG_1_3_4.getWcagEmId(), true);
			// Check 1.3.5
			processSimpleVerification(tmpWcag, tmp.get(_2_5), WcagEmPointKey.WCAG_1_3_5.getWcagEmId(), true);
			// Check 2.4.3
			processSimpleVerification(tmpWcag, tmp.get(_2_5), WcagEmPointKey.WCAG_2_4_3.getWcagEmId(), true);
			// Check 2.4.7
			processSimpleVerification(tmpWcag, tmp.get(_2_5), WcagEmPointKey.WCAG_2_4_7.getWcagEmId(), true);
			/*
			 * This OAW verification points only matchs with multiple WCAG verfication
			 */
			/**
			 * Multiple verifications involved
			 */
			// Check 1.1.1
			List<ObservatorySubgroupForm> verifications = new ArrayList<ObservatorySubgroupForm>();
			verifications.add(tmp.get(_1_2));
			verifications.add(tmp.get(_1_3));
			verifications.add(tmp.get(_1_4));
			verifications.add(tmp.get(_1_5));
			verifications.add(tmp.get(_1_6));
			verifications.add(tmp.get(_1_9));
			verifications.add(tmp.get(_1_10));
			processMultipleVerification(tmpWcag, verifications, WcagEmPointKey.WCAG_1_3_1.getWcagEmId(), true);
			// Check 4.1.2
			verifications = new ArrayList<ObservatorySubgroupForm>();
			verifications.add(tmp.get(_1_8));
			verifications.add(tmp.get(_1_9));
			verifications.add(tmp.get(_1_10));
			verifications.add(tmp.get(_1_11));
			processMultipleVerification(tmpWcag, verifications, WcagEmPointKey.WCAG_4_1_2.getWcagEmId(), true);
			// Add to globall
			wcagCompliance.put(url, tmpWcag);
		}
		return wcagCompliance;
	}

	/**
	 * Process simple verification.
	 *
	 * @param tmpWcag                 Map of verification details to add result
	 * @param observatorySubgroupForm Verification result
	 * @param wcagEmId                Id of WCAG verification point on WCAG EM Tool
	 * @param filterChecks            If true, is mandatory to filter checks and onlu process that are related with WCAG Point
	 */
	private static void processSimpleVerification(Map<String, ValidationDetails> tmpWcag, final ObservatorySubgroupForm observatorySubgroupForm, final String wcagEmId, boolean filterChecks) {
		ValidationDetails validationDetailes = new ValidationDetails();
		List<ValidationResult> results = new ArrayList<>();
		Map<String, List<String>> checkWcagRelationMap = checkWcagRelationMap();
		// Check if problems coitains any check that relationates wcag
		switch (observatorySubgroupForm.getValue()) {
		case Constants.OBS_VALUE_GREEN_ZERO:
			// We can asegurte automatic PASS
			if (filterChecks) {
				filterObservatorySubgroupForm(tmpWcag, observatorySubgroupForm, wcagEmId, validationDetailes, results, checkWcagRelationMap, EARL_CANNOT_TELL);
			} else {
				validationDetailes.setResult(EARL_CANNOT_TELL);
				tmpWcag.put(wcagEmId, validationDetailes);
			}
			break;
		case Constants.OBS_VALUE_RED_ZERO:
			if (filterChecks) {
				filterObservatorySubgroupForm(tmpWcag, observatorySubgroupForm, wcagEmId, validationDetailes, results, checkWcagRelationMap, EARL_CANNOT_TELL);
			} else {
				validationDetailes.setResult(EARL_FAILED);
				processChecks(observatorySubgroupForm, validationDetailes, results);
				tmpWcag.put(wcagEmId, validationDetailes);
			}
			break;
		case Constants.OBS_VALUE_GREEN_ONE:
			if (filterChecks) {
				filterObservatorySubgroupForm(tmpWcag, observatorySubgroupForm, wcagEmId, validationDetailes, results, checkWcagRelationMap, EARL_CANNOT_TELL);
			} else {
				validationDetailes.setResult(EARL_CANNOT_TELL);
				results = new ArrayList<>();
				processChecks(observatorySubgroupForm, validationDetailes, results);
				tmpWcag.put(wcagEmId, validationDetailes);
			}
			break;
		case Constants.OBS_VALUE_NOT_SCORE:
			if (filterChecks) {
				filterObservatorySubgroupForm(tmpWcag, observatorySubgroupForm, wcagEmId, validationDetailes, results, checkWcagRelationMap, EARL_CANNOT_TELL);
			} else {
				validationDetailes.setResult(EARL_CANNOT_TELL);
				processChecks(observatorySubgroupForm, validationDetailes, results);
				tmpWcag.put(wcagEmId, validationDetailes);
			}
			break;
		}
	}

	/**
	 * Filter observatory subgroup form. Removes checks nat not related with WCAG point passed as param
	 *
	 * @param tmpWcag                 Map of verification details to add result
	 * @param observatorySubgroupForm Verification result
	 * @param wcagEmId                Id of WCAG verification point on WCAG EM Tool
	 * @param validationDetailes      the validation detailes
	 * @param results                 the results
	 * @param checkWcagRelationMap    Map that link OAW checks with WCAG verification points
	 * @param result                  the result
	 */
	private static void filterObservatorySubgroupForm(Map<String, ValidationDetails> tmpWcag, final ObservatorySubgroupForm observatorySubgroupForm, final String wcagEmId,
			ValidationDetails validationDetailes, List<ValidationResult> results, Map<String, List<String>> checkWcagRelationMap, final String result) {
		List<Integer> onlyWarnings = observatorySubgroupForm.getOnlyWarningChecks();
		List<Integer> onlyWarningsRelatedThisWcagPoint = new ArrayList<>();
		if (onlyWarnings != null) {
			for (Integer warning : onlyWarnings) {
				if (checkWcagRelationMap.get(wcagEmId) != null && ((List<String>) checkWcagRelationMap.get(wcagEmId)).contains(warning.toString())) {
					onlyWarningsRelatedThisWcagPoint.add(warning);
				}
			}
		}
		List<Integer> notExecuted = observatorySubgroupForm.getNotExecutedChecks();
		List<Integer> notExecutedrealtedThisWcagPoint = new ArrayList<>();
		if (notExecuted != null) {
			for (Integer notEx : notExecuted) {
				if (checkWcagRelationMap.get(wcagEmId) != null && ((List<String>) checkWcagRelationMap.get(wcagEmId)).contains(notEx.toString())) {
					notExecutedrealtedThisWcagPoint.add(notEx);
				}
			}
		}
		List<Integer> successChecks = observatorySubgroupForm.getSuccessChecks();
		List<Integer> successRelatedThisWcagPoint = new ArrayList<>();
		if (successChecks != null) {
			for (Integer success : successChecks) {
				if (checkWcagRelationMap.get(wcagEmId) != null && ((List<String>) checkWcagRelationMap.get(wcagEmId)).contains(success.toString())) {
					successRelatedThisWcagPoint.add(success);
				}
			}
		}
		List<ProblemForm> problems = observatorySubgroupForm.getProblems();
		List<ProblemForm> problemsrealtedThisWcagPoint = new ArrayList<>();
		if (problems != null) {
			for (ProblemForm problem : problems) {
				if (checkWcagRelationMap.get(wcagEmId) != null && ((List<String>) checkWcagRelationMap.get(wcagEmId)).contains(problem.getCheck())) {
					problemsrealtedThisWcagPoint.add(problem);
				}
			}
		}
		// Only if had checks related
		if (!problemsrealtedThisWcagPoint.isEmpty() || !successRelatedThisWcagPoint.isEmpty() || !notExecutedrealtedThisWcagPoint.isEmpty()) {
			ObservatorySubgroupForm filteredObservatorySubgroupForm = new ObservatorySubgroupForm();
			try {
				BeanUtils.copyProperties(filteredObservatorySubgroupForm, observatorySubgroupForm);
				filteredObservatorySubgroupForm.setProblems(problemsrealtedThisWcagPoint);
				filteredObservatorySubgroupForm.setSuccessChecks(successRelatedThisWcagPoint);
				filteredObservatorySubgroupForm.setNotExecutedChecks(notExecutedrealtedThisWcagPoint);
				if (!problemsrealtedThisWcagPoint.isEmpty()) {
					validationDetailes.setResult(EARL_FAILED);
				} else if (!EARL_FAILED.equals(validationDetailes.getResult())) {
					validationDetailes.setResult(result);
				}
				processChecks(filteredObservatorySubgroupForm, validationDetailes, results);
				tmpWcag.put(wcagEmId, validationDetailes);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Process checks.
	 *
	 * @param observatorySubgroupForm the observatory subgroup form
	 * @param validationDetailes      the validation detailes
	 * @param results                 the results
	 */
	private static void processChecks(final ObservatorySubgroupForm observatorySubgroupForm, ValidationDetails validationDetailes, List<ValidationResult> results) {
		// Not Executed
		if (observatorySubgroupForm.getNotExecutedChecks() != null && !observatorySubgroupForm.getNotExecutedChecks().isEmpty()) {
			for (Integer notExecuted : observatorySubgroupForm.getNotExecutedChecks()) {
				if (notExecuted != null) {
					ValidationResult validationResult = processNotExecuted(observatorySubgroupForm, notExecuted);
					results.add(validationResult);
				}
			}
			validationDetailes.setResults(results);
		}
		// Problems
		if (observatorySubgroupForm.getProblems() != null && !observatorySubgroupForm.getProblems().isEmpty()) {
			for (ProblemForm problem : observatorySubgroupForm.getProblems()) {
				ValidationResult validationResult = processError(observatorySubgroupForm, problem);
				results.add(validationResult);
			}
			validationDetailes.setResults(results);
		}
		// Success
		if (observatorySubgroupForm.getSuccessChecks() != null && !observatorySubgroupForm.getSuccessChecks().isEmpty()) {
			for (Integer successCheck : observatorySubgroupForm.getSuccessChecks()) {
				if (successCheck != null) {
					ValidationResult validationResult = processSuccess(observatorySubgroupForm, successCheck);
					results.add(validationResult);
				}
			}
			validationDetailes.setResults(results);
		}
	}

	/**
	 * Process error.
	 *
	 * @param observatorySubgroupForm the observatory subgroup form
	 * @param problem                 the problem
	 * @return the validation result
	 */
	private static ValidationResult processError(final ObservatorySubgroupForm observatorySubgroupForm, ProblemForm problem) {
		ValidationResult validationResult = new ValidationResult();
		validationResult.setOawVerification(observatorySubgroupForm.getDescription());
		validationResult.setOawDescription(problem.getError());
		validationResult.setResult(EARL_FAILED);
		validationResult.setProblem(problem);
		return validationResult;
	}

	/**
	 * Process success.
	 *
	 * @param observatorySubgroupForm the observatory subgroup form
	 * @param check                   the check
	 * @return the validation result
	 */
	private static ValidationResult processSuccess(final ObservatorySubgroupForm observatorySubgroupForm, Integer check) {
		ValidationResult validationResult = new ValidationResult();
		validationResult.setOawVerification(observatorySubgroupForm.getDescription());
		validationResult.setOawDescription("check." + check + ".error");
		validationResult.setResult(EARL_PASSED);
		return validationResult;
	}

	/**
	 * Process not executed.
	 *
	 * @param observatorySubgroupForm the observatory subgroup form
	 * @param check                   the check
	 * @return the validation result
	 */
	private static ValidationResult processNotExecuted(final ObservatorySubgroupForm observatorySubgroupForm, Integer check) {
		ValidationResult validationResult = new ValidationResult();
		validationResult.setOawVerification(observatorySubgroupForm.getDescription());
		validationResult.setOawDescription("check." + check + ".error");
		validationResult.setResult(EARL_INAPPLICABLE);
		return validationResult;
	}

	/**
	 * Process multiple verification.
	 *
	 * @param tmpWcag                  the tmp wcag
	 * @param observatorySubgroupForms the observatory subgroup forms
	 * @param wcagEmId                 the wcag em id
	 * @param checkChecks              the check checks
	 */
	private static void processMultipleVerification(Map<String, ValidationDetails> tmpWcag, final List<ObservatorySubgroupForm> observatorySubgroupForms, final String wcagEmId,
			final boolean checkChecks) {
		// To simply comparation, only compare integet values
		List<Integer> integerList = new ArrayList<Integer>();
		for (ObservatorySubgroupForm obs : observatorySubgroupForms) {
			integerList.add(new Integer(obs.getValue()));
		}
		boolean allEqual = new HashSet<Integer>(integerList).size() <= 1;
		ValidationDetails validationDetailes = new ValidationDetails();
		if (allEqual) {
			processSimpleVerification(tmpWcag, observatorySubgroupForms.get(0), wcagEmId, checkChecks);
		} else if (integerList.contains(Constants.OBS_VALUE_RED_ZERO)) {
			Map<String, List<String>> checkWcagRelationMap = checkWcagRelationMap();
			validationDetailes.setResult(EARL_CANNOT_TELL);
			// Detail errors
			List<ValidationResult> results = new ArrayList<>();
			for (ObservatorySubgroupForm observatorySubgroupForm : observatorySubgroupForms) {
				filterObservatorySubgroupForm(tmpWcag, observatorySubgroupForm, wcagEmId, validationDetailes, results, checkWcagRelationMap, EARL_CANNOT_TELL);
			}
		} else {
			validationDetailes.setResult(EARL_CANNOT_TELL);
			List<ValidationResult> results = new ArrayList<>();
			for (ObservatorySubgroupForm observatorySubgroupForm : observatorySubgroupForms) {
				processChecks(observatorySubgroupForm, validationDetailes, results);
			}
			validationDetailes.setResults(results);
			tmpWcag.put(wcagEmId, validationDetailes);
		}
	}

	/**
	 * Prints the problems as table.
	 *
	 * @param results          the results
	 * @param messageResources the message resources
	 * @return the string
	 */
	private static String printProblemsAsTable(List<ValidationResult> results, final MessageResources messageResources) {
		final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
		StringBuilder tableBuilder = new StringBuilder("<table class=\"w-table no-border border-solid\">");
		tableBuilder.append("<thead>");
		tableBuilder.append("<tr>");
		tableBuilder.append("<th>Verificación OAW</th>");
		tableBuilder.append("<th>Descripción</th>");
		tableBuilder.append("<th>Resultado</th>");
		tableBuilder.append("</tr>");
		tableBuilder.append("</thead>");
		tableBuilder.append("<tbody>");
		for (ValidationResult result : results) {
			tableBuilder.append("<tr>");
			tableBuilder.append("<td>");
			tableBuilder.append(messageResources.getMessage(result.getOawVerification()));
			tableBuilder.append("</td>");
			tableBuilder.append("<td>");
			tableBuilder.append(checkDescriptionsManager.getString(result.getOawDescription()));
			tableBuilder.append("</td>");
			tableBuilder.append("<td>");
			final String spanClass = result.getResult().toLowerCase().replace(":", "_");
			final String spanText = result.getResult().toUpperCase().replace("EARL:", "");
			tableBuilder.append("<span class='" + spanClass + "'>{{ 'EARL." + spanText + "' | translate}}</span>");
			tableBuilder.append("</td>");
			tableBuilder.append("</tr>");
		}
		tableBuilder.append("</tbody>");
		tableBuilder.append("</table>");
		return tableBuilder.toString();
	}

	/**
	 * Check wcag relation map. Generates a map with WCAG-Checks relation
	 *
	 * @return the map
	 */
	private static Map<String, List<String>> checkWcagRelationMap() {
		Map<String, List<String>> checkWcagRelationMap = new TreeMap<>();
		List<String> checks = new ArrayList<>();
		// WCAG
		// 1.3.1
		checks = new ArrayList<>();
		checks.add("37"); // 1.2
		checks.add("421"); // 1.2
		checks.add("38"); // 1.2
		checks.add("395"); // 1.2
		checks.add("422"); // 1.2
		checks.add("433"); // 1.2
		checks.add("469"); // 1.2
		checks.add("470"); // 1.2
		checks.add("471"); // 1.2
		checks.add("472"); // 1.2
		checks.add("101"); // 1.3
		checks.add("120"); // 1.3
		checks.add("121"); // 1.3
		checks.add("410"); // 1.3
		checks.add("311"); // 1.3
		checks.add("427"); // 1.3
		checks.add("313"); // 1.3
		checks.add("314"); // 1.3
		checks.add("317"); // 1.3
		checks.add("318"); // 1.3
		checks.add("319"); // 1.3
		checks.add("320"); // 1.3
		checks.add("423"); // 1.3
		checks.add("424"); // 1.3
		checks.add("425"); // 1.3
		checks.add("416"); // 1.3
		checks.add("445"); // 1.3
		checks.add("459"); // 1.3
		checks.add("431"); // 1.3
		checks.add("7"); // 1.4
		checks.add("86"); // 1.4
		checks.add("116"); // 1.4
		checks.add("156"); // 1.4
		checks.add("159"); // 1.4
		checks.add("245"); // 1.4
		checks.add("418"); // 1.4
		checks.add("243"); // 1.4
		checks.add("415"); // 1.4
		checks.add("464"); // 1.4
		checks.add("492"); // 1.4
		checks.add("16"); // 1.5
		checks.add("33"); // 1.5
		checks.add("436"); // 1.5
		checks.add("45"); // 1.6
		checks.add("345"); // 1.6
		checks.add("447"); // 1.6
		checks.add("57"); // 1.9
		checks.add("91"); // 1.9
		checks.add("95"); // 1.9
		checks.add("67"); // 1.9
		checks.add("461"); // 1.9
		checks.add("443"); // 1.10
		checks.add("429"); // 1.10
		checks.add("466"); // 1.10
		checks.add("430"); // 1.10
		checks.add("444"); // 1.10
		checks.add("473"); // 1.10
		checks.add("406"); // 1.10
		checks.add("417"); // 1.10
		checks.add("407"); // 1.10
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_1_3_1.getWcagEmId(), checks);
		// 1.3.4
		checks = new ArrayList<>();
		checks.add("480"); // 2.5
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_1_3_4.getWcagEmId(), checks);
		// 1.3.5
		checks = new ArrayList<>();
		checks.add("481"); // 2.5
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_1_3_5.getWcagEmId(), checks);
		// 1.4.3
		checks = new ArrayList<>();
		checks.add("448"); // 2.2
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_1_4_3.getWcagEmId(), checks);
		// 1.4.12
		checks = new ArrayList<>();
		checks.add("477"); // 2.2
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_1_4_12.getWcagEmId(), checks);
		// 2.1.1
		checks = new ArrayList<>();
		checks.add("160"); // 1.8
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_2_1_1.getWcagEmId(), checks);
		// 2.2.1
		checks = new ArrayList<>();
		checks.add("71");// 1.8
		checks.add("72");// 1.8
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_2_2_1.getWcagEmId(), checks);
		// 2.2.2
		checks = new ArrayList<>();
		checks.add("130");// 1.8
		checks.add("449");// 1.8
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_2_2_2.getWcagEmId(), checks);
		// 2.4.1
		checks = new ArrayList<>();
		checks.add("31");// 1.11
		checks.add("295");// 1.11
		checks.add("158");// 1.11
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_2_4_1.getWcagEmId(), checks);
		// 2.4.2
		checks = new ArrayList<>();
		checks.add("50");// 1.11
		checks.add("51");// 1.11
		checks.add("53");// 1.11
		checks.add("462");// 1.11
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_2_4_2.getWcagEmId(), checks);
		// 2.4.3
		checks = new ArrayList<>();
		checks.add("434"); // 2.5
		checks.add("435"); // 2.5
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_2_4_3.getWcagEmId(), checks);
		// 2.4.7
		checks = new ArrayList<>();
		checks.add("451"); // 2.5
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_2_4_7.getWcagEmId(), checks);
		// 2.5.3
		checks = new ArrayList<>();
		checks.add("476");// 1.9
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_2_5_3.getWcagEmId(), checks);
		// 3.2.1
		checks = new ArrayList<>();
		checks.add("452");// 1.13
		checks.add("453");// 1.13
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_3_2_1.getWcagEmId(), checks);
		// 3.2.1
		checks = new ArrayList<>();
		checks.add("454");// 1.13
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_3_2_2.getWcagEmId(), checks);
		// 3.3.2
		checks = new ArrayList<>();
		checks.add("446");// 1.9
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_3_3_2.getWcagEmId(), checks);
		// 4.1.2
		checks = new ArrayList<>();
		checks.add("432");// 1.8
		checks.add("57"); // 1.9
		checks.add("91"); // 1.9
		checks.add("95"); // 1.9
		checks.add("444"); // 1.10
		checks.add("473"); // 1.10
		checks.add("407"); // 1.10
		checks.add("31");// 1.11
		checks.add("295");// 1.11
		checks.add("158");// 1.11
		checkWcagRelationMap.put(WcagEmPointKey.WCAG_4_1_2.getWcagEmId(), checks);
		return checkWcagRelationMap;
	}
}
