/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cranfield.mzqlib.converter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import uk.ac.cranfield.mzqlib.MzqLib;
import uk.ac.cranfield.mzqlib.data.FeatureData;
import uk.ac.cranfield.mzqlib.data.MzqData;
import uk.ac.cranfield.mzqlib.data.PeptideData;
import uk.ac.cranfield.mzqlib.data.ProteinData;
import uk.ac.cranfield.mzqlib.data.QuantitationLevel;
import uk.ac.liv.jmzqml.model.mzqml.Assay;
import uk.ac.liv.jmzqml.model.mzqml.CvParam;
import uk.ac.liv.jmzqml.model.mzqml.Software;
import uk.ac.liv.jmzqml.model.mzqml.StudyVariable;

/**
 *
 * @author Jun Fan<j.fan@cranfield.ac.uk>
 */
public class XlsConverter extends GenericConverter {

    final static String SEPERATOR = "";
    private WritableCellFormat boldFormat;
    private WritableCellFormat normalFormat;

    public XlsConverter(String filename, String outputFile) {
        super(filename, outputFile);
        WritableFont boldFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD);
        boldFormat = new WritableCellFormat(boldFont);
        WritableFont normalFont = new WritableFont(WritableFont.ARIAL, 10);
        normalFormat = new WritableCellFormat(normalFont);
    }

    @Override
    public void convert() {
        if (outfile.length() == 0) {
            outfile = getBaseFilename() + ".xls";
        }

        try {
            WritableWorkbook wb = Workbook.createWorkbook(new File(outfile));
            wb.createSheet("metadata", 0);
            wb.createSheet("proteins", 1);
            wb.createSheet("peptides", 2);
            wb.createSheet("features", 3);

            WritableSheet metaSheet = wb.getSheet("metadata");
            outputMetadata(metaSheet);
            WritableSheet proteinSheet = wb.getSheet("proteins");
//            ArrayList<ProteinData> proteins = MzqLib.data.getProteins();
            ArrayList<QuantitationLevel> proteins = new ArrayList<QuantitationLevel>();
            for (ProteinData protein : MzqLib.data.getProteins()) {
                proteins.add(protein);
            }
            //not just the protein artificially created
            if (proteins.size() > 1 || !MzqLib.data.getProteins().get(0).getAccession().equals(MzqData.ARTIFICIAL)) {
                int index = 0;
                index = outputAssayAndSV(MzqData.PROTEIN, proteinSheet, proteins);
                if (MzqLib.data.control.isRequired(MzqData.PROTEIN, MzqData.RATIO, MzqData.RATIO_STRING)) {
                    index = outputRatio(MzqData.PROTEIN, proteinSheet, proteins, index);
                }
                if (!MzqLib.data.control.getElements(MzqData.PROTEIN, MzqData.GLOBAL).isEmpty()) {
                    outputGlobal(MzqData.PROTEIN, proteinSheet, proteins, index);
                }
            }

            WritableSheet peptideSheet = wb.getSheet("peptides");
            ArrayList<QuantitationLevel> peptides = new ArrayList<QuantitationLevel>();
            for (PeptideData peptide : MzqLib.data.getPeptides()) {
                peptides.add(peptide);
            }
            if (!peptides.isEmpty()) {
                int index = 0;
                index = outputAssayAndSV(MzqData.PEPTIDE, peptideSheet, peptides);
                if (MzqLib.data.control.isRequired(MzqData.PEPTIDE, MzqData.RATIO, MzqData.RATIO_STRING)) {
                    outputRatio(MzqData.PEPTIDE, peptideSheet, peptides, index);
                }
                if (!MzqLib.data.control.getElements(MzqData.PEPTIDE, MzqData.GLOBAL).isEmpty()) {
                    outputGlobal(MzqData.PEPTIDE, peptideSheet, peptides, index);
                }
            }

            WritableSheet featureSheet = wb.getSheet("features");
            ArrayList<QuantitationLevel> features = new ArrayList<QuantitationLevel>();
            for (FeatureData feature : MzqLib.data.getFeatures()) {
                features.add(feature);
            }
            if (!features.isEmpty()) {
                int index = 0;
                index = outputAssayAndSV(MzqData.FEATURE, featureSheet, features);
                if (MzqLib.data.control.isRequired(MzqData.FEATURE, MzqData.RATIO, MzqData.RATIO_STRING)) {
                    outputRatio(MzqData.FEATURE, featureSheet, features, index);
                }
                if (!MzqLib.data.control.getElements(MzqData.FEATURE, MzqData.GLOBAL).isEmpty()) {
                    outputGlobal(MzqData.FEATURE, featureSheet, features, index);
                }
            }

            wb.write();
            wb.close();
        } catch (WriteException ex) {
            Logger.getLogger(XlsConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int outputAssayAndSV(int level, WritableSheet sheet, ArrayList<QuantitationLevel> objects) throws NumberFormatException, WriteException {
        //reference to CsvConverter
        //when sb.append("\n"), it should be rowCount++ and colCount=1 here
        //when sb.append(SEPERATOR), it should be colCount++ here except every new line colCount should be reset
        int rowCount = 0;
        int colCount = 1;
        for (String quantityName : MzqLib.data.getQuantitationNames()) {
            if (MzqLib.data.control.isRequired(level, MzqData.ASSAY, quantityName) || MzqLib.data.control.isRequired(level, MzqData.SV, quantityName)) {
                sheet.addCell(new Label(0, rowCount, quantityName, boldFormat));
                if (MzqLib.data.control.isRequired(level, MzqData.ASSAY, quantityName)) {
                    for (String assayID : MzqLib.data.getAssayIDs()) {
                        sheet.addCell(new Label(colCount, rowCount, assayID, boldFormat));
                        colCount++;
                    }
                }
                if (MzqLib.data.control.isRequired(level, MzqData.SV, quantityName)) {
                    for (StudyVariable sv : MzqLib.data.getSvs()) {
                        sheet.addCell(new Label(colCount, rowCount, sv.getId(), boldFormat));
                        colCount++;
                    }
                }
                rowCount++;
                colCount = 1;
            }
            for (QuantitationLevel obj : objects) {
                if (obj.hasQuantitation(quantityName) || obj.hasSV(quantityName)) {
                    printQuantitationLevel(level, sheet, rowCount, obj);
                    if (MzqLib.data.control.isRequired(level, MzqData.ASSAY, quantityName)) {
                        for (String assayID : MzqLib.data.getAssayIDs()) {
                            Double value = obj.getQuantity(quantityName, assayID);
                            printValue(value, sheet, colCount, rowCount);
                            colCount++;
                        }
                    }
                    if (MzqLib.data.control.isRequired(level, MzqData.SV, quantityName)) {
                        for (StudyVariable sv : MzqLib.data.getSvs()) {
                            Double value = obj.getStudyVariableQuantity(quantityName, sv.getId());
                            printValue(value, sheet, colCount, rowCount);
                            colCount++;
                        }
                    }
                    rowCount++;
                    colCount = 1;
                }
            }
            rowCount++;
        }
        return rowCount;
    }

    private void printValue(Double value, WritableSheet sheet, int colCount, int rowCount) throws WriteException, NumberFormatException {
        if (value == null) {
            sheet.addCell(new Label(colCount, rowCount, "null", normalFormat));
        } else {
            sheet.addCell(new Number(colCount, rowCount, value, normalFormat));
        }
    }

    private void printQuantitationLevel(int level, WritableSheet sheet, int rowCount, QuantitationLevel obj) throws WriteException {
        switch (level) {
            case MzqData.PROTEIN:
                sheet.addCell(new Label(0, rowCount, ((ProteinData) obj).getId(), normalFormat));
                break;
            case MzqData.PEPTIDE:
                sheet.addCell(new Label(0, rowCount, ((PeptideData) obj).getId(), normalFormat));
                break;
            case MzqData.FEATURE:
                sheet.addCell(new Label(0, rowCount, ((FeatureData) obj).getId(), normalFormat));
                break;
        }
    }

    private int outputRatio(int level, WritableSheet sheet, ArrayList<QuantitationLevel> objects, int rowCount) throws NumberFormatException, WriteException {
        sheet.addCell(new Label(0, rowCount, "Ratios", boldFormat));
        int colCount = 1;
        for (String ratioID : MzqLib.data.getRatios()) {
            sheet.addCell(new Label(colCount, rowCount, ratioID, boldFormat));
            colCount++;
        }
        rowCount++;
        colCount = 1;

        for (QuantitationLevel object : objects) {
            if (object.hasRatio()) {
                printQuantitationLevel(level, sheet, rowCount, object);
                for (String ratioID : MzqLib.data.getRatios()) {
                    Double value = object.getRatio(ratioID);
                    printValue(value, sheet, colCount, rowCount);
                    colCount++;
                }
                rowCount++;
                colCount = 1;
            }
        }
        return rowCount;
    }

    private void outputGlobal(int level, WritableSheet sheet, ArrayList<QuantitationLevel> objects, int rowCount) throws NumberFormatException, WriteException {
        sheet.addCell(new Label(0, rowCount, "Global", boldFormat));
        int colCount = 1;
        for (String columnID : MzqLib.data.control.getElements(level, MzqData.GLOBAL)) {
            sheet.addCell(new Label(colCount, rowCount, columnID, boldFormat));
            colCount++;
        }
        rowCount++;
        colCount = 1;
        for (QuantitationLevel object : objects) {
            if (object.hasGlobal()) {
                printQuantitationLevel(level, sheet, rowCount, object);
                for (String columnID : MzqLib.data.control.getElements(level, MzqData.GLOBAL)) {
                    Double value = object.getGlobal(columnID);
                    printValue(value, sheet, colCount, rowCount);
                    colCount++;
                }
                rowCount++;
                colCount = 1;
            }
        }
    }

    private void outputMetadata(WritableSheet metaSheet) throws NumberFormatException, WriteException {
        int rowCount = 0;
        //analysis summary
        metaSheet.addCell(new Label(0, rowCount, "Analysis Summary", boldFormat));
        rowCount++;
        for (CvParam cv : MzqLib.data.getAnalysisSummary().getCvParam()) {
            metaSheet.addCell(new Label(0, rowCount, cv.getName(), boldFormat));
            if (cv.getValue() != null && cv.getValue().length() > 1 && (!cv.getValue().equalsIgnoreCase("null"))) {
                metaSheet.addCell(new Label(1, rowCount, cv.getValue(), normalFormat));
            }
            rowCount++;
        }
        rowCount++;
        //software
        metaSheet.addCell(new Label(0, rowCount, "Software List", boldFormat));
        rowCount++;
        for (Software software : MzqLib.data.getSoftwareList().getSoftware()) {
            StringBuilder sb = new StringBuilder();
            sb.append(software.getId());
            sb.append(" (Version: ");
            sb.append(software.getVersion());
            sb.append(")");
            metaSheet.addCell(new Label(0, rowCount, sb.toString(), normalFormat));
            rowCount++;
        }
        rowCount++;
        //Assays
        metaSheet.addCell(new Label(0, rowCount, "Assays", boldFormat));
        rowCount++;
        for (Assay assay : MzqLib.data.getAssays()) {
            metaSheet.addCell(new Label(0, rowCount, assay.getId(), boldFormat));
            metaSheet.addCell(new Label(1, rowCount, assay.getName(), normalFormat));
            rowCount++;
        }
        rowCount++;
    }
}