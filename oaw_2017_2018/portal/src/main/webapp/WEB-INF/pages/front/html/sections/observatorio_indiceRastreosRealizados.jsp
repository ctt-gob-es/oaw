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

	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>
	<bean:parameter id="idrastreo" name="idrastreo"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<html:link forward="resultadosObservatorioSemillas" paramId="<%= Constants.OBSERVATORY_ID %>" paramName="<%= Constants.OBSERVATORY_ID %>"><bean:message key="migas.resultado.observatorio" /></html:link> /
		<bean:message key="migas.resultado.rastreos.realizados.observatorio" />
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
								<logic:present name="<%=Constants.CRAWLINGS_FORM %>">
									<logic:empty name="<%=Constants.CRAWLINGS_FORM %>">
										<div class="notaInformativaExito" id="nBoton10">
											<p><bean:message key="resultado.observatorio.rastreo.realizado.vacio"/></p>	
										</div>
									</logic:empty>
									<jsp:include page="/common/crawler_messages.jsp" />
									<logic:notEmpty name="<%=Constants.CRAWLINGS_FORM %>">
										<table>
											<caption><bean:message key="resultado.observatorio.lista.rastreos.realizados" /></caption>
											<tr>
												<th><bean:message key="resultado.observatorio.rastreo.realizado.fecha.ejecucion" /></th>
												<th><bean:message key="resultado.observatorio.rastreo.realizado.cartucho.asociado"/></th>
												<th><bean:message key="resultado.observatorio.rastreo.realizado.acciones" /></th>
											</tr>
											<logic:iterate name="<%=Constants.CRAWLINGS_FORM %>" id="crawlingForm" type="es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm">
												<tr>
													<td><bean:write name="crawlingForm" property="date"/></td>
													<td><bean:write name="crawlingForm" property="cartridge"/></td>
													<td>
														<ul class="lista_linea">
															<bean:define id="id_observatorio" name="<%= Constants.OBSERVATORY_ID %>" />
															<jsp:useBean id="params" class="java.util.HashMap" />
															<c:set target="${params}" property="idrastreo" value="${crawlingForm.idCrawling}" />
															<c:set target="${params}" property="id" value="${crawlingForm.id}" />
															<c:set target="${params}" property="observatorio" value="si" />
															<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
															<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeIntav%>">
																<li><html:link forward="showTracking" name="params"><img src="../images/list.gif" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
															<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeLenox%>">
																<li><html:link forward="showLenoxResultsByUrl" name="params"><img src="../images/transgender.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
															<logic:equal name="crawlingForm" property="idCartridge" value="<%=idCartridgeMultilanguage%>">
																<li><html:link forward="multilanguageListAnalysis" name="params"><img src="../images/multilanguage.png" alt="<bean:message key="indice.rastreo.ver.informe.rastreo" />"/></html:link></li>
															</logic:equal>
														</ul>
													</td>
												</tr>
											</logic:iterate>
										</table>
										<jsp:include page="pagination.jsp" />
									</logic:notEmpty>
								</logic:present>
							</div>
							<p id="pCenter">
								<html:link styleClass="boton" forward="resultadosObservatorioSemillas" paramId="<%= Constants.OBSERVATORY_ID %>" paramName="<%= Constants.OBSERVATORY_ID %>"> <bean:message key="boton.volver"/> </html:link>
							</p>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
</inteco:sesion>