<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	
    <!-- servicioDiagnostico.jsp -->
    <div id="main">
        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li class="active"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.diagnostico" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="servicio.diagnostico.title" /></h2>

                <html:form styleClass="formulario" method="post" action="/secure/ServicioDiagnostico.do" onsubmit="return chooseValidation(this)">
                    <jsp:include page="/common/crawler_messages.jsp" />

                    <fieldset>
                        <legend><bean:message key="servicio.diagnostico.fecha.legend"/></legend>
                        <p><bean:message key="leyenda.campo.obligatorio" /></p>
                        <div class="formItem">
                            <label for="startDate" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="servicio.diagnostico.fecha.inicio" />: </strong></label>
                            <html:text styleClass="textoCorto" name="ServicioDiagnosticoForm" property="startDate" styleId="startDate" onkeyup="escBarra(event, document.forms['ServicioDiagnosticoForm'].elements['startDate'], 1)" maxlength="10"/>
                            <span>
                                <img src="../images/boton-calendario.gif" onclick="popUpCalendar(this, document.forms['ServicioDiagnosticoForm'].elements['startDate'], 'dd/mm/yyyy')" alt="<bean:message key="img.calendario.alt" />"/>
                            </span>
                            <bean:message key="date.format"/>
                        </div>
                        <div class="formItem">
                            <label for="endDate" class="labelCorto"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="servicio.diagnostico.fecha.fin" />: </strong></label>
                            <html:text styleClass="textoCorto" name="ServicioDiagnosticoForm" property="endDate" styleId="endDate" onkeyup="escBarra(event, document.forms['ServicioDiagnosticoForm'].elements['endDate'], 1)" maxlength="10"/>
                            <span>
                                <img src="../images/boton-calendario.gif" onclick="popUpCalendar(this, document.forms['ServicioDiagnosticoForm'].elements['endDate'], 'dd/mm/yyyy')" alt="<bean:message key="img.calendario.alt" />"/>
                            </span>
                            <bean:message key="date.format"/>
                        </div>
                        <div class="formButton">
                                                <html:submit property="accion" styleClass="btn btn-primary btn-lg"><bean:message key="boton.exportar" /></html:submit>
                                            </div>
                    </fieldset>
                </html:form>
                <p id="pCenter"><html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
            </div><!-- fin cajaformularios -->
        </div>
    </div>