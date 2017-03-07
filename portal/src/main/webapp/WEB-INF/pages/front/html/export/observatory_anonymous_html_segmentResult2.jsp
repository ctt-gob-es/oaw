<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>

		<bean:define id="categoryName"><bean:write name="<%= Constants.CATEGORY_NAME %>" /></bean:define>
		<bean:parameter id="idCat" name="<%= Constants.ID_CATEGORIA %>"/>
		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		
		<h1><bean:message key="ob.resAnon.intav.report.chapterCat2.title" /></h1>
		
		<p><bean:message key="ob.resAnon.intav.report.Cat2.p1" /></p>
		<p><bean:message key="ob.resAnon.intav.report.Cat2.p12.html" /></p>
		<p><bean:message key="ob.resAnon.intav.report.Cat2.p13" /></p>
		
		<bean:define id="rutaImg">./img/graficas/<bean:message key="observatory.graphic.mark.allocation.segment.name" arg0="<%= idCat %>"/>.jpg</bean:define>
		
		<p class="imagen"><img src="<bean:write name="rutaImg" />" alt="" /></p>
		
		<p><bean:message key="ob.resAnon.intav.report.Cat2.p15" /></p>
		<p><bean:message key="ob.resAnon.intav.report.Cat2.p16" /></p>
		<p><bean:message key="ob.resAnon.intav.report.Cat2.p17" /></p>
		<p><bean:message key="ob.resAnon.intav.report.Cat2.p18" /></p>
	</div>
</div>
