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
<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<html:javascript formName="TemplateRangeForm" />
<bean:parameter name="<%=Constants.ID_OBSERVATORIO%>" id="idObservatorio" />
<link rel="stylesheet" href="/oaw/js/tagbox/tagbox.css">
<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script src="/oaw/js/tagbox/tagbox.js" type="text/javascript"></script>
<script>
	var script = document.createElement('script');
	var lang = (navigator.language || navigator.browserLanguage)
	script.src = '/oaw/js/jqgrid/i18n/grid.locale-'+lang.substring(0,2)+'.js';
	document.head.appendChild(script);
</script>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script>
	$(window).on('load', function() {
	
	    $(".checkTag").on("change", function() {
	        var id = $(this).attr("id");
	        if ($(this).is(":checked")) {
	            $('#fieldset_' + $(this).val()).show();
	        } else {
	            $('#fieldset_' + $(this).val()).hide();
	        }
	    });
	
	    var idObs = '<c:out value="${param.id_observatorio}"/>';
	
	    $('.select_previous').each(function(index) {
	        loadOptions(idObs, $(this).attr("data-tag"), this);
	    });
	
	    var $jq = $.noConflict();
	    $jq(document).ready(function() {
	    	
	        $.ajax({
	            url: '/oaw/secure/ViewEtiquetasObservatorio.do?action=all',
	            method: 'POST',
	            cache: false
	        }).done(function(response) {
	
	            $('#tagsFilterFixed').tagbox({
	                items: response.etiquetas,
	                searchIn: ['name'],
	                rowFormat: '<span class="name">{{name}}</span>',
	                tokenFormat: '{{name}}',
	                valueField: 'id',
	                itemClass: 'user'
	            });
	
	        });
	
	    });
	    
	    
	    $.each($(".checkTag"), function(index, value) {
	    	if ($(this).is(":checked")) {
	            $('#fieldset_' + $(this).val()).show();
	        } else {
	            $('#fieldset_' + $(this).val()).hide();
	        }
        });

	
	});
	
	function formatDate(date) {
		var d = new Date(date), month = '' + (d.getMonth() + 1), day = ''
				+ d.getDate(), year = d.getFullYear();

		if (month.length < 2)
			month = '0' + month;
		if (day.length < 2)
			day = '0' + day;

		return [ year, month, day ].join('-');
	}
	
	var configComparision = "<s:property value='configComparision'/>";
	
	
	var cmpArray=new Array();

	var rowCounter=0;
	
	<c:forEach items="${configComparision}" var="cmp">

    var fieldObj=new Object();

    fieldObj.idTag='<c:out value="${cmp.idTag}"/>';
    fieldObj.previous='<c:out value="${cmp.previous}"/>';

    cmpArray[rowCounter]=fieldObj;

    rowCounter++;

	</c:forEach>
	
	$.each(cmpArray,function(key,value){
		console.log(key)
		console.log(value)
	})
	
	
	function loadOptions(idObs, tagId, element) {
	    $.ajaxSetup({
	        cache: false
	    });
	    $
	        .ajax({
	            type: "GET",
	            url: '/oaw/secure/databaseExportAction.do?action=observatoriesByTag&id_observatorio=' +
	                idObs + '&tagId=' + tagId,
	            dataType: "json",
	            success: function(data) {
	                if (data.length > 0) {
	                    $
	                        .each(
	                            data,
	                            function(key, value) {
	                            	
	                            	var isSelected = false;
	                            	$.each(cmpArray,function(k,v){
	                            		if(!isSelected && v.idTag == tagId && v.previous == formatDate(value.fecha)){
	                            			console.log(value.fecha);
	                            			isSelected = true;
	                            		}  
	                            	});
	                            	
	                            	
	                                $(element)
	                                    .append(
	                                        "<option " +(isSelected ? " selected ": "") + " value=" +
	                                        formatDate(value.fecha) +
	                                        ">" + 
	                                        value.fechaStr +
	                                        "</option>");
	                            });
	                } else {
	                    $(element).append(
	                        "<option value=''>" + noResults +
	                        "</option>");
	                }
	            },
	            error: function(data) {
	                
	            }
	        });
	}
	
	function generate(){
		window.location='/oaw/secure/ConfigSendResultsByMailAction.do?action=execute&'
				+ $('#sendResultsByMailForm').serialize();
	}
	
	function gotostep2(){
		window.location='/oaw/secure/ConfigSendResultsByMailAction.do?action=finish&'
				+ $('#sendResultsByMailForm').serialize();
	}

