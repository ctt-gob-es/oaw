
package es.ctic.mail.sim;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "EnvioMensajesService", targetNamespace = "http://misim.redsara.es/misim-bus-webapp/", wsdlLocation = "https://pre-misim.redsara.es/misim-bus-webapp/EnvioMensajesService?wsdl")
public class EnvioMensajesService
    extends Service
{

    private static final URL ENVIOMENSAJESSERVICE_WSDL_LOCATION;
    private static final WebServiceException ENVIOMENSAJESSERVICE_EXCEPTION;
    private static final QName ENVIOMENSAJESSERVICE_QNAME = new QName("http://misim.redsara.es/misim-bus-webapp/", "EnvioMensajesService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://pre-misim.redsara.es/misim-bus-webapp/EnvioMensajesService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        ENVIOMENSAJESSERVICE_WSDL_LOCATION = url;
        ENVIOMENSAJESSERVICE_EXCEPTION = e;
    }

    public EnvioMensajesService() {
        super(internalGetWsdlLocation(), ENVIOMENSAJESSERVICE_QNAME);
    }

    public EnvioMensajesService(WebServiceFeature... features) {
        super(internalGetWsdlLocation(), ENVIOMENSAJESSERVICE_QNAME, features);
    }

    public EnvioMensajesService(URL wsdlLocation) {
        super(wsdlLocation, ENVIOMENSAJESSERVICE_QNAME);
    }

    public EnvioMensajesService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, ENVIOMENSAJESSERVICE_QNAME, features);
    }

    public EnvioMensajesService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EnvioMensajesService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns EnvioMensajesServiceWSBindingPortType
     */
    @WebEndpoint(name = "EnvioMensajesServicePort")
    public EnvioMensajesServiceWSBindingPortType getEnvioMensajesServicePort() {
        return super.getPort(new QName("http://misim.redsara.es/misim-bus-webapp/", "EnvioMensajesServicePort"), EnvioMensajesServiceWSBindingPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns EnvioMensajesServiceWSBindingPortType
     */
    @WebEndpoint(name = "EnvioMensajesServicePort")
    public EnvioMensajesServiceWSBindingPortType getEnvioMensajesServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://misim.redsara.es/misim-bus-webapp/", "EnvioMensajesServicePort"), EnvioMensajesServiceWSBindingPortType.class, features);
    }

    private static URL internalGetWsdlLocation() {
        if (ENVIOMENSAJESSERVICE_EXCEPTION!= null) {
            throw ENVIOMENSAJESSERVICE_EXCEPTION;
        }
        return ENVIOMENSAJESSERVICE_WSDL_LOCATION;
    }

}
