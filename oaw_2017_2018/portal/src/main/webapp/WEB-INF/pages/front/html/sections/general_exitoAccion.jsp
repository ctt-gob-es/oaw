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
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<html:xhtml />
<%-- <div id="migas">
	<p class="oculto">
		<bean:message key="ubicacion.usuario" />
	</p>
	<p>
		<html:link forward="indexAdmin">
			<bean:message key="migas.inicio" />
		</html:link>
		/
		<bean:message key="migas.informacion" />
	</p>
</div> --%>




<div id="main">
	<bean:define id="mensajeExito">
		<bean:write name="mensajeExito" />
	</bean:define>
	<bean:define id="accionVolver">
		<bean:write name="accionVolver" />
	</bean:define>

<%-- 	<h1>
		<img src="../images/bullet_h1.gif" />
		<bean:message key="pagina.exito" />
	</h1>
 --%>


	<div id="container_menu_izq">
		<jsp:include page="menu.jsp" />
	</div>

	<div id="container_der">
	
	            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.indice.observatorios.realizados.lista"/></li>
                </ol>
            </div>
	
		<div id="cajaformularios">

			<h2>
				<bean:message key="exito.informacion" />
			</h2>
			<div class="notaInformativaExito">
				<c:if test="${mensajeExito!=null}">
					<p>
						<bean:write name="mensajeExito" filter="false" />
					</p>
				</c:if>
				<c:set var="volvemos" value="ValidarLoginSistema" />
				<c:if test="${accionVolver!=null}">
					<bean:define id="volvemos">
						<c:out value="${pageContext.request.contextPath}" />
						<bean:write name="accionVolver" filter="false" />
					</bean:define>
					<p>
						<html:link styleClass="btn btn-default btn-lg"
							href="<%=volvemos%>">
							<bean:message key="boton.volver" />
						</html:link>
					</p>
				</c:if>
			</div>

		</div>
		<!-- fin cajaformularios -->
	</div>

</div>
</div>
</div>
