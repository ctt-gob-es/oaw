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

    <!-- observatorio_verCategoriaSemilla.jsp -->
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
                  <li class="active"><bean:message key="migas.categoria.semillas"/></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="categoria.semillas.titulo" /></h2>

                <html:form action="/secure/ViewSeedCategoriesAction.do" method="get" styleClass="formulario">
                    <input type="hidden" name="<%= Constants.ACTION %>" value="<%= Constants.VIEW_SEED_CATEGORY %>"/>
                    <bean:define id="categoryId" name="<%=Constants.CATEGORIA_FORM %>" property="id" />
                    <input type="hidden" name="<%= Constants.ID_CATEGORIA %>" value="<%= categoryId %>"/>
                    <fieldset>
                        <jsp:include page="/common/crawler_messages.jsp" />
                        <div class="formItem">
                            <label for="nombre"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.nombre" /></strong></label>
                            <html:text styleClass="texto" styleId="nombre" property="nombre" />
                        </div>
                        <div class="formButton">
                            <html:submit styleClass="btn btn-default btn-lg"><bean:message key="boton.aceptar" /></html:submit>
                        </div>
                    </fieldset>
                </html:form>

                <div class="detail">
                    <div class="formItem">
                        <strong class="labelVisu"><bean:message key="categoria.semillas.nombre" />: </strong>
                        <bean:write name="<%=Constants.CATEGORIA_FORM %>" property="name" />
                    </div>

                    <logic:empty name="<%=Constants.CATEGORIA_FORM %>" property="seeds">
                        <p><bean:message key="categoria.semillas.asociadas.vacio"/></p>
                    </logic:empty>
                    <logic:notEmpty name="<%=Constants.CATEGORIA_FORM %>" property="seeds">
                        <div class="pag">
                            <table>
                                <caption><bean:message key="categoria.semillas.lista.semillas"/></caption>
                                <tr>
                                    <th><bean:message key="categoria.semillas.nombre"/></th>
                                </tr>
                                <logic:iterate name="<%=Constants.CATEGORIA_FORM %>" property="seeds" id="seed">
                                    <tr>
                                        <td>
                                            <bean:write name="seed" property="nombre"/>
                                            <logic:iterate name="seed" property="listaUrls" id="url" length="1">
                                                <bean:define id="altLink">
                                                    <bean:message key="categoria.semillas.enlace.externo">
                                                        <jsp:attribute name="arg0">
                                                            <bean:write name="seed" property="nombre"/>
                                                        </jsp:attribute>
                                                    </bean:message>
                                                </bean:define>

                                                <a class="pull-right" href="<bean:write name="url"/>" data-toggle="tooltip" title="<%=altLink %>">
                                                    <span class="glyphicon glyphicon-new-window" aria-hidden="true"></span>
                                                    <span class="sr-only"><%=altLink %></span>
                                                </a>
                                            </logic:iterate>
                                        </td>
                                    </tr>
                                </logic:iterate>
                            </table>
                            <jsp:include page="pagination.jsp" />
                        </logic:notEmpty>
                        <p id="pCenter"><html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
                    </div>
                </div>
            </div>
        </div>
    </div>

