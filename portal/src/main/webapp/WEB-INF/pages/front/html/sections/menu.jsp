<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<bean:define id="rolAdmin"><inteco:properties key="role.administrator.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolConfig"><inteco:properties key="role.configurator.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolVisor"><inteco:properties key="role.visualizer.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolCustResp"><inteco:properties key="role.customer.responsible.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolCustUser"><inteco:properties key="role.customer.user.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolObservatory"><inteco:properties key="role.observatory.id" file="crawler.properties" /></bean:define>


    <ul role="presentation" class="nav nav-pills nav-stacked">
        <inteco:menu roles="<%=rolAdmin + \";\" + rolConfig + \";\" + rolObservatory%>">
            <logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_OBSERVATORIO %>">
            <li class="active"><a><bean:message key="menuadmin.observatorio" /></a></li>
            </logic:equal>
            <logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_OBSERVATORIO %>">
            <li><html:link forward="observatoryMenu" titleKey="menuadmin.observatorio.title"><bean:message key="menuadmin.observatorio" /></html:link></li>
            </logic:notEqual>
            <li>
                <ul role="presentation" class="nav nav-pills nav-stacked">
                    <inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
                        <logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_CATEGORIES %>">
                        <li class="active">
                            <html:link forward="getSeedCategories" styleId="subselected" titleKey="menuconfig.semillas.listado.categorias.title"><bean:message key="menuconfig.semillas.listado.categorias" /></html:link>
                        </logic:equal>
                        <logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_CATEGORIES %>">
                        <li>
                            <html:link forward="getSeedCategories" titleKey="menuconfig.semillas.listado.categorias.title"><bean:message key="menuconfig.semillas.listado.categorias" /></html:link>
                        </logic:notEqual>
                        </li>

                        <logic:equal name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_OBS_SEMILLA %>">
                        <li class="active">
                            <html:link styleId="subselected" forward="observatorySeed" titleKey="menu.config.observatory.seed.title"><bean:message key="menu.config.observatory.seed" /></html:link>
                        </logic:equal>
                        <logic:notEqual name="<%=Constants.SUBMENU %>" value="<%=Constants.SUBMENU_OBS_SEMILLA %>">
                        <li>
                            <html:link forward="observatorySeed" titleKey="menu.config.observatory.seed.title"><bean:message key="menu.config.observatory.seed" /></html:link>
                        </logic:notEqual>
                        </li>
                    </inteco:menu>
                </ul>
            </li>
    	</inteco:menu>

        <inteco:menu roles="<%=rolAdmin%>">
            <logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_USERS %>">
                <li class="active"><html:link forward="usersMenu" styleId="selected" titleKey="menuadmin.usuarios.title"><bean:message key="menuadmin.usuarios" /></html:link></li>
                <li>
                    <ul role="presentation" class="nav nav-pills nav-stacked">
                        <li>
                            <html:link forward="changePassMenu" titleKey="menuvisor.cambiar.pass.title"><bean:message key="menuvisor.cambiar.pass" /></html:link>
                        </li>
                    </ul>
                </li>
            </logic:equal>
                        <logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_PASSWORD %>">
                            <li><html:link forward="usersMenu" styleId="selected" titleKey="menuadmin.usuarios.title"><bean:message key="menuadmin.usuarios" /></html:link></li>
                            <li>
                                <ul role="presentation" class="nav nav-pills nav-stacked">
                                    <li class="active">
                                        <html:link forward="changePassMenu" styleId="selected" titleKey="menuvisor.cambiar.pass.title"><bean:message key="menuvisor.cambiar.pass" /></html:link>
                                    </li>
                                </ul>
                            </li>
                        </logic:equal>
        </inteco:menu>

        <inteco:menu roles="<%=rolAdmin%>">
            <logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_SERVICIO_DIAGNOSTICO %>">
            <li class="active">
                <html:link forward="servicioDiagnostico" titleKey="menuadmin.servicio_diagnostico.title"><bean:message key="menuadmin.servicio_diagnostico" /></html:link>
            </logic:equal>
            <logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_SERVICIO_DIAGNOSTICO %>">
            <li>
                <html:link forward="servicioDiagnostico" titleKey="menuadmin.servicio_diagnostico.title"><bean:message key="menuadmin.servicio_diagnostico" /></html:link>
            </logic:notEqual>
            </li>
        </inteco:menu>

        <li role="presentation" class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Otras opciones <span class="caret pull-right"></span></a>
            <ul class="dropdown-menu nav nav-stacked nav-pills">
                <inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
                <li>
                    <html:link forward="clientAccountsMenu" styleId="selected" titleKey="menuadmin.cuenta.usuarios.title"><bean:message key="menuadmin.cuenta.usuarios" /></html:link>
                </li>
                </inteco:menu>
                <inteco:menu roles="<%=rolAdmin + \";\" + rolConfig + \";\" + rolVisor%>">
                <li>
                    <html:link styleId="selected" forward="crawlingsMenu" titleKey="menuadmin.rastreos.title"><bean:message key="menuadmin.rastreos" /></html:link>
                </li>
                </inteco:menu>
                <inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
                <li>
                    <html:link forward="listSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.listado.semillas.title"><bean:message key="menuadmin.semillas.listado.semillas" /></html:link>
                </li>
                <li>
                    <html:link forward="ipSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.rangoIP.title"><bean:message key="menuadmin.semillas.rangoIP" /></html:link>
                </li>
                <li>
                    <html:link forward="googleSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.resultados.google.title"><bean:message key="menuadmin.semillas.resultados.google" /></html:link>
                </li>
                </inteco:menu>
            </ul>
          </li>
    </ul>

<!--
<ul>
	<inteco:menu roles="<%=rolAdmin%>">
    	<li class="menutit">
    		<logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_USERS %>">
    			<html:link forward="usersMenu" styleId="selected" titleKey="menuadmin.usuarios.title"><bean:message key="menuadmin.usuarios" /></html:link>
    			<li><ul>
                    <li class="menutit">
                        <logic:equal name="<%=Constants.MENU %>" value="<%=Constants.MENU_PASSWORD %>">
                            <html:link forward="changePassMenu" styleId="selected" titleKey="menuvisor.cambiar.pass.title"><bean:message key="menuvisor.cambiar.pass" /></html:link>
                        </logic:equal>
                        <logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_PASSWORD %>">
                            <html:link forward="changePassMenu" titleKey="menuvisor.cambiar.pass.title"><bean:message key="menuvisor.cambiar.pass" /></html:link>
                        </logic:notEqual>
                    </li>
                </ul>
    		</logic:equal>
    		<logic:notEqual name="<%=Constants.MENU %>" value="<%=Constants.MENU_USERS %>">
    			<html:link forward="usersMenu" titleKey="menuadmin.usuarios.title"><bean:message key="menuadmin.usuarios" /></html:link>
    		</logic:notEqual>
    	</inteco:menu>
    	</li>

    <inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
        <li class="menutit">
            Advanced
        </li>
        <li class="menutit">
            <ul>



            </ul>
        <li>
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

</ul>

-->