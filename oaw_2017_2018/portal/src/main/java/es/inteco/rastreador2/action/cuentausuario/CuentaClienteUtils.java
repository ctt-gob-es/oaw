package es.inteco.rastreador2.action.cuentausuario;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.VerCuentaUsuarioForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;

import java.sql.Connection;

public final class CuentaClienteUtils {

    private CuentaClienteUtils() {
    }

    public static VerCuentaUsuarioForm getCuentaUsuarioForm(long idCuenta) throws Exception {
        VerCuentaUsuarioForm verCuentaClienteForm = new VerCuentaUsuarioForm();
        try (Connection c = DataBaseManager.getConnection()) {
            CuentaUsuarioDAO.getDatosUsuarioVer(c, verCuentaClienteForm, idCuenta);

            verCuentaClienteForm.setNormaAnalisisSt(RastreoDAO.getNombreNorma(c, verCuentaClienteForm.getNormaAnalisis()));
        } catch (Exception e) {
            Logger.putLog("Exception", CuentaClienteUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return verCuentaClienteForm;
    }

}
