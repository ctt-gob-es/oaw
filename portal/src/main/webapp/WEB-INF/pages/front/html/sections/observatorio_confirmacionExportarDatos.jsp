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
<bean:parameter name="<%=Constants.ID_OBSERVATORIO%>" id="idObservatorio" />
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="/oaw/js/tagbox/tagbox.js" type="text/javascript"></script>
<link rel="stylesheet" href="/oaw/js/tagbox/tagbox.css">
<script>
	$(window).on('load', function() {

		var $jq = $.noConflict();
		$jq(document).ready(function() {
			$.ajax({
				url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=all',
				method : 'POST',
				cache : false
			}).success(function(response) {

				$('#tagsFilter').tagbox({
					items : response.etiquetas,
					searchIn : [ 'name' ],
					rowFormat : '<span class="name">{{name}}</span>',
					tokenFormat : '{{name}}',
					valueField : 'id',
					itemClass : 'user'
				});

			});
		});

	});
</script>
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
			<form action="/oaw/secure/databaseExportAction.do">
				<h2>
					<bean:message key="exportar.resultados.observatorio.title" />
				</h2>
				<fieldset>
					<legend>
						<bean:message key="report.config.etiquetas.filter.title" />
					</legend>
					<div class="formItem">
						<label for="url" class="control-label">
							<strong class="labelVisu">
								<bean:message key="report.config.tags.title" />
							</strong>
						</label>
						<input name="tags" autocapitalize="off" placeholder="<bean:message key="placeholder.tags" />" autofocus
							id="tagsFilter" type="text" value="" />
					</div>
					<p class="alert alert-info">
						<span class="glyphicon glyphicon-info-sign"></span>
						<em>
							<bean:message key="nueva.semilla.webs.informacion" />
						</em>
						:
						<bean:message key="report.config.etiquetas.filter.info" />
					</p>
				</fieldset>
				<fieldset>
					<legend>
						<bean:message key="report.config.observatorios.filter.title" />
					</legend>
					<div class="formItem">
						<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES%>" id="fulfilledObservatory">
							<c:if test="${fulfilledObservatory.id == param.idExObs}">
								<label class="label100">
									<input type="checkbox" checked value="<c:out value="${fulfilledObservatory.id}" />" name="evol">
									<bean:write name="fulfilledObservatory" property="fechaStr" />
									(
									<bean:message key="current" />
									)
								</label>
							</c:if>
							<c:if test="${fulfilledObservatory.id != param.idExObs}">
								<label class="label100">
									<input type="checkbox" value="<c:out value="${fulfilledObservatory.id}" />" name="evol">
									<bean:write name="fulfilledObservatory" property="fechaStr" />
								</label>
							</c:if>
						</logic:iterate>
					</div>
				</fieldset>
				<fieldset>
					<legend>
						<bean:message key="confirmacion.exportar.resultados.observatorio.recurrencia.fija" />
					</legend>
					<div class="formItem">
						<label for="url" class="control-label">
							<strong class="labelVisu">
<%-- 								<abbr title="<bean:message key="campo.obligatorio" />"> * </abbr> --%>
								<bean:message key="confirmacion.exportar.resultados.observatorio.recurrencia.primera" />
							</strong>
						</label>
						<select  name="compareFixedFirst">
							<option value=""></option>
							<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES%>" id="fulfilledObservatory">
								<option value="<bean:write name="fulfilledObservatory" property="id" />"><bean:write
										name="fulfilledObservatory" property="fechaStr" /></option>
							</logic:iterate>
						</select>
					</div>
					<div class="formItem">
						<label for="url" class="control-label">
							<strong class="labelVisu">
<%-- 								<abbr title="<bean:message key="campo.obligatorio" />"> * </abbr> --%>
								<bean:message key="confirmacion.exportar.resultados.observatorio.recurrencia.anterior" />
							</strong>
						</label>
						<select  name="compareFixedLast">
							<option value=""></option>
							<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES%>" id="fulfilledObservatory">
								<option value="<bean:write name="fulfilledObservatory" property="id" />"><bean:write
										name="fulfilledObservatory" property="fechaStr" /></option>
							</logic:iterate>
						</select>
					</div>
				</fieldset>
				<fieldset>
					<legend>
						<bean:message key="confirmacion.exportar.resultados.observatorio.recurrencia.variable" arg0="--reemplazar--" />
					</legend>
					<div class="formItem">
						<label for="url" class="control-label">
							<strong class="labelVisu">
<%-- 								<abbr title="<bean:message key="campo.obligatorio" />"> * </abbr> --%>
								<bean:message key="confirmacion.exportar.resultados.observatorio.recurrencia.primera" />
							</strong>
						</label>
						<select name="compareFixedFirst">
							<option value=""></option>
							<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES%>" id="fulfilledObservatory">
								<option value="<bean:write name="fulfilledObservatory" property="id" />"><bean:write
										name="fulfilledObservatory" property="fechaStr" /></option>
							</logic:iterate>
						</select>
					</div>
					<div class="formItem">
						<label for="url" class="control-label">
							<strong class="labelVisu">
<%-- 								<abbr title="<bean:message key="campo.obligatorio" />"> * </abbr> --%>
								<bean:message key="confirmacion.exportar.resultados.observatorio.recurrencia.anterior" />
							</strong>
						</label>
						<select name="compareFixedLast">
							<option value=""></option>
							<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES%>" id="fulfilledObservatory">
								<option value="<bean:write name="fulfilledObservatory" property="id" />"><bean:write
										name="fulfilledObservatory" property="fechaStr" /></option>
							</logic:iterate>
						</select>
					</div>
				</fieldset>
				<fieldset>
					<p>
						<strong class="labelVisu">
							<bean:message key="confirmacion.exportar.resultados.observatorio.pregunta" />
						</strong>
					</p>
					<p>
						<strong class="labelVisu">
							<bean:message key="confirmacion.exportar.resultados.observatorio.info" />
						</strong>
					</p>
					<div class="formItem">
						<label>
							<strong class="labelVisu">
								<bean:message key="confirmacion.eliminar.observatorio.nombre" />
								:
							</strong>
						</label>
						<p>
							<bean:write name="<%=Constants.OBSERVATORY_FORM%>" property="nombre" />
						</p>
					</div>
				</fieldset>
				<div class="formButton">
					<%-- <jsp:useBean id="params" class="java.util.HashMap" />
				<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>" />
				<bean:parameter id="idExObs" name="<%= Constants.ID_EX_OBS %>" />
				<bean:parameter id="idCartucho" name="<%= Constants.ID_CARTUCHO %>" />
				<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
				<c:set target="${params}" property="idExObs" value="${idExObs}" />
				<c:set target="${params}" property="idCartucho" value="${idCartucho}" />
				<html:link styleClass="btn btn-primary btn-lg" forward="databaseExportActionExport" name="params">
					<bean:message key="boton.aceptar" />
				</html:link>--%>
					<input type="hidden" name="action" value="export" />
					<input type="hidden" name="idCartucho" value="<c:out value="${param.idCartucho}"/>" />
					<input type="hidden" name="id_observatorio" value="<c:out value="${param.id_observatorio}"/>" />
					<input type="hidden" name="idExObs" value="<c:out value="${param.idExObs}"/>" />
					<input type="submit" class="btn btn-primary btn-lg" value=<bean:message key="boton.enviar" />>
					<html:link styleClass="btn btn-default btn-lg" forward="observatoryMenu">
						<bean:message key="boton.cancelar" />
					</html:link>
				</div>
			</form>
		</div>
		<!-- fin cajaformularios -->
	</div>
</div>