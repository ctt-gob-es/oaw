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
package es.inteco.rastreador2.filter;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.utils.CrawlerUtils;

/**
 * The Class SecurityFilter.
 */
public class SecurityFilter implements Filter {
	/**
	 * Destroy.
	 */
	@Override
	public void destroy() {
	}

	/**
	 * Do filter.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param chain    the chain
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (CrawlerUtils.hasToBeFilteredUri((HttpServletRequest) request) && ((HttpServletRequest) request).getSession().getAttribute(Constants.ROLE) == null) {
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("mensaje.error.no.login"));
			((HttpServletRequest) request).getSession().setAttribute(Globals.ERROR_KEY, errors);
			PropertiesManager pmgr = new PropertiesManager();
			((HttpServletRequest) request).getSession().setAttribute(Constants.URL, ((HttpServletRequest) request).getRequestURI() + "?" + ((HttpServletRequest) request).getQueryString());
			((HttpServletResponse) response).sendRedirect(pmgr.getValue(CRAWLER_PROPERTIES, "application.root"));
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * Inits the.
	 *
	 * @param config the config
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
	}
}
