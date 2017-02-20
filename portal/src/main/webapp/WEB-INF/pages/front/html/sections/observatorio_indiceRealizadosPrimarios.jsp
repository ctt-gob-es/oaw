<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>

	<div id="migas">
        <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
        <ol class="breadcrumb">
          <li><a href="#"><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link></a></li>
          <li><html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link></li>
          <li class="active"><bean:message key="migas.indice.observatorios.realizados.lista"/></li>
        </ol>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"> <bean:message key="gestion.resultados.observatorio"/> </h1>
	
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="gestion.resultados.observatorio.ejecuciones" /></h2>
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
											<th>Resultado</th>
											<th>Exportar</th>
											<th>Eliminar</th>
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
													<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="1">
														<bean:message key="resultado.observatorio.rastreo.realizado.estado.lanzado" />
													</logic:equal>
													<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="0">
														<bean:message key="resultado.observatorio.rastreo.realizado.estado.terminado" />
													</logic:equal>
													<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="2">
														<bean:message key="resultado.observatorio.rastreo.realizado.estado.error" />
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
                                                <!-- <td>
													<jsp:useBean id="params2" class="java.util.HashMap" />
													<c:set target="${params2}" property="id_observatorio" value="${id_observatorio}" />
													<c:set target="${params2}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
													<c:set target="${params2}" property="esPrimera" value="true"/>
													<c:set target="${params2}" property="isPrimary" value="true"/>
                                                    <html:link forward="getAnnexes" name="params2">
                                                        <span class="glyphicon glyphicon-save" aria-hidden="true" data-toggle="tooltip" title="Exportar los resultados de todas las iteraciones del observatorio en formato XML"/>
                                                        <span class="sr-only">Generar anexos de resultados</span>
                                                    </html:link>
                                                </td> -->
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
												    <html:link forward="deleteFulfilledObservatory" name="params2">
                                                	    <span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip" title="Eliminar esta iteración del observatorio"/><span class="sr-only">Eliminar esta iteración del observatorio</span>
                                                    </html:link>
												</td
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
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
</inteco:sesion>