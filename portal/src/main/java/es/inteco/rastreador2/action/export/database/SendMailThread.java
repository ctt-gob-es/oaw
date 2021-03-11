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
	private String emailSubject;

	/**
	 * Instantiates a new send mail thread.
	 *
	 * @param idObsExecution the id obs execution
	 * @param idObs          the id obs
	 * @param idCartucho     the id cartucho
	 */
	public SendMailThread(Long idObs, Long idObsExecution, Long idCartucho, String emailSubject) {
		this.idObsExecution = idObsExecution;
		this.idObs = idObs;
		this.idCartucho = idCartucho;
		this.emailSubject = emailSubject;
	}

	/**
	 * Run.
	 */
	@Override
	public final void run() {
		try {
			SendResultsMailUtils.generateAndSendData(idObs, idCartucho, idObsExecution, emailSubject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
