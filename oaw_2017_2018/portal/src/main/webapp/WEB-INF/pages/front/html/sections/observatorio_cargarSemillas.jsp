<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<html:javascript formName="SemillaObservatorioForm" />

<jsp:useBean id="paramsNS" class="java.util.HashMap" />
<c:set target="${paramsNS}" property="action" value="anadir" />
<c:set target="${paramsNS}" property="esPrimera" value="si" />

<style>
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


<!--  JQ GRID   -->
<script>
	var dialog;

	var windowWidth = $(window).width() * 0.8;
	var windowHeight = $(window).height() * 0.8;

	function dialogoNuevaSemilla() {

		window.scrollTo(0, 0);

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
			close : function() {
				$('#nuevaSemillaMultidependencia')[0].reset();
				//allFields.removeClass( "ui-state-error" );
			}
		});

	}

	//Guardar la nueva semilla

	function guardarNuevaSemilla() {
		var guardado = $.ajax({
			url : '/oaw/secure/JsonSemillasObservatorio.do?action=save',
			dataType : 'json',
			data : $('#nuevaSemillaMultidependencia').serialize(),
			method : 'POST'
		}).done(function() {
			reloadGrid(lastUrl);
			dialog.dialog("close");

		}).fail(function() {
			console.log("Fail :( ");
		}

		);

		return guardado;
	}

	// Formatters de celdas
	function categoriaFormatter(cellvalue, options, rowObject) {
		return cellvalue.name;
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
												colNames : [ "Id", "Nombre",
														"Acrónimo", "Segmento",
														"Dependencia", "URLs",
														"Activa" ],
												colModel : [
														{
															name : "id",
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
																
																style: "height:100px; width:100%; text-align:left;",
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
															editoptions: { style: "height:100px; width:100%; text-align:left;" }
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

												],
												inlineEditing : {
													keys : false,
													defaultFocusField : "nombre"
												},
												cmTemplate : {
													autoResizable : true,
													editable : true
												},
												viewrecords : false,
												caption : "Semillas",
												autowidth : true,
												//pager : '#gridbuttons',
												pgbuttons : false,
												pgtext : false,
												pginput : false,
												/* ondblClickRow: function (rowid, status, e) {  */
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
																		beforeSaveRow : function(
																				o,
																				rowid) {
																			/* $('#grid').jqGrid('saveRow',rowid, true, '/oaw/secure/JsonSemillasObservatorio.do?action=update'); 
																			reloadGrid(lastUrl)
																			return false; */
																			$(
																					'#grid')
																					.jqGrid(
																							'saveRow',
																							rowid,
																							{
																								successfunc : function(
																										response) {
																									reloadGrid(lastUrl);
																								},
																								url : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
																								restoreAfterError : true
																							});

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
															// one can replace saveRow to restoreRow in the next line
															/* $self.jqGrid("saveRow", savedRows[i].id, true, '/oaw/secure/JsonSemillasObservatorio.do?action=update');*/

															$self
																	.jqGrid(
																			'saveRow',
																			savedRows[i].id,
																			{
																				successfunc : function(
																						response) {
																					reloadGrid(lastUrl);
																				},
																				url : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
																				restoreAfterError : true
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



			<table id="grid">
			</table>
			<!-- <div id="gridbuttons"></div> -->

			<p id="paginador"></p>

			<input type="button"
				value="<bean:message key="cargar.semilla.observatorio.nueva.semilla" />"
				onclick="dialogoNuevaSemilla()" />



			<%-- 			<div class="detail">
				<logic:notPresent name="<%=Constants.OBSERVATORY_SEED_LIST%>">
					<div class="notaInformativaExito">
						<p>
							<bean:message key="semilla.observatorio.vacia" />
						</p>
						<p>
							<html:link forward="observatorySeeds" name="paramsNS"
								styleClass="boton">
								<bean:message key="cargar.semilla.observatorio.nueva.semilla" />
							</html:link>
							<html:link styleClass="btn btn-default btn-lg"
								forward="indexAdmin">
								<bean:message key="boton.volver" />
							</html:link>
						</p>
					</div>
			</div> 
			</logic:notPresent> --%>

			<%-- 			<logic:present name="<%=Constants.OBSERVATORY_SEED_LIST%>">
				<logic:empty name="<%=Constants.OBSERVATORY_SEED_LIST%>">
					<div class="notaInformativaExito">
						<p>
							<bean:message key="semilla.observatorio.vacia" />
						</p>
						<p>
							<html:link forward="observatorySeeds" name="paramsNS"
								styleClass="boton">
								<bean:message key="cargar.semilla.observatorio.nueva.semilla" />
							</html:link>
							<html:link styleClass="btn btn-default btn-lg"
								forward="indexAdmin">
								<bean:message key="boton.volver" />
							</html:link>
						</p>
					</div>
				</logic:empty>
				<logic:notEmpty name="<%=Constants.OBSERVATORY_SEED_LIST%>">
					<p class="pull-right">
						<html:link forward="observatorySeeds" name="paramsNS"
							styleClass="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-plus" aria-hidden="true"
								data-toggle="tooltip" title="Crear una nueva semilla"></span>
							<bean:message key="cargar.semilla.observatorio.nueva.semilla" />
						</html:link>
					</p>
					<div class="pag">
						<table class="table table-stripped table-bordered table-hover">
							<caption>
								<bean:message key="lista.semillas.observatorio" />
							</caption>
							<tr>
								<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
								<th><bean:message
										key="cargar.semilla.observatorio.categoria" /></th>
								<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
								<th class="accion">Eliminar</th>
							</tr>
							<logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>"
								id="semilla">
								<bean:define id="action"><%=Constants.ACTION%></bean:define>
								<bean:define id="semillaId" name="semilla" property="id" />
								<bean:define id="semillaSTR"><%=Constants.SEMILLA%></bean:define>
								<tr>
									<td style="text-align: left"><bean:define id="actionDet"><%=Constants.ACCION_SEED_DETAIL%></bean:define>
										<jsp:useBean id="params" class="java.util.HashMap" /> <bean:define
											id="actionMod"><%=Constants.ACCION_MODIFICAR%></bean:define>
										<c:set target="${params}" property="${semillaSTR}"
											value="${semillaId}" /> <c:set target="${params}"
											property="${action}" value="${actionMod}" /> <html:link
											forward="observatorySeeds" name="params">
											<span data-toggle="tooltip"
												title="Editar la configuraci&oacute;n de esta semilla" />
											<bean:write name="semilla" property="nombre" />
											</span>
										</html:link> <span class="glyphicon glyphicon-edit pull-right edit-mark"
										aria-hidden="true" /></td>
									<td><bean:write name="semilla" property="categoria.name" />
									</td>
									<td><logic:equal name="semilla" property="activa"
											value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="activa" value="false">
											<bean:message key="no" />
										</logic:equal></td>
									<td><logic:equal value="false" name="semilla"
											property="asociada">
											<jsp:useBean id="paramsD" class="java.util.HashMap" />
											<bean:define id="actionDel"><%=Constants.ACCION_CONFIRMACION_BORRAR%></bean:define>
											<c:set target="${paramsD}" property="${semillaSTR}"
												value="${semillaId}" />
											<c:set target="${paramsD}" property="${action}"
												value="${actionDel}" />
											<html:link forward="observatorySeeds" name="paramsD">
												<span class="glyphicon glyphicon-remove" aria-hidden="true"
													data-toggle="tooltip" title="Eliminar esta semilla" />
												<span class="sr-only"><bean:message
														key="eliminar.semilla.observatorio" /></span>
											</html:link>
										</logic:equal> <logic:equal value="true" name="semilla" property="asociada">
											<img src="../images/bt_eliminar_escala_grises.gif"
												alt="<bean:message key="eliminar.semilla.observatorio.desactivado" />" />
										</logic:equal></td>
								</tr>
							</logic:iterate>
						</table>
						<jsp:include page="pagination.jsp" />
					</div>
				</logic:notEmpty>
			</logic:present> --%>
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
