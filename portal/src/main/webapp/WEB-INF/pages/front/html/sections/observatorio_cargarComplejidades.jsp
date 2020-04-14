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
<html:javascript formName="ComplejidadForm" />

<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">

<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js"
	type="text/javascript"></script>


<!--  JQ GRID   -->
<script>
	var scroll;
	
	var colNameOldName = '<bean:message key="colname.oldname"/>';
	var colNameId = '<bean:message key="colname.id"/>';
	var colNameName = '<bean:message key="colname.name"/>';
	var colNameRemove = '<bean:message key="colname.remove"/>';
	
	var colNameDepth = '<bean:message key="colname.depth"/>';
	var colNameWidth = '<bean:message key="colname.width"/>';
	
	
	
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

							ajaxJson = JSON.stringify(data.complejidades);

							total = data.paginador.total;

							$('#grid')
									.jqGrid(
											{
												editUrl : '/oaw/secure/ViewComplejidadesObservatorio.do?action=update',
												colNames : [ colNameId, colNameName, colNameOldName,
													colNameDepth, colNameWidth,
													colNameRemove ],
												colModel : [
														{
															name : "id",
															hidden : true,
															sortable : false
														},

														{
															name : "name",
															width : 20,
															editrules : {
																required : true
															},
															sortable : false,
															align : "center"
														},
														{
															name : "nombreAntiguo",
															formatter : nombreAntiguoFormatter,
															hidden : true,
															sortable : false
														},
														{
															name : "profundidad",
															formatter : profundidadFormatter,
															hidden : false,
															sortable : false
														},
														{
															name : "amplitud",
															formatter : amplitudFormatter,
															hidden : false,
															sortable : false
														},
														{
															name : "eliminar",
															width : 20,
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
																		url : '/oaw/secure/ViewComplejidadesObservatorio.do?action=update',
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
																				url : '/oaw/secure/ViewComplejidadesObservatorio.do?action=update',
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
		return "<span style='cursor:pointer' onclick='eliminarComplejidad("
				+ options.rowId
				+ ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>" + colNameRemove + "</span></span>";
	}
	
	function profundidadFormatter(cellvalue, options, rowObject) {
		if (rowObject.profundidad != null) {

			return rowObject.profundidad;

		} else {
			return "";
		}
	}
	
	function amplitudFormatter(cellvalue, options, rowObject) {
		if (rowObject.amplitud != null) {

			return rowObject.amplitud;

		} else {
			return "";
		}
	}

	function eliminarComplejidad(rowId) {

		var complejidad = $('#grid').jqGrid('getRowData', rowId);
		
		
		
	var windowTitle = '<bean:message key="complex.delete.modal.title"/>';
		
		var saveButton = '<bean:message key="boton.aceptar"/>';
		
		var cancelButton = '<bean:message key="boton.cancelar"/>';
		
		var confirmRemoveMessage = '<bean:message key="complex.delete.modal.confirm"/>';

		var idComplejidad = complejidad.id;
		var dialogoEliminar = $('<div id="dialogoEliminarContent"></div>');

		dialogoEliminar.append('<p>'+confirmRemoveMessage+' "'
				+ complejidad.name + '"?</p>');

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
											url : '/oaw/secure/ViewComplejidadesObservatorio.do?action=delete&idComplejidad='
													+ idComplejidad,
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
									reloadGrid('/oaw/secure/ViewComplejidadesObservatorio.do?action=search');

								});

			});

	var windowWidth = $(window).width() * 0.5;
	var windowHeight = $(window).height() * 0.4;

	var dialog;

	function dialogoNuevaComplejidad() {

		
		var windowTitle = '<bean:message key="complex.new.modal.title"/>';
		
		var saveButton = '<bean:message key="boton.guardar"/>';
		
		var cancelButton = '<bean:message key="boton.cancelar"/>';
		
		
		
		window.scrollTo(0, 0);

		$('#exitosNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').hide();

		dialog = $("#dialogoNuevaComplejidad").dialog({
			height : windowHeight,
			width : windowWidth,
			modal : true,
			title : windowTitle,
			buttons : {
				"Guardar" : {
					click: function() {
						guardarNuevaComplejidad();
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
				$('#nuevaComplejidadForm')[0].reset();
			}
		});
	}

	function guardarNuevaComplejidad() {
		$('#exitosNuevaSemillaMD').hide();
		$('#exitosNuevaSemillaMD').html("");
		$('#erroresNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').html("");

		var guardado = $.ajax({
			url : '/oaw/secure/ViewComplejidadesObservatorio.do?action=save',
			data : $('#nuevaComplejidadForm').serialize(),
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


<!-- observatorio_cargarComplejidades.jsp -->
<div id="main">


	<div id="dialogoNuevaComplejidad" style="display: none">
		<div id="main" style="overflow: hidden">

			<div id="erroresNuevaSemillaMD" style="display: none"></div>

			<form id="nuevaComplejidadForm">
				<!-- Nombre -->
				<div class="row formItem">
					<label for="nombre" class="control-label"
						style="margin-left: 25px;"><strong class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="nueva.complejidad.observatorio.nombre" /></strong></label>
					<div class="col-xs-8">
						<input type="text" id="nombre" name="nombre"
							class="textoLargo form-control" />
					</div>
				</div>
				<!-- Profundidad -->
				<div class="row formItem">
					<label for="profundidad" class="control-label"
						style="margin-left: 25px;"><strong class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="nueva.complejidad.observatorio.profundidad" /></strong></label>
					<div class="col-xs-8">
						<input type="text" id="profundidad" name="profundidad"
							class="textoCorto form-control" />
					</div>
				</div>
				<!-- Amplitud -->
				<div class="row formItem">
					<label for="amplitud" class="control-label"
						style="margin-left: 25px;"><strong class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="nueva.complejidad.observatorio.amplitud" /></strong></label>
					<div class="col-xs-8">
						<input type="text" id="amplitud" name="amplitud"
							class="textoCorto form-control" />
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
						key="migas.complejidades.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.complejidades.observatorio.titulo" />
			</h2>

			<div id="exitosNuevaSemillaMD" style="display: none"></div>

			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg"
					onclick="dialogoNuevaComplejidad()"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true"
					data-toggle="tooltip" title=""
					data-original-title="Crear una semilla"></span> <bean:message
						key="nueva.complejidad.observatorio" />
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
