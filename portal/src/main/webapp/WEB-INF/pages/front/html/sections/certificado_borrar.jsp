<%@ include file="/common/taglibs.jsp" %> 

<%@page import="es.inteco.common.Constants"%>

<inteco:sesion action="ifConfigAdmin">
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<bean:message key="migas.certificados" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"> <bean:message key="gestion.certificado" /></h1>
				
				<div id="cuerpoprincipal">
				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="confirmar.borrar.certificado" /></h2>
							
							<p><bean:message key="confirmar.borrar.certificado.info" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/certificatesAction.do">
								<input type="hidden" name="<%=Constants.ACCION %>" value="<%=Constants.DELETE_CERTIFICATE %>"/>
								<input type="hidden" name="<%=Constants.ALIAS %>" value="<bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="alias" />"/>
								<fieldset>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.emisor" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="issuer" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.receptor" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="subject" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.valido.desde" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="validateFrom" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.valido.hasta" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="validateTo" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.version" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="version" /></p>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> <!-- fin CONTENEDOR GRAL. -->
</inteco:sesion>