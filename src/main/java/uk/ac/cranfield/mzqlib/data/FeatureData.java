package uk.ac.cranfield.mzqlib.data;

import uk.ac.liv.pgb.jmzqml.model.mzqml.Feature;

/**
 * FeatureData contains all the data about Feature.
 *
 * @author Jun Fan@cranfield
 */
public class FeatureData extends QuantitationLevel {

//  /**
//   * Peptide ID, normally in the form of peptideSeq_modificationString
//   */
//  private String peptideID;
    private final Feature feature;
    private String        rawFilesGroupRef;

    /**
     * Constructor.
     *
     * @param feature Feature.
     */
    public FeatureData(final Feature feature) {
        this.feature = feature;
    }

    /**
     * Get feature count.
     *
     * @return feature count.
     */
    @Override
    public final int getCount() {
        return 1;
    }

    /**
     * Get Feature.
     *
     * @return Feature.
     */
    public final Feature getFeature() {
        return feature;
    }

//  public String getPeptideID() {
//      return peptideID;
//  }
//
//  public void setPeptideID(String peptideID) {
//      this.peptideID = peptideID;
//  }

    /**
     * Get feature id.
     *
     * @return feature id.
     */
    public final String getId() {
        return feature.getId();
    }

    /**
     * Get RawFilesGroupRef.
     *
     * @return rawFilesGroupRef.
     */
    public final String getRawFilesGroupRef() {
        return rawFilesGroupRef;
    }

    /**
     * Set RawFilesGroupRef.
     *
     * @param rawFilesGroupRef rawFilesGroupRef.
     */
    public final void setRawFilesGroupRef(final String rawFilesGroupRef) {
        this.rawFilesGroupRef = rawFilesGroupRef;
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
