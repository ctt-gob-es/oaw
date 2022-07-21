<!--

Copyright (C) 2019  Ministerio de Hacienda y Función Pública, 
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

<link rel="stylesheet" href="/oaw/js/jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet" href="/oaw/css/jqgrid.semillas.css">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="/oaw/js/jqgrid/jquery.jqgrid.src.js"></script>
<script>
	var script = document.createElement('script');
	var lang = (navigator.language || navigator.browserLanguage)
	script.src = '/oaw/js/jqgrid/i18n/grid.locale-'+lang.substring(0,2)+'.js';
	document.head.appendChild(script);
</script>
<script>


	var $ja = jQuery.noConflict();

	var scroll;	
	
	var colNameId = '<input type="checkbox" id="threshold_checkbox_all" name="threshold_checkbox_all">';
	var colNameId2 = '<input type="checkbox" id="checkbox_all" name="threshold_checkbox_all">';
	var colNameName = '<bean:message key="colname.name"/>';
	var colNameComplex ='<bean:message key="colname.complex" />';
	var colNameUrls='<bean:message key="colname.total.url" />';
	var colNamePercent='%&nbsp;<span class="glyphicon glyphicon-info-sign" style="color:#fff !important;" aria-hidden="true" data-toggle="tooltip" title="<bean:message key="observatory.status.percent.threshold" />"></span><span class="sr-only"><bean:message key="observatory.status.percent.threshold" /></span>';
	var colNameRelaunch='<bean:message key="observatory.status.no.results.relaunch"/>';
	var colNameObs='<bean:message key="nueva.semilla.observatorio.observaciones" />';
	
	function reloadGrid(path) {

		lastUrlResults = path;

		// Mantener el scroll
		scroll = $(window).scrollTop();

		$ja('#grid').jqGrid('clearGridData')

		$ja
				.ajax({
					url : path,
					dataType : "json",
					cache : false
				})
				.done(

						function(data) {

							ajaxJson = JSON.stringify(data.seeds);

							total = data.paginador.total;

							$ja('#grid')
									.jqGrid(
											{
												editUrl : '/oaw/secure/estadoObservatorio.do?action=getLessThreshold&idExObs='
													+ $('[name=idExObs]').val(),
												colNames : [ colNameId, colNameName, colNameComplex, "URLs",
													colNameUrls, colNamePercent, colNameObs,
													colNameRelaunch ],
													
												colModel : [
														{
															name : "id",
															sortable : false,
															formatter : checkboxFormattter,
															width : 5,
															editable : false,
														},

														{
															name : "nombre",
															width : 30,
															sortable : false,
															align : "left",
															editable : false,
														},
														{
															name : "complejidad.name",
															width : 10,
															align : "center",
															sortable : false,
															editable : false,
															
														},
														{
															name : "listaUrlsString",
															align : "left",
															width : 50,
															edittype : 'custom',
															sortable : false,
															formatter : urlsFormatter,
															editable : false
														},
														{
															name : "numCrawls",
															width : 5,
															align : "center",
															sortable : false,
															editable : false,
														},
														{
															name : "percentNumCrawls",
															width : 10,
															align : "center",
															sortable : false,
															editable : false,
															formatter: percentFormatter
															
														},{														
															name : "observaciones",
															width : 20,
															align : "left",
															sortable : false
														},
														{
															name : "relaunch",
															width : 5,
															sortable : false,
															editable : false,
															formatter : relaunchFormatter,
														}

												],
												inlineEditing : {
													keys : true,
													defaultFocusField : "observaciones",
													focusField: "observaciones",
												},
												cmTemplate : {
													autoResizable : true,
													editable : true
												},
												onSelectRow : function(rowid,
														status, e) {

													var $self = $ja(this), savedRow = $self
															.jqGrid(
																	"getGridParam",
																	"savedRow");
													if (savedRow.length > 0
															&& savedRow[0].id !== rowid) {
														$self.jqGrid(
																"restoreRow",
																savedRow[0].id);
													}

													$self
															.jqGrid(
																	"editRow",
																	rowid,
																	{
																		focusField : e.target,
																		keys : true,
																		url : '/oaw/secure/estadoObservatorio.do?action=update',
																		restoreAfterError : false,
																		successfunc : function(
																				response) {
																			reloadGrid(lastUrlResults);
																		},
																		afterrestorefunc : function(
																				response) {
																			reloadGrid(lastUrlResults);
																		}

																	});

												},
												beforeSelectRow : function(
														rowid, e) {
													var $self = $ja(this), i, $td = $(
															e.target).closest(
															"td"), iCol = $ja.jgrid
															.getCellIndex($td[0]);

													if (this.p.colModel[iCol].name === "relaunch" || this.p.colModel[iCol].name === "id") {
														return false;
													}

													savedRows = $self.jqGrid(
															"getGridParam",
															"savedRow");
													for (i = 0; i < savedRows.length; i++) {
														if (savedRows[i].id !== rowid) {
															$self
																	.jqGrid(
																			'saveRow',
																			savedRows[i].id,
																			{
																				successfunc : function(
																						response) {
																					reloadGrid(lastUrlResults);
																				},
																				afterrestorefunc : function(
																						response) {
																					reloadGrid(lastUrlResults);
																				},
																				url : '/oaw/secure/estadoObservatorio.do?action=update',
																				restoreAfterError : false,
																			});

														}
													}
													return savedRows.length === 0;
												},
												viewrecords : false,
												autowidth : true,
												pgbuttons : false,
												pgtext : false,
												pginput : false,
												hidegrid : false,
												altRows : true,
												mtype : 'POST'
											}).jqGrid("inlineNav");

							$ja('#grid').jqGrid('setGridParam', {
								data : JSON.parse(ajaxJson)
							}).trigger('reloadGrid');

							$ja('#grid').unbind("contextmenu");
							
							$ja('#finishLessThresholdSizeText').text(total);
							
							if (total == 0) {
								$ja('#grid')
										.append(
												'<tr role="row" class="ui-widget-content jqgfirstrow ui-row-ltr"><td colspan="14" style="padding: 15px !important;" role="gridcell">'+noResults+'</td></tr>');
							}


						}).error(function(data) {
					console.log("Error")
					console.log(data)
				});

	}
	
	
	function reloadGridWithoutresults(path) {

		lastUrlWithoutResults = path;

		// Mantener el scroll
		scroll = $(window).scrollTop();

		$ja('#gridWithoutresults').jqGrid('clearGridData')

		$ja
				.ajax({
					url : path,
					dataType : "json",
					cache : false
				})
				.done(

						function(data) {

							ajaxJson = JSON.stringify(data.seeds);

							total = data.paginador.total;

							$ja('#gridWithoutresults')
									.jqGrid(
											{
												editUrl : '/oaw/secure/estadoObservatorio.do?action=finishWithoutResults&idExObs='
													+ $('[name=idExObs]').val()+"&id_observatorio="+ $('[name=id_observatorio]').val(),
												colNames : [ colNameId2, colNameName, colNameComplex, "URLs",
													 colNameObs,
													colNameRelaunch ],
													
												colModel : [
														{
															name : "id",
															sortable : false,
															formatter : checkboxFormattter2,
															width : 5,
															editable : false,
														},
														{
															name : "nombre",
															width : 30,
															sortable : false,
															align : "left",
															editable : false,
														},
														{
															name : "complejidad.name",
															width : 10,
															align : "center",
															sortable : false,
															editable : false,
															
														},
														{
															name : "listaUrlsString",
															align : "left",
															width : 50,
															edittype : 'custom',
															sortable : false,
															formatter : urlsFormatter,
															editable : false
														},
														{														
															name : "observaciones",
															width : 20,
															align : "left",
															sortable : false
														},
														{
															name : "relaunch",
															width : 5,
															sortable : false,
															editable : false,
															formatter : relaunchFormatter,
														}

												],
												inlineEditing : {
													keys : true,
													defaultFocusField : "observaciones"
												},
												cmTemplate : {
													autoResizable : true,
													editable : true
												},
												onSelectRow : function(rowid,
														status, e) {

													var $self = $ja(this), savedRow = $self
															.jqGrid(
																	"getGridParam",
																	"savedRow");
													if (savedRow.length > 0
															&& savedRow[0].id !== rowid) {
														$self.jqGrid(
																"restoreRow",
																savedRow[0].id);
													}

													$self
															.jqGrid(
																	"editRow",
																	rowid,
																	{
																		focusField : e.target,
																		keys : true,
																		url : '/oaw/secure/estadoObservatorio.do?action=update',
																		restoreAfterError : false,
																		successfunc : function(
																				response) {
																			reloadGridWithoutresults(lastUrlWithoutResults);
																		},
																		afterrestorefunc : function(
																				response) {
																			reloadGridWithoutresults(lastUrlWithoutResults);
																		}

																	});

												},
												beforeSelectRow : function(
														rowid, e) {
													var $self = $ja(this), i, $td = $(
															e.target).closest(
															"td"), iCol = $ja.jgrid
															.getCellIndex($td[0]);

													
													
													if (this.p.colModel[iCol].name === "relaunch" || this.p.colModel[iCol].name === "id") {
														return false;
													}

													savedRows = $self.jqGrid(
															"getGridParam",
															"savedRow");
													for (i = 0; i < savedRows.length; i++) {
														if (savedRows[i].id !== rowid) {
															$self
																	.jqGrid(
																			'saveRow',
																			savedRows[i].id,
																			{
																				successfunc : function(
																						response) {
																					reloadGridWithoutresults(lastUrlWithoutResults);
																				},
																				afterrestorefunc : function(
																						response) {
																					reloadGridWithoutresults(lastUrlWithoutResults);
																				},
																				url : '/oaw/secure/estadoObservatorio.do?action=update',
																				restoreAfterError : false,
																			});

														}
													}
													return savedRows.length === 0;
												},
												viewrecords : false,
												autowidth : true,
												pgbuttons : false,
												pgtext : false,
												pginput : false,
												hidegrid : false,
												altRows : true,
												mtype : 'POST'
											}).jqGrid("inlineNav");

							$ja('#gridWithoutresults').jqGrid('setGridParam', {
								data : JSON.parse(ajaxJson)
							}).trigger('reloadGrid');

							$ja('#gridWithoutresults').unbind("contextmenu");
							
							$ja('#finishWithoutResultsText').text(total);
							
							if (total == 0) {
								$ja('#gridWithoutresults')
										.append(
												'<tr role="row" class="ui-widget-content jqgfirstrow ui-row-ltr"><td colspan="14" style="padding: 15px !important;" role="gridcell">'+noResults+'</td></tr>');
							}


						}).error(function(data) {
					console.log("Error")
					console.log(data)
				});

	}

	function urlsFormatter(cellvalue, options, rowObject) {
		return rowObject.listaUrls.toString().replace(/\,/g, '\r\n');
	}
						
	
	function percentFormatter(cellvalue, options, rowObject) {
		return (Math.round(cellvalue * 100) / 100).toFixed(2);
	}

	
	function checkboxFormattter(cellvalue, options, rowObject) {
		
		return '<input type="checkbox" class="threshold_selectionCheckBox" name="line_check_'+options.pos +'"><input type="hidden" name="line_data_'+options.pos+'" value="'+ rowObject.id +'" />';
	}
	
	
	function checkboxFormattter2(cellvalue, options, rowObject) {
		
		return '<input type="checkbox" class="selectionCheckBox" name="line_check_'+options.pos +'"><input type="hidden" name="line_data_'+options.pos+'" value="'+ rowObject.id +'" />';
	}
	
	
	
	function relaunchFormatter(cellvalue, options, rowObject){	
		return '<a href="/oaw/secure/ResultadosObservatorio.do?action=lanzarEjecucion&id_observatorio='+$('[name=id_observatorio]').val()
				+'&idExObs=' + $('[name=idExObs]').val()
				+ '&idCartucho='+ $('[name=idCartucho]').val()
				+ '&idSemilla='+rowObject.id+'">'
				+ '<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip" title="Relanzar"></span><span class="sr-only">Relanzar</span></a>';
	}

	$(window)
	.on(
			'load',
			function() {

				var $jq = $.noConflict();

				var lastUrlResults;
				var lastUrlWithoutResults;

				$jq(document)
						.ready(
								function() {
									reloadGrid('/oaw/secure/estadoObservatorio.do?action=getLessThreshold&idExObs='
											+ $('[name=idExObs]').val());
									
									
									reloadGridWithoutresults('/oaw/secure/estadoObservatorio.do?action=finishWithoutResults&idExObs='
										+ $('[name=idExObs]').val()+"&id_observatorio="+ $('[name=id_observatorio]').val());
									

								});
				
				

				$jq(".selectionCheckBox").val(this.checked);
				$jq(".threshold_selectionCheckBox").val(this.checked);

// 				$jq("#checkbox_all").change(function() {
//                    if(this.checked) {
//                        if ($jq(".selectionCheckBox")[0]){
//                     	   $jq("#relaunchselected").prop( "disabled", false );
//                        }
//                        $jq(".selectionCheckBox").prop( "checked", true );
//                    }
//                    else {
//                 	   $jq(".selectionCheckBox").prop( "checked", false );
//                 	   $jq("#relaunchselected").prop( "disabled", true );
//                    }
//                 });
	

// 				$jq(".selectionCheckBox").change(function() {
//                    if(this.checked) {
//                 	   $jq("#relaunchselected").prop( "disabled", false );
//                    }
//                    else {
//                 	   $jq("#relaunchselected").prop( "disabled", true );
//                        var inputElements = [].slice.call(document.querySelectorAll('.selectionCheckBox'));
//                        var checkedValue = inputElements.filter(chk => chk.checked).length;
//                        if (checkedValue > 0){
//                     	   $jq("#relaunchselected").prop( "disabled", false );
//                        }
//                    }
//                 });

				
				$jq(document).on('change', '#checkbox_all', function(){
                      if(this.checked) {
                          if ($jq(".selectionCheckBox")[0]){
                        	  $jq("#relaunchselected").prop( "disabled", false );
                          }
                          $jq(".selectionCheckBox").prop( "checked", true );
                      }
                      else {
                    	  $jq(".selectionCheckBox").prop( "checked", false );
                    	  $jq("#relaunchselected").prop( "disabled", true );
                      }
                   });
				
				$jq('#gridWithoutresults').on('change', '.selectionCheckBox', function(){
                  if(this.checked) {
                	  $jq("#relaunchselected").prop( "disabled", false );
                  }
                  else {
                	  $jq("#relaunchselected").prop( "disabled", true );
                       var inputElements = [].slice.call(document.querySelectorAll('.selectionCheckBox'));
                       var checkedValue = inputElements.filter(chk => chk.checked).length;
                       if (checkedValue > 0){
                    	   $jq("#relaunchselected").prop( "disabled", false );
                       }
                  }
               });

				
				$jq(document).on('change', '#threshold_checkbox_all', function(){
                      if(this.checked) {
                          if ($jq(".threshold_selectionCheckBox")[0]){
                        	  $jq("#threshold_relaunchselected").prop( "disabled", false );
                          }
                          $jq(".threshold_selectionCheckBox").prop( "checked", true );
                      }
                      else {
                    	  $jq(".threshold_selectionCheckBox").prop( "checked", false );
                    	  $jq("#threshold_relaunchselected").prop( "disabled", true );
                      }
                   });
				
				$jq('#grid').on('change', '.threshold_selectionCheckBox', function(){
                  if(this.checked) {
                	  $jq("#threshold_relaunchselected").prop( "disabled", false );
                  }
                  else {
                	  $jq("#threshold_relaunchselected").prop( "disabled", true );
                       var inputElements = [].slice.call(document.querySelectorAll('.threshold_selectionCheckBox'));
                       var checkedValue = inputElements.filter(chk => chk.checked).length;
                       if (checkedValue > 0){
                    	   $jq("#threshold_relaunchselected").prop( "disabled", false );
                       }
                  }
               });
				
				$jq("#filterResultsThresholdButton").click(function(){
					reloadGrid('/oaw/secure/estadoObservatorio.do?action=getLessThreshold'
							+'&idExObs='+ $('[name=idExObs]').val()
							+'&percent='+ $('#percentLess').val()
							+'&seeds='+ $('#seedCrawledLess').val());
				});

				
				$jq("#exportTreshold").on("click", function(){
					downloadResultsCSV($ja('#grid').jqGrid('getRowData'));
				})

			});
	
	   function downloadResultsCSV(objArray)
	    {
	        var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;

	        var str = '';

	        for (var i = 0; i < array.length; i++) {
	            var line = '';

	            for (var index in array[i]) {
	            	if("id"!=index && "relaunch"!=index){
	            		
	                line += array[i][index] + ',';
	            	}
	            }

	            line.slice(0,line.Length-1); 

	            str += line + '\r\n';
	        }
	        
	        
	        if (navigator.msSaveBlob) { // IE10+ : (has Blob, but not a[download] or URL)
	              return navigator.msSaveBlob(encodeURI("data:text/csv;charset=utf-8," + str), 'umbral.csv');
	        }
	        
	        var encodedUri = encodeURI("data:text/csv;charset=utf-8," + str);
			var link = document.createElement("a");
			link.setAttribute("href", encodedUri);
			link.setAttribute("download", "umbral.csv");
			document.body.appendChild(link); // Required for FF
			link.click()
	    }

