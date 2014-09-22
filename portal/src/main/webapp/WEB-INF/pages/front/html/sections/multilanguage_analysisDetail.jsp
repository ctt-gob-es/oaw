<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.sun.corba.se.impl.orbutil.closure.Constant"%>
<html:xhtml/>

	<logic:present parameter="<%= Constants.ID_RASTREO %>">
		<bean:parameter id="idrastreo" name="<%= Constants.ID_RASTREO %>"/>
	</logic:present>
	<logic:present parameter="<%= Constants.CODE %>">
		<bean:parameter id="code" name="<%= Constants.CODE %>"/>
	</logic:present>
	<logic:present parameter="<%= Constants.ID %>">
		<bean:parameter id="id" name="<%= Constants.ID %>"/>
	</logic:present>
	<logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
		<bean:parameter id="id_observatorio" name="<%= Constants.ID_OBSERVATORIO %>"/>
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
		 		/ <html:link forward="multilanguageListAnalysis" name="params"><bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
			</logic:present>
			<logic:notPresent parameter="isCliente"> 
				/ <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link>
				/ <html:link forward="loadFulfilledCrawlings" paramId="<%= Constants.ID_RASTREO %>" paramName="<%= Constants.ID_RASTREO %>"><bean:message key="migas.rastreos.realizados" /></html:link>
				/ <html:link forward="multilanguageListAnalysis" name="params"><bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link>
			 </logic:notPresent>
		 </logic:notPresent>
		 <logic:present parameter="<%= Constants.ID_OBSERVATORIO %>">
		 	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
		 	/ <html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="resultadosObservatorioSemillas" name="params"><bean:message key="migas.resultado.observatorio" /></html:link> /
			<html:link forward="resultadosObservatorioSemillasLista" name="params"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link> /
			<html:link forward="multilanguageListAnalysis" name="params"><bean:message key="migas.rastreos.realizados.url.analizadas" /></html:link> /
		 </logic:present>
		  / <bean:message key="migas.rastreos.realizados.detalle" /></p>
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
							<h2 class="config"><bean:message key="multilanguage.detail.title"/></h2>
							<logic:notPresent name="<%=Constants.ANALYSIS %>">	
								<div class="notaInformativaExito">
									<p><bean:message key="indice.rastreo.vacio"/></p>
								</div>
							</logic:notPresent>
							<logic:present name="<%=Constants.ANALYSIS %>">
								<p>
									<strong><bean:message key="multilanguage.detail.url"/>: </strong>
									<bean:write name="<%=Constants.ANALYSIS %>" property="url"/>
								</p>
								<p>
									<strong><bean:message key="multilanguage.detail.date"/>: </strong>
									<bean:write name="<%=Constants.ANALYSIS %>" property="date"/>
								</p>
								<logic:notEmpty name="<%=Constants.ANALYSIS %>" property="languagesFound">
									<table>
										<caption><bean:message key="multilanguage.detail.caption"/></caption>
										<tr>
											<th><bean:message key="multilanguage.detail.language"/></th>
											<th><bean:message key="multilanguage.detail.location"/></th>
											<th><bean:message key="multilanguage.detail.declaration.language"/></th>
											<th><bean:message key="multilanguage.detail.translation.language"/></th>
											<th><bean:message key="multilanguage.detail.correct"/></th>
											<th><bean:message key="multilanguage.detail.page.codice"/></th>
										</tr>
										<logic:iterate name="<%=Constants.ANALYSIS %>" property="languagesFound" id="languageFound">
											<tr>
												<td class="alignLeft">
													<logic:notEmpty name="languageFound" property="language">
														<bean:write name="languageFound" property="language.name"/>
													</logic:notEmpty>
												</td>
												<bean:define id="altLink">
													<bean:message key="categoria.semillas.enlace.externo">
														<jsp:attribute name="arg0">
															<logic:notEmpty name="languageFound" property="hrefTitle">
																<bean:write name="languageFound" property="hrefTitle"/>
															</logic:notEmpty>
															<logic:empty name="languageFound" property="hrefTitle">
																<bean:write name="languageFound" property="href"/>
															</logic:empty>
														</jsp:attribute>
													</bean:message>
												</bean:define>
												<logic:empty name="languageFound" property="hrefTitle">
													<td class="alignLeft">
														<bean:write name="languageFound" property="href"/>
														<a href="<bean:write name="languageFound" property="href"/>">
															<img src="../images/external_link.gif" alt="<%=altLink %>"/>
														</a>
													</td>
												</logic:empty>
												<logic:notEmpty name="languageFound" property="hrefTitle">
													<td class="alignLeft" title ="<bean:write name="languageFound" property="hrefTitle" />">
														<bean:write name="languageFound" property="href"/>
														<a href="<bean:write name="languageFound" property="hrefTitle"/>">
															<img src="../images/external_link.gif" alt="<%=altLink %>"/>
														</a>
													</td>
												</logic:notEmpty>
												<td class="alignLeft">
													<logic:notEmpty name="languageFound" property="declarationLang">
														<bean:write name="languageFound" property="declarationLang"/>
													</logic:notEmpty>
													<logic:empty name="languageFound" property="languageSuspected">
														<bean:message key="multilanguage.detail.not.declaration"/>
													</logic:empty>
												</td>
												<td class="alignLeft">
													<logic:notEmpty name="languageFound" property="languageSuspected">
														<bean:write name="languageFound" property="languageSuspected.name"/>
													</logic:notEmpty>
													<logic:empty name="languageFound" property="languageSuspected">
														<bean:message key="multilanguage.detail.not.found"/>
													</logic:empty>
												</td>
												<td>
													<logic:equal name="languageFound" property="correct" value="true">
														<html:img src="../images/modalidadVerde.png" altKey=""/>
													</logic:equal>
													<logic:equal name="languageFound" property="correct" value="false">
														<html:img src="../images/modalidadRojo.png" altKey=""/>
													</logic:equal>
												</td>
												<td>
													<c:set target="${params}" property="code" value="${code}" />
													<c:set target="${params}" property="idLang" value="${languageFound.id}" />
													<html:link forward="getMultilanguageHtmlSource" name="params"><img src="../images/html_icon.png" alt="<bean:message key="indice.rastreo.ver.codigo.analizado" />"/></html:link>
												</td>
											</tr>
										</logic:iterate>
									</table>
								</logic:notEmpty>
							</logic:present>
							
							<logic:present name="<%= Constants.MULTILANGUAJE_NOT_FOUND_LANG %>">
								<logic:notEmpty name="<%= Constants.MULTILANGUAJE_NOT_FOUND_LANG %>">
									<div>
										<strong><bean:message key="multilanguage.detail.notFound.languages"/></strong> : 
										<ul class="lista_languages">
											<logic:iterate name="<%= Constants.MULTILANGUAJE_NOT_FOUND_LANG %>" id="notFoundlanguage">
												<li><bean:write name="notFoundlanguage" property="name" /></li>
											</logic:iterate>
										</ul>
									</div>
								</logic:notEmpty>
							</logic:present>
							
							<p id="pCenter">	
								<html:link styleClass="boton" forward="multilanguageListAnalysis" name="params"><bean:message key="boton.volver"/></html:link>
							</p>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>	
	</div> 
