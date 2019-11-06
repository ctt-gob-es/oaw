<!--
Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
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
<html:javascript formName="JsonSemillaObservatorioForm" />


<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

<script type="text/javascript">
	var $jn = jQuery.noConflict();

	function cargarSelect(rowObject) {

		//Cargar las categorias

		$jn('#selectCategoriasNuevaSemilla').empty();
		$jn
				.ajax(
						{
							url : '/oaw/secure/JsonSemillasObservatorio.do?action=listCategorias',
						})
				.done(
						function(data) {

							var response = $jn.parseJSON(data);

							$jn('#selectCategoriasNuevaSemilla').append(
									"<option value=''></option>");
							if (response && response.length) {
								for (var i = 0, l = response.length; i < l; i++) {
									var ri = response[i];
									$jn('#selectCategoriasNuevaSemilla')
											.append(
													'<option value="'+ri.id+'">'
															+ ri.name
															+ '</option>');
								}
							}

							if (rowObject != null) {

								$jn('#selectCategoriasNuevaSemilla').val(
										rowObject.categoria.id);
							}

						});

		//Cargar los ambitos

		$jn('#selectAmbitosNuevaSemilla').empty();
		$jn.ajax({
			url : '/oaw/secure/JsonSemillasObservatorio.do?action=listAmbitos',
		}).done(
				function(data) {

					var response = $jn.parseJSON(data);

					$jn('#selectAmbitosNuevaSemilla').append(
							"<option value=''></option>");
					if (response && response.length) {
						for (var i = 0, l = response.length; i < l; i++) {
							var ri = response[i];
							$jn('#selectAmbitosNuevaSemilla').append(
									'<option value="'+ri.id+'">' + ri.name
											+ '</option>');
						}
					}

					if (rowObject != null) {

						$jn('#selectAmbitosNuevaSemilla').val(
								rowObject.ambito.id);
					}

				});

		//Cargar las complejidades

		$jn('#selectComplejidadesNuevaSemilla').empty();
		$jn
				.ajax(
						{
							url : '/oaw/secure/JsonSemillasObservatorio.do?action=listComplejidades',
						})
				.done(
						function(data) {

							var response = $jn.parseJSON(data);

							$jn('#selectComplejidadesNuevaSemilla').append(
									"<option value=''></option>");
							if (response && response.length) {
								for (var i = 0, l = response.length; i < l; i++) {
									var ri = response[i];
									$jn('#selectComplejidadesNuevaSemilla')
											.append(
													'<option value="'+ri.id+'">'
															+ ri.name
															+ '</option>');
								}
							}

							if (rowObject != null) {

								$jn('#selectComplejidadesNuevaSemilla').val(
										rowObject.complejidad.id);
							}

						});

		//Cargar las dependencias
		$jn('#selectDependenciasNuevaSemilla').empty();
		$('#selectDependenciasNuevaSemillaSeleccionadas').empty();
		$jn
				.ajax(
						{
							url : '/oaw/secure/JsonSemillasObservatorio.do?action=listDependencias',
						})
				.done(
						function(data) {

							var response = $jn.parseJSON(data);

							if (response && response.length) {
								for (var i = 0, l = response.length; i < l; i++) {
									var ri = response[i];
									$jn('#selectDependenciasNuevaSemilla')
											.append(
													'<option value="'+ri.id+'">'
															+ ri.name
															+ '</option>');
								}

								$(
										'#selectDependenciasNuevaSemillaSeleccionadas')
										.css(
												'width',
												$(
														'#selectDependenciasNuevaSemilla')
														.width()
														+ 'px');

								//Marcar seleccionadas si exiten

								if (rowObject != null) {

									$
											.each(
													rowObject.dependencias,
													function(index, value) {

														$(
																'#selectDependenciasNuevaSemilla option[value='
																		+ value.id
																		+ ']')
																.attr(
																		'selected',
																		'selected');
														$(
																'#selectDependenciasNuevaSemilla')
																.find(
																		'option:selected')
																.remove()
																.appendTo(
																		'#selectDependenciasNuevaSemillaSeleccionadas');
													});
								}
							}
						});
	}
</script>

