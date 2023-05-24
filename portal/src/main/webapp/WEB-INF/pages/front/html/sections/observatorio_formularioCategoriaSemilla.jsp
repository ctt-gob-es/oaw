<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnolog�as de la Comunicaci�n, 
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
<html:javascript formName="CategoriaForm" />
<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script>
	var script = document.createElement('script');
	var lang = (navigator.language || navigator.browserLanguage)
	script.src = '/oaw/js/jqgrid/i18n/grid.locale-'+lang.substring(0,2)+'.js';
	document.head.appendChild(script);
</script>


<script src="/oaw/js/gridSemillas.js" type="text/javascript"></script>
<script src="/oaw/js/tagbox/tagbox.js" type="text/javascript"></script>
<link rel="stylesheet" href="/oaw/js/tagbox/tagbox.css">
<style>
/* Make sure you reset e'erything beforehand. */
* {
	margin: 0;
	padding: 0;
}

/* Although you can't see the box here, so add some padding. */
.tagbox-item .name, .tagbox-item .email { /* The name and email within the dropdown */
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

.tagbox-wrapper {
	border: none !important;
	box-shadow: none !important;
}
</style>
<script type="text/javascript">
	var paginadorTotal = '<bean:message key="cargar.semilla.observatorio.buscar.total"/>';

	var colNameId = '<bean:message key="colname.id"/>';
	var colNameAcronym = '<bean:message key="colname.acronym"/>';
	var colNameActive = '<bean:message key="colname.active"/>';
	var colNameComplex = '<bean:message key="colname.complex"/>';
	var colNameDependencies = '<bean:message key="colname.dependecies"/>';
	var colNameDirectory = '<bean:message key="colname.directory"/>';
	var colNameTags = '<bean:message key="colname.etiqeutas"/>';
	var colNameGo = '<bean:message key="colname.go"/>';
	var colNameName = '<bean:message key="colname.name"/>';
	var colNameObs = '<bean:message key="colname.observations"/>';
	var colNameOldName = '<bean:message key="colname.oldname"/>';
	var colNameRemove = '<bean:message key="colname.remove"/>';
	var colNameRemovePerm = '<bean:message key="colname.remove.permanently"/>';
	var colNameScope = '<bean:message key="colname.scope"/>';
	var colNameSegment = '<bean:message key="colname.segment"/>';

	var windowTitleRemoveSeed = '<bean:message key="eliminar.semilla.modal.titulo"/>';

	var saveButton = '<bean:message key="boton.aceptar"/>';

	var cancelButton = '<bean:message key="boton.cancelar"/>';

	var confirmRemoveMessage1 = '<bean:message key="eliminar.semilla.modal.confirmacion.1"/>';
	var confirmRemoveMessage2 = '<bean:message key="eliminar.semilla.modal.confirmacion.2"/>';
	var confirmRemoveMessage3 = '<bean:message key="eliminar.semilla.modal.confirmacion.3"/>';

	var semillaIrAlt = '<bean:message key="semilla.ir"/>';

	var semillaEliminarDefinitiva = '<bean:message key="seed.remove.permanently.alt"/>';

	var translatedColNames = [ colNameId, colNameOldName, colNameName,
			colNameAcronym, colNameSegment, colNameScope, colNameComplex,
			colNameTags, colNameDependencies, "URLs", colNameObs,
			colNameActive, colNameDirectory, colNameGo, colNameRemove,
			colNameRemovePerm ];

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

				// 					$.ajax({
				// 						url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=search',
				// 						method : 'POST',
				// 						cache : false
				// 					}).success(function(response) {

				// 						$('#tagsFilter').tagbox({
				// 							items : response.etiquetas,
				// 							searchIn : [ 'name' ],
				// 							rowFormat : '<span class="name">{{name}}</span>',
				// 							tokenFormat : '{{name}}',
				// 							valueField : 'id',
				// 							itemClass : 'user',
				// 						});

				// 					});

			},
			close : function() {
				$('#nuevaSemillaMultidependencia')[0].reset();
				$('#selectDependenciasNuevaSemillaSeleccionadas').html('');
				//Clear tagbox
				$('.tagbox-token a').click();
				$('.tagbox-wrapper').remove();
				$('#tagsFilter').show();

			}
		});

	}

	//Guardar la nueva semilla

	function guardarNuevaSemilla() {
		$('#exitosNuevaSemillaMD').hide();
		$('#exitosNuevaSemillaMD').html("");
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

	$(window)
			.on(
					'load',
					function() {

						if ($('#CategoriaForm input[name=id]').val() != null
								&& $('#CategoriaForm input[name=id]').val() != "") {

							var $jq = $.noConflict();

							//Primera carga del grid el grid
							$jq(document)
									.ready(
											function() {
												reloadGrid('/oaw/secure/JsonViewSemillasObservatorio.do?action=buscar&categoria='
														+ $(
																'#CategoriaForm input[name=id]')
																.val());
											});
						}

					});
</script>
<logic:present parameter="<%=Constants.ID_CATEGORIA%>">
	<bean:parameter name="<%=Constants.ID_CATEGORIA %>" id="idcat" />
</logic:present>
<div id="dialogoNuevaSemilla" style="display: none">
	<jsp:include page="./observatorio_nuevaSemilla_multidependencia.jsp" />
</div>
<!-- observatorio_formularioCategoriaSemilla.jsp -->
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
				<li>
					<html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link>
				</li>
				<li>
					<html:link forward="getSeedCategories">
						<bean:message key="migas.categoria" />
					</html:link>
				</li>
				<li class="active">
					<logic:equal parameter="<%=Constants.ACTION%>" value="<%=Constants.NEW_SEED_CATEGORY%>">
						<bean:message key="migas.nueva.categoria" />
					</logic:equal>
					<logic:equal parameter="<%=Constants.ACTION%>" value="<%=Constants.EDIT_SEED_CATEGORY%>">
						<bean:message key="migas.modificar.categoria" />
					</logic:equal>
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2>
				<bean:message key="categoria.semillas.titulo" />
			</h2>
			<div id="exitosNuevaSemillaMD" style="display: none"></div>
			<p>
				<bean:message key="categoria.semillas.fichero.info">
					<jsp:attribute name="arg0">
                            <a href="../xml/seeds.xml"
							title="<bean:message key="categoria.semillas.fichero.ejemplo.title"/>" download><bean:message
								key="categoria.semillas.fichero.ejemplo" /></a>
                    </jsp:attribute>
					<jsp:attribute name="arg1">
                            <a href="../xlsx/seeds.xlsx"
							title="<bean:message key="categoria.semillas.fichero.ejemplo.title"/>" download><bean:message
								key="categoria.semillas.fichero.ejemplo" /></a>
                    </jsp:attribute>
				</bean:message>
			</p>
			<p>
				<bean:message key="leyenda.campo.obligatorio" />
			</p>
			<html:form styleClass="formulario form-horizontal" method="post" action="/secure/SeedCategoriesAction"
				enctype="multipart/form-data" onsubmit="return validateCategoriaForm(this)">
				<html:hidden property="id" />
				<input type="hidden" name="<%=Constants.ACTION%>" value="<bean:write name="<%=Constants.ACTION%>"/>" />
				<fieldset>
					<jsp:include page="/common/crawler_messages.jsp" />
					<div class="formItem">
						<label for="name" class="control-label">
							<strong class="labelVisu">
								<acronym title="<bean:message key="campo.obligatorio" />"> * </acronym>
								<bean:message key="migas.categoria" />
								:
							</strong>
						</label>
						<html:text styleClass="texto form-control" property="name" styleId="name" maxlength="256" />
					</div>
					<div class="formItem">
						<label for="orden" class="control-label">
							<strong class="labelVisu">
								<bean:message key="categoria.semillas.orden" />
								:
							</strong>
						</label>
						<html:select styleClass="textoSelect form-control" styleId="orden" property="orden">
							<option value="1" <c:if test="${CategoriaForm.orden==1}">selected="selected"</c:if>>1</option>
							<option value="2" <c:if test="${CategoriaForm.orden==2}">selected="selected"</c:if>>2</option>
							<option value="3" <c:if test="${CategoriaForm.orden==3}">selected="selected"</c:if>>3</option>
							<option value="4" <c:if test="${CategoriaForm.orden==4}">selected="selected"</c:if>>4</option>
							<option value="5" <c:if test="${CategoriaForm.orden==5}">selected="selected"</c:if>>5</option>
							<option value="6" <c:if test="${CategoriaForm.orden==6}">selected="selected"</c:if>>6</option>
							<option value="7" <c:if test="${CategoriaForm.orden==7}">selected="selected"</c:if>>7</option>
							<option value="8" <c:if test="${CategoriaForm.orden==8}">selected="selected"</c:if>>8</option>
							<option value="9" <c:if test="${CategoriaForm.orden==9}">selected="selected"</c:if>>9</option>
							<option value="10" <c:if test="${CategoriaForm.orden==10}">selected="selected"</c:if>>10</option>
							<option value="11" <c:if test="${CategoriaForm.orden==11}">selected="selected"</c:if>>11</option>
							<option value="12" <c:if test="${CategoriaForm.orden==12}">selected="selected"</c:if>>12</option>
							<option value="13" <c:if test="${CategoriaForm.orden==13}">selected="selected"</c:if>>13</option>
							<option value="14" <c:if test="${CategoriaForm.orden==14}">selected="selected"</c:if>>14</option>
							<option value="15" <c:if test="${CategoriaForm.orden==15}">selected="selected"</c:if>>15</option>
							<option value="16" <c:if test="${CategoriaForm.orden==16}">selected="selected"</c:if>>16</option>
							<option value="17" <c:if test="${CategoriaForm.orden==17}">selected="selected"</c:if>>17</option>
							<option value="18" <c:if test="${CategoriaForm.orden==18}">selected="selected"</c:if>>18</option>
							<option value="19" <c:if test="${CategoriaForm.orden==19}">selected="selected"</c:if>>19</option>
							<option value="20" <c:if test="${CategoriaForm.orden==20}">selected="selected"</c:if>>20</option>
						</html:select>
					</div>
					<div class="formItem">
						<label for="principal" class="control-label">
							<strong class="labelVisu">
								<bean:message key="modificar.categoria.principal" />
								:
							</strong>
						</label>
						<html:select property="principal" styleClass="textoSelect form-control" styleId="principal">
							<html:option value="true">
								<bean:message key="select.yes" />
							</html:option>
							<html:option value="false">
								<bean:message key="select.no" />
							</html:option>
						</html:select>
					</div>
					<div class="formItem">
						<label for="name" class="control-label">
							<strong class="labelVisu">
								<bean:message key="modificar.categoria.clave" />
								:
							</strong>
						</label>
						<html:text styleClass="texto form-control" property="key" styleId="name" maxlength="256" />
					</div>
					<div class="formItem">
						<label for="fileSeeds" class="control-label">
							<strong class="labelVisu">
								<bean:message key="categoria.semillas.fichero" />
								:
							</strong>
						</label>
						<html:file styleClass="texto" property="fileSeeds" styleId="fileSeeds" />
					</div>
					<div class="formButton">
						<html:submit styleClass="btn btn-primary btn-lg">
							<bean:message key="boton.aceptar" />
						</html:submit>
					</div>
				</fieldset>
			</html:form>
		</div>
	</div>
	<div id="fullwidthgrid">
		<div id="fullwidthgridheader">
			<p class="alert alert-info pull-left">
				<span class="glyphicon glyphicon-info-sign"></span>
				<em>
					<bean:message key="nueva.semilla.webs.informacion" />
				</em>
				:
				<bean:message key="nueva.semilla.webs.info" />
			</p>
			<logic:present name="<%=Constants.ID_CATEGORIA%>">
				<p class="pull-right">
					<a href="#" class="btn btn-default btn-lg" onclick="dialogoNuevaSemilla()">
						<span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title=""
							data-original-title="Crear una semilla"></span>
						<bean:message key="cargar.semilla.observatorio.nueva.semilla" />
					</a>
				</p>
			</logic:present>
		</div>
		<!-- Grid -->
		<table id="grid">
		</table>
		<p id="paginador"></p>
		<p id="pCenter">
			<html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg">
				<bean:message key="boton.volver" />
			</html:link>
		</p>
	</div>
</div>
