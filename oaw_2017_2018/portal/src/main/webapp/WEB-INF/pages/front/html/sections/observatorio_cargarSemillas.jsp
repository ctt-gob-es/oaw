<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<html:javascript formName="SemillaObservatorioForm" />

<jsp:useBean id="paramsNS" class="java.util.HashMap" />
<c:set target="${paramsNS}" property="action" value="anadir" />
<c:set target="${paramsNS}" property="esPrimera" value="si" />
	<!--  JQ GRID   -->
   <link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="/oaw/js/jquery-ui-1.12.4.min.js"></script>
    <script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>


	<!--  JQ GRID   -->
    <script>
    
    var $jq = jQuery.noConflict();
    
    var lastUrl;
    
    
    // Formatters de celdas
    function categoriaFormatter (cellvalue, options, rowObject){
   		return cellvalue.name;
    }
    
    function dependenciasFormatter (cellvalue, options, rowObject){
   		var cellFormatted = "<ul>";
    	
    	
    	$jq.each(cellvalue, function(index, value){
    		cellFormatted=cellFormatted+"<li>"+value.name+"</li>";	
    	});
    	
    	cellFormatted=cellFormatted+"</ul>";
    	
    	
    	return cellFormatted;
    }

    // Recarga el grid. Recibe como parámetro la url de la acción con la información de paginación.
	function reloadGrid(path){
		
		lastUrl = path;
    	
       	$jq('#grid').jqGrid('clearGridData')
		
		$jq.ajax({url:path ,dataType: "json"}).success(
				
				
       			
      		function(data){
  				        				
  				ajaxJson = JSON.stringify(data.semillas);
  				
  				total = data.paginador.total;
   				
  				$jq('#grid').jqGrid({
  					editUrl : '/oaw/secure/JsonSemillasObservatorio.do?action=update',
            		colNames: ["Id", "Nombre", "Acrónimo", "Segmento", "Dependencia", "URLs", "Activa"],
            		colModel: [    
                		{ name: "id", hidden : true, sortable: false},
                		{ name: "nombre", width : 50, editrules: { required: true}, sortable: false},
                		{ name: "acronimo", width : 20, sortable: false},
                		{ name: "categoria", width : 20, edittype: "select", editoptions: { 
                	 	
                			dataUrl:'/oaw/secure/JsonSemillasObservatorio.do?action=listCategorias',
                     		buildSelect: function(data) {
                         	
                     			var response = jQuery.parseJSON(data);
                     			var s = '<select>';
                         		
                     			if (response && response.length) {
                             		for (var i = 0, l=response.length; i<l ; i++) {
                                 		var ri = response[i];
                                 		s += '<option value="'+ri.id+'">'+ri.name+'</option>';
                             		}
                         		}
                         		
                     			return s + "</select>";
                     		}
                	
                		}, editrules: { required: true }, formatter: categoriaFormatter , sortable: false},
                		{ name: "dependencias",width : 50, editrules: {required: true}, edittype: "select", editoptions: { 
                			multiple: true,
                			dataUrl:'/oaw/secure/JsonSemillasObservatorio.do?action=listDependencias',
	           				buildSelect: function(data) {
	                         		
	           						var response = jQuery.parseJSON(data);
	                         		var s = '<select multiple>';
	                         
	                         		if (response && response.length) {
	                             		for (var i = 0, l=response.length; i<l ; i++) {
	                                 		var ri = response[i];
	                                 		s += '<option value="'+ri.id+'">'+ri.name+'</option>';
	                             		}
	                         		}
	                        	 	return s + "</select>";
	                     		}	
                			},
                		formatter: dependenciasFormatter, sortable: false},
                		{ name: "listaUrls", width : 50, edittype : 'textarea', sortable: false},
                		{ name: "activa", width : 10, template: "booleanCheckboxFa", sortable: false},
                
            		],
            		inlineEditing: { keys: true, defaultFocusField: "nombre" },
            		cmTemplate: { autoResizable: true, editable: true },
            		viewrecords: true,
            		caption: "Semillas",
            		autowidth: true,
            		pager : '#gridbuttons',
            		pgbuttons: false,
            		pgtext:false,
            		pginput:false,
            		onSelectRow: function (rowid, status, e) {
                		var $self = $jq(this), savedRow = $self.jqGrid("getGridParam", "savedRow");
                			if (savedRow.length > 0 && savedRow[0].id !== rowid) {
                    			$self.jqGrid("restoreRow", savedRow[0].id);
                			}

			                $self.jqGrid("editRow", rowid, { focusField: e.target, keys : true, 
			                	beforeSaveRow: function(o, rowid) {
			                		$jq('#grid').jqGrid('saveRow',rowid, true, '/oaw/secure/JsonSemillasObservatorio.do?action=update');
			                		reloadGrid(lastUrl)
		        			      	return false;
								}
			                });
			                
            		},
           	       beforeSelectRow: function (rowid) {
           	            var $self = $jq(this), i,
           	                // savedRows array is not empty if some row is in inline editing mode
           	                savedRows = $self.jqGrid("getGridParam", "savedRow");
           	            for (i = 0; i < savedRows.length; i++) {
           	                if (savedRows[i].id !== rowid) {
           	                    // save currently editing row
           	                    // one can replace saveRow to restoreRow in the next line
           	                    $self.jqGrid("saveRow", savedRows[i].id, true, '/oaw/secure/JsonSemillasObservatorio.do?action=update');
           	                 	reloadGrid(lastUrl);
           	                }
           	            }
           	            return savedRows.length === 0; // allow selection if saving successful
           	        },
        		}).jqGrid("inlineNav");
  				
  				//Recargar el grid
       			$jq('#grid').jqGrid('setGridParam', {data: JSON.parse(ajaxJson)}).trigger('reloadGrid');
  				
  				//Botones
  				
  				$jq('#grid')
  				.navGrid('#gridbuttons',{edit:false,add:false,del:false,search:false})
  				.navButtonAdd('#gridbuttons',{
  				   caption:"Add", 
  				   buttonicon:"ui-icon-add", 
  				   onClickButton: function(){ 
  				     $jq('#dialogoNuevaSemilla').show();
  				   }, 
  				   position:"last"
  				});
        
		      	//Paginador
      			paginas = data.paginas;

				$jq('#paginador').empty();

				$jq.each(paginas, function(key, value){
					if(value.active==true){
						$jq('#paginador').append('<a href="javascript:reloadGrid(\''+value.path+'\')" class="'+value.styleClass+' btn btn-default">'+value.title+'</a>');
					} else {
						$jq('#paginador').append('<span class="'+value.styleClass+' btn">'+value.title+'</span>');	
				}
	
			});
    	}).error(function(data){
       		console.log("Error")
       		console.log(data)
       	});
	}
    
           		
	//Buscador
	function buscar(){
		reloadGrid('/oaw/secure/JsonSemillasObservatorio.do?action=buscar&' + $jq('#SemillaSearchForm').serialize());
	}

	//Primera carga del grid el grid
	$jq(document).ready(function () {
   		reloadGrid('JsonSemillasObservatorio.do?action=buscar');
   	});
	
	
       /* grid.on('rowDataChanged', function (e, id, semilla) {
           $.ajax({ url: '/oaw/secure/JsonSemillasObservatorio.do?action=update&id='+id+"&esPrimera=si",dataType: 'json', data: { semilla: JSON.stringify(semilla) }, method: 'POST' })
               .fail(function () {
                   alert('Failed to save.');
               });
       }); */
	
    </script>


