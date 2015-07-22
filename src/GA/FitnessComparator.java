package GA;

import GAModels.Genome;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by Scott on 7/20/2015.
 */
public class FitnessComparator implements Comparator<Genome> {
    @Override
    public int compare(Genome g1, Genome g2) {
        Double val = g1.getFitness();
        Double val2 = g2.getFitness();

        int res = val.compareTo(val2);

        if (res > 0) return -1;
        else if (res == 0) return 0;
        else return 1;
    }
}
