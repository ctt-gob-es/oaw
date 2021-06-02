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
package es.inteco.rastreador2.imp.xml.result;

import es.inteco.common.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class SectionForm.
 */
public class SectionForm {

    /** The title. */
    private String title;
    
    /** The section number. */
    private int sectionNumber;
    
    /** The final section to paint. */
    private int finalSectionToPaint;
    
    /** The object list. */
    private List<Object> objectList;
    
    /** The type. */
    private String type;

    /**
	 * Instantiates a new section form.
	 */
    public SectionForm() {
        this.objectList = new ArrayList<>();
        this.title = "";
        this.type = Constants.OBJECT_TYPE_SECTION;
    }

    /**
	 * Gets the title.
	 *
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * Gets the object list.
	 *
	 * @return the object list
	 */
    public List<Object> getObjectList() {
        return objectList;
    }

    /**
	 * Sets the object list.
	 *
	 * @param objectList the new object list
	 */
    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    /**
	 * Adds the object.
	 *
	 * @param object the object
	 */
    public void addObject(Object object) {
        objectList.add(object);
    }

    /**
	 * Gets the section number.
	 *
	 * @return the section number
	 */
    public int getSectionNumber() {
        return sectionNumber;
    }

    /**
	 * Sets the section number.
	 *
	 * @param sectionNumber the new section number
	 */
    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    /**
	 * Gets the type.
	 *
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * Gets the final section to paint.
	 *
	 * @return the final section to paint
	 */
    public int getFinalSectionToPaint() {
        return finalSectionToPaint;
    }

    /**
	 * Sets the final section to paint.
	 *
	 * @param finalSectionToPaint the new final section to paint
	 */
    public void setFinalSectionToPaint(int finalSectionToPaint) {
        this.finalSectionToPaint = finalSectionToPaint;
    }
}
