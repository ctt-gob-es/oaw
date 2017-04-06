<%@ include file="/common/taglibs.jsp" %>
<html:xhtml/>
<inteco:sesion action="ifConfigAdmin">

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			 / <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> 
			 / <bean:message key="migas.ver.rastreo" />
		 </p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="ver.rastreo.title" /></h2>
							<html:form styleClass="formulario" method="post" action="/secure/CargarRastreos.do">
								<fieldset>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.rastreo.nombre" />: </strong></label>
										<p><bean:write name="VerRastreoForm" property="rastreo" /></p>
									</div>
									<logic:notEmpty name="VerRastreoForm" property="cuentaCliente">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.cuenta.cliente" />: </strong></label>
											<p><bean:write name="VerRastreoForm" property="cuentaCliente" /></p>
										</div>
									</logic:notEmpty>
									<logic:notEmpty name="VerRastreoForm" property="automatico">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.programado" />: </strong></label>
											<logic:equal value="1" name="VerRastreoForm" property="automatico">
												<p><bean:message key="si" /></p>
											</logic:equal>
											<logic:equal value="0" name="VerRastreoForm" property="automatico">
												<p><bean:message key="no" /></p>
											</logic:equal>
										</div>
									</logic:notEmpty>
									<logic:notEmpty name="VerRastreoForm" property="activo">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.activo" />: </strong></label>
											<logic:equal value="1" name="VerRastreoForm" property="activo">
												<p><bean:message key="si" /></p>
											</logic:equal>
											<logic:equal value="0" name="VerRastreoForm" property="activo">
												<p><bean:message key="no" /></p>
											</logic:equal>
										</div>
									</logic:notEmpty>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.rastreo.fecha.creacion" />: </strong></label>
										<p><bean:write name="VerRastreoForm" property="fecha" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.rastreo.cartucho" />: </strong></label>
										<c:set var="cartuchoName" value="${VerRastreoForm.nombre_cartucho}"/>
										<p><inteco:trunp cad="cartuchoName"/></p>
									</div>
									<logic:notEmpty name="VerRastreoForm" property="normaAnalisisSt">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.norma" />: </strong></label>
											<p><bean:write name="VerRastreoForm" property="normaAnalisisSt" /></p>
										</div>
									</logic:notEmpty>
									<logic:notEmpty name="VerRastreoForm" property="url_semilla">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.url.semilla" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="semillas" name="VerRastreoForm" property="url_semilla">
														<li><bean:write name="semillas" /></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									<logic:notEmpty name="VerRastreoForm" property="listaRastreable">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.lista.rastreable" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="l_rastreable" name="VerRastreoForm" property="url_listaRastreable">
														<li><bean:write name="l_rastreable" /></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									<logic:notEmpty name="VerRastreoForm" property="listaNoRastreable">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.lista.no.rastreable" />: </strong></label>
											<div class="alinForm">
												<ul class="sublista">
													<logic:iterate id="l_no_rastreable" name="VerRastreoForm" property="url_listaNoRastreable">
														<li><bean:write name="l_no_rastreable" /></li>
													</logic:iterate>
												</ul>
											</div>
										</div>
									</logic:notEmpty>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.rastreo.profundidad" />: </strong></label>
										<p><bean:write name="VerRastreoForm" property="profundidad" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.rastreo.paginas.nivel" />: </strong></label>
										<bean:define id="unlimitedTopN">
											<inteco:properties key="amplitud.ilimitada.value" file="crawler.core.properties" />
										</bean:define>
										<logic:equal name="VerRastreoForm" property="topN_ver" value="<%=unlimitedTopN %>">
											<bean:message key="nuevo.rastreo.amplitud.ilimitada" />
										</logic:equal>
										<logic:notEqual name="VerRastreoForm" property="topN_ver" value="<%=unlimitedTopN %>">
											<p><bean:write name="VerRastreoForm" property="topN_ver" /></p>
										</logic:notEqual>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.rastreo.pseudoaleatorio" />: </strong></label>
										<logic:equal name="VerRastreoForm" property="pseudoAleatorio" value="true">
											<bean:message key="select.yes"/>
										</logic:equal>
										<logic:equal name="VerRastreoForm" property="pseudoAleatorio" value="false">
											<bean:message key="select.no"/>
										</logic:equal>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.rastreo.in.directory" />: </strong></label>
										<logic:equal name="VerRastreoForm" property="inDirectory" value="true">
											<bean:message key="select.yes"/>
										</logic:equal>
										<logic:equal name="VerRastreoForm" property="inDirectory" value="false">
											<bean:message key="select.no"/>
										</logic:equal>
									</div>
									<logic:notEmpty name="VerRastreoForm" property="fechaLanzado">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.fecha.lanzado" />: </strong></label>
											<p><bean:write name="VerRastreoForm" property="fechaLanzado" /></p>
										</div>
									</logic:notEmpty>
									<logic:empty name="VerRastreoForm" property="fechaLanzado">
										<div class="formItem">
											<label><strong class="labelVisu"><bean:message key="ver.rastreo.fecha.lanzado" />: </strong></label>
											<p><bean:message key="ver.rastreo.fecha.lanzado.no.lanzado" /></p>
										</div>
									</logic:empty>
									<div class="formItem">
										<html:cancel><bean:message key="boton.aceptar" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>		
	</div> 

</inteco:sesion>
