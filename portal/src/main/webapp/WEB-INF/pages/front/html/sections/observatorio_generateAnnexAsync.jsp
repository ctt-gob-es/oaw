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
                      <li><html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link></li>
                    </ol>
                </div>

                <div id="cajaformularios">

                    <h2><bean:message key="resultado.observatorio.rastreo.realizado.anexos" /></h2>

                    <div class="detail">
                        <p><strong class="labelVisu"><bean:message key="generar.informes.observatorio.tiempo_espera.annex"/></strong></p>
                        <p><strong class="labelVisu"><bean:message key="generar.informes.observatorio.aviso_correo" arg0='<%= request.getAttribute("EMAIL").toString() %>'/></strong></p>

                        <div class="formButton">
                            <html:link styleClass="btn btn-default btn-lg" forward="observatoryMenu" ><bean:message key="boton.volver"/></html:link>
                        </div>
                    </div>
                </div><!-- fin cajaformularios -->
            </div>
    </div>
