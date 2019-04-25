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
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="categoriesMenu"><bean:message key="migas.categoria" /></html:link> / 
			<bean:message key="migas.ver.categoria" />
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="ver.categoria.title" /></h2>
							<html:form method="post" action="/secure/CargarCategorias.do" styleClass="formulario">
								<fieldset>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.categoria.categoria" />: </strong></label>
										<p><bean:write name="VerCategoriaForm" property="categoria" /></p>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.categoria.umbral" />: </strong></label>
										<p><bean:write name="VerCategoriaForm" property="umbral" /></p>
									</div>
									
									<h2><bean:message key="ver.categoria.terminos" /></h2>
									<logic:empty name="VerCategoriaForm" property="vectorTerminos">
										<div class="notaInformativaExito">
											<p><bean:message key="ver.categoria.termino.vacio"/></p>
										</div>
									</logic:empty>
									<logic:notEmpty name="VerCategoriaForm" property="vectorTerminos">
										<div class="formItem">
											<div class="pag">
												<table>
													<tr>
														<th><bean:message key="ver.categoria.termino" /></th>
														<th><bean:message key="ver.categoria.peso" /></th>
														<th><bean:message key="ver.categoria.normalizacion" /></th>
													</tr>
													<logic:iterate name="VerCategoriaForm" type="es.inteco.rastreador2.utils.TerminoCatVer" property="vectorTerminos" id="elemento">
														<tr>
															<td><bean:write name="elemento" property="termino" /></td>
															<td><bean:write name="elemento" property="porcentaje" /></td>
															<td><bean:write name="elemento" property="porcentajeNorm" /></td>
														</tr>
													</logic:iterate>
												</table>
												<jsp:include page="pagination.jsp" />
											</div>
										</div>
									</logic:notEmpty>
									<div class="formItem">
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
