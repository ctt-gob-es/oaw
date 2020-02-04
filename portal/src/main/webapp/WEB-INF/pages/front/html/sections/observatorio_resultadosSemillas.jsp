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
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<html:javascript formName="SemillaObservatorioForm" />

<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">


<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js" type="text/javascript"></script>

<script src="/oaw/js/gridSemillasResultado.js" type="text/javascript"></script>


<!--  JQ GRID   -->
<script>
	//Buscador
	function buscar() {
		reloadGrid('JsonResultadoObservatorio.do?action=resultados&id_observatorio='
				+ $('[name=id_observatorio]').val()
				+ '&idExObs='
				+ $('[name=idExObs]').val()
				+ '&idCartucho='
				+ $('[name=idCartucho]').val()
				+ '&'
				+ $('#resultadosObsBuscador').serialize());
	}

	function limpiar() {

		$('#resultadosObsBuscador')[0].reset();

		reloadGrid('JsonResultadoObservatorio.do?action=resultados&id_observatorio='
				+ $('[name=id_observatorio]').val()
				+ '&idExObs='
				+ $('[name=idExObs]').val()
				+ '&idCartucho='
				+ $('[name=idCartucho]').val());

	}

	$(window)
			.on(
					'load',
					function() {

						//Desactivar cache de AJAX de forma global
						$.ajaxSetup({
							cache : false
						});

						var $jq = $.noConflict();

						var lastUrl;

						$('#resultadosObsBuscador')[0].reset();

						//Primera carga del grid el grid
						$jq(document)
								.ready(
										function() {
											reloadGrid('JsonResultadoObservatorio.do?action=resultados&id_observatorio='
													+ $(
															'[name=id_observatorio]')
															.val()
													+ '&idExObs='
													+ $('[name=idExObs]').val()
													+ '&idCartucho='
													+ $('[name=idCartucho]')
															.val());
										});

					});

	var dialog;

	var windowWidth = $(window).width() * 0.8;
	var windowHeight = $(window).height() * 0.8;

	function dialogoEditarSemilla(rowid) {

		window.scrollTo(0, 0);
		
		windowWidth = $(window).width() * 0.8;
		windowHeight = $(window).height() * 0.8;

		$('#exitosNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').hide();

		dialog = $("#dialogoEditarSemilla").dialog({
			height : windowHeight,
			width : windowWidth,
			modal : true,
			title: 'RASTREADOR WEB - Editar semilla',
			buttons : {
				"Guardar" : {
					click: function() {
						editarNuevaSemilla();
					},
					text : "Guardar",
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

				//Pasamos la fila
				var a  = $('#grid').getLocalRow(rowid);
				cargarSelect(a);

			},
			close : function() {
				$('#nuevaSemillaMultidependencia')[0].reset();
				$('#selectDependenciasNuevaSemillaSeleccionadas').html('');
				$('.tagbox-token a').click();
				$('.tagbox-wrapper').remove();
				$('#tagsFilter').removeData();
				$('#tagsFilter').show();
				
			}
		});

		//Cargamos los datos

		$('#nuevaSemillaMultidependencia input[name=id]').val(
				$('#grid').getLocalRow(rowid).id);
		$('#nuevaSemillaMultidependencia input[name=nombre]').val(
				$('#grid').getLocalRow(rowid).nombre);
		$('#nuevaSemillaMultidependencia input[name=nombreAntiguo]').val(
				$('#grid').getLocalRow(rowid).nombre);
		$('#nuevaSemillaMultidependencia input[name=acronimo]').val(
				$('#grid').getLocalRow(rowid).acronimo);
		$('#nuevaSemillaMultidependencia textarea[name=listaUrlsString]').val(
				$('#grid').getLocalRow(rowid).listaUrls.toString().replace(/\,/g, '\r\n'));
		$('#nuevaSemillaMultidependencia  select[name=activa] option[value='
						+ $('#grid').getLocalRow(rowid).activa + ']').attr(
				'selected', 'selected');
		$('#nuevaSemillaMultidependencia  select[name=inDirectory] option[value='
						+ $('#grid').getLocalRow(rowid).inDirectory + ']')
				.attr('selected', 'selected');
		$('#nuevaSemillaMultidependencia  select[name=inDirectory] option[value='
						+ $('#grid').getLocalRow(rowid).inDirectory + ']')
				.attr('selected', 'selected');
		
		$('#nuevaSemillaMultidependencia #tagsFilter').val(
				$('#grid').getLocalRow(rowid).tagsString);
		
		
		
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

	}
	
	function dialogAddSeed(){
		
		windowWidth = $(window).width() * 0.3;
		windowHeight = $(window).height() * 0.2;
		
		window.scrollTo(0, 0);

		$('#erroresAddSeedObservatory').hide();

		dialog = $("#dialogAddSeedObservatory").dialog({
			height : windowHeight,
			width : windowWidth,
			modal : true,
			title: 'RASTREADOR WEB - Añadir semilla al observatorio',
			buttons : {
				"Añadir" : {
					click: function() {
						addSeedObservatory($('#selectAddSeedObservatory').val());
					},
					text : "Guardar",
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

				
				
				//Load candidate seed
				$.ajax({
					url : '/oaw/secure/JsonSemillasObservatorio.do?action=candidates&idObservatorio='
					+ $('[name=id_observatorio]').val()
					+ '&idExObs='
					+ $('[name=idExObs]').val(),
					method : 'POST',
					cache : false
				}).success(function(response) {
					
					$('#selectAddSeedObservatory').find('option').remove();
					
					
					
					if (response && response.length) {
						for (var i = 0, l = response.length; i < l; i++) {
							var ri = response[i];
							$jn('#selectAddSeedObservatory')
									.append(
											'<option value="'+ri.id+'">'
													+ ri.nombre
													+ '</option>');
						}
					} else {

						$('#selectAddSeedObservatory').append("<option value=''>---No hay semillas que se puedan añadir---</option>");
					}

				});
				

			},
			close : function() {
				$('#addSeedObservatoryForm')[0].reset();				
			}
		});
	}

	function reduceSize(){
		
		windowWidth = $(window).width() * 0.3;
		windowHeight = $(window).height() * 0.2;
		
		window.scrollTo(0, 0);

		$('#erroresReduceSizeObservatory').hide();

		var dialogoReducir = $('<div id="dialogoReducirContent"></div>');

		dialogoReducir.append('<p>&#191;Desea eliminar el contenido de las tablas relacionadas con esta ejecuci&#243n?</p>');
		
		dialogoReducir
			.dialog({
				autoOpen : false,
				height : windowHeight,
				width : windowWidth,
				modal : true,
				title: 'RASTREADOR WEB - Liberar espacio del observatorio',
				buttons : {
				"Aceptar" : {
					click: function() {
						$
						.ajax(
								{
									url : '/oaw/secure/ViewReducirTablasObservatorio.do?action=removeTables&idExObs='
											+ $('[name=idExObs]').val(),
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

	
	//Guardar la nueva semilla

	function editarNuevaSemilla() {
		$('#exitosNuevaSemillaMD').hide();
		$('#exitosNuevaSemillaMD').html("");
		$('#erroresNuevaSemillaMD').hide();
		$('#erroresNuevaSemillaMD').html("");

		var guardado = $.ajax({
			url : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
			data : $('#nuevaSemillaMultidependencia').serialize(),
			method : 'POST',
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
	
	function addSeedObservatory(idSeed){
		
		
		window.location.href = '/oaw/secure/ResultadosObservatorio.do?action=addSeed&id_observatorio='
			+ $('[name=id_observatorio]').val()
			+ '&idExObs='
			+ $('[name=idExObs]').val()
			+ '&idSemilla=' + idSeed
			+ '&idCartucho='
			+ $('[name=idCartucho]').val();
		
		
		
	}
</script>

<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">


<bean:define id="idCartridgeMalware">
	<inteco:properties key="cartridge.malware.id" file="crawler.properties" />
</bean:define>
<bean:define id="idCartridgeLenox">
	<inteco:properties key="cartridge.lenox.id" file="crawler.properties" />
</bean:define>
<bean:define id="idCartridgeIntav">
	<inteco:properties key="cartridge.intav.id" file="crawler.properties" />
</bean:define>
<bean:define id="idCartridgeMultilanguage">
	<inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" />
</bean:define>

<bean:parameter name="<%=Constants.ID_OBSERVATORIO%>" id="idObservatorio" />
<bean:parameter name="<%=Constants.ID_EX_OBS%>" id="idExObs" />
<bean:parameter name="<%=Constants.ID_CARTUCHO%>" id="idCartucho" />

<div id="dialogoEditarSemilla" style="display: none">
	<jsp:include page="./observatorio_nuevaSemilla_multidependencia.jsp"></jsp:include>

</div>


<div id="dialogAddSeedObservatory" style="display: none">
	<div id="main" style="overflow: hidden">

		<div id="erroresAddSeedObservatory" style="display: none"></div>

		<form id="addSeedObservatoryForm">
			<div class="row formItem">
				<label for="categoria" class="control-label"><strong class="labelVisu"><bean:message
							key="nuevo.observatorio.semillas.nombre" /></strong></label>
				<div class="col-md-8">
					<select name="segmento" id="selectAddSeedObservatory" class="form-control"></select>
				</div>
			</div>
		</form>
	</div>
</div>



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
				<li><html:link forward="resultadosPrimariosObservatorio" paramName="idObservatorio"
						paramId="<%=Constants.ID_OBSERVATORIO%>">
						<bean:message key="migas.indice.observatorios.realizados.lista" />
					</html:link></li>
				<li class="active"><bean:message key="migas.resultado.observatorio" /></li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.resultados.observatorio" />
			</h2>

			<div id="exitosNuevaSemillaMD" style="display: none"></div>

			<html:form action="/secure/ResultadosObservatorio.do" method="get" styleClass="formulario form-horizontal"
				styleId="resultadosObsBuscador">
				<input type="hidden" name="<%=Constants.ACTION%>" value="<%=Constants.GET_SEEDS%>" />
				<input type="hidden" name="<%=Constants.ID_OBSERVATORIO%>" value="<bean:write name="idObservatorio"/>" />
				<input type="hidden" name="<%=Constants.ID_EX_OBS%>" value="<bean:write name="idExObs"/>" />
				<input type="hidden" name="<%=Constants.ID_CARTUCHO%>" value="<bean:write name="<%=Constants.ID_CARTUCHO%>"/>" />
				<fieldset>
					<legend>Buscador</legend>
					<jsp:include page="/common/crawler_messages.jsp" />
					<div class="formItem">
						<label for="nombre" class="control-label"><strong class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.nombre" /></strong></label>
						<html:text styleClass="texto form-control" styleId="nombre" property="nombre" />
					</div>
					<div class="formItem">
						<label for="listaUrlsString" class="control-label"><strong class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.url" /></strong></label>
						<html:text styleClass="texto form-control" styleId="listaUrlsString" property="listaUrlsString" />
					</div>
					<div class="formButton">
						<span onclick="buscar()" class="btn btn-default btn-lg"> <span class="glyphicon glyphicon-search"
							aria-hidden="true"></span> <bean:message key="boton.buscar" />
						</span> <span onclick="limpiar()" class="btn btn-default btn-lg"> <span aria-hidden="true"></span> <bean:message
								key="boton.limpiar" />
						</span>
						</button>
				</fieldset>
			</html:form>

			<jsp:useBean id="params" class="java.util.HashMap" />
			<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
			<c:set target="${params}" property="idCartucho" value="${idCartucho}" />
			<c:set target="${params}" property="idExObs" value="${idExObs}" />

			<p class="pull-right">

				<!-- AÑADIR SEMILLAS -->

				<a onclick="dialogAddSeed()"> <span class="btn btn-default btn-lg"><span
						class="glyphicon glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip"
						title="Regenerar las puntuaciones" /> </span> <span>Añadir semilla</span>
				</a>

				<html:link forward="regenerarResultadosObservatorioSemillas" name="params">
					<span class="btn btn-default btn-lg"><span class="glyphicon glyphicon 	glyphicon glyphicon-repeat"
						aria-hidden="true" data-toggle="tooltip" title="Regenerar las puntuaciones" /> </span>
					<span>Regenerar puntuaciones</span>
				</html:link>
				
				<a onclick="reduceSize()"> <span class="btn btn-default btn-lg"><span
						class="glyphicon glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip"
						title="Liberar espacio" /> </span> <span>Liberar espacio</span>
				</a>
			</p>


			<!-- Grid -->
			<table id="grid">
			</table>

			<p id="paginador"></p>




		</div>
		<!-- fin cajaformularios -->
	</div>
</div>
