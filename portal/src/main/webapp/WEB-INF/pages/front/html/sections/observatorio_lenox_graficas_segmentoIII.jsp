<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:parameter id="id" name="<%=Constants.ID %>"/>
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="observatoryType" name="<%=Constants.TYPE_OBSERVATORY %>"/>
	
	<bean:define id="grParam" ><%= Constants.GRAPHIC %></bean:define>
	<bean:define id="grValue" ><%= Constants.OBSERVATORY_GRAPHIC_INITIAL %></bean:define>
	<bean:define id="grRegenerate" ><%= Constants.OBSERVATORY_GRAPHIC_SEGMENT_III %></bean:define>
	
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
		<bean:message key="migas.indice.observatorios.menu.graficas.segmento3"/>
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2><bean:message key="indice.observatorios.menu.graficas.segmento3" /></h2>
							
							<jsp:include page="/common/crawler_messages.jsp" />
							<logic:equal name="<%= Constants.OBSERVATORY_RESULTS %>" value="<%= Constants.SI %>">
								<logic:notEmpty name="<%= Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_TERMS %>">
									<h3><bean:message key="resultados.anonimos.lenox.porcentaje.prioridad.terminos.s3.title" /></h3>
									<div class="graphicInfo2">
										<strong><bean:message key="resultados.anonimos.porcentaje.terminos"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_TERMS %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_SEG3 %>"></img></div>
								</logic:notEmpty>
								<logic:empty name="<%= Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_TERMS %>">
									<bean:message key="resultados.observatorio.vista.primaria.vacio"/>
								</logic:empty>
							</logic:equal>
							<p id="pCenter">
								<html:link forward="getObservatoryGraphic" name="params" styleClass="boton"> <bean:message key="boton.volver"/> </html:link>
								<c:set target="${params}" property="${grParam}" value="${grRegenerate}" />
								<html:link forward="regenerateGraphicLenox" name="params" styleClass="boton"> <bean:message key="boton.regenerar.resultados"/> </html:link>
							</p>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
</inteco:sesion>