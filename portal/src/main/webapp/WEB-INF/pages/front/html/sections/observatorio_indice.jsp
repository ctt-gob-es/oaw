<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<bean:define id="rolObservatory"><inteco:properties key="role.observatory.id" file="crawler.properties" /></bean:define>
	<bean:define id="rolAdmin"><inteco:properties key="role.administrator.id" file="crawler.properties" /></bean:define>

    <div id="main">
        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li class="active"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2 class="pull-left"><bean:message key="indice.observatorio.observatorio" /></h2>
                <jsp:include page="/common/crawler_messages.jsp" />
                <logic:notPresent name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>">
                    <div class="notaInformativaExito">
                        <p><bean:message key="indice.observatorio.vacio"/></p>
                        <p><html:link forward="newObservatory" styleClass="btn btn-default btn-lg"><bean:message key="indice.observatorio.nuevo.observatorio" /></html:link>
                        <html:link forward="indexAdmin" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link></p>
                    </div>
                </logic:notPresent>
                <logic:present name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>">
                    <logic:empty name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>" property="listadoObservatorio">
                        <div class="notaInformativaExito">
                            <p><bean:message key="indice.observatorio.vacio"/></p>
                            <p class="pull-right"><html:link forward="newObservatory" styleClass="boton"><bean:message key="indice.observatorio.nuevo.observatorio" /></html:link>
                            <html:link forward="indexAdmin" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link></p>
                        </div>
                    </logic:empty>

                    <logic:notEmpty name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>" property="listadoObservatorio">
                        <p class="pull-right"><html:link forward="newObservatory" styleClass="btn btn-default btn-lg">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title="Crear un nuevo observatorio"></span> <bean:message key="indice.observatorio.nuevo.observatorio" />
                        </html:link></p>
                        <div class="pag">
                            <table class="table table-stripped table-bordered table-hover">
                                <caption><bean:message key="indice.observatorio.lista" /></caption>
                                <tr>
                                    <th><bean:message key="indice.observatorio.nombre" /></th>
                                    <th>Tipo</th>
                                    <th><bean:message key="indice.observatorio.cartucho" /></th>
                                    <th class="accion"><bean:message key="indice.observatorio.acciones" /></th>
                                    <th class="accion">Exportar</th>
                                    <th class="accion">Eliminar</th>
                                </tr>
                                <logic:iterate name="<%=Constants.CARGAR_OBSERVATORIO_FORM %>" property="listadoObservatorio" id="elemento">
                                        <jsp:useBean id="params" class="java.util.HashMap" />
                                        <bean:define id="actionMod" value="<%= Constants.ACCION_MODIFICAR %>" />
                                        <bean:define id="action" value="<%= Constants.ACTION %>" />
                                        <bean:define id="observatoryId" name="elemento" property="id_observatorio" />
                                        <bean:define id="observatorySTR" value="<%= Constants.OBSERVATORY_ID %>" />
                                        <c:set target="${params}" property="${observatorySTR}" value="${observatoryId}" />
                                        <c:set target="${params}" property="${action}" value="${actionMod}"/>
                                    <tr>
                                        <bean:define id="detailTitle"><bean:message key="indice.observatorio.detalle.alt" /></bean:define>
                                        <td style="text-align: left">
                                            <inteco:menu roles="<%=rolAdmin%>">
                                                <html:link forward="editObservatory" name="params"><span data-toggle="tooltip" title="Editar la configuraci\u00f3n de este observatorio"><bean:write name="elemento" property="nombreObservatorio" /></span></html:link>
                                                <span class="glyphicon glyphicon-edit pull-right edit-mark" aria-hidden="true"/>
                                            </inteco:menu>
                                        </td>
                                        <td><bean:write name="elemento" property="tipo"/></td>
                                        <td><bean:write name="elemento" property="cartucho"/></td>
                                        <td>
                                            <html:link forward="resultadosPrimariosObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="elemento" paramProperty="id_observatorio"><span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip" title="Ver iteraciones de este observatorio"/><span class="sr-only">Resultados</span></html:link>
                                            <!-- <li><html:link forward="getFulfilledObservatories" paramId="<%=Constants.OBSERVATORY_ID %>" paramName="observatoryId"><img src="../images/anonimo.gif" alt="<bean:message key="indice.observatorio.resultados.anonimos.alt" />"/></html:link></li> -->
                                            <!-- <li><html:link forward="databaseExportActionConfirm" paramId="<%=Constants.OBSERVATORY_ID %>" paramName="observatoryId" ><img src="../images/database.jpg" alt="<bean:message key="indice.rastreo.exportar.database" />"/></html:link></li> -->
                                        </td>
                                        <td>
                                            <!-- <jsp:useBean id="params2" class="java.util.HashMap" />
                                            <c:set target="${params2}" property="id_observatorio" value="${id_observatorio}" />
                                            <c:set target="${params2}" property="${id_ex_obs}" value="${fulfilledObservatory.id}" />
                                            <c:set target="${params2}" property="esPrimera" value="true"/>
                                            <c:set target="${params2}" property="isPrimary" value="true"/>
                                            <html:link forward="getAnnexes" name="params2">
                                                <span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip" title="Exportar los resultados de todas las iteraciones del observatorio en formato XML"/>
                                                <span class="sr-only">Exportar resultados</span>
                                            </html:link> -->
                                            <jsp:useBean id="paramsInformeAgregado" class="java.util.HashMap" />
                                            <c:set target="${paramsInformeAgregado}" property="id_observatorio" value="${id_observatorio}" />
                                            <c:set target="${paramsInformeAgregado}" property="id" value="${fulfilledObservatory.id}" />
                                            <c:set target="${paramsInformeAgregado}" property="esPrimera" value="true"/>
                                            <c:set target="${paramsInformeAgregado}" property="isPrimary" value="false"/>
                                            <c:set target="${paramsInformeAgregado}" property="idExObs" value="${fulfilledObservatory.id}" />
                                            <html:link forward="getAnnexes" name="paramsInformeAgregado">
                                                <span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip" title="Exportar los resultados de todas las iteraciones del observatorio en formato XML"/>
                                                <span class="sr-only">Exportar resultados</span>
                                            </html:link>
                                        </td>
                                        <td>
                                            <jsp:useBean id="paramsEsPrim" class="java.util.HashMap" />
                                            <bean:define id="actionEsPrim" value="<%= Constants.ES_PRIMERA %>" />
                                            <bean:define id="observatoryId" name="elemento" property="id_observatorio" />
                                            <bean:define id="observatorySTR" value="<%= Constants.OBSERVATORY_ID %>" />
                                            <c:set target="${paramsEsPrim}" property="${observatorySTR}" value="${observatoryId}" />
                                            <c:set target="${paramsEsPrim}" property="${actionEsPrim}" value="si"/>
                                            <html:link forward="deleteObservatory" name="paramsEsPrim">
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip" title="Eliminar este observatorio"/><span class="sr-only">Eliminar</span>
                                            </html:link>
                                        </td>
                                    </tr>
                                </logic:iterate>
                            </table>

                            <jsp:include page="pagination.jsp" />
                        </div>
                        <p id="pCenter"><html:link forward="indexAdmin" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
                    </logic:notEmpty>
                </logic:present>
            </div><!-- fin cajaformularios -->
        </div><!-- Container Derecha -->
	</div>