<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>

	<bean:define id="graphicParam" value="<%= Constants.GRAPHIC %>" />
	<bean:define id="initial" value="<%= Constants.OBSERVATORY_GRAPHIC_INITIAL %>" />
	<bean:define id="observatoryType" value="<%= Constants.TYPE_OBSERVATORY %>" />

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<bean:message key="migas.indice.observatorios.realizados.lista"/>
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="gestion.resultados.observatorio.ejecuciones" /></h2>
							<div class="pag">
								<logic:empty name="<%=Constants.FULFILLED_OBSERVATORIES %>">
									<bean:message key="indice.observatorios.realizados.lista.vacia"/>
								</logic:empty>
								<jsp:include page="/common/crawler_messages.jsp" />
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
										<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
										<c:set target="${params}" property="${graphicParam}" value="${initial}" />
										
										<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES %>" id="fulfilledObservatory">
											<c:set target="${params}" property="id" value="${fulfilledObservatory.id}" />
											<c:set target="${params}" property="${observatoryType}" value="${fulfilledObservatory.cartucho.id}" />
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
													<ul class="lista_linea">
														<jsp:useBean id="params2" class="java.util.HashMap" />
														<c:set target="${params2}" property="id_observatorio" value="${id_observatorio}" />
														<c:set target="${params2}" property="id" value="${fulfilledObservatory.id}" />
														<c:set target="${params2}" property="esPrimera" value="true"/>
														<c:set target="${params2}" property="isPrimary" value="false"/>
														<c:set target="${params2}" property="idExObs" value="${fulfilledObservatory.id}" />
														<logic:notEqual name="fulfilledObservatory" property="observatorio.estado" value="1">
															<%-- <logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeIntav%>"> --%>
																<li><html:link forward="<%= Constants.OBSERVATORY_GRAPHIC %>" name="params"><img src="../images/list.gif" alt="<bean:message key="indice.observatorio.resultados.alt"/>"/></html:link></li>
																<%-- <li><html:link forward="anonymousExportPdfAction" name="params" ><img src="../images/icono_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.pdf" />"/></html:link></li>
																<bean:define id="regeneratePDF"><%= Constants.EXPORT_PDF_REGENERATE %></bean:define>
																<c:set target="${params}" property="${regeneratePDF}" value="true" />
																<li><html:link forward="anonymousExportPdfAction" name="params" ><img src="../images/icono_regenerar_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.pdf.regenerate" />"/></html:link></li>--%>
																<%--<li><html:link forward="anonymousExportHtmlAction" name="params" ><img src="../images/export_html.png" alt="<bean:message key="indice.rastreo.exportar.html" />"/></html:link></li> --%>
																<li><html:link forward="anonymousExportOpenOfficeAction" name="params" ><img src="../images/openoffice.png" alt="<bean:message key="indice.rastreo.exportar.openOffice" />"/></html:link></li>
															<%-- </logic:equal> --%>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeLenox%>">
																<li><html:link forward="<%= Constants.OBSERVATORY_GRAPHIC %>" name="params"><img src="../images/transgender.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeMultilanguage%>">
																<li><html:link forward="<%= Constants.OBSERVATORY_GRAPHIC %>" name="params"><img src="../images/multilanguage.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeIntav%>">
																<li><html:link forward="uploadConclusion" name="params2"><img src="../images/ico_subir.png" alt="<bean:message key="indice.observatorio.conclusiones.alt" />"/></html:link></li>
															</logic:equal>
															<li><html:link forward="deleteFulfilledObservatory" name="params2"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.observatorio.eliminar.alt" />"/></html:link></li>
														</logic:notEqual>
														<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="1">
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeIntav%>">
																<li><img src="../images/list_disable.gif" alt="<bean:message key="indice.observatorio.resultados.alt"/>"/></li>
																<li><img src="../images/icono_pdf_disable.gif" alt="<bean:message key="indice.rastreo.exportar.pdf" />"/></li>
																<li><img src="../images/icono_regenerar_pdf_disable.gif" alt="<bean:message key="indice.rastreo.exportar.pdf.regenerate" />"/></li>
																<li><img src="../images/export_html_disable.png" alt="<bean:message key="indice.rastreo.exportar.html" />"/></li>
																<li><img src="../images/openoffice_disable.png" alt="<bean:message key="indice.rastreo.exportar.openOffice" />"/></li>
															</logic:equal>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeLenox%>">
																<li><html:link forward="<%= Constants.OBSERVATORY_GRAPHIC %>" name="params"><img src="../images/transgender_disable.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeMultilanguage%>">
																<li><html:link forward="<%= Constants.OBSERVATORY_GRAPHIC %>" name="params"><img src="../images/multilanguage_disable.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
															<logic:equal name="fulfilledObservatory" property="cartucho.id" value="<%=idCartridgeIntav%>">
																<li><html:link forward="uploadConclusion" name="params2"><img src="../images/ico_subir.png" alt="<bean:message key="indice.observatorio.conclusiones.alt" />"/></html:link></li>
															</logic:equal>
															<li><html:link forward="deleteFulfilledObservatory" name="params2"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.observatorio.eliminar.alt" />"/></html:link></li>
														</logic:equal>
													</ul>
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
		</div>	
	</div> 
</inteco:sesion>
