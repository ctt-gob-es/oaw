<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<div id="content">
	<jsp:include page="observatory_anonymous_html_menu.jsp"/>
	<div id="contenido">
		<jsp:include page="observatory_anonymous_html_breadCoumbs.jsp" />
		
		<h1><bean:write name="<%= Constants.SECTION_FORM %>" property="title"/></h1>
		
		<logic:iterate id="object" name="<%= Constants.SECTION_FORM %>" property="objectList" type="java.lang.Object">
			<logic:equal name="object" property="type" value="<%= Constants.OBJECT_TYPE_PARAGRAPH %>">
				<bean:write name="object" property="paragraph" filter="false"/>
			</logic:equal>
			
			<logic:equal name="object" property="type" value="<%= Constants.OBJECT_TYPE_SECTION %>">
				<logic:equal name="<%= Constants.SECTION_FORM %>" property="finalSectionToPaint" value="<%= String.valueOf(0)%>">
					<h2><bean:write name="object" property="title"/></h2>
					<logic:iterate id="subObject" name="object" property="objectList">
						<logic:equal name="subObject" property="type" value="<%= Constants.OBJECT_TYPE_PARAGRAPH %>">
							<bean:write name="subObject" property="paragraph" filter="false"/>
						</logic:equal>
					</logic:iterate>
				</logic:equal>
				<logic:notEmpty name="<%= Constants.SECTION_FORM %>" property="finalSectionToPaint">
					<logic:notEqual name="<%= Constants.SECTION_FORM %>" property="finalSectionToPaint" value="<%= String.valueOf(0)%>">
						<bean:define id="lastSection" name="<%= Constants.SECTION_FORM %>" property="finalSectionToPaint"/>
						<bean:define id="sectionNumber" name="object" property="sectionNumber"/>
						<bean:define id="sectionNumber"><%= sectionNumber.toString() %></bean:define>
						<logic:lessEqual name="sectionNumber" value="<%= lastSection.toString() %>">
							<h2><bean:write name="object" property="title"/></h2>
							<logic:iterate id="subObject" name="object" property="objectList">
								<logic:equal name="subObject" property="type" value="<%= Constants.OBJECT_TYPE_PARAGRAPH %>">
									<bean:write name="subObject" property="paragraph" filter="false"/>
								</logic:equal>
							</logic:iterate>
						</logic:lessEqual>
					</logic:notEqual>
				</logic:notEmpty>
			</logic:equal>
		</logic:iterate>
	</div>
</div>