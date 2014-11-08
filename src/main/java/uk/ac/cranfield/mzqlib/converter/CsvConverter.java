package uk.ac.cranfield.mzqlib.converter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import uk.ac.cranfield.mzqlib.MzqLib;
import uk.ac.cranfield.mzqlib.data.FeatureData;
import uk.ac.cranfield.mzqlib.data.MzqData;
import uk.ac.cranfield.mzqlib.data.PeptideData;
import uk.ac.cranfield.mzqlib.data.ProteinData;
import uk.ac.cranfield.mzqlib.data.ProteinGroupData;
import uk.ac.cranfield.mzqlib.data.QuantitationLevel;

/**
 *
 * @author Jun Fan@cranfield
 */
public class CsvConverter extends GenericConverter {

    final static String SEPARATOR = ",";

    public CsvConverter(String filename, String outputFile) {
        super(filename, outputFile);
    }

    @Override
    public void convert() {
        if (outfile.length() == 0) {
            outfile = getBaseFilename() + ".csv";
        }
        StringBuilder sb = new StringBuilder();
        //deal with protein groups
        ArrayList<QuantitationLevel> pgs = new ArrayList<>();
        for(ProteinGroupData pg : MzqLib.data.getProteinGroups()){
            pgs.add(pg);
        }
        outputAssayAndSV(sb, MzqData.PROTEIN_GROUP, pgs);
        if (MzqLib.data.control.isRequired(MzqData.PROTEIN_GROUP, MzqData.RATIO, MzqData.RATIO_STRING)) {
            outputRatio(sb, MzqData.PROTEIN, pgs);
        }
        if (!MzqLib.data.control.getElements(MzqData.PROTEIN_GROUP, MzqData.GLOBAL).isEmpty()) {
            outputGlobal(sb, MzqData.PROTEIN, pgs);
        }
        sb.append("\n");
        
        //deal with proteins
        ArrayList<QuantitationLevel> proteins = new ArrayList<>();
        for (ProteinData protein : MzqLib.data.getProteins()) {
            proteins.add(protein);
        }
        //not just the obj artificially created
        if (proteins.size() > 1 || !MzqLib.data.getProteins().get(0).getAccession().equals(MzqData.ARTIFICIAL)) {
            outputAssayAndSV(sb, MzqData.PROTEIN, proteins);
            if (MzqLib.data.control.isRequired(MzqData.PROTEIN, MzqData.RATIO, MzqData.RATIO_STRING)) {
                outputRatio(sb, MzqData.PROTEIN, proteins);
            }
            if (!MzqLib.data.control.getElements(MzqData.PROTEIN, MzqData.GLOBAL).isEmpty()) {
                outputGlobal(sb, MzqData.PROTEIN, proteins);
            }
        }
        sb.append("\n");
        //deal with peptide
        ArrayList<QuantitationLevel> peptides = new ArrayList<QuantitationLevel>();
        for (PeptideData peptide : MzqLib.data.getPeptides()) {
            peptides.add(peptide);
        }
        if (!peptides.isEmpty()) {
            outputAssayAndSV(sb, MzqData.PEPTIDE, peptides);
            if (MzqLib.data.control.isRequired(MzqData.PEPTIDE, MzqData.RATIO, MzqData.RATIO_STRING)) {
                outputRatio(sb, MzqData.PEPTIDE, peptides);
            }
            if (!MzqLib.data.control.getElements(MzqData.PEPTIDE, MzqData.GLOBAL).isEmpty()) {
                outputGlobal(sb, MzqData.PEPTIDE, peptides);
            }
        }
        //deal with features
        ArrayList<QuantitationLevel> features = new ArrayList<QuantitationLevel>();
        for (FeatureData feature : MzqLib.data.getFeatures()) {
            features.add(feature);
        }
        if (!features.isEmpty()) {
            outputAssayAndSV(sb, MzqData.FEATURE, features);
            if (MzqLib.data.control.isRequired(MzqData.FEATURE, MzqData.RATIO, MzqData.RATIO_STRING)) {
                outputRatio(sb, MzqData.FEATURE, features);
            }
            if (!MzqLib.data.control.getElements(MzqData.FEATURE, MzqData.GLOBAL).isEmpty()) {
                outputGlobal(sb, MzqData.FEATURE, features);
            }
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
            out.append(sb.toString());
            out.close();
        } catch (IOException e) {
            System.out.println("Problems while closing file " + outfile + "!\n" + e);
        }
    }

    private void addHeaderLine(int level, StringBuilder sb, String quantityName) {
        if (MzqLib.data.control.isRequired(level, MzqData.ASSAY, quantityName) || MzqLib.data.control.isRequired(level, MzqData.SV, quantityName)) {
            if(level == MzqData.PEPTIDE){
                sb.append("Peptide");
                sb.append(SEPARATOR);
                sb.append("Charge");
                sb.append(SEPARATOR);
                sb.append("Modification");
            }else if(level == MzqData.PROTEIN_GROUP){
                sb.append("Anchor protein");
                sb.append(SEPARATOR);
                sb.append("Ambiguity member");
            }else{
                sb.append("Entity");
            }
            addAssayHeader(level, sb, quantityName, SEPARATOR, "");
            addSvHeader(level, sb, quantityName, SEPARATOR, "");
            sb.append("\n");
        }
    }

    private void outputAssayAndSV(StringBuilder sb, int level, ArrayList<QuantitationLevel> objects) {
        printQuantitationLevelHeader(sb, level);
        for (String quantityName : MzqLib.data.getQuantitationNames()) {
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

    private void outputRatio(StringBuilder sb, int level, ArrayList<QuantitationLevel> objects) {
        sb.append("Ratios");
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

    private void outputGlobal(StringBuilder sb, int level, ArrayList<QuantitationLevel> objects) {
        HashSet<String> globals = MzqLib.data.control.getElements(level, MzqData.GLOBAL);
        if (globals.size() > 0) {
            sb.append("Global");
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

    private void printQuantitationLevel(StringBuilder sb, int level, QuantitationLevel obj) {
        switch (level) {
            case MzqData.PROTEIN_GROUP:
                ProteinGroupData pg = (ProteinGroupData) obj;
                sb.append(pg.getAnchorProteinStr());
                sb.append(SEPARATOR);
                sb.append(pg.getAmbiguityMemberStr());
                break;
            case MzqData.PROTEIN:
                sb.append(((ProteinData) obj).getId());
                break;
            case MzqData.PEPTIDE:
                PeptideData pepData = (PeptideData) obj;
                sb.append(pepData.getSeq());
                sb.append(SEPARATOR);
                sb.append(Arrays.toString(pepData.getCharges()));
                sb.append(SEPARATOR);
                sb.append(pepData.getModifications());
                break;
            case MzqData.FEATURE:
                sb.append(((FeatureData) obj).getId());
                break;
        }
    }

    private void printQuantitationLevelHeader(StringBuilder sb, int level) {
        switch (level) {
            case MzqData.PROTEIN_GROUP:
                sb.append("Protein groups");
                break;
            case MzqData.PROTEIN:
                sb.append("Proteins");
                break;
            case MzqData.PEPTIDE:
                sb.append("Peptides");
                break;
            case MzqData.FEATURE:
                sb.append("Features");
                break;
        }
        sb.append("\n");
    }
}
