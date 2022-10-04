<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<bean:define id="idSystemRole">
	<inteco:properties key="role.type.system" file="crawler.properties" />
</bean:define>
<bean:define id="idClientRole">
	<inteco:properties key="role.type.client" file="crawler.properties" />
</bean:define>
<bean:define id="idObservatoryRole">
	<inteco:properties key="role.type.observatory" file="crawler.properties" />
</bean:define>
	
	<logic:present name="deMenu">
		<html:javascript formName="ModificarUsuarioPassForm"/>
	</logic:present>
	<logic:notPresent name="deMenu">
		<html:javascript formName="ModificarUsuarioPassFormAdmin"/>
	</logic:notPresent>
	
    <div id="main">
        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>
        <div id="container_der">
            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.other.options" /></li>
                  <li class="active"><bean:message key="migas.importar.entidad"/></li>
                </ol>
            </div>
            <div id="cajaformularios">
                <h2><bean:message key="menuadmin.importar.entidades" /></h2>
                <bean:define id="action" value="" />
                <bean:define id="form" value="" />
                <bean:define id="formName" value="" />
                
                <p>Importación finalizada con éxito</p>
                </html:form>
            </div><!-- fin cajaformularios -->
        </div>
</div>