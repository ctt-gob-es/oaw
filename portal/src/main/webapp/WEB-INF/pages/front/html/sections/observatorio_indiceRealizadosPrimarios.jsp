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
<link rel="stylesheet" href="/oaw/js/tagbox/tagbox.css">
<style>
/* Make sure you reset e'erything beforehand. */
* {
	margin: 0;
	padding: 0;
}

/* Although you can't see the box here, so add some padding. */
.tagbox-item .name, .tagbox-item .email {
	/* The name and email within the dropdown */
	display: block;
	float: left;
	width: 35%;
	overflow: hidden;
	text-overflow: ellipsis;
}

.tagbox-item .email {
	float: right;
	width: 65%;
}

.tagbox-wrapper input {
	display: block;
	width: 100% !important;
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 1.42857143;
	color: #555;
	background-color: #fff;
	background-image: none;
	border: 1px solid #ccc;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
	border: none !important;
}

.tagbox-wrapper {
	width: 100% !important;
	border: none !important;
	text-align: left !important;
	box-shadow: none !important;
}
}
</style>
<inteco:sesion action="ifConfigAdmin">
	<bean:define id="idCartridgeMalware">
		<inteco:properties key="cartridge.malware.id" file="crawler.properties" />
	</bean:define>
	<bean:define id="idCartridgeLenox">
		<inteco:properties key="cartridge.lenox.id" file="crawler.properties" />
	</bean:define>
	<bean:define id="idCartridgeIntav">
		<inteco:properties key="cartridge.intav.id" file="crawler.properties" />
	</bean:define>
	<bean:define id="idCartridgeMultilanguage">
		<inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" />
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
					<li class="active">
						<bean:message key="migas.indice.observatorios.realizados.lista" />
					</li>
				</ol>
			</div>
			<div id="cajaformularios">
				<h2>
					<bean:message key="gestion.resultados.observatorio.ejecuciones" />
				</h2>
				<jsp:include page="/common/crawler_messages.jsp" />
				<div class="pag">
					<logic:empty name="<%=Constants.FULFILLED_OBSERVATORIES%>">
						<bean:message key="indice.observatorios.realizados.lista.vacia" />
					</logic:empty>
					<logic:notEmpty name="<%=Constants.FULFILLED_OBSERVATORIES%>">
						<table class="table table-stripped table-bordered table-hover">
							<caption>
								<bean:message key="indice.observatorios.realizados.lista" />
							</caption>
							<tr>
								<th>
									<bean:message key="resultado.observatorio.rastreo.realizado.fecha.ejecucion" />
								</th>
								<th>
									<bean:message key="resultado.observatorio.rastreo.realizado.cartucho.asociado" />
								</th>
								<th>
									<bean:message key="indice.observatorio.etiquetas" />
								</th>
								<th>
									<bean:message key="resultado.observatorio.rastreo.realizado.estado" />
								</th>
								<th class="accion">
									<bean:message key="resultado.observatorio.rastreo.realizado.resultados" />
								</th>
								<th class="accion">
									<bean:message key="resultado.observatorio.rastreo.realizado.informe.agregado" />
								</th>
								<th class="accion">
									<bean:message key="resultado.observatorio.rastreo.realizado.informes.individuales" />
								</th>
								<th class="accion">
									<bean:message key="resultado.observatorio.rastreo.realizado.graficas.agregadas" />
								</th>
								<th class="accion">
									<bean:message key="resultado.observatorio.rastreo.realizado.anexos" />
								</th>
								<th class="accion">
									<bean:message key="resultado.observatorio.rastreo.realizado.send" />
								</th>
								<th class="accion">
									<bean:message key="resultado.observatorio.rastreo.realizado.eliminar" />
								</th>
							</tr>
							<jsp:useBean id="params" class="java.util.HashMap" />
							<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>" />
							<bean:define id="id_ex_obs" value="<%=Constants.ID_EX_OBS%>" />
							<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
							<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES %>" id="fulfilledObservatory">
								<c:set target="${params}" property="idCartucho" value="${fulfilledObservatory.cartucho.id}" />
								<c:set target="${params}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
								<tr>
									<td>
										<bean:write name="fulfilledObservatory" property="fechaStr" />
									</td>
									<td>
										<bean:write name="fulfilledObservatory" property="cartucho.name" />
									</td>
									<td>
										<div class='tagbox-wrapper'>
											<logic:iterate name="fulfilledObservatory" property="tags" id="etiqueta">
												<c:if test="${etiqueta!= null}">
													<div class='tagbox-token'>
														<span>
															<bean:write name="etiqueta" />
														</span>
													</div>
												</c:if>
												<c:if test="${etiqueta == null}">
										-
									</c:if>
											</logic:iterate>
										</div>
									</td>
									<td><jsp:useBean id="paramsRelanzar" class="java.util.HashMap" />
										<c:set target="${paramsRelanzar}" property="action" value="confirm" />
										<c:set target="${paramsRelanzar}" property="id_observatorio" value="${id_observatorio}" />
										<c:set target="${paramsRelanzar}" property="idExObs" value="${fulfilledObservatory.id}" />
										<c:set target="${paramsRelanzar}" property="idCartucho" value="${fulfilledObservatory.cartucho.id}" />
										<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="4">
											<bean:message key="resultado.observatorio.rastreo.realizado.estado.parado" />
											<html:link forward="estadoObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-info-sign" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.obs'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.obs' />
												</span>
											</html:link>
											<html:link forward="relanzarObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.relaunch'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.relaunch' />
												</span>
											</html:link>
											<html:link forward="estadoObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-info-sign" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.obs'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.obs' />
												</span>
											</html:link>
										</logic:equal>
										<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="3">
											<bean:message key="resultado.observatorio.rastreo.realizado.estado.relanzado" />
											<html:link forward="estadoObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-info-sign" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.obs'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.obs' />
												</span>
											</html:link>
											<html:link forward="stopObservatorio" name="params">
												<span class="glyphicon glyphicon-stop" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.stop' />"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.stop' />
												</span>
											</html:link>
										</logic:equal>
										<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="1">
											<bean:message key="resultado.observatorio.rastreo.realizado.estado.lanzado" />
											<html:link forward="relanzarObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.relaunch'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.relaunch' />
												</span>
											</html:link>
											<html:link forward="stopObservatorio" name="params">
												<span class="glyphicon glyphicon-stop" aria-hidden="true" data-toggle="tooltip" title="Parar ejecución" />
												<span class="sr-only">Parar</span>
											</html:link>
											<html:link forward="estadoObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-info-sign" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.obs'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.obs' />
												</span>
											</html:link>
										</logic:equal>
										<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="0">
											<bean:message key="resultado.observatorio.rastreo.realizado.estado.terminado" />
											<html:link forward="estadoObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-info-sign" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.obs'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.obs' />
												</span>
											</html:link>
										</logic:equal>
										<logic:equal name="fulfilledObservatory" property="observatorio.estado" value="2">
											<bean:message key="resultado.observatorio.rastreo.realizado.estado.error" />
											<html:link forward="estadoObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-info-sign" aria-hidden="true" data-toggle="tooltip"
													title="Estado de  esta iteraci&oacute;n del observatorio"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.obs' />
												</span>
											</html:link>
											<html:link forward="relanzarObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.relaunch'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.relaunch' />
												</span>
											</html:link>
											<html:link forward="estadoObservatorio" name="paramsRelanzar">
												<span class="glyphicon glyphicon-info-sign" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.obs'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.obs' />
												</span>
											</html:link>
										</logic:equal>
									</td>
									<td>
										<html:link forward="resultadosObservatorioSemillas" name="params">
											<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key='tooltip.status.results'/>"></span>
											<span class="sr-only">
												<bean:message key='results' />
											</span>
										</html:link>
									</td>
									<td><jsp:useBean id="paramsInformeAgregado" class="java.util.HashMap" />
										<c:set target="${paramsInformeAgregado}" property="idCartucho" value="${fulfilledObservatory.cartucho.id}" />
										<c:set target="${paramsInformeAgregado}" property="id_observatorio" value="${id_observatorio}" />
										<c:set target="${paramsInformeAgregado}" property="id" value="${fulfilledObservatory.id}" />
										<c:set target="${paramsInformeAgregado}" property="esPrimera" value="true" />
										<c:set target="${paramsInformeAgregado}" property="isPrimary" value="false" />
										<c:set target="${paramsInformeAgregado}" property="idExObs" value="${fulfilledObservatory.id}" />
										<logic:notEqual name="fulfilledObservatory" property="observatorio.estado" value="1">
											<html:link forward="anonymousExportOpenOfficeAction" name="paramsInformeAgregado">
												<span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip"
													title="<bean:message key='tooltip.status.report.odt'/>"></span>
												<span class="sr-only">
													<bean:message key='tooltip.status.report.odt' />
												</span>
											</html:link>
										</logic:notEqual>
									</td>
									<td><jsp:useBean id="paramsExportPDF" class="java.util.HashMap" />
										<c:set target="${paramsExportPDF}" property="id_observatorio" value="${id_observatorio}" />
										<c:set target="${paramsExportPDF}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
										<html:link forward="<%=Constants.EXPORT_ALL_PDF_FORWARD%>" name="paramsExportPDF">
											<span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key='tooltip.status.report.pdf'/>"></span>
											<span class="sr-only">
												<bean:message key='tooltip.status.report.pdf' />
											</span>
										</html:link>
										</li>
										<jsp:useBean id="paramsExportPDFMail" class="java.util.HashMap" />
										<c:set target="${paramsExportPDFMail}" property="id_observatorio" value="${id_observatorio}" />
										<c:set target="${paramsExportPDFMail}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
										<html:link forward="<%=Constants.EXPORT_ALL_PDF_FORWARD_MAIL%>" name="paramsExportPDFMail">
											<span class="glyphicon glyphicon-send" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key='tooltip.status.report.pdf.email'/>"></span>
											<span class="sr-only">
												<bean:message key='tooltip.status.report.pdf.email' />
											</span>
										</html:link>
									</td>
									<td><jsp:useBean id="paramsGraphic" class="java.util.HashMap" />
										<c:set target="${paramsGraphic}" property="id" value="${fulfilledObservatory.id}" />
										<c:set target="${paramsGraphic}" property="id_observatorio" value="${id_observatorio}" />
										<c:set target="${paramsGraphic}" property="graphic" value="initial" />
										<c:set target="${paramsGraphic}" property="Otype" value="${fulfilledObservatory.cartucho.id}" />
										<html:link forward="getObservatoryGraphic" name="paramsGraphic">
											<span class="glyphicon glyphicon-stats" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key='tooltip.status.obs.graphics'/>"></span>
											<span class="sr-only">
												<bean:message key="migas.indice.observatorios.menu.graficas" />
											</span>
										</html:link>
									</td>
									<td>
										<html:link forward="databaseExportActionConfirm" name="paramsInformeAgregado">
											<span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key="indice.rastreo.exportar.database" />"></span>
											<span class="sr-only">
												<bean:message key="indice.rastreo.exportar.database" />
											</span>
										</html:link>
									</td>
									<td>
										<html:link forward="configSendObservatoryResultsByMail" name="paramsInformeAgregado">
											<span class="glyphicon glyphicon-envelope" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key="indice.rastreo.send.database.config" />"></span>
											<span class="sr-only">
												<bean:message key="indice.rastreo.send.database.config" />
											</span>
										</html:link>
										<!--<html:link forward="confirmSendObservatoryResultsByMail" name="paramsInformeAgregado">
											<span class="glyphicon glyphicon-envelope" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key="indice.rastreo.exportar.database" />"></span>
											<span class="sr-only">
												<bean:message key="indice.rastreo.send.database" />
											</span>
										</html:link>-->
									</td>
									<td><jsp:useBean id="paramsDeleteExecution" class="java.util.HashMap" />
										<c:set target="${paramsDeleteExecution}" property="id_observatorio" value="${id_observatorio}" />
										<c:set target="${paramsDeleteExecution}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
										<c:set target="${paramsDeleteExecution}" property="esPrimera" value="true" />
										<c:set target="${paramsDeleteExecution}" property="isPrimary" value="true" />
										<c:set target="${paramsDeleteExecution}" property="idExObs" value="${fulfilledObservatory.id}" />
										<html:link forward="deleteFulfilledObservatory" name="paramsDeleteExecution">
											<span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key='tooltip.status.obs.remove'/>"></span>
											<span class="sr-only">
												<bean:message key='tooltip.status.obs.remove' />
											</span>
										</html:link>
									</td>
								</tr>
							</logic:iterate>
						</table>
						<jsp:include page="pagination.jsp" />
					</logic:notEmpty>
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
</inteco:sesion>
