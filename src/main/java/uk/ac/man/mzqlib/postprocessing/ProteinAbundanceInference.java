package uk.ac.man.mzqlib.postprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.System.exit;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import uk.ac.liv.jmzqml.MzQuantMLElement;
import uk.ac.liv.jmzqml.model.mzqml.*;
import uk.ac.liv.jmzqml.xml.io.MzQuantMLMarshaller;
import uk.ac.liv.jmzqml.xml.io.MzQuantMLUnmarshaller;

/**
 * This code is created for calculating protein abundance inference
 *
 * @author man-mqbsshz2
 * @version 0.2
 */
public final class ProteinAbundanceInference {

    private static final Map<String, HashSet<String>> proteinToPeptide = new HashMap<String, HashSet<String>>();
    private static final Map<String, HashSet<String>> peptideToProtein = new HashMap<String, HashSet<String>>();
    private Map<String, List<String>> peptideAssayValues = new HashMap<String, List<String>>();
    private static final Map<String, String> proteinToAccession = new HashMap<String, String>();

    private static Map<String, HashSet<String>> sameSetGroup = new HashMap<String, HashSet<String>>();
    private static Map<String, HashSet<String>> subSetGroup = new HashMap<String, HashSet<String>>();
    private static Map<String, HashSet<String>> uniSetGroup = new HashMap<String, HashSet<String>>();

    final static String path = "./src/main/resources/";
    final static String proteinGroupList = "ProteinGroupList11";
    final static String searchDatabase = "SD1";
    final static String cvParamId = "PSI-MS";
    final static String cvRef = "PSI-MS";

    private String in_file;
    private String out_file;
    private String quantLayerType;
    private String abundanceOperation;
    private String inputDataTypeAccession;
    private String inputRawDataTypeAccession;
    private String outputProteinGroupDTAccession;
    private String outputRawProteinGroupDTAccession;
    private String outputProteinGroupDTName;
    private String outputRawProteinGroupDTName;
    private String outputAssayQuantLayerID;
    private String outputRawAssayQuantLayerID;

    /**
     * set quant layer type
     *
     * @param qlt - quant layer type
     */
    public void setQuantLayerType(String qlt) {
        quantLayerType = qlt;
    }

    /**
     * set calculation operator
     *
     * @param cal - operator
     */
    public void setCalOperation(String cal) {
        abundanceOperation = cal;
    }

    /**
     * set input peptide datatype accession
     *
     * @param pdta - datatype accession
     */
    public void setInPDTA(String pdta) {
        inputDataTypeAccession = pdta;
    }

    /**
     * set input raw data peptide datatype accession
     *
     * @param rdta - datatype accession
     */
    public void setInRawPDTA(String rdta) {
        inputRawDataTypeAccession = rdta;
    }

    /**
     * set output protein group datatype accession
     *
     * @param pgdta - datatype accession
     */
    public void setOutPGDTA(String pgdta) {
        outputProteinGroupDTAccession = pgdta;
    }

    /**
     * set output protein group datatype name
     *
     * @param pgdtn - datatype name
     */
    public void setOutPGDTN(String pgdtn) {
        outputProteinGroupDTName = pgdtn;
    }

    /**
     * set output raw protein group datatype accession
     *
     * @param rpgdta - datatype accession
     */
    public void setOutRawPGDTA(String rpgdta) {
        outputRawProteinGroupDTAccession = rpgdta;
    }

    /**
     * set output raw protein group datatype name
     *
     * @param rpgdtn - datatype name
     */
    public void setOutRawPGDTN(String rpgdtn) {
        outputRawProteinGroupDTName = rpgdtn;
    }

    /**
     * constructor
     *
     * @param in_file - input file
     * @param out_file - output file
     * @param abundanceOperation - calculation operator
     * @param inputDataTypeAccession - input datatype accession
     * @param inputRawDataTypeAccession - input raw datatype accession
     * @param outputProteinGroupDTAccession - output protein group datatype
     * accession
     * @param outputProteinGroupDTName - output protein group datatype name
     * @param outputRawProteinGroupDTAccession - output raw protein group
     * datatype accession
     * @param outputRawProteinGroupDTName - output raw protein group datatype
     * name
     * @param QuantLayerType - quant layer type
     *
     * @throws FileNotFoundException
     */
    public ProteinAbundanceInference(String in_file, String out_file,
            String abundanceOperation,
            String inputDataTypeAccession,
            String inputRawDataTypeAccession,
            String outputProteinGroupDTAccession,
            String outputProteinGroupDTName,
            String outputRawProteinGroupDTAccession,
            String outputRawProteinGroupDTName,
            String QuantLayerType)
            throws FileNotFoundException {

        String cvAccessionPrefix = "MS:";
        int cvAccssionLength = 10;
        int cvAccessionLastSevenNumMax = 1002437;

        int length1 = inputDataTypeAccession.length();
        int length2 = outputProteinGroupDTAccession.length();
        String inputCvAccessionSuffix = inputDataTypeAccession.substring(3, inputDataTypeAccession.length() - 3);
        String outputCvAccessionSuffix = outputProteinGroupDTAccession.substring(3, outputProteinGroupDTAccession.length() - 3);
        String qlt1 = "AssayQuantLayer";
        String qlt2 = "RatioQuantLayer";
        String qlt3 = "StudyVariableQuantLayer";
        String co1 = "sum";
        String co2 = "mean";
        String co3 = "median";

        if (!(length1 == cvAccssionLength)) {
            throw new IllegalArgumentException("Invalid Input Peptide Datatype Parameter!!! " + inputDataTypeAccession);
        }

        if (!(inputDataTypeAccession.substring(0, 3).equals(cvAccessionPrefix)) || !(Integer.parseInt(inputCvAccessionSuffix) >= 0)
                || !(Integer.parseInt(inputCvAccessionSuffix) <= cvAccessionLastSevenNumMax)) {
            throw new IllegalArgumentException("Wrong Input Peptide Datatype CV Accession!!! " + inputDataTypeAccession);
        }

        if (!(length2 == cvAccssionLength)) {
            throw new IllegalArgumentException("Invalid Output Protein Group CV Accession!!! " + outputProteinGroupDTAccession);
        }

        if (!(outputProteinGroupDTAccession.substring(0, 3).equals(cvAccessionPrefix) && (Integer.parseInt(outputCvAccessionSuffix) >= 0)
                && (Integer.parseInt(outputCvAccessionSuffix) <= cvAccessionLastSevenNumMax))) {
            throw new IllegalArgumentException("Wrong Output Protein Group CV Accession!!! " + outputProteinGroupDTAccession);
        }

        if (!textHasContext(outputProteinGroupDTName)) {
            throw new IllegalArgumentException("Invalid Output Protein Group CV Name!!! " + outputProteinGroupDTName);
        }

//        if (!textHasContext(QuantLayerType)) {
//            throw new IllegalArgumentException("Invalid Quant Layer Type!!!");
//        }
        if (!(QuantLayerType.equals(qlt1) || QuantLayerType.equals(qlt2) || QuantLayerType.equals(qlt3))) {
            throw new IllegalArgumentException("Invalid Quant Layer Type!!! " + QuantLayerType);
        }

        if (!(abundanceOperation.equals(co1) || abundanceOperation.equals(co2) || abundanceOperation.equals(co3))) {
            throw new IllegalArgumentException("Method slected is not correct: " + abundanceOperation);
        }

        this.in_file = in_file;
        this.out_file = out_file;
        this.abundanceOperation = abundanceOperation;
        this.inputDataTypeAccession = inputDataTypeAccession;
        this.inputRawDataTypeAccession = inputRawDataTypeAccession;
        this.outputProteinGroupDTAccession = outputProteinGroupDTAccession;
        this.outputProteinGroupDTName = outputProteinGroupDTName;
        this.outputRawProteinGroupDTAccession = outputRawProteinGroupDTAccession;
        this.outputRawProteinGroupDTName = outputRawProteinGroupDTName;
        this.quantLayerType = QuantLayerType;

    }

