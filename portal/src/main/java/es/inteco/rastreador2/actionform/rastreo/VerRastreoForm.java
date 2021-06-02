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
package es.inteco.rastreador2.actionform.rastreo;

import org.apache.struts.action.ActionForm;

import java.util.List;


/**
 * The Class VerRastreoForm.
 */
public class VerRastreoForm extends ActionForm {


    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id rastreo. */
    private long id_rastreo;
    
    /** The id usuario. */
    private int id_usuario;
    
    /** The nombre usuario. */
    private String nombre_usuario;
    
    /** The norma analisis. */
    private long normaAnalisis;
    
    /** The norma analisis st. */
    private String normaAnalisisSt;
    
    /** The id cartucho. */
    private int id_cartucho;
    
    /** The nombre cartucho. */
    private String nombre_cartucho;
    
    /** The url semilla. */
    private List<String> url_semilla;
    
    /** The profundidad. */
    private int profundidad;
    
    /** The top N ver. */
    private String topN_ver;
    
    /** The fecha. */
    private String fecha;
    
    /** The rastreo. */
    private String rastreo;
    
    /** The fecha lanzado. */
    private String fechaLanzado;
    
    /** The lista rastreable. */
    private String listaRastreable;
    
    /** The url lista rastreable. */
    private List<String> url_listaRastreable;
    
    /** The lista no rastreable. */
    private String listaNoRastreable;
    
    /** The url lista no rastreable. */
    private List<String> url_listaNoRastreable;
    
    /** The cuenta cliente. */
    private String cuentaCliente;
    
    /** The automatico. */
    private int automatico;
    
    /** The activo. */
    private long activo;
    
    /** The pseudo aleatorio. */
    private String pseudoAleatorio;
    
    /** The in directory. */
    private boolean inDirectory;

    /**
	 * Gets the cuenta cliente.
	 *
	 * @return the cuenta cliente
	 */
    public String getCuentaCliente() {
        return cuentaCliente;
    }

    /**
	 * Sets the cuenta cliente.
	 *
	 * @param cuentaCliente the new cuenta cliente
	 */
    public void setCuentaCliente(String cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
    }

    /**
	 * Gets the fecha lanzado.
	 *
	 * @return the fecha lanzado
	 */
    public String getFechaLanzado() {
        return fechaLanzado;
    }

    /**
	 * Sets the fecha lanzado.
	 *
	 * @param fechaLanzado the new fecha lanzado
	 */
    public void setFechaLanzado(String fechaLanzado) {
        this.fechaLanzado = fechaLanzado;
    }

    /**
	 * Gets the lista rastreable.
	 *
	 * @return the lista rastreable
	 */
    public String getListaRastreable() {
        return listaRastreable;
    }

    /**
	 * Sets the lista rastreable.
	 *
	 * @param listaRastreable the new lista rastreable
	 */
    public void setListaRastreable(String listaRastreable) {
        this.listaRastreable = listaRastreable;
    }

    /**
	 * Gets the lista no rastreable.
	 *
	 * @return the lista no rastreable
	 */
    public String getListaNoRastreable() {
        return listaNoRastreable;
    }

    /**
	 * Sets the lista no rastreable.
	 *
	 * @param listaNoRastreable the new lista no rastreable
	 */
    public void setListaNoRastreable(String listaNoRastreable) {
        this.listaNoRastreable = listaNoRastreable;
    }

    /**
	 * Gets the serial version UID.
	 *
	 * @return the serial version UID
	 */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
	 * Gets the rastreo.
	 *
	 * @return the rastreo
	 */
    public String getRastreo() {
        return rastreo;
    }

    /**
	 * Sets the rastreo.
	 *
	 * @param rastreo the new rastreo
	 */
    public void setRastreo(String rastreo) {
        this.rastreo = rastreo;
    }

    /**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
    public String getFecha() {
        return fecha;
    }

    /**
	 * Sets the fecha.
	 *
	 * @param fecha the new fecha
	 */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
	 * Gets the profundidad.
	 *
	 * @return the profundidad
	 */
    public int getProfundidad() {
        return profundidad;
    }

    /**
	 * Sets the profundidad.
	 *
	 * @param profundidad the new profundidad
	 */
    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    /**
	 * Gets the nombre cartucho.
	 *
	 * @return the nombre cartucho
	 */
    public String getNombre_cartucho() {
        return nombre_cartucho;
    }

    /**
	 * Sets the nombre cartucho.
	 *
	 * @param nombre_cartucho the new nombre cartucho
	 */
    public void setNombre_cartucho(String nombre_cartucho) {
        this.nombre_cartucho = nombre_cartucho;
    }

    /**
	 * Gets the url semilla.
	 *
	 * @return the url semilla
	 */
    public List<String> getUrl_semilla() {
        return url_semilla;
    }

    /**
	 * Sets the url semilla.
	 *
	 * @param url_semilla the new url semilla
	 */
    public void setUrl_semilla(List<String> url_semilla) {
        this.url_semilla = url_semilla;
    }

