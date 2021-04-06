package es.oaw.wcagem;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Context.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "@vocab", "reporter", "wcagem", "WCAG2", "earl", "dct", "wai", "sch", "Evaluation", "EvaluationScope", "TestSubject", "WebSite", "Sample", "WebPage", "Technology", "Assertion",
		"Assertor", "TestResult", "title", "summary", "creator", "date", "commissioner", "reliedUponTechnology", "evaluationScope2", "commonPages", "essentialFunctionality", "pageTypeVariety",
		"otherRelevantPages", "structuredSample", "randomSample", "auditResult", "specifics", "publisher", "conformanceTarget", "accessibilitySupportBaseline", "additionalEvalRequirement", "website",
		"siteScope", "siteName", "webpage", "description", "source", "tested", "test", "assertedBy", "subject", "result", "mode", "hasPart", "outcome", "id", "type", "lang" })
public class Context {
	
	/** The vocab. */
	@JsonProperty("@vocab")
	private String vocab;
	
	/** The reporter. */
	@JsonProperty("reporter")
	private String reporter;
	
	/** The wcagem. */
	@JsonProperty("wcagem")
	private String wcagem;
	
	/** The w CAG 2. */
	@JsonProperty("WCAG2")
	private String wCAG2;
	
	/** The earl. */
	@JsonProperty("earl")
	private String earl;
	
	/** The dct. */
	@JsonProperty("dct")
	private String dct;
	
	/** The wai. */
	@JsonProperty("wai")
	private String wai;
	
	/** The sch. */
	@JsonProperty("sch")
	private String sch;
	
	/** The evaluation. */
	@JsonProperty("Evaluation")
	private String evaluation;
	
	/** The evaluation scope. */
	@JsonProperty("EvaluationScope")
	private String evaluationScope;
	
	/** The test subject. */
	@JsonProperty("TestSubject")
	private String testSubject;
	
	/** The web site. */
	@JsonProperty("WebSite")
	private String webSite;
	
	/** The sample. */
	@JsonProperty("Sample")
	private String sample;
	
	/** The web page. */
	@JsonProperty("WebPage")
	private String webPage;
	
	/** The technology. */
	@JsonProperty("Technology")
	private String technology;
	
	/** The assertion. */
	@JsonProperty("Assertion")
	private String assertion;
	
	/** The assertor. */
	@JsonProperty("Assertor")
	private String assertor;
	
	/** The test result. */
	@JsonProperty("TestResult")
	private String testResult;
	
	/** The title. */
	@JsonProperty("title")
	private String title;
	
	/** The summary. */
	@JsonProperty("summary")
	private String summary;
	
	/** The creator. */
	@JsonProperty("creator")
	private Creator creator;
	
	/** The date. */
	@JsonProperty("date")
	private String date;
	
	/** The commissioner. */
	@JsonProperty("commissioner")
	private String commissioner;
	
	/** The relied upon technology. */
	@JsonProperty("reliedUponTechnology")
	private String reliedUponTechnology;
	
	/** The evaluation scope 2. */
	@JsonProperty("evaluationScope")
	private String evaluationScope2;
	
	/** The common pages. */
	@JsonProperty("commonPages")
	private String commonPages;
	
	/** The essential functionality. */
	@JsonProperty("essentialFunctionality")
	private String essentialFunctionality;
	
	/** The page type variety. */
	@JsonProperty("pageTypeVariety")
	private String pageTypeVariety;
	
	/** The other relevant pages. */
	@JsonProperty("otherRelevantPages")
	private String otherRelevantPages;
	
	/** The structured sample. */
	@JsonProperty("structuredSample")
	private String structuredSample;
	
	/** The random sample. */
	@JsonProperty("randomSample")
	private String randomSample;
	
	/** The audit result. */
	@JsonProperty("auditResult")
	private String auditResult;
	
	/** The specifics. */
	@JsonProperty("specifics")
	private String specifics;
	
