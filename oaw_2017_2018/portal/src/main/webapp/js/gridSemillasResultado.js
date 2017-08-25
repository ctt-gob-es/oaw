var lastUrl;

// Formatters de celdas
function categoriaFormatter(cellvalue, options, rowObject) {
	return cellvalue.name;
}

function nombreAntiguoFormatter(cellvalue, options, rowObject) {
	return rowObject.nombre;
}

function dependenciasFormatter(cellvalue, options, rowObject) {
	var cellFormatted = "<ul style='list-style: none; padding-left: 0;' >";

	$.each(cellvalue, function(index, value) {
		cellFormatted = cellFormatted + "<li>" + value.name + "</li>";
	});

	cellFormatted = cellFormatted + "</ul>";

	return cellFormatted;
}

function resultadosFormatter(cellvalue, options, rowObject) {

	// /oaw/secure/showTrackingAction.do?regeneratePDF=true&idExObs=6&observatorio=si&idCartucho=5&id_observatorio=9&id=81&idrastreo=151
	return "<a href="
			+ "/oaw/secure//showTrackingAction.do?regeneratePDF=true&observatorio=si&id_observatorio="
			+ $('[name=id_observatorio]').val()
			+ '&idExObs='
			+ $('[name=idExObs]').val()
			+ '&idCartucho='
			+ $('[name=idCartucho]').val()
			+ "&id="
			+ rowObject.idFulfilledCrawling
			+ "&idrastreo="
			+ rowObject.idCrawling
			+ "><span class='glyphicon glyphicon-list-alt'></span><span class='sr-only'>Resultados</span></a>";
}

function informesFormatter(cellvalue, options, rowObject) {
	// /oaw/secure/primaryExportPdfAction.do?regeneratePDF=true&idExObs=6&observatorio=si&idCartucho=5&id_observatorio=9&id=81&idrastreo=151
	return "<a href=/oaw/secure/primaryExportPdfAction.do?regeneratePDF=true&id_observatorio="
			+ $('[name=id_observatorio]').val()
			+ '&idExObs='
			+ $('[name=idExObs]').val()
			+ '&idCartucho='
			+ $('[name=idCartucho]').val()
			+ "&id="
			+ rowObject.idFulfilledCrawling
			+ "&idrastreo="
			+ rowObject.idCrawling
			+ "><span class='glyphicon glyphicon-cloud-download' aria-hidden='true' data-toggle='tooltip' title='' data-original-title='Descargar el informe individual de esta semilla' ></span><span class='sr-only'>Informe individual</span></a>";

}

function relanzarFormatter(cellvalue, options, rowObject) {
	// /oaw/secure/ResultadosObservatorio.do?action=lanzarEjecucion&id_observatorio=9&idExObs=6&idCartucho=5&idSemilla=97
	return "<a href="
			+ "/oaw/secure/ResultadosObservatorio.do?action=lanzarEjecucion&id_observatorio="
			+ $('[name=id_observatorio]').val()
			+ '&idExObs='
			+ $('[name=idExObs]').val()
			+ '&idCartucho='
			+ $('[name=idCartucho]').val()
			+ "&idSemilla="
			+ rowObject.id
			+ "><span class='glyphicon glyphicon-refresh'></span><span class='sr-only'>Relanzar</span></a>";
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
											method : 'POST'
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

function textareaEdit(value, options) {
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

// Recarga el grid. Recibe como parámetro la url de la acción con la información
// de paginación.
function reloadGrid(path) {

	lastUrl = path;

	$('#grid').jqGrid('clearGridData')

	$
			.ajax({
				url : path,
				dataType : "json"
			})
			.done(

					function(data) {

						ajaxJson = JSON.stringify(data.resultados);

						total = data.paginador.total;

						$('#grid')
								.jqGrid(
										{
											editUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
											colNames : [ "Id", "NombreAntiguo",
													"Nombre", "Acr\u00F3nimo",
													"Segmento", "Dependencia",
													"URLs", "Activa",
													"Directorio",
													"Puntuac\u00F3n",
													"Nivel Accesibilidad",
													"Resultados", "Informe",
													"Relanzar", "Eliminar" ],
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
														width : 60,
														editrules : {
															required : true
														},
														sortable : false,
														align : "left"
													},
													{
														name : "acronimo",
														width : 15,
														sortable : false,
														align : "left"
													},
													{
														name : "categoria",
														width : 15,
														edittype : "select",
														align : "left",
														editoptions : {

															dataUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=listCategorias',
															buildSelect : function(
																	data) {

																var response = jQuery
																		.parseJSON(data);
																var s = '<select>';

																if (response
																		&& response.length) {
																	for (var i = 0, l = response.length; i < l; i++) {
																		var ri = response[i];
																		s += '<option value="'
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
														editrules : {
															required : true
														},
														formatter : categoriaFormatter,
														sortable : false
													},
													{
														name : "dependencias",
														align : "left",
														width : 60,
														editrules : {
															required : true
														},
														edittype : "select",
														editoptions : {

															style : "height:100px; width:100%; text-align:left;",
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
																var s = '<select><option disabled></option>';

																if (response
																		&& response.length) {
																	for (var i = 0; i < response.length; i++) {
																		var ri = response[i];

																		if ($
																				.inArray(
																						ri.id,
																						idsDependencias) >= 0) {

																			s += '<option selected="selected" value="'
																					+ ri.id
																					+ '">'
																					+ ri.name
																					+ '</option>';

																		} else {
																			s += '<option value="'
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
														name : "listaUrls",
														align : "left",
														width : 60,
														edittype : 'custom',
														sortable : false,
														editoptions : {
															custom_element : textareaEdit,
															custom_value : textareaEditValue
														}
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
														name : 'score',
														align : "center",
														width : 10,
														editable : false,
													},
													{
														name : 'nivel',
														align : "center",
														width : 10,
														editable : false,
													},
													{
														name : 'verResultados',
														formatter : resultadosFormatter,
														align : "center",
														width : 10,
														editable : false,
														datatype : 'html'
													},
													{
														name : 'verInformes',
														formatter : informesFormatter,
														align : "center",
														width : 10,
														editable : false,
														datatype : 'html'
													},
													{
														name : 'relanzar',
														formatter : relanzarFormatter,
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
											/* caption : "Semillas", */
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
												if (this.p.colModel[iCol].name === "verResultados"
														|| this.p.colModel[iCol].name === "verInformes"
														|| this.p.colModel[iCol].name === "relanzar"
														|| this.p.colModel[iCol].name === "eliminarSemilla"
														|| this.p.colModel[iCol].name === "score"
														|| this.p.colModel[iCol].name === "nivel") {
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
										}).jqGrid("inlineNav");

						// Recargar el grid
						$('#grid').jqGrid('setGridParam', {
							data : JSON.parse(ajaxJson)
						}).trigger('reloadGrid');
						
						$('#grid').unbind("contextmenu");

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