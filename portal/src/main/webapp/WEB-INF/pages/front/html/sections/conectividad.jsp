<!--
Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
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

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

<script type="text/javascript">
	function checksim() {

		var email = $("#checksimemail").val();

		$("#checksimemail").attr('disabled', 'disabled');

		if (validateEmail(email)) {

			$('#simcheck-error-message').addClass('hidden');

			$('#checksim').addClass('hidden');
			$('#checkingsim').removeClass('hidden');

			$
					.ajax(
							{
								url : '/oaw/secure/conectividad.do?action=checksim&email='
										+ email,
								method : 'POST'
							}).success(
							function(data) {

								$("#checksimemail").removeAttr('disabled');
								$("#checksimemail").val("")

								$('#checksimresult-url').text(data.url);
								$('#checksimresult-url-link').attr('href',
										data.url);

								if (data.connection) {
									$('#checksimresult-ok').removeClass(
											'hidden');
									$('#checksimresult-ko').addClass('hidden');
									$('#checksimresult-error').addClass(
											'hidden');
								} else {
									$('#checksimresult-ok').addClass('hidden');
									$('#checksimresult-ko').removeClass(
											'hidden');
									$('#checksimresult-error').removeClass(
											'hidden');
									$('#checksimresult-error-detalle').text(
											data.error);
								}

								$('#checksimresult').removeClass('hidden');
								$('#checkingurl').addClass('hidden');
								$('#checkurl').removeClass('hidden');

								$('#checkingsim').addClass('hidden');
								$('#checksim').removeClass('hidden');
							});
		} else {
			$('#simcheck-error-message').removeClass('hidden');
			$("#checksimemail").removeAttr('disabled');
			return false;
		}
	}

	function checkurl() {

		var url = $('#urlcheck').val();

		if (url == "") {
			$('#urlcheck-error-message').removeClass('hidden');
			return false;
		} else {

			$("#urlcheck").attr('disabled', 'disabled');
			$('#urlcheck-error-message').addClass('hidden');
			$('#checkurl').addClass('hidden');
			$('#checkingurl').removeClass('hidden');
			$('#checkurlresult').hide();

			$
					.ajax(
							{
								url : '/oaw/secure/conectividad.do?action=checkurl&url='
										+ decodeURIComponent($('#urlcheck')
												.val()),
								method : 'POST'
							}).success(
							function(data) {

								$('#checkurlresult').show();
								$("#urlcheck").removeAttr('disabled');
								$('#urlcheck').val("");

								$('#checkurlresult-url').text(data.url);
								$('#checkurlresult-url-link').attr('href',
										data.url);

								if (data.connection) {
									$('#checkurlresult-ok').removeClass(
											'hidden');
									$('#checkurlresult-ko').addClass('hidden');
									$('#checkurlresult-error').addClass(
											'hidden');
								} else {
									$('#checkurlresult-ok').addClass('hidden');
									$('#checkurlresult-ko').removeClass(
											'hidden');
									$('#checkurlresult-error').removeClass(
											'hidden');
									$('#checkurlresult-error-detalle').text(
											data.error);
								}

								if (data.connectionProxy) {
									$('#checkurlresult-ok-proxy').removeClass(
											'hidden');
									$('#checkurlresult-ko-proxy').addClass(
											'hidden');
									$('#checkurlresult-error-proxy').addClass(
											'hidden');
								} else {
									$('#checkurlresult-ok-proxy').addClass(
											'hidden');
									$('#checkurlresult-ko-proxy').removeClass(
											'hidden');
									$('#checkurlresult-error-proxy')
											.removeClass('hidden');
									$('#checkurlresult-error-detalle-proxy')
											.text(data.errorProxy);
								}

								$('#checkurlresult').removeClass('hidden');
								$('#checkingurl').addClass('hidden');
								$('#checkurl').removeClass('hidden');
							});

		}
	}

	function modifyProxy() {

		var proxyUrl = $('#proxyUrl').val();
		var proxyPort = $('#proxyPort').val();
		var proxyStatus = $('#proxyStatus').is(':checked');

		if (proxyUrl == "") {
			$('#proxy-url-error-message').removeClass('hidden');
			return false;
		} else if (proxyPort == "" || !validPort(proxyPort)) {
			$('#proxy-port-error-message').removeClass('hidden');
			return false;
		} else {

			$
					.ajax(
							{
								url : '/oaw/secure/conectividad.do?action=modifyproxy&proxyUrl='
										+ decodeURI($('#proxyUrl').val())
										+ "&proxyPort="
										+ proxyPort
										+ "&proxyStatus=" + proxyStatus,
								method : 'POST'
							}).success(function(data) {
						location.reload();
					});

		}

	}

	function validateEmail(email) {
		var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return re.test(email);
	}

	function validPort(port) {
		var re = /^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/;
		return re.test(port);

	}
