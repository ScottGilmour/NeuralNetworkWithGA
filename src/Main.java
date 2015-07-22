import GA.GeneticAlgorithm;
import GA.NetworkController;
import GAModels.Genome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Scott on 7/20/2015.
 */
public class Main {
    private static int GENERATIONS = 20;

    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        List<Genome> pop = geneticAlgorithm.getPopulation();

        Genome fittest = geneticAlgorithm.evolve(pop, GENERATIONS);

        List<Double> results = new ArrayList<Double>();
        List<Double> inputOne = new ArrayList<Double>();
        List<Double> inputTwo = new ArrayList<Double>();

        Double[] input1 = {0.0, 1.0, 0.0,
                           0.0, 1.0, 0.0,
                           0.0, 1.0, 0.0,
                           0.0, 1.0, 0.0};

        Double[] input2 = {1.0, 0.0, 0.0,
                           1.0, 0.0, 0.0,
                           1.0, 0.0, 0.0,
                           1.0, 0.0, 0.0};

        Collections.addAll(inputOne, input1);
        Collections.addAll(inputTwo, input2);

        NetworkController networkController = new NetworkController();
        networkController.createNetwork();
        networkController.placeWeights(fittest.getWeights());

        results.addAll(networkController.updateNetwork(inputOne));
        results.addAll(networkController.updateNetwork(inputTwo));

        System.out.println("Results from input one: " + results.get(0));
        System.out.println("Results from input two: " + results.get(1));

    }
}
