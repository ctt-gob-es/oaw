<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

	<bean:parameter name="<%=Constants.ID_EX_OBS %>" id="id"/>
	<bean:parameter name="<%=Constants.ID_OBSERVATORIO %>" id="id_observatorio"/>

    <!-- observatorio_confirmacionBorrarSemillaObEx.jsp -->
    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                    <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                    <li><html:link forward="resultadosPrimariosObservatorio" paramName="id_observatorio" paramId="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link></li>
                    <li class="active"><bean:message key="migas.eliminar.observatorio" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="eliminar.observatorio.realizado.title" /></h2>

                <div>
                    <bean:define id="seedName" name="SemillaForm" property="nombre"/>
                    <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.ejecucion.semilla.observatorio">
                        <jsp:attribute name="arg0">
                            <bean:write name="<%=Constants.SEMILLA_FORM %>" property="nombre"/>
                        </jsp:attribute>
                    </bean:message></strong></p>
                    <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.ejecucion.semilla.observatorio.info" /></strong></p>
                </div>
                <div class="formButton">
                    <bean:parameter id="observatoryId" name="<%= Constants.ID_OBSERVATORIO %>"/>
                    <bean:parameter id="observatoryExId" name="<%= Constants.ID_EX_OBS %>"/>
                    <bean:parameter id="id" name="<%= Constants.ID %>"/>
                    <bean:parameter id="cartridgeId" name="<%= Constants.ID_CARTUCHO %>"/>
                    <bean:define id="observatoryIdSTR"><%= Constants.ID_OBSERVATORIO %></bean:define>
                    <bean:define id="observatoryExIdSTR"><%= Constants.ID_EX_OBS %></bean:define>
                    <bean:define id="idSTR"><%= Constants.ID %></bean:define>
                    <bean:define id="cartridgeIdSTR"><%= Constants.ID_CARTUCHO %></bean:define>
                    <jsp:useBean id="params" class="java.util.HashMap" />
                    <c:set target="${params}" property="${observatoryIdSTR}" value="${observatoryId}"/>
                    <c:set target="${params}" property="${observatoryExIdSTR}" value="${observatoryExId}"/>
                    <c:set target="${params}" property="${cartridgeIdSTR}" value="${cartridgeId}"/>
                    <c:set target="${params}" property="${idSTR}" value="${id}"/>
                    <html:link styleClass="btn btn-primary btn-lg" forward="deleteObservatoryCrawlerExecution" name="params"><bean:message key="boton.aceptar"/></html:link>

                    <html:link styleClass="btn btn-default btn-lg" forward="resultadosObservatorioSemillas" name="params"><bean:message key="boton.cancelar"/></html:link>
                </div>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
