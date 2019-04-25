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
package es.inteco.rastreador2.dao.rastreo;


import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class DatosCartuchoRastreoForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String nombreRastreo;
    private int id_rastreo;
    private int id_cartucho;
    private String nombre_cart;
    private String nombre_rastreo;
    private int numRastreos;
    private int profundidad;
    private int topN;
    private long idSemilla;
    private String seedAcronym;
    private String listaNoRastreable;
    private String listaRastreable;
    private long idCuentaCliente;
    private long idObservatory;
    private boolean pseudoaleatorio;
    private boolean exhaustive;
    private boolean inDirectory;
    private LenguajeForm language;
    private String[] cartuchos;
    private List<String> urls;
    private List<String> domains;
    private List<String> exceptions;
    private List<String> crawlingList;
    private long id_guideline;
    private String ficheroNorma;

    public String getFicheroNorma() {
        return ficheroNorma;
    }

    public void setFicheroNorma(String ficheroNorma) {
        this.ficheroNorma = ficheroNorma;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public long getId_guideline() {
        return id_guideline;
    }

    public void setId_guideline(long id_guideline) {
        this.id_guideline = id_guideline;
    }

    public String[] getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(String[] cartuchos) {
        this.cartuchos = cartuchos;
    }

    public int getId_rastreo() {
        return id_rastreo;
    }

    public void setId_rastreo(int id_rastreo) {
        this.id_rastreo = id_rastreo;
    }

    public int getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    public String getNombre_cart() {
        return nombre_cart;
    }

    public void setNombre_cart(String nombre_cart) {
        this.nombre_cart = nombre_cart;
    }

    public int getNumRastreos() {
        return numRastreos;
    }

    public void setNumRastreos(int numRastreos) {
        this.numRastreos = numRastreos;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public int getTopN() {
        return topN;
    }

    public void setTopN(int topN) {
        this.topN = topN;
    }

    public String getNombre_rastreo() {
        return nombre_rastreo;
    }

    public void setNombre_rastreo(String nombre_rastreo) {
        this.nombre_rastreo = nombre_rastreo;
    }

    public String getListaNoRastreable() {
        return listaNoRastreable;
    }

    public void setListaNoRastreable(String listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
    }

    public String getListaRastreable() {
        return listaRastreable;
    }

    public void setListaRastreable(String listaRastreable) {
        this.listaRastreable = listaRastreable;
    }

    public long getIdCuentaCliente() {
        return idCuentaCliente;
    }

    public void setIdCuentaCliente(long idCuentaCliente) {
        this.idCuentaCliente = idCuentaCliente;
    }

    public long getIdObservatory() {
        return idObservatory;
    }

    public void setIdObservatory(long idObservatory) {
        this.idObservatory = idObservatory;
    }

    public boolean isPseudoaleatorio() {
        return pseudoaleatorio;
    }

    public void setPseudoaleatorio(boolean pseudoaleatorio) {
        this.pseudoaleatorio = pseudoaleatorio;
    }

    public String getNombreRastreo() {
        return nombreRastreo;
    }

    public void setNombreRastreo(String nombreRastreo) {
        this.nombreRastreo = nombreRastreo;
    }

    public LenguajeForm getLanguage() {
        return language;
    }

    public void setLanguage(LenguajeForm language) {
        this.language = language;
    }

    public long getIdSemilla() {
        return idSemilla;
    }

    public void setIdSemilla(long idSemilla) {
        this.idSemilla = idSemilla;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (this.language == null) {
            this.language = new LenguajeForm();
        }
        if (this.urls == null) {
            this.urls = new ArrayList<>();
        }
        if (this.domains == null) {
            this.domains = new ArrayList<>();
        }
        if (this.exceptions == null) {
            this.exceptions = new ArrayList<>();
        }
        if (this.crawlingList == null) {
            this.crawlingList = new ArrayList<>();
        }
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }

    public List<String> getCrawlingList() {
        return crawlingList;
    }

    public void setCrawlingList(List<String> crawlingList) {
        this.crawlingList = crawlingList;
    }

    public boolean isExhaustive() {
        return exhaustive;
    }

    public void setExhaustive(boolean exhaustive) {
        this.exhaustive = exhaustive;
    }

    public String getSeedAcronym() {
        return seedAcronym;
    }

    public void setSeedAcronym(String seedAcronym) {
        this.seedAcronym = seedAcronym;
    }

    public boolean isInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }
}