<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<inteco:sesion action="ifConfigAdmin">
<html:xhtml/>
<html:javascript formName="TestRastreoForm"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			 / <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> 
			 / <html:link forward="testCrawling"><bean:message key="migas.test.rastreo" /></html:link>
			 / <bean:message key="migas.test.rastreo.resultados" />
		 </p>
	</div>



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="test.rastreo.results.title" /></h2>
							
							<p><bean:message key="test.rastreo.results.info"/></p>
							
							<p>
								<bean:size id="numResults" name="<%=Constants.RASTREO_TEST_RESULTS %>"/>
								<bean:message key="test.rastreo.results.number">
									<jsp:attribute name="arg0">
										<bean:write name="numResults"/>
									</jsp:attribute>
								</bean:message>
							</p>
							
							<ul class="lista_inicial">
								<logic:iterate name="<%=Constants.RASTREO_TEST_RESULTS %>" id="testResult">
									<li><bean:write name="testResult" property="url"/></li>
								</logic:iterate>
							</ul>
							
							<p id="pCenter"><html:link forward="testCrawling" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
						</div>
					</div>

			</div>
		</div>	
	</div> 

</inteco:sesion>