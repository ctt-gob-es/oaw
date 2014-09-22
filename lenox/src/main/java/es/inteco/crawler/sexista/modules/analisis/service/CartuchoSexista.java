package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.AnalizerUtils;
import es.inteco.crawler.sexista.core.util.ConfigUtil;
import es.inteco.crawler.sexista.modules.analisis.dto.AnalisisDto;
import es.inteco.crawler.sexista.modules.commons.Constants.AnalyzeConstants;
import es.inteco.plugin.Cartucho;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Clase CartuchoSexista.
 *
 * @author psanchez
 */
public class CartuchoSexista extends Cartucho {

    /**
     * M&eacute;todo analizar.
     *
     * @param dto Dto de análisis
     * @throws BusinessException BusinessException
     */
    public void analizar(AnalisisDto dto) throws BusinessException {

        AnalyzeService service = new AnalyzeService();

        // Analizamos y guardamos
        service.analyze(dto, true);
    }

    /**
     * M&eacute;todo analizar.
     *
     * @param datos Hashtable de datos a analizar
     */
    @Override
    public void analyzer(Map<String, Object> datos) {
        AnalisisDto dto = new AnalisisDto();
        dto.setIdRastreo((Long) datos.get(AnalyzeConstants.CLAVE_CRAWLER_ID_RASTREO_REALIZADO));

        SimpleDateFormat sdf = new SimpleDateFormat(ConfigUtil
                .getConfiguracion("lenox.properties").getProperty(
                        "config.formato.fecha.crawler"));
        Date date = null;

        if (datos.containsKey(AnalyzeConstants.CLAVE_CRAWLER_FECHA_HORA)) {

            try {
                date = sdf.parse((String) datos.get(AnalyzeConstants.CLAVE_CRAWLER_FECHA_HORA));
            } catch (ParseException e) {
                Logger.getLogger(this.getClass().getName()).error(e.getMessage());
                date = new Date();
            }
        } else {
            Logger.getLogger(this.getClass().getName()).error("No existe fecha en datos Crawler");
            date = new Date();
        }

        SimpleDateFormat sdfa = new SimpleDateFormat(ConfigUtil
                .getConfiguracion("lenox.properties").getProperty(
                        "config.formato.fecha.insercion"));

        dto.setFecha(sdfa.format(date));
        dto.setUrl((String) datos.get(AnalyzeConstants.CLAVE_CRAWLER_URL));
        dto.setContenido(AnalizerUtils.getContentFromHtml((String) datos.get(AnalyzeConstants.CLAVE_CRAWLER_CONTENIDO)));
        dto.setUsuario((String) datos.get(AnalyzeConstants.CLAVE_CRAWLER_USUARIO));

        try {
            Logger.getLogger(this.getClass().getName()).info("Iniciando el análisis de " + dto.getUrl());
            this.analizar(dto);
        } catch (BusinessException e) {
            Logger.getLogger(this.getClass().getName()).error(e.getMessage());
        }

    }

    /**
     * M&eacute;todo setConfig.
     *
     * @param idRastreo id
     */
    public void setConfig(long idRastreo) {
    }

}
