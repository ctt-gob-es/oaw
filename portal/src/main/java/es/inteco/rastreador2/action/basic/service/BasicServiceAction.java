package es.inteco.rastreador2.action.basic.service;

import es.ctic.basicservice.BasicServiceManager;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Iterator;

public class BasicServiceAction extends Action {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final BasicServiceForm basicServiceFormRequest = BasicServiceUtils.getBasicServiceForm((BasicServiceForm) form, request);
        Logger.putLog("after getBasicServiceForm " + basicServiceFormRequest.toString(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
        final ActionErrors errors = validateBasicServiceForm(basicServiceFormRequest, mapping, request);

        if (errors.isEmpty()) {
            final String action = request.getParameter(Constants.ACTION);
            final BasicServiceManager basicServiceManager = new BasicServiceManager();
            if (Constants.EXECUTE.equalsIgnoreCase(action)) {
                Logger.putLog("EXECUTE --  " + basicServiceFormRequest.toString(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
                final BasicServiceForm basicServiceForm = DiagnosisDAO.getBasicServiceRequestById(DataBaseManager.getConnection(), basicServiceFormRequest.getId());
                basicServiceForm.setContent(basicServiceFormRequest.getContent());
                basicServiceForm.setAnalysisToDelete(basicServiceFormRequest.getAnalysisToDelete());
                // TODO: Quitar param request
                basicServiceManager.executeCrawling(basicServiceForm, CrawlerUtils.getResources(request));
            } else {
                Logger.putLog("ENQUEUE --  " + basicServiceFormRequest.toString(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
                final String serverResponse = basicServiceManager.enqueueCrawling(basicServiceFormRequest);
                CrawlerUtils.returnText(response, serverResponse, false);
            }
        } else {
            final String serverResponse = processValidationErrors(basicServiceFormRequest, errors);
            CrawlerUtils.returnText(response, serverResponse, false);
        }

        return null;
    }

    private ActionErrors validateBasicServiceForm(final BasicServiceForm basicServiceForm, final ActionMapping mapping, final HttpServletRequest request) {
        final ActionErrors errors = basicServiceForm.validate(mapping, request);
        errors.add(BasicServiceUtils.validateReport(basicServiceForm));
        errors.add(BasicServiceUtils.validateUrlOrContent(basicServiceForm));
        return errors;
    }

    private String processValidationErrors(final BasicServiceForm basicServiceForm, final ActionErrors errors) {
        final PropertiesManager pmgr = new PropertiesManager();
        final StringBuilder text = new StringBuilder(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.validation.errors"));

        for (Iterator<ActionMessage> iterator = errors.get(); iterator.hasNext(); ) {
            final ActionMessage message = iterator.next();
            Logger.putLog(message.getKey(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
            text.append("\n - ").append(MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, message.getKey()), message.getValues()));
        }

        basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_MISSING_PARAMS));

        return text.toString();
    }

}
