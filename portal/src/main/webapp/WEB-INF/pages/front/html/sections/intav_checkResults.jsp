<%@ include file="/common/taglibs.jsp" %>

<bean:define id="confidenceHigh"><inteco:properties key="confidence.level.high" file="intav.properties" /></bean:define>
<bean:define id="confidenceMedium"><inteco:properties key="confidence.level.medium" file="intav.properties" /></bean:define>
<bean:define id="confidenceCanNotTell"><inteco:properties key="confidence.level.cannottell" file="intav.properties" /></bean:define>

<bean:parameter id="idrastreo" name="idrastreo"/>
<bean:parameter name="id" id="id"/>
<bean:parameter name="code" id="code"/>

<!-- Migas -->
<%@page import="es.inteco.common.Constants"%>
<%@page import="es.inteco.intav.properties.PropertiesManager"%>
<div id="migas">
	<jsp:useBean id="paramsCFC" class="java.util.HashMap" />
	<c:set target="${paramsCFC}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${paramsCFC}" property="id" value="${id}" />
	<c:set target="${paramsCFC}" property="code" value="${code}" />
	<c:set target="${paramsCFC}" property="isCliente" value="true" />
	
	<jsp:useBean id="paramsST" class="java.util.HashMap" />
	<c:set target="${paramsST}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${paramsST}" property="id" value="${id}" />
	
	<p class="oculto"><bean:message key="ubicacion.migas"/></p>
	<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> /
	<logic:present parameter="isCliente">
		<html:link forward="loadClientCrawlings"><bean:message key="migas.rastreos.cliente" /></html:link>
 		/ <html:link forward="loadClientFulfilledCrawlings" name="paramsCFC"><bean:message key="migas.rastreos.realizados" /></html:link>
 		/ <html:link forward="showTracking" name="paramsCFC"> <bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
	</logic:present>
	<logic:notPresent parameter="isCliente"> 
		<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link>
	 	/ <html:link forward="verRastreosRealizados" paramId="<%= Constants.ID_RASTREO %>" paramName="idrastreo"><bean:message key="migas.rastreos.realizados" /></html:link>
	 	/ <html:link forward="showTracking" name="paramsST"><bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
	 </logic:notPresent>
 	/ <bean:message key="migas.rastreos.realizados.resultados" /></p>
</div>

