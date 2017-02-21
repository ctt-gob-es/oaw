<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="NuevaSemillaGoogleForm"/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / <bean:message key="migas.semillas" /> / <bean:message key="migas.semillas.resultados.google" /></p>
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
						
							<h2 class="config"><bean:message key="nueva.semilla.google.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/NuevaSemillaGoogle.do" onsubmit="return validateNuevaSemillaGoogleForm(this)">
								<input type="hidden" name="control" id="control" value="si" />
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="query"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.google.query" /></strong></label>
										<html:text styleClass="texto" styleId="query" property="query" />
									</div>
									<div class="formItem">
										<label for="paginas"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.google.numero.paginas" /></strong></label>
										<html:text styleClass="texto" styleId="paginas" property="paginas" />
										<p class="notaInformativa"><bean:message key="nueva.semilla.google.info" /> </p>
									</div>
									<div class="formItem">
										<label for="nombreSemilla"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.google.nombreSemilla" /></strong></label>
										<html:text styleClass="texto" styleId="nombreSemilla" property="nombreSemilla" />
									</div>
									<div class="formButton">
										<html:hidden property="<%= Constants.BOTON_SEMILLA_GOOGLE %>" value="boton"/>
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
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
