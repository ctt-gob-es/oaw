<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>

		<bean:define id="categoryName"><bean:write name="<%= Constants.CATEGORY_NAME %>" /></bean:define>
		<bean:parameter id="idCat" name="<%= Constants.ID_CATEGORIA %>"/>
		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		
		<h1><bean:message key="ob.resAnon.intav.report.chapterCat4.title" /></h1>
		
		<p><bean:message key="ob.resAnon.intav.report.Cat4.p1"/></p>
		<p><bean:message key="ob.resAnon.intav.report.Cat4.p2" /></p>
		<p><bean:message key="ob.resAnon.intav.report.Cat4.p3" /></p>
		
		<h2><bean:message key="ob.resAnon.intav.report.Cat4.sub1" /></h2>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.modality.by.verification.level.1.name"/><%= idCat %>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.modality.by.verification.level.1.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="ob.resAnon.intav.report.Cat4.tableTitle1" arg0="<%= categoryName %>"/></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.puntuacion.verificacion" /></th>
						<th><bean:message key="resultados.anonimos.porc.pasa" /></th>
						<th><bean:message key="resultados.anonimos.porc.falla" /></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I %>">
						<tr>
							<td class="izda">
								<bean:message>
									<jsp:attribute name="key">
										<bean:write name="item" property="verification" />
									</jsp:attribute>
								</bean:message>
							</td>
							<td><bean:write name="item" property="greenPercentage" /></td>
							<td><bean:write name="item" property="redPercentage" /></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
		
		<h2><bean:message key="ob.resAnon.intav.report.Cat4.sub2" /></h2>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.modality.by.verification.level.2.name"/><%= idCat %>.jpg</bean:define>
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<div class="tabla">
			<bean:define id="graphName"> <bean:message key="observatory.graphic.modality.by.verification.level.2.title"/> </bean:define>
			<table summary="<bean:message key="anonymous.html.summary.table" arg0="<%= graphName %>" />">
				<caption><span><bean:message key="ob.resAnon.intav.report.Cat4.tableTitle2" arg0="<%= categoryName %>"/></span></caption>
				<thead>
					<tr>
						<th><bean:message key="resultados.anonimos.puntuacion.verificacion" /></th>
						<th><bean:message key="resultados.anonimos.porc.pasa" /></th>
						<th><bean:message key="resultados.anonimos.porc.falla" /></th>
					</tr>
				</thead>
				<tbody>
					<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II %>">
						<tr>
							<td class="izda">
								<bean:message>
									<jsp:attribute name="key">
										<bean:write name="item" property="verification" />
									</jsp:attribute>
								</bean:message>
							</td>
							<td><bean:write name="item" property="greenPercentage" /></td>
							<td><bean:write name="item" property="redPercentage" /></td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</div>
		
	</div>
</div>
