package es.inteco.rastreador2.actionform.cuentausuario;

import org.apache.struts.validator.ValidatorForm;

public class RastreoClienteForm extends ValidatorForm {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String idCuenta;
    private String cartucho;
    private String normaAnalisis;
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public String getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(String normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }
}
