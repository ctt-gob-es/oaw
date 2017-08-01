package es.inteco.rastreador2.actionform.semillas;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;


public class NuevaSemillaIpForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;

    private String ipInicial1, ipInicial2, ipInicial3, ipInicial4, ipFinal1, ipFinal2, ipFinal3, ipFinal4;
    private String puerto1, puerto2, puerto3;
    private String nombreSemilla;
    private CategoriaForm categoria;

    public String getNombreSemilla() {
        return nombreSemilla;
    }

    public void setNombreSemilla(String nombreSemilla) {
        this.nombreSemilla = nombreSemilla;
    }

    public String getIpInicial1() {
        return ipInicial1;
    }

    public void setIpInicial1(String ipInicial1) {
        this.ipInicial1 = ipInicial1;
    }

    public String getIpInicial2() {
        return ipInicial2;
    }

    public void setIpInicial2(String ipInicial2) {
        this.ipInicial2 = ipInicial2;
    }

    public String getIpInicial3() {
        return ipInicial3;
    }

    public void setIpInicial3(String ipInicial3) {
        this.ipInicial3 = ipInicial3;
    }

    public String getIpFinal1() {
        return ipFinal1;
    }

    public void setIpFinal1(String ipFinal1) {
        this.ipFinal1 = ipFinal1;
    }

    public String getIpFinal2() {
        return ipFinal2;
    }

    public void setIpFinal2(String ipFinal2) {
        this.ipFinal2 = ipFinal2;
    }

    public String getIpFinal3() {
        return ipFinal3;
    }

    public void setIpFinal3(String ipFinal3) {
        this.ipFinal3 = ipFinal3;
    }

    public String getPuerto1() {
        return puerto1;
    }

    public void setPuerto1(String puerto1) {
        this.puerto1 = puerto1;
    }

    public String getPuerto2() {
        return puerto2;
    }

    public void setPuerto2(String puerto2) {
        this.puerto2 = puerto2;
    }

    public String getPuerto3() {
        return puerto3;
    }

    public void setPuerto3(String puerto3) {
        this.puerto3 = puerto3;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIpInicial4() {
        return ipInicial4;
    }

    public void setIpInicial4(String ipInicial4) {
        this.ipInicial4 = ipInicial4;
    }

    public String getIpFinal4() {
        return ipFinal4;
    }

    public void setIpFinal4(String ipFinal4) {
        this.ipFinal4 = ipFinal4;
    }

    public CategoriaForm getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaForm categoria) {
        this.categoria = categoria;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        if (categoria == null) {
            categoria = new CategoriaForm();
        }
    }
}