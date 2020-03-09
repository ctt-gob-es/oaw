package es.oaw.wcagem;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "@vocab", "reporter", "wcagem", "WCAG2", "earl", "dct", "wai", "sch", "Evaluation", "EvaluationScope", "TestSubject", "WebSite", "Sample", "WebPage", "Technology", "Assertion",
		"Assertor", "TestResult", "title", "summary", "creator", "date", "commissioner", "reliedUponTechnology", "evaluationScope2", "commonPages", "essentialFunctionality", "pageTypeVariety",
		"otherRelevantPages", "structuredSample", "randomSample", "auditResult", "specifics", "publisher", "conformanceTarget", "accessibilitySupportBaseline", "additionalEvalRequirement", "website",
		"siteScope", "siteName", "webpage", "description", "source", "tested", "test", "assertedBy", "subject", "result", "mode", "hasPart", "outcome", "id", "type", "lang" })
public class Context {
	@JsonProperty("@vocab")
	private String vocab;
	@JsonProperty("reporter")
	private String reporter;
	@JsonProperty("wcagem")
	private String wcagem;
	@JsonProperty("WCAG2")
	private String wCAG2;
	@JsonProperty("earl")
	private String earl;
	@JsonProperty("dct")
	private String dct;
	@JsonProperty("wai")
	private String wai;
	@JsonProperty("sch")
	private String sch;
	@JsonProperty("Evaluation")
	private String evaluation;
	@JsonProperty("EvaluationScope")
	private String evaluationScope;
	@JsonProperty("TestSubject")
	private String testSubject;
	@JsonProperty("WebSite")
	private String webSite;
	@JsonProperty("Sample")
	private String sample;
	@JsonProperty("WebPage")
	private String webPage;
	@JsonProperty("Technology")
	private String technology;
	@JsonProperty("Assertion")
	private String assertion;
	@JsonProperty("Assertor")
	private String assertor;
	@JsonProperty("TestResult")
	private String testResult;
	@JsonProperty("title")
	private String title;
	@JsonProperty("summary")
	private String summary;
	@JsonProperty("creator")
	private Creator creator;
	@JsonProperty("date")
	private String date;
	@JsonProperty("commissioner")
	private String commissioner;
	@JsonProperty("reliedUponTechnology")
	private String reliedUponTechnology;
	@JsonProperty("evaluationScope")
	private String evaluationScope2;
	@JsonProperty("commonPages")
	private String commonPages;
	@JsonProperty("essentialFunctionality")
	private String essentialFunctionality;
	@JsonProperty("pageTypeVariety")
	private String pageTypeVariety;
	@JsonProperty("otherRelevantPages")
	private String otherRelevantPages;
	@JsonProperty("structuredSample")
	private String structuredSample;
	@JsonProperty("randomSample")
	private String randomSample;
	@JsonProperty("auditResult")
	private String auditResult;
	@JsonProperty("specifics")
	private String specifics;
	@JsonProperty("publisher")
	private Publisher publisher;
	@JsonProperty("conformanceTarget")
	private ConformanceTarget conformanceTarget;
	@JsonProperty("accessibilitySupportBaseline")
	private String accessibilitySupportBaseline;
	@JsonProperty("additionalEvalRequirement")
	private String additionalEvalRequirement;
	@JsonProperty("website")
	private String website;
	@JsonProperty("siteScope")
	private String siteScope;
	@JsonProperty("siteName")
	private String siteName;
	@JsonProperty("webpage")
	private String webpage;
	@JsonProperty("description")
	private String description;
	@JsonProperty("source")
	private Source source;
	@JsonProperty("tested")
	private String tested;
	@JsonProperty("test")
	private Test test;
	@JsonProperty("assertedBy")
	private AssertedBy assertedBy;
	@JsonProperty("subject")
	private Subject subject;
	@JsonProperty("result")
	private String result;
	@JsonProperty("mode")
	private Mode mode;
	@JsonProperty("hasPart")
	private String hasPart;
	@JsonProperty("outcome")
	private Outcome outcome;
	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
	private String type;
	@JsonProperty("lang")
	private String lang;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("@vocab")
	public String getVocab() {
		return vocab;
	}

	@JsonProperty("@vocab")
	public void setVocab(String vocab) {
		this.vocab = vocab;
	}

	@JsonProperty("reporter")
	public String getReporter() {
		return reporter;
	}

	@JsonProperty("reporter")
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	@JsonProperty("wcagem")
	public String getWcagem() {
		return wcagem;
	}

	@JsonProperty("wcagem")
	public void setWcagem(String wcagem) {
		this.wcagem = wcagem;
	}

