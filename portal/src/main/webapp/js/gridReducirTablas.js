var lastUrl;
var scroll;

var gridSelRow;


// Formatters de celdas

function sizeFormatter(cellvalue, options, rowObject) {
	if (rowObject.size.nombre != null) {

		return rowObject.size.nombre;

	} else {
		return "";
	}
}

function reducirFormatter(cellvalue, options, rowObject) {
	return "<span style='cursor:pointer' onclick='reducirTabla(\""
			+ options.rowId
			+ "\")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>Reducir</span></span>";
}




function reducirTabla(rowId) {

	var tabla = $('#grid').jqGrid('getRowData', rowId);

	var dialogoReducir = $('<div id="dialogoReducirContent"></div>');

	dialogoReducir.append('<p>&#191;Desea reducir los registros de la tabla "'
			+ tabla.name + '" en un 10%?</p>');

	var nameTabla = tabla.name;

	dialogoReducir
			.dialog({
				autoOpen : false,
				minHeight : $(window).height() * 0.25,
				minWidth : $(window).width() * 0.25,
				modal : true,
				title : 'RASTREADOR WEB - Reducir tabla',
				buttons : {
					"Aceptar" : {
						click: function() {
							$
							.ajax(
									{
										url : '/oaw/secure/ViewReducirTablasObservatorio.do?action=reduce&nameTabla='
												+ nameTabla,
										method : 'POST',
										cache : false
									}).success(function(response) {
								reloadGrid(lastUrl);
								dialogoReducir.dialog("close");
							});
				},
				text: 'Aceptar',
				class: 'jdialog-btn-save'
					},
					"Cancelar" : {
						click:function() {
							dialogoReducir.dialog("close");
						},
						text: 'Cancelar',
						class: 'jdialog-btn-cancel'
					}
				}
			});

	dialogoReducir.dialog("open");
}


$(window)
.on(
		'load',
		function() {

			var $jq = $.noConflict();

			var lastUrl;

			// Primera carga del grid el grid
			$jq(document)
					.ready(
							function() {
								reloadGrid('/oaw/secure/ViewReducirTablasObservatorio.do?action=search');

							});

		});


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
											//editUrl : '/oaw/secure/ViewEtiquetasObservatorio.do?action=update',
											colNames : ["Id", "Nombre", "Espacio ocupado (MB)",
													"Reducir" ],
											colModel : [
												{
													name : "id",
													hidden : true,
													sortable : false
												},
													{
														name : "name",
														width : 30,
														editable : false,
														sortable : false,
														align : "left"
													},
													{
														name : "size",
														width : 50,
														editable : false,
														sortable : false,
														align : "center"
													},
													{
														name : "reducir",
														width : 20,
														sortable : false,
														editable : false,
														formatter : reducirFormatter,
													}

											],
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
												if (this.p.colModel[iCol].name === "reducir") {
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
																			//url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=update',
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

						// Si solo hay una página no pintamos el paginador
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

