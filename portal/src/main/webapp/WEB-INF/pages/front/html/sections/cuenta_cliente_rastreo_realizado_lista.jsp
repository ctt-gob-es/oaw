<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
This program is licensed and may be used, modified and redistributed under the terms
of the European Public License (EUPL), either version 1.2 or (at your option) any later 
version as soon as they are approved by the European Commission.
Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND, either express or implied. See the License for the specific language governing 
permissions and more details.
You should have received a copy of the EUPL1.2 license along with this program; if not, 
you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>

	<bean:parameter id="idrastreo" name="idrastreo"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="loadClientCrawlings"><bean:message key="migas.rastreos.cliente" /></html:link>
	 	/ <bean:message key="migas.rastreos.realizados" /></p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2><bean:message key="indice.rastreo.rastreo" /></h2>
							
							<logic:present name="<%=Constants.CRAWLINGS_FORM %>">
								<logic:empty name="<%=Constants.CRAWLINGS_FORM %>">
									<p><bean:message key="indice.rastreo.realizado.vacio"/></p>
								</logic:empty>
							
								<logic:notEmpty name="<%=Constants.CRAWLINGS_FORM %>">
									<div class="pag">
										<table>
											<caption><bean:message key="indice.rastreo.lista.rastreos.realizados" /></caption>
											<tr>
												<th><bean:message key="indice.rastreo.realizado.fecha" /></th>
												<th><bean:message key="indice.rastreo.acciones" /></th>
											</tr>
									
											<logic:iterate name="<%=Constants.CRAWLINGS_FORM %>" id="crawlingForm" type="es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm">
											
												<tr>
													<td><bean:write name="crawlingForm" property="date"/></td>
													<td>
														<ul class="lista_linea">
															<bean:define id="crawlerTypeParam"><%= Constants.EXPORT_PDF_TYPE %></bean:define>
															<bean:define id="regeneratePDF"><%= Constants.EXPORT_PDF_REGENERATE %></bean:define>
															<bean:define id="intavCrawler"><%= Constants.EXPORT_PDF_INTAV %></bean:define>
															<bean:define id="intavSimpleCrawler"><%= Constants.EXPORT_PDF_INTAV_SIMPLE %></bean:define>
														
															<jsp:useBean id="params" class="java.util.HashMap" />
															<c:set target="${params}" property="idrastreo" value="${crawlingForm.idCrawling}" />
															<c:set target="${params}" property="id" value="${crawlingForm.id}" />
															<c:set target="${params}" property="isCliente" value="true" />
														
															<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeIntav%>">
																<li>
																	<html:link forward="showTracking" name="params"><img src="../images/list.gif" alt="indice.rastreo.ver.informe.rastreo"/></html:link>
																</li>
																<li>
																	<c:set target="${params}" property="${crawlerTypeParam}" value="${intavCrawler}" />
																	<html:link forward="exportToPdf" name="params"><img src="../images/icono_pdf.gif" alt="indice.rastreo.exportar.pdf" /></html:link>
																</li>
																<li>
																	<c:set target="${params}" property="${crawlerTypeParam}" value="${intavSimpleCrawler}" />
																	<html:link forward="exportToPdf" name="params" ><img src="../images/icono_vis_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.simple.pdf" />"/></html:link>
																</li>
																<li>
																	<bean:define id="regeneratePDF"><%= Constants.EXPORT_PDF_REGENERATE %></bean:define>
																	<c:set target="${params}" property="${regeneratePDF}" value="true" />
																	<html:link forward="exportToPdf" name="params"><img src="../images/icono_regenerar_pdf.gif" alt="indice.rastreo.exportar.pdf.regenerate" /></html:link>
																</li>
																<li>
																	<html:link forward="exportToChart" name="params"><img src="../images/chart.gif" alt="indice.rastreo.ver.grafica" /></html:link>
																</li>
															</logic:equal>
															<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeLenox%>">
																<li>
																	<html:link forward="showLenoxResultsByUrl" name="params"><img src="../images/list.gif" alt="indice.rastreo.ver.informe.rastreo"/></html:link>
																</li>
															</logic:equal>
															<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeMalware%>">
																<li>
																	<html:link forward="showMalwareResultsByUrl" name="params"><img src="../images/list.gif" alt="indice.rastreo.ver.informe.rastreo"/></html:link>
																</li>
															</logic:equal>
															<%--
															<li>
																<html:link forward="getBrokenLinksList" name="params"><img src="../images/brokenlink.jpg" alt="indice.rastreo.ver.informe.rastreo"/></html:link>
															</li>
															--%>
														</ul>
													</td>
												</tr>
											</logic:iterate>
										</table>
										<jsp:include page="pagination.jsp" />
									</div>
								</logic:notEmpty>
							</logic:present>
							<p id="pCenter"><html:link forward="loadClientCrawlings" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
</inteco:sesion>
