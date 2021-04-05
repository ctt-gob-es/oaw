package es.inteco.rastreador2.action.export.database;

import es.inteco.rastreador2.utils.SendResultsMailUtils;

/**
 * The Class SendMailThread.
 */
public class SendMailThread extends Thread {
	/** The id obs execution. */
	private Long idObsExecution;
	/** The id obs. */
	private Long idObs;
	/** The id cartucho. */
	private Long idCartucho;
	/** The email subject. */
	private String emailSubject;
	/** The cco. */
	private String cco;
	private String notifyMail;

	/**
	 * Instantiates a new send mail thread.
	 *
	 * @param idObs          the id obs
	 * @param idObsExecution the id obs execution
	 * @param idCartucho     the id cartucho
	 * @param emailSubject   the email subject
	 * @param cco            the cco
	 */
	public SendMailThread(Long idObs, Long idObsExecution, Long idCartucho, String emailSubject, String cco, String notifyMail) {
		this.idObsExecution = idObsExecution;
		this.idObs = idObs;
		this.idCartucho = idCartucho;
		this.emailSubject = emailSubject;
		this.cco = cco;
		this.notifyMail = notifyMail;
	}

	/**
	 * Run.
	 */
	@Override
	public final void run() {
		try {
			SendResultsMailUtils.generateAndSendData(idObs, idCartucho, idObsExecution, emailSubject, cco, notifyMail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
