<%@ include file="/common/taglibs.jsp" %> 

<%@page import="es.inteco.common.Constants"%>

<html:javascript formName="CertificateForm"/>

<inteco:sesion action="ifConfigAdmin">
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="loadCertificateForm"><bean:message key="migas.certificados" /></html:link> / 
		<bean:message key="migas.nuevo.certificado"/>
		</p>
	</div>
	



			<div id="main">

				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="subir.certificado.nuevo" /> </h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" enctype="multipart/form-data" method="post" action="/secure/certificatesAction.do" onsubmit="return validateCertificateForm(this)">
								<input type="hidden" name="<%=Constants.ACCION %>" value="<%=Constants.UPLOAD_CERTIFICATE %>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="host"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="formulario.certificados.host"/>:</strong></label>
										<html:text styleClass="texto" property="host" styleId="host"/>
									</div>
									<div class="formItem">
										<label for="port"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="formulario.certificados.port"/>:</strong></label>
										<html:text property="port" styleClass="texto" styleId="port" value="443"/>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
									</div>
								</fieldset>
							</html:form>
							<p id="pCenter"><html:link forward="loadCertificateForm" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
						</div><!-- fin cajaformularios -->
					</div>

			</div>

</inteco:sesion>
