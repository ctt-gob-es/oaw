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
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<bean:message key="migas.indice.observatorios.realizados.lista"/>
		</p>
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
									<table>
										<caption><bean:message key="indice.observatorios.realizados.lista"/></caption>
										<tr>
											<th><bean:message key="resultado.observatorio.rastreo.realizado.fecha.ejecucion" /></th>
											<th><bean:message key="resultado.observatorio.rastreo.realizado.cartucho.asociado" /></th>
											<th><bean:message key="resultado.observatorio.rastreo.realizado.estado"/></th>
											<th><bean:message key="resultado.observatorio.rastreo.realizado.acciones" /></th>
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
													<jsp:useBean id="params2" class="java.util.HashMap" />
													<c:set target="${params2}" property="id_observatorio" value="${id_observatorio}" />
													<c:set target="${params2}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
													<c:set target="${params2}" property="esPrimera" value="true"/>
													<c:set target="${params2}" property="isPrimary" value="true"/>
													
													<jsp:useBean id="paramsExportPDF" class="java.util.HashMap" />
													<c:set target="${paramsExportPDF}" property="id_observatorio" value="${id_observatorio}" />
													<c:set target="${paramsExportPDF}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
													<ul class="lista_linea">
														<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="0">

																<li><html:link forward="resultadosObservatorioSemillas" name="params"><img src="../images/list.gif" alt="<bean:message key="indice.observatorio.resultados.alt"/>"/></html:link></li>

															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeLenox%>">
																<li><html:link forward="resultadosObservatorioSemillas" name="params"><img src="../images/transgender.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeMultilanguage%>">
																<li><html:link forward="resultadosObservatorioSemillas" name="params"><img src="../images/multilanguage.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
														</logic:equal>
														<logic:notEqual name="fulfilledObservatory" property="observatorio.estado" value="0">
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeIntav%>">
																<li><img src="../images/list_disable.gif" alt="<bean:message key="indice.observatorio.resultados.alt"/>"/></li>
															</logic:equal>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeLenox%>">
																<li><img src="../images/transgender_disable.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></li>
															</logic:equal>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeMultilanguage%>">
																<li><img src="../images/multilanguage_disable.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></li>
															</logic:equal>
														</logic:notEqual>
														<%-- <logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeIntav%>"> --%>
															<li><html:link forward="getAnnexes" name="params2"><img src="../images/xml.jpg" alt="<bean:message key="indice.observatorio.anexos.alt" />"/></html:link></li>
															<li><html:link forward="<%= Constants.EXPORT_ALL_PDF_FORWARD %>" name="paramsExportPDF"><img src="../images/AllPDF.gif" alt="<bean:message key="indice.observatorio.generar.PDFs.alt" />"/></html:link></li>
														<%-- </logic:equal> --%>
														<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeMultilanguage%>">
															<li><html:link forward="getAnnexesMultilanguage" name="params2"><img src="../images/xml.jpg" alt="<bean:message key="indice.observatorio.eliminar.alt" />"/></html:link></li>
															<li><html:link forward="<%= Constants.EXPORT_ALL_PDF_FORWARD_MULTILANGUAGE %>" name="paramsExportPDF"><img src="../images/AllPDF.gif" alt="<bean:message key="indice.observatorio.generar.PDFs.alt" />"/></html:link></li>
														</logic:equal>
														<li><html:link forward="deleteFulfilledObservatory" name="params2"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.observatorio.eliminar.alt" />"/></html:link></li>														
													</ul>
												</td>
											</tr>
										</logic:iterate>
									</table>
									
									<jsp:include page="pagination.jsp" />
								</logic:notEmpty>
							</div>
							<p id="pCenter">
								<html:link styleClass="boton" forward="observatoryMenu"> <bean:message key="boton.volver"/> </html:link>
							</p>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
</inteco:sesion>