	/** The publisher. */
	@JsonProperty("publisher")
	private Publisher publisher;
	
	/** The conformance target. */
	@JsonProperty("conformanceTarget")
	private ConformanceTarget conformanceTarget;
	
	/** The accessibility support baseline. */
	@JsonProperty("accessibilitySupportBaseline")
	private String accessibilitySupportBaseline;
	
	/** The additional eval requirement. */
	@JsonProperty("additionalEvalRequirement")
	private String additionalEvalRequirement;
	
	/** The website. */
	@JsonProperty("website")
	private String website;
	
	/** The site scope. */
	@JsonProperty("siteScope")
	private String siteScope;
	
	/** The site name. */
	@JsonProperty("siteName")
	private String siteName;
	
	/** The webpage. */
	@JsonProperty("webpage")
	private String webpage;
	
	/** The description. */
	@JsonProperty("description")
	private String description;
	
	/** The source. */
	@JsonProperty("source")
	private Source source;
	
	/** The tested. */
	@JsonProperty("tested")
	private String tested;
	
	/** The test. */
	@JsonProperty("test")
	private Test test;
	
	/** The asserted by. */
	@JsonProperty("assertedBy")
	private AssertedBy assertedBy;
	
	/** The subject. */
	@JsonProperty("subject")
	private Subject subject;
	
	/** The result. */
	@JsonProperty("result")
	private String result;
	
	/** The mode. */
	@JsonProperty("mode")
	private Mode mode;
	
	/** The has part. */
	@JsonProperty("hasPart")
	private String hasPart;
	
	/** The outcome. */
	@JsonProperty("outcome")
	private Outcome outcome;
	
	/** The id. */
	@JsonProperty("id")
	private String id;
	
	/** The type. */
	@JsonProperty("type")
	private String type;
	
	/** The lang. */
	@JsonProperty("lang")
	private String lang;
	
	/** The additional properties. */
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the vocab.
	 *
	 * @return the vocab
	 */
	@JsonProperty("@vocab")
	public String getVocab() {
		return vocab;
	}

	/**
	 * Sets the vocab.
	 *
	 * @param vocab the new vocab
	 */
	@JsonProperty("@vocab")
	public void setVocab(String vocab) {
		this.vocab = vocab;
	}

	/**
	 * Gets the reporter.
	 *
	 * @return the reporter
	 */
	@JsonProperty("reporter")
	public String getReporter() {
		return reporter;
	}

	/**
	 * Sets the reporter.
	 *
	 * @param reporter the new reporter
	 */
	@JsonProperty("reporter")
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	/**
	 * Gets the wcagem.
	 *
	 * @return the wcagem
	 */
	@JsonProperty("wcagem")
	public String getWcagem() {
		return wcagem;
	}

	/**
	 * Sets the wcagem.
	 *
	 * @param wcagem the new wcagem
	 */
	@JsonProperty("wcagem")
	public void setWcagem(String wcagem) {
		this.wcagem = wcagem;
	}

	/**
	 * Gets the wcag2.
	 *
	 * @return the wcag2
	 */
	@JsonProperty("WCAG2")
	public String getWCAG2() {
		return wCAG2;
	}

	/**
	 * Sets the wcag2.
	 *
	 * @param wCAG2 the new wcag2
	 */
	@JsonProperty("WCAG2")
	public void setWCAG2(String wCAG2) {
		this.wCAG2 = wCAG2;
	}

	/**
	 * Gets the earl.
	 *
	 * @return the earl
	 */
	@JsonProperty("earl")
	public String getEarl() {
		return earl;
	}

	/**
	 * Sets the earl.
	 *
	 * @param earl the new earl
	 */
	@JsonProperty("earl")
	public void setEarl(String earl) {
		this.earl = earl;
	}

	/**
	 * Gets the dct.
	 *
	 * @return the dct
	 */
	@JsonProperty("dct")
	public String getDct() {
		return dct;
	}

