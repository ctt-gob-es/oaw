<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="SemillaObservatorioForm"/>

	<jsp:useBean id="paramsNS" class="java.util.HashMap" />
	<c:set target="${paramsNS}" property="action" value="anadir" />
	<c:set target="${paramsNS}" property="esPrimera" value="si" />
	

    <!-- observatorio_cargarSemillas.jsp -->
    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.semillas.observatorio" /> </li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="gestion.semillas.observatorio.titulo"/></h2>

                <html:form action="/secure/ViewSemillasObservatorio.do" method="get" styleClass="formulario form-horizontal">
                    <input type="hidden" name="<%= Constants.ACTION %>" value="<%= Constants.LOAD %>"/>
                    <fieldset>
                        <legend>Buscador</legend>
                        <jsp:include page="/common/crawler_messages.jsp" />
                        <div class="formItem">
                            <label for="nombre" class="control-label"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.nombre" /></strong></label>
                            <html:text styleClass="texto form-control" styleId="nombre" property="nombre" />
                        </div>
                        <div class="formItem">
                            <label for="categoria" class="control-label"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.categoria" /></strong></label>
                            <html:select styleClass="textoSelect form-control" styleId="categoria" property="categoria" >
                                <html:option value=""><bean:message key="resultados.observatorio.cualquier.categoria" /></html:option>
                                <logic:iterate name="<%= Constants.CATEGORIES_LIST %>" id="categoria">
                                    <bean:define id="idCategoria"><bean:write name="categoria" property="id"/></bean:define>
                                    <html:option value="<%=idCategoria %>"><bean:write name="categoria" property="name"/></html:option>
                                </logic:iterate>
                            </html:select>
                        </div>
                        <div class="formItem">
                            <label for="url" class="control-label"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.url" /></strong></label>
                            <html:text styleClass="texto form-control" styleId="url" property="url" />
                        </div>
                        <div class="formButton">
                            <button type="submit" class="btn btn-default btn-lg">
                                <span class="glyphicon glyphicon-search" aria-hidden="true"></span> <bean:message key="boton.buscar" />
                            </button>
                        </div>
                    </fieldset>
                </html:form>

                <div class="detail">
                    <logic:notPresent name="<%= Constants.OBSERVATORY_SEED_LIST %>">
                        <div class="notaInformativaExito">
                            <p><bean:message key="semilla.observatorio.vacia"/></p>
                            <p><html:link forward="observatorySeeds" name="paramsNS" styleClass="boton"><bean:message key="cargar.semilla.observatorio.nueva.semilla" /></html:link>
                                <html:link styleClass="btn btn-default btn-lg" forward="indexAdmin"><bean:message key="boton.volver"/></html:link></p>
                    </div>
                        </div>
                    </logic:notPresent>
                    <logic:present name="<%= Constants.OBSERVATORY_SEED_LIST %>">
                        <logic:empty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
                            <div class="notaInformativaExito">
                                <p><bean:message key="semilla.observatorio.vacia"/></p>
                                <p><html:link forward="observatorySeeds" name="paramsNS" styleClass="boton"><bean:message key="cargar.semilla.observatorio.nueva.semilla" /></html:link>
                                   <html:link styleClass="btn btn-default btn-lg" forward="indexAdmin"><bean:message key="boton.volver"/></html:link></p>
                            </div>
                        </logic:empty>
                        <logic:notEmpty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
                            <p class="pull-right">
                                <html:link forward="observatorySeeds" name="paramsNS" styleClass="btn btn-default btn-lg">
                                    <span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title="Crear una nueva semilla"></span> <bean:message key="cargar.semilla.observatorio.nueva.semilla" />
                                </html:link>
                            </p>
                            <div class="pag">
                                <table class="table table-stripped table-bordered table-hover">
                                    <caption><bean:message key="lista.semillas.observatorio"/></caption>
                                    <tr>
                                        <th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
                                        <th><bean:message key="cargar.semilla.observatorio.categoria" /></th>
                                        <th><bean:message key="cargar.semilla.observatorio.activa" /></th>
                                        <th class="accion">Eliminar</th>
                                    </tr>
                                    <logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>" id="semilla">
                                        <bean:define id="action"><%= Constants.ACTION %></bean:define>
                                        <bean:define id="semillaId" name="semilla" property="id" />
                                        <bean:define id="semillaSTR"><%= Constants.SEMILLA %></bean:define>
                                        <tr>
                                            <td style="text-align: left">
                                                <bean:define id="actionDet"><%= Constants.ACCION_SEED_DETAIL %></bean:define>
                                                <jsp:useBean id="params" class="java.util.HashMap" />
                                                <bean:define id="actionMod"><%= Constants.ACCION_MODIFICAR %></bean:define>
                                                <c:set target="${params}" property="${semillaSTR}" value="${semillaId}" />
                                                <c:set target="${params}" property="${action}" value="${actionMod}"/>
                                                <html:link forward="observatorySeeds" name="params">
                                                    <span data-toggle="tooltip" title="Editar la configuraci&oacute;n de esta semilla"/><bean:write name="semilla" property="nombre" /></span>
                                                </html:link>

                                                <span class="glyphicon glyphicon-edit pull-right edit-mark" aria-hidden="true"/>
                                            </td>
                                            <td>
                                                <bean:write name="semilla" property="categoria.name"/>
                                            </td>
                                            <td>
                                                <logic:equal name="semilla" property="activa" value="true">
                                                    <bean:message key="si"/>
                                                </logic:equal>
                                                <logic:equal name="semilla" property="activa" value="false">
                                                    <bean:message key="no"/>
                                                </logic:equal>
                                            </td>
                                            <td>
                                                <logic:equal value="false" name="semilla" property="asociada">
                                                    <jsp:useBean id="paramsD" class="java.util.HashMap" />
                                                    <bean:define id="actionDel"><%= Constants.ACCION_CONFIRMACION_BORRAR %></bean:define>
                                                    <c:set target="${paramsD}" property="${semillaSTR}" value="${semillaId}" />
                                                    <c:set target="${paramsD}" property="${action}" value="${actionDel}"/>
                                                    <html:link forward="observatorySeeds" name="paramsD">
                                                        <span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip" title="Eliminar esta semilla"/>
                                                        <span class="sr-only"><bean:message key="eliminar.semilla.observatorio" /></span>
                                                    </html:link>
                                                </logic:equal>
                                                <logic:equal value="true" name="semilla" property="asociada">
                                                    <img src="../images/bt_eliminar_escala_grises.gif" alt="<bean:message key="eliminar.semilla.observatorio.desactivado" />"/>
                                                </logic:equal>
                                            </td>
                                        </tr>
                                    </logic:iterate>
                                </table>
                                <jsp:include page="pagination.jsp" />
                                </div>
                            </logic:notEmpty>
                    </logic:present>
                </div>
                <p id="pCenter"><html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
