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
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>

<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>

		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		
		<h1><bean:message key="ob.resAnon.intav.report.chapter45.title" /></h1>
		
		<p><bean:message key="ob.resAnon.intav.report.45.p1" /></p>
		<p><bean:message key="ob.resAnon.intav.report.45.p2" /></p>
		<p><bean:message key="ob.resAnon.intav.report.45.p3" /></p>
		
		<h2><bean:message key="ob.resAnon.intav.report.chapter451.title" /></h2>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.modality.by.verification.level.1.name"/>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.modality.by.verification.level.1.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="anonymous.html.global.result.5.table.1.caption" /></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.puntuacion.verificacion" /></th>
						<th><bean:message key="resultados.anonimos.porc.pasa" /></th>
						<th><bean:message key="resultados.anonimos.porc.falla" /></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I %>" type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
						<tr>
							<td class="izda"><bean:message key="<%=item.getVerification() %>" /></td>
							<td><bean:write name="item" property="greenPercentage" /></td>
							<td><bean:write name="item" property="redPercentage" /></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
		
		<h2><bean:message key="ob.resAnon.intav.report.chapter452.title" /></h2>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.modality.by.verification.level.2.name"/>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.modality.by.verification.level.2.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="anonymous.html.global.result.5.table.2.caption" /></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.puntuacion.verificacion" /></th>
						<th><bean:message key="resultados.anonimos.porc.pasa" /></th>
						<th><bean:message key="resultados.anonimos.porc.falla" /></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II %>" type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
						<tr>
							<td class="izda"><bean:message key="<%=item.getVerification() %>" /></td>
							<td><bean:write name="item" property="greenPercentage" /></td>
							<td><bean:write name="item" property="redPercentage" /></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
	</div>
</div>
