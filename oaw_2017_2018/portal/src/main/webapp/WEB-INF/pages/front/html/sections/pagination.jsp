<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<bean:size id="linksSize" name="<%= Constants.LIST_PAGE_LINKS %>"/>
<logic:greaterThan value="1" name="linksSize">
	<p>
		<logic:iterate id="linkItem" name="<%= Constants.LIST_PAGE_LINKS %>">
		<logic:equal name="linkItem" property="active" value="true">
			<a href="<bean:write name="linkItem" property="path" />" class="<bean:write name="linkItem" property="styleClass" /> btn btn-default"><bean:write name="linkItem" property="title" /></a>
			</logic:equal>
			<logic:equal name="linkItem" property="active" value="false">
			<span class="<bean:write name="linkItem" property="styleClass" /> btn"><bean:write name="linkItem" property="title" /></span>
			</logic:equal>
		</logic:iterate>
	</p>
</logic:greaterThan>
