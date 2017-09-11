<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<html:javascript formName="DependenciaForm"/>

<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">

<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js"
	type="text/javascript"></script>


<!--  JQ GRID   -->
<script>
	//Recarga el grid. Recibe como parámetro la url de la acción con la información
	//de paginación.
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

							ajaxJson = JSON.stringify(data.dependencias);

							total = data.paginador.total;

							$('#grid')
									.jqGrid(
											{
												editUrl : '/oaw/secure/ViewDependenciasObservatorio.do?action=update',
												colNames : [ "Id", "Nombre",
														"NombreAntiguo",
														"Eliminar" ],
												colModel : [
														{
															name : "id",
															hidden : true,
															sortable : false
														},

														{
															name : "name",
															width : 60,
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
											}).jqGrid("inlineNav");

							// Recargar el grid
							$('#grid').jqGrid('setGridParam', {
								data : JSON.parse(ajaxJson)
							}).trigger('reloadGrid');

							$('#grid').unbind("contextmenu");

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

	// Conservamos el nombre original para comprobaciones posteriores
	function nombreAntiguoFormatter(cellvalue, options, rowObject) {
		return rowObject.name;
	}

	function eliminarFormatter(cellvalue, options, rowObject) {
		return "<span style='cursor:pointer' onclick='eliminarDependencia("
				+ options.rowId
				+ ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>Eliminar</span></span>";
	}

	function eliminarDependencia(rowId) {

		var dependencia = $('#grid').jqGrid('getRowData', rowId);

		var idDependencia = dependencia.id;
		var dialogoEliminar = $('<div/><div>');

		dialogoEliminar.append('<p>&#191;Desea eliminar la dependencia "'
				+ dependencia.name + '"?</p>');

		dialogoEliminar
				.dialog({
					autoOpen : false,
					modal : true,
					title : 'Eliminar dependencia',
					buttons : {
						"Aceptar" : function() {
							$
									.ajax(
											{
												url : '/oaw/secure/ViewDependenciasObservatorio.do?action=delete&idDependencia='
														+ idDependencia,
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

	//Buscador
	function buscar() {
		reloadGrid('/oaw/secure/ViewDependenciasObservatorio.do?action=search&'
				+ $('#buscadorDependencias').serialize());
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
											reloadGrid('/oaw/secure/ViewDependenciasObservatorio.do?action=search');
										});

					});

	var windowWidth = $(window).width() * 0.3;
	var windowHeight = $(window).height() * 0.3;

	var dialog;

	function dialogoNuevaDependencia() {

		window.scrollTo(0, 0);

		$('#exitosNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').hide();

		dialog = $("#dialogoNuevaDependencia").dialog({
			height : windowHeight,
			width : windowWidth,
			modal : true,
			title : 'Nueva dependencia',
			buttons : {
				"Guardar" : function() {
					guardarNuevaDependencia();
				},
				"Cancelar" : function() {
					dialog.dialog("close");
				}
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
			method : 'POST'
		}).success(
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

				}).error(
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
</script>


<!-- observatorio_cargarDependencias.jsp -->
<div id="main">


	<div id="dialogoNuevaDependencia" style="display: none">
		<div id="main" style="overflow: hidden">

			<div id="erroresNuevaSemillaMD" style="display: none"></div>

			<form id="nuevaDependenciaForm">
				<!-- Nombre -->
				<div class="row formItem">
					<label for="nombre" class="control-label"><strong
						class="labelVisu"><acronym
							title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
								key="nueva.dependencia.observatorio.nombre" /></strong></label>
					<div class="col-xs-8">

						<input type="text" id="nombre" name="nombre"
							class="texto form-control" />
					</div>
				</div>
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
				<li class="active"><bean:message
						key="migas.dependencias.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.dependencias.observatorio.titulo" />
			</h2>

			<div id="exitosNuevaSemillaMD" style="display: none"></div>

			<form id="buscadorDependencias" class="formulario form-horizontal">
				<fieldset>
					<legend>Buscador</legend>
					<jsp:include page="/common/crawler_messages.jsp" />
					<div class="formItem">
						<label for="nombre" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.dependencia.observatorio.nombre" /></strong></label> <input
							type="text" class="texto form-control" id="nombre" name="nombre" />
					</div>
					<div class="formButton">
						<span onclick="buscar()" class="btn btn-default btn-lg"> <span
							class="glyphicon glyphicon-search" aria-hidden="true"></span> <bean:message
								key="boton.buscar" />
						</span>
					</div>
				</fieldset>
			</form>

			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg"
					onclick="dialogoNuevaDependencia()"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true"
					data-toggle="tooltip" title=""
					data-original-title="Crear una semilla"></span> <bean:message
						key="nueva.dependencia.observatorio" />
				</a>
			</p>
			<!-- Grid -->
			<table id="grid">
			</table>



			<p id="paginador"></p>

		</div>
		<p id="pCenter">
			<html:link forward="observatoryMenu"
				styleClass="btn btn-default btn-lg">
				<bean:message key="boton.volver" />
			</html:link>
		</p>
	</div>
	<!-- fin cajaformularios -->
</div>
</div>
