//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.03.18 at 02:59:01 PM GMT 
//

package uk.ac.liv.pgb.mzqlib.openms.jaxb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}centroid"/>
 *         &lt;element ref="{}groupedElementList"/>
 *         &lt;element name="PeptideIdentification" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="PeptideHit" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence minOccurs="0">
 *                             &lt;element name="userParam" type="{}userParam" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="sequence" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="charge" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                           &lt;attribute name="score" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *                           &lt;attribute name="aa_before">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="0"/>
 *                                 &lt;maxLength value="1"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="aa_after">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="0"/>
 *                                 &lt;maxLength value="1"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="protein_refs" type="{http://www.w3.org/2001/XMLSchema}IDREFS" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="userParam" type="{}userParam" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="identification_run_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *                 &lt;attribute name="score_type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="higher_score_better" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;attribute name="significance_threshold" type="{http://www.w3.org/2001/XMLSchema}float" />
 *                 &lt;attribute name="spectrum_reference" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="RT" type="{http://www.w3.org/2001/XMLSchema}float" />
 *                 &lt;attribute name="MZ" type="{http://www.w3.org/2001/XMLSchema}float" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="userParam" type="{}userParam" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="quality" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="charge" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "centroid",
    "groupedElementList",
    "peptideIdentification",
    "userParam"
})
@XmlRootElement(name = "consensusElement")
public class ConsensusElement {

    /**
     *
     */
    @XmlElement(required = true)
    protected Centroid centroid;

    /**
     *
     */
    @XmlElement(required = true)
    protected GroupedElementList groupedElementList;

    /**
     *
     */
    @XmlElement(name = "PeptideIdentification")
    protected List<ConsensusElement.PeptideIdentification> peptideIdentification;

    /**
     *
     */
    protected List<UserParam> userParam;

    /**
     *
     */
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     *
     */
    @XmlAttribute(name = "quality")
    protected Double quality;

    /**
     *
     */
    @XmlAttribute(name = "charge")
    protected BigInteger charge;

    /**
     * Gets the value of the centroid property.
     *
     * @return
     *         possible object is
     *         {@link Centroid }
     *
     */
    public Centroid getCentroid() {
        return centroid;
    }

    /**
     * Sets the value of the centroid property.
     *
     * @param value
     *              allowed object is
     *              {@link Centroid }
     *
     */
    public void setCentroid(Centroid value) {
        this.centroid = value;
    }

    /**
     * Gets the value of the groupedElementList property.
     *
     * @return
     *         possible object is
     *         {@link GroupedElementList }
     *
     */
    public GroupedElementList getGroupedElementList() {
        return groupedElementList;
    }

    /**
     * Sets the value of the groupedElementList property.
     *
     * @param value
     *              allowed object is
     *              {@link GroupedElementList }
     *
     */
    public void setGroupedElementList(GroupedElementList value) {
        this.groupedElementList = value;
    }

    /**
     * Gets the value of the peptideIdentification property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the
     * peptideIdentification property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPeptideIdentification().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConsensusElement.PeptideIdentification }
     *
     *
     * @return PeptideIdentification list.
     */
    public List<ConsensusElement.PeptideIdentification> getPeptideIdentification() {
        if (peptideIdentification == null) {
            peptideIdentification = new ArrayList<>();
        }
        return this.peptideIdentification;
    }

    /**
     * Gets the value of the userParam property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userParam
     * property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserParam().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserParam }
     *
     *
     * @return UserParam list.
     */
    public List<UserParam> getUserParam() {
        if (userParam == null) {
            userParam = new ArrayList<>();
        }
        return this.userParam;
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
     * Gets the value of the quality property.
     *
     * @return
     *         possible object is
     *         {@link Double }
     *
     */
    public Double getQuality() {
        return quality;
    }

    /**
     * Sets the value of the quality property.
     *
     * @param value
     *              allowed object is
     *              {@link Double }
     *
     */
    public void setQuality(Double value) {
        this.quality = value;
    }

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
     * <p>
     * Java class for anonymous complex type.
     *
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="PeptideHit" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence minOccurs="0">
     *                   &lt;element name="userParam" type="{}userParam" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="sequence" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="charge" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                 &lt;attribute name="score" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
     *                 &lt;attribute name="aa_before">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="0"/>
     *                       &lt;maxLength value="1"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="aa_after">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="0"/>
     *                       &lt;maxLength value="1"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="protein_refs" type="{http://www.w3.org/2001/XMLSchema}IDREFS" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="userParam" type="{}userParam" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="identification_run_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
     *       &lt;attribute name="score_type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="higher_score_better" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *       &lt;attribute name="significance_threshold" type="{http://www.w3.org/2001/XMLSchema}float" />
     *       &lt;attribute name="spectrum_reference" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="RT" type="{http://www.w3.org/2001/XMLSchema}float" />
     *       &lt;attribute name="MZ" type="{http://www.w3.org/2001/XMLSchema}float" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "peptideHit",
        "userParam"
    })
    public static class PeptideIdentification {

