package es.inteco.rastreador2.dao.language;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.utils.DAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class LanguageDAO {

    private LanguageDAO() {
    }

    public static List<LenguajeForm> loadLanguages(Connection c) {
        List<LenguajeForm> lenguajeFormList = new ArrayList<LenguajeForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT * FROM languages");
            rs = ps.executeQuery();

            while (rs.next()) {
                LenguajeForm lenguajeForm = new LenguajeForm();
                lenguajeForm.setId(rs.getLong("id_language"));
                lenguajeForm.setCodice(rs.getString("codice"));
                lenguajeForm.setKeyName(rs.getString("key_name"));
                lenguajeFormList.add(lenguajeForm);
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", LanguageDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                DAOUtils.closeQueries(ps, rs);
            } catch (SQLException e) {
                Logger.putLog("Exception: ", LanguageDAO.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }

        return lenguajeFormList;
    }

}
