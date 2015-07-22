package GA;

import GAModels.Genome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Scott on 7/20/2015.
 */
public class GeneticAlgorithm {
    private List<Genome> population;
    private int POP_SIZE = 512;
    private int CHROMOSOME_LENGTH = 169;
    private double MUTATION = 0.1;
    private double MAX_DEVIATION = 0.25;
    private double CROSSOVER = 0.55;
    private static Random random = new Random(666);

    public GeneticAlgorithm() {
        System.out.println("Creating initial population");
        population = new ArrayList<Genome>();

        for (int i = 0; i < POP_SIZE; i++) {
            population.add(new Genome());

            for (int j = 0; j < CHROMOSOME_LENGTH; j++) {
                population.get(i).getWeights().add(random.nextDouble());
            }
        }

        evaluate(population);
        calculateBestWorstAverage(population);
    }

    public List<Genome> getPopulation() {
        return population;
    }

    public List<Genome> epoch(List<Genome> oldPopulation) {
        List<Genome> newPopulation = new ArrayList<Genome>();

        Collections.sort(oldPopulation, new FitnessComparator());

        //Create new population
        for (int i = 0; newPopulation.size() < POP_SIZE-1; i++) {
            newPopulation.add(breed(oldPopulation));
        }

        evaluate(oldPopulation);
        Collections.sort(oldPopulation, new FitnessComparator());
        newPopulation.add(oldPopulation.get(0));
        //evaluate(newPopulation);

        Collections.sort(newPopulation, new FitnessComparator());

        return newPopulation;
    }

    private double calculateBestWorstAverage(List<Genome> newPopulation) {
        double worstFitness = newPopulation.get(newPopulation.size()-1).getFitness();
        double bestFitness = newPopulation.get(0).getFitness();
        double averageFitness = 0;

        for (Genome genome : newPopulation) {
            averageFitness += genome.getFitness();
        }

        averageFitness = averageFitness / newPopulation.size();

        System.out.println("Best fitness: " + bestFitness + " Worst: " + worstFitness + " Average: " + averageFitness);

        return bestFitness;
    }

    private Genome breed(List<Genome> population) {
        Genome childGenome;
        Genome parentGenomeOne, parentGenomeTwo;

        parentGenomeOne = selectGenome(population);
        parentGenomeTwo = selectGenome(population);

        childGenome = crossover(parentGenomeOne, parentGenomeTwo);
        childGenome = mutate(childGenome);
        evalFitness(childGenome);

        return childGenome;
    }

    private Genome mutate(Genome childGenome) {
        //for each weight if a random double is less than the mutation rate randomly deviate within the max_deviation range

        for (int i = 0; i < childGenome.getWeights().size(); i++) {
            if (random.nextDouble() < MUTATION) {
                double newWeight = childGenome.getWeights().get(i);

                if (random.nextInt() % 2 == 0) {
                    newWeight = newWeight + random.nextDouble() * MAX_DEVIATION;
                } else {
                    newWeight = newWeight - random.nextDouble() * MAX_DEVIATION;
                }

                childGenome.getWeights().set(i, newWeight);
            }
        }

        return childGenome;
    }

    private Genome crossover(Genome parentGenomeOne, Genome parentGenomeTwo) {
        Genome result = new Genome();

        if (random.nextDouble() < CROSSOVER) {
            List<Double> holder = new ArrayList<Double>();
            List<Double> holder2 = new ArrayList<Double>();
            int position = random.nextInt(parentGenomeOne.getWeights().size());

            holder.addAll(parentGenomeOne.getWeights().subList(0, position));
            holder2.addAll(parentGenomeOne.getWeights().subList(position, parentGenomeTwo.getWeights().size()));

            result.getWeights().addAll(holder2);
            result.getWeights().addAll(holder);

        } else {
            if (random.nextInt() % 2 == 0) {
                result = parentGenomeOne;
            } else {
                result = parentGenomeTwo;
            }
        }

        return result;
    }

    /**
     * Roulette wheel selection process
     * @return
     */
    private static Genome selectGenome(List<Genome> population) {

        double totalFitness = 0.0, loopTotal = 0.0;
        for (Genome genome : population) {
            totalFitness = totalFitness + genome.getFitness();
        }

        double randomVal = totalFitness * random.nextDouble();

        for (int i = 0; i < population.size(); i++) {
            loopTotal += population.get(i).getFitness();
            if (loopTotal >= randomVal) {
                return population.get(i);
            }
        }
        return population.get(population.size()-1);
    }

    public void evaluate(List<Genome> population) {
        for (Genome genome : population) {
            evalFitness(genome);
        }
    }

    private void evalFitness(Genome genome) {
        //Create a network with the genomes weights, run it against a 1 & then against a full block, the higher the first value and lower the second results in overall score
        //val1 = against 1
        //val2 = against full array
        //total = (val1 - val2)
        //optimal would be a 0 for block array & 1.0 for a 1

        List<Double> results = new ArrayList<Double>();

        NetworkController networkController = new NetworkController();

        networkController.createNetwork();
        networkController.placeWeights(genome.getWeights());

        results.addAll(networkController.updateNetwork(init(true)));
        results.addAll(networkController.updateNetwork(init(false)));

        if (results.get(0) > results.get(1)) {
            genome.setFitness((results.get(0) * 100) - (results.get(1) * 100));
        } else {
            genome.setFitness((results.get(1) * 100) - (results.get(0) * 100));
        }

    }

    public static List<Double> init(boolean todo) {
        List<Double> inputs = new ArrayList<Double>();

        if (todo) {
            Double[] input = {0.0, 1.0, 0.0,
                              0.0, 1.0, 0.0,
                              0.0, 1.0, 0.0,
                              0.0, 1.0, 0.0};
            Collections.addAll(inputs, input);
        } else {
            Double[] input = {1.0, 0.0, 0.0,
                              1.0, 0.0, 0.0,
                              1.0, 0.0, 0.0,
                              1.0, 0.0, 0.0};
            Collections.addAll(inputs, input);
        }

        return inputs;
    }

    public Genome getFittestGenome(List<Genome> pop) {
        Collections.sort(pop, new FitnessComparator());
        return pop.get(0);
    }

    public Genome evolve(List<Genome> pop, int generations) {
        int index = 0;
        double bestFit = 0;
        List<Genome> newPopulation = new ArrayList<Genome>();
        newPopulation.addAll(pop);
        while (index < generations) {
            newPopulation = epoch(newPopulation);
            bestFit = calculateBestWorstAverage(newPopulation);

            if (bestFit > 97) {
                System.out.println("Trained data set");
                break;
            }
        }
        return getFittestGenome(newPopulation);
    }
}