<!-- Cuerpo -->

	<div id="cIzq">&nbsp;</div>

		<div id="main">

				<div id="container_menu_izq">
					<jsp:include page="menu.jsp"/>
				</div>
				<div id="container_der">
					<div id="cajaformularios">
						<p id="indice" class="intro"><bean:message key="accesibility.results"/> <a href="<bean:write name="<%=Constants.EVALUATION_FORM %>" property="url"/>"><bean:write name="<%=Constants.EVALUATION_FORM %>" property="url"/></a> <bean:message key="accesibility.results.entity"/> <span class="negrita"><bean:write name="<%=Constants.EVALUATION_FORM %>" property="entity"/></span>.</p>
						<ul class="topo">
							<logic:iterate name="<%=Constants.EVALUATION_FORM %>" property="priorities" id="priority" indexId="index" type="es.inteco.intav.form.PriorityForm">
								<li>
									<a href="#a<%=index + 1%>"><bean:message key='<%=priority.getPriorityName() + ".fulltext"%>'/></a>
									<ul class="resumen">
										<li class="proble"><bean:message key="analyse.problems"/> <span><bean:write name="priority" property="numProblems"/></span></li>
										<li class="advert"><bean:message key="analyse.warnings"/> <span><bean:write name="priority" property="numWarnings"/></span></li>
										<li class="observ"><bean:message key="analyse.observations"/> <span><bean:write name="priority" property="numInfos"/></span></li>
									</ul>
								</li>
							</logic:iterate>
						</ul>
					</div>
					
					<div class="tabber">
						<logic:iterate name="<%=Constants.EVALUATION_FORM %>" property="priorities" id="priority" indexId="counterPriorities" type="es.inteco.intav.form.PriorityForm">
							<bean:define id="titleTab">
								<bean:message key="<%=priority.getPriorityName()%>"/>
							</bean:define>
							<div class="tabbertab" title="<bean:write name="titleTab" />">
								<noscript>
									<h1 id="a<%=counterPriorities + 1%>" class="check"><bean:write name="titleTab" /></h1>
								</noscript>
								<logic:iterate name="priority" property="guidelines" id="guideline">
									<bean:define id="keyDescription">
										<bean:write name="guideline" property="description"/>
									</bean:define>
									<bean:define id="guidelineTotal">
										<bean:message key="<%=keyDescription%>"/>
									</bean:define>
									<logic:notEmpty name="guideline" property="pautas">
										<h2 class=guideline><span class="gGrande"><bean:write name="guideline" property="guidelineId"/></span> <bean:write name="guideline" property="description" filter="false"/></h2>
									</logic:notEmpty>
									<logic:iterate name="guideline" property="pautas" id="pauta">
										<bean:define id="keyDescription">
											<bean:write name="pauta" property="name"/>
										</bean:define>
										<bean:define id="pautaTotal">
											<bean:message key="<%=keyDescription%>"/>
										</bean:define>
										<h3 class="requisito"><span class="grande"><bean:write name="pauta" property="pautaId"/></span> <bean:write name="pauta" property="name" filter="false"/></h3>
										<logic:iterate name="pauta" property="problems" id="problem">
											<bean:define id="keyError">
												<bean:write name="problem" property="error"/>
											</bean:define>
											<logic:equal name="problem" property="type" value="<%=confidenceHigh%>">
												<h4 class="proble"><bean:message key="analyse.problem"/> <bean:message key="<%=keyError%>"/>
											</logic:equal>
											<logic:equal name="problem" property="type" value="<%=confidenceMedium%>">
												<h4 class="advert"><bean:message key="analyse.warning"/> <bean:message key="<%=keyError%>"/>
											</logic:equal>
											<logic:equal name="problem" property="type" value="<%=confidenceCanNotTell%>">
												<h4 class="observ"><bean:message key="analyse.observation"/> <bean:message key="<%=keyError%>"/>
											</logic:equal>
											<logic:notPresent parameter="isCliente">
												<a href="<html:rewrite forward="showAnalysisFromCrawlerDetail"/>&amp;<%=Constants.ID %>=<bean:write name="<%=Constants.ID %>"/>&amp;<%=Constants.CODE %>=<bean:write name="<%=Constants.CODE %>"/>&amp;<%=Constants.ID_RASTREO %>=<bean:write name="<%=Constants.ID_RASTREO %>"/>&amp;<%=Constants.ID_CHECK %>=<bean:write name="problem" property="check"/>">
														<img src="../images/lupa.png" alt="<bean:message key="ver.ejemplos"/>"/>
												</a>
											</logic:notPresent>
											<logic:present parameter="isCliente">
												<a href="<html:rewrite forward="showAnalysisFromCrawlerDetail"/>&amp;<%=Constants.ID %>=<bean:write name="<%=Constants.ID %>"/>&amp;<%=Constants.CODE %>=<bean:write name="<%=Constants.CODE %>"/>&amp;<%=Constants.ID_RASTREO %>=<bean:write name="<%=Constants.ID_RASTREO %>"/>&amp;<%=Constants.ID_CHECK %>=<bean:write name="problem" property="check"/>&amp;isCliente=true">
													<img src="../images/lupa.png" alt="<bean:message key="ver.ejemplos"/>"/>
												</a>
											</logic:present>
											</h4>
										</logic:iterate>
									</logic:iterate>
								</logic:iterate>
							</div>
						</logic:iterate>
					</div>
					
					<jsp:useBean id="paramsVolver" class="java.util.HashMap" />
					<c:set target="${paramsVolver}" property="idrastreo" value="${idrastreo}" />
					<c:set target="${paramsVolver}" property="id" value="${id}" />
					
					<logic:present parameter="isCliente">
						<c:set target="${paramsVolver}" property="isCliente" value="true" />
						<p id="pCenter"><html:link styleClass="btn btn-default btn-lg" forward="showTracking" name="paramsCFC"> <bean:message key="boton.volver"/> </html:link></p>
					</logic:present>
					<logic:notPresent parameter="isCliente">
						<p id="pCenter"><html:link styleClass="btn btn-default btn-lg" forward="showTracking" name="paramsVolver"> <bean:message key="boton.volver"/> </html:link></p>
					</logic:notPresent>
				</div>
			</div>
		</div>
	</div>
</div>