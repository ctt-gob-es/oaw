<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="NuevoTerminoCatForm"/>

	<bean:define id="idcat" value="<%= Constants.ID_CATEGORIA %>" />

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="categoriesMenu"><bean:message key="migas.categoria" /></html:link> / 
			<html:link forward="editCategorie" paramId="<%= Constants.ID_CATEGORIA %>" paramName="NuevoTerminoCatForm" paramProperty="id_categoria"><bean:message key="migas.editar.categoria" /></html:link> / 
			<bean:message key="migas.nuevo.termino.categoria" />
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
						
							<h2 class="config"><bean:message key="nuevo.termino.categoria.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/NuevoTerminoCat.do" onsubmit="return validateNuevoTerminoCatForm(this)">
								<input type="hidden" id="idcat" name="idcat" value="<bean:write name="NuevoTerminoCatForm" property="id_categoria" />" />
								<input type="hidden" id="accion" name="accion" value="nuevo" />
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="termino"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.termino.categoria.termino" />: </strong></label>
										<html:text styleClass="texto" styleId="termino" property="termino" maxlength="50"/>
									</div>
									<div class="formItem">
										<label for="porcentaje"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.termino.categoria.peso" />: </strong></label>
										<html:text styleClass="texto" styleId="porcentaje" property="porcentaje"/>
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