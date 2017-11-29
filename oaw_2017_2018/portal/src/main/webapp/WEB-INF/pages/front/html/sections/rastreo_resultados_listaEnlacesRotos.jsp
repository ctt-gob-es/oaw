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

	<bean:parameter id="idrastreo" name="idrastreo"/>
	<bean:parameter id="id" name="id"/>
	
	<jsp:useBean id="paramsCC" class="java.util.HashMap" />
	<c:set target="${paramsCC}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${paramsCC}" property="isCliente" value="true" />

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> /
		<logic:present parameter="isCliente"> 
			<html:link forward="loadClientCrawlings"><bean:message key="migas.rastreos.cliente" /></html:link>
	 		/ <html:link forward="loadClientFulfilledCrawlings" name="paramsCC"><bean:message key="migas.rastreos.realizados" /></html:link>
	 	</logic:present>
		<logic:notPresent parameter="isCliente"> 
			<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link>
		 	/ <html:link forward="verRastreosRealizados" paramId="<%= Constants.ID_RASTREO %>" paramName="idrastreo"><bean:message key="migas.rastreos.realizados" /></html:link>
	 	</logic:notPresent>
	 	/ <bean:message key="migas.rastreos.realizados.enlaces.rotos" /></p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<logic:empty name="<%=Constants.BROKEN_LINKS %>">
								<div class="notaInformativaExito">
									<p><bean:message key="broken.links.empty.results"/></p>
								</div>
							</logic:empty>
							<logic:notEmpty name="<%=Constants.BROKEN_LINKS %>">
								<div class="pag">
									<table>
										<thead>
											<tr>
												<th><bean:message key="search.results.domain"/></th>
												<th><bean:message key="search.results.num.broken.links"/></th>											
											</tr>
										</thead>
										<tbody>
											<logic:iterate name="<%=Constants.BROKEN_LINKS %>" id="brokenLink">
												<tr>
													<td><bean:write name="brokenLink" property="url"/></td>
													<td><bean:write name="brokenLink" property="numBrokenLinks"/></td>
												</tr>
											</logic:iterate>
										</tbody>
									</table>
									<jsp:include page="pagination.jsp" />
								</div>
							</logic:notEmpty>
							<div id="pCenter">
								<p><html:link forward="loadFulfilledCrawlings" paramId="<%= Constants.ID_RASTREO %>" paramName="idrastreo" styleClass="boton"></html:link>
							</div>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
