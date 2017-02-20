<%@ include file="/common/taglibs.jsp" %>

<%@page import="es.inteco.common.Constants"%>

<bean:parameter id="idrastreo" name="idrastreo"/>
<bean:parameter id="id" name="id"/>
<bean:parameter id="observatorio" name="observatorio"/>
<bean:parameter id="id_observatorio" name="id_observatorio"/>
<bean:parameter id="code" name="code"/>
<bean:parameter id="idCartucho" name="idCartucho"/>

<jsp:useBean id="paramsVolver" class="java.util.HashMap" />
<c:set target="${paramsVolver}" property="idrastreo" value="${idrastreo}" />
<c:set target="${paramsVolver}" property="id_observatorio" value="${id_observatorio}" />

<jsp:useBean id="paramsVolverAFC" class="java.util.HashMap" />
<c:set target="${paramsVolverAFC}" property="idrastreo" value="${idrastreo}" />
<c:set target="${paramsVolverAFC}" property="observatorio" value="${observatorio}" />
<c:set target="${paramsVolverAFC}" property="id" value="${id}" />
<c:set target="${paramsVolverAFC}" property="id_observatorio" value="${id_observatorio}" />
<c:set target="${paramsVolverAFC}" property="idCartucho" value="${idCartucho}" />

<html:xhtml/>
	
<div id="migas">
	<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
	<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		/ <html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link>
		/ <html:link forward="resultadosObservatorioSemillas" paramId="<%= Constants.OBSERVATORY_ID %>" paramName="<%= Constants.OBSERVATORY_ID %>"><bean:message key="migas.resultado.observatorio" /></html:link>
		/ <html:link forward="resultadosObservatorioSemillasLista" name="paramsVolver"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link>
		/ <html:link forward="showTracking" name="paramsVolverAFC"><bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
		/ <bean:message key="migas.resultados.observatorio.puntuacion" />
	</p>
</div>

