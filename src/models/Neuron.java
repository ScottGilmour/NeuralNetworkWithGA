package models;


import java.util.Random;
import java.util.Vector;

/**
 * Created by Scott on 7/17/2015.
 */
public class Neuron {
    private Vector<Double> weights;
    private double inputs;
    private Random random = new Random(666);

    public boolean activation() {


        return false;
    }

    public Neuron(int numOfInputs) {
        weights = new Vector<Double>();
        inputs = numOfInputs; //Bias

        for (int i = 0; i < numOfInputs + 1; i++) {
            weights.add(random.nextDouble());
        }
    }

    public double getInputs() {
        return inputs;
    }

    public Vector<Double> getWeights() {
        return weights;
    }
}
