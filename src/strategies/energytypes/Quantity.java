package strategies.energytypes;

import entities.Producer;
import strategies.Strategy;
import utils.sortstrategy.SortOperation;
import utils.sortstrategy.sorttypes.SortQuantity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Quantity implements Strategy {
    /**
     * Metoda intoarce o lista a id-urilor producatorilor in functie de
     * cantitatea de energie pe care o ofera.
     * @param producers
     * @param energyNeededKW
     * @return
     */
    @Override
    public List<Integer> chooseProducers(Map<Integer, Producer> producers, long energyNeededKW) {
        List<Integer> chosenProducers = new ArrayList<>();
        long energyObtained = 0;
        SortOperation sortOperation = new SortOperation(new SortQuantity());
        Map<Integer, Producer> greenSortedProducers = sortOperation.sortProducers(producers);

        for (Map.Entry<Integer, Producer> producer : greenSortedProducers.entrySet()) {
            if (energyObtained < energyNeededKW) {
                if (!chosenProducers.contains(producer.getValue().getID())
                    && producer.getValue().getDistributorsNumber()
                     < producer.getValue().getMaxDistributors()) {
                    chosenProducers.add(producer.getValue().getID());
                    energyObtained += producer.getValue().getEnergyPerDistributor();
                }
            }
        }
        return chosenProducers;
    }
}
