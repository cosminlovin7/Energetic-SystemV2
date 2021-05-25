package utils.sortstrategy;

import entities.Producer;

import java.util.Map;

public class SortOperation {
    private SortStrategy sortStrategy;

    public SortOperation(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    /**
     * Metoda sorteaza producatorii in functie de strategia aleasa.
     * @param producers
     * @return
     */
    public Map<Integer, Producer> sortProducers(Map<Integer, Producer> producers) {
        return sortStrategy.sort(producers);
    }

    /**
     * Intoarce tipul strategiei de sort.
     * @return
     */
    public SortStrategy getSortStrategy() {
        return sortStrategy;
    }
}
