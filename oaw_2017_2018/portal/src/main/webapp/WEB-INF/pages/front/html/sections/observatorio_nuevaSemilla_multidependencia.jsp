<%@ include file="/common/taglibs.jsp"%>

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

<script type="text/javascript">

	var $jn = jQuery.noConflict();

	//Cargar las categorias
	
	$jn.ajax({
		url : '/oaw/secure/JsonSemillasObservatorio.do?action=listCategorias',
	}).done(function(data) {

		var response = $jn.parseJSON(data);

		if (response && response.length) {
			for (var i = 0, l = response.length; i < l; i++) {
				var ri = response[i];
				$jn('#selectCategoriasNuevaSemilla').append('<option value="'+ri.id+'">' + ri.name + '</option>');
			}
		}

	});
	
	//Cargar las dependencias
	
	$jn.ajax({
		url : '/oaw/secure/JsonSemillasObservatorio.do?action=listDependencias',
	}).done(function(data) {

		var response = $jn.parseJSON(data);

		if (response && response.length) {
			for (var i = 0, l = response.length; i < l; i++) {
				var ri = response[i];
				$jn('#selectDependenciasNuevaSemilla').append('<option value="'+ri.id+'">' + ri.name + '</option>');
			}
		}

	});
	
	
	$jn('[id^=\"addDependencia\"]').click(function (e) {
	    
	    $jn(this).prev('select').find('option:selected').remove().appendTo('#selectCategoriasNuevaSemillaSeleccionadas');
	    
	});

	$jn('[id^=\"removeDependencia\"]').click(function (e) {

	    $jn(this).next('select').find('option:selected').remove().appendTo('#selectCategoriasNuevaSemilla');

	});

	
</script>

<div id="main">

	<h2><bean:message key="gestion.semillas.observatorio.titulo"/></h2>

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
							key="nueva.semilla.observatorio.activa" /></strong></label>&nbsp; <select>
					<option value="true"><bean:message key="select.yes" /></option>
					<option value="false"><bean:message key="select.no" /></option>
				</select>
			</div>
			<!-- Dependencias -->
			<div class="formItem">
				<label for="dependencia"><strong class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.dependencia" /></strong></label>
				<select multiple id="selectDependenciasNuevaSemilla"></select>
				<a id="addDependencia" href="#">&gt;&gt;</a>
				<a id="removeDependencia" href="#">&lt;&lt;</a>
				<select multiple id="selectDependenciasNuevaSemillaSeleccionadas"></select>


			</div>
			<!-- Acronimo -->
			<div class="formItem">
				<label for="acronimo"><strong class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.acronimo" /></strong></label> <input type="text" />
			</div>
			<!-- Categoria/Segmento -->
			<div class="formItem">
				<label for="categoria"><strong class="labelVisu"><bean:message
							key="nueva.semilla.webs.categoria" /></strong></label>
							<select id="selectCategoriasNuevaSemilla"></select>
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
				<textarea rows="5" cols="50"></textarea>
			</div>
			<!-- Directorio -->
			<div class="formItem">
				<label for="inDirectory"><strong class="labelVisu"><bean:message
							key="nueva.semilla.observatorio.in.directory" /></strong></label> <select>
					<option value="true">
						<bean:message key="select.yes" />
					</option>
					<option value="false">
						<bean:message key="select.no" />
					</option>
				</select>
			</div>
		</fieldset>
	</form>
</div>