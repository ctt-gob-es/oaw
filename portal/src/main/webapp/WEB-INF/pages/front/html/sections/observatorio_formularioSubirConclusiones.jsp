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
<html:javascript formName="SubirConclusionesForm"/>
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${idExObs}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
		<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
		<html:link forward="getFulfilledObservatories" name="params"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
		<bean:message key="migas.indice.observatorios.subir.conclusiones"/>
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="subir.conclusiones.titulo" /></h2>
					
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form  styleClass="formulario" method="post"  action="/secure/ResultadosAnonimosObservatorio" enctype="multipart/form-data" onsubmit="return validateSubirConclusionesForm(this)">
								<input type="hidden" name="<%=Constants.ACTION %>" value="<%=Constants.UPLOAD_FILE %>"/>
								<input type="hidden" name="<%=Constants.ID_OBSERVATORIO %>" value="<bean:write name="<%=Constants.ID_OBSERVATORIO %>"/>"/>
								<input type="hidden" name="<%=Constants.ID_EX_OBS %>" value="<bean:write name="<%=Constants.ID_EX_OBS %>"/>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="file"><strong class="labelVisu"><bean:message key="fichero.conclusiones" />: </strong></label>
										<html:file styleClass="texto" property="file" styleId="file"/>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div>
					</div>

			</div>

