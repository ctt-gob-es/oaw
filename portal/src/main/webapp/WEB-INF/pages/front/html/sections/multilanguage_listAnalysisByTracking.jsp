<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>

	<logic:present parameter="<%= Constants.ID_RASTREO %>">
		<bean:parameter id="idrastreo" name="<%= Constants.ID_RASTREO %>"/>
	</logic:present>
	<logic:present parameter="<%= Constants.ID %>">
		<bean:parameter id="id" name="<%= Constants.ID %>"/>
	</logic:present>
	<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
		<bean:parameter id="id_observatorio" name="<%= Constants.ID_OBSERVATORIO %>"/>
	</logic:present>
	<logic:present parameter="<%= Constants.ID_EX_OBS %>">
		<bean:parameter id="idExObs" name="<%= Constants.ID_EX_OBS %>"/>
	</logic:present>

	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="idrastreo" value="${idrastreo}" />
	<c:set target="${params}" property="id" value="${id}" />

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		<logic:notPresent parameter="<%= Constants.ID_OBSERVATORIO %>">
			<logic:present parameter="isCliente">
				/ <html:link forward="loadClientCrawlings"><bean:message key="migas.rastreos.cliente" /></html:link>
		 		/ <html:link forward="loadClientFulfilledCrawlings" name="paramsVolverFC"><bean:message key="migas.rastreos.realizados" /></html:link>
			</logic:present>
			<logic:notPresent parameter="isCliente"> 
				/ <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link>
				/ <html:link forward="loadFulfilledCrawlings" paramId="<%= Constants.ID_RASTREO %>" paramName="<%= Constants.ID_RASTREO %>"><bean:message key="migas.rastreos.realizados" /></html:link>
			 </logic:notPresent>
		 </logic:notPresent>
		 
	 	<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
	 		<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
		 	/ <html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		 	<c:set target="${params}" property="idCartucho" value="5" />
	 		<c:set target="${params}" property="idExObs" value="${idExObs}" />
		 	<html:link forward="resultadosPrimariosObservatorio" name="params"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link> /
			<html:link forward="resultadosObservatorioSemillas" name="params"><bean:message key="migas.resultado.observatorio" /></html:link> 
			
	 	</logic:present>
	 	/ <bean:message key="migas.rastreos.realizados.url.analizadas" /> </p>
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
							<h2 class="config"><bean:message key="search.results.by.tracking"/></h2>
							<logic:notPresent name="<%=Constants.LIST_ANALYSIS %>">	
								<div class="notaInformativaExito">
									<p><bean:message key="indice.rastreo.vacio"/></p>
								</div>
							</logic:notPresent>
							<logic:present name="<%=Constants.LIST_ANALYSIS %>">
								<logic:empty name="<%=Constants.LIST_ANALYSIS %>">	
									<div class="notaInformativaExito">
										<p><bean:message key="indice.rastreo.vacio"/></p>
									</div>
								</logic:empty>
								<logic:notEmpty name="<%=Constants.LIST_ANALYSIS %>">
								
									<div id="observatoryInfo">
										<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
										<html:link forward="multilanguagePrimaryExportPdfAction" name="params" ><img src="../images/icono_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.pdf" />"/></html:link>
										<bean:define id="regeneratePDF"><%= Constants.EXPORT_PDF_REGENERATE %></bean:define>
										<c:set target="${params}" property="${regeneratePDF}" value="true" />
										<html:link forward="multilanguagePrimaryExportPdfAction" name="params" ><img src="../images/icono_regenerar_pdf.gif" alt="<bean:message key="indice.rastreo.exportar.pdf.regenerate" />"/></html:link>
										<div class="spacer"></div>
									</div>
									
									<div class="pag">
										<table>
											<thead>
												<tr>
													<th><bean:message key="search.results.domain"/></th>
													<th><bean:message key="search.results.date"/></th>
													<th><bean:message key="search.results.operations"/></th>
												</tr>
											</thead>
											<tbody>
												<logic:iterate id="analysis" name="<%=Constants.LIST_ANALYSIS %>">
													<c:set target="${params}" property="code" value="${analysis.id}" />
													<tr>
														<logic:empty name="analysis" property="urlTitle">
															<td><bean:write name="analysis" property="url"/></td>
														</logic:empty>
														<logic:notEmpty name="analysis" property="urlTitle">
															<td title ="<bean:write name="analysis" property="urlTitle" />"><bean:write name="analysis" property="url"/></td>
														</logic:notEmpty>
														<td><bean:write name="analysis" property="date"/></td>
														<td>
															<html:link forward="multilanguageGetAnalysis" name="params"><img src="../images/multilanguage.png" alt="<bean:message key="" />" /></html:link>
														</td>
													</tr>
												</logic:iterate>
											</tbody>
										</table>
										
										<jsp:include page="pagination.jsp" />
									</div>
								</logic:notEmpty>
							</logic:present>
							<p id="pCenter">	
								<logic:notPresent parameter="<%= Constants.ID_OBSERVATORIO %>">
									<html:link styleClass="boton" forward="loadFulfilledCrawlings" paramId="<%= Constants.ID_RASTREO %>" paramName="<%= Constants.ID_RASTREO %>"><bean:message key="boton.volver"/></html:link>
								</logic:notPresent>
								<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
									<html:link styleClass="boton" forward="resultadosObservatorioSemillas" name="params"><bean:message key="boton.volver"/></html:link>
								</logic:present>
							</p>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
