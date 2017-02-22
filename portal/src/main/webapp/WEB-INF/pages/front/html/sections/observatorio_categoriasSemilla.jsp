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
                <h2 class="pull-left"><bean:message key="categoria.semillas.titulo" /></h2>
                <logic:empty name="<%=Constants.SEED_CATEGORIES %>">
                    <p>
                        <bean:message key="categoria.semillas.vacia"/>
                    </p>
                </logic:empty>
                <p class="pull-right">
                    <html:link forward="newSeedCategory" styleClass="btn btn-default btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title="Crear una nueva categoría de semillas"></span> <bean:message key="categoria.semillas.nueva"/></html:link>
                </p>
                <logic:notEmpty name="<%=Constants.SEED_CATEGORIES %>">
                    <div class="pag">
                        <table class="table table-stripped table-bordered table-hover">
                            <caption><bean:message key="categoria.semillas.lista"/></caption>
                            <tr>
                                <th><bean:message key="categoria.semillas.nombre"/></th>
                                <th class="accion"><bean:message key="categoria.semillas.operaciones"/></th>
                                <th class="accion"><bean:message key="categoria.semillas.operaciones"/></th>
                            </tr>
                            <logic:iterate name="<%=Constants.SEED_CATEGORIES %>" id="category">
                                <tr>
                                    <td style="text-align: left;">
                                        <html:link forward="viewSeedCategory" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id">
                                            <bean:write name="category" property="name"/>
                                        </html:link>
                                        <html:link forward="editSeedCategory" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id" styleClass="pull-right">
                                            <span class="glyphicon glyphicon-edit" aria-hidden="true" data-toggle="tooltip" title="Editar la configuración de esta categoría"/>
                                            <span class="sr-only"><bean:message key="indice.rastreo.img.editar.rastreo.alt" /></span>
                                        </html:link>
                                    </td>
                                    <td>
                                        <html:link forward="getCategorySeedsFile" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id">
                                            <span class="glyphicon glyphicon-save" aria-hidden="true" data-toggle="tooltip" title="Descargar las semillas de esta categoria en formato XML"/>
                                            <span class="sr-only">Descargar</span>
                                        </html:link>
                                    </td>
                                    <td>
                                        <html:link forward="deleteSeedCategoryConfirmation" paramId="<%= Constants.ID_CATEGORIA %>" paramName="category" paramProperty="id">
                                            <span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="tooltip" title="Eliminar esta categoria"/>
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

