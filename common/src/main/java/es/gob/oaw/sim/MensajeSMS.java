
package es.gob.oaw.sim;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MensajeSMS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MensajeSMS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Cuerpo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DestinatariosSMS" type="{http://misim.redsara.es/misim-bus-webapp/peticion}DestinatariosSMS"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MensajeSMS", propOrder = {
    "cuerpo",
    "destinatariosSMS"
})
public class MensajeSMS {

    /** The cuerpo. */
    @XmlElement(name = "Cuerpo", required = true)
    protected String cuerpo;
    
    /** The destinatarios SMS. */
    @XmlElement(name = "DestinatariosSMS", required = true)
    protected DestinatariosSMS destinatariosSMS;

    /**
     * Gets the value of the cuerpo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuerpo() {
        return cuerpo;
    }

    /**
     * Sets the value of the cuerpo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuerpo(String value) {
        this.cuerpo = value;
    }

    /**
     * Gets the value of the destinatariosSMS property.
     * 
     * @return
     *     possible object is
     *     {@link DestinatariosSMS }
     *     
     */
    public DestinatariosSMS getDestinatariosSMS() {
        return destinatariosSMS;
    }

    /**
     * Sets the value of the destinatariosSMS property.
     * 
     * @param value
     *     allowed object is
     *     {@link DestinatariosSMS }
     *     
     */
    public void setDestinatariosSMS(DestinatariosSMS value) {
        this.destinatariosSMS = value;
    }

}
