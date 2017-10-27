package es.inteco.rastreador2.action.observatorio;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.rastreador2.utils.CrawlerUtils.getResources;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.utils.RelanzarObservatorioThread;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.RastreoUtils;

public class RelanzarObservatorioAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		try {
			if (CrawlerUtils.hasAccess(request, "delete.observatory")) {
				if ("confirm".equals(request.getParameter(Constants.ACTION))) {
					return confirm(mapping, request);
				} else if ("relanzar".equals(request.getParameter(Constants.ACTION))) {
					if (request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_SI)) {
						return lanzarRastreosPendientes(mapping, request);
					} else {
						return mapping.findForward(Constants.VOLVER);
					}
				} else {
					return mapping.findForward(Constants.NO_PERMISSION);
				}

			} else {
				return mapping.findForward(Constants.NO_PERMISSION);
			}
		} catch (Exception e) {
			CrawlerUtils.warnAdministrators(e, this.getClass());
			return mapping.findForward(Constants.ERROR_PAGE);
		}

	}

	private ActionForward confirm(ActionMapping mapping, HttpServletRequest request) throws Exception {
		final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
		try (Connection c = DataBaseManager.getConnection()) {
			ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
			request.setAttribute(Constants.ID_EX_OBS, request.getParameter(Constants.ID_EX_OBS));
		}

		return mapping.findForward(Constants.CONFIRMACION_RELANZAR);
	}

	private ActionForward lanzarRastreosPendientes(ActionMapping mapping, HttpServletRequest request) throws Exception {

		ActionForward forward = null;

		String idObservatorio = request.getParameter(Constants.ID_OBSERVATORIO);
		String idEjecucionObservatorio = request.getParameter(Constants.ID_EX_OBS);
		// Lanzar en un hilo nuevo

		new RelanzarObservatorioThread(idObservatorio, idEjecucionObservatorio).start();

		final PropertiesManager pmgr = new PropertiesManager();
		request.setAttribute("mensajeExito", getResources(request).getMessage("mensaje.exito.relanzar.observatorio"));
		request.setAttribute("accionVolver", pmgr.getValue("returnPaths.properties", "volver.carga.observatorio"));

		return mapping.findForward(Constants.EXITO);
	}

}