    /**
     * main method
     *
     * @param args - paramters for command line
     *
     * @throws JAXBException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     */
    public static void main(String[] args)
            throws JAXBException, InstantiationException,
            IllegalAccessException, IllegalStateException, FileNotFoundException {

        String quantLT = "AssayQuantLayer";

        String refNo = "RefAssay2";
        String filter = "qvalue001";

//        String infile = "C:\\Manchester\\work\\Puf3Study\\ProteoSuite\\IdenOutcomes\\Filtered_AJ_" + filter
//                + "\\mzq\\consensusonly\\test\\unlabeled_result_FLUQT_mapped_" + filter
//                + "_peptideNormalization_medianSelectedRefAssay_25Oct.mzq";
//
//        String outfile = "C:\\Manchester\\work\\Puf3Study\\ProteoSuite\\Idenoutcomes\\Filtered_AJ_" + filter
//                + "\\mzq\\consensusonly\\test\\unlabeled_result_FLUQT_mapped_" + filter
//                + "_peptideNormalized" + refNo + "_protein_inference_revDecoyTest_25Oct_samesetOrdered.mzq";
//        String infile = "C:\\Manchester\\work\\Puf3Study\\ProteoSuite\\"
//                + "IdenOutcomes\\CPTAC_EvsB\\qvalue001\\mzq\\"
//                + "unlabeled_result_FLUQT_mapped_normalised_peptide_speciesNormalised.mzq";
//                + "unlabeled_result_FLUQT_mapped_normalised_peptide.mzq";
//                + "unlabeled_result_FLUQT_mapped_normalised_simon.mzq";
//                + "unlabeled_result_FLUQT_mapped_speciesNormalised_peptide_1_0.mzq";
        String infile = "C:\\Manchester\\work\\ProteoSuite\\proteosuite-0.3.5\\puf3\\"
                + "unlabeled_result_FLUQT__mapped_v0.3.5_peptide_normalised.mzq";

//        String outfile = "C:\\Manchester\\work\\Puf3Study\\ProteoSuite\\"
//                + "IdenOutcomes\\CPTAC_EvsB\\qvalue001\\mzq\\"
//                + "unlabeled_result_FLUQT_mapped_normalised_peptide_speciesNormalised_proteinInference.mzq";
//                + "unlabeled_result_FLUQT_mapped_normalised_peptide_proteinInference.mzq";
//                + "unlabeled_result_FLUQT_mapped_normalised_simon_proteinInference.mzq";
//                + "unlabeled_result_FLUQT_mapped_speciesNormalised_peptide_proteinInference_1_0.mzq";
        String outfile = "C:\\Manchester\\work\\ProteoSuite\\proteosuite-0.3.5\\puf3\\"
                + "unlabeled_result_FLUQT__mapped_v0.3.5_peptide_normalised_proteinInference_filtered.mzq";

        String operator = "sum"; //"median", "mean"

        String inputPeptideDTCA = null;
        String inputRawPeptideDTCA = null;
        String outputProteinGCA = null;
        String outputProteinGCN = null;
        String outputRawProteinGCA = null;
        String outputRawProteinGCN = null;

        inputPeptideDTCA = "MS:1001891"; //Progenesis:peptide normalised abundance
        inputRawPeptideDTCA = "MS:1001840";  //"MS:1001893"
        outputProteinGCA = "MS:1001890"; //Progenesis:protein normalised abundance
        outputProteinGCN = "Progenesis: protein normalised abundance";
        outputRawProteinGCA = "MS:1001892";
        outputRawProteinGCN = "Progenesis: protein raw abundance";

        //simon data
//        inputPeptideDTCA = "MS:1001850"; //Progenesis:peptide normalised abundance
//        inputRawPeptideDTCA = "MS:1001840";
//        outputProteinGCA = "MS:1002518"; //Progenesis:protein normalised abundance
//        outputProteinGCN = "Progenesis: protein normalised abundance";
//        outputRawProteinGCA = "MS:1002519";
//        outputRawProteinGCN = "Progenesis: protein raw abundance";
        if (args.length != 10 && args.length != 0) {
            System.out.println("Please input all eight parameters in order: input file, "
                    + "output file, quant layer type, input normalised peptide datatype CV accession,"
                    + "input raw peptide datatype CV accession, output protein group CV accession,"
                    + "output protein group CV name, output raw protein group CV accession, "
                    + "output raw protein group CV name" + "operator.");
        } else if (args.length == 10) {
            infile = args[0];
            outfile = args[1];
            operator = args[2];
            inputPeptideDTCA = args[3];
            inputRawPeptideDTCA = args[4];
            outputProteinGCA = args[5];
            outputProteinGCN = args[6];
            outputRawProteinGCA = args[7];
            outputRawProteinGCN = args[8];
            quantLT = args[9];
        }

        ProteinAbundanceInference pai = new ProteinAbundanceInference(infile, outfile, operator,
                inputPeptideDTCA, inputRawPeptideDTCA, outputProteinGCA, outputProteinGCN,
                outputRawProteinGCA, outputRawProteinGCN, quantLT);
        pai.proteinInference();

    }

