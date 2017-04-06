<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<p id="migas">
	<bean:message key="migas.html.is.in"/>
	
	<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_INTRODUCTION %>">
		<bean:message key="migas.html.home" />
	</logic:equal>
	<logic:notEqual name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_INTRODUCTION %>">
		<html:link href="<%= Constants.INTRODUCTION_FILE %>"><bean:message key="migas.html.home" /></html:link> <%= Constants.BREAD_CRUMBS_SEPARATOR %>
	</logic:notEqual>
	
	<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_OBJECTIVE %>">
		<bean:message key="migas.html.objective" />
	</logic:equal>
	
	<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_METHODOLOGY %>">
		<logic:empty name="<%=Constants.HTML_SUBMENU %>">
			<bean:message key="migas.html.methodology" />
		</logic:empty>
		<logic:notEmpty name="<%=Constants.HTML_SUBMENU %>">
			<html:link href="<%= Constants.METHODOLOGY_FILE %>"><bean:message key="migas.html.methodology" /></html:link> <%= Constants.BREAD_CRUMBS_SEPARATOR %>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_1 %>">
				<bean:message key="migas.html.met1" />
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_2 %>">
				<bean:message key="migas.html.met2" />
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_3 %>">
				<bean:message key="migas.html.met3" />
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_4 %>">
				<bean:message key="migas.html.met4" />
			</logic:equal>
		</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS %>">
		<logic:empty name="<%=Constants.HTML_SUBMENU %>">
			<bean:message key="migas.html.global.results" />
		</logic:empty>
		<logic:notEmpty name="<%=Constants.HTML_SUBMENU %>">
			<html:link href="<%= Constants.GLOBAL_RESULTS_FILE %>"><bean:message key="migas.html.global.results" /></html:link> <%= Constants.BREAD_CRUMBS_SEPARATOR %>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_1 %>">
				<bean:message key="migas.html.global.results.1" />
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_2 %>">
				<bean:message key="migas.html.global.results.2" />
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_3 %>">
				<bean:message key="migas.html.global.results.3" />
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_4 %>">
				<bean:message key="migas.html.global.results.4" />
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_5 %>">
				<bean:message key="migas.html.global.results.5" />
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_6 %>">
				<bean:message key="migas.html.global.results.6" />
			</logic:equal>
		</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS %>">
		<bean:define id="categoryName"><bean:write name="<%= Constants.CATEGORY_NAME %>" /></bean:define>
		<bean:parameter id="idCat" name="<%= Constants.ID_CATEGORIA %>"/>
		<logic:empty name="<%=Constants.HTML_SUBMENU %>">
			<bean:message key="migas.html.segment.results" arg0="<%= categoryName %>"/>
		</logic:empty>
		<logic:notEmpty name="<%=Constants.HTML_SUBMENU %>">
			<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_1.replace(\"{0}\", idCat) %>" ><bean:message key="migas.html.segment.results" arg0="<%= categoryName %>" /></html:link> <%= Constants.BREAD_CRUMBS_SEPARATOR %>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_1 %>">
				<bean:message key="migas.html.segment.results.1"/>
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_2 %>">
				<bean:message key="migas.html.segment.results.2"/>
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_3 %>">
				<bean:message key="migas.html.segment.results.3"/>
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_4 %>">
				<bean:message key="migas.html.segment.results.4"/>
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_5 %>">
				<bean:message key="migas.html.segment.results.5"/>
			</logic:equal>
		</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_EVOLUTION %>">
		<logic:empty name="<%=Constants.HTML_SUBMENU %>">
			<bean:message key="migas.html.evolution.results" />
		</logic:empty>
		<logic:notEmpty name="<%=Constants.HTML_SUBMENU %>">
			<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE %>"><bean:message key="migas.html.evolution.results" /></html:link> <%= Constants.BREAD_CRUMBS_SEPARATOR %>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%= Constants.HTML_MENU_EVOLUTION_RESULTS_1 %>">
				<bean:message key="migas.html.evolution.results.1"/>
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%= Constants.HTML_MENU_EVOLUTION_RESULTS_2 %>">
				<bean:message key="migas.html.evolution.results.2"/>
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%= Constants.HTML_MENU_EVOLUTION_RESULTS_3 %>">
				<bean:message key="migas.html.evolution.results.3"/>
			</logic:equal>
			<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%= Constants.HTML_MENU_EVOLUTION_RESULTS_4 %>">
				<bean:message key="migas.html.evolution.results.4"/>
			</logic:equal>
		</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_CONCLUSION %>">
		<logic:empty name="<%=Constants.HTML_SUBMENU %>">
			<bean:message key="migas.html.conclusion" />
		</logic:empty>
		<logic:notEmpty name="<%=Constants.HTML_SUBMENU %>">
			<html:link href="<%= Constants.CONCLUSION_FILE %>"><bean:message key="migas.html.conclusion" /></html:link> <%= Constants.BREAD_CRUMBS_SEPARATOR %>
			<logic:equal name="<%= Constants.HTML_SUBMENU %>" value="<%= Constants.HTML_SUBMENU_GLOBAL_CONCLUSION%>">
				<bean:message key="migas.html.global.conclusion"/>
			</logic:equal>
			<logic:equal name="<%= Constants.HTML_SUBMENU %>" value="<%= Constants.HTML_SUBMENU_SEGMENT_CONCLUSION%>">
				<bean:message key="migas.html.segment.conclusion"/>
			</logic:equal>
			<logic:equal name="<%= Constants.HTML_SUBMENU %>" value="<%= Constants.HTML_SUBMENU_EVOLUTION_CONCLUSION%>">
				<bean:message key="migas.html.evolution.conclusion"/>
			</logic:equal>
		</logic:notEmpty>
	</logic:equal>
</p>
