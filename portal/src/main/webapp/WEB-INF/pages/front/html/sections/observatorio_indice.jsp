<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
This program is licensed and may be used, modified and redistributed under the terms
of the European Public License (EUPL), either version 1.2 or (at your option) any later 
version as soon as they are approved by the European Commission.
Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND, either express or implied. See the License for the specific language governing 
permissions and more details.
You should have received a copy of the EUPL1.2 license along with this program; if not, 
you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
-->
<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet" href="/oaw/js/tagbox/tagbox.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js" type="text/javascript"></script>
<script src="https://code.jquery.com/jquery-1.12.1.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<style>
/* Make sure you reset e'erything beforehand. */
* {
	margin: 0;
	padding: 0;
}

/* Although you can't see the box here, so add some padding. */
.tagbox-item .name, .tagbox-item .email {
	/* The name and email within the dropdown */
	display: block;
	float: left;
	width: 35%;
	overflow: hidden;
	text-overflow: ellipsis;
}

.tagbox-item .email {
	float: right;
	width: 65%;
}

.tagbox-wrapper input {
	display: block;
	width: 100% !important;
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 1.42857143;
	color: #555;
	background-color: #fff;
	background-image: none;
	border: 1px solid #ccc;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
	border: none !important;
}

.tagbox-wrapper {
	width: 100% !important;
	border: none !important;
	text-align: left !important;
	box-shadow: none !important;
}
}
</style>
<script type="text/javascript">

var colNameName = '<bean:message key="observatory.extra.config.name"/>';
var colNameValue = '<bean:message key="observatory.extra.config.value"/>';

var $jq = $.noConflict();

	var scroll;
	function reloadGrid(path) {

		lastUrl = path;

		scroll = $jq(window).scrollTop();//keep scroll to top

		$jq('#grid').jqGrid('clearGridData')

		$jq
				.ajax({
					url : path,
					dataType : "json",
					cache : false
				})
				.done(

						function(data) {

							ajaxJson = JSON.stringify(data.configs);

							total = data.paginador.total;

							$jq('#grid')
									.jqGrid(
											{
												editUrl : '/oaw/secure/CargarObservatorio.do?action=loadConfig',
												colNames : [ "id", "key", colNameName,
													colNameValue ],
												colModel : [
														{
															name : "id",
															hidden : true,
															sortable : false
														},
														{
															name : "key",
															hidden : true,
															sortable : false
														},
														{
															name : "name",
															width : 60,
															sortable : false,
															align : "left",
															editable : false
														},
														{
															name : "value",
															width : 40,
															editrules : {
																required : true
															},
															sortable : false,
															align : "left"
														},

												],
												inlineEditing : {
													keys : true,
													defaultFocusField : "value"
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

													var $self = $jq(this), savedRow = $self
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
																		url : '/oaw/secure/CargarObservatorio.do?action=update',
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
													var $self = $jq(this), i, $td = $jq(
															e.target).closest(
															"td"), iCol = $jq.jgrid
															.getCellIndex($td[0]);

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
																				url : '/oaw/secure/CargarObservatorio.do?action=update',
																				restoreAfterError : false,
																			});

														}
													}
													return savedRows.length === 0;
												},
												gridComplete : function() {
													// restore scroll
													$jq(window).scrollTop(scroll);
												}
											}).jqGrid("inlineNav");

							// Reload
							$jq('#grid').jqGrid('setGridParam', {
								data : JSON.parse(ajaxJson)
							}).trigger('reloadGrid');

							$jq('#grid').unbind("contextmenu");

							// No results
							if (total == 0) {
								$jq('#grid')
										.append(
												'<tr role="row" class="ui-widget-content jqgfirstrow ui-row-ltr"><td colspan="9" style="padding: 15px !important;" role="gridcell">Sin resultados</td></tr>');
							}

							// Paginator
							paginas = data.paginas;

							$jq('#paginador').empty();

						}).error(function(data) {
					console.log("Error")
					console.log(data)
				});
	}


var dialog;



