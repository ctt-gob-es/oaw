<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="SubirConclusionesForm"/>
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${idExObs}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<html:link forward="getFulfilledObservatories" name="params"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
		<bean:message key="migas.indice.observatorios.subir.conclusiones"/>
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
							<h2 class="config"><bean:message key="subir.conclusiones.titulo" /></h2>
					
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form  styleClass="formulario" method="post"  action="/secure/ResultadosAnonimosObservatorio" enctype="multipart/form-data" onsubmit="return validateSubirConclusionesForm(this)">
								<input type="hidden" name="<%=Constants.ACTION %>" value="<%=Constants.UPLOAD_FILE %>"/>
								<input type="hidden" name="<%=Constants.ID_OBSERVATORIO %>" value="<bean:write name="<%=Constants.ID_OBSERVATORIO %>"/>"/>
								<input type="hidden" name="<%=Constants.ID_EX_OBS %>" value="<bean:write name="<%=Constants.ID_EX_OBS %>"/>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="file"><strong class="labelVisu"><bean:message key="fichero.conclusiones" />: </strong></label>
										<html:file styleClass="texto" property="file" styleId="file"/>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div>
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> <!-- fin CONTENEDOR GRAL. -->
