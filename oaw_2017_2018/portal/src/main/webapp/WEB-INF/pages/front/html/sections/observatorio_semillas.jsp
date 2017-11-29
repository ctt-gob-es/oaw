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
<html:xhtml/>
	
	<bean:define id="action" value="<%= Constants.ACTION %>" />
	<bean:define id="semillaSTR" value="<%= Constants.SEMILLA %>" />
	<bean:define id="actionSep" value="<%= Constants.ACCION_SEPARATE_SEED %>" />
	<bean:define id="actionAdd" value="<%= Constants.ACCION_ADD_SEED %>" />
	<bean:define id="esPrimera" value="<%= Constants.ES_PRIMERA %>" />
	<bean:define id="accionAceptar" value="<%= Constants.ACCION_ACEPTAR %>" />
	<jsp:useBean id="paramsV" class="java.util.HashMap" />
	<c:set target="${paramsV}" property="${action}" value="${accionAceptar}"/>
	<c:set target="${paramsV}" property="${esPrimera}" value="no"/>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">

                  <li><html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link></li>
                  <li><html:link forward="newObservatory" name="paramsV"><bean:message key="migas.nuevo.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.nuevo.observatorio.anadir.semillas" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="semillas.observatorio.title" /></h2>

                <!-- SEMILLAS ASOCIADAS AL RASTREO QUE ES POSIBLE DESVINCULAR DEL MISMO -->
                <div class="detail">
                    <logic:notPresent name="<%= Constants.ADD_OBSERVATORY_SEED_LIST %>">
                        <div class="notaInformativaExito">
                            <p><bean:message key="modificar.observatorio.semillas.anadidas.vacio"/></p>
                        </div>
                    </logic:notPresent>

                    <logic:present name="<%= Constants.ADD_OBSERVATORY_SEED_LIST %>">
                        <logic:empty name="<%= Constants.ADD_OBSERVATORY_SEED_LIST %>">
                            <div class="notaInformativaExito">
                                <p><bean:message key="modificar.observatorio.semillas.anadidas.vacio"/></p>
                            </div>
                        </logic:empty>
                        <logic:notEmpty name="<%= Constants.ADD_OBSERVATORY_SEED_LIST %>">
                            <div class="pag">
                                <table>
                                    <tr>
                                        <th class="fixWidth"><bean:message key="modificar.observatorio.semillas.operaciones" /></th>
                                        <th><bean:message key="modificar.observatorio.semillas.nombre" /></th>
                                    </tr>
                                    <bean:define id="resultFrom" name="<%= Constants.OBS_PAGINATION_RESULT_FROM %>" type="java.lang.Integer"/>
                                    <bean:define id="pagination" name="<%= Constants.OBS_PAGINATION %>" type="java.lang.Integer"/>
                                    <logic:iterate name="<%= Constants.ADD_OBSERVATORY_SEED_LIST %>" id="elemento" length="<%= pagination.toString() %>" offset="<%= resultFrom.toString() %>">
                                        <tr>
                                            <td>
                                                <jsp:useBean id="paramsD" class="java.util.HashMap" />
                                                <bean:define id="semillaId" name="elemento" property="id" />
                                                <c:set target="${paramsD}" property="${semillaSTR}" value="${semillaId}" />
                                                <c:set target="${paramsD}" property="${action}" value="${actionSep}"/>
                                                <c:set target="${paramsD}" property="${esPrimera}" value="no"/>
                                                <logic:notEqual name="<%= Constants.IS_UPDATE %>" value="<%= Constants.CONF_SI %>" scope="request">
                                                    <html:link forward="newObservatory" name="paramsD"><img src="../images/bt_eliminar.gif" alt="<bean:message key="eliminar.semilla.observatorio" />"/></html:link>
                                                </logic:notEqual>
                                                <logic:equal name="<%= Constants.IS_UPDATE %>" value="<%= Constants.CONF_SI %>" scope="request">
                                                    <html:link forward="editObservatory" name="paramsD"><img src="../images/bt_eliminar.gif" alt="<bean:message key="eliminar.semilla.observatorio" />"/></html:link>
                                                </logic:equal>
                                            </td>
                                            <td><bean:write name="elemento" property="nombre" /></td>
                                        </tr>
                                    </logic:iterate>
                                </table>
                                <jsp:include page="pagination.jsp" />
                            </div>
                        </logic:notEmpty>
                    </logic:present>
                </div>

                <!-- SEMILLAS NO ASOCIADAS AL RASTREO QUE ES POSIBLE ASOCIAR AL MISMO -->
                <div class="detail">
                    <logic:notPresent name="<%= Constants.OTHER_OBSERVATORY_SEED_LIST %>" >
                        <div class="notaInformativaExito">
                            <p><bean:message key="modificar.observatorio.semillas.no.anadidas.vacio"/></p>
                        </div>
                    </logic:notPresent>

                    <logic:present name="<%= Constants.OTHER_OBSERVATORY_SEED_LIST %>" >
                        <logic:empty name="<%= Constants.OTHER_OBSERVATORY_SEED_LIST %>" >
                            <div class="notaInformativaExito">
                                <p><bean:message key="modificar.observatorio.semillas.no.anadidas.vacio"/></p>
                            </div>
                        </logic:empty>
                        <logic:notEmpty name="<%= Constants.OTHER_OBSERVATORY_SEED_LIST %>" >
                            <div class="pag">
                                <table>
                                    <tr>
                                        <th class="fixWidth"></th>
                                        <th><bean:message key="modificar.observatorio.semillas.nombre" /></th>
                                    </tr>
                                    <bean:define id="resultFromNA" name="<%= Constants.OBS_PAGINATION_RESULTNA_FROM %>" type="java.lang.Integer"/>
                                    <bean:define id="pagination" name="<%= Constants.OBS_PAGINATION %>" type="java.lang.Integer"/>
                                    <logic:iterate name="<%= Constants.OTHER_OBSERVATORY_SEED_LIST %>" id="elemento" length="<%= pagination.toString() %>" offset="<%= resultFromNA.toString() %>">
                                        <tr>
                                            <td>
                                                <jsp:useBean id="params" class="java.util.HashMap" />
                                                <bean:define id="semillaId" name="elemento" property="id" />
                                                <c:set target="${params}" property="${semillaSTR}" value="${semillaId}" />
                                                <c:set target="${params}" property="${action}" value="${actionAdd}"/>
                                                <c:set target="${params}" property="${esPrimera}" value="no"/>
                                                <logic:notEqual name="<%= Constants.IS_UPDATE %>" value="<%= Constants.CONF_SI %>" scope="request">
                                                    <html:link forward="newObservatory" name="params"><img src="../images/bt_anadir.gif" alt="<bean:message key="anadir.semilla.observatorio" />"/></html:link>
                                                </logic:notEqual>
                                                <logic:equal name="<%= Constants.IS_UPDATE %>" value="<%= Constants.CONF_SI %>" scope="request">
                                                    <html:link forward="editObservatory" name="params"><img src="../images/bt_anadir.gif" alt="<bean:message key="anadir.semilla.observatorio" />"/></html:link>
                                                </logic:equal>
                                            </td>
                                            <td><bean:write name="elemento" property="nombre" /></td>
                                        </tr>
                                    </logic:iterate>
                                </table>
                                <bean:size id="linksSize" name="<%= Constants.LIST_PAGE_LINKS2 %>"/>
                                <logic:greaterThan value="1" name="linksSize">
                                    <p>
                                        <logic:iterate id="linkItem" name="<%= Constants.LIST_PAGE_LINKS2 %>">
                                        <logic:equal name="linkItem" property="active" value="true">
                                            <a href="<bean:write name="linkItem" property="path" />" class="<bean:write name="linkItem" property="styleClass" />"><bean:write name="linkItem" property="title" /></a>
                                            </logic:equal>
                                            <logic:equal name="linkItem" property="active" value="false">
                                            <span class="<bean:write name="linkItem" property="styleClass" />"><bean:write name="linkItem" property="title" /></span>
                                            </logic:equal>
                                        </logic:iterate>
                                    </p>
                                </logic:greaterThan>
                            </div>
                        </logic:notEmpty>
                    </logic:present>
                </div>
                <p id="pCenter">
                    <logic:notEqual name="<%= Constants.IS_UPDATE %>" value="<%= Constants.CONF_SI %>" scope="request">
                        <html:link styleClass="boton" forward="newObservatory" name="paramsV"><bean:message key="boton.aceptar" /></html:link>
                    </logic:notEqual>
                    <logic:equal name="<%= Constants.IS_UPDATE %>" value="<%= Constants.CONF_SI %>" scope="request">
                        <html:link styleClass="boton" forward="editObservatory" name="paramsV"><bean:message key="boton.aceptar" /></html:link>
                    </logic:equal>
                </p>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
