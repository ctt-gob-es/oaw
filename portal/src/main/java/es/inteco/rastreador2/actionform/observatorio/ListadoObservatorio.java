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
package es.inteco.rastreador2.actionform.observatorio;

import java.util.List;

public class ListadoObservatorio {

    private String nombreObservatorio;
    private List<String> dominio;
    private Long id_observatorio;
    private long id_cartucho;
    private String cartucho;
    private String tipo;

    public String getCartucho() {
        return cartucho;
    }

    public void setCartucho(String cartucho) {
        this.cartucho = cartucho;
    }

    public String getNombreObservatorio() {
        return nombreObservatorio;
    }

    public void setNombreObservatorio(String nombreObservatorio) {
        this.nombreObservatorio = nombreObservatorio;
    }

    public List<String> getDominio() {
        return dominio;
    }

    public void setDominio(List<String> dominio) {
        this.dominio = dominio;
    }

    public Long getId_observatorio() {
        return id_observatorio;
    }

    public void setId_observatorio(Long id_observatorio) {
        this.id_observatorio = id_observatorio;
    }

    public long getId_cartucho() {
        return id_cartucho;
    }

    public void setId_cartucho(long id_cartucho) {
        this.id_cartucho = id_cartucho;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
