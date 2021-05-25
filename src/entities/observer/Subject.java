package entities.observer;

import entities.Distributor;

import java.util.Map;

public interface Subject {
    /**
     * Metoda adauga un distribuitor in lista de distribuitori
     * a producatorului.
     * @param distributorID
     */
    void addDistributor(Integer distributorID);

    /**
     * Metoda sterge un distribuitor din lista de distribuitori
     * a producatorului.
     * @param distributorID
     */
    void removeDistributor(Integer distributorID);

    /**
     * Metoda notifica toti distribuitorii care au contract cu
     * actualul producatori, in legatura cu schimbarea costului
     * de productie.
     * @param distributors
     */
    void notifyDistributors(Map<Integer, Distributor> distributors);
}
