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
<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml />

<logic:present parameter="<%=Constants.ID_RASTREO%>">
	<bean:parameter id="idrastreo" name="<%=Constants.ID_RASTREO%>" />
</logic:present>
<logic:present parameter="<%=Constants.ID%>">
	<bean:parameter id="id" name="<%=Constants.ID%>" />
</logic:present>
<logic:present parameter="<%=Constants.ID_OBSERVATORIO%>">
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO%>" />
</logic:present>
<logic:present parameter="<%=Constants.ID_EX_OBS%>">
	<bean:parameter id="idExObs" name="<%=Constants.ID_EX_OBS%>" />
</logic:present>
<logic:present parameter="<%=Constants.ID_EX_OBS%>">
	<bean:parameter id="idCartucho" name="<%=Constants.ID_CARTUCHO%>" />
</logic:present>

<jsp:useBean id="paramsVolver" class="java.util.HashMap" />
<c:set target="${paramsVolver}" property="idrastreo" value="${idrastreo}" />
<c:set target="${paramsVolver}" property="id_observatorio" value="${id_observatorio}" />
<c:set target="${paramsVolver}" property="id" value="${id}" />
<c:set target="${paramsVolver}" property="idExObs" value="${idExObs}" />
<c:set target="${paramsVolver}" property="idCartucho" value="${idCartucho}" />

<jsp:useBean id="paramsVolverFC" class="java.util.HashMap" />
<c:set target="${paramsVolverFC}" property="idrastreo" value="${idrastreo}" />
<c:set target="${paramsVolverFC}" property="isCliente" value="true" />

<jsp:useBean id="params" class="java.util.HashMap" />
<c:set target="${params}" property="idrastreo" value="${idrastreo}" />
<c:set target="${params}" property="id" value="${id}" />
<c:set target="${params}" property="idCartucho" value="${idCartucho}" />
<logic:present parameter="isCliente">
	<c:set target="${params}" property="isCliente" value="true" />
</logic:present>
<logic:present parameter="<%=Constants.OBSERVATORY%>">
	<c:set target="${params}" property="observatorio" value="si" />
