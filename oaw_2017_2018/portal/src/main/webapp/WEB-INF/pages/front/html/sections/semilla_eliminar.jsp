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
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			/ <bean:message key="migas.semillas" /> 
			/ <html:link forward="listSeedsMenu"> <bean:message key="migas.listado.borrar.semillas" /> </html:link>
			/ <bean:message key="migas.eliminar.semilla" />
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
					
						<div id="cajaformularios">
						
							<h2><bean:message key="eliminar.semilla.titulo" /></h2>
							
							<html:form styleClass="formulario" method="post" action="/secure/EliminarSemilla.do">
								<input type="hidden" name="segunda" id="segunda" value="si" />
								<input type="hidden" name="semilla" id="semilla" value="<bean:write name="SemillaForm" property="id" />" />
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									
									<p><strong class="labelVisu"><bean:message key="eliminar.semilla.confirmacion" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="eliminar.semilla.semilla" />: </strong></label>
										<p><bean:write name="SemillaForm" property="nombre" /></p>
									</div>
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
