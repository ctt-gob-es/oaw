<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>


<%@page import="java.net.URLDecoder"%>
<bean:define id="observatoryName"><bean:write name="<%= Constants.OBSERVATORY_NAME %>" /></bean:define>
<bean:define id="observatoryDate"><bean:write name="<%= Constants.OBSERVATORY_DATE %>" /></bean:define>

<div id="footer">
	<p><bean:message key="anonymous.html.footer" arg0="<%= observatoryName %>" arg1="<%= observatoryDate %>"/></p>
</div>