</script>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script>
	var $jn = jQuery.noConflict();
	var paginadorTotal = '<bean:message key="cargar.semilla.observatorio.buscar.total"/>';
	
	var colNameId = '<bean:message key="colname.id"/>';
	var colNameName = '<bean:message key="colname.name"/>';
	var colNameMinValue = '<bean:message key="colname.min.value"/>';
	var colNameMaxValue = '<bean:message key="colname.max.value"/>';
	var colNameMinValueOperator = '<bean:message key="colname.min.value.operator"/>';
	var colNameMaxValueOperator = '<bean:message key="colname.max.value.operator"/>';
	var colNameTemplate = '<bean:message key="colname.template"/>';
	var colNameRemove = '<bean:message key="colname.remove"/>';
	
	var windowTitleRemove = '<bean:message key="range.observatory.modal.delete.title"/>';
	var saveButton = '<bean:message key="boton.aceptar"/>';
	var cancelButton = '<bean:message key="boton.cancelar"/>';
	var confirmRemoveMessage = '<bean:message key="range.observatory.modal.delete.confirm"/>';
	
	var lastUrl;
	var scroll;
	
	var gridSelRow;
	
	
	function eliminarFormatter(cellvalue, options, rowObject) {
	    return "<span style='cursor:pointer' onclick='deleteRange(" +
	        options.rowId +
	        ")'class='glyphicon glyphicon-remove'></span><span class='sr-only'>" + colNameRemove + "</span></span>";
	}
	
	function deleteRange(rowId) {
	
	    var range = $('#grid').jqGrid('getRowData', rowId);
	    var id = range.id;
	    var deleteDialog = $('<div id="deleteRangeDialogContent"></div>');
	
	    deleteDialog.append('<p>' + confirmRemoveMessage + ' "' + range.name + '"?</p>');
	
	    deleteDialog
	        .dialog({
	            autoOpen: false,
	            minHeight: $(window).height() * 0.25,
	            minWidth: $(window).width() * 0.25,
	            modal: true,
	            title: windowTitleRemove,
	            buttons: {
	                "Aceptar": {
	                    click: function() {
	                        $
	                            .ajax({
	                                url: '/oaw/secure/TemplateRangeObservatorio.do?action=delete&id=' +
	                                    id,
	                                method: 'POST',
	                                cache: false
	                            }).success(function(response) {
	                                reloadGrid(lastUrl);
	                                deleteDialog.dialog("close");
	                            });
	                    },
	                    text: saveButton,
	                    class: 'jdialog-btn-save'
	                },
	                "Cancelar": {
	                    click: function() {
	                        deleteDialog.dialog("close");
	                    },
	                    text: cancelButton,
	                    class: 'jdialog-btn-cancel'
	                }
	            }
	        });
	
	    deleteDialog.dialog("open");
	}
	
	
	$(window)
	    .on(
	        'load',
	        function() {
	            var $jq = $.noConflict();
	            var lastUrl;
	            $jq(document)
	                .ready(
	                    function() {
	                        reloadGrid('/oaw/secure/TemplateRangeObservatorio.do?action=all&idExObs=' + $('[name=idExObs]').val());
	                    });
	        });
	

	function reloadGrid(path) {
	
	    lastUrl = path;	
	    
	    // keep scroll
	    scroll = $(window).scrollTop();
	
	    $('#grid').jqGrid('clearGridData')
	
	    $
	        .ajax({
	            url: path,
	            dataType: "json",
	            cache: false
	        })
	        .done(
	
	            function(data) {
	
	                ajaxJson = JSON.stringify(data.templates);
	
	                total = data.paginador.total;
	
	                $('#grid')
	                    .jqGrid({
	                        editUrl: '/oaw/secure/TemplateRangeObservatorio.do?action=update',
	                        colNames: [colNameId, colNameName, colNameMinValue, colNameMinValueOperator, "x",
	                            colNameMaxValueOperator, colNameMaxValue, colNameTemplate,
	                            colNameRemove
	                        ],
	                        colModel: [{
	                                name: "id",
	                                hidden: true,
	                                sortable: false
	                            },
	                            {
	                                name: "name",
	                                width: 20,
	                                editrules: {
	                                    required: true
	                                },
	
	                                align: "center",
	                                sortable: true
	
	                            },
								{
									name: "minValue",
									width: 20,
									editrules: {
										required: true,
										number: true,
										minValue: -100,
										maxValue: 100
									},
									align: "center",
									formatter: cellFormatter,
									sortable: true

								},
								{
									name: "minValueOperator",
									width: 20,
									edittype : "select",
									editoptions : {
										value: ":;<:<;<=:<="
									},
									align: "center",
									sortable: false

								},
								 {
									name:'x',
									width: 5,
									editable:'false',
									formatter: xFormatter,
									
								 },

								{
									name: "maxValueOperator",
									width: 20,
									align: "center",
									edittype : "select",
									formatter: cellFormatter,
									editoptions : {
										value: ":;<:<;<=:<="
									},
									sortable: false

								},								
								{
									name: "maxValue",
									width: 20,
									editrules: {
									    required: true,
										number: true,
										minValue: -100,
										maxValue: 100
									},
									formatter: cellFormatter,
									align: "center",
									sortable: true,

								},	
	                            {
	                                name: "template",
	                                width: 50,
	                                editable: true,
	                                editrules: {
	                                    required: true,
	                                },
	                                edittype : 'custom',
									editoptions : {
										custom_element : templateEdit,
										custom_value : templateEditValue
									},
									formatter: templateFormatter,
									unformat: templateUnformat,
	                                align: "left",
	                                sortable: false,
									cellattr : function(
											rowId, val,
											rawObject, cm,
											rdata) {
										return 'title="'
												+ rdata.template.replace(/<\/?[^>]+(>|$)/g, "")
												+ '"';
									},
	
	                            },
	                            {
	                                name: "eliminar",
	                                width: 10,
	                                sortable: false,
	                                editable: false,
	                                formatter: eliminarFormatter,
	                            }
	
	                        ],
	                        inlineEditing: {
	                            keys: true,
	                            defaultFocusField: "name"
	                        },
	                        cmTemplate: {
	                            autoResizable: true,
	                            editable: true
	                        },
	                        viewrecords: false,
	                        autowidth: true,
	                        pgbuttons: false,
	                        pgtext: false,
	                        pginput: false,
	                        hidegrid: false,
	                        altRows: false,
	                        mtype: 'POST',
	
	                        onSelectRow: function(rowid,
	                            status, e) {
	
	                            var $self = $(this),
	                                savedRow = $self
	                                .jqGrid(
	                                    "getGridParam",
	                                    "savedRow");
	                            if (savedRow.length > 0 &&
	                                savedRow[0].id !== rowid) {
	                                $self.jqGrid(
	                                    "restoreRow",
	                                    savedRow[0].id);
	                            }
	
	                            $self
	                                .jqGrid(
	                                    "editRow",
	                                    rowid, {
	                                        focusField: e.target,
	                                        keys: true,
	                                        url: '/oaw/secure/TemplateRangeObservatorio.do?action=update',
	                                        restoreAfterError: false,
	                                        successfunc: function(
	                                            response) {
	                                            reloadGrid(lastUrl);
	                                        },
	                                        afterrestorefunc: function(
	                                            response) {
	                                            reloadGrid(lastUrl);
	                                        }
	
	                                    });
	
	                        },
	                        beforeSelectRow: function(
	                            rowid, e) {
	                            var $self = $(this),
	                                i, $td = $(
	                                    e.target).closest(
	                                    "td"),
	                                iCol = $.jgrid
	                                .getCellIndex($td[0]);
	
	                            if (this.p.colModel[iCol].name === "eliminar") {
	                                return false;
	                            }
	
	                            savedRows = $self.jqGrid(
	                                "getGridParam",
	                                "savedRow");
	                            for (i = 0; i < savedRows.length; i++) {
	                                if (savedRows[i].id !== rowid) {
	                                    // save currently
	                                    // editing row
	                                    $self
	                                        .jqGrid(
	                                            'saveRow',
	                                            savedRows[i].id, {
	                                                successfunc: function(
	                                                    response) {
	                                                    reloadGrid(lastUrl);
	                                                },
	                                                afterrestorefunc: function(
	                                                    response) {
	                                                    reloadGrid(lastUrl);
	                                                },
	                                                url: '/oaw/secure/TemplateRangeObservatorio.do?action=update',
	                                                restoreAfterError: false,
	                                            });
	
	                                }
	                            }
	                            return savedRows.length === 0;
	                        },
	                        gridComplete: function() {
	                            // Restaurar el scroll
	                            $(window).scrollTop(scroll);
	                        }
	                    }).jqGrid("inlineNav");
	
	                // Recargar el grid
	                $('#grid').jqGrid('setGridParam', {
	                    data: JSON.parse(ajaxJson)
	                }).trigger('reloadGrid');
	
	                $('#grid').unbind("contextmenu");
	
	                // Mostrar sin resultados
	                if (total == 0) {
	                    $('#grid')
	                        .append(
	                            '<tr role="row" class="ui-widget-content jqgfirstrow ui-row-ltr"><td colspan="9" style="padding: 15px !important;" role="gridcell">Sin resultados</td></tr>');
	                }
	
	                // Paginador
	                paginas = data.paginas;
	
	                $('#paginador').empty();
	
	                $('#paginador').append("<span style='float: left;clear: both; display: block; width: 100%; text-align: left;padding: 10px 5px;'><strong>" + paginadorTotal + "</strong> " + data.paginador.total + "</span>");
	
	                // Si solo hay una página no pintamos el paginador
	                if (paginas.length > 1) {
	
	                    $
	                        .each(
	                            paginas,
	                            function(key, value) {
	                                if (value.active == true) {
	                                    $('#paginador')
	                                        .append(
	                                            '<a href="javascript:reloadGrid(\'' +
	                                            value.path +
	                                            '\')" class="' +
	                                            value.styleClass +
	                                            ' btn btn-default">' +
	                                            value.title +
	                                            '</a>');
	                                } else {
	                                    $('#paginador')
	                                        .append(
	                                            '<span class="' + value.styleClass +
	                                            ' btn">' +
	                                            value.title +
	                                            '</span>');
	                                }
	
	                            });
	                }
	            }).error(function(data) {
	            console.log("Error")
	            console.log(data)
	        });
	
	}
	
	function templateEdit(value, options) {
		var element = document.createElement('textarea');
		element.setAttribute("name", "templateEdit");
		element.setAttribute("id", "templateEdit");
		
		CKEDITOR.replace(element);
		
		//CKEDITOR.instances.templateEdit.setData(value);
		CKEDITOR.instances.templateEdit.setData($('#grid').getLocalRow(options.rowId).template);
		
		return element;
	}

	function templateEditValue(elem, operation, value) {

		if (operation === 'get') {
			return CKEDITOR.instances.templateEdit.getData();
		} else if (operation === 'set') {
			//CKEDITOR.replace('templateEdit');
			//CKEDITOR.instances.templateEdit.setData(value);
		}
	}
	
	
	function xFormatter(cellvalue, options, rowObject) {
		return "X";
	}
	
	function templateUnformat(cellvalue, options, cell){
		return cellvalue;
	}
	
	function templateFormatter(cellvalue, options, rowObject) {
		if (cellvalue && cellvalue.length > 100){
			return cellvalue.substr(0,100) + '...';
		}
		return cellvalue;
	}
	
	
	var dialog;
	
	var windowWidth = $(window).width() * 0.55;
	var windowHeight = $(window).height() * 0.6;
	
	
	function dialogNewRange() {
	
	    window.scrollTo(0, 0);
	
	    $('#successMD').hide();
	    $('#errorMD').hide();
	
	
	    var windowTitle = '<bean:message key="nueva.range.observatorio.modal.title"/>';
	
	    var saveButton = '<bean:message key="boton.guardar"/>';
	
	    var cancelButton = '<bean:message key="boton.cancelar"/>';
	
	
	
	    dialog = $("#dialogNewRange").dialog({
	        height: windowHeight,
	        width: windowWidth,
	        modal: true,
	        title: windowTitle,
	        tabindex: "",
	        buttons: {
	            "Guardar": {
	                click: function() {
	                    saveNewRange();
	                },
	                text: saveButton,
	                class: 'jdialog-btn-save'
	            },
	            "Cancelar": {
	                click: function() {
	                    dialog.dialog("close");
	                },
	                text: cancelButton,
	                class: 'jdialog-btn-cancel'
	            }
	        },
	        open: function() {
	            CKEDITOR.replace('template');
	        },
	        close: function() {
	            $('#newRangeForm')[0].reset();
	            CKEDITOR.instances.template.setData('');
	        }
	    });
	}
	
	function saveNewRange() {
	    $('#successMD').hide();
	    $('#successMD').html("");
	    $('#errorMD').hide();
	    $('#errorMD').html("");
	
	    // Need to can serialize form
	    CKEDITOR.instances.template.updateElement();
	
	    var guardado = $.ajax({
	        url: '/oaw/secure/TemplateRangeObservatorio.do?action=save',
	        data: $('#newRangeForm').serialize(),
	        method: 'POST',
	        cache: false
	    }).success(
	        function(response) {
	            $('#successMD').addClass('alert alert-success');
	            $('#successMD').append("<ul>");
	
	            $.each(JSON.parse(response), function(index, value) {
	                $('#successMD').append(
	                    '<li>' + value.message + '</li>');
	            });
	
	            $('#successMD').append("</ul>");
	            $('#successMD').show();
	            dialog.dialog("close");
	            reloadGrid(lastUrl);
	
	        }).error(
	        function(response) {
	            $('#errorMD').addClass('alert alert-danger');
	            $('#errorMD').append("<ul>");
	
	            $.each(JSON.parse(response.responseText), function(index,
	                value) {
	                $('#errorMD').append(
	                    '<li>' + value.message + '</li>');
	            });
	
	            $('#errorMD').append("</ul>");
	            $('#errorMD').show();
	
	        }
	
	    );
	
	    return guardado;
	
	}
	
	function cellFormatter(cellvalue, options, rowObject) {
	    if (cellvalue) {
	        return cellvalue;
	    } else {
	        return "";
	    }
	}
