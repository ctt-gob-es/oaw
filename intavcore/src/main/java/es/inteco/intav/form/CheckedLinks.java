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
package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class CheckedLinks.
 */
public class CheckedLinks implements Serializable {

    /** The checked links. */
    private List<String> checkedLinks;
    
    /** The broken links. */
    private List<String> brokenLinks;
    
    /** The availablelinks. */
    private List<String> availablelinks;

    /**
	 * Instantiates a new checked links.
	 */
    public CheckedLinks() {
        brokenLinks = new ArrayList<>();
        availablelinks = new ArrayList<>();
        checkedLinks = new ArrayList<>();
    }

    /**
	 * Gets the broken links.
	 *
	 * @return the broken links
	 */
    public List<String> getBrokenLinks() {
        return brokenLinks;
    }

    /**
	 * Sets the broken links.
	 *
	 * @param brokenLinks the new broken links
	 */
    public void setBrokenLinks(List<String> brokenLinks) {
        this.brokenLinks = brokenLinks;
    }

    /**
	 * Gets the availablelinks.
	 *
	 * @return the availablelinks
	 */
    public List<String> getAvailablelinks() {
        return availablelinks;
    }

    /**
	 * Sets the availablelinks.
	 *
	 * @param availablelinks the new availablelinks
	 */
    public void setAvailablelinks(List<String> availablelinks) {
        this.availablelinks = availablelinks;
    }

    /**
	 * Gets the checked links.
	 *
	 * @return the checked links
	 */
    public List<String> getCheckedLinks() {
        return checkedLinks;
    }

    /**
	 * Sets the checked links.
	 *
	 * @param checkedLinks the new checked links
	 */
    public void setCheckedLinks(List<String> checkedLinks) {
        this.checkedLinks = checkedLinks;
    }
}
