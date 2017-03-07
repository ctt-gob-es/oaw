<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="ModificarObservatorioForm"/>


    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.modificar.observatorio" /></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="modificar.observatorio.title" /></h2>

                <p><bean:message key="leyenda.campo.obligatorio" /></p>

                <html:form method="post" styleClass="formulario" action="/secure/ModificarObservatorio.do" onsubmit="return validateModificarObservatorioForm(this)">
                    <input type="hidden" name="<%= Constants.NOMBRE_ANTIGUO %>"  value="<bean:write name="ModificarObservatorioForm" property="nombre_antiguo" />" />
                    <input type="hidden" name="<%= Constants.OBSERVATORY_ID %>"  value="<bean:write name="ModificarObservatorioForm" property="id_observatorio" />" />
                    <input type="hidden" name="esPrimera" id="esPrimera" value="no" />
                    <input type="hidden" name="<%= Constants.ACTION %>"  value="<%= Constants.ACCION_MODIFICAR %>" />
                    <html:hidden name="ModificarObservatorioForm" property="normaAnalisis"/>
                    <fieldset>
                        <jsp:include page="/common/crawler_messages.jsp" />

                        <div class="formItem">
                            <label for="nombre"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.nombre" />: </strong></label>
                            <html:text styleClass="texto" name="ModificarObservatorioForm" property="nombre" styleId="nombre" maxlength="100"/>
                        </div>

                        <div class="formItem">
                            <label for="tipo"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.tipo" /></strong></label>
                            <html:select name="ModificarObservatorioForm" styleClass="textoSelect" styleId="cartucho" property="tipo.id" >
                                <logic:iterate name="<%=Constants.TIPOS_VECTOR %>" id="tipo">
                                    <bean:define id="idType"><bean:write name="tipo" property="id"/></bean:define>
                                    <html:option value="<%=idType %>" ><bean:write name="tipo" property="name"/></html:option>
                                </logic:iterate>
                            </html:select>
                        </div>

                        <div class="formItem">
                            <label for="activo"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.activo" /></strong></label>
                            <html:select styleClass="textoSelect" styleId="activo"  property="activo" >
                                <html:option value="true"><bean:message key="select.yes"/></html:option>
                                <html:option value="false"><bean:message key="select.no"/></html:option>
                            </html:select>
                        </div>

                        <div class="formItem">
                            <label for="cartucho"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.cartucho" /></strong></label>
                            <html:select name="ModificarObservatorioForm" styleClass="textoSelect" styleId="cartucho" property="cartucho.id" >
                                <logic:iterate name="<%=Constants.CARTUCHOS_VECTOR %>" id="cartucho">
                                    <bean:define id="idCartucho"><bean:write name="cartucho" property="id"/></bean:define>
                                    <html:option value="<%=idCartucho %>" ><bean:write name="cartucho" property="name"/></html:option>
                                </logic:iterate>
                            </html:select>
                        </div>

                        <div class="formItem">
                            <label for="categoria"><strong class="labelVisu"><bean:message key="nuevo.observatorio.categoria" />: </strong></label>
                            <html:select size="5" multiple="true" name="ModificarObservatorioForm" property="categoria" styleClass="textoSelect" styleId="categoria">
                                <logic:iterate name="<%=Constants.SEED_CATEGORIES %>" id="category">
                                    <bean:define id="idCategory">
                                        <bean:write name="category" property="id"/>
                                    </bean:define>
                                    <html:option value="<%=idCategory %>"><bean:write name="category" property="name"/></html:option>
                                </logic:iterate>
                            </html:select>
                        </div>

                        <div class="formItem">
                            <label for="lenguaje"><strong class="labelVisu"><bean:message key="modificar.observatorio.lenguaje" /></strong></label>
                            <html:select styleClass="textoSelect" styleId="lenguaje" property="lenguaje" >
                                <logic:iterate name="ModificarObservatorioForm" property="lenguajeVector" id="lenguaje">
                                    <bean:define id="idLang"><bean:write name="lenguaje" property="id"/></bean:define>
                                    <html:option value="<%=idLang %>"><bean:write name="lenguaje" property="name"/></html:option>
                                </logic:iterate>
                            </html:select>
                        </div>

                        <div class="formItem">
                            <label for="periodicidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.periodicidad" />: </strong></label>
                            <html:select styleClass="textoSelect" styleId="periodicidad"  property="periodicidad" >
                                <logic:iterate name="ModificarObservatorioForm" property="periodicidadVector" id="periodicidads">
                                    <c:if test="${ModificarObservatorioForm.periodicidad==periodicidads.id}">
                                        <option selected value="<bean:write name="periodicidads" property="id"/>"> <bean:write name="periodicidads" property="nombre"/></option>
                                    </c:if>
                                    <c:if test="${ModificarObservatorioForm.periodicidad!=periodicidads.id}">
                                        <option value="<bean:write name="periodicidads" property="id"/>"> <bean:write name="periodicidads" property="nombre"/></option>
                                    </c:if>
                                </logic:iterate>
                            </html:select>
                        </div>

                        <div class="formItem">
                            <label for="profundidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.profundidad" />: </strong></label>
                            <bean:define id="maxProfundidad">
                                <inteco:properties key="profundidadMax.rastreo" file="crawler.properties" />
                            </bean:define>
                            <html:select styleClass="textoSelect" styleId="profundidad"  property="profundidad" >
                                <c:forEach begin="1" end="${maxProfundidad}" varStatus="status">
                                    <c:if test="${ModificarObservatorioForm.profundidad==status.count}">
                                        <option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
                                    </c:if>
                                    <c:if test="${ModificarObservatorioForm.profundidad!=status.count}">
                                        <option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
                                    </c:if>
                                </c:forEach>
                            </html:select>
                        </div>

                        <div class="formItem">
                            <label for="amplitud"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.amplitud" />: </strong></label>
                            <bean:define id="maxAmplitud">
                                <inteco:properties key="pagPorNivelMax.rastreo" file="crawler.properties" />
                            </bean:define>
                            <html:select styleClass="textoSelect" styleId="amplitud"  property="amplitud" >
                                <c:forEach begin="1" end="${maxAmplitud}" varStatus="status">
                                    <c:if test="${ModificarObservatorioForm.amplitud==status.count}">
                                        <option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
                                    </c:if>
                                    <c:if test="${ModificarObservatorioForm.amplitud!=status.count}">
                                        <option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
                                    </c:if>
                                </c:forEach>
                                <option value="<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />"><bean:message key="nuevo.rastreo.amplitud.ilimitada" /></option>
                            </html:select>
                        </div>

                        <div class="formItem">
                            <label for="pseudoAleatorio"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.pseudoaleatorio" /></strong></label>
                            <html:select name="ModificarObservatorioForm" styleClass="textoSelect" styleId="pseudoAleatorio"  property="pseudoAleatorio" >
                                <html:option value="true"><bean:message key="select.yes"/></html:option>
                                <html:option value="false"><bean:message key="select.no"/></html:option>
                            </html:select>
                        </div>

                        <fieldset class="innerFieldset">
                            <legend><bean:message key="modificar.observatorio.fecha.legend"/></legend>
                            <div class="formItem">
                                <label for="fechaInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.fechaInicio" />: </strong></label>
                                <html:text styleClass="textoCorto" name="ModificarObservatorioForm" property="fechaInicio" styleId="fechaInicio" onkeyup="escBarra(event, document.forms['ModificarCuentaUsuarioForm'].elements['fechaInicio'], 1)" maxlength="10"/>
                                <span id="calendar">
                                    <img src="../images/boton-calendario.gif" onclick="popUpCalendar(this, document.forms['ModificarObservatorioForm'].elements['fechaInicio'], 'dd/mm/yyyy')" alt="<bean:message key="img.calendario.alt" />"/>
                                </span>
                                <bean:message key="date.format"/>
                            </div>

                            <div class="formItem">
                                <label for="horaInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.horaInicio" />: </strong></label>
                                <html:select styleClass="textoSelectCorto" styleId="horaInicio" name="ModificarObservatorioForm" property="horaInicio">
                                    <html:options name="<%=Constants.HOURS %>"/>
                                </html:select>

                                <label for="minutoInicio" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.observatorio.minutoInicio" />: </strong></label>
                                <html:select styleClass="textoSelectCorto" styleId="minutoInicio" name="ModificarObservatorioForm" property="minutoInicio" >
                                    <html:options name="<%=Constants.MINUTES %>"/>
                                </html:select>
                            </div>
                        </fieldset>
                        <fieldset>
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
                                                    <th><bean:message key="nuevo.observatorio.semillas.asociadas.nombre" /></th>
                                                </tr>
                                                <bean:define id="resultFrom" name="<%= Constants.OBS_PAGINATION_RESULT_FROM %>" type="java.lang.Integer"/>
                                                <bean:define id="pagination" name="<%= Constants.OBS_PAGINATION %>" type="java.lang.Integer"/>
                                                <logic:iterate name="<%= Constants.ADD_OBSERVATORY_SEED_LIST %>" id="elemento" length="<%= pagination.toString() %>" offset="<%= resultFrom.toString() %>">
                                                    <tr>
                                                        <td><bean:write name="elemento" property="nombre" /></td>
                                                    </tr>
                                                </logic:iterate>
                                            </table>
                                            <jsp:include page="pagination.jsp" />
                                        </div>
                                    </logic:notEmpty>
                                </logic:present>
                            </div>
                        </fieldset>
                        <div class="formButton">
                            <html:submit property="buttonAction" styleClass="btn btn-default btn-lg pull-left"><bean:message key="boton.aceptar.anadir.semilla" /></html:submit>
                            <html:submit property="buttonAction" styleClass="btn btn-primary btn-lg"><bean:message key="boton.aceptar" /></html:submit>
                            <html:cancel styleClass="btn btn-default btn-lg"><bean:message key="boton.cancelar" /></html:cancel>
                        </div>
                    </fieldset>
                </html:form>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
