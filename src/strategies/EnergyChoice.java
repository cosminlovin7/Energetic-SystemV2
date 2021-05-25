package strategies;

import entities.Producer;

import java.util.List;
import java.util.Map;

public final class EnergyChoice {
    private Strategy energyStrategy;

    public EnergyChoice(final Strategy energyStrategy) {
        this.energyStrategy = energyStrategy;
    }

    /**
     * Metoda intoarce o lista de id-uri a producatorilor potriviti care
     * respecta strategia de obtinere a energiei.
     * @param producers
     * @param energyNeededKW
     * @return
     */
    public List<Integer> chooseProducers(Map<Integer, Producer> producers, long energyNeededKW) {
        return energyStrategy.chooseProducers(producers, energyNeededKW);
    }
}
