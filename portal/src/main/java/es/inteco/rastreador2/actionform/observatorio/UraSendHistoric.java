package es.inteco.rastreador2.actionform.observatorio;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class UraSendResultForm.
 */
public class UraSendHistoric extends ValidatorForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7906403455383002683L;
	/** The id. */
	private Long id;
	/** The id ex obs. */
	private Long idExObs;
	/** The ids ex obs. */
	private String idsExObs;
	/** The cco. */
	private String cco;
	/** The subject. */
	private String subject;
	/** The comparisions. */
	private List<UraSendHistoricComparision> comparisions;
	/** The ranges. */
	private List<UraSendHistoricRange> ranges;
	/** The results. */
	private List<UraSendHistoricResults> results;
	/** The observatories. */
	private List<ObservatorioRealizadoForm> observatories;
	/** The sended. */
	private List<UraSendHistoricResults> sended;
	/** The not auto. */
	private List<UraSendHistoricResults> notAuto;
	/** The send date. */
	private Date sendDate;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the ids ex obs.
	 *
	 * @return the ids ex obs
	 */
	public String getIdsExObs() {
		return idsExObs;
	}

	/**
	 * Sets the ids ex obs.
	 *
	 * @param idsExObs the new ids ex obs
	 */
	public void setIdsExObs(String idsExObs) {
		this.idsExObs = idsExObs;
	}

	/**
	 * Gets the id ex obs.
	 *
	 * @return the id ex obs
	 */
	public Long getIdExObs() {
		return idExObs;
	}

	/**
	 * Sets the id ex obs.
	 *
	 * @param idExObs the new id ex obs
	 */
	public void setIdExObs(Long idExObs) {
		this.idExObs = idExObs;
	}

	/**
	 * Gets the cco.
	 *
	 * @return the cco
	 */
	public String getCco() {
		return cco;
	}

	/**
	 * Sets the cco.
	 *
	 * @param cco the new cco
	 */
	public void setCco(String cco) {
		this.cco = cco;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the comparisions.
	 *
	 * @return the comparisions
	 */
	public List<UraSendHistoricComparision> getComparisions() {
		return comparisions;
	}

	/**
	 * Sets the comparisions.
	 *
	 * @param comparisions the new comparisions
	 */
	public void setComparisions(List<UraSendHistoricComparision> comparisions) {
		this.comparisions = comparisions;
	}

	/**
	 * Gets the ranges.
	 *
	 * @return the ranges
	 */
	public List<UraSendHistoricRange> getRanges() {
		return ranges;
	}

	/**
	 * Sets the ranges.
	 *
	 * @param ranges the new ranges
	 */
	public void setRanges(List<UraSendHistoricRange> ranges) {
		this.ranges = ranges;
	}

	/**
	 * Gets the results.
	 *
	 * @return the results
	 */
	public List<UraSendHistoricResults> getResults() {
		return results;
	}

	/**
	 * Sets the results.
	 *
	 * @param results the new results
	 */
	public void setResults(List<UraSendHistoricResults> results) {
		this.results = results;
	}

	/**
	 * Gets the observatories.
	 *
	 * @return the observatories
	 */
	public List<ObservatorioRealizadoForm> getObservatories() {
		return observatories;
	}

	/**
	 * Sets the observatories.
	 *
	 * @param observatories the new observatories
	 */
	public void setObservatories(List<ObservatorioRealizadoForm> observatories) {
		this.observatories = observatories;
	}

	/**
	 * Gets the sended.
	 *
	 * @return the sended
	 */
	public List<UraSendHistoricResults> getSended() {
		return sended;
	}

	/**
	 * Sets the sended.
	 *
	 * @param sended the new sended
	 */
	public void setSended(List<UraSendHistoricResults> sended) {
		this.sended = sended;
	}

	/**
	 * Gets the not auto.
	 *
	 * @return the not auto
	 */
	public List<UraSendHistoricResults> getNotAuto() {
		return notAuto;
	}

	/**
	 * Sets the not auto.
	 *
	 * @param notAuto the new not auto
	 */
	public void setNotAuto(List<UraSendHistoricResults> notAuto) {
		this.notAuto = notAuto;
	}

	/**
	 * Gets the send date.
	 *
	 * @return the send date
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * Sets the send date.
	 *
	 * @param sendDate the new send date
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
}
