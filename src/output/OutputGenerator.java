package output;

import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import org.json.simple.JSONArray;

import java.util.Map;

public final class OutputGenerator {
    private static final OutputGenerator INSTANCE = new OutputGenerator();
    private OutputGenerator() { }
    public static OutputGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * Intoarce un jsonArray ce contine toti consumatorii din joc
     * @param consumersList
     * @return
     */
    public JSONArray getJSONConsumers(final Map<Integer, Consumer> consumersList) {
        JSONArray jsonConsumers = new JSONArray();

        for (Map.Entry<Integer, Consumer> consumerIterator : consumersList.entrySet()) {
            Consumer consumer = consumerIterator.getValue();
            jsonConsumers.add(consumer);
        }

        return jsonConsumers;
    }

    /**
     * Intoarce un jsonArray ce contine toti distribuitorii din joc
     * @param distributorsList
     * @return
     */
    public JSONArray getJSONDistributors(final Map<Integer, Distributor> distributorsList,
                                         final Map<Integer, Consumer> consumersList) {
        JSONArray jsonDistributors = new JSONArray();

        for (Map.Entry<Integer, Distributor> distributorIterator : distributorsList.entrySet()) {
            Distributor distributor = distributorIterator.getValue();
            distributor.prepareContractsList(consumersList);
            jsonDistributors.add(distributor);
        }

        return jsonDistributors;
    }

    /**
     * Intoarce un jsonArray ce contine toti producatorii din joc
     * @param producersList
     * @return
     */
    public JSONArray getJSONProducers(final Map<Integer, Producer> producersList) {
        JSONArray jsonProducers = new JSONArray();

        for (Map.Entry<Integer, Producer> producerIterator : producersList.entrySet()) {
            Producer producer = producerIterator.getValue();
            jsonProducers.add(producer);
        }

        return jsonProducers;
    }
}
