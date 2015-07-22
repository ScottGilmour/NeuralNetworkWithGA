package GA;

import models.Neuron;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 7/17/2015.
 */
public class NeuronLayer {
    private int numOfNeurons;
    private List<Neuron> neurons;

    public NeuronLayer(int numOfNeurons, int numOfInputs) {
        neurons = new ArrayList<Neuron>();
        this.numOfNeurons = numOfNeurons;

        for (int i = 0; i < numOfNeurons; i++) {
            neurons.add(new Neuron(numOfInputs));
        }
    }

    public int getNumOfNeurons() {
        return numOfNeurons;
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }
}
