var lastUrl;
var scroll;

// Recarga el grid. Recibe como parámetro la url de la acción con la información
// de paginación. Si no le llega nada usa la url por defecto
function reloadGrid(path) {

	// Mantener el scroll
	scroll = $(window).scrollTop();

	if (typeof path != 'undefined' && path != null && path != '') {
		lastUrl = path;
	} else {
		lastUrl = '/oaw/secure/JsonServicioDiagnostico.do?action=search';
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

						ajaxJson = JSON.stringify(data.diagnosticos);

						total = data.paginador.total;

						$('#grid').jqGrid(
								{
									colNames : [ "URL", "Tipo de portal",
											"Usuario", "Email", "Profundidad",
											"Amplitud", "Tipo de informe",
											"Tipo de an&#225;lisis",
											"En directorio", "Hist&#243;rico",
											"Estado", "Fecha de solicitud",
											"Fecha de env&#237;o" ],
									colModel : [ {
										name : "domain",
										width : 50,
										sortable : false
									}, {
										name : "type",
										width : 20,
										sortable : false
									}, {
										name : "user",
										width : 20,
										sortable : false,
									}, {
										name : "email",
										width : 20,
										sortable : false,
									}, {
										name : "depth",
										width : 10,
										sortable : false,
									}, {
										name : "width",
										width : 10,
										sortable : false,
									}, {
										name : "report",
										width : 20,
										sortable : false,
									}, {
										name : "analysisType",
										width : 20,
										sortable : false,
									}, {
										name : "inDirectory",
										width : 20,
										sortable : false,
									}, {
										name : "registerResult",
										width : 20,
										sortable : false,
									}, {
										name : "status",
										width : 20,
										sortable : false,
									}, {
										name : "date",
										width : 20,
										sortable : false,
									}, {
										name : "sendDate",
										width : 20,
										sortable : false,
									} ],
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
											'<tr role="row" class="ui-widget-content jqgfirstrow ui-row-ltr"><td colspan="13" style="padding: 15px !important;" role="gridcell">Sin resultados</td></tr>');
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