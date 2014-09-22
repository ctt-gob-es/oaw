package es.inteco.rastreador2.dao.categorias;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.categoria.NuevaCategoriaForm;
import es.inteco.rastreador2.actionform.categoria.NuevoTerminoCatForm;
import es.inteco.rastreador2.actionform.categoria.VerCategoriaForm;
import es.inteco.rastreador2.utils.Categoria;
import es.inteco.rastreador2.utils.DAOUtils;
import es.inteco.rastreador2.utils.NormalizarCats;
import es.inteco.rastreador2.utils.TerminoCatVer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;


public final class CategoriasDAO {

    private CategoriasDAO() {
    }

    public static int countCategories(Connection c) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM categoria ");
            rs = ps.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static int countTerms(Connection c, int id_category) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM categoria_termino WHERE id_categoria = ? ");
            ps.setInt(1, id_category);
            rs = ps.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<Categoria> loadCategories(Connection c, int page) throws Exception {
        List<Categoria> cats = new ArrayList<Categoria>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * page;
        try {
            ps = c.prepareStatement("SELECT DISTINCT(c.id_categoria), c.categoria , c.umbral, count(t.id_termino) as numTerminos " +
                    "FROM categoria c LEFT JOIN categoria_termino ct ON (c.id_categoria = ct.id_categoria) " +
                    "LEFT JOIN termino t ON (ct.id_termino = t.id_termino) " +
                    "GROUP BY c.id_categoria, c.categoria, c.umbral ORDER BY c.categoria LIMIT ? OFFSET ? ;");
            ps.setInt(1, pagSize);
            ps.setInt(2, resultFrom);
            rs = ps.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setNombre(rs.getString("categoria"));
                categoria.setUmbral(rs.getFloat("umbral"));
                categoria.setId_categoria(rs.getInt("id_categoria"));
                categoria.setTerminos(rs.getInt("numTerminos"));
                cats.add(categoria);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return cats;
    }

    public static void deleteCategory(Connection c, String id_category) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("DELETE FROM categoria WHERE id_categoria = ?");
            ps.setInt(1, Integer.parseInt(id_category));
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);

        }
    }

    public static VerCategoriaForm showCategories(Connection c, VerCategoriaForm verCategoriaForm, String id_category) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            List<TerminoCatVer> termsVector = new ArrayList<TerminoCatVer>();
            ps = c.prepareStatement("SELECT * FROM categoria c " +
                    "INNER JOIN categoria_termino ct ON (c.id_categoria = ct.id_categoria)" +
                    "INNER JOIN termino t ON (ct.id_termino = t.id_termino)" +
                    "WHERE c.id_categoria = ?;");
            ps.setInt(1, Integer.parseInt(id_category));
            rs = ps.executeQuery();
            DAOUtils.closeQueries(ps, rs);
            boolean areThereResults = false;
            while (rs.next()) {
                areThereResults = true;
                verCategoriaForm.setUmbral(String.valueOf(rs.getFloat("umbral")));
                verCategoriaForm.setCategoria(rs.getString("categoria"));
                verCategoriaForm.setId_categoria(Integer.parseInt(id_category));

                TerminoCatVer t = new TerminoCatVer();
                t.setPorcentaje(String.valueOf(rs.getFloat("porcentaje")));
                t.setPorcentajeNorm(String.valueOf(rs.getFloat("porcentaje_normalizado")));
                t.setTermino(rs.getString("termino"));

                termsVector.add(t);
            }
            if (!areThereResults) {
                ps = c.prepareStatement("SELECT * FROM categoria WHERE id_categoria= ?;");
                ps.setInt(1, Integer.parseInt(id_category));
                rs = ps.executeQuery();
                if (rs.next()) {
                    verCategoriaForm.setUmbral(String.valueOf(rs.getFloat("umbral")));
                    verCategoriaForm.setCategoria(rs.getString("categoria"));
                    verCategoriaForm.setId_categoria(Integer.parseInt(id_category));
                }
            }

            verCategoriaForm.setVectorTerminos(termsVector);
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return verCategoriaForm;
    }

    public static NuevoTerminoCatForm getTermDates(Connection c, NuevoTerminoCatForm nuevoTerminoCatForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet re = null;
        try {
            ps = c.prepareStatement("SELECT * FROM termino t INNER JOIN categoria_termino ct ON t.id_termino = ct.id_termino WHERE ct.id_categoria = ? AND ct.id_termino = ?;");
            ps.setInt(1, nuevoTerminoCatForm.getId_categoria());
            ps.setInt(2, nuevoTerminoCatForm.getId_termino());
            re = ps.executeQuery();
            if (re.next()) {
                nuevoTerminoCatForm.setTermino(re.getString("termino"));
                nuevoTerminoCatForm.setPorcentaje(String.valueOf(re.getFloat("porcentaje")));
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, re);
        }
        return nuevoTerminoCatForm;
    }

    public static void deleteTerm(Connection c, NuevoTerminoCatForm nuevoTerminoCatForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet re = null;
        try {
            ps = c.prepareStatement("DELETE FROM termino WHERE id_termino = ?");
            ps.setInt(1, nuevoTerminoCatForm.getId_termino());
            ps.executeUpdate();
            NormalizarCats.normaliza(nuevoTerminoCatForm.getId_termino());
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, re);
        }
    }

    public static List<TerminoCatVer> loadCategoryTerms(Connection c, int id_category, int page) throws Exception {
        PreparedStatement ps = null;
        ResultSet rss = null;
        List<TerminoCatVer> termsVector = new ArrayList<TerminoCatVer>();
        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * page;
        try {
            ps = c.prepareStatement("SELECT * FROM categoria_termino WHERE id_categoria = ? LIMIT ? OFFSET ?;");
            ps.setInt(1, id_category);
            ps.setInt(2, pagSize);
            ps.setInt(3, resultFrom);
            rss = ps.executeQuery();
            while (rss.next()) {
                TerminoCatVer t = new TerminoCatVer();
                t.setId_termino(String.valueOf(rss.getInt("id_termino")));
                t.setPorcentaje(String.valueOf(rss.getFloat("porcentaje")));
                t.setPorcentajeNorm(String.valueOf(rss.getFloat("porcentaje_normalizado")));
                int id_termino = rss.getInt("id_termino");
                PreparedStatement ps2 = c.prepareStatement("SELECT * FROM termino WHERE id_termino = ?");
                ps2.setInt(1, id_termino);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    t.setTermino(rs2.getString("termino"));
                }
                termsVector.add(t);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rss);
        }
        return termsVector;
    }

    public static VerCategoriaForm getCategory(Connection c, VerCategoriaForm verCategoriaForm) throws Exception {

        PreparedStatement ps = null;
        ResultSet rss = null;
        try {
            ps = c.prepareStatement("SELECT * FROM categoria WHERE id_categoria = ?");
            ps.setInt(1, verCategoriaForm.getId_categoria());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                verCategoriaForm.setUmbral(String.valueOf(rs.getFloat("umbral")));
                verCategoriaForm.setCategoria(rs.getString("categoria"));
                verCategoriaForm.setNombre_antiguo(rs.getString("categoria"));
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rss);
        }
        return verCategoriaForm;
    }

    public static void updateCategory(Connection c, VerCategoriaForm verCategoriaForm) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("UPDATE categoria SET categoria = ?, umbral = ? WHERE categoria = ?");
            ps.setString(1, verCategoriaForm.getCategoria());
            ps.setFloat(2, Float.parseFloat(verCategoriaForm.getUmbral()));
            ps.setString(3, verCategoriaForm.getNombre_antiguo());
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static void updatePercentage(Connection c, String percentage, String id_category, String id_term) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("UPDATE categoria_termino SET porcentaje = ? WHERE id_categoria = ? AND id_termino = ?");
            ps.setFloat(1, Float.parseFloat(percentage));
            ps.setInt(2, Integer.parseInt(id_category));
            ps.setInt(3, Integer.parseInt(id_term));
            ps.executeUpdate();
            NormalizarCats.normaliza(Integer.parseInt(id_category));
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static boolean existCategory(Connection c, String category_name) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM categoria WHERE categoria = ?");
            ps.setString(1, category_name);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return false;
    }

    public static int insertrCategory(Connection c, NuevaCategoriaForm nuevaCategoriaForm, String[] arrayTerms, float[] arrayWeights) throws Exception {

        PreparedStatement ps = null;
        ResultSet rsd = null;
        int id_c = -1;
        try {
            ps = c.prepareStatement("INSERT INTO categoria(categoria, umbral) VALUES (?, ?)");
            ps.setString(1, nuevaCategoriaForm.getNombre());
            ps.setFloat(2, Float.parseFloat(nuevaCategoriaForm.getUmbral()));
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rsd);

            ps = c.prepareStatement("SELECT id_categoria FROM categoria WHERE categoria = ?");
            ps.setString(1, nuevaCategoriaForm.getNombre());
            rsd = ps.executeQuery();
            if (rsd.next()) {
                id_c = rsd.getInt("id_categoria");
                insertTerm(c, id_c, arrayTerms, arrayWeights);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rsd);
        }
        return id_c;
    }

    public static int existTerm(Connection c, String term) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM termino WHERE termino = ?");
            ps.setString(1, term);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_termino");
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return -1;
    }

    public static void insertTerm(Connection c, int id_category, String[] arrayTerms, float[] arrayWeights) throws Exception {

        for (int i = 0; i < arrayTerms.length; i++) {
            int id_term = -1;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                if (CategoriasDAO.existTerm(c, arrayTerms[i]) != -1) {
                    ps = c.prepareStatement("INSERT INTO categoria_termino(id_categoria, id_termino, porcentaje, porcentaje_normalizado) VALUES (?, ?, ?, 0)");
                    ps.setInt(1, id_category);
                    ps.setInt(2, id_term);
                    ps.setFloat(3, arrayWeights[i]);
                    ps.executeUpdate();
                } else {
                    ps = c.prepareStatement("INSERT INTO termino(termino) VALUES (?)");
                    ps.setString(1, arrayTerms[i]);
                    ps.executeUpdate();
                    int id_t = -1;
                    ps = c.prepareStatement("SELECT * FROM termino WHERE termino = ?");
                    ps.setString(1, arrayTerms[i]);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        id_t = rs.getInt(1);
                    }
                    ps = c.prepareStatement("INSERT INTO categoria_termino(id_categoria, id_termino, porcentaje, porcentaje_normalizado) VALUES (?, ?, ?, 0)");
                    ps.setInt(1, id_category);
                    ps.setInt(2, id_t);
                    ps.setFloat(3, arrayWeights[i]);
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            } finally {
                DAOUtils.closeQueries(ps, rs);
            }
        }
    }

    public static List<Integer> termCategories(Connection c, int id_term) throws Exception {
        List<Integer> ids_c = new ArrayList<Integer>();
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("SELECT * FROM categoria_termino WHERE id_termino = ?");
            ps.setInt(1, id_term);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                ids_c.add(rs2.getInt("id_categoria"));
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CategoriasDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
        return ids_c;
    }

}
