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

	<bean:parameter name="<%=Constants.ID_EX_OBS %>" id="id"/>
	<bean:parameter name="<%=Constants.ID_OBSERVATORIO %>" id="id_observatorio"/>
	<bean:parameter name="<%=Constants.IS_PRIMARY %>" id="isPrimary"/>


    <!-- observatorio_confirmacionBorrarRealizado.jsp -->
    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                    <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                    <li>
                        <logic:equal name="isPrimary" value="true">
                            <html:link forward="resultadosPrimariosObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link>/
                        </logic:equal>
                        <logic:equal name="isPrimary" value="false">
                            <html:link forward="getFulfilledObservatories" paramId="<%=Constants.ID_OBSERVATORIO %>" paramName="<%=Constants.ID_OBSERVATORIO %>"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link>/
                        </logic:equal>
                    </li>
                    <li class="active"><bean:message key="migas.eliminar.observatorio" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="eliminar.observatorio.realizado.title" /></h2>

                <div>
                    <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.realizado" /></strong></p>
                    <p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.info" /></strong></p>
                </div>
                <div class="formButton">
                    <jsp:useBean id="params" class="java.util.HashMap"/>
                    <c:set target="${params}" property="id" value="${id}" />
                    <c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
                    <c:set target="${params}" property="isPrimary" value="${isPrimary}" />
                    <html:link styleClass="btn btn-primary btn-lg" forward="deleteFulfilledObservatory" name="params"><bean:message key="boton.aceptar"/></html:link>
                    <logic:equal name="isPrimary" value="true">
                        <html:link styleClass="btn btn-default btn-lg" forward="resultadosPrimariosObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="boton.cancelar"/></html:link>
                    </logic:equal>
                    <logic:equal name="isPrimary" value="false">
                        <html:link styleClass="btn btn-default btn-lg" forward="getFulfilledObservatories" paramId="<%=Constants.ID_OBSERVATORIO %>" paramName="<%=Constants.ID_OBSERVATORIO %>"><bean:message key="boton.cancelar"/></html:link>
                    </logic:equal>
                </div>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
