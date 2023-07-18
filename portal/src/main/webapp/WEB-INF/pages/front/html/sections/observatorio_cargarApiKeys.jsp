<!--
Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Funci�n P�blica, 
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
<html:javascript formName="ApiKeyForm"/>

<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script>
	var script = document.createElement('script');
	var lang = (navigator.language || navigator.browserLanguage)
	script.src = '/oaw/js/jqgrid/i18n/grid.locale-'+lang.substring(0,2)+'.js';
	document.head.appendChild(script);
</script>

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

<script src="/oaw/js/gridApiKeys.js" type="text/javascript"></script>


<!--  JQ GRID   -->
<script type="text/javascript">
var $jn = jQuery.noConflict();

var paginadorTotal = '<bean:message key="cargar.semilla.observatorio.buscar.total"/>'; 


var colNameOldName = '<bean:message key="colname.oldname"/>';
var colNameId = '<bean:message key="colname.id"/>';

var colNameName = '<bean:message key="colname.name"/>';
var colNameRemove = '<bean:message key="colname.remove"/>';


var windowTitleRemove = '<bean:message key="nueva.etiqueta.observatorio.modal.eliminar.title"/>';

var saveButton = '<bean:message key="boton.aceptar"/>';

var cancelButton = '<bean:message key="boton.cancelar"/>';

var confirmRemoveMessage = '<bean:message key="nueva.etiqueta.observatorio.modal.eliminar.confirm"/>';


var dialog;

var windowWidth = $(window).width() * 0.4;
var windowHeight = $(window).height() * 0.3;


function dialogoNuevaApiKey() {

	window.scrollTo(0, 0);

	$('#exitosNuevaApiKey').hide();
	$('#erroresNuevaSemillaMD').hide();

	
	var windowTitle = '<bean:message key="nueva.etiqueta.observatorio.modal.title"/>';
	
	var saveButton = '<bean:message key="boton.guardar"/>';
	
	var cancelButton = '<bean:message key="boton.cancelar"/>';
	
	
	
	dialog = $("#dialogoNuevaApiKey").dialog({
		height : windowHeight,
		width : windowWidth,
		modal : true,
		title : windowTitle,
		buttons : {
			"Guardar" : {
				click: function() {
					guardarNuevaApiKey();
				},
				text : saveButton,
				class: 'jdialog-btn-save'
			},
			"Cancelar" : {
				click: function() {
					dialog.dialog("close");
				},
				text: cancelButton,
				class: 'jdialog-btn-cancel'
			}
		},
	});
}

function guardarNuevaApiKey() {
	$('#exitosNuevaApiKey').hide();
	$('#exitosNuevaApiKey').html("");
	$('#erroresNuevaSemillaMD').hide();
	$('#erroresNuevaSemillaMD').html("");

	var guardado = $.ajax({
		url : '/oaw/secure/ApiKey.do?action=save',
		data : $('#nuevaApiKeyForm').serialize(),
		method : 'POST',
		cache : false
	}).success(
			function(response) {
				$('#exitosNuevaApiKey').addClass('alert alert-success');
				$('#exitosNuevaApiKey').append("<ul>");

				$.each(JSON.parse(response), function(index, value) {
					$('#exitosNuevaApiKey').append(
							'<li>' + value.message + '</li>');
				});

				$('#exitosNuevaApiKey').append("</ul>");
				$('#exitosNuevaApiKey').show();
				dialog.dialog("close");
				reloadGrid(lastUrl);

			}).error(
			function(response) {
				$('#erroresNuevaSemillaMD').addClass('alert alert-danger');
				$('#erroresNuevaSemillaMD').append("<ul>");

				$.each(JSON.parse(response.responseText), function(index,
						value) {
					$('#erroresNuevaSemillaMD').append(
							'<li>' + value.message + '</li>');
				});

				$('#erroresNuevaSemillaMD').append("</ul>");
				$('#erroresNuevaSemillaMD').show();

			}

	);

	return guardado;
	
}


</script>


<!-- observatorio_cargarApiKeys.jsp -->
<div id="main">


	<div id="dialogoNuevaApiKey" style="display: none">
		<div id="main" style="overflow: hidden">

			<div id="erroresNuevaSemillaMD" style="display: none"></div>

			<form id="nuevaApiKeyForm">
				<!-- Nombre -->
				<div class="row formItem">
					<label for="nombre" class="control-label"><strong
						class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="nombre" /></strong></label>
					<div class="col-xs-6">
						<input type="text" id="nombre" name="nombre"
							class="textoLargo form-control" />
					</div>
				</div>

				<div class="row formItem">
					<label for="tipo" class="control-label"><strong
						class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="tipo" /></strong></label>
					<div class="col-xs-6">
						<input type="text" id="type" name="tipo"
							class="textoLargo form-control" />
					</div>
				</div>

				<div class="row formItem">
					<label for="tipo" class="control-label"><strong
						class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="descripcion" /></strong></label>
					<div class="col-xs-6">
						<input type="text" id="description" name="descripcion"
							class="textoLargo form-control" />
					</div>
				</div>

				


			</form>
		</div>
	</div>


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
				<li class="active"><bean:message
						key="migas.etiquetas.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.etiquetas.observatorio.titulo" />
			</h2>

			<div id="exitosNuevaApiKey" style="display: none"></div>

			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg"
					onclick="dialogoNuevaApiKey()"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true"
					data-toggle="tooltip" title=""
					data-original-title="Crear una apiKey"></span> <bean:message
						key="nueva.etiqueta.observatorio" />
				</a>
			</p>
			<!-- Grid -->
			<table id="grid">
			</table>



			<p id="paginador"></p>

		</div>
		<p id="pCenter">
			<html:link forward="observatoryMenu"
				styleClass="btn btn-default btn-lg">
				<bean:message key="boton.volver" />
			</html:link>
		</p>
	</div>
	<!-- fin cajaformularios -->
</div>
