package entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import entities.observer.Observer;
import strategies.EnergyChoice;
import strategies.energytypes.Green;
import strategies.energytypes.Price;
import strategies.energytypes.Quantity;
import utils.Constants;
import utils.Formulas;
import utils.SortContracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({
        "id",
        "energyNeededKW",
        "contractCost",
        "budget",
        "producerStrategy",
        "isBankrupt",
        "contracts"
})
@JsonIgnoreProperties({
        "contractLength",
        "producersID",
        "consumersID"
})
public final class Distributor implements Entity, Observer {
    /**
     * Clasa interna de contracte pentru distribuitori
     */
    @JsonPropertyOrder({ "consumerId", "price", "remainedContractMonths" })
    public class Contracts {
        private Integer consumerId;
        private long price;
        private Integer remainedContractMonths;

        public Contracts(final Integer consumerId,
                         final long price,
                         final Integer remainedContractMonths) {
            this.consumerId = consumerId;
            this.price = price;
            this.remainedContractMonths = remainedContractMonths;
        }

        /**
         * @return id-ul consumatorului
         */
        public Integer getConsumerId() {
            return consumerId;
        }

        /**
         * @return pretul platit de consumator
         */
        public long getPrice() {
            return price;
        }

        /**
         * @return durata ramasa pentru contract
         */
        public Integer getRemainedContractMonths() {
            return remainedContractMonths;
        }

        /**
         * Seteaza valoarea contractului
         * @param price
         */
        public void setPrice(final long price) {
            this.price = price;
        }

        /**
         * Seteaza durata contractului
         * @param remainedContractMonths
         */
        public void setRemainedContractMonths(final Integer remainedContractMonths) {
            this.remainedContractMonths = remainedContractMonths;
        }
    }
    private final Integer id;
    private final Integer contractLength;
    private final long initialBudget;
    private long infrastructureCost;
    private long energyNeededKW;
    private long productCost;
    private String producerStrategy;
    private boolean isBankrupt = false;
    private long profit = -1;
    private long budget;

    /**
     * Variabile ce definesc legatura cu producatorii
     */
    private boolean reconsiderProductionCost = false;
    private Integer producerToReconsiderID = -1;
    private List<Integer> producersID;
    private boolean hasProducers = false;

    /**
     * Variabile ce definesc legatura cu consumatorii
     */
    private List<Integer> consumersID;
    private long contractValue = -1;
    private Integer consumersNumber = 0;
    private List<Contracts> contractsList;

    public Distributor(final Integer id,
                       final Integer contractLength,
                       final long initialBudget,
                       final long initialInfrastructureCost,
                       final long energyNeededKW,
                       final String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.initialBudget = initialBudget;
        budget = initialBudget;
        this.infrastructureCost = initialInfrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = producerStrategy;
        producersID = new ArrayList<>();
        consumersID = new ArrayList<>();
        productCost = 0;
    }

    /**
     * Metoda seteaza variabilele reconsiderProductionCost si producerToReconsiderID,
     * pentru ca distribuitorul sa stie ca trebuie sa aiba in vedere schimbarile
     * de pret ale producatorului cu care are contract acum.
     * E de ajuns sa trebuiasca sa fie anuntat de un singur producator dintre cei
     * cu care are deja contract, de aceea nu e nevoie sa retinem o lista de
     * producatori ale caror costuri s-au schimbat, in caz ca are contract cu
     * mai multi.
     * @param producerID
     */
    @Override
    public void reconsiderProductionCost(Integer producerID) {
        producerToReconsiderID = producerID;
        reconsiderProductionCost = true;
    }

    /**
     * Prin intermediul acestei metode distribuitorul isi alege
     * producatorii cu care doreste sa aiba contract in functie de
     * productionStrategy-ul pe care il are.
     * @param producers
     */
    public void chooseProducer(Map<Integer, Producer> producers) {
        if (isBankrupt) {
            return;
        }

        EnergyChoice energyChoice;
        if (producerStrategy.equalsIgnoreCase(Constants.GREEN)) {
            energyChoice = new EnergyChoice(new Green());
        } else if (producerStrategy.equalsIgnoreCase(Constants.PRICE)) {
            energyChoice = new EnergyChoice(new Price());
        } else {
            energyChoice = new EnergyChoice(new Quantity());
        }

        producersID = energyChoice.chooseProducers(producers, energyNeededKW);
        long cost = 0;
        for (Integer producerID : producersID) {
            Producer currentProducer = producers.get(producerID);
            currentProducer.addDistributor(this.id);
            cost += currentProducer.getEnergyPerDistributor() * currentProducer.getPriceKW();
        }
        productCost = Formulas.getProductionCost(cost);
        this.hasProducers = true;
    }

