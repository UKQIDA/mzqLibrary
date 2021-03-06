package uk.ac.cranfield.mzqlib.converter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.cranfield.mzqlib.MzqLib;
import uk.ac.cranfield.mzqlib.data.FeatureData;
import uk.ac.cranfield.mzqlib.data.MzqData;
import uk.ac.cranfield.mzqlib.data.PeptideData;
import uk.ac.cranfield.mzqlib.data.ProteinData;
import uk.ac.cranfield.mzqlib.data.ProteinGroupData;
import uk.ac.cranfield.mzqlib.data.QuantitationLevel;

/**
 * CsvConverter is to convert mzq file to csv file.
 *
 * @author Jun Fan@cranfield
 */
public class CsvConverter extends GenericConverter {
    static final String SEPARATOR = ",";

    /**
     * Constructor.
     *
     * @param filename   input mzq file name.
     * @param outputFile output csv file name.
     */
    public CsvConverter(final String filename, final String outputFile) {
        super(filename, outputFile);
    }

    private void addEntityHeader(final int level, final StringBuilder sb) {
        switch (level) {
        case MzqData.PEPTIDE :
            sb.append("Peptide");
            sb.append(SEPARATOR);
            sb.append("Charge");
            sb.append(SEPARATOR);
            sb.append("Modification");

            break;

        case MzqData.PROTEIN_GROUP :
            sb.append("Anchor protein");
            sb.append(SEPARATOR);
            sb.append("Ambiguity member");

            break;

        default :
            sb.append("Entity");

            break;
        }
    }

    private void addHeaderLine(final int level, final StringBuilder sb, final String quantityName) {
        if (MzqLib.DATA.getControl().isRequired(level, MzqData.ASSAY, quantityName)
                || MzqLib.DATA.getControl().isRequired(level, MzqData.SV, quantityName)) {
            addEntityHeader(level, sb);
            addAssayHeader(level, sb, quantityName, SEPARATOR, "");
            addSvHeader(level, sb, quantityName, SEPARATOR, "");
            sb.append("\n");
        }
    }

