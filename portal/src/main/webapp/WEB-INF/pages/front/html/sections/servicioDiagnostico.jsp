<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link>
			 / <html:link forward="observatoryMenu"><bean:message key="migas.diagnostico" /></html:link>
		 </p>
	</div>
	
	<div id="cuerpo">
        <div id="cIzq">&nbsp;</div>
        <div id="contenido">
            <div id="main">

                <div id="cuerpoprincipal">
                    
                    <div id="container_menu_izq">
                        <jsp:include page="menu.jsp"/>
                    </div>
                    
                    <div id="container_der">
                        <div id="cajaformularios">
                        
                            <h2 class="config"><bean:message key="servicio.diagnostico.title" /></h2>
                            
                            <p><bean:message key="leyenda.campo.obligatorio" /></p>
                            
                            <html:form styleClass="formulario" method="post" action="/secure/ServicioDiagnostico.do" onsubmit="return chooseValidation(this)">
                                <fieldset>
                                    <jsp:include page="/common/crawler_messages.jsp" />
                                    
                                    <fieldset class="innerFieldset">
                                        <legend><bean:message key="servicio.diagnostico.fecha.legend"/></legend>
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
                                    </fieldset>                                                                       
                                    <div class="formButton">
                                        <html:submit property="accion"><bean:message key="boton.exportar" /></html:submit>
                                        <html:link forward="indexAdmin" styleClass="boton"> <bean:message key="boton.volver"/> </html:link>
                                    </div>
                                </fieldset>
                            </html:form>
                        </div><!-- fin cajaformularios -->
                    </div>
                </div><!-- fin CUERPO PRINCIPAL -->
            </div>
        </div>	
    </div>