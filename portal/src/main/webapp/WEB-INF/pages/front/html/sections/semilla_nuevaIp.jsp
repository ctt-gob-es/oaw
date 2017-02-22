<%@ include file="/common/taglibs.jsp" %>  
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="NuevaSemillaIpForm"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / <bean:message key="migas.semillas" /> / <bean:message key="migas.rangoIp" /></p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="nueva.semilla.ip.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/NuevaSemillaIp.do" onsubmit="return validateNuevaSemillaIpForm(this)">
								<input type="hidden" value="nuevo" name="accion"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<legend><bean:message key="nueva.semilla.ip.configuracion"/></legend>
										<div class="formItem">
											<label for="ipInicial1"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.ip.ip.inicial" /></strong></label>												<html:text styleId="ipInicial1" styleClass="textIp"  maxlength="3" name="NuevaSemillaIpForm" property="ipInicial1"  /> .
											<html:text styleId="ipInicial2" styleClass="textIp"  maxlength="3" name="NuevaSemillaIpForm" property="ipInicial2"  /> .
											<html:text styleId="ipInicial3" styleClass="textIp"  maxlength="3" name="NuevaSemillaIpForm" property="ipInicial3"  /> .
											<html:text styleId="ipInicial4" styleClass="textIp"  maxlength="3" name="NuevaSemillaIpForm" property="ipInicial4"  />
										</div>
										<div class="formItem">
											<label for="ipFinal1"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.ip.ip.final" /></strong></label>
											<html:text styleId="ipFinal1" styleClass="textIp"  maxlength="3" name="NuevaSemillaIpForm" property="ipFinal1"  /> .
											<html:text styleId="ipFinal2" styleClass="textIp"  maxlength="3" name="NuevaSemillaIpForm" property="ipFinal2"  /> .
											<html:text styleId="ipFinal3" styleClass="textIp"  maxlength="3" name="NuevaSemillaIpForm" property="ipFinal3"  /> .
											<html:text styleId="ipFinal4" styleClass="textIp"  maxlength="3" name="NuevaSemillaIpForm" property="ipFinal4"  />
										</div>
										<div class="formItem">
											<label for="puerto1"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.ip.puertos" /></strong></label>
											<html:text styleId="puerto1" styleClass="textIp"  maxlength="5" name="NuevaSemillaIpForm" property="puerto1"  />
											<html:text styleId="puerto2" styleClass="textIp"  maxlength="5" name="NuevaSemillaIpForm" property="puerto2"  />
											<html:text styleId="puerto3" styleClass="textIp"  maxlength="5" name="NuevaSemillaIpForm" property="puerto3"  />
										</div>
										<div class="formItem">
											<label for="nombreSemilla"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.ip.nombre" /></strong></label>
											<html:text styleClass="texto" maxlength="20" name="NuevaSemillaIpForm" property="nombreSemilla" styleId="nombreSemilla" />
										</div>
										<div class="formButton">
											<html:hidden property="<%= Constants.BOTON_SEMILLA_IP %>" value="boton"/>
											<html:submit><bean:message key="boton.aceptar" /></html:submit>
											<html:cancel><bean:message key="boton.volver"/></html:cancel>
										</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 