package utils;

/**
 * Formulele folosite pentru a realiza calculele contractelor, productionCost-urilor,
 * profitului, taxelor.
 */
public final class Formulas {
    private static final Formulas INSTANCE = new Formulas();
    private Formulas() { }
    public static Formulas getInstance() {
        return INSTANCE;
    }
    /**
     * Metoda intoarce costul unui contract in functie de numarul de
     * consumatori actuali.
     * @param infrastructureCost
     * @param productionCost
     * @param profit
     * @param consumatorsNumber
     * @return
     */
    public static long getContract(final long infrastructureCost,
                            final long productionCost,
                            final long profit,
                            final Integer consumatorsNumber) {
        return Math.round(
                Math.floor(infrastructureCost / consumatorsNumber)
                        + productionCost
                        + profit
        );
    }

    /**
     * Metoda intoarce costul unui contract atunci cand distribuitorul
     * nu are consumatori.
     * @param infrastructureCost
     * @param productionCost
     * @param profit
     * @return
     */
    public static long getContractWC(final long infrastructureCost,
                              final long productionCost,
                              final long profit) {
        return infrastructureCost + productionCost + profit;
    }

    /**
     * Metoda intoarce profitul obtinut de un distribuitor per contract.
     * @param productionCost
     * @return
     */
    public static long getProfit(final long productionCost) {
        return Math.round(Math.floor(Constants.PROFIT_MULTIPLIER * productionCost));
    }

    /**
     * Metoda intoarce costul taxelor pe care trebuie sa-l plateasca distribuitorul.
     * @param infrastructureCost
     * @param productionCost
     * @param consumatorsNumber
     * @return
     */
    public static long getTotalCost(final long infrastructureCost,
                             final long productionCost,
                             final Integer consumatorsNumber) {
        return infrastructureCost + productionCost * consumatorsNumber;
    }

    /**
     * Metoda intoarce costul datoriilor unui consumator.
     * @param oldContract
     * @return
     */
    public static long getPenaltyCost(final long oldContract) {
        return Math.round(Math.floor(Constants.DEBTS_MULTIPLIER * oldContract));
    }

    /**
     * Metoda intoarce productionCost-ul.
     * @param cost
     * @return
     */
    public static long getProductionCost(final long cost) {
        return Math.round(Math.floor(cost / Constants.PRODUCTION_COST_COEF));
    }
}
