
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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * Type-Name-Value type for annotations
 *
 * <p>
 * Java class for userParam complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="userParam"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="type" use="required" type="{}UserParamType" /&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userParam")
@XmlSeeAlso({uk.ac.liv.pgb.mzqlib.openms.jaxb.ConsensusXML.DataProcessing.UserParam.class,
             uk.ac.liv.pgb.mzqlib.openms.jaxb.FeatureMap.DataProcessing.UserParam.class})
public class UserParam {

    /**
     *
     */
    @XmlAttribute(
        name     = "type",
        required = true
    )
    protected UserParamType type;

    /**
     *
     */
    @XmlAttribute(
        name     = "name",
        required = true
    )
    protected String name;

    /**
     *
     */
    @XmlAttribute(
        name     = "value",
        required = true
    )
    @XmlSchemaType(name = "anySimpleType")
    protected String value;

    /**
     * Gets the value of the name property.
     *
     * @return
     *         possible object is
     *         {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *              allowed object is
     *              {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return
     *         possible object is
     *         {@link UserParamType }
     *
     */
    public UserParamType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *              allowed object is
     *              {@link UserParamType }
     *
     */
    public void setType(UserParamType value) {
        this.type = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return
     *         possible object is
     *         {@link String }
     *
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *              allowed object is
     *              {@link String }
     *
     */
    public void setValue(String value) {
        this.value = value;
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
