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
