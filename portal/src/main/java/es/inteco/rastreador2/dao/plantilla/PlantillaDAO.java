package es.inteco.rastreador2.dao.plantilla;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.semillas.PlantillaForm;
import es.inteco.rastreador2.dao.dependencia.DependenciaDAO;

/**
 * The Class PlantillaDAO.
 */
public final class PlantillaDAO {
	/**
	 * Find all.
	 *
	 * @param c      the c
	 * @param pagina the pagina
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	public static List<PlantillaForm> findAll(Connection c, int pagina) throws SQLException {
		List<PlantillaForm> plantillas = new ArrayList<PlantillaForm>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;
		// query += " LIMIT ? OFFSET ?";
		try (PreparedStatement ps = c.prepareStatement("SELECT p.id_plantilla, p.nombre FROM observatorio_plantillas p ORDER BY p.nombre ASC LIMIT ? OFFSET ?")) {
			ps.setLong(1, pagSize);
			ps.setLong(2, resultFrom);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					PlantillaForm plantilla = new PlantillaForm();
					plantilla.setId(rs.getLong("p.id_plantilla"));
					plantilla.setNombre(rs.getString("p.nombre"));
					plantillas.add(plantilla);
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		return plantillas;
	}

	/**
	 * Find by id.
	 *
	 * @param c  the c
	 * @param id the id
	 * @return the plantilla form
	 * @throws SQLException the SQL exception
	 */
	public static PlantillaForm findById(Connection c, Long id) throws SQLException {
		PlantillaForm plantilla = null;
		// query += " LIMIT ? OFFSET ?";
		try (PreparedStatement ps = c.prepareStatement("SELECT p.id_plantilla, p.nombre, p.documento FROM observatorio_plantillas p WHERE p.id_plantilla = ?")) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					plantilla = new PlantillaForm();
					plantilla.setId(rs.getLong("p.id_plantilla"));
					plantilla.setNombre(rs.getString("p.nombre"));
					
					Blob blob = rs.getBlob("p.documento");

					int blobLength = (int) blob.length();  
					byte[] blobAsBytes = blob.getBytes(1, blobLength);

					//release the blob and free up memory. (since JDBC 4.0)
					blob.free();
					
					plantilla.setDocumento(blobAsBytes);
					return plantilla;
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		return plantilla;
	}

	/**
	 * Exists plantilla.
	 *
	 * @param c         the c
	 * @param plantilla the plantilla
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean existsPlantilla(Connection c, PlantillaForm plantilla) throws SQLException {
		boolean exists = false;
		final String query = "SELECT * FROM observatorio_plantillas WHERE UPPER(nombre) = UPPER(?)";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, plantilla.getNombre());
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return exists;
	}

	/**
	 * Count.
	 *
	 * @param c the c
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int count(Connection c) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM observatorio_plantillas p ORDER BY p.nombre ASC")) {
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		return 0;
	}

	/**
	 * Save.
	 *
	 * @param c         the c
	 * @param plantilla the plantilla
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean save(Connection c, final PlantillaForm plantilla) throws SQLException {
		boolean saved = false;
		try (PreparedStatement ps = c.prepareStatement("INSERT INTO observatorio_plantillas(nombre, documento) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, plantilla.getNombre());
			Blob blobValue = new SerialBlob(plantilla.getDocumento());
			ps.setBlob(2, blobValue);
			// ps.setBytes(2, plantilla.getDocumento());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				saved = true;
			}
		} catch (SQLException e) {
			throw e;
		}
		return saved;
	}

	/**
	 * Update.
	 *
	 * @param c         the c
	 * @param plantilla the plantilla
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean update(Connection c, final PlantillaForm plantilla) throws SQLException {
		boolean saved = false;
		try (PreparedStatement ps = c.prepareStatement("UPDATE observatorio_plantillas SET nombre = ?, clave = ?, c.documento WHERE id_plantilla = ?", Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, plantilla.getNombre());
			ps.setBytes(2, plantilla.getDocumento());
			ps.setLong(3, plantilla.getId());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				saved = true;
			}
		} catch (SQLException e) {
			throw e;
		}
		return saved;
	}

	/**
	 * Delete.
	 *
	 * @param c           the c
	 * @param idPlantilla the id plantilla
	 * @throws SQLException the SQL exception
	 */
	public static void delete(Connection c, Long idPlantilla) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_plantillas WHERE id_plantilla = ?")) {
			ps.setLong(1, idPlantilla);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}
}
