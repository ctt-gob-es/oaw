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
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js"
	type="text/javascript"></script>

<script src="/oaw/js/gridSemillas.js" type="text/javascript"></script>


<!--  JQ GRID   -->
<script>

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

			<div id="exitosNuevaSemillaMD" style="display: none"></div>

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

			<!-- Nueva semilla -->
			<p class="pull-right">
				<a href="#" class="btn btn-default btn-lg"
					onclick="dialogoNuevaSemilla()"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true"
					data-toggle="tooltip" title=""
					data-original-title="Crear una semilla"></span> <bean:message
						key="cargar.semilla.observatorio.nueva.semilla" />
				</a>
			</p>
			<!-- Grid -->
			<table id="grid">
			</table>



			<p id="paginador"></p>

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
