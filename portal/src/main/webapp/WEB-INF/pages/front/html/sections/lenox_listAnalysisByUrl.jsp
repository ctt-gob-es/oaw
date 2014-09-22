<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>

	<bean:parameter name="<%=Constants.URL %>" id="url"/>
	<bean:parameter name="<%=Constants.ACCION %>" id="accion"/>
	<bean:parameter name="<%=Constants.ID %>" id="id"/>
	<bean:parameter name="<%=Constants.ID_RASTREO %>" id="idrastreo"/>
	<bean:define id="idobservatorioStr" value="<%= Constants.ID_OBSERVATORIO %>" />
	<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
		<bean:parameter name="<%= Constants.ID_OBSERVATORIO %>" id="idobservatorio"/>
	</logic:present>
	
	<jsp:useBean id="paramsCFC" class="java.util.HashMap" />
	<c:set target="${paramsCFC}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${paramsCFC}" property="isCliente" value="true" />
	
	<jsp:useBean id="paramsSLR" class="java.util.HashMap" />
	<c:set target="${paramsSLR}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${paramsSLR}" property="id" value="${id}" />

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> /	
		<logic:notPresent parameter="<%= Constants.ID_OBSERVATORIO %>">
				<logic:present parameter="isCliente">
					<html:link forward="loadClientCrawlings"><bean:message key="migas.rastreos.cliente" /></html:link>
			 		/ <html:link forward="loadClientFulfilledCrawlings" name="paramsCFC"><bean:message key="migas.rastreos.realizados" /></html:link>
			 		<c:set target="${paramsCFC}" property="id" value="${id}" />
			 		/ <html:link forward="showLenoxResultsByUrl" name="paramsCFC"><bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
				</logic:present> 
				<logic:notPresent parameter="isCliente">
					<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link>
				 	/ <html:link forward="verRastreosRealizados" paramId="<%= Constants.ID_RASTREO %>" paramName="idrastreo"><bean:message key="migas.rastreos.realizados" /></html:link>
			 		/ <html:link forward="showLenoxResultsByUrl" name="paramsSLR"><bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
				</logic:notPresent>
			 	/ <bean:message key="migas.rastreos.realizados.resultados" />
		</logic:notPresent>
		<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
			<c:set target="${paramsSLR}" property="${idobservatorioStr}" value="${idobservatorio}" />
	 		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="resultadosObservatorioSemillas" paramId="<%= idobservatorioStr %>" paramName="idobservatorio"><bean:message key="migas.resultado.observatorio" /></html:link> /
			<html:link forward="resultadosObservatorioSemillasLista" name="paramsSLR"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link> /
			<html:link forward="showLenoxResultsByUrl" name="paramsSLR"><bean:message key="migas.rastreos.realizados.url.analizadas"/></html:link>
			/ <bean:message key="migas.rastreos.realizados.resultados" />
		</logic:present></p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"> <bean:message key="indice.rastreo.gestion.rastreos.realizados"/> </h1>
	
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2><bean:message key="search.results.by.tracking"/></h2>
							
							<html:form action="/secure/lenoxResultsAction.do" method="get" styleClass="formulario">
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<input type="hidden" name="url" value="<bean:write name="<%=Constants.URL %>"/>"/>
									<input type="hidden" name="accion" value="<bean:write name="<%=Constants.ACCION %>"/>"/>
									<input type="hidden" name="id" value="<bean:write name="<%=Constants.ID %>"/>"/>
									<input type="hidden" name="idrastreo" value="<bean:write name="<%=Constants.ID_RASTREO %>"/>"/>
									<div class="formItem">
										<label for="priority"><strong class="labelVisu"><bean:message key="lenox.crawling.priority" /></strong></label>
										<html:select styleClass="textoSelect" styleId="priority" property="priority" >
											<html:option value=""> - <bean:message key="select.one.femenine"/> - </html:option>
											<html:option value="1"><bean:message key="lenox.crawling.priority.low" /></html:option>
											<html:option value="2"><bean:message key="lenox.crawling.priority.medium" /></html:option>
											<html:option value="3"><bean:message key="lenox.crawling.priority.high" /></html:option>
										</html:select>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
									</div>
								</fieldset>
							</html:form>
							
							<div class="ficha_lenox">
								<ul>
									<li><strong><bean:message key="lenox.crawling.user" />:</strong> <bean:write name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="rastreo.loginUsuario" /></li>
									<li><strong><bean:message key="lenox.crawling.url" /> :</strong> <bean:write name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="url" /></li>
									<li><strong><bean:message key="lenox.crawling.date" />:</strong> <bean:write name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="rastreo.fecha" /></li>
									<li><strong><bean:message key="lenox.crawling.num.localized.terms" />:</strong> <bean:write name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="rastreo.numTerminosLocalizados" /></li>
									<li><strong><bean:message key="lenox.crawling.num.terms" />:</strong> <bean:write name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="rastreo.numTerminosOcurrentes" /></li>
									<li><strong><bean:message key="lenox.crawling.num.terms.high.priority" />:</strong> <bean:write name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="rastreo.numTerminosPrioridadAlta" /></li>
									<li><strong><bean:message key="lenox.crawling.num.terms.medium.priority" />:</strong> <bean:write name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="rastreo.numTerminosPrioridadMedia" /></li>
									<li><strong><bean:message key="lenox.crawling.num.terms.low.priority" />:</strong> <bean:write name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="rastreo.numTerminosPrioridadBaja" /></li>
								</ul>
							</div>
							
							<div class="informe_lenox">
								<logic:empty name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="detalleTerminos">
									<p><bean:message key="errors.noResults"/></p>
								</logic:empty>
							
								<logic:notEmpty name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="detalleTerminos">
									<!-- Iteramos todas las urls del detalle -->
									<logic:iterate name="<%=Constants.DETALLE_RASTREO_LENOX %>" property="detalleTerminos" id="terminoDetalle">
								        <bean:define id="maxContext"><bean:write name="<%= Constants.LENOX_MAX_CONTEXT%>"/></bean:define>
								        <bean:size id="contextSize" name="terminoDetalle" property="contextos"/>	
										<div class="term_lenox">
											<h3><strong><bean:message key="lenox.crawling.term" />:</strong> <bean:write name="terminoDetalle" property="nombre" /> </h3>
								         	<p><strong><bean:message key="lenox.crawling.term.num.times" /></strong> <bean:write name="terminoDetalle" property="numOcurrencias" /> 
								         		<logic:greaterThan value="<%= maxContext %>" name="contextSize">
								         			<bean:message key="lenox.max.context.info" arg0="<%= maxContext %>"/>
								         		</logic:greaterThan>
								         	</p>
								        </div>
								         
								         <logic:iterate name="terminoDetalle" property="contextos" id="resultado" length="<%= maxContext %>">
								         	<ul class="item_lenox">
								         		<li><strong><bean:message key="lenox.crawling.context" />: </strong><bean:write name="resultado" property="contexto" filter="false"/></li>
								         		<li>
								         			<bean:define id="highPriority"><%= Constants.HIGH_LENOX_PRIORITY %></bean:define>
								         			<bean:define id="mediumPriority"><%= Constants.MEDIUM_LENOX_PRIORITY %></bean:define>
								         			<bean:define id="lowPriority"><%= Constants.LOW_LENOX_PRIORITY %></bean:define>
													<strong><bean:message key="lenox.crawling.priority"/>: </strong>
													<c:choose>												          
														<c:when test="${resultado.gravedad == highPriority}">
															<bean:message key="lenox.crawling.priority.low"/>
														</c:when>
														<c:when test="${resultado.gravedad == mediumPriority}">
															<bean:message key="lenox.crawling.priority.medium"/>
														</c:when>
														<c:when test="${resultado.gravedad == lowPriority}">
														    <bean:message key="lenox.crawling.priority.high"/>	
														</c:when>
													</c:choose>
												</li>
								          		<%--<li><strong><bean:message key="lenox.crawling.url" />: </strong><bean:write name="resultado" property="urlTermino" /></li>--%>
								          	</ul>
								         </logic:iterate>
										<ul class="result_lenox">
								         	<logic:iterate name="terminoDetalle" property="alternativas" id="sugerencia">
								         		<li><bean:write name="sugerencia" property="alternativa" />:&nbsp;&nbsp;&nbsp;<bean:write name="sugerencia" property="descripcion" /></li>
								         	</logic:iterate>
								        </ul>
									</logic:iterate>
								</logic:notEmpty>
								<div class="space" />
								<div class="pag">
									<jsp:include page="pagination.jsp" />
								</div>
								<p id="pCenter"><html:link forward="showLenoxResultsByUrl" name="paramsSLR" styleClass="boton"> <bean:message key="boton.volver"/> </html:link></p>
							</div> 
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
