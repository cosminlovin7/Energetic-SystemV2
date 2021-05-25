package utils;

import entities.Distributor;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class SortDistributors {
    private static final SortDistributors INSTANCE = new SortDistributors();
    private SortDistributors() { }
    public static SortDistributors getInstance() {
        return INSTANCE;
    }

    /**
     * Metoda sorteaza distribuitorii in functie de costul contractelor,
     * apoi dupa ID.
     * @param distributors
     * @return
     */
    public Map<Integer, Distributor> sort(Map<Integer, Distributor> distributors) {
        List<Map.Entry<Integer, Distributor>> list = new LinkedList<>(distributors.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Distributor>>() {
            public int compare(Map.Entry<Integer, Distributor> o1,
                               Map.Entry<Integer, Distributor> o2) {
                if (o1.getValue().getContractCost() > o2.getValue().getContractCost()) {
                    return 1;
                } else if (o2.getValue().getContractCost() > o1.getValue().getContractCost()) {
                    return -1;
                }

                return o1.getValue().getID() - o2.getValue().getID();
            }
        });

        HashMap<Integer, Distributor> sortedDistributors = new LinkedHashMap<>();
        for (Map.Entry<Integer, Distributor> distributor : list) {
            sortedDistributors.put(distributor.getKey(), distributor.getValue());
        }

        return sortedDistributors;
    }
}
