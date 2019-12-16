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
<html:javascript formName="SemillaObservatorioForm" />

<jsp:useBean id="paramsNS" class="java.util.HashMap" />
<c:set target="${paramsNS}" property="action" value="anadir" />
<c:set target="${paramsNS}" property="esPrimera" value="si" />


<!--  JQ GRID   -->
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">

<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js" type="text/javascript"></script>

<script src="/oaw/js/gridSemillas.js" type="text/javascript"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

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
</style>

<!--  JQ GRID   -->
<script>



	$(function() {
	   $("#importFile").change(function (){
	     var fileName = $(this).val();

	     $(this).closest('form').submit();
	    		 
	    		 
	    	 //$('<form method="post" action="/oaw/secure/ViewSemillasObservatorio.do?action=loadSeedsFile" enctype="multipart/form-data"><input type="file" name="fileSeeds" value='+ $(this).val()+'</form>').appendTo('body').submit();
	    		 
	    		 
	    	 
	     
	     
	   });
	});


	function selectXMLFile(){
		
		 document.getElementById('importFile').click();
	}


	var dialog;

	var windowWidth = $(window).width() * 0.8;
	var windowHeight = $(window).height() * 0.85;
	

	function dialogoNuevaSemilla() {

		window.scrollTo(0, 0);

		$('#exitosNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').hide();

		dialog = $("#dialogoNuevaSemilla").dialog({
			minHeight : windowHeight,
			minWidth : windowWidth,
			title: 'RASTREADOR WEB - Nueva semilla',
			modal : true,
			buttons : {
				"Guardar" : {
					click: function() {
						guardarNuevaSemilla();
					},
					text: 'Guardar',
					class: 'jdialog-btn-save'
				},
				"Cancelar" : {
					click: function() {
						dialog.dialog("close");
					},
					text: "Cancelar", 
					class :'jdialog-btn-cancel'
				}
			},
			open : function() {
				cargarSelect();
				
				
				$(document).ready(function() {
					$.ajax({
						url : '/oaw/secure/ViewEtiquetasObservatorio.do?action=search',
						method : 'POST',
						cache : false
					}).success(function(response) {

						$('#tagsFilter').tagbox({
							items : response.etiquetas,
							searchIn : [ 'name' ],
							rowFormat : '<span class="name">{{name}}</span>',
							tokenFormat : '{{name}}',
							valueField : 'id',
							itemClass : 'user',
						});

					})

				});
				
				
			},
			close : function() {
				$('#nuevaSemillaMultidependencia')[0].reset();
				$('#selectDependenciasNuevaSemillaSeleccionadas').html('');
				$('.tagbox-token a').click();
				
			},
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
			method : 'POST',
			cache: false,
			traditional : true,
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

	//Buscador
	function buscar() {
		reloadGrid('/oaw/secure/JsonViewSemillasObservatorio.do?action=buscar&'
				+ $('#SemillaSearchForm').serialize());
	}
	
	function limpiar(){
		$('#SemillaSearchForm')[0].reset();
		reloadGrid('/oaw/secure/JsonViewSemillasObservatorio.do?action=buscar&'
				+ $('#SemillaSearchForm')
						.serialize());
	}

	$(window)
			.on(
					'load',
					function() {

						var $jq = $.noConflict();

						//Primera carga del grid el grid
						$jq(document)
								.ready(
										function() {
											//Desactivar cache de AJAX de forma global
											$.ajaxSetup({ cache: false });
											
											$('#SemillaSearchForm')[0].reset();
											reloadGrid('/oaw/secure/JsonViewSemillasObservatorio.do?action=buscar&'
													+ $('#SemillaSearchForm')
															.serialize());
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
				<li class="active"><bean:message key="migas.semillas.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.semillas.observatorio.titulo" />
			</h2>

			<div id="exitosNuevaSemillaMD" style="display: none"></div>
			<jsp:include page="/common/crawler_messages.jsp" />


			<html:form action="/secure/ViewSemillasObservatorio.do" method="get" styleClass="formulario form-horizontal">
				<input type="hidden" name="<%=Constants.ACTION%>" value="<%=Constants.LOAD%>" />
				<fieldset>
					<legend>Buscador</legend>
					<div class="formItem">
						<label for="nombre" class="control-label"><strong class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.nombre" /></strong></label>
						<html:text styleClass="texto form-control" styleId="nombre" property="nombre" />
					</div>
					<div class="formItem">
						<label for="categoria" class="control-label"><strong class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.categoria" /></strong></label>
						<html:select styleClass="textoSelect form-control" styleId="categoria" property="categoria">
							<html:option value="">
								<bean:message key="resultados.observatorio.cualquier.categoria" />
							</html:option>
							<logic:iterate name="<%=Constants.CATEGORIES_LIST%>" id="categoria">
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
						<label for="url" class="control-label"><strong class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.url" /></strong></label>
						<html:text styleClass="texto form-control" styleId="url" property="url" />
					</div>
					<div class="formButton">
						<span onclick="buscar()" class="btn btn-default btn-lg"> <span class="glyphicon glyphicon-search"
							aria-hidden="true"></span> <bean:message key="boton.buscar" />
						</span> <span onclick="limpiar()" class="btn btn-default btn-lg"> <span aria-hidden="true"></span> <bean:message
								key="boton.limpiar" />
						</span>
					</div>
				</fieldset>
			</html:form>

		</div>

	</div>
	<!-- fin cajaformularios -->

	<div id="fullwidthgrid">

		<div id="fullwidthgridheader">
			<p class="alert alert-info pull-left">
				<span class="glyphicon glyphicon-info-sign"></span> <em><bean:message key="nueva.semilla.webs.informacion" />
				</em>:
				<bean:message key="nueva.semilla.webs.info" />
			</p>

			<!-- Importar todas las semillas -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg " onclick="selectXMLFile()"> <span
					class="glyphicon glyphicon-cloud-upload" aria-hidden="true" data-toggle="tooltip" title=""
					data-original-title="Importar un fichero XML de semillas"></span> <bean:message
						key="cargar.semilla.observatorio.importar.todo" />


				</a>
			</p>


			<form method="post" style="display: none" action="/oaw/secure/SeedMassImport.do?action=confirm"
				enctype="multipart/form-data">
				<div class="formItem">
					<label for="fileSeeds" class="control-label"><strong class="labelVisu"><bean:message
								key="categoria.semillas.fichero" />: </strong></label> <input type="file" id="importFile" name="fileSeeds" style="display: none">
				</div>
			</form>


			<!-- Exportar todas las semillas -->
			<p class="pull-right">
				<a href="/oaw/secure/SeedMassImport.do?action=exportAllSeeds"> <span class="btn btn-default btn-lg"> <span
						class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip" title=""
						data-original-title="Exportar todas las semillas a un fichero XML"></span> <bean:message
							key="cargar.semilla.observatorio.exportar.todo" />
				</span>
				</a>
			</p>

			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg" onclick="dialogoNuevaSemilla()"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title=""
					data-original-title="Crear una semilla"></span> <bean:message key="cargar.semilla.observatorio.nueva.semilla" />
				</a>
			</p>


		</div>

		<!-- Grid -->
		<table id="grid"></table>
		<p id="paginador"></p>

		<p id="pCenter">
			<html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg">
				<bean:message key="boton.volver" />
			</html:link>
		</p>
	</div>


</div>