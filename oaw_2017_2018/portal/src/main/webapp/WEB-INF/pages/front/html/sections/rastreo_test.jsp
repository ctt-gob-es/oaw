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
<html:javascript formName="TestRastreoForm"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			 / <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> 
			 / <bean:message key="migas.test.rastreo" />
		 </p>
	</div>



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="test.rastreo.title" /></h2>
							
							<p><bean:message key="test.rastreo.info"/></p>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" method="get" action="/secure/TestRastreo.do" onsubmit="return validateTestRastreoForm(this)" enctype="multipart/form-data">
								<input type="hidden" name="action" value="<%=Constants.LAUNCH_TEST %>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="semilla" ><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.url.semilla" /></strong></label>
										<html:text styleClass="texto" property="semilla" styleId="semilla" />
									</div>
									<div class="formItem">
										<label for="listaRastreable"><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.lista.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" property="listaRastreable" styleId="listaRastreable" />
									</div>
									<div class="formItem">
										<label for="listaNoRastreable"><strong class="labelVisu"><bean:message key="nueva.cuenta.cliente.lista.no.rastreable" />: </strong></label>
										<html:text styleClass="texto" maxlength="100" property="listaNoRastreable" styleId="listaNoRastreable" />
									</div>
									<div class="formItem">
										<label for="profundidad"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.profundidad" /></strong></label>
										<bean:define id="maxProfundidad">
											<inteco:properties key="profundidadMax.rastreo" file="crawler.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="profundidad"  property="profundidad" >
											<c:forEach begin="1" end="${maxProfundidad}" varStatus="status">
												<c:if test="${InsertarRastreoForm.profundidad==status.count}">
													<option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
												<c:if test="${InsertarRastreoForm.profundidad!=status.count}">
													<option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
											</c:forEach>
										</html:select>
									</div>
									<div class="formItem">
										<label for="topN"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.paginas.nivel" /></strong></label>
										<bean:define id="maxtopN">
											<inteco:properties key="pagPorNivelMax.rastreo" file="crawler.properties" />
										</bean:define>
										<html:select styleClass="textoSelect" styleId="topN"  property="topN" >
											<c:forEach begin="1" end="${maxtopN}" varStatus="status">
												<c:if test="${InsertarRastreoForm.topN== status.count}">
													<option selected value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
												<c:if test="${InsertarRastreoForm.topN!=status.count}">
													<option value="<c:out value="${status.count}" />"> <c:out value="${status.count}" /></option>
												</c:if>
											</c:forEach>
											<option value="<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />"><bean:message key="nuevo.rastreo.amplitud.ilimitada" /></option>
										</html:select>
									</div>
									<div class="formItem">
										<label for="pseudoAleatorio"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.pseudoaleatorio" /></strong></label>
										<html:select styleClass="textoSelect" styleId="pseudoAleatorio"  property="pseudoAleatorio" >
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									<div class="formItem">
										<label for="exhaustive"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.exhaustive" /></strong></label>
										<html:select styleClass="textoSelect" styleId="exhaustive"  property="exhaustive" >
											<html:option value="false"><bean:message key="select.no"/></html:option>
											<html:option value="true"><bean:message key="select.yes"/></html:option>
										</html:select>
									</div>
									<div class="formItem">
										<label for="inDirectory"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nuevo.rastreo.in.directory" /></strong></label>
										<html:select styleClass="textoSelect" styleId="inDirectory"  property="inDirectory" >
											<html:option value="false"><bean:message key="select.no"/></html:option>
											<html:option value="true"><bean:message key="select.yes"/></html:option>
										</html:select>
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
		</div>	
	</div> 

</inteco:sesion>
