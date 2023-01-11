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
            <li class="active"><bean:message key="migas.importar.entidad"/></li>
          </ol>
      </div>
      <div id="cajaformularios">
          <h2><bean:message key="menuadmin.importar.entidades" /></h2>
          <p><bean:message key="importar.entidades.exito" /></p>
          <ul>
            <li><strong><bean:message key="importar.entidades.ambitos" />:</strong> <bean:write name="results" property="numImportAdminLevels"/>/<bean:write name="results" property="numAdminLevels"/></li>
         	<li><strong><bean:message key="importar.entidades.complejidades" />:</strong> <bean:write name="results" property="numImportComplexities"/>/<bean:write name="results" property="numComplexities"/></li>
        	<li><strong><bean:message key="importar.entidades.dependencias" />:</strong> <bean:write name="results" property="numImportScopes"/>/<bean:write name="results" property="numScopes"/></li>
            <li><strong><bean:message key="importar.entidades.etiquetas" />:</strong> <bean:write name="results" property="numImportLabels"/>/<bean:write name="results" property="numLabels"/></li>
            <li><strong><bean:message key="importar.entidades.segmentos" />:</strong> <bean:write name="results" property="numImportSegments"/>/<bean:write name="results" property="numSegments"/></li>
          	<li><strong><bean:message key="importar.entidades.semillas" />:</strong> <bean:write name="results" property="numImportSeeds"/>/<bean:write name="results" property="numSeeds"/></li>
            <li><strong><bean:message key="importar.entidades.tiposEtiqueta" />:</strong> <bean:write name="results" property="numImportClassificationLabels"/>/<bean:write name="results" property="numClassificationLabels"/></li>
         	<li><strong><bean:message key="importar.entidades.tiposSemilla" />:</strong> <bean:write name="results" property="numImportSeedTypes"/>/<bean:write name="results" property="numSeedTypes"/></li>
           </ul>        
      </div>
   </div>
</div>