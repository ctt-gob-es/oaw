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
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.PageForm;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * The Class Pagination.
 */
public final class Pagination {

    /**
	 * Instantiates a new pagination.
	 */
    private Pagination() {
    }

    /**
	 * Creates the pagination.
	 *
	 * @param request     the request
	 * @param numResult   the num result
	 * @param currentPage the current page
	 * @return the list
	 */
    public static List<PageForm> createPagination(HttpServletRequest request, int numResult, int currentPage) {
        final PropertiesManager properties = new PropertiesManager();
        return createPagination(request, numResult, properties.getValue(CRAWLER_PROPERTIES, "pagination.size"), currentPage, Constants.PAG_PARAM);
    }

    /**
	 * Creates the pagination.
	 *
	 * @param request     the request
	 * @param numResult   the num result
	 * @param pageSize    the page size
	 * @param currentPage the current page
	 * @param parameter   the parameter
	 * @return the list
	 */
    public static List<PageForm> createPagination(HttpServletRequest request, int numResult, String pageSize, int currentPage, String parameter) {
        final PropertiesManager pmgr = new PropertiesManager();

        final int numPages = (int) Math.ceil((float) numResult / (float) Integer.parseInt(pageSize));
        final int numShowedPages = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.showed.pages"));

        int begin = 1;
        int end = numPages;

        if ((numPages > numShowedPages)) {
            if (currentPage <= numShowedPages / 2) {
                end = numShowedPages;
            } else {
                begin = currentPage - numShowedPages / 2;
                if ((currentPage + numShowedPages / 2) <= numPages) {
                    end = currentPage + numShowedPages / 2;
                } else {
                    begin = numPages - numShowedPages + 1;
                }
            }
        }
        return createPageList(request, currentPage, begin, end, parameter, numPages);
    }

    /**
	 * Creates the page list.
	 *
	 * @param request     the request
	 * @param currentPage the current page
	 * @param begin       the begin
	 * @param end         the end
	 * @param parameter   the parameter
	 * @param numPages    the num pages
	 * @return the list
	 */
    private static List<PageForm> createPageList(HttpServletRequest request, int currentPage, int begin, int end, String parameter, int numPages) {
        List<PageForm> pageFormList = new ArrayList<>();

        String path = request.getRequestURI() + "?";
        if (request.getQueryString() != null) {
            path += request.getQueryString();
        }
        path = removeParameterP(path, parameter);

        PageForm pForm;
        final MessageResources messageResources = CrawlerUtils.getResources(request);
        if (currentPage != 1) {
            if (begin != 1) {
                pForm = new PageForm(messageResources.getMessage("linkList.beginning"),
                        calculatePath(path, parameter).concat("1"), Constants.PAGINATION_PF_STYLE, true);
                pageFormList.add(pForm);
            }

            pForm = new PageForm(messageResources.getMessage("linkList.previous"),
                    calculatePath(path, parameter).concat(String.valueOf((currentPage - 1))), Constants.PAGINATION_PF_STYLE, true);
            pageFormList.add(pForm);
        }

        for (int i = begin; i <= end; i++) {
            pForm = new PageForm();
            pForm.setTitle(String.valueOf(i));
            if (i != currentPage) {
                pForm.setPath(calculatePath(path, parameter).concat(String.valueOf(i)));
                pForm.setStyleClass(Constants.PAGINATION_STYLE);
                pForm.setActive(true);
            } else {
                pForm.setStyleClass(Constants.PAGINATION_CURRENT_STYLE);
                pForm.setActive(false);
            }
            pageFormList.add(pForm);
        }

        if (currentPage != numPages) {
            pForm = new PageForm(messageResources.getMessage("linkList.following"),
                    calculatePath(path, parameter).concat(String.valueOf((currentPage + 1))), Constants.PAGINATION_PF_STYLE, true);
            pageFormList.add(pForm);

            if (end < numPages) {
                pForm = new PageForm(messageResources.getMessage("linkList.end"),
                        calculatePath(path, parameter).concat(String.valueOf(numPages)), Constants.PAGINATION_PF_STYLE, true);
                pageFormList.add(pForm);
            }
        }

        return pageFormList;
    }

    /**
	 * Calculate path.
	 *
	 * @param path  the path
	 * @param param the param
	 * @return the string
	 */
    private static String calculatePath(final String path, final String param) {
        if (path.charAt(path.length() - 1) == '?') {
            return path.concat(param + "=");
        } else {
            return path.concat("&" + param + "=");
        }
    }

    /**
	 * Removes the parameter P.
	 *
	 * @param parameterP the parameter P
	 * @param parameter  the parameter
	 * @return the string
	 */
    private static String removeParameterP(final String parameterP, final String parameter) {
        return parameterP.replaceAll("&*" + parameter + "=\\d+", "");
    }

    /**
	 * Gets the page.
	 *
	 * @param request the request
	 * @param param   the param
	 * @return the page
	 */
    public static int getPage(final HttpServletRequest request, final String param) {
        if (request.getParameter(param) != null) {
            try {
                return Integer.parseInt(request.getParameter(param));
            } catch (NumberFormatException nfe) {
                return 1;
            }
        } else {
            return 1;
        }
    }

}
