<%@ taglib uri="/tld/inteco.tld" prefix="inteco" %>  
<%@ include file="/common/taglibs.jsp" %>   
<%@page import="es.inteco.common.Constants"%>

<div id="cabeza">
	<a href="http://www.minhap.gob.es/es-ES/Paginas/Home.aspx">
		<img src="../images/escudo.png" alt="<bean:message key="application.logo.alt" />" />
	</a>
</div> <!-- fin cabecera -->
<div class="language">
	<logic:notEmpty name="<%= Constants.LANGUAGE_LIST %>">
		<logic:iterate id="languages" name="<%= Constants.LANGUAGE_LIST %>">
			<bean:define id="language"><bean:write name="languages" property="codice"/></bean:define>
			<bean:define id="key"><bean:write name="languages" property="keyName"/></bean:define>
			<html:link forward="language" paramId="<%= Constants.LOCALE %>" paramName="language"><bean:message name="key"/></html:link>
		</logic:iterate>
	</logic:notEmpty>
</div>


	