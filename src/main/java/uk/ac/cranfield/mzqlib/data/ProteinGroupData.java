package uk.ac.cranfield.mzqlib.data;

import java.util.List;
import uk.ac.liv.jmzqml.model.mzqml.ProteinGroup;
import uk.ac.liv.jmzqml.model.mzqml.ProteinRef;

/**
 *
 * @author Jun
 */
public class ProteinGroupData extends QuantitationLevel{
    private ProteinGroup pg;
    private final static String SEPARATOR = ";";
    public ProteinGroupData (ProteinGroup proteinGroup){
        pg = proteinGroup;
    }

    public ProteinGroup getPg() {
        return pg;
    }
    
    public String getAnchorProteinStr(){
        ProteinRef lead = pg.getProteinRef().get(0);//ProteinRef 1:n
        return lead.getProteinRef();
    }
    
    public String getAmbiguityMemberStr(){
        List<ProteinRef> proteinRefs = pg.getProteinRef();
        if (proteinRefs.size()==1) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < proteinRefs.size(); i++) {
            ProteinRef ref = proteinRefs.get(i);
            sb.append(ref.getProteinRef());
            sb.append(SEPARATOR);
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
