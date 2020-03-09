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
