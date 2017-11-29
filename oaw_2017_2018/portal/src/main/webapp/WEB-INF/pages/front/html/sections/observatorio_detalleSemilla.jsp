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
	
	<div id="migas">
		<bean:define id="action"><%= Constants.ACTION %></bean:define>
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> /
			<html:link forward="observatorySeed"><bean:message key="migas.semillas.observatorio" /></html:link> / 
			<bean:message key="migas.ver.observatorio" />
		</p>
	</div>
	



			<div id="main">

					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="detalle.semilla.observatorio.title" /></h2>
								<div class="detail">
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="detalle.semilla.observatorio.nombre" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="detalle.semilla.observatorio.url" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="listaUrlsString" /></p>
									</div>
									<div id="pCenter">
										<logic:notPresent parameter="<%= Constants.ACCION_DE_OBSERVATORIO%>">
											<html:link forward="observatorySeed" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link>
										</logic:notPresent>
										<logic:present parameter="<%= Constants.ACCION_DE_OBSERVATORIO%>">
											<html:link forward="resultadosObservatorio" name="breadCrumbsParams"  styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link>
										</logic:present>
									</div>
								</div>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 

