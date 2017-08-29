<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:javascript formName="JsonSemillaObservatorioForm"/>


<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

<script type="text/javascript">
	var $jn = jQuery.noConflict();

	function cargarSelect() {

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
							}
						});
	}
</script>

<div id="main" style="overflow: hidden">

	<h2>
		<bean:message key="gestion.semillas.observatorio.titulo" />
	</h2>

	<div id="erroresNuevaSemillaMD" style="display: none"></div>

	<html:form styleId="nuevaSemillaMultidependencia" action="/secure/JsonSemillasObservatorio.do?action=save">
		<fieldset>
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

			<!-- Urls -->
			<div class="row formItem">
				<p class="alert alert-info">
					<span class="glyphicon glyphicon-info-sign"></span> <em><bean:message
							key="nueva.semilla.webs.informacion" /> </em>:
					<bean:message key="nueva.semilla.webs.info" />
				</p>
				<label for="listaUrlsString" class="control-label"><strong
					class="labelVisu"><acronym
						title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
							key="nueva.semilla.observatorio.url" /></strong></label>
				<div class="col-xs-8">
					<textarea rows="5" cols="50" name="listaUrlsString" class="form-control"></textarea>
				</div>
			</div>

			<!-- Directorio -->
			<div class="row formItem ">

				<label for="inDirectory" class="control-label"><strong
					class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.in.directory" /></strong></label>
				<div class="col-xs-2">
					<select name="directorio" class="textoSelect form-control">
						<option value="true">
							<bean:message key="select.yes" />
						</option>
						<option value="false">
							<bean:message key="select.no" />
						</option>
					</select>
				</div>
			</div>





		</fieldset>
	</html:form>
</div>