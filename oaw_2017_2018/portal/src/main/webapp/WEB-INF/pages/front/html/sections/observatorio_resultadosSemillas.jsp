<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<html:javascript formName="SemillaObservatorioForm" />

<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/jqgrid/i18n/grid.locale-es.js"
	type="text/javascript"></script>

<script src="/oaw/js/gridSemillasResultado.js" type="text/javascript"></script>


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
			reloadGrid('JsonResultadoObservatorio.do?action=resultados&id_observatorio='+$('[name=id_observatorio]').val()+'&idExObs='+$('[name=idExObs]').val()+'&idCartucho='+$('[name=idCartucho]').val());
		});

	});
</script>

<style>
.ui-jqgrid-htable, #grid {
	border: none !Important;
	margin: 0 !Important;
	font-size: 14px !Important;
}

.ui-jqgrid .ui-jqgrid-bdiv {
	overflow-x: hidden !Important;
	overflow-y: auto !Important;;
}

.ui-th-ltr, .ui-jqgrid .ui-jqgrid-htable th.ui-th-ltr {
	padding: 1%;
	font-weight: bold;
}

.ui-jqgrid .ui-jqgrid-bdiv tr.ui-row-ltr>td {
	padding: 5px;
}

.ui-jqgrid {
	clear: both;
}

/* Para evitar el parpadeo al recargar */
.ui-jqgrid-bdiv {
	/*min-height: 500px !Important;*/
}

.ui-widget-content a , .ui-widget-content .glyphicon {
    color: #e21430 !important;
}
</style>


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
	<inteco:properties key="cartridge.multilanguage.id"
		file="crawler.properties" />
</bean:define>

<bean:parameter name="<%=Constants.ID_OBSERVATORIO%>"
	id="idObservatorio" />
