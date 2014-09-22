<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="categoriesMenu"><bean:message key="migas.categoria" /></html:link> / 
			<bean:define id="idCategoria" name="NuevoTerminoCatForm" property="id_categoria" />
			<html:link forward="editCategorie" paramName="idCategoria" paramId="<%= Constants.ID_CATEGORIA %>"><bean:message key="migas.editar.categoria" /></html:link> /
			<bean:message key="migas.eliminar.termino.categoria" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bullet"> <bean:message key="gestion.terminos" /> </h1>
	
				<div id="cuerpoprincipal">
				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="eliminar.termino.categoria.titulo" /></h2>
							
							<html:form styleClass="formulario" method="post" action="/secure/EliminarTerminoCat.do">
								<input type="hidden" id="idcat" name="idcat" value="<bean:write name="NuevoTerminoCatForm" property="id_categoria" />" />
								<input type="hidden" id="accion" name="accion" value="eliminar" />
								<input type="hidden" id="idter" name="idter" value="<bean:write name="NuevoTerminoCatForm" property="id_termino" />" />
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<p><strong class="labelVisu"><bean:message key="eliminar.termino.confirmacion" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.termino.termino" /> : </strong></label>
										<p><bean:write name="NuevoTerminoCatForm" property="termino" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.termino.peso" /></strong></label>
										<p><bean:write name="NuevoTerminoCatForm" property="porcentaje" /></p>
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
	</div> 