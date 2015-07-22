package GA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 7/17/2015.
 */
public class NetworkController {
    int numberOfInputs = 12;
    int numberOfOutputs = 1;
    int numberOfHiddenLayers = 1;
    int neuronsPerHiddenLayer = 12;
    private List<NeuronLayer> neuronLayerList = new ArrayList<NeuronLayer>();

    public void createNetwork() {
        if (numberOfHiddenLayers > 0) {
            neuronLayerList.add(new NeuronLayer(neuronsPerHiddenLayer, numberOfInputs));

            for (int i = 0; i < numberOfHiddenLayers - 1; i++) {
                neuronLayerList.add(new NeuronLayer(neuronsPerHiddenLayer, numberOfInputs));
            }

            neuronLayerList.add(new NeuronLayer(numberOfOutputs, numberOfInputs));
        } else {
            neuronLayerList.add(new NeuronLayer(numberOfOutputs, numberOfInputs));
        }
    }

    public List<Double> getWeights() {
        List<Double> weights = new ArrayList<Double>();

        for (int i = 0; i < numberOfHiddenLayers + 1; i++) {
            for (int j = 0; j < neuronLayerList.get(i).getNumOfNeurons(); j++) {
                for (int k = 0; k < neuronLayerList.get(i).getNeurons().get(j).getWeights().size(); k++) {
                    weights.add(neuronLayerList.get(i).getNeurons().get(j).getWeights().get(k));
                }
            }
        }

        return weights;
    }

    public int getNumberOfWeights() {
        int tWeight = 0;

        for (int i = 0; i < numberOfHiddenLayers + 1; i++) {
            for (int j = 0; j < neuronLayerList.get(i).getNumOfNeurons(); j++) {
                for (int k = 0; k < neuronLayerList.get(i).getNeurons().get(j).getWeights().size(); k++) {
                    tWeight++;
                }
            }
        }

        return tWeight;
    }

    public void placeWeights(List<Double> weights) {
        int cWeight = 0;

        for (int i = 0; i < numberOfHiddenLayers + 1; i++) {
            for (int j = 0; j < neuronLayerList.get(i).getNumOfNeurons(); j++) {
                for (int k = 0; k < neuronLayerList.get(i).getNeurons().get(j).getWeights().size(); k++) {
                    neuronLayerList.get(i).getNeurons().get(j).getWeights().set(k,weights.get(cWeight));
                    cWeight++;
                }
            }
        }
    }

    public List<Double> updateNetwork(List<Double> input) {
        List<Double> outputs = new ArrayList<Double>();
        int counterWeights;

        if (input.size() != numberOfInputs) {
            return outputs;
        }

        for (int i = 0; i < numberOfHiddenLayers + 1; i++) {
            if (i > 0) {
                input.clear();
                input.addAll(outputs);
            }
            outputs.clear();
            counterWeights = 0;

            for (int j = 0; j < neuronLayerList.get(i).getNumOfNeurons(); j++) {
                double netInput = 0;
                int inputs = (int) Math.abs(neuronLayerList.get(i).getNeurons().get(j).getInputs());

                for (int k = 0; k < inputs - 1; k++) {
                    netInput += neuronLayerList.get(i).getNeurons().get(j).getWeights().get(k) * input.get(counterWeights++);
                }

                int temp = inputs-1;
                netInput += neuronLayerList.get(i).getNeurons().get(j).getWeights().get(temp) * -1;

                outputs.add(Sigmoid(netInput, 1));
                counterWeights = 0;
            }
        }

        return outputs;
    }

    double Sigmoid(double netInput, double response) {
        return ( 1 / ( 1 + Math.exp(-netInput / response)));
    }

}