    /**
     * Convert method. Convert mzq file to csv file.
     * Override the method in GenericConverter.
     */
    @Override
    public final void convert() {
        if (this.getOutfile().length() == 0) {
            this.setOutfile(getBaseFilename() + ".csv");
        }

        StringBuilder sb = new StringBuilder();

        // deal with protein groups
        List<QuantitationLevel> pgs = new ArrayList<>();

        for (ProteinGroupData pg : MzqLib.DATA.getProteinGroups()) {
            pgs.add(pg);
        }

        outputAssayAndSV(sb, MzqData.PROTEIN_GROUP, pgs);

        if (MzqLib.DATA.getControl().isRequired(MzqData.PROTEIN_GROUP, MzqData.RATIO, MzqData.RATIO_STRING)) {
            outputRatio(sb, MzqData.PROTEIN_GROUP, pgs);
        }

        if (!MzqLib.DATA.getControl().getElements(MzqData.PROTEIN_GROUP, MzqData.GLOBAL).isEmpty()) {
            outputGlobal(sb, MzqData.PROTEIN_GROUP, pgs);
        }

        sb.append("\n");

        // deal with proteins
        List<QuantitationLevel> proteins = new ArrayList<>();

        for (ProteinData protein : MzqLib.DATA.getProteins()) {
            proteins.add(protein);
        }

        // not just the obj artificially created
        if (proteins.size() > 1 || !MzqLib.DATA.getProteins().get(0).getAccession().equals(MzqData.ARTIFICIAL)) {
            outputAssayAndSV(sb, MzqData.PROTEIN, proteins);

            if (MzqLib.DATA.getControl().isRequired(MzqData.PROTEIN, MzqData.RATIO, MzqData.RATIO_STRING)) {
                outputRatio(sb, MzqData.PROTEIN, proteins);
            }

            if (!MzqLib.DATA.getControl().getElements(MzqData.PROTEIN, MzqData.GLOBAL).isEmpty()) {
                outputGlobal(sb, MzqData.PROTEIN, proteins);
            }
        }

        sb.append("\n");

        // deal with peptide
        List<QuantitationLevel> peptides = new ArrayList<>();

        for (PeptideData peptide : MzqLib.DATA.getPeptides()) {
            peptides.add(peptide);
        }

        if (!peptides.isEmpty()) {
            outputAssayAndSV(sb, MzqData.PEPTIDE, peptides);

            if (MzqLib.DATA.getControl().isRequired(MzqData.PEPTIDE, MzqData.RATIO, MzqData.RATIO_STRING)) {
                outputRatio(sb, MzqData.PEPTIDE, peptides);
            }

            if (!MzqLib.DATA.getControl().getElements(MzqData.PEPTIDE, MzqData.GLOBAL).isEmpty()) {
                outputGlobal(sb, MzqData.PEPTIDE, peptides);
            }
        }

        // deal with features
        List<QuantitationLevel> features = new ArrayList<>();

        for (FeatureData feature : MzqLib.DATA.getFeatures()) {
            features.add(feature);
        }

        if (!features.isEmpty()) {
            outputAssayAndSV(sb, MzqData.FEATURE, features);

            if (MzqLib.DATA.getControl().isRequired(MzqData.FEATURE, MzqData.RATIO, MzqData.RATIO_STRING)) {
                outputRatio(sb, MzqData.FEATURE, features);
            }

            if (!MzqLib.DATA.getControl().getElements(MzqData.FEATURE, MzqData.GLOBAL).isEmpty()) {
                outputGlobal(sb, MzqData.FEATURE, features);
            }
        }

        BufferedWriter   out = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(this.getOutfile());
            out = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            out.append(sb.toString());
        } catch (IOException e) {
            System.out.println("Problems while closing file " + this.getOutfile() + "!\n" + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(CsvConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(CsvConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void outputAssayAndSV(final StringBuilder sb, final int level, final List<QuantitationLevel> objects) {
        printQuantitationLevelHeader(sb, level);

        for (String quantityName : MzqLib.DATA.getQuantitationNames()) {
            addHeaderLine(level, sb, quantityName);

            for (QuantitationLevel obj : objects) {
                if (obj.hasQuantitation(quantityName) || obj.hasSV(quantityName)) {
                    printQuantitationLevel(sb, level, obj);
                    addAssayValue(level, sb, obj, SEPARATOR, quantityName);
                    addSvValue(level, sb, obj, SEPARATOR, quantityName);
                    sb.append("\n");
                }
            }

            sb.append("\n");
        }
    }

    private void outputGlobal(final StringBuilder sb, final int level, final List<QuantitationLevel> objects) {
        Set<String> globals = MzqLib.DATA.getControl().getElements(level, MzqData.GLOBAL);

        if (globals.size() > 0) {
            sb.append("Global\n");
            addEntityHeader(level, sb);
            addGlobalHeader(sb, SEPARATOR, "", globals);
            sb.append("\n");

            for (QuantitationLevel obj : objects) {
                if (obj.hasGlobal()) {
                    printQuantitationLevel(sb, level, obj);
                    addGlobalValue(level, sb, obj, SEPARATOR, globals);
                    sb.append("\n");
                }
            }

            sb.append("\n");
        }
    }

    private void outputRatio(final StringBuilder sb, final int level, final List<QuantitationLevel> objects) {
        sb.append("Ratios\n");
        addEntityHeader(level, sb);
        addRatioHeader(level, sb, SEPARATOR, "");
        sb.append("\n");

        for (QuantitationLevel obj : objects) {
            if (obj.hasRatio()) {
                printQuantitationLevel(sb, level, obj);
                addRatioValue(level, sb, obj, SEPARATOR);
                sb.append("\n");
            }
        }

        sb.append("\n");
    }

    private void printQuantitationLevel(final StringBuilder sb, final int level, final QuantitationLevel obj) {
        switch (level) {
        case MzqData.PROTEIN_GROUP :
            if (obj instanceof ProteinGroupData) {
                ProteinGroupData pg = (ProteinGroupData) obj;

                sb.append(MzqLib.DATA.getProtein(pg.getAnchorProteinStr()).getAccession());
                sb.append(SEPARATOR);
                sb.append(pg.getAmbiguityMemberStr());
            }

            break;

        case MzqData.PROTEIN :
            if (obj instanceof ProteinData) {
                sb.append(((ProteinData) obj).getAccession());
            }

            break;

        case MzqData.PEPTIDE :
            if (obj instanceof PeptideData) {
                PeptideData pepData = (PeptideData) obj;

                sb.append(pepData.getSeq());
                sb.append(SEPARATOR);
                sb.append(Arrays.toString(pepData.getCharges()));
                sb.append(SEPARATOR);
                sb.append(pepData.getModifications());
            }

            break;

        case MzqData.FEATURE :
            if (obj instanceof FeatureData) {
                sb.append(((FeatureData) obj).getId());
            }

            break;

        default :
            break;
        }
    }

    private void printQuantitationLevelHeader(final StringBuilder sb, final int level) {
        switch (level) {
        case MzqData.PROTEIN_GROUP :
            sb.append("Protein groups");

            break;

        case MzqData.PROTEIN :
            sb.append("Proteins");

            break;

        case MzqData.PEPTIDE :
            sb.append("Peptides");

            break;

        case MzqData.FEATURE :
            sb.append("Features");

            break;

        default :
            break;
        }

        sb.append("\n");
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
