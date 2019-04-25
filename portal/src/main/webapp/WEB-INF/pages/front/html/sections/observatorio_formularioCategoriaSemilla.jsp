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
<html:javascript formName="CategoriaForm" />


<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js"
	type="text/javascript"></script>

<script src="/oaw/js/gridSemillas.js" type="text/javascript"></script>



<script type="text/javascript">
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
				<li><html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link></li>
				<li><html:link forward="getSeedCategories">
						<bean:message key="migas.categoria" />
					</html:link></li>
				<li class="active"><logic:equal
						parameter="<%=Constants.ACTION%>"
						value="<%=Constants.NEW_SEED_CATEGORY%>">
						<bean:message key="migas.nueva.categoria" />
					</logic:equal> <logic:equal parameter="<%=Constants.ACTION%>"
						value="<%=Constants.EDIT_SEED_CATEGORY%>">
						<bean:message key="migas.modificar.categoria" />
					</logic:equal></li>
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
				</bean:message>
			</p>
			<p>
				<bean:message key="leyenda.campo.obligatorio" />
			</p>

			<html:form styleClass="formulario form-horizontal" method="post"
				action="/secure/SeedCategoriesAction" enctype="multipart/form-data"
				onsubmit="return validateCategoriaForm(this)">
				<html:hidden property="id" />
				<input type="hidden" name="<%=Constants.ACTION%>"
					value="<bean:write name="<%=Constants.ACTION%>"/>" />
				<fieldset>
					<jsp:include page="/common/crawler_messages.jsp" />
					<div class="formItem">
						<label for="name" class="control-label"><strong
							class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
									key="migas.categoria" />: </strong></label>
						<html:text styleClass="texto form-control" property="name"
							styleId="name" maxlength="30" />
					</div>
					<div class="formItem">
						<label for="orden" class="control-label"><strong
							class="labelVisu"><bean:message
									key="categoria.semillas.orden" />: </strong></label>
						<html:select styleClass="textoSelect form-control" styleId="orden"
							property="orden">
							<option value="1"
								<c:if test="${CategoriaForm.orden==1}">selected="selected"</c:if>>1</option>
							<option value="2"
								<c:if test="${CategoriaForm.orden==2}">selected="selected"</c:if>>2</option>
							<option value="3"
								<c:if test="${CategoriaForm.orden==3}">selected="selected"</c:if>>3</option>
							<option value="4"
								<c:if test="${CategoriaForm.orden==4}">selected="selected"</c:if>>4</option>
							<option value="5"
								<c:if test="${CategoriaForm.orden==5}">selected="selected"</c:if>>5</option>
							<option value="6"
								<c:if test="${CategoriaForm.orden==6}">selected="selected"</c:if>>6</option>
							<option value="7"
								<c:if test="${CategoriaForm.orden==7}">selected="selected"</c:if>>7</option>
							<option value="8"
								<c:if test="${CategoriaForm.orden==8}">selected="selected"</c:if>>8</option>
							<option value="9"
								<c:if test="${CategoriaForm.orden==9}">selected="selected"</c:if>>9</option>
							<option value="10"
								<c:if test="${CategoriaForm.orden==10}">selected="selected"</c:if>>10</option>
						</html:select>
					</div>
					<div class="formItem">
						<label for="fileSeeds" class="control-label"><strong
							class="labelVisu"><bean:message
									key="categoria.semillas.fichero" />: </strong></label>
						<html:file styleClass="texto" property="fileSeeds"
							styleId="fileSeeds" />
					</div>
					<div class="formButton">
						<html:submit styleClass="btn btn-primary btn-lg">
							<bean:message key="boton.aceptar" />
						</html:submit>
					</div>
				</fieldset>
			</html:form>

			<logic:present name="<%=Constants.ID_CATEGORIA%>">


				<p class="pull-right">
					<a href="#" class="btn btn-default btn-lg"
						onclick="dialogoNuevaSemilla()"> <span
						class="glyphicon glyphicon-plus" aria-hidden="true"
						data-toggle="tooltip" title=""
						data-original-title="Crear una semilla"></span> <bean:message
							key="cargar.semilla.observatorio.nueva.semilla" />
					</a>
				</p>

			</logic:present>


			<!-- Grid -->
			<table id="grid">
			</table>



			<p id="paginador"></p>

		</div>

<p id="pCenter"><html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link></p>
	</div>
</div>

