package es.inteco.crawler.sexista.modules.analisis.service.base;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.crawler.sexista.modules.analisis.dto.AnalisisDto;
import es.inteco.crawler.sexista.modules.analisis.service.AnalyzeService;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseTest {

    private static final String ERRROR_MSG_1 = "El número de resultados no es el esperado.";
    private static final String ERROR_MSG_2 = "El numero de terminos esperado no es el mismo que el encontrado";

    protected static final String URL_DEFAULT = "http://junitPrueba.html";
    protected static final String ID_RASTREO_DEFAULT = "";//"idRastreoJunit";

    protected static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    protected AnalyzeService service;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Creamos la fuente de datos y la asignamos mediante JNDI para que coincida con la definida en el context.xml
        try {
            // Create initial context
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

            final InitialContext ic = new InitialContext();
            ic.createSubcontext("java:");
            ic.createSubcontext("java:/comp");
            ic.createSubcontext("java:/comp/env");

            final Context jdbcContext = ic.createSubcontext("java:/comp/env/jdbc");

            // Construct DataSource
            final org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
            ds.setUrl("jdbc:mysql://localhost:3306/inteco_lenox_2");
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            ds.setUsername("root");
            ds.setPassword("root");

            jdbcContext.bind("lenox2", ds);
        } catch (NamingException ex) {
            //Logger.getLogger(MyDAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUp() throws Exception {
        this.service = new AnalyzeService();
    }

    @After
    public void tearDown() throws Exception {
        this.service = null;
    }

    /**
     * Creamos el dto de analisis.
     *
     * @param text - Texto a analizar
     * @return AnalisisDto
     */
    protected AnalisisDto getAnalisisDto(String text) {
        Long idRastreo = Long.valueOf(ID_RASTREO_DEFAULT + System.currentTimeMillis());
        return this.getAnalisisDto(idRastreo, URL_DEFAULT, text);
    }

    protected List<ResultadoDto> find(Long idRastreo) throws BusinessException {

        Connection con = null;
        PreparedStatement ps = null;

        List<ResultadoDto> list = new ArrayList<ResultadoDto>();
        ResultadoDto resultDto = null;

        try {

            con = ConexionBBDD.conectar();

            //Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT * FROM SEXISTA_RESULTADOS WHERE ID_RASTREO = ? AND ID_TERMINO <> 0");

            ps = (PreparedStatement) con.prepareStatement(sb.toString());

            //A�adimos los parametros de seleccion
            ps.setLong(1, idRastreo);

            //Ejecutamos la sql
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resultDto = new ResultadoDto();
                resultDto.setIdResultado(rs.getInt("ID_RESULTADO"));
                resultDto.setIdRastreo(rs.getLong("ID_RASTREO"));
                resultDto.setIdTermino(rs.getInt("ID_TERMINO"));
                resultDto.setContexto(rs.getString("CONTEXTO"));
                resultDto.setUrlTermino(rs.getString("URL_TERMINO"));

                //A�adimos al listado
                list.add(resultDto);
            }

        } catch (Exception e) {
            throw new BusinessException(e);

        } finally {

            try {
                if (ps != null) {
                    ps.close();
                } // end if

                if (con != null) {
                    con.close();
                } // end if

            } catch (SQLException e) {
                throw new BusinessException(e);

            }

        }// end finally

        return list;
    }

    protected List<TerminoDto> getTerminos(List<Integer> lstTerminos) throws Exception {

        TerminoDto dto;
        List<TerminoDto> listado = new ArrayList<TerminoDto>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionBBDD.conectar();

            //Construimos las consulta
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ID_TERMINO, TERMINO, USO_GLOBAL, FEMENINO, PLURAL, FEMENINO_PLURAL ");
            sb.append(" FROM SEXISTA_TERMINOS ");
            sb.append(" WHERE ID_TERMINO IN ( ");

            //Añadimos interrogantes por cada termino que deseemos consultar
            StringBuilder sbuilder = new StringBuilder();
            for (int i = 0; i < lstTerminos.size(); i++) {
                sbuilder.append(" ? ").append(" , ");
            }

            //Eliminamos los tres ultimos caracteres para eliminar la coma sobrane
            sb.append(sbuilder.substring(0, sbuilder.length() - 3));
            sb.append(" ) ");

            ps = (PreparedStatement) con.prepareStatement(sb.toString());

            //Anadimos los terminos
            for (int i = 0; i < lstTerminos.size(); i++) {
                ps.setInt(i + 1, lstTerminos.get(i));
            }

            //Ejecutamos la consulta
            rs = ps.executeQuery();

            while (rs.next()) {
                dto = new TerminoDto();

                dto.setIdTermino(rs.getInt("ID_TERMINO"));
                dto.setTermino(rs.getString("TERMINO"));
                dto.setUsaGlobalException(rs.getBoolean("USO_GLOBAL"));
                dto.setFemenino(rs.getString("FEMENINO"));
                dto.setPlural(rs.getString("PLURAL"));
                dto.setFemeninoPlural(rs.getString("FEMENINO_PLURAL"));

                listado.add(dto);

            }

        } catch (Exception e) {
            throw new BusinessException(e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                } // end if

                if (ps != null) {
                    ps.close();
                } // end if

                if (con != null) {
                    con.close();
                } // end if
            } catch (SQLException e) {
                throw new BusinessException(e);
            }

        }
        return listado;
    }

    /**
     * Creamos el dto de analisis.
     *
     * @param idRastreo - identificador del rastreo
     * @param url       - URL del texto a analizar
     * @param text      - Texto a analizar
     * @return AnalisisDto
     */
    protected AnalisisDto getAnalisisDto(Long idRastreo, String url, String text) {
        AnalisisDto dto = new AnalisisDto();
        dto.setIdRastreo(idRastreo);
        dto.setContenido(text);
        dto.setFecha(sdf.format(new Date()));
        dto.setUsuario("jUnit");
        dto.setUrl(url);
        return dto;
    }

    /**
     * Ejecutamos el test
     *
     * @param text
     * @param terminosSexistas
     * @throws es.inteco.crawler.sexista.core.exception.BusinessException
     *
     * @throws Exception
     */
    protected void executeAndValidate(String text, String... terminosSexistas)
            throws BusinessException, Exception {
        //Montamos los datos de entrada
        AnalisisDto analisisDto = this.getAnalisisDto(text);

        //Ejecutamos el metodo
        this.service.analyze(analisisDto, true);

        //Validamos el analisis
        this.validarAnalisis(terminosSexistas, analisisDto);
    }

    /**
     * Validamos el analisis
     *
     * @param terminosSexistas
     * @param analisisDto
     * @throws es.inteco.crawler.sexista.core.exception.BusinessException
     *
     * @throws Exception
     */
    private void validarAnalisis(String[] terminosSexistas,
                                 AnalisisDto analisisDto) throws BusinessException, Exception {

        //Recuperamos los datos
        List<ResultadoDto> result = this.find(analisisDto.getIdRastreo());

        //Verificamos
        Assert.assertNotNull(result);
        Assert.assertEquals(ERRROR_MSG_1, terminosSexistas.length, result.size());

        //Comprobamos los valores de los terminos sexistas que se deben  encontrar
        if (terminosSexistas.length != 0) {

            //Montamos el listado de id de terminos
            List<Integer> lstTerminos = new ArrayList<Integer>();
            for (ResultadoDto resultadoDto : result) {
                lstTerminos.add(resultadoDto.getIdTermino());
            }

            //Recuperamos los terminos
            List<TerminoDto> terminos = this.getTerminos(lstTerminos);
            Assert.assertNotNull(result);
            Assert.assertEquals(ERROR_MSG_2, terminos.size(), terminosSexistas.length);

            boolean control = false;
            for (String terminoSexista : terminosSexistas) {

                control = false;

                for (TerminoDto terminoDto : terminos) {
                    if (terminoSexista.equals(terminoDto.getTermino())) {
                        control = true;
                    }
                }

                Assert.assertTrue(this.mountErrorMsg(terminos, terminoSexista), control);
            }
        }
    }

    private String mountErrorMsg(List<TerminoDto> terminos, String terminoSexista) {

        StringBuilder sb = new StringBuilder();
        sb.append("No se ha encontrado el termino: ");
        sb.append(terminoSexista);

        sb.append("\n [").append(terminos.size()).append("] ");
        sb.append("terminos encontrados: ");
        for (TerminoDto terminoDto : terminos) {
            sb.append(terminoDto.getTermino()).append(" - ");
        }

        return (terminos.size() > 0) ? sb.substring(0, sb.length() - 3) : sb.toString();

    }
}
