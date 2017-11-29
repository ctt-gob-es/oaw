<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
This program is licensed and may be used, modified and redistributed under the terms
of the European Public License (EUPL), either version 1.2 or (at your option) any later 
version as soon as they are approved by the European Commission.
Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND, either express or implied. See the License for the specific language governing 
permissions and more details.
You should have received a copy of the EUPL1.2 license along with this program; if not, 
you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
-->
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
