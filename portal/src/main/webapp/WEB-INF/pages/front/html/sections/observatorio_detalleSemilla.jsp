<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<%@page import="java.util.HashMap"%>
<html:xhtml/>
	
	<div id="migas">
		<bean:define id="action"><%= Constants.ACTION %></bean:define>
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> /
			<html:link forward="observatorySeed"><bean:message key="migas.semillas.observatorio" /></html:link> / 
			<bean:message key="migas.ver.observatorio" />
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
							<h2 class="config"><bean:message key="detalle.semilla.observatorio.title" /></h2>
								<div class="detail">
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="detalle.semilla.observatorio.nombre" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="detalle.semilla.observatorio.url" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="listaUrlsString" /></p>
									</div>
									<div id="pCenter">
										<logic:notPresent parameter="<%= Constants.ACCION_DE_OBSERVATORIO%>">
											<html:link forward="observatorySeed" styleClass="boton"><bean:message key="boton.volver" /></html:link>
										</logic:notPresent>
										<logic:present parameter="<%= Constants.ACCION_DE_OBSERVATORIO%>">
											<html:link forward="resultadosObservatorio" name="breadCrumbsParams"  styleClass="boton"><bean:message key="boton.volver" /></html:link> 
										</logic:present>
									</div>
								</div>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 

