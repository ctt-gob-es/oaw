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
	<bean:parameter name="<%=Constants.ID_CARTUCHO %>" id="idCartucho"/>
	<bean:parameter name="<%=Constants.ID_EX_OBS%>" id="idExObs"/>
	<bean:define id="idObservatorioSTR" value="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:define id="idExObsSTR" value="<%=Constants.ID_EX_OBS %>"/>
	<bean:define id="idCartuchoSTR" value="<%=Constants.ID_CARTUCHO %>"/>
	<bean:define id="semillaSTR"><%= Constants.ID_SEMILLA%></bean:define>
	<bean:define id="semillaId" name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="id"/>
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="${idObservatorioSTR}" value="${idObservatorio}"/>
	<c:set target="${params}" property="${idExObsSTR}" value="${idExObs}"/>
	<c:set target="${params}" property="${idCartuchoSTR}" value="${idCartucho}"/>
	<c:set target="${params}" property="${semillaSTR}" value="${semillaId}" />

    <!-- observatorio_confirmacionLanzarSemilla.jsp -->
    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                    <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                    <li><html:link forward="resultadosPrimariosObservatorio" paramName="id_observatorio" paramId="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link></li>
                    <li><html:link forward="resultadosObservatorioSemillas" name="params"><bean:message key="migas.resultado.observatorio" /></html:link></li>
                    <li class="active"><bean:message key="migas.resultado.observatorio.confirmacion.lanzar.semilla" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="resultados.observatorio.lanzar.semilla.confirmacion" /></h2>
                    <div>
                        <p><strong class="labelVisu"><bean:message key="resultados.observatorio.lanzar.semilla.conf" /></strong></p>
                        <p><strong class="labelVisu"><bean:message key="resultados.observatorio.lanzar.semilla.info" /></strong></p>
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.observatorio.nombre" />: </strong></label>
                            <p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre" /></p>
                        </div>
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.observatorio.url" />: </strong></label>
                            <p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="listaUrlsString" /></p>
                        </div>
                    </div>
                    <div class="formButton">
                        <bean:define id="confirmacion"><%= Constants.CONFIRMACION%></bean:define>
                        <bean:define id="confSi"><%= Constants.CONF_SI%></bean:define>
                        <bean:define id="confNo"><%= Constants.CONF_NO%></bean:define>
                        <c:set target="${params}" property="${confirmacion}" value="${confSi}" />
                        <html:link styleClass="btn btn-primary btn-lg" forward="resultadosObservatorioLanzarEjecucion" name="params"><bean:message key="boton.aceptar"/></html:link>
                        <c:set target="${params}" property="${confirmacion}" value="${confNo}" />
                        <html:link styleClass="btn btn-default btn-lg" forward="resultadosObservatorioLanzarEjecucion" name="params"><bean:message key="boton.cancelar"/></html:link>
                    </div>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
