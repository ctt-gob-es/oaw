<%@ include file="/common/taglibs.jsp" %>

<bean:define id="confidenceHigh"><inteco:properties key="confidence.level.high" file="<%=  Constants.INTAV_PROPERTIES%>" /></bean:define>
<bean:define id="confidenceMedium"><inteco:properties key="confidence.level.medium" file="<%=  Constants.INTAV_PROPERTIES%>" /></bean:define>
<bean:define id="confidenceCanNotTell"><inteco:properties key="confidence.level.cannottell" file="<%=  Constants.INTAV_PROPERTIES%>" /></bean:define>

<bean:parameter id="idrastreo" name="idrastreo"/>
<bean:parameter name="id" id="id"/>

<!-- Migas -->
<%@page import="es.inteco.common.Constants"%>
<%@page import="es.inteco.intav.properties.PropertiesManager"%>
<div id="migas">
	<jsp:useBean id="paramsCFC" class="java.util.HashMap" />
	<c:set target="${paramsCFC}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${paramsCFC}" property="id" value="${id}" />
	<c:set target="${paramsCFC}" property="code" value="${code}" />
	<c:set target="${paramsCFC}" property="isCliente" value="true" />
	
	<bean:parameter name="code" id="code"/>
	<jsp:useBean id="paramsVolver" class="java.util.HashMap" />
	<c:set target="${paramsVolver}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${paramsVolver}" property="id" value="${id}" />
	<c:set target="${paramsVolver}" property="code" value="${code}" />
	
	<p class="oculto"><bean:message key="ubicacion.migas"/></p>
	<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> /
	<logic:present parameter="isCliente">
		<c:set target="${paramsVolver}" property="isCliente" value="true" />
		<html:link forward="loadClientCrawlings"><bean:message key="migas.rastreos.cliente" /></html:link>
 		/ <html:link forward="loadClientFulfilledCrawlings" name="paramsCFC"><bean:message key="migas.rastreos.realizados" /></html:link>
 		/ <html:link forward="showTracking" name="paramsCFC"> <bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
		/ <html:link forward="showAnalysisFromCrawlerRecover" name="paramsVolver"><bean:message key="migas.rastreos.realizados.resultados" /></html:link>
	</logic:present>
	<logic:notPresent parameter="isCliente"> 
		<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link>
	 	/ <html:link forward="verRastreosRealizados" name="paramsVolver"><bean:message key="migas.rastreos.realizados" /></html:link>
	 	/ <html:link forward="showTracking" name="paramsVolver"><bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
		/ <html:link forward="showAnalysisFromCrawlerRecover" name="paramsVolver"><bean:message key="migas.rastreos.realizados.resultados" /></html:link>
	 </logic:notPresent>
 	/ <bean:message key="migas.rastreos.realizados.detalle" /></p> 
</div>

<!-- Cuerpo -->
<div id="cuerpo">
	<div id="contenido">
		<div id="main">
			
			<bean:define id="keyError">
				<bean:write name="problem" property="error"/>
			</bean:define>
			<logic:equal name="problem" property="type" value="<%=confidenceHigh%>">
				<h3 class="proble"><bean:message key="analyse.problem"/> <bean:message key="<%=keyError%>"/> </h3> 
			</logic:equal>
			<logic:equal name="problem" property="type" value="<%=confidenceMedium%>">
				<h3 class="advert"><bean:message key="analyse.warning"/> <bean:message key="<%=keyError%>"/></h3>
			</logic:equal>
			<logic:equal name="problem" property="type" value="<%=confidenceCanNotTell%>">
				<h3 class="observ"><bean:message key="analyse.observation"/> <bean:message key="<%=keyError%>"/></h3>
			</logic:equal>
			<logic:notEmpty name="problem" property="rationale">
				<bean:define id="keyRationale">
					<bean:write name="problem" property="rationale"/>
				</bean:define>
				<p class="toggler"><bean:message key="analyse.explanation"/></p> <div class="rationale"><bean:message key="<%=keyRationale%>"/></div>
			</logic:notEmpty>
							 
			<logic:notEmpty name="problem" property="specificProblems">
				
				<bean:define id="pagination"><inteco:properties key="pagination.check.results" file="intav.properties" /></bean:define>
				<bean:define id="initialPag" value="0"/>
				
				<logic:present name="<%= Constants.RESULTS_PAGINATION_INITIAL_VALUE %>">
					<bean:define id="initialPag" name="<%= Constants.RESULTS_PAGINATION_INITIAL_VALUE %>" type="java.lang.String"/>
				</logic:present>

				<ul class="resultados">
					<logic:iterate name="problem" property="specificProblems" id="specificProblem" length="<%= pagination %>" offset="<%= initialPag %>">
						<li>
							<logic:notEmpty name="specificProblem" property="code">
								<p><bean:message key="problem.line"/> <bean:write name="specificProblem" property="line"/>; <bean:message key="problem.column"/> <bean:write name="specificProblem" property="column"/></p>
								<p><bean:message key="problem.codice"/> </p>
								<div class="codigo">
									<logic:notEmpty name="specificProblem" property="message">
										<p><strong><bean:write name="specificProblem" property="message"/></strong></p>
									</logic:notEmpty>
									<logic:iterate id="cod" name="specificProblem" property="code">
										<p><bean:write name="cod"/></p>
									</logic:iterate>
								</div>
							</logic:notEmpty>
							<logic:notEmpty name="specificProblem" property="note">
								<div class="nota">
									<logic:iterate id="note" name="specificProblem" property="note">
										<p><bean:write name="note" filter="false"/></p>
									</logic:iterate>
								</div>
							</logic:notEmpty>
						</li>
					</logic:iterate>
				</ul>
				
				<logic:notEmpty name="problem" property="note">
					<bean:define id="keyNote">
						<bean:write name="problem" property="note"/>
					</bean:define>
					<div class="nota">
						<p><bean:message key="<%=keyNote %>"/></p>
					</div>
					<div class="spacer"></div>
				</logic:notEmpty>
				
				<div id="pCenter">
					<jsp:include page="pagination.jsp" />
				</div>
			</logic:notEmpty>
			<logic:notPresent parameter="isCliente"> 
				<p id="pCenter">
					<html:link styleClass="boton" forward="showAnalysisFromCrawlerRecover" name="paramsVolver"><bean:message key="boton.volver"/> </html:link>
				</p>
			</logic:notPresent>
			<logic:present parameter="isCliente"> 
				<p id="pCenter">
					<html:link styleClass="boton" forward="showAnalysisFromCrawlerRecover" name="paramsVolver"><bean:message key="boton.volver"/> </html:link>
				</p>
			</logic:present>
		</div>
	</div>
</div>