    /**
     * Prin intermediul acestei metode sunt eliminati producatorii cu care
     * distribuitorul are contract. Acest lucru se intampla dupa ce a fost
     * anuntat ca unul sau mai multi dintre producatorii cu care avea contract
     * si-au schimbat valorile, asa ca variabila reconsiderProductionCost
     * devine false.
     * @param producers
     */
    public void removeProducers(Map<Integer, Producer> producers) {
        for (Integer producerID : producersID) {
            producers.get(producerID).removeDistributor(this.id);
        }
        producersID.clear();
        reconsiderProductionCost = false;
    }

    /**
     * Calculeaza profitul folosindu-se de formula data.
     */
    public void calculateProfit() {
        profit = Formulas.getProfit(productCost);
    }

    /**
     * Calculeaza costul contractului in functie de numarul de
     * consumatori.
     */
    public void calculateContractValue() {
        calculateProfit();
        if (consumersNumber == 0) {
            contractValue = Formulas.getContractWC(infrastructureCost,
                                                   productCost,
                                                   profit);
        } else {
            contractValue = Formulas.getContract(infrastructureCost,
                                                 productCost,
                                                 profit,
                                                 consumersNumber);
        }
    }

    /**
     * Adauga un consumator in lista de consumatori carora le da
     * energie.
     * @param consumerID
     */
    public void addConsumer(final Integer consumerID) {
        consumersID.add(consumerID);
        consumersNumber += 1;
    }

    /**
     * Sterge un consumator din lista de consumatori cu care
     * avea contract.
     * @param consumerID
     */
    public void removeConsumer(final Integer consumerID) {
        consumersID.remove(consumerID);
        consumersNumber -= 1;
    }

    /**
     * Metoda updateaza costul de infrastructura.
     * @param initialInfrastructureCost
     */
    public void setInfrastructureCost(long initialInfrastructureCost) {
        this.infrastructureCost = initialInfrastructureCost;
    }

    /**
     * Distribuitorul colecteaza taxele de la consumatori.
     * @param taxPayed
     */
    public void receiveTaxes(long taxPayed) {
        budget += taxPayed;
    }

    /**
     * Distribuitorul plateste taxele la stat.
     */
    public void payTaxes() {
        long totalCost = Formulas.getTotalCost(infrastructureCost, productCost, consumersNumber);
        if (budget - totalCost < 0) {
            isBankrupt = true;
            return;
        } else {
            budget -= totalCost;
        }
    }

    /**
     * Metoda este folosita in special pentru output.
     * Ea pregateste lista de contracte.
     * @param consumers
     */
    public void prepareContractsList(Map<Integer, Consumer> consumers) {
        contractsList = new ArrayList<>();
        for (Integer consumerID : consumersID) {
            Consumer currentConsumer = consumers.get(consumerID);
            contractsList.add(new Contracts(currentConsumer.getID(),
                                            currentConsumer.getContractCost(),
                                            currentConsumer.getRemainedContractMonths()));
        }

        contractsList.sort(new SortContracts());
    }

    /**
     * Metoda intoarce id-ul distribuitorului.
     * @return
     */
    public Integer getID() {
        return id;
    }

    /**
     * Metoda intoarce lungimea contractelor oferite de distribuitor.
     * @return
     */
    public Integer getContractLength() {
        return contractLength;
    }

    /**
     * Metoda intoarce true/false daca trebuie sa-si reconsidere contractele
     * cu producatorii.
     * @return
     */
    public boolean hasToReconsiderProductionCost() {
        return reconsiderProductionCost;
    }

    /**
     * Metoda intoarce true/false daca are contract cu unul sau mai multi
     * producatori.
     * @return
     */
    public boolean hasProducers() {
        return hasProducers;
    }

    /**
     * Intoarce o lista de id-uri ale consumatorilor cu care are contract.
     * @return
     */
    public List<Integer> getConsumersID() {
        return consumersID;
    }

    /**
     * Metoda intoarce true/false daca distribuitorul este falimentat.
     * @return
     */
    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    /**
     * Metoda intoarce costul contractului oferit de distribuitor.
     * @return
     */
    public long getContractCost() {
        return contractValue;
    }

    /**
     * Metoda intoarce bugetul actual al distribuitorului.
     * @return
     */
    public long getBudget() {
        return budget;
    }

    /**
     * Metoda intoarce lista de contracte pentru output.
     * @return
     */
    public List<Contracts> getContracts() {
        return contractsList;
    }

    /**
     * Metoda intoarce valoarea energiei necesare lunar.
     * @return
     */
    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    /**
     * Metoda intoarce strategia pe care o urmeaza distribuitorul in
     * alegerea producatorilor.
     * @return
     */
    public String getProducerStrategy() {
        return producerStrategy;
    }
}
