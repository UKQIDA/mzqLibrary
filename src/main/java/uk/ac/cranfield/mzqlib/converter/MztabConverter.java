package uk.ac.cranfield.mzqlib.converter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cranfield.mzqlib.MzqLib;
import uk.ac.cranfield.mzqlib.Utils;
import uk.ac.cranfield.mzqlib.data.FeatureData;
import uk.ac.cranfield.mzqlib.data.MzqData;
import uk.ac.cranfield.mzqlib.data.PeptideData;
import uk.ac.cranfield.mzqlib.data.ProteinData;
import uk.ac.ebi.pride.jmztab.model.CV;
import uk.ac.ebi.pride.jmztab.model.MZTabColumnFactory;
import uk.ac.ebi.pride.jmztab.model.MZTabDescription;
import uk.ac.ebi.pride.jmztab.model.Metadata;
import uk.ac.ebi.pride.jmztab.model.Modification;
import uk.ac.ebi.pride.jmztab.model.Modification.Type;
import uk.ac.ebi.pride.jmztab.model.MsRun;
import uk.ac.ebi.pride.jmztab.model.Param;
import uk.ac.ebi.pride.jmztab.model.Peptide;
import uk.ac.ebi.pride.jmztab.model.PeptideColumn;
import uk.ac.ebi.pride.jmztab.model.Protein;
import uk.ac.ebi.pride.jmztab.model.ProteinColumn;
import uk.ac.ebi.pride.jmztab.model.Section;
import uk.ac.ebi.pride.jmztab.model.SplitList;
import uk.ac.liv.jmzqml.model.mzqml.AnalysisSummary;
import uk.ac.liv.jmzqml.model.mzqml.Assay;
import uk.ac.liv.jmzqml.model.mzqml.Cv;
import uk.ac.liv.jmzqml.model.mzqml.CvParam;
import uk.ac.liv.jmzqml.model.mzqml.ModParam;
import uk.ac.liv.jmzqml.model.mzqml.RawFile;
import uk.ac.liv.jmzqml.model.mzqml.RawFilesGroup;
import uk.ac.liv.jmzqml.model.mzqml.Software;
import uk.ac.liv.jmzqml.model.mzqml.StudyVariable;

/**
 *
 * @author Jun Fan@qmul
 */
public class MztabConverter extends GenericConverter {

    public MztabConverter(String filename, String outputFile) {
        super(filename, outputFile);
    }

