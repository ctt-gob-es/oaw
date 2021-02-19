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
<html:javascript formName="DependenciaForm" />
<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script>
	var script = document.createElement('script');
	var lang = (navigator.language || navigator.browserLanguage)
	script.src = '/oaw/js/jqgrid/i18n/grid.locale-'+lang.substring(0,2)+'.js';
	document.head.appendChild(script);
</script>
<!--  JQ GRID   -->
<script>

var colNameOldName = '<bean:message key="colname.oldname"/>';
var colNameId = '<bean:message key="colname.id"/>';
var colNameTags = '<bean:message key="colname.etiqeutas"/>';
var colNameName = '<bean:message key="colname.name"/>';
var colNameRemove = '<bean:message key="colname.remove"/>';
var colNameScope = '<bean:message key="colname.scope"/>';


	var scroll;
	//Recarga el grid. Recibe como parámetro la url de la acción con la información
	//de paginación.
	function reloadGrid(path) {

		lastUrl = path;

		// Mantener el scroll
		scroll = $(window).scrollTop();

		$('#grid').jqGrid('clearGridData')

		$
				.ajax({
					url : path,
					dataType : "json",
					cache : false
				})
				.done(

						function(data) {

							ajaxJson = JSON.stringify(data.dependencias);

							total = data.paginador.total;

							$('#grid')
									.jqGrid(
											{
												editUrl : '/oaw/secure/ViewDependenciasObservatorio.do?action=update',
												colNames : [ colNameId, colNameName,
													colNameOldName,colNameScope, colNameTags, "Emails", "Auto", "Official",
													colNameRemove ],
												colModel : [
														{
															name : "id",
															hidden : true,
															sortable : false
														},

														{
															name : "name",
															width : 30,
															editrules : {
																required : true
															},
															sortable : false,
															align : "left"
														},
														{
															name : "nombreAntiguo",
															formatter : nombreAntiguoFormatter,
															hidden : true,
															sortable : false
														},
														{														
															name : "ambitoaux",
															width : 15,
															edittype : "select",
															align : "center",
															editoptions : {

																dataUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=listAmbitos',
																buildSelect : function(
																		data) {

																	var response = jQuery
																			.parseJSON(data);
																	var s = '<select><option value=""></option>';

																	if (response
																			&& response.length) {
																		for (var i = 0, l = response.length; i < l; i++) {
																			var ri = response[i];
																			s += '<option class="dependenciaOption" value="'
																					+ ri.id
																					+ '">'
																					+ ri.name
																					+ '</option>';
																		}
																	}

																	return s
																			+ "</select>";
																}

															},
															// editrules : {
															// required : true
															// },
															formatter : ambitoFormatter,
															sortable : false

														},
														{
															name : "etiquetasSeleccionadas",
															cellattr : function(
																	rowId, val,
																	rawObject, cm,
																	rdata) {
																return 'title="'
																		+ titleEtiquetasFormatter(
																				val,
																				null,
																				rawObject)
																		+ '"';
															},
															align : "left",
															width : 25,
															edittype : 'custom',
															sortable : false,
															editoptions : {
																custom_element : tagEdit,
																custom_value : tagEditValue
															},
															editrules : {
																required : false
															},
															formatter : etiquetasFormatter,
															sortable : false
														},
														{
															name : "emails",
															width : 30,
															editrules : {
																required : true
															},
															sortable : false,
															align : "left", 
															formatter: emailsFormatter
														},
														{
															name : "official",
															align : "center",
															width : 10,
															template : "booleanCheckboxFa",
															edittype : "checkbox",
															editoptions : {
																value : "true:false"
															},
															sortable : false
														},
														{
															name : "sendAuto",
															align : "center",
															width : 10,
															template : "booleanCheckboxFa",
															edittype : "checkbox",
															editoptions : {
																value : "true:false"
															},
															sortable : false
														},														

														{
															name : "eliminar",
															width : 10,
															sortable : false,
															editable : false,
															formatter : eliminarFormatter,
														}

												],
												inlineEditing : {
													keys : true,
													defaultFocusField : "nombre"
												},
												cmTemplate : {
													autoResizable : true,
													editable : true
												},
												viewrecords : false,
												autowidth : true,
												pgbuttons : false,
												pgtext : false,
												pginput : false,
												hidegrid : false,
												altRows : true,
												mtype : 'POST',

												onSelectRow : function(rowid,
														status, e) {

													var $self = $(this), savedRow = $self
															.jqGrid(
																	"getGridParam",
																	"savedRow");
													if (savedRow.length > 0
															&& savedRow[0].id !== rowid) {
														$self.jqGrid(
																"restoreRow",
																savedRow[0].id);
													}

													$self
															.jqGrid(
																	"editRow",
																	rowid,
																	{
																		focusField : e.target,
																		keys : true,
																		url : '/oaw/secure/ViewDependenciasObservatorio.do?action=update',
																		restoreAfterError : false,
																		successfunc : function(
																				response) {
																			reloadGrid(lastUrl);
																		},
																		afterrestorefunc : function(
																				response) {
																			reloadGrid(lastUrl);
																		}

																	});

												},
												beforeSelectRow : function(
														rowid, e) {
													var $self = $(this), i, $td = $(
															e.target).closest(
															"td"), iCol = $.jgrid
															.getCellIndex($td[0]);

													// En la columna
													// eliminar desactivamos la
													// selección para evitar que se
													// active la edidion
													if (this.p.colModel[iCol].name === "eliminar") {
														return false;
													}

													savedRows = $self.jqGrid(
															"getGridParam",
															"savedRow");
													for (i = 0; i < savedRows.length; i++) {
														if (savedRows[i].id !== rowid) {
															// save currently
															// editing row
															$self
																	.jqGrid(
																			'saveRow',
																			savedRows[i].id,
																			{
																				successfunc : function(
																						response) {
																					reloadGrid(lastUrl);
																				},
																				afterrestorefunc : function(
																						response) {
																					reloadGrid(lastUrl);
																				},
																				url : '/oaw/secure/ViewDependenciasObservatorio.do?action=update',
																				restoreAfterError : false,
																			});

														}
													}
													return savedRows.length === 0;
												},
												gridComplete : function() {
													// Restaurar el scroll
													$(window).scrollTop(scroll);
												}
											}).jqGrid("inlineNav");

							// Recargar el grid
							$('#grid').jqGrid('setGridParam', {
								data : JSON.parse(ajaxJson)
							}).trigger('reloadGrid');

							$('#grid').unbind("contextmenu");

							// Mostrar sin resultados
							if (total == 0) {
								$('#grid')
										.append(
												'<tr role="row" class="ui-widget-content jqgfirstrow ui-row-ltr"><td colspan="9" style="padding: 15px !important;" role="gridcell">Sin resultados</td></tr>');
							}

							// Paginador
							paginas = data.paginas;

							$('#paginador').empty();

							//Si solo hay una página no pintamos el paginador
							if (paginas.length > 1) {

								$
										.each(
												paginas,
												function(key, value) {
													if (value.active == true) {
														$('#paginador')
																.append(
																		'<a href="javascript:reloadGrid(\''
																				+ value.path
																				+ '\')" class="'
																				+ value.styleClass
																				+ ' btn btn-default">'
																				+ value.title
																				+ '</a>');
													} else {
														$('#paginador')
																.append(
																		'<span class="' + value.styleClass
													+ ' btn">'
																				+ value.title
																				+ '</span>');
													}

												});
							}
						}).error(function(data) {
					console.log("Error")
					console.log(data)
				});

	}

	// Conservamos el nombre original para comprobaciones posteriores
	function nombreAntiguoFormatter(cellvalue, options, rowObject) {
		return rowObject.name;
	}

	function eliminarFormatter(cellvalue, options, rowObject) {
		return "<span style='cursor:pointer' onclick='eliminarDependencia("
				+ options.rowId
				+ ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>"+ colNameRemove +"</span></span>";
	}
	
	function emailsFormatter(cellvalue, options, rowObject) {
		return rowObject.emails.toString().replace(/\,/g, '\r\n');
	}
	
	function ambitoFormatter(cellvalue, options, rowObject) {
		if (rowObject.ambito && rowObject.ambito.name != null) {
			return rowObject.ambito.name;
		} else {
			return "";
		}
	}
	
	function titleEtiquetasFormatter(cellvalue, options, rowObject) {
		var cellFormatted = "";

		$.each(rowObject.idTag, function(index, value) {
			cellFormatted = cellFormatted + value.name + "\n";
		});

		return cellFormatted;
	}
	
	function etiquetasFormatter(cellvalue, options, rowObject) {

		var data = "";

		$.each(rowObject.idTag, function(index, value) {
			data = data + value.id + ",";
		});

		if (data) {
			data = data.slice(0, -1);
		}

		var cellFormatted = "<div class='tagbox-wrapper'>";

		$.each(rowObject.idTag, function(index, value) {
			cellFormatted = cellFormatted + "<div class='tagbox-token'><span>"
					+ value.name + "</span></div>";
		});

		cellFormatted = cellFormatted + "</div>";

		return cellFormatted;
	}
	
	function tagEdit(value, options, rowObject) {

		var element = document.createElement('input');

		element.setAttribute("name", "tags");
		element.setAttribute("id", "tagsFilter");
		element.setAttribute("type", "text");
		element.setAttribute("autocapitalize", "off");
		element.setAttribute("placeholder", "Escriba para buscar...");

		var seed = $('#grid').jqGrid('getRowData', options.rowId);

		var data = $("#grid").jqGrid('getGridParam', 'data');


		$.ajax({
			url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=all',
			method : 'POST',
			cache : false
		}).success(function(response) {
			
			
			$.each(data, function(index, item) {
				if (item.id == options.rowId) {

					var data = "";

					$.each(item.etiquetas, function(index, value) {
						data = data + value.id + ",";
					});

					if (data) {
						data = data.slice(0, -1);
					}

					element.setAttribute('value', data);

				}
			});

			$(element).tagbox({
				items : response.etiquetas,
				searchIn : [ 'name' ],
				rowFormat : '<span class="name">{{name}}</span>',
				tokenFormat : '{{name}}',
				valueField : 'id',
				itemClass : 'user'
			});

		});

		return element;

	}

	function tagEditValue(elem, operation, value) {

		if (operation === 'get') {
			return $(elem).val();
		} else if (operation === 'set') {
			$('input', elem).val(value);
		}
	}


	function eliminarDependencia(rowId) {
		
		
		
		var windowTitle = '<bean:message key="eliminar.dependencia.modal.title"/>';
		
		var saveButton = '<bean:message key="boton.aceptar"/>';
		
		var cancelButton = '<bean:message key="boton.cancelar"/>';
		
		var confirmRemoveMessage = '<bean:message key="eliminar.dependencia.modal.confirm"/>';
		

		var dependencia = $('#grid').jqGrid('getRowData', rowId);

		var idDependencia = dependencia.id;
		var dialogoEliminar = $('<div id="dialogoEliminarContent"></div>');

		dialogoEliminar.append('<p>'+ confirmRemoveMessage +' ""'+ dependencia.name + '"?</p>');

		dialogoEliminar
				.dialog({
					autoOpen : false,
					minHeight : $(window).height() * 0.25,
					minWidth : $(window).width() * 0.25,
					modal : true,
					title : windowTitle,
					buttons : {
						"Aceptar" : {
							click: function() {
								$
								.ajax(
										{
											url : '/oaw/secure/ViewDependenciasObservatorio.do?action=delete&idDependencia='
													+ idDependencia,
											method : 'POST',
											cache : false
										}).success(function(response) {
									reloadGrid(lastUrl);
									dialogoEliminar.dialog("close");
								});
					},
					text: saveButton,
					class: 'jdialog-btn-save'
						},
						"Cancelar" : {
							click:function() {
								dialogoEliminar.dialog("close");
							},
							text: cancelButton,
							class: 'jdialog-btn-cancel'
						}
					}
				});

		dialogoEliminar.dialog("open");
	}

	//Buscador
	function buscar() {
		reloadGrid('/oaw/secure/ViewDependenciasObservatorio.do?action=search&'
				+ $('#buscadorDependencias').serialize());
	}
	
	function limpiar(){
		$('#buscadorDependencias')[0].reset();
		reloadGrid('/oaw/secure/ViewDependenciasObservatorio.do?action=search&'
				+ $('#buscadorDependencias').serialize());
	}

	$(window)
			.on(
					'load',
					function() {

						var $jq = $.noConflict();

						var lastUrl;

						//Primera carga del grid el grid
						$jq(document)
								.ready(
										function() {
											reloadGrid('/oaw/secure/ViewDependenciasObservatorio.do?action=search');

											$('#buscadorDependencias').on(
													'keyup keypress',
													function(e) {
														var keyCode = e.keyCode
																|| e.which;
														if (keyCode === 13) {
															e.preventDefault();
															return false;
														}
													});

										});

					});

	var windowWidth = $(window).width() * 0.3;
	var windowHeight = $(window).height() * 0.3;

	var dialog;

	function dialogoNuevaDependencia() {

		window.scrollTo(0, 0);
		
		var windowTitle = '<bean:message key="nueva.dependencia.modal.title"/>';
		
		var saveButton = '<bean:message key="boton.guardar"/>';
		
		var cancelButton = '<bean:message key="boton.cancelar"/>';
		
		var confirmRemoveMessage = '<bean:message key="eliminar.dependencia.modal.confirm"/>';

		$('#exitosNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').hide();

		dialog = $("#dialogoNuevaDependencia").dialog({
			height : windowHeight,
			width : windowWidth,
			modal : true,
			title : windowTitle,
			buttons : {
				"Guardar" : {
					click: function() {
						guardarNuevaDependencia();
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
			close : function() {
				$('#nuevaDependenciaForm')[0].reset();
			}
		});
	}

	function guardarNuevaDependencia() {
		$('#exitosNuevaSemillaMD').hide();
		$('#exitosNuevaSemillaMD').html("");
		$('#erroresNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').html("");

		var guardado = $.ajax({
			url : '/oaw/secure/ViewDependenciasObservatorio.do?action=save',
			data : $('#nuevaDependenciaForm').serialize(),
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
<!-- observatorio_cargarDependencias.jsp -->
<div id="main">
	<div id="dialogoNuevaDependencia" style="display: none">
		<div id="main" style="overflow: hidden">
			<div id="erroresNuevaSemillaMD" style="display: none"></div>
			<form id="nuevaDependenciaForm">
				<!-- Nombre -->
				<div class="row formItem">
					<label for="nombre" class="control-label" style="margin-left: 25px;">
						<strong class="labelVisu">
							<acronym title="<bean:message key="campo.obligatorio" />"> * </acronym>
							<bean:message key="nueva.dependencia.observatorio.nombre" />
						</strong>
					</label>
					<div class="col-xs-8">
						<input type="text" id="nombre" name="nombre" class="textoLargo form-control" />
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
				<li>
					<html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link>
				</li>
				<li class="active">
					<bean:message key="migas.dependencias.observatorio" />
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.dependencias.observatorio.titulo" />
			</h2>
			<div id="exitosNuevaSemillaMD" style="display: none"></div>
			<form id="buscadorDependencias" class="formulario form-horizontal" onsubmit="buscar()">
				<fieldset>
					<legend>
						<bean:message key="buscador" />
					</legend>
					<jsp:include page="/common/crawler_messages.jsp" />
					<div class="formItem">
						<label for="nombre" class="control-label">
							<strong class="labelVisu">
								<bean:message key="nueva.dependencia.observatorio.nombre" />
							</strong>
						</label>
						<input type="text" class="texto form-control" id="nombre" name="nombre" />
					</div>
					<div class="formButton">
						<span onclick="buscar()" class="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							<bean:message key="boton.buscar" />
						</span>
						<span onclick="limpiar()" class="btn btn-default btn-lg">
							<span aria-hidden="true"></span>
							<bean:message key="boton.limpiar" />
						</span>
					</div>
				</fieldset>
			</form>
			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg" onclick="dialogoNuevaDependencia()">
					<span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title=""
						data-original-title="Crear una semilla"></span>
					<bean:message key="nueva.dependencia.observatorio" />
				</a>
			</p>
			<!-- Grid -->
			<table id="grid">
			</table>
			<p id="paginador"></p>
		</div>
		<p id="pCenter">
			<html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg">
				<bean:message key="boton.volver" />
			</html:link>
		</p>
	</div>
	<!-- fin cajaformularios -->
</div>
</div>
