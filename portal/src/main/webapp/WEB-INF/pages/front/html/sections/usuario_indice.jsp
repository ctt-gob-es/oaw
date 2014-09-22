<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>

<bean:define id="idSystemRole">
	<inteco:properties key="role.type.system" file="crawler.properties" />
</bean:define>
<bean:define id="idClientRole">
	<inteco:properties key="role.type.client" file="crawler.properties" />
</bean:define>
<bean:define id="idObservatoryRole">
	<inteco:properties key="role.type.observatory" file="crawler.properties" />
</bean:define>

<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / <bean:message key="migas.usuarios" /></p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="usu"><bean:message key="indice.usuarios.sistema.gestion.usuarios" /> </h1>
				<div id="cuerpoprincipal">		
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="indice.usuarios.sistema.usuarios" /></h2>
							
							<logic:notPresent name="<%=Constants.CARGAR_USUARIOS_SISTEMA_FORM %>">	
								<div class="notaInformativaExito">
									<p><bean:message key="indice.usuarios.sistema.vacio"/></p>
									<p>
										<html:link forward="newSystemUser" styleClass="boton" paramId="<%=Constants.ROLE_TYPE %>" paramName="idSystemRole"><bean:message key="indice.usuarios.sistema.nuevo.usuario" /></html:link>
										<html:link forward="newClientUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idClientRole" styleClass="boton"><bean:message key="indice.usuarios.sistema.nuevo.usuario.cliente" /></html:link>
										<html:link forward="newObservatoryUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idObservatoryRole" styleClass="boton"><bean:message key="indice.usuarios.sistema.nuevo.usuario.observatorio" /></html:link>
									</p>
								</div>
							</logic:notPresent>
							<logic:present name="<%=Constants.CARGAR_USUARIOS_SISTEMA_FORM %>">
								<logic:empty name="<%=Constants.CARGAR_USUARIOS_SISTEMA_FORM %>" property="listadoUsuarios">	
									<div class="notaInformativaExito">
										<p><bean:message key="indice.usuarios.sistema.vacio"/></p>
										<p>
											<html:link forward="newSystemUser" styleClass="boton" paramId="<%=Constants.ROLE_TYPE %>" paramName="idSystemRole"><bean:message key="indice.usuarios.sistema.nuevo.usuario" /></html:link>
											<html:link forward="newClientUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idClientRole" styleClass="boton"><bean:message key="indice.usuarios.sistema.nuevo.usuario.cliente" /></html:link>
											<html:link forward="newObservatoryUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idObservatoryRole" styleClass="boton"><bean:message key="indice.usuarios.sistema.nuevo.usuario.observatorio" /></html:link>
										</p>
									</div>
								</logic:empty>
								<logic:notEmpty name="<%=Constants.CARGAR_USUARIOS_SISTEMA_FORM %>" property="listadoUsuarios">	
									<p>
										<html:link forward="newSystemUser" styleClass="boton"  paramId="<%=Constants.ROLE_TYPE %>" paramName="idSystemRole" titleKey="indice.usuarios.sistema.nuevo.usuario.alt"><bean:message key="indice.usuarios.sistema.nuevo.usuario" /></html:link>
										<html:link forward="newClientUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idClientRole" styleClass="boton"><bean:message key="indice.usuarios.sistema.nuevo.usuario.cliente" /></html:link>
										<html:link forward="newObservatoryUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idObservatoryRole" styleClass="boton"><bean:message key="indice.usuarios.sistema.nuevo.usuario.observatorio" /></html:link>
									</p>
									<div class="pag">
										<table>
											<caption><bean:message key="indice.usuarios.sistema.lista.usuarios" /></caption>
											<tr>
												<th><bean:message key="indice.usuarios.sistema.usuario" /></th>
												<th><bean:message key="indice.usuarios.sistema.cartucho" /></th>
												<th><bean:message key="indice.usuarios.sistema.tipo" /></th>
												<th><bean:message key="indice.usuarios.sistema.acciones" /></th>
											</tr>
											<logic:iterate name="CargarUsuariosSistemaForm" type="es.inteco.rastreador2.utils.ListadoUsuario" property="listadoUsuarios" id="elemento">
												<tr>
													<td ><html:link forward="viewUser" paramId="<%= Constants.USER %>" paramName="elemento" paramProperty="id_usuario"><bean:write name="elemento" property="usuario" /></html:link></td>
													<td>
														<logic:notEmpty name="elemento" property="cartucho">
															<ul>
																<logic:iterate id="cartuchoL" name="elemento" property="cartucho">
																	<li><bean:write name="cartuchoL" /></li>
																</logic:iterate>
															</ul>
														</logic:notEmpty>
													</td>
													<td>
														<logic:notEmpty name="elemento" property="tipo">
															<ul>
																<logic:iterate id="listaTipo" name="elemento" property="tipo">
																	<li><bean:write name="listaTipo" /></li>
																</logic:iterate>
															</ul>
														</logic:notEmpty>
													</td>
													<td>
														<ul class="lista_linea">
															<li><html:link forward="viewUser" paramId="<%= Constants.USER %>" paramName="elemento" paramProperty="id_usuario"><img src="../images/bt_ver.gif" alt="<bean:message key="indice.usuarios.sistema.img.visualizar.alt" />"/></html:link></li>
															
															<jsp:useBean id="paramsEU" class="java.util.HashMap" />
															<c:set target="${paramsEU}" property="user" value="${elemento.id_usuario}" />
															
															<logic:equal value="<%=idSystemRole %>" name="elemento" property="tipoRol">
																<li><html:link forward="editSystemUser" name="paramsEU"><img src="../images/bt_modificar.gif" alt="<bean:message key="indice.usuarios.sistema.img.editar.alt" />"/></html:link></li>
															</logic:equal>
															<logic:equal value="<%=idClientRole %>" name="elemento" property="tipoRol">
																<li><html:link forward="editClientUser" name="paramsEU"><img src="../images/bt_modificar.gif" alt="<bean:message key="indice.usuarios.sistema.img.editar.alt" />"/></html:link></li>
															</logic:equal>
															<logic:equal value="<%=idObservatoryRole %>" name="elemento" property="tipoRol">
																<li><html:link forward="editObservatoryUser" name="paramsEU"><img src="../images/bt_modificar.gif" alt="<bean:message key="indice.usuarios.sistema.img.editar.alt" />"/></html:link></li>
															</logic:equal>
															<li><html:link forward="deleteSystemUser" paramId="<%= Constants.USER %>" paramName="elemento" paramProperty="id_usuario"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.usuarios.sistema.img.eliminar.alt" />"/></html:link></li>
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
	</div> 