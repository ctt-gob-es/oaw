<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>

<html:xhtml/>

    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="usersMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.usuarios" /></html:link></li>
                  <li class="active"><bean:message key="migas.eliminar.usuario" /></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2><bean:message key="eliminar.usuario.titulo" /></h2>

                <html:form styleClass="formulario" method="post" action="/secure/EliminarUsuarioSistema.do" >
                    <input type="hidden" name="<%=Constants.ID_USUARIO %>" value="<bean:write name="EliminarUsuarioSistemaForm" property="id" />" />

                    <jsp:include page="/common/crawler_messages.jsp" />

                    <p><strong class="labelVisu"><bean:message key="eliminar.usuario.confirmacion" /></strong></p>

                    <div class="formItem">
                        <label><strong class="labelVisu"><bean:message key="eliminar.usuario.usuario" />:</strong> </label>
                        <p><bean:write name="EliminarUsuarioSistemaForm" property="usuario" /></p>
                    </div>

                    <logic:notEmpty name="EliminarUsuarioSistemaForm" property="cartuchos">
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="eliminar.usuario.cartucho" />:</strong> </label>
                            <div class="alinForm">
                                <ul class="sublista">
                                    <logic:iterate id="cratuchoL" name="EliminarUsuarioSistemaForm" property="cartuchos">
                                        <li><bean:write name="cratuchoL" property="name"/></li>
                                    </logic:iterate>
                                </ul>
                            </div>
                        </div>
                    </logic:notEmpty>

                    <logic:notEmpty name="EliminarUsuarioSistemaForm" property="nombreCuenta">
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="eliminar.usuario.cuenta" />:</strong> </label>
                            <div class="alinForm">
                                <ul class="sublista">
                                    <logic:iterate name="EliminarUsuarioSistemaForm" property="nombreCuenta" id="cuenta">
                                        <li><bean:write name="cuenta"/></li>
                                    </logic:iterate>
                                </ul>
                            </div>
                        </div>
                    </logic:notEmpty>

                    <div class="formItem">
                        <label><strong class="labelVisu"><bean:message key="eliminar.usuario.tipo" />:</strong> </label>
                            <div class="alinForm">
                                <ul class="sublista">
                                    <logic:iterate name="EliminarUsuarioSistemaForm" property="roles" id="rol">
                                        <li><bean:write name="rol" property="name"/></li>
                                    </logic:iterate>
                                </ul>
                            </div>
                    </div>

                    <logic:notEmpty name="EliminarUsuarioSistemaForm" property="nombre">
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="eliminar.usuario.nombre" />:</strong> </label>
                            <p><bean:write name="EliminarUsuarioSistemaForm" property="nombre" /></p>
                        </div>
                    </logic:notEmpty>

                    <logic:notEmpty name="EliminarUsuarioSistemaForm" property="apellidos">
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="eliminar.usuario.apellidos" />:</strong> </label>
                            <p><bean:write name="EliminarUsuarioSistemaForm" property="apellidos" /></p>
                        </div>
                    </logic:notEmpty>

                    <logic:notEmpty name="EliminarUsuarioSistemaForm" property="departamento">
                        <div class="formItem">
                            <label><strong class="labelVisu"><bean:message key="eliminar.usuario.departamento" />:</strong> </label>
                            <p><bean:write name="EliminarUsuarioSistemaForm" property="departamento" /></p>
                        </div>
                    </logic:notEmpty>

                    <div class="formItem">
                        <label><strong class="labelVisu"><bean:message key="eliminar.usuario.email" />:</strong> </label>
                        <p><bean:write name="EliminarUsuarioSistemaForm" property="email" /></p>
                    </div>
                    <div class="formButton">
                        <html:submit styleClass="btn btn-primary btn-lg"><bean:message key="boton.aceptar" /></html:submit>
                        <html:cancel styleClass="btn btn-default btn-lg"><bean:message key="boton.cancelar" /></html:cancel>
                    </div>
                </html:form>
            </div><!-- fin cajaformularios -->
        </div>
    </div>