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
<html:xhtml/>

	<div id="migas">
		<bean:parameter name="<%= Constants.ID_RASTREO %>" id="idCrawler"/>
		<bean:define id="idRastreoSTR"><%= Constants.ID_RASTREO  %></bean:define>
		<jsp:useBean id="params" class="java.util.HashMap"/>
		<c:set target="${params}" property="${idRastreoSTR}" value="${idCrawler}" />
		
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> / 
			<html:link forward="verRastreosRealizados" name = "params"><bean:message key="migas.rastreos.realizados" /></html:link>
			 / <bean:message key="migas.eliminar.rastreo.ejecutado" />
		 </p>
	</div>
	



			<div id="main">

					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="confirmacion.eliminar.rastreo.title" /></h2>
							<div class="detail">
								<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.rastreo.pregunta" /></strong></p>
								<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.rastreo.info" /></strong></p>
								<html:form action="/secure/EliminarRastreoRealizado.do" method="post">
									<input type="hidden" name="<%= Constants.CONFIRMACION %>" value="<%= Constants.CONF_SI %>" />
									<input type="hidden" name="<%= Constants.ID_RASTREO %>" value="<%= idCrawler %>" />
									<table>
										<tr>
											<th><bean:message key="confirmacion.eliminar.rastreo.nombre" /></th>
											<th><bean:message key="confirmacion.eliminar.rastreo.fecha" /></th>
											<th><bean:message key="confirmacion.eliminar.marcado" /></th>
										</tr>
										<logic:iterate name="<%= Constants.RASTREO_LIST_FORM %>" id="rastreo" type="es.inteco.rastreador2.actionform.rastreo.RastreoEjecutadoForm">
											<tr>
												<td><bean:write name="rastreo" property="nombre_rastreo" /></td>
												<td><bean:write name="rastreo" property="fecha" /></td>
												<td><html:multibox property="select" value="<%= String.valueOf(rastreo.getId_ejecucion()) %>"/></td>
											</tr>
										</logic:iterate>
									</table>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar"/></html:submit>
										<html:cancel><bean:message key="boton.cancelar"/></html:cancel>
									</div>
								</html:form>
							</div>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
