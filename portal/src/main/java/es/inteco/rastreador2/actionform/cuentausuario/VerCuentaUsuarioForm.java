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

import org.apache.struts.action.ActionForm;

import java.util.List;


public class VerCuentaUsuarioForm extends ActionForm {


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String nombre;
    private List<String> dominio;
    private List<String> cartuchos;
    private String periodicidad;
    private String profundidad;
    private String amplitud;
    private List<String> responsable;
    private List<String> usuarios;
    private Long normaAnalisis;
    private String normaAnalisisSt;
    private List<String> listaRastreable;
    private List<String> listaNoRastreable;
    private String pseudoAleatorio;
    private boolean activo;
    private boolean inDirectory;

    public List<String> getListaRastreable() {
        return listaRastreable;
    }

    public void setListaRastreable(List<String> listaRastreable) {
        this.listaRastreable = listaRastreable;
    }

    public List<String> getListaNoRastreable() {
        return listaNoRastreable;
    }

    public void setListaNoRastreable(List<String> listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
    }

    public List<String> getResponsable() {
        return responsable;
    }

    public void setResponsable(List<String> responsable) {
        this.responsable = responsable;
    }

    public List<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getDominio() {
        return dominio;
    }

    public void setDominio(List<String> dominio) {
        this.dominio = dominio;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    public String getAmplitud() {
        return amplitud;
    }

    public void setAmplitud(String amplitud) {
        this.amplitud = amplitud;
    }

    public List<String> getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(List<String> cartuchos) {
        this.cartuchos = cartuchos;
    }

    public Long getNormaAnalisis() {
        return normaAnalisis;
    }

    public void setNormaAnalisis(Long normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    public String getNormaAnalisisSt() {
        return normaAnalisisSt;
    }

    public void setNormaAnalisisSt(String normaAnalisisSt) {
        this.normaAnalisisSt = normaAnalisisSt;
    }

    public String getPseudoAleatorio() {
        return pseudoAleatorio;
    }

    public void setPseudoAleatorio(String pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }
}
