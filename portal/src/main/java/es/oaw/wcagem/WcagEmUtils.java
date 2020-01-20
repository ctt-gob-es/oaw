package es.oaw.wcagem;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.struts.util.MessageResources;

import es.gob.oaw.rastreador2.observatorio.ObservatoryManager;
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
	private static final String _1_6 = "1.6";
	/** The Constant _1_5. */
	private static final String _1_5 = "1.5";
	/** The Constant _1_4. */
	private static final String _1_4 = "1.4";
	/** The Constant _1_3. */
	private static final String _1_3 = "1.3";
	/** The Constant _1_2. */
	private static final String _1_2 = "1.2";
	/** The Constant _1_10. */
	private static final String _1_10 = "1.10";
	/** The Constant _2_6. */
	private static final String _2_6 = "2.6";
	/** The Constant _1_13. */
	private static final String _1_13 = "1.13";
	/** The Constant _2_1. */
	private static final String _2_1 = "2.1";
	/** The Constant _1_7. */
	private static final String _1_7 = "1.7";
	/** The Constant _1_9. */
	private static final String _1_9 = "1.9";
	/** The Constant _2_4. */
	private static final String _2_4 = "2.4";
	/** The Constant _1_12. */
	private static final String _1_12 = "1.12";
	/** The Constant _1_11. */
	private static final String _1_11 = "1.11";
	/** The Constant _1_8. */
	private static final String _1_8 = "1.8";
	/** The Constant _2_2. */
	private static final String _2_2 = "2.2";
	/** The Constant _2_3. */
	private static final String _2_3 = "2.3";
	/** The Constant _2_5. */
	private static final String _2_5 = "2.5";
	/** The Constant _1_1. */
	private static final String _1_1 = "1.1";
	/** The Constant EARL_INAPPLICABLE. */
	private static final String EARL_INAPPLICABLE = "earl:inapplicable";
	/** The Constant EARL_FAILED. */
	private static final String EARL_FAILED = "earl:failed";
	/** The Constant EARL_PASSED. */
	private static final String EARL_PASSED = "earl:passed";
	/** The Constant EARL_CANNOT_TELL. */
	private static final String EARL_CANNOT_TELL = "earl:cantTell";

	/**
	 * Generate report.
	 *
	 * @param messageResources the message resources
	 * @param pdfBuilder       the pdf builder
	 * @param analisisId       the analisis id
	 * @return the wcag em report
	 * @throws Exception the exception
	 */
	public static WcagEmReport generateReport(final MessageResources messageResources, final AnonymousResultExportPdf pdfBuilder, Long analisisId) throws Exception {
		WcagEmReport wcagEmReport = new WcagEmReport();
		// Get scores
		final ObservatoryManager observatoryManager = new ObservatoryManager();
		final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), analisisId);
		// This map store, the url and a map with everi wcag automatic validation an result
		Map<String, Map<String, ValidationDetails>> wcagCompliance = new TreeMap<>();
		// Pagaes
		final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
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
			// TODO Modificar para que sean un objeto validation
			Map<String, ValidationDetails> tmpWcag = new TreeMap<>();
			// Match oaw validations with wcag (this url)
			// Check 1.1.1
			processSimpleVerification(tmpWcag, tmp.get(_1_1), WcagEmPointKey.WCAG_1_1_1.getWcagEmId());
			// Check 1.3.4
			processSimpleVerification(tmpWcag, tmp.get(_2_5), WcagEmPointKey.WCAG_1_3_4.getWcagEmId());
			// Check 1.3.5
			processSimpleVerification(tmpWcag, tmp.get(_2_5), WcagEmPointKey.WCAG_1_3_5.getWcagEmId());
			// Check 1.4.10
			processSimpleVerification(tmpWcag, tmp.get(_2_3), WcagEmPointKey.WCAG_1_4_10.getWcagEmId());
			// Check 1.4.12
			processSimpleVerification(tmpWcag, tmp.get(_2_2), WcagEmPointKey.WCAG_1_4_12.getWcagEmId());
			// Check 1.4.3
			processSimpleVerification(tmpWcag, tmp.get(_2_2), WcagEmPointKey.WCAG_1_4_3.getWcagEmId());
			// Check 2.1.1
			processSimpleVerification(tmpWcag, tmp.get(_1_8), WcagEmPointKey.WCAG_2_1_1.getWcagEmId());
			// Check 2.2.1
			processSimpleVerification(tmpWcag, tmp.get(_1_8), WcagEmPointKey.WCAG_2_2_1.getWcagEmId());
			// Check 2.3.1
			processSimpleVerification(tmpWcag, tmp.get(_1_8), WcagEmPointKey.WCAG_2_3_1.getWcagEmId());
			// Check 2.4.1
			processSimpleVerification(tmpWcag, tmp.get(_1_11), WcagEmPointKey.WCAG_2_4_1.getWcagEmId());
			// Check 2.4.2
			processSimpleVerification(tmpWcag, tmp.get(_1_11), WcagEmPointKey.WCAG_2_4_2.getWcagEmId());
			// Check 2.4.3
			processSimpleVerification(tmpWcag, tmp.get(_2_5), WcagEmPointKey.WCAG_2_4_3.getWcagEmId());
			// Check 2.4.4
			processSimpleVerification(tmpWcag, tmp.get(_1_12), WcagEmPointKey.WCAG_2_4_4.getWcagEmId());
			// Check 2.4.5
			processSimpleVerification(tmpWcag, tmp.get(_2_4), WcagEmPointKey.WCAG_2_4_5.getWcagEmId());
			// Check 2.5.3
			processSimpleVerification(tmpWcag, tmp.get(_1_9), WcagEmPointKey.WCAG_2_5_3.getWcagEmId());
			// Check 3.1.1
			processSimpleVerification(tmpWcag, tmp.get(_1_7), WcagEmPointKey.WCAG_3_1_1.getWcagEmId());
			// Check 3.1.2
			processSimpleVerification(tmpWcag, tmp.get(_2_1), WcagEmPointKey.WCAG_3_1_2.getWcagEmId());
			// Check 3.2.1
			processSimpleVerification(tmpWcag, tmp.get(_1_13), WcagEmPointKey.WCAG_3_2_1.getWcagEmId());
			// Check 3.2.2
			processSimpleVerification(tmpWcag, tmp.get(_1_13), WcagEmPointKey.WCAG_3_2_2.getWcagEmId());
			// Check 3.2.3
			processSimpleVerification(tmpWcag, tmp.get(_2_6), WcagEmPointKey.WCAG_3_2_3.getWcagEmId());
			// Check 3.3.2
			processSimpleVerification(tmpWcag, tmp.get(_1_9), WcagEmPointKey.WCAG_3_3_2.getWcagEmId());
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
			processMultipleVerification(tmpWcag, verifications, WcagEmPointKey.WCAG_1_3_1.getWcagEmId());
			// Check 4.1.2
			verifications = new ArrayList<ObservatorySubgroupForm>();
			verifications.add(tmp.get(_1_8));
			verifications.add(tmp.get(_1_9));
			verifications.add(tmp.get(_1_10));
			verifications.add(tmp.get(_1_11));
			processMultipleVerification(tmpWcag, verifications, WcagEmPointKey.WCAG_4_1_2.getWcagEmId());
			// Add to globall
			wcagCompliance.put(url, tmpWcag);
		}
		// TODO Generate analysis info
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
		graph.setLang("en");
		{
			EvaluationScope evaluationScope = new EvaluationScope();
			evaluationScope.setType("EvaluationScope");
			evaluationScope.setConformanceTarget("wai:WCAG2AA-Conformance");
			evaluationScope.setAdditionalEvalRequirement("");
			{
				Website website = new Website();
				website.setType(Arrays.asList(new String[] { "TestSubject", "WebSite" }));
				website.setId("_:website");
				// TODO Seed name
				website.setSiteName(BasicServiceUtils.getTitleDocFromContent(currentEvaluationPageList.get(0).getSource(), false));
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
					result.setDate(OffsetDateTime.now(ZoneId.of("Europe/Madrid")).toString());
					result.setDescription(wcagEmPointKey.getWcagPoint());
					result.setType("TestResult");
					// TODO calculate
					result.setOutcome("earl:cantTell");
					auditResult.setResult(result);
				}
				// TODO Check valid mode
				auditResult.setMode("earl:automatic");
				{
					List<HasPart> hasParts = new ArrayList<HasPart>();
					// TODO Iterate WCAG Points
					if (wcagCompliance != null && !wcagCompliance.isEmpty()) {
						int pageCounter = 0;
						for (Entry<String, Map<String, ValidationDetails>> result : wcagCompliance.entrySet()) {
							// if cointain current wcag rule
							if (result.getValue().containsKey(wcagEmPointKey.getWcagEmId())) {
								HasPart hasPart = new HasPart();
								hasPart.setType("Assertion");
								hasPart.setAssertedBy("_:evaluator");
								hasPart.setSubject(Arrays.asList(new String[] { "_:rand_" + pageCounter }));
								{
									Result_ resultP = new Result_();
									resultP.setType("TestResult");
									resultP.setDate(OffsetDateTime.now(ZoneId.of("Europe/Madrid")).toString());
									// TODO Result from Details
									final List<ValidationResult> results = result.getValue().get(wcagEmPointKey.getWcagEmId()).getResults();
									resultP.setDescription(results != null ? results.toString() : "");
									resultP.setOutcome(result.getValue().get(wcagEmPointKey.getWcagEmId()).getResult());
									hasPart.setResult(resultP);
								}
								hasPart.setMultiPage(false);
								// TODO Check valid mode
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
		graph.setTitle("Observatorio de Accesibilidad Web (OAW) - " + currentEvaluationPageList.get(0).getEntity());
		graph.setCommissioner("Observatorio de Accesibilidad Web (OAW)");
		{
			StructuredSample structuredSample = new StructuredSample();
			List<Webpage> webpages = new ArrayList<>();
			Webpage webpage = new Webpage();
			webpage.setType(Arrays.asList(new String[] { "TestSubject", "WebPage" }));
			webpage.setId("_:struct_0");
			webpage.setTitle("");
			webpage.setTested(false);
			webpages.add(webpage);
			structuredSample.setWebpage(webpages);
			graph.setStructuredSample(structuredSample);
		}
		{
			RandomSample randomSample = new RandomSample();
			List<Webpage_> webpages = new ArrayList<>();
			int randCounter = 0;
			for (Entry<String, Map<String, ValidationDetails>> result : wcagCompliance.entrySet()) {
				Webpage_ webpage = new Webpage_();
				webpage.setType(Arrays.asList(new String[] { "TestSubject", "WebPage" }));
				webpage.setId("_:rand_" + randCounter);
				webpage.setDescription(result.getKey());
				webpage.setSource(result.getKey());
				webpage.setTitle(result.getKey());
				webpage.setTested(false);// false to mark as incomplete un report step
				webpages.add(webpage);
				randCounter++;
			}
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
	 * Process simple verification.
	 *
	 * @param tmpWcag  the tmp wcag
	 * @param integer  the integer
	 * @param wcagEmId the wcag em id
	 */
	private static void processSimpleVerification(Map<String, ValidationDetails> tmpWcag, final ObservatorySubgroupForm observatorySubgroupForm, final String wcagEmId) {
		ValidationDetails validationDetailes = new ValidationDetails();
		switch (observatorySubgroupForm.getValue()) {
		case Constants.OBS_VALUE_GREEN_ZERO:
			// We can asegurte automatic PASS
			validationDetailes.setResult(EARL_CANNOT_TELL);
			tmpWcag.put(wcagEmId, validationDetailes);
			break;
		case Constants.OBS_VALUE_RED_ZERO:
			validationDetailes.setResult(EARL_FAILED);
			// TODO Detail errors
			if (observatorySubgroupForm.getProblems() != null && !observatorySubgroupForm.getProblems().isEmpty()) {
				List<ValidationResult> results = new ArrayList<>();
				for (ProblemForm problem : observatorySubgroupForm.getProblems()) {
					ValidationResult validationResult = new ValidationResult();
					validationResult.setOawVerification(problem.getCode());
					validationResult.setOawDescription(problem.getRationale());
					validationResult.setResult(EARL_FAILED);
					results.add(validationResult);
				}
				validationDetailes.setResults(results);
			}
			tmpWcag.put(wcagEmId, validationDetailes);
			break;
		case Constants.OBS_VALUE_GREEN_ONE:
			validationDetailes.setResult(EARL_FAILED);
			tmpWcag.put(wcagEmId, validationDetailes);
			break;
		case Constants.OBS_VALUE_NOT_SCORE:
			validationDetailes.setResult(EARL_INAPPLICABLE);
			tmpWcag.put(wcagEmId, validationDetailes);
			break;
		}
	}

	/**
	 * Process multiple verification.
	 *
	 * @param tmpWcag     the tmp wcag
	 * @param integerList the integer list
	 * @param wcagEmId    the wcag em id
	 */
	private static void processMultipleVerification(Map<String, ValidationDetails> tmpWcag, final List<ObservatorySubgroupForm> observatorySubgroupForms, final String wcagEmId) {
		// To simply comparation, only compare integet values
		List<Integer> integerList = new ArrayList<Integer>();
		for (ObservatorySubgroupForm obs : observatorySubgroupForms) {
			integerList.add(new Integer(obs.getValue()));
		}
		boolean allEqual = new HashSet<Integer>(integerList).size() <= 1;
		ValidationDetails validationDetailes = new ValidationDetails();
		if (allEqual) {
			processSimpleVerification(tmpWcag, observatorySubgroupForms.get(0), wcagEmId);
		} else if (integerList.contains(Constants.OBS_VALUE_RED_ZERO)) {
			validationDetailes.setResult(EARL_FAILED);
			// TODO Detail errors
			List<ValidationResult> results = new ArrayList<>();
			for (ObservatorySubgroupForm observatorySubgroupForm : observatorySubgroupForms) {
				if (observatorySubgroupForm.getProblems() != null && !observatorySubgroupForm.getProblems().isEmpty()) {
					for (ProblemForm problem : observatorySubgroupForm.getProblems()) {
						ValidationResult validationResult = new ValidationResult();
						validationResult.setOawVerification(problem.getCode());
						validationResult.setOawDescription(problem.getRationale());
						validationResult.setResult(EARL_FAILED);
						results.add(validationResult);
					}
				}
			}
			validationDetailes.setResults(results);
			tmpWcag.put(wcagEmId, validationDetailes);
		} else {
			validationDetailes.setResult(EARL_CANNOT_TELL);
			tmpWcag.put(wcagEmId, validationDetailes);
		}
	}
}
