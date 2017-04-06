<%@ include file="/common/taglibs.jsp" %> 
<%@page import="es.inteco.common.Constants"%>
<html:xhtml/>

<!-- observatorio_categoriasSemilla.jsp -->
    <div id="main">

        <div id="container_menu_izq">
            <jsp:include page="menu.jsp"/>
        </div>

        <div id="container_der">

            <div id="migas">
                <p class="sr-only"><bean:message key="ubicacion.usuario" /></p>
                <ol class="breadcrumb">
                  <li><html:link forward="observatoryMenu"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><bean:message key="migas.observatorio" /></html:link></li>
                  <li class="active"><bean:message key="migas.categoria" /></li>
                </ol>
            </div>

            <div id="cajaformularios">
                <h2 class="pull-left"><bean:message key="indice.categorias.gestion.categorias" /></h2>
                <logic:empty name="<%=Constants.SEED_CATEGORIES %>">
                    <p>
                        <bean:message key="categoria.semillas.vacia"/>
                    </p>
                </logic:empty>
                <p class="pull-right">
                    <html:link forward="newSeedCategory" styleClass="btn btn-default btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title="Crear un segmento"></span> <bean:message key="categoria.semillas.nueva"/></html:link>
                </p>
                <logic:notEmpty name="<%=Constants.SEED_CATEGORIES %>">
                    <div class="pag">
                        <table class="table table-stripped table-bordered table-hover">
                            <caption><bean:message key="categoria.semillas.lista"/></caption>
                            <tr>
                                <th><bean:message key="cargar.semilla.observatorio.categoria"/></th>
                                <th class="accion">Fichero semillas</th>
                                <th class="accion">Eliminar</th>
                            </tr>
                            <logic:iterate name="<%=Constants.SEED_CATEGORIES %>" id="category">
                                <tr>
                                    <td style="text-align: left;">
                                        <html:link forward="editSeedCategory" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id">
                                            <span data-toggle="tooltip" title="Editar la configuraci&oacute;n de este segmento"><bean:write name="category" property="name"/></span>
                                        </html:link>
                                        <span class="glyphicon glyphicon-edit pull-right edit-mark" aria-hidden="true" />
                                    </td>
                                    <td>
                                        <html:link forward="getCategorySeedsFile" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id">
                                            <span class="glyphicon glyphicon-cloud-download" aria-hidden="true" data-toggle="tooltip" title="Descargar las semillas de este segmento en formato XML"/>
                                            <span class="sr-only">Descargar</span>
                                        </html:link>
                                    </td>
                                    <td>
                                        <html:link forward="deleteSeedCategoryConfirmation" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id">
                                            <span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip" title="Eliminar este segmento"/>
                                            <span class="sr-only"><bean:message key="indice.rastreo.img.eliminar.rastreo.alt" /></span>
                                        </html:link>
                                    </td>
                                </tr>
                            </logic:iterate>
                        </table>
                        <jsp:include page="pagination.jsp" />
                    </div>
                </logic:notEmpty>
                <p id="pCenter"><html:link forward="observatoryMenu" styleClass="btn btn-default btn-lg"> <bean:message key="boton.volver"/> </html:link></p>
            </div>
        </div>
    </div>

