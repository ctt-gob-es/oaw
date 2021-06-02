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
package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Class MenuTag.
 */
public class MenuTag extends TagSupport {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The roles. */
    private String roles = null;
    
    /** The no roles. */
    private String noRoles = null;

    /**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
    public String getRoles() {
        return roles;
    }

    /**
	 * Sets the roles.
	 *
	 * @param roles the new roles
	 */
    public void setRoles(String roles) {
        this.roles = roles;
    }

    /**
	 * Gets the no roles.
	 *
	 * @return the no roles
	 */
    public String getNoRoles() {
        return noRoles;
    }

    /**
	 * Sets the no roles.
	 *
	 * @param noRoles the new no roles
	 */
    public void setNoRoles(String noRoles) {
        this.noRoles = noRoles;
    }

    /**
	 * Contains user rol.
	 *
	 * @param userRoles the user roles
	 * @return true, if successful
	 */
    private boolean containsUserRol(String userRoles) {
        ArrayList<String> role = (ArrayList<String>) pageContext.getSession().getAttribute(Constants.ROLE);
        String[] roles = userRoles.split(";");
        boolean hasRol = false;
        for (int i = 0; i < roles.length && !hasRol; i++) {
            if (role.contains(roles[i])) {
                hasRol = true;
            }
        }
        return hasRol;
    }

    /**
	 * Do start tag.
	 *
	 * @return the int
	 * @throws JspException the jsp exception
	 */
    public int doStartTag() throws JspException {
        if (this.noRoles != null) {
            if (this.roles != null && containsUserRol(this.roles) && !containsUserRol(this.noRoles)) {
                return EVAL_BODY_INCLUDE;
            }
        } else {
            if (this.roles != null && containsUserRol(this.roles)) {
                return EVAL_BODY_INCLUDE;
            }
        }
        return SKIP_BODY;
    }

    /**
	 * Do end tag.
	 *
	 * @return the int
	 * @throws JspException the jsp exception
	 */
    public int doEndTag() throws JspException {
        // Imprime el elemento de cierre.
        JspWriter writer = pageContext.getOut();
        try {
            writer.print("");
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

}
