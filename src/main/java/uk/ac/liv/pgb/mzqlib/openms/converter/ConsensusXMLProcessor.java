
package uk.ac.liv.pgb.mzqlib.openms.converter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import uk.ac.liv.pgb.jmzqml.model.mzqml.Assay;
import uk.ac.liv.pgb.jmzqml.model.mzqml.AssayList;
import uk.ac.liv.pgb.jmzqml.model.mzqml.Cv;
import uk.ac.liv.pgb.jmzqml.model.mzqml.FeatureList;
import uk.ac.liv.pgb.jmzqml.model.mzqml.PeptideConsensusList;
import uk.ac.liv.pgb.jmzqml.model.mzqml.RawFilesGroup;

/**
 * The interface of ConsensusXMLProcessor that provides methods of creating
 * mzQuantML elements from consensusxml file.
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 18-Mar-2014 15:17:10
 */
public interface ConsensusXMLProcessor {

    /**
     * Get PeptideConsensusList element.
     *
     * @return PeptideConsensusList
     */
    PeptideConsensusList getPeptideConsensusList();

    /**
     * Get map of rawFilesGroup's id to FeatureList.
     *
     * @return Map<String, FeatureList>
     */
    Map<String, FeatureList> getRawFilesGroupIdToFeatureListMap();

    /**
     * Get Cv element.
     *
     * @return Cv
     */
    Cv getCv();

    /**
     * Get AssayList element.
     *
     * @return AssayList
     */
    AssayList getAssayList();

    /**
     * Get map of rawFilesGroup to Assay.
     *
     * @return Map<String, Assay>
     */
    Map<String, Assay> getRawFilesGroupAssayMap();

    /**
     * Get list of rawFilesGroup.
     *
     * @return List<RawFilesGroup>
     */
    List<RawFilesGroup> getRawFilesGroupList();

    /**
     * Convert to mzQuantML file.
     *
     * @param outputFn output file name
     *
     * @throws IOException io exception
     */
    void convert(String outputFn)
            throws IOException;

    /**
     * Convert method.
     *
     * @param outputFn              output file name.
     * @param studyVariablesToFiles study variables to files map.
     *
     * @throws IOException io exception.
     */
    void convert(String outputFn,
                 Map<String, ? extends Collection<File>> studyVariablesToFiles)
            throws IOException;

}
