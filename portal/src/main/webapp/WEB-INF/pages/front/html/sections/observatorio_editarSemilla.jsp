<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="SemillaObservatorioForm"/>

    <!-- observatorio_editarSemilla.jsp -->
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
                  <li class="active"><bean:message key="migas.editar.semillas.observatorio" /></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="editar.semillas.observatorio.title"/></h2>

                <jsp:include page="/common/crawler_messages.jsp" />
                <html:form action="/secure/SemillasObservatorio.do" method="post" styleClass="formulario" onsubmit="return validateSemillaObservatorioForm(this)">
                    <input type="hidden" name="<%= Constants.NOMBRE_ANTIGUO %>" value="<bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre_antiguo" />" />
                    <input type="hidden" name="<%= Constants.ACTION %>" id="<%= Constants.ACTION %>" value="<%= Constants.ACCION_MODIFICAR %>"/>
                    <input type="hidden" name="<%= Constants.ES_PRIMERA %>" id="<%= Constants.ES_PRIMERA %>" value="<%= Constants.CONF_SI %>"/>
                    <input type="hidden" name="<%= Constants.SEMILLA %>" id="<%= Constants.SEMILLA %>" value="<bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="id"/>"/>
                    <fieldset>
                        <legend>Editar semilla</legend>
                        <div class="formItem">
                            <label for="nombre"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="editar.semilla.observatorio.nombre" /></strong></label>
                            <html:text styleClass="texto" styleId="nombre" property="nombre" />
                        </div>
                        <div class="formItem">
                            <label for="activa"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="editar.semilla.observatorio.activa" /></strong></label>
                            <html:select property="activa" styleClass="textoSelect" styleId="activa">
                                <html:option value="true"><bean:message key="select.yes"/></html:option>
                                <html:option value="false"><bean:message key="select.no"/></html:option>
                            </html:select>
                        </div>
                        <div class="formItem">
                            <label for="dependencia"><strong class="labelVisu"><bean:message key="editar.semilla.observatorio.dependencia" /></strong></label>
                            <html:text styleClass="texto" styleId="dependencia" property="dependencia" />
                        </div>
                        <div class="formItem">
                            <label for="acronimo"><strong class="labelVisu"><bean:message key="editar.semilla.observatorio.acronimo" /></strong></label>
                            <html:text styleClass="texto" styleId="acronimo" property="acronimo" />
                        </div>
                        <div class="formItem">
                            <label for="categoria"><strong class="labelVisu"><bean:message key="nueva.semilla.webs.categoria" /></strong></label>
                            <html:select property="categoria.id" styleClass="textoSelect" styleId="categoria">
                                <html:option value=""> - <bean:message key="select.one.femenine"/> - </html:option>
                                <logic:iterate name="<%=Constants.SEED_CATEGORIES %>" id="category">
                                    <bean:define id="idCategory">
                                        <bean:write name="category" property="id"/>
                                    </bean:define>
                                    <html:option value="<%=idCategory %>"><bean:write name="category" property="name"/></html:option>
                                </logic:iterate>
                            </html:select>
                        </div>
                        <div class="formItem">
                            <p class="alert alert-info"><span class="glyphicon glyphicon-info-sign"></span> <em><bean:message key="nueva.semilla.webs.informacion"/> </em>: <bean:message key="nueva.semilla.webs.info" /></p>
                            <label for="listaUrlsString"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="editar.semilla.observatorio.url" /></strong></label>
                            <html:textarea rows="5" cols="50" styleId="listaUrlsString" property="listaUrlsString" />
                        </div>
                        <div class="formItem">
                            <label for="inDirectory"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="editar.semilla.observatorio.in.directory" /></strong></label>
                            <html:select property="inDirectory" styleClass="textoSelect" styleId="inDirectory">
                                <html:option value="true"><bean:message key="select.yes"/></html:option>
                                <html:option value="false"><bean:message key="select.no"/></html:option>
                            </html:select>
                        </div>
                        <div class="formButton">
                            <html:submit styleClass="btn btn-primary btn-lg"><bean:message key="boton.aceptar" /></html:submit>
                        </div>
                    </fieldset>
                </html:form>
                <p id="pCenter"><html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
