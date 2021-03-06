package uk.ac.cranfield.mzqlib.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.cranfield.mzqlib.MzqLib;
import uk.ac.cranfield.mzqlib.data.FeatureData;
import uk.ac.cranfield.mzqlib.data.MzqData;
import uk.ac.cranfield.mzqlib.data.PeptideData;
import uk.ac.cranfield.mzqlib.data.ProteinData;
import uk.ac.liv.pgb.jmzqml.model.mzqml.Assay;
import uk.ac.liv.pgb.jmzqml.model.mzqml.CvParam;
import uk.ac.liv.pgb.jmzqml.model.mzqml.Software;

/**
 * HtmlConverter is to convert mzq file to html file.
 *
 * @author Jun Fan@cranfield
 */
public class HtmlConverter extends GenericConverter {
    private static final String                  SEPERATOR = "</td><td>";
    private static final HashMap<String, String> TEMPLATES = new HashMap<>();
    private final String                         template  = "htmlTemplate.txt";

    /**
     * Constructor.
     *
     * @param filename   input mzq file name.
     * @param outputFile output html file name.
     */
    public HtmlConverter(final String filename, final String outputFile) {
        super(filename, outputFile);
    }

    private void addHeader(final int level, final StringBuilder sb) {
        for (String quantityName : MzqLib.DATA.getQuantitationNames()) {
            if (level == MzqData.FEATURE) {
                addAssayHeader(level, sb, quantityName, "<th class=\"table-sortable:numeric\">", "</th>");
                addSvHeader(level, sb, quantityName, "<th class=\"table-sortable:numeric\">", "</th>");
            } else {
                addAssayHeader(level, sb, quantityName, "<th>", "</th>");
                addSvHeader(level, sb, quantityName, "<th>", "</th>");
            }
        }

        if (level == MzqData.FEATURE) {
            addRatioHeader(level, sb, "<th class=\"table-sortable:numeric\">", "</th>");
        } else {
            addRatioHeader(level, sb, "<th>", "</th>");
        }

        Set<String> names = MzqLib.DATA.getControl().getElements(level, MzqData.GLOBAL);

        if (names.size() > 0) {
            if (level == MzqData.FEATURE) {
                addGlobalHeader(sb, "<th class=\"table-sortable:numeric\">", "</th>", names);
            } else {
                addGlobalHeader(sb, "<th>", "</th>", names);
            }

            sb.append("\n");
        }
    }