<bean:parameter name="<%=Constants.ID_EX_OBS%>" id="idExObs" />
<bean:parameter name="<%=Constants.ID_CARTUCHO%>" id="idCartucho" />


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
				<li><html:link forward="resultadosPrimariosObservatorio"
						paramName="idObservatorio"
						paramId="<%=Constants.ID_OBSERVATORIO%>">
						<bean:message key="migas.indice.observatorios.realizados.lista" />
					</html:link></li>
				<li class="active"><bean:message
						key="migas.resultado.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.resultados.observatorio" />
			</h2>

			<html:form action="/secure/ResultadosObservatorio.do" method="get"
				styleClass="formulario form-horizontal">
				<input type="hidden" name="<%=Constants.ACTION%>"
					value="<%=Constants.GET_SEEDS%>" />
				<input type="hidden" name="<%=Constants.ID_OBSERVATORIO%>"
					value="<bean:write name="idObservatorio"/>" />
				<input type="hidden" name="<%=Constants.ID_EX_OBS%>"
					value="<bean:write name="idExObs"/>" />
				<input type="hidden" name="<%=Constants.ID_CARTUCHO%>"
					value="<bean:write name="<%=Constants.ID_CARTUCHO%>"/>" />
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
						<label for="listaUrlsString" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.url" /></strong></label>
						<html:text styleClass="texto form-control"
							styleId="listaUrlsString" property="listaUrlsString" />
					</div>
					<div class="formButton">
						<button type="submit" class="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							<bean:message key="boton.buscar" />
						</button>
					</div>
				</fieldset>
			</html:form>
			
						<!-- Grid -->
			<table id="grid">
			</table>



			<p id="paginador"></p>

			<div class="detail">
				<logic:notPresent name="<%=Constants.OBSERVATORY_SEED_LIST%>">
					<div class="notaInformativaExito">
						<p id="nBoton10">
							<bean:message key="semilla.observatorio.vacia" />
						</p>
					</div>
				</logic:notPresent>
				<logic:present name="<%=Constants.OBSERVATORY_SEED_LIST%>">
					<logic:empty name="<%=Constants.OBSERVATORY_SEED_LIST%>">
						<div class="notaInformativaExito">
							<p>
								<bean:message key="semilla.observatorio.vacia" />
							</p>
						</div>
					</logic:empty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_SEED_LIST%>">
						<div class="pag">
							<table class="table table-stripped table-bordered table-hover">
								<caption>
									<bean:message key="lista.semillas.observatorio" />
								</caption>
								<tr>
									<th><bean:message key="resultados.observatorio.nombre" /></th>
									<th class="accion"><bean:message
											key="resultados.observatorio.ultima.puntuacion" /></th>
									<th class="accion" style="width: 110px">Nivel
										accesibilidad</th>
									<th class="accion">Resultados</th>
									<th class="accion">Informes</th>
									<th class="accion">Relanzar</th>
									<th class="accion">Eliminar</th>
								</tr>
								<logic:iterate name="<%=Constants.OBSERVATORY_SEED_LIST%>"
									id="semilla">
									<bean:define id="action"><%=Constants.ACTION%></bean:define>
									<bean:define id="semillaSTR"><%=Constants.SEMILLA%></bean:define>
									<bean:define id="parameterReturnRes"><%=Constants.RETURN_OBSERVATORY_RESULTS%></bean:define>
									<bean:define id="observatorioSTR"><%=Constants.ID_OBSERVATORIO%></bean:define>
									<bean:define id="observatorioExSTR"><%=Constants.ID_EX_OBS%></bean:define>
									<bean:define id="rastreoSTR"><%=Constants.ID_RASTREO%>
									</bean:define>
									<bean:define id="deObservatorio"><%=Constants.ACCION_DE_OBSERVATORIO%>
									</bean:define>
									<bean:define id="idCartuchoSTR"
										value="<%=Constants.ID_CARTUCHO%>" />
									<bean:define id="actionSR"><%=Constants.ACCION_MOSTRAR_LISTA_RESULTADOS%></bean:define>
									<bean:define id="idSeedSTR" value="<%=Constants.ID_SEMILLA%>" />

									<jsp:useBean id="paramSTR" class="java.util.HashMap" />
									<c:set target="${paramSTR}" property="${rastreoSTR}"
										value="${semilla.idCrawling}" />
									<c:set target="${paramSTR}" property="observatorio" value="si" />
									<c:set target="${paramSTR}" property="${observatorioSTR}"
										value="${idObservatorio}" />
									<c:set target="${paramSTR}" property="${observatorioExSTR}"
										value="${idExObs}" />
									<c:set target="${paramSTR}" property="id"
										value="${semilla.idFulfilledCrawling}" />
									<c:set target="${paramSTR}" property="${idCartuchoSTR}"
										value="${idCartucho}" />
									<c:set target="${paramSTR}" property="regeneratePDF"
										value="true" />


									<jsp:useBean id="paramThrow" class="java.util.HashMap" />
									<c:set target="${paramThrow}" property="${observatorioSTR}"
										value="${idObservatorio}" />
									<c:set target="${paramThrow}" property="${observatorioExSTR}"
										value="${idExObs}" />
									<c:set target="${paramThrow}" property="${idSeedSTR}"
										value="${semilla.id}" />
									<c:set target="${paramThrow}" property="${idCartuchoSTR}"
										value="${idCartucho}" />

									<jsp:useBean id="paramDelete" class="java.util.HashMap" />
									<c:set target="${paramDelete}" property="${observatorioSTR}"
										value="${idObservatorio}" />
									<c:set target="${paramDelete}" property="${observatorioExSTR}"
										value="${idExObs}" />
									<c:set target="${paramDelete}" property="${idCartuchoSTR}"
										value="${idCartucho}" />
									<c:set target="${paramDelete}" property="id"
										value="${semilla.idFulfilledCrawling}" />
									<c:set target="${paramDelete}" property="${idSeedSTR}"
										value="${semilla.id}" />

									<tr>
										<td style="text-align: left"><jsp:useBean id="params"
												class="java.util.HashMap" /> <bean:define id="actionMod"><%=Constants.ACCION_MODIFICAR%></bean:define>
											<c:set target="${params}" property="${semillaSTR}"
												value="${semilla.id}" /> <c:set target="${params}"
												property="${action}" value="${actionMod}" /> <html:link
												forward="observatorySeeds" name="params">
												<span aria-hidden="true" data-toggle="tooltip"
													title="Editar la semilla de este resultado" />
												<bean:write name="semilla" property="name" />
												</span>
											</html:link> <span class="glyphicon glyphicon-edit pull-right edit-mark"
											aria-hidden="true" /></td>
										<td><bean:write name="semilla" property="score" /></td>
										<td><bean:write name="semilla" property="nivel" /></td>
										<td><html:link forward="showTracking" name="paramSTR">
												<span class="glyphicon glyphicon-list-alt"
													aria-hidden="true" data-toggle="tooltip"
													title="Ver resultados de esta semilla" />
												<span class="sr-only">Resultados</span>
											</html:link></td>
										<td><html:link forward="primaryExportPdfAction"
												name="paramSTR">
												<span class="glyphicon glyphicon-cloud-download"
													aria-hidden="true" data-toggle="tooltip"
													title="Descargar el informe individual de esta semilla" />
												<span class="sr-only">Informe individual</span>
											</html:link></td>
										<td><html:link
												forward="resultadosObservatorioLanzarEjecucion"
												name="paramThrow">
												<span class="glyphicon glyphicon-refresh" aria-hidden="true"
													data-toggle="tooltip"
													title="Relanzar el an&aacute;lisis de esta semilla" />
												<span class="sr-only">Reanalizar</span>
											</html:link></td>
										<td><html:link
												forward="deleteObservatoryCrawlerExecutionConf"
												name="paramDelete">
												<span class="glyphicon glyphicon-remove" aria-hidden="true"
													data-toggle="tooltip"
													title="Eliminar este resultado del observatorio" />
												<span class="sr-only">Eliminar</span>
											</html:link></td>
									</tr>
								</logic:iterate>
							</table>
							<jsp:include page="pagination.jsp" />
						</div>
					</logic:notEmpty>
				</logic:present>
				<div id="pCenter">
					<p>
						<html:link forward="resultadosPrimariosObservatorio"
							styleClass="btn btn-default btn-lg" paramName="idObservatorio"
							paramId="<%=Constants.ID_OBSERVATORIO%>">
							<bean:message key="boton.volver" />
						</html:link>
					</p>
				</div>
			</div>
		</div>
		<!-- fin cajaformularios -->
	</div>
</div>
