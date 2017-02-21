<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="ModificarMiCuentaUsuarioForm"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<bean:message key="migas.modificar.cuenta.cliente" /></p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<div id="cuerpoprincipal">
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="modificar.cuenta.cliente.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form method="post" styleClass="formulario" action="/secure/ModificarMiCuentaUsuario.do" onsubmit="return validateModificarCuentaUsuarioForm(this)">
								<input type="hidden" name="<%= Constants.NOMBRE_ANTIGUO %>"  value="<bean:write name="ModificarMiCuentaUsuarioForm" property="nombre_antiguo" />" />
								<input type="hidden" name="esPrimera" id="esPrimera" value="no" />
								<input type="hidden" name="deMenu" id="deMenu" value="true" />
								<logic:present name="<%= Constants.ID_CUENTA %>">
									<input type="hidden" name="<%= Constants.ID_CUENTA %>" value="<bean:write name="<%= Constants.ID_CUENTA %>" />" />
								</logic:present>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<div class="formItem">
										<bean:define id="nombreForm" value="" />
										<logic:notEmpty name="ModificarMiCuentaUsuarioForm" property="nombre">
											<bean:define id="nombreForm"><bean:write name="ModificarMiCuentaUsuarioForm" property="nombre" /></bean:define>
										</logic:notEmpty>
										<label for="nombre"><strong class="labelVisu"><bean:message key="modificar.cuenta.cliente.nombre" />: </strong></label>
										<p><%= nombreForm %></p>
									</div>
									
									<div class="formItem">
										<label for="periodicidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.periodicidad" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="periodicidad"  property="periodicidad" >
											<logic:iterate name="ModificarMiCuentaUsuarioForm" property="periodicidadVector" id="periodicidads">
												<c:if test="${ModificarMiCuentaUsuarioForm.periodicidad==periodicidads.id}">
													<option selected value="<bean:write name="periodicidads" property="id"/>"> <bean:write name="periodicidads" property="nombre"/></option>
												</c:if>
												<c:if test="${ModificarMiCuentaUsuarioForm.periodicidad!=periodicidads.id}">
													<option value="<bean:write name="periodicidads" property="id"/>"> <bean:write name="periodicidads" property="nombre"/></option>
												</c:if>
											</logic:iterate>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="profundidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.profundidad" />: </strong></label>
										<html:select styleClass="textoSelect" styleId="profundidad"  property="profundidad" >
											<logic:iterate name="ModificarMiCuentaUsuarioForm" property="profundidadesVector" id="profundidads">
												<c:if test="${ModificarMiCuentaUsuarioForm.profundidad==profundidads}">
													<option selected value="<bean:write name="profundidads"/>"> <bean:write name="profundidads" /></option>
												</c:if>
												<c:if test="${ModificarMiCuentaUsuarioForm.profundidad!=profundidads}">
													<option value="<bean:write name="profundidads"/>"> <bean:write name="profundidads" /></option>
												</c:if>
											</logic:iterate>
										</html:select>
									</div>
									
									<div class="formItem">
										<label for="amplitud"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.cuenta.cliente.amplitud" />: </strong></label>
										<html:text styleClass="texto" name="ModificarMiCuentaUsuarioForm" property="amplitud" styleId="amplitud" maxlength="3"/>
									</div>
									
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
