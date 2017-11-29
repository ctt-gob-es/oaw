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

            <div id="cajaformularios">
                <h2><bean:message key="migas.eliminar.categoria" /></h2>
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
    </div>
