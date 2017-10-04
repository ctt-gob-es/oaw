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
			$
					.ajax(
							{
								url : '/oaw/secure/conectividad.do?action=checkurl&url='
										+ $('#urlcheck').val(),
								method : 'POST'
							}).success(
							function(data) {

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

								$('#checkurlresult').removeClass('hidden');
								$('#checkingurl').addClass('hidden');
								$('#checkurl').removeClass('hidden');
							});
		}
	}

	function validateEmail(email) {
		var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return re.test(email);
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
				<li class="active"><span class="glyphicon glyphicon-home"
					aria-hidden="true"></span> <bean:message key="migas.conectividad" /></li>
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

						<div id="simcheck-error-message" class="alert alert-danger hidden">Debe
							introducir un email válido</div>

						<p>Comprobar la conectividad con el Sistema Integral de
							Mensajería (SIM). Se intentará enviar un correo electrónico a la
							dirección indicada.</p>

<%-- 						<p>
							<bean:message key="leyenda.campo.obligatorio" />
						</p> --%>

						<p>
							<label for="url" class="labelCorto"><strong
								class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym>
									Email:</strong></label> <input type="text" id="checksimemail" />

							<button id="checksim" onclick="checksim()"
								class="btn btn-primary btn-sm">Comprobar</button>

							<button id="checkingsim" class="btn btn-primary btn-sm hidden">
								<span
									class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>&nbsp;Comprobando
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
								<span class="bold">Estado: </span> <img src="../img/up.png"
									alt="Imagen flecha verde hacia arriba"
									title="Comporbación del servicio correcta" />
							</p>



							<p id="checksimresult-ko" class="hidden">
								<span class="bold">Estado: </span> <img src="../img/down.png"
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

						<div id="urlcheck-error-message" class="alert alert-danger hidden">Debe
							introducir una URL válida</div>

						<p>Comprobar la conectividad con la URL indicada.</p>

<%-- 						<p>
							<bean:message key="leyenda.campo.obligatorio" />
						</p> --%>
						
						<div class="formItem">
							<label for="url" class="labelCorto"><strong
								class="labelVisu"><acronym
									title="<bean:message key="campo.obligatorio" />"> * </acronym>
									<bean:message key="conectividad.form.url" />: </strong></label> <input
								id="urlcheck" type="text" class="textoCorto" name="url"
								required="required" /> <span id="checkurl" onclick="checkurl()"
								class="btn btn-primary btn-sm">Comprobar</span> <span
								id="checkingurl" class="btn btn-primary btn-sm hidden"> <span
								class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>&nbsp;Comprobando
							</span>
						</div>

						<div id="checkurlresult" class="hidden">
							<p>
								<span class="bold">URL:</span> &nbsp; <span
									id="checkurlresult-url"></span> &nbsp;<a
									id="checkurlresult-url-link" href="" alt="URL comprobada"
									title="Ir al URL comprobada" target="_blank"><span
									class="glyphicon glyphicon-new-window" aria-hidden="true">
								</span></a>
							</p>
							<p id="checkurlresult-ok" class="hidden">
								<span class="bold">Estado:</span>&nbsp;<img src="../img/up.png"
									alt="Imagen flecha verde hacia arriba"
									title="Comporbación del servicio correcta" />
							</p>

							<p id="checkurlresult-ko" class="hidden">
								<span class="bold">Estado:</span>&nbsp; <img
									src="../img/down.png" alt="Imagen flecha roja hacia abajo"
									title="Comporbación del servicio incorrecta" />
							</p>
							<p id="checkurlresult-error" class="hidden">
								<span class="bold">Error:</span>&nbsp; <span
									id="checkurlresult-error-detalle"></span>
							</p>
						</div>


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