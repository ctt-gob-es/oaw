package es.inteco.rastreador2.dao.observatorio;

import java.sql.Connection;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;

/**
 * Common methods DAO class.
 */
public abstract class DataBaseDAO {
	
	/**
	 * Check if the connection is correct and refresh it if dont.
	 *
	 * @param c          the connection to check.
	 * @throws Exception the SQL exception.
	 */
	public static Connection reOpenConnectionIfIsNecessary(Connection c) 
			throws Exception
	{
		try {
			if (!c.isValid(0)) {
				c = DataBaseManager.getConnection();
			}
			return c;
		} catch (Exception e) {
			Logger.putLog("SQL Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			e.printStackTrace();
			c = DataBaseManager.getConnection();
			return c;
		}
	}
	
}
