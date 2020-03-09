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

<inteco:sesion action="ifConfigAdmin">

<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> / 
			<bean:message key="eliminar.rastreo" />
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
						
							<h2><bean:message key="eliminar.rastreo.titulo" /> </h2>
							
							<html:form styleClass="formulario" method="post" action="/secure/EliminarRastreo.do">
								<input type="hidden" name="rastreo" id="rastreo" value="<bean:write name="EliminarRastreoForm" property="codigo" />" />
								<input type="hidden" name="idrastreo" value="<bean:write name="EliminarRastreoForm" property="idrastreo" />"/>
								<input type="hidden" name="confirmacion" value="si"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<p><strong class="labelVisu"><bean:message key="eliminar.rastreo.confirmacion" /></strong></p>
									<p><strong class="labelVisu"><bean:message key="eliminar.rastreo.confirmacion2" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.rastreo.nombre.rastreo" />: </strong></label>
										<p><bean:write name="EliminarRastreoForm" property="codigo" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.rastreo.fecha.creacion" />: </strong></label>
										<p><bean:write name="EliminarRastreoForm" property="fecha" /></p>
									</div>
									<div class="formItem">
										<c:set var="cartuchoName" value="${EliminarRastreoForm.cartucho}"/>
										<label><strong class="labelVisu"><bean:message key="eliminar.rastreo.cartucho" />: </strong></label>
										<p><inteco:trunp cad="cartuchoName"/></p>
									</div>
									<logic:notEmpty name="EliminarRastreoForm" property="normaAnalisis">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="eliminar.rastreo.norma" />: </strong></label>
											<p><bean:write name="EliminarRastreoForm" property="normaAnalisis" /></p>
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
		</div>
	</div> 

</inteco:sesion>