	/**
	 * Sets the dct.
	 *
	 * @param dct the new dct
	 */
	@JsonProperty("dct")
	public void setDct(String dct) {
		this.dct = dct;
	}

	/**
	 * Gets the wai.
	 *
	 * @return the wai
	 */
	@JsonProperty("wai")
	public String getWai() {
		return wai;
	}

	/**
	 * Sets the wai.
	 *
	 * @param wai the new wai
	 */
	@JsonProperty("wai")
	public void setWai(String wai) {
		this.wai = wai;
	}

	/**
	 * Gets the sch.
	 *
	 * @return the sch
	 */
	@JsonProperty("sch")
	public String getSch() {
		return sch;
	}

	/**
	 * Sets the sch.
	 *
	 * @param sch the new sch
	 */
	@JsonProperty("sch")
	public void setSch(String sch) {
		this.sch = sch;
	}

	/**
	 * Gets the evaluation.
	 *
	 * @return the evaluation
	 */
	@JsonProperty("Evaluation")
	public String getEvaluation() {
		return evaluation;
	}

	/**
	 * Sets the evaluation.
	 *
	 * @param evaluation the new evaluation
	 */
	@JsonProperty("Evaluation")
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	/**
	 * Gets the evaluation scope.
	 *
	 * @return the evaluation scope
	 */
	@JsonProperty("EvaluationScope")
	public String getEvaluationScope() {
		return evaluationScope;
	}

	/**
	 * Sets the evaluation scope.
	 *
	 * @param evaluationScope the new evaluation scope
	 */
	@JsonProperty("EvaluationScope")
	public void setEvaluationScope(String evaluationScope) {
		this.evaluationScope = evaluationScope;
	}

	/**
	 * Gets the test subject.
	 *
	 * @return the test subject
	 */
	@JsonProperty("TestSubject")
	public String getTestSubject() {
		return testSubject;
	}

