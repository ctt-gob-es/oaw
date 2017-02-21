<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="NuevoUsuarioObservatorioForm"/>

<bean:parameter name="<%=Constants.ROLE_TYPE %>" id="roleType"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="usersMenu"><bean:message key="migas.usuarios" /></html:link>/ 
			<bean:message key="migas.nuevo.usuario.observatorio" />
		</p>
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
	
							<h2 class="config"><bean:message key="nuevo.usuario.title" /></h2>
							<html:form styleClass="formulario" method="post" action="/secure/NuevoUsuarioObservatorio.do" onsubmit="return validateNuevoUsuarioObservatorioForm(this)">
								<input type="hidden" name="<%= Constants.ES_PRIMERA %>" value="no" />
								<input type="hidden" name="<%=Constants.ROLE_TYPE %>" value="<bean:write name="roleType"/>"/>
								<jsp:include page="/common/crawler_messages.jsp" />
									<fieldset>
										<logic:empty name="NuevoUsuarioObservatorioForm" property="observatorioV.listadoObservatorio">	
											<p class="notaInformativaExito">
												<p><bean:message key="nuevo.usuario.sin.observatorio"/></p>
												<p>
													<html:link forward="newObservatory" styleClass="boton"><bean:message key="boton.crear.observatorio" /></html:link>
													<html:link forward="usersMenu" styleClass="boton"><bean:message key="boton.volver" /></html:link>
												</p>
											</p>
										</logic:empty>
										<logic:notEmpty name="NuevoUsuarioObservatorioForm" property="observatorioV.listadoObservatorio">	
											<p><bean:message key="leyenda.campo.obligatorio" /></p>
											<div class="formItem">
												<label for="nombre"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.usuario" /></strong></label>
												<html:text styleId="nombre" styleClass="texto" property="nombre" maxlength="100"/>
											</div>
											
											<div class="formItem">
												<label for="password"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.password" /></strong></label>
												<html:password styleId="password" styleClass="texto" property="password" maxlength="20"/>
											</div>
											
											<div class="formItem">
												<label for="confirmar_password"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.confirmar.password" /></strong></label>
												<html:password styleId="confirmar_password" styleClass="texto" property="confirmar_password" maxlength="20"/>
											</div>
											
											<div class="formItem">
												<label for="observatorio"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.observatorio" /></strong></label>
												<logic:notEqual value="null" name="NuevoUsuarioObservatorioForm" property="observatorioV">
													<bean:define id="cargarO" name="NuevoUsuarioObservatorioForm" property="observatorioV" type="es.inteco.rastreador2.actionform.observatorio.CargarObservatorioForm"/>
												</logic:notEqual>
												<logic:notEmpty name="cargarO" property="listadoObservatorio">
													<html:select size="5" multiple="true" styleClass="textoSelect" styleId="observatorio" property="observatorio" >
														<logic:iterate name="cargarO" property="listadoObservatorio" id="elemento">
															<bean:define id="idObservatorio"><bean:write name="elemento" property="id_observatorio"/></bean:define>
															<html:option value="<%=idObservatorio %>"><bean:write name="elemento" property="nombreObservatorio"/></html:option>
														</logic:iterate>
													</html:select>
												</logic:notEmpty>
											</div>
											
											<div class="formItem">
												<label for="nombre2"><strong class="labelVisu"><bean:message key="nuevo.usuario.nombre" /></strong></label>
												<html:text styleClass="texto" styleId="nombre2" property="nombre2" maxlength="100"/>
											</div>
											
											<div class="formItem">
												<label for="apellidos"><strong class="labelVisu"><bean:message key="nuevo.usuario.apellidos" /></strong></label>
												<html:text styleClass="texto" property="apellidos" styleId="apellidos" maxlength="150"/>
											</div>
											
											<div class="formItem">
												<label for="departamento"><strong class="labelVisu"><bean:message key="nuevo.usuario.departamento" /></strong></label>
												<html:text styleClass="texto"  property="departamento" styleId="departamento" maxlength="100"/>
											</div>
											
											<div class="formItem">
												<label for="email"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.eMail" /></strong></label>
												<html:text styleClass="texto"  property="email" styleId="email" maxlength="100"/>
											</div>
											
											<div class="formButton">
												<html:submit><bean:message key="boton.aceptar"/></html:submit>
												<html:cancel><bean:message key="boton.volver"/></html:cancel>
											</div>
										</logic:notEmpty>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> <!-- fin CONTENEDOR GRAL. -->

