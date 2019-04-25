package es.inteco.rastreador2.dao.categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.proxy.ProxyDAO;

/**
 * The Class CategoriaDAO.
 */
public class CategoriaDAO {

	/**
	 * Gets the category by ID.
	 *
	 * @param c the c
	 * @param categoryId the category id
	 * @return the category by ID
	 * @throws Exception the exception
	 */
	public static CategoriaForm getCategoryByID(Connection c, String categoryId) throws Exception {

		CategoriaForm category = new CategoriaForm();

		String query = "SELECT c.id_categoria, c.nombre FROM categorias_lista c WHERE c.id_categoria = ?";

		try (PreparedStatement ps = c.prepareStatement(query)) {

			ps.setString(1, categoryId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					category.setId(categoryId);
					category.setName(rs.getString("c.nombre"));

				}
			}

		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return category;

	}

}
