<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>

<bean:define id="rolAdmin"><inteco:properties key="role.administrator.id" file="crawler.properties" /></bean:define>
<bean:define id="rolConfig"><inteco:properties key="role.configurator.id" file="crawler.properties" /></bean:define>
<bean:define id="rolVisor"><inteco:properties key="role.visualizer.id" file="crawler.properties" /></bean:define>
<bean:define id="rolCustResp"><inteco:properties key="role.customer.responsible.id" file="crawler.properties" /></bean:define>
<bean:define id="rolCustUser"><inteco:properties key="role.customer.user.id" file="crawler.properties" /></bean:define>
<html:xhtml/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		 / <bean:message key="migas.rastreo" /></p>
	</div>



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2><bean:message key="indice.rastreo.rastreo" /></h2>
							
							<html:form action="/secure/CargarRastreos.do" method="get" styleClass="formulario">
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="name"><strong class="labelVisu"><bean:message key="rastreo.busqueda.nombre" /></strong></label>
										<html:text styleClass="texto" styleId="name" property="name" />
									</div>
									<div class="formItem">
										<label for="cartridge"><strong class="labelVisu"><bean:message key="rastreo.busqueda.cartucho" /></strong></label>
										<html:select styleClass="textoSelect" styleId="cartridge" property="cartridge" >
											<html:option value=""> - <bean:message key="select.one.masculine"/> - </html:option>
											<logic:iterate id="cartridge" name="<%= Constants.LISTADO_CARTUCHOS %>" type="es.inteco.rastreador2.dao.login.CartuchoForm">
												<bean:define id="idCartridge"><bean:write name="cartridge" property="id"/></bean:define>
												<html:option value="<%=idCartridge %>"><bean:write name="cartridge" property="name"/></html:option>
											</logic:iterate>
										</html:select>
									</div>
									<div class="formItem">
										<label for="active"><strong class="labelVisu"><bean:message key="rastreo.busqueda.activo" /></strong></label>
										<html:select styleClass="textoSelect" styleId="active" property="active" >
											<html:option value=""> - <bean:message key="select.one.masculine"/> - </html:option>
											<html:option value="1"><bean:message key="rastreo.busqueda.activo.si" /></html:option>
											<html:option value="0"><bean:message key="rastreo.busqueda.activo.no" /></html:option>
										</html:select>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
									</div>
								</fieldset>
							</html:form>
							
							<div class="detail">
								<jsp:include page="/common/crawler_messages.jsp" />
								
								<logic:notPresent name="<%=Constants.CARGAR_RASTREOS_FORM %>">	
									<div class="notaInformativaExito">
										<p><bean:message key="indice.rastreo.vacio"/></p>
										<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
											<p id="nTop10">
												<html:link forward="addCrawling" styleClass="boton"><bean:message key="indice.rastreo.nuevo.rastreo.exhaustivo" /></html:link>
												<html:link styleClass="boton" forward="newClientCrawling"><bean:message key="indice.rastreo.nuevo.rastreo.cuenta.cliente" /></html:link>
											</p>
										</inteco:menu>
									</div>
								</logic:notPresent>
								<logic:present name="<%=Constants.CARGAR_RASTREOS_FORM %>">
									<logic:empty name="<%=Constants.CARGAR_RASTREOS_FORM %>" property="vr">	
										<div class="notaInformativaExito">
											<p><bean:message key="indice.rastreo.vacio"/></p>
											<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
											<p id="nTop10">
												<html:link forward="addCrawling" styleClass="boton"><bean:message key="indice.rastreo.nuevo.rastreo.exhaustivo" /></html:link>
												<html:link styleClass="boton" forward="newClientCrawling"><bean:message key="indice.rastreo.nuevo.rastreo.cuenta.cliente" /></html:link>
												<html:link styleClass="boton" forward="testCrawling"><bean:message key="indice.rastreo.test.rastreo" /></html:link>
											</p>
										</inteco:menu>
										</div>
									</logic:empty>
									<logic:notEmpty name="<%=Constants.CARGAR_RASTREOS_FORM %>" property="vr">	
										<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
											<p id="nTop10">
												<html:link forward="addCrawling" styleClass="boton"><bean:message key="indice.rastreo.nuevo.rastreo.exhaustivo" /></html:link>
												<html:link styleClass="boton" forward="newClientCrawling"><bean:message key="indice.rastreo.nuevo.rastreo.cuenta.cliente" /></html:link>
												<html:link styleClass="boton" forward="testCrawling"><bean:message key="indice.rastreo.test.rastreo" /></html:link>
											</p>
										</inteco:menu>
										<div class="pag">
											<table>
												<caption><bean:message key="indice.rastreo.lista.rastreos" /></caption>
											<tr>
												<th><bean:message key="indice.rastreo.nombre" /></th>
												<th><bean:message key="indice.rastreo.cartucho" /></th>
												<th><bean:message key="indice.rastreo.activo" /></th>
												<th><bean:message key="indice.rastreo.fecha" /></th>
												<th><bean:message key="indice.rastreo.estado" /></th>
												<th><bean:message key="indice.rastreo.acciones" /></th>
											</tr>
											<logic:iterate name="CargarRastreosForm" type="es.inteco.rastreador2.utils.Rastreo" property="vr" id="elemento">
												<tr>
													<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
														<logic:empty name="elemento" property="codigoTitle">
															<td>
																<html:link forward="viewCrawling" paramId="<%= Constants.ID_RASTREO %>" paramName="elemento" paramProperty="id_rastreo">
																	<bean:write name="elemento" property="codigo" />
																</html:link>
															</td>
														</logic:empty>
														<logic:notEmpty name="elemento" property="codigoTitle">
															<td title="<bean:write name="elemento" property="codigoTitle" />">
																<html:link forward="viewCrawling" paramId="<%= Constants.ID_RASTREO %>" paramName="elemento" paramProperty="id_rastreo">
																	<bean:write name="elemento" property="codigo" />
																</html:link>
															</td>
														</logic:notEmpty>
													</inteco:menu>
													<inteco:menu roles="<%=rolVisor + \";\" + rolCustResp + \";\" + rolCustUser %>" noRoles="<%=rolConfig + \";\" + rolAdmin %>">
														<td><bean:write name="elemento" property="codigo" /></td>
													</inteco:menu>
													<td><bean:write name="elemento" property="cartucho" /></td>
													<td>
														<logic:equal value="1" name="elemento" property="activo">
															<img src="../images/verde.jpg" alt="<bean:message key="indice.rastreo.img.activo.alt" />"/>
														</logic:equal>
														<logic:equal value="0" name="elemento" property="activo">
															<img src="../images/rojo.jpg" alt="<bean:message key="indice.rastreo.img.inactivo.alt" />"/>
														</logic:equal>
													</td>
													<td><bean:write name="elemento" property="fecha" /></td>
													<td>
														<bean:define id="estadoTexto">
															<bean:write name="elemento" property="estadoTexto" /> 
														</bean:define>
														<bean:message key="<%=estadoTexto%>"/>
													</td>
													<td>
														<ul class="lista_linea">
															<c:if test="${elemento.estado != 2}">
																<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
																	<li>
																		<logic:equal value="1" name="elemento" property="activo">
																			<html:link forward="lanzarWrapCommand" paramId="<%= Constants.ID_RASTREO %>" paramName="elemento" paramProperty="id_rastreo">
																				<img src="../images/bt_lanzar.gif" alt="<bean:message key="indice.rastreo.img.lanzar.rastreo.alt" />"/>
																			</html:link>
																		</logic:equal>
																		<logic:equal value="0" name="elemento" property="activo">
																			<img src="../images/bt_lanzar_escala_grises.gif" alt="<bean:message key="indice.rastreo.img.lanzar.rastreo.inactivo.alt" />"/>
																		</logic:equal>
																	</li>
																</inteco:menu>
															</c:if>
															<c:if test="${elemento.estado == 2}">
																<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
																	<li>
																		<html:link forward="pararWrapCommand" paramId="<%= Constants.ID_RASTREO %>" paramName="elemento" paramProperty="id_rastreo">
																			<img src="../images/bt_parar.gif" alt="<bean:message key="indice.rastreo.img.parar.rastreo.alt" />"/>
																		</html:link>
																	</li>
																</inteco:menu>
															</c:if>	
															<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
																<li><html:link forward="viewCrawling" paramId="<%= Constants.ID_RASTREO %>" paramName="elemento" paramProperty="id_rastreo"><img src="../images/bt_ver.gif" alt="<bean:message key="indice.rastreo.img.visualizar.rastreo.alt" />"/></html:link></li>
															</inteco:menu>
															<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
																<logic:equal value="0" name="elemento" property="id_cuenta">
																	<li><html:link forward="editCrawling" paramId="<%= Constants.ID_RASTREO %>" paramName="elemento" paramProperty="id_rastreo"><img src="../images/bt_modificar.gif" alt="<bean:message key="indice.rastreo.img.editar.rastreo.alt" />"/></html:link></li>
																</logic:equal>
																<logic:notEqual value="0" name="elemento" property="id_cuenta">
																	<li><img src="../images/bt_modificar_grises.gif" alt="<bean:message key="indice.rastreo.img.editar.rastreo.inactivo.alt" />"/></li>
																</logic:notEqual>
															</inteco:menu>
															<inteco:menu roles="<%=rolConfig + \";\" + rolAdmin %>">
																<logic:equal value="0" name="elemento" property="id_cuenta">
																	<li><html:link forward="deleteCrawling" paramId="<%= Constants.ID_RASTREO %>" paramName="elemento" paramProperty="id_rastreo"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.rastreo.img.eliminar.rastreo.alt" />"/></html:link></li>
																</logic:equal>
																<logic:notEqual value="0" name="elemento" property="id_cuenta">
																	<li><img src="../images/bt_eliminar_escala_grises.gif" alt="<bean:message key="indice.rastreo.img.eliminar.rastreo.inactivo.alt" />"/></li>
																</logic:notEqual>
															</inteco:menu>
															<li><html:link forward="verRastreosRealizados" paramId="<%=Constants.ID_RASTREO %>" paramName="elemento" paramProperty="id_rastreo"><img src="../images/list.gif" alt="<bean:message key="indice.rastreo.img.resultados.realizados.alt" />" /></html:link></li>
														</ul>
													</td>
												</tr>
											</logic:iterate>
											</table>
											<jsp:include page="pagination.jsp" />
										</div>
										<p id="pCenter"><html:link forward="indexAdmin" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
									</logic:notEmpty>
								</logic:present>
							</div>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 