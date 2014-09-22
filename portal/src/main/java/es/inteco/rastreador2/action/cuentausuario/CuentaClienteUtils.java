package es.inteco.rastreador2.action.cuentausuario;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.VerCuentaUsuarioForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;

import java.sql.Connection;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class CuentaClienteUtils {

    private CuentaClienteUtils() {
    }

    public static VerCuentaUsuarioForm getCuentaUsuarioForm(long idCuenta) throws Exception {
        VerCuentaUsuarioForm verCuentaClienteForm = new VerCuentaUsuarioForm();
        PropertiesManager pmgr = new PropertiesManager();
        Connection c = null;
        Connection con = null;
        try {
            c = DataBaseManager.getConnection();
            con = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));
            CuentaUsuarioDAO.getDatosUsuarioVer(c, verCuentaClienteForm, idCuenta);

            verCuentaClienteForm.setNormaAnalisisSt(RastreoDAO.getNombreNorma(con, verCuentaClienteForm.getNormaAnalisis()));

        } catch (Exception e) {
            Logger.putLog("Exception", CuentaClienteUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw new Exception(e);
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(con);
        }

        return verCuentaClienteForm;
    }

}
