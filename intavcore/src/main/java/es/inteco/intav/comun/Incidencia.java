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
package es.inteco.intav.comun;

public class Incidencia {

    private int codigoIncidencia;
    private int codigoComprobacion;
    private long codigoAnalisis;
    private int codigoLineaFuente;
    private int codigoColumnaFuente;
    private String codigoFuente;

    public int getCodigoIncidencia() {
        return codigoIncidencia;
    }

    public void setCodigoIncidencia(int codigoIncidencia) {
        this.codigoIncidencia = codigoIncidencia;
    }

    public int getCodigoComprobacion() {
        return codigoComprobacion;
    }

    public void setCodigoComprobacion(int codigoComprobacion) {
        this.codigoComprobacion = codigoComprobacion;
    }

    public int getCodigoLineaFuente() {
        return codigoLineaFuente;
    }

    public void setCodigoLineaFuente(int codigoLineaFuente) {
        this.codigoLineaFuente = codigoLineaFuente;
    }

    public int getCodigoColumnaFuente() {
        return codigoColumnaFuente;
    }

    public void setCodigoColumnaFuente(int codigoColumnaFuente) {
        this.codigoColumnaFuente = codigoColumnaFuente;
    }

    public String getCodigoFuente() {
        return codigoFuente;
    }

    public void setCodigoFuente(String codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    public long getCodigoAnalisis() {
        return codigoAnalisis;
    }

    public void setCodigoAnalisis(long codigoAnalisis) {
        this.codigoAnalisis = codigoAnalisis;
    }
}
