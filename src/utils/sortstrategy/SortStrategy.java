package utils.sortstrategy;

import entities.Producer;

import java.util.Map;

public interface SortStrategy {
    /**
     * Metoda realizeaza o sortarea a producatorilor in functie
     * de strategia dorita.
     * @param producers
     * @return
     */
    Map<Integer, Producer> sort(Map<Integer, Producer> producers);
}
