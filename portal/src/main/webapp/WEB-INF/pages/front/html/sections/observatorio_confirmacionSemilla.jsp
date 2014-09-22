<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
	<div id="migas">
		<p>
			<html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link> / 
			<html:link forward="observatoryMenu"><bean:message key="migas.observatorio" /></html:link> /
			<html:link forward="observatorySeed"><bean:message key="migas.semillas.observatorio" /></html:link> / 
			<bean:message key="migas.eliminar.semillas.observatorio" /> 
		</p>
	</div>
	
	<div id="cuerpo">
		<div id="cIzq">&nbsp;</div>
		<div id="contenido">
			<div id="main">
				<h1 class="sem"><bean:message key="gestion.semillas.observatorio" /> </h1>
				<div id="cuerpoprincipal">
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2 class="config"><bean:message key="confirmacion.eliminar.semilla.title" /></h2>
								<div class="detail">
									<logic:notEmpty name="<%= Constants.OBSERVATORY_SEED_LIST %>">
										<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.info" /></strong></p>
										<ul class="lista_inicial">
											<logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>" id="elemento">
												<li><bean:write name="elemento" property="nombre"/></li>
											</logic:iterate>
										</ul>
										<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.info2" /></strong></p>
									</logic:notEmpty>
									<p><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.pregunta" /></strong></p>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.nombre" />: </strong></label>
										<p><bean:write name="<%= Constants.OBSERVATORY_SEED_FORM %>" property="nombre" /></p>
									</div>
									<div class="formItem">
										<label><strong class="labelVisu"><bean:message key="confirmacion.eliminar.semilla.url" />: </strong></label>
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
										
										<jsp:useBean id="paramsSI" class="java.util.HashMap"/>
										<jsp:useBean id="paramsNO" class="java.util.HashMap"/>
										
										<c:set target="${paramsSI}" property="${action}" value="${actionDel}" />
										<c:set target="${paramsSI}" property="${confirmacion}" value="${confSi}" />
										<c:set target="${paramsSI}" property="${semillaSTR}" value="${semillaId}" />
										
										<c:set target="${paramsNO}" property="${action}" value="${actionDel}" />
										<c:set target="${paramsNO}" property="${confirmacion}" value="${confNo}" />
										
										<html:link styleClass="boton" forward="observatorySeeds" name="paramsSI"><bean:message key="boton.aceptar"/></html:link>
										<html:link styleClass="boton" forward="observatorySeeds" name="paramsNO"><bean:message key="boton.cancelar"/></html:link>
									</div>
								</div>
						</div><!-- fin cajaformularios -->
					</div>
				</div><!-- fin CUERPO PRINCIPAL -->
			</div>
		</div>
	</div> 
