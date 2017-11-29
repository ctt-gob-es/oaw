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
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="clientAccountsMenu"><bean:message key="migas.cuentas.cliente" /></html:link> / 
			<bean:message key="migas.cuenta.cliente" />
		</p>
	</div>
	



			<div id="main">

				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="eliminar.cuenta.cliente.titulo" /></h2>
						
							<html:form styleClass="formulario" method="post" action="/secure/EliminarCuentaUsuario.do" >
								<input type="hidden" name="<%=Constants.CUENTA_ELIMINAR %>" value="<bean:write name="EliminarCuentaUsuarioForm" property="id" />" />
								<input type="hidden" name="<%=Constants.ID_CUENTA %>" value="<bean:write name="EliminarCuentaUsuarioForm" property="id" />" />
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<p><strong class="labelVisu"><bean:message key="eliminar.cuenta.cliente.confirmacion" /></strong></p>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.cuenta.cliente.nombre" />:</strong> </label>
										<p><bean:write name="EliminarCuentaUsuarioForm" property="nombre" /></p>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.cuenta.cliente.dominio" />:</strong> </label>
										<p><bean:write name="EliminarCuentaUsuarioForm" property="dominio" /></p>
									</div>
									
									<logic:notEmpty name="EliminarCuentaUsuarioForm" property="cartuchos">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="eliminar.cuenta.cliente.cartuchos" />:</strong> </label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="cratuchoL" name="EliminarCuentaUsuarioForm" property="cartuchos">
														<li><bean:write name="cratuchoL" /></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="EliminarCuentaUsuarioForm" property="normaAnalisisSt">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="eliminar.cuenta.cliente.norma" />: </strong></label>
											<p><bean:write name="EliminarCuentaUsuarioForm" property="normaAnalisisSt" /></p>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="EliminarCuentaUsuarioForm" property="responsable">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="eliminar.cuenta.cliente.responsables" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="resp" name="EliminarCuentaUsuarioForm" property="responsable">
														<li><bean:write name="resp"/></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="EliminarCuentaUsuarioForm" property="usuarios">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="eliminar.cuenta.cliente.usuarios" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="usu" name="EliminarCuentaUsuarioForm" property="usuarios">
														<li><bean:write name="usu"/></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
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
