<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.indice.observatorios.realizados.lista"/></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="gestion.resultados.observatorio.ejecuciones" /></h2>
                <jsp:include page="/common/crawler_messages.jsp" />
                <div class="pag">
                    <logic:empty name="<%=Constants.FULFILLED_OBSERVATORIES %>">
                        <bean:message key="indice.observatorios.realizados.lista.vacia"/>
                    </logic:empty>
                    <logic:notEmpty name="<%=Constants.FULFILLED_OBSERVATORIES %>">
                        <table class="table table-stripped table-bordered table-hover">
                            <caption><bean:message key="indice.observatorios.realizados.lista"/></caption>
                            <tr>
                                <th><bean:message key="resultado.observatorio.rastreo.realizado.fecha.ejecucion" /></th>
                                <th><bean:message key="resultado.observatorio.rastreo.realizado.cartucho.asociado" /></th>
                                <th><bean:message key="resultado.observatorio.rastreo.realizado.estado"/></th>
                                <th class="accion"><bean:message key="resultado.observatorio.rastreo.realizado.resultados"/></th>
                                <th class="accion"><bean:message key="resultado.observatorio.rastreo.realizado.informe.agregado"/></th>
                                <th class="accion"><bean:message key="resultado.observatorio.rastreo.realizado.informes.individuales"/></th>
                                <th class="accion"><bean:message key="resultado.observatorio.rastreo.realizado.graficas.agregadas"/></th>
                                <th class="accion"><bean:message key="resultado.observatorio.rastreo.realizado.anexos"/></th>
                                <th class="accion"><bean:message key="resultado.observatorio.rastreo.realizado.eliminar"/></th>
                            </tr>

                            <jsp:useBean id="params" class="java.util.HashMap" />
                            <bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
                            <bean:define id="id_ex_obs" value="<%= Constants.ID_EX_OBS %>" />
                            <c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />

                            <logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES %>" id="fulfilledObservatory">
                                <c:set target="${params}" property="idCartucho" value="${fulfilledObservatory.cartucho.id}" />
                                <c:set target="${params}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
                                <tr>
                                    <td><bean:write name="fulfilledObservatory" property="fechaStr"/></td>
                                    <td><bean:write name="fulfilledObservatory" property="cartucho.name"/></td>
                                    <td>
                                    
                                    
                                    <jsp:useBean id="paramsRelanzar" class="java.util.HashMap"/>
                        

                        						<c:set target="${paramsRelanzar}" property="action" value="confirm" />
                        						<c:set target="${paramsRelanzar}" property="id_observatorio" value="${id_observatorio}" />
                        						<c:set target="${paramsRelanzar}" property="idExObs" value="${fulfilledObservatory.id}" />
                                        <logic:equal name="fulfilledObservatory" property="observatorio.estado" value="1">
                                            <bean:message key="resultado.observatorio.rastreo.realizado.estado.lanzado" />
                                            
                       
                        
                        <html:link forward="relanzarObservatorio" name="paramsRelanzar">
                        	<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip" title="Relanzar esta iteraci&oacute;n del observatorio"/>
                        	<span class="sr-only">Relanzar esta iteraci&oacute;n del observatorio</span>
                        </html:link>
                                            
                                        </logic:equal>
                                        <logic:equal name="fulfilledObservatory" property="observatorio.estado" value="0">
                                            <bean:message key="resultado.observatorio.rastreo.realizado.estado.terminado" />
                                        </logic:equal>
                                        <logic:equal name="fulfilledObservatory" property="observatorio.estado" value="2">
                                            <bean:message key="resultado.observatorio.rastreo.realizado.estado.error" />
                                            
                                            
                        <html:link forward="relanzarObservatorio" name="paramsRelanzar">
                        	<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip" title="Relanzar esta iteraci&oacute;n del observatorio"/>
                        	<span class="sr-only">Relanzar esta iteraci&oacute;n del observatorio</span>
                        </html:link>
                       
                        
                                        </logic:equal>
                                    </td>
                                    <td>
                                        <logic:equal name="fulfilledObservatory" property="observatorio.estado" value="0">
                                            <html:link forward="resultadosObservatorioSemillas" name="params">
                                                <span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip" title="Ver resultados de este observatorio"/><span class="sr-only">Resultados</span>
                                            </html:link>
                                        </logic:equal>
                                        <logic:notEqual name="fulfilledObservatory" property="observatorio.estado" value="0">
                                            <img src="../images/list_disable.gif" alt="<bean:message key="indice.observatorio.resultados.alt"/>"/>
                                        </logic:notEqual>
                                    </td>
                                    <td>
                                        <jsp:useBean id="paramsInformeAgregado" class="java.util.HashMap" />
                                        <c:set target="${paramsInformeAgregado}" property="id_observatorio" value="${id_observatorio}" />
                                        <c:set target="${paramsInformeAgregado}" property="id" value="${fulfilledObservatory.id}" />
                                        <c:set target="${paramsInformeAgregado}" property="esPrimera" value="true"/>
                                        <c:set target="${paramsInformeAgregado}" property="isPrimary" value="false"/>
                                        <c:set target="${paramsInformeAgregado}" property="idExObs" value="${fulfilledObservatory.id}" />
                                        <logic:notEqual name="fulfilledObservatory" property="observatorio.estado" value="1">
                                            <!-- <html:link forward="<%= Constants.OBSERVATORY_GRAPHIC %>" name="params"><img src="../images/list.gif" alt="<bean:message key="indice.observatorio.resultados.alt"/>"/></html:link></li> -->

                                            <html:link forward="anonymousExportOpenOfficeAction" name="paramsInformeAgregado" >
                                                <span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip" title="Descargar informe de resultados agregados en formato odt (LibreOffice)"/>
                                                <span class="sr-only">Generar informe de resultados agregados</span>
                                            </html:link>
                                        </logic:notEqual>
                                    </td>
                                    <td>
                                        <jsp:useBean id="paramsExportPDF" class="java.util.HashMap" />
                                        <c:set target="${paramsExportPDF}" property="id_observatorio" value="${id_observatorio}" />
                                        <c:set target="${paramsExportPDF}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
                                        <html:link forward="<%= Constants.EXPORT_ALL_PDF_FORWARD %>" name="paramsExportPDF">
                                            <span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip" title="Exportar todos los informes indidividuales de todos los portales"/>
                                            <span class="sr-only">Generar los informes individuales</span>
                                        </html:link></li>
                                    </td>
                                    <td>
                                    	<jsp:useBean id="paramsGraphic" class="java.util.HashMap" />
                                    	<c:set target="${paramsGraphic}" property="id" value="${fulfilledObservatory.id}" />
                                    	<c:set target="${paramsGraphic}" property="id_observatorio" value="${id_observatorio}" />
                                    	<c:set target="${paramsGraphic}" property="graphic" value="initial" />
                                    	<c:set target="${paramsGraphic}" property="Otype" value="5" />

                                        <html:link forward="getObservatoryGraphic" name="paramsGraphic">
                                            <span class="glyphicon glyphicon-stats" aria-hidden="true" data-toggle="tooltip" title="Acceder a las gr&aacute;ficas agregadas de esta iteraci&oacute;n"/>
                                            <span class="sr-only"><bean:message key="migas.indice.observatorios.menu.graficas"/></span>
                                        </html:link>
                                    </td>
                                    <td>
                                        <html:link forward="databaseExportActionConfirm" name="paramsInformeAgregado">
                                            <span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip" title="Actualizar la exportaci&oacute;n de resultados de esta iteraci&oacute;n"/>
                                            <span class="sr-only"><bean:message key="indice.rastreo.exportar.database" /></span>
                                        </html:link>
                                    </td>
                                    <td>
                                     	<jsp:useBean id="paramsDeleteExecution" class="java.util.HashMap" />
                                    	<c:set target="${paramsDeleteExecution}" property="id_observatorio" value="${id_observatorio}" />
                                        <c:set target="${paramsDeleteExecution}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
                                        <c:set target="${paramsDeleteExecution}" property="esPrimera" value="true"/>
                                        <c:set target="${paramsDeleteExecution}" property="isPrimary" value="true"/>
                                        <c:set target="${paramsDeleteExecution}" property="idExObs" value="${fulfilledObservatory.id}" />
                                        <html:link forward="deleteFulfilledObservatory" name="paramsDeleteExecution">
                                            <span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip" title="Eliminar esta iteraci&oacute;n del observatorio"/><span class="sr-only">Eliminar esta iteraci&oacute;n del observatorio</span>
                                        </html:link>
                                    </td>
                                </tr>
                            </logic:iterate>
                        </table>

                        <jsp:include page="pagination.jsp" />
                    </logic:notEmpty>
                </div>
                <p id="pCenter">
                    <html:link styleClass="btn btn-default btn-lg" forward="observatoryMenu"> <bean:message key="boton.volver"/> </html:link>
                </p>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
</inteco:sesion>
