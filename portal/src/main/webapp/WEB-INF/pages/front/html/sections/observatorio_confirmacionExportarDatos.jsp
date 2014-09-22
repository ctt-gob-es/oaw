<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link>/ 
			<bean:message key="migas.eliminar.observatorio" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"><bean:message key="indice.observatorio.gestion.observatorio" /> </h1>
				<div id="cuerpoprincipal">
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="exportar.resultados.observatorio.title" /></h2>
							
							<div class="detail">
								<p><strong class="labelVisu"><bean:message key="confirmacion.exportar.resultados.observatorio.pregunta" /></strong></p>
								<p><strong class="labelVisu"><bean:message key="confirmacion.exportar.resultados.observatorio.info" /></strong></p>
								<div class="formItem">
									<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.nombre" />: </strong></label>
									<p><bean:write name="<%= Constants.OBSERVATORY_FORM %>" property="nombre" /></p>
								</div>
								<div class="formButton">
									<html:link styleClass="boton" forward="databaseExportActionExport" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="<%= Constants.OBSERVATORY_FORM %>" paramProperty="id"><bean:message key="boton.aceptar"/></html:link>
									<html:link styleClass="boton" forward="observatoryMenu" ><bean:message key="boton.cancelar"/></html:link>
								</div>
							</div>
							
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
