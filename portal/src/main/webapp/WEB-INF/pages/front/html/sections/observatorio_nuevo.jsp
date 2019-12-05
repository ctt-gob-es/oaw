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
<bean:define id="idIntav">
	<inteco:properties key="cartridge.intav.id" file="crawler.properties" />
</bean:define>
<html:xhtml />

<html:javascript formName="NuevoObservatorioForm" />

<bean:define id="okButtonValue">
	<bean:message key="boton.aceptar" />
</bean:define>

<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="/oaw/js/tagbox/tagbox.js" type="text/javascript"></script>
<link rel="stylesheet" href="/oaw/js/tagbox/tagbox.css"> 
<style>
.tagbox-wrapper input {

display: block;
    width: 100% !important;
    height: 34px;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555;
    background-color: #fff;
    background-image: none;
    border: 1px solid #ccc;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    border:none !important;
}


}
</style>

<script>
	$(window).on('load', function() {

		var $jq = $.noConflict();
		$jq(document).ready(function() {
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
					itemClass : 'user'
				});

			})

		});

	});
</script>

<script type="text/javascript">
	function chooseValidation(this){
		if (this.buttonAction == okButtonValue){
			validateNuevoObservatorioForm(this);
		}
	}
</script>

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
				<li class="active"><bean:message key="migas.nuevo.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">

			<h2>
				<bean:message key="nuevo.observatorio.title" />
			</h2>

			<p>
				<bean:message key="leyenda.campo.obligatorio" />
			</p>

			<html:form styleClass="formulario" method="post" action="/secure/NuevoObservatorio.do"
				onsubmit="return chooseValidation(this)">
				<input type="hidden" name="esPrimera" value="no" />
				<fieldset>
					<jsp:include page="/common/crawler_messages.jsp" />

					<div class="formItem">
						<label for="nombre" class="control-label"><strong class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message key="nuevo.observatorio.nombre" />:
						</strong></label>
						<html:text styleClass="texto form-control" name="NuevoObservatorioForm" property="nombre" styleId="nombre"
							maxlength="100" />
					</div>

					<div class="formItem">
						<label for="type" class="control-label"><strong class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message key="nuevo.observatorio.tipo" /></strong></label>
						<html:select name="NuevoObservatorioForm" styleClass="textoSelect form-control" styleId="type" property="tipo.id">
							<logic:iterate name="<%=Constants.TIPOS_VECTOR%>" id="type">
								<bean:define id="idType">
									<bean:write name="type" property="id" />
								</bean:define>
								<html:option value="<%=idType%>">
									<bean:write name="type" property="name" />
								</html:option>
							</logic:iterate>
						</html:select>
					</div>

					<div class="formItem">
						<label for="type" class="control-label"><strong class="labelVisu"> <bean:message
									key="nuevo.observatorio.ambito" /></strong></label>
						<html:select name="NuevoObservatorioForm" styleClass="textoSelect form-control" styleId="ambitoForm"
							property="ambitoForm.id">
							<html:option value="0">-</html:option>
							<logic:iterate name="<%=Constants.AMBITOS_VECTOR %>" id="ambitoForm">
								<bean:define id="idAmbito">
									<bean:write name="ambitoForm" property="id" />
								</bean:define>
								<html:option value="<%=idAmbito%>">
									<bean:write name="ambitoForm" property="name" />
								</html:option>
							</logic:iterate>
						</html:select>
					</div>

					<div class="formItem">
						<label for="activo" class="control-label"><strong class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message key="nuevo.observatorio.activo" /></strong></label>
						<html:select styleClass="textoSelect form-control" styleId="activo" property="activo">
							<html:option value="true">
								<bean:message key="select.yes" />
							</html:option>
							<html:option value="false">
								<bean:message key="select.no" />
							</html:option>
						</html:select>
					</div>

					<div class="formItem">
						<label for="cartucho" class="control-label"><strong class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message key="nuevo.observatorio.cartucho" /></strong></label>
						<html:select name="NuevoObservatorioForm" styleClass="textoSelect form-control" styleId="cartucho"
							property="cartucho.id">
							<logic:iterate name="<%=Constants.CARTUCHOS_VECTOR %>" id="cartucho">
								<bean:define id="idCartucho">
									<bean:write name="cartucho" property="id" />
								</bean:define>
								<html:option value="<%=idCartucho%>">
									<bean:write name="cartucho" property="name" />
								</html:option>
							</logic:iterate>
						</html:select>
					</div>

					<div class="formItem">
						<label for="categoria" class="control-label"><strong class="labelVisu"><bean:message
									key="nuevo.observatorio.categoria" />: </strong></label>
						<html:select size="5" multiple="true" name="NuevoObservatorioForm" property="categoria"
							styleClass="textoSelect form-control" styleId="categoria">
							<logic:iterate name="<%=Constants.SEED_CATEGORIES %>" id="category">
								<bean:define id="idCategory">
									<bean:write name="category" property="id" />
								</bean:define>
								<html:option value="<%=idCategory%>">
									<bean:write name="category" property="name" />
								</html:option>
							</logic:iterate>
						</html:select>
					</div>


					<div class="formItem">
						<label for="tags" class="control-label"><strong class="labelVisu"><bean:message
									key="menu.config.etiquetas" />: </strong></label> 
									
									
									
