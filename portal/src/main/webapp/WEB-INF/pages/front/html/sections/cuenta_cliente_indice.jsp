<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / <bean:message key="migas.cuenta.cliente" /></p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="usu"><bean:message key="indice.cuentas.usuario.sistema.gestion.usuarios" /> </h1>
				<div id="cuerpoprincipal">		
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					<div id="container_der">
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="indice.cuentas.usuario.sistema.usuarios" /></h2>
							
							<logic:notPresent name="<%=Constants.CARGAR_CUENTA_USUARIO_FORM %>">	
								<div class="notaInformativaExito">
									<p><bean:message key="indice.cuentas.usuario.vacio"/></p>
									<p><html:link forward="newUserAccount" styleClass="boton"><bean:message key="indice.usuarios.sistema.nueva.cuenta.usuario" /></html:link>
									<html:link forward="indexAdmin" styleClass="boton"><bean:message key="boton.volver" /></html:link></p>
								</div>
							</logic:notPresent>
							<logic:present name="<%=Constants.CARGAR_CUENTA_USUARIO_FORM %>">
								<logic:empty name="<%=Constants.CARGAR_CUENTA_USUARIO_FORM %>" property="listadoCuentasUsuario">	
									<div class="notaInformativaExito">
										<p><bean:message key="indice.cuentas.usuario.vacio"/></p>
										<p><html:link forward="newUserAccount" styleClass="boton"><bean:message key="indice.usuarios.sistema.nueva.cuenta.usuario" /></html:link>
										<html:link forward="indexAdmin" styleClass="boton"><bean:message key="boton.volver" /></html:link></p>
									</div>
								</logic:empty>
								<logic:notEmpty name="<%=Constants.CARGAR_CUENTA_USUARIO_FORM %>" property="listadoCuentasUsuario">	
									<p><html:link forward="newUserAccount" styleClass="boton"><bean:message key="indice.usuarios.sistema.nueva.cuenta.usuario" /></html:link></p>
									<div class="pag">
										<table>
											<caption><bean:message key="indice.cuentas.usuario.sistema.lista.usuarios" /></caption>
											<tr>
												<th><bean:message key="indice.cuentas.usuario.nombre" /></th>
												<th><bean:message key="indice.cuentas.usuario.cartuchos" /></th>
												<th><bean:message key="indice.cuentas.usuario.dominio" /></th>
												<th><bean:message key="indice.cuentas.usuario.acciones" /></th>
											</tr>
											<logic:iterate name="CargarCuentasUsuarioForm" type="es.inteco.rastreador2.utils.ListadoCuentasUsuario" property="listadoCuentasUsuario" id="elemento">
												<tr>
													<td ><html:link forward="verCuentaUsuario" paramId="<%= Constants.ID_CUENTA %>" paramName="elemento" paramProperty="id_cuenta"><bean:write name="elemento" property="nombreCuenta" /></html:link></td>
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
														<ul>
															<logic:iterate id="dominioL" name="elemento" property="dominio">
																<li><bean:write name="dominioL" /></li>
															</logic:iterate>
														</ul>
													</td>
													<td>
														<ul class="lista_linea">
															<li><html:link forward="verCuentaUsuario" paramId="<%= Constants.ID_CUENTA %>" paramName="elemento" paramProperty="id_cuenta"><img src="../images/bt_ver.gif" alt="<bean:message key="indice.cuentas.usuario.img.visualizar.alt" />"/></html:link></li>
															<li><html:link forward="editUserAccount" paramId="<%= Constants.ID_CUENTA %>" paramName="elemento" paramProperty="id_cuenta"><img src="../images/bt_modificar.gif" alt="<bean:message key="indice.cuentas.usuario.img.editar.alt" />"/></html:link></li>
															<li><html:link forward="deleteUserAccount" paramId="<%= Constants.ID_CUENTA %>" paramName="elemento" paramProperty="id_cuenta"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.cuentas.usuario.img.eliminar.alt" />"/></html:link></li>
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