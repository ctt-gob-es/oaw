<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>

		<bean:define id="categoryName"><bean:write name="<%= Constants.CATEGORY_NAME %>" /></bean:define>
		<bean:parameter id="idCat" name="<%= Constants.ID_CATEGORIA %>"/>
		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		
		<h1><bean:message key="ob.resAnon.intav.report.chapterCat3.title" /></h1>
		
		<bean:define id="p1Bold"><strong><bean:write name="<%= Constants.CATEGORY_NAME %>" /></strong></bean:define>
		<p><bean:message key="ob.resAnon.intav.report.Cat3.p1" arg0="<%= p1Bold %>"/></p>
		<p><bean:message key="ob.resAnon.intav.report.Cat3.p2.html" /></p>
		
		<h2><bean:message key="ob.resAnon.intav.report.Cat3.sub1" /></h2>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.verification.mid.comparation.level.1.name"/><%= idCat %>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.verification.mid.comparation.level.1.title" /> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="ob.resAnon.intav.report.Cat3.tableTitle1" arg0="<%= categoryName %>" /></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.puntuacion.verificacion" /></th>
						<th><bean:message key="resultados.anonimos.punt.media" /></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI %>">
						<tr>
							<td  class="izda"><bean:write name="item" property="label" /></td>
							<td><bean:write name="item" property="value" /></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
		
		<h2><bean:message key="ob.resAnon.intav.report.Cat3.sub2" /></h2>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.verification.mid.comparation.level.2.name"/><%= idCat %>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.verification.mid.comparation.level.2.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="ob.resAnon.intav.report.Cat3.tableTitle2" arg0="<%= categoryName %>" /></span></caption>
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
