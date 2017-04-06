<%@ include file="/common/taglibs.jsp" %>
<html:xhtml/>
<html:javascript formName="NuevaCategoriaForm"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="categoriesMenu"><bean:message key="migas.categoria" /></html:link> / 
			<bean:message key="migas.nueva.categoria" />
		</p>
	</div>
	



			<div id="main">

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="nueva.categoria.title" /></h2>
							
							<p><bean:message key="leyenda.campo.obligatorio" /></p>
							
							<html:form method="post" styleClass="formulario" action="/secure/NuevaCategoria.do" onsubmit="return validateNuevaCategoriaForm(this)">
								<input type="hidden" name="actionFor" value="anadir"/>
								<fieldset>
									<jsp:include page="/common/crawler_messages.jsp" />
									<div class="formItem">
										<label for="nombre"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.categoria.nombre" /> </strong></label>
										<html:text styleClass="texto" styleId="nombre"  maxlength="50" property="nombre"/>
									</div>
									
									<div class="formItem">
										<label for="umbral"><strong class="labelVisu"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><bean:message key="nueva.categoria.umbral" /> </strong></label>
										<html:text styleClass="texto" styleId="umbral" property="umbral"/>
									</div>
									
									<div class="formItem">
										<label for="termino1"><strong class="labelVisu"><bean:message key="nueva.categoria.termino1" /></strong></label>
										<html:text styleClass="texto" styleId="termino1" property="termino1" maxlength="50"/>
									</div>
									
									<div class="formItem">
										<label for="peso1"><strong class="labelVisu"><bean:message key="nueva.categoria.peso" /> </strong></label>
										<html:text styleClass="texto" styleId="peso1" property="peso1"/>
									</div>
									
									<div class="formItem">
										<label for="termino2"><strong class="labelVisu"><bean:message key="nueva.categoria.termino2" /> </strong></label>
										<html:text styleClass="texto" styleId="termino2" property="termino2" maxlength="50"/>
									</div>
									
									<div class="formItem">
										<label for="peso2"><strong class="labelVisu"><bean:message key="nueva.categoria.peso" /> </strong></label>
										<html:text styleClass="texto" styleId="peso2" property="peso2"/>
									</div>
									
									<div class="formItem">
										<label for="termino3"><strong class="labelVisu"><bean:message key="nueva.categoria.termino3" /> </strong></label>
										<html:text styleClass="texto" styleId="termino3" property="termino3" maxlength="50"/>
									</div>
									
									<div class="formItem">
										<label for="peso3"><strong class="labelVisu"><bean:message key="nueva.categoria.peso" /> </strong></label>
										<html:text styleClass="texto" styleId="peso3" property="peso3"/>
									</div>
									
									<div class="formItem">
										<label for="termino4"><strong class="labelVisu"><bean:message key="nueva.categoria.termino4" /> </strong></label>
										<html:text styleClass="texto" styleId="termino4" property="termino4" maxlength="50"/>
									</div>
									
									<div class="formItem">
										<label for="peso4"><strong class="labelVisu"><bean:message key="nueva.categoria.peso" /> </strong></label>
										<html:text styleClass="texto" styleId="peso4" property="peso4"/>
									</div>
									
									<div class="formButton">
										<html:submit><bean:message key="boton.aceptar"/></html:submit>
										<html:cancel><bean:message key="boton.volver"/></html:cancel>
									</div>
								</fieldset>
							</html:form>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
