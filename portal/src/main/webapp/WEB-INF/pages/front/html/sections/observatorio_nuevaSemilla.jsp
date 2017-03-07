<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="SemillaObservatorioForm"/>
	<div id="migas">
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link>
			/ <html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link>
			/ <html:link forward="observatorySeed"><bean:message key="migas.semillas.observatorio" /> </html:link>
			/ <bean:message key="migas.nueva.semilla.observatorio" />
		</p>
	</div>
	



			<div id="main">

				
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2><bean:message key="gestion.semillas.observatorio.titulo"/></h2>
							
							<jsp:include page="/common/crawler_messages.jsp" />
							<html:form action="/secure/SemillasObservatorio.do" method="get" styleClass="formulario" onsubmit="return validateSemillaObservatorioForm(this)">
								<input type="hidden" name="<%= Constants.ACTION %>" id="<%= Constants.ACTION %>" value="<%= Constants.ACCION_ANADIR %>"/>
								<fieldset>
									<div class="formItem">
										<label for="nombre"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.observatorio.nombre" /></strong></label>
										<html:text styleClass="texto" styleId="nombre" property="nombre" />
									</div>
									<div class="formItem">
										<label for="activa"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.observatorio.activa" /></strong></label>
										<html:select property="activa" styleClass="textoSelect" styleId="activa">
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									<div class="formItem">
										<label for="dependencia"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.dependencia" /></strong></label>
										<html:text styleClass="texto" styleId="dependencia" property="dependencia" />
									</div>
									<div class="formItem">
										<label for="acronimo"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.acronimo" /></strong></label>
										<html:text styleClass="texto" styleId="acronimo" property="acronimo" />
									</div>
									<div class="formItem">
										<label for="categoria"><strong class="labelVisu"><bean:message key="nueva.semilla.webs.categoria" /></strong></label>
										<html:select property="categoria.id" styleClass="textoSelect" styleId="categoria">
											<html:option value=""> - <bean:message key="select.one.femenine"/> - </html:option>	
											<logic:iterate name="<%=Constants.SEED_CATEGORIES %>" id="category">
												<bean:define id="idCategory">
													<bean:write name="category" property="id"/>
												</bean:define>
												<html:option value="<%=idCategory %>"><bean:write name="category" property="name"/></html:option>	
											</logic:iterate>
										</html:select>
									</div>
									<div class="formItem">
										<p class="observ"><em><bean:message key="nueva.semilla.webs.informacion"/> </em>: <bean:message key="nueva.semilla.webs.info" /></p>
										<label for="listaUrlsString"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.observatorio.url" /></strong></label>
										<html:textarea rows="5" cols="50" styleId="listaUrlsString" property="listaUrlsString" />
									</div>
									<div class="formItem">
										<label for="inDirectory"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.semilla.observatorio.in.directory" /></strong></label>
										<html:select property="inDirectory" styleClass="textoSelect" styleId="inDirectory">
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
									</div>
								</fieldset>
							</html:form>
							<div id="pCenter">
								<p><html:link styleClass="btn btn-default btn-lg" forward="observatorySeed"><bean:message key="boton.volver"/></html:link></p>
							</div>
							
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
