<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<bean:define id="rolAdmin"><inteco:properties key="role.administrator.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolConfig"><inteco:properties key="role.configurator.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolVisor"><inteco:properties key="role.visualizer.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolCustResp"><inteco:properties key="role.customer.responsible.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolCustUser"><inteco:properties key="role.customer.user.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolObservatory"><inteco:properties key="role.observatory.id" file="crawler.properties" /></bean:define>
<ul>
	<li class="menutit"><html:link forward="logout" titleKey="menuvisor.logOut.title"><bean:message key="menuvisor.logOut" /></html:link></li>
	
	<li class="menutit">
		<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_PASSWORD %>">
			<html:link forward="changePassMenu" styleId="selected" titleKey="menuvisor.cambiar.pass.title"><bean:message key="menuvisor.cambiar.pass" /></html:link>
		</logic:equal>
		<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_PASSWORD %>">
			<html:link forward="changePassMenu" titleKey="menuvisor.cambiar.pass.title"><bean:message key="menuvisor.cambiar.pass" /></html:link>
		</logic:notEqual>
	</li>
	
	<inteco:menu roles="<%=rolAdmin%>">
    	<li class="menutit">
    		<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_USERS %>">
    			<html:link forward="usersMenu" styleId="selected" titleKey="menuadmin.usuarios.title"><bean:message key="menuadmin.usuarios" /></html:link>
    		</logic:equal>
    		<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_USERS %>">
    			<html:link forward="usersMenu" titleKey="menuadmin.usuarios.title"><bean:message key="menuadmin.usuarios" /></html:link>
    		</logic:notEqual>
    	</li>
    </inteco:menu>
    
    <inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
    	<li class="menutit">
    		<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_CLIENT %>">
    			<html:link forward="clientAccountsMenu" styleId="selected" titleKey="menuadmin.cuenta.usuarios.title"><bean:message key="menuadmin.cuenta.usuarios" /></html:link>
    		</logic:equal>
    		<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_CLIENT %>">
    			<html:link forward="clientAccountsMenu" titleKey="menuadmin.cuenta.usuarios.title"><bean:message key="menuadmin.cuenta.usuarios" /></html:link>
    		</logic:notEqual>
    	</li>
	</inteco:menu>
	
	<%--
	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
		<li class="menutit">
			<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_CERTIFICATES %>">
				<html:link forward="loadCertificateForm" title="Gesti�n certificados digitales" styleId="selected"><bean:message key="menuadmin.certificates" /></html:link>
			</logic:equal>
			<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_CERTIFICATES %>">
				<html:link forward="loadCertificateForm" title="Gesti�n certificados digitales"><bean:message key="menuadmin.certificates" /></html:link>
			</logic:notEqual>
		</li>
	</inteco:menu>
	--%>
	
	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig + \";\" + rolVisor%>">
		<li class="menutit">
			<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_CRAWLINGS %>">
				<html:link styleId="selected" forward="crawlingsMenu" titleKey="menuadmin.rastreos.title"><bean:message key="menuadmin.rastreos" /></html:link>
			</logic:equal>
			<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_CRAWLINGS %>">
				<html:link forward="crawlingsMenu" titleKey="menuadmin.rastreos.title"><bean:message key="menuadmin.rastreos" /></html:link>
			</logic:notEqual>
		</li>
	</inteco:menu>
		
	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
		<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_SEEDS %>">
			<li class="menutit"><html:link forward="listSeedsMenu" titleKey="menuadmin.semillas.title"><bean:message key="menuadmin.semillas" /></html:link></li>
		</logic:notEqual>
		<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_SEEDS %>">
			<li id="submenu"><span><bean:message key="menuadmin.semillas" /></span>
				<ul>
					<li>
			   			<logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_LISTADO_SEM %>">
			   				<html:link forward="listSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.listado.semillas.title"><bean:message key="menuadmin.semillas.listado.semillas" /></html:link>
			   			</logic:equal>
			   			<logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_LISTADO_SEM %>">
			   				<html:link forward="listSeedsMenu" titleKey="menuadmin.semillas.listado.semillas.title"><bean:message key="menuadmin.semillas.listado.semillas" /></html:link>
			   			</logic:notEqual>
			   		</li>   		    	
	    			<li>
	    				<logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_IP %>">
	    					<html:link forward="ipSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.rangoIP.title"><bean:message key="menuadmin.semillas.rangoIP" /></html:link>
	    				</logic:equal>
	    				<logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_IP %>">
	    					<html:link forward="ipSeedsMenu" titleKey="menuadmin.semillas.rangoIP.title"><bean:message key="menuadmin.semillas.rangoIP" /></html:link>
	    				</logic:notEqual>
	    			</li>
			   		<li>
			   			<logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_GOOGLE %>">
			   				<html:link forward="googleSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.resultados.google.title"><bean:message key="menuadmin.semillas.resultados.google" /></html:link>
			   			</logic:equal>
			   			<logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_GOOGLE %>">
			   				<html:link forward="googleSeedsMenu" titleKey="menuadmin.semillas.resultados.google.title"><bean:message key="menuadmin.semillas.resultados.google" /></html:link>
			   			</logic:notEqual>
			   		</li>
				</ul>
			</li>
		</logic:equal>
	</inteco:menu>
	
	
	<inteco:menu roles="<%=rolCustResp + \";\" + rolCustUser%>">
		<li class="menutit">
			<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_CLIENT_CRAWLINGS %>">
				<html:link forward="loadClientCrawlings" styleId="selected" titleKey="menuclient.rastreos.title"><bean:message key="menuclient.rastreos" /></html:link>
			</logic:equal>
			<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_CLIENT_CRAWLINGS %>">
				<html:link forward="loadClientCrawlings" titleKey="menuclient.rastreos.title"><bean:message key="menuclient.rastreos"/></html:link>
			</logic:notEqual>
		</li>
	</inteco:menu>
	
	<inteco:menu roles="<%=rolCustResp%>">
		<li class="menutit">
			<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_CLIENT_CRAWLINGS_ACCOUNT %>">
				<html:link forward="verCuentasUsuario" styleId="selected" titleKey="menuclient.rastreos.cuenta.title"><bean:message key="menuclient.rastreos.cuenta" /></html:link>
			</logic:equal>
			<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_CLIENT_CRAWLINGS_ACCOUNT %>">
				<html:link forward="verCuentasUsuario" titleKey="menuclient.rastreos.cuenta.title"><bean:message key="menuclient.rastreos.cuenta"/></html:link>
			</logic:notEqual>
		</li>
	</inteco:menu>

	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig + \";\" + rolObservatory%>">	
		<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_INTECO_OBS %>">
			<li class="menutit"><html:link forward="observatoryMenu" titleKey="menuadmin.observatorio.title"><bean:message key="menuadmin.observatorio" /></html:link></li>
		</logic:notEqual>
		<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_INTECO_OBS %>">
			<li id="submenu"><span><bean:message key="menuadmin.observatorio" /></span>
				<ul>
					<li>
	    		    	<logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_OBSERVATORIO %>">
	    		    		<html:link styleId="subselected" forward="observatoryMenu" titleKey="menu.config.observatory.title"><bean:message key="menu.config.observatory" /></html:link>
	    		    	</logic:equal>
	    		    	<logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_OBSERVATORIO %>">
	    		    		<html:link forward="observatoryMenu" titleKey="menu.config.observatory.title"><bean:message key="menu.config.observatory" /></html:link>
	    		    	</logic:notEqual>
	    		    </li>
	    		    <inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
	    		    	<li>
		    		    	<logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_CATEGORIES %>">
		    		    		<html:link forward="getSeedCategories" styleId="subselected" titleKey="menuconfig.semillas.listado.categorias.title"><bean:message key="menuconfig.semillas.listado.categorias" /></html:link>
		    		    	</logic:equal>
		    		    	<logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_CATEGORIES %>">
		    		    		<html:link forward="getSeedCategories" titleKey="menuconfig.semillas.listado.categorias.title"><bean:message key="menuconfig.semillas.listado.categorias" /></html:link>
		    		    	</logic:notEqual>
		    		    </li>
						<li>
		    		    	<logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_OBS_SEMILLA %>">
		    		    		<html:link styleId="subselected" forward="observatorySeed" titleKey="menu.config.observatory.seed.title"><bean:message key="menu.config.observatory.seed" /></html:link>
		    		    	</logic:equal>
		    		    	<logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_OBS_SEMILLA %>">
		    		    		<html:link forward="observatorySeed" titleKey="menu.config.observatory.seed.title"><bean:message key="menu.config.observatory.seed" /></html:link>
		    		    	</logic:notEqual>
		    		    </li>
		    	    </inteco:menu>
	    		</ul>
			</li>
		</logic:equal>
	</inteco:menu>

		<inteco:menu roles="<%=rolAdmin%>">
        	<li class="menutit">
        		<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_SERVICIO_DIAGNOSTICO %>">
        			<html:link forward="servicioDiagnostico" styleId="selected" titleKey="menuadmin.servicio_diagnostico.title"><bean:message key="menuadmin.servicio_diagnostico" /></html:link>
        		</logic:equal>
        		<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_SERVICIO_DIAGNOSTICO %>">
        			<html:link forward="servicioDiagnostico" titleKey="menuadmin.servicio_diagnostico.title"><bean:message key="menuadmin.servicio_diagnostico" /></html:link>
        		</logic:notEqual>
        	</li>
        </inteco:menu>
</ul>
