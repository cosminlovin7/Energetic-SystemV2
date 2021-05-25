package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import entities.observer.Subject;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@JsonPropertyOrder({
        "id",
        "maxDistributors",
        "priceKW",
        "energyType",
        "energyPerDistributor",
        "monthlyStats"
})
@JsonIgnoreProperties({
        "distributorsNumber"
})
public final class Producer implements Subject {
    /**
     * Clasa interna pentru statisticile lunare.
     */
    @JsonPropertyOrder({ "month", "distributorsIds" })
    public class MonthlyStats {
        private final Integer month;
        private final List<Integer> distributorsIds;

        public MonthlyStats(final Integer month,
                            final List<Integer> distributorsIds) {
            this.month = month;
            this.distributorsIds = distributorsIds;
        }

        /**
         * Metoda intoarce valoarea lunii respective.
         * @return
         */
        public Integer getMonth() {
            return month;
        }

        /**
         * Metoda intoarce lista de distribuitori din luna respectiva.
         * @return
         */
        public List<Integer> getDistributorsIds() {
            return distributorsIds;
        }
    }
    private final Integer id;
    private String energyType;
    private Integer maxDistributors;
    private double priceKW;
    private long energyPerDistributor;

    /**
     * Variabilele legate de statisticile lunare(id distribuitori).
     */
    private List<MonthlyStats> monthlyStats;
    private List<Integer> currentMonthStats;
    private Integer distributorsNumber;

    public Producer(final Integer id,
                    final String energyType,
                    final Integer maxDistributors,
                    final double priceKW,
                    final long energyPerDistributor) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        monthlyStats = new ArrayList<>();
        currentMonthStats = new ArrayList<>();
        distributorsNumber = 0;
    }

    /**
     * Metoda adauga un distribuitor cu care incheie un contract
     * in lista de distribuitori din acea luna.
     * @param distributorID
     */
    @Override
    public void addDistributor(final Integer distributorID) {
        currentMonthStats.add(distributorID);
        distributorsNumber += 1;
    }

    /**
     * Atunci cand un distribuitor nu mai doreste energie de la producator,
     * el este sters din lista acestuia.
     * @param distributorID
     */
    @Override
    public void removeDistributor(final Integer distributorID) {
        currentMonthStats.remove(distributorID);
        distributorsNumber -= 1;
    }

    /**
     * Distribuitorii cu care are contract producatorul sunt notificati in lega-
     * tura cu noile schimbari legate de cantitatea de energie disponibila/distribuitor.
     * @param distributors
     */
    @Override
    public void notifyDistributors(final Map<Integer, Distributor> distributors) {
        if (currentMonthStats == null) {
            return;
        }
        for (Integer iteratorId : currentMonthStats) {
            Distributor distributorToNotify = distributors.get(iteratorId);
            distributorToNotify.reconsiderProductionCost(this.id);
        }
    }

    /**
     * Metoda updateaza statistica lunara salvata de fiecare producator.
     * @param currentMonth
     */
    public void updateMonthlyStats(Integer currentMonth) {
        List<Integer> currentMonthDistributors = new ArrayList<>(currentMonthStats);
        Collections.sort(currentMonthDistributors);
        monthlyStats.add(new MonthlyStats(currentMonth, currentMonthDistributors));
    }

    /**
     * Metoda intoarce id-ul producatorului.
     * @return
     */
    public Integer getID() {
        return id;
    }

    /**
     * Metoda intoarce tipul de energie distribuit.
     * @return
     */
    public String getEnergyType() {
        return energyType;
    }

    /**
     * Metoda intoarce numarul maxim de distribuitori.
     * @return
     */
    public Integer getMaxDistributors() {
        return maxDistributors;
    }

    /**
     * Metoda intoarce pretul pe kwh oferit de producator.
     * @return
     */
    public double getPriceKW() {
        return priceKW;
    }

    /**
     * Metoda intoarce cantitatea de energie oferita per distribuitor.
     * @return
     */
    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    /**
     * Metoda intoarce statistica din luna curenta.
     * @return
     */
    public List<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }

    /**
     * Metoda intoarce numarul de distribuitori cu care are contract.
     * @return
     */
    public Integer getDistributorsNumber() {
        return distributorsNumber;
    }

    /**
     * Metoda updateaza cantitatea de energie oferita per distribuitor.
     * @param energyPerDistributor
     */
    public void setEnergyPerDistributor(long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }
}
