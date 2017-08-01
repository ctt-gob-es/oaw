<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>


<%@page import="java.net.URLDecoder"%><bean:define id="observatoryName"><bean:write name="<%= Constants.OBSERVATORY_NAME %>" /></bean:define>
<bean:define id="observatoryDate"><bean:write name="<%= Constants.OBSERVATORY_DATE %>" /></bean:define>
  
<meta http-equiv="content-Type" content="text/html; charset=UTF-8" />
<meta name="description" content="<bean:message key="anonymous.html.description" arg0="<%=observatoryDate%>"/>" />
<meta name="keywords" content="<bean:message key="anonymous.html.keywords" />" />
<meta name="build" content="v3.5.0" />
<link rel="stylesheet" type="text/css" href="./styles/estilo_html.css" media="all" />
<title><bean:message key="anonymous.html.head.title" arg0="<%= observatoryName %>"/></title>

 
