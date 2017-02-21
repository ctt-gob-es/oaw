<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:parameter id="id" name="<%=Constants.ID %>"/>
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="observatoryType" name="<%=Constants.TYPE_OBSERVATORY %>"/>
	
	<bean:define id="grParam" ><%= Constants.GRAPHIC %></bean:define>
	<bean:define id="grValue" ><%= Constants.OBSERVATORY_GRAPHIC_INITIAL %></bean:define>
	<bean:define id="grRegenerate" ><%= Constants.OBSERVATORY_GRAPHIC_COMPARATIVE %></bean:define>
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${id}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
	<c:set target="${params}" property="${grParam}" value="${grValue}" />
	<c:set target="${params}" property="Otype" value="${observatoryType}" />
			
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<html:link forward="getFulfilledObservatories" name="params"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
		<html:link forward="getObservatoryGraphic" name="params"><bean:message key="migas.indice.observatorios.menu.graficas"/></html:link> /
		<bean:message key="migas.indice.observatorios.menu.graficas.evolucion"/>
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
							<h2 class="config"><bean:message key="indice.observatorios.menu.graficas.evolucion" /></h2>
								<jsp:include page="/common/crawler_messages.jsp" />
								<logic:equal name="<%= Constants.OBSERVATORY_RESULTS %>" value="<%= Constants.SI %>">
								
									<bean:message key="resultados.observatorio.vista.primaria.vacio"/>
									
								</logic:equal>
								<p id="pCenter">
									<html:link forward="getObservatoryGraphic" name="params" styleClass="boton"> <bean:message key="boton.volver"/> </html:link>
									<c:set target="${params}" property="${grParam}" value="${grRegenerate}" />
									<html:link forward="regenerateGraphicMultilanguage" name="params" styleClass="boton"> <bean:message key="boton.regenerar.resultados"/> </html:link>
								</p>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
</inteco:sesion>