    /**
     * protein inference workflow
     *
     * @throws FileNotFoundException
     */
    public void proteinInference()
            throws FileNotFoundException {

        boolean pipeline_flag = true;

        MzQuantMLUnmarshaller infile_um = null;
        try {
            infile_um = mzqFileInput(in_file);
        } catch (IllegalStateException ex) {
            System.out.println("****************************************************");
            System.out.println("The mzq file is not found!!! Please check the input.");
            System.err.println(ex);
            System.out.println("****************************************************");
        }

        //remove the previous protein group list if existing
        checkProteinGroupList(infile_um);
        //
        pipeline_flag = pipeline_executor(infile_um);

        System.out.println("****************************************************");
        if (pipeline_flag) {
            System.out.println("******* The pipeline does work successfully! *******");
            System.out.println("**** The protein abundance is output correctly! ****");
        } else {

            throw new IllegalStateException("****** Some errors exist within the pipeline *******");
        }
        System.out.println("****************************************************");
    }

    /**
     * execute the methods for mapping, grouping, calculation and output
     *
     * @param infile_um - input unmarshalled mzq file
     *
     * @return - true/false
     */
    public boolean pipeline_executor(MzQuantMLUnmarshaller infile_um) {

        boolean flag = true;
        String inputAssayQLID = "";
        String inputRawAssayQLID = "";
        Map<String, List<String>> peptideRawAssayValues = new HashMap<String, List<String>>();
//        String inputRawPQLID = "AQL_intensity";
        peptideRawAssayValues = peptideAssayValues(infile_um, inputRawDataTypeAccession);
//        peptideAssayValues = peptideAssayValues(infile_um, inputDataTypeAccession);

        //filtering is applied, pepCon has more than 50% of total assays
        peptideAssayValues = peptideValidAssayValues(infile_um, inputDataTypeAccession);
//        System.out.println("peptide raw assay values: " + peptideRawAssayValues);
//System.out.println("peptide assay values: " + peptideAssayValues);
        if (peptideAssayValues == null) {

            throw new IllegalStateException("The desired assay quant layer is not found!!! Please check the input file.");
        }

//        System.out.println("Peptide Assay Values: " + peptideAssayValues);
        proteinToPeptide(infile_um, peptideAssayValues);
        proteinToAccession(infile_um);
        peptideToProtein(proteinToPeptide);

        if (quantLayerType.equals("AssayQuantLayer")) {
            List<QuantLayer<IdOnly>> assayQLs = assayQLs(infile_um);
            inputAssayQLID = assayQuantLayerId(infile_um, inputDataTypeAccession);
            outputAssayQuantLayerID = "PGL_" + inputAssayQLID;
            inputRawAssayQLID = assayQuantLayerId(infile_um, inputRawDataTypeAccession);
            outputRawAssayQuantLayerID = "PGL_raw_" + inputRawAssayQLID;

            MzQuantML mzq = mzq(infile_um);

//        List<QuantLayer> ratioQLs = RatioQLs(infile_um);
//        List<QuantLayer> studyVariableQLs = StudyVariableQLs(infile_um);
            uniSetGroup = ProteinGrouping.uniSetGrouping(peptideToProtein, proteinToPeptide);
            sameSetGroup = ProteinGrouping.sameSetGrouping(peptideToProtein, proteinToPeptide);
            subSetGroup = ProteinGrouping.subSetGrouping(peptideToProtein, proteinToPeptide);

            Map<String, List<String>> proteinAbundance = proteinAbundanceCalculation(abundanceOperation,
                    uniSetGroup, sameSetGroup, subSetGroup, peptideAssayValues);
            Map<String, List<String>> rawProteinAbundance = proteinAbundanceCalculation(abundanceOperation,
                    uniSetGroup, sameSetGroup, subSetGroup, peptideRawAssayValues);

            mzqOutput(infile_um, mzq, assayQLs, abundanceOperation, uniSetGroup, sameSetGroup,
                    subSetGroup, outputAssayQuantLayerID, inputAssayQLID, outputRawAssayQuantLayerID,
                    inputRawAssayQLID, out_file, proteinAbundance, rawProteinAbundance);

//        } else if (quantLayerType.equals("RatioQuantLayer")) {
//
//        } else if (quantLayerType.equals("StudyVariableQuantLayer")) {
//
//            List<QuantLayer> studyVariableQLs = StudyVariableQLs(infile_um);
//            String assayQuantLayerId_pep = inputPQLID; //AssayQuantLayerId(infile_um, inputPepDTCA);
//            outputAssayQuantLayerID = "PGL_" + assayQuantLayerId_pep;
//
//            MzQuantML mzq = Mzq(infile_um);
//
//            uniSetGroup = ProteinGrouping.UniSetGrouping(peptideToProtein, proteinToPeptide);
//            sameSetGroup = ProteinGrouping.SameSetGrouping(peptideToProtein, proteinToPeptide);
//            subSetGroup = ProteinGrouping.SubSetGrouping(peptideToProtein, proteinToPeptide);
//
//           
//            
//            MzqOutput(mzq, studyVariableQLs, abunOperation, uniSetGroup, sameSetGroup,
//                    subSetGroup, outputAssayQuantLayerID, inputPQLID, outputFile,
//                    proteinAbundance, rawProteinAbundance);
        }
        return flag;
    }

    /**
     * examine if there is a protein group list. If existing, remove it.
     *
     * @param um - unmarshalled mzq file
     */
    private void checkProteinGroupList(MzQuantMLUnmarshaller um) {
        MzQuantML mzq = um.unmarshal(MzQuantMLElement.MzQuantML);
        ProteinGroupList protGrList = um.unmarshal(MzQuantMLElement.ProteinGroupList);
        if (protGrList != null) {
            protGrList = null;
            mzq.setProteinGroupList(protGrList);

            MzQuantMLMarshaller marshaller = new MzQuantMLMarshaller(in_file);
            marshaller.marshall(mzq);
        }
    }

    /**
     * String examination
     *
     * @param aText - a character string
     *
     * @return - true/false
     */
    private boolean textHasContext(String aText) {
        String EMPTY_STRING = "";
        return (aText != null) && (!aText.trim().equals(EMPTY_STRING));
    }

    /**
     * unmarshal mzq to java object
     *
     * @param infile - mzq file
     *
     * @return - unmarshalled mzq Java object file
     *
     * @throws IllegalStateException
     * @throws FileNotFoundException
     */
    public static MzQuantMLUnmarshaller mzqFileInput(String infile)
            throws IllegalStateException, FileNotFoundException {
        File mzqFile = new File(infile);
        MzQuantMLUnmarshaller infile_um = new MzQuantMLUnmarshaller(mzqFile);
        return infile_um;
    }

