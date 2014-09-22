package es.inteco.rastreador2.lenox.dto;

/**
 * Dto de Usuario.
 *
 * @author psanchez
 */
public class UsuarioDto extends BaseDto {

    /**
     * Campo login.
     */
    private String login;

    /**
     * Campo password.
     */
    private String password;


    /**
     * Obtiene el valor del campo login.<br>
     *
     * @return el campo login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Obtiene el valor del campo password.<br>
     *
     * @return el campo password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Fija el valor para el campo login.<br>
     *
     * @param login el vlor de login a fijar
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Fija el valor para el campo password.<br>
     *
     * @param password el vlor de password a fijar
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