    @Override
    public void convert() {
        try {
            BufferedWriter out = null;
            if (outfile.length() == 0) {
                outfile = getBaseFilename() + ".mztab";
            }
            out = new BufferedWriter(new FileWriter(outfile));

            MZTabDescription tabDesc = new MZTabDescription(MZTabDescription.Mode.Complete, MZTabDescription.Type.Quantification);
            tabDesc.setId(MzqLib.data.getMzqID().replace("-", "_"));
            Metadata mtd = new Metadata(tabDesc);
            mtd.setDescription(MzqLib.data.getMzqName());
            //software
            int swCount = 1;
            for (Software software : MzqLib.data.getSoftwareList().getSoftware()) {
                for (CvParam cvParam : software.getCvParam()) {
                    mtd.addSoftwareParam(swCount, Utils.convertMztabParam(cvParam));
                }
                swCount++;
            }
            //input files
            final List<RawFilesGroup> rfgs = MzqLib.data.getInputFiles().getRawFilesGroup();
            HashMap<String, Integer> msruns = new HashMap<String, Integer>();
            for (int i = 0; i < rfgs.size(); i++) {
                RawFilesGroup rfg = rfgs.get(i);
                MsRun msrun = new MsRun(i + 1);
                List<RawFile> rawFileList = rfg.getRawFile();
                StringBuilder sb = new StringBuilder();
                sb.append("file://");
                for (RawFile raw : rawFileList) {
                    sb.append(raw.getLocation());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                msrun.setLocation(new URL(sb.toString()));
                mtd.addMsRun(msrun);
                msruns.put(rfg.getId(), msrun.getId());
            }
            //assays
            final ArrayList<Assay> assays = MzqLib.data.getAssays();
            HashMap<String, uk.ac.ebi.pride.jmztab.model.Assay> assayMap = new HashMap<String, uk.ac.ebi.pride.jmztab.model.Assay>();
            ArrayList<uk.ac.ebi.pride.jmztab.model.Assay> tabAssays = new ArrayList<uk.ac.ebi.pride.jmztab.model.Assay>();
            for (int i = 0; i < assays.size(); i++) {
                Assay assay = assays.get(i);
                uk.ac.ebi.pride.jmztab.model.Assay tabAssay = new uk.ac.ebi.pride.jmztab.model.Assay(i + 1);
                for (ModParam mod : assay.getLabel().getModification()) {
                    CvParam modParam = mod.getCvParam();
                    tabAssay.setQuantificationReagent(Utils.convertMztabParam(modParam));
                }
                tabAssay.setMsRun(mtd.getMsRunMap().get(msruns.get(assay.getRawFilesGroupRef())));
                mtd.addAssay(tabAssay);
                tabAssays.add(tabAssay);
                assayMap.put(assay.getId(), tabAssay);
            }

            ArrayList<String> names = MzqLib.data.getQuantitationNames();
            CvParam proCvParam = determineQuantitationUnit(MzqData.PROTEIN, names);
            CvParam pepCvParam = determineQuantitationUnit(MzqData.PEPTIDE, names);
            if (proCvParam != null) {
                mtd.setProteinQuantificationUnit(Utils.convertMztabParam(proCvParam));
            }
            if (pepCvParam != null) {
                mtd.setPeptideQuantificationUnit(Utils.convertMztabParam(pepCvParam));
            }
            mtd.setQuantificationMethod(determineQuantitationMethod(MzqLib.data.getAnalysisSummary()));
            //cvs
            final List<Cv> cvs = MzqLib.data.getCvList();
            for (int i = 0; i < cvs.size(); i++) {
                Cv cv = cvs.get(i);
                CV tabCv = new CV(i + 1);
                tabCv.setFullName(cv.getFullName());
                tabCv.setLabel(cv.getId());
                tabCv.setVersion(cv.getVersion());
                tabCv.setUrl(cv.getUri());
                mtd.addCV(tabCv);
            }
            //study variables
            final List<StudyVariable> svs = MzqLib.data.getSvs();
            ArrayList<uk.ac.ebi.pride.jmztab.model.StudyVariable> tabSvs = new ArrayList<uk.ac.ebi.pride.jmztab.model.StudyVariable>();
            if (svs.isEmpty()){
                for (int i = 0; i < assays.size(); i++) {
                    Assay assay = assays.get(i);
                    uk.ac.ebi.pride.jmztab.model.StudyVariable tabSv = new uk.ac.ebi.pride.jmztab.model.StudyVariable(i+1);
                    tabSv.setDescription("manually created study variable for assay "+assay.getId());
                    tabSv.addAssay(assayMap.get(assay.getId()));
                    mtd.addStudyVariable(tabSv);
                    tabSvs.add(tabSv);
                }
            }else{
                for (int i = 0; i < svs.size(); i++) {
                    StudyVariable studyVariable = svs.get(i);
                    uk.ac.ebi.pride.jmztab.model.StudyVariable tabSv = new uk.ac.ebi.pride.jmztab.model.StudyVariable(i + 1);
                    tabSv.setDescription(studyVariable.getName());
                    for (Assay assay : studyVariable.getAssays()) {
                        tabSv.addAssay(assayMap.get(assay.getId()));
                    }
                    mtd.addStudyVariable(tabSv);
                    tabSvs.add(tabSv);
                }
            }
//            System.out.println(mtd.toString());
            out.append(mtd.toString());
            
//            String searchEngineStr = getSearchEngineString();
//            Param searchEngine = null;
//            String searchEngineScoreAccession = null;
//            String searchEngineScoreName = null;
//            String cvID = null;
//            if (searchEngineStr != null) {
//                String[] elements = searchEngineStr.split(";");
//                cvID = elements[0];
//                searchEngine = new uk.ac.ebi.pride.jmztab.model.CVParam(cvID, elements[3], elements[4], null);
//                searchEngineScoreAccession = elements[1];
//                searchEngineScoreName = elements[2];
//            }
            //add mandatory columns in the complete and quantitation combination
            //see https://code.google.com/p/mztab/wiki/jmzTab2_column table 1 and 2
            MZTabColumnFactory proFactory = MZTabColumnFactory.getInstance(Section.Protein);
            //assay and sv are mandatory 
            for (uk.ac.ebi.pride.jmztab.model.Assay tabAssay : tabAssays) {
                proFactory.addAbundanceOptionalColumn(tabAssay);
            }
            for (uk.ac.ebi.pride.jmztab.model.StudyVariable sv : tabSvs) {
                proFactory.addAbundanceOptionalColumn(sv);
            }
            
            MZTabColumnFactory pepFactory = MZTabColumnFactory.getInstance(Section.Peptide);
            for (uk.ac.ebi.pride.jmztab.model.Assay tabAssay : tabAssays) {
                pepFactory.addAbundanceOptionalColumn(tabAssay);
            }
            for (uk.ac.ebi.pride.jmztab.model.StudyVariable sv : tabSvs) {
                pepFactory.addAbundanceOptionalColumn(sv);
            }
            
            for(MsRun msrun:mtd.getMsRunMap().values()){
                proFactory.addOptionalColumn(ProteinColumn.SEARCH_ENGINE_SCORE, msrun);
                proFactory.addOptionalColumn(ProteinColumn.NUM_PEPTIDES_DISTINCT, msrun);
                proFactory.addOptionalColumn(ProteinColumn.NUM_PEPTIDES_UNIQUE, msrun);
                pepFactory.addOptionalColumn(PeptideColumn.SEARCH_ENGINE_SCORE, msrun);
            }

            StringBuilder proSb = new StringBuilder();
            proSb.append(proFactory.toString());
            proSb.append("\n");
            StringBuilder pepSb = new StringBuilder();
            pepSb.append(pepFactory.toString());
            pepSb.append("\n");

            for (ProteinData protein : MzqLib.data.getProteins()) {
                Protein tabProt = new Protein(proFactory);
                tabProt.setAccession(protein.getAccession());
                final String searchDatabase = protein.getSearchDatabase();
                tabProt.setDatabase(searchDatabase);
                final String searchDatabaseVersion = protein.getSearchDatabaseVersion();
                tabProt.setDatabaseVersion(searchDatabaseVersion);
                if (proCvParam != null) {
                    if (MzqLib.data.control.isRequired(MzqData.PROTEIN, MzqData.ASSAY, proCvParam.getName())) {
                        for (int i = 0; i < assays.size(); i++) {
                            String assay = assays.get(i).getId();
                            final Double value = protein.getQuantity(proCvParam.getName(), assay);
                            if (value!=null) tabProt.setAbundanceColumn(assayMap.get(assay), value);
                        }
                    }

                    if (MzqLib.data.control.isRequired(MzqData.PROTEIN, MzqData.SV, proCvParam.getName())) {
                        for (int i = 0; i < svs.size(); i++) {
                            StudyVariable sv = svs.get(i);
                            final Double value = protein.getStudyVariableQuantity(proCvParam.getName(), sv.getId());
                            if (value!=null) tabProt.setAbundanceColumn(tabSvs.get(i), value);
                        }
                    }
                }
//                System.out.println(tabProt);
                proSb.append(tabProt.toString());
                proSb.append("\n");

                for (PeptideData peptide : protein.getPeptides()) {
                    for (int charge : peptide.getCharges()) {
                        Peptide tabPep = new Peptide(pepFactory, mtd);
                        tabPep.setSequence(peptide.getSeq());
                        tabPep.setAccession(protein.getAccession());
                        tabPep.setDatabase(searchDatabase);
                        tabPep.setDatabaseVersion(searchDatabaseVersion);
//                        if (searchEngine != null) {
//                            tabPep.setSearchEngine(searchEngine.getName());
//                            uk.ac.ebi.pride.jmztab.model.ParamList paramList = new uk.ac.ebi.pride.jmztab.model.ParamList();
//                            paramList.add(new Param(cvID, searchEngineScoreAccession, searchEngineScoreName, String.valueOf(peptide.getGlobal(searchEngineScoreName))));
//                            tabPep.setSearchEngineScore(paramList);
//                        }
                        final List<uk.ac.liv.jmzqml.model.mzqml.Modification> modifications = peptide.getPeptide().getModification();
                        if (!modifications.isEmpty()) {
                            SplitList<Modification> mods = new SplitList<Modification>(' ');
                            for (int i = 0; i < modifications.size(); i++) {
                                uk.ac.liv.jmzqml.model.mzqml.Modification modification = modifications.get(i);
//                                //<xsd:element name="cvParam" type="CVParamType" minOccurs="1"
                                CvParam param = modification.getCvParam().get(0);
                                Type type = Modification.findType(param.getCvRef());
//                                type = Modification.Type.UNKNOW;
//                                if(param.getCv().getId().equals("UNIMOD")){
//                                    type = Modification.Type.UNIMOD;
//                                }else if(param.getCv().getId().equals("PSI-MOD")){
//                                    type = Modification.Type.MOD;
//                                }
                                Modification mod = new Modification(Section.Peptide, type, param.getAccession());
                                mods.add(mod);
                            }
                            tabPep.setModifications(mods);;
                        }
                        tabPep.setCharge(charge);
                        ArrayList<Double> mzs = new ArrayList<Double>();
                        SplitList<Double> rts = new SplitList<Double>(' ');
                        for (FeatureData feature : peptide.getFeaturesWithCharge(charge)) {
                            //<xsd:attribute name="mz" type="xsd:double" use="required">
                            mzs.add(feature.getFeature().getMz());
                            //<xsd:attribute name="rt" type="doubleOrNullType" use="required">
                            //on the retention time axis in minutes
                            //in mzTab retention_time Double List (“,”) Time points in seconds.
                            String rtStr = feature.getFeature().getRt();
                            if (rtStr != null && !rtStr.equalsIgnoreCase("null")) {
                                double rt = 60 * Double.parseDouble(rtStr);
                                rts.add(rt);
                            }
                        }
                        tabPep.setMassToCharge(Utils.mean(mzs));
                        tabPep.setRetentionTime(rts);
                        if (pepCvParam != null) {
                            if (MzqLib.data.control.isRequired(MzqData.PEPTIDE, MzqData.ASSAY, pepCvParam.getName())) {
                                for (int i = 0; i < assays.size(); i++) {
                                    String assay = assays.get(i).getId();
                                    final Double value = peptide.getQuantity(pepCvParam.getName(), assay);
                                    if(value!=null) tabPep.setAbundanceColumn(assayMap.get(assay), value);
                                }
                            }
                            if (MzqLib.data.control.isRequired(MzqData.PEPTIDE, MzqData.SV, pepCvParam.getName())) {
                                for (int i = 0; i < svs.size(); i++) {
                                    StudyVariable sv = svs.get(i);
                                    final Double value = peptide.getStudyVariableQuantity(pepCvParam.getName(), sv.getId());
                                    if(value!=null) tabPep.setAbundanceColumn(tabSvs.get(i), value);
                                }
                            }
                        }
                        pepSb.append(tabPep);
                        pepSb.append("\n");
                    }
                }
            }
//            System.out.println("");
//            System.out.println(pepSb.toString());
            out.append("\n");
            out.append(proSb.toString());
            out.append("\n");
            out.append(pepSb.toString());
//            out.append(mztab.toMzTab());
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(MztabConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private CvParam determineQuantitationUnit(int level, ArrayList<String> names) {
        for (String name : names) {
            if (MzqLib.data.control.isRequired(level, MzqData.ASSAY, name)) {
                return MzqLib.data.getQuantitationCvParam(name);
            }
        }
        return null;
    }

    private Param determineQuantitationMethod(AnalysisSummary analysisSummary) {
        for (CvParam cvParam : analysisSummary.getCvParam()) {
            String accession = cvParam.getAccession();
            if (accession.contains("MS:1001834")
                    || accession.contains("MS:1001836")
                    || accession.contains("MS:1002023")
                    || accession.contains("MS:1002018")) {
                return Utils.convertMztabParam(cvParam);

            }
        }
        return null;
    }

    private String getSearchEngineString() {
        for (String name : MzqLib.data.control.getElements(MzqData.PEPTIDE, MzqData.GLOBAL)) {
            CvParam param = MzqLib.data.getQuantitationCvParam(name);
//            String value = ((Cv) param.getCvRef()).getId();
//            String value = ((Cv) param.getCv()).getId();
            String value = param.getCvRef();
            if (param != null) {
                String accession = param.getAccession();
                if (accession.contains("MS:1001171")) {
                    return value + ";MS:1001171;Mascot:score;MS:1001207;Mascot";
                }
                if (accession.contains("MS:1001390")) {
                    return value + ";MS:1001390;Phenyx:Score;MS:1001209;Phenyx";
                }
                if (accession.contains("MS:1001492")) {
                    return value + ";MS:1001492;percolator:score;MS:1001490;percolator";
                }
            }
        }
        return null;
    }
}
