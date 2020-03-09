
package es.gob.oaw.sim;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mensajesRespuesta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mensajesRespuesta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Mensaje" type="{http://misim.redsara.es/misim-bus-webapp/respuesta}mensajeRespuesta"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mensajesRespuesta", namespace = "http://misim.redsara.es/misim-bus-webapp/respuesta", propOrder = {
    "mensaje"
})
public class MensajesRespuesta {

    @XmlElement(name = "Mensaje", required = true)
    protected MensajeRespuesta mensaje;

    /**
     * Gets the value of the mensaje property.
     * 
     * @return
     *     possible object is
     *     {@link MensajeRespuesta }
     *     
     */
    public MensajeRespuesta getMensaje() {
        return mensaje;
    }

    /**
     * Sets the value of the mensaje property.
     * 
     * @param value
     *     allowed object is
     *     {@link MensajeRespuesta }
     *     
     */
    public void setMensaje(MensajeRespuesta value) {
        this.mensaje = value;
    }

}
