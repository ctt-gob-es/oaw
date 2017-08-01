package es.inteco.rastreador2.actionform.observatorio;

import es.inteco.rastreador2.dao.login.CartuchoForm;

import java.util.Date;

public class ObservatorioRealizadoForm {

    private long id;
    private ObservatorioForm observatorio;
    private CartuchoForm cartucho;
    private Date fecha;
    private String fechaStr;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ObservatorioForm getObservatorio() {
        return observatorio;
    }

    public void setObservatorio(ObservatorioForm observatorio) {
        this.observatorio = observatorio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFechaStr() {
        return fechaStr;
    }

    public void setFechaStr(String fechaStr) {
        this.fechaStr = fechaStr;
    }

    public CartuchoForm getCartucho() {
        return cartucho;
    }

    public void setCartucho(CartuchoForm cartucho) {
        this.cartucho = cartucho;
    }
}