<!-- Cuerpo -->
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"> <bean:message key="resultados.observatorio.vista.primaria"/> </h1>
	
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2><bean:message key="resultados.observatorio.vista.primaria.h2"/></h2>
							<logic:notPresent name="<%=Constants.EVALUATION_FORM %>" property="groups">	
								<div class="notaInformativaExito">
									<p><bean:message key="indice.rastreo.vacio"/></p>
								</div>
							</logic:notPresent>
							<logic:present name="<%=Constants.EVALUATION_FORM %>" property="groups">
								<logic:empty name="<%=Constants.EVALUATION_FORM %>" property="groups">	
									<div class="notaInformativaExito">
										<p><bean:message key="resultados.observatorio.vista.primaria.vacio"/></p>
									</div>
								</logic:empty>
								<logic:notEmpty name="<%=Constants.EVALUATION_FORM %>" property="groups">
									<div class="detail">
										<div class="observatoryInfo">
											<p>
												<strong><bean:message key="resultados.observatorio.vista.primaria.title"/>: </strong>
												<bean:write name="<%=Constants.EVALUATION_FORM %>" property="source"/>
											</p>
											<p>
												<strong><bean:message key="resultados.observatorio.vista.primaria.url"/>: </strong>
												<bean:write name="<%=Constants.EVALUATION_FORM %>" property="url"/>
											</p>
											<p>
												<strong><bean:message key="observatorio.puntuacion"/>: </strong>
												<bean:write name="<%=Constants.EVALUATION_FORM %>" property="score"/>
											</p>
										</div>
									
										<div class="spacer"></div>
									
										<logic:iterate id="levelGroup" name="<%=Constants.EVALUATION_FORM %>" property="groups" type="es.inteco.intav.form.ObservatoryLevelForm">
											<h3>
												<logic:equal value="Priority 1" name="levelGroup" property="name">
													<bean:message key="first.level.bs"/>
												</logic:equal>
												<logic:equal value="Priority 2" name="levelGroup" property="name">
													<bean:message key="second.level.bs"/>
												</logic:equal>
											</h3>
											<p>
												<strong><bean:message key="observatorio.puntuacion"/>: </strong>
												<bean:write name="levelGroup" property="score"/>
											</p>
											<table>
												<caption>
													<bean:message key="observatorio.desglose.resultados"/>
												</caption>
												<logic:iterate id="suitabilityGroup" name="levelGroup" property="suitabilityGroups">
													<thead>
														<tr>
															<th>
																<bean:message key="observatorio.nivel.adecuacion"/> - <bean:write name="suitabilityGroup" property="name"/> 
															</th>
															<th colspan="2">
																<bean:message key="observatorio.puntuacion"/>: <bean:write name="suitabilityGroup" property="score"/>
															</th>
														</tr>
														<tr class="suitability">
															<th><bean:message key="resultados.observatorio.vista.primaria.grupo"/></th>
															<th><bean:message key="resultados.observatorio.vista.primaria.valor"/></th>
															<th><bean:message key="resultados.observatorio.vista.primaria.modalidad"/></th>
														</tr>
													</thead>
													<tbody>
														<logic:iterate id="subgroup" name="suitabilityGroup" property="subgroups">
															<tr>
																<td class="alignLeft"><bean:write name="subgroup" property="guidelineId"/> : <bean:write name="subgroup" property="description"/></td>
																<logic:equal value="<%= String.valueOf(Constants.OBS_VALUE_NOT_SCORE) %>" name="subgroup" property="value">
																	<td><bean:message key="resultados.observatorio.vista.primaria.valor.noPuntua"/></td>
																	<td><html:img src="../images/modalidadVerde.png" altKey="resultados.observatorio.vista.primaria.modalidad.noPuntua"/></td>
																</logic:equal>
																<logic:equal value="<%= String.valueOf(Constants.OBS_VALUE_GREEN_ONE) %>" name="subgroup" property="value">
																	<td><bean:message key="resultados.observatorio.vista.primaria.valor.uno"/></td>
																	<td><html:img src="../images/modalidadVerde.png" altKey="resultados.observatorio.vista.primaria.modalidad.verde"/></td>
																</logic:equal>
																<logic:equal value="<%= String.valueOf(Constants.OBS_VALUE_GREEN_ZERO) %>" name="subgroup" property="value">
																	<td><bean:message key="resultados.observatorio.vista.primaria.valor.cero"/></td>
																	<td><html:img src="../images/modalidadVerde.png" altKey="resultados.observatorio.vista.primaria.modalidad.verde"/></td>
																</logic:equal>
																<logic:equal value="<%= String.valueOf(Constants.OBS_VALUE_RED_ZERO) %>" name="subgroup" property="value">
																	<td><bean:message key="resultados.observatorio.vista.primaria.valor.cero"/></td>
																	<td><html:img src="../images/modalidadRojo.png"  altKey="resultados.observatorio.vista.primaria.modalidad.rojo"/></td>
																</logic:equal>
															</tr>
														</logic:iterate>
													</tbody>
												</logic:iterate>
											</table>
										</logic:iterate>
										
										<%-- Puntuaciones por aspecto --%>
										<logic:notEmpty name="<%=Constants.EVALUATION_FORM %>" property="aspects">
											<h3><bean:message key="aspect.list.score"/></h3>
											<ul class="resultados">
												<logic:iterate name="<%=Constants.EVALUATION_FORM %>" property="aspects" id="aspect">
													<li>
														<bean:define id="aspectKey">
															<bean:write name="aspect" property="name"/>
														</bean:define>
														<strong><bean:message key="<%=aspectKey %>"/>:</strong> <bean:write name="aspect" property="score"/>
													</li>
												</logic:iterate>
											</ul>
										</logic:notEmpty>
										
										<%-- Puntuaciones por aspecto --%>
										<h3><bean:message key="resultados.observatorio.vista.primaria.errores.por.verificacion"/></h3>
										<logic:notEmpty name="<%=Constants.FAILED_CHECKS %>">
											<ul>
												<logic:iterate name="<%=Constants.FAILED_CHECKS %>" id="failedCheck">
													<li>
														<bean:write name="failedCheck" property="key" filter="false"/>
														<ul>
															<logic:iterate name="failedCheck" property="value" id="failedCheckValue">
																<li><bean:write name="failedCheckValue" filter="false"/></li>
															</logic:iterate>
														</ul>
													</li>
												</logic:iterate>
											</ul>
										</logic:notEmpty>
										
									</div>
								</logic:notEmpty>
							</logic:present>
						</div><!-- fin cajaformularios -->
						<p id="pCenter">
							<html:link styleClass="boton" forward="showTracking" name="paramsVolverAFC"><bean:message key="boton.volver"/> </html:link>
						</p>
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 