	@JsonProperty("WCAG2")
	public String getWCAG2() {
		return wCAG2;
	}

	@JsonProperty("WCAG2")
	public void setWCAG2(String wCAG2) {
		this.wCAG2 = wCAG2;
	}

	@JsonProperty("earl")
	public String getEarl() {
		return earl;
	}

	@JsonProperty("earl")
	public void setEarl(String earl) {
		this.earl = earl;
	}

	@JsonProperty("dct")
	public String getDct() {
		return dct;
	}

	@JsonProperty("dct")
	public void setDct(String dct) {
		this.dct = dct;
	}

	@JsonProperty("wai")
	public String getWai() {
		return wai;
	}

	@JsonProperty("wai")
	public void setWai(String wai) {
		this.wai = wai;
	}

	@JsonProperty("sch")
	public String getSch() {
		return sch;
	}

	@JsonProperty("sch")
	public void setSch(String sch) {
		this.sch = sch;
	}

	@JsonProperty("Evaluation")
	public String getEvaluation() {
		return evaluation;
	}

	@JsonProperty("Evaluation")
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	@JsonProperty("EvaluationScope")
	public String getEvaluationScope() {
		return evaluationScope;
	}

	@JsonProperty("EvaluationScope")
	public void setEvaluationScope(String evaluationScope) {
		this.evaluationScope = evaluationScope;
	}

	@JsonProperty("TestSubject")
	public String getTestSubject() {
		return testSubject;
	}

