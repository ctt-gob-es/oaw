
package es.gob.oaw.sim;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DestinatariosPush complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DestinatariosPush">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DestinatarioPush" type="{http://misim.redsara.es/misim-bus-webapp/peticion}DestinatarioPush" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DestinatariosPush", propOrder = {
    "destinatarioPush"
})
public class DestinatariosPush {

    @XmlElement(name = "DestinatarioPush", required = true)
    protected List<DestinatarioPush> destinatarioPush;

    /**
     * Gets the value of the destinatarioPush property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinatarioPush property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinatarioPush().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DestinatarioPush }
     * 
     * 
     */
    public List<DestinatarioPush> getDestinatarioPush() {
        if (destinatarioPush == null) {
            destinatarioPush = new ArrayList<DestinatarioPush>();
        }
        return this.destinatarioPush;
    }

}
