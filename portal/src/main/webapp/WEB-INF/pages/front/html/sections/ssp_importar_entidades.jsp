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
                
                <html:form method="post" styleClass="formulario" action="/secure/ImportarEntidades.do?action=upload" enctype="multipart/form-data">
				            
					<jsp:include page="/common/crawler_messages.jsp" />
					<div class="formItem">
						<label for="name" class="control-label">
						<strong class="labelVisu">
							<acronym title="<bean:message key="campo.obligatorio" />"> * </acronym>
							<bean:message key="subir.fichero.import" />
						</strong>
						</label>
						<html:file styleClass="texto formControl" maxlength="100" styleId="file" property="file"/>
					</div>
					<div class="formButton">
						<html:submit styleClass="btn btn-primary btn-lg"><bean:message key="boton.aceptar"/></html:submit>
						<html:cancel styleClass="btn btn-default btn-lg"><bean:message key="boton.cancelar"/></html:cancel>
					</div>
                  
                </html:form>
            </div>
          
    </div>
</div>