    /**
     * identify peptide assay values using input data type accession and quant
     * layer type (assay quant layer by default)
     *
     * @param in_file_um - unmarshalled mzq file
     * @param inputPepDTCA - input peptide datatype accession
     *
     * @return - peptide assay values
     */
    private Map<String, List<String>> peptideAssayValues(
            MzQuantMLUnmarshaller in_file_um, String inputPepDTCA) {
        boolean first_list = false;
        Map<String, List<String>> peptideAV = new HashMap<String, List<String>>();

        PeptideConsensusList pepConList = in_file_um.unmarshal(MzQuantMLElement.PeptideConsensusList);
        List<QuantLayer<IdOnly>> assayQLs = pepConList.getAssayQuantLayer();
        for (QuantLayer assayQL : assayQLs) {
            if ((assayQL.getDataType().getCvParam().getAccession()).equalsIgnoreCase(inputPepDTCA)) {

                DataMatrix assayDM = assayQL.getDataMatrix();
                List<Row> rows = assayDM.getRow();
                for (Row row : rows) {
                    //get peptide reference
                    String peptideRef = row.getObjectRef();

                    //get value String type
                    List<String> values = row.getValue();

                    peptideAV.put(peptideRef, values);

//                    System.out.println("Peptide ref.: " + row.getObjectRef());
//                System.out.println("Peptide raw abundance: " + values.toString());
                }
                //use the first AQL encountered even if there are multiple AQLs with the same data type
//                System.out.println("Peptide Assay Values: " + peptideAssayValues);
                first_list = true;
                break;
            }
        }
        return peptideAV;
    }

    private Map<String, List<String>> peptideValidAssayValues(
            MzQuantMLUnmarshaller in_file_um, String inputPepDTCA) {
        boolean first_list = false;
        Map<String, List<String>> peptideAV = new HashMap<String, List<String>>();

        PeptideConsensusList pepConList = in_file_um.unmarshal(MzQuantMLElement.PeptideConsensusList);
        List<QuantLayer<IdOnly>> assayQLs = pepConList.getAssayQuantLayer();
        int ass_no_invalid;
        int ass_size;
        for (QuantLayer assayQL : assayQLs) {
            if ((assayQL.getDataType().getCvParam().getAccession()).equalsIgnoreCase(inputPepDTCA)) {

                DataMatrix assayDM = assayQL.getDataMatrix();
                List<Row> rows = assayDM.getRow();

                for (Row row : rows) {
                    ass_no_invalid = 0;
                    //get peptide reference
                    String peptideRef = row.getObjectRef();

                    //get value String type
                    List<String> values = row.getValue();
                    ass_size = values.size();

                    for (String value : values) {

                        if (value.equalsIgnoreCase("null") | value.equalsIgnoreCase("nan")) {
                            ++ass_no_invalid;
                        }
                    }

                    if (ass_no_invalid < (int) Math.round(ass_size / 2)) {
                        peptideAV.put(peptideRef, values);
                    }

//                    System.out.println("ass_size: " + ass_size);
//                    System.out.println("ass_no_invalid: " + ass_no_invalid);
                }
                //use the first AQL encountered even if there are multiple AQLs with the same data type
//                System.out.println("Peptide Assay Values: " + peptideAssayValues);
                first_list = true;
                break;
            }
        }
        return peptideAV;
    }

    /**
     * obtain the map of Protein to Peptide, which the peptides are the existing
     * ones in the quant layer identified by the user
     *
     * @param in_file_um - input unmarshalled mzq file
     * @param pepAssVal - peptide assay values
     *
     * @return protein-to-peptide map
     */
    private Map<String, HashSet<String>> proteinToPeptide(
            MzQuantMLUnmarshaller in_file_um,
            Map<String, List<String>> pepAssVal) {
        Set<String> peptideList = pepAssVal.keySet();
        ProteinList protList = in_file_um.unmarshal(MzQuantMLElement.ProteinList);
        List<Protein> proteins = protList.getProtein();
        for (Protein protein : proteins) {

            //remove the decoy proteins
            String protAccession = protein.getAccession();
            if (!protAccession.contains("XXX_")) {
                //    

                List<String> pepConRefs = protein.getPeptideConsensusRefs();

                /**
                 * generate the protein-to-peptide map
                 */
                HashSet<String> setOfPeptides = new HashSet<String>();
                for (String pepCon : pepConRefs) {
                    //examine whether pepCon is in peptideList (given assayQuantLayer)
                    if (peptideList.contains(pepCon)) {

                        setOfPeptides.add(pepCon);

                        /**
                         * Accession or ID for protein
                         */
//                proteinToPeptide.put(protein.getAccession(), setOfPeptides);
                        proteinToPeptide.put(protein.getId(), setOfPeptides);
//                proteinToAccession.put(protein.getId(), protein.getAccession());
                    }
                }
            }
        }
        return proteinToPeptide;
    }

    /**
     * mapping protein to its accession
     *
     * @param in_file_um - unmarshalled mzq file
     *
     * @return a map for protein-to-accession
     */
    private Map<String, String> proteinToAccession(
            MzQuantMLUnmarshaller in_file_um) {

        ProteinList protList = in_file_um.unmarshal(MzQuantMLElement.ProteinList);
        List<Protein> proteins = protList.getProtein();
        for (Protein protein : proteins) {
            List<String> pepConRefs = protein.getPeptideConsensusRefs();

            /**
             * generate the protein-to-peptide map
             */
            HashSet<String> setOfPeptides = new HashSet<String>();
            for (String pepCon : pepConRefs) {
                setOfPeptides.add(pepCon);

                /*
                 * Accession or ID for protein
                 */
                proteinToAccession.put(protein.getId(), protein.getAccession());
            }
        }
        return proteinToAccession;
    }

    /**
     * obtain the map of Peptide-to-Protein from the map of Protein-to-Peptide
     *
     * @param protToPep - protein-to-peptide map
     *
     * @return a map for peptide-to-protein
     */
    private Map<String, HashSet<String>> peptideToProtein(
            Map<String, HashSet<String>> protToPep) {

        for (Map.Entry<String, HashSet<String>> entry : protToPep.entrySet()) {
            for (String value : entry.getValue()) {
                if (!peptideToProtein.containsKey(value)) {
                    peptideToProtein.put(value, new HashSet<String>());
                }
                peptideToProtein.get(value).add(entry.getKey());
            }
        }

        return peptideToProtein;
    }

    /**
     * obtain the assay quant layer list
     *
     * @param in_file_um - input marshalled mzq file
     *
     * @return
     */
    private List<QuantLayer<IdOnly>> assayQLs(MzQuantMLUnmarshaller in_file_um) {

        PeptideConsensusList pepConList = in_file_um.unmarshal(MzQuantMLElement.PeptideConsensusList);
        List<QuantLayer<IdOnly>> assayQLs = pepConList.getAssayQuantLayer();
        return assayQLs;
    }

