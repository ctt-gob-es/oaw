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
<script src="/oaw/js/tagbox/tagbox.js" type="text/javascript"></script>
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

.tagbox-wrapper {
	width: 100% !important;
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
}
</style>
<script>
	var script = document.createElement('script');
	var lang = (navigator.language || navigator.browserLanguage)
	script.src = '/oaw/js/jqgrid/i18n/grid.locale-'+lang.substring(0,2)+'.js';
	document.head.appendChild(script);
</script>
<!--  JQ GRID   -->
<script>

var paginadorTotal = '<bean:message key="cargar.semilla.observatorio.buscar.total"/>'; 

var colNameOldName = '<bean:message key="colname.oldname"/>';
var colNameId = '<bean:message key="colname.id"/>';
var colNameTags = '<bean:message key="colname.province"/>';
var colNameName = '<bean:message key="colname.name"/>';
var colNameRemove = '<bean:message key="colname.remove"/>';
var colNameScope = '<bean:message key="colname.scope"/>';
var colNameOfficial = '<bean:message key="colname.official"/>';
var colNameSendAuto = '<bean:message key="colname.send.auto"/>';

var colNameAcronym = '<bean:message key="colname.acronym"/>';


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
													colNameOldName,colNameAcronym, colNameScope, colNameTags, "Emails", colNameSendAuto, colNameOfficial,
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
															name : "acronym",
															width : 20,
															sortable : true
														},
														{														
															name : "ambitoaux",
															width : 15,
															edittype : "select",
															align : "center",
															editoptions : {
																multiple: true,
																dataUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=listAmbitos',
																buildSelect : function(
																		data) {
																	var rowid = $(this).jqGrid("getGridParam", "selrow");
																	var ambitos = $(this).jqGrid ('getLocalRow', rowid).ambitos;
																	var response = jQuery
																			.parseJSON(data);
																	
																	var s = '<select ><option disabled hidden></option>';

																	if (response
																			&& response.length && ambitos) {
																		for (var i = 0, l = response.length; i < l; i++) {
																			var ri = response[i];
																			if(ambitos.some(ambito => ambito.id === ri.id)){
																				s += '<option selected class="dependenciaOption" value="'
																					+ ri.id
																					+ '">'
																					+ ri.name
																					+ '</option>';
																			}else{
																			s += '<option  class="dependenciaOption" value="'
																					+ ri.id
																					+ '">'
																					+ ri.name
																					+ '</option>';
																			}
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
															name : "tagaux",
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
																required : false
															},
															sortable : false,
															align : "left", 
															formatter: emailsFormatter
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
							
							$('#paginador').append("<span style='float: left;clear: both; display: block; width: 100%; text-align: left;padding: 10px 5px;'><strong>"+ paginadorTotal+ "</strong> " + data.paginador.total +"</span>");

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
		if(rowObject.emails){
			return rowObject.emails.toString().replace(/\,/g, '\r\n');
		} 
		return "";
	}
	
// 	function ambitoFormatter(cellvalue, options, rowObject) {
// 		if (rowObject.ambito && rowObject.ambito.name != null) {
// 			return rowObject.ambito.name;
// 		} else {
// 			return "";
// 		}
// 	}
	
	function ambitoFormatter(cellvalue, options, rowObject) {
		var cellFormatted = "<ul style='list-style: none; padding-left: 0; margin-top: 10px;' >";

		$.each(rowObject.ambitos, function(index, value) {
			cellFormatted = cellFormatted + "<li class='listado-grid'>"
					+ value.name + "</li>";
		});

		cellFormatted = cellFormatted + "</ul>";

		return cellFormatted;
	}
	
	function titleEtiquetasFormatter(cellvalue, options, rowObject) {
		var cellFormatted = "";

		if(rowObject.tag){
			cellFormatted = rowObject.tag.name;
		}

		return cellFormatted;
	}
	
	function etiquetasFormatter(cellvalue, options, rowObject) {
		if(rowObject.tag){
			var cellFormatted = "<div class='tagbox-wrapper'>";
			cellFormatted = "<div class='tagbox-token'><span>" + rowObject.tag.name + "</span></div>";
			cellFormatted = cellFormatted + "</div>";
			return cellFormatted;
		}
		return "";
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
			url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=clasification&clasification=2',
			method : 'POST',
			cache : false
		}).success(function(response) {
			
			
			$.each(data, function(index, item) {
				if (item.id == options.rowId) {

					var data = "";
					if(item.tag){
						data = data + item.tag.id + ",";
					}

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
				itemClass : 'user',
				maxItems: 1
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
		$('#buscadorDependencias .tagbox-token a').click();
		reloadGrid('/oaw/secure/ViewDependenciasObservatorio.do?action=search&'
				+ $('#buscadorDependencias').serialize());
	}

	$(window)
			.on(
					'load',
					function() {

						var $jq = $.noConflict(false);

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
						
						$jq(document).ready(function() {
							$.ajax({
								url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=clasification&clasification=2',
								method : 'POST',
								cache : false
							}).success(function(response) {

								$('#tagsFilterForm').tagbox({
									items : response.etiquetas,
									searchIn : [ 'name' ],
									rowFormat : '<span class="name">{{name}}</span>',
									tokenFormat : '{{name}}',
									valueField : 'id',
									itemClass : 'user'
								});

							})

						});
						
						
						$.ajax({
							url : '/oaw/secure/JsonSemillasObservatorio.do?action=listAmbitos',
						}).done(
								function(data) {

									var response = $.parseJSON(data);

									$('#selectAmbitsSearch').append(
											"<option value=''>Cualquiera</option>");
									$('#selectAmbitsSearch').append("<option value='0'>Ninguno</option>");
									if (response && response.length) {
										for (var i = 0, l = response.length; i < l; i++) {
											var ri = response[i];
											$('#selectAmbitsSearch').append(
													'<option value="'+ri.id+'">' + ri.name
															+ '</option>');
										}
									}
								});
						
						$.ajax({
							url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=clasification&clasification=2',
							method : 'POST',
							cache : false
						}).done(function(response) {

							$('#tagsFilterSearch').tagbox({
								items : response.etiquetas,
								searchIn : [ 'name' ],
								rowFormat : '<span class="name">{{name}}</span>',
								tokenFormat : '{{name}}',
								valueField : 'id',
								itemClass : 'user'
							});

						});
						
						$jq('#selectAmbitNewDependency').empty();
						$jq.ajax({
							url : '/oaw/secure/JsonSemillasObservatorio.do?action=listAmbitos',
						}).done(
								function(data) {

									var response = $jq.parseJSON(data);

									$jq('#selectAmbitNewDependency').append(
											"<option value=''></option>");
									if (response && response.length) {
										for (var i = 0, l = response.length; i < l; i++) {
											var ri = response[i];
											$jq('#selectAmbitNewDependency').append(
													'<option value="'+ri.id+'">' + ri.name
															+ '</option>');
										}
									}

									if (rowObject != null) {

										$jq('#selectAmbitNewDependency').val(
												rowObject.ambito.id);
									}

								});
								
						

					});

	var windowWidth = $(window).width() * 0.4;
	var windowHeight = $(window).height() * 0.5;

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
			open : function() {
				$('#selectAmbitosNuevaSemilla').empty();
				$.ajax({
					url : '/oaw/secure/JsonSemillasObservatorio.do?action=listAmbitos',
				}).done(
						function(data) {

							var response = $.parseJSON(data);

							$('#selectAmbitosNuevaSemilla').append(
									"<option value=''></option>");
							if (response && response.length) {
								for (var i = 0, l = response.length; i < l; i++) {
									var ri = response[i];
									$('#selectAmbitosNuevaSemilla').append(
											'<option value="'+ri.id+'">' + ri.name
													+ '</option>');
								}
							}
						});
				
				
				$.ajax({
						url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=clasification&clasification=2',
						method : 'POST',
						cache : false
					}).done(function(response) {

						$('#tagsFilter').tagbox({
							items : response.etiquetas,
							searchIn : [ 'name' ],
							rowFormat : '<span class="name">{{name}}</span>',
							tokenFormat : '{{name}}',
							valueField : 'id',
							itemClass : 'user',
							maxItems: 1
						});

					});
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
		}).done(
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

				}).fail(
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
	
	
	function selectXMLFile(){
		
		 document.getElementById('importFile').click();
	}
	

	$(function() {
	   $("#importFile").change(function (){
	     var fileName = $(this).val();
	     $(this).closest('form').submit();	     
	   });
	});

</script>
<!-- observatorio_cargarDependencias.jsp -->
<div id="main">
	<div id="dialogoNuevaDependencia" style="display: none">
		<div id="main" style="overflow: hidden">
			<div id="erroresNuevaSemillaMD" style="display: none"></div>
			<form id="nuevaDependenciaForm">
				<!-- Nombre -->
				<div class="row formItem">
					<label for="name" class="control-label">
						<strong class="labelVisu">
							<acronym title="<bean:message key="campo.obligatorio" />"> * </acronym>
							<bean:message key="nueva.dependencia.observatorio.nombre" />
						</strong>
					</label>
					<div class="col-xs-8">
						<input type="text" id="name" name="name" class="textoLargo form-control" />
					</div>
				</div>
				<div class="row formItem">
					<label for="acronym" class="control-label">
						<strong class="labelVisu">
							<bean:message key="colname.acronym" />
						</strong>
					</label>
					<div class="col-xs-8">
						<input type="text" id="name" name="acronym" class="textoLargo form-control" />
					</div>
				</div>
				<div class="row formItem">
					<label for="ambito" class="control-label">
						<strong class="labelVisu">
							<bean:message key="nueva.semilla.webs.ambito" />
						</strong>
					</label>
					<div class="col-xs-4">
						<select name="ambitoaux" id="selectAmbitNewDependency" class="textoSelect form-control" multiple></select>
					</div>
				</div>
				<!-- Etiquetas -->
				<div class="row formItem">
					<label for="tagaux" class="control-label">
						<strong class="labelVisu">
							<bean:message key="nueva.semilla.observatorio.etiqueta" />
						</strong>
					</label>
					<div class="col-xs-6">
						<input name="tagaux" autocapitalize="off" placeholder="Escriba para buscar..." autofocus id="tagsFilter"
							type="text" value="" />
					</div>
				</div>
				<!-- Urls -->
				<div class="row formItem">
					<label for="emails" class="control-label">
						<strong class="labelVisu">
							<acronym title="<bean:message key="campo.obligatorio" />"> * </acronym>
							Emails
						</strong>
					</label>
					<div class="col-xs-8">
						<textarea rows="2" cols="50" name="emails" class="form-control"></textarea>
					</div>
				</div>
				<!-- Official -->
				<div class="row formItem">
					<label for="official" class="control-label">
						<strong class="labelVisu">
							<bean:message key="colname.official" />
						</strong>
					</label>
					<div class="col-xs-4">
						<select name="official" class="textoSelect form-control">
							<option value="true"><bean:message key="select.yes" /></option>
							<option value="false"><bean:message key="select.no" /></option>
						</select>
					</div>
				</div>
				<!-- Send Auto -->
				<div class="row formItem">
					<label for="sendAuto" class="control-label">
						<strong class="labelVisu">
							<bean:message key="colname.send.auto" />
						</strong>
					</label>
					<div class="col-xs-4">
						<select name="sendAuto" class="textoSelect form-control">
							<option value="true"><bean:message key="select.yes" /></option>
							<option value="false"><bean:message key="select.no" /></option>
						</select>
					</div>
				</div>
			</form>
		</div>
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
					<label for="name" class="control-label">
						<strong class="labelVisu">
							<bean:message key="nueva.dependencia.observatorio.nombre" />
						</strong>
					</label>
					<input type="text" class="texto form-control" id="name" name="name" />
				</div>
				<!-- Ambito/Ambitoaux -->
				<div class="row formItem">
					<label for="ambitoaux" class="control-label">
						<strong class="labelVisu">
							<bean:message key="nueva.semilla.webs.ambito" />
						</strong>
					</label>
					<div class="col-xs-4">
						<select name="ambitoaux" id="selectAmbitsSearch" class="textoSelect form-control"></select>
					</div>
				</div>
				<!-- Etiquetas -->
				<div class="row formItem">
					<label for="tagaux" class="control-label">
						<strong class="labelVisu">
							<bean:message key="colname.province" />
						</strong>
					</label>
					<div class="col-xs-6">
						<input name="tagaux" autocapitalize="off" placeholder="Escriba para buscar..." autofocus id="tagsFilterSearch"
							type="text" value="" />
					</div>
				</div>
				<!-- Official -->
				<div class="row formItem">
					<label for="official" class="control-label">
						<strong class="labelVisu">
							<bean:message key="colname.official" />
						</strong>
					</label>
					<div class="col-xs-4">
						<select name="officialSearch" class="textoSelect form-control">
							<option value="2"><bean:message key="select.any" /></option>
							<option value="3"><bean:message key="select.none" /></option>
							<option value="1"><bean:message key="select.yes" /></option>
							<option value="0"><bean:message key="select.no" /></option>
						</select>
					</div>
				</div>
				<!-- Send Auto -->
				<div class="row formItem">
					<label for="sendAuto" class="control-label">
						<strong class="labelVisu">
							<bean:message key="colname.send.auto" />
						</strong>
					</label>
					<div class="col-xs-4">
						<select name="sendAutoSearch" class="textoSelect form-control">
							<option value="2"><bean:message key="select.any" /></option>
							<option value="3"><bean:message key="select.none" /></option>
							<option value="1"><bean:message key="select.yes" /></option>
							<option value="0"><bean:message key="select.no" /></option>
						</select>
					</div
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
			<!-- Importar todas las semillas -->
			<a href="#" class="btn btn-default btn-lg " onclick="selectXMLFile()">
				<span class="glyphicon glyphicon-cloud-upload" aria-hidden="true" data-toggle="tooltip" title=""
					data-original-title="Importar un fichero XML/xlsx de semillas"></span>
				<bean:message key="cargar.semilla.observatorio.importar.todo" />
			</a>
			<a href="#" class="btn btn-default btn-lg" onclick="dialogoNuevaDependencia()">
				<span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title=""
					data-original-title="Crear una semilla"></span>
				<bean:message key="nueva.dependencia.observatorio" />
			</a>
		<form method="post" style="display: none" action="/oaw/secure/ViewDependenciasObservatorio.do?action=upload"
			enctype="multipart/form-data">
			<div class="formItem">
				<label for="importFile" class="control-label">
					<strong class="labelVisu">
						<bean:message key="categoria.semillas.fichero" />
						:
					</strong>
				</label>
				<input type="file" id="importFile" name="dependencyFile" style="display: none">
			</div>
		</form>
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
