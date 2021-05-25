package entities;

public final class EntityGenerator {
    private static final EntityGenerator INSTANCE = new EntityGenerator();
    private EntityGenerator() { }
    public static EntityGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * @param id
     * @param initialBudget
     * @param monthlyIncome
     * @return un nou consumator(input)
     */
    public Entity getMember(final int id,
                            final int initialBudget,
                            final int monthlyIncome) {
        return new Consumer(id, initialBudget, monthlyIncome);
    }

    /**
     * @param id
     * @param contractLength
     * @param initialBudget
     * @param initialInfrastructureCost
     * @param energyNeededKW
     * @param producerStrategy
     * @return un nou distribuitor
     */
    public Entity getMember(final int id,
                             final int contractLength,
                             final long initialBudget,
                             final long initialInfrastructureCost,
                             final long energyNeededKW,
                             final String producerStrategy) {
        return new Distributor(id,
                               contractLength,
                               initialBudget,
                               initialInfrastructureCost,
                               energyNeededKW,
                               producerStrategy);
    }
}
