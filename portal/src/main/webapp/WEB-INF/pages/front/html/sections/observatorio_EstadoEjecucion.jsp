<!--
Copyright (C) 2019  Ministerio de Hacienda y Funci�n P�blica, 
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
				<li>
					<html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link>
				</li>
				<li>
					<html:link forward="resultadosPrimariosObservatorio" paramName="idObservatory" paramId="id_observatorio">
						<bean:message key="migas.indice.observatorios.realizados.lista" />
					</html:link>
				</li>
				<li class="active">
					<bean:message key="migas.estado.observatorio" />
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2>
				<bean:message key="observatory.status.title" />
			</h2>
			<table class="table table-stripped table-bordered table-hover">
				<caption>
					<bean:message key="observatory.status.caption" />
				</caption>
				<tbody>
					<tr>
						<th>
							<bean:message key="observatory.status.status" />
						</th>
						<th>
							<bean:message key="observatory.status.actions" />
						</th>
					</tr>
					<tr>
						<jsp:useBean id="paramsRelanzar" class="java.util.HashMap" />
						<c:set target="${paramsRelanzar}" property="action" value="confirm" />
						<c:set target="${paramsRelanzar}" property="id_observatorio" value="${idObservatory}" />
						<c:set target="${paramsRelanzar}" property="idExObs" value="${idExecutedObservatorio}" />
						<c:set target="${paramsRelanzar}" property="idCartucho" value="${idCartucho}" />
						<logic:equal name="estado" property="idEstado" value="3">
							<td style="text-align: center">
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.relanzado" />
							</td>
							<td>
								<html:link forward="relanzarObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.relaunh"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.relaunh" />
									</span>
								</html:link>
							</td>
						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="1">
							<td>
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.lanzado" />
							</td>
							<td>
								<html:link forward="relanzarObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.relaunh"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.relaunh" />
									</span>
								</html:link>
							</td>
						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="0">
							<td>
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.terminado" />
							</td>
							<td>
								<html:link forward="resultadosObservatorioSemillas" name="paramsRelanzar">
									<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.results"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.results" />
									</span>
								</html:link>
							</td>
						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="2">
							<td>
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.error" />
							</td>
							<td>
								<html:link forward="relanzarObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.relaunh"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.relaunh" />
									</span>
								</html:link>
							</td>
						</logic:equal>
					</tr>
				</tbody>
			</table>
			<h2>
				<bean:message key="observatory.status.summary" />
			</h2>
			<table class="table table-stripped table-bordered table-hover">
				<caption>
					<bean:message key="observatory.status.summary.caption" />
				</caption>
				<tbody>
					<tr>
						<th>
							<bean:message key="observatory.status.summary.seed.total" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.seed.processed" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.seed.results" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.time.total" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.time.avg" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.time.end" />
						</th>
					</tr>
					<tr>
						<td style="text-align: center">
							<bean:write name="estado" property="totalSemillas" />
						</td>
						<td>
							<bean:write name="estado" property="semillasAnalizadas" />
							(
							<bean:write name="estado" property="porcentajeCompletado" format="###.##" />
							%
							<bean:message key="completado" />
							)
						</td>
						<td>
							<bean:write name="estado" property="semillasAnalizadasOk" />
							(
							<bean:write name="estado" property="porcentajeCompletadoOk" format="###.##" />
							%
							<bean:message key="completado" />
							)
						</td>
						<td>
							<bean:write name="estado" property="tiempoTotal" />
							<bean:message key="minutos" />
							(
							<bean:write name="estado" property="tiempoTotalHoras" />
							<bean:message key="horas" />
							)
						</td>
						<td>
							<bean:write name="estado" property="tiempoMedio" />
							<bean:message key="minutos" />
						</td>
						<td>
							<bean:write name="estado" property="tiempoEstimado" />
							<bean:message key="minutos" />
							(
							<bean:write name="estado" property="tiempoEstimadoHoras" />
							<bean:message key="horas" />
							)
						</td>
					</tr>
				</tbody>
			</table>
			<logic:notEqual name="estado" property="idEstado" value="0">
				<h2>
					<bean:message key="observatory.status.last.title" />
				</h2>
				<table class="table table-stripped table-bordered table-hover">
					<caption>
						<bean:message key="observatory.status.last.caption" />
					</caption>
					<tbody>
						<tr>
							<th>
								<bean:message key="observatory.status.last.seed" />
							</th>
						</tr>
						<tr>
							<td style="text-align: center">
								<bean:write name="analisis" property="nombre" />
								&nbsp;(
								<bean:write name="analisis" property="url" />
								)
							</td>
						</tr>
					</tbody>
				</table>
				<table class="table table-stripped table-bordered table-hover">
					<caption>
						<bean:message key="observatory.status.last.caption" />
					</caption>
					<tbody>
						<tr>
							<th>
								<bean:message key="observatory.status.last.total.url" />
							</th>
							<th>
								<bean:message key="observatory.status.last.total.url.analized" />
							</th>
							<th>
								<bean:message key="observatory.status.last.total.url.last" />
							</th>
							<th>
								<bean:message key="observatory.status.last.total.url.last.end" />
							</th>
							<th>
								<bean:message key="observatory.status.last.time.avg" />
							</th>
							<th>
								<bean:message key="observatory.status.last.time.total" />
							</th>
							<th>
								<bean:message key="observatory.status.last.time.end" />
							</th>
						</tr>
						<tr>
							<td>
								<bean:write name="analisis" property="totalUrl" />
							</td>
							<td>
								<bean:write name="analisis" property="totalUrlAnalizadas" />
								(
								<bean:write name="analisis" property="porcentajeCompletado" format="###.##" />
								%
								<bean:message key="completado" />
								)
							</td>
							<td>
								<bean:write name="analisis" property="ultimaUrl" />
							</td>
							<td>
								<fmt:formatDate value="${analisis.fechaUltimaUrl}" pattern="dd-MM-yyyy HH:mm" />
							</td>
							<td>
								<bean:write name="analisis" property="tiempoMedio" />
								<bean:message key="segundos" />
							</td>
							<td>
								<bean:write name="analisis" property="tiempoAcumulado" />
								<bean:message key="segundos" />
							</td>
							<td>
								<bean:write name="analisis" property="tiempoEstimado" />
								<bean:message key="segundos" />
							</td>
						</tr>
					</tbody>
				</table>
			</logic:notEqual>
			<logic:notEqual name="estado" property="idEstado" value="0">
				<h2>
					<bean:size id="notCrawledSeedsYetSize" name="notCrawledSeedsYet" />
					<bean:message key="observatory.status.pending.title" />
					<c:if test="${notCrawledSeedsYetSize > 0}}">
					&nbsp;(
					<bean:write name="notCrawledSeedsYetSize" />
					)
					</c:if>
				</h2>
				<table class="table table-stripped table-bordered table-hover table-console">
					<caption>
						<bean:message key="observatory.status.pending.caption" />
					</caption>
					<colgroup>
						<col style="width: 5%">
						<col style="width: 30%">
						<col style="width: 65%">
					</colgroup>
					<tbody>
						<tr>
							<th>#</th>
							<th>
								<bean:message key="observatory.status.pending.name" />
							</th>
							<th>URL</th>
						</tr>
						<logic:empty name="notCrawledSeedsYet">
							<tr>
								<td colspan="3">
									<bean:message key="no.results" />
								</td>
							</tr>
						</logic:empty>
						<logic:iterate name="notCrawledSeedsYet" id="notCrawledSeedsYet" indexId="index">
							<tr>
								<td class="col-md-1">
									<c:out value="${index + 1}" />
								</td>
								<td style="text-align: left">
									<bean:write name="notCrawledSeedsYet" property="nombre" />
								</td>
								<td style="text-align: left" title="<bean:write name="notCrawledSeedsYet" property="listaUrlsString" />">
									<bean:write name="notCrawledSeedsYet" property="listaUrlsString" />
								</td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</logic:notEqual>
			<!-- Less Threshold -->
			<h2>
				<bean:size id="finishLessThresholdSize" name="finishLessThreshold" />
				<bean:message key="observatory.status.less.threshold.title" />
				&nbsp;
				<c:if test="${finishLessThresholdSize > 0}}">
				(
				<bean:write name="finishLessThresholdSize" />
				)
				</c:if>
			</h2>

			<form action="/oaw/secure/RelanzarObservatorioSeleccionandoAction.do">
			    <input type="submit" value='<bean:message key="observatory.status.no.results.relaunchselected"/>' id="threshold_relaunchselected" disabled />
			    <input type="checkbox" id='threshold_checkbox_all' name='threshold_checkbox_all'>
                <table class="table table-stripped table-bordered table-hover table-console">
                    <caption>
                        <bean:message key="observatory.status.less.threshold.caption" />
                    </caption>
                    <colgroup>
                        <col style="width: 7%">
                        <col style="width: 5%">
                        <col style="width: 30%">
                        <col style="width: 50%">
                        <col style="width: 8%">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th>
                                <bean:message key="observatory.status.no.results.selector" />
                            </th>
                            <th>#</th>
                            <th>
                                <bean:message key="observatory.status.no.results.name" />
                            </th>
                            <th>URL</th>
                            <th>
                                <bean:message key="observatory.status.no.results.relaunch" />
                            </th>
                        </tr>
                        <logic:empty name="finishLessThreshold">
                            <tr>
                                <td colspan="4">
                                    <bean:message key="no.results" />
                                </td>
                            </tr>
                        </logic:empty>
                        <logic:iterate name="finishLessThreshold" id="crawlLessThreshold" indexId="index">
                            <tr>
                                <td>
                                    <input type="checkbox" class="threshold_selectionCheckBox" <c:out value='name=line_check_${index}' />>
                                    <input type="hidden" <c:out value='name=line_data_${index}' /> <c:out value='value=${crawlLessThreshold.id}' />
                                </td>
                                <td class="col-md-1">
                                    <c:out value="${index + 1}" />
                                </td>
                                <td style="text-align: left" class="col-md-4">
                                    <bean:write name="crawlLessThreshold" property="nombre" />
                                </td>
                                <td style="text-align: left" class="col-md-5"
                                    title="<logic:iterate
                                        name="crawlLessThreshold" property="listaUrls" id="url">
                                        <bean:write name="url" />
                                    </logic:iterate>">
                                    <logic:iterate name="crawlLessThreshold" property="listaUrls" id="url">
                                        <bean:write name="url" />
                                    </logic:iterate>
                                </td>
                                <td class="col-md-2"><jsp:useBean id="paramsRelaunchThreshold" class="java.util.HashMap" />
                                    <c:set target="${paramsRelaunchThreshold}" property="id_observatorio" value="${idObservatory}" />
                                    <c:set target="${paramsRelaunchThreshold}" property="idExObs" value="${idExecutedObservatorio}" />
                                    <c:set target="${paramsRelaunchThreshold}" property="idCartucho" value="${idCartucho}" />
                                    <c:set target="${paramsRelaunchThreshold}" property="idSemilla" value="${crawlLessThreshold.id}" />
                                    <html:link forward="resultadosObservatorioLanzarEjecucion" name="paramsRelaunchThreshold">
                                        <span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
                                            title="<bean:message key="observatory.status.no.results.relaunch"/>"></span>
                                        <span class="sr-only">
                                            <bean:message key="observatory.status.no.results.relaunch" />
                                        </span>
                                    </html:link>
                                </td>
                            </tr>
                        </logic:iterate>
                    </tbody>
                </table>
                <input type="hidden" name='id_observatorio' <c:out value='value=${idObservatory}' /> >
                    <input type="hidden" name='idExObs' <c:out value='value=${idExecutedObservatorio}' /> >
            </form>
			<h2>
				<bean:size id="finishWithoutResultsSize" name="finishWithoutResults" />
				<bean:message key="observatory.status.no.results.title" />
				<c:if test="${finishWithoutResultsSize > 0}}">
				&nbsp;(
				<bean:write name="finishWithoutResultsSize" />
				)
				</c:if>
			</h2>








            <form action="/oaw/secure/RelanzarObservatorioSeleccionandoAction.do">
                <input type="submit" value='<bean:message key="observatory.status.no.results.relaunchselected"/>' id="relaunchselected" disabled />
                <input type="checkbox" id='checkbox_all' name='checkbox_all'>
                <table class="table table-stripped table-bordered table-hover table-console">
                    <caption>
                        <bean:message key="observatory.status.no.results.caption" />
                    </caption>
                    <colgroup>
                        <col style="width: 7%">
                        <col style="width: 5%">
                        <col style="width: 30%">
                        <col style="width: 50%">
                        <col style="width: 8%">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th>
                                <bean:message key="observatory.status.no.results.selector" />
                            </th>
                            <th>#</th>
                            <th>
                                <bean:message key="observatory.status.no.results.name" />
                            </th>
                            <th>URL</th>
                            <th>
                                <bean:message key="observatory.status.no.results.relaunch" />
                            </th>
                        </tr>
                        <logic:empty name="finishWithoutResults">
                            <tr>
                                <td colspan="4">
                                    <bean:message key="no.results" />
                                </td>
                            </tr>
                        </logic:empty>
                        <logic:iterate name="finishWithoutResults" id="crawlWithoutAnalisis" indexId="index">
                            <tr>
                                <td>
                                    <input type="checkbox" class="selectionCheckBox" <c:out value='name=line_check_${index}' />>
                                    <input type="hidden" <c:out value='name=line_data_${index}' /> <c:out value='value=${crawlWithoutAnalisis.id}' />
                                </td>
                                <td class="col-md-1">
                                    <c:out value="${index + 1}" />
                                </td>
                                <td style="text-align: left" class="col-md-4">
                                    <bean:write name="crawlWithoutAnalisis" property="nombre" />
                                </td>
                                <td style="text-align: left" class="col-md-5"
                                    title="<logic:iterate
                                        name="crawlWithoutAnalisis" property="listaUrls" id="url">
                                        <bean:write name="url" />
                                    </logic:iterate>">
                                    <logic:iterate name="crawlWithoutAnalisis" property="listaUrls" id="url">
                                        <bean:write name="url" />
                                    </logic:iterate>
                                </td>
                                <td class="col-md-2"><jsp:useBean id="paramsRelanzarCrawl" class="java.util.HashMap" />
                                                    <%-- 							<c:set --%>
                                                    <%-- 	target="${paramsRelanzarCrawl}" property="action" value="confirmacionExSeed" /> <c:set
                                                            target="${paramsRelanzarCrawl}" property="id" value="${crawlWithoutAnalisis.idFulfilledCrawling}" />--%>
                                    <c:set target="${paramsRelanzarCrawl}" property="id_observatorio" value="${idObservatory}" />
                                    <c:set target="${paramsRelanzarCrawl}" property="idExObs" value="${idExecutedObservatorio}" />
                                    <c:set target="${paramsRelanzarCrawl}" property="idCartucho" value="${idCartucho}" />
                                    <c:set target="${paramsRelanzarCrawl}" property="idSemilla" value="${crawlWithoutAnalisis.id}" />
                                    <html:link forward="resultadosObservatorioLanzarEjecucion" name="paramsRelanzarCrawl">
                                        <span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
                                            title="<bean:message key="observatory.status.no.results.relaunch"/>"></span>
                                        <span class="sr-only">
                                            <bean:message key="observatory.status.no.results.relaunch" />
                                        </span>
                                    </html:link>
                                </td>
                            </tr>
                        </logic:iterate>
                    </tbody>
                </table>
                <input type="hidden" name='id_observatorio' <c:out value='value=${idObservatory}' /> >
                <input type="hidden" name='idExObs' <c:out value='value=${idExecutedObservatorio}' /> >
            </form>
            <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
            <script language="JavaScript">
                $(document).ready(function() {

                    $(".selectionCheckBox").val(this.checked);
                    $(".threshold_selectionCheckBox").val(this.checked);

                    $("#checkbox_all").change(function() {
                       if(this.checked) {
                           if ($(".selectionCheckBox")[0]){
                                $("#relaunchselected").prop( "disabled", false );
                           }
                           $(".selectionCheckBox").prop( "checked", true );
                       }
                       else {
                           $(".selectionCheckBox").prop( "checked", false );
                           $("#relaunchselected").prop( "disabled", true );
                       }
                    });
                    $("#threshold_checkbox_all").change(function() {
                          if(this.checked) {
                              if ($(".threshold_selectionCheckBox")[0]){
                                   $("#threshold_relaunchselected").prop( "disabled", false );
                              }
                              $(".threshold_selectionCheckBox").prop( "checked", true );
                          }
                          else {
                              $(".threshold_selectionCheckBox").prop( "checked", false );
                              $("#threshold_relaunchselected").prop( "disabled", true );
                          }
                       });

                    $(".selectionCheckBox").change(function() {
                       if(this.checked) {
                           $("#relaunchselected").prop( "disabled", false );
                       }
                       else {
                           $("#relaunchselected").prop( "disabled", true );
                           var inputElements = [].slice.call(document.querySelectorAll('.selectionCheckBox'));
                           var checkedValue = inputElements.filter(chk => chk.checked).length;
                           if (checkedValue > 0){
                              $("#relaunchselected").prop( "disabled", false );
                           }
                       }
                    });

                    $(".threshold_selectionCheckBox").change(function() {
                      if(this.checked) {
                          $("#threshold_relaunchselected").prop( "disabled", false );
                      }
                      else {
                           $("#threshold_relaunchselected").prop( "disabled", true );
                           var inputElements = [].slice.call(document.querySelectorAll('.threshold_selectionCheckBox'));
                           var checkedValue = inputElements.filter(chk => chk.checked).length;
                           if (checkedValue > 0){
                              $("#threshold_relaunchselected").prop( "disabled", false );
                           }
                      }
                   });
                });
             </script>
		</div>
		<!-- fin cajaformularios -->
	</div>
	<!-- Container Derecha -->
</div>