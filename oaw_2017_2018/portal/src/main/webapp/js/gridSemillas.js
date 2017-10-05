var lastUrl;
var scroll;

function cambiaTitulo() {
	$("option").hover(function(e) {
		var $target = $(e.target);
		if ($target.is('option')) {
			this.title = $target.text();
		}
	});
}

// Formatters de celdas
function categoriaFormatter(cellvalue, options, rowObject) {
	if (rowObject.categoria.name != null) {

		return rowObject.categoria.name;

	} else {
		return "";
	}
}

function nombreAntiguoFormatter(cellvalue, options, rowObject) {
	return rowObject.nombre;
}

function dependenciasFormatter(cellvalue, options, rowObject) {
	var cellFormatted = "<ul style='list-style: none; padding-left: 0; margin-top: 10px;' >";

	$.each(rowObject.dependencias, function(index, value) {
		cellFormatted = cellFormatted + "<li class='listado-grid'>"
				+ value.name + "</li>";
	});

	cellFormatted = cellFormatted + "</ul>";

	return cellFormatted;
}

function titleDependenciasFormatter(cellvalue, options, rowObject) {
	var cellFormatted = "";

	$.each(rowObject.dependencias, function(index, value) {
		cellFormatted = cellFormatted + value.name + "\n";
	});

	return cellFormatted;
}

function urlsFormatter(cellvalue, options, rowObject) {

	return rowObject.listaUrls.toString().replace(/\,/g, '\r\n');
	;
}

function irDependenciaFormatter(cellvalue, options, rowObject) {
	return "<a target='blank' href="
			+ rowObject.listaUrls[0]
			+ "><span class='glyphicon glyphicon-new-window'></span><span class='sr-only'>Ir a la p&aacute;gina web de esta semilla</span></a>";
}

function eliminarDependenciaFormater(cellvalue, options, rowObject) {

	return "<span style='cursor:pointer' onclick='eliminarSemilla("
			+ options.rowId
			+ ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>Eliminar</span></span>";
}

function eliminarSemilla(rowId) {

	var semilla = $('#grid').jqGrid('getRowData', rowId);

	var idSemilla = semilla.id;
	var dialogoEliminar = $('<div/><div>');

	dialogoEliminar.append('<p>&#191;Desea eliminar la semilla "'
			+ semilla.nombre + '"?</p>');

	dialogoEliminar
			.dialog({
				autoOpen : false,
				modal : true,
				title : 'Eliminar semilla',
				buttons : {
					"Aceptar" : function() {
						$
								.ajax(
										{
											url : '/oaw/secure/JsonSemillasObservatorio.do?action=delete&idSemilla='
													+ idSemilla,
											method : 'POST',
											cache : false
										}).success(function(response) {
									reloadGrid(lastUrl);
									dialogoEliminar.dialog("close");
								});
					},
					"Cancelar" : function() {
						dialogoEliminar.dialog("close");
					}
				}
			});

	dialogoEliminar.dialog("open");
}

// Edicion de las urls en linea formatea el texto para colocar una por línea

function textareaEdit(value, options, rowObject) {
	var el = document.createElement("textarea");
	el.setAttribute("style", "height:100px; width:100%; text-align:left;");

	el.value = value.replace(/\,/g, '\r\n');
	return el;
}

function textareaEditValue(elem, operation, value) {

	if (operation === 'get') {
		return $(elem).val();
	} else if (operation === 'set') {
		$('textarea', elem).val(value);
	}
}

function dialogoErrorEdit(mensaje) {

	var errorDialog = $('<div>' + mensaje + '</div>');

	errorDialog.dialog({
		modal : true,
		title : 'Error',
		buttons : {
			"Cerrar" : function() {
				errorDialog.dialog("close");
			}
		}
	});

}

