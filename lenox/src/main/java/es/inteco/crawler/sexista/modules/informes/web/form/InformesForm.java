package es.inteco.crawler.sexista.modules.informes.web.form;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.sexista.core.dto.BasePagingDto;
import es.inteco.crawler.sexista.core.util.ConfigUtil;
import es.inteco.crawler.sexista.core.web.form.BasePagingSearchForm;
import es.inteco.crawler.sexista.modules.commons.Constants.CommonsConstants;
import es.inteco.crawler.sexista.modules.informes.dto.DetalleDto;
import es.inteco.crawler.sexista.modules.informes.dto.RastreosSearchDto;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Formulario de informes.
 *
 * @author psanchez
 */
public class InformesForm extends BasePagingSearchForm {


    private static final long serialVersionUID = -7873747526756834079L;
    private DetalleDto detalle;
    private String idRastreoSeleccionado;
    private RastreosSearchDto searchDto = new RastreosSearchDto();
    private RastreosSearchDto searchDtoFinal = new RastreosSearchDto();
    private List<LabelValueBean> lstGravedad;
    private Boolean analizar;
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InformesForm() {
        super();
        searchDto = new RastreosSearchDto();
    }

    public DetalleDto getDetalle() {
        return detalle;
    }

    public String getIdRastreoSeleccionado() {
        return idRastreoSeleccionado;
    }

    public RastreosSearchDto getSearchDto() {
        return searchDto;
    }

    public void setDetalle(DetalleDto detalle) {
        this.detalle = detalle;
    }

    public void setIdRastreoSeleccionado(String idRastreoSeleccionado) {
        this.idRastreoSeleccionado = idRastreoSeleccionado;
    }

    public void setSearchDto(RastreosSearchDto searchDto) {
        this.searchDto = searchDto;
    }

    public List<LabelValueBean> getLstGravedad() {
        return lstGravedad;
    }

    public void setLstGravedad(List<LabelValueBean> lstGravedad) {
        this.lstGravedad = lstGravedad;
    }

    public RastreosSearchDto getSearchDtoFinal() {
        return searchDtoFinal;
    }

    public void setSearchDtoFinal(RastreosSearchDto searchDtoFinal) {
        this.searchDtoFinal = searchDtoFinal;
    }

    public BasePagingDto getPagingSearchDto() {
        return searchDtoFinal;
    }

    public Boolean getAnalizar() {
        return analizar;
    }

    public void setAnalizar(Boolean analizar) {
        this.analizar = analizar;
    }

    /**
     * Validacion del formulario.
     *
     * @param mapping ActionMapping
     * @param request HttpServletRequest
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        String formato = ConfigUtil.getConfiguracion().getProperty(
                "config.formato.fecha");

        SimpleDateFormat sdf = new SimpleDateFormat(formato);

        String fechaDesde = searchDto.getFechaDesde();
        String fechaHasta = searchDto.getFechaHasta();

        if (null != fechaDesde && !CommonsConstants.CADENA_VACIA.equals(fechaDesde)) {
            try {
                sdf.parse(fechaDesde);
            } catch (ParseException e) {
                errors.add("fechaDesde", new ActionMessage("errors.fechadesde.invalid"));
            }
        }

        if (null != fechaHasta && !CommonsConstants.CADENA_VACIA.equals(fechaHasta)) {
            try {
                sdf.parse(fechaHasta);
            } catch (ParseException e) {
                errors.add("fechaHasta", new ActionMessage("errors.fechahasta.invalid"));
            }
        }

        // Duplicar
        BeanUtilsBean bub = new BeanUtilsBean();

        if (errors.isEmpty()) {
            try {
                this.setSearchDtoFinal((RastreosSearchDto) bub.cloneBean(this.getSearchDto()));
            } catch (Exception e) {
                Logger.putLog("Excepcion validando", InformesForm.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }

        return errors;
    }

}
