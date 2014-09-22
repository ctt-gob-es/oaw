<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>

<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>
	<bean:define id="observatoryType" name="<%= Constants.OBSERVATORY_T %>" type="java.lang.String"/>
	
	<div id="contenido">
		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		<h1><bean:message key="ob.resAnon.intav.report.chapterEv.title" /></h1>
		
		<p><bean:message key="ob.resAnon.intav.report.Ev.p1" /></p>
		<p><bean:message key="ob.resAnon.intav.report.Ev.p2" /></p>
		<p><bean:message key="ob.resAnon.intav.report.Ev.p3" /></p>
	</div>	
</div>