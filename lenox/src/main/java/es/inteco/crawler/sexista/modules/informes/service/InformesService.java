package es.inteco.crawler.sexista.modules.informes.service;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.commons.Constants.CommonsConstants;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;
import es.inteco.crawler.sexista.modules.commons.util.LenoxUtils;
import es.inteco.crawler.sexista.modules.commons.util.UtilProcessingText;
import es.inteco.crawler.sexista.modules.informes.constants.InformesConstants;
import es.inteco.crawler.sexista.modules.informes.dao.InformesDao;
import es.inteco.crawler.sexista.modules.informes.dto.DetalleDto;
import es.inteco.crawler.sexista.modules.informes.dto.RastreoExtDto;
import es.inteco.crawler.sexista.modules.informes.dto.RastreosSearchDto;
import es.inteco.crawler.sexista.modules.informes.dto.TerminoDetalleDto;
import es.inteco.crawler.sexista.modules.informes.web.form.InformesForm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase InformesService.
 * Servicio de Informes
 *
 * @author psanchez
 */
public class InformesService {

    /**
     * M&eacute;todo searchRastreosPaginado.
     * Búsqueda paginada de rastreos.
     *
     * @param dto dto de búsqueda
     * @return Dto de registros paginados
     * @throws BusinessException BusinessException
     */
    public RastreosSearchDto searchRastreosPaginado(RastreosSearchDto dto) throws BusinessException {

        if (null == dto) {
            throw new BusinessException(CommonsConstants.ERROR_RASTREOSEARCHDTO_NULO);
        }

        InformesDao dao = new InformesDao();

        return dao.searchRastreosPaginado(dto);
    }

    /**
     * M&eacute;todo obtenerDetalle.
     * Obtiene el campo detalle a partir de un dto de búsqueda
     *
     * @param dto dto de búsqueda
     * @return DetalleDto
     * @throws BusinessException BusinessException
     */
    public DetalleDto obtenerDetalle(DetalleDto dto) throws BusinessException {

        if (null == dto) {
            throw new BusinessException(CommonsConstants.ERROR_DETALLEDTO_NULO);
        }

        InformesDao dao = new InformesDao();

        Connection conn = null;
        try {
            conn = ConexionBBDD.conectar();

            dto.setRastreo(dao.searchRastreoDetalle(dto));

            List<Integer> ids = dao.searchIdTerminosDetalle(dto);

            List<TerminoDetalleDto> detalleTerminos = new ArrayList<TerminoDetalleDto>();

            for (Object o : ids) {
                Integer i = (Integer) o;

                TerminoDetalleDto tdto = new TerminoDetalleDto();

                tdto.setGravedad(dto.getGravedad());

                tdto.setIdTermino(i);
                tdto.setIdRastreo(dto.getRastreo().getIdRastreo());

                tdto = dao.searchTerminoDetalle(tdto);

                tdto.setContextos(dao.searchContextosTerminoDetalle(conn, tdto));

                tdto.setAlternativas(dao.searchAlternativasTerminoDetalle(conn, tdto));

                detalleTerminos.add(tdto);

            }

            dto.setDetalleTerminos(detalleTerminos);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            ConexionBBDD.desconectar(conn);
        }

        return dto;
    }

