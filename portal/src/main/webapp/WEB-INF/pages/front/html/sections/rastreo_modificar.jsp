<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<html:javascript formName="InsertarRastreoForm"/>

<script type="text/javascript">
	
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

<inteco:sesion action="ifConfigAdmin">

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link>  / 
			<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> / 
			<bean:message key="migas.editar.rastreo" />
		</p>
	</div>



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
					
							<h2><bean:message key="modificar.rastreo.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" enctype="multipart/form-data" method="post" action="/secure/ModificarRastreo.do" onsubmit="return validate(this)">
								<input type="hidden" name="accionFor" value="modificar"/>
								<input type="hidden" name="<%= Constants.IS_NEW %>" value="false"/>
								<input type="hidden" name="<%= Constants.ID_RASTREO %>" value="<c:out value="${InsertarRastreoForm.id_rastreo}"/>"/>
								<input type="hidden" name="rastreo_antiguo" id="rastreo_antiguo" value="<bean:write name="InsertarRastreoForm" property="rastreo" />" />
								<html:hidden name="InsertarRastreoForm" property="cuenta_cliente"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<bean:define id="nombreR" value="" />
										<logic:notEmpty name="InsertarRastreoForm" property="rastreo">
											<bean:define id="nombreR"><bean:write name="InsertarRastreoForm" property="codigo" /></bean:define>
										</logic:notEmpty>
										<label for="codigo"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.rastreo.nombre" />: </strong></label>
										<html:text styleClass="texto" styleId="codigo" property="codigo" value="<%= nombreR %>"/>
									</div>
									<div class="formItem">
										<label for="cartucho"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.rastreo.cartucho" />: </strong></label>
										<html:select onchange="checkCartridge('enlacesRotos', 'normaDiv', this.value)" styleClass="textoSelect" styleId="cartucho" property="cartucho" >
											<logic:iterate name="InsertarRastreoForm" property="cartuchos" id="cartucho">
												<bean:define id="idCartucho"><bean:write name="cartucho" property="id"/></bean:define>
												<html:option value="<%=idCartucho %>"><bean:write name="cartucho" property="name"/></html:option>
											</logic:iterate>
										</html:select>
									</div>
									<div class="formItem" id="normaDiv">
										<label for="norma"><strong class="labelVisu"><bean:message key="modificar.rastreo.norma" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="norma" property="normaAnalisis" >
											<logic:iterate name="InsertarRastreoForm" property="normaVector" id="norma">
												<c:if test="${InsertarRastreoForm.normaAnalisis == norma.id}">
													<option selected value="<bean:write name="norma" property="id"/>"> <bean:write name="norma" property="name" /></option>
												</c:if>
												<c:if test="${InsertarRastreoForm.normaAnalisis != norma.id}">
													<option value="<bean:write name="norma" property="id"/>"> <bean:write name="norma" property="name" /></option>
												</c:if>
											</logic:iterate>
										</html:select>
										<noscript>
											<p><strong class="labelVisu"><bean:message key="norma.para.intav" /></strong></p>
										</noscript>
									</div>
									<div class="formItem" id="enlacesRotos">
										<label for="enlaces"><strong class="labelVisu"><bean:message key="modificar.rastreo.enlaces.rotos" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="enlaces" property="normaAnalisisEnlaces" >
											<c:if test="${InsertarRastreoForm.normaAnalisisEnlaces == 0}">
												<option selected value="0"><bean:message key="enlaces.rotos.inactivo"/></option>
											</c:if>
											<c:if test="${InsertarRastreoForm.normaAnalisisEnlaces != 0}">
												<option value="0"><bean:message key="enlaces.rotos.inactivo"/></option>
											</c:if>
											<c:if test="${InsertarRastreoForm.normaAnalisisEnlaces == 1}">
												<option selected value="1"><bean:message key="enlaces.rotos.activo"/></option>
											</c:if>
											<c:if test="${InsertarRastreoForm.normaAnalisisEnlaces != 1}">
												<option value="1"><bean:message key="enlaces.rotos.activo"/></option>
											</c:if>
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
										<bean:define id="sem" value="" />
										<logic:notEmpty name="InsertarRastreoForm" property="semilla">
											<bean:define id="sem"><bean:write name="InsertarRastreoForm" property="semilla" /></bean:define>
										</logic:notEmpty>
										<label for="semilla"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.rastreo.url.semilla" />: </strong></label>
										<html:text styleClass="texto" styleId="semilla" property="semilla" value="<%= sem %>" />
										<html:submit property="semillaBoton"><bean:message key="boton.semilla" /></html:submit>
									</div>
									<div class="formItem">
										<label for="listaRastreable"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.lista.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" name="InsertarRastreoForm" property="listaRastreable" styleId="listaRastreable" />
									</div>
									
									<div class="formItem">
										<label for="listaNoRastreable"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.lista.no.rastreable" />: </strong></label>
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
										<bean:define id="amplitudIlimitada">
											<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="topN"  property="topN" >
											<c:forEach begin="1" end="${maxtopN}" varStatus="status">
												<bean:define id="count">
													<c:out value="${status.count}" />
												</bean:define>
												<html:option value="<%=count%>"> <%=count%></html:option>
											</c:forEach>
											<html:option value="<%=amplitudIlimitada%>"><bean:message key="nuevo.rastreo.amplitud.ilimitada" /></html:option>											
										</html:select>
									</div>
									<div class="formItem">
										<label for="pseudoAleatorio"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.pseudoaleatorio" /></strong></label>
										<html:select name="InsertarRastreoForm" styleClass="textoSelect" styleId="pseudoAleatorio"  property="pseudoAleatorio" >
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
										<html:cancel><bean:message key="boton.cancelar" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 

</inteco:sesion>