var windowWidth = $jq(window).width() * 0.3;
var windowHeight = $jq(window).height() * 0.5;

function extraConfigDialog() {

	window.scrollTo(0, 0);
	var windowTitle = '<bean:message key="observatory.extra.config.title"/>';
	var saveButton = '<bean:message key="boton.guardar"/>';
	var cancelButton = '<bean:message key="boton.cancelar"/>';
	
	$jq('#errorExtraConfig').hide();

	dialog = $jq("#extraConfigDialog").dialog({
		height : windowHeight,
		width : windowWidth,
		modal : true,
		title : windowTitle,
		buttons : {
			"Guardar" : {
				click: function() {
					updateConfig();
				},
				text : saveButton,
				class: 'jdialog-btn-save'
			},
			"Cancelar" : {
				click: function() {
					dialog.dialog("close");
				},
				text: cancelButton,
				class: 'jdialog-btn-cancel'
			}
		},
		open: function(){
			reloadGrid('/oaw/secure/CargarObservatorio.do?action=loadConfig');
		}
	});
}

function updateConfig(){
	//editExtraConfigForm
	
	var guardado = $.ajax({
		url : '/oaw/secure/CargarObservatorio.do?action=saveConfig',
		//data : $jq('#editExtraConfigForm').serialize(),
		data: new FormData($jq("#editExtraConfigForm")[0]),
    	processData: false, 
    	contentType: false, 
		method : 'POST',
		cache : false
	}).success(
			function(response) {
				$jq('#loading_cover_div').fadeOut(1000);
				$jq('#existosPlantilla').addClass('alert alert-success');
				$jq('#existosPlantilla').append("<ul>");

				$.each(JSON.parse(response), function(index, value) {
					$jq('#existosPlantilla').append(
							'<li>' + value.message + '</li>');
				});

				$jq('#existosPlantilla').append("</ul>");
				$jq('#existosPlantilla').show();
				dialog.dialog("close");
				reloadGrid(lastUrl);

			}).error(
			function(response) {
				$jq('#loading_cover_div').fadeOut(1000);
				$jq('#erroresPlantilla').addClass('alert alert-danger');
				$jq('#erroresPlantilla').append("<ul>");

				$.each(JSON.parse(response.responseText), function(index,
						value) {
					$jq('#erroresPlantilla').append(
							'<li>' + value.message + '</li>');
				});

				$jq('#erroresPlantilla').append("</ul>");
				$jq('#erroresPlantilla').show();

			}

	);

	return guardado;
	
}

</script>
<bean:define id="rolObservatory">
	<inteco:properties key="role.observatory.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolAdmin">
	<inteco:properties key="role.administrator.id" file="crawler.properties" />