    /**
     *
     * @param in_file_um
     *
     * @return
     */
//    private List<QuantLayer<IdOnly>> studyVariableQLs(
//            MzQuantMLUnmarshaller in_file_um) {
//
//        PeptideConsensusList pepConList = in_file_um.unmarshal(MzQuantMLElement.PeptideConsensusList);
//        List<QuantLayer<IdOnly>> sVQLs = pepConList.getStudyVariableQuantLayer();
//        return sVQLs;
//    }
//    private RatioQuantLayer ratioQLs(MzQuantMLUnmarshaller in_file_um) {
//
//        PeptideConsensusList pepConList = in_file_um.unmarshal(MzQuantMLElement.PeptideConsensusList);
//        RatioQuantLayer ratioQLs = pepConList.getRatioQuantLayer();
//        return ratioQLs;
//    }
    /**
     * get the object of MzQuantML from the input file
     *
     * @param in_file_um - input unmarshalled mzq file
     *
     * @return java object
     */
    private MzQuantML mzq(MzQuantMLUnmarshaller in_file_um) {

        MzQuantML mzq = in_file_um.unmarshal(MzQuantMLElement.MzQuantML);
        return mzq;
    }

    /**
     * get the identification of assay quant layer
     *
     * @param in_file_um - input unmarshalled mzq file
     * @param inputPeptideDTCA - input peptide datatype accession
     *
     * @return - assay quant layer ID
     */
    private static String assayQuantLayerId(MzQuantMLUnmarshaller in_file_um,
            String inputPeptideDTCA) {

        String assayQLID = null;
        PeptideConsensusList pepConList = in_file_um.unmarshal(MzQuantMLElement.PeptideConsensusList);

        List<QuantLayer<IdOnly>> assayQLs = pepConList.getAssayQuantLayer();

        for (QuantLayer assayQL : assayQLs) {
            if ((assayQL.getDataType().getCvParam().getAccession()).equalsIgnoreCase(inputPeptideDTCA)) {
                assayQLID = assayQL.getId();
                break;
            }
        }
        return assayQLID;

    }

    /**
     * output the result (protein groups, assay quant layers) with the mzQuantML
     * standard format
     *
     * @param mzq - MzQuantML object file
     * @param assayQLs - assay quant layer list
     * @param operation - calculation operator
     * @param uniSetGr - unique set group map
     * @param sameSetGr - sameset group map
     * @param subSetGr - subset group map
     * @param assayQlId - assay quant layer ID
     * @param inPepQLID - input peptide quant layer ID
     * @param outRawPQlId - output raw peptide quant layer ID
     * @param inRawPepQLID - input raw peptide quant layer ID
     * @param outFile - output mzq file
     * @param protAbundance - protein abundance
     * @param rawProtAbundance - raw protein abundance
     * @return - true/false
     */
    private boolean mzqOutput(MzQuantMLUnmarshaller um,
            MzQuantML mzq, List<QuantLayer<IdOnly>> assayQLs,
            String operation,
            Map<String, HashSet<String>> uniSetGr,
            Map<String, HashSet<String>> sameSetGr,
            Map<String, HashSet<String>> subSetGr,
            String assayQlId, String inPepQLID,
            String outRawPQlId, String inRawPepQLID,
            String outFile,
            Map<String, List<String>> protAbundance,
            Map<String, List<String>> rawProtAbundance) {

        boolean flag = true;
//        Map<String, List<String>> proteinAbundance = ProteinAbundanceCalculation(operation,
//                uniSetGr, sameSetGr, subSetGr, pepAssayVals);
        Map<String, String> groupInOrder = ProteinGrouping.groupInOrder(protAbundance);

        Map<String, String> rawGroupInOrder = ProteinGrouping.groupInOrder(rawProtAbundance);
//        System.out.println("Group in Order:" + groupInOrder.entrySet());
//        Map<String, String> rawGroupInOrder = ProteinGrouping.GroupInOrder(rawProtAbundance);

        //        uk.ac.liv.jmzqml.model.mzqml.ProteinGroupList protGroupList = 
//        uk.ac.liv.jmzqml.model.mzqml.ProteinGroupList.class.newInstance();
//        ProteinGroupList protGroupList = um.unmarshal(MzQuantMLElement.ProteinGroupList);
        ProteinGroupList protGroupList = null;
        protGroupList = new ProteinGroupList();

        protGroupList.setId(proteinGroupList);

        //create protein groups in ProteinGroupList
        proteinGroups(protGroupList, groupInOrder);

        //create assay quant layers for raw peptide abundances
        flag = assayQuantLayers(protGroupList, assayQLs, outRawPQlId, inRawPepQLID,
                rawProtAbundance, rawGroupInOrder, outputRawProteinGroupDTAccession, cvParamId, outputRawProteinGroupDTName);

        //create assay quant layers in ProteinGroupList for normalised peptide abundances
        flag = assayQuantLayers(protGroupList, assayQLs, assayQlId, inPepQLID,
                protAbundance, groupInOrder, outputProteinGroupDTAccession, cvParamId, outputProteinGroupDTName);

        /**
         * Create the ProteinGroupList in mzq
         */
        mzq.setProteinGroupList(protGroupList);

        /**
         * Marshall the created object to MzQuantML The output of MzQuantML
         * format file
         */
//  MzQuantMLMarshaller marshaller = new MzQuantMLMarshaller("CPTAC-Progenesis-small-example_proteinAbundance.mzq");
//  MzQuantMLMarshaller marshaller = new MzQuantMLMarshaller("test_grouping_data_proteinAbundance.mzq");
//  MzQuantMLMarshaller marshaller = new MzQuantMLMarshaller("ProteoSuite_xTracker_fourProteins_result_proteinAbundance.mzq");
        MzQuantMLMarshaller marshaller = new MzQuantMLMarshaller(outFile);
        marshaller.marshall(mzq);

        return flag;

    }

