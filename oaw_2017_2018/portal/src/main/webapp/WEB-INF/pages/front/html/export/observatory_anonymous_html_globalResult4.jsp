<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>

<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>

		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		
		<h1><bean:message key="ob.resAnon.intav.report.chapter44.title" /></h1>
		
		<p><bean:message key="ob.resAnon.intav.report.44.p1" /></p>
		<p><bean:message key="ob.resAnon.intav.report.44.p2" /></p>
		<p><bean:message key="ob.resAnon.intav.report.44.p7.html" /></p>
		<p><bean:message key="ob.resAnon.intav.report.44.p8" /></p>
		
		<h2><bean:message key="ob.resAnon.intav.report.chapter441.title" /></h2>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.verification.mid.comparation.level.1.name"/>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.verification.mid.comparation.level.1.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="anonymous.html.global.result.4.table.1.caption" /></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.puntuacion.verificacion" /></th>
						<th><bean:message key="resultados.anonimos.punt.media" /></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI %>">
						<tr>
							<td class="izda"><bean:write name="item" property="label" /></td>
							<td><bean:write name="item" property="value" /></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
		
		<h2><bean:message key="ob.resAnon.intav.report.chapter442.title" /></h2>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.verification.mid.comparation.level.2.name"/>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.verification.mid.comparation.level.2.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="anonymous.html.global.result.4.table.2.caption" /></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.puntuacion.verificacion" /></th>
						<th><bean:message key="resultados.anonimos.punt.media" /></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII %>">
						<tr>
							<td class="izda"><bean:write name="item" property="label" /></td>
							<td><bean:write name="item" property="value" /></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
		
	</div>
</div>
