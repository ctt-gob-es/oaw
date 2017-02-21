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
	<bean:define id="grRegenerate" ><bean:write name="segment"/></bean:define>
	
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
		<bean:message key="migas.indice.observatorios.menu.graficas.segmento1"/>
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
							<h2 class="config"><bean:message key="indice.observatorios.menu.graficas.segmento1" /></h2>
							
							<jsp:include page="/common/crawler_messages.jsp" />
							
							<logic:equal name="<%= Constants.OBSERVATORY_RESULTS %>" value="<%= Constants.SI %>">
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.lang.home.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language"/></th>
											<th><bean:message key="resultados.primarios.porc.paginas"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_LANG_HOME_INFO %>" id="item">
											<tr>
												<td><bean:write name="item" property="label" /></td>
												<td><bean:write name="item" property="value"/></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_HOME %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.lang.inpg.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language"/></th>
											<th><bean:message key="resultados.primarios.porc.paginas"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_LANG_INPG_INFO %>" id="item">
											<tr>
												<td><bean:write name="item" property="label" /></td>
												<td><bean:write name="item" property="value"/></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_INTERNAL %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.correct.link.home.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language.lang"/></th>
											<th><bean:message key="resultados.anonimos.language.noT"/></th>
											<th><bean:message key="resultados.anonimos.language.noCorrectT"/></th>
											<th><bean:message key="resultados.anonimos.language.correctT"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_HOME_LINK_TR_INFO %>" id="item">
											<bean:define id="noT"><bean:write name="item" property="noTranslationPercentage"/></bean:define>
											<bean:define id="bT"><bean:write name="item" property="correctTranslationPercentage"/></bean:define>
											<bean:define id="mT"><bean:write name="item" property="noCorrectTranslationPercentage"/></bean:define>
											<tr>
												<td><bean:write name="item" property="name" /></td>
												<td><%= noT.toString().replace(".00", "") + "%" %></td>
												<td><%= mT.toString().replace(".00", "") + "%" %></td>
												<td><%= bT.toString().replace(".00", "") + "%" %></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_CORRECT_LINK_HOME %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.correct.link.inpg.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language.lang"/></th>
											<th><bean:message key="resultados.anonimos.language.noT"/></th>
											<th><bean:message key="resultados.anonimos.language.noCorrectT"/></th>
											<th><bean:message key="resultados.anonimos.language.correctT"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_INPG_LINK_TR_INFO %>" id="item">
											<bean:define id="noT"><bean:write name="item" property="noTranslationPercentage"/></bean:define>
											<bean:define id="bT"><bean:write name="item" property="correctTranslationPercentage"/></bean:define>
											<bean:define id="mT"><bean:write name="item" property="noCorrectTranslationPercentage"/></bean:define>
											<tr>
												<td><bean:write name="item" property="name" /></td>
												<td><%= noT.toString().replace(".00", "") + "%" %></td>
												<td><%= mT.toString().replace(".00", "") + "%" %></td>
												<td><%= bT.toString().replace(".00", "") + "%" %></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_CORRECT_LINK_INTERNAL %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.total.home.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.valid"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.invalid"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_HOME_VAL_INFO %>" id="item">
											<tr>
												<td><bean:write name="item" property="language" /></td>
												<td><bean:write name="item" property="greenPercentage"/></td>
												<td><bean:write name="item" property="redPercentage"/></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_HOME %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.total.inpg.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.valid"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.invalid"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_INPG_VAL_INFO %>" id="item">
											<tr>
												<td><bean:write name="item" property="language" /></td>
												<td><bean:write name="item" property="greenPercentage"/></td>
												<td><bean:write name="item" property="redPercentage"/></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_INTERNAL %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.dec.home.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.valid"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.invalid"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_HOME_DEC_INFO %>" id="item">
											<tr>
												<td><bean:write name="item" property="language" /></td>
												<td><bean:write name="item" property="greenPercentage"/></td>
												<td><bean:write name="item" property="redPercentage"/></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_DEC_HOME %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.dec.inpg.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.valid"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.invalid"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_INPG_DEC_INFO%>" id="item">
											<tr>
												<td><bean:write name="item" property="language" /></td>
												<td><bean:write name="item" property="greenPercentage"/></td>
												<td><bean:write name="item" property="redPercentage"/></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_DEC_INTERNAL %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.tr.home.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.valid"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.invalid"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_HOME_TR_INFO %>" id="item">
											<tr>
												<td><bean:write name="item" property="language" /></td>
												<td><bean:write name="item" property="greenPercentage"/></td>
												<td><bean:write name="item" property="redPercentage"/></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_TR_HOME %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
								
								<h3><bean:message key="resultados.anonimos.multilanguage.gl.tr.inpg.title" /></h3>
								<div class="graphicInfo2">
									<table>
										<caption></caption>
										<tr>
											<th><bean:message key="resultados.anonimos.language"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.valid"/></th>
											<th><bean:message key="resultados.anonimos.porc.paginas.invalid"/></th>
										</tr>
										<logic:iterate name="<%= Constants.MULTILANGUAGE_INPG_TR_INFO %>" id="item">
											<tr>
												<td><bean:write name="item" property="language" /></td>
												<td><bean:write name="item" property="greenPercentage"/></td>
												<td><bean:write name="item" property="redPercentage"/></td>
											</tr>
										</logic:iterate>
									</table>
								</div>
								<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_TR_INTERNAL %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%=segment %>"/></div>
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