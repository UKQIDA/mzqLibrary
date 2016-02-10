
package uk.ac.liv.mzqlib.idmapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import uk.ac.liv.pgb.jmzqml.MzQuantMLElement;
import uk.ac.liv.pgb.jmzqml.model.mzqml.EvidenceRef;
import uk.ac.liv.pgb.jmzqml.model.mzqml.Feature;
import uk.ac.liv.pgb.jmzqml.model.mzqml.PeptideConsensus;
import uk.ac.liv.pgb.jmzqml.model.mzqml.PeptideConsensusList;
import uk.ac.liv.pgb.jmzqml.xml.io.MzQuantMLUnmarshaller;

/**
 * The wrapper class for mzQuantML file.
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 24-Jun-2013 14:06:51
 */
public class Mzq {

    private MzQuantMLUnmarshaller um;

    /**
     * Constructor of Mzq class.
     *
     * @param mzqUm the instance of MzQuantMLUnmarshaller
     */
    public Mzq(MzQuantMLUnmarshaller mzqUm) {
        this.um = mzqUm;
    }

    /**
     * Get map of PeptideConsensus id to list of SimpleFeature.
     *
     * @return Map<String, List<SimpleFeature>>
     *
     * @throws JAXBException
     */
    public Map<String, List<SimpleFeature>> getPepIdFeature()
            throws JAXBException {

        Map<String, List<SimpleFeature>> retMap = new HashMap();

        Iterator<PeptideConsensusList> pepConListIter = this.um.unmarshalCollectionFromXpath(MzQuantMLElement.PeptideConsensusList);
        while (pepConListIter.hasNext()) {
            PeptideConsensusList pepConList = pepConListIter.next();
            List<PeptideConsensus> pepCons = pepConList.getPeptideConsensus();

            for (PeptideConsensus pepCon : pepCons) {
                String pepId = pepCon.getId();
                String charge = pepCon.getCharge().get(0);

                List<EvidenceRef> eviRefList = pepCon.getEvidenceRef();
                for (EvidenceRef eviRef : eviRefList) {

                    String ftRef = eviRef.getFeatureRef();

                    Feature ft = this.um.unmarshal(uk.ac.liv.pgb.jmzqml.model.mzqml.Feature.class, ftRef);
                    double mz = ft.getMz();
                    double rw = this.getRetentionWindow(ft.getMassTrace());

                    List<SimpleFeature> sfList = retMap.get(pepId);
                    if (sfList == null) {
                        // initialise a new list of SimpleFeature
                        sfList = new ArrayList();
                        retMap.put(pepId, sfList);
                    }
                    SimpleFeature sf = new SimpleFeature(charge, ftRef, mz, rw);
                    sfList.add(sf);
                }
            }
        }
        return retMap;
    }

    /**
     * The inner static class SimpleFeature.
     *
     * SimpleFeature contains charge,feature id, m/z value and retention time window.
     */
    public static class SimpleFeature {

        /**
         * Feature charge
         */
        private String charge;

        /**
         * Feature ID
         */
        private String ftId;

        /**
         * Feature m/z
         */
        private double mz;

        /**
         * Feature retention time window
         */
        private double retWin;

        /**
         * Constructor of SimpleFeature by initial values.
         *
         * @param chr  feature charge
         * @param ftId feature id
         * @param mz   feature m/z
         * @param rw   feature retention time window
         */
        public SimpleFeature(String chr, String ftId, double mz, double rw) {
            this.charge = chr;
            this.ftId = ftId;
            this.mz = mz;
            this.retWin = rw;
        }

        /**
         * @return the charge
         */
        public String getCharge() {
            return charge;
        }

        /**
         * @param charge the charge to set
         */
        public void setCharge(String charge) {
            this.charge = charge;
        }

        /**
         * @return the ftId
         */
        public String getFtId() {
            return ftId;
        }

        /**
         * @param ftId the ftId to set
         */
        public void setFtId(String ftId) {
            this.ftId = ftId;
        }

        /**
         * @return the mz
         */
        public double getMz() {
            return mz;
        }

        /**
         * @param mz the mz to set
         */
        public void setMz(double mz) {
            this.mz = mz;
        }

        /**
         * @return the retWin
         */
        public double getRetWin() {
            return retWin;
        }

        /**
         * @param retWin the retWin to set
         */
        public void setRetWin(double retWin) {
            this.retWin = retWin;
        }

    }

    private double getRetentionWindow(List<Double> massTrace) {
        return Math.abs(massTrace.get(0) - massTrace.get(2));
    }

}
