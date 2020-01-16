package es.oaw.wcagem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.util.MessageResources;

import es.gob.oaw.rastreador2.observatorio.ObservatoryManager;
import es.inteco.common.Constants;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.oaw.wcagem.enums.WcagEmPointKey;

/**
 * The Class WcagEmUtils.
 */
public final class WcagEmUtils {
	private static final String _1_6 = "1.6";
	private static final String _1_5 = "1.5";
	private static final String _1_4 = "1.4";
	private static final String _1_3 = "1.3";
	private static final String _1_2 = "1.2";
	private static final String _1_10 = "1.10";
	private static final String _2_6 = "2.6";
	private static final String _1_13 = "1.13";
	private static final String _2_1 = "2.1";
	private static final String _1_7 = "1.7";
	private static final String _1_9 = "1.9";
	private static final String _2_4 = "2.4";
	private static final String _1_12 = "1.12";
	private static final String _1_11 = "1.11";
	private static final String _1_8 = "1.8";
	private static final String _2_2 = "2.2";
	private static final String _2_3 = "2.3";
	private static final String _2_5 = "2.5";
	private static final String _1_1 = "1.1";
	/** The Constant EARL_INAPPLICABLE. */
	private static final String EARL_INAPPLICABLE = "earl:inapplicable";
	/** The Constant EARL_FAILED. */
	private static final String EARL_FAILED = "earl:failed";
	/** The Constant EARL_PASSED. */
	private static final String EARL_PASSED = "earl:passed";

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
		// Pages
		Map<String, Integer> siteCompliance = new TreeMap<>();
		// This map store, the url and a map with everi wcag automatic validation an result
		Map<String, Map<String, Integer>> oawCompliance = new TreeMap<>();
		Map<String, Map<String, String>> wcagCompliance = new TreeMap<>();
		// Pagaes
		final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
		for (ObservatoryEvaluationForm observatoryEvaluationForm : currentEvaluationPageList) {
			String url = observatoryEvaluationForm.getUrl();
			Map<String, Integer> tmp = new TreeMap<>();
			// Process groups
			for (ObservatorySuitabilityForm suitabilityForm : observatoryEvaluationForm.getGroups().get(0).getSuitabilityGroups()) {
				for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
					final String name = subgroupForm.getDescription()
							.substring(subgroupForm.getDescription().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length());
					tmp.put(name, subgroupForm.getValue());
				}
			}
			for (ObservatorySuitabilityForm suitabilityForm : observatoryEvaluationForm.getGroups().get(1).getSuitabilityGroups()) {
				for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
					final String name = subgroupForm.getDescription()
							.substring(subgroupForm.getDescription().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length());
					tmp.put(name, subgroupForm.getValue());
				}
			}
			oawCompliance.put(url, tmp);
			Map<String, String> tmpWcag = new TreeMap<>();
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
			List<Integer> verifications = new ArrayList<Integer>();
			verifications.add(tmp.get(_1_2));
			verifications.add(tmp.get(_1_3));
			verifications.add(tmp.get(_1_4));
			verifications.add(tmp.get(_1_5));
			verifications.add(tmp.get(_1_6));
			verifications.add(tmp.get(_1_9));
			verifications.add(tmp.get(_1_10));
			processMultipleVerification(tmpWcag, verifications, WcagEmPointKey.WCAG_1_1_1.getWcagEmId());
			// Check 4.1.2
			verifications = new ArrayList<Integer>();
			verifications.add(tmp.get(_1_8));
			verifications.add(tmp.get(_1_9));
			verifications.add(tmp.get(_1_10));
			verifications.add(tmp.get(_1_11));
			processMultipleVerification(tmpWcag, verifications, WcagEmPointKey.WCAG_4_1_2.getWcagEmId());
			// Add to globall
			wcagCompliance.put(url, tmpWcag);
		}
		return wcagEmReport;
	}

	/**
	 * Process simple verification.
	 *
	 * @param tmpWcag  the tmp wcag
	 * @param integer  the integer
	 * @param wcagEmId the wcag em id
	 */
	private static void processSimpleVerification(Map<String, String> tmpWcag, final Integer integer, final String wcagEmId) {
		switch (integer) {
		case Constants.OBS_VALUE_GREEN_ZERO:
			tmpWcag.put(wcagEmId, EARL_PASSED);
			break;
		case Constants.OBS_VALUE_RED_ZERO:
			tmpWcag.put(wcagEmId, EARL_FAILED);
			break;
		case Constants.OBS_VALUE_GREEN_ONE:
			tmpWcag.put(wcagEmId, EARL_FAILED);
			break;
		case Constants.OBS_VALUE_NOT_SCORE:
			tmpWcag.put(wcagEmId, EARL_INAPPLICABLE);
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
	private static void processMultipleVerification(Map<String, String> tmpWcag, final List<Integer> integerList, final String wcagEmId) {
		boolean allEqual = new HashSet<Integer>(integerList).size() <= 1;
		if (allEqual) {
			processSimpleVerification(tmpWcag, integerList.get(0), wcagEmId);
		} else if (integerList.contains(Constants.OBS_VALUE_RED_ZERO)) {
			tmpWcag.put(wcagEmId, EARL_FAILED);
		} else {
			tmpWcag.put(wcagEmId, EARL_PASSED);
		}
	}
}
