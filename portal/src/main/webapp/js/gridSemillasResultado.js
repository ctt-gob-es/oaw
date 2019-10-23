var lastUrl;
var scroll;

function cambiaTitulo(){
	$("option").hover(function(e) {
		var $target = $(e.target);
		if ($target.is('option')) {
			this.title = $target.text();
		}
	});
}

var gridSelRow;

// Formatters de celdas
function categoriaFormatter(cellvalue, options, rowObject) {
	if (rowObject.categoria.name != null) {

		return rowObject.categoria.name;

	} else {
		return "";
	}
}

function ambitoFormatter(cellvalue, options, rowObject) {
	if (rowObject.ambito.name != null) {

		return rowObject.ambito.name;

	} else {
		return "";
	}
}

function complejidadFormatter(cellvalue, options, rowObject) {
	if (rowObject.complejidad.name != null) {

		return rowObject.complejidad.name;

	} else {
		return "";
	}
}

function nombreAntiguoFormatter(cellvalue, options, rowObject) {
	return rowObject.nombre;
}

function dependenciasFormatter(cellvalue, options, rowObject) {
	var cellFormatted = "<ul style='list-style: none; padding-left: 0;' >";

	$.each(rowObject.dependencias, function(index, value) {
		cellFormatted = cellFormatted + "<li class='listado-grid'>" + value.name + "</li>";
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
	return rowObject.listaUrls.toString().replace(/\,/g, '\r\n');;
}

function nombreSemillaFormatter(cellvalue, options, rowObject) {
	
	return "<a onclick=dialogoEditarSemilla("
			+ options.rowId
			+ ") class='pull-left col-lg-12'><span class='glyphicon glyphicon-edit pull-right edit-mark'></span><span class='sr-only'>Resultados</span>"
			+ rowObject.nombre + "</a>";
}

function resultadosFormatter(cellvalue, options, rowObject) {
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

function eliminarResultadoFormater(cellvalue, options, rowObject) {
	return "<a href="
			+ "/oaw/secure/ResultadosObservatorio.do?action=confirmacionExSeed&id_observatorio="
			+ $('[name=id_observatorio]').val()
			+ '&idExObs='
			+ $('[name=idExObs]').val()
			+ '&idCartucho='
			+ $('[name=idCartucho]').val()
			+ "&idSemilla="
			+ rowObject.id
			+ "&id="
			+ rowObject.idFulfilledCrawling
			+ "><span class='glyphicon glyphicon-remove'></span><span class='sr-only'>Relanzar</span></a>";
}

// Edicion de las urls en linea formatea el texto para colocar una por línea

function textareaEdit(value, options) {
	var el = document.createElement("textarea");
	el.setAttribute("style", "height:100px; width:100%; text-align:left;");
	el.setAttribute("class", "form-control");

	var valueUrl = "";

	$.each($('#grid').jqGrid('getLocalRow', options.rowId).listaUrls, function(
			index, value) {
		valueUrl += value + "\n";
	});

	el.value = valueUrl;
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
	
	// Mantener el scroll
	scroll = $(window).scrollTop();

	$('#grid').jqGrid('clearGridData')

	$
			.ajax({
				url : path,
				cache : false,
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
													"Segmento","Ambito", "Complejidad", "Dependencia",
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
														width : 50,
														editrules : {
															required : true
														},
														edittype : "text",
														sortable : false,
														align : "left",
														formatter : nombreSemillaFormatter,
														edittype : "custom",
														editoptions : {
															custom_element : function(
																	value,
																	options) {
																var el = document
																		.createElement("input");
																el
																		.setAttribute(
																				"class",
																				"texto form-control");

																el.value = $(
																		'#grid')
																		.jqGrid(
																				'getLocalRow',
																				options.rowId).nombre;

																return el;
															},
															custom_value : function(
																	elem,
																	operation,
																	value) {
																if (operation === 'get') {
																	return $(
																			elem)
																			.val();
																} else if (operation === 'set') {
																	$(
																			'textarea',
																			elem)
																			.val(
																					value);
																}
															}
														}
													},
													{
														name : "acronimo",
														width : 15,
														sortable : false,
														align : "left",
														hidden : true,
														editrules : {
															edithidden : true
														},
														edittype : "custom",
														editoptions : {
															custom_element : function(
																	value,
																	options) {
																var el = document
																		.createElement("input");
																el
																		.setAttribute(
																				"class",
																				"texto form-control");

																el.value = $(
																		'#grid')
																		.jqGrid(
																				'getLocalRow',
																				options.rowId).acronimo;

																return el;
															},
															custom_value : function(
																	elem,
																	operation,
																	value) {
																if (operation === 'get') {
																	return $(
																			elem)
																			.val();
																} else if (operation === 'set') {
																	$(
																			'textarea',
																			elem)
																			.val(
																					value);
																}
															}
														}
													},
													{
														name : "segmento",
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
																		if(ri.id == $('#grid').getLocalRow(gridSelRow).categoria.id){
																			

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
														hidden : true,
														editrules : {
															required : true,
															edithidden : true
														},
														formatter : categoriaFormatter,
														sortable : false
													},
													{
														name : "ambitoaux",
														width : 15,
														edittype : "select",
														align : "left",
														editoptions : {
															dataUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=listAmbitos',
															buildSelect : function(
																	data) {

																var response = jQuery
																		.parseJSON(data);
																var s = '<select>';

																if (response
																		&& response.length) {
																	for (var i = 0, l = response.length; i < l; i++) {
																		var ri = response[i];
																		if(ri.id == $('#grid').getLocalRow(gridSelRow).ambito.id){
																			

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
														hidden : true,
														editrules : {
															required : true,
															edithidden : true
														},
														formatter : ambitoFormatter,
														sortable : false
													},
													{
														name : "complejidadaux",
														width : 15,
														edittype : "select",
														align : "center",
														editoptions : {
															dataUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=listComplejidades',
															buildSelect : function(
																	data) {

																var response = jQuery
																		.parseJSON(data);
																var s = '<select>';

																if (response
																		&& response.length) {
																	for (var i = 0, l = response.length; i < l; i++) {
																		var ri = response[i];
																		if(ri.id == $('#grid').getLocalRow(gridSelRow).complejidad.id){
																			

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
														hidden : true,
														editrules : {
															required : true,
															edithidden : true
														},
														//formatter : complejidadFormatter,
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
															return 'title="'+titleDependenciasFormatter(val,null,rawObject)+'"';
														},
														align : "left",
														width : 60,
														hidden : true,
														editrules : {
															required : true,
															edithidden : true
														},
														edittype : "select",
														editoptions : {

															class : "form-control",
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
																								gridSelRow).dependencias,
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
														name : "listaUrlsString",
														align : "left",
														width : 60,
														edittype : 'custom',
														formatter : urlsFormatter,
														sortable : false,
														hidden : true,
														editrules : {
															required : true,
															edithidden : true
														},
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
														edittype : "select",
														editoptions : {
															value : "true:S\u00ED;false:No"
														},
														hidden : true,
														editrules : {
															edithidden : true
														},
														sortable : false,

													},
													{
														name : "inDirectory",
														align : "center",
														width : 10,
														template : "booleanCheckboxFa",
														edittype : "select",
														editoptions : {
															value : "true:S\u00ED;false:No"
														},
														hidden : true,
														editrules : {
															edithidden : true
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
														formatter : eliminarResultadoFormater,
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
											beforeSelectRow : function(rowid, e) {
												var $self = $(this), i, $td = $(
														e.target).closest("td"), iCol = $.jgrid
														.getCellIndex($td[0]);

												gridSelRow = rowid;

												// Desactivamos el evento en
												// ciertas columnas
												if (this.p.colModel[iCol].name === "verResultados"
														|| this.p.colModel[iCol].name === "verInformes"
														|| this.p.colModel[iCol].name === "relanzar"
														|| this.p.colModel[iCol].name === "eliminarSemilla"
														|| this.p.colModel[iCol].name === "score"
														|| this.p.colModel[iCol].name === "nivel") {
													return false;
												}
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
						if (data.resultados.length == 0) {
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