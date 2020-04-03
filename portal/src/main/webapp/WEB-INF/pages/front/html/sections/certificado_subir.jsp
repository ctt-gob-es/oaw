<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnolog�as de la Comunicaci�n, 
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

<html:javascript formName="CertificateForm"/>

<inteco:sesion action="ifConfigAdmin">
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<bean:message key="migas.certificados" /></p>
	</div>
	
	<jsp:useBean id="paramsNC" class="java.util.HashMap" />
	<c:set target="${paramsNC}" property="esPrimera" value="si" />
	



			<div id="main">

				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="subir.certificado.nuevo" /> </h2>
							<div class="certificados">
								<jsp:include page="/common/crawler_messages.jsp" />
								<p><html:link forward="uploadCertificate" name="paramsNC" styleClass="boton"><bean:message key="certificado.nuevo.certificado" /></html:link></p>
								<h3><bean:message key="lista.certificados.importados" /></h3>
								<p>
									<bean:message key="mensaje.numero.certificados">
										<jsp:attribute name="arg0">
											<bean:write name="<%=Constants.NUM_CERTIFICATES %>"/>
										</jsp:attribute>
									</bean:message>
								</p>	
								<table>
									<caption><bean:message key="lista.certificados.importados" /></caption>
									<tr>
										<th><bean:message key="certificado.emisor" /></th>
										<th><bean:message key="certificado.receptor" /></th>
										<th><bean:message key="certificado.valido.desde" /></th>
										<th><bean:message key="certificado.valido.hasta" /></th>
										<th><bean:message key="certificado.version" /></th>
										<th><bean:message key="certificado.acciones" /></th>
									</tr>
									
									<logic:iterate name="<%=Constants.CERTIFICATES %>" id="certificate">
										<tr>
											<bean:define id="eliminateTitle">
												<bean:message key="certificado.borrar.titulo">
													<jsp:attribute name="arg0">
														<bean:write name="certificate" property="issuer" ignore="true"/>
													</jsp:attribute>
												</bean:message>
											</bean:define>
											<td><bean:write name="certificate" property="issuer" ignore="true"/></td>
											<td><bean:write name="certificate" property="subject" ignore="true"/></td>
											<td><bean:write name="certificate" property="validateFrom" ignore="true"/></td>
											<td><bean:write name="certificate" property="validateTo" ignore="true"/></td>
											<td><bean:write name="certificate" property="version" ignore="true"/></td>
											<td><html:link forward="deleteConfirmation" paramId="<%=Constants.ALIAS %>" paramName="certificate" paramProperty="alias" title="<%=eliminateTitle%>"><img src="../images/bt_eliminar.gif" alt="<%=eliminateTitle%>"/></html:link></td>
										</tr>
									</logic:iterate>
									
								</table>
								<div class="pag">
									<jsp:include page="pagination.jsp" />
								</div>
								<p id="pCenter"><html:link forward="indexAdmin" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
						</div><!-- fin cajaformularios -->
					</div>

			</div>

</inteco:sesion>
