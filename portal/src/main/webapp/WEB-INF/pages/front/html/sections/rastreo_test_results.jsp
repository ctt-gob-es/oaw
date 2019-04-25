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
<inteco:sesion action="ifConfigAdmin">
<html:xhtml/>
<html:javascript formName="TestRastreoForm"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			 / <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> 
			 / <html:link forward="testCrawling"><bean:message key="migas.test.rastreo" /></html:link>
			 / <bean:message key="migas.test.rastreo.resultados" />
		 </p>
	</div>



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="test.rastreo.results.title" /></h2>
							
							<p><bean:message key="test.rastreo.results.info"/></p>
							
							<p>
								<bean:size id="numResults" name="<%=Constants.RASTREO_TEST_RESULTS %>"/>
								<bean:message key="test.rastreo.results.number">
									<jsp:attribute name="arg0">
										<bean:write name="numResults"/>
									</jsp:attribute>
								</bean:message>
							</p>
							
							<ul class="lista_inicial">
								<logic:iterate name="<%=Constants.RASTREO_TEST_RESULTS %>" id="testResult">
									<li><bean:write name="testResult" property="url"/></li>
								</logic:iterate>
							</ul>
							
							<p id="pCenter"><html:link forward="testCrawling" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
						</div>
					</div>

			</div>
		</div>	
	</div> 

</inteco:sesion>
