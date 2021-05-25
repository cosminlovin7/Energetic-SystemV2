package input;

import entities.Producer;
import entities.update.DistributorChanges;
import entities.update.ProducerChanges;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import entities.Consumer;
import entities.Distributor;
import entities.EntityGenerator;

import entities.update.Update;

import utils.Constants;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Citeste datele de intrare din fisierele .json
 */
public final class InputLoader {
    private final String inputPath;

    public InputLoader(final String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * @return locatia fisierului de input
     */
    public String getInputPath() {
        return inputPath;
    }

    /**
     * Citeste datele de intrare si returneaza un obiect
     * input ce contine listele de consumatori, distribuitori
     * si update-urile lunare.
     * @return
     */
    public Input readInputData() {
        Integer numberOfTurns = 0;
        Map<Integer, Consumer> consumersMap = new HashMap<>();
        Map<Integer, Distributor> distributorsMap = new HashMap<>();
        Map<Integer, Producer> producersMap = new HashMap<>();
        List<Update> monthlyUpdatesList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));
            numberOfTurns = Integer.parseInt(jsonObject
                    .get(Constants.NUMBER_OF_TURNS).toString());

            JSONObject initialData = (JSONObject) jsonObject.get(Constants.INITIAL_DATA);
            JSONArray consumers = (JSONArray) initialData.get(Constants.CONSUMERS);
            JSONArray distributors = (JSONArray) initialData.get(Constants.DISTRIBUTORS);
            JSONArray producers = (JSONArray) initialData.get(Constants.PRODUCERS);
            EntityGenerator entityGenerator = EntityGenerator.getInstance();

            if (consumers != null) {
                for (Object consumer : consumers) {
                    Integer id = Integer.parseInt(((JSONObject) consumer)
                            .get(Constants.ID).toString());
                    consumersMap.put(id, (Consumer) entityGenerator.getMember(id,
                                      Integer.parseInt(((JSONObject) consumer)
                                              .get(Constants.INITIAL_BUDGET).toString()),
                                      Integer.parseInt(((JSONObject) consumer)
                                              .get(Constants.MONTHLY_INCOME).toString())));
                }
            } else {
                System.out.println("Nu exista consumatori.");
                consumersMap = null;
            }

            if (distributors != null) {
                for (Object distributor : distributors) {
                    Integer id = Integer.parseInt(((JSONObject) distributor)
                            .get(Constants.ID).toString());
                    distributorsMap.put(id, (Distributor) entityGenerator.getMember(id,
                                      Integer.parseInt(((JSONObject) distributor)
                                              .get(Constants.CONTRACT_LENGTH).toString()),
                                      Integer.parseInt(((JSONObject) distributor)
                                              .get(Constants.INITIAL_BUDGET).toString()),
                                      Integer.parseInt(((JSONObject) distributor)
                                              .get(Constants.INITIAL_INFR_COST).toString()),
                                      Integer.parseInt(((JSONObject) distributor)
                                              .get(Constants.ENERGY_NEEDED).toString()),
                                      (((JSONObject) distributor)
                                              .get(Constants.PRODUCER_STRATEGY).toString())));
                }
            } else {
                System.out.println("Nu exista distribuitori.");
                distributorsMap = null;
            }

            if (producers != null) {
                for (Object producer : producers) {
                    Integer id = Integer.parseInt(((JSONObject) producer)
                            .get(Constants.ID).toString());
                    producersMap.put(id, new Producer(id,
                                     ((JSONObject) producer)
                                              .get(Constants.ENERGY_TYPE).toString(),
                                     Integer.parseInt(((JSONObject) producer)
                                              .get(Constants.MAX_DISTRIBUTORS).toString()),
                                     Double.parseDouble(((JSONObject) producer)
                                              .get(Constants.PRICE_KW).toString()),
                                     Integer.parseInt(((JSONObject) producer)
                                              .get(Constants.ENERGY_PER_DISTRIBUTOR).toString())));
                }
            } else {
                System.out.println("Nu exista producatori.");
                distributorsMap = null;
            }

            JSONArray monthlyUpdates = (JSONArray) jsonObject.get(Constants.MONTHLY_UPDATES);

            if (monthlyUpdates != null) {
                for (Object monthlyUpdate : monthlyUpdates) {
                    Update newUpdate = new Update();
                    List<Consumer> newUpdateConsumers = newUpdate.getNewConsumers();
                    List<DistributorChanges> newUpdateDistributorChanges
                            = newUpdate.getDistributorChanges();
                    List<ProducerChanges> newUpdateProducerChanges
                            = newUpdate.getProducerChanges();

                    JSONArray newConsumers =
                            (JSONArray) ((JSONObject) monthlyUpdate)
                                    .get(Constants.NEW_CONSUMERS);
                    JSONArray distributorChanges =
                            (JSONArray) ((JSONObject) monthlyUpdate)
                                    .get(Constants.DISTRIBUTOR_CHANGES);
                    JSONArray producerChanges =
                            (JSONArray) ((JSONObject) monthlyUpdate)
                                    .get(Constants.PRODUCER_CHANGES);

                    if (newConsumers.size() == 0) {
                        newUpdateConsumers.add(new Consumer(
                                -1,
                                -1,
                                -1));
                    } else {
                        for (Object consumer : newConsumers) {
                            newUpdateConsumers.add(new Consumer(
                                Integer.parseInt(((JSONObject) consumer)
                                        .get(Constants.ID).toString()),
                                Integer.parseInt(((JSONObject) consumer)
                                        .get(Constants.INITIAL_BUDGET).toString()),
                                Integer.parseInt(((JSONObject) consumer)
                                        .get(Constants.MONTHLY_INCOME).toString())));
                        }
                    }

                    if (distributorChanges.size() == 0) {
                        newUpdateDistributorChanges.add(new DistributorChanges(
                                -1,
                                -1));
                    } else {
                        for (Object dChange : distributorChanges) {
                            newUpdateDistributorChanges.add(new DistributorChanges(
                                Integer.parseInt(((JSONObject) dChange)
                                        .get(Constants.ID).toString()),
                                Integer.parseInt(((JSONObject) dChange)
                                        .get(Constants.INFR_COST).toString())));
                        }
                    }

                    if (producerChanges.size() == 0) {
                        newUpdateProducerChanges.add(new ProducerChanges(
                                -1,
                                -1));
                    } else {
                        for (Object pChange : producerChanges) {
                            newUpdateProducerChanges.add(new ProducerChanges(
                                            Integer.parseInt(((JSONObject) pChange)
                                                    .get(Constants.ID).toString()),
                                            Integer.parseInt(((JSONObject) pChange)
                                                    .get(Constants.ENERGY_PER_DISTRIBUTOR)
                                                    .toString())));
                        }
                    }
                    monthlyUpdatesList.add(newUpdate);
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return new Input(numberOfTurns, consumersMap, distributorsMap, producersMap,
                monthlyUpdatesList);
    }
}