        /**
         *
         */
        @XmlElement(name = "PeptideHit")
        protected List<ConsensusElement.PeptideIdentification.PeptideHit> peptideHit;

        /**
         *
         */
        protected List<UserParam> userParam;

        /**
         *
         */
        @XmlAttribute(name = "identification_run_ref", required = true)
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
        protected Object identificationRunRef;

        /**
         *
         */
        @XmlAttribute(name = "score_type", required = true)
        protected String scoreType;

        /**
         *
         */
        @XmlAttribute(name = "higher_score_better", required = true)
        protected boolean higherScoreBetter;

        /**
         *
         */
        @XmlAttribute(name = "significance_threshold")
        protected Float significanceThreshold;

        /**
         *
         */
        @XmlAttribute(name = "spectrum_reference")
        @XmlSchemaType(name = "unsignedInt")
        protected Long spectrumReference;

        /**
         *
         */
        @XmlAttribute(name = "RT")
        protected Float rt;

        /**
         *
         */
        @XmlAttribute(name = "MZ")
        protected Float mz;

        /**
         * Gets the value of the peptideHit property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the peptideHit
         * property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPeptideHit().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ConsensusElement.PeptideIdentification.PeptideHit }
         *
         *
         * @return PeptideHit list.
         */
        public List<ConsensusElement.PeptideIdentification.PeptideHit> getPeptideHit() {
            if (peptideHit == null) {
                peptideHit = new ArrayList<>();
            }
            return this.peptideHit;
        }

        /**
         * Gets the value of the userParam property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the userParam
         * property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUserParam().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link UserParam }
         *
         *
         * @return UserParam list.
         */
        public List<UserParam> getUserParam() {
            if (userParam == null) {
                userParam = new ArrayList<>();
            }
            return this.userParam;
        }

        /**
         * Gets the value of the identificationRunRef property.
         *
         * @return
         *         possible object is
         *         {@link Object }
         *
         */
        public Object getIdentificationRunRef() {
            return identificationRunRef;
        }

        /**
         * Sets the value of the identificationRunRef property.
         *
         * @param value
         *              allowed object is
         *              {@link Object }
         *
         */
        public void setIdentificationRunRef(Object value) {
            this.identificationRunRef = value;
        }

        /**
         * Gets the value of the scoreType property.
         *
         * @return
         *         possible object is
         *         {@link String }
         *
         */
        public String getScoreType() {
            return scoreType;
        }

        /**
         * Sets the value of the scoreType property.
         *
         * @param value
         *              allowed object is
         *              {@link String }
         *
         */
        public void setScoreType(String value) {
            this.scoreType = value;
        }

        /**
         * Gets the value of the higherScoreBetter property.
         *
         * @return true if higher score is better.
         */
        public boolean isHigherScoreBetter() {
            return higherScoreBetter;
        }

        /**
         * Sets the value of the higherScoreBetter property.
         *
         * @param value value of the higherScoreBetter.
         */
        public void setHigherScoreBetter(boolean value) {
            this.higherScoreBetter = value;
        }

        /**
         * Gets the value of the significanceThreshold property.
         *
         * @return
         *         possible object is
         *         {@link Float }
         *
         */
        public Float getSignificanceThreshold() {
            return significanceThreshold;
        }

        /**
         * Sets the value of the significanceThreshold property.
         *
         * @param value
         *              allowed object is
         *              {@link Float }
         *
         */
        public void setSignificanceThreshold(Float value) {
            this.significanceThreshold = value;
        }

        /**
         * Gets the value of the spectrumReference property.
         *
         * @return
         *         possible object is
         *         {@link Long }
         *
         */
        public Long getSpectrumReference() {
            return spectrumReference;
        }

