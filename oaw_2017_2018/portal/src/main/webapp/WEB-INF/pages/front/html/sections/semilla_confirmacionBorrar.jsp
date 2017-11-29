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
<html:xhtml/>

	<bean:parameter name="<%=Constants.ID_SEMILLA %>" id="idSemilla"/>
	<bean:parameter name="<%=Constants.ID_CATEGORIA %>" id="idcat"/>
	<jsp:useBean id="breadCrumbsParams" class="java.util.HashMap" />
	<bean:define id="idCat"><%= Constants.ID_CATEGORIA %></bean:define>
	<c:set target="${breadCrumbsParams}" property="${idCat}" value="${idcat}"/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="getSeedCategories"><bean:message key="migas.categoria" /></html:link> /
			<html:link forward="editSeedCategory" name="breadCrumbsParams"><bean:message key="migas.modificar.categoria" /></html:link> /
			<bean:message key="migas.eliminar.semillas.observatorio"/>
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">

						<h2><bean:message key="categoria.semillas.titulo" /></h2>
						<div class="detail">
							<logic:notEmpty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
								<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.info" /></strong></p>
								<ul class="lista_inicial">
									<logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>" id="elemento">
										<li><bean:write name="elemento" property="nombre"/></li>
									</logic:iterate>
								</ul>
								<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.info2" /></strong></p>
							</logic:notEmpty>
							<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.pregunta" /></strong></p>
							<div class="formItem">
								<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.nombre" />: </strong></label>
								<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre" /></p>
							</div>
							<div class="formItem">
								<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.url" />: </strong></label>
								<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="listaUrlsString" /></p>
							</div>
							
							<div class="formButton">
								<jsp:useBean id="params" class="java.util.HashMap" />
								<c:set target="${params}" property="idcat" value="${idcat}" />
								<c:set target="${params}" property="idSemilla" value="${idSemilla}" />
								<html:link styleClass="boton" forward="deleteCategorySeed" name="params"><bean:message key="boton.aceptar"/></html:link>
								<html:link styleClass="boton" forward="editSeedCategory" paramId="<%= Constants.ID_CATEGORIA %>" paramName="<%=Constants.ID_CATEGORIA%>"><bean:message key="boton.cancelar"/></html:link>
							</div>
						</div>
					</div>

			</div>

