<!--
Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
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
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />

<div id="main">

	<div id="container_menu_izq">
		<jsp:include page="menu.jsp" />
	</div>

	<div id="container_der">

		<div id="migas">
			<p class="sr-only">
				<bean:message key="ubicacion.usuario" />
			</p>
			<ol class="breadcrumb">
				<li><html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link></li>
				<li class="active"><bean:message key="migas.eliminar.observatorio" /></li>
			</ol>
		</div>
		<div id="cajaformularios">

			<h2>
				<bean:message key="importar.semillas.title" />
			</h2>

			<div class="detail">
				<p>
					<bean:message key="importar.semillas.info" />
				</p>


				<logic:present name="errorSeeds">

					<logic:notEmpty name="errorSeeds">

						<h3>
							<bean:message key="cargar.semilla.errors.title" />
						</h3>

						<bean:size id="errorSeedsSize" name="errorSeeds" />




						<bean:message key="cargar.semilla.observatorio.total.errores">
							<jsp:attribute name="arg0">
                            <bean:write name="errorSeedsSize" />
                        </jsp:attribute>
						</bean:message>


						<table class="table table-stripped table-bordered table-hover" style="width: 100%; overflow-wrap: break-word;">
							<caption>
								<bean:message key="lista.semillas.observatorio" />
							</caption>

							<thead>
								<tr>
									<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
									<th><bean:message key="cargar.semilla.observatorio.acronimo" /></th>
									<th><bean:message key="cargar.semilla.observatorio.categoria" /></th>
									<th><bean:message key="cargar.semilla.observatorio.ambito" /></th>
									<th><bean:message key="cargar.semilla.observatorio.complejidad" /></th>
									<th><bean:message key="cargar.semilla.observatorio.dependencia" /></th>
									<th style="max-width: 20%"><bean:message key="cargar.semilla.observatorio.urls" /></th>
									<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
									<th><bean:message key="cargar.semilla.observatorio.eliminada" /></th>
									<th><bean:message key="cargar.semilla.observatorio.directorio" /></th>
									<th><bean:message key="cargar.semilla.observatorio.errores" /></th>

								</tr>
							</thead>
							<tbody>
								<logic:iterate name="errorSeeds" id="semilla">

									<tr>
										<td><bean:write name="semilla" property="nombre" /></td>
										<td><bean:write name="semilla" property="acronimo" /></td>
										<td><bean:write name="semilla" property="categoria.name" /></td>
										<td><bean:write name="semilla" property="ambito.name" /></td>
										<td><bean:write name="semilla" property="complejidad.name" /></td>

										<td><logic:iterate name="semilla" property="dependencias" id="dependencia">
												<bean:write name="dependencia" property="name" />
											</logic:iterate></td>

										<td style="max-width: 20%; text-align: left !important;"><logic:iterate name="semilla"
												property="listaUrls" id="url">
												<bean:write name="url" />
												<br />
											</logic:iterate></td>




										<td><logic:equal name="semilla" property="activa" value="true">
												<bean:message key="si" />
											</logic:equal> <logic:equal name="semilla" property="activa" value="false">
												<bean:message key="no" />
											</logic:equal></td>


										<td><logic:equal name="semilla" property="eliminar" value="true">
												<bean:message key="si" />
											</logic:equal> <logic:equal name="semilla" property="eliminar" value="false">
												<bean:message key="no" />
											</logic:equal></td>


										<td><logic:equal name="semilla" property="inDirectory" value="true">
												<bean:message key="si" />
											</logic:equal> <logic:equal name="semilla" property="inDirectory" value="false">
												<bean:message key="no" />
											</logic:equal></td>


										<td><ul class="seedImportErrorList">
												<logic:iterate name="semilla" property="errors" id="error">
													<li><bean:write name="error" /></li>
												</logic:iterate>
											</ul></td>
									</tr>
								</logic:iterate>
							</tbody>
						</table>
					</logic:notEmpty>
				</logic:present>















				<logic:present name="updatedSeeds">


					<logic:notEmpty name="updatedSeeds">


						<h3>
							<bean:message key="cargar.semilla.actualizables.title" />
						</h3>


						<bean:size id="beansize" name="updatedSeeds" />

						<bean:message key="cargar.semilla.observatorio.total">
							<jsp:attribute name="arg0">
                            <bean:write name="beansize" />
                        </jsp:attribute>
						</bean:message>

						<logic:iterate name="updatedSeeds" id="semilla">

							<table class="table table-stripped table-bordered table-hover">
								<caption>
									<bean:message key="lista.semillas.observatorio" />
								</caption>
								<tr>
									<th></th>
									<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
									<th><bean:message key="cargar.semilla.observatorio.acronimo" /></th>
									<th><bean:message key="cargar.semilla.observatorio.categoria" /></th>
									<th><bean:message key="cargar.semilla.observatorio.ambito" /></th>
									<th><bean:message key="cargar.semilla.observatorio.complejidad" /></th>
									<th><bean:message key="cargar.semilla.observatorio.dependencia" /></th>
									<th><bean:message key="cargar.semilla.observatorio.etiquetas" /></th>
									<th><bean:message key="cargar.semilla.observatorio.urls" /></th>
									<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
									<th><bean:message key="cargar.semilla.observatorio.eliminada" /></th>
									<th><bean:message key="cargar.semilla.observatorio.directorio" /></th>
									<th><bean:message key="cargar.semilla.observatorio.observaciones" /></th>

								</tr>

								<tr>
									<td>Valor actual</td>
									<td><bean:write name="semilla" property="nombre" /></td>
									<td><bean:write name="semilla" property="acronimo" /></td>
									<td><bean:write name="semilla" property="categoria.name" /></td>
									<td><bean:write name="semilla" property="ambito.name" /></td>
									<td><bean:write name="semilla" property="complejidad.name" /></td>

									<td><logic:iterate name="semilla" property="dependencias" id="dependencia">
											<bean:write name="dependencia" property="name" />
										</logic:iterate></td>

									<td><logic:iterate name="semilla" property="etiquetas" id="etiqueta">
											<bean:write name="etiqueta" property="name" />
										</logic:iterate></td>

									<td><logic:iterate name="semilla" property="urls" id="url">
											<bean:write name="url" />
										</logic:iterate></td>

									<td><logic:equal name="semilla" property="activa" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="activa" value="false">
											<bean:message key="no" />
										</logic:equal></td>

									<td><logic:equal name="semilla" property="eliminada" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="eliminada" value="false">
											<bean:message key="no" />
										</logic:equal></td>

									<td><logic:equal name="semilla" property="inDirectory" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="inDirectory" value="false">
											<bean:message key="no" />
										</logic:equal></td>

									<td><bean:write name="semilla" property="observaciones" /></td>
								</tr>
								<tr>
									<td><bean:message key="cargar.semilla.observatorio.new.value" /></td>

									<td class="<c:if test="${semilla.sameNombre!=true}">warning-import</c:if>"><bean:write name="semilla"
											property="nombreNuevo" /></td>

									<td class="<c:if test="${semilla.sameAcronimo!=true}">warning-import</c:if>"><bean:write name="semilla"
											property="acronimoNuevo" /></td>

									<td class="<c:if test="${semilla.sameCategoria!=true}">warning-import</c:if>"><bean:write name="semilla"
											property="categoriaNuevo.name" /></td>

									<td class="<c:if test="${semilla.sameAmbito!=true}">warning-import</c:if>"><bean:write name="semilla"
											property="ambitoNuevo.name" /></td>

									<td class="<c:if test="${semilla.sameComplejidad!=true}">warning-import</c:if>"><bean:write name="semilla"
											property="complejidadNuevo.name" /></td>

									<td class="<c:if test="${semilla.sameDependencias!=true}">warning-import</c:if>"><logic:iterate
											name="semilla" property="dependenciasNuevo" id="dependencia">
											<bean:write name="dependencia" property="name" />
										</logic:iterate></td>

									<td class="<c:if test="${semilla.sameEtiquetas!=true}">warning-import</c:if>"><logic:iterate
											name="semilla" property="etiquetasNuevo" id="etiqueta">
											<bean:write name="etiqueta" property="name" />
										</logic:iterate></td>

									<td class="<c:if test="${semilla.sameListaURLs!=true}">warning-import</c:if>"><logic:iterate
											name="semilla" property="urlsNuevo" id="url">
											<bean:write name="url" />
										</logic:iterate></td>

									<td class='<c:if test="${semilla.sameActiva!=true}">warning-import</c:if>'><logic:equal name="semilla"
											property="activaNuevo" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="activaNuevo" value="false">
											<bean:message key="no" />
										</logic:equal></td>
									<td class='<c:if test="${semilla.sameEliminada!=true}">warning-import</c:if>'><logic:equal name="semilla"
											property="eliminadaNuevo" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="eliminadaNuevo" value="false">
											<bean:message key="no" />
										</logic:equal></td>




									<td class='<c:if test="${semilla.sameInDirectory!=true}">warning-import</c:if>'><logic:equal
											name="semilla" property="inDirectoryNuevo" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="inDirectoryNuevo" value="false">
											<bean:message key="no" />
										</logic:equal></td>

									<td class="<c:if test="${semilla.sameObservaciones!=true}">warning-import</c:if>"><bean:write
											name="semilla" property="observacionesNuevo" /></td>
								</tr>
							</table>
						</logic:iterate>
					</logic:notEmpty>
				</logic:present>


				<logic:present name="newSeedList">
					<logic:notEmpty name="newSeedList">

						<h3>
							<bean:message key="cargar.semilla.nuevas.title" />
						</h3>

						<bean:size id="newSeedListSize" name="newSeedList" />

						<bean:message key="cargar.semilla.observatorio.total.nuevas">
							<jsp:attribute name="arg0">
                            <bean:write name="newSeedListSize" />
                        </jsp:attribute>
						</bean:message>

						<table class="table table-stripped table-bordered table-hover">
							<caption>
								<bean:message key="lista.semillas.observatorio" />
							</caption>
							<tr>
								<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
								<th><bean:message key="cargar.semilla.observatorio.acronimo" /></th>
								<th><bean:message key="cargar.semilla.observatorio.categoria" /></th>
								<th><bean:message key="cargar.semilla.observatorio.ambito" /></th>
								<th><bean:message key="cargar.semilla.observatorio.complejidad" /></th>
								<th><bean:message key="cargar.semilla.observatorio.dependencia" /></th>
								<th><bean:message key="cargar.semilla.observatorio.etiquetas" /></th>
								<th><bean:message key="cargar.semilla.observatorio.urls" /></th>
								<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
								<th><bean:message key="cargar.semilla.observatorio.eliminada" /></th>
								<th><bean:message key="cargar.semilla.observatorio.directorio" /></th>
								<th><bean:message key="cargar.semilla.observatorio.observaciones" /></th>

							</tr>
							<logic:iterate name="newSeedList" id="semilla">

								<tr>
									<td><bean:write name="semilla" property="nombre" /></td>
									<td><bean:write name="semilla" property="acronimo" /></td>
									<td><bean:write name="semilla" property="categoria.name" /></td>
									<td><bean:write name="semilla" property="ambito.name" /></td>
									<td><bean:write name="semilla" property="complejidad.name" /></td>
									<td><logic:iterate name="semilla" property="dependencias" id="dependencia">
											<bean:write name="dependencia" property="name" />
										</logic:iterate></td>

									<td><logic:iterate name="semilla" property="etiquetas" id="etiqueta">
											<bean:write name="etiqueta" property="name" />
										</logic:iterate></td>

									<td><logic:iterate name="semilla" property="listaUrls" id="url">
											<bean:write name="url" />
										</logic:iterate></td>


									<td><logic:equal name="semilla" property="activa" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="activa" value="false">
											<bean:message key="no" />
										</logic:equal></td>


									<td><logic:equal name="semilla" property="eliminar" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="eliminar" value="false">
											<bean:message key="no" />
										</logic:equal></td>
									<td><logic:equal name="semilla" property="inDirectory" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="inDirectory" value="false">
											<bean:message key="no" />
										</logic:equal></td>

									<td><bean:write name="semilla" property="observaciones" /></td>
								</tr>
							</logic:iterate>
						</table>
					</logic:notEmpty>
				</logic:present>

				<logic:present name="inalterableSeeds">

					<logic:notEmpty name="inalterableSeeds">

						<h3>
							<bean:message key="cargar.semilla.inalterables.title" />
						</h3>

						<bean:size id="inalterableSeedsSize" name="inalterableSeeds" />




						<bean:message key="cargar.semilla.observatorio.total.sincambios">
							<jsp:attribute name="arg0">
                            <bean:write name="inalterableSeedsSize" />
                        </jsp:attribute>
						</bean:message>


						<table class="table table-stripped table-bordered table-hover" style="width: 100%; overflow-wrap: break-word;">
							<caption>
								<bean:message key="lista.semillas.observatorio" />
							</caption>

							<thead>
								<tr>
									<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
									<th><bean:message key="cargar.semilla.observatorio.acronimo" /></th>
									<th><bean:message key="cargar.semilla.observatorio.categoria" /></th>
									<th><bean:message key="cargar.semilla.observatorio.dependencia" /></th>
									<th style="max-width: 20%"><bean:message key="cargar.semilla.observatorio.urls" /></th>
									<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
									<th><bean:message key="cargar.semilla.observatorio.eliminada" /></th>
									<th><bean:message key="cargar.semilla.observatorio.directorio" /></th>
									<th><bean:message key="cargar.semilla.observatorio.observaciones" /></th>

								</tr>
							</thead>
							<tbody>
								<logic:iterate name="inalterableSeeds" id="semilla">

									<tr>
										<td><bean:write name="semilla" property="nombre" /></td>
										<td><bean:write name="semilla" property="acronimo" /></td>
										<td><bean:write name="semilla" property="categoria.name" /></td>

										<td><logic:iterate name="semilla" property="dependencias" id="dependencia">
												<bean:write name="dependencia" property="name" />
											</logic:iterate></td>

										<td style="max-width: 20%; text-align: left !important;"><logic:iterate name="semilla"
												property="listaUrls" id="url">
												<bean:write name="url" />
												<br />
											</logic:iterate></td>




										<td><logic:equal name="semilla" property="activa" value="true">
												<bean:message key="si" />
											</logic:equal> <logic:equal name="semilla" property="activa" value="false">
												<bean:message key="no" />
											</logic:equal></td>


										<td><logic:equal name="semilla" property="eliminar" value="true">
												<bean:message key="si" />
											</logic:equal> <logic:equal name="semilla" property="eliminar" value="false">
												<bean:message key="no" />
											</logic:equal></td>


										<td><logic:equal name="semilla" property="inDirectory" value="true">
												<bean:message key="si" />
											</logic:equal> <logic:equal name="semilla" property="inDirectory" value="false">
												<bean:message key="no" />
											</logic:equal></td>
										<td><bean:write name="semilla" property="observaciones" /></td>
									</tr>
								</logic:iterate>
							</tbody>
						</table>
					</logic:notEmpty>
				</logic:present>


				<p>
					<bean:message key="importar.semillas.info2" />
				</p>

				<div class="formButton">
					<bean:define id="confSi" value="<%=Constants.CONF_SI%>" />
					<bean:define id="confNo" value="<%=Constants.CONF_NO%>" />
					<bean:define id="confirmacion" value="<%=Constants.CONFIRMACION%>" />
					<jsp:useBean id="paramsSI" class="java.util.HashMap" />
					<jsp:useBean id="paramsNO" class="java.util.HashMap" />

					<c:set target="${paramsSI}" property="action" value="loadSeedsFile" />
					<c:set target="${paramsSI}" property="confirmacion" value="${confSi}" />

					<html:link styleClass="btn btn-primary btn-lg" forward="loadSeedsFile" name="paramsSI">
						<bean:message key="boton.aceptar" />
					</html:link>
					<html:link styleClass="btn btn-default btn-lg" forward="observatorySeed">
						<bean:message key="boton.cancelar" />
					</html:link>
				</div>
			</div>

		</div>
		<!-- fin cajaformularios -->
	</div>
</div>
