var lastUrl;
var scroll;



// colNames : [ "Id", "NombreAntiguo",
// "Nombre", "Acr\u00F3nimo",
// "Segmento", "\u00C1mbito",
// "Complejidad", "Etiquetas",
// "Dependencia", "URLs","Observaciones",
// "Activa", "Directorio",
// "Ir", "Eliminar", "Eliminar definitivamente" ],

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
	var cellFormatted = "<ul style='list-style: none; padding-left: 0; margin-top: 10px;' >";

	$.each(rowObject.dependencias, function(index, value) {
		cellFormatted = cellFormatted + "<li class='listado-grid'>"
				+ value.name + "</li>";
	});

	cellFormatted = cellFormatted + "</ul>";

	return cellFormatted;
}

function titleEtiquetasFormatter(cellvalue, options, rowObject) {
	var cellFormatted = "";

	$.each(rowObject.etiquetas, function(index, value) {
		cellFormatted = cellFormatted + value.name + "\n";
	});

	return cellFormatted;
}

function etiquetasFormatter(cellvalue, options, rowObject) {

	var data = "";

	$.each(rowObject.etiquetas, function(index, value) {
		data = data + value.id + ",";
	});

	if (data) {
		data = data.slice(0, -1);
	}

	var cellFormatted = "<div class='tagbox-wrapper'>";

	$.each(rowObject.etiquetas, function(index, value) {
		cellFormatted = cellFormatted + "<div class='tagbox-token'><span>"
				+ value.name + "</span></div>";
	});

	cellFormatted = cellFormatted + "</div>";

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
}

function irDependenciaFormatter(cellvalue, options, rowObject) {
	return "<a target='blank' href="
			+ rowObject.listaUrls[0]
			+ "><span class='glyphicon glyphicon-new-window'></span><span class='sr-only'>"+ semillaIrAlt +"</span></a>";
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

function complexityEdit(value, options, rowObject) {

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

function complexityEditValue(elem, operation, value) {

	if (operation === 'get') {
		return $(elem).val();
	} else if (operation === 'set') {
		$('input', elem).val(value);
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

function eliminarSemillaFormater(cellvalue, options, rowObject) {

	return "<span style='cursor:pointer' onclick='eliminarSemilla("
			+ options.rowId
			+ ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>"+semillaEliminarDefinitiva+"</span></span>";
}

function eliminarSemilla(rowId) {

	var semilla = $('#grid').jqGrid('getRowData', rowId);

	var idSemilla = semilla.id;
	var dialogoEliminar = $('<div id="dialogoEliminarContent"></div>');

	dialogoEliminar.append('<p>'+confirmRemoveMessage1+' "'
			+ semilla.nombre + '"? '+ confirmRemoveMessage1 +'</p>');
	
	$
			.ajax(
					{
						url : '/oaw/secure/JsonSemillasObservatorio.do?action=listObservatoriosSemillaDelete&semilla='
								+ idSemilla,
						method : 'POST',
						cache : false
					})
			.success(
					function(data) {

						if (data != null) {

							if (JSON.parse(data).length > 0) {

								dialogoEliminar
										.append('<p>'+ confirmRemoveMessage3+ ' </p></ul>');

								$.each(JSON.parse(data),
										function(index, value) {
											dialogoEliminar.append('<li>'
													+ value.nombre + '</li>')
										});

								dialogoEliminar.append("</ul>");
							}

						}

					});

	dialogoEliminar
			.dialog({
				autoOpen : false,
				minHeight : $(window).height() * 0.25,
				minWidth : $(window).width() * 0.25,
				modal : true,
				title : windowTitleRemoveSeed,
				buttons : {
					"Aceptar" : {
						click: function() {
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
						text: saveButton,
						class: 'jdialog-btn-save'
					},
					"Cancelar" : {
						click: function() {
							dialogoEliminar.dialog("close");
						},
						text: cancelButton,
						class: 'jdialog-btn-cancel'
					}
				}
			});

	dialogoEliminar.dialog("open");
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
											colNames: translatedColNames, //note this is definesd in parent JSP in order to work
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
														formatter : complejidadFormatter,
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
														formatter : urlsFormatter,
														editoptions : {
															custom_element : complexityEdit,
															custom_value : complexityEditValue
														},
														editrules : {
															required : false
														},
														formatter : etiquetasFormatter,
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
														name : "observaciones",
														width : 50,
														edittype : 'custom',
														editoptions : {
															custom_element : textareaEdit,
															custom_value : textareaEditValue
														},
														editrules : {
															required : false
														},
														sortable : false,
														align : "left"
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
														name : "eliminar",
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
														name : 'eliminarSemilla',
														formatter : eliminarSemillaFormater,
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
											'<tr role="row" class="ui-widget-content jqgfirstrow ui-row-ltr"><td colspan="14" style="padding: 15px !important;" role="gridcell">'+noResults+'</td></tr>');
						}

						// Paginador
						paginas = data.paginas;

						$('#paginador').empty();

						$('#paginador').append("<span style='float: left;clear: both; display: block; width: 100%; text-align: left;padding: 10px 5px;'><strong>"+ paginadorTotal+ "</strong> " + data.paginador.total +"</span>");
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