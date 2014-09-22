<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

	<bean:parameter name="<%=Constants.ID_EX_OBS %>" id="id"/>
	<bean:parameter name="<%=Constants.ID_OBSERVATORIO %>" id="id_observatorio"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link>/ 
			<html:link forward="resultadosPrimariosObservatorio" paramName="id_observatorio" paramId="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
			<bean:message key="migas.eliminar.observatorio" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="bulleth1"><bean:message key="indice.observatorio.gestion.observatorio" /> </h1>
				<div id="cuerpoprincipal">
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="eliminar.observatorio.realizado.title" /></h2>
							
							<div class="detail">
								<bean:define id="seedName" name="SemillaForm" property="nombre"/>
								<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.ejecucion.semilla.observatorio">
									<jsp:attribute name="arg0">
										<bean:write name="<%=Constants.SEMILLA_FORM %>" property="nombre"/>
									</jsp:attribute>
								</bean:message></strong></p>
								<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.ejecucion.semilla.observatorio.info" /></strong></p>
								<div class="formButton">
									<bean:parameter id="observatoryId" name="<%= Constants.ID_OBSERVATORIO %>"/>
									<bean:parameter id="observatoryExId" name="<%= Constants.ID_EX_OBS %>"/>
									<bean:parameter id="id" name="<%= Constants.ID %>"/>
									<bean:parameter id="cartridgeId" name="<%= Constants.ID_CARTUCHO %>"/>
									<bean:define id="observatoryIdSTR"><%= Constants.ID_OBSERVATORIO %></bean:define>
									<bean:define id="observatoryExIdSTR"><%= Constants.ID_EX_OBS %></bean:define>
									<bean:define id="idSTR"><%= Constants.ID %></bean:define>
									<bean:define id="cartridgeIdSTR"><%= Constants.ID_CARTUCHO %></bean:define>
									<jsp:useBean id="params" class="java.util.HashMap" />
									<c:set target="${params}" property="${observatoryIdSTR}" value="${observatoryId}"/>
									<c:set target="${params}" property="${observatoryExIdSTR}" value="${observatoryExId}"/>
									<c:set target="${params}" property="${cartridgeIdSTR}" value="${cartridgeId}"/>
									<c:set target="${params}" property="${idSTR}" value="${id}"/>
									<html:link styleClass="boton" forward="deleteObservatoryCrawlerExecution" name="params"><bean:message key="boton.aceptar"/></html:link>
								</div>
							</div>
							
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
