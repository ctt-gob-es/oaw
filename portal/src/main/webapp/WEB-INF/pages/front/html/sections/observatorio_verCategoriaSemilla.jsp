<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="getSeedCategories"><bean:message key="migas.categoria" /></html:link> /
			<bean:message key="migas.categoria.semillas"/>
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
							<h2 class="config"><bean:message key="categoria.semillas.titulo" /></h2>
							
							<html:form action="/secure/ViewSeedCategoriesAction.do" method="get" styleClass="formulario">
								<input type="hidden" name="<%= Constants.ACTION %>" value="<%= Constants.VIEW_SEED_CATEGORY %>"/>
								<bean:define id="categoryId" name="<%=Constants.CATEGORIA_FORM %>" property="id" />
								<input type="hidden" name="<%= Constants.ID_CATEGORIA %>" value="<%= categoryId %>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="nombre"><strong class="labelVisu"><bean:message key="nueva.semilla.observatorio.nombre" /></strong></label>
										<html:text styleClass="texto" styleId="nombre" property="nombre" />
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
									</div>
								</fieldset>
							</html:form>
		
							<div class="detail">
								<div class="formItem">
									<strong class="labelVisu"><bean:message key="categoria.semillas.nombre" />: </strong>
									<bean:write name="<%=Constants.CATEGORIA_FORM %>" property="name" />
								</div>
								
								<logic:empty name="<%=Constants.CATEGORIA_FORM %>" property="seeds">
									<p><bean:message key="categoria.semillas.asociadas.vacio"/></p>	
								</logic:empty>
								<logic:notEmpty name="<%=Constants.CATEGORIA_FORM %>" property="seeds">
									<div class="pag">
										<table>
											<caption><bean:message key="categoria.semillas.lista.semillas"/></caption>
											<tr>
												<th><bean:message key="categoria.semillas.nombre"/></th>
											</tr>
											<logic:iterate name="<%=Constants.CATEGORIA_FORM %>" property="seeds" id="seed">
												<tr>
													<td>
														<bean:write name="seed" property="nombre"/>
														<logic:iterate name="seed" property="listaUrls" id="url" length="1">
															<bean:define id="altLink">
																<bean:message key="categoria.semillas.enlace.externo">
																	<jsp:attribute name="arg0">
																		<bean:write name="seed" property="nombre"/>
																	</jsp:attribute>
																</bean:message>
															</bean:define>
														
															<a href="<bean:write name="url"/>">
																<img src="../images/external_link.gif" alt="<%=altLink %>"/>
															</a>
														</logic:iterate>
													</td>
												</tr>
											</logic:iterate>
										</table>
										<jsp:include page="pagination.jsp" />
									</logic:notEmpty>
								</div>
							</div>
						</div>	
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> <!-- fin CONTENEDOR GRAL. -->
