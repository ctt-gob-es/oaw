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
<bean:define id="idIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
<html:xhtml/>

<html:javascript formName="NuevaCuentaUsuarioForm"/>
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
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			 / <html:link forward="clientAccountsMenu"><bean:message key="migas.cuenta.cliente" /></html:link> 
			 / <bean:message key="migas.nueva.cuenta.cliente" />
		 </p>
	</div>



			<div id="main">


					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">

                        <div id="migas">
                            <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                            <ol class="breadcrumb">
                              <li><html:link forward="usersMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.usuarios" /></html:link></li>
                              <li class="active"><bean:message key="migas.nueva.cuenta.cliente" /></li>
                            </ol>
                        </div>

						<div id="cajaformularios">
						
							<h2><bean:message key="nueva.cuenta.cliente.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/NuevaCuentaUsuario.do" onsubmit="return validateNuevaCuentaUsuarioForm(this)">
								<input type="hidden" name="esPrimera" value="no"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<div class="formItem">
										<label for="nombre"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.nombre" />: </strong></label>
										<html:text styleClass="texto" name="NuevaCuentaUsuarioForm" property="nombre" styleId="nombre" maxlength="100"/>
									</div>
									
									<div class="formItem">
										<label for="activo"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.activo" /></strong></label>
										<html:select styleClass="textoSelect" styleId="activo"  property="activo" >
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									
									<p><bean:message key="nueva.cuenta.cliente.urls.info" /></p>
									
									<div class="formItem">
										<label for="dominio"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.dominio" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" name="NuevaCuentaUsuarioForm" property="dominio" styleId="dominio" />
									</div>
									
									<div class="formItem">
										<label for="listaRastreable"><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.lista.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" name="NuevaCuentaUsuarioForm" property="listaRastreable" styleId="listaRastreable" />
									</div>
									
									<div class="formItem">
										<label for="listaNoRastreable"><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.lista.no.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" name="NuevaCuentaUsuarioForm" property="listaNoRastreable" styleId="listaNoRastreable" />
									</div>
									
									<div class="formItem">
										<label for="lenguaje"><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.lenguaje" /></strong></label>
										<html:select styleClass="textoSelect" styleId="lenguaje" property="lenguaje" >
											<logic:iterate name="NuevaCuentaUsuarioForm" property="lenguajeVector" id="lenguaje">
												<bean:define id="idLang"><bean:write name="lenguaje" property="id"/></bean:define>
												<html:option value="<%=idLang %>"><bean:write name="lenguaje" property="name"/></html:option>
											</logic:iterate>
										</html:select>
									</div>
									
									<logic:notEmpty name="NuevaCuentaUsuarioForm" property="cartuchos">
										<div class="formItem">
											<label for="cartuchosSelected"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.cartuchos" />: </strong></label>
											<div class="alinForm">
												<ul>
													<logic:iterate name="NuevaCuentaUsuarioForm" property="cartuchos" id="cartucho" type="es.inteco.rastreador2.dao.login.CartuchoForm">
														<bean:define id="styleIdCartucho">cartucho<bean:write name="cartucho" property="id" /></bean:define>
														<li><label for="<%= styleIdCartucho %>" class="noFloat"><html:multibox onchange="checkCartridge('normaDiv', 'enlacesRotos', this.value)" styleClass="multiboxStyle" property="cartuchosSelected" value="<%=cartucho.getId().toString() %>" styleId="<%= styleIdCartucho %>"/> <bean:write name="cartucho" property="name" /></label></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									
									<logic:notEmpty name="<%= Constants.LISTADO_NORMAS %>" >
										<div class="formItem" id="normaDiv">
											<label for="norma"><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.norma" />: </strong></label>
											<html:select styleClass="textoSelect" styleId="norma" property="normaAnalisis" >
												<logic:iterate name="<%= Constants.LISTADO_NORMAS %>" id="norma">
													<bean:define id="idNorma"><bean:write name="norma" property="id"/></bean:define>
													<html:option value="<%=idNorma %>"><bean:write name="norma" property="name"/></html:option>
												</logic:iterate>
											</html:select>
											<noscript>
												<p><strong class="labelVisu"><bean:message key="norma.para.intav" /></strong></p>
											</noscript>
										</div>
									</logic:notEmpty>
									
									<div class="formItem" id="enlacesRotos">
										<label for="enlaces"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.enlaces.rotos" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="enlaces" property="normaAnalisisEnlaces" >
											<html:option value="0"><bean:message key="enlaces.rotos.inactivo"/></html:option>
											<html:option value="1"><bean:message key="enlaces.rotos.activo"/></html:option>
										</html:select>
										<noscript>
											<p><strong class="labelVisu"><bean:message key="norma.para.intav" /></strong></p>
										</noscript>
									</div>
									
									<div class="formItem">
										<label for="periodicidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.periodicidad" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="periodicidad"  property="periodicidad" >
											<logic:iterate name="NuevaCuentaUsuarioForm" property="periodicidadVector" id="periodicidads">
												<c:if test="${NuevaCuentaUsuarioForm.periodicidad==periodicidads}">
													<option selected value="<bean:write name="periodicidads" property="id"/>"> <bean:write name="periodicidads" property="nombre"/></option>
												</c:if>
												<c:if test="${NuevaCuentaUsuarioForm.periodicidad!=periodicidads}">
													<option value="<bean:write name="periodicidads" property="id"/>"> <bean:write name="periodicidads" property="nombre"/></option>
												</c:if>
											</logic:iterate>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="profundidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.profundidad" />: </strong></label>
										<bean:define id="maxProfundidad">
											<inteco:properties key="profundidadMax.rastreo" file="crawler.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="profundidad"  property="profundidad" >
											<c:forEach begin="1" end="${maxProfundidad}" varStatus="status">
												<c:if test="${NuevaCuentaUsuarioForm.profundidad==status.count}">
													<option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
												<c:if test="${NuevaCuentaUsuarioForm.profundidad!=status.count}">
													<option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
											</c:forEach>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="amplitud"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.amplitud" />: </strong></label>
										<bean:define id="maxAmplitud">
											<inteco:properties key="pagPorNivelMax.rastreo" file="crawler.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="amplitud"  property="amplitud" >
											<c:forEach begin="1" end="${maxAmplitud}" varStatus="status">
												<c:if test="${NuevaCuentaUsuarioForm.amplitud==status.count}">
													<option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
												<c:if test="${NuevaCuentaUsuarioForm.amplitud!=status.count}">
													<option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
											</c:forEach>
											<option value="<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />"><bean:message key="nuevo.rastreo.amplitud.ilimitada" /></option>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="pseudoAleatorio"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.pseudoaleatorio" /></strong></label>
										<html:select styleClass="textoSelect" styleId="pseudoAleatorio"  property="pseudoAleatorio" >
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="inDirectory"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.in.directory" /></strong></label>
										<html:select styleClass="textoSelect" styleId="inDirectory"  property="inDirectory" >
											<html:option value="false"><bean:message key="select.no"/></html:option>
											<html:option value="true"><bean:message key="select.yes"/></html:option>
										</html:select>
									</div>
									
									<fieldset class="innerFieldset">
										<legend><bean:message key="nueva.cuenta.cliente.fecha.legend"/></legend>
										<div class="formItem">
											<label for="fechaInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.fecha.inicio" />: </strong></label>
											<html:text styleClass="textoCorto" name="NuevaCuentaUsuarioForm" property="fechaInicio" styleId="fechaInicio" onkeyup="escBarra(event, document.forms['NuevaCuentaUsuarioForm'].elements['fechaInicio'], 1)" maxlength="10"/>
											<span id="calendar">
												<img src="../images/boton-calendario.gif" onclick="popUpCalendar(this, document.forms['NuevaCuentaUsuarioForm'].elements['fechaInicio'], 'dd/mm/yyyy')" alt="<bean:message key="img.calendario.alt" />"/> 
											</span>
											<bean:message key="date.format"/>
										</div>
										
										<div class="formItem">
											<label for="horaInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.hora.inicio" />: </strong></label>
											<html:select styleClass="textoSelectCorto" styleId="horaInicio" name="NuevaCuentaUsuarioForm" property="horaInicio" >
												<html:options name="<%=Constants.HOURS %>"/>
											</html:select>
											<label for="minutoInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.cuenta.cliente.minuto.inicio" />: </strong></label>
											<html:select styleClass="textoSelectCorto" styleId="minutoInicio" name="NuevaCuentaUsuarioForm" property="minutoInicio" >
												<html:options name="<%=Constants.MINUTES %>"/>
											</html:select>
										</div>
									</fieldset>
									
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.volver"/></html:cancel>
									</div>
									
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
