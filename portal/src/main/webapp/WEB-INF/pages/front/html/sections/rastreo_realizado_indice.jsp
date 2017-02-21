<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="EliminarRastreosRealizadosForm"/>
<inteco:sesion action="ifConfigAdmin">

	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>

	<bean:parameter id="idrastreo" name="idrastreo"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link>
	 		/ <bean:message key="migas.rastreos.realizados" />
	 	</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="indice.rastreo.rastreo" /></h2>
								
								<jsp:include page="/common/crawler_messages.jsp" />
								
								<html:form action="/secure/CargarRastreosRealizados.do" method="get" styleClass="formulario">
									<input type="hidden" name="<%= Constants.ID_RASTREO %>" value="<bean:write name="<%=Constants.ID_RASTREO %>" />"/>
									<fieldset>
										<div class="formItem">
											<label for="initial_date"><strong class="labelVisu"><bean:message key="rastreo.realizado.busqueda.fecha.inicio" /></strong></label>
											<html:text property="initial_date" styleClass="texto" styleId="initial_date" onkeyup="escBarra(event, document.forms['CargarRastreosRealizadosSearchForm'].elements['initial_date'], 1)" maxlength="10"/>
											<span id="initial_calendar">
												<img src="../images/boton-calendario.gif" onclick="popUpCalendar(this, document.forms['CargarRastreosRealizadosSearchForm'].elements['initial_date'], 'dd/mm/yyyy')" alt="<bean:message key="img.calendario.alt" />"/> 
											</span>
											<bean:message key="date.format"/>
										</div>
										<div class="formItem">
											<label for="final_date"><strong class="labelVisu"><bean:message key="rastreo.realizado.busqueda.fecha.fin" /></strong></label>
											<html:text property="final_date" styleClass="texto" styleId="final_date" onkeyup="escBarra(event, document.forms['CargarRastreosRealizadosSearchForm'].elements['final_date'], 1)" maxlength="10"/>
											<span id="final_calendar">
												<img src="../images/boton-calendario.gif" onclick="popUpCalendar(this, document.forms['CargarRastreosRealizadosSearchForm'].elements['final_date'], 'dd/mm/yyyy')" alt="<bean:message key="img.calendario.alt" />"/> 
											</span>
											<bean:message key="date.format"/>
										</div>
										<div class="formItem">
											<label for="cartridge"><strong class="labelVisu"><bean:message key="rastreo.realizado.busqueda.cartucho" /></strong></label>
											<html:select styleClass="textoSelect" styleId="cartridge" property="cartridge" >
												<html:option value=""> - <bean:message key="select.one.masculine"/> - </html:option>
												<logic:iterate id="cartridge" name="<%= Constants.LISTADO_CARTUCHOS %>" type="es.inteco.rastreador2.dao.login.CartuchoForm">
													<bean:define id="idCartridge"><bean:write name="cartridge" property="id"/></bean:define>
													<html:option value="<%=idCartridge %>"><bean:write name="cartridge" property="name"/></html:option>
												</logic:iterate>
											</html:select>
										</div>
										<div class="formItem">
											<label for="seed"><strong class="labelVisu"><bean:message key="rastreo.realizado.busqueda.semilla" /></strong></label>
											<html:select styleClass="textoSelect" styleId="seed" property="seed" >
												<html:option value=""> - <bean:message key="select.one.masculine"/> - </html:option>
												<logic:iterate id="seed" name="<%= Constants.LISTADO_SEMILLAS %>" type="es.inteco.rastreador2.actionform.semillas.SemillaForm">
													<bean:define id="idSeed"><bean:write name="seed" property="id"/></bean:define>
													<html:option value="<%=idSeed %>"><bean:write name="seed" property="nombre"/></html:option>
												</logic:iterate>
											</html:select>
										</div>
										<div class="formButton">
											<html:submit><bean:message key="boton.aceptar"/></html:submit>
										</div>
									</fieldset>
								</html:form>	
							
							<div class="detail">
								<logic:present name="<%=Constants.CRAWLINGS_FORM %>">
									<logic:empty name="<%=Constants.CRAWLINGS_FORM %>">
										<p><bean:message key="indice.rastreo.realizado.vacio"/></p>	
									</logic:empty>
									<logic:notEmpty name="<%=Constants.CRAWLINGS_FORM %>">
										<html:form action="/secure/EliminarRastreoRealizado.do" method="post" onsubmit="return validateEliminarRastreosRealizadosForm(this)">
											<input type="hidden" name="<%= Constants.ID_RASTREO %>" value="<%= idrastreo %>"/>
											<div class="pag">
												<table>
													<caption><bean:message key="indice.rastreo.lista.rastreos.realizados" /></caption>
													<tr>
														<th><bean:message key="indice.rastreo.realizado.usuario" /></th>
														<th><bean:message key="indice.rastreo.realizado.fecha" /></th>
														<th><bean:message key="indice.rastreo.realizado.cartucho.asociado" /></th>
														<th><bean:message key="indice.rastreo.realizado.semilla.asociada" /></th>
														<th><bean:message key="indice.rastreo.acciones" /></th>
														<th><bean:message key="indice.rastreo.eliminar" /></th>
													</tr>
											
													<logic:iterate name="<%=Constants.CRAWLINGS_FORM %>" id="crawlingForm" type="es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm">
														<tr>
															<td><bean:write name="crawlingForm" property="user"/></td>
															<td><bean:write name="crawlingForm" property="date"/></td>
															<td><bean:write name="crawlingForm" property="cartridge"/></td>
															<td><bean:write name="crawlingForm" property="seed.nombre"/></td>
															<td>
																<ul class="lista_linea">
																	<bean:define id="crawlerTypeParam"><%= Constants.EXPORT_PDF_TYPE %></bean:define>
																	<bean:define id="regeneratePDF"><%= Constants.EXPORT_PDF_REGENERATE %></bean:define>
																	<bean:define id="intavCrawler"><%= Constants.EXPORT_PDF_INTAV %></bean:define>
																	<bean:define id="intavSimpleCrawler"><%= Constants.EXPORT_PDF_INTAV_SIMPLE %></bean:define>
																	<bean:define id="lenoxCrawler"><%= Constants.EXPORT_PDF_LENOX %></bean:define>
																	<jsp:useBean id="params" class="java.util.HashMap" />
																	<c:set target="${params}" property="idrastreo" value="${crawlingForm.idCrawling}" />
																	<c:set target="${params}" property="id" value="${crawlingForm.id}" />
																	
																	<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeIntav%>">
																		<li>
																			<html:link forward="showTracking" name="params" ><img src="../images/list.gif" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link>
																		</li>
																		<li>
																			<c:set target="${params}" property="${crawlerTypeParam}" value="${intavCrawler}" />
																			<html:link forward="exportToPdf" name="params" ><img src="../images/icono_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.pdf" />"/></html:link>
																		</li>
																		<li>
																			<c:set target="${params}" property="${crawlerTypeParam}" value="${intavSimpleCrawler}" />
																			<html:link forward="exportToPdf" name="params" ><img src="../images/icono_vis_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.simple.pdf" />"/></html:link>
																		</li>
																		<li>
																			<c:set target="${params}" property="${regeneratePDF}" value="true" />
																			<c:set target="${params}" property="${crawlerTypeParam}" value="${intavCrawler}" />
																			<html:link forward="exportToPdf" name="params" ><img src="../images/icono_regenerar_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.pdf.regenerate" />"/></html:link>
																		</li>
																		<li>
																			<html:link forward="getCharts" name="params" ><img src="../images/chart.gif" alt="<bean:message key="indice.rastreo.ver.grafica" />"/></html:link>
																		</li>
																	</logic:equal>
																	<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeLenox%>">
																		<li>
																			<html:link forward="showLenoxResultsByUrl" name="params" ><img src="../images/transgender.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />" /></html:link>
																		</li>
																		<li>
																			<c:set target="${params}" property="${crawlerTypeParam}" value="${lenoxCrawler}" />
																			<html:link forward="exportToPdf" name="params" ><img src="../images/icono_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.pdf" />"/></html:link>
																		</li>
																		<li>
																			<c:set target="${params}" property="${regeneratePDF}" value="true" />
																			<c:set target="${params}" property="${crawlerTypeParam}" value="${lenoxCrawler}" />
																			<html:link forward="exportToPdf" name="params" ><img src="../images/icono_regenerar_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.pdf.regenerate" />"/></html:link>
																		</li>
																	</logic:equal>
																	<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeMalware%>">
																		<li>
																			<html:link forward="malwareResultsByModule" name="params" ><img src="../images/malwareMODULO.jpg" alt="<bean:message key="indice.rastreo.ver.estadisticas.modulo.alt" />" /></html:link>
																			<html:link forward="showMalwareResultsByUrl" name="params" ><img src="../images/malwareURL.jpg" alt="<bean:message key="indice.rastreo.ver.estadisticas.url.alt" />" /></html:link>
																			<html:link forward="personalStadistics" name="params" ><img src="../images/malwarePERSON.jpg" alt="<bean:message key="indice.rastreo.ver.estadisticas.person.alt" />" /></html:link>
																		</li>
																	</logic:equal>
																	<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeMultilanguage%>">
																		<li>
																			<html:link forward="multilanguageListAnalysis" name="params"><img src="../images/multilanguage.png" alt="<bean:message key="" />" /></html:link>
																		</li>
																	</logic:equal>
																	<%--<li>
																		<html:link forward="deleteExecutedCrawling" name="params" ><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.rastreo.eliminar.rastreo" />"/></html:link>
																	</li>
																	
																	<li>
																		<html:link forward="getBrokenLinksList" name="params" ><img src="../images/brokenlink.jpg" alt="<bean:message key="indice.rastreo.ver.enlaces.rotos" />" /></html:link>
																	</li>
																	--%>
																</ul>
															</td>
															<td><html:multibox property="select" value="<%= crawlingForm.getId() %>"/></td>
														</tr>
													</logic:iterate>
												</table>
												<jsp:include page="pagination.jsp" />
											</div>
											<div class="formButton">
												<html:submit><bean:message key="indice.rastreo.eliminar.selec" /></html:submit>
											</div>
										</html:form>
									</logic:notEmpty>
								</logic:present>
								<p id="pCenter"><html:link forward="crawlingsMenu" styleClass="boton"> <bean:message key="boton.volver"/> </html:link></p>
							</div>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
</inteco:sesion>