	/**
	 * Sets the test subject.
	 *
	 * @param testSubject the new test subject
	 */
	@JsonProperty("TestSubject")
	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}

	/**
	 * Gets the web site.
	 *
	 * @return the web site
	 */
	@JsonProperty("WebSite")
	public String getWebSite() {
		return webSite;
	}

	/**
	 * Sets the web site.
	 *
	 * @param webSite the new web site
	 */
	@JsonProperty("WebSite")
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	/**
	 * Gets the sample.
	 *
	 * @return the sample
	 */
	@JsonProperty("Sample")
	public String getSample() {
		return sample;
	}

	/**
	 * Sets the sample.
	 *
	 * @param sample the new sample
	 */
	@JsonProperty("Sample")
	public void setSample(String sample) {
		this.sample = sample;
	}

	/**
	 * Gets the web page.
	 *
	 * @return the web page
	 */
	@JsonProperty("WebPage")
	public String getWebPage() {
		return webPage;
	}

	/**
	 * Sets the web page.
	 *
	 * @param webPage the new web page
	 */
	@JsonProperty("WebPage")
	public void setWebPage(String webPage) {
		this.webPage = webPage;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	@JsonProperty("Technology")
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	@JsonProperty("Technology")
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the assertion.
	 *
	 * @return the assertion
	 */
	@JsonProperty("Assertion")
	public String getAssertion() {
		return assertion;
	}

	/**
	 * Sets the assertion.
	 *
	 * @param assertion the new assertion
	 */
	@JsonProperty("Assertion")
	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}

	/**
	 * Gets the assertor.
	 *
	 * @return the assertor
	 */
	@JsonProperty("Assertor")
	public String getAssertor() {
		return assertor;
	}

	/**
	 * Sets the assertor.
	 *
	 * @param assertor the new assertor
	 */
	@JsonProperty("Assertor")
	public void setAssertor(String assertor) {
		this.assertor = assertor;
	}

	/**
	 * Gets the test result.
	 *
	 * @return the test result
	 */
	@JsonProperty("TestResult")
	public String getTestResult() {
		return testResult;
	}

	/**
	 * Sets the test result.
	 *
	 * @param testResult the new test result
	 */
	@JsonProperty("TestResult")
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the summary.
	 *
	 * @return the summary
	 */
	@JsonProperty("summary")
	public String getSummary() {
		return summary;
	}

	/**
	 * Sets the summary.
	 *
	 * @param summary the new summary
	 */
	@JsonProperty("summary")
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	@JsonProperty("creator")
	public Creator getCreator() {
		return creator;
	}

	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	@JsonProperty("creator")
	public void setCreator(Creator creator) {
		this.creator = creator;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	@JsonProperty("date")
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the commissioner.
	 *
	 * @return the commissioner
	 */
	@JsonProperty("commissioner")
	public String getCommissioner() {
		return commissioner;
	}

	/**
	 * Sets the commissioner.
	 *
	 * @param commissioner the new commissioner
	 */
	@JsonProperty("commissioner")
	public void setCommissioner(String commissioner) {
		this.commissioner = commissioner;
	}

	/**
	 * Gets the relied upon technology.
	 *
	 * @return the relied upon technology
	 */
	@JsonProperty("reliedUponTechnology")
	public String getReliedUponTechnology() {
		return reliedUponTechnology;
	}

	/**
	 * Sets the relied upon technology.
	 *
	 * @param reliedUponTechnology the new relied upon technology
	 */
	@JsonProperty("reliedUponTechnology")
	public void setReliedUponTechnology(String reliedUponTechnology) {
		this.reliedUponTechnology = reliedUponTechnology;
	}

	/**
	 * Gets the evaluation scope 2.
	 *
	 * @return the evaluation scope 2
	 */
	@JsonProperty("evaluationScope")
	public String getEvaluationScope2() {
		return evaluationScope2;
	}

	/**
	 * Sets the evaluation scope 2.
	 *
	 * @param evaluationScope2 the new evaluation scope 2
	 */
	@JsonProperty("evaluationScope")
	public void setEvaluationScope2(String evaluationScope2) {
		this.evaluationScope2 = evaluationScope2;
	}

	/**
	 * Gets the common pages.
	 *
	 * @return the common pages
	 */
	@JsonProperty("commonPages")
	public String getCommonPages() {
		return commonPages;
	}

	/**
	 * Sets the common pages.
	 *
	 * @param commonPages the new common pages
	 */
	@JsonProperty("commonPages")
	public void setCommonPages(String commonPages) {
		this.commonPages = commonPages;
	}

	/**
	 * Gets the essential functionality.
	 *
	 * @return the essential functionality
	 */
	@JsonProperty("essentialFunctionality")
	public String getEssentialFunctionality() {
		return essentialFunctionality;
	}

	/**
	 * Sets the essential functionality.
	 *
	 * @param essentialFunctionality the new essential functionality
	 */
	@JsonProperty("essentialFunctionality")
	public void setEssentialFunctionality(String essentialFunctionality) {
		this.essentialFunctionality = essentialFunctionality;
	}

	/**
	 * Gets the page type variety.
	 *
	 * @return the page type variety
	 */
	@JsonProperty("pageTypeVariety")
	public String getPageTypeVariety() {
		return pageTypeVariety;
	}

	/**
	 * Sets the page type variety.
	 *
	 * @param pageTypeVariety the new page type variety
	 */
	@JsonProperty("pageTypeVariety")
	public void setPageTypeVariety(String pageTypeVariety) {
		this.pageTypeVariety = pageTypeVariety;
	}

	/**
	 * Gets the other relevant pages.
	 *
	 * @return the other relevant pages
	 */
	@JsonProperty("otherRelevantPages")
	public String getOtherRelevantPages() {
		return otherRelevantPages;
	}

	/**
	 * Sets the other relevant pages.
	 *
	 * @param otherRelevantPages the new other relevant pages
	 */
	@JsonProperty("otherRelevantPages")
	public void setOtherRelevantPages(String otherRelevantPages) {
		this.otherRelevantPages = otherRelevantPages;
	}

	/**
	 * Gets the structured sample.
	 *
	 * @return the structured sample
	 */
	@JsonProperty("structuredSample")
	public String getStructuredSample() {
		return structuredSample;
	}

	/**
	 * Sets the structured sample.
	 *
	 * @param structuredSample the new structured sample
	 */
	@JsonProperty("structuredSample")
	public void setStructuredSample(String structuredSample) {
		this.structuredSample = structuredSample;
	}

	/**
	 * Gets the random sample.
	 *
	 * @return the random sample
	 */
	@JsonProperty("randomSample")
	public String getRandomSample() {
		return randomSample;
	}

	/**
	 * Sets the random sample.
	 *
	 * @param randomSample the new random sample
	 */
	@JsonProperty("randomSample")
	public void setRandomSample(String randomSample) {
		this.randomSample = randomSample;
	}

	/**
	 * Gets the audit result.
	 *
	 * @return the audit result
	 */
	@JsonProperty("auditResult")
	public String getAuditResult() {
		return auditResult;
	}

	/**
	 * Sets the audit result.
	 *
	 * @param auditResult the new audit result
	 */
	@JsonProperty("auditResult")
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	/**
	 * Gets the specifics.
	 *
	 * @return the specifics
	 */
	@JsonProperty("specifics")
	public String getSpecifics() {
		return specifics;
	}

	/**
	 * Sets the specifics.
	 *
	 * @param specifics the new specifics
	 */
	@JsonProperty("specifics")
	public void setSpecifics(String specifics) {
		this.specifics = specifics;
	}

	/**
	 * Gets the publisher.
	 *
	 * @return the publisher
	 */
	@JsonProperty("publisher")
	public Publisher getPublisher() {
		return publisher;
	}

	/**
	 * Sets the publisher.
	 *
	 * @param publisher the new publisher
	 */
	@JsonProperty("publisher")
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	/**
	 * Gets the conformance target.
	 *
	 * @return the conformance target
	 */
	@JsonProperty("conformanceTarget")
	public ConformanceTarget getConformanceTarget() {
		return conformanceTarget;
	}

	/**
	 * Sets the conformance target.
	 *
	 * @param conformanceTarget the new conformance target
	 */
	@JsonProperty("conformanceTarget")
	public void setConformanceTarget(ConformanceTarget conformanceTarget) {
		this.conformanceTarget = conformanceTarget;
	}

	/**
	 * Gets the accessibility support baseline.
	 *
	 * @return the accessibility support baseline
	 */
	@JsonProperty("accessibilitySupportBaseline")
	public String getAccessibilitySupportBaseline() {
		return accessibilitySupportBaseline;
	}

	/**
	 * Sets the accessibility support baseline.
	 *
	 * @param accessibilitySupportBaseline the new accessibility support baseline
	 */
	@JsonProperty("accessibilitySupportBaseline")
	public void setAccessibilitySupportBaseline(String accessibilitySupportBaseline) {
		this.accessibilitySupportBaseline = accessibilitySupportBaseline;
	}

	/**
	 * Gets the additional eval requirement.
	 *
	 * @return the additional eval requirement
	 */
	@JsonProperty("additionalEvalRequirement")
	public String getAdditionalEvalRequirement() {
		return additionalEvalRequirement;
	}

	/**
	 * Sets the additional eval requirement.
	 *
	 * @param additionalEvalRequirement the new additional eval requirement
	 */
	@JsonProperty("additionalEvalRequirement")
	public void setAdditionalEvalRequirement(String additionalEvalRequirement) {
		this.additionalEvalRequirement = additionalEvalRequirement;
	}

	/**
	 * Gets the website.
	 *
	 * @return the website
	 */
	@JsonProperty("website")
	public String getWebsite() {
		return website;
	}

	/**
	 * Sets the website.
	 *
	 * @param website the new website
	 */
	@JsonProperty("website")
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * Gets the site scope.
	 *
	 * @return the site scope
	 */
	@JsonProperty("siteScope")
	public String getSiteScope() {
		return siteScope;
	}

	/**
	 * Sets the site scope.
	 *
	 * @param siteScope the new site scope
	 */
	@JsonProperty("siteScope")
	public void setSiteScope(String siteScope) {
		this.siteScope = siteScope;
	}

	/**
	 * Gets the site name.
	 *
	 * @return the site name
	 */
	@JsonProperty("siteName")
	public String getSiteName() {
		return siteName;
	}

	/**
	 * Sets the site name.
	 *
	 * @param siteName the new site name
	 */
	@JsonProperty("siteName")
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * Gets the webpage.
	 *
	 * @return the webpage
	 */
	@JsonProperty("webpage")
	public String getWebpage() {
		return webpage;
	}

	/**
	 * Sets the webpage.
	 *
	 * @param webpage the new webpage
	 */
	@JsonProperty("webpage")
	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	@JsonProperty("source")
	public Source getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
	@JsonProperty("source")
	public void setSource(Source source) {
		this.source = source;
	}

	/**
	 * Gets the tested.
	 *
	 * @return the tested
	 */
	@JsonProperty("tested")
	public String getTested() {
		return tested;
	}

	/**
	 * Sets the tested.
	 *
	 * @param tested the new tested
	 */
	@JsonProperty("tested")
	public void setTested(String tested) {
		this.tested = tested;
	}

	/**
	 * Gets the test.
	 *
	 * @return the test
	 */
	@JsonProperty("test")
	public Test getTest() {
		return test;
	}

	/**
	 * Sets the test.
	 *
	 * @param test the new test
	 */
	@JsonProperty("test")
	public void setTest(Test test) {
		this.test = test;
	}

	/**
	 * Gets the asserted by.
	 *
	 * @return the asserted by
	 */
	@JsonProperty("assertedBy")
	public AssertedBy getAssertedBy() {
		return assertedBy;
	}

	/**
	 * Sets the asserted by.
	 *
	 * @param assertedBy the new asserted by
	 */
	@JsonProperty("assertedBy")
	public void setAssertedBy(AssertedBy assertedBy) {
		this.assertedBy = assertedBy;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	@JsonProperty("subject")
	public Subject getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	@JsonProperty("subject")
	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	@JsonProperty("result")
	public String getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	@JsonProperty("result")
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	@JsonProperty("mode")
	public Mode getMode() {
		return mode;
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	@JsonProperty("mode")
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * Gets the checks for part.
	 *
	 * @return the checks for part
	 */
	@JsonProperty("hasPart")
	public String getHasPart() {
		return hasPart;
	}

	/**
	 * Sets the checks for part.
	 *
	 * @param hasPart the new checks for part
	 */
	@JsonProperty("hasPart")
	public void setHasPart(String hasPart) {
		this.hasPart = hasPart;
	}

	/**
	 * Gets the outcome.
	 *
	 * @return the outcome
	 */
	@JsonProperty("outcome")
	public Outcome getOutcome() {
		return outcome;
	}

	/**
	 * Sets the outcome.
	 *
	 * @param outcome the new outcome
	 */
	@JsonProperty("outcome")
	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	@JsonProperty("lang")
	public String getLang() {
		return lang;
	}

	/**
	 * Sets the lang.
	 *
	 * @param lang the new lang
	 */
	@JsonProperty("lang")
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * Gets the additional properties.
	 *
	 * @return the additional properties
	 */
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	/**
	 * Sets the additional property.
	 *
	 * @param name  the name
	 * @param value the value
	 */
	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
