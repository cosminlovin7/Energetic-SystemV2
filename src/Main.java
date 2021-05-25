import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import entities.update.Update;
import input.Input;
import input.InputLoader;

import org.json.simple.JSONArray;
import output.OutputGenerator;
import output.OutputWriter;
import simulate.Simulate;
import utils.Constants;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() { }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        final String inputPath = args[Constants.INPUT_FILE];
        InputLoader inputLoader = new InputLoader(inputPath);
        Input input = inputLoader.readInputData();

        Integer numberOfTurns = input.getNumberOfTurns();
        Map<Integer, Consumer> consumers = input.getConsumers();
        Map<Integer, Distributor> distributors = input.getDistributors();
        Map<Integer, Producer> producers = input.getProducers();
        List<Update> monthlyUpdates = input.getMonthlyUpdates();

        Simulate simulate = Simulate.getInstance();
        simulate.game(numberOfTurns, consumers, distributors, producers, monthlyUpdates);
        simulate.resetGameTime();

        final String outputPath = args[Constants.OUTPUT_FILE];
        OutputGenerator outputGenerator = OutputGenerator.getInstance();
        JSONArray jsonConsumers = outputGenerator.getJSONConsumers(consumers);
        JSONArray jsonDistributors = outputGenerator.getJSONDistributors(distributors, consumers);
        JSONArray jsonProducers = outputGenerator.getJSONProducers(producers);
        OutputWriter outWriter = new OutputWriter(outputPath);

        HashMap<String, JSONArray> newMap = new LinkedHashMap<>();
        newMap.put("consumers", jsonConsumers);
        newMap.put("distributors", jsonDistributors);
        newMap.put("energyProducers", jsonProducers);
        outWriter.closeJSON(newMap);
    }
}
