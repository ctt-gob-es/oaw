<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<bean:define id="idSystemRole">
	<inteco:properties key="role.type.system" file="crawler.properties" />
</bean:define>
<bean:define id="idClientRole">
	<inteco:properties key="role.type.client" file="crawler.properties" />
</bean:define>
<bean:define id="idObservatoryRole">
	<inteco:properties key="role.type.observatory" file="crawler.properties" />
</bean:define>
	
	<logic:present name="deMenu">
		<html:javascript formName="ModificarUsuarioPassForm"/>
	</logic:present>
	<logic:notPresent name="deMenu">
		<html:javascript formName="ModificarUsuarioPassFormAdmin"/>
	</logic:notPresent>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio"/></html:link> / 
		<logic:notPresent name="deMenu">
			<bean:parameter name="<%=Constants.USER %>" id="user"/>
			<bean:parameter name="<%=Constants.ROLE_TYPE %>" id="roleType"/>
			<html:link forward="usersMenu"><bean:message key="migas.usuarios" /></html:link> /
			<logic:equal name="<%=Constants.ROLE_TYPE %>" value="<%=idSystemRole %>">
				<html:link forward="editSystemUser" paramName="user" paramId="<%= Constants.COM_USER %>"><bean:message key="migas.editar.usuario" /></html:link> /
			</logic:equal>
			<logic:equal name="<%=Constants.ROLE_TYPE %>" value="<%=idClientRole %>">
				<html:link forward="editClientUser" paramName="user" paramId="<%= Constants.COM_USER %>"><bean:message key="migas.editar.usuario" /></html:link> /
			</logic:equal>
			<logic:equal name="<%=Constants.ROLE_TYPE %>" value="<%=idObservatoryRole %>">
				<html:link forward="editObservatoryUser" paramName="user" paramId="<%= Constants.COM_USER %>"><bean:message key="migas.editar.usuario" /></html:link> /
			</logic:equal>
		</logic:notPresent>
		<bean:message key="migas.cambiar.pass.admin" /></p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
					
							<h2><bean:message key="cambiar.pass.admin.titulo" /></h2>
							<bean:define id="action" value="" />
							<bean:define id="form" value="" />
							<bean:define id="formName" value="" />
							<logic:present name="deMenu">
								<bean:define id="action">/secure/CambiarPassAdmin</bean:define>
								<bean:define id="form">return validateModificarUsuarioPassForm(this)</bean:define>
								<bean:define id="formName">ModificarUsuarioPassForm</bean:define>
							</logic:present>
							<logic:notPresent name="deMenu">
								<bean:define id="action">/secure/CambiarPassAdminA</bean:define>
								<bean:define id="form">return validateModificarUsuarioPassFormAdmin(this)</bean:define>
								<bean:define id="formName">ModificarUsuarioPassFormAdmin</bean:define>	
							</logic:notPresent>	
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form method="post" styleClass="formulario" action="<%= action %>" onsubmit="<%= form %>">
								<input type="hidden" name="action" value="modificar"/>
								<input type="hidden" name="<%=Constants.USER %>" value="<bean:write name="<%= formName %>" property="idUsuario" />" />
								<input type="hidden" name="<%=Constants.ROLE_TYPE %>" value="<bean:write name="<%= formName %>" property="roleType" />" />
								<logic:present name="deMenu">
									<input type="hidden" name="deMenu" value="<bean:write name="deMenu"/>" />
								</logic:present>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<div class="formItem">
										<bean:define id="userN" value="" />
										<logic:notEmpty name="<%= formName %>" property="username">
											<bean:define id="userN"><bean:write name="<%= formName %>" property="username" /></bean:define>
										</logic:notEmpty>
										<label for="username"><strong class="labelVisu"><bean:message key="cambiar.pass.admin.usuario" /></strong></label>
										<html:text styleClass="texto" maxlength="100" styleId="username" property="username" value="<%= userN%>" readonly="true"/>
									</div>
									<logic:present name="deMenu">
										<div class="formItem">
											<label for="passwold"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="cambiar.pass.admin.pass.antiguo" /></strong></label>
											<html:password styleClass="texto" styleId="passwold" property="passwold" value="" maxlength="20" />
										</div>
									</logic:present>
									<div class="formItem">
										<label for="password"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="cambiar.pass.admin.pass.nuevo" /></strong></label>
										<html:password styleClass="texto" styleId="password" property="password" value="" maxlength="20"/>
									</div>
									<div class="formItem">
										<label for="password2"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="cambiar.pass.admin.pass.confirmacion" /></strong></label>
										<html:password styleId="password2" styleClass="texto" property="password2" value="" maxlength="20"/>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar"/></html:submit>
										<html:cancel><bean:message key="boton.cancelar"/></html:cancel>
									</div>
								</fieldset>	
							</html:form>
							
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
