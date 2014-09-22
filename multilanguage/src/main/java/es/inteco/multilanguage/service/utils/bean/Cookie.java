package es.inteco.multilanguage.service.utils.bean;

public class Cookie {
    private String name;
    private String value;

    public Cookie(String cookie) {
        if (cookie.contains("=")) {
            this.name = cookie.substring(0, cookie.indexOf('='));

            if (cookie.contains(";")) {
                this.value = cookie.substring(cookie.indexOf('=') + 1, cookie.indexOf(';'));
            } else {
                this.value = cookie.substring(cookie.indexOf('=') + 1, cookie.length());
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name + "=" + this.value + ";";
    }
}
