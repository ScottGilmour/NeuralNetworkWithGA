package GAModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 7/20/2015.
 */
public class Genome {
    private List<Double> weights;
    private double fitness;

    public Genome() {
        weights = new ArrayList<Double>();
    }

    public List<Double> getWeights() {
        return weights;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
