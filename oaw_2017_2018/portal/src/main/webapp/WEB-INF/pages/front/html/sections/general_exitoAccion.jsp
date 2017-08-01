<%@ include file="/common/taglibs.jsp" %> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<html:xhtml/>
	<div id="migas">
		<p class="oculto"><bean:message key="ubicacion.usuario" /> </p> 
		<p><html:link forward="indexAdmin"><bean:message key="migas.inicio" /></html:link>
		 / <bean:message key="migas.informacion" /></p>
	</div>
	



			<div id="main">
				<bean:define id="mensajeExito">
					<bean:write name="mensajeExito"/>
				</bean:define>
				<bean:define id="accionVolver">
					<bean:write name="accionVolver"/>
				</bean:define>
				
				<h1><img src="../images/bullet_h1.gif" /> <bean:message key="pagina.exito" /> </h1>
				

					
					<div id="container_menu_izq">
						<jsp:include page="menu.jsp"/>
					</div>
					
					<div id="container_der">
						<div id="cajaformularios">
						
							<h2><bean:message key="exito.informacion" /></h2>
							<div class="notaInformativaExito">
								<c:if test="${mensajeExito!=null}">
									<p><bean:write name="mensajeExito" filter="false"/></p>
								</c:if>
								<c:set var="volvemos" value="ValidarLoginSistema"/> 
								<c:if test="${accionVolver!=null}">	
									<bean:define id="volvemos"><c:out value="${pageContext.request.contextPath}" /><bean:write name="accionVolver" filter="false"/></bean:define>
									<p><html:link styleClass="btn btn-default btn-lg" href="<%= volvemos %>"><bean:message key="boton.volver" /></html:link></p>
								</c:if>
							</div>

						</div><!-- fin cajaformularios -->
					</div>

			</div>
		</div>
	</div> 
