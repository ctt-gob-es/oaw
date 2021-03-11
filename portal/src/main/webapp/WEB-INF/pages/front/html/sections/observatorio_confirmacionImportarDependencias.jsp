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
				<li>
					<html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link>
				</li>
				<li class="active">
					<bean:message key="migas.import.dependencies" />
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2>
				<bean:message key="importar.dependencias.title" />
			</h2>
			<div class="detail">
				<p>
					<bean:message key="importar.semillas.info" />
				</p>
				<logic:present name="errorDependencies">
					<logic:notEmpty name="errorDependencies">
						<h3>
							<bean:message key="importar.dependencias.error.title" />
						</h3>
						<bean:size id="beansize" name="errorDependencies" />
						<bean:message key="importar.dependencias.error.title.total">
							<jsp:attribute name="arg0">
                            <bean:write name="beansize" />
                        </jsp:attribute>
						</bean:message>
						<logic:iterate name="errorDependencies" id="dependency">
							<table class="table table-stripped table-bordered table-hover">
								<caption>
									<bean:message key="lista.semillas.observatorio" />
								</caption>
								<colgroup>
									<col style="width: 50%">
									<col style="width: 10%">
									<col style="width: 10%">
									<col style="width: 10%">
									<col style="width: 20%">
								</colgroup>
								<tbody>
									<tr>
										<th>
											<bean:message key="colname.name" />
										</th>
										<th>
											<bean:message key="colname.province" />
										</th>
										<th>
											<bean:message key="colname.official" />
										</th>
										<th>
											<bean:message key="colname.email" />
										</th>
										<th>
											<bean:message key="cargar.semilla.observatorio.errores" />
										</th>
									</tr>
									<tr>
										<td>
											<bean:write name="dependency" property="name" />
										</td>
										<td>
											<logic:notEmpty name="dependency" property="tag">
												<bean:write name="dependency" property="tag.name" />
											</logic:notEmpty>
										</td>
										<td>
											<logic:equal name="dependency" property="official" value="true">
												<bean:message key="si" />
											</logic:equal>
											<logic:equal name="dependency" property="official" value="false">
												<bean:message key="no" />
											</logic:equal>
										</td>
										<td>
											<bean:write name="dependency" property="emails" />
										</td>
										<td>
											<ul class="seedImportErrorList">
												<logic:iterate name="dependency" property="errors" id="error">
													<li>
														<bean:write name="error" />
													</li>
												</logic:iterate>
											</ul>
										</td>
									</tr>
								</tbody>
							</table>
						</logic:iterate>
					</logic:notEmpty>
				</logic:present>
				<!-- Updated -->
				<logic:present name="updatedDependencies">
					<logic:notEmpty name="updatedDependencies">
						<h3>
							<bean:message key="importar.dependencias.updated.title" />
						</h3>
						<bean:size id="beansize" name="updatedDependencies" />
						<bean:message key="importar.dependencias.updated.title.total">
							<jsp:attribute name="arg0">
                            <bean:write name="beansize" />
                        </jsp:attribute>
						</bean:message>
						<logic:iterate name="updatedDependencies" id="dependency">
							<table class="table table-stripped table-bordered table-hover">
								<caption>
									<bean:message key="lista.semillas.observatorio" />
								</caption>
								<colgroup>
									<col style="width: 10%">
									<col style="width: 40%">
									<col style="width: 10%">
									<col style="width: 10%">
									<col style="width: 30%">
								</colgroup>
								<tbody>
									<tr>
										<th></th>
										<th>
											<bean:message key="colname.name" />
										</th>
										<th>
											<bean:message key="colname.province" />
										</th>
										<th>
											<bean:message key="colname.official" />
										</th>
										<th>
											<bean:message key="colname.email" />
										</th>
									</tr>
									<tr>
										<td>Valor actual</td>
										<td>
											<bean:write name="dependency" property="dependency.name" />
										</td>
										<td>
											<logic:notEmpty name="dependency" property="dependency.tag">
												<bean:write name="dependency" property="dependency.tag.name" />
											</logic:notEmpty>
										</td>
										<td>
											<logic:equal name="dependency" property="dependency.official" value="true">
												<bean:message key="si" />
											</logic:equal>
											<logic:equal name="dependency" property="dependency.official" value="false">
												<bean:message key="no" />
											</logic:equal>
										</td>
										<td>
											<bean:write name="dependency" property="dependency.emails" />
										</td>
									</tr>
									<tr>
										<td>Valor nuevo</td>
										<td>
											<bean:write name="dependency" property="newDependency.name" />
										</td>
										<td class="<c:if test="${dependency.sameProvince!=true}">warning-import</c:if>">
											<logic:notEmpty name="dependency" property="newDependency.tag">
												<bean:write name="dependency" property="newDependency.tag.name" />
											</logic:notEmpty>
										</td>
										<td class="<c:if test="${dependency.sameOfficial!=true}">warning-import</c:if>">
											<logic:equal name="dependency" property="newDependency.official" value="true">
												<bean:message key="si" />
											</logic:equal>
											<logic:equal name="dependency" property="newDependency.official" value="false">
												<bean:message key="no" />
											</logic:equal>
										</td>
										<td class="<c:if test="${dependency.sameEmails!=true}">warning-import</c:if>">
											<bean:write name="dependency" property="newDependency.emails" />
										</td>
									</tr>
								</tbody>
							</table>
						</logic:iterate>
					</logic:notEmpty>
				</logic:present>
				<!-- New -->
				<logic:present name="newDependencies">
					<logic:notEmpty name="newDependencies">
						<h3>
							<bean:message key="importar.dependencias.new.title" />
						</h3>
						<bean:size id="beansize" name="newDependencies" />
						<bean:message key="importar.dependencias.new.title.total">
							<jsp:attribute name="arg0">
                            <bean:write name="beansize" />
                        </jsp:attribute>
						</bean:message>
						<logic:iterate name="newDependencies" id="dependency">
							<table class="table table-stripped table-bordered table-hover">
								<caption>
									<bean:message key="lista.semillas.observatorio" />
								</caption>
								<colgroup>
									<col style="width: 50%">
									<col style="width: 10%">
									<col style="width: 10%">
									<col style="width: 30%">
								</colgroup>
								<tbody>
									<tr>
										<th>
											<bean:message key="colname.name" />
										</th>
										<th>
											<bean:message key="colname.province" />
										</th>
										<th>
											<bean:message key="colname.official" />
										</th>
										<th>
											<bean:message key="colname.email" />
										</th>
									</tr>
									<tr>
										<td>
											<bean:write name="dependency" property="name" />
										</td>
										<td>
											<logic:notEmpty name="dependency" property="tag">
												<bean:write name="dependency" property="tag.name" />
											</logic:notEmpty>
										</td>
										<td>
											<logic:equal name="dependency" property="official" value="true">
												<bean:message key="si" />
											</logic:equal>
											<logic:equal name="dependency" property="official" value="false">
												<bean:message key="no" />
											</logic:equal>
										</td>
										<td>
											<bean:write name="dependency" property="emails" />
										</td>
									</tr>
								</tbody>
							</table>
						</logic:iterate>
					</logic:notEmpty>
				</logic:present>
				<!-- Inalterable -->
				<logic:present name="inalterableDependencies">
					<logic:notEmpty name="inalterableDependencies">
						<h3>
							<bean:message key="importar.dependencias.inalterable.title" />
						</h3>
						<bean:size id="beansize" name="inalterableDependencies" />
						<bean:message key="importar.dependencias.inalterable.title.total">
							<jsp:attribute name="arg0">
                            <bean:write name="beansize" />
                        </jsp:attribute>
						</bean:message>
						<logic:iterate name="inalterableDependencies" id="dependency">
							<table class="table table-stripped table-bordered table-hover">
								<caption>
									<bean:message key="lista.semillas.observatorio" />
								</caption>
								<colgroup>
									<col style="width: 50%">
									<col style="width: 10%">
									<col style="width: 10%">
									<col style="width: 30%">
								</colgroup>
								<tbody>
									<tr>
										<th>
											<bean:message key="colname.name" />
										</th>
										<th>
											<bean:message key="colname.province" />
										</th>
										<th>
											<bean:message key="colname.official" />
										</th>
										<th>
											<bean:message key="colname.email" />
										</th>
									</tr>
									<tr>
										<td>
											<bean:write name="dependency" property="name" />
										</td>
										<td>
											<logic:notEmpty name="dependency" property="tag">
												<bean:write name="dependency" property="tag.name" />
											</logic:notEmpty>
										</td>
										<td>
											<logic:equal name="dependency" property="official" value="true">
												<bean:message key="si" />
											</logic:equal>
											<logic:equal name="dependency" property="official" value="false">
												<bean:message key="no" />
											</logic:equal>
										</td>
										<td>
											<bean:write name="dependency" property="emails" />
										</td>
									</tr>
								</tbody>
							</table>
						</logic:iterate>
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
					<c:set target="${paramsSI}" property="action" value="importAll" />
					<c:set target="${paramsSI}" property="confirmacion" value="${confSi}" />
					<html:link styleClass="btn btn-primary btn-lg" forward="importAllDependencies" name="paramsSI">
						<bean:message key="boton.aceptar" />
					</html:link>
					<html:link styleClass="btn btn-default btn-lg" forward="observatoryDependencias">
						<bean:message key="boton.cancelar" />
					</html:link>
				</div>
			</div>
		</div>
		<!-- fin cajaformularios -->
	</div>
</div>
