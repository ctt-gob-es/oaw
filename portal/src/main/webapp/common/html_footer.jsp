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


<%@page import="java.net.URLDecoder"%>
<bean:define id="observatoryName"><bean:write name="<%= Constants.OBSERVATORY_NAME %>" /></bean:define>
<bean:define id="observatoryDate"><bean:write name="<%= Constants.OBSERVATORY_DATE %>" /></bean:define>

<div id="footer">
	<p><bean:message key="anonymous.html.footer" arg0="<%= observatoryName %>" arg1="<%= observatoryDate %>"/></p>
</div>