    /**
     * calculate protein abundance with the protein grouping result
     *
     * @param operation - calculation operator
     * @param uniSetGr - unique set group map
     * @param sameSetGr - sameset group map
     * @param subSetGr - subset group map
     * @param pepAssVal - peptide assay values
     *
     * @return - protein abundance
     */
    private Map<String, List<String>> proteinAbundanceCalculation(
            String operation,
            Map<String, HashSet<String>> uniSetGr,
            Map<String, HashSet<String>> sameSetGr,
            Map<String, HashSet<String>> subSetGr,
            Map<String, List<String>> pepAssVal) {

        Map<String, List<String>> protAbu = new HashMap<String, List<String>>();
//        int groupId = 0;
        int groupLeader = 0;

        DecimalFormat df = new DecimalFormat(".000");

        /**
         * unique set case
         */
        for (Map.Entry<String, HashSet<String>> entry : uniSetGr.entrySet()) {
//            groupId++;
            /**
             * get the dimension of peptide assay values
             */

            String pepSelect = entry.getValue().iterator().next();
//            System.out.println("pepSelect: " + pepSelect);
            List<String> assayValTmp = pepAssVal.get(pepSelect);
//            System.out.println("assay value temp: " + assayValTmp);
            //number of assay values in each peptide
            int assaySize = assayValTmp.size();
            //number of peptides correponding to the protein
            int numPeptides = entry.getValue().size();

            /**
             * create an array for calculating the abundance
             */
            double[] operationResult = new double[assaySize + 1];
            double[] operationPepValue = new double[assaySize];
            double[][] matrixPepValue = new double[numPeptides][assaySize];
            int tempNo = 0;

            /**
             * initialization of operationOfpepValues, matrixPepValues
             */
            for (int i = 0; i < assaySize; i++) {
//                sumOfpepValues[i] = 0;

                operationPepValue[i] = 0;
                operationResult[i] = 0;
                for (int j = 0; j < numPeptides; j++) {
                    matrixPepValue[j][i] = 0;
                }
            }
            operationResult[assaySize] = 0;

            for (String peptide : entry.getValue()) {
                List<String> assayValues = pepAssVal.get(peptide);

                for (int j = 0; j < assayValues.size(); j++) {
                    String componentValue = assayValues.get(j);

                    /**
                     * set NaN/Null to zero
                     */
                    double temp = (componentValue.equals("NaN") | componentValue.equals("nan")
                            | componentValue.equals("Null") | componentValue.equals("null"))
                                    ? Double.parseDouble("0") : Double.parseDouble(componentValue);
//                        sumOfpepValues[j] = sumOfpepValues[j] + temp;
                    matrixPepValue[tempNo][j] = temp;
                }
                tempNo++;
            }

            /**
             * calculation the abundance according to operation method
             */
            if (operation.equals("sum")) {
                operationPepValue = Utils.columnSum(matrixPepValue);
            } else if (operation.equals("mean")) {
                operationPepValue = Utils.columnSum(matrixPepValue);
                for (int i = 0; i < operationPepValue.length; i++) {
                    operationPepValue[i] = operationPepValue[i] / numPeptides;
                }
            } else if (operation.equals("median")) {
                double[] tmp = new double[numPeptides];
                for (int j = 0; j < operationPepValue.length; j++) {
                    for (int i = 0; i < numPeptides; i++) {
                        tmp[i] = matrixPepValue[i][j];
                    }
                    operationPepValue[j] = Utils.median(tmp);
                }
            }

            System.arraycopy(operationPepValue, 0, operationResult, 0, assaySize);
            operationResult[assaySize] = groupLeader;
//            operationResult[assaySize + 1] = groupId;

            String[] operationResultFormat = new String[assaySize + 1];
            for (int i = 0; i < assaySize + 1; i++) {
                operationResultFormat[i] = df.format(operationResult[i]);
            }

            List<String> proteinAbundanceList = Arrays.asList(operationResultFormat);
            protAbu.put(entry.getKey(), proteinAbundanceList);
//            proteinAbundance.put("ProteinGroup" + groupId, proteinAbundanceList);
//            }
        }

        /**
         * sameSet case
         */
        for (Map.Entry<String, HashSet<String>> entry : sameSetGr.entrySet()) {

//            groupId++;
            String pepSelect = entry.getValue().iterator().next().toString();

            List<String> assayValTmp = pepAssVal.get(pepSelect);

            //number of assay values in each peptide
            int assaySize = assayValTmp.size();
            //number of peptides correponding to the protein
            int numPeptides = entry.getValue().size();

            /**
             * create an array for calculating the abundance
             */
            double[] operationResult = new double[assaySize + 1];
            double[] operationPepValue = new double[assaySize];
            double[][] matrixPepValue = new double[numPeptides][assaySize];
            int tempNo = 0;

            /**
             * initialization of operationOfpepValues, matrixPepValues
             */
            for (int i = 0; i < assaySize; i++) {
//                sumOfpepValues[i] = 0;

                operationPepValue[i] = 0;
                operationResult[i] = 0;
                for (int j = 0; j < numPeptides; j++) {
                    matrixPepValue[j][i] = 0;
                }
            }
            operationResult[assaySize] = 0;
//            operationResult[assaySize + 1] = 0;

            for (String peptide : entry.getValue()) {
                List<String> assayValues = pepAssVal.get(peptide);
                for (int j = 0; j < assayValues.size(); j++) {
                    String componentValue = assayValues.get(j);

                    /**
                     * set NaN/Null to zero
                     */
                    double temp = (componentValue.equalsIgnoreCase("nan") | componentValue.equalsIgnoreCase("null"))
                            ? Double.parseDouble("0") : Double.parseDouble(componentValue);
                    matrixPepValue[tempNo][j] = temp;
                }
                tempNo++;
            }

            /**
             * calculation the abundance according to operation method
             */
            if (operation.equals("sum")) {
                operationPepValue = Utils.columnSum(matrixPepValue);
            } else if (operation.equals("mean")) {
                operationPepValue = Utils.columnSum(matrixPepValue);
                for (int i = 0; i < operationPepValue.length; i++) {
                    operationPepValue[i] = operationPepValue[i] / numPeptides;
                }
            } else if (operation.equals("median")) {
                double[] tmp = new double[numPeptides];
                for (int j = 0; j < operationPepValue.length; j++) {
                    for (int i = 0; i < numPeptides; i++) {
                        tmp[i] = matrixPepValue[i][j];
                    }
                    operationPepValue[j] = Utils.median(tmp);
                }
            }
            System.arraycopy(operationPepValue, 0, operationResult, 0, assaySize);
            operationResult[assaySize] = groupLeader;
//            operationResult[assaySize + 1] = groupId;

            String[] operationResultFormat = new String[assaySize + 1];
            for (int i = 0; i < assaySize + 1; i++) {
                operationResultFormat[i] = df.format(operationResult[i]);
            }
            List<String> proteinAbundanceList = Arrays.asList(operationResultFormat);

//            List<String> proteinAbundanceList = Arrays.asList(Arrays.toString(operationResult));
            protAbu.put(entry.getKey(), proteinAbundanceList);
//proteinAbundance.put("ProteinGroup" + groupId, proteinAbundanceList);
//            }
        }

        /**
         * subSet case
         */
        for (Map.Entry<String, HashSet<String>> entry : subSetGr.entrySet()) {

//            groupId++;
            String pepSelect = entry.getValue().iterator().next().toString();
//            HashSet<String> proteins = peptideToProtein.get(pepSelect);
            List<String> assayValTmp = pepAssVal.get(pepSelect);
            //number of assay values in each peptide
            int assaySize = assayValTmp.size();
            //number of peptides correponding to the protein
            int numPeptides = entry.getValue().size();

            /**
             * create an array for calculating the abundance
             */
            double[] operationResult = new double[assaySize + 1];
            double[] operationPepValue = new double[assaySize];
            double[][] matrixPepValue = new double[numPeptides][assaySize];
            int tempNo = 0;

            /*
             * initialization of operationOfpepValues, matrixPepValues
             */
            for (int i = 0; i < assaySize; i++) {
//                sumOfpepValues[i] = 0;

                operationPepValue[i] = 0;
                operationResult[i] = 0;
                for (int j = 0; j < numPeptides; j++) {
                    matrixPepValue[j][i] = 0;
                }
            }
            operationResult[assaySize] = 0;

            for (String peptide : entry.getValue()) {
                List<String> assayValues = pepAssVal.get(peptide);
                for (int j = 0; j < assayValues.size(); j++) {
                    String componentValue = assayValues.get(j);

                    /**
                     * set NaN/Null to zero
                     */
                    double temp = (componentValue.equals("NaN") | componentValue.equals("nan")
                            | componentValue.equals("Null") | componentValue.equals("null"))
                                    ? Double.parseDouble("0") : Double.parseDouble(componentValue);
                    matrixPepValue[tempNo][j] = temp;
                }
                tempNo++;
            }

            /**
             * calculation the abundance according to operation method
             */
            if (operation.equals("sum")) {
                operationPepValue = Utils.columnSum(matrixPepValue);
            } else if (operation.equals("mean")) {
                operationPepValue = Utils.columnSum(matrixPepValue);
                for (int i = 0; i < operationPepValue.length; i++) {
                    operationPepValue[i] = operationPepValue[i] / numPeptides;
                }
            } else if (operation.equals("median")) {
                double[] tmp = new double[numPeptides];
                for (int j = 0; j < operationPepValue.length; j++) {
                    for (int i = 0; i < numPeptides; i++) {
                        tmp[i] = matrixPepValue[i][j];
                    }
                    operationPepValue[j] = Utils.median(tmp);
                }
            }
            System.arraycopy(operationPepValue, 0, operationResult, 0, assaySize);
            operationResult[assaySize] = groupLeader;
//            operationResult[assaySize + 1] = groupId;

            String[] operationResultFormat = new String[assaySize + 1];
            for (int i = 0; i < assaySize + 1; i++) {
                operationResultFormat[i] = df.format(operationResult[i]);
            }
            List<String> proteinAbundanceList = Arrays.asList(operationResultFormat);

            protAbu.put(entry.getKey(), proteinAbundanceList);
        }

        return protAbu;
    }

