<%@ include file="/common/taglibs.jsp" %>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>
<html:javascript formName="NuevoUsuarioClienteForm"/>

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
                  <li class="active"><bean:message key="migas.nuevo.usuario.cliente" /></li>
                </ol>
            </div>

            <div id="cajaformularios">

                <h2><bean:message key="nuevo.usuario.title" /></h2>

                <p><bean:message key="leyenda.campo.obligatorio" /></p>
                <html:form styleClass="formulario form-horizontal" method="post" action="/secure/NuevoUsuarioCliente.do" onsubmit="return validateNuevoUsuarioClienteForm(this)">
                    <input type="hidden" name="esPrimera" value="no" />
                    <input type="hidden" name="<%=Constants.ROLE_TYPE %>" value="<bean:write name="roleType"/>"/>
                    <jsp:include page="/common/crawler_messages.jsp" />
                    <fieldset>
                        <logic:empty name="NuevoUsuarioClienteForm" property="cuenta_clienteV.listadoCuentasUsuario">
                            <p class="notaInformativaExito">
                                <p><bean:message key="nuevo.usuario.cliente.sin.cuenta"/></p>
                                <p>
                                    <html:link forward="newUserAccount" styleClass="boton"><bean:message key="boton.crear.cuenta.cliente" /></html:link>
                                    <html:link forward="usersMenu" styleClass="btn btn-default btn-lg"><bean:message key="boton.volver" /></html:link>
                                </p>
                            </p>
                        </logic:empty>
                        <logic:notEmpty name="NuevoUsuarioClienteForm" property="cuenta_clienteV.listadoCuentasUsuario" >
                            <div class="formItem">
                                <label for="nombre"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.usuario" /></strong></label>
                                <html:text styleId="nombre" styleClass="texto form-control" property="nombre" maxlength="100"/>
                            </div>

                            <div class="formItem">
                                <label for="password"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.password" /></strong></label>
                                <html:password styleId="password" styleClass="texto form-control" property="password" maxlength="20"/>
                            </div>

                            <div class="formItem">
                                <label for="confirmar_password"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.confirmar.password" /></strong></label>
                                <html:password styleId="confirmar_password" styleClass="texto form-control" property="confirmar_password" maxlength="20"/>
                            </div>

                            <div class="formItem">
                                <label for="cuenta_cliente"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.cuenta.cliente" /></strong></label>
                                <logic:notEqual value="null" name="NuevoUsuarioClienteForm" property="cuenta_clienteV">
                                    <bean:define id="cargarC" name="NuevoUsuarioClienteForm" property="cuenta_clienteV" type="es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm"/>
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
                                <label for="selectedRoles"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.tipo.usuario" /></strong></label>
                                <div class="alinForm form-inline">
                                    <ul>
                                        <logic:iterate name="NuevoUsuarioClienteForm" property="roles" id="rol" type="es.inteco.rastreador2.dao.login.Role">
                                            <li><label for="<bean:write name="rol" property="name" />" class="noFloat control-label"><html:multibox styleClass="multiboxStyle" property="selectedRoles" value="<%=rol.getId().toString() %>" styleId="<%=rol.getName() %>"/> <bean:write name="rol" property="name" /></label></li>
                                        </logic:iterate>
                                    </ul>
                                </div>
                            </div>

                            <div class="formItem">
                                <label for="nombre2"><strong class="labelVisu"><bean:message key="nuevo.usuario.nombre" /></strong></label>
                                <html:text styleClass="texto form-control" styleId="nombre2" property="nombre2" maxlength="100"/>
                            </div>

                            <div class="formItem">
                                <label for="apellidos"><strong class="labelVisu"><bean:message key="nuevo.usuario.apellidos" /></strong></label>
                                <html:text styleClass="texto form-control" property="apellidos" styleId="apellidos" maxlength="150"/>
                            </div>

                            <div class="formItem">
                                <label for="departamento"><strong class="labelVisu"><bean:message key="nuevo.usuario.departamento" /></strong></label>
                                <html:text styleClass="texto form-control"  property="departamento" styleId="departamento" maxlength="100"/>
                            </div>

                            <div class="formItem">
                                <label for="email"><acronym title="<bean:message key="campo.obligatorio" />"> * </acronym><strong class="labelVisu"><bean:message key="nuevo.usuario.eMail" /></strong></label>
                                <html:text styleClass="texto form-control"  property="email" styleId="email" maxlength="100"/>
                            </div>

                            <div class="formButton">
                                <html:submit styleClass="btn btn-primary btn-lg"><bean:message key="boton.aceptar"/></html:submit>
                                <html:cancel styleClass="btn btn-default btn-lg"><bean:message key="boton.volver"/></html:cancel>
                            </div>
                        </logic:notEmpty>
                    </fieldset>
                </html:form>
            </div><!-- fin cajaformularios -->
        </div>
    </div>


