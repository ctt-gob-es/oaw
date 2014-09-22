package es.inteco.crawler.sexista.core.dto.impl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Clase base para los Dtos.
 *
 * @author GPM
 * @version 1.0
 */
public abstract class BaseDto {

    /**
     * Indicates whether some other object is "equal to" this one.
     * The equals method implements an equivalence relation on non-null object references:
     * - It is reflexive: for any non-null reference value x, x.equals(x) should return true.
     * - It is symmetric: for any non-null reference values x and y, x.equals(y) should return
     * true if and only if y.equals(x) returns true.
     * - It is transitive: for any non-null reference values x, y, and z, if x.equals(y) returns
     * true and y.equals(z) returns true, then x.equals(z) should return true.
     * - It is consistent: for any non-null reference values x and y, multiple invocations of
     * x.equals(y) consistently return true or consistently return false, provided no information
     * used in equals comparisons on the objects is modified.
     * - For any non-null reference value x, x.equals(null) should return false.
     *
     * @param other the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object other) {
        //Si es la misma instancia --> son iguales
        if (other == this) {
            return true;
        }
        //Por definicion, si el otro objeto es null --> son distintos
        if (other == null) {
            return false;
        }
        //Si los objetos son de distinta clase también son distintos.
        //Esto implica que no puede haber igualdad entre clases/subclases!!
        if (this.getClass() != other.getClass()) {
            return false;
        }

        //realizar una comparación basada en el contenido de todos los campos
        return EqualsBuilder.reflectionEquals(this, other);
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hashtables
     * such as those provided by java.util.Hashtable.
     * <p/>
     * The general contract of hashCode is:
     * -  Whenever it is invoked on the same object more than once during an execution of a Java application,
     * the hashCode method must consistently return the same integer, provided no information used in equals
     * comparisons on the object is modified. This integer need not remain consistent from one execution of
     * an application to another execution of the same application.
     * <p/>
     * -  If two objects are equal according to the equals(Object) method, then calling the hashCode method
     * on each of the two objects must produce the same integer result.
     * <p/>
     * -  It is not required that if two objects are unequal according to the equals(java.lang.Object)
     * method, then calling the hashCode method on each of the two objects must produce distinct integer
     * results. However, the programmer should be aware that producing distinct integer results for unequal
     * objects may improve the performance of hashtables.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        //Generar el hash code basandose en el contenido de los campos.
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Método toString.
     *
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