    /**
     * get protein group list from the map of ordered groups
     *
     * @param protGL - protein group list
     * @param groupIO - ordered protein by alphabet
     *
     * @return - protein group
     */
    private void proteinGroups(ProteinGroupList protGL,
            Map<String, String> groupIO) {
        List<ProteinGroup> protGrs = protGL.getProteinGroup();
        for (int i = 1; i < groupIO.size() + 1; i++) {
            for (Map.Entry<String, String> entry : groupIO.entrySet()) {
                if (entry.getValue().equals("ProteinGroup" + i)) {
                    String proteinGroupId = entry.getValue();
                    String proteinGroupIdOri = entry.getKey();

//                    System.out.println("Protein Group ID: " + proteinGroupId);
                    ProteinGroup protGroup = new ProteinGroup();
                    protGroup.setId(proteinGroupId);

                    SearchDatabase searchDb = new SearchDatabase();
                    searchDb.setId(searchDatabase);
                    protGroup.setSearchDatabase(searchDb);

                    List<IdentificationRef> idenRefs = protGroup.getIdentificationRef();
                    IdentificationRef idenRef = new IdentificationRef();
                    IdentificationFile idenFile = new IdentificationFile();
                    idenFile.setId("IdenFile");
                    idenFile.setName("fasf");
                    idenRef.setIdentificationFile(idenFile);

                    idenRefs.add(idenRef);

                    List<ProteinRef> protRefs = protGroup.getProteinRef();
                    String anchorProtein = null;

                    if (proteinGroupIdOri.contains("UniSetGroup")) {
                        String pepSel = uniSetGroup.get(proteinGroupIdOri).iterator().next().toString();
                        String protId = peptideToProtein.get(pepSel).iterator().next().toString();

                        ProteinRef protRef = new ProteinRef();
                        Protein prot = new Protein();

                        prot.setId(protId);
                        prot.setAccession(proteinToAccession.get(protId));
                        protRef.setProtein(prot);

                        List<CvParam> cvParams = protRef.getCvParam();

                        CvParam cvParam = new CvParam();
//                        cvParam.setAccession(prot.getAccession());
                        cvParam.setAccession("MS:1001591");
//                        System.out.println("Protein Accession: " + prot.getAccession());
                        Cv cv = new Cv();
                        cv.setId(cvRef);
                        cvParam.setCv(cv);

                        HashSet<String> setPeptides = uniSetGroup.get(proteinGroupIdOri);
                        String pepTmp = setPeptides.iterator().next();
                        anchorProtein = peptideToProtein.get(pepTmp).iterator().next();

//                        cvParam.setName(anchorProtein);
                        cvParam.setName("anchor protein");
                        CvParamRef cvParamRef = new CvParamRef();
                        cvParamRef.setCvParam(cvParam);
                        cvParams.add(cvParam);
                        protRefs.add(protRef);
                    }

                    if (proteinGroupIdOri.contains("SameSetGroup")) {
                        String pepSel = sameSetGroup.get(proteinGroupIdOri).iterator().next().toString();
//                        System.out.println("peptide selection: " + pepSel);
                        Set<String> protsId = peptideToProtein.get(pepSel);
                        //sort proteins
//                        Set<String> protIds = new TreeSet<String>(protsId);
                        TreeSet<String> protIds = new TreeSet<String>(protsId);
                        Iterator<String> protIdsIter = protIds.iterator();

                        int sig = 0;
                        while (protIdsIter.hasNext()) {
//                        for (String protId : protIds) {
                            String protId = protIdsIter.next();

                            //get the first protein
                            if (sig == 0) {
                                sig = 1;
                            } else {
                                sig = -1;
                            }
                            ProteinRef protRef = new ProteinRef();
                            Protein prot = new Protein();

                            prot.setId(protId);
                            prot.setAccession(proteinToAccession.get(protId));
                            protRef.setProtein(prot);

                            List<CvParam> cvParams = protRef.getCvParam();

                            CvParam cvParam = new CvParam();
//                            cvParam.setAccession(prot.getAccession());
                            if (sig == 1) {
                                cvParam.setAccession("MS:1001591");
                            } else {
                                cvParam.setAccession("MS:1001594");
                            }
//                            System.out.println("Protein Accession: " + prot.getAccession());
                            Cv cv = new Cv();
                            cv.setId(cvRef);
                            cvParam.setCv(cv);

//                            if (sig == 1) {
//                                anchorProtein = protId;
//                            }
//                            cvParam.setName(anchorProtein);
                            if (sig == 1) {
                                cvParam.setName("anchor protein");
                            } else {
                                cvParam.setName("sequence same-set protein");
                            }
                            CvParamRef cvParamRef = new CvParamRef();
                            cvParamRef.setCvParam(cvParam);
                            cvParams.add(cvParam);
                            protRefs.add(protRef);
                        }
                    }

                    if (proteinGroupIdOri.contains("SubSetGroup")) {
                        String pepSel = subSetGroup.get(proteinGroupIdOri).iterator().next().toString();

                        Set<String> protsId = peptideToProtein.get(pepSel);
                        //sort proteins
                        Set<String> protIds = new TreeSet<String>(protsId);
//                        if (proteinGroupIdOri.equalsIgnoreCase("SubSetGroup1")) {
//                        System.out.println( proteinGroupIdOri + " Peptides: " + subSetGroup.get(proteinGroupIdOri));
//                            System.out.println( proteinGroupIdOri + " IDs: " + protsId);
//                             System.out.println( proteinGroupIdOri + protIds);
//                        }
                        //anchor protein: protein with most peptides
                        int pepNo = 0;
                        for (String protId : protIds) {
                            int pepNoTmp = proteinToPeptide.get(protId).size();

                            if (pepNoTmp > pepNo) {
                                pepNo = pepNoTmp;
                                anchorProtein = protId;
                            }
                        }

//                            System.out.println("Anchor Protein: " + proteinGroupIdOri + anchorProtein);
                        for (String protId : protIds) {

                            ProteinRef protRef = new ProteinRef();
                            Protein prot = new Protein();

                            prot.setId(protId);
                            prot.setAccession(proteinToAccession.get(protId));
                            protRef.setProtein(prot);

                            List<CvParam> cvParams = protRef.getCvParam();

                            CvParam cvParam = new CvParam();
//                            cvParam.setAccession(prot.getAccession());
                            if (protId.equals(anchorProtein)) {
                                cvParam.setAccession("MS:1001591");
                            } else {
                                cvParam.setAccession("MS:1001596");
                            }
//                            System.out.println("Protein Accession: " + prot.getAccession());
                            Cv cv = new Cv();
                            cv.setId(cvRef);
                            cvParam.setCv(cv);

//                            cvParam.setName(anchorProtein);
                            if (protId.equals(anchorProtein)) {
                                cvParam.setName("anchor protein");
                            } else {
                                cvParam.setName("sequence sub-set protein");
                            }
                            CvParamRef cvParamRef = new CvParamRef();
                            cvParamRef.setCvParam(cvParam);
                            cvParams.add(cvParam);
                            protRefs.add(protRef);
                        }
                    }
                    protGrs.add(i - 1, protGroup);
                }
            }
        }
    }

