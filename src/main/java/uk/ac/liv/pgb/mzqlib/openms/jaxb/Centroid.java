
//
//This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
//See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
//Any modifications to this file will be lost upon recompilation of the source schema. 
//Generated on: 2014.03.18 at 02:59:01 PM GMT 
//
package uk.ac.liv.pgb.mzqlib.openms.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="rt" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="mz" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="it" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "centroid")
public class Centroid {

    /**
     *
     */
    @XmlAttribute(
        name     = "rt",
        required = true
    )
    protected double rt;

    /**
     *
     */
    @XmlAttribute(
        name     = "mz",
        required = true
    )
    protected double mz;

    /**
     *
     */
    @XmlAttribute(
        name     = "it",
        required = true
    )
    protected double it;

    /**
     * Gets the value of the it property.
     *
     * @return it value.
     */
    public double getIt() {
        return it;
    }

    /**
     * Sets the value of the it property.
     *
     * @param value it value.
     */
    public void setIt(double value) {
        this.it = value;
    }

    /**
     * Gets the value of the mz property.
     *
     * @return mz value.
     */
    public double getMz() {
        return mz;
    }

    /**
     * Sets the value of the mz property.
     *
     * @param value mz value.
     */
    public void setMz(double value) {
        this.mz = value;
    }

    /**
     * Gets the value of the rt property.
     *
     * @return rt value.
     */
    public double getRt() {
        return rt;
    }

    /**
     * Sets the value of the rt property.
     *
     * @param value rt value.
     */
    public void setRt(double value) {
        this.rt = value;
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
