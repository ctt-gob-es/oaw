<%@page language="java"  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import= "java.io.InputStreamReader" %>
<%@page import= "java.io.BufferedReader" %>
<%@page import= "java.io.OutputStreamWriter" %>
<%@page import= "java.net.*" %>
<%@page import= "java.util.ArrayList" %>
<%@page import= "java.util.List" %>
<%@page import= "java.util.Objects" %>
<%@page import= "java.util.Properties" %>
<%@page import= "org.apache.tomcat.util.http.fileupload.*" %>
<%@page import= "org.apache.tomcat.util.http.fileupload.servlet.*" %>
<%@page import= "org.apache.tomcat.util.http.fileupload.disk.*" %>
<%@page import= "org.apache.commons.codec.net.URLCodec" %>
<%!
    // URL donde se encuentra ubicado el servidor OAW (dependerá del entorno donde estemos)
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
                        codigo = new String(item.get());
                    }
                }
            } catch (FileUploadException fue) {
            }

            // TODO: Controlar el punto del flujo en el que estamos (request, select_historico, confirm_request,...)
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
                    return "Error, no se ha podido procesar su solicitud (c&oacute;digo de error " + httpConnection.getResponseCode() + "). Por favor int&eacute;ntelo de nuevo pasados unos minutos y si el problema persiste informe sobre ello a la direcci&oacute;n de correo observ_accesibilidad@mpt.es.<br/>";
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
                final String postRequest = String.format("content=%s&url=%s&correo=%s&profundidad=%s&amplitud=%s&informe=%s&usuario=%s&inDirectory=%s&registerAnalysis=%s&analysisToDelete=%s&informe-nobroken=%s&urls=%s&type=%s",
                        Objects.toString(codec.encode(codigo), ""),
                        Objects.toString(url, ""),
                        correo,
                        profundidad,
                        amplitud,
                        informe,
                        username,
                        directorio,
                        registerAnalysis,
                        analysisToDelete,
                        nobroken,
                        urls,
                        type
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
                final int depth = Integer.parseInt(profundidad);
                final int width = Integer.parseInt(amplitud);
                if (depth < 0 || depth > 4) {
                    errores.add("La profundidad de rastreo es incorrecta");
                }
                if (width < 0 || width > 4) {
                    errores.add("La amplitud de rastreo es incorrecta");
                }
            } else if (isListaUrlsRequest()) {
                for (String domain: urls.split("\r\n")) {
                    if (!domain.startsWith("http") && !domain.startsWith("https")) {
                        errores.add("La URL " + domain + " debe comenzar por http:// o https://");
                    }
                }
                if (isEvolutivoHistorico()) {
                    final String[] domains = urls.split("\r\n");
                    if (domains.length!=17) {
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
                if (codigo.length() > 2000000) {
                    errores.add("El c&oacute;digo fuente a analizar es demasiado largo");
                }
                this.registerAnalysis = "false";
            }
        }

        private boolean isCrawlingRequest() {
            return url!=null && url.trim().length() > 0;
        }

        private boolean isListaUrlsRequest() {
            return "lista_urls".equalsIgnoreCase(type); //urls!=null && urls.trim().length() > 0;
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
              radioFragment.innerHTML = radioHtml;

              var labelHtml = '<label for="delete_id_' + value + '">' + label + '<\/label>';
              radioFragment.innerHTML += labelHtml;

              return radioFragment;
            }
            function checkHistoricoResults(historicoResult) {
              var fieldset = document.getElementById('historico_resultados');
              if (historicoResult.historico.length > 0) {
                fieldset.innerHTML += "<p>A continuaci&oacute;n se indican los an&aacute;lisis existentes para la url indicada. Seleccione el an&aacute;lisis a eliminar.<\/p>";
                for (i in historicoResult.historico) {
                  fieldset.appendChild(createRadioElement(historicoResult.historico[i].id, historicoResult.historico[i].date, i == 2));
                }
              } else {
                fieldset.innerHTML += "<p>No existen an&aacute;lisis existentes para la url indicada. Se guardar&aacute;n los resultados para incluirlos como referencia en posteriores an&aacute;lisis.<\/p>";
              }
            }
        </script>
    </head>
    <body>
        <%
            final RequestManager requestManager = new RequestManager(request);
            if (requestManager.isValidRequest()) {
                if (requestManager.isConfirmed()) {
                    out.println(requestManager.launchServicioDiagnostico());
                } else if (requestManager.isEvolutivoHistorico()) {%>
        <form method="POST" enctype="multipart/form-data">
            <input type="hidden" name="confirm"             value="true">
            <input type="hidden" name="url"                 value="<%=requestManager.url%>">
            <input type="hidden" name="urls"                value="<%=requestManager.urls%>">
            <input type="hidden" name="profundidad"         value="<%=requestManager.profundidad%>">
            <input type="hidden" name="amplitud"            value="<%=requestManager.amplitud%>">
            <input type="hidden" name="correo"              value="<%=requestManager.correo%>">
            <input type="hidden" name="informe"             value="<%=requestManager.informe%>">
            <input type="hidden" name="informe-nobroken"    value="<%=Objects.toString(requestManager.nobroken,"")%>">
            <input type="hidden" name="registerAnalysis"    value="<%=requestManager.registerAnalysis%>">
            <input type="hidden" name="type"                value="<%=requestManager.type%>">

            <fieldset id="historico_resultados">
                <legend>Histórico de resultados</legend>                

            </fieldset>
            <div><input type="submit" value="Enviar"></div>
        </form>
        <%
                    out.println(requestManager.generateCallHistoricoJavaScriptFunction(requestManager.getHistorico()));
                } else {
                    out.println(requestManager.launchServicioDiagnostico());
                }
            } else {
                // Si los parámetros no son válidos se muestran los errores.
                out.println("<p><strong>ERROR</strong> los par&aacute;metros no son correctos.</p>");
                out.println("<ul>");
                for (String error : requestManager.getErrores()) {
                    out.println("<li>" + error + "</li>");
                }
                out.println("</ul>");
            }
        %>
    </body>
</html>
