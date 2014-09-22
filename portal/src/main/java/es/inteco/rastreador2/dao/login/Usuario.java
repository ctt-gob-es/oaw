package es.inteco.rastreador2.dao.login;

import java.util.List;

public class Usuario {
    private Long id;
    private String login;
    private List<String> tipos;
    private String email;
    private String name;
    private String surname;
    private String department;
    private List<String> cartridge;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getCartridge() {
        return cartridge;
    }

    public void setCartridge(List<String> cartridge) {
        this.cartridge = cartridge;
    }

    public List<String> getTipos() {
        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