</logic:present>




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
				<logic:present parameter="isCliente">
					<li><html:link forward="loadClientCrawlings">
							<bean:message key="migas.rastreos.cliente" />
						</html:link></li>
					<li><html:link forward="loadClientFulfilledCrawlings" name="paramsVolverFC">
							<bean:message key="migas.rastreos.realizados" />
						</html:link></li>
				</logic:present>
				<logic:notPresent parameter="isCliente">
					<logic:notPresent parameter="<%=Constants.OBSERVATORY%>">
						<li><html:link forward="crawlingsMenu">
								<bean:message key="migas.rastreo" />
							</html:link></li>
						<li><html:link forward="loadFulfilledCrawlings" paramId="<%=Constants.ID_RASTREO%>"
								paramName="<%=Constants.ID_RASTREO%>">
								<bean:message key="migas.rastreos.realizados" />
							</html:link></li>
					</logic:notPresent>
					<logic:present parameter="<%=Constants.OBSERVATORY%>">
						<li><html:link forward="observatoryMenu">
								<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
								<bean:message key="migas.observatorio" />
							</html:link></li>
						<li><html:link forward="resultadosPrimariosObservatorio" paramName="id_observatorio"
								paramId="<%=Constants.ID_OBSERVATORIO%>">
								<bean:message key="migas.resultado.rastreos.realizados.observatorio" />
							</html:link></li>
						<li><html:link forward="resultadosObservatorioSemillas" name="paramsVolver">
								<bean:message key="migas.resultado.observatorio" />
							</html:link></li>
					</logic:present>
				</logic:notPresent>
				<li class="active"><bean:message key="migas.rastreos.realizados.url.analizadas" /></li>
			</ol>
		</div>


		<div id="cajaformularios">
			<h2>
				<bean:message key="search.results.by.tracking" />
			</h2>
			<logic:notPresent name="<%=Constants.LIST_ANALYSIS%>">
				<div class="notaInformativaExito">
					<p>
						<bean:message key="indice.rastreo.vacio" />
					</p>
				</div>
			</logic:notPresent>
			<logic:present name="<%=Constants.LIST_ANALYSIS%>">
				<logic:empty name="<%=Constants.LIST_ANALYSIS%>">
					<div class="notaInformativaExito">
						<p>
							<bean:message key="indice.rastreo.vacio" />
						</p>
					</div>
				</logic:empty>
				<logic:notEmpty name="<%=Constants.LIST_ANALYSIS%>">
					<logic:present name="<%=Constants.SCORE%>">
						<div id="observatoryInfo">
							<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
							<c:set target="${params}" property="idExObs" value="${idExObs}" />

							<p>
								<strong><bean:message key="observatorio.nivel.adecuacion" />: </strong>
								<bean:write name="<%=Constants.SCORE%>" property="level" />
							</p>
							<p>
								<strong><bean:message key="observatorio.puntuacion.total" />: </strong>
								<bean:write name="<%=Constants.SCORE%>" property="totalScore" />
							</p>


							<!-- Textos en función de la metodología -->

							<logic:present name="aplicacion">

								<logic:equal value="<%=String.valueOf(Constants.NORMATIVA_ACCESIBILIDAD)%>" name="aplicacion">
									<p>
										<strong><bean:message key="observatorio.puntuacion.prioridad.1" bundle="resources2019" />: </strong>
										<bean:write name="<%=Constants.SCORE%>" property="scoreLevel1" />
									</p>
									<p>
										<strong><bean:message key="observatorio.puntuacion.prioridad.2" bundle="resources2019" />: </strong>
										<bean:write name="<%=Constants.SCORE%>" property="scoreLevel2" />
									</p>
								</logic:equal>


								<logic:equal value="<%=String.valueOf(Constants.NORMATIVA_UNE_EN2019)%>" name="aplicacion">


									<p>
										<strong><bean:message key="observatorio.puntuacion.prioridad.1" bundle="resources2019" />: </strong>
										<bean:write name="<%=Constants.SCORE%>" property="scoreLevel1" />
									</p>
									<p>
										<strong><bean:message key="observatorio.puntuacion.prioridad.2" bundle="resources2019" />: </strong>
										<bean:write name="<%=Constants.SCORE%>" property="scoreLevel2" />
									</p>
								</logic:equal>

								<logic:notEqual value="<%=String.valueOf(Constants.NORMATIVA_UNE_EN2019)%>" name="aplicacion">
									<logic:notEqual value="<%=String.valueOf(Constants.NORMATIVA_ACCESIBILIDAD)%>" name="aplicacion">
										<p>
											<strong><bean:message key="observatorio.puntuacion.prioridad.1" />: </strong>
											<bean:write name="<%=Constants.SCORE%>" property="scoreLevel1" />
										</p>
										<p>
											<strong><bean:message key="observatorio.puntuacion.prioridad.2" />: </strong>
											<bean:write name="<%=Constants.SCORE%>" property="scoreLevel2" />
										</p>
									</logic:notEqual>
								</logic:notEqual>




							</logic:present>
							<logic:notPresent name="aplicacion">


								<p>
									<strong><bean:message key="observatorio.puntuacion.nivel.1" />: </strong>
									<bean:write name="<%=Constants.SCORE%>" property="scoreLevel1" />
								</p>
								<p>
									<strong><bean:message key="observatorio.puntuacion.nivel.2" />: </strong>
									<bean:write name="<%=Constants.SCORE%>" property="scoreLevel2" />
								</p>
							</logic:notPresent>
							<p>
								<strong><bean:message key="search.results.by.tracking.pages" />: </strong>
								<bean:write name="numResult" />
							</p>

						</div>
					</logic:present>

					<div class="pag">
						<table class="table table-stripped table-bordered table-hover">
							<thead>
								<tr>
									<th>#</th>
									<th><bean:message key="search.results.domain" /></th>
									<th><bean:message key="search.results.entity" /></th>
									<th><bean:message key="search.results.date" /></th>
									<logic:notPresent parameter="<%=Constants.OBSERVATORY%>">
										<th><bean:message key="search.results.problems" /></th>
										<th><bean:message key="search.results.warnings" /></th>
										<th><bean:message key="search.results.observations" /></th>
									</logic:notPresent>
									<th>Resultados</th>
									<th>C&oacute;digo fuente</th>
								</tr>
							</thead>
							<tbody>
								<logic:iterate id="analysis" name="<%=Constants.LIST_ANALYSIS%>" indexId="index">
									<c:set target="${params}" property="code" value="${analysis.code}" />

									<c:set var="currentPage" value="${currentPage}" scope="request" />
									<c:set var="pageSize" value="${pageSize}" scope="request" />

									<tr>
										<td><c:out value="${index + 1 + ((currentPage -1) * pageSize)}" /></td>
										<logic:empty name="analysis" property="urlTitle">
											<td class="alignLeft"><bean:write name="analysis" property="url" /></td>
										</logic:empty>
										<logic:notEmpty name="analysis" property="urlTitle">
											<td class="alignLeft" title="<bean:write name="analysis" property="urlTitle" />"><bean:write
													name="analysis" property="url" /></td>
										</logic:notEmpty>
										<logic:empty name="analysis" property="entityTitle">
											<td><bean:write name="analysis" property="entity" /></td>
										</logic:empty>
										<logic:notEmpty name="analysis" property="entityTitle">
											<td title="<bean:write name="analysis" property="entityTitle" />"><bean:write name="analysis"
													property="entity" /></td>
										</logic:notEmpty>
										<td><bean:write name="analysis" property="date" /></td>
										<logic:notPresent parameter="<%=Constants.OBSERVATORY%>">
											<logic:equal name="analysis" property="status" value="<%=String.valueOf(Constants.STATUS_SUCCESS)%>">
												<td><bean:write name="analysis" property="problems" /></td>
												<td><bean:write name="analysis" property="warnings" /></td>
												<td><bean:write name="analysis" property="observations" /></td>
												<bean:define id="title">
													<bean:message key="view.analyse.title" />
													<bean:write name="analysis" property="entity" /> (<bean:write name="analysis" property="date" />)</bean:define>
												<td><html:link forward="showAnalysisFromCrawler" name="params" title="<%=title%>">
														<bean:message key="view.analyse" />
													</html:link></td>
											</logic:equal>
											<logic:equal name="analysis" property="status" value="<%=String.valueOf(Constants.STATUS_ERROR)%>">
												<td>-</td>
												<td>-</td>
												<td>-</td>
												<td><html:img src="../images/error.png" altKey="search.results.analysis.error" /></td>
											</logic:equal>
										</logic:notPresent>
										<logic:present parameter="<%=Constants.OBSERVATORY%>">
											<td><c:set target="${params}" property="id_observatorio" value="${id_observatorio}" /> <logic:equal
													name="analysis" property="status" value="<%=String.valueOf(Constants.STATUS_SUCCESS)%>">
													<html:link forward="showAnalysisFromCrawler" name="params">
														<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
															title="Ver resultados de esta p&aacute;gina" />
														<span class="sr-only">Resultados</span>
													</html:link>
												</logic:equal> <logic:equal name="analysis" property="status" value="<%=String.valueOf(Constants.STATUS_ERROR)%>">
													<html:img src="../images/error.png" altKey="search.results.analysis.error" />
												</logic:equal></td>
											<td><html:link forward="getHtmlSource" name="params">
													<span class="glyphicon glyphicon-file" aria-hidden="true" data-toggle="tooltip"
														title="Ver el c&oacute;digo fuente analizado" />
													<span class="sr-only">C&oacute;digo fuente</span>
												</html:link></td>
										</logic:present>
									</tr>
								</logic:iterate>
							</tbody>
						</table>

						<jsp:include page="pagination.jsp" />
					</div>
				</logic:notEmpty>
			</logic:present>
			<p id="pCenter">
				<logic:notPresent parameter="isCliente">
					<logic:notPresent parameter="<%=Constants.OBSERVATORY%>">
						<html:link forward="loadFulfilledCrawlings" styleClass="btn btn-default btn-lg"
							paramId="<%=Constants.ID_RASTREO%>" paramName="<%=Constants.ID_RASTREO%>">
							<bean:message key="boton.volver" />
						</html:link>
					</logic:notPresent>
					<logic:present parameter="<%=Constants.OBSERVATORY%>">
						<html:link forward="resultadosObservatorioSemillas" styleClass="btn btn-default btn-lg" name="paramsVolver">
							<bean:message key="boton.volver" />
						</html:link>
					</logic:present>
				</logic:notPresent>
			</p>
		</div>
		<!-- fin cajaformularios -->
	</div>

</div>
</div>
</div>
