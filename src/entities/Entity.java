package entities;

public interface Entity {
    /**
     * Metoda intoarce id-ul entitatii.
     * @return
     */
    Integer getID();

    /**
     * Metoda intoarce true/false daca entitatea e falimentata.
     * @return
     */
    boolean getIsBankrupt();

    /**
     * Metoda intoarce bugetul entitatii.
     * @return
     */
    long getBudget();

    /**
     * Metoda intoarce contractCost-ul entitatii.
     * @return
     */
    long getContractCost();
}
