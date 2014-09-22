<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="categoriesMenu"><bean:message key="migas.categoria" /></html:link> / 
			<bean:message key="migas.ver.categoria" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bullet"> <bean:message key="indice.categorias.gestion.categorias" /> </h1>

				<div id="cuerpoprincipal">
					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="ver.categoria.title" /></h2>
							<html:form method="post" action="/secure/CargarCategorias.do" styleClass="formulario">
								<fieldset>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.categoria.categoria" />: </strong></label>
										<p><bean:write name="VerCategoriaForm" property="categoria" /></p>
									</div>
									
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="ver.categoria.umbral" />: </strong></label>
										<p><bean:write name="VerCategoriaForm" property="umbral" /></p>
									</div>
									
									<h2><bean:message key="ver.categoria.terminos" /></h2>
									<logic:empty name="VerCategoriaForm" property="vectorTerminos">
										<div class="notaInformativaExito">
											<p><bean:message key="ver.categoria.termino.vacio"/></p>
										</div>
									</logic:empty>
									<logic:notEmpty name="VerCategoriaForm" property="vectorTerminos">
										<div class="formItem">
											<div class="pag">
												<table>
													<tr>
														<th><bean:message key="ver.categoria.termino" /></th>
														<th><bean:message key="ver.categoria.peso" /></th>
														<th><bean:message key="ver.categoria.normalizacion" /></th>
													</tr>
													<logic:iterate name="VerCategoriaForm" type="es.inteco.rastreador2.utils.TerminoCatVer" property="vectorTerminos" id="elemento">
														<tr>
															<td><bean:write name="elemento" property="termino" /></td>
															<td><bean:write name="elemento" property="porcentaje" /></td>
															<td><bean:write name="elemento" property="porcentajeNorm" /></td>
														</tr>
													</logic:iterate>
												</table>
												<jsp:include page="pagination.jsp" />
											</div>
										</div>
									</logic:notEmpty>
									<div class="formItem">
										<html:cancel><bean:message key="boton.volver" /></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
