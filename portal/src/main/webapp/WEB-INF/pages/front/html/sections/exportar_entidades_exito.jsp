<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<div id="main">
	<div id="container_menu_izq">
	    <jsp:include page="menu.jsp"/>
	</div>
	<div id="container_der">
      <div id="migas">
          <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
          <ol class="breadcrumb">
            <li><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.other.options" /></li>
            <li class="active"><bean:message key="migas.exportar.entidad"/></li>
          </ol>
      </div>
      <div id="cajaformularios">
          <h2><bean:message key="menuadmin.exportar.entidades" /></h2>
          <p><bean:message key="exportar.entidades.exito" /></p>
      </div>
   </div>
</div>