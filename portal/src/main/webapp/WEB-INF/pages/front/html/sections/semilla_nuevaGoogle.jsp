<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="NuevaSemillaGoogleForm"/>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">
            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                    <li class="active"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.semillas.resultados.google" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="nueva.semilla.google.title" /></h2>

                <p><bean:message key="leyenda.campo.obligatorio" /></p>

                <html:form styleClass="formulario" method="post" action="/secure/NuevaSemillaGoogle.do" onsubmit="return validateNuevaSemillaGoogleForm(this)">
                    <input type="hidden" name="control" id="control" value="si" />
                    <fieldset>
                        <jsp:include page="/common/crawler_messages.jsp" />
                        <div class="formItem">
                            <label for="query"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.google.query" /></strong></label>
                            <html:text styleClass="texto" styleId="query" property="query" />
                        </div>
                        <div class="formItem">
                            <label for="paginas"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.google.numero.paginas" /></strong></label>
                            <html:text styleClass="texto" styleId="paginas" property="paginas" />
                            <p class="notaInformativa"><bean:message key="nueva.semilla.google.info" /> </p>
                        </div>
                        <div class="formItem">
                            <label for="nombreSemilla"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.google.nombreSemilla" /></strong></label>
                            <html:text styleClass="texto" styleId="nombreSemilla" property="nombreSemilla" />
                        </div>
                        <div class="formButton">
                            <html:hidden property="<%= Constants.BOTON_SEMILLA_GOOGLE %>" value="boton"/>
                            <html:submit styleClass="btn btn-primary btn-lg"><bean:message key="boton.aceptar" /></html:submit>
                            <html:cancel styleClass="btn btn-default btn-lg"><bean:message key="boton.volver"/></html:cancel>
                        </div>
                    </fieldset>
                </html:form>
            </div><!-- fin cajaformularios -->
        </div>
    </div>

