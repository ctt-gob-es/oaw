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
<html:javascript formName="ModificarCuentaUsuarioForm"/>

<script type="text/javascript">

	function checkCartridge(obj, obj2, cartridge){
		var idIntav = <inteco:properties key="cartridge.intav.id" file="crawler.properties" />;
		var idLenox = <inteco:properties key="cartridge.lenox.id" file="crawler.properties" />;
		var idMalware = <inteco:properties key="cartridge.malware.id" file="crawler.properties" />;
		var cartridgeId = "cartucho" + cartridge;
		if (cartridge == idIntav){
			if (document.getElementById(cartridgeId).checked){
				enableField(obj);
				enableField(obj2);
			}else{
				disableField(obj);
				disableField(obj2);
			}
		}
	}
	
</script>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="clientAccountsMenu"><bean:message key="migas.cuenta.cliente" /></html:link> 
			/ <bean:message key="migas.modificar.cuenta.cliente" />
		</p>
	</div>
	



			<div id="main">

					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="modificar.cuenta.cliente.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form method="post" styleClass="formulario" action="/secure/ModificarCuentaUsuario.do" onsubmit="return validateModificarCuentaUsuarioForm(this)">
								<input type="hidden" name="<%= Constants.NOMBRE_ANTIGUO %>"  value="<bean:write name="ModificarCuentaUsuarioForm" property="nombre_antiguo" />" />
								<input type="hidden" name="esPrimera" id="esPrimera" value="no" />
								<logic:present name="<%= Constants.ID_CUENTA %>">
									<input type="hidden" name="<%= Constants.ID_CUENTA %>" value="<bean:write name="<%= Constants.ID_CUENTA %>" />" />
								</logic:present>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<div class="formItem">
										<label for="nombre"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.nombre" />: </strong></label>
										<html:text styleClass="texto" name="ModificarCuentaUsuarioForm" property="nombre" styleId="nombre" maxlength="100"/>
									</div>
									
									<div class="formItem">
										<label for="activo"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.activo" /></strong></label>
										<html:select styleClass="textoSelect" styleId="activo"  property="activo" >
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									
									<p><bean:message key="modificar.cuenta.cliente.dominio.info" /></p>
									
									<div class="formItem">
										<label for="dominio"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.dominio" />: </strong></label>
										<html:text styleClass="texto" name="ModificarCuentaUsuarioForm" property="dominio" styleId="dominio" maxlength="100"/>
									</div>
									
									<div class="formItem">
										<label for="listaRastreable"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.lista.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" name="ModificarCuentaUsuarioForm" property="listaRastreable" styleId="listaRastreable" />
									</div>
									
									<div class="formItem">
										<label for="listaNoRastreable"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.lista.no.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" name="ModificarCuentaUsuarioForm" property="listaNoRastreable" styleId="listaNoRastreable" />
									</div>
									
									<div class="formItem">
										<label for="lenguaje"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.lenguaje" /></strong></label>
										<html:select styleClass="textoSelect" styleId="lenguaje" property="lenguaje" >
											<logic:iterate name="ModificarCuentaUsuarioForm" property="lenguajeVector" id="lenguaje">
												<bean:define id="idLang"><bean:write name="lenguaje" property="id"/></bean:define>
												<html:option value="<%=idLang %>"><bean:write name="lenguaje" property="name"/></html:option>
											</logic:iterate>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="cartuchosSelected"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.cartuchos" /></strong>: </label>
										<div class="alinForm">
											<logic:notEmpty name="ModificarCuentaUsuarioForm" property="cartuchosSelected">
												<ul>
													<bean:define id="cartuchoList" name="ModificarCuentaUsuarioForm" property="cartuchosSelected" />
													<logic:iterate name="ModificarCuentaUsuarioForm" property="cartuchosList" id="cartucho" type="es.inteco.rastreador2.dao.login.CartuchoForm">
														<bean:define id="styleIdCartucho">cartucho<bean:write name="cartucho" property="id" /></bean:define>
														<li><label for="<%= styleIdCartucho%>" class="noFloat"><html:multibox onchange="checkCartridge('normaDiv', 'enlacesRotos', this.value)" styleClass="multiboxStyle" name="ModificarCuentaUsuarioForm" property="cartuchosSelected" value="<%=cartucho.getId().toString() %>" styleId="<%= styleIdCartucho%>"/> <bean:write name="cartucho" property="name" /></label></li>
													</logic:iterate>
												</ul>
											</logic:notEmpty>
										</div>
									</div>
									
									<div class="formItem" id="normaDiv">
										<label for="norma"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.norma" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="norma" property="normaAnalisis" >
											<logic:iterate name="ModificarCuentaUsuarioForm" property="normaV" id="norma">
												<c:if test="${ModificarCuentaUsuarioForm.normaAnalisis == norma.id}">
													<option selected value="<bean:write name="norma" property="id"/>"> <bean:write name="norma" property="name" /></option>
												</c:if>
												<c:if test="${ModificarCuentaUsuarioForm.normaAnalisis != norma.id}">
													<option value="<bean:write name="norma" property="id"/>"> <bean:write name="norma" property="name" /></option>
												</c:if>
											</logic:iterate>
										</html:select>
										<noscript>
											<p><strong class="labelVisu"><bean:message key="norma.para.intav" /></strong></p>
										</noscript>
									</div>
									
									<div class="formItem" id="enlacesRotos">
										<label for="enlaces"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.enlaces.rotos" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="enlaces" property="normaAnalisisEnlaces" >
											<c:if test="${ModificarCuentaUsuarioForm.normaAnalisisEnlaces == 0}">
												<option selected value="0"><bean:message key="enlaces.rotos.inactivo"/></option>
											</c:if>
											<c:if test="${ModificarCuentaUsuarioForm.normaAnalisisEnlaces != 0}">
												<option value="0"><bean:message key="enlaces.rotos.inactivo"/></option>
											</c:if>
											<c:if test="${ModificarCuentaUsuarioForm.normaAnalisisEnlaces == 1}">
												<option selected value="1"><bean:message key="enlaces.rotos.activo"/></option>
											</c:if>
											<c:if test="${ModificarCuentaUsuarioForm.normaAnalisisEnlaces != 1}">
												<option value="1"><bean:message key="enlaces.rotos.activo"/></option>
											</c:if>
										</html:select>
										<noscript>
											<p><strong class="labelVisu"><bean:message key="norma.para.intav" /></strong></p>
										</noscript>
									</div>
									
									<div class="formItem">
										<label for="periodicidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.periodicidad" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="periodicidad"  property="periodicidad" >
											<logic:iterate name="ModificarCuentaUsuarioForm" property="periodicidadVector" id="periodicidads">
												<c:if test="${ModificarCuentaUsuarioForm.periodicidad==periodicidads.id}">
													<option selected value="<bean:write name="periodicidads" property="id"/>"> <bean:write name="periodicidads" property="nombre"/></option>
												</c:if>
												<c:if test="${ModificarCuentaUsuarioForm.periodicidad!=periodicidads.id}">
													<option value="<bean:write name="periodicidads" property="id"/>"> <bean:write name="periodicidads" property="nombre"/></option>
												</c:if>
											</logic:iterate>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="profundidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.profundidad" />: </strong></label>
										<bean:define id="maxProfundidad">
											<inteco:properties key="profundidadMax.rastreo" file="crawler.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="profundidad"  property="profundidad" >
											<c:forEach begin="1" end="${maxProfundidad}" varStatus="status">
												<c:if test="${ModificarCuentaUsuarioForm.profundidad==status.count}">
													<option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
												<c:if test="${ModificarCuentaUsuarioForm.profundidad!=status.count}">
													<option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
											</c:forEach>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="amplitud"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.amplitud" />: </strong></label>
										<bean:define id="maxAmplitud">
											<inteco:properties key="pagPorNivelMax.rastreo" file="crawler.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="amplitud"  property="amplitud" >
											<c:forEach begin="1" end="${maxAmplitud}" varStatus="status">
												<c:if test="${ModificarCuentaUsuarioForm.amplitud==status.count}">
													<option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
												<c:if test="${ModificarCuentaUsuarioForm.amplitud!=status.count}">
													<option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
											</c:forEach>
											<option value="<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />"><bean:message key="nuevo.rastreo.amplitud.ilimitada" /></option>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="pseudoAleatorio"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.pseudoaleatorio" /></strong></label>
										<html:select styleClass="textoSelect" styleId="pseudoAleatorio"  property="pseudoAleatorio" >
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="inDirectory"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.in.directory" /></strong></label>
										<html:select styleClass="textoSelect" styleId="inDirectory"  property="inDirectory" >
											<html:option value="false"><bean:message key="select.no"/></html:option>
											<html:option value="true"><bean:message key="select.yes"/></html:option>
										</html:select>
									</div>
									
									<fieldset class="innerFieldset">
										<legend><bean:message key="nueva.cuenta.cliente.fecha.legend"/></legend>
										<div class="formItem">
											<label for="fechaInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.fechaInicio" />: </strong></label>
											<html:text styleClass="textoCorto" name="ModificarCuentaUsuarioForm" property="fechaInicio" styleId="fechaInicio" onkeyup="escBarra(event, document.forms['ModificarCuentaUsuarioForm'].elements['fechaInicio'], 1)" maxlength="10"/>
											<span id="calendar">
												<img src="../images/boton-calendario.gif" onclick="popUpCalendar(this, document.forms['ModificarCuentaUsuarioForm'].elements['fechaInicio'], 'dd/mm/yyyy')" alt="<bean:message key="img.calendario.alt" />"/> 
											</span> 
											<bean:message key="date.format"/>
										</div>
										
										<div class="formItem">
											<label for="horaInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.horaInicio" />: </strong></label>
											<html:select styleClass="textoSelectCorto" styleId="horaInicio" name="ModificarCuentaUsuarioForm" property="horaInicio">
												<html:options name="<%=Constants.HOURS %>"/>
											</html:select>
											
											<label for="minutoInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.minutoInicio" />: </strong></label>
											<html:select styleClass="textoSelectCorto" styleId="minutoInicio" name="ModificarCuentaUsuarioForm" property="minutoInicio" >
												<html:options name="<%=Constants.MINUTES %>"/>
											</html:select>
										</div>
									</fieldset>
									
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
