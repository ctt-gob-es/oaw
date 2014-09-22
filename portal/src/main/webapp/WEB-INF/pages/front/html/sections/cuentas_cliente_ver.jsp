<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<bean:message key="migas.cuentas.cliente" /></p>
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
							<h2 class="config"><bean:message key="ver.usuario.title" /></h2>
							<div class="detail">
								<p><bean:message key="client.accounts.info"/></p>
								<ul class="lista_inicial">
									<logic:iterate name="<%=Constants.LIST_ACCOUNTS %>" id="account">
										<li>
											<html:link forward="verCuentaUsuario" paramId="<%=Constants.ID_CUENTA %>" paramName="account" paramProperty="value">
												<bean:write name="account" property="label" />
											</html:link>
										</li> 
									</logic:iterate>
								</ul>
							</div>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 

