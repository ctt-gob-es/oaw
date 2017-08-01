package es.inteco.rastreador2.actionform.cuentausuario;

import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;

import java.sql.Timestamp;

public class CuentaCliente {
    private Long idCuenta;
    private boolean active;
    private String nombre;
    private PeriodicidadForm periodicidad;
    private Timestamp fecha;
    private DatosCartuchoRastreoForm datosRastreo;

    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PeriodicidadForm getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(PeriodicidadForm periodicidad) {
        this.periodicidad = periodicidad;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public DatosCartuchoRastreoForm getDatosRastreo() {
        return datosRastreo;
    }

    public void setDatosRastreo(DatosCartuchoRastreoForm datosRastreo) {
        this.datosRastreo = datosRastreo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
