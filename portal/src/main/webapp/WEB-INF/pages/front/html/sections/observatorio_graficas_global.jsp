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
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml />
<inteco:sesion action="ifConfigAdmin">
	<bean:parameter id="id" name="<%=Constants.ID%>" />
	<bean:parameter id="id_observatorio" name="<%=Constants.ID_OBSERVATORIO%>" />
	<bean:parameter id="idObservatorio" name="<%=Constants.ID_OBSERVATORIO%>" />
	<bean:parameter id="observatoryType" name="<%=Constants.TYPE_OBSERVATORY%>" />
	<bean:define id="grParam"><%=Constants.GRAPHIC%></bean:define>
	<bean:define id="grValue"><%=Constants.OBSERVATORY_GRAPHIC_INITIAL%></bean:define>
	<bean:define id="grRegenerate"><%=Constants.OBSERVATORY_GRAPHIC_GLOBAL%></bean:define>
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="id" value="${id}" />
	<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
	<c:set target="${params}" property="${grParam}" value="${grValue}" />
	<c:set target="${params}" property="Otype" value="${observatoryType}" />
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
					<li>
						<html:link forward="resultadosPrimariosObservatorio" paramName="idObservatorio"
							paramId="<%=Constants.ID_OBSERVATORIO%>">
							<bean:message key="migas.indice.observatorios.realizados.lista" />
						</html:link>
					</li>
					<li>
						<html:link forward="getObservatoryGraphic" name="params">
							<bean:message key="migas.indice.observatorios.menu.graficas" />
						</html:link>
					</li>
					<li class="active">
						<bean:message key="migas.indice.observatorios.menu.graficas.global" />
					</li>
				</ol>
			</div>
			<div id="cajaformularios">
				<h2>
					<bean:message key="indice.observatorios.menu.graficas.global" />
				</h2>
				<jsp:include page="/common/crawler_messages.jsp" />
				<logic:equal name="<%=Constants.OBSERVATORY_RESULTS%>" value="<%=Constants.SI%>">
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG%>">
						<h3>
							<bean:message key="resultados.anonimos.nivel.accesibilidad.global.title" />
						</h3>
						<div class="graphicInfo2">
							<table>
								<tr>
									<th>
										<bean:message key="resultados.anonimos.level" />
									</th>
									<th>
										<bean:message key="resultados.anonimos.porc.portales" />
									</th>
									<th>
										<bean:message key="resultados.anonimos.num.portales" />
									</th>
								</tr>
								<logic:iterate id="item" name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG%>">
									<tr>
										<td>
											<bean:write name="item" property="adecuationLevel" />
										</td>
										<td>
											<bean:write name="item" property="percentageP" />
										</td>
										<td>
											<bean:write name="item" property="numberP" />
										</td>
									</tr>
								</logic:iterate>
							</table>
						</div>
						<div class="divCenter">
							<img
								src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_ALLOCATION%>"></img>
						</div>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DCG%>">
						<h3>
							<bean:message key="observatorio.nivel.cumplimiento" />
						</h3>
						<div class="graphicInfo2">
							<table>
								<tr>
									<th>
										<bean:message key="resultados.anonimos.level" />
									</th>
									<th>
										<bean:message key="resultados.anonimos.porc.portales" />
									</th>
									<th>
										<bean:message key="resultados.anonimos.num.portales" />
									</th>
								</tr>
								<logic:iterate id="item" name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DCG%>">
									<tr>
										<td>
											<bean:write name="item" property="adecuationLevel" />
										</td>
										<td>
											<bean:write name="item" property="percentageP" />
										</td>
										<td>
											<bean:write name="item" property="numberP" />
										</td>
									</tr>
								</logic:iterate>
							</table>
						</div>
						<div class="divCenter">
							<img
								src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_COMPLIANCE%>"></img>
						</div>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPS%>">
						<h3>
							<bean:message key="observatory.graphic.global.puntuation.compilance.segments.mark.title" />
						</h3>
						<div class="graphicInfo2">
							<logic:iterate name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPS%>" id="item">
								<bean:define id="categoryForm" type="es.inteco.rastreador2.actionform.semillas.CategoriaForm" name="item"
									property="category" />
								<table>
									<caption class="global-results-caption">
										<bean:write name="categoryForm" property="name" />
									</caption>
									<tr>
										<th>
											<bean:message key="resultados.anonimos.level" />
										</th>
										<th>
											<bean:message key="resultados.anonimos.porc.portales" />
										</th>
									</tr>
									<logic:iterate id="item2" name="item" property="viewList">
										<tr>
											<td>
												<bean:write name="item2" property="label" />
											</td>
											<td>
												<bean:write name="item2" property="value" />
												%
											</td>
										</tr>
									</logic:iterate>
								</table>
							</logic:iterate>
						</div>
						<logic:notEmpty name="<%=Constants.OBSERVATORY_NUM_CMPS_GRAPH%>">
							<bean:define id="numCPS" name="<%=Constants.OBSERVATORY_NUM_CMPS_GRAPH%>" type="java.lang.Integer" />
							<c:forEach var="count" begin="1" end="${numCPS}">
								<bean:define id="countBean">
									<c:out value="${count}" />
								</bean:define>
								<div class="divCenter">
									<img
										src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_SEGMENTS_CMP_MARK%>&amp;<%=Constants.OBSERVATORY_NUM_GRAPH%>=<%=countBean%>"></img>
								</div>
							</c:forEach>
						</logic:notEmpty>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPS_AMBIT%>">
						<h3>
							<bean:message key="observatory.graphic.global.puntuation.compliance.ambit.mark.title" />
						</h3>
						<div class="graphicInfo2">
							<logic:iterate name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPS_AMBIT%>" id="item">
								<bean:define id="ambitForm" type="es.inteco.rastreador2.actionform.semillas.AmbitoForm" name="item"
									property="ambit" />
								<table>
									<caption class="global-results-caption">
										<bean:write name="ambitForm" property="name" />
									</caption>
									<tr>
										<th>
											<bean:message key="resultados.anonimos.level" />
										</th>
										<th>
											<bean:message key="resultados.anonimos.porc.portales" />
										</th>
									</tr>
									<logic:iterate id="item2" name="item" property="viewList">
										<tr>
											<td>
												<bean:write name="item2" property="label" />
											</td>
											<td>
												<bean:write name="item2" property="value" />
												%
											</td>
										</tr>
									</logic:iterate>
								</table>
							</logic:iterate>
						</div>
						<logic:notEmpty name="<%=Constants.OBSERVATORY_NUM_CMPS_AMBIT_GRAPH%>">
							<bean:define id="numCPS" name="<%=Constants.OBSERVATORY_NUM_CMPS_AMBIT_GRAPH%>" type="java.lang.Integer" />
							<c:forEach var="count" begin="1" end="${numCPS}">
								<bean:define id="countBean">
									<c:out value="${count}" />
								</bean:define>
								<div class="divCenter">
									<img
										src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_AMBIT_MARK%>&amp;<%=Constants.OBSERVATORY_NUM_GRAPH%>=<%=countBean%>"></img>
								</div>
							</c:forEach>
						</logic:notEmpty>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS%>">
						<h3>
							<bean:message key="resultados.anonimos.nivel.adecuacion.segmento.title" />
						</h3>
						<div class="graphicInfo2">
							<logic:iterate name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS%>" id="item">
								<bean:define id="categoryForm" type="es.inteco.rastreador2.actionform.semillas.CategoriaForm" name="item"
									property="category" />
								<table>
									<caption class="global-results-caption">
										<bean:write name="categoryForm" property="name" />
									</caption>
									<tr>
										<th>
											<bean:message key="resultados.anonimos.level" />
										</th>
										<th>
											<bean:message key="resultados.anonimos.porc.portales" />
										</th>
									</tr>
									<logic:iterate id="item2" name="item" property="viewList">
										<tr>
											<td>
												<bean:write name="item2" property="label" />
											</td>
											<td>
												<bean:write name="item2" property="value" />
												%
											</td>
										</tr>
									</logic:iterate>
								</table>
							</logic:iterate>
						</div>
						<logic:notEmpty name="<%=Constants.OBSERVATORY_NUM_CPS_GRAPH%>">
							<bean:define id="numCPS" name="<%=Constants.OBSERVATORY_NUM_CPS_GRAPH%>" type="java.lang.Integer" />
							<c:forEach var="count" begin="1" end="${numCPS}">
								<bean:define id="countBean">
									<c:out value="${count}" />
								</bean:define>
								<div class="divCenter">
									<img
										src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_SEGMENTS_MARK%>&amp;<%=Constants.OBSERVATORY_NUM_GRAPH%>=<%=countBean%>"></img>
								</div>
							</c:forEach>
						</logic:notEmpty>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS%>">
						<h3>
							<bean:message key="resultados.anonimos.comparacion.puntuacion.segmento.title" />
						</h3>
						<div class="graphicInfo2">
							<logic:iterate name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS%>" id="item">
								<bean:define id="categoryForm" type="es.inteco.rastreador2.actionform.semillas.CategoriaForm" name="item"
									property="category" />
								<table>
									<caption class="global-results-caption">
										<bean:write name="categoryForm" property="name" />
									</caption>
									<tr>
										<th>
											<bean:message key="resultados.anonimos.level" />
										</th>
										<th>
											<bean:message key="resultados.anonimos.punt.portales" />
										</th>
									</tr>
									<logic:iterate id="item2" name="item" property="viewList">
										<tr>
											<td>
												<bean:write name="item2" property="label" />
											</td>
											<td>
												<bean:write name="item2" property="value" />
											</td>
										</tr>
									</logic:iterate>
								</table>
							</logic:iterate>
						</div>
						<logic:notEmpty name="<%=Constants.OBSERVATORY_NUM_CAS_GRAPH%>">
							<bean:define id="numCAS" name="<%=Constants.OBSERVATORY_NUM_CAS_GRAPH%>" type="java.lang.Integer" />
							<c:forEach var="count" begin="1" end="${numCAS}">
								<bean:define id="countBean">
									<c:out value="${count}" />
								</bean:define>
								<div class="divCenter">
									<img
										src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_GROUP_SEGMENT_MARK%>&amp;<%=Constants.OBSERVATORY_NUM_GRAPH%>=<%=countBean%>"></img>
								</div>
							</c:forEach>
						</logic:notEmpty>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA%>">
						<div class="graphicInfo2">
							<h3>
								<bean:message key="resultados.anonimos.comparacion.medias.aspecto.title" />
							</h3>
							<table>
								<tr>
									<th>
										<bean:message key="resultados.anonimos.aspect" />
									</th>
									<th>
										<bean:message key="resultados.anonimos.punt.media" />
									</th>
								</tr>
								<logic:iterate id="item" name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA%>">
									<tr>
										<td>
											<bean:write name="item" property="label" />
										</td>
										<td>
											<bean:write name="item" property="value" />
										</td>
									</tr>
								</logic:iterate>
							</table>
						</div>
						<div class="divCenter">
							<img
								src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_MID_ASPECT%>"></img>
						</div>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI%>">
						<h3>
							<bean:message key="resultados.anonimos.comparacion.medias.verificacion.I.title" />
						</h3>
						<div class="graphicInfo2">
							<table>
								<tr>
									<th>
										<bean:message key="resultados.anonimos.verification" />
									</th>
									<th>
										<bean:message key="resultados.anonimos.punt.media" />
									</th>
								</tr>
								<logic:iterate id="item" name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI%>">
									<tr>
										<td>
											<bean:write name="item" property="label" />
										</td>
										<td>
											<bean:write name="item" property="value" />
										</td>
									</tr>
								</logic:iterate>
							</table>
						</div>
						<div class="divCenter">
							<img
								src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N1%>"></img>
						</div>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII%>">
						<h3>
							<bean:message key="resultados.anonimos.comparacion.medias.verificacion.II.title" />
						</h3>
						<div class="graphicInfo2">
							<table>
								<tr>
									<th>
										<bean:message key="resultados.anonimos.verification" />
									</th>
									<th>
										<bean:message key="resultados.anonimos.punt.media" />
									</th>
								</tr>
								<logic:iterate id="item" name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII%>">
									<tr>
										<td>
											<bean:write name="item" property="label" />
										</td>
										<td>
											<bean:write name="item" property="value" />
										</td>
									</tr>
								</logic:iterate>
							</table>
						</div>
						<div class="divCenter">
							<img
								src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N2%>"></img>
						</div>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I%>">
						<h3>
							<bean:message key="observatory.graphic.modality.by.verification.level.1.title" />
						</h3>
						<div class="graphicInfo2">
							<table>
								<tr>
									<th>
										<bean:message key="observatory.graphic.verification" />
									</th>
									<th>
										<bean:message key="observatory.graphic.modality.green" />
									</th>
									<th>
										<bean:message key="observatory.graphic.modality.red" />
									</th>
								</tr>
								<logic:iterate id="item" name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I%>"
									type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
									<tr>
										<td>
											<bean:message key="<%=item.getVerification()%>" />
										</td>
										<td><%=item.getGreenPercentage()%>
										</td>
										<td><%=item.getRedPercentage()%>
										</td>
									</tr>
								</logic:iterate>
							</table>
						</div>
						<div class="divCenter">
							<img
								src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N1%>"></img>
						</div>
					</logic:notEmpty>
					<logic:notEmpty name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II%>">
						<h3>
							<bean:message key="observatory.graphic.modality.by.verification.level.2.title" />
						</h3>
						<div class="graphicInfo2">
							<table>
								<tr>
									<th>
										<bean:message key="observatory.graphic.verification" />
									</th>
									<th>
										<bean:message key="observatory.graphic.modality.green" />
									</th>
									<th>
										<bean:message key="observatory.graphic.modality.red" />
									</th>
								</tr>
								<logic:iterate id="item" name="<%=Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II%>"
									type="es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm">
									<tr>
										<td>
											<bean:message key="<%=item.getVerification()%>" />
										</td>
										<td><%=item.getGreenPercentage()%>
										</td>
										<td><%=item.getRedPercentage()%>
										</td>
									</tr>
								</logic:iterate>
							</table>
						</div>
						<div class="divCenter">
							<img
								src="<%=request.getContextPath()%>/secure/GraficasObservatorio.do?<%=Constants.TYPE_OBSERVATORY%>=<%=observatoryType%>&amp;<%=Constants.ID%>=<%=id%>&amp;<%=Constants.ID_OBSERVATORIO%>=<%=id_observatorio%>&amp;<%=Constants.GRAPHIC%>=<%=Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N2%>"></img>
						</div>
					</logic:notEmpty>
				</logic:equal>
				<p id="pCenter">
					<c:set target="${params}" property="${grParam}" value="${grRegenerate}" />
					<html:link forward="regenerateGraphicIntav" name="params" styleClass="btn btn-primary btn-lg">
						<bean:message key="boton.regenerar.resultados" />
					</html:link>
					<c:set target="${params}" property="${grParam}" value="${grValue}" />
					<html:link forward="getObservatoryGraphic" name="params" styleClass="btn btn-default btn-lg">
						<bean:message key="boton.volver" />
					</html:link>
				</p>
			</div>
			<!-- fin cajaformularios -->
		</div>
		<!-- container dch -->
	</div>
	<!-- main -->
</inteco:sesion>
