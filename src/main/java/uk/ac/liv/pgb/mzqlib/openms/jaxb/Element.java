
//
//This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
//See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
//Any modifications to this file will be lost upon recompilation of the source schema. 
//Generated on: 2014.03.18 at 02:59:01 PM GMT 
//
package uk.ac.liv.pgb.mzqlib.openms.jaxb;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;attribute name="map" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" /&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="rt" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="mz" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="it" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="charge" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "element")
public class Element {

    /**
     *
     */
    @XmlAttribute(
        name     = "map",
        required = true
    )
    @XmlSchemaType(name = "unsignedInt")
    protected long map;

    /**
     *
     */
    @XmlAttribute(
        name     = "id",
        required = true
    )
    protected String id;

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
     *
     */
    @XmlAttribute(name = "charge")
    protected BigInteger charge;

    /**
     * Gets the value of the charge property.
     *
     * @return
     *         possible object is
     *         {@link BigInteger }
     *
     */
    public BigInteger getCharge() {
        return charge;
    }

    /**
     * Sets the value of the charge property.
     *
     * @param value
     *              allowed object is
     *              {@link BigInteger }
     *
     */
    public void setCharge(BigInteger value) {
        this.charge = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *         possible object is
     *         {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *              allowed object is
     *              {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

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
     * Gets the value of the map property.
     *
     * @return value of map.
     */
    public long getMap() {
        return map;
    }

    /**
     * Sets the value of the map property.
     *
     * @param value value of map.
     */
    public void setMap(long value) {
        this.map = value;
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
