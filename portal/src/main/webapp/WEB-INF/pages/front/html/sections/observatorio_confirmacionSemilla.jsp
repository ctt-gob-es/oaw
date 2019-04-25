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
-->
<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>



<!-- observatorio_confirmacionSemilla -->
    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li><html:link forward="observatorySeed"><bean:message key="migas.semillas.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.eliminar.semillas.observatorio" /> </li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="confirmacion.eliminar.semilla.title" /></h2>
                    <div class="detail">
                        <logic:notEmpty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
                            <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.info" /></strong></p>
                            <ul class="lista_inicial">
                                <logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>" id="elemento">
                                    <li><bean:write name="elemento" property="nombre"/></li>
                                </logic:iterate>
                            </ul>
                            <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.info2" /></strong></p>
                        </logic:notEmpty>
                        <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.pregunta" /></strong></p>
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.nombre" />: </strong></label>
                            <p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre" /></p>
                        </div>
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.url" />: </strong></label>
                            <p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="listaUrlsString" /></p>
                        </div>
                        <div class="formButton">
                            <bean:define id="action"><%= Constants.ACTION%></bean:define>
                            <bean:define id="actionDel"><%= Constants.ACCION_BORRAR%></bean:define>
                            <bean:define id="confirmacion"><%= Constants.CONFIRMACION%></bean:define>
                            <bean:define id="confSi"><%= Constants.CONF_SI%></bean:define>
                            <bean:define id="confNo"><%= Constants.CONF_NO%></bean:define>
                            <bean:define id="semillaSTR"><%= Constants.SEMILLA%></bean:define>
                            <bean:define id="semillaId" name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="id"/>

                            <jsp:useBean id="paramsSI" class="java.util.HashMap"/>
                            <jsp:useBean id="paramsNO" class="java.util.HashMap"/>

                            <c:set target="${paramsSI}" property="${action}" value="${actionDel}" />
                            <c:set target="${paramsSI}" property="${confirmacion}" value="${confSi}" />
                            <c:set target="${paramsSI}" property="${semillaSTR}" value="${semillaId}" />

                            <c:set target="${paramsNO}" property="${action}" value="${actionDel}" />
                            <c:set target="${paramsNO}" property="${confirmacion}" value="${confNo}" />

                            <html:link styleClass="btn btn-primary btn-lg" forward="observatorySeeds" name="paramsSI"><bean:message key="boton.aceptar"/></html:link>
                            <html:link styleClass="btn btn-default btn-lg" forward="observatorySeeds" name="paramsNO"><bean:message key="boton.cancelar"/></html:link>
                        </div>
                    </div>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
