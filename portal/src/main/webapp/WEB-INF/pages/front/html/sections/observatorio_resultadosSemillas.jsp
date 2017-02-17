<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="SemillaObservatorioForm"/>
	
	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>
	
	<bean:parameter name="<%=Constants.ID_OBSERVATORIO %>" id="idObservatorio"/>
	<bean:parameter name="<%=Constants.ID_EX_OBS%>" id="idExObs"/>
	<bean:parameter name="<%= Constants.ID_CARTUCHO %>" id="idCartucho"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="resultadosPrimariosObservatorio" paramName="idObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
			<bean:message key="migas.resultado.observatorio" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="sem"> <bean:message key="gestion.resultados.observatorio"/> </h1>
	
				<div id="cuerpoprincipal">
				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="gestion.resultados.observatorio"/></h2>
							
							<html:form action="/secure/ResultadosObservatorio.do" method="get" styleClass="formulario">
								<input type="hidden" name="<%= Constants.ACTION %>" value="<%= Constants.GET_SEEDS %>"/>
								<input type="hidden" name="<%= Constants.ID_OBSERVATORIO %>" value="<bean:write name="idObservatorio"/>"/>
								<input type="hidden" name="<%= Constants.ID_EX_OBS %>" value="<bean:write name="idExObs"/>"/>
								<input type="hidden" name="<%= Constants.ID_CARTUCHO %>" value="<bean:write name="<%= Constants.ID_CARTUCHO %>"/>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="nombre"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.nombre" /></strong></label>
										<html:text styleClass="texto" styleId="nombre" property="nombre" />
									</div>
									<div class="formItem">
										<label for="listaUrlsString"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.url" /></strong></label>
										<html:text styleClass="texto" styleId="listaUrlsString" property="listaUrlsString" />
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
									</div>
								</fieldset>
							</html:form>
							
							<div class="detail">
								<logic:notPresent name="<%= Constants.OBSERVATORY_SEED_LIST %>">
									<div class="notaInformativaExito">
										<p id="nBoton10"><bean:message key="semilla.observatorio.vacia"/></p>
									</div>
								</logic:notPresent>
								<logic:present name="<%= Constants.OBSERVATORY_SEED_LIST %>">
									<logic:empty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
										<div class="notaInformativaExito">
											<p><bean:message key="semilla.observatorio.vacia"/></p>
										</div>
									</logic:empty>
									<logic:notEmpty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
										<div class="pag">
											<table class="table table-stripped table-bordered table-hover">
												<caption><bean:message key="lista.semillas.observatorio"/></caption>
												<tr>
													<th><bean:message key="resultados.observatorio.nombre" /></th>

													<th><bean:message key="resultados.observatorio.ultima.puntuacion" /></th>

													<th>Nive accesibilidad</th>
													<th><bean:message key="resultados.observatorio.acciones" /></th>
												</tr>
												<logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>" id="semilla">
													<bean:define id="action"><%= Constants.ACTION %></bean:define>
													<bean:define id="semillaSTR"><%= Constants.SEMILLA %></bean:define>
													<bean:define id="parameterReturnRes" ><%= Constants.RETURN_OBSERVATORY_RESULTS %></bean:define>
													<bean:define id="observatorioSTR" ><%= Constants.ID_OBSERVATORIO %></bean:define>
													<bean:define id="observatorioExSTR" ><%= Constants.ID_EX_OBS %></bean:define>
													<bean:define id="rastreoSTR" ><%= Constants.ID_RASTREO %> </bean:define>
													<bean:define id="deObservatorio" ><%= Constants.ACCION_DE_OBSERVATORIO %> </bean:define>
													<bean:define id="idCartuchoSTR" value="<%= Constants.ID_CARTUCHO %>" />
													<bean:define id="actionSR"><%= Constants.ACCION_MOSTRAR_LISTA_RESULTADOS %></bean:define>
													<bean:define id="idSeedSTR" value="<%= Constants.ID_SEMILLA %>"/>
													
													<jsp:useBean id="paramSTR" class="java.util.HashMap" />
													<c:set target="${paramSTR}" property="${rastreoSTR}" value="${semilla.idCrawling}"/>
													<c:set target="${paramSTR}" property="observatorio" value="si" />
													<c:set target="${paramSTR}" property="${observatorioSTR}" value="${idObservatorio}" />
													<c:set target="${paramSTR}" property="${observatorioExSTR}" value="${idExObs}" />
													<c:set target="${paramSTR}" property="id" value="${semilla.idFulfilledCrawling}"/>
													<c:set target="${paramSTR}" property="${idCartuchoSTR}" value="${idCartucho}"/>
															
													<jsp:useBean id="paramThrow" class="java.util.HashMap" />
													<c:set target="${paramThrow}" property="${observatorioSTR}" value="${idObservatorio}" />
													<c:set target="${paramThrow}" property="${observatorioExSTR}" value="${idExObs}" />
													<c:set target="${paramThrow}" property="${idSeedSTR}" value="${semilla.id}"/>
													<c:set target="${paramThrow}" property="${idCartuchoSTR}" value="${idCartucho}"/>
													
													<jsp:useBean id="paramDelete" class="java.util.HashMap" />
													<c:set target="${paramDelete}" property="${observatorioSTR}" value="${idObservatorio}" />
													<c:set target="${paramDelete}" property="${observatorioExSTR}" value="${idExObs}" />
													<c:set target="${paramDelete}" property="${idCartuchoSTR}" value="${idCartucho}"/>
													<c:set target="${paramDelete}" property="id" value="${semilla.idFulfilledCrawling}"/>
													<c:set target="${paramDelete}" property="${idSeedSTR}" value="${semilla.id}"/>
													
													<tr>
														<td>

																<li><html:link forward="showTracking" name="paramSTR"><bean:write name="semilla" property="name" /></html:link></li>

															<logic:equal parameter="idCartucho" value="<%=idCartridgeLenox%>">
																<li><html:link forward="showLenoxResultsByUrl" name="paramSTR"><bean:write name="semilla" property="name" /></html:link></li>
															</logic:equal>
															<logic:equal parameter="idCartucho" value="<%=idCartridgeMultilanguage%>">
																<li><html:link forward="multilanguageListAnalysis" name="paramSTR"><bean:write name="semilla" property="name" /></html:link></li>
															</logic:equal>
														</td>

															<td>
																<bean:write name="semilla" property="score"/>
															</td>

														<td>
															<logic:equal value="true" name="semilla" property="active">
																<img src="../images/verde.jpg" alt="<bean:message key="resultado.observatorio.activo.alt" />"/>
															</logic:equal>
															<logic:equal value="false" name="semilla" property="active">
																<img src="../images/rojo.jpg" alt="<bean:message key="resultado.observatorio.inActivo.alt" />"/>
															</logic:equal>
														</td>
														<td>
															<ul class="lista_linea">

																<li><html:link forward="showTracking" name="paramSTR"><img src="../images/list.gif" alt="<bean:message key="resultado.observatorio.ver.rastreos.realizados"/>" /></html:link></li>

																<logic:equal parameter="idCartucho" value="<%=idCartridgeLenox%>">
																	<li><html:link forward="showLenoxResultsByUrl" name="paramSTR"><img src="../images/transgender.png" alt="<bean:message key="resultado.observatorio.ver.rastreos.realizados"/>" /></html:link></li>
																</logic:equal>
																<logic:equal parameter="idCartucho" value="<%=idCartridgeMultilanguage%>">
																	<li><html:link forward="multilanguageListAnalysis" name="paramSTR"><img src="../images/multilanguage.png" alt="<bean:message key="resultado.observatorio.ver.rastreos.realizados"/>" /></html:link></li>
																</logic:equal>
																<li><html:link forward="resultadosObservatorioLanzarEjecucion" name="paramThrow"><img src="../images/bt_lanzar.gif" alt="<bean:message key="resultado.observatorio.relanzar.ejecucion.rastreo"/>" /></html:link></li>
																<li><html:link forward="deleteObservatoryCrawlerExecutionConf" name="paramDelete"><img src="../images/bt_eliminar.gif" alt="<bean:message key="resultado.observatorio.eliminar.ejecucion.rastreo" />"/></html:link></li>
															</ul>
														</td>
													</tr>
												</logic:iterate>
											</table>
											<jsp:include page="pagination.jsp" />
										</div>
									</logic:notEmpty>
								</logic:present>
								<div id="pCenter">
									<p><html:link forward="resultadosPrimariosObservatorio" styleClass="boton" paramName="idObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="boton.volver"/></html:link></p>
								</div>
							</div>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 