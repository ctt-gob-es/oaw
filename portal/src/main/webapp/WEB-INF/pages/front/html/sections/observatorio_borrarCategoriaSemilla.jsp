<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

	<bean:parameter name="<%=Constants.ID_CATEGORIA %>" id="idcat"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="getSeedCategories"><bean:message key="migas.categoria" /></html:link> /
			<bean:message key="migas.eliminar.categoria" />
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

						<h2 class="config"><bean:message key="categoria.semillas.titulo" /></h2>
						<div class="detail">
							<p><strong class="labelVisu"><bean:message key="categoria.semillas.borrar.advertencia" /></strong></p>
							<logic:notEmpty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
								<p><strong class="labelVisu"><bean:message key="categoria.semillas.borrar.info" /></strong></p>
								<ul class="lista_inicial">
									<logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>" id="elemento">
										<li><bean:write name="elemento" property="nombre"/></li>
									</logic:iterate>
								</ul>
								<p><strong class="labelVisu"><bean:message key="categoria.semillas.borrar.info2" /></strong></p>
							</logic:notEmpty>
							<div class="formButton">
								<html:link styleClass="boton" forward="deleteSeedCategory" paramId="<%= Constants.ID_CATEGORIA %>" paramName="<%=Constants.ID_CATEGORIA %>"><bean:message key="boton.aceptar"/></html:link>
								<html:link styleClass="boton" forward="getSeedCategories"><bean:message key="boton.cancelar"/></html:link>
							</div>
						</div>
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> <!-- fin CONTENEDOR GRAL. -->
