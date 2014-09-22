<%@ include file="/common/taglibs.jsp" %> 
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>INTECO - RASTREADOR DE MALWARE</title>
	
		<script type="text/javascript">
			function submitForm(){
				document.getElementById('formulario').submit();
			}
		</script>
	</head>
	<BODY onLoad="submitForm();" style="display:none;">
	
		<form id="formulario" action="../<bean:write name="RedireccionConfigForm" property="proyecto" />/secure/ValidarUsuario.do" method="POST">
			<input type="text" name="user" value="<bean:write name="RedireccionConfigForm" property="user"/>"/>
			<input type="text" name="tipo" value="<bean:write name="RedireccionConfigForm" property="tipo"/>"/>
			<input type="password" name="pass" value="<bean:write name="RedireccionConfigForm" property="pass"/>"/>
		</form>
	</body>
</html>