<div id="dialogoNuevaSemilla" style="display:none">
<jsp:include page="./observatorio_nuevaSemilla_multidependencia.jsp"></jsp:include>

</div>

<!-- observatorio_cargarSemillas.jsp -->
<div id="main">

	<div id="container_menu_izq">
		<jsp:include page="menu.jsp" />
	</div>

	<div id="container_der">

		<div id="migas">
			<p class="sr-only">
				<bean:message key="ubicacion.usuario" />
			</p>
			<ol class="breadcrumb">
				<li><html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link></li>
				<li class="active"><bean:message
						key="migas.semillas.observatorio" /></li>
			</ol>
		</div>

		<div id="cajaformularios">
			<h2>
				<bean:message key="gestion.semillas.observatorio.titulo" />
			</h2>

			<html:form action="/secure/ViewSemillasObservatorio.do" method="get"
				styleClass="formulario form-horizontal">
				<input type="hidden" name="<%=Constants.ACTION%>"
					value="<%=Constants.LOAD%>" />
				<fieldset>
					<legend>Buscador</legend>
					<jsp:include page="/common/crawler_messages.jsp" />
					<div class="formItem">
						<label for="nombre" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.nombre" /></strong></label>
						<html:text styleClass="texto form-control" styleId="nombre"
							property="nombre" />
					</div>
					<div class="formItem">
						<label for="categoria" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.categoria" /></strong></label>
						<html:select styleClass="textoSelect form-control"
							styleId="categoria" property="categoria">
							<html:option value="">
								<bean:message key="resultados.observatorio.cualquier.categoria" />
							</html:option>
							<logic:iterate name="<%= Constants.CATEGORIES_LIST %>"
								id="categoria">
								<bean:define id="idCategoria">
									<bean:write name="categoria" property="id" />
								</bean:define>
								<html:option value="<%=idCategoria%>">
									<bean:write name="categoria" property="name" />
								</html:option>
							</logic:iterate>
						</html:select>
					</div>
					<div class="formItem">
						<label for="url" class="control-label"><strong
							class="labelVisu"><bean:message
									key="nueva.semilla.observatorio.url" /></strong></label>
						<html:text styleClass="texto form-control" styleId="url"
							property="url" />
					</div>
					<div class="formButton">
