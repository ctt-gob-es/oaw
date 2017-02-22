<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<inteco:sesion action="ifConfigAdmin">

<bean:define id="idIntav">
	<inteco:properties key="cartridge.intav.id" file="crawler.properties" />
</bean:define>
<html:xhtml/>
<html:javascript formName="InsertarRastreoForm"/>

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
	
	function checkCartridge(obj, obj2, cartridge){
		var idIntav = <inteco:properties key="cartridge.intav.id" file="crawler.properties" />;
		var idLenox = <inteco:properties key="cartridge.lenox.id" file="crawler.properties" />;
		var idMalware = <inteco:properties key="cartridge.malware.id" file="crawler.properties" />;
		if (cartridge == idIntav){
			enableField(obj);
			enableField(obj2);
		}else{
			disableField(obj);
			disableField(obj2);
		}
	}
</script>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			 / <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> 
			 / <bean:message key="migas.nuevo.rastreo" />
		 </p>
	</div>



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="nuevo.rastreo.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/InsertarRastreo.do" onsubmit="return validate(this)" enctype="multipart/form-data">
								<input type="hidden" name="accionFor" value="insertar"/>
								<input type="hidden" name="<%= Constants.IS_NEW %>" value="true"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="codigo"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.nombre" /></strong></label>
										<html:text styleClass="texto" name="InsertarRastreoForm" property="codigo" styleId="codigo" />
									</div>
									<div class="formItem">
										<label for="cartucho"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.cartucho" /></strong></label>
										<html:select onchange="checkCartridge('normaDiv', 'enlacesRotos', this.value)" styleClass="textoSelect" styleId="cartucho" property="cartucho" >
											<logic:iterate name="InsertarRastreoForm" property="cartuchos" id="cartucho">
												<bean:define id="idCartucho"><bean:write name="cartucho" property="id"/></bean:define>
												<html:option value="<%=idCartucho %>" ><bean:write name="cartucho" property="name"/></html:option>
											</logic:iterate>
										</html:select>
									</div>
									<div class="formItem" id="normaDiv">
										<label for="norma"><strong class="labelVisu"><bean:message key="nuevo.rastreo.norma" /></strong></label>
										<html:select styleClass="textoSelect" styleId="norma" property="normaAnalisis" >
											<logic:iterate name="InsertarRastreoForm" property="normaVector" id="norma">
												<bean:define id="idNorma"><bean:write name="norma" property="id"/></bean:define>
												<html:option value="<%=idNorma %>"><bean:write name="norma" property="name"/></html:option>
											</logic:iterate>
										</html:select>
										<noscript>
											<p><strong class="labelVisu"><bean:message key="norma.para.intav" /></strong></p>
										</noscript>
									</div>
									<div class="formItem" id="enlacesRotos">
										<label for="enlaces"><strong class="labelVisu"><bean:message key="modificar.rastreo.enlaces.rotos" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="enlaces" property="normaAnalisisEnlaces" >
											<html:option value="1"><bean:message key="enlaces.rotos.activo"/></html:option>
											<html:option value="0"><bean:message key="enlaces.rotos.inactivo"/></html:option>
										</html:select>
										<noscript>
											<p><strong class="labelVisu"><bean:message key="norma.para.intav" /></strong></p>
										</noscript>
									</div>
									<div class="formItem">
										<label for="lenguaje"><strong class="labelVisu"><bean:message key="nuevo.rastreo.lenguaje" /></strong></label>
										<html:select styleClass="textoSelect" styleId="lenguaje" property="lenguaje" >
											<logic:iterate name="InsertarRastreoForm" property="lenguajeVector" id="lenguaje">
												<bean:define id="idLang"><bean:write name="lenguaje" property="id"/></bean:define>
												<html:option value="<%=idLang %>"><bean:write name="lenguaje" property="name"/></html:option>
											</logic:iterate>
										</html:select>
									</div>
									<div class="formItem">
										<label for="semilla" ><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.url.semilla" /></strong></label>
										<html:text styleClass="texto" property="semilla" styleId="semilla" readonly="true" />
										<html:submit property="semillaBoton"><bean:message key="boton.semilla" /></html:submit>
									</div>
									<div class="formItem">
										<label for="listaRastreable"><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.lista.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" name="InsertarRastreoForm" property="listaRastreable" styleId="listaRastreable" />
									</div>
									<div class="formItem">
										<label for="listaNoRastreable"><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.lista.no.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" name="InsertarRastreoForm" property="listaNoRastreable" styleId="listaNoRastreable" />
									</div>
									<div class="formItem">
										<label for="profundidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.profundidad" /></strong></label>
										<bean:define id="maxProfundidad">
											<inteco:properties key="profundidadMax.rastreo" file="crawler.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="profundidad"  property="profundidad" >
											<c:forEach begin="1" end="${maxProfundidad}" varStatus="status">
												<c:if test="${InsertarRastreoForm.profundidad==status.count}">
													<option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
												<c:if test="${InsertarRastreoForm.profundidad!=status.count}">
													<option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
											</c:forEach>
										</html:select>
									</div>
									<div class="formItem">
										<label for="topN"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.paginas.nivel" /></strong></label>
										<bean:define id="maxtopN">
											<inteco:properties key="pagPorNivelMax.rastreo" file="crawler.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="topN"  property="topN" >
											<c:forEach begin="1" end="${maxtopN}" varStatus="status">
												<c:if test="${InsertarRastreoForm.topN== status.count}">
													<option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
												<c:if test="${InsertarRastreoForm.topN!=status.count}">
													<option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
											</c:forEach>
											<option value="<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />"><bean:message key="nuevo.rastreo.amplitud.ilimitada" /></option>
										</html:select>
									</div>
									<div class="formItem">
										<label for="pseudoAleatorio"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.pseudoaleatorio" /></strong></label>
										<html:select styleClass="textoSelect" styleId="pseudoAleatorio"  property="pseudoAleatorio" >
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									<div class="formItem">
										<label for="exhaustive"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.exhaustive" /></strong></label>
										<html:select styleClass="textoSelect" styleId="exhaustive"  property="exhaustive" >
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									<div class="formItem">
										<label for="inDirectory"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.in.directory" /></strong></label>
										<html:select styleClass="textoSelect" styleId="inDirectory"  property="inDirectory" >
											<html:option value="false"><bean:message key="select.no"/></html:option>
											<html:option value="true"><bean:message key="select.yes"/></html:option>
										</html:select>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 

</inteco:sesion>