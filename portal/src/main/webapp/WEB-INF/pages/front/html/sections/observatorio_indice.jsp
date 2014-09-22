<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<bean:define id="rolObservatory"><inteco:properties key="role.observatory.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolAdmin"><inteco:properties key="role.administrator.id" file="crawler.properties" /></bean:define>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / <bean:message key="migas.observatorio" /></p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">

				<h1 class="cart"> <bean:message key="indice.observatorio.gestion.observatorio" /> </h1>
				
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="indice.observatorio.observatorio" /></h2>
							<jsp:include page="/common/crawler_messages.jsp" />
							<logic:notPresent name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>">	
								<div class="notaInformativaExito">
									<p><bean:message key="indice.observatorio.vacio"/></p>
									<p><html:link forward="newObservatory" styleClass="boton"><bean:message key="indice.observatorio.nuevo.observatorio" /></html:link>
									<html:link forward="indexAdmin" styleClass="boton"><bean:message key="boton.volver" /></html:link></p>
								</div>
							</logic:notPresent>
							<logic:present name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>">
								<logic:empty name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>" property="listadoObservatorio">	
									<div class="notaInformativaExito">
										<p><bean:message key="indice.observatorio.vacio"/></p>
										<p><html:link forward="newObservatory" styleClass="boton"><bean:message key="indice.observatorio.nuevo.observatorio" /></html:link>
										<html:link forward="indexAdmin" styleClass="boton"><bean:message key="boton.volver" /></html:link></p>
									</div>
								</logic:empty>
								
								<logic:notEmpty name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>" property="listadoObservatorio">
									<p><html:link forward="newObservatory" styleClass="boton"><bean:message key="indice.observatorio.nuevo.observatorio" /></html:link>	
									<div class="pag">
										<table>
											<caption><bean:message key="indice.observatorio.lista" /></caption>
											<tr>
												<th><bean:message key="indice.observatorio.nombre" /></th>
												<th><bean:message key="indice.observatorio.cartucho" /></th>
												<th><bean:message key="indice.observatorio.acciones" /></th>
											</tr>
											<logic:iterate name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>" property="listadoObservatorio" id="elemento">
												<tr>
													<bean:define id="detailTitle"><bean:message key="indice.observatorio.detalle.alt" /></bean:define>
													<td ><html:link forward="verObservatorio" paramId="id_observatorio" paramName="elemento" paramProperty="id_observatorio" title="<%= detailTitle %>"><bean:write name="elemento" property="nombreObservatorio" /></html:link></td>
													<td><bean:write name="elemento" property="cartucho"/></td>
													<td>
														<ul class="lista_linea">
															<li><html:link forward="verObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="elemento" paramProperty="id_observatorio"><img src="../images/bt_ver.gif" alt="<bean:message key="indice.observatorio.ver.alt" />"/></html:link></li>
															<li>
																<jsp:useBean id="params" class="java.util.HashMap" />
																<bean:define id="actionMod" value="<%= Constants.ACCION_MODIFICAR %>" />
																<bean:define id="action" value="<%= Constants.ACTION %>" />
																<bean:define id="observatoryId" name="elemento" property="id_observatorio" />
																<bean:define id="observatorySTR" value="<%= Constants.OBSERVATORY_ID %>" />
																<c:set target="${params}" property="${observatorySTR}" value="${observatoryId}" />
																<c:set target="${params}" property="${action}" value="${actionMod}"/>
																<inteco:menu roles="<%=rolAdmin%>">
																	<html:link forward="editObservatory" name="params"><img src="../images/bt_modificar.gif" alt="<bean:message key="indice.observatorio.modificar.alt"/>"/></html:link>
																</inteco:menu>
																<inteco:menu roles="<%=rolObservatory%>">
																	<img src="../images/bt_modificar_grises.gif" alt="<bean:message key="indice.observatorio.modificar.alt"/>"/>
																</inteco:menu>
															</li>
															<li><html:link forward="resultadosPrimariosObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="elemento" paramProperty="id_observatorio"><img src="../images/list.gif" alt="<bean:message key="indice.observatorio.resultados.alt"/>"/></html:link></li>
															<li><html:link forward="getFulfilledObservatories" paramId="<%=Constants.OBSERVATORY_ID %>" paramName="observatoryId"><img src="../images/anonimo.gif" alt="<bean:message key="indice.observatorio.resultados.anonimos.alt" />"/></html:link></li>
															<li><html:link forward="databaseExportActionConfirm" paramId="<%=Constants.OBSERVATORY_ID %>" paramName="observatoryId" ><img src="../images/database.jpg" alt="<bean:message key="indice.rastreo.exportar.database" />"/></html:link></li>
															<jsp:useBean id="paramsEsPrim" class="java.util.HashMap" />
															<bean:define id="actionEsPrim" value="<%= Constants.ES_PRIMERA %>" />
															<bean:define id="observatoryId" name="elemento" property="id_observatorio" />
															<bean:define id="observatorySTR" value="<%= Constants.OBSERVATORY_ID %>" />
															<c:set target="${paramsEsPrim}" property="${observatorySTR}" value="${observatoryId}" />
															<c:set target="${paramsEsPrim}" property="${actionEsPrim}" value="si"/>
															<li><html:link forward="deleteObservatory" name="paramsEsPrim"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.observatorio.eliminar.alt" />"/></html:link></li>
														</ul>
													</td>
												</tr> 
											</logic:iterate> 
										</table>
										
										<jsp:include page="pagination.jsp" />
									</div>
									<p id="pCenter"><html:link forward="indexAdmin" styleClass="boton"> <bean:message key="boton.volver"/> </html:link></p>
								</logic:notEmpty>
							</logic:present>
						</div><!-- fin cajaformularios -->
					</div><!-- Container Derecha -->
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> <!-- fin CONTENEDOR GRAL. -->
