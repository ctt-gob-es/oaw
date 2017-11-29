<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
This program is licensed and may be used, modified and redistributed under the terms
of the European Public License (EUPL), either version 1.2 or (at your option) any later 
version as soon as they are approved by the European Commission.
Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND, either express or implied. See the License for the specific language governing 
permissions and more details.
You should have received a copy of the EUPL1.2 license along with this program; if not, 
you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>

<div id="cabeza">
    <h1>
        <img class="pull-left" src="/oaw/images/logo.jpg" alt="<bean:message key="application.logo.alt" />" />
        <img src="/oaw/images/mhfp.gif" alt="<bean:message key="ministerio.logo.alt" />" />
    </h1>
	 <div id="logout">
         <html:link forward="logout"><span class="glyphicon glyphicon-log-out" aria-hidden="true" data-toggle="tooltip" title="Salir de la aplicaci&oacute;n"></span> <bean:message key="menuvisor.logOut" /></html:link>
     </div>
</div> <!-- fin cabecera -->


	
