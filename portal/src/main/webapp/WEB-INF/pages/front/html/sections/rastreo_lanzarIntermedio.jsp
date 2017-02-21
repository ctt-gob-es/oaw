<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>

<%@page import="com.sun.corba.se.impl.orbutil.closure.Constant"%>
<inteco:sesion action="ifConfigAdmin">
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		 / <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> / 
		 <logic:equal name="LanzarWrapCommandForm" property="mensaje" value="<%= Constants.PARAR1 %>">
		 	<%= Constants.PARAR %>
		 </logic:equal>
		 <logic:equal name="LanzarWrapCommandForm" property="mensaje" value="<%= Constants.LANZAR1 %>">
		 	<%= Constants.LANZAR %>
		 </logic:equal>
		 <bean:message key="migas.rastreoo" /></p>
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
						
							<logic:equal name="LanzarWrapCommandForm" property="mensaje" value="<%= Constants.PARAR1 %>">
								<h2 class="config"><%= Constants.PARAR %> <bean:message key="lanzar.intermedio.rastreo.titulo" /></h2>
							</logic:equal>
							<logic:equal name="LanzarWrapCommandForm" property="mensaje" value="<%= Constants.LANZAR1 %>">
								<h2 class="config"><%= Constants.LANZAR %> <bean:message key="lanzar.intermedio.rastreo.titulo" /></h2>
							</logic:equal>
							<html:form styleClass="formulario" method="post" action="/secure/LanzarWrapCommand.do">
								<input type="hidden" name="intermedio" id="intermedio" value="si" />
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<p><strong class="labelVisu"><bean:message key="lanzar.intermedio.seguro1" /> <bean:write name="LanzarWrapCommandForm" property="mensaje" /> <bean:message key="lanzar.intermedio.seguro2" /></strong></p>
									<p><strong class="labelVisu"><bean:write name="LanzarWrapCommandForm" property="textoAdicional" /><br/><bean:write name="LanzarWrapCommandForm" property="textoAdicional2" /></strong></p>
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="lanzar.intermedio.nombre.rastreo" />: </strong></label>
											<p><bean:write name="LanzarWrapCommandForm" property="rastreo" /></p>
										</div>
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="lanzar.intermedio.cartucho" />: </strong></label>
												<bean:define id="cartuchoName">
													<bean:write name="LanzarWrapCommandForm" property="cartucho" />
												</bean:define>
												<p><inteco:trunp cad="cartuchoName"/></p>
										</div>
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="lanzar.intermedio.fecha.lanzamiento" />: </strong></label>
											<logic:notEmpty name="LanzarWrapCommandForm" property="fecha">
												<p><bean:write name="LanzarWrapCommandForm" property="fecha" /></p>
											</logic:notEmpty>
											<logic:empty name="LanzarWrapCommandForm" property="fecha">
												<p><bean:message key="ver.rastreo.fecha.lanzado.no.lanzado" /></p>
											</logic:empty>
										</div>
										<div class="formButton">
											<html:submit><bean:message key="boton.aceptar" /></html:submit>
											<html:cancel><bean:message key="boton.cancelar" /></html:cancel>
										</div>
								</fieldset>		
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 


</inteco:sesion>