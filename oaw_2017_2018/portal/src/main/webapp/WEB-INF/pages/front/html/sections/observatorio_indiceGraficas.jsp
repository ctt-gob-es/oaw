<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">
	
	<bean:define id="idCartridgeMalware"><inteco:properties key="cartridge.malware.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeLenox"><inteco:properties key="cartridge.lenox.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeIntav"><inteco:properties key="cartridge.intav.id" file="crawler.properties" /></bean:define>
	<bean:define id="idCartridgeMultilanguage"><inteco:properties key="cartridge.multilanguage.id" file="crawler.properties" /></bean:define>

	<bean:parameter id="id" name="<%=Constants.ID %>"/>
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="idObservatorio" name="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:parameter id="observatoryType" name="<%= Constants.TYPE_OBSERVATORY %>" />
	
	<bean:define id="graphicParam" value="<%= Constants.GRAPHIC %>" />
	<bean:define id="global" value="<%= Constants.OBSERVATORY_GRAPHIC_GLOBAL %>" />
	<bean:define id="comparative" value="<%= Constants.OBSERVATORY_GRAPHIC_COMPARATIVE %>" />
	<bean:define id="categories" value="<%= Constants.OBSERVATORY_GRAPHIC_CATEGORIES %>" />
	<bean:define id="initial" ><%= Constants.OBSERVATORY_GRAPHIC_INITIAL %></bean:define>
	
	<bean:define id="forward" value="" />

		<bean:define id="forward" value="<%= Constants.OBSERVATORY_GRAPHIC_INTAV %>" />
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${id}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
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
                  <li class="active"><bean:message key="migas.indice.observatorios.menu.graficas"/></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="indice.rastreo.graficas" /></h2>

                <div class="graphicIcon1Row">
                    <c:set target="${params}" property="${graphicParam}" value="${categories}" />
                    <div class="graphicIcon">
                        <html:link forward="<%= forward %>" name="params">
                            <img src="../images/segmento1.gif" alt=""/>
                            <p><bean:message key="resultados.anonimos.img.categorias"/></p>
                        </html:link>
                    </div>
                    <c:set target="${params}" property="${graphicParam}" value="${global}" />
                    <div class="graphicIcon">
                        <html:link forward="<%= forward %>" name="params">
                            <img src="../images/resultados_globales.gif" alt=""/>
                            <p><bean:message key="resultados.anonimos.img.globales"/></p>
                        </html:link>
                    </div>
                    <c:set target="${params}" property="${graphicParam}" value="${comparative}" />
                    <div class="graphicIcon">
                        <html:link forward="<%= forward %>" name="params">
                            <img src="../images/evolucion.gif" alt=""/>
                            <p><bean:message key="resultados.anonimos.img.comparativa"/></p>
                        </html:link>
                    </div>
                    <div class="spacer"></div>
                </div>
                <p id="pCenter">
                    <html:link forward="resultadosPrimariosObservatorio" paramName="idObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link>
                </p>
            </div><!-- fin cajaformularios -->
        </div>
    </div>
</inteco:sesion>