<%-- 						<button type="submit" class="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							<bean:message key="boton.buscar" />
						</button> --%>
						
						<span onclick="buscar()" class="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							<bean:message key="boton.buscar" />
						</span>
					</div>
				</fieldset>
			</html:form>
			
		
			
			 <table id="grid">
			 </table>
			 <div id="gridbuttons"></div>
			 
			 <p id="paginador">
			 </p>
			 
			 			<html:link forward="observatorySeeds" name="paramsNS"
								styleClass="boton">
								<bean:message key="cargar.semilla.observatorio.nueva.semilla" />
							</html:link>
			

<%-- 			<div class="detail">
				<logic:notPresent name="<%=Constants.OBSERVATORY_SEED_LIST%>">
					<div class="notaInformativaExito">
						<p>
							<bean:message key="semilla.observatorio.vacia" />
						</p>
						<p>
							<html:link forward="observatorySeeds" name="paramsNS"
								styleClass="boton">
								<bean:message key="cargar.semilla.observatorio.nueva.semilla" />
							</html:link>
							<html:link styleClass="btn btn-default btn-lg"
								forward="indexAdmin">
								<bean:message key="boton.volver" />
							</html:link>
						</p>
					</div>
			</div> 
			</logic:notPresent> --%>
			
<%-- 			<logic:present name="<%=Constants.OBSERVATORY_SEED_LIST%>">
				<logic:empty name="<%=Constants.OBSERVATORY_SEED_LIST%>">
					<div class="notaInformativaExito">
						<p>
							<bean:message key="semilla.observatorio.vacia" />
						</p>
						<p>
							<html:link forward="observatorySeeds" name="paramsNS"
								styleClass="boton">
								<bean:message key="cargar.semilla.observatorio.nueva.semilla" />
							</html:link>
							<html:link styleClass="btn btn-default btn-lg"
								forward="indexAdmin">
								<bean:message key="boton.volver" />
							</html:link>
						</p>
					</div>
				</logic:empty>
				<logic:notEmpty name="<%=Constants.OBSERVATORY_SEED_LIST%>">
					<p class="pull-right">
						<html:link forward="observatorySeeds" name="paramsNS"
							styleClass="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-plus" aria-hidden="true"
								data-toggle="tooltip" title="Crear una nueva semilla"></span>
							<bean:message key="cargar.semilla.observatorio.nueva.semilla" />
						</html:link>
					</p>
					<div class="pag">
						<table class="table table-stripped table-bordered table-hover">
							<caption>
								<bean:message key="lista.semillas.observatorio" />
							</caption>
							<tr>
								<th><bean:message key="cargar.semilla.observatorio.nombre" /></th>
								<th><bean:message
										key="cargar.semilla.observatorio.categoria" /></th>
								<th><bean:message key="cargar.semilla.observatorio.activa" /></th>
								<th class="accion">Eliminar</th>
							</tr>
							<logic:iterate name="<%= Constants.OBSERVATORY_SEED_LIST %>"
								id="semilla">
								<bean:define id="action"><%=Constants.ACTION%></bean:define>
								<bean:define id="semillaId" name="semilla" property="id" />
								<bean:define id="semillaSTR"><%=Constants.SEMILLA%></bean:define>
								<tr>
									<td style="text-align: left"><bean:define id="actionDet"><%=Constants.ACCION_SEED_DETAIL%></bean:define>
										<jsp:useBean id="params" class="java.util.HashMap" /> <bean:define
											id="actionMod"><%=Constants.ACCION_MODIFICAR%></bean:define>
										<c:set target="${params}" property="${semillaSTR}"
											value="${semillaId}" /> <c:set target="${params}"
											property="${action}" value="${actionMod}" /> <html:link
											forward="observatorySeeds" name="params">
											<span data-toggle="tooltip"
												title="Editar la configuraci&oacute;n de esta semilla" />
											<bean:write name="semilla" property="nombre" />
											</span>
										</html:link> <span class="glyphicon glyphicon-edit pull-right edit-mark"
										aria-hidden="true" /></td>
									<td><bean:write name="semilla" property="categoria.name" />
									</td>
									<td><logic:equal name="semilla" property="activa"
											value="true">
											<bean:message key="si" />
										</logic:equal> <logic:equal name="semilla" property="activa" value="false">
											<bean:message key="no" />
										</logic:equal></td>
									<td><logic:equal value="false" name="semilla"
											property="asociada">
											<jsp:useBean id="paramsD" class="java.util.HashMap" />
											<bean:define id="actionDel"><%=Constants.ACCION_CONFIRMACION_BORRAR%></bean:define>
											<c:set target="${paramsD}" property="${semillaSTR}"
												value="${semillaId}" />
											<c:set target="${paramsD}" property="${action}"
												value="${actionDel}" />
											<html:link forward="observatorySeeds" name="paramsD">
												<span class="glyphicon glyphicon-remove" aria-hidden="true"
													data-toggle="tooltip" title="Eliminar esta semilla" />
												<span class="sr-only"><bean:message
														key="eliminar.semilla.observatorio" /></span>
											</html:link>
										</logic:equal> <logic:equal value="true" name="semilla" property="asociada">
											<img src="../images/bt_eliminar_escala_grises.gif"
												alt="<bean:message key="eliminar.semilla.observatorio.desactivado" />" />
										</logic:equal></td>
								</tr>
							</logic:iterate>
						</table>
						<jsp:include page="pagination.jsp" />
					</div>
				</logic:notEmpty>
			</logic:present> --%>
		</div>
		<p id="pCenter">
			<html:link forward="observatoryMenu"
				styleClass="btn btn-default btn-lg">
				<bean:message key="boton.volver" />
			</html:link>
		</p>
	</div>
	<!-- fin cajaformularios -->
</div>
</div>