    /**
     * Convert method. Convert mzq file to html file.
     * Override the method in GenericConverter.
     */
    @Override
    public final void convert() {
        if (this.getOutfile().length() == 0) {
            this.setOutfile(getBaseFilename() + ".html");
        }

        // read in the template from the local file and saved in the hash
        if (TEMPLATES.isEmpty()) {
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(
                    new InputStreamReader(HtmlConverter.class.getClassLoader().getResourceAsStream(template),
                                          "UTF-8"));

                String        line = "";
                String        tag  = "";
                StringBuilder sb   = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#")) {    // tag line
                        if (tag.length() > 0) {
                            sb.deleteCharAt(sb.length() - 1);
                            sb.deleteCharAt(sb.length() - 1);
                            TEMPLATES.put(tag, sb.toString());
                        }

                        sb  = new StringBuilder();
                        tag = line.substring(1);
                    } else {
                        sb.append(line);
                        sb.append("\n");
                    }
                }

                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);
                TEMPLATES.put(tag, sb.toString());
            } catch (IOException ioe) {
                throw new IllegalStateException("Can not find the template file: " + ioe.getMessage(), ioe);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(HtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append(TEMPLATES.get("MAIN_HEADER_1"));
        sb.append(getBaseFilename());
        sb.append(TEMPLATES.get("MAIN_HEADER_2"));

        // add metadata
        // analysis summary
        sb.append("<h3>Analysis Summary</h3>\n<ul>\n");

        for (CvParam cv : MzqLib.DATA.getAnalysisSummary().getCvParam()) {
            sb.append("<li><b>");
            sb.append(cv.getName());

            if (cv.getValue() != null && cv.getValue().length() > 1 && !cv.getValue().equalsIgnoreCase("null")) {
                sb.append(":</b> ");
                sb.append(cv.getValue());
            } else {
                sb.append("</b>");
            }

            sb.append("</li>\n");
        }

        sb.append("</ul>\n");

        // software
        sb.append("<h3>Software List</h3>\n<ul>\n");

        for (Software software : MzqLib.DATA.getSoftwareList().getSoftware()) {
            sb.append("<li>");
            sb.append(software.getId());
            sb.append(" (Version: ");
            sb.append(software.getVersion());
            sb.append(")</li>\n");
        }

        sb.append("</ul>\n");

        // Assays
        sb.append("<h3>Assays</h3>\n<ul>\n");

        for (Assay assay : MzqLib.DATA.getAssays()) {
            sb.append("<li><b> ");
            sb.append(assay.getId());
            sb.append(":</b>");
            sb.append(assay.getName());
            sb.append("</li>\n");
        }

        sb.append("</ul>\n");

        // start the protein table
        sb.append(TEMPLATES.get("PROTEIN_HEADER_START"));
        addHeader(MzqData.PROTEIN, sb);
        sb.append(TEMPLATES.get("PROTEIN_HEADER_END"));

        Set<String> proteinGlobalNames = MzqLib.DATA.getControl().getElements(MzqData.PROTEIN, MzqData.GLOBAL);
        Set<String> peptideGlobalNames = MzqLib.DATA.getControl().getElements(MzqData.PEPTIDE, MzqData.GLOBAL);
        Set<String> featureGlobalNames = MzqLib.DATA.getControl().getElements(MzqData.FEATURE, MzqData.GLOBAL);
        int         proteinAssaySize   = MzqLib.DATA.getControl()
                                                    .getElements(MzqData.PROTEIN, MzqData.ASSAY)
                                                    .size() * MzqLib.DATA.getAssayIDs().size();
        int proteinSvSize = MzqLib.DATA.getControl()
                                       .getElements(MzqData.PROTEIN, MzqData.SV)
                                       .size() * MzqLib.DATA.getSvs().size();
        int proteinRatioSize = 0;

        if (MzqLib.DATA.getControl().isRequired(MzqData.PROTEIN, MzqData.RATIO, MzqData.RATIO_STRING)) {
            proteinRatioSize = MzqLib.DATA.getRatios().size();
        }

        // start from the first quantitation/sv column
        final int proteinTotalSize = proteinAssaySize + proteinSvSize + proteinRatioSize + proteinGlobalNames.size()
                                     + 1;    // +1 for the protein accession column
        int peptideAssaySize = MzqLib.DATA.getControl()
                                          .getElements(MzqData.PEPTIDE, MzqData.ASSAY)
                                          .size() * MzqLib.DATA.getAssayIDs().size();
        int peptideSvSize = MzqLib.DATA.getControl()
                                       .getElements(MzqData.PEPTIDE, MzqData.SV)
                                       .size() * MzqLib.DATA.getSvs().size();
        int peptideRatioSize = 0;

        if (MzqLib.DATA.getControl().isRequired(MzqData.PEPTIDE, MzqData.RATIO, MzqData.RATIO_STRING)) {
            peptideRatioSize = MzqLib.DATA.getRatios().size();
        }

        // start from modification column
        final int peptideTotalSize = peptideAssaySize + peptideSvSize + peptideRatioSize + peptideGlobalNames.size()
                                     + 2;    // +2 for the peptide sequence and modification columns

//      for(ProteinData protein:MzqLib.data.getProteins()){
        for (int proIdx = 0; proIdx < MzqLib.DATA.getProteins().size(); proIdx++) {
            ProteinData protein = MzqLib.DATA.getProteins().get(proIdx);

            // it seems html does not like . which is fine in NCName
            String proteinID = protein.getAccession().replace(".", "_");

            sb.append(TEMPLATES.get("PROTEIN_START_1"));
            sb.append(proteinID);

            if (proIdx % 2 != 0) {
                sb.append("\" class=\"pro-alternate");
            }

            sb.append(TEMPLATES.get("PROTEIN_START_2"));
            sb.append(protein.getAccession());

            for (String quantityName : MzqLib.DATA.getQuantitationNames()) {
                addAssayValue(MzqData.PROTEIN, sb, protein, SEPERATOR, quantityName);
                addSvValue(MzqData.PROTEIN, sb, protein, SEPERATOR, quantityName);
            }

            addRatioValue(MzqData.PROTEIN, sb, protein, SEPERATOR);
            addGlobalValue(MzqData.PROTEIN, sb, protein, SEPERATOR, proteinGlobalNames);
            sb.append(TEMPLATES.get("PROTEIN_END_1"));
            sb.append(proteinID);
            sb.append("-detail");
            sb.append(TEMPLATES.get("PROTEIN_END_2"));
            sb.append("child-of-");
            sb.append(proteinID);
            sb.append(TEMPLATES.get("PROTEIN_END_3"));
            sb.append(proteinTotalSize);
            sb.append(TEMPLATES.get("PROTEIN_END_4"));
            addHeader(MzqData.PEPTIDE, sb);
            sb.append(TEMPLATES.get("PROTEIN_END_5"));

//          for(PeptideData peptide:protein.getPeptides()){
            for (int pepIdx = 0; pepIdx < protein.getPeptides().size(); pepIdx++) {
                PeptideData peptide   = protein.getPeptides().get(pepIdx);
                String      peptideID = proteinID + "-" + peptide.getSeq() + "-"
                                        + peptide.getPeptide().getModification().size();

                sb.append(TEMPLATES.get("PEPTIDE_START_1"));
                sb.append(peptideID);

                if (pepIdx % 2 != 0) {
                    sb.append("\" class=\"pep-alternate");
                }

                sb.append(TEMPLATES.get("PEPTIDE_START_2"));
                sb.append(peptide.getSeq());
                sb.append(SEPERATOR);
                sb.append(peptide.getPeptide().getModification().size());

                for (String quantityName : MzqLib.DATA.getQuantitationNames()) {
                    addAssayValue(MzqData.PEPTIDE, sb, peptide, SEPERATOR, quantityName);
                    addSvValue(MzqData.PEPTIDE, sb, peptide, SEPERATOR, quantityName);
                }

                addRatioValue(MzqData.PEPTIDE, sb, peptide, SEPERATOR);
                addGlobalValue(MzqData.PEPTIDE, sb, peptide, SEPERATOR, peptideGlobalNames);
                sb.append(TEMPLATES.get("PEPTIDE_END_1"));
                sb.append(peptideID);
                sb.append("-detail");
                sb.append(TEMPLATES.get("PEPTIDE_END_2"));
                sb.append("child-of-");
                sb.append(peptideID);
                sb.append(TEMPLATES.get("PEPTIDE_END_3"));
                sb.append(peptideTotalSize);
                sb.append(TEMPLATES.get("PEPTIDE_END_4"));
                addHeader(MzqData.FEATURE, sb);
                sb.append(TEMPLATES.get("PEPTIDE_END_5"));

                for (FeatureData feature : peptide.getFeatures()) {
                    sb.append(TEMPLATES.get("FEATURE_START"));
                    sb.append(feature.getFeature().getCharge());
                    sb.append(SEPERATOR);
                    sb.append(feature.getFeature().getMz());

                    for (String quantityName : MzqLib.DATA.getQuantitationNames()) {
                        addAssayValue(MzqData.FEATURE, sb, feature, SEPERATOR, quantityName);
                        addSvValue(MzqData.FEATURE, sb, feature, SEPERATOR, quantityName);
                    }

                    addRatioValue(MzqData.FEATURE, sb, feature, SEPERATOR);
                    addGlobalValue(MzqData.FEATURE, sb, feature, SEPERATOR, featureGlobalNames);
                    sb.append(TEMPLATES.get("FEATURE_END"));
                }

                sb.append(TEMPLATES.get("PEPTIDE_FINISH"));
            }

            sb.append(TEMPLATES.get("PROTEIN_FINISH"));
        }

        sb.append(TEMPLATES.get("MAIN_FOOTER"));

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
                    Logger.getLogger(HtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