    /**
     * create assay quant layers using the information of CvParams, calculated
     * abundances and grouping results.
     *
     * @param protGroList - protein group list
     * @param aQLs - assay quant layers
     * @param assayQI - assay quant layer ID
     * @param inputPepDTCA - input peptide datatype accession
     * @param protAbun - protein abundance
     * @param groupInOrd - ordered protein group
     *
     * @return a boolean value for confirming whether the layers of assay quant
     * are created correctly
     */
    private boolean assayQuantLayers(ProteinGroupList protGroList,
            List<QuantLayer<IdOnly>> aQLs,
            String assayQI,
            String inPQLID,
            Map<String, List<String>> protAbun,
            Map<String, String> groupInOrd,
            String cvPA, String cvPI, String cvPN) {
        boolean first_layer = false;
        List<QuantLayer<IdOnly>> assayQuantLayers = protGroList.getAssayQuantLayer();
        QuantLayer assayQuantLayer = new QuantLayer();
        assayQuantLayer.setId(assayQI);

        /**
         * Create the part of DataType
         */
        CvParam cvParam1 = new CvParam();
        cvParam1.setAccession(cvPA);
        Cv cv = new Cv();
        cv.setId(cvPI);
        cvParam1.setCv(cv);
        cvParam1.setName(cvPN);
        CvParamRef cvParamRef1 = new CvParamRef();
        cvParamRef1.setCvParam(cvParam1);
        assayQuantLayer.setDataType(cvParamRef1);

        /**
         * Create the part of ColumnIndex
         */
        /**
         * Get the column indices from the QuantLayer in the original file and
         * then add these to the generated QuantLayer in ProteinGroup
         */
        for (QuantLayer assayQL : aQLs) {
            if (assayQL.getId().equalsIgnoreCase(inPQLID)) {

                List<String> assayCI = (List<String>) assayQL.getColumnIndex();
                int nCI = assayCI.size();
                for (int i = 0; i < nCI; i++) {
                    assayQuantLayer.getColumnIndex().add(assayCI.get(i));
                }
                //use the first APL encountered even if having multiple APLs with the same datatype
                first_layer = true;
                break;
            }
        }
        if (first_layer == false) {
            System.out.println("The desired assay quant layer is not found!!! "
                    + "Please check the input data type accession.");
            exit(1);
        }

        /**
         * Create the part of DataMatrix
         */
        DataMatrix dm = new DataMatrix() {
        };

        /**
         * make the records in order when outputing
         */
        Map<String, List<String>> proteinAbundanceTmp = new HashMap<String, List<String>>();
        for (Map.Entry<String, List<String>> entry : protAbun.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            String newKey = groupInOrd.get(key);
            proteinAbundanceTmp.put(newKey, values);

        }

        /**
         * Alternatively, use tree map to sort the records in DataMatrix for
         * output
         */
//        Map<String, List<String>> treeMap = new TreeMap<String, List<String>>(pa);
//        DataMatrix dMatrix = SortedMap(treeMap, dm, groupInOrder);
        Map<String, List<String>> treeMap = new TreeMap<String, List<String>>(proteinAbundanceTmp);
        DataMatrix dMatrix = Utils.sortedMap(treeMap, dm);

        /**
         * the data is input in the created QuantLayer
         */
        assayQuantLayer.setDataMatrix(dMatrix);
        assayQuantLayers.add(assayQuantLayer);
        return first_layer;
    }

}
