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

<inteco:sesion action="ifConfigAdmin">
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<bean:message key="migas.certificados" />
		</p>
	</div>
	



			<div id="main">

				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="confirmar.borrar.certificado" /></h2>
							
							<p><bean:message key="confirmar.borrar.certificado.info" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/certificatesAction.do">
								<input type="hidden" name="<%=Constants.ACCION %>" value="<%=Constants.DELETE_CERTIFICATE %>"/>
								<input type="hidden" name="<%=Constants.ALIAS %>" value="<bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="alias" />"/>
								<fieldset>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.emisor" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="issuer" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.receptor" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="subject" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.valido.desde" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="validateFrom" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.valido.hasta" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="validateTo" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="certificado.version" />: </strong></label>
										<p><bean:write name="<%=Constants.CERTIFICATE_FORM %>" property="version" /></p>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>

</inteco:sesion>
