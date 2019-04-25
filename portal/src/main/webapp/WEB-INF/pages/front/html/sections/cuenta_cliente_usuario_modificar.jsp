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
<html:javascript formName="ModificarUsuarioClienteForm"/>

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
							
							<html:form method="post" styleClass="formulario" action="/secure/ModificarUsuarioCliente.do" onsubmit="return validateModificarUsuarioClienteForm(this)">
								<input type="hidden" name="<%= Constants.NOMBRE_ANTIGUO %>" value="<bean:write name="ModificarUsuarioClienteForm" property="nombre_antiguo" />" />
								<input type="hidden" name="valEmail" id="valEmail" value="bien" />
								<input type="hidden" name="<%= Constants.USER %>" id="<%= Constants.USER %>" value="<bean:write name="user" />" />
								
								<jsp:useBean id="params" class="java.util.HashMap" />
								<c:set target="${params}" property="user" value="${ModificarUsuarioClienteForm.id_usuario}" />
								<c:set target="${params}" property="roleType" value="${ModificarUsuarioClienteForm.roleType}" />
								
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<bean:define id="nombreForm" value="" />
									<logic:notEmpty name="ModificarUsuarioClienteForm" property="nombre">
										<bean:define id="nombreForm"><bean:write name="ModificarUsuarioClienteForm" property="nombre" /></bean:define>
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
										<label for="cuentaCliente"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.cuenta.cliente" /></strong></label>
										<logic:notEqual value="null" name="ModificarUsuarioClienteForm" property="cuenta_clienteV">
											<bean:define id="cargarC" name="ModificarUsuarioClienteForm" property="cuenta_clienteV" type="es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm"/>
										</logic:notEqual>
										<%-- SOLO UNA CUENTA CLIENTE --%>
										<%--<logic:notEmpty name="cargarC" property="listadoCuentasUsuario">
											<html:select size="5" multiple="true" styleClass="textoSelect" styleId="cuentaCliente" property="cuentaCliente" >
												<logic:iterate name="cargarC" type="es.inteco.rastreador2.utils.ListadoCuentasUsuario" property="listadoCuentasUsuario" id="elemento">
													<bean:define id="idCuenta"><bean:write name="elemento" property="id_cuenta"/></bean:define>
													<bean:define id="nombreC"><bean:write name="elemento" property="nombreCuenta"/></bean:define>
													<logic:equal value="<%= nombreC %>" name="ModificarUsuarioClienteForm" property="cuentaCliente">
														<option selected  value="<%=idCuenta %>"><bean:write name="elemento" property="nombreCuenta"/></option>
													</logic:equal>
													<logic:notEqual value="<%= nombreC %>" name="ModificarUsuarioClienteForm" property="cuentaCliente">
														<option value="<%=idCuenta %>"><bean:write name="elemento" property="nombreCuenta"/></option>
													</logic:notEqual>
												</logic:iterate>
											</html:select>
										</logic:notEmpty> --%>
										<logic:notEmpty name="cargarC" property="listadoCuentasUsuario">
											<html:select size="5" multiple="true" styleClass="textoSelect" styleId="cuentaCliente" property="selectedCuentaCliente" >
												<logic:notEmpty name="ModificarUsuarioClienteForm" property="cuentaCliente">
													<logic:iterate name="cargarC" type="es.inteco.rastreador2.utils.ListadoCuentasUsuario" property="listadoCuentasUsuario" id="elemento">
														<bean:define id="idCuenta"><bean:write name="elemento" property="id_cuenta"/></bean:define>
														<bean:define id="nombreC"><bean:write name="elemento" property="nombreCuenta"/></bean:define>
														<bean:define id="encontrado" value="0" />
														<logic:iterate name="ModificarUsuarioClienteForm" property="cuentaCliente" id="elemento2">
															<logic:equal value="<%= idCuenta %>" name="elemento2">
																<option selected  value="<%=idCuenta %>"><bean:write name="elemento" property="nombreCuenta"/></option>
																<bean:define id="encontrado" value="1" />
															</logic:equal>
														</logic:iterate>
														<logic:equal value="0" name="encontrado">
															<option value="<%=idCuenta %>"><bean:write name="elemento" property="nombreCuenta"/></option>
														</logic:equal>
													</logic:iterate>
												</logic:notEmpty>
												<logic:empty name="ModificarUsuarioClienteForm" property="cuentaCliente">
													<logic:iterate name="cargarC" type="es.inteco.rastreador2.utils.ListadoCuentasUsuario" property="listadoCuentasUsuario" id="elemento">
														<bean:define id="idCuenta"><bean:write name="elemento" property="id_cuenta"/></bean:define>
														<option value="<%=idCuenta %>"><bean:write name="elemento" property="nombreCuenta"/></option>
													</logic:iterate>
												</logic:empty>
											</html:select>
										</logic:notEmpty>
									</div>
									
									<div class="formItem">
										<label for="selectedRoles"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.tipo.usuario" /></strong></label>
										<div class="alinForm">
											<ul>
												<logic:iterate name="ModificarUsuarioClienteForm" property="roles" id="rol" type="es.inteco.rastreador2.dao.login.Role">
													<bean:define id="identif">role<bean:write name="rol" property="id" /></bean:define>
													<li><label for="<%= identif %>" class="noFloat"><html:multibox styleClass="multiboxStyle" name="ModificarUsuarioClienteForm" property="selectedRoles" value="<%=rol.getId().toString() %>" styleId="<%=identif %>"/> <bean:write name="rol" property="name" /></label></li>
												</logic:iterate>
											</ul>
										</div>
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
