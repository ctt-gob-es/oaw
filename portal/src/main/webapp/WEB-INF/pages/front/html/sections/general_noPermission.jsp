<%@ include file="/common/taglibs.jsp" %> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		 / <bean:message key="migas.error" /></p>
	</div>
	



			<div id="main">
				
				<h1><img src="../images/bullet_h1.gif" /> <bean:message key="pagina.error" /> </h1>
				

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios_login">
							<h2><bean:message key="pagina.error" /></h2>
							<div class="notaInformativaExito">
								<p><bean:message key="mensaje.error.permisos"/></p>
								<p><html:link forward="indexAdmin" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link></p>
							</div>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