    /**
	 * Gets the id rastreo.
	 *
	 * @return the id rastreo
	 */
    public long getId_rastreo() {
        return id_rastreo;
    }

    /**
	 * Sets the id rastreo.
	 *
	 * @param id_rastreo the new id rastreo
	 */
    public void setId_rastreo(long id_rastreo) {
        this.id_rastreo = id_rastreo;
    }

    /**
	 * Gets the id usuario.
	 *
	 * @return the id usuario
	 */
    public int getId_usuario() {
        return id_usuario;
    }

    /**
	 * Sets the id usuario.
	 *
	 * @param id_usuario the new id usuario
	 */
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    /**
	 * Gets the nombre usuario.
	 *
	 * @return the nombre usuario
	 */
    public String getNombre_usuario() {
        return nombre_usuario;
    }

    /**
	 * Sets the nombre usuario.
	 *
	 * @param nombre_usuario the new nombre usuario
	 */
    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    /**
	 * Gets the id cartucho.
	 *
	 * @return the id cartucho
	 */
    public int getId_cartucho() {
        return id_cartucho;
    }

    /**
	 * Sets the id cartucho.
	 *
	 * @param id_cartucho the new id cartucho
	 */
    public void setId_cartucho(int id_cartucho) {
        this.id_cartucho = id_cartucho;
    }

    /**
	 * Gets the norma analisis.
	 *
	 * @return the norma analisis
	 */
    public long getNormaAnalisis() {
        return normaAnalisis;
    }

    /**
	 * Sets the norma analisis.
	 *
	 * @param normaAnalisis the new norma analisis
	 */
    public void setNormaAnalisis(long normaAnalisis) {
        this.normaAnalisis = normaAnalisis;
    }

    /**
	 * Gets the norma analisis st.
	 *
	 * @return the norma analisis st
	 */
    public String getNormaAnalisisSt() {
        return normaAnalisisSt;
    }

    /**
	 * Sets the norma analisis st.
	 *
	 * @param normaAnalisisSt the new norma analisis st
	 */
    public void setNormaAnalisisSt(String normaAnalisisSt) {
        this.normaAnalisisSt = normaAnalisisSt;
    }

    /**
	 * Gets the automatico.
	 *
	 * @return the automatico
	 */
    public int getAutomatico() {
        return automatico;
    }

    /**
	 * Sets the automatico.
	 *
	 * @param automatico the new automatico
	 */
    public void setAutomatico(int automatico) {
        this.automatico = automatico;
    }

    /**
	 * Gets the url lista rastreable.
	 *
	 * @return the url lista rastreable
	 */
    public List<String> getUrl_listaRastreable() {
        return url_listaRastreable;
    }

    /**
	 * Sets the url lista rastreable.
	 *
	 * @param url_listaRastreable the new url lista rastreable
	 */
    public void setUrl_listaRastreable(List<String> url_listaRastreable) {
        this.url_listaRastreable = url_listaRastreable;
    }

    /**
	 * Gets the url lista no rastreable.
	 *
	 * @return the url lista no rastreable
	 */
    public List<String> getUrl_listaNoRastreable() {
        return url_listaNoRastreable;
    }

    /**
	 * Sets the url lista no rastreable.
	 *
	 * @param url_listaNoRastreable the new url lista no rastreable
	 */
    public void setUrl_listaNoRastreable(List<String> url_listaNoRastreable) {
        this.url_listaNoRastreable = url_listaNoRastreable;
    }

    /**
	 * Gets the activo.
	 *
	 * @return the activo
	 */
    public long getActivo() {
        return activo;
    }

    /**
	 * Sets the activo.
	 *
	 * @param activo the new activo
	 */
    public void setActivo(long activo) {
        this.activo = activo;
    }

    /**
	 * Gets the top N ver.
	 *
	 * @return the top N ver
	 */
    public String getTopN_ver() {
        return topN_ver;
    }

    /**
	 * Sets the top N ver.
	 *
	 * @param topN_ver the new top N ver
	 */
    public void setTopN_ver(String topN_ver) {
        this.topN_ver = topN_ver;
    }

    /**
	 * Gets the pseudo aleatorio.
	 *
	 * @return the pseudo aleatorio
	 */
    public String getPseudoAleatorio() {
        return pseudoAleatorio;
    }

    /**
	 * Sets the pseudo aleatorio.
	 *
	 * @param pseudoAleatorio the new pseudo aleatorio
	 */
    public void setPseudoAleatorio(String pseudoAleatorio) {
        this.pseudoAleatorio = pseudoAleatorio;
    }

    /**
	 * Checks if is in directory.
	 *
	 * @return true, if is in directory
	 */
    public boolean isInDirectory() {
        return inDirectory;
    }

    /**
	 * Sets the in directory.
	 *
	 * @param inDirectory the new in directory
	 */
    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }

}
