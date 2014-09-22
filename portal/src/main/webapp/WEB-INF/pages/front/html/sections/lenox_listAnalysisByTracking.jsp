<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>

	<bean:parameter id="idrastreo" name="idrastreo"/>
	<bean:parameter id="id" name="id"/>
	
	<jsp:useBean id="paramsCFC" class="java.util.HashMap" />
	<c:set target="${paramsCFC}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${paramsCFC}" property="isCliente" value="true" />
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> /
		<logic:notPresent parameter="<%= Constants.ID_OBSERVATORIO %>">
			<logic:present parameter="isCliente">
				<html:link forward="loadClientCrawlings"><bean:message key="migas.rastreos.cliente" /></html:link>
		 		/ <html:link forward="loadClientFulfilledCrawlings" name="paramsCFC"><bean:message key="migas.rastreos.realizados" /></html:link>
			</logic:present> 
			<logic:notPresent parameter="isCliente">
				<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link>
			 	/ <html:link forward="verRastreosRealizados" paramId="<%= Constants.ID_RASTREO %>" paramName="idrastreo"><bean:message key="migas.rastreos.realizados" /></html:link>
			</logic:notPresent>
		 	/ <bean:message key="migas.rastreos.realizados.url.analizadas" />
	 	</logic:notPresent>
	 	<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
	 		<c:set target="${paramsCFC}" property="id_observatorio" value="${id_observatorio}" />
	 		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="resultadosObservatorioSemillas" paramId="<%= Constants.OBSERVATORY_ID %>" paramName="<%= Constants.OBSERVATORY_ID %>"><bean:message key="migas.resultado.observatorio" /></html:link> /
			<html:link forward="resultadosObservatorioSemillasLista" name="paramsCFC"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link> /
			<bean:message key="migas.rastreos.realizados.url.analizadas"/>
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
							<logic:notPresent name="<%=Constants.RESULTS_BY_URL %>">	
								<div class="notaInformativaExito">
									<p><bean:message key="indice.termino.vacio"/></p>
								</div>
							</logic:notPresent>
							<logic:present name="<%=Constants.RESULTS_BY_URL %>">
								<logic:empty name="<%=Constants.RESULTS_BY_URL %>">	
									<div class="notaInformativaExito">
										<p><bean:message key="indice.termino.vacio"/></p>
									</div>
								</logic:empty>
								<logic:notEmpty name="<%=Constants.RESULTS_BY_URL %>">
									<div class="pag">
										<table>
											<thead>
												<tr>
													<th><bean:message key="search.results.domain"/></th>
													<th><bean:message key="search.results.num.terms"/></th>
													<th><bean:message key="search.results.operations"/></th>
												</tr>
											</thead>
											<tbody>
												<logic:iterate id="result" name="<%=Constants.RESULTS_BY_URL %>">
													<jsp:useBean id="params" class="java.util.HashMap" />
													<c:set target="${params}" property="idrastreo" value="${idrastreo}" />
													<c:set target="${params}" property="id" value="${id}" />
													<c:set target="${params}" property="url" value="${result.url}" />
													<logic:present parameter="isCliente">
														<c:set target="${params}" property="isCliente" value="true" />
													</logic:present>
													<tr>
														<td><bean:write name="result" property="url"/></td>
														<td><bean:write name="result" property="numTerms"/></td>
														<bean:define id ="title"><bean:message key="view.analyse.title"/> <bean:write name="result" property="url"/></bean:define>
														<td>
															<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
																<bean:define id="idobservatorioStr" value="<%= Constants.ID_OBSERVATORIO %>" />
																<bean:define id="idobservatorio" name="<%= Constants.ID_OBSERVATORIO %>" />
																<c:set target="${params}" property="${idobservatorioStr}" value="${idobservatorio}" />
															</logic:present>
															<logic:greaterThan name="result" property="numTerms" value="0">
																<html:link forward="showLenoxResultsUrlDetail" name="params" title="<%= title %>"><bean:message key="view.analyse"/></html:link>
															</logic:greaterThan>
														</td>
													</tr>
												</logic:iterate>
											</tbody>
										</table>
							
										<jsp:include page="pagination.jsp" />
									</div>
								</logic:notEmpty>
							</logic:present>
							<logic:notPresent parameter="<%= Constants.ID_OBSERVATORIO %>">
								<p id="pCenter"><html:link forward="verRastreosRealizados" paramId="<%= Constants.ID_RASTREO %>" paramName="idrastreo" styleClass="boton"> <bean:message key="boton.volver"/> </html:link></p>
							</logic:notPresent>
							<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
								<p id="pCenter"><html:link forward="resultadosObservatorioSemillasLista" name="paramsCFC" styleClass="boton"> <bean:message key="boton.volver"/> </html:link></p>
							</logic:present>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
