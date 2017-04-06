<%@ include file="/common/taglibs.jsp" %> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
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
                    <li class="active"><bean:message key="migas.error" /></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="pagina.error" /></h2>
                <div class="notaInformativaExito">
                    <p><bean:message key="mensaje.error.generico"/></p>
                    <p><html:link styleClass="btn btn-default btn-lg" forward="observatoryMenu"><bean:message key="boton.volver.inicio" /></html:link></p>
                </div>
            </div><!-- fin cajaformularios -->
        </div>

    </div>
