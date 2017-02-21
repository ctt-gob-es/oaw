<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

	<bean:parameter name="<%=Constants.ID_EX_OBS %>" id="id"/>
	<bean:parameter name="<%=Constants.ID_OBSERVATORIO %>" id="id_observatorio"/>
	<bean:parameter name="<%=Constants.IS_PRIMARY %>" id="isPrimary"/>

	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link>/ 
			<logic:equal name="isPrimary" value="true">
				<html:link forward="resultadosPrimariosObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link>/ 
			</logic:equal>
			<logic:equal name="isPrimary" value="false">
				<html:link forward="getFulfilledObservatories" paramId="<%=Constants.ID_OBSERVATORIO %>" paramName="<%=Constants.ID_OBSERVATORIO %>"><bean:message key="migas.resultado.rastreos.realizados.observatorio" /></html:link>/ 
			</logic:equal>
			<bean:message key="migas.eliminar.observatorio" />
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
						
							<h2 class="config"><bean:message key="eliminar.observatorio.realizado.title" /></h2>
							
							<div class="detail">
								<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.realizado" /></strong></p>
								<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.observatorio.info" /></strong></p>
								<div class="formButton">
									<jsp:useBean id="params" class="java.util.HashMap"/>
									<c:set target="${params}" property="id" value="${id}" />
									<c:set target="${params}" property="id_observatorio" value="${id_observatorio}" />
									<c:set target="${params}" property="isPrimary" value="${isPrimary}" />
									<html:link styleClass="boton" forward="deleteFulfilledObservatory" name="params"><bean:message key="boton.aceptar"/></html:link>
									<logic:equal name="isPrimary" value="true">
										<html:link styleClass="boton" forward="resultadosPrimariosObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="boton.cancelar"/></html:link> 
									</logic:equal>
									<logic:equal name="isPrimary" value="false">
										<html:link styleClass="boton" forward="getFulfilledObservatories" paramId="<%=Constants.ID_OBSERVATORIO %>" paramName="<%=Constants.ID_OBSERVATORIO %>"><bean:message key="boton.cancelar"/></html:link>
									</logic:equal>
								</div>
							</div>
							
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
