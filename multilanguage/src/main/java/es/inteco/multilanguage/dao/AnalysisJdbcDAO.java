package es.inteco.multilanguage.dao;

import es.inteco.multilanguage.dao.utils.DAOUtils;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.persistence.Language;
import es.inteco.multilanguage.persistence.LanguageFound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisJdbcDAO {

    /**
     * Devolver un listado de análisis relacionados con un rastreo
     *
     * @param idCrawling
     * @param offset
     * @param numResults
     * @return
     */
    public static List<Analysis> getObservatoryAnalysis(Connection connection, List<Long> listCrawlings) throws Exception {
        List<Analysis> results = new ArrayList<>();

        Map<Long, List<LanguageFound>> languagesFoundMap = getLanguagesFoundMap(connection, listCrawlings);

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder listCrawlingsIdStr = new StringBuilder("SELECT * FROM analysis a WHERE a.id IN (");
            for (int i = 1; i <= listCrawlings.size(); i++) {
                if (listCrawlings.size() > i) {
                    listCrawlingsIdStr.append("?,");
                } else if (listCrawlings.size() == i) {
                    listCrawlingsIdStr.append("?)");
                }
            }

            ps = connection.prepareStatement(listCrawlingsIdStr.toString());

            for (int i = 0; i < listCrawlings.size(); i++) {
                ps.setLong(i + 1, listCrawlings.get(i));
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                Analysis analysis = new Analysis();

                analysis.setId(rs.getLong("id"));
                analysis.setDate(rs.getTimestamp("date"));

                analysis.setLanguagesFound(languagesFoundMap.get(analysis.getId()));

                results.add(analysis);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }

    private static Map<Long, List<LanguageFound>> getLanguagesFoundMap(Connection connection, List<Long> listCrawlings) throws Exception {
        Map<Long, List<LanguageFound>> results = new HashMap<>();

        Map<Long, Language> languages = getLanguagesMap(connection);

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder listCrawlingsIdStr = new StringBuilder("SELECT * FROM languages_found lf WHERE lf.id_analysis IN (");
            for (int i = 1; i <= listCrawlings.size(); i++) {
                if (listCrawlings.size() > i) {
                    listCrawlingsIdStr.append("?,");
                } else if (listCrawlings.size() == i) {
                    listCrawlingsIdStr.append("?)");
                }
            }

            ps = connection.prepareStatement(listCrawlingsIdStr.toString());

            for (int i = 0; i < listCrawlings.size(); i++) {
                ps.setLong(i + 1, listCrawlings.get(i));
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                LanguageFound languageFound = new LanguageFound();

                languageFound.setId(rs.getLong("id"));
                languageFound.setDeclarationLang(rs.getString("declaration_lang"));

                languageFound.setLanguage(languages.get(rs.getLong("id_language")));
                languageFound.setLanguageSuspected(languages.get(rs.getLong("id_sus_language")));

                Long idAnalysis = rs.getLong("id_analysis");
                if (results.get(idAnalysis) == null) {
                    results.put(idAnalysis, new ArrayList<LanguageFound>());
                }
                results.get(idAnalysis).add(languageFound);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }

    private static Map<Long, Language> getLanguagesMap(Connection connection) throws Exception {
        Map<Long, Language> results = new HashMap<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT * FROM languages WHERE to_analyze = true");

            rs = ps.executeQuery();

            while (rs.next()) {
                Language language = new Language();

                language.setId(rs.getLong("id"));
                language.setName(rs.getString("name"));
                language.setCode(rs.getString("code"));
                language.setText(rs.getString("text"));

                results.put(rs.getLong("id"), language);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return results;
    }
}
