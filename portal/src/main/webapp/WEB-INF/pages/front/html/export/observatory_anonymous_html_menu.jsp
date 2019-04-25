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
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>

<html:xhtml/>

<div id="menu">
	<ul>
		<li>
			<logic:notEqual name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_INTRODUCTION %>">
				<html:link href="<%= Constants.INTRODUCTION_FILE %>" ><bean:message key="anonymous.html.menu.introduction" /></html:link>
			</logic:notEqual>
			<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_INTRODUCTION %>">
				<html:link href="<%= Constants.INTRODUCTION_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.introduction" /></html:link>
			</logic:equal>
		</li>
		
		<li>
			<logic:notEqual name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_OBJECTIVE %>">
				<html:link href="<%= Constants.OBJECTIVE_FILE %>"><bean:message key="anonymous.html.menu.objective" /></html:link>
			</logic:notEqual>
			<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_OBJECTIVE %>">
				<html:link href="<%= Constants.OBJECTIVE_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.objective" /></html:link>
			</logic:equal>
		</li>
		
		<li>
			<logic:notEqual name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_METHODOLOGY %>">
				<html:link href="<%= Constants.METHODOLOGY_FILE %>"><bean:message key="anonymous.html.menu.methodology" /></html:link>
			</logic:notEqual>
			<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_METHODOLOGY %>">
				<html:link href="<%= Constants.METHODOLOGY_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.methodology" /></html:link>
				<ul>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_1 %>">
							<html:link href="<%= Constants.METHODOLOGY_SUB1_FILE %>"><bean:message key="anonymous.html.menu.methodology.1" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_1 %>">
							<html:link href="<%= Constants.METHODOLOGY_SUB1_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.methodology.1" /></html:link>
						</logic:equal>
					</li>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_2 %>">
							<html:link href="<%= Constants.METHODOLOGY_SUB2_FILE %>"><bean:message key="anonymous.html.menu.methodology.2" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_2 %>">
							<html:link href="<%= Constants.METHODOLOGY_SUB2_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.methodology.2" /></html:link>
						</logic:equal>
					</li>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_3 %>">
							<html:link href="<%= Constants.METHODOLOGY_SUB3_FILE %>"><bean:message key="anonymous.html.menu.methodology.3" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_3 %>">
							<html:link href="<%= Constants.METHODOLOGY_SUB3_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.methodology.3" /></html:link>
						</logic:equal>
					</li>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_4 %>">
							<html:link href="<%= Constants.METHODOLOGY_SUB4_FILE %>"><bean:message key="anonymous.html.menu.methodology.4" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_METHODOLOGY_4 %>">
							<html:link href="<%= Constants.METHODOLOGY_SUB4_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.methodology.4" /></html:link>
						</logic:equal>
					</li>
				</ul> 
			</logic:equal>
		</li>
		<li>
			<logic:notEqual name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS %>">
				<html:link href="<%= Constants.GLOBAL_RESULTS_FILE %>"><bean:message key="anonymous.html.menu.globalResult" /></html:link>
			</logic:notEqual>
			<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS %>">
				<html:link href="<%= Constants.GLOBAL_RESULTS_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.globalResult" /></html:link>
				<ul>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_1 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS_FILE %>"><bean:message key="anonymous.html.menu.globalResult.1" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_1 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.globalResult.1" /></html:link>
						</logic:equal>
					</li>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_2 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS2_FILE %>"><bean:message key="anonymous.html.menu.globalResult.2" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_2 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS2_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.globalResult.2" /></html:link>
						</logic:equal>
					</li>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_3 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS3_FILE %>"><bean:message key="anonymous.html.menu.globalResult.3" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_3 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS3_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.globalResult.3" /></html:link>
						</logic:equal>
					</li>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_4 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS4_FILE %>"><bean:message key="anonymous.html.menu.globalResult.4" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_4 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS4_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.globalResult.4" /></html:link>
						</logic:equal>
					</li>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_5 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS5_FILE %>"><bean:message key="anonymous.html.menu.globalResult.5" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_5 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS5_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.globalResult.5" /></html:link>
						</logic:equal>
					</li>
					<li>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_6 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS6_FILE %>"><bean:message key="anonymous.html.menu.globalResult.6" /></html:link>
						</logic:notEqual>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_GLOBAL_RESULTS_6 %>">
							<html:link href="<%= Constants.GLOBAL_RESULTS6_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.globalResult.6" /></html:link>
						</logic:equal>
					</li>
				</ul>
			</logic:equal>
		</li>
		<logic:iterate id="category" name="<%= Constants.CATEGORIES_LIST %>" type="es.inteco.rastreador2.actionform.semillas.CategoriaForm">
			<li>
				<bean:define id="idCategory"><bean:write name="category" property="id" /></bean:define>
				<logic:notEqual name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS %>">
					<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_1.replace(\"{0}\", idCategory) %>" ><bean:message key="anonymous.html.menu.segmentResult" /><bean:write name="category" property="name" /></html:link>
				</logic:notEqual>
				<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS %>">
					<logic:equal parameter="<%= Constants.ID_CATEGORIA %>" value="<%= idCategory %>">
						<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_1.replace(\"{0}\", idCategory) %>" styleClass="current"><bean:message key="anonymous.html.menu.segmentResult" /><bean:write name="category" property="name" /></html:link>
						<ul>
							<li>
								<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_1 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_1.replace(\"{0}\", idCategory) %>" styleClass="current"><bean:message key="ob.resAnon.intav.report.chapterCat1.title" /></html:link>
								</logic:equal>
								<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_1 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_1.replace(\"{0}\", idCategory) %>"><bean:message key="ob.resAnon.intav.report.chapterCat1.title" /></html:link>
								</logic:notEqual>
							</li>
							<li>
								<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_2 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_2.replace(\"{0}\", idCategory) %>" styleClass="current"><bean:message key="ob.resAnon.intav.report.chapterCat2.title" /></html:link>
								</logic:equal>
								<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_2 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_2.replace(\"{0}\", idCategory) %>"><bean:message key="ob.resAnon.intav.report.chapterCat2.title" /></html:link>
								</logic:notEqual>
							</li>
							<li>
								<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_3 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_3.replace(\"{0}\", idCategory) %>" styleClass="current"><bean:message key="ob.resAnon.intav.report.chapterCat3.title" /></html:link>
								</logic:equal>
								<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_3 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_3.replace(\"{0}\", idCategory) %>"><bean:message key="ob.resAnon.intav.report.chapterCat3.title" /></html:link>
								</logic:notEqual>
							</li>
							<li>
								<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_4 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_4.replace(\"{0}\", idCategory) %>" styleClass="current"><bean:message key="ob.resAnon.intav.report.chapterCat4.title" /></html:link>
								</logic:equal>
								<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_4 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_4.replace(\"{0}\", idCategory) %>"><bean:message key="ob.resAnon.intav.report.chapterCat4.title" /></html:link>
								</logic:notEqual>
							</li>
							<li>
								<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_5 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_5.replace(\"{0}\", idCategory) %>" styleClass="current"><bean:message key="ob.resAnon.intav.report.chapterCat5.title" /></html:link>
								</logic:equal>
								<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_SEGMENT_RESULTS_5 %>">
									<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_5.replace(\"{0}\", idCategory) %>"><bean:message key="ob.resAnon.intav.report.chapterCat5.title" /></html:link>
								</logic:notEqual>
							</li>
						</ul>
					</logic:equal>
					<logic:notEqual parameter="<%= Constants.ID_CATEGORIA %>" value="<%= idCategory %>">
						<html:link href="<%= Constants.SEGMENT_RESULTS_FILE_1.replace(\"{0}\", idCategory) %>" ><bean:message key="anonymous.html.menu.segmentResult" /><bean:write name="category" property="name" /></html:link>
					</logic:notEqual>			
				</logic:equal>
			</li>
		</logic:iterate>
		<logic:present parameter="<%= Constants.OBSERVATORY_EVOLUTION %>">
			<li>
				<logic:notEqual name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_EVOLUTION %>">
					<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE %>"><bean:message key="anonymous.html.menu.evolutionResult" /></html:link>
				</logic:notEqual>
				<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_EVOLUTION %>">
					<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.evolutionResult" /></html:link>
					<ul>
						<li>
							<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_EVOLUTION_RESULTS_1 %>">
								<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE_1 %>"><bean:message key="anonymous.html.menu.evolutionResult.1" /></html:link>
							</logic:notEqual>
							<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_EVOLUTION_RESULTS_1 %>">
								<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE_1 %>" styleClass="current"><bean:message key="anonymous.html.menu.evolutionResult.1" /></html:link>
							</logic:equal>
						</li>
						<li>
							<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_EVOLUTION_RESULTS_2 %>">
								<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE_2 %>"><bean:message key="anonymous.html.menu.evolutionResult.2" /></html:link>
							</logic:notEqual>
							<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_EVOLUTION_RESULTS_2 %>">
								<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE_2 %>" styleClass="current"><bean:message key="anonymous.html.menu.evolutionResult.2" /></html:link>
							</logic:equal>
						</li>
						<li>
							<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_EVOLUTION_RESULTS_3 %>">
								<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE_3 %>"><bean:message key="anonymous.html.menu.evolutionResult.3" /></html:link>
							</logic:notEqual>
							<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_EVOLUTION_RESULTS_3 %>">
								<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE_3 %>" styleClass="current"><bean:message key="anonymous.html.menu.evolutionResult.3" /></html:link>
							</logic:equal>
						</li>
						<li>
							<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_EVOLUTION_RESULTS_4 %>">
								<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE_4 %>"><bean:message key="anonymous.html.menu.evolutionResult.4" /></html:link>
							</logic:notEqual>
							<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_MENU_EVOLUTION_RESULTS_4 %>">
								<html:link href="<%= Constants.EVOLUTION_RESULTS_FILE_4 %>" styleClass="current"><bean:message key="anonymous.html.menu.evolutionResult.4" /></html:link>
							</logic:equal>
						</li>
					</ul>
				</logic:equal>
			</li>
		</logic:present>
		<li>
			<logic:notEqual name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_CONCLUSION %>">
				<html:link href="<%= Constants.CONCLUSION_FILE %>"><bean:message key="anonymous.html.menu.conclusion" /></html:link>
			</logic:notEqual>
			<logic:equal name="<%=Constants.HTML_MENU %>" value="<%=Constants.HTML_MENU_CONCLUSION %>">
				<html:link href="<%= Constants.CONCLUSION_FILE %>" styleClass="current"><bean:message key="anonymous.html.menu.conclusion" /></html:link>
				<ul>
					<li>
						<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_GLOBAL_CONCLUSION %>">
							<html:link href="<%= Constants.CONCLUSION_FILE %>" styleClass="current"><bean:message key="ob.resAnon.intav.report.summary.1.title" /></html:link>
						</logic:equal>
						<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_GLOBAL_CONCLUSION %>">
							<html:link href="<%= Constants.CONCLUSION_FILE %>" ><bean:message key="ob.resAnon.intav.report.summary.1.title" /></html:link>
						</logic:notEqual>
					</li>
					<logic:present parameter="<%=Constants.OBSERVATORY_SEGMENTS %>">
						<li>
							<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_SEGMENT_CONCLUSION %>">
								<html:link href="<%= Constants.SEGMENT_CONCLUSION_FILE %>" styleClass="current"><bean:message key="ob.resAnon.intav.report.summary.2.title" /></html:link>
							</logic:equal>
							<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_SEGMENT_CONCLUSION %>">
								<html:link href="<%= Constants.SEGMENT_CONCLUSION_FILE %>"><bean:message key="ob.resAnon.intav.report.summary.2.title" /></html:link>
							</logic:notEqual>
						</li>
					</logic:present>
					<logic:present parameter="<%= Constants.OBSERVATORY_EVOLUTION %>">
						<li>
							<logic:equal name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_EVOLUTION_CONCLUSION %>">
								<html:link href="<%= Constants.EVOLUTION_CONCLUSION_FILE %>" styleClass="current"><bean:message key="ob.resAnon.intav.report.summary.3.title" /></html:link>
							</logic:equal>
							<logic:notEqual name="<%=Constants.HTML_SUBMENU %>" value="<%=Constants.HTML_SUBMENU_EVOLUTION_CONCLUSION %>">
								<html:link href="<%= Constants.EVOLUTION_CONCLUSION_FILE %>" ><bean:message key="ob.resAnon.intav.report.summary.3.title" /></html:link>
							</logic:notEqual>
						</li>
					</logic:present>
				</ul>
			</logic:equal>
		</li>
	</ul>
</div>
