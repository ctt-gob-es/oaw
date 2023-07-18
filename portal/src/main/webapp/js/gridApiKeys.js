var lastUrl;
var scroll;

var gridSelRow;

function eliminarFormatter(cellvalue, options, rowObject) {
	return "<span style='cursor:pointer' onclick='eliminarEtiqueta("
			+ options.rowId
			+ ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>"+colNameRemove+"</span></span>";
}




function eliminarApiKey(rowId) {
	var apiKey = $('#grid').jqGrid('getRowData', rowId);

	var idApiKey = apiKey.idApiKey;
	var dialogoEliminar = $('<div id="dialogoEliminarContent"></div>');

	dialogoEliminar.append('<p>'+ confirmRemoveMessage +' "'
			+ apiKey.name + '"?</p>');

	dialogoEliminar
			.dialog({
				autoOpen : false,
				minHeight : $(window).height() * 0.25,
				minWidth : $(window).width() * 0.25,
				modal : true,
				title : windowTitleRemove,
				buttons : {
					"Aceptar" : {
						click: function() {
							$
							.ajax(
									{
										url : '/oaw/secure/ApiKey.do?action=delete&idApiKey='
												+ idApiKey,
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


			// Primera carga del grid el grid
			$jq(document)
					.ready(
							function() {
								reloadGrid('/oaw/secure/ApiKey.do?action=list');

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

						ajaxJson = JSON.stringify(data.apiKeys);

						total = data.paginador.total;

						$('#grid')
								.jqGrid(
										{
											editUrl : '/oaw/secure/ApiKey.do?action=update',
											colNames : [ colNameId, colNameOldName,colNameName,
												colNameClassification,
												colNameRemove ],
											colModel : [
													{
														name : "idApiKey",
														hidden : true,
														sortable : false
													},
													{
														name : "name",
														editrules : {
															required : true
														},
														sortable : false
													},
													{
														name : "type",
														width : 50,
														editrules : {
															required : true
														},
														
														align : "center",
														sortable : false
															
													},
													{
														name : "description",
														width : 50,
														editrules : {
															required : true
														},
														
														align : "center",
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
																	url : '/oaw/secure/ApiKey.do?action=update',
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
																			url : '/oaw/secure/ApiKey.do?action=update',
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