<!-- 									<input class="textoSelect form-control" name="tags" autocapitalize="off" -->
<!-- 							placeholder="Escriba para buscar..." autofocus id="tagsFilter" type="text" value="" /> -->
							
							
							
							<html:text styleClass="texto form-control" name="NuevoObservatorioForm" property="tagsString" styleId="tagsFilter"
							maxlength="100" />
					</div>

					<div class="formItem">
						<label for="lenguaje" class="control-label"><strong class="labelVisu"><bean:message
									key="nuevo.observatorio.lenguaje" /></strong></label>
						<html:select styleClass="textoSelect form-control" styleId="lenguaje" property="lenguaje">
							<logic:iterate name="NuevoObservatorioForm" property="lenguajeVector" id="lenguaje">
								<bean:define id="idLang">
									<bean:write name="lenguaje" property="id" />
								</bean:define>
								<html:option value="<%=idLang%>">
									<bean:write name="lenguaje" property="name" />
								</html:option>
							</logic:iterate>
						</html:select>
					</div>

					<div class="formItem">
						<label for="periodicidad" class="control-label"><strong class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
									key="nuevo.observatorio.periodicidad" />: </strong></label>
						<html:select styleClass="textoSelect form-control" styleId="periodicidad" property="periodicidad">
							<logic:notEmpty name="NuevoObservatorioForm" property="periodicidadVector">
								<logic:iterate name="NuevoObservatorioForm" property="periodicidadVector" id="periodicidads">
									<c:if test="${NuevoObservatorioForm.periodicidad==periodicidads.id}">
										<option selected value="<bean:write name="periodicidads" property="id"/>">
											<bean:write name="periodicidads" property="nombre" /></option>
									</c:if>
									<c:if test="${NuevoObservatorioForm.periodicidad!=periodicidads.id}">
										<option value="<bean:write name="periodicidads" property="id"/>">
											<bean:write name="periodicidads" property="nombre" /></option>
									</c:if>
								</logic:iterate>
							</logic:notEmpty>
						</html:select>
					</div>

					<div class="formItem">
						<label for="profundidad" class="control-label"><strong class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
									key="nuevo.observatorio.profundidad" />: </strong></label>
						<bean:define id="maxProfundidad">
							<inteco:properties key="profundidadMax.rastreo" file="crawler.properties" />
						</bean:define>
						<html:select styleClass="textoSelect form-control" styleId="profundidad" property="profundidad">
							<c:forEach begin="1" end="${maxProfundidad}" varStatus="status">
								<c:if test="${NuevoObservatorioForm.profundidad==status.count}">
									<option selected value="<c:out value="${status.count}" />">
										<c:out value="${status.count}" /></option>
								</c:if>
								<c:if test="${NuevoObservatorioForm.profundidad!=status.count}">
									<option value="<c:out value="${status.count}" />">
										<c:out value="${status.count}" /></option>
								</c:if>
							</c:forEach>
						</html:select>
					</div>

					<div class="formItem">
						<label for="amplitud" class="control-label"><strong class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message key="nuevo.observatorio.amplitud" />:
						</strong></label>
						<bean:define id="maxAmplitud">
							<inteco:properties key="pagPorNivelMax.rastreo" file="crawler.properties" />
						</bean:define>
						<html:select styleClass="textoSelect form-control" styleId="amplitud" property="amplitud">
							<c:forEach begin="1" end="${maxAmplitud}" varStatus="status">
								<c:if test="${NuevoObservatorioForm.amplitud==status.count}">
									<option selected value="<c:out value="${status.count}" />">
										<c:out value="${status.count}" /></option>
								</c:if>
								<c:if test="${NuevoObservatorioForm.amplitud!=status.count}">
									<option value="<c:out value="${status.count}" />">
										<c:out value="${status.count}" /></option>
								</c:if>
							</c:forEach>
							<option value="<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />"><bean:message
									key="nuevo.observatorio.amplitud.ilimitada" /></option>
						</html:select>
					</div>

					<div class="formItem">
						<label for="pseudoAleatorio" class="control-label"><strong class="labelVisu"><acronym
								title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
									key="nuevo.observatorio.pseudoaleatorio" /></strong></label>
						<html:select styleClass="textoSelect form-control" styleId="pseudoAleatorio" property="pseudoAleatorio">
							<html:option value="true">
								<bean:message key="select.yes" />
							</html:option>
							<html:option value="false">
								<bean:message key="select.no" />
							</html:option>
						</html:select>
					</div>

					<fieldset class="innerFieldset">
						<legend>
							<bean:message key="nuevo.observatorio.fecha.legend" />
						</legend>
						<div class="formItem">
							<label for="fechaInicio" class="labelCorto"><strong class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
										key="nuevo.observatorio.fecha.inicio" />: </strong></label>
							<html:text styleClass="textoCorto form-control" name="NuevoObservatorioForm" property="fechaInicio"
								styleId="fechaInicio"
								onkeyup="escBarra(event, document.forms['NuevoObservatorioForm'].elements['fechaInicio'], 1)" maxlength="10" />
							<span id="calendar"> <img src="../images/boton-calendario.gif"
								onclick="popUpCalendar(this, document.forms['NuevoObservatorioForm'].elements['fechaInicio'], 'dd/mm/yyyy')"
								alt="<bean:message key="img.calendario.alt" />" />
							</span>
							<bean:message key="date.format" />
						</div>

						<div class="formItem">
							<label for="horaInicio" class="labelCorto"><strong class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
										key="nuevo.observatorio.hora.inicio" />: </strong></label>
							<html:select styleClass="textoSelectCorto form-control" styleId="horaInicio" name="NuevoObservatorioForm"
								property="horaInicio">
								<html:options name="<%=Constants.HOURS%>" />
							</html:select>
							<label for="minutoInicio" class="labelCorto"><strong class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym> <bean:message
										key="nuevo.observatorio.minuto.inicio" />: </strong></label>
							<html:select styleClass="textoSelectCorto form-control" styleId="minutoInicio" name="NuevoObservatorioForm"
								property="minutoInicio">
								<html:options name="<%=Constants.MINUTES%>" />
							</html:select>
						</div>
					</fieldset>

					<fieldset>
						<!-- SEMILLAS ASOCIADAS AL RASTREO QUE ES POSIBLE DESVINCULAR DEL MISMO -->
						<div class="detail">
							<logic:notPresent name="<%=Constants.ADD_OBSERVATORY_SEED_LIST%>">
								<div class="notaInformativaExito">
									<p>
										<bean:message key="modificar.observatorio.semillas.anadidas.vacio" />
									</p>
								</div>
							</logic:notPresent>

							<logic:present name="<%=Constants.ADD_OBSERVATORY_SEED_LIST%>">
								<logic:empty name="<%=Constants.ADD_OBSERVATORY_SEED_LIST%>">
									<div class="notaInformativaExito">
										<p>
											<bean:message key="modificar.observatorio.semillas.anadidas.vacio" />
										</p>
									</div>
								</logic:empty>
								<logic:notEmpty name="<%=Constants.ADD_OBSERVATORY_SEED_LIST%>">
									<div class="pag">
										<table>
											<tr>
												<th><bean:message key="nuevo.observatorio.semillas.asociadas.nombre" /></th>
											</tr>
											<bean:define id="resultFrom" name="<%=Constants.OBS_PAGINATION_RESULT_FROM%>" type="java.lang.Integer" />
											<bean:define id="pagination" name="<%=Constants.OBS_PAGINATION%>" type="java.lang.Integer" />
											<logic:iterate name="<%= Constants.ADD_OBSERVATORY_SEED_LIST %>" id="elemento"
												length="<%=pagination.toString()%>" offset="<%=resultFrom.toString()%>">
												<tr>
													<td><bean:write name="elemento" property="nombre" /></td>
												</tr>
											</logic:iterate>
										</table>
										<jsp:include page="pagination.jsp" />
									</div>
								</logic:notEmpty>
							</logic:present>
						</div>
					</fieldset>

					<div class="formButton">
						<html:submit property="buttonAction" styleClass="pull-left btn btn-default btn-lg">
							<bean:message key="boton.aceptar.anadir.semilla" />
						</html:submit>
						<html:submit property="buttonAction" styleClass="btn btn-primary btn-lg">
							<bean:message key="boton.aceptar" />
						</html:submit>
						<html:cancel styleClass="btn btn-default btn-lg">
							<bean:message key="boton.volver" />
						</html:cancel>
					</div>
				</fieldset>
			</html:form>
		</div>
		<!-- fin cajaformularios -->
	</div>
</div>