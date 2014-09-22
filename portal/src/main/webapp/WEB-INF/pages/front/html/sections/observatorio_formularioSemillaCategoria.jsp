<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<jsp:useBean id="breadCrumbsParams" class="java.util.HashMap" />
	<bean:define id="idCat"><%= Constants.ID_CATEGORIA %></bean:define>
	<bean:parameter name="<%= Constants.ID_CATEGORIA %>" id="idCategory"/>
	<bean:parameter name="<%= Constants.ACTION %>" id="accion"/>
	<bean:define id="actionNewCS"><%= Constants.NEW_CATEGORY_SEED %></bean:define>
	<c:set target="${breadCrumbsParams}" property="${idCat}" value="${idCategory}"/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="getSeedCategories"><bean:message key="migas.categoria" /></html:link> /
			<html:link forward="editSeedCategory" name="breadCrumbsParams"><bean:message key="migas.modificar.categoria" /></html:link> /
			<logic:equal name="accion" value="<%= actionNewCS %>">
				<bean:message key="migas.nueva.semilla.observatorio"/>
			</logic:equal>
			<logic:notEqual name="accion" value="<%= actionNewCS %>">
				<bean:message key="migas.editar.semillas.observatorio"/>
			</logic:notEqual>
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="sem"> <bean:message key="gestion.semillas"/> </h1>

				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="categoria.semillas.titulo" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/CategorySeedAction" onsubmit="return validateSemillaForm(this)">
								<html:hidden property="id"/>
								<html:hidden property="categoria.id" value="<%=idCategory %>"/>
								<input type="hidden" name="<%=Constants.ACTION %>" value="<bean:write name="<%=Constants.ACTION %>"/>"/>
								<input type="hidden" name="<%=Constants.ID_CATEGORIA %>" value="<%=idCategory %>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="nombre"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="editar.semilla.observatorio.nombre" /></strong></label>
										<html:text styleClass="texto" styleId="nombre" property="nombre" />
									</div>
									<div class="formItem">
										<label for="activa"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="editar.semilla.observatorio.activa" /></strong></label>
										<html:select property="activa" styleClass="textoSelect" styleId="activa">
											<html:option value="true"><bean:message key="select.yes"/></html:option>
											<html:option value="false"><bean:message key="select.no"/></html:option>
										</html:select>
									</div>
									<div class="formItem">
										<label for="dependencia"><strong class="labelVisu"><bean:message key="editar.semilla.observatorio.dependencia" /></strong></label>
										<html:text styleClass="texto" styleId="dependencia" property="dependencia" />
									</div>
									<div class="formItem">
										<label for="acronimo"><strong class="labelVisu"><bean:message key="editar.semilla.observatorio.acronimo" /></strong></label>
										<html:text styleClass="texto" styleId="acronimo" property="acronimo" />
									</div>
									<logic:notEmpty name="<%=Constants.SEED_CATEGORIES %>">
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
									</logic:notEmpty>
									<div class="formItem">
										<p class="observ"><em><bean:message key="nueva.semilla.webs.informacion"/> </em>: <bean:message key="nueva.semilla.webs.info" /></p>
										<label for="listaUrlsString"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="editar.semilla.observatorio.url" /></strong></label>
										<html:textarea rows="5" cols="50" styleId="listaUrlsString" property="listaUrlsString" />
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div>
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> <!-- fin CONTENEDOR GRAL. -->
