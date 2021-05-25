package strategies;

import entities.Producer;

import java.util.List;
import java.util.Map;

public interface Strategy {
    /**
     * Metoda intoarce o lista de id-uri ale producatorilor potriviti
     * pentru tipul de strategie dorit.
     * @param producers
     * @param energyNeededKW
     * @return
     */
    List<Integer> chooseProducers(Map<Integer, Producer> producers, long energyNeededKW);
}
