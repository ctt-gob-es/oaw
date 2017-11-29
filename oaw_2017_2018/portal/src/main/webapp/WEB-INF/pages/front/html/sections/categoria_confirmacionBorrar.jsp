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
-->
<%@ include file="/common/taglibs.jsp" %> 
	<html:xhtml/>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">
            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li><html:link forward="categoriesMenu"><bean:message key="migas.categoria" /></html:link></li>
                  <li class="active"><bean:message key="migas.eliminar.categoria" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="eliminar.categoria.titulo" /></h2>

                <html:form styleClass="formulario" method="post" action="/secure/EliminarCategoria.do">
                    <input type="hidden" name="idcat" id="id_categoria" value="<bean:write name="verCategoriaForm" property="id_categoria" />" />
                    <input type="hidden" name="confirmacion" value="si"/>
                    <fieldset>
                        <jsp:include page="/common/crawler_messages.jsp" />

                        <p><strong class="labelVisu"><bean:message key="eliminar.categoria.confirmacion" /></strong></p>
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="eliminar.categoria.categoria" /></strong></label>
                            <p><bean:write name="VerCategoriaForm" property="categoria" /></p>
                        </div>
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="eliminar.categoria.umbral" /></strong></label>
                            <p><bean:write name="VerCategoriaForm" property="umbral" /></p>
                        </div>
                        <div class="formButton">
                            <html:submit><bean:message key="boton.aceptar" /></html:submit>
                            <html:cancel><bean:message key="boton.volver" /></html:cancel>
                        </div>
                    </fieldset>
                </html:form>

            </div><!-- fin cajaformularios -->
        </div>
    </div>
