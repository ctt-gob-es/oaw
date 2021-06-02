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
<html:javascript formName="EtiquetaForm" />

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

<script src="/oaw/js/gridEtiquetas.js" type="text/javascript"></script>


<!--  JQ GRID   -->
<script type="text/javascript">
var $jn = jQuery.noConflict();

var paginadorTotal = '<bean:message key="cargar.semilla.observatorio.buscar.total"/>'; 


var colNameOldName = '<bean:message key="colname.oldname"/>';
var colNameId = '<bean:message key="colname.id"/>';

var colNameName = '<bean:message key="colname.name"/>';
var colNameRemove = '<bean:message key="colname.remove"/>';

var colNameClassification = '<bean:message key="colname.classification"/>';


var windowTitleRemove = '<bean:message key="nueva.etiqueta.observatorio.modal.eliminar.title"/>';

var saveButton = '<bean:message key="boton.aceptar"/>';

var cancelButton = '<bean:message key="boton.cancelar"/>';

var confirmRemoveMessage = '<bean:message key="nueva.etiqueta.observatorio.modal.eliminar.confirm"/>';


//Cargar las clasificaciones
	function cargarSelect(rowObject) {

$jn('#selectClasificacionesNuevaEtiqueta').empty();
$jn
		.ajax(
				{
					url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=listClasificaciones',
				})
		.done(
				function(data) {

					var response = $jn.parseJSON(data);

					$jn('#selectClasificacionesNuevaEtiqueta').append();
					if (response && response.length) {
						for (var i = 0, l = response.length; i < l; i++) {
							var ri = response[i];
							$jn('#selectClasificacionesNuevaEtiqueta').append(
											'<option value="'+ri.id+'">'
													+ ri.nombre
													+ '</option>');
						}
					}

					if (rowObject != null) {

						$jn('#selectClasificacionesNuevaEtiqueta').val(
								rowObject.clasificacion.id);
					}

				});
}



var dialog;

var windowWidth = $(window).width() * 0.4;
var windowHeight = $(window).height() * 0.3;


function dialogoNuevaEtiqueta() {

	window.scrollTo(0, 0);

	$('#exitosNuevaSemillaMD').hide();
	$('#erroresNuevaSemillaMD').hide();

	
	var windowTitle = '<bean:message key="nueva.etiqueta.observatorio.modal.title"/>';
	
	var saveButton = '<bean:message key="boton.guardar"/>';
	
	var cancelButton = '<bean:message key="boton.cancelar"/>';
	
	
	
	dialog = $("#dialogoNuevaEtiqueta").dialog({
		height : windowHeight,
		width : windowWidth,
		modal : true,
		title : windowTitle,
		buttons : {
			"Guardar" : {
				click: function() {
					guardarNuevaEtiqueta();
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
		open : function() {
			cargarSelect();
		},
		close : function() {
			$('#nuevaEtiquetaForm')[0].reset();
		}
	});
}

function guardarNuevaEtiqueta() {
	$('#exitosNuevaSemillaMD').hide();
	$('#exitosNuevaSemillaMD').html("");
	$('#erroresNuevaSemillaMD').hide();
	$('#erroresNuevaSemillaMD').html("");

	var guardado = $.ajax({
		url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=save',
		data : $('#nuevaEtiquetaForm').serialize(),
		method : 'POST',
		cache : false
	}).success(
			function(response) {
				$('#exitosNuevaSemillaMD').addClass('alert alert-success');
				$('#exitosNuevaSemillaMD').append("<ul>");

				$.each(JSON.parse(response), function(index, value) {
					$('#exitosNuevaSemillaMD').append(
							'<li>' + value.message + '</li>');
				});

				$('#exitosNuevaSemillaMD').append("</ul>");
				$('#exitosNuevaSemillaMD').show();
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


<!-- observatorio_cargarEtiquetas.jsp -->
<div id="main">


	<div id="dialogoNuevaEtiqueta" style="display: none">
		<div id="main" style="overflow: hidden">

			<div id="erroresNuevaSemillaMD" style="display: none"></div>

			<form id="nuevaEtiquetaForm">
				<!-- Nombre -->
				<div class="row formItem">
					<label for="nombre" class="control-label"><strong
						class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="nueva.etiqueta.observatorio.nombre" /></strong></label>
					<div class="col-xs-6">
						<input type="text" id="nombre" name="nombre"
							class="textoLargo form-control" />
					</div>
				</div>

				<logic:present parameter="<%=Constants.ID_CLASIFICACION%>">
					<input type="hidden" name="clasificacionaux"
						value="<c:out value="${ClasificacionForm.id }" />" />
				</logic:present>
				<logic:notPresent parameter="<%=Constants.ID_CLASIFICACION%>">
					<!-- Clasificacion/Clasificacionaux -->
					<div class="row formItem">
						<label for="clasificacion" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.semilla.webs.clasificacion" /></strong></label>
						<div class="col-xs-4">
							<select name="clasificacionaux"
								id="selectClasificacionesNuevaEtiqueta"
								class="textoSelect form-control"></select>
						</div>
					</div>
				</logic:notPresent>


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

			<div id="exitosNuevaSemillaMD" style="display: none"></div>

			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg"
					onclick="dialogoNuevaEtiqueta()"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true"
					data-toggle="tooltip" title=""
					data-original-title="Crear una etiqueta"></span> <bean:message
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
