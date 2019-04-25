/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.intav.test;

import org.apache.commons.codec.net.URLCodec;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestObservatory {
    public static void main(String[] args) throws Exception {
        Long testTime = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd(HH'H'mm)");

        int countTestFiles = 0;
        File dirTest = new File("/media/truecrypt1/workspaces-intav/IntavCore/metadata/observatoryTest/tests/");
        for (int i = 0; i < dirTest.listFiles().length; i++) {
            if (!dirTest.listFiles()[i].isDirectory()) {
                countTestFiles++;
            }
        }

        for (int i = 0; i < countTestFiles; i++) {
            String content = getFileText("/media/truecrypt1/workspaces-intav/IntavCore/metadata/observatoryTest/tests/test" + i + ".html");

            HttpURLConnection connection = getConnection("http://rastreador.inteco.es/intav/checkAccessibilityAction.do", "POST", true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            URLCodec codec = new URLCodec();
            String postRequest = "content=" + codec.encode(content) + "&guideline=observatorio-inteco-1-0&level=aa&action=analyze";
            writer.write(postRequest);
            writer.flush();
            writer.close();

            connection.connect();

            String result1 = getTextContent(connection).trim();
            String result2 = getFileText("/media/truecrypt1/workspaces-intav/IntavCore/metadata/observatoryTest/results/result" + i + ".html").trim();

            if (normalize(result1).equals(normalize(result2))) {
                System.out.println("Test " + i + " pasado con éxito");
            } else {
                System.out.println("Test " + i + " no válido");
                File file = new File("/media/truecrypt1/workspaces-intav/IntavCore/metadata/observatoryTest/failed/" + dateFormat.format(new Date(testTime)) + "/result" + i + ".html");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                writeToFile(file.getPath(), result1);
            }
        }
    }

    private static HttpURLConnection getConnection(String url, String method, boolean followRedirects) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setRequestMethod(method);
        connection.setConnectTimeout(120000);
        connection.setReadTimeout(120000);
        connection.addRequestProperty("Accept", "text/html");
        connection.addRequestProperty("Accept-Language", "es,en;q=0.8,es-es;q=0.5,en-us;q=0.3");
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES; rv:1.9.1.3) Gecko/20090915 Firefox/3.5.3 (.NET CLR 3.5.30729)");
        return connection;
    }

    private static String getTextContent(HttpURLConnection connection) throws Exception {
        String textContent = getContentAsString(connection.getInputStream(), null);

        return textContent;
    }

    // Convierte un inputStream en un string
    private static String getContentAsString(InputStream in, String charset) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            if (charset != null) {
                out.append(new String(b, 0, n, charset));
            } else {
                out.append(new String(b, 0, n));
            }
        }
        return out.toString();
    }

    private static String getFileText(String filePath) throws Exception {
        String text = new String();
        FileReader reader = new FileReader(filePath);
        BufferedReader bfReader = new BufferedReader(reader);
        String line = bfReader.readLine();
        while (line != null) {
            text += line + "\n";
            line = bfReader.readLine();
        }

        return text;
    }

    private static String normalize(String text) {
        // Quitamos los JSESSIONID
        Pattern pattern = Pattern.compile("(;jsessionid=[0-9A-F]*)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            text = text.replaceAll(matcher.group(1), "");
        }

        text = text.replaceAll("\\s", "");

        return text;
    }

    private static void writeToFile(String filePath, String text) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, true);
            fileWriter.write(text);
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (Exception e) {
                System.out.println("No ha habido manera de loguear");
                e.printStackTrace();
            }
        }
    }
}
