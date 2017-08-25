package es.inteco.rastreador2.dao.dependencia;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;

public final class DependenciaDAO {

	private DependenciaDAO() {
	}

	public static int countDependencias(Connection c, String nombre) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) FROM dependencia d WHERE 1=1 ";

		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(d.nombre) like UPPER(?) ";
		}

		try (PreparedStatement ps = c.prepareStatement(query)) {

			if (StringUtils.isNotEmpty(nombre)) {
				ps.setString(count++, "%" + nombre + "%");
			}

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	public static List<DependenciaForm> getDependencias(Connection c, String nombre, int page) throws SQLException {
		final List<DependenciaForm> results = new ArrayList<>();
		String query = "SELECT d.id_dependencia, d.nombre FROM dependencia d WHERE 1=1 ";

		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(d.nombre) like UPPER(?) ";
		}

		query += "ORDER BY d.nombre ASC ";

		if (page != Constants.NO_PAGINACION) {
			query += "LIMIT ? OFFSET ?";
		}

		try (PreparedStatement ps = c.prepareStatement(query)) {
			int count = 1;

			if (StringUtils.isNotEmpty(nombre)) {
				ps.setString(count++, "%" + nombre + "%");
			}

			if (page != Constants.NO_PAGINACION) {
				PropertiesManager pmgr = new PropertiesManager();
				int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
				int resultFrom = pagSize * page;
				ps.setInt(count++, pagSize);
				ps.setInt(count, resultFrom);
			}
			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));

					results.add(dependenciaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

}