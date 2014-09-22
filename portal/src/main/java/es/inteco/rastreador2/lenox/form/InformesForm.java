package es.inteco.rastreador2.lenox.form;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.sexista.core.util.ConfigUtil;
import es.inteco.rastreador2.lenox.constants.CommonsConstants;
import es.inteco.rastreador2.lenox.dto.BasePagingDto;
import es.inteco.rastreador2.lenox.dto.DetalleDto;
import es.inteco.rastreador2.lenox.dto.RastreosSearchDto;
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

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -7873747526756834079L;

    /**
     * Campo detalle.
     */
    private DetalleDto detalle;
    /**
     * Campo idRastreoSeleccionado.
     */
    private String idRastreoSeleccionado;
    /**
     * Campo searchDto.
     */
    private RastreosSearchDto searchDto = new RastreosSearchDto();

    /**
     * Campo searchDtoFinal.
     */
    private RastreosSearchDto searchDtoFinal = new RastreosSearchDto();


    /**
     * Campo lstGravedad.
     */
    private List<LabelValueBean> lstGravedad;


    /**
     * Campo analizar.
     */
    private Boolean analizar;


    /**
     * Constructor InformesForm.
     */
    public InformesForm() {
        super();
        searchDto = new RastreosSearchDto();
    }

    /**
     * Obtiene el valor del campo detalle.<br>
     *
     * @return el campo detalle
     */
    public DetalleDto getDetalle() {
        return detalle;
    }

    /**
     * Obtiene el valor del campo idRastreoSeleccionado.<br>
     *
     * @return el campo idRastreoSeleccionado
     */
    public String getIdRastreoSeleccionado() {
        return idRastreoSeleccionado;
    }

    /**
     * Obtiene el valor del campo searchDto.<br>
     *
     * @return el campo searchDto
     */
    public RastreosSearchDto getSearchDto() {
        return searchDto;
    }

    /**
     * Fija el valor para el campo detalle.<br>
     *
     * @param detalle el vlor de detalle a fijar
     */
    public void setDetalle(DetalleDto detalle) {
        this.detalle = detalle;
    }

    /**
     * Fija el valor para el campo idRastreoSeleccionado.<br>
     *
     * @param idRastreoSeleccionado el vlor de idRastreoSeleccionado a fijar
     */
    public void setIdRastreoSeleccionado(String idRastreoSeleccionado) {
        this.idRastreoSeleccionado = idRastreoSeleccionado;
    }

    /**
     * Fija el valor para el campo searchDto.<br>
     *
     * @param searchDto el vlor de searchDto a fijar
     */
    public void setSearchDto(RastreosSearchDto searchDto) {
        this.searchDto = searchDto;
    }

    /**
     * Obtiene el valor del campo lstGravedad.<br>
     *
     * @return el campo lstGravedad
     */
    public List<LabelValueBean> getLstGravedad() {
        return lstGravedad;
    }

    /**
     * Fija el valor para el campo lstGravedad.<br>
     *
     * @param lstGravedad el valor de lstGravedad a fijar
     */
    public void setLstGravedad(List<LabelValueBean> lstGravedad) {
        this.lstGravedad = lstGravedad;
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
                Logger.putLog("Exception al validar formulario", InformesForm.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }

        return errors;
    }

    /**
     * Obtiene el valor del campo searchDtoFinal.<br>
     *
     * @return el campo searchDtoFinal
     */
    public RastreosSearchDto getSearchDtoFinal() {
        return searchDtoFinal;
    }

    /**
     * Fija el valor para el campo searchDtoFinal.<br>
     *
     * @param searchDtoFinal el valor de searchDtoFinal a fijar
     */
    public void setSearchDtoFinal(RastreosSearchDto searchDtoFinal) {
        this.searchDtoFinal = searchDtoFinal;
    }

    /**
     * Método getPagingSearchDto.<br>
     * 1.- Devolver searchDtoFinal
     *
     * @return PagingDto
     */
    public BasePagingDto getPagingSearchDto() {
        return searchDtoFinal;
    }

    /**
     * Obtiene el valor del campo analizar.<br>
     *
     * @return el campo analizar
     */
    public Boolean getAnalizar() {
        return analizar;
    }

    /**
     * Fija el valor para el campo analizar.<br>
     *
     * @param analizar el valor de analizar a fijar
     */
    public void setAnalizar(Boolean analizar) {
        this.analizar = analizar;
    }

}
