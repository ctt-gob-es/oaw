<%@ include file="/common/taglibs.jsp" %>
<html:xhtml/>

<logic:messagesPresent message="false">
	<div class="error">
		<ul>
			<html:messages id="error" message="false">
				<li><bean:write name="error"/></li>
			</html:messages>
		</ul>
	</div>
</logic:messagesPresent>

<logic:messagesPresent message="true">
	<div class="info">
		<ul>
			<html:messages id="error" message="true">
				<li><bean:write name="error"/></li>
			</html:messages>
		</ul>
	</div>
</logic:messagesPresent>

<% request.getSession().removeAttribute("org.apache.struts.action.ACTION_MESSAGE");%>