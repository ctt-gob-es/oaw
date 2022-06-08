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
package es.inteco.rastreador2.dao.login;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.comun.RedireccionConfigForm;
import es.inteco.rastreador2.actionform.usuario.EliminarUsuarioSistemaForm;
import es.inteco.rastreador2.actionform.usuario.ModificarUsuarioPassForm;
import es.inteco.rastreador2.actionform.usuario.ModificarUsuarioSistemaForm;
import es.inteco.rastreador2.actionform.usuario.NuevoUsuarioSistemaForm;
import es.inteco.rastreador2.actionform.usuario.VerUsuarioSistemaForm;
import es.inteco.rastreador2.utils.EncryptUtils;
import es.inteco.rastreador2.utils.ListadoUsuario;

/**
 * The Class LoginDAO.
 */
public final class LoginDAO {
	/**
	 * Instantiates a new login DAO.
	 */
	private LoginDAO() {
	}

	/**
	 * Comprueba si el usuario está registrado.
	 *
	 * @param c        the c
	 * @param userName the user name
	 * @param password the password
	 * @return the registered user
	 * @throws SQLException the SQL exception
	 */
	public static Usuario getRegisteredUser(final Connection c, final String userName, final String password) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM usuario u WHERE u.usuario = ? AND Password = md5(?)")) {
			ps.setString(1, userName);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Usuario user = new Usuario();
					user.setId(rs.getLong("id_usuario"));
					user.setName(rs.getString("Nombre"));
					user.setSurname(rs.getString("Apellidos"));
					user.setLogin(rs.getString("Usuario"));
					user.setEmail(rs.getString("Email"));
					user.setDepartment(rs.getString("Departamento"));
					return user;
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getRegisteredUser", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user rol type.
	 *
	 * @param c      the c
	 * @param idUser the id user
	 * @return the user rol type
	 * @throws SQLException the SQL exception
	 */
	public static int getUserRolType(final Connection c, final Long idUser) throws SQLException {
		try (PreparedStatement ps = c
				.prepareStatement("SELECT id_tipo FROM roles r " + "JOIN usuario_rol ur ON (ur.id_rol = r.id_rol) " + "JOIN usuario u ON (ur.usuario = u.id_usuario) " + "WHERE u.id_usuario = ?")) {
			ps.setLong(1, idUser);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("id_tipo");
				} else {
					throw new SQLException("Usuario no encontrado");
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getUserRolType", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user account.
	 *
	 * @param c      the c
	 * @param idUser the id user
	 * @return the user account
	 * @throws SQLException the SQL exception
	 */
	public static List<String> getUserAccount(final Connection c, final Long idUser) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT  DISTINCT(ccu.id_usuario), GROUP_CONCAT(DISTINCT(cc.nombre)) as nombre " + "FROM cuenta_cliente cc "
				+ "JOIN cuenta_cliente_usuario ccu ON (cc.id_cuenta = ccu.id_cuenta) " + "WHERE ccu.id_usuario = ? GROUP BY ccu.id_usuario")) {
			ps.setLong(1, idUser);
			try (ResultSet rs = ps.executeQuery()) {
				final List<String> nombreCuenta = new ArrayList<>();
				if (rs.next()) {
					nombreCuenta.addAll(listFromString(rs.getString("nombre")));
				}
				return nombreCuenta;
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getUserAccount", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user account ids.
	 *
	 * @param c      the c
	 * @param idUser the id user
	 * @return the user account ids
	 * @throws SQLException the SQL exception
	 */
	public static List<String> getUserAccountIds(final Connection c, final Long idUser) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT id_cuenta FROM cuenta_cliente_usuario WHERE id_usuario = ?")) {
			ps.setLong(1, idUser);
			try (ResultSet rs = ps.executeQuery()) {
				List<String> idsCuenta = new ArrayList<>();
				while (rs.next()) {
					idsCuenta.add(rs.getString("id_cuenta"));
				}
				return idsCuenta;
			} catch (SQLException e) {
				Logger.putLog("Error en getUserAccountIds", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
	}

	/**
	 * Gets the user observatory ids.
	 *
	 * @param c      the c
	 * @param idUser the id user
	 * @return the user observatory ids
	 * @throws SQLException the SQL exception
	 */
	public static List<String> getUserObservatoryIds(final Connection c, final Long idUser) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT id_observatorio FROM observatorio_usuario WHERE id_usuario = ?")) {
			ps.setLong(1, idUser);
			try (ResultSet rs = ps.executeQuery()) {
				List<String> idsObservatorio = new ArrayList<>();
				if (rs.next()) {
					idsObservatorio.add(rs.getString("id_observatorio"));
				}
				return idsObservatorio;
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getUserObservatoryIds", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user cartridge.
	 *
	 * @param c      the c
	 * @param idUser the id user
	 * @return the user cartridge
	 * @throws SQLException the SQL exception
	 */
	public static List<CartuchoForm> getUserCartridge(final Connection c, final Long idUser) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement(
				"SELECT c.id_cartucho, c.aplicacion FROM usuario_cartucho uc " + "JOIN cartucho c ON (c.id_cartucho = uc.id_cartucho) WHERE uc.id_usuario = ? AND c.instalado = true")) {
			ps.setLong(1, idUser);
			try (ResultSet rs = ps.executeQuery()) {
				final List<CartuchoForm> cartridgeList = new ArrayList<>();
				while (rs.next()) {
					final CartuchoForm cartucho = new CartuchoForm();
					cartucho.setId(rs.getLong("id_cartucho"));
					cartucho.setName(rs.getString("aplicacion"));
					cartridgeList.add(cartucho);
				}
				return cartridgeList;
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getUserCartridge", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user roles.
	 *
	 * @param c      the c
	 * @param idUser the id user
	 * @return the user roles
	 * @throws SQLException the SQL exception
	 */
	public static List<Role> getUserRoles(final Connection c, final Long idUser) throws SQLException {
		final List<Role> roleList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM usuario_rol ur " + "JOIN roles r ON (r.id_rol = ur.id_rol) WHERE ur.usuario = ?")) {
			ps.setLong(1, idUser);
			try (ResultSet rs = ps.executeQuery()) {
				populateRoleListFromResultSet(roleList, rs);
			}
			return roleList;
		} catch (SQLException e) {
			Logger.putLog("Error en getUserRoles", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the all user cartridge.
	 *
	 * @param c the c
	 * @return the all user cartridge
	 * @throws SQLException the SQL exception
	 */
	public static List<CartuchoForm> getAllUserCartridge(Connection c) throws SQLException {
		final List<CartuchoForm> cartridgeList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT c.id_cartucho, c.aplicacion FROM cartucho c WHERE c.instalado = 1"); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				final CartuchoForm cartucho = new CartuchoForm();
				cartucho.setId(rs.getLong("id_cartucho"));
				cartucho.setName(rs.getString("aplicacion"));
				cartridgeList.add(cartucho);
			}
			return cartridgeList;
		} catch (SQLException e) {
			Logger.putLog("Error al recuperar la lista de roles", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the all user roles.
	 *
	 * @param c           the c
	 * @param userRolType the user rol type
	 * @return the all user roles
	 * @throws SQLException the SQL exception
	 */
	public static List<Role> getAllUserRoles(Connection c, int userRolType) throws SQLException {
		final List<Role> roleList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM roles WHERE id_tipo = ?")) {
			ps.setInt(1, userRolType);
			try (ResultSet rs = ps.executeQuery()) {
				populateRoleListFromResultSet(roleList, rs);
			}
			return roleList;
		} catch (SQLException e) {
			Logger.putLog("Error al recuperar la lista de roles", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Count users.
	 *
	 * @param c the c
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int countUsers(Connection c) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM usuario u WHERE u.id_usuario != 1")) {
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en countUsers", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * User list.
	 *
	 * @param c    the c
	 * @param page the page
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<ListadoUsuario> userList(Connection c, int page) throws Exception {
		final List<ListadoUsuario> userList = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * page;
		try (PreparedStatement ps = c
				.prepareStatement("SELECT DISTINCT(u.usuario), u.id_usuario , r.id_tipo, GROUP_CONCAT(DISTINCT(r.rol)) AS grupo, GROUP_CONCAT(DISTINCT(c.aplicacion)) AS cartuchos "
						+ "FROM usuario u	" + "LEFT JOIN usuario_rol ur ON (ur.usuario = u.id_usuario) " + "LEFT JOIN roles r ON (r.id_rol = ur.id_rol) "
						+ "LEFT JOIN usuario_cartucho uc ON (u.id_usuario = uc.id_usuario) " + "LEFT JOIN cartucho c ON (c.id_cartucho = uc.id_cartucho) " + "WHERE u.id_usuario != 1 "
						+ "GROUP BY u.usuario, u.id_usuario, r.id_tipo ORDER BY u.usuario LIMIT ? OFFSET ?")) {
			ps.setInt(1, pagSize);
			ps.setInt(2, resultFrom);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ListadoUsuario ls = new ListadoUsuario();
					ls.setUsuario(rs.getString("usuario"));
					ls.setId_usuario(rs.getLong("id_usuario"));
					ls.setTipo(listFromString(rs.getString("grupo")));
					ls.setTipoRol(rs.getInt("id_tipo"));
					if (rs.getString("cartuchos") != null) {
						ls.setCartucho(listFromString(rs.getString("cartuchos")));
					}
					userList.add(ls);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error en userList", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return userList;
	}

	/**
	 * Gets the delete user.
	 *
	 * @param c                          the c
	 * @param idUser                     the id user
	 * @param eliminarUsuarioSistemaForm the eliminar usuario sistema form
	 * @return the delete user
	 * @throws Exception the exception
	 */
	public static EliminarUsuarioSistemaForm getDeleteUser(final Connection c, final Long idUser, final EliminarUsuarioSistemaForm eliminarUsuarioSistemaForm) throws Exception {
		try (PreparedStatement ps = c
				.prepareStatement("SELECT u.*, GROUP_CONCAT(DISTINCT(cc.nombre)) AS nombreCuentas " + "FROM usuario u " + "LEFT JOIN cuenta_cliente_usuario ccu ON (ccu.id_usuario = u.id_usuario) "
						+ "LEFT JOIN cuenta_cliente cc ON (ccu.id_cuenta = cc.id_cuenta) " + "WHERE u.id_usuario = ? GROUP BY u.id_usuario;")) {
			ps.setLong(1, idUser);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					eliminarUsuarioSistemaForm.setNombre(rs.getString("nombre"));
					eliminarUsuarioSistemaForm.setApellidos(rs.getString("apellidos"));
					eliminarUsuarioSistemaForm.setDepartamento(rs.getString("departamento"));
					eliminarUsuarioSistemaForm.setEmail(rs.getString("email"));
					eliminarUsuarioSistemaForm.setId(rs.getLong("id_usuario"));
					if ((rs.getString("nombreCuentas") != null)) {
						eliminarUsuarioSistemaForm.setNombreCuenta(listFromString(rs.getString("nombreCuentas")));
					}
					eliminarUsuarioSistemaForm.setUsuario(rs.getString("usuario"));
				}
				eliminarUsuarioSistemaForm.setRoles(getUserRoles(c, eliminarUsuarioSistemaForm.getId()));
				eliminarUsuarioSistemaForm.setCartuchos(getUserCartridge(c, eliminarUsuarioSistemaForm.getId()));
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return eliminarUsuarioSistemaForm;
	}

	/**
	 * Delete user.
	 *
	 * @param c      the c
	 * @param idUser the id user
	 * @throws SQLException the SQL exception
	 */
	public static void deleteUser(Connection c, Long idUser) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM usuario WHERE id_usuario = ?")) {
			ps.setLong(1, idUser);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Exception: ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Exist user with key.
	 *
	 * @param c        the c
	 * @param passwold the passwold
	 * @param idUser   the id user
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean existUserWithKey(Connection c, String passwold, Long idUser) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT 1 from usuario where id_usuario = ? AND password = md5(?);")) {
			ps.setLong(1, idUser);
			ps.setString(2, passwold);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			Logger.putLog("Error en existUserWithKey", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update password.
	 *
	 * @param c                        the c
	 * @param modificarUsuarioPassForm the modificar usuario pass form
	 * @param idUser                   the id user
	 * @throws SQLException the SQL exception
	 */
	public static void updatePassword(Connection c, ModificarUsuarioPassForm modificarUsuarioPassForm, Long idUser) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("UPDATE usuario SET Password = md5(?) where id_usuario = ?;")) {
			ps.setString(1, modificarUsuarioPassForm.getPassword());
			ps.setLong(2, idUser);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Error en updatePassword", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user dates to update.
	 *
	 * @param c                           the c
	 * @param modificarUsuarioSistemaForm the modificar usuario sistema form
	 * @param idUser                      the id user
	 * @param userRolType                 the user rol type
	 * @return the user dates to update
	 * @throws Exception the exception
	 */
	public static void getUserDatesToUpdate(Connection c, ModificarUsuarioSistemaForm modificarUsuarioSistemaForm, Long idUser, int userRolType) throws Exception {
		try {
			DatosForm dataForm = getUserDataById(c, idUser);
			modificarUsuarioSistemaForm.setNombre2(dataForm.getNombre());
			modificarUsuarioSistemaForm.setApellidos(dataForm.getApellidos());
			modificarUsuarioSistemaForm.setDepartamento(dataForm.getDepartamento());
			modificarUsuarioSistemaForm.setEmail(dataForm.getEmail());
			modificarUsuarioSistemaForm.setUsuario(dataForm.getUsuario());
			modificarUsuarioSistemaForm.setNombre_antiguo(dataForm.getUsuario());
			modificarUsuarioSistemaForm.setNombre(dataForm.getUsuario());
			modificarUsuarioSistemaForm.setRoleType(String.valueOf(userRolType));
			modificarUsuarioSistemaForm.setCartuchosList(getAllUserCartridge(c));
			List<CartuchoForm> selectedCartridge = getUserCartridge(c, Long.valueOf(dataForm.getId()));
			modificarUsuarioSistemaForm.setSelectedCartuchos(new String[selectedCartridge.size()]);
			int i = 0;
			for (CartuchoForm cartucho : selectedCartridge) {
				modificarUsuarioSistemaForm.getSelectedCartuchos()[i] = cartucho.getId().toString();
				i++;
			}
			modificarUsuarioSistemaForm.setRoles(getAllUserRoles(c, userRolType));
			List<Role> selectedRoles = getUserRoles(c, Long.valueOf(dataForm.getId()));
			modificarUsuarioSistemaForm.setSelectedRoles(new String[selectedRoles.size()]);
			i = 0;
			for (Role role : selectedRoles) {
				modificarUsuarioSistemaForm.getSelectedRoles()[i] = role.getId().toString();
				i++;
			}
			modificarUsuarioSistemaForm.setCuentaCliente(getUserAccountIds(c, Long.valueOf(dataForm.getId())));
			modificarUsuarioSistemaForm.setObservatorio(getUserObservatoryIds(c, Long.valueOf(dataForm.getId())));
		} catch (Exception e) {
			Logger.putLog("Error al intentar actualizar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user data to see.
	 *
	 * @param c                     the c
	 * @param verUsuarioSistemaForm the ver usuario sistema form
	 * @param idUser                the id user
	 * @return the user data to see
	 * @throws Exception the exception
	 */
	public static void getUserDataToSee(Connection c, VerUsuarioSistemaForm verUsuarioSistemaForm, Long idUser) throws Exception {
		try {
			DatosForm dataForm = getUserDataById(c, idUser);
			verUsuarioSistemaForm.setNombre(dataForm.getNombre());
			verUsuarioSistemaForm.setUsuario(dataForm.getUsuario());
			verUsuarioSistemaForm.setApellidos(dataForm.getApellidos());
			verUsuarioSistemaForm.setDepartamento(dataForm.getDepartamento());
			verUsuarioSistemaForm.setEmail(dataForm.getEmail());
			verUsuarioSistemaForm.setCartucho(getUserCartridge(c, Long.valueOf(dataForm.getId())));
			verUsuarioSistemaForm.setTipos(getUserRoles(c, Long.valueOf(dataForm.getId())));
			verUsuarioSistemaForm.setNombreCuenta(getUserAccount(c, Long.valueOf(dataForm.getId())));
		} catch (Exception e) {
			Logger.putLog("Error en getUserDataToSee", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user data by id.
	 *
	 * @param c      the c
	 * @param idUser the id user
	 * @return the user data by id
	 * @throws SQLException the SQL exception
	 */
	public static DatosForm getUserDataById(Connection c, Long idUser) throws SQLException {
		final DatosForm datosForm = new DatosForm();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM usuario u WHERE u.id_usuario = ?")) {
			ps.setLong(1, idUser);
			try (ResultSet rs = ps.executeQuery()) {
				populateUserDataFromResultSet(datosForm, rs);
			}
			return datosForm;
		} catch (SQLException e) {
			Logger.putLog("Error en getUserDataById", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the user data by name.
	 *
	 * @param c    the c
	 * @param user the user
	 * @return the user data by name
	 * @throws SQLException the SQL exception
	 */
	public static DatosForm getUserDataByName(Connection c, String user) throws SQLException {
		final DatosForm datosForm = new DatosForm();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM usuario u WHERE u.usuario = ?")) {
			ps.setString(1, user);
			try (ResultSet rs = ps.executeQuery()) {
				populateUserDataFromResultSet(datosForm, rs);
			}
			return datosForm;
		} catch (SQLException e) {
			Logger.putLog("Error en getUserDataByName", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Populate user data from result set.
	 *
	 * @param userData  the user data
	 * @param resultado the resultado
	 * @throws SQLException the SQL exception
	 */
	private static void populateUserDataFromResultSet(final DatosForm userData, final ResultSet resultado) throws SQLException {
		while (resultado.next()) {
			userData.setNombre(resultado.getString("Nombre").trim());
			userData.setUsuario(resultado.getString("Usuario").trim());
			userData.setApellidos(resultado.getString("Apellidos").trim());
			userData.setDepartamento(resultado.getString("Departamento").trim());
			userData.setEmail(resultado.getString("Email").trim());
			userData.setId(resultado.getString("id_usuario"));
		}
	}

	/**
	 * Comprueba si ya existe una cuenta de usuario con un determinado nombre.
	 *
	 * @param c    conexión Connection a la BD
	 * @param name cadena String con el nombre de usuario a comprobar
	 * @return true si ya existe una cuenta de usuario con ese nombre o false en caso contrario
	 * @throws SQLException the SQL exception
	 */
	public static boolean existUser(final Connection c, final String name) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM usuario WHERE usuario = ?")) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			Logger.putLog("Error en existUser", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update user.
	 *
	 * @param c                           the c
	 * @param modificarUsuarioSistemaForm the modificar usuario sistema form
	 * @throws Exception the exception
	 */
	public static void updateUser(final Connection c, final ModificarUsuarioSistemaForm modificarUsuarioSistemaForm) throws Exception {
		try {
			c.setAutoCommit(false);
			// Borramos los roles antiguos
			final long idUsuario = Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario());
			try (PreparedStatement deteleUsuarioRolStatement = c.prepareStatement("DELETE FROM usuario_rol WHERE usuario = ?")) {
				deteleUsuarioRolStatement.setLong(1, idUsuario);
				deteleUsuarioRolStatement.executeUpdate();
			}
			// Borramos las cuentas de usuario antiguas
			try (PreparedStatement deleteCuentaClienteUsuarioStatement = c.prepareStatement("DELETE FROM cuenta_cliente_usuario WHERE id_usuario = ?")) {
				deleteCuentaClienteUsuarioStatement.setLong(1, idUsuario);
				deleteCuentaClienteUsuarioStatement.executeUpdate();
			}
			// Borramos los cartuchos antiguos
			try (PreparedStatement deleteUsuarioCartuchoStatement = c.prepareStatement("DELETE FROM usuario_cartucho WHERE id_usuario = ?")) {
				deleteUsuarioCartuchoStatement.setLong(1, idUsuario);
				deleteUsuarioCartuchoStatement.executeUpdate();
			}
			// Actualizamos los datos de usuario
			try (PreparedStatement updateUsuarioStatement = c.prepareStatement("UPDATE usuario SET Usuario = ?, Nombre = ?, Apellidos = ?, Departamento = ?, Email = ? WHERE id_usuario = ?")) {
				updateUsuarioStatement.setString(1, modificarUsuarioSistemaForm.getNombre());
				updateUsuarioStatement.setString(2, modificarUsuarioSistemaForm.getNombre2());
				updateUsuarioStatement.setString(3, modificarUsuarioSistemaForm.getApellidos());
				updateUsuarioStatement.setString(4, modificarUsuarioSistemaForm.getDepartamento());
				updateUsuarioStatement.setString(5, modificarUsuarioSistemaForm.getEmail());
				updateUsuarioStatement.setLong(6, idUsuario);
				updateUsuarioStatement.executeUpdate();
			}
			// Insertamos los roles nuevos
			if (modificarUsuarioSistemaForm.getSelectedRoles() != null && modificarUsuarioSistemaForm.getSelectedRoles().length > 0 && idUsuario != 0) {
				insertUsuarioRoles(c, idUsuario, modificarUsuarioSistemaForm.getSelectedRoles());
			}
			// Insertamos los cartuchos nuevos
			if (modificarUsuarioSistemaForm.getSelectedCartuchos() != null && modificarUsuarioSistemaForm.getSelectedCartuchos().length > 0 && idUsuario != 0) {
				insertUsuarioCartucho(c, idUsuario, modificarUsuarioSistemaForm.getSelectedCartuchos());
			}
			// Insertamos las cuenta cliente nuevas
			if (modificarUsuarioSistemaForm.getSelectedCuentaCliente() != null && modificarUsuarioSistemaForm.getSelectedCuentaCliente().length > 0 && idUsuario != 0) {
				insertCuentaClienteUsuario(c, idUsuario, modificarUsuarioSistemaForm.getSelectedCuentaCliente());
			}
			c.commit();
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar modificar el usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			c.rollback();
			c.setAutoCommit(true);
			throw e;
		}
	}

	/**
	 * Insert usuario cartucho.
	 *
	 * @param c                 the c
	 * @param idUsuario         the id usuario
	 * @param selectedCartuchos the selected cartuchos
	 * @throws SQLException the SQL exception
	 */
	private static void insertUsuarioCartucho(final Connection c, final long idUsuario, final String[] selectedCartuchos) throws SQLException {
		try (PreparedStatement insertUsuarioCartuchoStatement = c.prepareStatement("INSERT INTO usuario_cartucho VALUES (?,?)")) {
			for (String selectedCartucho : selectedCartuchos) {
				insertUsuarioCartuchoStatement.setLong(1, idUsuario);
				insertUsuarioCartuchoStatement.setInt(2, Integer.parseInt(selectedCartucho));
				insertUsuarioCartuchoStatement.addBatch();
			}
			insertUsuarioCartuchoStatement.executeBatch();
		}
	}

	/**
	 * Insert usuario roles.
	 *
	 * @param c             the c
	 * @param idUsuario     the id usuario
	 * @param selectedRoles the selected roles
	 * @throws SQLException the SQL exception
	 */
	private static void insertUsuarioRoles(final Connection c, final long idUsuario, final String[] selectedRoles) throws SQLException {
		try (PreparedStatement insertUsuarioRolStatement = c.prepareStatement("INSERT INTO usuario_rol VALUES (?,?)")) {
			for (String selectedRole : selectedRoles) {
				insertUsuarioRolStatement.setLong(1, idUsuario);
				insertUsuarioRolStatement.setInt(2, Integer.parseInt(selectedRole));
				insertUsuarioRolStatement.addBatch();
			}
			insertUsuarioRolStatement.executeBatch();
		}
	}

	/**
	 * Insert cuenta cliente usuario.
	 *
	 * @param c                     the c
	 * @param idUsuario             the id usuario
	 * @param selectedCuentaCliente the selected cuenta cliente
	 * @throws SQLException the SQL exception
	 */
	private static void insertCuentaClienteUsuario(final Connection c, final long idUsuario, final String[] selectedCuentaCliente) throws SQLException {
		try (PreparedStatement insertCuentaClientStatement = c.prepareStatement("INSERT INTO cuenta_cliente_usuario VALUES (?,?)")) {
			for (String aSelectedCuentaCliente : selectedCuentaCliente) {
				insertCuentaClientStatement.setInt(1, Integer.parseInt(aSelectedCuentaCliente));
				insertCuentaClientStatement.setLong(2, idUsuario);
				insertCuentaClientStatement.addBatch();
			}
			insertCuentaClientStatement.executeBatch();
		}
	}

	/**
	 * Insert user.
	 *
	 * @param c                       the c
	 * @param nuevoUsuarioSistemaForm the nuevo usuario sistema form
	 * @throws Exception the exception
	 */
	public static void insertUser(final Connection c, final NuevoUsuarioSistemaForm nuevoUsuarioSistemaForm) throws Exception {
		try {
			c.setAutoCommit(false);
			final long idUsuario = insertIntoUsuario(c, nuevoUsuarioSistemaForm);
			if (nuevoUsuarioSistemaForm.getObservatorio() != null && nuevoUsuarioSistemaForm.getObservatorio().length != 0 && idUsuario != 0) {
				insertObservatorioUsuario(c, idUsuario, nuevoUsuarioSistemaForm.getObservatorio());
				final PropertiesManager pmgr = new PropertiesManager();
				final String[] roles = new String[] { pmgr.getValue(CRAWLER_PROPERTIES, "role.observatory.id") };
				nuevoUsuarioSistemaForm.setSelectedRoles(roles);
			}
			if (nuevoUsuarioSistemaForm.getSelectedRoles() != null && nuevoUsuarioSistemaForm.getSelectedRoles().length > 0 && idUsuario != 0) {
				insertUsuarioRoles(c, idUsuario, nuevoUsuarioSistemaForm.getSelectedRoles());
			}
			if (nuevoUsuarioSistemaForm.getSelectedCartuchos() != null && nuevoUsuarioSistemaForm.getSelectedCartuchos().length > 0 && idUsuario != 0) {
				insertUsuarioCartucho(c, idUsuario, nuevoUsuarioSistemaForm.getSelectedCartuchos());
			}
			if (nuevoUsuarioSistemaForm.getCuenta_cliente() != null && nuevoUsuarioSistemaForm.getCuenta_cliente().length != 0 && idUsuario != 0) {
				insertCuentaClienteUsuario(c, idUsuario, nuevoUsuarioSistemaForm.getCuenta_cliente());
			}
			c.commit();
			c.setAutoCommit(true);
		} catch (Exception e) {
			c.rollback();
			c.setAutoCommit(true);
			Logger.putLog("Error al insertar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Insert into usuario.
	 *
	 * @param c                       the c
	 * @param nuevoUsuarioSistemaForm the nuevo usuario sistema form
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	private static long insertIntoUsuario(final Connection c, NuevoUsuarioSistemaForm nuevoUsuarioSistemaForm) throws SQLException {
		try (PreparedStatement insertUsuarioStatement = c.prepareStatement("INSERT INTO usuario(Usuario, Password,  Nombre, Apellidos, Departamento, Email) VALUES (?, md5(?), ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS)) {
			insertUsuarioStatement.setString(1, nuevoUsuarioSistemaForm.getNombre());
			insertUsuarioStatement.setString(2, nuevoUsuarioSistemaForm.getPassword());
			insertUsuarioStatement.setString(3, nuevoUsuarioSistemaForm.getNombre2());
			insertUsuarioStatement.setString(4, nuevoUsuarioSistemaForm.getApellidos());
			insertUsuarioStatement.setString(5, nuevoUsuarioSistemaForm.getDepartamento());
			insertUsuarioStatement.setString(6, nuevoUsuarioSistemaForm.getEmail());
			insertUsuarioStatement.executeUpdate();
			try (ResultSet rs = insertUsuarioStatement.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Insert observatorio usuario.
	 *
	 * @param c                     the c
	 * @param idUsuario             the id usuario
	 * @param selectedObservatorios the selected observatorios
	 * @throws SQLException the SQL exception
	 */
	private static void insertObservatorioUsuario(final Connection c, final long idUsuario, final String[] selectedObservatorios) throws SQLException {
		try (PreparedStatement insertObservatorioUsuarioStatement = c.prepareStatement("INSERT INTO observatorio_usuario VALUES (?,?)")) {
			for (String selectedObservatorio : selectedObservatorios) {
				insertObservatorioUsuarioStatement.setInt(1, Integer.parseInt(selectedObservatorio));
				insertObservatorioUsuarioStatement.setLong(2, idUsuario);
				insertObservatorioUsuarioStatement.addBatch();
			}
			insertObservatorioUsuarioStatement.executeBatch();
		}
	}

	/**
	 * Load user data.
	 *
	 * @param c                     the c
	 * @param user                  the user
	 * @param redireccionConfigForm the redireccion config form
	 * @return the redireccion config form
	 * @throws Exception the exception
	 */
	public static RedireccionConfigForm loadUserData(Connection c, String user, RedireccionConfigForm redireccionConfigForm) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT id_cartucho, password, tipo FROM usuario WHERE usuario = ?")) {
			ps.setString(1, user);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					redireccionConfigForm.setId_cartucho(rs.getInt(1));
					redireccionConfigForm.setPass(EncryptUtils.encrypt(rs.getString(2)));
					redireccionConfigForm.setTipo(rs.getString(3));
				}
				redireccionConfigForm.setNumCartuchos(1);
				redireccionConfigForm.setSeleccionados(1);
			}
		} catch (Exception e) {
			Logger.putLog("Error en loadUserData", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return redireccionConfigForm;
	}

	/**
	 * List from string.
	 *
	 * @param roleString the role string
	 * @return the list
	 */
	private static List<String> listFromString(String roleString) {
		final List<String> roleList = new ArrayList<>();
		if (roleString != null) {
			final StringTokenizer tokenizer = new StringTokenizer(roleString, ",");
			while (tokenizer.hasMoreTokens()) {
				roleList.add(tokenizer.nextToken());
			}
		}
		return roleList;
	}

	/**
	 * Gets the client account.
	 *
	 * @param c         the c
	 * @param idAccount the id account
	 * @return the client account
	 * @throws SQLException the SQL exception
	 */
	public static List<DatosForm> getClientAccount(Connection c, Long idAccount) throws SQLException {
		final List<DatosForm> results = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT DISTINCT u.id_usuario, u.nombre, u.usuario, u.apellidos, u.departamento, u.email FROM usuario u "
				+ "INNER JOIN usuario_rol ur ON (ur.usuario = u.id_usuario) " + "INNER JOIN cuenta_cliente_usuario ccu ON (ccu.id_usuario = u.id_usuario) "
				+ "INNER JOIN cuenta_cliente cc ON (cc.id_cuenta = ccu.id_cuenta) " + "WHERE cc.id_cuenta = ?;")) {
			ps.setLong(1, idAccount);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					DatosForm userData = new DatosForm();
					userData.setNombre(rs.getString("Nombre").trim());
					userData.setUsuario(rs.getString("Usuario").trim());
					userData.setApellidos(rs.getString("Apellidos").trim());
					userData.setDepartamento(rs.getString("Departamento").trim());
					userData.setEmail(rs.getString("Email").trim());
					userData.setId(rs.getString("id_usuario"));
					userData.setRoles(getUserRoles(c, rs.getLong("id_usuario")));
					results.add(userData);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getClientAccount", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Gets the mails by role.
	 *
	 * @param c      the c
	 * @param idRole the id role
	 * @return the mails by role
	 * @throws SQLException the SQL exception
	 */
	public static List<String> getMailsByRole(Connection c, Long idRole) throws SQLException {
		final List<String> mails = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT email FROM usuario u " + "LEFT JOIN usuario_rol ur ON (u.id_usuario = ur.usuario) " + "WHERE ur.id_rol = ?;")) {
			ps.setLong(1, idRole);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					mails.add(rs.getString("email"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getMailsByRole", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return mails;
	}

	/**
	 * Populate role list from result set.
	 *
	 * @param roleList the role list
	 * @param rs       the rs
	 * @throws SQLException the SQL exception
	 */
	private static void populateRoleListFromResultSet(final List<Role> roleList, final ResultSet rs) throws SQLException {
		while (rs.next()) {
			final Role role = new Role();
			role.setId(rs.getLong("id_rol"));
			role.setName(rs.getString("rol"));
			roleList.add(role);
		}
	}

	/**
	 * Gets the user data by name.
	 *
	 * @param c the c
	 * @return the user data by name
	 * @throws SQLException the SQL exception
	 */
	public static List<DatosForm> getAdminUsers(Connection c) throws SQLException {
		List<DatosForm> adminData = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT u.* FROM usuario u JOIN usuario_rol ur ON u.id_usuario=ur.usuario WHERE ur.id_rol = 1")) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final DatosForm userData = new DatosForm();
					userData.setNombre(rs.getString("nombre").trim());
					userData.setUsuario(rs.getString("usuario").trim());
					userData.setApellidos(rs.getString("apellidos").trim());
					userData.setDepartamento(rs.getString("departamento").trim());
					userData.setEmail(rs.getString("email").trim());
					userData.setId(rs.getString("id_usuario"));
					adminData.add(userData);
				}
			}
			return adminData;
		} catch (SQLException e) {
			Logger.putLog("Error en getUserDataByName", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}
}
