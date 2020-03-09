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
<bean:define id="rolObservatory">
	<inteco:properties key="role.observatory.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolAdmin">
	<inteco:properties key="role.administrator.id" file="crawler.properties" />
</bean:define>

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
				<li><html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link></li>
				<li><html:link forward="resultadosPrimariosObservatorio" paramName="idObservatory" paramId="id_observatorio">
						<bean:message key="migas.indice.observatorios.realizados.lista" />
					</html:link></li>
				<li class="active"><bean:message key="migas.estado.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">



			<h2>Estdo de la ejecución</h2>

			<table class="table table-stripped table-bordered table-hover">
				<caption>Estado de la ejecución del observatorio</caption>
				<tbody>
					<tr>
						<th>Estado</th>
						<th>Acciones</th>
					</tr>
					<tr>





						<jsp:useBean id="paramsRelanzar" class="java.util.HashMap" />
						<c:set target="${paramsRelanzar}" property="action" value="confirm" />
						<c:set target="${paramsRelanzar}" property="id_observatorio" value="${idObservatory}" />
						<c:set target="${paramsRelanzar}" property="idExObs" value="${idExecutedObservatorio}" />
						<c:set target="${paramsRelanzar}" property="idCartucho" value="${idCartucho}" />

						<logic:equal name="estado" property="idEstado" value="3">
							<td style="text-align: center"><bean:message key="resultado.observatorio.rastreo.realizado.estado.relanzado" />
							</td>
							<td><html:link forward="relanzarObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="Relanzar esta iteraci&oacute;n del observatorio" />
									<span class="sr-only">Relanzar esta iteraci&oacute;n del observatorio</span>
								</html:link></td>

						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="1">
							<td><bean:message key="resultado.observatorio.rastreo.realizado.estado.lanzado" /></td>

							<td><html:link forward="relanzarObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="Relanzar esta iteraci&oacute;n del observatorio" />
									<span class="sr-only">Relanzar esta iteraci&oacute;n del observatorio</span>
								</html:link></td>

						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="0">
							<td><bean:message key="resultado.observatorio.rastreo.realizado.estado.terminado" /></td>
							<td><html:link forward="resultadosObservatorioSemillas" name="paramsRelanzar">
									<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
										title="Ver resultados de este observatorio" />
									<span class="sr-only">Resultados</span>
								</html:link></td>
						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="2">
							<td><bean:message key="resultado.observatorio.rastreo.realizado.estado.error" /></td>

							<td><html:link forward="relanzarObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="Relanzar esta iteraci&oacute;n del observatorio" />
									<span class="sr-only">Relanzar esta iteraci&oacute;n del observatorio</span>
								</html:link></td>

						</logic:equal>
					</tr>
				</tbody>
			</table>

			<h2>Resumen de la ejecución del observatorio</h2>

			<table class="table table-stripped table-bordered table-hover">
				<caption>Información del análisis en curso</caption>
				<tbody>
					<tr>
						<th>Total de semillas</th>
						<th>Total de semillas procesadas</th>
						<th>Total de semillas procesada con resultados</th>
						<th>Tiempo total</th>
						<th>Tiempo medio</th>
						<th>Tiempo estimado fin</th>
					</tr>

					<tr>
						<td style="text-align: center"><bean:write name="estado" property="totalSemillas" /></td>
						<td><bean:write name="estado" property="semillasAnalizadas" /> (<bean:write name="estado"
								property="porcentajeCompletado" format="###.##" />% completado)</td>
						<td><bean:write name="estado" property="semillasAnalizadasOk" /> (<bean:write name="estado"
								property="porcentajeCompletadoOk" format="###.##" />% completado)</td>
						<td><bean:write name="estado" property="tiempoTotal" /> minutos (<bean:write name="estado"
								property="tiempoTotalHoras" /> horas)</td>
						<td><bean:write name="estado" property="tiempoMedio" /> minutos</td>
						<td><bean:write name="estado" property="tiempoEstimado" /> minutos (<bean:write name="estado"
								property="tiempoEstimadoHoras" /> Horas)</td>
					</tr>

				</tbody>
			</table>

			<logic:notEqual name="estado" property="idEstado" value="0">
				<h2>Análisis en curso/útlimo análisis</h2>

				<table class="table table-stripped table-bordered table-hover">
					<caption>Información del análisis en curso</caption>
					<tbody>

						<tr>
							<th>Semilla</th>
						</tr>
						<tr>
							<td style="text-align: center"><bean:write name="analisis" property="nombre" />&nbsp;(<bean:write
									name="analisis" property="url" />)</td>
						</tr>
					</tbody>
				</table>
				<table class="table table-stripped table-bordered table-hover">
					<caption>Información del análisis en curso</caption>
					<tbody>
						<tr>
							<th>Total URL recogidas</th>
							<th>Total URL analizadas</th>
							<th>Última URL analizada</th>
							<th>Fin última URl analizada</th>
							<th>Tiempo medio</th>
							<th>Tiempo total</th>
							<th>Tiempo estimado fin</th>
						</tr>

						<tr>
							<td><bean:write name="analisis" property="totalUrl" /> <span
								class="glyphicon glyphicon glyphicon-info-sign" style="color: #e21430"></span></td>
							<td><bean:write name="analisis" property="totalUrlAnalizadas" /> (<bean:write name="analisis"
									property="porcentajeCompletado" format="###.##" />% completado)</td>
							<td><bean:write name="analisis" property="ultimaUrl" /></td>
							<td><fmt:formatDate value="${analisis.fechaUltimaUrl}" pattern="dd-MM-yyyy HH:mm" /></td>
							<td><bean:write name="analisis" property="tiempoMedio" /> segundos</td>
							<td><bean:write name="analisis" property="tiempoAcumulado" /> segundos</td>
							<td><bean:write name="analisis" property="tiempoEstimado" /> segundos</td>
						</tr>

					</tbody>
				</table>





				<h2>Portales sin analizar aún</h2>
				<table class="table table-stripped table-bordered table-hover table-console">
					<caption>Información de portales sin analizar aún</caption>
					<colgroup>
						<col style="width: 5%">
						<col style="width: 30%">
						<col style="width: 65%">
					</colgroup>
					<tbody>
						<tr>
							<th>#</th>
							<th>Nombre</th>
							<th>URL</th>
						</tr>

						<logic:empty name="notCrawledSeedsYet">
							<tr>
								<td colspan="3">Sin resultados</td>
							</tr>
						</logic:empty>

						<logic:iterate name="notCrawledSeedsYet" id="notCrawledSeedsYet" indexId="index">

							<tr>
								<td class="col-md-1"><c:out value="${index + 1}" /></td>
								<td style="text-align: left"><bean:write name="notCrawledSeedsYet" property="nombre" /></td>
								<td style="text-align: left" title="<bean:write name="notCrawledSeedsYet" property="listaUrlsString" />"><bean:write
										name="notCrawledSeedsYet" property="listaUrlsString" /></td>
							</tr>
						</logic:iterate>



					</tbody>
				</table>
			</logic:notEqual>

			<h2>Portales sin resultados</h2>
			<table class="table table-stripped table-bordered table-hover table-console">
				<caption>Información de análisis pendientes</caption>
				<colgroup>
					<col style="width: 5%">
					<col style="width: 30%">
					<col style="width: 55%">
					<col style="width: 10%">
				</colgroup>
				<tbody>
					<tr>
						<th>#</th>
						<th>Nombre</th>
						<th>URL</th>
						<th>Relanzar</th>
					</tr>

					<logic:empty name="finishWithoutResults">
						<tr>
							<td colspan="4">Sin resultados</td>
						</tr>
					</logic:empty>

					<logic:iterate name="finishWithoutResults" id="crawlWithoutAnalisis" indexId="index">

						<tr>
							<td class="col-md-1"><c:out value="${index + 1}" /></td>
							<td style="text-align: left" class="col-md-4"><bean:write name="crawlWithoutAnalisis" property="nombre" /></td>
							<td style="text-align: left" class="col-md-5"
								title="<logic:iterate
									name="crawlWithoutAnalisis" property="listaUrls" id="url">
									<bean:write name="url" />
								</logic:iterate>">
								<logic:iterate name="crawlWithoutAnalisis" property="listaUrls" id="url">
									<bean:write name="url" />
								</logic:iterate>

							</td>
							<td class="col-md-2"><jsp:useBean id="paramsRelanzarCrawl" class="java.util.HashMap" /> <%-- 							<c:set --%>
								<%-- 									target="${paramsRelanzarCrawl}" property="action" value="confirmacionExSeed" /> <c:set
									target="${paramsRelanzarCrawl}" property="id" value="${crawlWithoutAnalisis.idFulfilledCrawling}" />--%> <c:set
									target="${paramsRelanzarCrawl}" property="id_observatorio" value="${idObservatory}" /> <c:set
									target="${paramsRelanzarCrawl}" property="idExObs" value="${idExecutedObservatorio}" /> <c:set
									target="${paramsRelanzarCrawl}" property="idCartucho" value="${idCartucho}" /> <c:set
									target="${paramsRelanzarCrawl}" property="idSemilla" value="${crawlWithoutAnalisis.id}" /> <html:link
									forward="resultadosObservatorioLanzarEjecucion" name="paramsRelanzarCrawl">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="Relanzar esta iteraci&oacute;n del observatorio" />
									<span class="sr-only">Relanzar análisis</span>
								</html:link></td>
						</tr>
					</logic:iterate>

				</tbody>
			</table>

		</div>
		<!-- fin cajaformularios -->
	</div>
	<!-- Container Derecha -->
</div>