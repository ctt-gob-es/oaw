<%@page language="java"  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- <%@page import="java.util.Base64"%> --%>
<%@page import= "java.io.InputStreamReader" %>
<%@page import= "java.io.BufferedReader" %>
<%@page import= "java.io.OutputStreamWriter" %>
<%@page import= "java.net.*" %>
<%@page import= "java.util.ArrayList" %>
<%@page import= "java.util.List" %>
<%@page import= "java.util.Properties" %>
<%@page import="org.apache.commons.codec.binary.Base64"%>

<%--
    Necesita las librerias commons-fileupload-1.2.1.jar y commons-io-1.3.2.jar
--%>
<%@page import= "org.apache.tomcat.util.http.fileupload.*" %>     
<%@page import= "org.apache.tomcat.util.http.fileupload.disk.*" %>
<%@page import= "org.apache.tomcat.util.http.fileupload.servlet.*" %>
<%@page import= "org.apache.commons.codec.net.URLCodec" %>
<%!
    // URL donde se encuentra ubicado el servidor OAW (dependerá del entorno donde estemos)
    // Indicar únicamente dominio + contexto de despliegue.
    // Valores típicos serán:
    // Producción: http://oaw.redsara.es/oaw/ 
    // Preproducción: http://pre-oaw.redsara.es/oaw/ 
    // Integración: http://des-oaw.redsara.es/oaw/
    private final static String BASE_URL = "http://localhost:8080/oaw/";   

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
        String profundidad;
        String amplitud;
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
        String depthReport;

        private final List<String> errores;

        public RequestManager(final HttpServletRequest request) {
            try {
                host = new URL(BASE_URL);
            } catch (Exception e) {
                throw new Error("ERROR de configuración.", e);
            } 

            // Create a factory for disk-based file items
            final DiskFileItemFactory factory = new DiskFileItemFactory();
            // Configure a repository (to ensure a secure temp location is used)
            final ServletContext servletContext = request.getServletContext();
            final java.io.File repository = (java.io.File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);

            // Create a new file upload handler
            final ServletFileUpload upload = new ServletFileUpload(factory);
            // Parse the request
            try {
                final List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));

                final java.util.Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    final FileItem item = (FileItem) iterator.next();
                    if (item.isFormField()) {
                        readParam(item);
                    } else {
                        // Si no es campo de formulario es el fichero
                        fileName = item.getName();
                        try{
                            //codigo = Base64.getUrlEncoder().encodeToString(item.get());
                        	codigo = Base64.encodeBase64URLSafeString(item.get());
                        } catch(Exception e){}
                    }
                }
            } catch (FileUploadException fue) {
            }

            // Controlar el punto del flujo en el que estamos (request, select_historico, confirm_request,...)
            this.errores = new ArrayList<String>();
            validateRequest();
        }

        private void readParam(final FileItem item) {
            final String paramName = item.getFieldName();
            if (paramName.equalsIgnoreCase("url")) {
                this.url = item.getString();
            } else if (paramName.equalsIgnoreCase("urls")) {
                this.urls = item.getString();
            } else if (paramName.equalsIgnoreCase("content")) {
                this.codigo = item.getString();
                this.fileName = item.getName();
            } else if (paramName.equalsIgnoreCase("correo")) {
                this.correo = item.getString();
            } else if (paramName.equalsIgnoreCase("profundidad")) {
                this.profundidad = item.getString();
            } else if (paramName.equalsIgnoreCase("amplitud")) {
                this.amplitud = item.getString();
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
            } else if (paramName.equalsIgnoreCase("depthReport")) {
                this.depthReport = item.getString();
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
                    return "Error, no se ha podido procesar su solicitud (c&oacute;digo de error " + httpConnection.getResponseCode() + "). Por favor int&eacute;ntelo de nuevo pasados unos minutos y si el problema persiste informe sobre ello a la direcci&oacute;n de correo observ_accesibilidad@correo.gob.es.<br/>";
                } else {
                    final String serverResponse = readServerResponse(httpConnection);
                    httpConnection.disconnect();
                    return serverResponse;
                }
            } catch (Exception e) {
                return "Error, no se ha podido procesar su solicitud " + e.getLocalizedMessage();
            }
        }

        private String buildPostRequest() {
            final URLCodec codec = new URLCodec();
            try {
                final String encodedCodigo = codec.encode(codigo);
                //Save filename
                final String postRequest = String.format("content=%s&url=%s&correo=%s&complexity=%s&informe=%s&usuario=%s&inDirectory=%s&registerAnalysis=%s&analysisToDelete=%s&informe-nobroken=%s&urls=%s&type=%s&filename=%s&depthReport=%s",
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
                        fileName,
                        depthReport
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
                    historicoParamQuery = "?type="+type+"&url=" + java.net.URLEncoder.encode(url, "ISO-8859-1")+"&depth="+profundidad+"&width="+amplitud+"&report="+informe;
                } else if ( "lista_urls".equalsIgnoreCase(type) ) {
                    historicoParamQuery = "?type="+type+"&url=" + java.net.URLEncoder.encode(urls, "ISO-8859-1")+"&depth="+profundidad+"&width="+amplitud+"&report="+informe;
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
                  errores.add("Indique al menos una url para análisis de tipo 'Conjunto de URLs'");
                } else {
                    for (String domain: urls.split("\r\n")) {
                        if (!domain.startsWith("http") && !domain.startsWith("https")) {
                            errores.add("La URL " + domain + " debe comenzar por http:// o https://");
                        }
                    }
                    
                    String[] domains = urls.split("\r\n");
                    if("observatorio-3".equals(informe) && domains.length > 33){                    	
                    	errores.add("El n&uacute;mero m&aacute;ximo de URLs a analizar para esta metodolog&iacute;a (versi&oacute;n 2) es de 33");
                    } else if(("observatorio-2".equals(informe) || "observatorio-1".equals(informe)) && domains.length > 17){
                    	errores.add("El n&uacute;mero m&aacute;ximo de URLs a analizar para esta metodolog&iacute;a  es de 17");
                    }
                    
                }
                if (isEvolutivoHistorico()) {
                    final String[] domains = urls.split("\r\n");
                    if("observatorio-3".equals(informe) && domains.length > 33){
                    	errores.add("El evolutivo para an&aacute;lisis de tipo 'Conjunto de URLs' para esta metodolog&iacute;a (versi&oacute;n 2) requiere 33 p&aacute;ginas");	
                    }
                    else if(("observatorio-2".equals(informe) || "observatorio-1".equals(informe)) && domains.length!=17) {
                        errores.add("El evolutivo para análsis de tipo 'Conjunto de URLs' requiere 17 páginas");
                    } else {
                        try {
                            final String pattern = new URL(domains[0]).getHost();
                            for (String domain: domains) {
                                final String host = new URL(domain).getHost();
                                if ( !host.equalsIgnoreCase(pattern) ) {
                                    errores.add("El evolutivo para análisis de tipo 'Conjunto de URLs' solo está permitido para páginas del mismo 'host' (exactamente mismo dominio) " + domain + " | " +pattern);
                                    break;
                                }
                            }
                        } catch (MalformedURLException murle) {
                            errores.add("Error al comprobar el dominio de un análisis de tipo 'Conjunto de URLs'");
                        }
                    }
                }
            } else {
            	
                if (codigo.length() > 4194304) {
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
                fieldset.innerHTML += "<p>No existen an&aacute;lisis existentes para la url indicada. Se guardar&aacute;n los resultados para incluirlos como referencia en posteriores an&aacute;lisis.<\/p>";
              }
            }
        </script>
        <link href="/oaw/bootstrap/css/bootstrap.min.css" rel="stylesheet">
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
            final RequestManager requestManager = new RequestManager(request);
            if (requestManager.isValidRequest()) {
                if (requestManager.isConfirmed()) {
                    out.println("<div class=\"mensaje\">");
                    
                    out.println(requestManager.launchServicioDiagnostico());
                    out.println("</div>");
                } else if (requestManager.isEvolutivoHistorico()) { %>
            <form method="POST" enctype="multipart/form-data">
                <input type="hidden" name="confirm"             value="true">
                <input type="hidden" name="url"                 value="<%=requestManager.url %>">
                <input type="hidden" name="urls"                value="<%=requestManager.urls %>">
                <input type="hidden" name="profundidad"         value="<%=requestManager.profundidad %>">
                <input type="hidden" name="amplitud"            value="<%=requestManager.amplitud %>">
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
                <a class="btn btn-default btn-lg" href="/oaw/diagnostico.html">Volver</a>
            </p>
        </div>
    </body>
</html>
