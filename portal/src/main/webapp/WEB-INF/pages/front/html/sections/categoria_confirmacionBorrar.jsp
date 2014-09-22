<%@ include file="/common/taglibs.jsp" %> 
	<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="categoriesMenu"><bean:message key="migas.categoria" /></html:link> / 
			<bean:message key="migas.eliminar.categoria" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bullet"> <bean:message key="indice.categorias.gestion.categorias" /> </h1>
	
				<div id="cuerpoprincipal">
				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					<div id="container_der">
					
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="eliminar.categoria.titulo" /></h2>
							
							<html:form styleClass="formulario" method="post" action="/secure/EliminarCategoria.do">
								<input type="hidden" name="idcat" id="id_categoria" value="<bean:write name="verCategoriaForm" property="id_categoria" />" />
								<input type="hidden" name="confirmacion" value="si"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<p><strong class="labelVisu"><bean:message key="eliminar.categoria.confirmacion" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.categoria.categoria" /></strong></label>
										<p><bean:write name="VerCategoriaForm" property="categoria" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.categoria.umbral" /></strong></label>
										<p><bean:write name="VerCategoriaForm" property="umbral" /></p>
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