        /**
         * Sets the value of the spectrumReference property.
         *
         * @param value
         *              allowed object is
         *              {@link Long }
         *
         */
        public void setSpectrumReference(Long value) {
            this.spectrumReference = value;
        }

        /**
         * Gets the value of the rt property.
         *
         * @return
         *         possible object is
         *         {@link Float }
         *
         */
        public Float getRT() {
            return rt;
        }

        /**
         * Sets the value of the rt property.
         *
         * @param value
         *              allowed object is
         *              {@link Float }
         *
         */
        public void setRT(Float value) {
            this.rt = value;
        }

        /**
         * Gets the value of the mz property.
         *
         * @return
         *         possible object is
         *         {@link Float }
         *
         */
        public Float getMZ() {
            return mz;
        }

        /**
         * Sets the value of the mz property.
         *
         * @param value
         *              allowed object is
         *              {@link Float }
         *
         */
        public void setMZ(Float value) {
            this.mz = value;
        }

        /**
         * <p>
         * Java class for anonymous complex type.
         *
         * <p>
         * The following schema fragment specifies the expected content
         * contained within this class.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence minOccurs="0">
         *         &lt;element name="userParam" type="{}userParam" maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attribute name="sequence" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="charge" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *       &lt;attribute name="score" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
         *       &lt;attribute name="aa_before">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="0"/>
         *             &lt;maxLength value="1"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="aa_after">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="0"/>
         *             &lt;maxLength value="1"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="protein_refs" type="{http://www.w3.org/2001/XMLSchema}IDREFS" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "userParam"
        })
        public static class PeptideHit {

            /**
             *
             */
            protected List<UserParam> userParam;

            /**
             *
             */
            @XmlAttribute(name = "sequence", required = true)
            protected String sequence;

            /**
             *
             */
            @XmlAttribute(name = "charge", required = true)
            protected BigInteger charge;

            /**
             *
             */
            @XmlAttribute(name = "score", required = true)
            protected float score;

            /**
             *
             */
            @XmlAttribute(name = "aa_before")
            protected String aaBefore;

            /**
             *
             */
            @XmlAttribute(name = "aa_after")
            protected String aaAfter;

            /**
             *
             */
            @XmlAttribute(name = "protein_refs")
            @XmlIDREF
            @XmlSchemaType(name = "IDREFS")
            protected List<Object> proteinRefs;

            /**
             * Gets the value of the userParam property.
             *
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the
             * userParam property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getUserParam().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link UserParam }
             *
             *
             * @return UserParam list.
             */
            public List<UserParam> getUserParam() {
                if (userParam == null) {
                    userParam = new ArrayList<>();
                }
                return this.userParam;
            }

            /**
             * Gets the value of the sequence property.
             *
             * @return
             *         possible object is
             *         {@link String }
             *
             */
            public String getSequence() {
                return sequence;
            }

            /**
             * Sets the value of the sequence property.
             *
             * @param value
             *              allowed object is
             *              {@link String }
             *
             */
            public void setSequence(String value) {
                this.sequence = value;
            }

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
             * Gets the value of the score property.
             *
             * @return score value.
             */
            public float getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             *
             * @param value score value.
             */
            public void setScore(float value) {
                this.score = value;
            }

            /**
             * Gets the value of the aaBefore property.
             *
             * @return
             *         possible object is
             *         {@link String }
             *
             */
            public String getAaBefore() {
                return aaBefore;
            }

            /**
             * Sets the value of the aaBefore property.
             *
             * @param value
             *              allowed object is
             *              {@link String }
             *
             */
            public void setAaBefore(String value) {
                this.aaBefore = value;
            }

            /**
             * Gets the value of the aaAfter property.
             *
             * @return
             *         possible object is
             *         {@link String }
             *
             */
            public String getAaAfter() {
                return aaAfter;
            }

            /**
             * Sets the value of the aaAfter property.
             *
             * @param value
             *              allowed object is
             *              {@link String }
             *
             */
            public void setAaAfter(String value) {
                this.aaAfter = value;
            }

            /**
             * Gets the value of the proteinRefs property.
             *
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the
             * proteinRefs property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getProteinRefs().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Object }
             *
             *
             * @return ProteinRef list.
             */
            public List<Object> getProteinRefs() {
                if (proteinRefs == null) {
                    proteinRefs = new ArrayList<>();
                }
                return this.proteinRefs;
            }

        }

    }

}
