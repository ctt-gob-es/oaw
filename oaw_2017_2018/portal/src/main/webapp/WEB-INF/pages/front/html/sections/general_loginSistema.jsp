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
<html:javascript formName="ValidarLoginSistemaForm"/>



    <div id="main">
        <div id="cuerpoprincipal" class="container">
            <div id="cajaformularios_login">
                <html:form styleClass="formulario" method="post" action="/login/ValidarLoginSistema.do" onsubmit="return validateValidarLoginSistemaForm(this)">
                    <logic:present name="url">
                        <input type="hidden" id="url" name="url" value="<bean:write name="url"/>" />
                    </logic:present>
                    <fieldset>
                        <jsp:include page="/common/crawler_messages.jsp" />

                        <legend><bean:message key="datos.acceso" /></legend>
                        <div class="formItem">
                            <label for="loginUser"><strong class="labelVisu"><bean:message key="login.sistema.nombre.usuario" /></strong> </label>
                            <html:text property="loginUser" styleId="loginUser" styleClass="texto" />
                        </div>
                        <div class="formItem">
                            <label for="loginPass"><strong class="labelVisu"><bean:message key="login.sistema.password" /> </strong></label>
                            <html:password property="loginPass" styleId="loginPass" styleClass="texto" />
                        </div>
                        <div class="formButton">
                            <html:submit styleClass="btn btn-primary btn-lg"><bean:message key="boton.enviar" /></html:submit>
                        </div>
                    </fieldset>
                </html:form>
            </div><!-- fin cajaformularios_login -->
        </div>
    </div>
