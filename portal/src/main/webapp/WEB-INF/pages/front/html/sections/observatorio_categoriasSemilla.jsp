<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<bean:message key="migas.categoria" />
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
							<logic:empty name="<%=Constants.SEED_CATEGORIES %>">
								<p>
									<bean:message key="categoria.semillas.vacia"/>
								</p>
							</logic:empty>
							<p>
								<html:link forward="newSeedCategory" styleClass="boton"><bean:message key="categoria.semillas.nueva"/></html:link>
							</p>
							<logic:notEmpty name="<%=Constants.SEED_CATEGORIES %>">
								<div class="pag">
									<table>
										<caption><bean:message key="categoria.semillas.lista"/></caption>
										<tr>
											<th><bean:message key="categoria.semillas.nombre"/></th>
											<th><bean:message key="categoria.semillas.operaciones"/></th>
										</tr>
										<logic:iterate name="<%=Constants.SEED_CATEGORIES %>" id="category">
											<tr>
												<td>
													<html:link forward="viewSeedCategory" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id">
														<bean:write name="category" property="name"/>
													</html:link>
												</td>
												<td>
													<ul class="lista_linea">
														<li><html:link forward="getCategorySeedsFile" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id"><img src="../images/xml.jpg" alt="<bean:message key="indice.rastreo.img.editar.rastreo.alt" />"/></html:link></li>
														<li><html:link forward="editSeedCategory" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id"><img src="../images/bt_modificar.gif" alt="<bean:message key="indice.rastreo.img.editar.rastreo.alt" />"/></html:link></li>
														<li><html:link forward="deleteSeedCategoryConfirmation" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id"><img src="../images/bt_eliminar.gif" alt="<bean:message key="indice.rastreo.img.eliminar.rastreo.alt" />"/></html:link></li>
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
