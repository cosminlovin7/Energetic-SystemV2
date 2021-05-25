package utils.sortstrategy.sorttypes;

import entities.EnergyType;
import entities.Producer;
import utils.sortstrategy.SortStrategy;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

public final class SortGreen implements SortStrategy {

    @Override
    public Map<Integer, Producer> sort(Map<Integer, Producer> producers) {
        List<Map.Entry<Integer, Producer>> list = new LinkedList<>(producers.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Producer>>() {
            public int compare(Map.Entry<Integer, Producer> o1,
                               Map.Entry<Integer, Producer> o2) {
                if (EnergyType.isRenewable(o1.getValue().getEnergyType())
                        && !EnergyType.isRenewable(o2.getValue().getEnergyType())) {
                    return -1;
                } else if (EnergyType.isRenewable(o2.getValue().getEnergyType())
                        && !EnergyType.isRenewable(o1.getValue().getEnergyType())) {
                    return 1;
                }

                if (o1.getValue().getPriceKW() > o2.getValue().getPriceKW()) {
                    return 1;
                } else if (o2.getValue().getPriceKW() > o1.getValue().getPriceKW()) {
                    return -1;
                }

                if (o1.getValue().getEnergyPerDistributor()
                        > o2.getValue().getEnergyPerDistributor()) {
                    return -1;
                } else if (o2.getValue().getEnergyPerDistributor()
                        > o1.getValue().getEnergyPerDistributor()) {
                    return 1;
                }

                return o1.getValue().getID() - o2.getValue().getID();
            }
        });

        HashMap<Integer, Producer> sortedProducers = new LinkedHashMap<>();
        for (Map.Entry<Integer, Producer> producer : list) {
            sortedProducers.put(producer.getKey(), producer.getValue());
        }

        return sortedProducers;
    }
}
