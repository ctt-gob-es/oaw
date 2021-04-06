
package es.gob.oaw.sim;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Mensajes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Mensajes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MensajeSMS" type="{http://misim.redsara.es/misim-bus-webapp/peticion}MensajeSMS" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MensajeEmail" type="{http://misim.redsara.es/misim-bus-webapp/peticion}MensajeEmail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MensajePush" type="{http://misim.redsara.es/misim-bus-webapp/peticion}MensajePush" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Mensajes", propOrder = {
    "mensajeSMS",
    "mensajeEmail",
    "mensajePush"
})
public class Mensajes {

    /** The mensaje SMS. */
    @XmlElement(name = "MensajeSMS")
    protected List<MensajeSMS> mensajeSMS;
    
    /** The mensaje email. */
    @XmlElement(name = "MensajeEmail")
    protected List<MensajeEmail> mensajeEmail;
    
    /** The mensaje push. */
    @XmlElement(name = "MensajePush")
    protected List<MensajePush> mensajePush;

    /**
	 * Gets the value of the mensajeSMS property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is
	 * not a <CODE>set</CODE> method for the mensajeSMS property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMensajeSMS().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link MensajeSMS }
	 *
	 * @return the mensaje SMS
	 */
    public List<MensajeSMS> getMensajeSMS() {
        if (mensajeSMS == null) {
            mensajeSMS = new ArrayList<MensajeSMS>();
        }
        return this.mensajeSMS;
    }

    /**
	 * Gets the value of the mensajeEmail property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is
	 * not a <CODE>set</CODE> method for the mensajeEmail property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMensajeEmail().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link MensajeEmail }
	 *
	 * @return the mensaje email
	 */
    public List<MensajeEmail> getMensajeEmail() {
        if (mensajeEmail == null) {
            mensajeEmail = new ArrayList<MensajeEmail>();
        }
        return this.mensajeEmail;
    }

    /**
	 * Gets the value of the mensajePush property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is
	 * not a <CODE>set</CODE> method for the mensajePush property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMensajePush().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link MensajePush }
	 *
	 * @return the mensaje push
	 */
    public List<MensajePush> getMensajePush() {
        if (mensajePush == null) {
            mensajePush = new ArrayList<MensajePush>();
        }
        return this.mensajePush;
    }

}
