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
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<bean:define id="rolObservatory">
	<inteco:properties key="role.observatory.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolAdmin">
	<inteco:properties key="role.administrator.id"
		file="crawler.properties" />
</bean:define>

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
				<li class="active"><span class="glyphicon glyphicon-home"
					aria-hidden="true"></span> <bean:message key="migas.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">



			<h2>Estdo de la ejecución</h2>

			<table class="table table-stripped table-bordered table-hover">
				<caption>Estado de la ejecución del observatorio</caption>
				<tbody>
					<tr>
						<th>Estado</th>
						<th>Acciones</th>
					</tr>
					<tr>
						<td style="text-align: center"><bean:write name="estado"
								property="estado" /></td>
						<td><span class="glyphicon glyphicon-refresh"
							style="color: #e21430"></span></td>
					</tr>
				</tbody>
			</table>

			<h2>Resumen de la ejecución del observatorio</h2>

			<table class="table table-stripped table-bordered table-hover">
				<caption>Información del análisis en curso</caption>
				<tbody>
					<tr>
						<th>Total de semillas</th>
						<th>Total de semillas analizadas correctamente</th>
						<th>Tiempo total</th>
						<th>Tiempo medio</th>
						<th>Tiempo estimado fin</th>
					</tr>

					<tr>
						<td style="text-align: center"><bean:write name="estado"
								property="totalSemillas" /></td>
						<td><bean:write name="estado" property="semillasAnalizadas" />
							(<bean:write name="estado" property="porcentajeCompletado"
								format="###.##" />% completado)</td>
						<td><bean:write name="estado" property="tiempoTotal" />
							minutos (<bean:write name="estado" property="tiempoTotalHoras" />
							horas)</td>
						<td><bean:write name="estado" property="tiempoMedio" />
							minutos</td>
						<td><bean:write name="estado" property="tiempoEstimado" />
							minutos (<bean:write name="estado" property="tiempoEstimadoHoras" />
							Horas)</td>
					</tr>

				</tbody>
			</table>

			<h2>Análisis en curso</h2>

			<table class="table table-stripped table-bordered table-hover">
				<caption>Información del análisis en curso</caption>
				<tbody>

					<tr>
						<th>Semilla actual</th>
					</tr>
					<tr>
						<td style="text-align: center"><bean:write name="analisis"
								property="nombre" />&nbsp;(<bean:write name="analisis" property="url" />)</td>
					</tr>
				</tbody>
			</table>
			<table class="table table-stripped table-bordered table-hover">
				<caption>Información del análisis en curso</caption>
				<tbody>
					<tr>
						<th>Total URL recogidas</th>
						<th>Total URL analizadas</th>
						<th>Última URL analizada</th>
						<th>Fin última URl analizada</th>
						<th>Tiempo medio</th>
						<th>Tiempo total</th>
						<th>Tiempo estimado fin</th>
					</tr>

					<tr>
						<td><bean:write name="analisis" property="totalUrl" /> <span
							class="glyphicon glyphicon glyphicon-info-sign"
							style="color: #e21430"></span></td>
						<td><bean:write name="analisis" property="totalUrlAnalizadas" />
							(<bean:write name="analisis" property="porcentajeCompletado"
								format="###.##" />% completado)</td>
						<td><bean:write name="analisis" property="ultimaUrl" /></td>
						<td><fmt:formatDate value="${analisis.fechaUltimaUrl}"
								pattern="dd-MM-yyyy HH:mm" /></td>
						<td><bean:write name="analisis" property="tiempoMedio" />
							segundos</td>
						<td><bean:write name="analisis" property="tiempoAcumulado" />
							segundos</td>
						<td><bean:write name="analisis" property="tiempoEstimado" />
							segundos</td>
					</tr>

				</tbody>
			</table>

			<h2>Portales sin analizar</h2>

			<table class="table table-stripped table-bordered table-hover">
				<caption>Información del análisis en curso</caption>
				<tbody>
					<tr>
						<th>Nombre</th>
						<th>URL</th>
						<th>Relanzar</th>
					</tr>

					<tr>
						<td style="text-align: left">Ministerio de Justicia</td>
						<td>http://www.mjusticia.gob.es/</td>
						<td><span class="glyphicon glyphicon-refresh"
							style="color: #e21430"></span></td>
					</tr>

				</tbody>
			</table>


		</div>
		<!-- fin cajaformularios -->
	</div>
	<!-- Container Derecha -->
</div>