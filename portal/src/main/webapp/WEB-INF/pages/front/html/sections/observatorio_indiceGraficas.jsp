<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">
	
	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>

	<bean:parameter id="id" name="<%=Constants.ID %>"/>
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="observatoryType" name="<%= Constants.TYPE_OBSERVATORY %>" />
	
	<bean:define id="graphicParam" value="<%= Constants.GRAPHIC %>" />
	<bean:define id="global" value="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL %>" />
	<bean:define id="comparative" value="<%= Constants.OBSERVATORY_GRAPHIC_COMPARATIVE %>" />
	<bean:define id="categories" value="<%= Constants.OBSERVATORY_GRAPHIC_CATEGORIES %>" />
	<bean:define id="initial" ><%= Constants.OBSERVATORY_GRAPHIC_INITIAL %></bean:define>
	
	<bean:define id="forward" value="" />

		<bean:define id="forward" value="<%= Constants.OBSERVATORY_GRAPHIC_INTAV %>" />

	<logic:equal name="observatoryType" value="<%= idCartridgeLenox %>">
		<bean:define id="forward" value="<%= Constants.OBSERVATORY_GRAPHIC_LENOX %>" />
	</logic:equal>
	<logic:equal name="observatoryType" value="<%= idCartridgeMultilanguage %>">
		<bean:define id="forward" value="<%= Constants.OBSERVATORY_GRAPHIC_MULTILANGUAGE %>" />
	</logic:equal>
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${id}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
	<c:set target="${params}" property="Otype" value="${observatoryType}" />
			
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<html:link forward="getFulfilledObservatories" name="params"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
		<bean:message key="migas.indice.observatorios.menu.graficas"/>
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2><bean:message key="indice.rastreo.graficas" /></h2>
							
							<div class="graphicIcon1Row">
								<c:set target="${params}" property="${graphicParam}" value="${categories}" />
								<div class="graphicIcon">
									<html:link forward="<%= forward %>" name="params">
										<img src="../images/segmento1.gif" alt=""/>
										<p><bean:message key="resultados.anonimos.img.categorias"/></p>
									</html:link>
								</div>
								<c:set target="${params}" property="${graphicParam}" value="${global}" />
								<div class="graphicIcon">
									<html:link forward="<%= forward %>" name="params">
										<img src="../images/resultados_globales.gif" alt=""/>
										<p><bean:message key="resultados.anonimos.img.globales"/></p>
									</html:link>
								</div>
								<c:set target="${params}" property="${graphicParam}" value="${comparative}" />
								<div class="graphicIcon">	
									<html:link forward="<%= forward %>" name="params">
										<img src="../images/evolucion.gif" alt=""/>
										<p><bean:message key="resultados.anonimos.img.comparativa"/></p>
									</html:link>
								</div>
								<div class="spacer"></div>
							</div>
							<c:set target="${params}" property="${graphicParam}" value="${initilal}" />
							<p id="pCenter"><html:link forward="getFulfilledObservatories" name="params" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
</inteco:sesion>