</bean:define>
<!-- observatorio_indice.jsp -->
<div id="main">
	<div id="extraConfigDialog" style="display: none">
		<div id="main" style="overflow: hidden">
			<div id="errorExtraConfig" style="display: none"></div>
			<table id="grid">
			</table>
			<!-- 			<form id="editExtraConfigForm"> -->
			<%-- 				<logic:notEmpty name="<%=Constants.OBSERVATORY_EXTRA_CONFIGURATION_LIST%>"> --%>
			<!-- 					<table class="table table-stripped table-bordered table-hover" style="width: 100%; overflow-wrap: break-word;"> -->
			<!-- 						<caption> -->
			<%-- 							<bean:message key="observatory.extra.config.title" /> --%>
			<!-- 						</caption> -->
			<!-- 						<thead> -->
			<!-- 							<tr> -->
			<!-- 								<th> -->
			<%-- 									<bean:message key="observatory.extra.config.name" /> --%>
			<!-- 								</th> -->
			<!-- 								<th> -->
			<%-- 									<bean:message key="observatory.extra.config.value" /> --%>
			<!-- 								</th> -->
			<!-- 							</tr> -->
			<!-- 						</thead> -->
			<!-- 						<tbody> -->
			<%-- 							<logic:iterate name="<%=Constants.OBSERVATORY_EXTRA_CONFIGURATION_LIST%>" id="extraConfig"> --%>
			<!-- 								<tr> -->
			<!-- 									<td> -->
			<%-- 										<input type="hidden" name="key" value="<bean:write name="extraConfig" property="key" />" /> --%>
			<%-- 										<bean:write name="extraConfig" property="name" /> --%>
			<!-- 									</td> -->
			<!-- 									<td> -->
			<%-- 										<input name="value" value="<bean:write name="extraConfig" property="value" />" type="number" min="0" step="1" /> --%>
			<!-- 									</td> -->
			<!-- 								</tr> -->
			<%-- 							</logic:iterate> --%>
			<!-- 						</tbody> -->
			<!-- 					</table> -->
			<%-- 				</logic:notEmpty> --%>
			<!-- 			</form> -->
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
				<li class="active">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<bean:message key="migas.observatorio" />
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2 class="pull-left">
				<bean:message key="indice.observatorio.observatorio" />
			</h2>
			<!-- LIST OBSEVATORIES -->
			<jsp:include page="/common/crawler_messages.jsp" />
			<logic:notPresent name="<%=Constants.CARGAR_OBSERVATORIO_FORM%>">
				<div class="notaInformativaExito">
					<p>
						<bean:message key="indice.observatorio.vacio" />
					</p>
					<p>
						<html:link forward="newObservatory" styleClass="btn btn-default btn-lg">
							<bean:message key="indice.observatorio.nuevo.observatorio" />
						</html:link>
						<html:link forward="indexAdmin" styleClass="btn btn-default btn-lg">
							<bean:message key="boton.volver" />
						</html:link>
					</p>
				</div>
			</logic:notPresent>
			<logic:present name="<%=Constants.CARGAR_OBSERVATORIO_FORM%>">
				<logic:empty name="<%=Constants.CARGAR_OBSERVATORIO_FORM%>" property="listadoObservatorio">
					<div class="notaInformativaExito">
						<p>
							<bean:message key="indice.observatorio.vacio" />
						</p>
						<p class="pull-right">
							<html:link forward="newObservatory" styleClass="boton">
								<bean:message key="indice.observatorio.nuevo.observatorio" />
							</html:link>
							<html:link forward="indexAdmin" styleClass="btn btn-default btn-lg">
								<bean:message key="boton.volver" />
							</html:link>
						</p>
					</div>
				</logic:empty>
				<logic:notEmpty name="<%=Constants.CARGAR_OBSERVATORIO_FORM%>" property="listadoObservatorio">
					<p class="pull-right">
						<html:link forward="newObservatory" styleClass="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip"
								title="Crear un nuevo observatorio"></span>
							<bean:message key="indice.observatorio.nuevo.observatorio" />
						</html:link>
						<html:link forward="newMergeObservatoryReport" styleClass="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-globe" aria-hidden="true" data-toggle="tooltip"
								title="<bean:message key="indice.observatorio.informe.global" />"></span>
							<bean:message key="indice.observatorio.informe.global" />
						</html:link>
						<a href="#" class="btn btn-default btn-lg" onclick="extraConfigDialog()">
							<span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip"
								title="<bean:message key="observatory.extra.config.button" />"
								data-original-title="<bean:message key="observatory.extra.config.button" />"></span>
							<bean:message key="observatory.extra.config.button" />
						</a>
					</p>
					<div class="pag">
						<table class="table table-stripped table-bordered table-hover">
							<caption>
								<bean:message key="indice.observatorio.lista" />
							</caption>
							<tr>
								<th>
									<bean:message key="indice.observatorio.nombre" />
								</th>
								<th>
									<bean:message key="indice.observatorio.activo" />
								</th>
								<th>
									<bean:message key="nuevo.observatorio.tipo" />
								</th>
								<th>
									<bean:message key="nuevo.observatorio.ambito" />
								</th>
								<th>
									<bean:message key="indice.observatorio.etiquetas" />
								</th>
								<th>
									<bean:message key="indice.observatorio.cartucho" />
								</th>
								<th class="accion">
									<bean:message key="indice.observatorio.acciones" />
								</th>
								<th class="accion">
									<bean:message key="indice.observatorio.eliminar" />
								</th>
							</tr>
							<logic:iterate name="<%=Constants.CARGAR_OBSERVATORIO_FORM%>" property="listadoObservatorio" id="elemento">
								<jsp:useBean id="params" class="java.util.HashMap" />
								<bean:define id="actionMod" value="<%=Constants.ACCION_MODIFICAR%>" />
								<bean:define id="action" value="<%=Constants.ACTION%>" />
								<bean:define id="observatoryId" name="elemento" property="id_observatorio" />
								<bean:define id="observatorySTR" value="<%=Constants.OBSERVATORY_ID%>" />
								<c:set target="${params}" property="${observatorySTR}" value="${observatoryId}" />
								<c:set target="${params}" property="${action}" value="${actionMod}" />
								<tr>
									<bean:define id="detailTitle">
										<bean:message key="indice.observatorio.detalle.alt" />
									</bean:define>
									<td style="text-align: left">
										<inteco:menu roles="<%=rolAdmin%>">
											<html:link forward="editObservatory" name="params">
												<span data-toggle="tooltip" title='<bean:message key="tooltip.edit.obs"/>'>
													<bean:write name="elemento" property="nombreObservatorio" />
												</span>
											</html:link>
											<span class="glyphicon glyphicon-edit pull-right edit-mark" aria-hidden="true" />
										</inteco:menu>
									</td>
									<td>
										<logic:equal name="elemento" property="estado" value="true">
											<bean:message key="select.yes" />
										</logic:equal>
										<logic:notEqual name="elemento" property="estado" value="true">
											<bean:message key="select.no" />
										</logic:notEqual>
									</td>
									<td>
										<bean:write name="elemento" property="tipo" />
									</td>
									<td>
										<logic:notEmpty name="elemento" property="ambito">
											<bean:write name="elemento" property="ambito" />
										</logic:notEmpty>
										<logic:empty name="elemento" property="ambito">
										-
									</logic:empty>
									</td>
									<td>
										<div class='tagbox-wrapper'>
											<logic:iterate name="elemento" property="etiquetas" id="etiqueta">
												<c:if test="${etiqueta!= null}">
													<div class='tagbox-token'>
														<span>
															<bean:write name="etiqueta" />
														</span>
													</div>
												</c:if>
												<c:if test="${etiqueta == null}">
										-
									</c:if>
											</logic:iterate>
										</div>
									</td>
									<td>
										<bean:write name="elemento" property="cartucho" />
									</td>
									<td>
										<html:link forward="resultadosPrimariosObservatorio" paramId="<%=Constants.ID_OBSERVATORIO%>"
											paramName="elemento" paramProperty="id_observatorio">
											<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key="tooltip.show.iter.obs"/>" />
											<span class="sr-only">
												<bean:message key="results" />
											</span>
										</html:link>
									</td>
									<td><jsp:useBean id="paramsEsPrim" class="java.util.HashMap" />
										<bean:define id="actionEsPrim" value="<%=Constants.ES_PRIMERA%>" />
										<bean:define id="observatoryId" name="elemento" property="id_observatorio" />
										<bean:define id="observatorySTR" value="<%=Constants.OBSERVATORY_ID%>" />
										<c:set target="${paramsEsPrim}" property="${observatorySTR}" value="${observatoryId}" />
										<c:set target="${paramsEsPrim}" property="${actionEsPrim}" value="si" />
										<html:link forward="deleteObservatory" name="paramsEsPrim">
											<span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key="tooltip.obs.remove"/>" />
											<span class="sr-only">
												<bean:message key="remove" />
											</span>
										</html:link>
									</td>
								</tr>
							</logic:iterate>
						</table>
						<jsp:include page="pagination.jsp" />
					</div>
					<!-- <p id="pCenter"><html:link forward="indexAdmin" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p> -->
				</logic:notEmpty>
			</logic:present>
		</div>
		<!-- fin cajaformularios -->
	</div>
	<!-- Container Derecha -->
</div>