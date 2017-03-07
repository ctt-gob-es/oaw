<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<bean:define id="rolAdmin"><inteco:properties key="role.administrator.id" file="crawler.properties" /></bean:define>
<bean:define id="rolConfig"><inteco:properties key="role.configurator.id" file="crawler.properties" /></bean:define>
<bean:define id="rolVisor"><inteco:properties key="role.visualizer.id" file="crawler.properties" /></bean:define>
<bean:define id="rolCustResp"><inteco:properties key="role.customer.responsible.id" file="crawler.properties" /></bean:define>
<html:xhtml/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		 / <bean:message key="migas.rastreos.cliente" /></p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
							<h2><bean:message key="indice.rastreos.cliente" /></h2>
							
							<table>
								<caption><bean:message key="indice.rastreo.lista.rastreos.cliente" /></caption>
								<tr>
									<th><bean:message key="indice.rastreo.nombre" /></th>
									<th><bean:message key="indice.rastreo.cartucho" /></th>
									<th><bean:message key="indice.rastreo.fecha" /></th>
									<th><bean:message key="indice.rastreo.estado" /></th>
									<th><bean:message key="indice.rastreo.acciones" /></th>
								</tr>
								<logic:iterate name="<%=Constants.RASTREOS %>" id="elemento">
									<tr>
										<td><bean:write name="elemento" property="codigo" /></td>
										<td><bean:write name="elemento" property="cartucho" /></td>
										<td><bean:write name="elemento" property="fecha" /></td>
										<td>
											<bean:define id="estadoTexto">
												<bean:write name="elemento" property="estadoTexto" /> 
											</bean:define>
											<bean:message key="<%=estadoTexto%>"/>
										</td>
										<td>
											<ul class="lista_linea">
												<jsp:useBean id="params" class="java.util.HashMap" />
												<c:set target="${params}" property="idrastreo" value="${elemento.id_rastreo}" />
												<c:set target="${params}" property="isCliente" value="true" />
												<li><html:link forward="loadClientFulfilledCrawlings" name="params"><img src="../images/list.gif" alt="indice.rastreo.ver.rastreos.realizados.alt"/></html:link></li>
											</ul>
										</td>
									</tr>
								</logic:iterate>
							</table>
							<jsp:include page="pagination.jsp" />
							</div>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>	
	</div> 
