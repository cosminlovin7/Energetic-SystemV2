package entities.observer;

public interface Observer {
    /**
     * Metoda instiinteaza distribuitorul ca trebuie
     * sa reconsidere costul de productie.
     * @param producerID
     */
    void reconsiderProductionCost(Integer producerID);
}
