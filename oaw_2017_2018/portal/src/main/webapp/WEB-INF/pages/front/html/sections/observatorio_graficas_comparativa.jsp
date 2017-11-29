<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
This program is licensed and may be used, modified and redistributed under the terms
of the European Public License (EUPL), either version 1.2 or (at your option) any later 
version as soon as they are approved by the European Commission.
Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND, either express or implied. See the License for the specific language governing 
permissions and more details.
You should have received a copy of the EUPL1.2 license along with this program; if not, 
you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
-->
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
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2><bean:message key="indice.observatorios.menu.graficas.evolucion" /></h2>
								<jsp:include page="/common/crawler_messages.jsp" />
								<logic:equal name="<%= Constants.OBSERVATORY_RESULTS %>" value="<%= Constants.SI %>">
									<h3>1. <bean:message key="resultados.anonimos.nivel.comformidad.accesibilidad.aa.title" /></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.porc.portales"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AA %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.porc.portales"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AA %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_AA %>"></img></div>
									
									<h3>2. <bean:message key="resultados.anonimos.nivel.comformidad.accesibilidad.a.title" /></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.porc.portales"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_A %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.porc.portales"/></strong>
											<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_A %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol>--%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_A %>"></img></div>
									
									<h3>3. <bean:message key="resultados.anonimos.nivel.comformidad.accesibilidad.nv.title" /></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.porc.portales"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_NV %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.porc.portales"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_NV %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_NV %>"></img></div>
									
									<h3>4. <bean:message key="resultados.anonimos.puntuacion.media.observatorio.title" /></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_MID_PUNT %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_MID_PUNT %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_MID_MARK %>"></img></div>
									
									<h3>5. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.1.1"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V111 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V111 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION %>"></img></div>
									
									<h3>6. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.1.2"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V112 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V112 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION %>"></img></div>
									
									<h3>7. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.1.3"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V113 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V113 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION %>"></img></div>
									
									<h3>8. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.1.4"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V114 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V114 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION %>"></img></div>
									
									<h3>9. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.2.1"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V121 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V121 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION %>"></img></div>
									
									<h3>10.<bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.2.2"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V122 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V122 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION %>"></img></div>
									
									<h3>11. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.2.3"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V123 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V123 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION %>"></img></div>
									
									<h3>12. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.2.4"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V124 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V124 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION %>"></img></div>
									
									<h3>13. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.2.5"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V125 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V125 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION %>"></img></div>
									
									<h3>14. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 1.2.6"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V126 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V126 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION %>"></img></div>
									
									<h3>15. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.1.1"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V211 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V211 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION %>"></img></div>
									
									<h3>16. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.1.2"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V212 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V212 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION %>"></img></div>
									
									<h3>17. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.1.3"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V213 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V213 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION %>"></img></div>
									
									<h3>18. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.1.4"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V214 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V214 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION %>"></img></div>
									
									<h3>19. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.2.1"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V221 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V221 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION %>"></img></div>
									
									<h3>20. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.2.2"/></h3>
									<div class="graphicInfo2">
									<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V222 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V222 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION %>"></img></div>
									
									<h3>21. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.2.3"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V223 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V223 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION %>"></img></div>
									
									<h3>22. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.2.4"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V224 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V224 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION %>"></img></div>
									
									<h3>23. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.2.5"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V225 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V225 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION %>"></img></div>
									
									<h3>24. <bean:message key="resultados.anonimos.puntuacion.media.verificacion.title" arg0=" 2.2.6"/></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V226 %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V226 %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_VERIFICATION %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION %>"></img></div>
							
									<h3>25. <bean:message key="resultados.anonimos.puntuacion.media.aspecto.title">
											<jsp:attribute name="arg0">
												<bean:message key="observatory.aspect.general"/>
											</jsp:attribute>
										</bean:message></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AG %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AG %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_ASPECT %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID %>"></img></div>
									
									<h3>26. <bean:message key="resultados.anonimos.puntuacion.media.aspecto.title">
											<jsp:attribute name="arg0">
												<bean:message key="observatory.aspect.presentation"/>
											</jsp:attribute>
										</bean:message></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AP %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AP %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_ASPECT %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID %>"></img></div>
									
									<h3>27. <bean:message key="resultados.anonimos.puntuacion.media.aspecto.title">
											<jsp:attribute name="arg0">
												<bean:message key="observatory.aspect.structure"/>
											</jsp:attribute>
										</bean:message></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AE %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AE %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_ASPECT %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID %>"></img></div>
									
									<h3>28. <bean:message key="resultados.anonimos.puntuacion.media.aspecto.title">
											<jsp:attribute name="arg0">
												<bean:message key="observatory.aspect.navigation"/>
											</jsp:attribute>
										</bean:message></h3>
									<div class="graphicInfo2">
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AN %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AN %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_ASPECT %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID %>"></img></div>
									
									<h3>29. <bean:message key="resultados.anonimos.puntuacion.media.aspecto.title">
											<jsp:attribute name="arg0">
												<bean:message key="observatory.aspect.alternatives"/>
											</jsp:attribute>
										</bean:message></h3>
									<div class="graphicInfo2">	
										<table>
											<tr>
												<th><bean:message key="resultados.anonimos.date"/></th>
												<th><bean:message key="resultados.anonimos.punt.media"/></th>
											</tr>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AAL %>">
												<tr>
													<td><bean:write name="item" property="label" /></td>
													<td><bean:write name="item" property="value"/></td>
												</tr>
											</logic:iterate>
										</table>
										<%-- %><strong><bean:message key="resultados.anonimos.punt.media"/></strong>
										<ol>
											<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AAL %>">
												<li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
											</logic:iterate>
										</ol> --%>
									</div>
									<div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC_TYPE %>=<%= Constants.GRAPHIC_ASPECT %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID %>"></img></div>
									
								</logic:equal>
								<p id="pCenter">
									<html:link forward="getObservatoryGraphic" name="params" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link>
									<c:set target="${params}" property="${grParam}" value="${grRegenerate}" />
									<html:link forward="regenerateGraphicIntav" name="params" styleClass="boton"> <bean:message key="boton.regenerar.resultados"/> </html:link>
								</p>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
</inteco:sesion>
