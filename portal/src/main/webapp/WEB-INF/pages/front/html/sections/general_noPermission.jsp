<%@ include file="/common/taglibs.jsp" %> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		 / <bean:message key="migas.error" /></p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				
				<h1><img src="../images/bullet_h1.gif" /> <bean:message key="pagina.error" /> </h1>
				
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios_login">
							<h2 class="config"><bean:message key="pagina.error" /></h2>
							<div class="notaInformativaExito">
								<p><bean:message key="mensaje.error.permisos"/></p>
								<p><html:link forward="indexAdmin" styleClass="boton"><bean:message key="boton.volver" /></html:link></p>
							</div>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