</script>
<script src="/oaw/js/ckeditor/ckeditor.js"></script>
<div id="main">
	<div id="dialogNewRange" style="display: none">
		<div id="main" style="overflow: hidden">
			<div id="errorMD" style="display: none"></div>
			<form id="newRangeForm">
				<input type="hidden" name="idObservatoryExecution" value="<c:out value="${param.idExObs}"/>" />
				<!-- Nombre -->
				<div class="row formItem">
					<label for="name" class="control-label">
						<strong class="labelVisu">
							<acronym title="<bean:message key="campo.obligatorio" />"> * </acronym>
							<bean:message key="range.observatory.new.name" />
						</strong>
					</label>
					<div class="col-xs-8">
						<input type="text" id="name" name="name" class="textoLargo form-control" />
					</div>
				</div>
				<div class="row formItem">
					<label for="minValue" class="control-label">
						<strong class="labelVisu">
							<acronym title="<bean:message key="campo.obligatorio" />"> * </acronym>
							<bean:message key="colname.range" />
						</strong>
					</label> 
					<div class="col-xs-2 padding-fields-new-range">
						<input type="number" id="minValue" name="minValue" class="form-control" step="0.01" min=-100" max="100"/>
					</div>
					<div class="col-xs-1 padding-fields-new-range">
						<select id="minValueOperator" name="minValueOperator" class="form-control">
							<option value=""></option>
							<option value="&#60;">&#60;</option>
							<option value="&#60;&#61;">&#60;&#61;</option>
						</select>
					</div> 
					<div class="col-xs-1-mod text-center">X</div> 
					<div class="col-xs-1 padding-fields-new-range"> 
						<select id="maxValueOperator" name="maxValueOperator" class="form-control">
							<option value=""></option>
							<option value="&#60;">&#60;</option>
							<option value="&#60;&#61;">&#60;&#61;</option>
						</select>
					</div>
					<div class="col-xs-2 padding-fields-new-range">
						<input type="number" id="maxValue" name="maxValue" class="form-control" step="0.01" min=-100" max="100"/>
					</div>
				</div>
				<div class="row formItem">
					<label for="template" class="control-label">
						<strong class="labelVisu">
							<acronym title="<bean:message key="campo.obligatorio" />"> * </acronym>
							<bean:message key="range.observatory.new.template" />
						</strong>
					</label>
					<div class="col-xs-8">
						<textarea type="text" id="template" name="template" class="textoLargo form-control" rows="10"></textarea>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div id="container_menu_izq">
		<jsp:include page="menu.jsp" />
	</div>
	<div id="container_der">
		<div id="migas">
			<p class="sr-only">
				<bean:message key="ubicacion.usuario" />
			</p>
			<ol class="breadcrumb">
				<li>
					<html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link>
				</li>
				<li>
					<html:link forward="resultadosPrimariosObservatorio" paramName="idObservatorio"
						paramId="<%=Constants.ID_OBSERVATORIO%>">
						<bean:message key="migas.indice.observatorios.realizados.lista" />
					</html:link>
				</li>
				<li class="active">
					<bean:message key="migas.exportar" />
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<form action="/oaw/secure/ConfigSendResultsByMailAction.do" id="sendResultsByMailForm">
				<h2>
					<bean:message key="send.results.observatory.title" />
				</h2>
				<fieldset>
					<legend>
						<bean:message key="report.config.etiquetas.filter.title" />
					</legend>
					<div class="formItem">
						<logic:iterate name="tagList" id="tag">
							<div class="tagList">
								<c:set var="contains" value="false" />
								<logic:notEmpty name="configComparision">
									<logic:iterate name="configComparision" id="configCmp">
										<c:if test="${tag.id == configCmp.idTag}">
											<c:set var="contains" value="true" />
										</c:if>
									</logic:iterate>
								</logic:notEmpty>
								<c:if test="${contains == true}">
									<input type="checkbox" checked="checked" value="<c:out value="${tag.id}" />" name="tags"
										id="check_<c:out value="${tag.id}" />" class="checkTag">
								</c:if>
								<c:if test="${contains == false}">
									<input type="checkbox" value="<c:out value="${tag.id}" />" name="tags" id="check_<c:out value="${tag.id}" />"
										class="checkTag">
								</c:if>
								<bean:write name="tag" property="name" />
							</div>
						</logic:iterate>
					</div>
					<p class="alert alert-info">
						<span class="glyphicon glyphicon-info-sign"></span>
						<em>
							<bean:message key="nueva.semilla.webs.informacion" />
						</em>
						:
						<bean:message key="report.config.etiquetas.filter.info" />
					</p>
				</fieldset>
				<logic:iterate name="tagList" id="tag">
					<fieldset id="fieldset_<c:out value="${tag.id}" />" style="display: none;">
						<legend>
							<bean:message key="confirmacion.exportar.resultados.observatorio.recurrencia.variable" arg0="" />
							<c:out value="${tag.name}" />
						</legend>
						<div class="formItem">
							<label for="url" class="control-label">
								<strong class="labelVisu">
									<bean:message key="confirmacion.exportar.resultados.observatorio.recurrencia.anterior" />
								</strong>
							</label>
							<select class="select_previous" name="previous_<c:out value="${tag.id}" />"
								id="select_previous_<c:out value="${tag.id}" />" data-tag="<c:out value="${tag.id}" />"></select>
						</div>
					</fieldset>
				</logic:iterate>
				<fieldset>
					<legend>
						<bean:message key="report.config.observatorios.filter.title" />
					</legend>
					<div class="formItem">
						<logic:iterate name="<%=Constants.FULFILLED_OBSERVATORIES%>" id="fulfilledObservatory">
							<c:if test="${fulfilledObservatory.id == param.idExObs}">
								<label class="label100">
									<input type="checkbox" checked value="<c:out value="${fulfilledObservatory.id}" />" name="evol">
									<bean:write name="fulfilledObservatory" property="fechaStr" />
									(
									<bean:message key="current" />
									)
								</label>
							</c:if>
							<c:if test="${fulfilledObservatory.id != param.idExObs}">
								<c:set var="contains" value="false" />
								<logic:notEmpty name="configExecutedObs">
									<logic:iterate name="configExecutedObs" id="configEx">
										<c:if test="${fulfilledObservatory.id == configEx}">
											<c:set var="contains" value="true" />
										</c:if>
									</logic:iterate>
								</logic:notEmpty>
								<c:if test="${contains == true}">
									<label class="label100">
										<input type="checkbox" checked value="<c:out value="${fulfilledObservatory.id}" />" name="evol">
										<bean:write name="fulfilledObservatory" property="fechaStr" />
									</label>
								</c:if>
								<c:if test="${contains == false}">
									<label class="label100">
										<input type="checkbox" value="<c:out value="${fulfilledObservatory.id}" />" name="evol">
										<bean:write name="fulfilledObservatory" property="fechaStr" />
									</label>
								</c:if>
							</c:if>
						</logic:iterate>
					</div>
				</fieldset>
				<fieldset>
					<legend>
						<bean:message key="report.config.observatorios.templates.title" />
					</legend>
					<div class="alert alert-info">
						<span class="glyphicon glyphicon-info-sign"></span>
						<em>
							<bean:message key="nueva.semilla.webs.informacion" />
						</em>
						:
						<bean:message key="report.config.observatorios.templates.info" />
						<br />
						<bean:message key="report.config.observatorios.templates.info.list" />
					</div>
					<div>
						<p class="pull-right">
							<a href="#" class="btn btn-default btn-lg" onclick="dialogNewRange()">
								<span class="glyphicon glyphicon-plus" aria-hidden="true" data-toggle="tooltip" title=""
									data-original-title="Crear una range"></span>
								<bean:message key="report.config.observatorios.templates.button.new" />
							</a>
						</p>
						<!-- Grid -->
						<table id="grid">
						</table>
					</div>
				</fieldset>
				<fieldset>
					<p>
						<strong class="labelVisu">
							<bean:message key="resultado.observatorio.rastreo.realizado.send.info" />
						</strong>
					</p>
					<p>
						<bean:message key="resultado.observatorio.rastreo.realizado.send.info2" />
					</p>
				</fieldset>
				<div class="formButton">
<!-- 					<input type="hidden" name="action" value="execute" /> -->
					<input type="hidden" name="idCartucho" value="<c:out value="${param.idCartucho}"/>" />
					<input type="hidden" name="id_observatorio" value="<c:out value="${param.id_observatorio}"/>" />
					<input type="hidden" name="idExObs" value="<c:out value="${param.idExObs}"/>" />
					<input type="button" class="btn btn-primary btn-lg" onclick="generate()" value="<bean:message key='boton.calcular.rangos' />">
					<input type="button" class="btn btn-info btn-lg" onclick="gotostep2()" value="<bean:message key='boton.ver.rangos' />">
<!-- 					<input type="submit" class="btn btn-primary btn-lg" value=<bean:message key="boton.siguiente" />> -->
					<html:link styleClass="btn btn-default btn-lg" forward="observatoryMenu">
						<bean:message key="boton.cancelar" />
					</html:link>
				</div>
			</form>
		</div>
		<!-- fin cajaformularios -->
	</div>
</div>