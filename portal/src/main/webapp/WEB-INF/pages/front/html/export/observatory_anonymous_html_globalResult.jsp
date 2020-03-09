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

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>
	<bean:define id="observatoryType" name="<%= Constants.OBSERVATORY_T %>" type="java.lang.String"/>
	

		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		<h1><bean:message key="ob.resAnon.intav.report.chapter4.title" /></h1>
		
		<p><bean:message key="ob.resAnon.intav.report.4.p1" /></p>
		<p><bean:message key="ob.resAnon.intav.report.4.p2" /></p>
		
		<h2><bean:message key="observatory.graphic.accessibility.level.allocation.title" /></h2>
		
		<logic:equal name="observatoryType" value="<%= String.valueOf(Constants.OBSERVATORY_TYPE_AGE) %>">
			<p><bean:message key="ob.resAnon.intav.report.41.p1.AGE" /></p>
		</logic:equal>
		
		<logic:equal name="observatoryType" value="<%= String.valueOf(Constants.OBSERVATORY_TYPE_CCAA) %>">
			<p><bean:message key="ob.resAnon.intav.report.41.p1.CCAA" /></p>
		</logic:equal>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.accessibility.level.allocation.name"/>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.accessibility.level.allocation.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="anonymous.html.global.result.1.table.caption" /></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.level" /></th>
						<th><bean:message key="resultados.anonimos.porc.portales" /></th>
						<th><bean:message key="resultados.anonimos.num.portales" /></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG %>">
						<tr>
							<td><bean:write name="item" property="adecuationLevel" /></td>
							<td><bean:write name="item" property="percentageP" /></td>
							<td><bean:write name="item" property="numberP" /></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
	</div>
</div>
