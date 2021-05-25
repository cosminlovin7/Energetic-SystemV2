package simulate;

import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import entities.update.Update;

import java.util.List;
import java.util.Map;

public final class Simulate {
    private Integer currentMonth = 0;

    private static final Simulate INSTANCE = new Simulate();
    private Simulate() { }
    public static Simulate getInstance() {
        return INSTANCE;
    }

    /**
     * Jocul inainteaza cu o luna.
     */
    public void stepTimeForward() {
        currentMonth++;
    }

    /**
     * Returneaza luna curenta.
     * @return
     */
    public Integer getCurrentMonth() {
        return currentMonth;
    }

    /**
     * Este simulata runda initiala, de pregatire.
     * @param consumers
     * @param distributors
     * @param producers
     */
    public void initialMonth(final Map<Integer, Consumer> consumers,
                             final Map<Integer, Distributor> distributors,
                             final Map<Integer, Producer> producers) {
        Action action = Action.getInstance();

        action.distributorsChooseProducers(distributors, producers);
        action.distributorsCalculateContracts(distributors);
        action.consumersChooseDistributors(consumers, distributors);
        action.consumersGetSalaries(consumers);
        action.consumersPayTaxes(consumers, distributors);
        action.distributorsPayTaxes(distributors);

        stepTimeForward();
    }

    /**
     * Este simulata o luna din joc.
     * @param consumers
     * @param distributors
     * @param producers
     * @param monthlyUpdates
     */
    public void monthlyFlow(final Map<Integer, Consumer> consumers,
                            final Map<Integer, Distributor> distributors,
                            final Map<Integer, Producer> producers,
                            final List<Update> monthlyUpdates) {
        Action action = Action.getInstance();

        action.updateEntities(consumers, distributors, monthlyUpdates, currentMonth);
        action.distributorsCalculateContracts(distributors);
        action.removeConsumers(consumers, distributors);
        action.consumersChooseDistributors(consumers, distributors);
        action.consumersGetSalaries(consumers);
        action.consumersPayTaxes(consumers, distributors);
        action.distributorsPayTaxes(distributors);
        action.updateProducers(currentMonth, distributors, producers, monthlyUpdates);
        action.distributorsChooseProducers(distributors, producers);
        action.updateProducerStats(producers);

        stepTimeForward();
    }

    /**
     * Simularea jocului propriu-zis.
     * @param numberOfTurns
     * @param consumers
     * @param distributors
     * @param producers
     * @param monthlyUpdates
     */
    public void game(final Integer numberOfTurns,
                     final Map<Integer, Consumer> consumers,
                     final Map<Integer, Distributor> distributors,
                     final Map<Integer, Producer> producers,
                     final List<Update> monthlyUpdates) {
        initialMonth(consumers, distributors, producers);

        for (int i = 0; i < numberOfTurns; i++) {
            monthlyFlow(consumers, distributors, producers, monthlyUpdates);
        }
    }

    /**
     * Resetarea timpului dupa terminarea jocului.
     */
    public void resetGameTime() {
        currentMonth = 0;
    }

}
