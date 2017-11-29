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
<html:xhtml/>
<html:javascript formName="NuevoUsuarioSistemaForm"/>

<bean:parameter name="<%=Constants.ROLE_TYPE %>" id="roleType"/>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="usersMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.usuarios" /></html:link></li>
                  <li class="active"><bean:message key="migas.nuevo.usuario" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="nuevo.usuario.title" /></h2>

                <p><bean:message key="leyenda.campo.obligatorio" /></p>

                <html:form styleClass="formulario form-horizontal" method="post" action="/secure/NuevoUsuarioSistema.do" onsubmit="return validateNuevoUsuarioSistemaForm(this)">
                    <input type="hidden" name="esPrimera" value="no" />
                    <input type="hidden" name="<%=Constants.ROLE_TYPE %>" value="<bean:write name="roleType"/>"/>
                    <jsp:include page="/common/crawler_messages.jsp" />
                    <fieldset>
                        <div class="formItem">
                            <label for="nombre" style="control-label"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.usuario" /></strong></label>
                            <html:text styleId="nombre" styleClass="texto form-control" name="NuevoUsuarioSistemaForm" property="nombre" maxlength="30"/>
                        </div>

                        <div class="formItem">
                            <label for="password" style="control-label"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.password" /></strong></label>
                            <html:password styleId="password" styleClass="texto form-control" name="NuevoUsuarioSistemaForm" property="password" maxlength="15"/>
                        </div>

                        <div class="formItem">
                            <label for="confirmar_password" style="control-label"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.confirmar.password" /></strong></label>
                            <html:password styleId="confirmar_password" styleClass="texto form-control" property="confirmar_password" maxlength="15"/>
                        </div>

                        <div class="formItem">
                            <label for="selectedCartuchos" style="control-label"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.cartucho" /></strong></label>
                            <div class="alinForm">
                                <ul>
                                    <logic:iterate name="NuevoUsuarioSistemaForm" property="cartuchosList" id="cartucho" type="es.inteco.rastreador2.dao.login.CartuchoForm">
                                        <bean:define id="cart">Cartucho_<bean:write name="cartucho" property="id" /></bean:define>
                                        <li><label for="<%= cart %>" class="noFloat control-label"><html:multibox styleClass="multiboxStyle form-control" property="selectedCartuchos" value="<%=cartucho.getId().toString() %>" styleId="<%=cart %>"/> <bean:write name="cartucho" property="name" /></label></li>
                                    </logic:iterate>
                                </ul>
                            </div>
                        </div>

                        <div class="formItem">
                            <label for="cuenta_cliente" style="control-label"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.cuenta.cliente" /></strong></label>
                            <logic:notEqual value="null" name="NuevoUsuarioSistemaForm" property="cuenta_clienteV">
                                <bean:define id="cargarC" name="NuevoUsuarioSistemaForm" property="cuenta_clienteV" type="es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm"/>
                            </logic:notEqual>
                            <logic:notEmpty name="cargarC" property="listadoCuentasUsuario">
                                <html:select size="5" multiple="true" styleClass="textoSelect form-control" styleId="cuenta_cliente" property="cuenta_cliente" >
                                    <logic:iterate name="cargarC" type="es.inteco.rastreador2.utils.ListadoCuentasUsuario" property="listadoCuentasUsuario" id="elemento">
                                        <bean:define id="idCuenta"><bean:write name="elemento" property="id_cuenta"/></bean:define>
                                        <html:option value="<%=idCuenta %>"><bean:write name="elemento" property="nombreCuenta"/></html:option>
                                    </logic:iterate>
                                </html:select>
                            </logic:notEmpty>
                        </div>

                        <div class="formItem">
                            <label for="selectedRoles" style="control-label"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.tipo.usuario" /></strong></label>
                            <div class="alinForm">
                                <ul>
                                    <logic:iterate name="NuevoUsuarioSistemaForm" property="roles" id="rol" type="es.inteco.rastreador2.dao.login.Role">
                                        <li>
                                            <label for="<bean:write name="rol" property="name" />" class="noFloat">
                                                <html:radio styleClass="multiboxStyle form-control" property="selectedRoles" value="<%=rol.getId().toString() %>" styleId="<%=rol.getName() %>"/> <bean:write name="rol" property="name" />
                                            </label>
                                        </li>
                                    </logic:iterate>
                                </ul>
                            </div>
                        </div>

                        <div class="formItem">
                            <label for="nombre2" style="control-label"><strong class="labelVisu"><bean:message key="nuevo.usuario.nombre" /></strong></label>
                            <html:text styleClass="texto form-control" styleId="nombre2" property="nombre2" maxlength="100"/>
                        </div>

                        <div class="formItem">
                            <label for="apellidos" style="control-label"><strong class="labelVisu"><bean:message key="nuevo.usuario.apellidos" /></strong></label>
                            <html:text styleClass="texto form-control" property="apellidos" styleId="apellidos" maxlength="150"/>
                        </div>

                        <div class="formItem">
                            <label for="departamento" style="control-label"><strong class="labelVisu"><bean:message key="nuevo.usuario.departamento" /></strong></label>
                            <html:text styleClass="texto form-control"  property="departamento" styleId="departamento" maxlength="100"/>
                        </div>

                        <div class="formItem">
                            <label for="email" style="control-label"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.eMail" /></strong></label>
                            <html:text styleClass="texto form-control"  property="email" styleId="email" maxlength="100"/>
                        </div>

                        <div class="formButton">
                            <html:submit styleClass="btn btn-primary btn-lg"><bean:message key="boton.aceptar"/></html:submit>
                            <html:cancel styleClass="btn btn-default btn-lg"><bean:message key="boton.volver"/></html:cancel>
                        </div>
                    </fieldset>
                </html:form>
            </div><!-- fin cajaformularios -->
        </div>
    </div>