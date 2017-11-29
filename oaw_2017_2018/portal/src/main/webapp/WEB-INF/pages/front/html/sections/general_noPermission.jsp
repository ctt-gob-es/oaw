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
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
		 / <bean:message key="migas.error" /></p>
	</div>
	



			<div id="main">
				
				<h1><img src="../images/bullet_h1.gif" /> <bean:message key="pagina.error" /> </h1>
				

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios_login">
							<h2><bean:message key="pagina.error" /></h2>
							<div class="notaInformativaExito">
								<p><bean:message key="mensaje.error.permisos"/></p>
								<p><html:link forward="indexAdmin" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link></p>
							</div>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
