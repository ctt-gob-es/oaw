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
<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<html:javascript formName="TemplateRangeForm" />
<bean:parameter name="<%=Constants.ID_OBSERVATORIO%>" id="idObservatorio" />
<link rel="stylesheet" href="/oaw/js/tagbox/tagbox.css">
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<div id="main">
	<div id="container_menu_izq">
		<jsp:include page="menu.jsp" />
	</div>
	<div id="container_der">
		<div id="migas">
			<p class="sr-only">
				<bean:message key="ubicacion.usuario" />
			</p>
			<ol class="breadcrumb">
				<li>
					<html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link>
				</li>
				<li>
					<html:link forward="resultadosPrimariosObservatorio" paramName="idObservatorio"
						paramId="<%=Constants.ID_OBSERVATORIO%>">
						<bean:message key="migas.indice.observatorios.realizados.lista" />
					</html:link>
				</li>
				<li class="active">
					<bean:message key="migas.exportar" />
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2>
				<bean:message key="send.results.observatory.title.results" />
			</h2>
			<div class="pag">
				<table class="table table-stripped table-bordered table-hover">
					<tr>
						<th>
							<bean:message key="colname.name" />
						</th>
						<th>
							<bean:message key="colname.email" />
						</th>
						<th>
							<bean:message key="colname.send.auto" />
						</th>
						<th>
							<bean:message key="colname.sended" />
						</th>
						<th>
							<bean:message key="colname.date" />
						</th>
						<th>
							<bean:message key="colname.error" />
						</th>
					</tr>
					<logic:empty name="uraSendResults">
						<tr>
							<td colspan="6">
								<bean:message key="no.results" />
							</td>
						</tr>
					</logic:empty>
					<logic:notEmpty name="uraSendResults">
						<logic:iterate name="uraSendResults" id="uraSend">
							<tr>
								<td>
									<bean:write name="uraSend" property="uraName" />
								</td>
								<td>
									<bean:write name="uraSend" property="ura.emails" />
								</td>
								<td>
									<logic:equal name="uraSend" property="ura.sendAuto" value="true">
										<bean:message key="select.yes" />
									</logic:equal>
									<logic:notEqual name="uraSend" property="ura.sendAuto" value="true">
										<bean:message key="select.no" />
									</logic:notEqual>
								</td>
								<td>
									<logic:equal name="uraSend" property="send" value="true">
										<bean:message key="select.yes" />
									</logic:equal>
									<logic:notEqual name="uraSend" property="send" value="true">
										<bean:message key="select.no" />
									</logic:notEqual>
								</td>
								<td>
									<fmt:formatDate value="${uraSend.sendDate}" pattern="dd-MM-yyyy HH:mm" />
								</td>
								<td>
									<bean:write name="uraSend" property="sendError" />
								</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</div>
			<p id="pCenter">
				<html:link styleClass="btn btn-default btn-lg" forward="observatoryMenu">
					<bean:message key="boton.volver" />
				</html:link>
			</p>
		</div>
		<!-- fin cajaformularios -->
	</div>
</div>