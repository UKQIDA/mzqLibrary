package uk.ac.liv.pgb.mzqlib.idmapper.data;

import java.util.List;

import uk.ac.liv.pgb.jmzqml.model.mzqml.AbstractParam;
import uk.ac.liv.pgb.jmzqml.model.mzqml.CvParam;
import uk.ac.liv.pgb.jmzqml.model.mzqml.Feature;
import uk.ac.liv.pgb.jmzqml.model.mzqml.RawFile;
import uk.ac.liv.pgb.jmzqml.model.mzqml.UserParam;
import uk.ac.liv.pgb.mzqlib.idmapper.data.Tolerance.ToleranceUnit;

/**
 * Subclass of Feature
 *
 * @author Da Qi
 * @since 06-Mar-2014 11:24:28
 */
public class ExtendedFeature extends Feature {
    private static final long   serialVersionUID = 107L;
    private static final double PPM_BASE         = 1000000.0;
    private static final double TOLERANCE        = 0.1;
    private final double        lmz;    // left boundary of mz
    private final double        rmz;    // right boundary of mz
    private final double        urt;    // up boundary of rt (min)
    private final double        brt;    // bottom boundary of rt (min)
    private final Feature       feature;

    /**
     * Constructor of ExtendedFeature base on Feature and default m/z window
     * (0.1
     * Da) and retention time window (20 second, 1/3 minute).
     *
     * @param ft Feature
     */
    public ExtendedFeature(final Feature ft) {
        this(ft, new Tolerance(TOLERANCE, ToleranceUnit.DALTON), 1.0 / 3.0);
    }

    /**
     * Constructor of ExtendedFeature base on Feature, m/z window and retention
     * time window.
     *
     * @param ft          the base Feature
     * @param msTolerance the m/z tolerance, can be expressed in Da or ppm, and
     *                    is
     *                    the measurement from the m/z value before and after the feature.
     *                    The MS tolerance is always calculated from the Tolerance object,
     *                    unless it is null or its value is 0.
     * @param rtWin       the retention time measured from top to bottom in
     *                    minutes. Only
     *                    used if the feature mass trace is null or empty.
     */
    public ExtendedFeature(final Feature ft, final Tolerance msTolerance, final double rtWin) {
        super();
        feature = ft;

        List<Double> massT = feature.getMassTrace();

        if (massT == null || massT.isEmpty()) {
            brt = Double.valueOf(ft.getRt()) - rtWin / 2;
            urt = Double.valueOf(ft.getRt()) + rtWin / 2;

            double mzToleranceDaltons = msTolerance.getUnit() == ToleranceUnit.DALTON
                                        ? msTolerance.getTolerance()
                                        : ft.getMz() / ((1 / msTolerance.getTolerance()) * PPM_BASE);

            lmz = ft.getMz() - mzToleranceDaltons;
            rmz = ft.getMz() + mzToleranceDaltons;
        } else {
            brt = massT.get(0);
            urt = massT.get(2);

            if (msTolerance != null && msTolerance.getTolerance() > 0.0) {
                double mzToleranceDaltons = msTolerance.getUnit() == ToleranceUnit.DALTON
                                            ? msTolerance.getTolerance()
                                            : ft.getMz() / ((1 / msTolerance.getTolerance()) * PPM_BASE);

                lmz = ft.getMz() - mzToleranceDaltons;
                rmz = ft.getMz() + mzToleranceDaltons;
            } else {
                lmz = massT.get(1);
                rmz = massT.get(3);
            }
        }
    }

    /**
     * Get bottom boundary retention time of the feature.
     *
     * @return double value of retention time
     */
    public final double getBRT() {
        return brt;
    }

    @Override
    public final String getCharge() {
        return feature.getCharge();
    }

    @Override
    public final String getChromatogramRefs() {
        return feature.getChromatogramRefs();
    }

    @Override
    public final List<CvParam> getCvParam() {
        return feature.getCvParam();
    }

    @Override
    public final String getId() {
        return feature.getId();
    }

    /**
     * Get left boundary m/z of the feature.
     *
     * @return double value of m/z
     */
    public final double getLMZ() {
        return lmz;
    }

    @Override
    public final List<Double> getMassTrace() {
        return feature.getMassTrace();
    }

    @Override
    public final double getMz() {
        return feature.getMz();
    }

    @Override
    public final List<AbstractParam> getParamGroup() {
        return feature.getParamGroup();
    }

    /**
     * Get right boundary m/z of the feature.
     *
     * @return double value of m/z
     */
    public final double getRMZ() {
        return rmz;
    }

    @Override
    public final RawFile getRawFile() {
        return feature.getRawFile();
    }

    @Override
    public final String getRawFileRef() {
        return feature.getRawFileRef();
    }

    @Override
    public final String getRt() {
        return feature.getRt();
    }

    @Override
    public final String getSpectrumRefs() {
        return feature.getSpectrumRefs();
    }

    /**
     * Get up boundary retention time of the feature.
     *
     * @return double value of retention time
     */
    public final double getURT() {
        return urt;
    }

    @Override
    public final List<UserParam> getUserParam() {
        return feature.getUserParam();
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
