package es.inteco.rastreador2.action.export.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.MessageResources;

import es.gob.oaw.MailException;
import es.gob.oaw.MailService;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.export.database.form.ComparisionForm;
import es.inteco.rastreador2.utils.AnnexUtils;
import es.inteco.rastreador2.utils.SendResultsMailUtils;

/**
 * The Class SendMailThread.
 */
public class SendMailCalculateResultsThread extends Thread {
	/** The message resources. */
	private MessageResources messageResources;
	/** The id obs. */
	private Long idObs;
	/** The id obs execution. */
	private Long idObsExecution;
	/** The tags to filter. */
	private String[] tagsToFilter;
	/** The ex obs ids. */
	private String[] exObsIds;
	/** The comparision. */
	private List<ComparisionForm> comparision;
	/** The email. */
	private String email;
	private String baseURL;

	/**
	 * Instantiates a new send mail thread.
	 *
	 * @param messageResources the message resources
	 * @param idObs            the id obs
	 * @param idObsExecution   the id obs execution
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @param comparision      the comparision
	 * @param email            the email
	 */
	public SendMailCalculateResultsThread(final MessageResources messageResources, final Long idObs, final Long idObsExecution, final String[] tagsToFilter, final String[] exObsIds,
			final List<ComparisionForm> comparision, final String email, final String baseURL) {
		this.idObsExecution = idObsExecution;
		this.idObs = idObs;
		this.messageResources = messageResources;
		this.tagsToFilter = tagsToFilter;
		this.exObsIds = exObsIds;
		this.email = email;
		this.baseURL = baseURL;
	}

	/**
	 * Run.
	 */
	@Override
	public final void run() {
		try {
			Logger.putLog("Inicio de la generación de datos de evolución", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			AnnexUtils.generateEvolutionData(messageResources, idObs, idObsExecution, tagsToFilter, exObsIds, comparision);
			Logger.putLog("Fin de la generación de datos de evolución", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			// Send email to notify end of process
			final MailService mailService = new MailService();
			MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
			StringBuilder mailBody = new StringBuilder("<p>" + messageResources.getMessage("send.mail.calculate.end.process.mail.body", new String[] { getObservatoryName(idObs) }) + "</p>");
			StringBuilder linkUrl = new StringBuilder(baseURL);
			// &idExObs=266&isPrimary=false&idCartucho=9&id_observatorio=53&id=266&esPrimera=true
			linkUrl.append("secure/ConfigSendResultsByMailAction.do?action=finish");
			linkUrl.append("&idExObs=").append(this.idObsExecution);
			linkUrl.append("&&id_observatorio=").append(this.idObs);
			mailBody.append("<a href=\"").append(linkUrl.toString()).append("\">").append("Ver resultados").append("</a><br>");
			List<String> mailsTo = new ArrayList<>();
			mailsTo.add(email);
			try {
				mailService.sendMail(mailsTo, messageResources.getMessage("send.mail.calculate.end.process.mail.subject"), mailBody.toString(), true);
			} catch (MailException e) {
				Logger.putLog("Fallo al enviar el correo", SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR, e);
			}
		} catch (Exception e) {
			Logger.putLog("Excepcion: ", SendMailCalculateResultsThread.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the observatory name.
	 *
	 * @param idObservatory the id observatory
	 * @return the observatory name
	 */
	private static String getObservatoryName(final Long idObservatory) {
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			return observatoryForm.getNombre();
		} catch (Exception e) {
			return "";
		}
	}
}