	@JsonProperty("TestSubject")
	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}

	@JsonProperty("WebSite")
	public String getWebSite() {
		return webSite;
	}

	@JsonProperty("WebSite")
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	@JsonProperty("Sample")
	public String getSample() {
		return sample;
	}

	@JsonProperty("Sample")
	public void setSample(String sample) {
		this.sample = sample;
	}

	@JsonProperty("WebPage")
	public String getWebPage() {
		return webPage;
	}

	@JsonProperty("WebPage")
	public void setWebPage(String webPage) {
		this.webPage = webPage;
	}

	@JsonProperty("Technology")
	public String getTechnology() {
		return technology;
	}

	@JsonProperty("Technology")
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	@JsonProperty("Assertion")
	public String getAssertion() {
		return assertion;
	}

	@JsonProperty("Assertion")
	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}

	@JsonProperty("Assertor")
	public String getAssertor() {
		return assertor;
	}

	@JsonProperty("Assertor")
	public void setAssertor(String assertor) {
		this.assertor = assertor;
	}

	@JsonProperty("TestResult")
	public String getTestResult() {
		return testResult;
	}

	@JsonProperty("TestResult")
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("summary")
	public String getSummary() {
		return summary;
	}

	@JsonProperty("summary")
	public void setSummary(String summary) {
		this.summary = summary;
	}

	@JsonProperty("creator")
	public Creator getCreator() {
		return creator;
	}

	@JsonProperty("creator")
	public void setCreator(Creator creator) {
		this.creator = creator;
	}

	@JsonProperty("date")
	public String getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	@JsonProperty("commissioner")
	public String getCommissioner() {
		return commissioner;
	}

	@JsonProperty("commissioner")
	public void setCommissioner(String commissioner) {
		this.commissioner = commissioner;
	}

	@JsonProperty("reliedUponTechnology")
	public String getReliedUponTechnology() {
		return reliedUponTechnology;
	}

	@JsonProperty("reliedUponTechnology")
	public void setReliedUponTechnology(String reliedUponTechnology) {
		this.reliedUponTechnology = reliedUponTechnology;
	}

	@JsonProperty("evaluationScope")
	public String getEvaluationScope2() {
		return evaluationScope2;
	}

	@JsonProperty("evaluationScope")
	public void setEvaluationScope2(String evaluationScope2) {
		this.evaluationScope2 = evaluationScope2;
	}

	@JsonProperty("commonPages")
	public String getCommonPages() {
		return commonPages;
	}

	@JsonProperty("commonPages")
	public void setCommonPages(String commonPages) {
		this.commonPages = commonPages;
	}

	@JsonProperty("essentialFunctionality")
	public String getEssentialFunctionality() {
		return essentialFunctionality;
	}

	@JsonProperty("essentialFunctionality")
	public void setEssentialFunctionality(String essentialFunctionality) {
		this.essentialFunctionality = essentialFunctionality;
	}

	@JsonProperty("pageTypeVariety")
	public String getPageTypeVariety() {
		return pageTypeVariety;
	}

	@JsonProperty("pageTypeVariety")
	public void setPageTypeVariety(String pageTypeVariety) {
		this.pageTypeVariety = pageTypeVariety;
	}

	@JsonProperty("otherRelevantPages")
	public String getOtherRelevantPages() {
		return otherRelevantPages;
	}

	@JsonProperty("otherRelevantPages")
	public void setOtherRelevantPages(String otherRelevantPages) {
		this.otherRelevantPages = otherRelevantPages;
	}

	@JsonProperty("structuredSample")
	public String getStructuredSample() {
		return structuredSample;
	}

	@JsonProperty("structuredSample")
	public void setStructuredSample(String structuredSample) {
		this.structuredSample = structuredSample;
	}

	@JsonProperty("randomSample")
	public String getRandomSample() {
		return randomSample;
	}

	@JsonProperty("randomSample")
	public void setRandomSample(String randomSample) {
		this.randomSample = randomSample;
	}

	@JsonProperty("auditResult")
	public String getAuditResult() {
		return auditResult;
	}

	@JsonProperty("auditResult")
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	@JsonProperty("specifics")
	public String getSpecifics() {
		return specifics;
	}

	@JsonProperty("specifics")
	public void setSpecifics(String specifics) {
		this.specifics = specifics;
	}

	@JsonProperty("publisher")
	public Publisher getPublisher() {
		return publisher;
	}

	@JsonProperty("publisher")
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	@JsonProperty("conformanceTarget")
	public ConformanceTarget getConformanceTarget() {
		return conformanceTarget;
	}

	@JsonProperty("conformanceTarget")
	public void setConformanceTarget(ConformanceTarget conformanceTarget) {
		this.conformanceTarget = conformanceTarget;
	}

	@JsonProperty("accessibilitySupportBaseline")
	public String getAccessibilitySupportBaseline() {
		return accessibilitySupportBaseline;
	}

	@JsonProperty("accessibilitySupportBaseline")
	public void setAccessibilitySupportBaseline(String accessibilitySupportBaseline) {
		this.accessibilitySupportBaseline = accessibilitySupportBaseline;
	}

	@JsonProperty("additionalEvalRequirement")
	public String getAdditionalEvalRequirement() {
		return additionalEvalRequirement;
	}

	@JsonProperty("additionalEvalRequirement")
	public void setAdditionalEvalRequirement(String additionalEvalRequirement) {
		this.additionalEvalRequirement = additionalEvalRequirement;
	}

	@JsonProperty("website")
	public String getWebsite() {
		return website;
	}

	@JsonProperty("website")
	public void setWebsite(String website) {
		this.website = website;
	}

	@JsonProperty("siteScope")
	public String getSiteScope() {
		return siteScope;
	}

	@JsonProperty("siteScope")
	public void setSiteScope(String siteScope) {
		this.siteScope = siteScope;
	}

	@JsonProperty("siteName")
	public String getSiteName() {
		return siteName;
	}

	@JsonProperty("siteName")
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@JsonProperty("webpage")
	public String getWebpage() {
		return webpage;
	}

	@JsonProperty("webpage")
	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("source")
	public Source getSource() {
		return source;
	}

	@JsonProperty("source")
	public void setSource(Source source) {
		this.source = source;
	}

	@JsonProperty("tested")
	public String getTested() {
		return tested;
	}

	@JsonProperty("tested")
	public void setTested(String tested) {
		this.tested = tested;
	}

	@JsonProperty("test")
	public Test getTest() {
		return test;
	}

	@JsonProperty("test")
	public void setTest(Test test) {
		this.test = test;
	}

	@JsonProperty("assertedBy")
	public AssertedBy getAssertedBy() {
		return assertedBy;
	}

	@JsonProperty("assertedBy")
	public void setAssertedBy(AssertedBy assertedBy) {
		this.assertedBy = assertedBy;
	}

	@JsonProperty("subject")
	public Subject getSubject() {
		return subject;
	}

	@JsonProperty("subject")
	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	@JsonProperty("result")
	public String getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(String result) {
		this.result = result;
	}

	@JsonProperty("mode")
	public Mode getMode() {
		return mode;
	}

	@JsonProperty("mode")
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	@JsonProperty("hasPart")
	public String getHasPart() {
		return hasPart;
	}

	@JsonProperty("hasPart")
	public void setHasPart(String hasPart) {
		this.hasPart = hasPart;
	}

	@JsonProperty("outcome")
	public Outcome getOutcome() {
		return outcome;
	}

	@JsonProperty("outcome")
	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("lang")
	public String getLang() {
		return lang;
	}

	@JsonProperty("lang")
	public void setLang(String lang) {
		this.lang = lang;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
