package strategies;

/**
 * Strategy types for distributors to choose their producers
 */
public enum EnergyChoiceStrategyType {
    GREEN("GREEN"),
    PRICE("PRICE"),
    QUANTITY("QUANTITY");
    private final String label;

    EnergyChoiceStrategyType(String label) {
        this.label = label;
    }

    /**
     * Metoda intoarce label-ul.
     * @return
     */
    public String getLabel() {
        return label;
    }
}