// Recarga el grid. Recibe como parámetro la url de la acción con la información
// de paginación. Si no le llega nada usa la url por defecto
function reloadGrid(path) {

	// Mantener el scroll
	scroll = $(window).scrollTop();
	
	if (typeof path != 'undefined' && path != null && path != '') {
		lastUrl = path;
	} else {
		lastUrl = '/oaw/secure/JsonViewSemillasObservatorio.do?action=buscar';
	}

	$('#grid').jqGrid('clearGridData');

	$
			.ajax({
				url : lastUrl,
				cache : false,
				dataType : "json"
			})
			.done(

					function(data) {

						ajaxJson = JSON.stringify(data.semillas);

						total = data.paginador.total;

						$('#grid')
								.jqGrid(
										{
											editUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
											colNames : [ "Id", "NombreAntiguo",
													"Nombre", "Acr\u00F3nimo",
													"Segmento", "Dependencia",
													"URLs", "Activa",
													"Directorio", "Ir",
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
														name : "nombre",
														width : 50,
														editrules : {
															required : true
														},
														sortable : false,
														align : "left"
													},
													{
														name : "acronimo",
														width : 20,
														sortable : false,
														align : "left"
													},
													{
														name : "segmento",
														width : 20,
														edittype : "select",
														align : "left",
														editoptions : {

															dataUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=listCategorias',
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
														formatter : categoriaFormatter,
														sortable : false
													},
													{
														name : "dependenciasSeleccionadas",
														// Prueba para devolver
														// un title
														// personalizado
														cellattr : function(
																rowId, val,
																rawObject, cm,
																rdata) {
															return 'title="'
																	+ titleDependenciasFormatter(
																			val,
																			null,
																			rawObject)
																	+ '"';
														},
														align : "left",
														width : 50,
														// editrules : {
														// required : true
														// },
														edittype : "select",
														editoptions : {

															style : "height:100px; width:100%; text-align:left; overflow-x: scroll;",
															multiple : true,
															dataUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=listDependencias',
															buildSelect : function(
																	data) {

																// Seleccionar
																// las que ya
																// asociadas

																var idsDependencias = [];

																$
																		.each(
																				$(
																						'#grid')
																						.getLocalRow(
																								$(
																										'#grid')
																										.jqGrid(
																												'getGridParam',
																												'selrow')).dependencias,
																				function(
																						index,
																						value) {
																					idsDependencias
																							.push(value.id);
																				});

																var response = jQuery
																		.parseJSON(data);
																var s = '<select><option disabled style="display: none;"></option>';

																if (response
																		&& response.length) {
																	for (var i = 0; i < response.length; i++) {
																		var ri = response[i];

																		if ($
																				.inArray(
																						ri.id,
																						idsDependencias) >= 0) {

																			s += '<option onmouseover="cambiaTitulo()" selected="selected" value="'
																					+ ri.id
																					+ '">'
																					+ ri.name
																					+ '</option>';

																		} else {
																			s += '<option onmouseover="cambiaTitulo()" value="'
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
														formatter : dependenciasFormatter,
														sortable : false
													},
													{
														name : "listaUrlsString",
														align : "left",
														width : 50,
														edittype : 'custom',
														sortable : false,
														formatter : urlsFormatter,
														editoptions : {
															custom_element : textareaEdit,
															custom_value : textareaEditValue
														},
														editrules : {
															required : true
														},
													},
													{
														name : "activa",
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
														name : "inDirectory",
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
														name : 'verUrl',
														formatter : irDependenciaFormatter,
														align : "center",
														width : 10,
														editable : false,
														datatype : 'html'
													},
													{
														name : 'eliminarSemilla',
														formatter : eliminarDependenciaFormater,
														align : "center",
														width : 10,
														editable : false
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
											headertitles : true,
											onSelectRow : function(rowid,
													status, e) {

												var $self = $(this), savedRow = $self
														.jqGrid("getGridParam",
																"savedRow");
												if (savedRow.length > 0
														&& savedRow[0].id !== rowid) {
													$self.jqGrid("restoreRow",
															savedRow[0].id);
												}

												$self
														.jqGrid(
																"editRow",
																rowid,
																{
																	focusField : e.target,
																	keys : true,
																	url : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
																	restoreAfterError : false,
																	successfunc : function(
																			response) {
																		reloadGrid(lastUrl);
																	},
																	errorfunc : function(
																			rowid,
																			response) {
																		if (response.status == 400) {
																			dialogoErrorEdit(JSON
																					.parse(response.responseText)[0].message);
																		}
																	},
																	afterrestorefunc : function(
																			response) {
																		reloadGrid(lastUrl);
																	}

																});

											},
											beforeSelectRow : function(rowid, e) {
												var $self = $(this), i, $td = $(
														e.target).closest("td"), iCol = $.jgrid
														.getCellIndex($td[0]);

												// Las columnas de ver url y
												// eliminar desactivamos la
												// selección par aevitar que se
												// active la edidion
												// if (iCol = 8) {
												if (this.p.colModel[iCol].name === "verUrl"
														|| this.p.colModel[iCol].name === "eliminarSemilla") {
													return false;
												}

												// savedRows array is not empty
												// if some row is in inline
												// editing mode
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
																			errorfunc : function(
																					rowid,
																					response) {
																				if (response.status == 400) {
																					dialogoErrorEdit(JSON
																							.parse(response.responseText)[0].message);
																				}
																			},
																			afterrestorefunc : function(
																					response) {

																				reloadGrid(lastUrl);

																			},
																			url : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
																			restoreAfterError : false,
																		});

													}
												}
												return savedRows.length === 0; // allow
												// selection
												// if
												// saving
												// successful
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

						// Si solo hay una página no pintamos el paginador
						if (paginas.length > 1) {

							$.each(paginas, function(key, value) {
								if (value.active == true) {
									$('#paginador').append(
											'<a href="javascript:reloadGrid(\''
													+ value.path
													+ '\')" class="'
													+ value.styleClass
													+ ' btn btn-default">'
													+ value.title + '</a>');
								} else {
									$('#paginador').append(
											'<span class="' + value.styleClass
													+ ' btn">' + value.title
													+ '</span>');
								}

							});
						}
					}).error(function(data) {
				console.log("Error")
				console.log(data)
			});

}