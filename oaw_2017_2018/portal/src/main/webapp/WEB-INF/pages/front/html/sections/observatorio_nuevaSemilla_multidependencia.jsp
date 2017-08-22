<%@ include file="/common/taglibs.jsp"%>

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

<script type="text/javascript">
	var $jn = jQuery.noConflict();

	//Cargar las categorias

	$jn.ajax({
		url : '/oaw/secure/JsonSemillasObservatorio.do?action=listCategorias',
	}).done(
			function(data) {

				var response = $jn.parseJSON(data);

				if (response && response.length) {
					for (var i = 0, l = response.length; i < l; i++) {
						var ri = response[i];
						$jn('#selectCategoriasNuevaSemilla').append(
								'<option value="'+ri.id+'">' + ri.name
										+ '</option>');
					}
				}

			});

	//Cargar las dependencias

	$jn
			.ajax(
					{
						url : '/oaw/secure/JsonSemillasObservatorio.do?action=listDependencias',
					}).done(
					function(data) {

						var response = $jn.parseJSON(data);

						if (response && response.length) {
							for (var i = 0, l = response.length; i < l; i++) {
								var ri = response[i];
								$jn('#selectDependenciasNuevaSemilla').append(
										'<option value="'+ri.id+'">' + ri.name
												+ '</option>');
							}
						}

					});

</script>

<div id="main">

	<h2>
		<bean:message key="gestion.semillas.observatorio.titulo" />
	</h2>

	<form id="nuevaSemillaMultidependencia">
		<fieldset>
			<!-- Nombre -->
			<div class="formItem">
				<label for="nombre" class="labelVisu"><strong
					class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.nombre" /></strong></label>&nbsp;<input
					type="text" id="nombre" name="nombre" />
			</div>
			<!-- Activa -->
			<div class="formItem">
				<label for="nombre" class="labelVisu"><strong
					class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.activa" /></strong></label>&nbsp; <select name="activa">
					<option value="true"><bean:message key="select.yes" /></option>
					<option value="false"><bean:message key="select.no" /></option>
				</select>
			</div>
			<!-- Dependencias -->
			<div class="formItem">
				<label for="dependencia"><strong class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.dependencia" /></strong></label> <select multiple
					id="selectDependenciasNuevaSemilla"></select> <input type='button'
					id="addDependencia" value="&gt;&gt;"
					onclick="$jn(this).prev('select').find('option:selected').remove().appendTo('#selectDependenciasNuevaSemillaSeleccionadas');">
				<input type='button' id="removeDependencia" value="&lt;&lt;"
					onclick="$jn(this).next('select').find('option:selected').remove().appendTo('#selectDependenciasNuevaSemilla');">
				<select multiple id="selectDependenciasNuevaSemillaSeleccionadas" name="dependencias"></select>


			</div>
			<!-- Acronimo -->
			<div class="formItem">
				<label for="acronimo"><strong class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.acronimo" /></strong></label> <input type="text" name="acronimo"/>
			</div>
			<!-- Categoria/Segmento -->
			<div class="formItem">
				<label for="categoria"><strong class="labelVisu"><bean:message
							key="nueva.semilla.webs.categoria" /></strong></label> <select name="segmento"
					id="selectCategoriasNuevaSemilla"></select>
			</div>
			<!-- Urls -->
			<div class="formItem">
				<p class="alert alert-info">
					<span class="glyphicon glyphicon-info-sign"></span> <em><bean:message
							key="nueva.semilla.webs.informacion" /> </em>:
					<bean:message key="nueva.semilla.webs.info" />
				</p>
				<label for="listaUrlsString"><strong class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.url" /></strong></label>
				<textarea rows="5" cols="50" name="urls"></textarea>
			</div>
			<!-- Directorio -->
			<div class="formItem">
				<label for="inDirectory"><strong class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.in.directory" /></strong></label> <select name="directorio">
					<option value="true">
						<bean:message key="select.yes" />
					</option>
					<option value="false">
						<bean:message key="select.no" />
					</option>
				</select>
			</div>
			
			<!-- <input type='button' value="Guardar" onclick="guardar()"/> -->
		</fieldset>
	</form>
</div>