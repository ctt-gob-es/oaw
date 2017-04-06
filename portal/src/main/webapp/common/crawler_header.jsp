<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>

<div id="cabeza">
    <h1>
        <img class="pull-left" src="/oaw/images/logo.jpg" alt="<bean:message key="application.logo.alt" />" />
        <img src="/oaw/images/mhfp.gif" alt="<bean:message key="ministerio.logo.alt" />" />
    </h1>
	 <div id="logout">
         <html:link forward="logout"><span class="glyphicon glyphicon-log-out" aria-hidden="true" data-toggle="tooltip" title="Salir de la aplicaci&oacute;n"></span> <bean:message key="menuvisor.logOut" /></html:link>
     </div>
</div> <!-- fin cabecera -->


	
