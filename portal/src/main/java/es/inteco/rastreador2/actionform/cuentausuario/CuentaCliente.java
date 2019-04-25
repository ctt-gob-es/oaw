/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
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
