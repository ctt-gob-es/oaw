<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">
	
	<html:javascript formName="NuevaSemillaWebsForm"/>
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		 / <bean:message key="migas.semillas" /> / <bean:message key="migas.semillas.listado.webs" /></p>
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
							
								<h2 class="config"><bean:message key="nueva.semilla.webs.title" /></h2>
								
								<p><bean:message key="leyenda.campo.obligatorio" /></p>
								
								<html:form styleClass="formulario" method="post" action="/secure/NuevaSemillaWebs.do" onsubmit="return validateNuevaSemillaWebsForm(this)" >
									<p class="observ"><em><bean:message key="nueva.semilla.webs.informacion"/> </em>: <bean:message key="nueva.semilla.webs.info" /></p>
									<fieldset>
										<jsp:include page="/common/crawler_messages.jsp" />
										<legend> <bean:message key="nueva.semilla.webs.introduccion.url"/> </legend>
										
										<div class="formItem">
											<label for="nombreSemilla"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.webs.nombre" /></strong></label>
											<html:text styleId="nombreSemilla" styleClass="texto" name="NuevaSemillaWebsForm" property="nombreSemilla" />
										</div>
										
										<div class="formItem">
											<p><label for="ta1"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.webs.lista.url"/></strong></label></p>
											<html:textarea styleId="ta1" name="NuevaSemillaWebsForm" property="ta1" rows="5" cols="50" />
										</div>
										<div class="formButton">
											<html:hidden property="<%= Constants.BOTON_SEMILLA_WEB %>" value="boton"/>
											<html:submit><bean:message key="boton.aceptar"/></html:submit>
											<html:cancel><bean:message key="boton.volver"/></html:cancel>
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