<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

	<bean:parameter name="<%=Constants.ID_OBSERVATORIO %>" id="idObservatorio"/>
	<bean:parameter name="<%=Constants.ID_CARTUCHO %>" id="idCartucho"/>
	<bean:parameter name="<%=Constants.ID_EX_OBS%>" id="idExObs"/>
	<bean:define id="idObservatorioSTR" value="<%=Constants.ID_OBSERVATORIO %>"/>
	<bean:define id="idExObsSTR" value="<%=Constants.ID_EX_OBS %>"/>
	<bean:define id="idCartuchoSTR" value="<%=Constants.ID_CARTUCHO %>"/>
	<bean:define id="semillaSTR"><%= Constants.ID_SEMILLA%></bean:define>
	<bean:define id="semillaId" name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="id"/>
	
	<jsp:useBean id="params" class="java.util.HashMap" />
	<c:set target="${params}" property="${idObservatorioSTR}" value="${idObservatorio}"/>
	<c:set target="${params}" property="${idExObsSTR}" value="${idExObs}"/>
	<c:set target="${params}" property="${idCartuchoSTR}" value="${idCartucho}"/>
	<c:set target="${params}" property="${semillaSTR}" value="${semillaId}" />
	
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="resultadosPrimariosObservatorio" paramName="idObservatorio" paramId="<%= Constants.ID_OBSERVATORIO %>"><bean:message key="migas.indice.observatorios.realizados.lista"/></html:link> /
			<html:link forward="resultadosObservatorioSemillas" name="params"><bean:message key="migas.resultado.observatorio" /></html:link> /
			<bean:message key="migas.resultado.observatorio.confirmacion.lanzar.semilla" />
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="sem"><bean:message key="resultados.observatorio.lanzar.semilla.confirmación" /> </h1>
				<div id="cuerpoprincipal">
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="resultados.observatorio.lanzar.semilla.confirmación" /></h2>
								<div class="detail">
									<p><strong class="labelVisu"><bean:message key="resultados.observatorio.lanzar.semilla.conf" /></strong></p>
									<p><strong class="labelVisu"><bean:message key="resultados.observatorio.lanzar.semilla.info" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.observatorio.nombre" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.observatorio.url" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="listaUrlsString" /></p>
									</div>
									<div class="formButton">
										<bean:define id="confirmacion"><%= Constants.CONFIRMACION%></bean:define>
										<bean:define id="confSi"><%= Constants.CONF_SI%></bean:define>
										<bean:define id="confNo"><%= Constants.CONF_NO%></bean:define>
										<c:set target="${params}" property="${confirmacion}" value="${confSi}" />
										<html:link styleClass="boton" forward="resultadosObservatorioLanzarEjecucion" name="params"><bean:message key="boton.aceptar"/></html:link>
										<c:set target="${params}" property="${confirmacion}" value="${confNo}" />
										<html:link styleClass="boton" forward="resultadosObservatorioLanzarEjecucion" name="params"><bean:message key="boton.cancelar"/></html:link>
									</div>
								</div>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
