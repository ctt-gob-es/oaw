
package es.ctic.mail.sim;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MensajePush complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MensajePush">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Titulo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Cuerpo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Icono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sonido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DestinatariosPush" type="{http://misim.redsara.es/misim-bus-webapp/peticion}DestinatariosPush" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MensajePush", propOrder = {
    "titulo",
    "cuerpo",
    "icono",
    "sonido",
    "destinatariosPush"
})
public class MensajePush {

    @XmlElement(name = "Titulo", required = true)
    protected String titulo;
    @XmlElement(name = "Cuerpo", required = true)
    protected String cuerpo;
    @XmlElement(name = "Icono")
    protected String icono;
    @XmlElement(name = "Sonido")
    protected String sonido;
    @XmlElement(name = "DestinatariosPush")
    protected DestinatariosPush destinatariosPush;

    /**
     * Gets the value of the titulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets the value of the titulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
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
     * Gets the value of the icono property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcono() {
        return icono;
    }

    /**
     * Sets the value of the icono property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcono(String value) {
        this.icono = value;
    }

    /**
     * Gets the value of the sonido property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSonido() {
        return sonido;
    }

    /**
     * Sets the value of the sonido property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSonido(String value) {
        this.sonido = value;
    }

    /**
     * Gets the value of the destinatariosPush property.
     * 
     * @return
     *     possible object is
     *     {@link DestinatariosPush }
     *     
     */
    public DestinatariosPush getDestinatariosPush() {
        return destinatariosPush;
    }

    /**
     * Sets the value of the destinatariosPush property.
     * 
     * @param value
     *     allowed object is
     *     {@link DestinatariosPush }
     *     
     */
    public void setDestinatariosPush(DestinatariosPush value) {
        this.destinatariosPush = value;
    }

}
