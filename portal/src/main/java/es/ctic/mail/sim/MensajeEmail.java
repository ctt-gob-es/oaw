
package es.ctic.mail.sim;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MensajeEmail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MensajeEmail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Asunto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Cuerpo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1000000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Origen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Modo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Adjuntos" type="{http://misim.redsara.es/misim-bus-webapp/peticion}Adjuntos" minOccurs="0"/>
 *         &lt;element name="DestinatariosMail" type="{http://misim.redsara.es/misim-bus-webapp/peticion}DestinatariosMail"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MensajeEmail", propOrder = {
    "asunto",
    "cuerpo",
    "origen",
    "modo",
    "adjuntos",
    "destinatariosMail"
})
public class MensajeEmail {

    @XmlElement(name = "Asunto", required = true)
    protected String asunto;
    @XmlElement(name = "Cuerpo", required = true)
    protected String cuerpo;
    @XmlElement(name = "Origen")
    protected String origen;
    @XmlElement(name = "Modo")
    protected String modo;
    @XmlElement(name = "Adjuntos")
    protected Adjuntos adjuntos;
    @XmlElement(name = "DestinatariosMail", required = true)
    protected DestinatariosMail destinatariosMail;

    /**
     * Gets the value of the asunto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Sets the value of the asunto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsunto(String value) {
        this.asunto = value;
    }

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
     * Gets the value of the origen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Sets the value of the origen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigen(String value) {
        this.origen = value;
    }

    /**
     * Gets the value of the modo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModo() {
        return modo;
    }

    /**
     * Sets the value of the modo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModo(String value) {
        this.modo = value;
    }

    /**
     * Gets the value of the adjuntos property.
     * 
     * @return
     *     possible object is
     *     {@link Adjuntos }
     *     
     */
    public Adjuntos getAdjuntos() {
        return adjuntos;
    }

    /**
     * Sets the value of the adjuntos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Adjuntos }
     *     
     */
    public void setAdjuntos(Adjuntos value) {
        this.adjuntos = value;
    }

    /**
     * Gets the value of the destinatariosMail property.
     * 
     * @return
     *     possible object is
     *     {@link DestinatariosMail }
     *     
     */
    public DestinatariosMail getDestinatariosMail() {
        return destinatariosMail;
    }

    /**
     * Sets the value of the destinatariosMail property.
     * 
     * @param value
     *     allowed object is
     *     {@link DestinatariosMail }
     *     
     */
    public void setDestinatariosMail(DestinatariosMail value) {
        this.destinatariosMail = value;
    }

}
