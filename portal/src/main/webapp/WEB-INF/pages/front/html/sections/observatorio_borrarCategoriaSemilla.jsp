<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

	<bean:parameter name="<%=Constants.ID_CATEGORIA %>" id="idcat"/>

    <!-- observatorio_borrarCategoriaSemilla.jsp -->
    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li><html:link forward="getSeedCategories"><bean:message key="migas.categoria" /></html:link></li>
                  <li class="active"><bean:message key="migas.eliminar.categoria" /></li>
                </ol>
            </div>

            <h2><bean:message key="categoria.semillas.titulo" /></h2>
            <div class="detail">
                <p><strong class="labelVisu"><bean:message key="categoria.semillas.borrar.advertencia" /></strong></p>
                <logic:notEmpty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
                    <p><strong class="labelVisu"><bean:message key="categoria.semillas.borrar.info" /></strong></p>
                    <ul class="lista_inicial">
                        <logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>" id="elemento">
                            <li><bean:write name="elemento" property="nombre"/></li>
                        </logic:iterate>
                    </ul>
                    <p><strong class="labelVisu"><bean:message key="categoria.semillas.borrar.info2" /></strong></p>
                </logic:notEmpty>
                <div class="formButton">
                    <html:link styleClass="btn btn-primary btn-lg" forward="deleteSeedCategory" paramId="<%= Constants.ID_CATEGORIA %>" paramName="<%=Constants.ID_CATEGORIA %>"><bean:message key="boton.aceptar"/></html:link>
                    <html:link styleClass="btn btn-default btn-lg" forward="getSeedCategories"><bean:message key="boton.cancelar"/></html:link>
                </div>
            </div>
        </div>
    </div>
