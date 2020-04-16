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
<html:javascript formName="PlantillaForm" />

<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">

<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js" type="text/javascript"></script>


<!--  JQ GRID   -->
<script>
	var scroll;
	
	var colNameId = '<bean:message key="colname.id"/>';
	var colNameName = '<bean:message key="colname.name"/>';
	var colNameUpload = '<bean:message key="colname.upload"/>';
	var colNameDownload = '<bean:message key="colname.download"/>';
	var colNameRemove = '<bean:message key="colname.remove"/>';

	
	
	function uploadFormatter(cellvalue, options, rowObject){
		//return "up";
		
		return "<span style='cursor:pointer' onclick='uploadPlantilla("
		+ options.rowId
		+ ")'class='glyphicon glyphicon-cloud-upload'></span><span class='sr-only'>"+colNameUpload+"</span></span>";
	}
	
	function downloadFormatter(cellvalue, options, rowObject){
		return "<span style='cursor:pointer' onclick='downloadPlantilla("
		+ options.rowId
		+ ")'class='glyphicon glyphicon-cloud-download'></span><span class='sr-only'>"+colNameDownload+"</span></span>";
	}

	function downloadPlantilla(rowId){
		window.location = '/oaw/secure/Plantilla.do?action=download&idPlantilla='+ rowId;
	}
	
	function uploadPlantilla(rowId){
		
		var plantilla = $('#grid').jqGrid('getRowData', rowId);
		
		$("#nuevaPlantillaForm #nombre").val(plantilla.nombre);
		$('#nuevaPlantillaForm #templateId').val(rowId);
		

		
		window.scrollTo(0, 0);

		$('#existosPlantilla').hide();
		$('#erroresPlantilla').hide();

		dialog = $("#dialogoNuevaPlantilla").dialog({
			height : windowHeight,
			width : windowWidth,
			modal : true,
			title : 'RASTREADOR WEB - Actualizar Plantilla',
			buttons : {
				"Guardar" : {
					click: function() {
						
						actualizarPlantilla();
					},
					text : "Guardar",
					class: 'jdialog-btn-save'
				},
				"Cancelar" : {
					click: function() {
						dialog.dialog("close");
					},
					text: "Cancelar",
					class: 'jdialog-btn-cancel'
				}
			},
			close : function() {
				$('#nuevaPlantillaForm')[0].reset();
			}
		});
		
	}

	
	function eliminarFormatter(cellvalue, options, rowObject) {
		return "<span style='cursor:pointer' onclick='eliminarPlantilla("
				+ options.rowId
				+ ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>"+colNameRemove+"</span></span>";
	}
	
	function eliminarPlantilla(rowId) {

		var plantilla = $('#grid').jqGrid('getRowData', rowId);

		var idPlantilla = plantilla.id;
		
		
		
		
	var windowTitle = '<bean:message key="plantilla.eliminar.modal.title"/>';
		
		var saveButton = '<bean:message key="boton.aceptar"/>';
		
		var cancelButton = '<bean:message key="boton.cancelar"/>';
		
		var confirmRemoveMessage = '<bean:message key="eliminar.dependencia.modal.confirm"/>';
		
		var dialogoEliminar = $('<div id="dialogoEliminarContent"></div>');

		dialogoEliminar.append('<p>'+confirmRemoveMessage+' "'
				+ plantilla.nombre + '"?</p>');
		
		
		
		

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
											url : '/oaw/secure/Plantilla.do?action=delete&idPlantilla='
													+ idPlantilla,
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

							ajaxJson = JSON.stringify(data.plantillas);

							total = data.paginador.total;

							$('#grid')
									.jqGrid(
											{
												editUrl : '/oaw/secure/Plantilla.do?action=update',
												colNames : [ colNameId, colNameName,
													colNameUpload,
													colNameDownload,
													colNameRemove ],
												colModel : [
														{
															name : "id",
															hidden : true,
															sortable : false
														},
														{
															name : "nombre",
															width : 60,
															editrules : {
																required : true
															},
															sortable : false,
															align : "left"
														},
														{
															name : "upload",
															formatter : uploadFormatter,
															width : 20,
															editable : false,
															sortable : false
														},

														{
															name : "download",
															width : 20,
															editable : false,
															sortable : false,
															formatter : downloadFormatter,
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
																		url : '/oaw/secure/Plantilla.do?action=update',
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
													if (this.p.colModel[iCol].name === "upload" || this.p.colModel[iCol].name === "download" || this.p.colModel[iCol].name === "eliminar") {
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
																				url : '/oaw/secure/Plantilla.do?action=update',
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
											reloadGrid('/oaw/secure/Plantilla.do?action=list');


										});

					});

	var windowWidth = $(window).width() * 0.3;
	var windowHeight = $(window).height() * 0.5;

	var dialog;

	function dialogoNuevaPlantilla() {

		window.scrollTo(0, 0);
		
		
		
		var windowTitle = '<bean:message key="plantilla.nueva.modal.title"/>';
		
		var saveButton = '<bean:message key="boton.guardar"/>';
		
		var cancelButton = '<bean:message key="boton.cancelar"/>';
		
		
		

		$('#existosPlantilla').hide();
		$('#erroresPlantilla').hide();

		dialog = $("#dialogoNuevaPlantilla").dialog({
			height : windowHeight,
			width : windowWidth,
			modal : true,
			title : windowTitle,
			buttons : {
				"Guardar" : {
					click: function() {
						guardarNuevaPlantilla();
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
				$('#nuevaPlantillaForm')[0].reset();
			}
		});
	}

	function guardarNuevaPlantilla() {
		$('#existosPlantilla').hide();
		$('#existosPlantilla').html("");
		$('#erroresPlantilla').hide();
		$('#erroresPlantilla').html("");
		$('#loading_cover_div').show();

		var guardado = $.ajax({
			url : '/oaw/secure/Plantilla.do?action=save',
			//data : $('#nuevaPlantillaForm').serialize(),
			data: new FormData($("#nuevaPlantillaForm")[0]),
        	processData: false, 
        	contentType: false, 
			method : 'POST',
			cache : false
		}).success(
				function(response) {
					$('#loading_cover_div').fadeOut(1000);
					$('#existosPlantilla').addClass('alert alert-success');
					$('#existosPlantilla').append("<ul>");

					$.each(JSON.parse(response), function(index, value) {
						$('#existosPlantilla').append(
								'<li>' + value.message + '</li>');
					});

					$('#existosPlantilla').append("</ul>");
					$('#existosPlantilla').show();
					dialog.dialog("close");
					reloadGrid(lastUrl);

				}).error(
				function(response) {
					$('#loading_cover_div').fadeOut(1000);
					$('#erroresPlantilla').addClass('alert alert-danger');
					$('#erroresPlantilla').append("<ul>");

					$.each(JSON.parse(response.responseText), function(index,
							value) {
						$('#erroresPlantilla').append(
								'<li>' + value.message + '</li>');
					});

					$('#erroresPlantilla').append("</ul>");
					$('#erroresPlantilla').show();

				}

		);

		return guardado;
	}
	
	
	function actualizarPlantilla() {
		$('#existosPlantilla').hide();
		$('#existosPlantilla').html("");
		$('#erroresPlantilla').hide();
		$('#erroresPlantilla').html("");

		$('#loading_cover_div').show();
		
		var guardado = $.ajax({
			url : '/oaw/secure/Plantilla.do?action=upload',
			data: new FormData($("#nuevaPlantillaForm")[0]),
        	processData: false, 
        	contentType: false, 
			method : 'POST',
			cache : false
		}).success(
				

				function(response) {
					$('#loading_cover_div').fadeOut(1000);
					$('#existosPlantilla').addClass('alert alert-success');
					$('#existosPlantilla').append("<ul>");

					$.each(JSON.parse(response), function(index, value) {
						$('#existosPlantilla').append(
								'<li>' + value.message + '</li>');
					});

					$('#existosPlantilla').append("</ul>");
					$('#existosPlantilla').show();
					dialog.dialog("close");
					reloadGrid(lastUrl);

				}).error(
				function(response) {
					$('#loading_cover_div').fadeOut(1000);
					$('#erroresPlantilla').addClass('alert alert-danger');
					$('#erroresPlantilla').append("<ul>");

					$.each(JSON.parse(response.responseText), function(index,
							value) {
						$('#erroresPlantilla').append(
								'<li>' + value.message + '</li>');
					});

					$('#erroresPlantilla').append("</ul>");
					$('#erroresPlantilla').show();

				}

		);

		return guardado;
	}
</script>

<div id="loading_cover_div"></div>
<!-- observatorio_cargarDependencias.jsp -->
<div id="main">




	<div id="dialogoNuevaPlantilla" style="display: none">
		<div id="main" style="overflow: hidden">

			<div id="erroresPlantilla" style="display: none"></div>

			<form id="nuevaPlantillaForm" enctype="multipart/form-data">
				<!-- Nombre -->
				<div class="row formItem">
					<label for="nombre" class="control-label" style="margin-left: 25px;"><strong class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="nueva.plantilla.observatorio.nombre" /></strong></label>
					<div class="col-xs-8">
						<input type="text" id="nombre" name="nombre" class="textoLargo form-control" />
					</div>
				</div>

				<div class="row formItem">
					<label for="nombre" class="control-label" style="margin-left: 25px;"><strong class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="nueva.plantilla.observatorio.fichero" /></strong></label>
					<div class="col-xs-8">
						<input type="file" name="file" accept=".odt" />
					</div>
				</div>
				
				<input type="hidden" name="id" value="0" id="templateId" />

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
				<li class="active"><bean:message key="migas.plantillas.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.plantillas.observatorio.titulo" />
			</h2>

			<div id="existosPlantilla" style="display: none"></div>


			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg" onclick="dialogoNuevaPlantilla()"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title=""
					data-original-title="Crear una semilla"></span> <bean:message key="nueva.plantilla.observatorio" />
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
