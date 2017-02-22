<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.ver.observatorio" /></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="ver.observatorio.title" /></h2>
                    <div class="detail">
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="ver.observatorio.nombre" />: </strong></label>
                            <p><bean:write name="VerObservatorioForm" property="nombre" /></p>
                        </div>

                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="ver.observatorio.tipo" />: </strong></label>
                            <p><bean:write name="VerObservatorioForm" property="tipo.name" /></p>
                        </div>

                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="ver.observatorio.activo" />: </strong></label>
                            <logic:equal name="VerObservatorioForm" property="activo" value="true">
                                <bean:message key="select.yes"/>
                            </logic:equal>
                            <logic:equal name="VerObservatorioForm" property="activo" value="false">
                                <bean:message key="select.no"/>
                            </logic:equal>
                        </div>

                        <logic:notEmpty name="VerObservatorioForm" property="categorias">
                            <div class="formItem">
                                <label><strong class="labelVisu"><bean:message key="ver.observatorio.categorias" />: </strong></label>
                                <div class="alinForm">
                                    <ul class="sublista">
                                        <logic:iterate id="categoria" name="VerObservatorioForm" property="categorias">
                                            <li><bean:write name="categoria" property="name"/></li>
                                        </logic:iterate>
                                    </ul>
                                </div>
                            </div>
                        </logic:notEmpty>

                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="ver.observatorio.cartucho" />: </strong></label>
                            <p><bean:write name="VerObservatorioForm" property="cartucho.name" /></p>
                        </div>

                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="ver.observatorio.periodicidad" />: </strong></label>
                            <p><bean:write name="VerObservatorioForm" property="periodicidad" /></p>
                        </div>

                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="ver.observatorio.profundidad" />: </strong></label>
                            <p><bean:write name="VerObservatorioForm" property="profundidad" /></p>
                        </div>

                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="ver.observatorio.amplitud" />: </strong></label>
                            <p><bean:write name="VerObservatorioForm" property="amplitud" /></p>
                        </div>

                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="ver.observatorio.pseudoaleatorio" />: </strong></label>
                            <logic:equal name="VerObservatorioForm" property="pseudoAleatorio" value="true">
                                <bean:message key="select.yes"/>
                            </logic:equal>
                            <logic:equal name="VerObservatorioForm" property="pseudoAleatorio" value="false">
                                <bean:message key="select.no"/>
                            </logic:equal>
                        </div>
                    </div>
                    <logic:notPresent name="VerObservatorioForm" property="semillasList">
                            <div class="notaInformativaExito">
                                <p><bean:message key="ver.observatorio.semillas.vacio"/></p>
                            </div>
                        </logic:notPresent>

                        <logic:present name="VerObservatorioForm" property="semillasList">
                            <logic:empty name="VerObservatorioForm" property="semillasList">
                                <div class="notaInformativaExito">
                                    <p><bean:message key="ver.observatorio.vacio"/></p>
                                </div>
                            </logic:empty>
                            <logic:notEmpty name="VerObservatorioForm" property="semillasList">
                                <div class="pag">
                                    <table>
                                        <tr>
                                            <th><bean:message key="ver.observatorio.semillas.nombre" /></th>
                                            <th><bean:message key="ver.observatorio.semillas.url" /></th>
                                        </tr>
                                        <logic:iterate name="VerObservatorioForm" property="semillasList" id="elemento">
                                            <tr>
                                                <td><bean:write name="elemento" property="nombre" /></td>
                                                <td><bean:write name="elemento" property="listaUrlsString" /></td>
                                            </tr>
                                        </logic:iterate>
                                    </table>
                                    <jsp:include page="pagination.jsp" />
                                </div>
                            </logic:notEmpty>
                        </logic:present>
                    <div id="pCenter">
                        <html:link styleClass="btn btn-default btn-lg" forward="observatoryMenu"><bean:message key="boton.volver" /></html:link>
                    </div>
            </div><!-- fin cajaformularios -->
        </div>
    </div>

