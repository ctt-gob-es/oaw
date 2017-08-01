<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<bean:parameter id="id" name="<%=Constants.ID %>"/>
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="idObservatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="observatoryType" name="<%=Constants.TYPE_OBSERVATORY %>"/>
	
	<bean:define id="grParam" ><%= Constants.GRAPHIC %></bean:define>
	<bean:define id="grValue" ><%= Constants.OBSERVATORY_GRAPHIC_INITIAL %></bean:define>
	<bean:define id="grRegenerate" ><%= Constants.OBSERVATORY_GRAPHIC_GLOBAL %></bean:define>
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${id}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
	<c:set target="${params}" property="${grParam}" value="${grValue}" />
	<c:set target="${params}" property="Otype" value="${observatoryType}" />
						
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
                  <li><html:link forward="getObservatoryGraphic" name="params"><bean:message key="migas.indice.observatorios.menu.graficas"/></html:link></li>
                  <li class="active"><bean:message key="migas.indice.observatorios.menu.graficas.global"/></li>
                </ol>
            </div>

            	<!-- <div id="migas">
            		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
            		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> /
            		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> /
            		<html:link forward="getFulfilledObservatories" name="params"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
            		<html:link forward="getObservatoryGraphic" name="params"><bean:message key="migas.indice.observatorios.menu.graficas"/></html:link> /
            		<bean:message key="migas.indice.observatorios.menu.graficas.global"/>
            		</p>
            	</div> -->

            <div id="cajaformularios">
                <h2><bean:message key="indice.observatorios.menu.graficas.global" /></h2>
                <jsp:include page="/common/crawler_messages.jsp" />
                <logic:equal name="<%= Constants.OBSERVATORY_RESULTS %>" value="<%= Constants.SI %>">
                    <h3><bean:message key="resultados.anonimos.nivel.accesibilidad.global.title" /></h3>
                    <div class="graphicInfo2">
                        <%--<strong><bean:message key="resultados.anonimos.num.portales"/></strong>
                         <ol>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG %>">
                                <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                            </logic:iterate>
                        </ol>--%>
                        <table>
                            <tr>
                                <th><bean:message key="resultados.anonimos.level"/></th>
                                <th><bean:message key="resultados.anonimos.porc.portales"/></th>
                                <th><bean:message key="resultados.anonimos.num.portales"/></th>
                            </tr>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG %>">
                                <tr>
                                    <td><bean:write name="item" property="adecuationLevel" /></td>
                                    <td><bean:write name="item" property="percentageP"/></td>
                                    <td><bean:write name="item" property="numberP"/></td>
                                </tr>
                            </logic:iterate>
                        </table>
                    </div>
                    <div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_ALLOCATION %>"></img></div>

                    <h3><bean:message key="resultados.anonimos.nivel.adecuacion.segmento.title" /></h3>
                    <div class="graphicInfo2">
                        <logic:iterate name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS %>" id="item">
                            <bean:define id="categoryForm" type="es.inteco.rastreador2.actionform.semillas.CategoriaForm" name="item" property="category" />
                            <table>
                                <caption><bean:write name="categoryForm" property="name"/></caption>
                                <tr>
                                    <th><bean:message key="resultados.anonimos.level"/></th>
                                    <th><bean:message key="resultados.anonimos.porc.portales"/></th>
                                </tr>
                                <logic:iterate id="item2" name="item" property="viewList">
                                    <tr>
                                        <td><bean:write name="item2" property="label" /></td>
                                        <td><bean:write name="item2" property="value"/>%</td>
                                    </tr>
                                </logic:iterate>
                            </table>
                        </logic:iterate>

                        <%-- <ol>
                            <strong><bean:message key="resultados.anonimos.porc.portales" /></strong>
                            <li><strong><bean:message key="resultados.anonimos.segmentoI"/></strong>
                            <div class="graphicInfo">
                                <ol>
                                    <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS1 %>">
                                        <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                                    </logic:iterate>
                                </ol>
                            </div></li>

                            <li><strong><bean:message key="resultados.anonimos.segmentoII"/></strong>
                            <div class="graphicInfo">
                                <ol>
                                    <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS2 %>">
                                        <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                                    </logic:iterate>
                                </ol>
                            </div></li>

                            <li><strong><bean:message key="resultados.anonimos.segmentoIII"/></strong>
                            <div class="graphicInfo">
                                <ol>
                                    <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS3 %>">
                                        <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                                    </logic:iterate>
                                </ol>
                            </div></li>
                        </ol> --%>
                    </div>
                    <bean:define id="numCPS" name="<%= Constants.OBSERVATORY_NUM_CPS_GRAPH %>" type="java.lang.Integer"/>
                    <c:forEach var="count" begin="1" end="${numCPS}">
                        <bean:define id="countBean"><c:out value="${count}" /></bean:define>
                        <div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_SEGMENTS_MARK %>&amp;<%= Constants.OBSERVATORY_NUM_GRAPH %>=<%= countBean %>"></img></div>
                    </c:forEach>

                    <h3><bean:message key="resultados.anonimos.comparacion.puntuacion.segmento.title" /></h3>
                    <div class="graphicInfo2">
                        <logic:iterate name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS %>" id="item">
                            <bean:define id="categoryForm" type="es.inteco.rastreador2.actionform.semillas.CategoriaForm" name="item" property="category" />
                            <table>
                                <caption><bean:write name="categoryForm" property="name"/></caption>
                                <tr>
                                    <th><bean:message key="resultados.anonimos.level"/></th>
                                    <th><bean:message key="resultados.anonimos.punt.portales"/></th>
                                </tr>
                                <logic:iterate id="item2" name="item" property="viewList">
                                    <tr>
                                        <td><bean:write name="item2" property="label" /></td>
                                        <td><bean:write name="item2" property="value"/></td>
                                    </tr>
                                </logic:iterate>
                            </table>
                        </logic:iterate>

                        <%--<strong><bean:message key="resultados.anonimos.punt.portales" /></strong>
                        <ol>
                            <li><strong><bean:message key="resultados.anonimos.segmentoI"/></strong>
                            <div class="graphicInfo">
                                <ol>
                                    <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS1 %>">
                                        <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                                    </logic:iterate>
                                </ol>
                            </div></li>

                            <li><strong><bean:message key="resultados.anonimos.segmentoII"/></strong>
                            <div class="graphicInfo">
                                <ol>
                                    <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS2 %>">
                                        <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                                    </logic:iterate>
                                </ol>
                            </div></li>

                            <li><strong><bean:message key="resultados.anonimos.segmentoIII"/></strong>
                            <div class="graphicInfo">
                                <ol>
                                    <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS3 %>">
                                        <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                                    </logic:iterate>
                                </ol>
                            </div></li>
                        </ol> --%>
                    </div>
                    <bean:define id="numCAS" name="<%= Constants.OBSERVATORY_NUM_CAS_GRAPH %>" type="java.lang.Integer"/>
                    <c:forEach var="count" begin="1" end="${numCAS}">
                        <bean:define id="countBean"><c:out value="${count}" /></bean:define>
                        <div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_GROUP_SEGMENT_MARK %>&amp;<%= Constants.OBSERVATORY_NUM_GRAPH %>=<%= countBean %>"></img></div>
                    </c:forEach>

                    <div class="graphicInfo2">
                        <h3><bean:message key="resultados.anonimos.comparacion.medias.aspecto.title" /></h3>
                        <table>
                            <tr>
                                <th><bean:message key="resultados.anonimos.aspect"/></th>
                                <th><bean:message key="resultados.anonimos.punt.media"/></th>
                            </tr>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA %>">
                                <tr>
                                    <td><bean:write name="item" property="label" /></td>
                                    <td><bean:write name="item" property="value"/></td>
                                </tr>
                            </logic:iterate>
                        </table>
                    <%-- %>
                        <strong><bean:message key="resultados.anonimos.punt.media" /></strong>
                        <ol>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA %>">
                                <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                            </logic:iterate>
                        </ol> --%>
                    </div>
                    <div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MID_ASPECT %>"></img></div>

                    <h3><bean:message key="resultados.anonimos.comparacion.medias.verificacion.I.title" /></h3>
                    <div class="graphicInfo2">
                        <table>
                            <tr>
                                <th><bean:message key="resultados.anonimos.verification"/></th>
                                <th><bean:message key="resultados.anonimos.punt.media"/></th>
                            </tr>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI %>">
                                <tr>
                                    <td><bean:write name="item" property="label" /></td>
                                    <td><bean:write name="item" property="value"/></td>
                                </tr>
                            </logic:iterate>
                        </table>
                        <%-- %><strong><bean:message key="resultados.anonimos.punt.media" /></strong>
                        <ol>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI %>">
                                <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                            </logic:iterate>
                        </ol> --%>
                    </div>
                    <div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N1 %>"></img></div>

                    <h3><bean:message key="resultados.anonimos.comparacion.medias.verificacion.II.title" /></h3>
                    <div class="graphicInfo2">
                    <table>
                            <tr>
                                <th><bean:message key="resultados.anonimos.verification"/></th>
                                <th><bean:message key="resultados.anonimos.punt.media"/></th>
                            </tr>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII %>">
                                <tr>
                                    <td><bean:write name="item" property="label" /></td>
                                    <td><bean:write name="item" property="value"/></td>
                                </tr>
                            </logic:iterate>
                        </table>
                        <%-- %><strong><bean:message key="resultados.anonimos.punt.media" /></strong>
                        <ol>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII %>">
                                <li><bean:write name="item" property="label" /> : <bean:write name="item" property="value"/></li>
                            </logic:iterate>
                        </ol> --%>
                    </div>
                    <div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N2 %>"></img></div>

                    <h3><bean:message key="observatory.graphic.modality.by.verification.level.1.title" /></h3>
                    <div class="graphicInfo2">
                        <table>
                            <tr>
                                <th><bean:message key="observatory.graphic.verification"/></th>
                                <th><bean:message key="observatory.graphic.modality.green"/></th>
                                <th><bean:message key="observatory.graphic.modality.red"/></th>
                            </tr>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I %>" type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
                                <tr>
                                    <td><bean:message key="<%=item.getVerification() %>" /></td>
                                    <td><%=item.getGreenPercentage() %> </td>
                                    <td><%=item.getRedPercentage() %> </td>
                                </tr>
                            </logic:iterate>
                        </table>
                        <%--<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I %>" type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
                            <strong><bean:message key="<%=item.getVerification() %>" /></strong>
                            <div class="graphicInfo">
                                <ol>
                                    <li><bean:message key="observatory.graphic.modality.green"/>: <%=item.getGreenPercentage() %> %</li>
                                    <li><bean:message key="observatory.graphic.modality.red"/>: <%=item.getRedPercentage() %> %</li>
                                </ol>
                            </div>
                        </logic:iterate> --%>
                    </div>
                    <div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N1 %>"></img></div>

                    <h3><bean:message key="observatory.graphic.modality.by.verification.level.2.title" /></h3>
                    <div class="graphicInfo2">
                        <table>
                            <tr>
                                <th><bean:message key="observatory.graphic.verification"/></th>
                                <th><bean:message key="observatory.graphic.modality.green"/></th>
                                <th><bean:message key="observatory.graphic.modality.red"/></th>
                            </tr>
                            <logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II %>" type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
                                <tr>
                                    <td><bean:message key="<%=item.getVerification() %>" /></td>
                                    <td><%=item.getGreenPercentage() %> </td>
                                    <td><%=item.getRedPercentage() %> </td>
                                </tr>
                            </logic:iterate>
                        </table>
                        <%--<logic:iterate id="item" name="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II %>" type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
                            <strong><bean:message key="<%=item.getVerification() %>" /></strong>
                            <div class="graphicInfo">
                                <ol>
                                    <li><bean:message key="observatory.graphic.modality.green"/>: <%=item.getGreenPercentage() %> %</li>
                                    <li><bean:message key="observatory.graphic.modality.red"/>: <%=item.getRedPercentage() %> %</li>
                                </ol>
                            </div>
                        </logic:iterate> --%>
                    </div>
                    <div class="divCenter"><img src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%= Constants.TYPE_OBSERVATORY %>=<%= observatoryType %>&amp;<%= Constants.ID %>=<%= id %>&amp;<%= Constants.ID_OBSERVATORIO %>=<%= id_observatorio %>&amp;<%= Constants.GRAPHIC %>=<%= Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N2 %>"></img></div>
                </logic:equal>
                <p id="pCenter">
                    <c:set target="${params}" property="${grParam}" value="${grRegenerate}" />
                    <html:link forward="regenerateGraphicIntav" name="params" styleClass="btn btn-primary btn-lg"> <bean:message key="boton.regenerar.resultados"/> </html:link>
                    <c:set target="${params}" property="${grParam}" value="${grValue}" />
                    <html:link forward="getObservatoryGraphic" name="params" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link>
                </p>
            </div><!-- fin cajaformularios -->
        </div> <!-- container dch -->
    </div> <!-- main -->
</inteco:sesion>
