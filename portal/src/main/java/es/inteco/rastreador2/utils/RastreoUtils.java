package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.utils.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Connection;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class RastreoUtils {

    private RastreoUtils() {
    }

    public static void borrarArchivosAsociados(HttpServletRequest request, Connection c, String id_rastreo) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        FulfilledCrawlingForm fulfilledCrawlingForm = RastreoDAO.getFullfilledCrawlingExecution(c, Long.parseLong(id_rastreo));
        int cartucho = Integer.parseInt(fulfilledCrawlingForm.getIdCartridge());
        int id = Integer.parseInt(fulfilledCrawlingForm.getId());
        String pdfPath = "";

        if (cartucho == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id"))) {
            String chartPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.general.intav.chart.files") +
                    File.separator + id_rastreo + File.separator + id;
            pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.intav");
            FileUtils.deleteDir(new File(chartPath));
            File rastreoDir = new File(pmgr.getValue(CRAWLER_PROPERTIES, "path.general.intav.chart.files") + File.separator + request.getParameter(Constants.ID_RASTREO));
            if (rastreoDir.isDirectory() && (rastreoDir.list().length == 0)) {
                rastreoDir.delete();
            }
        } else if (cartucho == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
            pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.lenox");
        }

        FileUtils.deleteDir(new File(pdfPath + File.separator + id_rastreo + File.separator + id));
        File rastreoDir = new File(pdfPath + File.separator + id_rastreo);
        if (rastreoDir.isDirectory() && (rastreoDir.list().length == 0)) {
            rastreoDir.delete();
        }
    }

}
