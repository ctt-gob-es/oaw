<%@ include file="/common/taglibs.jsp" %> 
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			/ <bean:message key="migas.semillas" /> 
			/ <html:link forward="listSeedsMenu"> <bean:message key="migas.listado.borrar.semillas" /> </html:link>
			/ <bean:message key="migas.eliminar.semilla" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="sem"> <bean:message key="gestion.semillas"/> </h1>
	
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="eliminar.semilla.titulo" /></h2>
							
							<html:form styleClass="formulario" method="post" action="/secure/EliminarSemilla.do">
								<input type="hidden" name="segunda" id="segunda" value="si" />
								<input type="hidden" name="semilla" id="semilla" value="<bean:write name="SemillaForm" property="id" />" />
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<p><strong class="labelVisu"><bean:message key="eliminar.semilla.confirmacion" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.semilla.semilla" />: </strong></label>
										<p><bean:write name="SemillaForm" property="nombre" /></p>
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
