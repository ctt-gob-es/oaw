<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<% request.getSession().removeAttribute(Constants.MENU); %>

	<bean:define id="rolAdmin"><inteco:properties key="role.administrator.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolConfig"><inteco:properties key="role.configurator.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolVisor"><inteco:properties key="role.visualizer.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolCustResp"><inteco:properties key="role.customer.responsible.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolCustUser"><inteco:properties key="role.customer.user.id" file="crawler.properties" /></bean:define>
	
<div id="migas">
	<span class="oculto"><bean:message key="ubicacion.usuario" /> </span> <bean:message key="migas.inicio" />
</div>

<div id="cuerpo">
	<div id="cIzq">&nbsp;</div>
	<div id="contenido">
		<div id="main">
			<div id="cuerpoprincipal">
				<div id="container_menu_izq">
					<jsp:include page="menu.jsp"/>
				</div>
				<div id="container_der">
					<div class="textoinformativo">
						<h2 class="config"><bean:message key="menuadmin.informacion" /></h2>
						<p>
							<bean:message key="welcome.common.message"/>
						</p>
						<div>
							<ul class="lista_inicial">
								<inteco:menu roles="<%=rolAdmin%>">
									<li><bean:message key="welcome.action.message.admin.users" /></li>
									<li><bean:message key="welcome.action.message.admin.cartridges" /></li>
									<li><bean:message key="welcome.action.message.admin.categories" /></li>
								</inteco:menu>
								<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
									<li><bean:message key="welcome.action.message.admin.crawlings" /></li>
									<li><bean:message key="welcome.action.message.generate.seeds" /></li>
								</inteco:menu>
								<inteco:menu roles="<%=rolVisor + \";\" + rolCustResp + \";\" + rolCustUser%>">
									<li><bean:message key="welcome.action.message.see.crawlings" /></li>
								</inteco:menu>
								<inteco:menu roles="<%=rolCustResp%>">
									<li><bean:message key="welcome.action.message.access.account.data" /></li>
								</inteco:menu>
									<li><bean:message key="welcome.action.message.change.password" /></li>
							</ul>
						</div>
					</div>
				</div>
			</div><!-- fin CUERPO PRINCIPAL -->
		</div> 
	</div>
</div>