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
			<html:link forward="clientAccountsMenu"><bean:message key="migas.cuenta.cliente" /></html:link> / 
			<bean:message key="migas.ver.cuenta.usuario" />
		</p>
	</div>
	



			<div id="main">

					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="ver.usuario.title" /></h2>
							<html:form styleClass="formulario" method="post" action="/secure/VerCuentaUsuario.do">
								<fieldset>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.nombre" />: </strong></label>
										<p><bean:write name="VerCuentaUsuarioForm" property="nombre" /></p>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.activo" />: </strong></label>
										<logic:equal name="VerCuentaUsuarioForm" property="activo" value="true">
											<bean:message key="select.yes"/>
										</logic:equal>
										<logic:equal name="VerCuentaUsuarioForm" property="activo" value="false">
											<bean:message key="select.no"/>
										</logic:equal>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.dominio" />: </strong></label>
										<div class="alinForm">
											<ul class="sublista">
												<logic:iterate id="dom" name="VerCuentaUsuarioForm" property="dominio">
													<li><bean:write name="dom"/></li>
												</logic:iterate>
											</ul>
										</div>
									</div>
									
									<logic:notEmpty name="VerCuentaUsuarioForm" property="listaRastreable">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.listaRastreable" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="rlist" name="VerCuentaUsuarioForm" property="listaRastreable">
														<li><bean:write name="rlist"/></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="VerCuentaUsuarioForm" property="listaNoRastreable">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.listaNoRastreable" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="rnlist" name="VerCuentaUsuarioForm" property="listaNoRastreable">
														<li><bean:write name="rnlist"/></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="VerCuentaUsuarioForm" property="cartuchos">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.cartuchos" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="cart" name="VerCuentaUsuarioForm" property="cartuchos">
														<li><bean:write name="cart"/></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="VerCuentaUsuarioForm" property="normaAnalisisSt">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.norma" />: </strong></label>
											<p><bean:write name="VerCuentaUsuarioForm" property="normaAnalisisSt" /></p>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="VerCuentaUsuarioForm" property="responsable">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.responsable" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="resp" name="VerCuentaUsuarioForm" property="responsable">
														<li><bean:write name="resp"/></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="VerCuentaUsuarioForm" property="usuarios">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.usuarios" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="usu" name="VerCuentaUsuarioForm" property="usuarios">
														<li><bean:write name="usu"/></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.periodicidad" />: </strong></label>
										<p><bean:write name="VerCuentaUsuarioForm" property="periodicidad" /></p>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.profundidad" />: </strong></label>
										<p><bean:write name="VerCuentaUsuarioForm" property="profundidad" /></p>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.amplitud" />: </strong></label>
										<bean:define id="unlimitedTopN">
											<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />
										</bean:define>
										<logic:equal name="VerCuentaUsuarioForm" property="amplitud" value="<%=unlimitedTopN %>">
											<bean:message key="nuevo.rastreo.amplitud.ilimitada" />
										</logic:equal>
										<logic:notEqual name="VerCuentaUsuarioForm" property="amplitud" value="<%=unlimitedTopN %>">
											<p><bean:write name="VerCuentaUsuarioForm" property="amplitud" /></p>
										</logic:notEqual>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.pseudoaleatorio" />: </strong></label>
										<logic:equal name="VerCuentaUsuarioForm" property="pseudoAleatorio" value="true">
											<bean:message key="select.yes"/>
										</logic:equal>
										<logic:equal name="VerCuentaUsuarioForm" property="pseudoAleatorio" value="false">
											<bean:message key="select.no"/>
										</logic:equal>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.cuenta.cliente.in.directory" />: </strong></label>
										<logic:equal name="VerCuentaUsuarioForm" property="inDirectory" value="true">
											<bean:message key="select.yes"/>
										</logic:equal>
										<logic:equal name="VerCuentaUsuarioForm" property="inDirectory" value="false">
											<bean:message key="select.no"/>
										</logic:equal>
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

