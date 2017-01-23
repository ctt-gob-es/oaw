
package es.ctic.sim;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DestinatariosSMS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DestinatariosSMS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DestinatarioSMS" type="{http://misim.redsara.es/misim-bus-webapp/peticion}DestinatarioSMS" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DestinatariosSMS", propOrder = {
    "destinatarioSMS"
})
public class DestinatariosSMS {

    @XmlElement(name = "DestinatarioSMS", required = true)
    protected List<DestinatarioSMS> destinatarioSMS;

    /**
     * Gets the value of the destinatarioSMS property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinatarioSMS property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinatarioSMS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DestinatarioSMS }
     * 
     * 
     */
    public List<DestinatarioSMS> getDestinatarioSMS() {
        if (destinatarioSMS == null) {
            destinatarioSMS = new ArrayList<DestinatarioSMS>();
        }
        return this.destinatarioSMS;
    }

}
