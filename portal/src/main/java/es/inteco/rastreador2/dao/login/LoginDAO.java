package es.inteco.rastreador2.dao.login;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.comun.RedireccionConfigForm;
import es.inteco.rastreador2.actionform.usuario.*;
import es.inteco.rastreador2.utils.DAOUtils;
import es.inteco.rastreador2.utils.EncryptUtils;
import es.inteco.rastreador2.utils.ListadoUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class LoginDAO {

    private LoginDAO() {
    }

    /**
     * Comprueba si el usuario está registrado
     *
     * @param c
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static Usuario getRegisteredUser(Connection c, String userName, String password) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM usuario u WHERE u.usuario = ? AND Password = md5(?)");
            ps.setString(1, userName);
            ps.setString(2, password);
            rs = ps.executeQuery();

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
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw new Exception(e);
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static int getUserRolType(Connection c, Long idUser) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT id_tipo FROM roles r " +
                    "JOIN usuario_rol ur ON (ur.id_rol = r.id_rol) " +
                    "JOIN usuario u ON (ur.usuario = u.id_usuario) " +
                    "WHERE u.id_usuario = ?");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_tipo");
            } else {
                throw new Exception("Usuario no encontrado");
            }

        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<String> getUserAccount(Connection c, Long idUser) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT  DISTINCT(ccu.id_usuario), GROUP_CONCAT(DISTINCT(cc.nombre)) as nombre " +
                    "FROM cuenta_cliente cc " +
                    "JOIN cuenta_cliente_usuario ccu ON (cc.id_cuenta = ccu.id_cuenta) " +
                    "WHERE ccu.id_usuario = ? GROUP BY ccu.id_usuario");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();
            List<String> nombreCuenta = new ArrayList<String>();
            if (rs.next()) {
                nombreCuenta.addAll(listFromString(rs.getString("nombre")));
            }
            return nombreCuenta;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<String> getUserAccountIds(Connection c, Long idUser) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT id_cuenta FROM cuenta_cliente_usuario WHERE id_usuario = ?");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();
            List<String> idsCuenta = new ArrayList<String>();
            while (rs.next()) {
                idsCuenta.add(rs.getString("id_cuenta"));
            }
            return idsCuenta;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<String> getUserObservatoryIds(Connection c, Long idUser) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT id_observatorio FROM observatorio_usuario WHERE id_usuario = ?");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();
            List<String> idsObservatorio = new ArrayList<String>();
            if (rs.next()) {
                idsObservatorio.add(rs.getString("id_observatorio"));
            }
            return idsObservatorio;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<String> getUserObservatory(Connection c, Long idUser) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT DISTINCT(ou.id_usuario), GROUP_CONCAT(DISTINCT(o.nombre)) as nombre " +
                    "FROM observatorio o " +
                    "JOIN observatorio_usuario ou ON (o.id_observatorio = ou.id_observatorio) " +
                    "WHERE ou.id_usuario = ? GROUP BY ou.id_usuario");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();
            List<String> nombreObservatorio = new ArrayList<String>();
            if (rs.next()) {
                nombreObservatorio.addAll(listFromString(rs.getString("nombre")));
            }
            return nombreObservatorio;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<CartuchoForm> getUserCartridge(Connection c, Long idUser) throws Exception {
        List<CartuchoForm> cartridgeList = new ArrayList<CartuchoForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT c.id_cartucho, c.aplicacion FROM usuario_cartucho uc " +
                    "JOIN cartucho c ON (c.id_cartucho = uc.id_cartucho) WHERE uc.id_usuario = ? AND c.instalado = true");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartuchoForm cartucho = new CartuchoForm();
                cartucho.setId(rs.getLong("id_cartucho"));
                cartucho.setName(rs.getString("aplicacion"));
                cartridgeList.add(cartucho);
            }
            return cartridgeList;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<Role> getUserRoles(Connection c, Long idUser) throws SQLException {
        List<Role> roleList = new ArrayList<Role>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM usuario_rol ur " +
                    "JOIN roles r ON (r.id_rol = ur.id_rol) WHERE ur.usuario = ?");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id_rol"));
                role.setName(rs.getString("rol"));
                roleList.add(role);
            }
            return roleList;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw new SQLException(e);
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<CartuchoForm> getAllUserCartridge(Connection c) throws Exception {
        final List<CartuchoForm> cartridgeList = new ArrayList<CartuchoForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT c.id_cartucho, c.aplicacion FROM cartucho c");
            rs = ps.executeQuery();

            while (rs.next()) {
                final CartuchoForm cartucho = new CartuchoForm();
                cartucho.setId(rs.getLong("id_cartucho"));
                cartucho.setName(rs.getString("aplicacion"));
                cartridgeList.add(cartucho);
            }
            return cartridgeList;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar la lista de roles", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<Role> getAllUserRoles(Connection c, int userRolType) throws Exception {
        List<Role> roleList = new ArrayList<Role>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM roles WHERE id_tipo = ?");
            ps.setInt(1, userRolType);
            rs = ps.executeQuery();

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id_rol"));
                role.setName(rs.getString("rol"));
                roleList.add(role);
            }
            return roleList;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar la lista de roles", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static void deleteUsers(String idCartridge, Connection c) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("DELETE FROM usuario WHERE Id_Cartucho = ?");
            ps.setString(1, idCartridge);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }
    }

    public static int countUsers(Connection c) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT COUNT(*) FROM usuario u WHERE u.id_usuario != 1");
            rs = ps.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static List<ListadoUsuario> userList(Connection c, int page) throws Exception {
        List<ListadoUsuario> userList = new ArrayList<ListadoUsuario>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int resultFrom = pagSize * page;
        try {
            ps = c.prepareStatement("SELECT DISTINCT(u.usuario), u.id_usuario , r.id_tipo, GROUP_CONCAT(DISTINCT(r.rol)) AS grupo, GROUP_CONCAT(DISTINCT(c.aplicacion)) AS cartuchos " +
                    "FROM usuario u	" +
                    "LEFT JOIN usuario_rol ur ON (ur.usuario = u.id_usuario) " +
                    "LEFT JOIN roles r ON (r.id_rol = ur.id_rol) " +
                    "LEFT JOIN usuario_cartucho uc ON (u.id_usuario = uc.id_usuario) " +
                    "LEFT JOIN cartucho c ON (c.id_cartucho = uc.id_cartucho) " +
                    "WHERE u.id_usuario != 1 " +
                    "GROUP BY u.usuario ORDER BY u.usuario LIMIT ? OFFSET ?");
            ps.setInt(1, pagSize);
            ps.setInt(2, resultFrom);
            rs = ps.executeQuery();

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
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

        return userList;
    }

    public static EliminarUsuarioSistemaForm getDeleteUser(Connection c, Long idUser, EliminarUsuarioSistemaForm eliminarUsuarioSistemaForm) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT u.*, GROUP_CONCAT(DISTINCT(cc.nombre)) AS nombreCuentas " +
                    "FROM usuario u " +
                    "LEFT JOIN cuenta_cliente_usuario ccu ON (ccu.id_usuario = u.id_usuario) " +
                    "LEFT JOIN cuenta_cliente cc ON (ccu.id_cuenta = cc.id_cuenta) " +
                    "WHERE u.id_usuario = ? GROUP BY u.id_usuario;");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();
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
        } catch (SQLException e) {
            Logger.putLog("Exception: ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return eliminarUsuarioSistemaForm;
    }

    public static void deleteUser(Connection c, Long idUser) throws SQLException {
        PreparedStatement ps = null;
        try {
            // Borramos los datos del usuario
            ps = c.prepareStatement("DELETE FROM usuario WHERE id_usuario = ?");
            ps.setLong(1, idUser);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("Exception: ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }

    }

    public static boolean existUserWithKey(Connection c, String passwold, Long idUser) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT 1 from usuario where id_usuario = ? AND password = md5(?);");
            ps.setLong(1, idUser);
            ps.setString(2, passwold);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return false;
    }

    public static void updatePassword(Connection c, ModificarUsuarioPassForm modificarUsuarioPassForm, Long idUser) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("UPDATE usuario SET Password = md5(?) where id_usuario = ?;");
            ps.setString(1, modificarUsuarioPassForm.getPassword());
            ps.setLong(2, idUser);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, null);
        }

    }

    public static void getUserDatesToUpdate(Connection c, ModificarUsuarioSistemaForm modificarUsuarioSistemaForm, Long idUser, int userRolType) throws Exception {
        try {
            DatosForm dataForm = getUserData(c, idUser);
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

    public static void getUserDataToSee(Connection c, VerUsuarioSistemaForm verUsuarioSistemaForm, Long idUser) throws Exception {
        try {
            DatosForm dataForm = getUserData(c, idUser);
            verUsuarioSistemaForm.setNombre(dataForm.getNombre());
            verUsuarioSistemaForm.setUsuario(dataForm.getUsuario());
            verUsuarioSistemaForm.setApellidos(dataForm.getApellidos());
            verUsuarioSistemaForm.setDepartamento(dataForm.getDepartamento());
            verUsuarioSistemaForm.setEmail(dataForm.getEmail());

            verUsuarioSistemaForm.setCartucho(getUserCartridge(c, Long.valueOf(dataForm.getId())));
            verUsuarioSistemaForm.setTipos(getUserRoles(c, Long.valueOf(dataForm.getId())));
            verUsuarioSistemaForm.setNombreCuenta(getUserAccount(c, Long.valueOf(dataForm.getId())));
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static DatosForm getUserData(Connection c, Long idUser) throws Exception {

        DatosForm dataForm = new DatosForm();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT * FROM usuario u WHERE u.id_usuario = ?");
            ps.setLong(1, idUser);
            rs = ps.executeQuery();
            while (rs.next()) {
                dataForm.setNombre(rs.getString("Nombre").trim());
                dataForm.setUsuario(rs.getString("Usuario").trim());
                dataForm.setApellidos(rs.getString("Apellidos").trim());
                dataForm.setDepartamento(rs.getString("Departamento").trim());
                dataForm.setEmail(rs.getString("Email").trim());
                dataForm.setId(rs.getString("id_usuario"));
            }
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return dataForm;
    }

    public static DatosForm getUserData(Connection c, String user) throws Exception {

        DatosForm datosForm = new DatosForm();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = c.prepareStatement("SELECT * FROM usuario u WHERE u.usuario = ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            while (rs.next()) {
                datosForm.setNombre(rs.getString("Nombre").trim());
                datosForm.setUsuario(rs.getString("Usuario").trim());
                datosForm.setApellidos(rs.getString("Apellidos").trim());
                datosForm.setDepartamento(rs.getString("Departamento").trim());
                datosForm.setEmail(rs.getString("Email").trim());
                datosForm.setId(rs.getString("id_usuario"));
            }
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return datosForm;
    }

    public static boolean existUser(Connection c, String name) throws SQLException {
        //Comprobamos que ese nombre de usuario no existe en la BD
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT 1 FROM usuario WHERE usuario = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static void updateUser(Connection c, ModificarUsuarioSistemaForm modificarUsuarioSistemaForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c.setAutoCommit(false);

            // Borramos los roles antiguos
            ps = c.prepareStatement("DELETE FROM usuario_rol WHERE usuario = ?");
            ps.setLong(1, Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()));
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            // Borramos las cuentas de usuario antiguas
            ps = c.prepareStatement("DELETE FROM cuenta_cliente_usuario WHERE id_usuario = ?");
            ps.setLong(1, Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()));
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            // Borramos los cartuchos antiguos
            ps = c.prepareStatement("DELETE FROM usuario_cartucho WHERE id_usuario = ?");
            ps.setLong(1, Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()));
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            ps = c.prepareStatement("UPDATE usuario SET Usuario = ?, Nombre = ?, Apellidos = ?, Departamento = ?, Email = ? WHERE id_usuario = ?");
            ps.setString(1, modificarUsuarioSistemaForm.getNombre());
            ps.setString(2, modificarUsuarioSistemaForm.getNombre2());
            ps.setString(3, modificarUsuarioSistemaForm.getApellidos());
            ps.setString(4, modificarUsuarioSistemaForm.getDepartamento());
            ps.setString(5, modificarUsuarioSistemaForm.getEmail());
            ps.setLong(6, Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()));
            ps.executeUpdate();
            DAOUtils.closeQueries(ps, rs);

            // Insertamos los roles nuevos
            if (modificarUsuarioSistemaForm.getSelectedRoles() != null
                    && modificarUsuarioSistemaForm.getSelectedRoles().length > 0 && Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()) != 0) {
                ps = c.prepareStatement("INSERT INTO usuario_rol VALUES (?,?)");
                for (int i = 0; i < modificarUsuarioSistemaForm.getSelectedRoles().length; i++) {
                    ps.setLong(1, Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()));
                    ps.setInt(2, Integer.parseInt(modificarUsuarioSistemaForm.getSelectedRoles()[i]));
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            // Insertamos los cartuchos nuevos
            if (modificarUsuarioSistemaForm.getSelectedCartuchos() != null
                    && modificarUsuarioSistemaForm.getSelectedCartuchos().length > 0 && Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()) != 0) {
                ps = c.prepareStatement("INSERT INTO usuario_cartucho VALUES (?,?)");
                for (int i = 0; i < modificarUsuarioSistemaForm.getSelectedCartuchos().length; i++) {
                    ps.setLong(1, Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()));
                    ps.setInt(2, Integer.parseInt(modificarUsuarioSistemaForm.getSelectedCartuchos()[i]));
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            //Insertamos las cuenta cliente nuevas
            if (modificarUsuarioSistemaForm.getSelectedCuentaCliente() != null
                    && modificarUsuarioSistemaForm.getSelectedCuentaCliente().length > 0 && Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()) != 0) {
                ps = c.prepareStatement("INSERT INTO cuenta_cliente_usuario VALUES (?,?)");
                for (int i = 0; i < modificarUsuarioSistemaForm.getSelectedCuentaCliente().length; i++) {
                    ps.setInt(1, Integer.parseInt(modificarUsuarioSistemaForm.getSelectedCuentaCliente()[i]));
                    ps.setLong(2, Long.parseLong(modificarUsuarioSistemaForm.getIdUsuario()));
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            c.commit();
        } catch (Exception e) {
            Logger.putLog("Error al cerrar modificar el usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            try {
                c.rollback();
            } catch (Exception excep) {
                Logger.putLog("Error al volver al estado anterior de la base de datos", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }

    }

    public static void insertUser(Connection c, NuevoUsuarioSistemaForm nuevoUsuarioSistemaForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c.setAutoCommit(false);

            ps = c.prepareStatement("INSERT INTO usuario(Usuario, Password,  Nombre, Apellidos, Departamento, Email) VALUES (?, md5(?), ?, ?, ?, ?)");
            ps.setString(1, nuevoUsuarioSistemaForm.getNombre());
            ps.setString(2, nuevoUsuarioSistemaForm.getPassword());
            ps.setString(3, nuevoUsuarioSistemaForm.getNombre2());
            ps.setString(4, nuevoUsuarioSistemaForm.getApellidos());
            ps.setString(5, nuevoUsuarioSistemaForm.getDepartamento());
            ps.setString(6, nuevoUsuarioSistemaForm.getEmail());
            ps.executeUpdate();

            long idUser = 0;
            ps = c.prepareStatement("SELECT id_usuario FROM usuario WHERE usuario = ?");
            ps.setString(1, nuevoUsuarioSistemaForm.getNombre());
            rs = ps.executeQuery();
            if (rs.next()) {
                idUser = rs.getLong("id_usuario");
            }

            if (nuevoUsuarioSistemaForm.getObservatorio() != null &&
                    nuevoUsuarioSistemaForm.getObservatorio().length != 0 && idUser != 0) {
                for (int i = 0; i < nuevoUsuarioSistemaForm.getObservatorio().length; i++) {
                    ps = c.prepareStatement("INSERT INTO observatorio_usuario VALUES (?,?)");
                    ps.setInt(1, Integer.parseInt(nuevoUsuarioSistemaForm.getObservatorio()[i]));
                    ps.setLong(2, idUser);
                    ps.executeUpdate();
                }
                PropertiesManager pmgr = new PropertiesManager();
                String[] roles = new String[]{
                        pmgr.getValue(CRAWLER_PROPERTIES, "role.observatory.id")
                };
                nuevoUsuarioSistemaForm.setSelectedRoles(roles);
            }

            if (nuevoUsuarioSistemaForm.getSelectedRoles() != null
                    && nuevoUsuarioSistemaForm.getSelectedRoles().length > 0 && idUser != 0) {
                ps = c.prepareStatement("INSERT INTO usuario_rol VALUES (?,?)");
                for (int i = 0; i < nuevoUsuarioSistemaForm.getSelectedRoles().length; i++) {
                    ps.setLong(1, idUser);
                    ps.setInt(2, Integer.parseInt(nuevoUsuarioSistemaForm.getSelectedRoles()[i]));
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            if (nuevoUsuarioSistemaForm.getSelectedCartuchos() != null
                    && nuevoUsuarioSistemaForm.getSelectedCartuchos().length > 0 && idUser != 0) {
                ps = c.prepareStatement("INSERT INTO usuario_cartucho VALUES (?,?)");
                for (int i = 0; i < nuevoUsuarioSistemaForm.getSelectedCartuchos().length; i++) {
                    ps.setInt(2, Integer.parseInt(nuevoUsuarioSistemaForm.getSelectedCartuchos()[i]));
                    ps.setLong(1, idUser);
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            if (nuevoUsuarioSistemaForm.getCuenta_cliente() != null &&
                    nuevoUsuarioSistemaForm.getCuenta_cliente().length != 0 && idUser != 0) {
                for (int i = 0; i < nuevoUsuarioSistemaForm.getCuenta_cliente().length; i++) {
                    ps = c.prepareStatement("INSERT INTO cuenta_cliente_usuario VALUES (?,?)");
                    ps.setInt(1, Integer.parseInt(nuevoUsuarioSistemaForm.getCuenta_cliente()[i]));
                    ps.setLong(2, idUser);
                    ps.executeUpdate();
                }
            }

            c.commit();
        } catch (Exception e) {
            Logger.putLog("Error al insertar los datos del usuario", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            try {
                c.rollback();
            } catch (Exception excep) {
                Logger.putLog("Error al volver al estado anterior de la base de datos", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
    }

    public static RedireccionConfigForm loadUserData(Connection c, String user, RedireccionConfigForm redireccionConfigForm) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT id_cartucho, password, tipo FROM usuario WHERE usuario = ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            while (rs.next()) {
                redireccionConfigForm.setId_cartucho(rs.getInt(1));
                redireccionConfigForm.setPass(EncryptUtils.encrypt(rs.getString(2)));
                redireccionConfigForm.setTipo(rs.getString(3));
            }
            redireccionConfigForm.setNumCartuchos(1);
            redireccionConfigForm.setSeleccionados(1);
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return redireccionConfigForm;
    }

    private static List<String> listFromString(String roleString) {
        final List<String> roleList = new ArrayList<String>();
        if (roleString != null) {
            final StringTokenizer tokenizer = new StringTokenizer(roleString, ",");
            while (tokenizer.hasMoreTokens()) {
                roleList.add(tokenizer.nextToken());
            }
        }

        return roleList;
    }

    public static List<DatosForm> getClientAccount(Connection c, Long idAccount) throws SQLException {
        List<DatosForm> results = new ArrayList<DatosForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT DISTINCT u.id_usuario, u.nombre, u.usuario, u.apellidos, u.departamento, u.email FROM usuario u " +
                    "INNER JOIN usuario_rol ur ON (ur.usuario = u.id_usuario) " +
                    "INNER JOIN cuenta_cliente_usuario ccu ON (ccu.id_usuario = u.id_usuario) " +
                    "INNER JOIN cuenta_cliente cc ON (cc.id_cuenta = ccu.id_cuenta) " +
                    "WHERE cc.id_cuenta = ?;");
            ps.setLong(1, idAccount);
            rs = ps.executeQuery();
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
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return results;
    }

    public static List<String> getMailsByRole(Connection c, Long idRole) throws SQLException {
        List<String> mails = new ArrayList<String>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT email FROM usuario u " +
                    "LEFT JOIN usuario_rol ur ON (u.id_usuario = ur.usuario) " +
                    "WHERE ur.id_rol = ?;");
            ps.setLong(1, idRole);

            rs = ps.executeQuery();

            while (rs.next()) {
                mails.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DAOUtils.closeQueries(ps, rs);
        }
        return mails;
    }

}
