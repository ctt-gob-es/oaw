<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="CategoriaForm"/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="getSeedCategories"><bean:message key="migas.categoria" /></html:link> /
			<logic:equal parameter="<%= Constants.ACTION %>" value="<%= Constants.NEW_SEED_CATEGORY %>" >
				<bean:message key="migas.nueva.categoria" />
			</logic:equal>
			<logic:equal parameter="<%= Constants.ACTION %>" value="<%= Constants.EDIT_SEED_CATEGORY %>" >
				<bean:message key="migas.modificar.categoria" />
			</logic:equal>
			
		</p>
	</div>
	
	<logic:present parameter="<%=Constants.ID_CATEGORIA %>">
		<bean:parameter name="<%=Constants.ID_CATEGORIA %>" id="idcat"/>
	</logic:present>
	
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
							
							<p>
								<bean:message key="categoria.semillas.fichero.info">
									<jsp:attribute name="arg0">
										<a href="../xml/seeds.xml" title="<bean:message key="categoria.semillas.fichero.ejemplo.title"/>"><bean:message key="categoria.semillas.fichero.ejemplo"/></a>
									</jsp:attribute>
								</bean:message>
							</p>
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form styleClass="formulario" method="post" action="/secure/SeedCategoriesAction" enctype="multipart/form-data" onsubmit="return validateCategoriaForm(this)">
								<html:hidden property="id"/>
								<input type="hidden" name="<%=Constants.ACTION %>" value="<bean:write name="<%=Constants.ACTION %>"/>"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="name"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="categoria.semillas.nombre" />: </strong></label>
										<html:text styleClass="texto" property="name" styleId="name" maxlength="30"/>
									</div>
									<div class="formItem">
                                        <label for="orden"><strong class="labelVisu"><bean:message key="categoria.semillas.orden" />: </strong></label>
                                        <html:select styleClass="textoSelect" styleId="orden"  property="orden" >
                                            <option value="1" <c:if test="${CategoriaForm.orden==1}">selected="selected"</c:if>>1</option>
                                            <option value="2" <c:if test="${CategoriaForm.orden==2}">selected="selected"</c:if>>2</option>
                                            <option value="3" <c:if test="${CategoriaForm.orden==3}">selected="selected"</c:if>>3</option>
                                            <option value="4" <c:if test="${CategoriaForm.orden==4}">selected="selected"</c:if>>4</option>
                                            <option value="5" <c:if test="${CategoriaForm.orden==5}">selected="selected"</c:if>>5</option>
                                            <option value="6" <c:if test="${CategoriaForm.orden==6}">selected="selected"</c:if>>6</option>
                                            <option value="7" <c:if test="${CategoriaForm.orden==7}">selected="selected"</c:if>>7</option>
                                            <option value="8" <c:if test="${CategoriaForm.orden==8}">selected="selected"</c:if>>8</option>
                                            <option value="9" <c:if test="${CategoriaForm.orden==9}">selected="selected"</c:if>>9</option>
                                            <option value="10" <c:if test="${CategoriaForm.orden==10}">selected="selected"</c:if>>10</option>
                                        </html:select>
                                    </div>
									<div class="formItem">
										<label for="fileSeeds"><strong class="labelVisu"><bean:message key="categoria.semillas.fichero" />: </strong></label>
										<html:file styleClass="texto" property="fileSeeds" styleId="fileSeeds"/>
									</div>
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar" /></html:submit>
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
							
							<logic:present name="<%=Constants.ID_CATEGORIA %>">
								<html:link forward="newCategorySeed" styleClass="boton" paramName="<%=Constants.ID_CATEGORIA %>" paramId="<%=Constants.ID_CATEGORIA %>"><bean:message key="categoria.semillas.nueva.semilla"/></html:link>
							</logic:present>
							
							<logic:notEmpty name="<%=Constants.CATEGORIA_FORM %>" property="seeds">
								<div class="pag">
									<table>
										<caption><bean:message key="categoria.semillas.lista.semillas"/></caption>
										<tr>
											<th><bean:message key="categoria.semillas.nombre"/></th>
											<th><bean:message key="categoria.semillas.operaciones"/></th>
										</tr>
										<logic:iterate name="<%=Constants.CATEGORIA_FORM %>" property="seeds" id="seed">
											<jsp:useBean id="params" class="java.util.HashMap" />
											<c:set target="${params}" property="idcat" value="${CategoriaForm.id}" />
											<c:set target="${params}" property="idSemilla" value="${seed.id}" />
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
												<td>
													<ul class="lista_linea">
														<li><html:link forward="editCategorySeed" name="params"><img src="../images/bt_modificar.gif" alt="<bean:message key="indice.rastreo.img.editar.rastreo.alt" />"/></html:link></li>
														<li><html:link forward="deleteCategorySeedConfirmation" name="params"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.rastreo.img.eliminar.rastreo.alt" />"/></html:link></li>
													</ul>
												</td>
											</tr>
										</logic:iterate>
									</table>
									<jsp:include page="pagination.jsp" />
								</div>
							</logic:notEmpty>
						</div>
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> <!-- fin CONTENEDOR GRAL. -->
