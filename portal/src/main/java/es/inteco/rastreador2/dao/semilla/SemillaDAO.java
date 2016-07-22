package es.inteco.rastreador2.dao.semilla;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.semillas.SeedUtils;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.actionform.semillas.UpdateListDataForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.DAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class SemillaDAO {

    private SemillaDAO() {
    }

    public static boolean existSeed(Connection c, String nombreSemilla, int type) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (type == -1) {
                ps = c.prepareStatement("SELECT 1 FROM lista WHERE nombre = ?");
            } else {
                ps = c.prepareStatement("SELECT 1 FROM lista WHERE nombre = ? AND id_tipo_lista = ?");
                ps.setLong(2, type);
            }
            ps.setString(1, nombreSemilla);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return false;
    }

    public static Long insertList(Connection c, long tipoLista, String nombreSemilla, String listaUrls, String categoria, String acronimo, String dependencia) throws Exception {
        return insertList(c, tipoLista, nombreSemilla, listaUrls, categoria, acronimo, dependencia, true, false);
    }

    public static Long insertList(Connection c, long tipoLista, String nombreSemilla, String listaUrls, String categoria, String acronimo, String dependencia, boolean activa, boolean inDirectory) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, acronimo, dependencia, activa, in_directory) VALUES (?,?,?,?,?,?,?,?)");
            ps.setLong(1, tipoLista);
            ps.setString(2, nombreSemilla);
            ps.setString(3, listaUrls);
            if (StringUtils.isNotEmpty(categoria)) {
                ps.setString(4, categoria);
            } else {
                ps.setString(4, null);
            }
            if (StringUtils.isNotEmpty(acronimo)) {
                ps.setString(5, acronimo);
            } else {
                ps.setString(5, null);
            }
            if (StringUtils.isNotEmpty(dependencia)) {
                ps.setString(6, dependencia);
            } else {
                ps.setString(6, null);
            }
            ps.setBoolean(7, activa);
            ps.setBoolean(8, inDirectory);
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, null);

            ps = c.prepareStatement("SELECT id_lista FROM lista WHERE nombre like ? ORDER BY id_lista DESC LIMIT 1");
            ps.setString(1, nombreSemilla);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("id_lista");
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return null;
    }

    public static List<SemillaForm> getSeeds(Connection c, int type) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SemillaForm> seedList = new ArrayList<SemillaForm>();

        try {
            ps = c.prepareStatement("SELECT * FROM lista l WHERE id_tipo_lista = ? ;");
            ps.setLong(1, type);
            rs = ps.executeQuery();

            while (rs.next()) {
                SemillaForm semillaForm = new SemillaForm();
                semillaForm.setId(rs.getLong("l.id_lista"));
                semillaForm.setNombre(rs.getString("nombre"));
                semillaForm.setListaUrls(convertStringToList(rs.getString("lista")));
                if (type == Constants.ID_LISTA_SEMILLA_OBSERVATORIO) {

                    CategoriaForm categoriaForm = new CategoriaForm();
                    categoriaForm.setId(rs.getString("id_categoria"));
                    categoriaForm.setName(rs.getString("nombreCat"));
                    categoriaForm.setOrden(rs.getInt("orden"));
                    semillaForm.setCategoria(categoriaForm);
                    if (rs.getLong("id_lista") == 0) {
                        semillaForm.setAsociada(false);
                    } else {
                        semillaForm.setAsociada(true);
                    }
                }
                seedList.add(semillaForm);
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return seedList;
    }

    public static List<SemillaForm> getObservatorySeeds(Connection c, int pagina, SemillaSearchForm searchForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SemillaForm> seedList = new ArrayList<SemillaForm>();

        try {
            PropertiesManager pmgr = new PropertiesManager();
            int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
            int resultFrom = pagSize * pagina;
            int count = 1;

            String query = "SELECT * FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) WHERE id_tipo_lista = ? ";

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                query += " AND l.nombre like ? ";
            }

            if (StringUtils.isNotEmpty(searchForm.getCategoria())) {
                query += " AND l.id_categoria = ? ";
            }

            if (StringUtils.isNotEmpty(searchForm.getUrl())) {
                query += " AND l.lista like ? ";
            }

            query += " LIMIT ? OFFSET ?";
            ps = c.prepareStatement(query);

            ps.setLong(count++, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                ps.setString(count++, "%" + searchForm.getNombre() + "%");
            }

            if (StringUtils.isNotEmpty(searchForm.getCategoria())) {
                ps.setString(count++, searchForm.getCategoria());
            }

            if (StringUtils.isNotEmpty(searchForm.getUrl())) {
                ps.setString(count++, "%" + searchForm.getUrl() + "%");
            }

            ps.setLong(count++, pagSize);
            ps.setLong(count, resultFrom);
            rs = ps.executeQuery();

            while (rs.next()) {
                SemillaForm semillaForm = new SemillaForm();
                semillaForm.setId(rs.getLong("l.id_lista"));
                semillaForm.setNombre(rs.getString("l.nombre"));
                semillaForm.setListaUrls(convertStringToList(rs.getString("lista")));

                CategoriaForm categoriaForm = new CategoriaForm();
                categoriaForm.setId(rs.getString("id_categoria"));
                categoriaForm.setName(rs.getString("cl.nombre"));
                categoriaForm.setOrden(rs.getInt("cl.orden"));
                semillaForm.setCategoria(categoriaForm);
                if (rs.getLong("l.activa") == 0) {
                    semillaForm.setActiva(false);
                } else {
                    semillaForm.setActiva(true);
                }

                seedList.add(semillaForm);
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return seedList;
    }

    public static int countObservatorySeeds(Connection c, SemillaSearchForm searchForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int count = 1;
            String query = "SELECT COUNT(*) FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) WHERE id_tipo_lista = ? ";

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                query += " AND l.nombre like ? ";
            }

            if (StringUtils.isNotEmpty(searchForm.getCategoria())) {
                query += " AND l.id_categoria = ? ";
            }

            if (StringUtils.isNotEmpty(searchForm.getUrl())) {
                query += " AND l.lista like ? ";
            }

            ps = c.prepareStatement(query);
            ps.setLong(count++, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                ps.setString(count++, "%" + searchForm.getNombre() + "%");
            }

            if (StringUtils.isNotEmpty(searchForm.getCategoria())) {
                ps.setString(count++, searchForm.getCategoria());
            }

            if (StringUtils.isNotEmpty(searchForm.getUrl())) {
                ps.setString(count, "%" + searchForm.getUrl() + "%");
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                return (rs.getInt(1));
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return 0;
    }

    public static List<SemillaForm> getSeedsChoose(Connection c, int pagina, SemillaSearchForm searchForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SemillaForm> seedList = new ArrayList<SemillaForm>();

        try {
            PropertiesManager pmgr = new PropertiesManager();
            int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
            int resultFrom = pagSize * pagina;
            int count = 1;
            String query = "SELECT l.*, COUNT(r.id_rastreo) AS rastreos_asociados FROM lista l " +
                    "LEFT JOIN rastreo r ON (l.id_lista = r.semillas) " +
                    "WHERE id_tipo_lista = ? AND id_lista NOT IN (" +
                    "SELECT DISTINCT(dominio) FROM cuenta_cliente) ";

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                query += " AND nombre like ? ";
            }

            query += " GROUP BY id_lista LIMIT ? OFFSET ?";
            ps = c.prepareStatement(query);

            ps.setLong(count++, Constants.ID_LISTA_SEMILLA);

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                ps.setString(count++, "%" + searchForm.getNombre() + "%");
            }

            ps.setLong(count++, pagSize);
            ps.setLong(count, resultFrom);
            rs = ps.executeQuery();

            while (rs.next()) {
                SemillaForm semillaForm = new SemillaForm();
                semillaForm.setId(rs.getLong("id_lista"));
                semillaForm.setNombre(rs.getString("nombre"));
                semillaForm.setListaUrls(convertStringToList(rs.getString("lista")));
                if (rs.getInt("rastreos_asociados") > 0) {
                    semillaForm.setAsociada(true);
                } else {
                    semillaForm.setAsociada(false);
                }
                seedList.add(semillaForm);
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return seedList;
    }

    public static int countSeedsChoose(Connection c, int type, SemillaSearchForm searchForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                ps = c.prepareStatement("SELECT COUNT(*) FROM lista WHERE id_tipo_lista = ? AND id_lista NOT IN (" +
                        "SELECT DISTINCT(dominio) FROM cuenta_cliente)  AND nombre like ?");
                ps.setString(2, "%" + searchForm.getNombre() + "%");
            } else {
                ps = c.prepareStatement("SELECT COUNT(*) FROM lista WHERE id_tipo_lista = ? AND id_lista NOT IN (" +
                        "SELECT DISTINCT(dominio) FROM cuenta_cliente)");
            }
            ps.setLong(1, type);

            rs = ps.executeQuery();

            if (rs.next()) {
                return (rs.getInt(1));
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return 0;
    }

    private static List<String> convertStringToList(String lista) {
        List<String> urlsList = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(lista, ";");
        while (tokenizer.hasMoreTokens()) {
            urlsList.add(tokenizer.nextToken());
        }
        return urlsList;
    }

    public static void deleteSeed(Connection c, long idSemilla) throws Exception {
        PreparedStatement ps = null;

        try {
            ps = c.prepareStatement("DELETE FROM lista WHERE id_lista = ?");
            ps.setLong(1, idSemilla);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static void deleteObservatorySeed(Connection c, long idSemilla, List<ObservatorioForm> observatoryFormList) throws Exception {
        PreparedStatement ps = null;

        try {
            c.setAutoCommit(false);

            for (ObservatorioForm observatorioForm : observatoryFormList) {
                deleteObservatorySeed(c, idSemilla, observatorioForm.getId());
            }

            ps = c.prepareStatement("DELETE FROM lista WHERE id_lista = ?");
            ps.setLong(1, idSemilla);
            ps.executeUpdate();

            c.commit();
        } catch (Exception e) {
            try {
                c.rollback();
            } catch (Exception excep) {
                Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static SemillaForm getSeedById(Connection c, long idSemilla) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SemillaForm semillaForm = new SemillaForm();

        try {
            /*if (type == -1) {
                ps = c.prepareStatement("SELECT * FROM lista l LEFT OUTER JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) " +
                        "WHERE l.id_lista = ? ");
            } else {
                ps = c.prepareStatement("SELECT * FROM lista l LEFT OUTER JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) " +
                        "WHERE l.id_lista = ? ");
            }*/
            ps = c.prepareStatement("SELECT * FROM lista l LEFT OUTER JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) WHERE l.id_lista = ?");
            ps.setLong(1, idSemilla);
            rs = ps.executeQuery();
            if (rs.next()) {
                semillaForm.setId(rs.getLong("id_lista"));
                semillaForm.setListaUrls(convertStringToList(rs.getString("lista")));
                semillaForm.setListaUrlsString(rs.getString("lista"));
                semillaForm.setNombre(rs.getString("nombre"));
                CategoriaForm categoriaForm = new CategoriaForm();
                categoriaForm.setId(rs.getString("id_categoria"));
                categoriaForm.setName(rs.getString("cl.nombre"));
                categoriaForm.setOrden(rs.getInt("cl.orden"));
                semillaForm.setCategoria(categoriaForm);
                semillaForm.setDependencia(rs.getString("dependencia"));
                semillaForm.setAcronimo(rs.getString("acronimo"));
                semillaForm.setActiva(rs.getBoolean("activa"));
                semillaForm.setInDirectory(rs.getBoolean("in_directory"));
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return semillaForm;
    }

    public static long getIdList(Connection c, String nombreLista, Long id_categoria) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query = "SELECT id_lista FROM lista WHERE nombre = ?";

            if (id_categoria != null) {
                query = query + " AND id_categoria = ?";
            }
            query += " ORDER BY id_lista DESC LIMIT 1";
            ps = c.prepareStatement(query);
            ps.setString(1, nombreLista);
            if (id_categoria != null) {
                ps.setLong(2, id_categoria);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("id_lista");
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return 0;
    }

    public static void editSeed(Connection c, SemillaForm semillaForm) throws Exception {
        PreparedStatement ps = null;

        try {
            ps = c.prepareStatement("UPDATE lista SET lista = ?, nombre = ?, id_categoria = ?, acronimo = ?, dependencia = ?, activa = ?, in_directory = ? WHERE id_lista = ? ");
            ps.setString(1, semillaForm.getListaUrlsString());
            ps.setString(2, semillaForm.getNombre());
            if (semillaForm.getCategoria().getId() != null && !StringUtils.isEmpty(semillaForm.getCategoria().getId())) {
                ps.setString(3, semillaForm.getCategoria().getId());
            } else {
                ps.setString(3, null);
            }
            if (semillaForm.getAcronimo() != null && !StringUtils.isEmpty(semillaForm.getAcronimo())) {
                ps.setString(4, semillaForm.getAcronimo());
            } else {
                ps.setString(4, null);
            }
            if (semillaForm.getDependencia() != null && !StringUtils.isEmpty(semillaForm.getDependencia())) {
                ps.setString(5, semillaForm.getDependencia());
            } else {
                ps.setString(5, null);
            }
            ps.setBoolean(6, semillaForm.isActiva());
            ps.setBoolean(7, semillaForm.isInDirectory());
            ps.setLong(8, semillaForm.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static void removeListById(Connection c, long idList) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("DELETE FROM lista WHERE id_lista = ?");
            ps.setLong(1, idList);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static void editList(Connection c, long id_lista, String lista, String nameList) throws Exception {
        PreparedStatement ps = null;
        try {
            if (nameList.isEmpty()) {
                ps = c.prepareStatement("UPDATE lista SET lista = ? WHERE id_lista = ? ");
                ps.setLong(2, id_lista);
            } else {
                ps = c.prepareStatement("UPDATE lista SET lista = ?, nombre = ? WHERE id_lista = ? ");
                ps.setString(2, nameList);
                ps.setLong(3, id_lista);
            }

            ps.setString(1, lista);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static void removeLists(Connection c, UpdateListDataForm updateListDataForm) throws Exception {
        if (updateListDataForm.getListaRastreable() == null || updateListDataForm.getListaRastreable().isEmpty()) {
            if (updateListDataForm.getIdRastreableAntiguo() != 0) {
                //Si hemos eliminado la lista rastreable, se borra de bbdd
                SemillaDAO.removeListById(c, updateListDataForm.getIdRastreableAntiguo());
            }
        }

        if (updateListDataForm.getListaNoRastreable() == null || updateListDataForm.getListaNoRastreable().isEmpty()) {
            if (updateListDataForm.getIdNoRastreableAntiguo() != 0) {
                //Si hemos eliminado la lista no rastreable, se borra de bbdd
                SemillaDAO.removeListById(c, updateListDataForm.getIdNoRastreableAntiguo());
            }
        }
    }

    public static UpdateListDataForm updateLists(final Connection c, final UpdateListDataForm updateListDataForm) throws Exception {
        // Si no habia lista rastreable y ahora se incluye, se crea
        if (updateListDataForm.getListaRastreable() != null && !updateListDataForm.getListaRastreable().isEmpty()) {
            if (updateListDataForm.getIdListaRastreable() == 0) {
                //Guardamos la lista Rastreable
                insertList(c, Constants.ID_LISTA_RASTREABLE, updateListDataForm.getNombre() + "-Rastreable", updateListDataForm.getListaRastreable(), null, null, null);
                final long idCrawlableList = SemillaDAO.getIdList(c, updateListDataForm.getNombre() + "-Rastreable", null);
                updateListDataForm.setIdListaRastreable(idCrawlableList);
            } else {
                editList(c, updateListDataForm.getIdListaRastreable(), updateListDataForm.getListaRastreable(), updateListDataForm.getNombre() + "-Rastreable");
            }
        } else {
            if (updateListDataForm.getIdListaRastreable() != 0) {
                updateListDataForm.setIdRastreableAntiguo(updateListDataForm.getIdListaRastreable());
                updateListDataForm.setIdListaRastreable(0);
            }
        }

        // Si no habia lista no rastreable y ahora se incluye, se crea
        if (updateListDataForm.getListaNoRastreable() != null && !updateListDataForm.getListaNoRastreable().isEmpty()) {
            if (updateListDataForm.getIdListaNoRastreable() == 0) {
                //Guardamos la lista no Rastreable
                insertList(c, Constants.ID_LISTA_NO_RASTREABLE, updateListDataForm.getNombre() + "-NoRastreable", updateListDataForm.getListaNoRastreable(), null, null, null);
                Long idNoCrawlableList = SemillaDAO.getIdList(c, updateListDataForm.getNombre() + "-NoRastreable", null);
                updateListDataForm.setIdListaNoRastreable(idNoCrawlableList);
            } else {
                editList(c, updateListDataForm.getIdListaNoRastreable(), updateListDataForm.getListaNoRastreable(), updateListDataForm.getNombre() + "-NoRastreable");
            }
        } else {
            if (updateListDataForm.getIdListaNoRastreable() != 0) {
                updateListDataForm.setIdNoRastreableAntiguo(updateListDataForm.getIdListaNoRastreable());
                updateListDataForm.setIdListaNoRastreable((long) 0);
            }
        }

        return updateListDataForm;
    }

    public static void deleteObservatorySeed(Connection c, long id_seed, long id_observatory) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        final List<Connection> connections = DAOUtils.getCartridgeConnections();

        try {
            c.setAutoCommit(false);
            for (Connection conn : connections) {
                conn.setAutoCommit(false);
            }

            //Se recupera el id del rastreo asociado a la semilla
            ps = c.prepareStatement("SELECT id_rastreo FROM rastreo r WHERE id_observatorio = ? AND semillas = ? ");
            ps.setLong(2, id_seed);
            ps.setLong(1, id_observatory);
            rs = ps.executeQuery();
            if (rs.next()) {
                RastreoDAO.borrarRastreo(c, connections, rs.getLong("id_rastreo"));
            }

            c.commit();
            for (Connection conn : connections) {
                conn.commit();
            }
        } catch (Exception e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            for (Connection conn : connections) {
                try {
                    conn.rollback();
                } catch (Exception excep) {
                    Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                    throw e;
                }
            }
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
            for (Connection conn : connections) {
                DataBaseManager.closeConnection(conn);
            }
        }
    }

    public static List<CategoriaForm> getSeedCategories(Connection c, int page) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CategoriaForm> categories = new ArrayList<CategoriaForm>();

        try {
            if (page == Constants.NO_PAGINACION) {
                ps = c.prepareStatement("SELECT * FROM categorias_lista");
            } else {
                PropertiesManager pmgr = new PropertiesManager();
                int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
                int resultFrom = pagSize * page;
                ps = c.prepareStatement("SELECT * FROM categorias_lista LIMIT ? OFFSET ?");
                ps.setInt(1, pagSize);
                ps.setInt(2, resultFrom);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                CategoriaForm categoriaForm = new CategoriaForm();
                categoriaForm.setId(rs.getString("id_categoria"));
                categoriaForm.setName(rs.getString("nombre"));
                categoriaForm.setOrden(rs.getInt("orden"));
                categories.add(categoriaForm);
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return categories;
    }

    public static CategoriaForm getSeedCategory(Connection c, long idCategory) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT * FROM categorias_lista WHERE id_categoria = ?");
            ps.setLong(1, idCategory);
            rs = ps.executeQuery();

            if (rs.next()) {
                CategoriaForm categoriaForm = new CategoriaForm();
                categoriaForm.setId(rs.getString("id_categoria"));
                categoriaForm.setName(rs.getString("nombre"));
                categoriaForm.setOrden(rs.getInt("orden"));
                return categoriaForm;
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return null;
    }

    public static List<SemillaForm> getSeedsByCategory(Connection c, long idCategory, int page, SemillaForm searchForm) throws Exception {
        List<SemillaForm> results = new ArrayList<SemillaForm>();
        String query = "SELECT * FROM lista l WHERE id_categoria = ? ";

        if (StringUtils.isNotEmpty(searchForm.getNombre())) {
            query += " AND l.nombre like ? ";
        }

        query += "ORDER BY l.nombre ASC ";

        if (page != Constants.NO_PAGINACION) {
            query += "LIMIT ? OFFSET ?";
        }

        PreparedStatement ps = c.prepareStatement(query);
        ResultSet rs = null;

        try {
            int count = 1;
            ps.setLong(count++, idCategory);

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                ps.setString(count++, "%" + searchForm.getNombre() + "%");
            }

            if (page != Constants.NO_PAGINACION) {
                PropertiesManager pmgr = new PropertiesManager();
                int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
                int resultFrom = pagSize * page;
                ps.setInt(count++, pagSize);
                ps.setInt(count, resultFrom);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                SemillaForm semillaForm = new SemillaForm();
                semillaForm.setId(rs.getLong("id_lista"));
                semillaForm.setNombre(rs.getString("nombre"));
                semillaForm.setListaUrlsString(rs.getString("lista"));
                semillaForm.setListaUrls(convertStringToList(rs.getString("lista")));
                semillaForm.setAcronimo(rs.getString("acronimo"));
                semillaForm.setDependencia(rs.getString("dependencia"));
                semillaForm.setActiva(rs.getBoolean("activa"));
                semillaForm.setInDirectory(rs.getBoolean("in_directory"));
                results.add(semillaForm);
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return results;
    }

    public static Integer countSeedsByCategory(Connection c, long idCategory, SemillaForm searchForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int count = 1;
            String query = "SELECT COUNT(*) AS numSeeds FROM lista WHERE id_categoria = ? ";

            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                query += " AND nombre like ? ";
            }

            ps = c.prepareStatement(query);

            ps.setLong(count++, idCategory);
            if (StringUtils.isNotEmpty(searchForm.getNombre())) {
                ps.setString(count, "%" + searchForm.getNombre() + "%");
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("numSeeds");
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return 0;
    }

    public static Integer countSeedCategories(Connection c) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) as numCategories FROM categorias_lista");
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("numCategories");
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return 0;
    }

    public static void deleteSeedCategory(Connection c, Long idCategory) throws Exception {
        PreparedStatement ps = null;
        final List<Connection> connections = DAOUtils.getCartridgeConnections();
        try {
            c.setAutoCommit(false);
            for (Connection conn : connections) {
                conn.setAutoCommit(false);
            }

            List<Long> observatoryIds = getCategoryObservatoryIds(c, idCategory);
            for (Long observatoryId : observatoryIds) {
                //RECUPERAMOS LOS IDS DE LOS RASTREOS DEL OBSERVATORIO
                List<Long> crawlIdsList = RastreoDAO.getCrawlerCategoryIds(c, observatoryId, idCategory);

                for (Long crawlId : crawlIdsList) {
                    RastreoDAO.borrarRastreo(c, connections, crawlId);
                }
            }

            ps = c.prepareStatement("DELETE FROM categorias_lista WHERE id_categoria = ?");
            ps.setLong(1, idCategory);
            ps.executeUpdate();

            c.commit();
            for (Connection conn : connections) {
                conn.commit();
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
            try {
                c.rollback();
            } catch (Exception excep) {
                Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            for (Connection conn : connections) {
                try {
                    conn.rollback();
                } catch (Exception excep) {
                    Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
                    throw e;
                }
            }
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
            for (Connection conn : connections) {
                DataBaseManager.closeConnection(conn);
            }
        }
    }

    public static List<Long> getCategoryObservatoryIds(Connection c, Long idCategory) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT id_observatorio FROM observatorio_categoria WHERE id_categoria = ?");
            ps.setLong(1, idCategory);
            rs = ps.executeQuery();

            List<Long> observatoriesIdsList = new ArrayList<Long>();
            while (rs.next()) {
                observatoriesIdsList.add(rs.getLong(1));
            }

            return observatoriesIdsList;
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static void deleteCategorySeed(Connection c, String idSemilla) throws Exception {
        try {
            final List<ObservatorioForm> observatoryFormList = ObservatorioDAO.getObservatoriesFromSeed(c, idSemilla);
            deleteObservatorySeed(c, Long.parseLong(idSemilla), observatoryFormList);
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static Long createSeedCategory(Connection c, CategoriaForm categoriaForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("INSERT INTO categorias_lista (nombre, orden) VALUES (?,?)");
            ps.setString(1, categoriaForm.getName());
            ps.setInt(2, categoriaForm.getOrden());
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, null);

            ps = c.prepareStatement("SELECT id_categoria FROM categorias_lista WHERE nombre LIKE ? ORDER BY id_categoria DESC");
            ps.setString(1, categoriaForm.getName());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("id_categoria");
            }
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return (long) 0;
    }

    public static void updateSeedCategory(Connection c, CategoriaForm categoriaForm) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("UPDATE categorias_lista SET nombre = ?, orden=? WHERE id_categoria = ?");
            ps.setString(1, categoriaForm.getName());
            ps.setInt(2, categoriaForm.getOrden());
            ps.setString(3, categoriaForm.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static void saveSeedsCategory(Connection c, List<SemillaForm> semillas, String idCategory) throws Exception {
        PreparedStatement ps = null;
        try {
            c.setAutoCommit(false);

            ps = c.prepareStatement("INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, acronimo, dependencia, activa) VALUES (?,?,?,?,?,?,?)");
            for (SemillaForm semillaForm : semillas) {
                ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
                ps.setString(2, semillaForm.getNombre());
                ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
                ps.setString(4, idCategory);
                if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
                    ps.setString(5, semillaForm.getAcronimo());
                } else {
                    ps.setString(5, null);
                }
                if (StringUtils.isNotEmpty(semillaForm.getDependencia())) {
                    ps.setString(6, semillaForm.getDependencia());
                } else {
                    ps.setString(6, null);
                }
                if (StringUtils.isNotEmpty(semillaForm.getActivaStr()) && semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
                    ps.setBoolean(7, false);
                } else {
                    ps.setBoolean(7, true);
                }
                ps.addBatch();
            }

            ps.executeBatch();

            c.commit();
        } catch (Exception e) {
            c.rollback();
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static void updateCategorySeeds(Connection c, List<SemillaForm> semillas) throws Exception {
        PreparedStatement ps = null;
        try {
            if (!semillas.isEmpty()) {
                c.setAutoCommit(false);
                ps = c.prepareStatement("UPDATE lista SET nombre = ? WHERE id_lista = ?");
                for (SemillaForm semillaForm : semillas) {
                    ps.setString(1, semillaForm.getNombre());
                    ps.setLong(2, semillaForm.getId());
                    ps.addBatch();
                }

                ps.executeBatch();

                c.commit();
            }
        } catch (Exception e) {
            c.rollback();
            Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

}