<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>

<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>

		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		
		<h1><bean:message key="observatory.graphic.aspect.mid.title" /></h1>
		
		<p><bean:message key="ob.resAnon.intav.report.46.p12.html" /></p>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.aspect.mid.name"/>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.aspect.mid.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="anonymous.html.global.result.6.table.2.caption" /></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.aspect"/></th>
						<th><bean:message key="resultados.anonimos.punt.media"/></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA %>">
						<tr>
							<td class="izda"><bean:write name="item" property="label" /></td>
							<td><bean:write name="item" property="value"/></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
	</div>
</div>
