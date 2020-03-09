<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
This program is licensed and may be used, modified and redistributed under the terms
of the European Public License (EUPL), either version 1.2 or (at your option) any later 
version as soon as they are approved by the European Commission.
Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND, either express or implied. See the License for the specific language governing 
permissions and more details.
You should have received a copy of the EUPL1.2 license along with this program; if not, 
you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>

<bean:define id="idSystemRole">
	<inteco:properties key="role.type.system" file="crawler.properties" />
</bean:define>
<bean:define id="idClientRole">
	<inteco:properties key="role.type.client" file="crawler.properties" />
</bean:define>
<bean:define id="idObservatoryRole">
	<inteco:properties key="role.type.observatory" file="crawler.properties" />
</bean:define>

<html:xhtml/>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li class="active"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.usuarios" /></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="indice.usuarios.sistema.usuarios" /></h2>

<!-- <p class="pull-right"><html:link forward="newObservatory" styleClass="btn btn-default btn-lg">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title="Crear un nuevo observatorio"></span> <bean:message key="indice.observatorio.nuevo.observatorio" />
                        </html:link></p> -->

                <logic:notPresent name="<%=Constants.CARGAR_USUARIOS_SISTEMA_FORM %>">
                    <div class="notaInformativaExito">
                        <p><bean:message key="indice.usuarios.sistema.vacio"/></p>
                        <p class="pull-right">
                            <html:link forward="newSystemUser" styleClass="btn btn-default btn-lg" paramId="<%=Constants.ROLE_TYPE %>" paramName="idSystemRole">
                                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                                <bean:message key="indice.usuarios.sistema.nuevo.usuario" />
                            </html:link>
                            <html:link forward="newClientUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idClientRole" styleClass="btn btn-default btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <bean:message key="indice.usuarios.sistema.nuevo.usuario.cliente" /></html:link>
                            <html:link forward="newObservatoryUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idObservatoryRole" styleClass="btn btn-default btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <bean:message key="indice.usuarios.sistema.nuevo.usuario.observatorio" /></html:link>
                        </p>
                    </div>
                </logic:notPresent>
                <logic:present name="<%=Constants.CARGAR_USUARIOS_SISTEMA_FORM %>">
                    <logic:empty name="<%=Constants.CARGAR_USUARIOS_SISTEMA_FORM %>" property="listadoUsuarios">
                        <div class="notaInformativaExito">
                            <p><bean:message key="indice.usuarios.sistema.vacio"/></p>
                            <p class="pull-right">
                                <html:link forward="newSystemUser" styleClass="btn btn-default btn-lg" paramId="<%=Constants.ROLE_TYPE %>" paramName="idSystemRole"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <bean:message key="indice.usuarios.sistema.nuevo.usuario" /></html:link>
                                <html:link forward="newClientUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idClientRole" styleClass="btn btn-default btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <bean:message key="indice.usuarios.sistema.nuevo.usuario.cliente" /></html:link>
                                <html:link forward="newObservatoryUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idObservatoryRole" styleClass="btn btn-default btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <bean:message key="indice.usuarios.sistema.nuevo.usuario.observatorio" /></html:link>
                            </p>
                        </div>
                    </logic:empty>
                    <logic:notEmpty name="<%=Constants.CARGAR_USUARIOS_SISTEMA_FORM %>" property="listadoUsuarios">
                        <p class="pull-right">
                            <html:link forward="newSystemUser" styleClass="btn btn-default btn-lg"  paramId="<%=Constants.ROLE_TYPE %>" paramName="idSystemRole" titleKey="indice.usuarios.sistema.nuevo.usuario.alt"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <bean:message key="indice.usuarios.sistema.nuevo.usuario" /></html:link>
                            <html:link forward="newClientUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idClientRole" styleClass="btn btn-default btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <bean:message key="indice.usuarios.sistema.nuevo.usuario.cliente" /></html:link>
                            <html:link forward="newObservatoryUser" paramId="<%=Constants.ROLE_TYPE %>" paramName="idObservatoryRole" styleClass="btn btn-default btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <bean:message key="indice.usuarios.sistema.nuevo.usuario.observatorio" /></html:link>
                        </p>
                        <div class="pag">
                            <table class="table table-stripped table-bordered table-hover">
                                <caption><bean:message key="indice.usuarios.sistema.lista.usuarios" /></caption>
                                <tr>
                                    <th><bean:message key="indice.usuarios.sistema.usuario" /></th>
                                    <th><bean:message key="indice.usuarios.sistema.cartucho" /></th>
                                    <th><bean:message key="indice.usuarios.sistema.tipo" /></th>
                                    <th>Eliminar</th>
                                </tr>
                                <logic:iterate name="CargarUsuariosSistemaForm" type="es.inteco.rastreador2.utils.ListadoUsuario" property="listadoUsuarios" id="elemento">
                                    <tr>
                                        <td>
                                            <jsp:useBean id="paramsEU" class="java.util.HashMap" />
                                            <c:set target="${paramsEU}" property="user" value="${elemento.id_usuario}" />

                                            <logic:equal value="<%=idSystemRole %>" name="elemento" property="tipoRol">
                                                <html:link forward="editSystemUser" name="paramsEU"><bean:write name="elemento" property="usuario" /></html:link>
                                            </logic:equal>
                                            <logic:equal value="<%=idClientRole %>" name="elemento" property="tipoRol">
                                                <html:link forward="editClientUser" name="paramsEU"><bean:write name="elemento" property="usuario" /></html:link>
                                            </logic:equal>
                                            <logic:equal value="<%=idObservatoryRole %>" name="elemento" property="tipoRol">
                                                <html:link forward="editObservatoryUser" name="paramsEU"><bean:write name="elemento" property="usuario" /></html:link>
                                            </logic:equal>

                                            <span class="glyphicon glyphicon-edit pull-right edit-mark" aria-hidden="true"/>
                                        </td>
                                        <td>
                                            <logic:notEmpty name="elemento" property="cartucho">
                                                <ul>
                                                    <logic:iterate id="cartuchoL" name="elemento" property="cartucho">
                                                        <li><bean:write name="cartuchoL" /></li>
                                                    </logic:iterate>
                                                </ul>
                                            </logic:notEmpty>
                                        </td>
                                        <td>
                                            <logic:notEmpty name="elemento" property="tipo">
                                                <ul>
                                                    <logic:iterate id="listaTipo" name="elemento" property="tipo">
                                                        <li><bean:write name="listaTipo" /></li>
                                                    </logic:iterate>
                                                </ul>
                                            </logic:notEmpty>
                                        </td>
                                        <td>
                                            <html:link forward="deleteSystemUser" paramId="<%= Constants.USER %>" paramName="elemento" paramProperty="id_usuario">
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip" title="Eliminar este usuario"/><span class="sr-only">Eliminar este usuario</span>
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