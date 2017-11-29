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

	<bean:parameter name="<%=Constants.ID_OBSERVATORIO %>" id="idObservatorio"/>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li><html:link forward="resultadosPrimariosObservatorio" paramName="idObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link></li>
                  <li class="active"><bean:message key="migas.exportar" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="exportar.resultados.observatorio.title" /></h2>

                <p><strong class="labelVisu"><bean:message key="confirmacion.exportar.resultados.observatorio.pregunta" /></strong></p>
                <p><strong class="labelVisu"><bean:message key="confirmacion.exportar.resultados.observatorio.info" /></strong></p>
                <div class="formItem">
                    <label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.nombre" />: </strong></label>
                    <p><bean:write name="<%= Constants.OBSERVATORY_FORM %>" property="nombre" /></p>
                </div>
                <div class="formButton">
                    <jsp:useBean id="params" class="java.util.HashMap" />
                    <bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
                    <bean:parameter id="idExObs" name="<%= Constants.ID_EX_OBS %>" />
                    <c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
                    <c:set target="${params}" property="idExObs" value="${idExObs}" />
                    <html:link styleClass="btn btn-primary btn-lg" forward="databaseExportActionExport" name="params"><bean:message key="boton.aceptar"/></html:link>
                    <html:link styleClass="btn btn-default btn-lg" forward="observatoryMenu" ><bean:message key="boton.cancelar"/></html:link>
                </div>

            </div><!-- fin cajaformularios -->
        </div>
    </div>