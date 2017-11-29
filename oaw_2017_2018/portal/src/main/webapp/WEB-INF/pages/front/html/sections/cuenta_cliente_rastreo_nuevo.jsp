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
<html:javascript formName="RastreoClienteForm"/>

<script type="text/javascript">
	
	function handleAll(field, event) {
	     var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	     if (keyCode) {
	         var i;
	         for (i = 0; i < field.form.elements.length; i++)
	             if (field == field.form.elements[i])
	                break;
	         i = (i + 1) % field.form.elements.length;
	         //field.form.elements[i].focus();
	         return false;
	     }
	     else
	     return true;
	}
	
	function checkCartridge(obj, cartridge){
		var idIntav = <inteco:properties key="cartridge.intav.id" file="crawler.properties" />;
		var idLenox = <inteco:properties key="cartridge.lenox.id" file="crawler.properties" />;
		var idMalware = <inteco:properties key="cartridge.malware.id" file="crawler.properties" />;
		if (cartridge == idIntav){
			enableField(obj);
		}else{
			disableField(obj);
		}
	}
</script>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			 / <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> 
			 / <bean:message key="migas.nuevo.rastreo.cliente" />
		 </p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
	
							<h2><bean:message key="nuevo.rastreo.cliente.title" /></h2>
							
							<logic:notPresent name="<%=Constants.LISTADO_CUENTAS_CLIENTE %>">	
								<p class="notaInformativaExito">
									<p><bean:message key="indice.cuentas.usuario.vacio"/></p>
									<p><html:link forward="newUserAccount" styleClass="boton"><bean:message key="indice.usuarios.sistema.nueva.cuenta.usuario" /></html:link>
									<html:link forward="crawlingsMenu" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link></p>
								</p>
							</logic:notPresent>
							<logic:present name="<%=Constants.LISTADO_CUENTAS_CLIENTE %>">
								<logic:empty name="<%=Constants.LISTADO_CUENTAS_CLIENTE %>" >	
									<div class="notaInformativaExito">
										<p><bean:message key="indice.cuentas.usuario.vacio"/></p>
										<p><html:link forward="newUserAccount" styleClass="boton"><bean:message key="indice.usuarios.sistema.nueva.cuenta.usuario" /></html:link>
										<html:link forward="crawlingsMenu" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link></p>
									</div>
								</logic:empty>
								<logic:notEmpty name="<%=Constants.LISTADO_CUENTAS_CLIENTE  %>" >	
					
								<p><bean:message key="leyenda.campo.obligatorio" /></p>
					
									<html:form action="/secure/clientCrawlingAction" onsubmit="return validateRastreoClienteForm(this)" styleClass="formulario">
										<input type="hidden" name="accionFor" value="insertar"/>
										<fieldset>
											<jsp:include page="/common/crawler_messages.jsp" />
											<div class="formItem">
												<label for="nombre"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.nombre" /></strong></label>
												<html:text styleClass="texto" name="RastreoClienteForm" property="nombre" styleId="nombre" maxlength="30"/>
											</div>
											<div class="formItem">
												<label for="cuenta_cliente"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.rastreo.cliente.cuenta.cliente" />: </strong></label>
												<logic:notEmpty name="<%=Constants.LISTADO_CUENTAS_CLIENTE %>">
													<html:select size="5" styleClass="textoSelect" styleId="cuenta_cliente" property="idCuenta" >
														<logic:iterate name="<%=Constants.LISTADO_CUENTAS_CLIENTE %>" type="es.inteco.rastreador2.utils.ListadoCuentasUsuario" id="elemento">
															<bean:define id="idCuenta"><bean:write name="elemento" property="id_cuenta"/></bean:define>
															<html:option value="<%=idCuenta %>"><bean:write name="elemento" property="nombreCuenta"/></html:option>
														</logic:iterate>
													</html:select>
												</logic:notEmpty>
											</div>
										
											<div class="formItem">
												<label for="cartucho"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.rastreo.cliente.cartucho" />: </strong></label>
												<logic:notEmpty name="<%=Constants.LISTADO_CARTUCHOS %>">
													<html:select onchange="checkCartridge('normaDiv', this.value)" styleClass="textoSelect" styleId="cartucho" property="cartucho" >
														<logic:iterate name="<%=Constants.LISTADO_CARTUCHOS %>" id="cartucho">
															<bean:define id="idCartucho"><bean:write name="cartucho" property="id"/></bean:define>
															<html:option value="<%=idCartucho %>"><bean:write name="cartucho" property="name"/></html:option>
														</logic:iterate>
													</html:select>
												</logic:notEmpty>
											</div>
											
											<logic:notEmpty name="<%=Constants.LISTADO_NORMAS %>">
												<div class="formItem" id="normaDiv">
													<label for="norma"><strong class="labelVisu"><bean:message key="nuevo.rastreo.cliente.norma" />: </strong></label>
													<html:select styleClass="textoSelect" styleId="norma" property="normaAnalisis" >
														<logic:iterate name="<%=Constants.LISTADO_NORMAS %>" id="norma">
															<bean:define id="idNorma"><bean:write name="norma" property="id"/></bean:define>
															<html:option value="<%=idNorma %>"><bean:write name="norma" property="name"/></html:option>
														</logic:iterate>
													</html:select>
													<noscript>
														<strong class="labelVisu"><bean:message key="norma.para.intav" /></strong>	
													</noscript>
												</div>
											</logic:notEmpty>
											
										</fieldset>
										
										<div class="formButton">
											<html:submit><bean:message key="boton.aceptar"/></html:submit>
											<html:cancel><bean:message key="boton.volver" /></html:cancel>
										</div>
									</html:form>
								</logic:notEmpty>
							</logic:present>
						</div>

					</div>

			</div>
		</div>	
	</div> 
