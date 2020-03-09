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
<html:javascript formName="ModificarUsuarioObservatorioForm"/>

	<bean:define id="username" value="<%= Constants.USERNAME%>" />
	<bean:parameter id="user" name="<%= Constants.USER%>" />
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="usersMenu"><bean:message key="migas.usuarios" /></html:link> / 
			<bean:message key="migas.editar.usuario" />
		</p>
	</div>
	



			<div id="main">

					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="modificar.usuario.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form method="post" styleClass="formulario" action="/secure/ModificarUsuarioObservatorio.do" onsubmit="return validateModificarUsuarioObservatorioForm(this)">
								<input type="hidden" name="<%= Constants.NOMBRE_ANTIGUO %>" value="<bean:write name="ModificarUsuarioObservatorioForm" property="nombre_antiguo" />" />
								<input type="hidden" name="valEmail" id="valEmail" value="bien" />
								<input type="hidden" name="<%= Constants.USER %>" id="<%= Constants.USER %>" value="<bean:write name="user" />" />
								
								<jsp:useBean id="params" class="java.util.HashMap" />
								<c:set target="${params}" property="user" value="${ModificarUsuarioObservatorioForm.id_usuario}" />
								<c:set target="${params}" property="roleType" value="${ModificarUsuarioObservatorioForm.roleType}" />
								
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<bean:define id="nombreForm" value="" />
									<logic:notEmpty name="ModificarUsuarioObservatorioForm" property="nombre">
										<bean:define id="nombreForm"><bean:write name="ModificarUsuarioObservatorioForm" property="nombre" /></bean:define>
									</logic:notEmpty>
									
									<div class="formItem">
										<label for="nombre"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="modificar.usuario.usuario" /></strong></label>
										<html:text styleId="nombre" styleClass="texto" property="nombre" value="<%= nombreForm %>" maxlength="100"/>
									</div>
									
									<div class="formItem">
										<label for="usuario"><strong class="labelVisu"><bean:message key="modificar.usuario.password" /></strong> </label>
										<p class="alinForm">
											<html:link forward="editUserAPassword" name="params"><bean:message key="modificar.usuario.cambiar.password" /></html:link>
										</p>
									</div>
									
									<div class="formItem">
										<label for="observatorio"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="modificar.usuario.observatorio.observatorio" /></strong></label>
										<logic:notEqual value="null" name="ModificarUsuarioObservatorioForm" property="observatorioV">
											<bean:define id="cargarO" name="ModificarUsuarioObservatorioForm" property="observatorioV" />
										</logic:notEqual>
										
										<logic:notEmpty name="cargarO" property="listadoObservatorio">
											<html:select size="5" multiple="true" styleClass="textoSelect" styleId="observatorio" property="selectedObservatorio" >
												<logic:notEmpty name="ModificarUsuarioObservatorioForm" property="observatorio">
													<logic:iterate name="cargarO" property="listadoObservatorio" id="elemento">
														<bean:define id="idObservatorio"><bean:write name="elemento" property="id_observatorio"/></bean:define>
														<bean:define id="nombreO"><bean:write name="elemento" property="nombreObservatorio"/></bean:define>
														<bean:define id="encontrado" value="0" />
														<logic:iterate name="ModificarUsuarioObservatorioForm" property="observatorio" id="elemento2">
															<logic:equal value="<%= idObservatorio %>" name="elemento2">
																<option selected  value="<%=idObservatorio %>"><bean:write name="elemento" property="nombreObservatorio"/></option>
																<bean:define id="encontrado" value="1" />
															</logic:equal>
														</logic:iterate>
														<logic:equal value="0" name="encontrado">
															<option value="<%=idObservatorio %>"><bean:write name="elemento" property="nombreObservatorio"/></option>
														</logic:equal>
													</logic:iterate>
												</logic:notEmpty>
												<logic:empty name="ModificarUsuarioObservatorioForm" property="observatorio">
													<logic:iterate name="cargarO" type="es.inteco.rastreador2.utils.ListadoCuentasUsuario" property="listadoCuentasUsuario" id="elemento">
														<bean:define id="idObservatorio"><bean:write name="elemento" property="id_observatorio"/></bean:define>
														<option value="<%=idObservatorio %>"><bean:write name="elemento" property="nombreObservatorio"/></option>
													</logic:iterate>
												</logic:empty>
											</html:select>
										</logic:notEmpty>
									</div>
									
									<div class="formItem">
										<label for="nombre2"><strong class="labelVisu"><bean:message key="modificar.usuario.nombre" /></strong></label>
										<html:text styleId="nombre2" maxlength="100" styleClass="texto" property="nombre2" />
									</div>
									
									<div class="formItem">
										<label for="apellidos"><strong class="labelVisu"><bean:message key="modificar.usuario.apellidos" /></strong></label>
										<html:text styleId="apellidos" maxlength="150" styleClass="texto" property="apellidos" />
									</div>
									
									<div class="formItem">
										<label for="departamento"><strong class="labelVisu"><bean:message key="modificar.usuario.departamento" /></strong></label>
										<html:text styleId="departamento" maxlength="100" styleClass="texto" property="departamento" />
									</div>
									
									<div class="formItem">
										<label for="email"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="modificar.usuario.eMail" /></strong></label>
										<html:text styleId="email" maxlength="100" styleClass="texto" property="email"  />
									</div>
									
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.cancelar" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
