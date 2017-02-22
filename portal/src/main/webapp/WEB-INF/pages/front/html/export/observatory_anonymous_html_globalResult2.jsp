<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>

<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>
	<bean:define id="observatoryType" name="<%= Constants.OBSERVATORY_T %>" type="java.lang.String"/>
	

		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		
		<h1><bean:message key="ob.resAnon.intav.report.chapter42.title" /></h1>
		
		<p><bean:message key="ob.resAnon.intav.report.42.p1" /></p>
		<p><bean:message key="ob.resAnon.intav.report.42.p2" /></p>
		<p><bean:message key="ob.resAnon.intav.report.42.p5" /></p>
		
		<logic:equal name="observatoryType" value="<%= String.valueOf(Constants.OBSERVATORY_TYPE_AGE) %>">
			<p><bean:message key="ob.resAnon.intav.report.42.p6" /></p>
		</logic:equal>
		
		<bean:define id="numCPS" name="<%= Constants.OBSERVATORY_NUM_CPS_GRAPH %>" type="java.lang.Integer"/>
		<c:forEach var="count" begin="1" end="${numCPS}">
			<bean:define id="countBean"><c:out value="${count}" /></bean:define>
			<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.global.puntuation.allocation.segments.mark.name"/><bean:write name="countBean"/>.jpg</bean:define>
			<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		</c:forEach>
		
		<logic:iterate name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS %>" id="item">
			<bean:define id="categoryForm" type="es.inteco.rastreador2.actionform.semillas.CategoriaForm" name="item" property="category" />
			<bean:define id="categoryName" name="categoryForm" property="name" type="java.lang.String" />
			<div class="tabla">
				<bean:define id="graphName"> <bean:message key="observatory.graphic.accessibility.level.allocation.title"/> </bean:define>
				<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
					<caption><span><bean:message key="ob.resAnon.intav.report.42.tableTitle" arg0="<%= categoryName %>" /></span></caption>
					<thead>
						<tr>
							<th><bean:message key="resultados.anonimos.level" /></th>
							<th><bean:message key="resultados.anonimos.porc.portales" /></th>
						</tr>
					</thead>
					<tbody>
						<logic:iterate id="item2" name="item" property="viewList">
							<tr>
								<td><bean:write name="item2" property="label" /></td>
								<td><bean:write name="item2" property="value" />%</td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</div>
		</logic:iterate> 
	</div>
</div>