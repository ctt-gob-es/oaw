<%@page language="java"  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import= "java.io.InputStreamReader" %>
<%@page import= "java.io.BufferedReader" %>
<%@page import= "java.io.OutputStreamWriter" %>
<%@page import= "java.net.*" %>
<%@page import= "java.util.ArrayList" %>
<%@page import= "java.util.List" %>
<%@page import= "java.util.Properties" %>
<%--
    Necesita las librerias de apache commons-fileupload-1.2.1.jar y commons-io-1.3.2.jar
--%>
<%@page import= "org.apache.commons.fileupload.*" %>
<%@page import= "org.apache.commons.fileupload.disk.*" %>
<%@page import= "org.apache.commons.fileupload.servlet.*" %>
<%@page import= "org.apache.commons.codec.net.URLCodec" %>
<%@page import= "org.apache.commons.lang.StringUtils" %>
<%@page import= "es.minhap.common.properties.PropertiesServices" %>
<%@page import= "es.minhap.common.security.util.UserSpringSecurityPAe" %>

<%!
    // Path de los servicios que se invocarán. Se mantienen invariables entre entornos y solo hará falta modificarlos como consecuencia de cambios en la aplicación.
    private final static String BASIC_SERVICE_ENDPOINT = "basicServiceAction.do";
    private final static String HISTORICO_ENDPOINT = "checkHistorico.do";

    // Clase para manejar las solicitudes al servicio de diagnóstico y realizar peticiones a los servicios que manejan el historico/evolutivo del servicio de diagnóstico.
    class RequestManager {
        private final URL host;
        String codigo;
        String url = "";
        String urls = "";
        String correo;
        String informe;
        String nobroken;
        String username;
        String directorio = "false";
        String registerAnalysis = "false";
        String analysisToDelete = "";
        String type = "url";
        String confirm = "false";
        String complexity ="";
        String fileName;


        private final List<String> errores;
        private final PropertiesServices propertiesServices;

        // Proxy
        final String RUTA_PROXY;
        final String OLD_TIME_OUT;
        final String OLD_PROXY_HOST;
        final String OLD_PROXY_PORT;

        public RequestManager(final HttpServletRequest request, final HttpServletResponse response) {
            propertiesServices = new PropertiesServices(request);
            username = UserSpringSecurityPAe.getNameUserSpringSecurity(request, response);
            try {
                // URL donde se encuentra ubicado el servidor OAW (dependerá del entorno donde estemos)
                // Indicar únicamente dominio + contexto de despliegue.
                // Ej. http://oaw.redsara.es/oaw/
                host = new URL(propertiesServices.getMessage("servicio.accesibilidad.url", null));
            } catch (Exception e) {
                throw new Error("ERROR de configuraci&oacuten.", e);
            }

            RUTA_PROXY = propertiesServices.getMessage("servicio.accesibilidad.proxy", null);
            OLD_TIME_OUT = System.getProperties().getProperty("sun.net.client.defaultReadTimeout");
            OLD_PROXY_HOST = System.getProperties().getProperty("http.proxyHost");
            OLD_PROXY_PORT = System.getProperties().getProperty("http.proxyPort");

            // Create a factory for disk-based file items
            final DiskFileItemFactory factory = new DiskFileItemFactory();
            // Configure a repository (to ensure a secure temp location is used)
//             final ServletContext servletContext = request.getServletContext();
//             final java.io.File repository = (java.io.File) servletContext.getAttribute("javax.servlet.context.tempdir");
            
            final java.io.File repository  = (java.io.File) request.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);

            // Create a new file upload handler
            final ServletFileUpload upload = new ServletFileUpload(factory);
            // Parse the request
            try {
                final List<FileItem> items = upload.parseRequest(request);

                final java.util.Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    final FileItem item = (FileItem) iterator.next();
                    if (item.isFormField()) {
                        readParam(item);
                    } else {
                        // Si no es campo de formulario es el fichero
                        codigo = new String(item.get());
                        fileName = item.getName();
                    }
                }
            } catch (FileUploadException fue) {
            }

            this.errores = new ArrayList<String>();
            validateRequest();
            initProxy();
        }

        private void readParam(final FileItem item) {
            final String paramName = item.getFieldName();
            if (paramName.equalsIgnoreCase("url")) {
                this.url = item.getString();
            } else if (paramName.equalsIgnoreCase("urls")) {
                this.urls = item.getString();
            } else if (paramName.equalsIgnoreCase("content")) {
                this.codigo = item.getString();
                
              //Save filename
                this.fileName = item.getName();
                
            } else if (paramName.equalsIgnoreCase("correo")) {
                this.correo = item.getString();
            } else if (paramName.equalsIgnoreCase("informe")) {
                this.informe = item.getString();
            } else if (paramName.equalsIgnoreCase("informe-nobroken")) {
                this.nobroken = item.getString();
            } else if (paramName.equalsIgnoreCase("username")) {
                this.username = item.getString();
            } else if (paramName.equalsIgnoreCase("inDirectory")) {
                this.directorio = item.getString();
            } else if (paramName.equalsIgnoreCase("registerAnalysis")) {
                this.registerAnalysis = item.getString();
            } else if (paramName.equalsIgnoreCase("analysisToDelete")) {
                this.analysisToDelete = item.getString();
            } else if (paramName.equalsIgnoreCase("type")) {
                this.type = item.getString();
            } else if (paramName.equalsIgnoreCase("confirm")) {
                this.confirm = item.getString();
            } else if (paramName.equalsIgnoreCase("complexity")) {
                this.complexity = item.getString();
            }
        }

        private void initProxy() {
            if (StringUtils.isNotEmpty(RUTA_PROXY)) {
                Properties systemProperties = System.getProperties();
                systemProperties.setProperty("sun.net.client.defaultReadTimeout", propertiesServices.getMessage("servicio.accesibilidad.read.timeout", null));
            	systemProperties.setProperty("http.proxyHost", RUTA_PROXY);
            	systemProperties.setProperty("http.proxyPort", propertiesServices.getMessage("servicio.accesibilidad.proxy.port", null));
            }
        }

        private void revertProxy() {
            if (StringUtils.isNotEmpty(RUTA_PROXY)) {
                Properties systemProperties = System.getProperties();
                if (StringUtils.isNotEmpty(OLD_TIME_OUT)) {
                    System.setProperty("sun.net.client.defaultReadTimeout", OLD_TIME_OUT);
                }
                if (StringUtils.isNotEmpty(OLD_PROXY_HOST)) {
                    System.setProperty("http.proxyHost", OLD_PROXY_HOST);
                }
                if (StringUtils.isNotEmpty(OLD_PROXY_PORT)) {
                    System.setProperty("http.proxyPort", OLD_PROXY_PORT);
                }
            }
        }

        public boolean isValidRequest() {
            return errores.isEmpty();
        }

        public List<String> getErrores() {
            return errores;
        }

        public String launchServicioDiagnostico() {
            try {
                final HttpURLConnection httpConnection = createConnection(BASIC_SERVICE_ENDPOINT);
                final String postRequest = buildPostRequest();

                final OutputStreamWriter writer = new OutputStreamWriter(httpConnection.getOutputStream());
                writer.write(postRequest);
                writer.flush();
                writer.close();

                httpConnection.connect();

                if (httpConnection.getResponseCode() != 200) {
                    httpConnection.disconnect();
                    return "Error, no se ha podido procesar su solicitud (c&oacute;digo de error " + httpConnection.getResponseCode() + "). Por favor int&eacute;ntelo de nuevo pasados unos minutos y si el problema persiste informe sobre ello a la direcci&oacute;n de correo observ_accesibilidad@correo.gob.es<br/>";
                } else {
                    final String serverResponse = readServerResponse(httpConnection);
                    httpConnection.disconnect();
                    return serverResponse;
                }
            } catch (Exception e) {
                return "Error, no se ha podido procesar su solicitud " + e.getLocalizedMessage();
            } finally {
                revertProxy();
            }
        }

        private String buildPostRequest() {
            final URLCodec codec = new URLCodec();
            try {
                final String encodedCodigo = codec.encode(codigo);
                final String postRequest = String.format("content=%s&url=%s&correo=%s&complexity=%s&informe=%s&usuario=%s&inDirectory=%s&registerAnalysis=%s&analysisToDelete=%s&informe-nobroken=%s&urls=%s&type=%s&filename=%s",
                        encodedCodigo != null ? encodedCodigo : "",
                        url != null ? url : "",
                        correo,
                        complexity,
                        informe,
                        username,
                        directorio,
                        registerAnalysis,
                        analysisToDelete,
                        nobroken,
                        urls,
                        type,
                        fileName
                );
                return postRequest;
            } catch (Exception e) {
                return "";
            }
        }

        public boolean isEvolutivoHistorico() {
            return Boolean.parseBoolean(registerAnalysis);
        }

        public String getHistorico() {
            try {
                final String historicoParamQuery;
                if ( "url".equalsIgnoreCase(type) ) {
                    historicoParamQuery = "?type="+type+"&url=" + java.net.URLEncoder.encode(url, "ISO-8859-1");
                } else if ( "lista_urls".equalsIgnoreCase(type) ) {
                    historicoParamQuery = "?type="+type+"&url=" + java.net.URLEncoder.encode(urls, "ISO-8859-1");
                } else {
                    historicoParamQuery = "";
                }
                final HttpURLConnection httpConnection = createConnection(HISTORICO_ENDPOINT + historicoParamQuery);
                httpConnection.connect();

                if (httpConnection.getResponseCode() != 200) {
                    return "{\"historico\":[]}";
                } else {
                    final String serverResponse = readServerResponse(httpConnection);
                    return serverResponse;
                }
            } catch (Exception e) {
                return "{\"historico\":[]}";
            } finally {
                revertProxy();
            }
        }

        public String generateCallHistoricoJavaScriptFunction(final String historicoResult) {
            return String.format("<script type='text/javascript'>checkHistoricoResults(JSON.parse('%s'));</script>", historicoResult);
        }

        private HttpURLConnection createConnection(final String endpoint) {
            try {
                final URL url = new URL(host, endpoint);
                final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(15000);
                httpConnection.setReadTimeout(15000);
                httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpConnection.setDoOutput(true);

                return httpConnection;
            } catch (Exception e) {
                return null;
            }
        }

        private String readServerResponse(final HttpURLConnection httpConnection) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                return in.readLine();
            } catch (Exception e) {
                return "";
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

        private void validateRequest() {
            if (!correo.contains("@")) {
                errores.add("La direcci&oacute;n de correo electr&oacute;nico no es v&aacute;lida");
            }
            if (isCrawlingRequest()) {
                if (!url.startsWith("http") && !url.startsWith("https")) {
                    errores.add("La URL debe comenzar por http:// o https://");
                } else {
                    try {
                        this.url = java.net.URLEncoder.encode(url, "ISO-8859-1");
                    } catch (Exception e) {
                        try {
                            this.url = java.net.URLEncoder.encode(url, "UTF-8");
                        } catch (Exception eutf8) {
                            errores.add("La URL tiene caracteres que no se pueden codificar");
                        }
                    }
                }
               
            } else if (isListaUrlsRequest()) {
                if (urls.isEmpty()) {
                  errores.add("Indique al menos una URL para an&aacutelisis de tipo 'Conjunto de URLs'");
                } else {
                    for (String domain: urls.split("\r\n")) {
                        if (!domain.startsWith("http") && !domain.startsWith("https")) {
                            errores.add("La URL " + domain + " debe comenzar por http:// o https://");
                        }
                    }
                    // Comprobar el número de URLs introducidas
                    String[] domains = urls.split("\r\n");
                    if("observatorio-3".equals(informe) && domains.length > 33){                    	
                   		 errores.add("El n&uacute;mero m&aacute;ximo de URLs a analizar para esta metodolog&iacute;a (versi&oacute;n 2) es de 33");
                    } else if("observatorio-4".equals(informe) && domains.length > 51){
                    	errores.add("El n&uacute;mero m&aacute;ximo de URLs a analizar para esta metodolog&iacute;a  es de 51");
                    }
                }
                
                if (isEvolutivoHistorico()) {
                    final String[] domains = urls.split("\r\n");
                    if("observatorio-3".equals(informe) && domains.length > 33){
                    	errores.add("El evolutivo para an&aacute;lisis de tipo 'Conjunto de URLs' para esta metodolog&iacute;a (versi&oacute;n 2) requiere 33 p&aacute;ginas");	
                    }
                    else if(("observatorio-2".equals(informe) || "observatorio-1".equals(informe)) && domains.length!=17) {
                        errores.add("El evolutivo para an&aacute;lisis de tipo 'Conjunto de URLs' requiere 17 p&aacute;ginas");
                    } else {
                        try {
                            final String pattern = new URL(domains[0]).getHost();
                            for (String domain: domains) {
                                final String host = new URL(domain).getHost();
                                if ( !host.equalsIgnoreCase(pattern) ) {
                                    errores.add("El evolutivo para an&aacute;lisis de tipo 'Conjunto de URLs' solo est&aacute; permitido para p&aacute;ginas del mismo 'host' (exactamente mismo dominio) " + domain + " | " +pattern);
                                    break;
                                }
                            }
                        } catch (MalformedURLException murle) {
                            errores.add("Error al comprobar el dominio de un an&aacute;lisis de tipo 'Conjunto de URLs'");
                        }
                    }
                }
            } else {
                if (codigo.length() > 2000000) {
                    errores.add("El c&oacute;digo fuente a analizar es demasiado largo");
                }
                this.registerAnalysis = "false";
            }
        }

        private boolean isCrawlingRequest() {
            return "url".equalsIgnoreCase(type);
        }

        private boolean isListaUrlsRequest() {
            return "lista_urls".equalsIgnoreCase(type);
        }

        public boolean isConfirmed() {
            return Boolean.parseBoolean(confirm);
        }
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Enviando datos</title>
        <script type="text/javascript">
            function createRadioElement(value, label, checked) {
              var radioHtml = '<input type="radio" id="delete_id_' + value + '" name="analysisToDelete" value="' + value + '"';
              if (checked) {
                radioHtml += ' checked="checked"';
              }
              radioHtml += '/>';

              var radioFragment = document.createElement('div');
              radioFragment.class= 'radio'
              radioFragment.innerHTML = radioHtml;

              var labelHtml = ' <label for="delete_id_' + value + '">' + label + '<\/label> ';
              radioFragment.innerHTML += labelHtml;

              return radioFragment;
            }
            function checkHistoricoResults(historicoResult) {
              var fieldset = document.getElementById('historico_resultados');
              if (historicoResult.historico.length > 0) {
                if (historicoResult.historico.length < 3) {
                    fieldset.innerHTML += "<p>A continuaci&oacute;n se indican los an&aacute;lisis existentes para el conjunto de URLs indicadas. Pueden registrarse hasta un m&aacute;ximo de 3 an&aacute;lisis, si lo desea puede seleccionar uno de ellos a eliminar:<\/p>";
                } else {
                    fieldset.innerHTML += "<p>A continuaci&oacute;n se indican los an&aacute;lisis existentes para el conjunto de URLs indicadas. Pueden registrarse hasta un m&aacute;ximo de 3 an&aacute;lisis por lo que debe seleccionar uno de ellos a eliminar para que sea sustituido por el actual:<\/p>";
                }
                for (i in historicoResult.historico) {
                  fieldset.appendChild(createRadioElement(historicoResult.historico[i].id, historicoResult.historico[i].date, i == 2));
                }
              } else {
                fieldset.innerHTML += "<p>No existen an&aacute;lisis existentes para la URL indicada. Se guardar&aacute;n los resultados para incluirlos como referencia en posteriores an&aacute;lisis.<\/p>";
              }
            }
        </script>
        <!-- Bootstrap -->
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <link rel="icon" href="images/favicon.ico" type="image/x-icon"/>

        <style type="text/css">
            #cajaformularios form {
                display: block;
            }
            #cajaformularios form fieldset,
            #cajaformularios div.mensaje {
                background-color: #fff;
                border: 1px solid #e5e5e5;
                margin-top: 25px;
                padding: 15px 25px;
            }
            #cajaformularios p {
                margin: 0 0 10px;
            }
            #cajaformularios #pCenter {
                margin-top: 30px;
                text-align: center;
            }
            /* Formularios */
            input:focus {border-color:#e21430;}
            textarea{overflow:auto;}
            fieldset ul li {list-style-image:none;list-style:none;margin:0%;padding:0%;}
            fieldset ul {list-style:none;margin:0%;padding:0%;margin-top:1%;}
            fieldset legend {background-color: #fff; border: 1px solid #e5e5e5;color:#E60D2E;font-size:130%; margin-bottom:0; padding:0 0.5%;}
            fieldset {padding-top: 1%; padding-bottom: 0.5%; margin-bottom:1%; margin-top:1%;border :none;}
            fieldset p {margin:1% 0% 0% 1%;}
            .formulario fieldset {padding: 1% 4%;}
            #cajaformularios {
                border: 1px solid #e5e5e5;
                padding: 1% 4%;
                background: #e5e5e5;
                background: -moz-linear-gradient(top, #ffffff 0%, #dbdbdb 100%, #d1d1d1 100%, #e5e5e5 100%);
                background: -webkit-linear-gradient(top, #ffffff 0%,#dbdbdb 100%,#d1d1d1 100%,#e5e5e5 100%);
                background: linear-gradient(to bottom, #ffffff 0%,#dbdbdb 100%,#d1d1d1 100%,#e5e5e5 100%);
                filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffffff', endColorstr='#e5e5e5',GradientType=0 );
            }
            body{margin: 1% 1% 0;}
        </style>
    </head>
    <body>
        <div id="cajaformularios">
        <%
            final RequestManager requestManager = new RequestManager(request, response);
            if (requestManager.isValidRequest()) {
                if (requestManager.isConfirmed()) {
                    out.println("<div class=\"mensaje\">");
                    out.println(requestManager.launchServicioDiagnostico());
                    out.println("</div>");
                } else if (requestManager.isEvolutivoHistorico()) {%>
        <form method="POST" enctype="multipart/form-data">
            <input type="hidden" name="confirm"             value="true">
            <input type="hidden" name="url"                 value="<%=requestManager.url %>">
            <input type="hidden" name="urls"                value="<%=requestManager.urls %>">
            <input type="hidden" name="correo"              value="<%=requestManager.correo %>">
			<input type="hidden" name="complexity"          value="<%=requestManager.complexity %>">
            <input type="hidden" name="informe"             value="<%=requestManager.informe %>">
            <input type="hidden" name="informe-nobroken"    value="<%=requestManager.nobroken!=null?requestManager.nobroken:"" %>">
            <input type="hidden" name="registerAnalysis"    value="<%=requestManager.registerAnalysis %>">
            <input type="hidden" name="type"                value="<%=requestManager.type %>">

            <fieldset id="historico_resultados">
                <legend>Histórico de resultados</legend>                

            </fieldset>
            <div><input type="submit" class="btn btn-primary btn-lg" value="Enviar"></div>
        </form>
        <%
                    out.println(requestManager.generateCallHistoricoJavaScriptFunction(requestManager.getHistorico()));
                } else {
                    out.println("<div class=\"mensaje\">");
                    out.println(requestManager.launchServicioDiagnostico());
                    out.println("</div>");
                }
            } else {
                // Si los parámetros no son válidos se muestran los errores.
                out.println("<p><strong>ERROR</strong> los par&aacute;metros no son correctos.</p>");
                out.println("<ul>");
                for (String error : requestManager.getErrores()) {
                    out.println("<li>" + error + "</li>");
                }
                out.println("</ul>");
            } %>
            <p id="pCenter">
                <a class="btn btn-default btn-lg" onclick="history.back()">Volver</a>
            </p>
        </div>
    </body>
</html>
