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
<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>

<bean:define id="rolAdmin">
	<inteco:properties key="role.administrator.id"
		file="crawler.properties" />
</bean:define>
<bean:define id="rolConfig">
	<inteco:properties key="role.configurator.id" file="crawler.properties" />
</bean:define>

<html:xhtml />

<!-- semilla_listado -->
<div id="main">

	<div id="container_menu_izq">
		<jsp:include page="menu.jsp" />
	</div>

	<div id="container_der">

		<div id="migas">
			<p class="sr-only">
				<bean:message key="ubicacion.usuario" />
			</p>
			<ol class="breadcrumb">
				<li class="active"><span class="glyphicon glyphicon-home"
					aria-hidden="true"></span>
				<bean:message key="lista.semillas" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="lista.semillas" />
			</h2>
			<html:form styleClass="formulario form-horizontal" method="get"
				action="/secure/ListadoSemillas.do">
				<input type="hidden" name="<%= Constants.INICIAL %>"
					id="<%= Constants.INICIAL %>" value="<%= Constants.CONF_SI %>" />
				<fieldset>
					<div class="formItem">
						<label for="nombre" class="control-label"><strong
							class="labelVisu"><bean:message
									key="borrar.semilla.busqueda.nombre" />: </strong></label>
						<html:text styleClass="texto" maxlength="100" property="nombre"
							styleId="nombre" />
					</div>
					<div class="formButton">
						<html:submit styleClass="btn btn-primary btn-lg">
							<bean:message key="boton.aceptar" />
						</html:submit>
					</div>
				</fieldset>
			</html:form>
			<div class="detail">
				<div class="formulario">
					<jsp:include page="/common/crawler_messages.jsp" />
					<logic:notPresent name="<%= Constants.SEED_LIST %>">
						<div class="notaInformativaExito">
							<p>
								<bean:message key="semilla.vacia" />
							</p>
						</div>
					</logic:notPresent>
					<logic:present name="<%= Constants.SEED_LIST %>">
						<logic:empty name="<%= Constants.SEED_LIST %>">
							<div class="notaInformativaExito">
								<p>
									<bean:message key="semilla.vacia" />
								</p>
							</div>
						</logic:empty>
						<logic:notEmpty name="<%= Constants.SEED_LIST %>">
							<jsp:useBean id="params" class="java.util.HashMap" />
							<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
								<p id="nTop10">
									<html:link forward="webSeedsMenuIni" styleClass="boton">
										<bean:message key="indice.semilla.nueva.semilla" />
									</html:link>
								</p>
							</inteco:menu>
							<div class="pag">
								<table>
									<caption>
										<bean:message key="lista.semillas" />
									</caption>
									<tr>
										<th><bean:message key="lista.semillas.semilla" /></th>
										<th><bean:message key="lista.semillas.operaciones" /></th>
									</tr>
									<logic:iterate name="<%= Constants.SEED_LIST %>" id="semilla">
										<c:set target="${params}" property="semilla"
											value="${semilla.id}" />
										<tr>
											<td><bean:write name="semilla" property="nombre" /></td>
											<td><logic:equal value="false" name="semilla"
													property="asociada">
													<html:link forward="deleteSeed" name="params">
														<img src="../images/bt_eliminar.gif"
															alt="<bean:message key="borrar.semillas.img.alt" />" />
													</html:link>
												</logic:equal> <logic:equal value="true" name="semilla"
													property="asociada">
													<html:link forward="deleteSeed" name="params">
														<img src="../images/bt_eliminar_escala_grises.gif"
															alt="<bean:message key="borrar.semillas.img.alt" />" />
													</html:link>
												</logic:equal> <html:link forward="editSeed" name="params">
													<img src="../images/bt_modificar.gif"
														alt="<bean:message key="editar.semillas.img.alt" />" />
												</html:link></td>
										</tr>
									</logic:iterate>
								</table>
								<jsp:include page="pagination.jsp" />
							</div>
						</logic:notEmpty>
					</logic:present>
					<div id="pCenter">
						<p>
							<html:link forward="indexAdmin"
								styleClass="btn btn-default btn-lg">
								<bean:message key="boton.volver" />
							</html:link>
						</p>
					</div>
				</div>
			</div>
			<!-- fin cajaformularios -->
		</div>
	</div>
</div>