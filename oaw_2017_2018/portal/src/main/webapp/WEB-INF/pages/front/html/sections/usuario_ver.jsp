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
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="usersMenu"><bean:message key="migas.usuarios" /></html:link> / 
			<bean:message key="migas.ver.usuario" />
		</p>
	</div>
	



			<div id="main">

					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="ver.usuario.title" /></h2>
							<html:form styleClass="formulario" method="post" action="/secure/CargarUsuariosSistema.do">
								<fieldset>
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.usuario.usuario" />: </strong></label>
											<p><bean:write name="VerUsuarioSistemaForm" property="usuario" /></p>
										</div>
										
										<div class="formItem">
											<logic:notEmpty name="VerUsuarioSistemaForm" property="nombreCuenta">
												<label><strong class="labelVisu"><bean:message key="ver.usuario.cuenta.cliente" />: </strong></label>
												<div class="alinForm">
													<ul class="sublista">
														<logic:iterate id="cuenta" name="VerUsuarioSistemaForm" property="nombreCuenta">
															<li><bean:write name="cuenta"/></li>
														</logic:iterate>
													</ul>
												</div>
											</logic:notEmpty>
										</div>
										
										<logic:notEmpty name="VerUsuarioSistemaForm" property="cartucho">
											<div class="formItem">
												<label><strong class="labelVisu"><bean:message key="ver.usuario.cartucho" />: </strong></label>
												<div class="alinForm">
													<ul class="sublista">
														<logic:iterate id="cart" name="VerUsuarioSistemaForm" property="cartucho">
															<li><bean:write name="cart" property="name"/></li>
														</logic:iterate>
													</ul>
												</div>
											</div>
										</logic:notEmpty>
										
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.usuario.tipo" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="rol" name="VerUsuarioSistemaForm" property="tipos">
														<li><bean:write name="rol" property="name"/></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
										
										<logic:notEmpty name="VerUsuarioSistemaForm" property="departamento">
											<div class="formItem">
												<label><strong class="labelVisu"><bean:message key="ver.usuario.nombre" />: </strong></label>
												<p><bean:write name="VerUsuarioSistemaForm" property="nombre" /></p>
											</div>
										</logic:notEmpty>
										
										<logic:notEmpty name="VerUsuarioSistemaForm" property="departamento">
											<div class="formItem">
												<label><strong class="labelVisu"><bean:message key="ver.usuario.apellidos" />: </strong></label>
												<p><bean:write name="VerUsuarioSistemaForm" property="apellidos" /></p>
											</div>
										</logic:notEmpty>
										
										<logic:notEmpty name="VerUsuarioSistemaForm" property="departamento">
											<div class="formItem">
												<label><strong class="labelVisu"><bean:message key="ver.usuario.departamento" />: </strong></label>
												<p><bean:write name="VerUsuarioSistemaForm" property="departamento" /></p>
											</div>
										</logic:notEmpty>
										
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.usuario.eMail" />: </strong></label>
											<p><bean:write name="VerUsuarioSistemaForm" property="email" /></p>
										</div>
										
										<div class="formItem">
											<html:cancel><bean:message key="boton.volver" /></html:cancel>
										</div>

								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 

