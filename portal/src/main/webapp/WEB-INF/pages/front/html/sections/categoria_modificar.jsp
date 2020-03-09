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
<html:javascript formName="VerCategoriaForm"/>
	
	<bean:define id="idcat" value="<%= Constants.ID_CATEGORIA %>" />
	<bean:define id="idter" value="<%= Constants.ID_TERMINO %>" />
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="categoriesMenu"><bean:message key="migas.categoria" /></html:link> / 
			<bean:message key="migas.editar.categoria" />
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="modificar.categoria.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form method="post" styleClass="formulario" action="/secure/ModificarCategoria.do" onsubmit="return validateVerCategoriaForm(this)">
								<input type="hidden" name="nombreAntiguo" id="nombreAntiguo" value="<bean:write name="VerCategoriaForm" property="nombre_antiguo" />" />
								<input type="hidden" name="action" value="modificar"/>
								<input type="hidden" name="idcat" value="<bean:write name="VerCategoriaForm" property="id_categoria" />"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="categoria"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.categoria.nombre" />: </strong></label>
										<html:text styleClass="texto" styleId="categoria" property="categoria" maxlength="35" />
									</div>
									<div class="formItem">
										<label for="umbral"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="modificar.categoria.umbral" />: </strong></label>
										<html:text styleClass="texto" styleId="umbral" property="umbral" />
									</div>
									<div class="formItem">
										<h2><bean:message key="modificar.categoria.terminos" /></h2>
										<logic:empty name="VerCategoriaForm" property="vectorTerminos">
											<div class="notaInformativaExito">
												<bean:message key="modificar.categoria.terminos.vacio"/>
												<p><html:link forward="newCategorieTerm" styleClass="boton" styleId="nBoton10" paramId="<%= Constants.ID_CATEGORIA %>" paramName="VerCategoriaForm" paramProperty="id_categoria"><bean:message key="modificar.categoria.nuevo.termino" />.</html:link></p>
											</div>
										</logic:empty>
										<logic:notEmpty name="VerCategoriaForm" property="vectorTerminos">
											<p><html:link forward="newCategorieTerm" styleClass="boton" styleId="nBoton10" paramId="<%= Constants.ID_CATEGORIA %>" paramName="VerCategoriaForm" paramProperty="id_categoria"><bean:message key="modificar.categoria.nuevo.termino" />.</html:link></p>
										</logic:notEmpty>
									</div>
									<logic:notEmpty name="VerCategoriaForm" property="vectorTerminos">
										<div class="formItem">	
											<div class="pag">
												<table>
													<tr>
														<th><bean:message key="modificar.categoria.terminos.termino" /></th>
														<th><bean:message key="modificar.categoria.terminos.peso" /></th>
														<th><bean:message key="modificar.categoria.terminos.acciones" /></th>
													</tr>
												
													<logic:iterate name="VerCategoriaForm" type="es.inteco.rastreador2.utils.TerminoCatVer" property="vectorTerminos" id="elem">
														<jsp:useBean id="params" class="java.util.HashMap" />
														<c:set target="${params}" property="${idcat}" value="${VerCategoriaForm.id_categoria}" />
														<c:set target="${params}" property="${idter}" value="${elem.id_termino}" />
														<tr>
															<td><bean:write name="elem" property="termino" /></td>
															<td><bean:write name="elem" property="porcentaje" /></td>
															<td><ul class="lista_linea">
																<li><html:link forward="editCategorieTerm" name="params"><img src="../images/bt_modificar.gif" alt="<bean:message key="modificar.categoria.editar.termino.categoria.alt" />"/></html:link></li>
																<li><html:link forward="deleteCategorieTerm" name="params"><img src="../images/bt_eliminar.gif" alt="<bean:message key="modificar.categoria.eliminar.termino.categoria.alt" />"/></html:link></li>
															</ul></td>
														</tr>
													</logic:iterate>
												</table>
												<jsp:include page="pagination.jsp" />
											</div>
										</div>
									</logic:notEmpty>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.cancelar" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>

