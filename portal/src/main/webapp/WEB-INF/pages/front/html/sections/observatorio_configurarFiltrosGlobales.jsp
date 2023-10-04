<!--
Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
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
<script src="https://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="/oaw/js/tagbox/tagbox.js" type="text/javascript"></script>
<link rel="stylesheet" href="/oaw/js/tagbox/tagbox.css">
<style>
/* Make sure you reset e'erything beforehand. */
* {
	margin: 0;
	padding: 0;
}

/* Although you can't see the box here, so add some padding. */
.tagbox-item .name, .tagbox-item .email { /* The name and email within the dropdown */
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
</style>

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

			})

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
				<li><html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link></li>
				<li class="active"><bean:message key="report.config.title" /></li>
			</ol>
		</div>


		<div id="cajaformularios" style="background: #FCFCFC !important;">

			<h2>
				<bean:message key="configurar.agregado.title" />
			</h2>



			<jsp:include page="/common/crawler_messages.jsp" />
			<div class="formulario">

				<form action="/oaw/secure/NuevoInformeGlobalObservatorio.do">


					<fieldset>
						<legend>
							<bean:message key="report.config.title.title" />
						</legend>
						<div class="formItem">

							<label for="url" class="control-label"><strong class="labelVisu"><bean:message
										key="report.config.title.label" /> </strong></label> <input style="width: 50%;" name="reportTitle" id="reportTitle"
								type="text" value="<bean:message key="report.config.title.default" /> <c:out value="${ambito}" />" />
						</div>
					</fieldset>


					<fieldset>
						<legend>
							<bean:message key="report.config.etiquetas.filter.title" />
						</legend>
						<div class="formItem">

							<label for="url" class="control-label"><strong class="labelVisu"><bean:message
										key="report.config.tags.title" /> </strong></label> <input name="tags" autocapitalize="off"
								placeholder="<bean:message key="placeholder.tags" />" autofocus id="tagsFilter" type="text" value="" />


						</div>

						<p class="alert alert-info">
							<span class="glyphicon glyphicon-info-sign"></span> <em><bean:message key="nueva.semilla.webs.informacion" />
							</em>:
							<bean:message key="report.config.etiquetas.filter.info" />
						</p>




					</fieldset>

					<fieldset>
						<legend>
							<bean:message key="report.config.observatorios.filter.title.global" />
						</legend>

						<div class="formItem">

							<table class="table table-stripped table-bordered table-hover">
								<caption>
									<bean:message key="indice.observatorio.lista" />
								</caption>
								<thead>
									<tr>
										<th></th>
										<th><bean:message key="indice.observatorio.nombre" /></th>
										<th><bean:message key="nuevo.observatorio.tipo" /></th>
										<th><bean:message key="nuevo.observatorio.ambito" /></th>
										<th><bean:message key="indice.observatorio.cartucho" /></th>
										<th><bean:message key="indice.rastreo.realizado.fecha" /></th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES%>" id="fulfilledObservatory">
										<tr>

											<td><input type="checkbox" value="<c:out value="${fulfilledObservatory.idObsEx}" />" name="evol"></td>
											<td><bean:write name="fulfilledObservatory" property="nombre" /></td>
											<td><bean:write name="fulfilledObservatory" property="tipo" /></td>
											<td><bean:write name="fulfilledObservatory" property="ambito" /></td>
											<td><bean:write name="fulfilledObservatory" property="cartucho" /></td>
											<td><bean:write name="fulfilledObservatory" property="fechaExSting" /></td>
										</tr>
									</logic:iterate>
								</tbody>
							</table>

						</div>
					</fieldset>

					<fieldset>
						<legend>
							<bean:message key="report.config.plantillas.filter.title" />
						</legend>
						<div class="formItem">

							<label for="url" class="control-label"><strong class="labelVisu"><abbr
									title="<bean:message key="campo.obligatorio" />"> * </abbr> <bean:message
										key="report.config.plantillas.filter.base" /></strong></label> <select required name="<%=Constants.ID_BASE_TEMPLATE%>">
								<option value=""></option>
								<logic:iterate name="plantillas" id="plantilla">

									<option value="<bean:write name="plantilla" property="id" />"><bean:write name="plantilla"
											property="nombre" /></option>

								</logic:iterate>
							</select>

						</div>
					</fieldset>
					<input type="hidden" name="action" value="report"> <input type="submit" class="btn btn-primary btn-lg"
						value=<bean:message key="boton.enviar" />>
				</form>
			</div>


		</div>
		<!-- fin cajaformularios -->
	</div>

</div>
