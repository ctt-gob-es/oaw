package es.inteco.rastreador2.lenox.form;

import java.io.Serializable;

public class ObservatoryLenoxForm implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long idExecution;
    private int terminosLocalizados;
    private int terminosAlta;
    private int terminosMedia;
    private int terminosBaja;


    public int getTerminosLocalizados() {
        return terminosLocalizados;
    }

    public void setTerminosLocalizados(int terminosLocalizados) {
        this.terminosLocalizados = terminosLocalizados;
    }

    public int getTerminosAlta() {
        return terminosAlta;
    }

    public void setTerminosAlta(int terminosAlta) {
        this.terminosAlta = terminosAlta;
    }

    public int getTerminosMedia() {
        return terminosMedia;
    }

    public void setTerminosMedia(int terminosMedia) {
        this.terminosMedia = terminosMedia;
    }

    public int getTerminosBaja() {
        return terminosBaja;
    }

    public void setTerminosBaja(int terminosBaja) {
        this.terminosBaja = terminosBaja;
    }

    public Long getIdExecution() {
        return idExecution;
    }

    public void setIdExecution(Long idExecution) {
        this.idExecution = idExecution;
    }
}
