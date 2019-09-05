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
				<li class="active"><bean:message
						key="migas.eliminar.observatorio" /></li>
			</ol>
		</div>
		<div id="cajaformularios">

			<h2>
				<bean:message key="importar.semillas.title" />
			</h2>

			<div class="detail">
				<p>
					<strong class="labelVisu"><bean:message
							key="importar.semillas.info" /></strong>
				</p>





				<logic:notEmpty name="seedComparisionList">

					<bean:size id="beansize" name="seedComparisionList" />




					<bean:message key="cargar.semilla.observatorio.total">
						<jsp:attribute name="arg0">
                            <bean:write name="beansize" />
                        </jsp:attribute>
					</bean:message>



					<div class="pag">
						<logic:iterate name="seedComparisionList" id="semilla">
							<table class="table table-stripped table-bordered table-hover">
								<caption>
									<bean:message key="lista.semillas.observatorio" />
								</caption>
								<tr>
									<th></th>
									<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
									<th><bean:message
											key="cargar.semilla.observatorio.acronimo" /></th>
									<th><bean:message
											key="cargar.semilla.observatorio.categoria" /></th>
									<th><bean:message
											key="cargar.semilla.observatorio.dependencia" /></th>
									<th><bean:message key="cargar.semilla.observatorio.urls" /></th>
									<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
									<th><bean:message
											key="cargar.semilla.observatorio.directorio" /></th>

								</tr>

								<tr>
									<td>Valor actual</td>
									<td><bean:write name="semilla" property="nombre" /></td>
									<td><bean:write name="semilla" property="acronimo" /></td>
									<td><bean:write name="semilla" property="categoria.name" /></td>

									<td><logic:iterate name="semilla" property="dependencias"
											id="dependencia">
											<bean:write name="dependencia" property="name" />
										</logic:iterate></td>

									<td><logic:iterate name="semilla" property="urls" id="url">
											<bean:write name="url" />
										</logic:iterate></td>

									<td><logic:equal name="semilla" property="activa"
											value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="activa" value="false">
											<bean:message key="no" />
										</logic:equal></td>

									<td><logic:equal name="semilla" property="inDirectory"
											value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="inDirectory"
											value="false">
											<bean:message key="no" />
										</logic:equal></td>
								</tr>
								<tr>
									<td>Valor nuevo</td>
									<td><bean:write name="semilla" property="nombreNuevo" /></td>
									<td><bean:write name="semilla" property="acronimoNuevo" /></td>
									<td><bean:write name="semilla"
											property="categoriaNuevo.name" /></td>

									<td><logic:iterate name="semilla"
											property="dependenciasNuevo" id="dependencia">
											<bean:write name="dependencia" property="name" />
										</logic:iterate></td>

									<td><logic:iterate name="semilla" property="urlsNuevo"
											id="url">
											<bean:write name="url" />
										</logic:iterate></td>

									<td><logic:equal name="semilla" property="activaNuevo"
											value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="activaNuevo"
											value="false">
											<bean:message key="no" />
										</logic:equal></td>
									<td><logic:equal name="semilla"
											property="inDirectoryNuevo" value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="inDirectory"
											value="false">
											<bean:message key="no" />
										</logic:equal></td>
								</tr>





							</table>
						</logic:iterate>
					</div>
				</logic:notEmpty>



				<logic:notEmpty name="newSeedList">

					<bean:size id="newSeedListSize" name="newSeedList" />




					<bean:message key="cargar.semilla.observatorio.total.nuevas">
						<jsp:attribute name="arg0">
                            <bean:write name="newSeedListSize" />
                        </jsp:attribute>
					</bean:message>



					<div class="pag">
						<table class="table table-stripped table-bordered table-hover">
							<caption>
								<bean:message key="lista.semillas.observatorio" />
							</caption>
							<tr>
								<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
								<th><bean:message
										key="cargar.semilla.observatorio.acronimo" /></th>
								<th><bean:message
										key="cargar.semilla.observatorio.categoria" /></th>
								<th><bean:message
										key="cargar.semilla.observatorio.dependencia" /></th>
								<th><bean:message key="cargar.semilla.observatorio.urls" /></th>
								<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
								<th><bean:message
										key="cargar.semilla.observatorio.directorio" /></th>

							</tr>
							<logic:iterate name="newSeedList" id="semilla">

								<tr>
									<td><bean:write name="semilla" property="nombre" /></td>
									<td><bean:write name="semilla" property="acronimo" /></td>
									<td><bean:write name="semilla" property="categoria.name" /></td>

									<td><logic:iterate name="semilla" property="dependencias"
											id="dependencia">
											<bean:write name="dependencia" property="name" />
										</logic:iterate></td>

									<td><logic:iterate name="semilla" property="listaUrls"
											id="url">
											<bean:write name="url" />
										</logic:iterate></td>


									<td><logic:equal name="semilla" property="inDirectory"
											value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="inDirectory"
											value="false">
											<bean:message key="no" />
										</logic:equal></td>


									<td><logic:equal name="semilla" property="activa"
											value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="activa" value="false">
											<bean:message key="no" />
										</logic:equal></td>
								</tr>
							</logic:iterate>
						</table>
					</div>
				</logic:notEmpty>






				<p>
					<strong class="labelVisu"><bean:message
							key="importar.semillas.info2" /></strong>
				</p>

				<div class="formButton">
					<bean:define id="confSi" value="<%=Constants.CONF_SI%>" />
					<bean:define id="confNo" value="<%=Constants.CONF_NO%>" />
					<bean:define id="confirmacion" value="<%=Constants.CONFIRMACION%>" />
					<jsp:useBean id="paramsSI" class="java.util.HashMap" />
					<jsp:useBean id="paramsNO" class="java.util.HashMap" />

					<c:set target="${paramsSI}" property="action" value="loadSeedsFile" />
					<c:set target="${paramsSI}" property="confirmacion"
						value="${confSi}" />

					<html:link styleClass="btn btn-primary btn-lg"
						forward="loadSeedsFile" name="paramsSI">
						<bean:message key="boton.aceptar" />
					</html:link>
					<html:link styleClass="btn btn-default btn-lg"
						forward="observatorySeed">
						<bean:message key="boton.cancelar" />
					</html:link>
				</div>
			</div>

		</div>
		<!-- fin cajaformularios -->
	</div>
</div>
