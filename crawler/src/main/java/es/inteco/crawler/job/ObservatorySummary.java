package es.inteco.crawler.job;

/**
 * The Class ObservatorySummary.
 */
public class ObservatorySummary {
	/** The estado. */
	private String estado;
	/** The id estado. */
	private Long idEstado;
	/** The total semillas. */
	private int totalSemillas;
	/** The total semillas ok. */
	private int semillasAnalizadasOk;
	/** The semillas analizadas. */
	private int semillasAnalizadas;
	/** The porcentaje completado. */
	private float porcentajeCompletado;
	/** The porcentaje completado ok. */
	private float porcentajeCompletadoOk;
	/** The tiempo estimado. */
	private int tiempoEstimado;
	/** The tiempo estimado horas. */
	private int tiempoEstimadoHoras;
	/** The tiempo total. */
	private int tiempoTotal;
	/** The tiempo total horas. */
	private int tiempoTotalHoras;
	/** The tiempo medio. */
	private int tiempoMedio;
	/** The tiempo minimo. */
	private ObservatorySummaryTimes tiempoMinimo;
	/** The tiempo maximo. */
	private ObservatorySummaryTimes tiempoMaximo;

	/**
	 * Gets the estado.
	 *
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Gets the porcentaje completado ok.
	 *
	 * @return the porcentaje completado ok
	 */
	public float getPorcentajeCompletadoOk() {
		return porcentajeCompletadoOk;
	}

	/**
	 * Sets the porcentaje completado ok.
	 *
	 * @param porcentajeCompletadoOk the new porcentaje completado ok
	 */
	public void setPorcentajeCompletadoOk(float porcentajeCompletadoOk) {
		this.porcentajeCompletadoOk = porcentajeCompletadoOk;
	}

	/**
	 * Gets the semillas analizadas ok.
	 *
	 * @return the semillas analizadas ok
	 */
	public int getSemillasAnalizadasOk() {
		return semillasAnalizadasOk;
	}

	/**
	 * Sets the semillas analizadas ok.
	 *
	 * @param semillasAnalizadasOk the new semillas analizadas ok
	 */
	public void setSemillasAnalizadasOk(int semillasAnalizadasOk) {
		this.semillasAnalizadasOk = semillasAnalizadasOk;
	}

	/**
	 * Sets the estado.
	 *
	 * @param estado the new estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Gets the total semillas.
	 *
	 * @return the total semillas
	 */
	public int getTotalSemillas() {
		return totalSemillas;
	}

	/**
	 * Sets the total semillas.
	 *
	 * @param totalSemillas the new total semillas
	 */
	public void setTotalSemillas(int totalSemillas) {
		this.totalSemillas = totalSemillas;
	}

	/**
	 * Gets the semillas analizadas.
	 *
	 * @return the semillas analizadas
	 */
	public int getSemillasAnalizadas() {
		return semillasAnalizadas;
	}

	/**
	 * Sets the semillas analizadas.
	 *
	 * @param semillasAnalizadas the new semillas analizadas
	 */
	public void setSemillasAnalizadas(int semillasAnalizadas) {
		this.semillasAnalizadas = semillasAnalizadas;
	}

	/**
	 * Gets the porcentaje completado.
	 *
	 * @return the porcentaje completado
	 */
	public float getPorcentajeCompletado() {
		return porcentajeCompletado;
	}

	/**
	 * Sets the porcentaje completado.
	 *
	 * @param porcentajeCompletado the new porcentaje completado
	 */
	public void setPorcentajeCompletado(float porcentajeCompletado) {
		this.porcentajeCompletado = porcentajeCompletado;
	}

	/**
	 * Gets the tiempo estimado.
	 *
	 * @return the tiempo estimado
	 */
	public int getTiempoEstimado() {
		return tiempoEstimado;
	}

	/**
	 * Sets the tiempo estimado.
	 *
	 * @param tiempoEstimado the new tiempo estimado
	 */
	public void setTiempoEstimado(int tiempoEstimado) {
		this.tiempoEstimado = tiempoEstimado;
	}

	/**
	 * Gets the tiempo total.
	 *
	 * @return the tiempo total
	 */
	public int getTiempoTotal() {
		return tiempoTotal;
	}

	/**
	 * Sets the tiempo total.
	 *
	 * @param tiempoTotal the new tiempo total
	 */
	public void setTiempoTotal(int tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

	/**
	 * Gets the tiempo medio.
	 *
	 * @return the tiempo medio
	 */
	public int getTiempoMedio() {
		return tiempoMedio;
	}

	/**
	 * Sets the tiempo medio.
	 *
	 * @param tiempoMedio the new tiempo medio
	 */
	public void setTiempoMedio(int tiempoMedio) {
		this.tiempoMedio = tiempoMedio;
	}

	/**
	 * Gets the tiempo minimo.
	 *
	 * @return the tiempo minimo
	 */
	public ObservatorySummaryTimes getTiempoMinimo() {
		return tiempoMinimo;
	}

	/**
	 * Sets the tiempo minimo.
	 *
	 * @param tiempoMinimo the new tiempo minimo
	 */
	public void setTiempoMinimo(ObservatorySummaryTimes tiempoMinimo) {
		this.tiempoMinimo = tiempoMinimo;
	}

	/**
	 * Gets the tiempo maximo.
	 *
	 * @return the tiempo maximo
	 */
	public ObservatorySummaryTimes getTiempoMaximo() {
		return tiempoMaximo;
	}

	/**
	 * Sets the tiempo maximo.
	 *
	 * @param tiempoMaximo the new tiempo maximo
	 */
	public void setTiempoMaximo(ObservatorySummaryTimes tiempoMaximo) {
		this.tiempoMaximo = tiempoMaximo;
	}

	/**
	 * Gets the tiempo total horas.
	 *
	 * @return the tiempo total horas
	 */
	public int getTiempoTotalHoras() {
		return tiempoTotalHoras;
	}

	/**
	 * Sets the tiempo total horas.
	 *
	 * @param tiempoTotalHoras the new tiempo total horas
	 */
	public void setTiempoTotalHoras(int tiempoTotalHoras) {
		this.tiempoTotalHoras = tiempoTotalHoras;
	}

	/**
	 * Gets the tiempo estimado horas.
	 *
	 * @return the tiempo estimado horas
	 */
	public int getTiempoEstimadoHoras() {
		return tiempoEstimadoHoras;
	}

	/**
	 * Sets the tiempo estimado horas.
	 *
	 * @param tiempoEstimadoHoras the new tiempo estimado horas
	 */
	public void setTiempoEstimadoHoras(int tiempoEstimadoHoras) {
		this.tiempoEstimadoHoras = tiempoEstimadoHoras;
	}

	/**
	 * Gets the id estado.
	 *
	 * @return the id estado
	 */
	public Long getIdEstado() {
		return idEstado;
	}

	/**
	 * Sets the id estado.
	 *
	 * @param idEstado the new id estado
	 */
	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}
}
