<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<bean:define id="observatorioId" name="<%= Constants.OBSERVATORY_ID %>"/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> / 
			<html:link forward="resultadosObservatorioSemillas" paramId="<%= Constants.ID_OBSERVATORIO %>" paramName="observatorioId"><bean:message key="migas.resultado.observatorio" /></html:link> / 
			<bean:message key="migas.eliminar.resultados.observatorio" />
		</p>
	</div>
	



			<div id="main">

					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="confirmacion.eliminar.semilla.title" /></h2>
								<div class="detail">
									<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.observatorio.pregunta" /></strong></p>
									<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.observatorio.info" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.observatorio.nombre" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.observatorio.url" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="listaUrlsString" /></p>
									</div>
									<div class="formButton">
										<bean:define id="action"><%= Constants.ACTION%></bean:define>
										<bean:define id="actionDel"><%= Constants.ACCION_BORRAR%></bean:define>
										<bean:define id="confirmacion"><%= Constants.CONFIRMACION%></bean:define>
										<bean:define id="confSi"><%= Constants.CONF_SI%></bean:define>
										<bean:define id="confNo"><%= Constants.CONF_NO%></bean:define>
										<bean:define id="semillaSTR"><%= Constants.SEMILLA%></bean:define>
										<bean:define id="semillaId" name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="id"/>
										<bean:define id="observatorioSTR"><%= Constants.OBSERVATORY_ID%></bean:define>
										<jsp:useBean id="paramsSI" class="java.util.HashMap"/>
										<jsp:useBean id="paramsNO" class="java.util.HashMap"/>
										
										<c:set target="${paramsSI}" property="${action}" value="${actionDel}" />
										<c:set target="${paramsSI}" property="${confirmacion}" value="${confSi}" />
										<c:set target="${paramsSI}" property="${semillaSTR}" value="${semillaId}" />
										<c:set target="${paramsSI}" property="${observatorioSTR}" value="${observatorioId}" />
										
										<c:set target="${paramsNO}" property="${action}" value="${actionDel}" />
										<c:set target="${paramsNO}" property="${confirmacion}" value="${confNo}" />
										<c:set target="${paramsNO}" property="${observatorioSTR}" value="${observatorioId}" />
										
										<html:link styleClass="boton" forward="resultadosObservatorio" name="paramsSI"><bean:message key="boton.aceptar"/></html:link>
										<html:link styleClass="boton" forward="resultadosObservatorio" name="paramsNO"><bean:message key="boton.cancelar"/></html:link>
									</div>
								</div>
						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