    public InformesForm getInforme(ArrayList<String> lstTerminos, List<TerminoPositionDto> lstGroupTerminos, InformesForm informeForm) throws BusinessException, SQLException {

        DetalleDto detalleDto = new DetalleDto();

        List<TerminoDetalleDto> detalleTerminos = new ArrayList<TerminoDetalleDto>();

        Connection conn = null;
        try {
            conn = ConexionBBDD.conectar();
            for (TerminoPositionDto terminoPositionDto : lstGroupTerminos) {
                List<ResultadoDto> listadoResult = new ArrayList<ResultadoDto>();

                ResultadoDto resultadoDto = new ResultadoDto();
                //Obtenemos el contexto del termino para cada posicion dada
                for (Integer posicion : terminoPositionDto.getPositions()) {
                    resultadoDto = new ResultadoDto();
                    //Añadimos el contexto
                    resultadoDto.setContexto((LenoxUtils.formatearContexto(conn, terminoPositionDto.getTermino(), UtilProcessingText.obtenerContexto(posicion, lstTerminos))));
                    if (terminoPositionDto.isFoundInSingular()) {
                        resultadoDto.setGravedad(String.valueOf(terminoPositionDto.getPrioridadSingular()));
                    } else {
                        resultadoDto.setGravedad(String.valueOf(terminoPositionDto.getPrioridadPlural()));
                    }

                    //Insertamos el resultado
                    listadoResult.add(resultadoDto);
                }

                try {
                    //detalleTerminos.add(getTerminoDetalleDto(conn, detalleTerminos, terminoPositionDto, listadoResult));
                    addDetalleTermino(conn, detalleTerminos, terminoPositionDto, listadoResult);
                } catch (Exception e) {
                    throw new BusinessException(e);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            ConexionBBDD.desconectar(conn);
        }
        detalleDto.setDetalleTerminos(detalleTerminos);

        detalleDto.setRastreo(getRastreoExtDto(detalleTerminos));

        informeForm.setDetalle(detalleDto);

        return informeForm;
    }

    private RastreoExtDto getRastreoExtDto(List<TerminoDetalleDto> detalleTerminos) {
        RastreoExtDto rastreoExtDto = new RastreoExtDto();
        rastreoExtDto.setNumTerminosLocalizados(detalleTerminos.size());

        for (TerminoDetalleDto detalleTermino : detalleTerminos) {
            rastreoExtDto.setNumTerminosOcurrentes(rastreoExtDto.getNumTerminosOcurrentes() + detalleTermino.getNumOcurrencias());

            for (ResultadoDto resultadoDto : detalleTermino.getContextos()) {
                if (resultadoDto.getGravedad().equals(InformesConstants.GRAVEDAD_BAJA_VALOR)) {
                    rastreoExtDto.setNumTerminosPrioridadBaja(rastreoExtDto.getNumTerminosPrioridadBaja() + 1);
                }

                if (resultadoDto.getGravedad().equals(InformesConstants.GRAVEDAD_MEDIA_ALTA_VALOR)) {
                    rastreoExtDto.setNumTerminosPrioridadMedia(rastreoExtDto.getNumTerminosPrioridadMedia() + 1);
                }

                if (resultadoDto.getGravedad().equals(InformesConstants.GRAVEDAD_ALTA_VALOR)) {
                    rastreoExtDto.setNumTerminosPrioridadAlta(rastreoExtDto.getNumTerminosPrioridadAlta() + 1);
                }
            }
        }

        return rastreoExtDto;
    }

    /**
     * Para agrupar los términos, miramos si el nuevo término a meter en el informe estaba previamente metido
     * en la otra forma (plural o singular), ya que si no aparecerán los mismos términos separados.
     *
     * @param conn
     * @param detalleTerminos
     * @param terminoPositionDto
     * @param listadoResult
     * @throws BusinessException
     */
    private void addDetalleTermino(Connection conn, List<TerminoDetalleDto> detalleTerminos,
                                   TerminoPositionDto terminoPositionDto, List<ResultadoDto> listadoResult) throws BusinessException {
        InformesDao dao = new InformesDao();

        TerminoDetalleDto tdto = new TerminoDetalleDto();
        tdto.setNombre(terminoPositionDto.getTermino());
        tdto.setIdTermino(terminoPositionDto.getIdTermino());

        boolean found = false;
        for (TerminoDetalleDto checkedTdto : detalleTerminos) {
            if (checkedTdto.getIdTermino().equals(terminoPositionDto.getIdTermino())) {
                tdto = checkedTdto;
                found = true;
            }
        }

        tdto.setNumOcurrencias(tdto.getNumOcurrencias() + terminoPositionDto.getPositions().size());
        tdto.getContextos().addAll(listadoResult);

        if (!found) {
            tdto.setAlternativas(dao.searchAlternativasTerminoDetalle(conn, tdto));
            detalleTerminos.add(tdto);
        }
    }

}
