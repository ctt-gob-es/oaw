<%@ include file="/common/taglibs.jsp" %> 

<inteco:sesion action="ifConfigAdmin">

<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> / 
			<bean:message key="eliminar.rastreo" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"> <bean:message key="indice.rastreo.gestion.rastreos"/> </h1>
	
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="eliminar.rastreo.titulo" /> </h2>
							
							<html:form styleClass="formulario" method="post" action="/secure/EliminarRastreo.do">
								<input type="hidden" name="rastreo" id="rastreo" value="<bean:write name="EliminarRastreoForm" property="codigo" />" />
								<input type="hidden" name="idrastreo" value="<bean:write name="EliminarRastreoForm" property="idrastreo" />"/>
								<input type="hidden" name="confirmacion" value="si"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<p><strong class="labelVisu"><bean:message key="eliminar.rastreo.confirmacion" /></strong></p>
									<p><strong class="labelVisu"><bean:message key="eliminar.rastreo.confirmacion2" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.rastreo.nombre.rastreo" />: </strong></label>
										<p><bean:write name="EliminarRastreoForm" property="codigo" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.rastreo.fecha.creacion" />: </strong></label>
										<p><bean:write name="EliminarRastreoForm" property="fecha" /></p>
									</div>
									<div class="formItem">
										<c:set var="cartuchoName" value="${EliminarRastreoForm.cartucho}"/>
										<label><strong class="labelVisu"><bean:message key="eliminar.rastreo.cartucho" />: </strong></label>
										<p><inteco:trunp cad="cartuchoName"/></p>
									</div>
									<logic:notEmpty name="EliminarRastreoForm" property="normaAnalisis">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="eliminar.rastreo.norma" />: </strong></label>
											<p><bean:write name="EliminarRastreoForm" property="normaAnalisis" /></p>
										</div>
									</logic:notEmpty>
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
