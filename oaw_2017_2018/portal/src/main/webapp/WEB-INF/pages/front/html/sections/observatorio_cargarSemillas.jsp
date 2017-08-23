<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<html:javascript formName="SemillaObservatorioForm" />

<jsp:useBean id="paramsNS" class="java.util.HashMap" />
<c:set target="${paramsNS}" property="action" value="anadir" />
<c:set target="${paramsNS}" property="esPrimera" value="si" />

<style>
.ui-jqgrid-htable, #grid {
	border: none !Important;
	margin: 0 !Important;
	font-size: 14px !Important;
}

.ui-jqgrid .ui-jqgrid-bdiv {
	overflow-x: hidden !Important;
	overflow-y: auto !Important;;
}

.ui-th-ltr, .ui-jqgrid .ui-jqgrid-htable th.ui-th-ltr {
	padding: 1%;
	font-weight: bold;
}

.ui-jqgrid .ui-jqgrid-bdiv tr.ui-row-ltr>td {
	padding: 5px;
}

.ui-jqgrid {
	clear: both;
}

/* Para evitar el parpadeo al recargar */
.ui-jqgrid-bdiv {
	min-height: 500px !Important;
}
</style>

<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
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
	var dialog;

	var windowWidth = $(window).width() * 0.8;
	var windowHeight = $(window).height() * 0.8;

	function dialogoNuevaSemilla() {

		window.scrollTo(0, 0);

		$('#exitosNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').hide();

		dialog = $("#dialogoNuevaSemilla").dialog({
			height : windowHeight,
			width : windowWidth,
			modal : true,
			buttons : {
				"Guardar" : function() {
					guardarNuevaSemilla();
				},
				"Cancelar" : function() {
					dialog.dialog("close");
				}
			},
			open : function() {
				cargarSelect();
			},
			close : function() {
				$('#nuevaSemillaMultidependencia')[0].reset();
				$('#selectDependenciasNuevaSemillaSeleccionadas').html('');
			}
		});

	}

	//Guardar la nueva semilla

	function guardarNuevaSemilla() {
		$('#exitosNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').html("");

		var guardado = $.ajax({
			url : '/oaw/secure/JsonSemillasObservatorio.do?action=save',
			data : $('#nuevaSemillaMultidependencia').serialize(),
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

	// Recarga el grid. Recibe como parámetro la url de la acción con la información de paginación.
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

							ajaxJson = JSON.stringify(data.semillas);

							total = data.paginador.total;

							$('#grid')
									.jqGrid(
											{
												editUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
												colNames : [ "Id",
														"NombreAntiguo",
														"Nombre", "Acrónimo",
														"Segmento",
														"Dependencia", "URLs",
														"Activa", "Directorio" ],
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
																			s += '<option value="'+ri.id+'">'
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

																	//Seleccionar las que ya asociadas

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
																	var s = '<select multiple>';

																	if (response
																			&& response.length) {
																		for (var i = 0, l = response.length; i < l; i++) {
																			var ri = response[i];

																			if ($
																					.inArray(
																							ri.id,
																							idsDependencias) >= 0) {

																				s += '<option selected="selected" value="'+ri.id+'">'
																						+ ri.name
																						+ '</option>';

																			} else {
																				s += '<option value="'+ri.id+'">'
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
															edittype : 'textarea',
															sortable : false,
															editoptions : {
																style : "height:100px; width:100%; text-align:left;"
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
												beforeSelectRow : function(
														rowid) {
													var $self = $(this), i,
													// savedRows array is not empty if some row is in inline editing mode
													savedRows = $self.jqGrid(
															"getGridParam",
															"savedRow");
													for (i = 0; i < savedRows.length; i++) {
														if (savedRows[i].id !== rowid) {
															// save currently editing row
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
													return savedRows.length === 0; // allow selection if saving successful
												},
											}).jqGrid("inlineNav");

							//Recargar el grid
							$('#grid').jqGrid('setGridParam', {
								data : JSON.parse(ajaxJson)
							}).trigger('reloadGrid');

							//Paginador
							paginas = data.paginas;

							$('#paginador').empty();

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
											'<span class="'+value.styleClass+' btn">'
													+ value.title + '</span>');
								}

							});
						}).error(function(data) {
					console.log("Error")
					console.log(data)
				});

	}

	//Buscador
	function buscar() {
		reloadGrid('/oaw/secure/JsonSemillasObservatorio.do?action=buscar&'
				+ $('#SemillaSearchForm').serialize());
	}

	$(window).on('load', function() {

		var $jq = $.noConflict();

		var lastUrl;

		//Primera carga del grid el grid
		$jq(document).ready(function() {
			reloadGrid('JsonSemillasObservatorio.do?action=buscar');
		});

	});
</script>


<div id="dialogoNuevaSemilla" style="display: none">
	<jsp:include page="./observatorio_nuevaSemilla_multidependencia.jsp"></jsp:include>

</div>

<!-- observatorio_cargarSemillas.jsp -->
<div id="main">

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
						key="migas.semillas.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.semillas.observatorio.titulo" />
			</h2>

			<div id="exitosNuevaSemillaMD" style="display: none"></div>

			<html:form action="/secure/ViewSemillasObservatorio.do" method="get"
				styleClass="formulario form-horizontal">
				<input type="hidden" name="<%=Constants.ACTION%>"
					value="<%=Constants.LOAD%>" />
				<fieldset>
					<legend>Buscador</legend>
					<jsp:include page="/common/crawler_messages.jsp" />
					<div class="formItem">
						<label for="nombre" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.nombre" /></strong></label>
						<html:text styleClass="texto form-control" styleId="nombre"
							property="nombre" />
					</div>
					<div class="formItem">
						<label for="categoria" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.categoria" /></strong></label>
						<html:select styleClass="textoSelect form-control"
							styleId="categoria" property="categoria">
							<html:option value="">
								<bean:message key="resultados.observatorio.cualquier.categoria" />
							</html:option>
							<logic:iterate name="<%=Constants.CATEGORIES_LIST%>"
								id="categoria">
								<bean:define id="idCategoria">
									<bean:write name="categoria" property="id" />
								</bean:define>
								<html:option value="<%=idCategoria%>">
									<bean:write name="categoria" property="name" />
								</html:option>
							</logic:iterate>
						</html:select>
					</div>
					<div class="formItem">
						<label for="url" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.url" /></strong></label>
						<html:text styleClass="texto form-control" styleId="url"
							property="url" />
					</div>
					<div class="formButton">
						<%-- 						<button type="submit" class="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							<bean:message key="boton.buscar" />
						</button> --%>

						<span onclick="buscar()" class="btn btn-default btn-lg"> <span
							class="glyphicon glyphicon-search" aria-hidden="true"></span> <bean:message
								key="boton.buscar" />
						</span>
					</div>
				</fieldset>
			</html:form>

			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg"
					onclick="dialogoNuevaSemilla()"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true"
					data-toggle="tooltip" title=""
					data-original-title="Crear una semilla"></span> <bean:message
						key="cargar.semilla.observatorio.nueva.semilla" />
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
