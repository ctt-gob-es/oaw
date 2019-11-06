var lastUrl;
var scroll;

var gridSelRow;


// Formatters de celdas

function clasificacionFormatter(cellvalue, options, rowObject) {
	if (rowObject.clasificacion.nombre != null) {

		return rowObject.clasificacion.nombre;

	} else {
		return "";
	}
}


// Conservamos el nombre original para comprobaciones posteriores
function nombreAntiguoFormatter(cellvalue, options, rowObject) {
	return rowObject.name;
}


function eliminarFormatter(cellvalue, options, rowObject) {
	return "<span style='cursor:pointer' onclick='eliminarEtiqueta("
			+ options.rowId
			+ ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>Eliminar</span></span>";
}




function eliminarEtiqueta(rowId) {

	var etiqueta = $('#grid').jqGrid('getRowData', rowId);

	var idEtiqueta = etiqueta.id;
	var dialogoEliminar = $('<div id="dialogoEliminarContent"></div>');

	dialogoEliminar.append('<p>&#191;Desea eliminar la etiqueta "'
			+ etiqueta.name + '"?</p>');

	dialogoEliminar
			.dialog({
				autoOpen : false,
				minHeight : $(window).height() * 0.25,
				minWidth : $(window).width() * 0.25,
				modal : true,
				title : 'RASTREADOR WEB - Eliminar etiqueta',
				buttons : {
					"Aceptar" : {
						click: function() {
							$
							.ajax(
									{
										url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=delete&idEtiqueta='
												+ idEtiqueta,
										method : 'POST',
										cache : false
									}).success(function(response) {
								reloadGrid(lastUrl);
								dialogoEliminar.dialog("close");
							});
				},
				text: 'Aceptar',
				class: 'jdialog-btn-save'
					},
					"Cancelar" : {
						click:function() {
							dialogoEliminar.dialog("close");
						},
						text: 'Cancelar',
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
								reloadGrid('/oaw/secure/ViewEtiquetasObservatorio.do?action=search');

							});

		});


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

						ajaxJson = JSON.stringify(data.etiquetas);

						total = data.paginador.total;

						$('#grid')
								.jqGrid(
										{
											editUrl : '/oaw/secure/ViewEtiquetasObservatorio.do?action=update',
											colNames : [ "Id", "NombreAntiguo","Nombre",
													"Clasificacion",
													"Eliminar" ],
											colModel : [
													{
														name : "id",
														hidden : true,
														sortable : false
													},
													{
														name : "nombreAntiguo",
														formatter : nombreAntiguoFormatter,
														hidden : true,
														sortable : false
													},
													{
														name : "name",
														width : 50,
														editrules : {
															required : true
														},
														sortable : false,
														align : "center"
													},
													{
														name : "clasificacionaux",
														width : 15,
														edittype : "select",
														align : "center",
														editoptions : {
															dataUrl : '/oaw/secure/ViewEtiquetasObservatorio.do?action=listClasificaciones',
															buildSelect : function(
																	data) {

																var response = jQuery
																.parseJSON(data);
														var s = '<select>';

														if (response
																&& response.length) {
															for (var i = 0, l = response.length; i < l; i++) {
																var ri = response[i];
																s += '<option class="dependenciaOption" value="'
																		+ ri.id
																		+ '">'
																		+ ri.nombre
																		+ '</option>';
															}
														}

														return s
																+ "</select>";
													}


														},
														hidden : false,
//														editrules : {
//															required : true,
//															edithidden : true
//														},
														formatter : clasificacionFormatter,
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
																	url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=update',
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
																			url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=update',
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

