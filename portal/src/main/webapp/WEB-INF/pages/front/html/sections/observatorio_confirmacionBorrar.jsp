<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.eliminar.observatorio" /></li>
                </ol>
            </div>
            <div id="cajaformularios">

                <h2><bean:message key="eliminar.observatorio.title" /></h2>

                <div class="detail">
                    <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.pregunta" /></strong></p>
                    <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.info" /></strong></p>
                    <div class="formItem">
                        <label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.nombre" />: </strong></label>
                        <p><bean:write name="<%= Constants.OBSERVATORY_FORM %>" property="nombre" /></p>
                    </div>
                    <div class="formButton">
                        <bean:define id="confSi" value="<%= Constants.CONF_SI%>" />
                        <bean:define id="confNo" value="<%= Constants.CONF_NO%>" />
                        <bean:define id="confirmacion" value="<%= Constants.CONFIRMACION%>" />
                        <bean:define id="observatorioSTR" value="<%= Constants.OBSERVATORY_ID%>" />
                        <bean:define id="observatorioId" name="<%= Constants.OBSERVATORY_FORM %>" property="id"/>
                        <jsp:useBean id="paramsSI" class="java.util.HashMap"/>
                        <jsp:useBean id="paramsNO" class="java.util.HashMap"/>

                        <c:set target="${paramsSI}" property="${action}" value="${actionDel}" />
                        <c:set target="${paramsSI}" property="${confirmacion}" value="${confSi}" />
                        <c:set target="${paramsSI}" property="${observatorioSTR}" value="${observatoryForm.id}" />

                        <c:set target="${paramsNO}" property="${confirmacion}" value="${confNo}" />
                        <c:set target="${paramsNO}" property="${observatorioSTR}" value="${observatoryForm.id}" />

                        <html:link styleClass="btn btn-primary btn-lg" forward="deleteObservatory" name="paramsSI"><bean:message key="boton.aceptar"/></html:link>
                        <html:link styleClass="btn btn-default btn-lg" forward="deleteObservatory" name="paramsNO"><bean:message key="boton.cancelar"/></html:link>
                    </div>
                </div>

            </div><!-- fin cajaformularios -->
        </div>
    </div>