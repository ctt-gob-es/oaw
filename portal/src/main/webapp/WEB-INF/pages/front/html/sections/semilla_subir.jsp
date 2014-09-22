<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>

<inteco:sesion action="ifConfigAdmin">
<html:xhtml/>

	<bean:define id="isNew" name="<%= Constants.IS_NEW %>" type="java.lang.String"/>
	<bean:define id="idRastreoName" value="<%= Constants.ID_RASTREO %>"/>
	<bean:define id="idSemillaName" value="<%= Constants.ID_SEMILLA %>" />
	<bean:define id="isNewName" value="<%= Constants.IS_NEW %>"/>
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p>
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> 
			 / <html:link forward="crawlingsMenu"><bean:message key="migas.rastreo" /></html:link> /
			<logic:equal name="isNew" value="true">
			 	<html:link forward="addCrawling"><bean:message key="migas.nuevo.rastreo" /></html:link> /
			</logic:equal>
			<logic:equal name="isNew" value="false">
				<bean:parameter id="idrastreo" name="<%= Constants.ID_RASTREO %>"/>
				<html:link forward="editCrawling" paramId="<%= Constants.ID_RASTREO %>" paramName="idrastreo"><bean:message key="migas.editar.rastreo" /></html:link> /
			</logic:equal>
			<bean:message key="migas.cargar.semilla" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"> <bean:message key="indice.rastreo.gestion.rastreos"/> </h1>
				<div id="cuerpoprincipal">
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div class="detail">
							<h2 class="config"><bean:message key="subir.semilla.servidor.title" /></h2>
							<bean:define id="action" type="java.lang.String">
								<logic:equal value="true" name="isNew">/secure/InsertarRastreo.do</logic:equal>
								<logic:equal value="false" name="isNew">/secure/ModificarRastreo.do</logic:equal>
							</bean:define>
							
							<jsp:include page="/common/crawler_messages.jsp" />
							<jsp:useBean id="params" class="java.util.HashMap" />
							<c:set target="${params}" property="${isNewName}" value="${isNew}" />
							<logic:equal name="<%= Constants.ID_RASTREO %>" value="null">
								<div class="notaInformativaExito">
									<p><bean:message key="subir.semilla.vacio"/></p>
								</div>
							</logic:equal>
							<logic:notEqual name="<%= Constants.SEED_LIST %>" value="null">
								<logic:empty name="<%= Constants.SEED_LIST %>">
									<div class="notaInformativaExito">
										<p><bean:message key="subir.semilla.vacio"/></p>
									</div>
								</logic:empty>
								<logic:notEmpty name="<%= Constants.SEED_LIST %>" >
									<div class="pag">
										<table>
											<caption><bean:message key="lista.semillas"/></caption>
											<tr>
												<th><bean:message key="lista.semillas.semilla"/></th>
												<th><bean:message key="lista.semillas.operaciones" /></th>
											</tr>
											<logic:iterate name="<%= Constants.SEED_LIST %>" id="semilla">
												<tr>
													<c:set target="${params}" property="${idSemillaName}" value="${semilla.id}" />
													<logic:equal name="isNew" value="false">
														<bean:parameter id="idRastreo" name="<%= Constants.ID_RASTREO %>"/>
														<c:set target="${params}" property="${idRastreoName}" value="${idRastreo}" />
													</logic:equal>
													<td><bean:write name="semilla" property="nombre"/></td>
													<td><html:link forward="uploadSeed" name="params"><img src="../images/icono_add.png" alt="anadir.semilla"/></html:link></td>
												</tr>
											</logic:iterate>
										</table>
										<jsp:include page="pagination.jsp" />
									</div>
								</logic:notEmpty>
							</logic:notEqual>
							
							<logic:equal name="isNew" value="true">
							 	<html:link forward="addCrawling" styleClass="boton"><bean:message key="boton.volver" /></html:link> 
							</logic:equal>
							<logic:equal name="isNew" value="false">
								<bean:parameter id="idrastreo" name="<%= Constants.ID_RASTREO %>"/>
								<html:link forward="editCrawling" paramId="<%= Constants.ID_RASTREO %>" paramName="idrastreo" styleClass="boton"><bean:message key="boton.volver" /></html:link>
							</logic:equal>
						</div>
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
</inteco:sesion>