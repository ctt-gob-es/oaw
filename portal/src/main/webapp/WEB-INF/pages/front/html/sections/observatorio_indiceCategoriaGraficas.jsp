<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>
	<bean:define id="initial" ><%= Constants.OBSERVATORY_GRAPHIC_INITIAL %></bean:define>
	<bean:define id="graphicParam" value="<%= Constants.GRAPHIC %>" />
	
	<bean:parameter id="id" name="<%=Constants.ID %>"/>
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="observatoryType" name="<%= Constants.TYPE_OBSERVATORY %>" />
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${id}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
	<c:set target="${params}" property="Otype" value="${observatoryType}" />
	<c:set target="${params}" property="${graphicParam}" value="${initial}" />
	
	<bean:define id="forward" value="" />

		<bean:define id="forward" value="<%= Constants.OBSERVATORY_GRAPHIC_INTAV %>" />

	<logic:equal name="observatoryType" value="<%= idCartridgeLenox %>">
		<bean:define id="forward" value="<%= Constants.OBSERVATORY_GRAPHIC_LENOX %>" />
	</logic:equal>
	<logic:equal name="observatoryType" value="<%= idCartridgeMultilanguage %>">
		<bean:define id="forward" value="<%= Constants.OBSERVATORY_GRAPHIC_MULTILANGUAGE %>" />
	</logic:equal>
			
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<html:link forward="getFulfilledObservatories" name="params"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
		<html:link forward="getObservatoryGraphic" name="params"><bean:message key="migas.indice.observatorios.menu.graficas"/></html:link>/
		<bean:message key="migas.indice.observatorios.menu.categorias.graficas"/>
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"> <bean:message key="indice.rastreo.gestion.graficas.categorias"/> </h1>
	
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="indice.rastreo.graficas.categorias" /></h2>
							
														
							<logic:notEmpty name="<%= Constants.CATEGORIES_LIST %>">
								<div class="graphicIcon1Row">
									<ul>
										<logic:iterate name="<%= Constants.CATEGORIES_LIST %>" id="category">
											<c:set target="${params}" property="${graphicParam}" value="${category.id}" />
											<li><html:link forward="<%= forward %>" name="params"><bean:write name="category" property="name" /></html:link></li>
										</logic:iterate>
									</ul>
								</div>
							</logic:notEmpty>
							<logic:empty name="<%= Constants.CATEGORIES_LIST %>">
								<div class="notaInformativaExito">
									<p><bean:message key="indice.categorias.vacias"/></p>
								</div>
							</logic:empty>
							
							<p id="pCenter">
								<c:set target="${params}" property="${graphicParam}" value="${initial}" />
								<html:link forward="getObservatoryGraphic" name="params" styleClass="boton"> <bean:message key="boton.volver"/> </html:link>
							</p>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
</inteco:sesion>