<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="SemillaObservatorioForm"/>
	<div id="migas">
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link>
			/ <html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link>
			/ <bean:message key="migas.semillas.observatorio" /> 
		</p>
	</div>
	
	<jsp:useBean id="paramsNS" class="java.util.HashMap" />
	<c:set target="${paramsNS}" property="action" value="anadir" />
	<c:set target="${paramsNS}" property="esPrimera" value="si" />
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<div id="cuerpoprincipal">
				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="gestion.semillas.observatorio.titulo"/></h2>
							
							<html:form action="/secure/ViewSemillasObservatorio.do" method="get" styleClass="formulario">
								<input type="hidden" name="<%= Constants.ACTION %>" value="<%= Constants.LOAD %>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="nombre"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.nombre" /></strong></label>
										<html:text styleClass="texto" styleId="nombre" property="nombre" />
									</div>
									<div class="formItem">
										<label for="categoria"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.categoria" /></strong></label>
										<html:select styleClass="textoSelect" styleId="categoria" property="categoria" >
											<html:option value=""><bean:message key="resultados.observatorio.cualquier.categoria" /></html:option>
											<logic:iterate name="<%= Constants.CATEGORIES_LIST %>" id="categoria">
												<bean:define id="idCategoria"><bean:write name="categoria" property="id"/></bean:define>
												<html:option value="<%=idCategoria %>"><bean:write name="categoria" property="name"/></html:option>
											</logic:iterate>
										</html:select>
									</div>
									<div class="formItem">
										<label for="url"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.url" /></strong></label>
										<html:text styleClass="texto" styleId="url" property="url" />
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
									</div>
								</fieldset>
							</html:form>
							
							<div class="detail">
								<logic:notPresent name="<%= Constants.OBSERVATORY_SEED_LIST %>">
									<div class="notaInformativaExito">
										<p><bean:message key="semilla.observatorio.vacia"/></p>
										<p><html:link forward="observatorySeeds" name="paramsNS" styleClass="boton"><bean:message key="cargar.semilla.observatorio.nueva.semilla" /></html:link>
											<html:link styleClass="boton" forward="indexAdmin"><bean:message key="boton.volver"/></html:link></p>
								</div>
									</div>
								</logic:notPresent>
								<logic:present name="<%= Constants.OBSERVATORY_SEED_LIST %>">
									<logic:empty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
										<div class="notaInformativaExito">
											<p><bean:message key="semilla.observatorio.vacia"/></p>
											<p><html:link forward="observatorySeeds" name="paramsNS" styleClass="boton"><bean:message key="cargar.semilla.observatorio.nueva.semilla" /></html:link>
											   <html:link styleClass="boton" forward="indexAdmin"><bean:message key="boton.volver"/></html:link></p>
										</div>
									</logic:empty>
									<logic:notEmpty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
										<p><html:link forward="observatorySeeds" name="paramsNS" styleClass="boton"><bean:message key="cargar.semilla.observatorio.nueva.semilla" /></html:link></p>
										<div class="pag">
											<table>
												<caption><bean:message key="lista.semillas.observatorio"/></caption>
												<tr>
													<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
													<th><bean:message key="cargar.semilla.observatorio.categoria" /></th>
													<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
													<th><bean:message key="cargar.semilla.observatorio.operaciones" /></th>
												</tr>
												<logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>" id="semilla">
													<bean:define id="action"><%= Constants.ACTION %></bean:define>
													<bean:define id="semillaId" name="semilla" property="id" />
													<bean:define id="semillaSTR"><%= Constants.SEMILLA %></bean:define>
													<tr>
														<td>
															<jsp:useBean id="paramsV" class="java.util.HashMap" />
															<bean:define id="actionDet"><%= Constants.ACCION_SEED_DETAIL %></bean:define>
															<c:set target="${paramsV}" property="${semillaSTR}" value="${semillaId}" />
															<c:set target="${paramsV}" property="${action}" value="${actionDet}" />
															<bean:define id="detailTitle"><bean:message key="eliminar.semilla.detalle.semilla.observatorio" /></bean:define>
															<html:link forward="observatorySeeds" name="paramsV" title="<%= detailTitle%>"><bean:write name="semilla" property="nombre" /></html:link>
														</td>
														<td>
															<bean:write name="semilla" property="categoria.name"/>
														</td>
														<td>
															<logic:equal name="semilla" property="activa" value="true">
																<bean:message key="si"/>
															</logic:equal>
															<logic:equal name="semilla" property="activa" value="false">
																<bean:message key="no"/>
															</logic:equal>
														</td>
														<td>
															<ul class="lista_linea">
																<li>
																	<jsp:useBean id="params" class="java.util.HashMap" />
																	<bean:define id="actionMod"><%= Constants.ACCION_MODIFICAR %></bean:define>
																	<c:set target="${params}" property="${semillaSTR}" value="${semillaId}" />
																	<c:set target="${params}" property="${action}" value="${actionMod}"/>
																	<html:link forward="observatorySeeds" name="params"><img src="../images/bt_modificar.gif" alt="<bean:message key="etitar.semilla.observatorio" />"/></html:link>
																</li>
																
																<logic:equal value="false" name="semilla" property="asociada">
																	<li>
																		<jsp:useBean id="paramsD" class="java.util.HashMap" />
																		<bean:define id="actionDel"><%= Constants.ACCION_CONFIRMACION_BORRAR %></bean:define>
																		<c:set target="${paramsD}" property="${semillaSTR}" value="${semillaId}" />
																		<c:set target="${paramsD}" property="${action}" value="${actionDel}"/>
																		<html:link forward="observatorySeeds" name="paramsD"><img src="../images/bt_eliminar.gif" alt="<bean:message key="eliminar.semilla.observatorio" />"/></html:link>
																	</li>
																</logic:equal>
																<logic:equal value="true" name="semilla" property="asociada">
																	<li><img src="../images/bt_eliminar_escala_grises.gif" alt="<bean:message key="eliminar.semilla.observatorio.desactivado" />"/></li>
																</logic:equal>
															</ul>
														</td>
													</tr>
												</logic:iterate>
											</table>
											<jsp:include page="pagination.jsp" />
											</div>
										</logic:notEmpty>
								</logic:present>
							</div>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 