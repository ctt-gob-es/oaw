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
