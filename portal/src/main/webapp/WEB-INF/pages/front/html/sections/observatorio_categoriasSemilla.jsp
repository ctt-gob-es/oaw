<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnolog�as de la Comunicaci�n, 
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
<!-- observatorio_categoriasSemilla.jsp -->
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
				<li>
					<html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link>
				</li>
				<li class="active">
					<bean:message key="migas.categoria" />
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2 class="pull-left">
				<bean:message key="indice.categorias.gestion.categorias" />
			</h2>
			<logic:empty name="<%=Constants.SEED_CATEGORIES%>">
				<p>
					<bean:message key="categoria.semillas.vacia" />
				</p>
			</logic:empty>
			<p class="pull-right">
				<html:link forward="newSeedCategory" styleClass="btn btn-default btn-lg">
					<span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title="Crear un segmento"></span>
					<bean:message key="categoria.semillas.nueva" />
				</html:link>
			</p>
			<logic:notEmpty name="<%=Constants.SEED_CATEGORIES%>">
				<div class="pag">
					<table class="table table-stripped table-bordered table-hover">
						<caption>
							<bean:message key="categoria.semillas.lista" />
						</caption>
						<colgroup>
							<col style="width: 70%">
							<col style="width: 20%">
							<col style="width: 5%">
							<col style="width: 5%">
							<col style="width: 5%">
						</colgroup>
						<tbody>
							<tr>
								<th>
									<bean:message key="cargar.semilla.observatorio.categoria" />
								</th>
								<th>
									<bean:message key="cargar.semilla.observatorio.clave" />
								</th>
								<th class="accion">
									<bean:message key="cargar.semilla.observatorio.principal" />
								</th>
								<th class="accion">
									<bean:message key="cargar.semilla.observatorio.file" />
								</th>
								<th class="accion">
									<bean:message key="cargar.semilla.observatorio.eliminar" />
								</th>
							</tr>
							<logic:iterate name="<%=Constants.SEED_CATEGORIES %>" id="category">
								<tr>
									<td style="text-align: left;">
										<html:link forward="editSeedCategory" paramId="<%=Constants.ID_CATEGORIA%>" paramName="category"
											paramProperty="id">
											<span data-toggle="tooltip" title="<bean:message key="indice.categorias.edit"/>">
												<bean:write name="category" property="name" />
											</span>
										</html:link>
										<span class="glyphicon glyphicon-edit pull-right edit-mark" aria-hidden="true" />
									</td>
									<td style="text-align: left;">
										<span data-toggle="tooltip" title="Agrupado por">
											<bean:write name="category" property="key" />
										</span>
									</td>
									<td style="text-align: left;">
										<span data-toggle="tooltip" title="Agrupado por">
											<logic:equal name="category" property="principal" value="true">
												<bean:message key="select.yes" />
											</logic:equal>
											<logic:notEqual name="category" property="principal" value="true">
												<bean:message key="select.no" />
											</logic:notEqual>
										</span>
									</td>
									<td>
										<html:link forward="getCategorySeedsFileXml" paramId="<%=Constants.ID_CATEGORIA%>" paramName="category"
											paramProperty="id">
											<span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip"
												title='<bean:message key="indice.categorias.download.xml"/>' />
											<span class="sr-only">
												<bean:message key='colname.download' />
											</span>
										</html:link>
										<html:link forward="getCategorySeedsFileXlsx" paramId="<%=Constants.ID_CATEGORIA%>" paramName="category"
											paramProperty="id">
											<span class="glyphicon glyphicon-save-file" aria-hidden="true" data-toggle="tooltip"
												title='<bean:message key="indice.categorias.download.xlsx"/>' />
											<span class="sr-only">
												<bean:message key='colname.download' />
											</span>
										</html:link>
									</td>
									<td>
										<html:link forward="deleteSeedCategoryConfirmation" paramId="<%=Constants.ID_CATEGORIA%>" paramName="category"
											paramProperty="id">
											<span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip"
												title="<bean:message key="indice.categorias.remove"/>" />
											<span class="sr-only">
												<bean:message key="indice.rastreo.img.eliminar.rastreo.alt" />
											</span>
										</html:link>
									</td>
								</tr>
							</logic:iterate>
						</tbody>
					</table>
					<jsp:include page="pagination.jsp" />
				</div>
			</logic:notEmpty>
			<p id="pCenter">
				<html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg">
					<bean:message key="boton.volver" />
				</html:link>
			</p>
		</div>
	</div>
</div>