</script>

<!-- conectividad.jsp -->
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
				<li><span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					Otras opciones</li>
				<li class="active"></span> <bean:message key="migas.conectividad" /></li>

			</ol>
		</div>

		<div id="cajaformularios">

			<h2>
				<bean:message key="conectividad.title" />
			</h2>

			<div class="formulario">
				<fieldset>
					<legend>
						<bean:message key="conectividad.sim" />
					</legend>
					<div class="formItem">

						<div id="simcheck-error-message" class="alert alert-danger hidden"><bean:message key="conectividad.sim.error.valido"/> </div>

						<p><bean:message key="conectividad.sim.info"/> </p>


						<p>
							<label for="url" class="labelCorto"><strong
								class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym>
									Email:</strong></label> <input type="text" id="checksimemail" />

							<button id="checksim" onclick="checksim()"
								class="btn btn-default btn-sm"><bean:message key="conectividad.url.comprobar"/></button>

							<button id="checkingsim" class="btn btn-default btn-sm hidden">
								<span
									class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>&nbsp;<bean:message key="conectividad.url.comprobando"/>
							</button>
						</p>

						<div id="checksimresult" class="hidden">
							<p>
								<span class="bold">URL: </span> <span id="checksimresult-url"></span>
								&nbsp; <a id="checksimresult-url-link" href=""
									alt="URL del WSDL de SIM" title="Ir al WSDL de SIM"
									target="_blank"><span
									class="glyphicon glyphicon-new-window" aria-hidden="true">
								</span></a>
							</p>


							<p id="checksimresult-ok" class="hidden">
								<span class="bold"><bean:message key="conectividad.url.comprobada.estado" />: </span> <img src="../img/up.png"
									alt="Imagen flecha verde hacia arriba"
									title="Comporbación del servicio correcta" />
							</p>



							<p id="checksimresult-ko" class="hidden">
								<span class="bold"><bean:message key="conectividad.url.comprobada.estado" />: </span> <img src="../img/down.png"
									alt="Imagen flecha roja hacia abajo"
									title="Comporbación del servicio incorrecta" />
							</p>
							<p id="checksimresult-error" class="hidden">
								<span class="bold">Error: </span> <span
									id="checksimresult-error-detalle"></span>
							</p>

						</div>
					</div>
				</fieldset>
			</div>

			<div>

				<div id="checkurlform" class="formulario">

					<fieldset>
						<legend>
							<bean:message key="conectividad.form.title" />
						</legend>

						<div id="urlcheck-error-message" class="alert alert-danger hidden">
							<bean:message key="conectividad.url.error.valida" /></div>

						<p><bean:message key="conectividad.url.comprobada.title"/></p>
						
						<p><bean:message key="conectividad.url.comprobada.subititle"/></p>


						<div class="formItem">
							<label for="url" class="labelCorto"><strong
								class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym>
									<bean:message key="conectividad.form.url" />: </strong></label> <input
								id="urlcheck" type="text" class="textoCorto" name="url"
								required="required" /> <span id="checkurl" onclick="checkurl()"
								class="btn btn-default btn-sm"><bean:message key="conectividad.url.comprobar"/> </span> <span
								id="checkingurl" class="btn btn-default btn-sm hidden"> <span
								class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>&nbsp;<bean:message key="conectividad.url.comprobando"/>
							</span>
						</div>

						<div id="checkurlresult" class="hidden">




							<ul>


								<li><span class="bold">URL:</span> &nbsp; <span
									id="checkurlresult-url"></span> &nbsp;<a
									id="checkurlresult-url-link" href="" alt="URL comprobada"
									title="Ir al URL comprobada" target="_blank"><span
										class="glyphicon glyphicon-new-window" aria-hidden="true">
									</span></a></li>


								<li>
									<p id="checkurlresult-ok" class="hidden">
										<span class="bold"><bean:message key="conectividad.url.oaw"/> :</span>&nbsp;<img
											src="../img/up.png" alt="Imagen flecha verde hacia arriba"
											title="Comporbación del servicio correcta" />
									</p>
									<p id="checkurlresult-ko" class="hidden">
										<span class="bold"><bean:message key="conectividad.url.oaw"/>:</span>&nbsp; <img
											src="../img/down.png" alt="Imagen flecha roja hacia abajo"
											title="Comporbación del servicio incorrecta" /> <span
											id="checkurlresult-error" class="hidden">&nbsp;<span
											class="bold">Error:</span>&nbsp; <span
											id="checkurlresult-error-detalle"></span></span>
									</p>

								</li>



								<li>

									<p id="checkurlresult-ok-proxy" class="hidden">
										<span class="bold"><bean:message key="conectividad.url.js"/>:</span>&nbsp;<img
											src="../img/up.png" alt="Imagen flecha verde hacia arriba"
											title="Comporbación del servicio correcta" />
									</p>

									<p id="checkurlresult-ko-proxy" class="hidden">
										<span class="bold"><bean:message key="conectividad.url.js"/>:</span>&nbsp;
										<img src="../img/down.png"
											alt="Imagen flecha roja hacia abajo"
											title="Comporbación del servicio incorrecta" /><span
											id="checkurlresult-error-proxy" class="hidden">&nbsp;<span
											class="bold">Error:</span>&nbsp; <span
											id="checkurlresult-error-detalle-proxy"></span></span>
									</p>



								</li>


							</ul>
						</div>





					</fieldset>
				</div>

			</div>


			<div>

				<div id="proxyform" class="formulario">

					<fieldset>
						<legend>
							<bean:message key="conectividad.proxy.title" />
						</legend>

						<div id="proxy-url-error-message"
							class="alert alert-danger hidden"><bean:message key="conectividad.proxy.config.error.url"/> </div>



						<div id="proxy-port-error-message"
							class="alert alert-danger hidden"><bean:message key="conectividad.proxy.config.error.port"/></div>


						<p><bean:message key="conectividad.proxy.config.title"/> </p>


						<div class="formItem">
							<label for="url" class="labelCorto"><strong
								class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym>
									<bean:message key="conectividad.proxy.activo" />: </strong></label>


							<logic:equal name="proxyconfig" property="status" value="1">
								<input id="proxyStatus" type="checkbox" class="textoCorto"
									name="proxyStatus" required="required" checked="checked"
									style="width: auto !important;" />
							</logic:equal>
							<logic:equal name="proxyconfig" property="status" value="0">
								<input id="proxyStatus" type="checkbox" class="textoCorto"
									style="width: auto !important;" name="proxyStatus"
									required="required" />
							</logic:equal>




						</div>
						<div class="formItem">
							<label for="url" class="labelCorto"><strong
								class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym>
									<bean:message key="conectividad.proxy.url" />: </strong></label> <input
								id="proxyUrl" type="text" class="textoCorto" name="proxyUrl"
								required="required"
								value="<bean:write name="proxyconfig" property="url" />" />
							<bean:message key="conectividad.proxy.config.url.tip"/>

						</div>
						<div class="formItem">

							<label for="url" class="labelCorto"><strong
								class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym>
									<bean:message key="conectividad.proxy.puerto" />: </strong></label> <input
								id="proxyPort" type="text" class="textoCorto" name="proxyPort"
								required="required"
								value="<bean:write name="proxyconfig" property="port" />" />




						</div>
						<span id="modifyProxy" onclick="modifyProxy()"
							class="btn btn-default btn-sm"><bean:message key="conectividad.proxy.config.update"/> </span> <span
							id="checkingurl" class="btn btn-default btn-sm hidden"> <span
							class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>
						</span>



					</fieldset>
				</div>

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