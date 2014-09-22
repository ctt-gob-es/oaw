<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<inteco:sesion action="ifConfigAdmin">

	<bean:parameter id="id" name="<%=Constants.ID %>"/>
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="observatoryType" name="<%=Constants.TYPE_OBSERVATORY %>"/>
	<bean:parameter id="segment" name="<%=Constants.GRAPHIC %>"/>
	
	<bean:define id="grParam" ><%= Constants.GRAPHIC %></bean:define>
	<bean:define id="grValue" ><%= Constants.OBSERVATORY_GRAPHIC_INITIAL %></bean:define>
	<bean:define id="grRegenerate" name="segment"/>
	<bean:define id="categories" ><%= Constants.OBSERVATORY_GRAPHIC_CATEGORIES %></bean:define>
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${id}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
	<c:set target="${params}" property="Otype" value="${observatoryType}" />
						
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<html:link forward="getFulfilledObservatories" name="params"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
		<c:set target="${params}" property="${grParam}" value="${grValue}" />
		<html:link forward="getObservatoryGraphic" name="params"><bean:message key="migas.indice.observatorios.menu.graficas"/></html:link> /
		<c:set target="${params}" property="${grParam}" value="${categories}" />
		<html:link forward="listCategory" name="params"><bean:message key="migas.indice.observatorios.menu.categorias.graficas"/></html:link> /
		<bean:message key="migas.indice.observatorios.menu.graficas.segmento1"/>
		</p>
	</div>
	
	
	<c:set target="${params}" property="${grParam}" value="${grValue}" />
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"> <bean:message key="indice.rastreo.gestion.graficas"/> </h1>
	
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="indice.observatorios.menu.graficas.segmento1" /></h2>
							
							<jsp:include page="/common/crawler_messages.jsp" />
							<logic:equal name="<%= Constants.OBSERVATORY_RESULTS %>" value="<%= Constants.SI %>">
								<h3><bean:message key="resultados.anonimos.nivel.accesibilidad.segmento.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<tr>
											<th><bean:message key="resultados.anonimos.level"/></th>
											<th><bean:message key="resultados.anonimos.porc.portales"/></th>
											<th><bean:message key="resultados.anonimos.num.portales"/></th>
										</tr>
										<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG %>">
											<tr>
												<td><bean:write name="item" property="adecuationLevel" /></td>
												<td><bean:write name="item" property="percentageP"/></td>
												<td><bean:write name="item" property="numberP"/></td>
											</tr>
										</logic:iterate>
									</table>
									<%-- %><strong><bean:message key="resultados.anonimos.porc.portales" /></strong>
									<ol>
										<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG %>">
											<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
										</logic:iterate>
									</ol> --%>
								</div>
								<img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_S %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/>
								
								<h3><bean:message key="resultados.anonimos.puntuaciones.segmento.title" /></h3>
								<div class="graphicInfo2">
									<logic:present name="<%= Constants.OBSERVATORY_GRAPHIC_SEGMENT_DATA_LIST_DP %>">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.level"/></th>
												<th><bean:message key="resultados.anonimos.num.portales"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_SEGMENT_DATA_LIST_DP %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
									</logic:present>
									<%-- %><strong><bean:message key="resultados.anonimos.num.portales"/></strong>
									<ol>
										<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_SEGMENT_DATA_LIST_DP %>">
											<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
										</logic:iterate>
									</ol> --%>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MARK_ALLOCATION_S %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"></img></div>
							
								<h3><bean:message key="resultados.anonimos.comp.medias.verificacion.1.segmento.title" /></h3>
								<div class="graphicInfo2">
									<logic:present name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI %>">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.punto.verification"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
									</logic:present>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N1_S %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"></img></div>
								
								<h3><bean:message key="resultados.anonimos.comp.medias.verificacion.2.segmento.title" /></h3>
								<div class="graphicInfo2">
									<logic:present name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII %>">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.punto.verification"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
									</logic:present>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N2_S %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"></img></div>
								
								<h3><bean:message key="resultados.anonimos.comp.modalidad.verificacion.1.segmento.title" /></h3>
								<div class="graphicInfo2">
									<logic:present name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I %>">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.punto.verification"/></th>
												<th><bean:message key="resultados.anonimos.porc.pasa"/></th>
												<th><bean:message key="resultados.anonimos.porc.falla"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I %>" type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
												<tr>
													<td><bean:message key="<%=item.getVerification() %>" /></td>
													<td><bean:write name="item" property="greenPercentage"/></td>
													<td><bean:write name="item" property="redPercentage"/></td>
												</tr>
											</logic:iterate>
										</table>
									</logic:present>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N1_S %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"></img></div>
								
								<h3><bean:message key="resultados.anonimos.comp.modalidad.verificacion.2.segmento.title" /></h3>
								<div class="graphicInfo2">
									<logic:present name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II %>">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.punto.verification"/></th>
												<th><bean:message key="resultados.anonimos.porc.pasa"/></th>
												<th><bean:message key="resultados.anonimos.porc.falla"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II %>" type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
												<tr>
													<td><bean:message key="<%=item.getVerification() %>" /></td>
													<td><bean:write name="item" property="greenPercentage"/></td>
													<td><bean:write name="item" property="redPercentage"/></td>
												</tr>
											</logic:iterate>
										</table>
									</logic:present>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N2_S %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"></img></div>
							
							</logic:equal>
							<p id="pCenter">
								<c:set target="${params}" property="${grParam}" value="${categories}" />
								<html:link forward="getObservatoryGraphic" name="params" styleClass="boton"> <bean:message key="boton.volver"/> </html:link>
								<c:set target="${params}" property="${grParam}" value="${grRegenerate}" />
								<html:link forward="regenerateGraphicIntav" name="params" styleClass="boton"> <bean:message key="boton.regenerar.resultados"/> </html:link>
							</p>
							
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
</inteco:sesion>