</script>
<bean:define id="rolObservatory">
	<inteco:properties key="role.observatory.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolAdmin">
	<inteco:properties key="role.administrator.id" file="crawler.properties" />
</bean:define>
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
				<li>
					<html:link forward="observatoryMenu">
						<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
						<bean:message key="migas.observatorio" />
					</html:link>
				</li>
				<li>
					<html:link forward="resultadosPrimariosObservatorio" paramName="idObservatory" paramId="id_observatorio">
						<bean:message key="migas.indice.observatorios.realizados.lista" />
					</html:link>
				</li>
				<li class="active">
					<bean:message key="migas.estado.observatorio" />
				</li>
			</ol>
		</div>
		<div id="cajaformularios">
			<h2>
				<bean:message key="observatory.status.title" />
			</h2>
			<table class="table table-stripped table-bordered table-hover">
				<caption>
					<bean:message key="observatory.status.caption" />
				</caption>
				<tbody>
					<tr>
						<th>
							<bean:message key="observatory.status.status" />
						</th>
						<th>
							<bean:message key="observatory.status.actions" />
						</th>
					</tr>
					<tr>
						<jsp:useBean id="paramsRelanzar" class="java.util.HashMap" />
						<c:set target="${paramsRelanzar}" property="action" value="confirm" />
						<c:set target="${paramsRelanzar}" property="id_observatorio" value="${idObservatory}" />
						<c:set target="${paramsRelanzar}" property="idExObs" value="${idExecutedObservatorio}" />
						<c:set target="${paramsRelanzar}" property="idCartucho" value="${idCartucho}" />
						<logic:equal name="estado" property="idEstado" value="4">
							<td style="text-align: center">
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.parado" />
							</td>
							<td>
								<html:link forward="relanzarObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.relaunh"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.relaunh" />
									</span>
								</html:link>
								<html:link forward="resultadosObservatorioSemillas" name="paramsRelanzar">
									<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.results"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.results" />
									</span>
								</html:link>
							</td>
						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="3">
							<td style="text-align: center">
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.relanzado" />
							</td>
							<td>
								<html:link forward="stopObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-stop" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key='tooltip.status.stop' />"></span>
									<span class="sr-only">
										<bean:message key='tooltip.status.stop' />
									</span>
								</html:link>
								<html:link forward="resultadosObservatorioSemillas" name="paramsRelanzar">
									<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.results"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.results" />
									</span>
								</html:link>
							</td>
						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="1">
							<td>
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.lanzado" />
							</td>
							<td>
								<html:link forward="stopObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-stop" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key='tooltip.status.stop' />"></span>
									<span class="sr-only">
										<bean:message key='tooltip.status.stop' />
									</span>
								</html:link>
								<html:link forward="resultadosObservatorioSemillas" name="paramsRelanzar">
									<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.results"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.results" />
									</span>
								</html:link>
							</td>
						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="0">
							<td>
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.terminado" />
							</td>
							<td>
								<html:link forward="resultadosObservatorioSemillas" name="paramsRelanzar">
									<span class="glyphicon glyphicon-list-alt" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.results"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.results" />
									</span>
								</html:link>
							</td>
						</logic:equal>
						<logic:equal name="estado" property="idEstado" value="2">
							<td>
								<bean:message key="resultado.observatorio.rastreo.realizado.estado.error" />
							</td>
							<td>
								<html:link forward="relanzarObservatorio" name="paramsRelanzar">
									<span class="glyphicon glyphicon-repeat" aria-hidden="true" data-toggle="tooltip"
										title="<bean:message key="observatory.status.relaunh"/>"></span>
									<span class="sr-only">
										<bean:message key="observatory.status.relaunh" />
									</span>
								</html:link>
							</td>
						</logic:equal>
					</tr>
				</tbody>
			</table>
			<br />
			<h2>
				<bean:message key="observatory.status.summary" />
			</h2>
			<table class="table table-stripped table-bordered table-hover">
				<caption>
					<bean:message key="observatory.status.summary.caption" />
				</caption>
				<tbody>
					<tr>
						<logic:notEqual name="estado" property="idEstado" value="0">
							<th>
								<bean:message key="observatory.status.summary.seed.total" />
							</th>
						</logic:notEqual>
						<th>
							<bean:message key="observatory.status.summary.seed.processed" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.seed.results" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.time.total" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.time.avg" />
						</th>
						<th>
							<bean:message key="observatory.status.summary.time.end" />
						</th>
					</tr>
					<tr>
						<logic:notEqual name="estado" property="idEstado" value="0">
							<td style="text-align: center">
								<bean:write name="estado" property="totalSemillas" />
							</td>
						</logic:notEqual>
						<td>
							<bean:write name="estado" property="semillasAnalizadas" />
							(
							<bean:write name="estado" property="porcentajeCompletado" format="###.##" />
							%
							<bean:message key="completado" />
							)
						</td>
						<td>
							<bean:write name="estado" property="semillasAnalizadasOk" />
							(
							<bean:write name="estado" property="porcentajeCompletadoOk" format="###.##" />
							%
							<bean:message key="completado" />
							)
						</td>
						<td>
							<bean:write name="estado" property="tiempoTotal" />
							<bean:message key="minutos" />
							(
							<bean:write name="estado" property="tiempoTotalHoras" />
							<bean:message key="horas" />
							)
						</td>
						<td>
							<bean:write name="estado" property="tiempoMedio" />
							<bean:message key="minutos" />
						</td>
						<td>
							<bean:write name="estado" property="tiempoEstimado" />
							<bean:message key="minutos" />
							(
							<bean:write name="estado" property="tiempoEstimadoHoras" />
							<bean:message key="horas" />
							)
						</td>
					</tr>
				</tbody>
			</table>
			<br />
			<logic:notEqual name="estado" property="idEstado" value="0">
				<h2>
					<bean:message key="observatory.status.last.title" />
				</h2>
				<table class="table table-stripped table-bordered table-hover">
					<caption>
						<bean:message key="observatory.status.last.caption" />
					</caption>
					<tbody>
						<tr>
							<th>
								<bean:message key="observatory.status.last.seed" />
							</th>
						</tr>
						<tr>
							<td style="text-align: center">
								<bean:write name="analisis" property="nombre" />
								&nbsp;(
								<bean:write name="analisis" property="url" />
								)
							</td>
						</tr>
					</tbody>
				</table>
				<table class="table table-stripped table-bordered table-hover">
					<caption>
						<bean:message key="observatory.status.last.caption" />
					</caption>
					<tbody>
						<tr>
							<th>
								<bean:message key="observatory.status.last.total.url" />
							</th>
							<th>
								<bean:message key="observatory.status.last.total.url.analized" />
							</th>
							<th>
								<bean:message key="observatory.status.last.total.url.last" />
							</th>
							<th>
								<bean:message key="observatory.status.last.total.url.last.end" />
							</th>
							<th>
								<bean:message key="observatory.status.last.time.avg" />
							</th>
							<th>
								<bean:message key="observatory.status.last.time.total" />
							</th>
							<th>
								<bean:message key="observatory.status.last.time.end" />
							</th>
						</tr>
						<tr>
							<td>
								<bean:write name="analisis" property="totalUrl" />
							</td>
							<td>
								<bean:write name="analisis" property="totalUrlAnalizadas" />
								(
								<bean:write name="analisis" property="porcentajeCompletado" format="###.##" />
								%
								<bean:message key="completado" />
								)
							</td>
							<td>
								<bean:write name="analisis" property="ultimaUrl" />
							</td>
							<td>
								<fmt:formatDate value="${analisis.fechaUltimaUrl}" pattern="dd-MM-yyyy HH:mm" />
							</td>
							<td>
								<bean:write name="analisis" property="tiempoMedio" />
								<bean:message key="segundos" />
							</td>
							<td>
								<bean:write name="analisis" property="tiempoAcumulado" />
								<bean:message key="segundos" />
							</td>
							<td>
								<bean:write name="analisis" property="tiempoEstimado" />
								<bean:message key="segundos" />
							</td>
						</tr>
					</tbody>
				</table>
			</logic:notEqual>
			<logic:notEqual name="estado" property="idEstado" value="0">
				<h2>
					<bean:size id="notCrawledSeedsYetSize" name="notCrawledSeedsYet" />
					<bean:message key="observatory.status.pending.title" />
					<c:if test="${notCrawledSeedsYetSize > 0}}">
					&nbsp;(
					<bean:write name="notCrawledSeedsYetSize" />
					)
					</c:if>
				</h2>
				<table class="table table-stripped table-bordered table-hover table-console">
					<caption>
						<bean:message key="observatory.status.pending.caption" />
					</caption>
					<colgroup>
						<col style="width: 5%">
						<col style="width: 30%">
						<col style="width: 65%">
					</colgroup>
					<tbody>
						<tr>
							<th>#</th>
							<th>
								<bean:message key="observatory.status.pending.name" />
							</th>
							<th>URL</th>
						</tr>
						<logic:empty name="notCrawledSeedsYet">
							<tr>
								<td colspan="3">
									<bean:message key="no.results" />
								</td>
							</tr>
						</logic:empty>
						<logic:iterate name="notCrawledSeedsYet" id="notCrawledSeedsYet" indexId="index">
							<tr>
								<td class="col-md-1">
									<c:out value="${index + 1}" />
								</td>
								<td style="text-align: left">
									<bean:write name="notCrawledSeedsYet" property="nombre" />
								</td>
								<td style="text-align: left" title="<bean:write name="notCrawledSeedsYet" property="listaUrlsString" />">
									<bean:write name="notCrawledSeedsYet" property="listaUrlsString" />
								</td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</logic:notEqual>
			<br />
			<!-- Less Threshold -->
			<h2>
				<bean:size id="finishLessThresholdSize" name="finishLessThreshold" />
				<bean:message key="observatory.status.less.threshold.title" />
				&nbsp; (
				<%-- 				<bean:write name="finishLessThresholdSize" /> --%>
				<span id="finishLessThresholdSizeText"></span>
				)
			</h2>
			<div id="filterResultsThreshold">
				<div class="form-inline">
					<div class="form-group">
						<label for="percentLess">
							<bean:message key="observatory.status.percent.threshold.filter.percent" />
						</label>
						<input type="number" class="form-control" id="percentLess" value="<bean:write name="umbral"/>" />
					</div>
					<div class="form-group">
						<label for="seedCrawledLess">
							<bean:message key="observatory.status.percent.threshold.filter.number" />
						</label>
						<input type="number" class="form-control" id="seedCrawledLess" />
					</div>
					<button type="submit" class="btn btn-default" id="filterResultsThresholdButton">
						<bean:message key="boton.buscar" />
					</button>
					<button id="exportTreshold" class="btn btn-default">CSV</button>
				</div>
			</div>
			<form action="/oaw/secure/RelanzarObservatorioSeleccionandoAction.do">
				<input type="submit" value='<bean:message key="observatory.status.no.results.relaunchselected"/>'
					id="threshold_relaunchselected" disabled class="btn btn-default btn-lg" style="margin-bottom: 15px" />
				<input type="hidden" name="<%=Constants.ID_EX_OBS%>" value="<bean:write name="idExecutedObservatorio"/>" />
				<input type="hidden" name="<%=Constants.ID_OBSERVATORIO%>" value="<bean:write name="idObservatory"/>" />
				<input type="hidden" name="<%=Constants.ID_CARTUCHO%>" value="<bean:write name="idCartucho"/>" />
				<table id="grid" class="gridTable table table-stripped table-bordered table-hover table-console"></table>
			</form>
			<br />
			<!-- Not finished -->
			<h2 style="margin-top: 15px">
				<bean:size id="finishWithoutResultsSize" name="finishWithoutResults" />
				<bean:message key="observatory.status.no.results.title" />
				&nbsp;(
				<%-- 				<bean:write name="finishWithoutResultsSize" /> --%>
				<span id="finishWithoutResultsText"></span>
				)
			</h2>
			<form action="/oaw/secure/RelanzarObservatorioSeleccionandoAction.do">
				<input type="submit" value='<bean:message key="observatory.status.no.results.relaunchselected"/>'
					id="relaunchselected" disabled class="btn btn-default btn-lg" />
				<table id="gridWithoutresults" class="gridTable table table-stripped table-bordered table-hover table-console"></table>
				<input type="hidden" name='id_observatorio' <c:out value='value=${idObservatory}' />>
				<input type="hidden" name='idExObs' <c:out value='value=${idExecutedObservatorio}' />>
			</form>
			<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
			<br />
			<h2>
				<bean:message key="observatory.status.notes.title" />
			</h2>
			<logic:notEqual name="estado" property="idEstado" value="0">
				<p>
					<bean:message key="observatory.status.notes.running" />
				</p>
			</logic:notEqual>
			<p>
				<bean:message key="observatory.status.notes.info" />
			</p>
			<p>
				<bean:message key="observatory.status.notes.info2" />
			</p>
		</div>
		<!-- fin cajaformularios -->
	</div>
	<!-- Container Derecha -->
</div>