<div id="main" style="overflow: hidden">

	<!-- <h2>Nueva Semilla</h2> -->

	<div id="erroresNuevaSemillaMD" style="display: none"></div>

	<html:form styleId="nuevaSemillaMultidependencia"
		action="/secure/JsonSemillasObservatorio.do?action=save">
		<fieldset>

			<input type="hidden" name="id" /> <input type="hidden"
				name="nombreAntiguo" />
			<!-- Nombre -->
			<div class="row formItem">
				<label for="nombre" class="control-label"><strong
					class="labelVisu"><acronym
						title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
							key="nueva.semilla.observatorio.nombre" /></strong></label>
				<div class="col-xs-6">
					<input type="text" id="nombre" name="nombre"
						class="texto form-control" />
				</div>
			</div>
			<!-- Activa -->
			<div class="row formItem">
				<label for="nombre" class="control-label"><strong
					class="labelVisu"><acronym
						title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
							key="nueva.semilla.observatorio.activa" /></strong></label>

				<div class="col-xs-2">
					<select name="activa" class="textoSelect form-control">
						<option value="true"><bean:message key="select.yes" /></option>
						<option value="false"><bean:message key="select.no" /></option>
					</select>
				</div>
			</div>
			<!-- Dependencias -->
			<div class="row formItem">
				<label for="dependencia" class="control-label"><strong
					class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.dependencia" /></strong></label>

				<div class="col-xs-4">
					<select multiple class="form-control"
						id="selectDependenciasNuevaSemilla"></select>
				</div>
				<div class="col-xs-1">
					<input type='button' id="addDependencia" value="&gt;&gt;"
						onclick="$('#selectDependenciasNuevaSemilla').find('option:selected').remove().appendTo('#selectDependenciasNuevaSemillaSeleccionadas');">

					<input type='button' id="removeDependencia" value="&lt;&lt;"
						onclick="$('#selectDependenciasNuevaSemillaSeleccionadas').find('option:selected').remove().appendTo('#selectDependenciasNuevaSemilla');">
				</div>
				<div class="col-xs-4">
					<select multiple class="form-control"
						id="selectDependenciasNuevaSemillaSeleccionadas"
						name="dependenciasSeleccionadas"></select>
				</div>


			</div>
			<!-- Acronimo -->
			<div class="row formItem">
				<label for="acronimo" class="control-label"><strong
					class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.acronimo" /></strong></label>
				<div class="col-xs-4">
					<input type="text" name="acronimo" class="texto form-control" />
				</div>
			</div>


			<logic:present parameter="<%=Constants.ID_CATEGORIA%>">
				<input type="hidden" name="segmento"
					value="<c:out value="${CategoriaForm.id }" />" />
			</logic:present>
			<logic:notPresent parameter="<%=Constants.ID_CATEGORIA%>">
				<!-- Categoria/Segmento -->
				<div class="row formItem">
					<label for="categoria" class="control-label"><strong
						class="labelVisu"><bean:message
								key="nueva.semilla.webs.categoria" /></strong></label>
					<div class="col-xs-4">
						<select name="segmento" id="selectCategoriasNuevaSemilla"
							class="textoSelect form-control"></select>
					</div>
				</div>
			</logic:notPresent>


			<logic:present parameter="<%=Constants.ID_AMBITO%>">
				<input type="hidden" name="ambitoaux"
					value="<c:out value="${AmbitoForm.id }" />" />
			</logic:present>
			<logic:notPresent parameter="<%=Constants.ID_AMBITO%>">
				<!-- Ambito/Ambitoaux -->
				<div class="row formItem">
					<label for="ambito" class="control-label"><strong
						class="labelVisu"><bean:message
								key="nueva.semilla.webs.ambito" /></strong></label>
					<div class="col-xs-4">
						<select name="ambitoaux" id="selectAmbitosNuevaSemilla"
							class="textoSelect form-control"></select>
					</div>
				</div>
			</logic:notPresent>

			<logic:present parameter="<%=Constants.ID_COMPLEJIDAD%>">
				<input type="hidden" name="complejidadaux"
					value="<c:out value="${ComplejidadForm.id }" />" />
			</logic:present>
			<logic:notPresent parameter="<%=Constants.ID_COMPLEJIDAD%>">
				<!-- Complejidad/Complejidadaux -->
				<div class="row formItem">
					<label for="complejidad" class="control-label"><strong
						class="labelVisu"><bean:message
								key="nueva.semilla.webs.complejidad" /></strong></label>
					<div class="col-xs-4">
						<select name="complejidadaux" id="selectComplejidadesNuevaSemilla"
							class="textoSelect form-control"></select>
					</div>
				</div>
			</logic:notPresent>

			<!-- Urls -->
			<div class="row formItem">
				<p class="alert alert-info" style="margin-left: 23%;">
					<span class="glyphicon glyphicon-info-sign"></span> <em><bean:message
							key="nueva.semilla.webs.informacion" /> </em>:
					<bean:message key="nueva.semilla.webs.info" />
				</p>
				<label for="listaUrlsString" class="control-label"><strong
					class="labelVisu"><acronym
						title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
							key="nueva.semilla.observatorio.url" /></strong></label>
				<div class="col-xs-8">
					<textarea rows="5" cols="50" name="listaUrlsString"
						class="form-control"></textarea>
				</div>
			</div>

			<!-- Directorio -->
			<div class="row formItem ">

				<label for="inDirectory" class="control-label"><strong
					class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.in.directory" /></strong></label>
				<div class="col-xs-2">
					<select name="inDirectory" class="textoSelect form-control">
						<option value="false">
							<bean:message key="select.no" />
						</option>
						<option value="true">
							<bean:message key="select.yes" />
						</option>
					</select>
				</div>
			</div>





		</fieldset>
	</html:form>
</div>