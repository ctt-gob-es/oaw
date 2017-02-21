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
				<div id="cuerpoprincipal">
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="generar.informes.observatorio.title" /></h2>
							
							<div class="detail">
							    <bean:define id="generateTime"><%= request.getParameter("GENERATE_TIME") %></bean:define>
								<p><strong class="labelVisu"><bean:message key="generar.informes.observatorio.tiempo_espera" arg0='<%= request.getAttribute("GENERATE_TIME").toString() %>'/></strong></p>
								<p><strong class="labelVisu"><bean:message key="generar.informes.observatorio.aviso_correo" arg0='<%= request.getAttribute("EMAIL").toString() %>'/></strong></p>

								<div class="formButton">
									<html:link styleClass="boton" forward="observatoryMenu" ><bean:message key="boton.volver"/></html:link>
								</div>
							</div>
							
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
