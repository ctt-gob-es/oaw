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
-->
<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:parameter id="idrastreo" name="idrastreo"/>
	<bean:parameter id="id" name="id"/>
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${params}" property="id" value="${id}" />

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link>  / 
			<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> /
		 	<html:link forward="loadFulfilledCrawlings" paramName="<%=Constants.ID_RASTREO %>" paramId="<%=Constants.ID_RASTREO %>"><bean:message key="back.to.fulfilled.crawlers.list"/></html:link> /
		 	<bean:message key="migas.graficas.rastreo" />
	 	</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2 class = "config"><bean:message key="indice.rastreo.gestion.graficas" /></h2>
							
							<jsp:include page="/common/crawler_messages.jsp" />
							<logic:equal name="<%= Constants.OBSERVATORY_RESULTS %>" value="<%= Constants.SI %>">
								<h3><bean:message key="chart.intav.priority.warnings" /></h3>
								<div class="graphicInfo2">
									<ol>
										<li><strong><bean:message key="first.level.incidents"/></strong>
										<div class="graphicInfo">
											<ol>
												<logic:iterate id="item" name="<%= Constants.CRAWLER_GRAPHIC_GLOBAL_RESULTS_LIST_N1 %>">
													<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
												</logic:iterate>
											</ol>
										</div></li>
										
										<li><strong><bean:message key="second.level.incidents"/></strong>
										<div class="graphicInfo">
											<ol>
												<logic:iterate id="item" name="<%= Constants.CRAWLER_GRAPHIC_GLOBAL_RESULTS_LIST_N2 %>">
													<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
												</logic:iterate>
											</ol>
										</div></li>
									</ol>
								</div>
								<img src="<%=request.getContextPath()%>/secure/GraficasRastreo.do?<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_RASTREO %>=<%= idrastreo %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.CRAWLER_GRAPHIC_GLOBAL_RESULTS %>"></img>
								
								<h3><bean:message key="chart.intav.total.results" /></h3>
								<div class="graphicInfo2">
									<ol>
										<logic:iterate id="item" name="<%= Constants.CRAWLER_GRAPHIC_TOTAL_RESULTS_LIST %>">
											<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
										</logic:iterate>
									</ol>
								</div>
								<img src="<%=request.getContextPath()%>/secure/GraficasRastreo.do?<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_RASTREO %>=<%= idrastreo %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.CRAWLER_GRAPHIC_TOTAL_RESULTS %>"></img>
							</logic:equal>
							
							<p id="pCenter">
								<html:link forward="loadFulfilledCrawlings" paramName="<%=Constants.ID_RASTREO %>" paramId="<%=Constants.ID_RASTREO %>" styleClass="boton"> <bean:message key="boton.volver"/> </html:link>
								<c:set target="${params}" property="${grParam}" value="${grRegenerate}" />
								<html:link forward="getChartsRegenerate" name="params" styleClass="boton"> <bean:message key="boton.regenerar.resultados"/> </html:link>
							</p>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
</inteco:sesion>
