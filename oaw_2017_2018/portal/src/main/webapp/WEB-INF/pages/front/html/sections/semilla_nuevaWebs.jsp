<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
This program is licensed and may be used, modified and redistributed under the terms
of the European Public License (EUPL), either version 1.2 or (at your option) any later 
version as soon as they are approved by the European Commission.
Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND, either express or implied. See the License for the specific language governing 
permissions and more details.
You should have received a copy of the EUPL1.2 license along with this program; if not, 
you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
-->
<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">
	
	<html:javascript formName="NuevaSemillaWebsForm"/>
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		 / <bean:message key="migas.semillas" /> / <bean:message key="migas.semillas.listado.webs" /></p>
	</div>
	



			<div id="main">

					
						<div id="container_menu_izq">
							<jsp:include page="menu.jsp"/>
						</div>
						
						<div id="container_der">
							<div id="cajaformularios">
							
								<h2><bean:message key="nueva.semilla.webs.title" /></h2>
								
								<p><bean:message key="leyenda.campo.obligatorio" /></p>
								
								<html:form styleClass="formulario" method="post" action="/secure/NuevaSemillaWebs.do" onsubmit="return validateNuevaSemillaWebsForm(this)" >
									<p class="observ"><em><bean:message key="nueva.semilla.webs.informacion"/> </em>: <bean:message key="nueva.semilla.webs.info" /></p>
									<fieldset>
										<jsp:include page="/common/crawler_messages.jsp" />
										<legend> <bean:message key="nueva.semilla.webs.introduccion.url"/> </legend>
										
										<div class="formItem">
											<label for="nombreSemilla"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.webs.nombre" /></strong></label>
											<html:text styleId="nombreSemilla" styleClass="texto" name="NuevaSemillaWebsForm" property="nombreSemilla" />
										</div>
										
										<div class="formItem">
											<p><label for="ta1"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.webs.lista.url"/></strong></label></p>
											<html:textarea styleId="ta1" name="NuevaSemillaWebsForm" property="ta1" rows="5" cols="50" />
										</div>
										<div class="formButton">
											<html:hidden property="<%= Constants.BOTON_SEMILLA_WEB %>" value="boton"/>
											<html:submit><bean:message key="boton.aceptar"/></html:submit>
											<html:cancel><bean:message key="boton.volver"/></html:cancel>
										</div>
									</fieldset>	
								</html:form>
							</div><!-- fin cajaformularios -->
						</div>

				</div>

</inteco:sesion>
