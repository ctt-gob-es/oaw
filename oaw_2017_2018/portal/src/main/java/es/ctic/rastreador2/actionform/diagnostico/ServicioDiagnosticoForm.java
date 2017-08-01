package es.ctic.rastreador2.actionform.diagnostico;

import org.apache.struts.validator.ValidatorForm;

/**
 * Form para obtener los parámetros de exportación de datos de uso del servicio de diagnóstico
 * @author miguel.garcia
 */
public class ServicioDiagnosticoForm extends ValidatorForm {

    private String startDate;
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
