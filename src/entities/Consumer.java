package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import utils.Formulas;

@JsonPropertyOrder({ "id", "isBankrupt", "budget" })
@JsonIgnoreProperties({
        "initialBudget",
        "monthlyIncome",
        "contractCost",
        "hasDistributor",
        "hasDebtsToID",
        "remainedContractMonths",
        "distributorID"
})
public final class Consumer implements Entity {
    private final Integer id;
    private long initialBudget;
    private long monthlyIncome;
    private long budget;

    /**
     * Variabile legate de relatia cu distribuitorul
     */
    private Integer distributorID = -1;
    private Integer hasDebtsToID = -1;
    private Integer remainedContractMonths = 0;
    private boolean hasDistributor = false;
    private boolean isBankrupt = false;
    private boolean hasDebts = false;
    private long debtValue = -1;
    private long contractValue = -1;

    public Consumer(final Integer id,
                    final long initialBudget,
                    final long monthlyIncome) {
        this.id = id;
        this.initialBudget = initialBudget;
        budget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    /**
     *Metoda atribuie un nou distribuitor consumatorului
     * @param distributorId
     * @param contractCost
     * @param contractLength
     */
    public void setDistributor(final Integer distributorId,
                               final long contractCost,
                               final Integer contractLength) {
        this.distributorID = distributorId;
        this.contractValue = contractCost;
        this.remainedContractMonths = contractLength;
        hasDistributor = true;
    }

    /**
     * Ii este adaugata suma de monthlyIncome la buget consumatorului.
     */
    public void getSalary() {
        budget += monthlyIncome;
    }

    /**
     * Consumatorul plateste taxele distribuitorului cu care are contract.
     * @param distributor
     */
    public void payTaxes(Distributor distributor) {
        if (budget - contractValue < 0) {
            hasDebts = true;
            hasDebtsToID = distributor.getID();
            debtValue = contractValue;
        } else {
            budget -= contractValue;
            distributor.receiveTaxes(contractValue);
        }

        remainedContractMonths -= 1;
    }

    /**
     * Consumatorul plateste datoriile catre distribuitorul vechi si taxele
     * care distribuitorul actual, fiind aplicata o formula specifica de cal-
     * cul a taxelor.
     * @param oldDistributor
     * @param distributor
     */
    public void payDebts(Distributor oldDistributor,
                         Distributor distributor) {
        long toPay;
        if (hasDebtsToID == distributorID) {
            toPay = Formulas.getPenaltyCost(debtValue) + contractValue;
            if (budget - toPay < 0) {
                isBankrupt = true;
                return;
            } else {
                budget -= toPay;
                distributor.receiveTaxes(toPay);
                hasDebtsToID = -1;
                hasDebts = false;
                debtValue = -1;
                remainedContractMonths -= 1;
            }
        } else {
            toPay = Formulas.getPenaltyCost(debtValue);
            if (budget - toPay - contractValue < 0) {
                if (budget - toPay < 0) {
                    isBankrupt = true;
                    return;
                } else {
                    budget -= toPay;
                    oldDistributor.receiveTaxes(toPay);
                    hasDebtsToID = distributorID;
                    debtValue = contractValue;
                    remainedContractMonths -= 1;
                }
            } else {
                budget -= (toPay + contractValue);
                oldDistributor.receiveTaxes(toPay);
                distributor.receiveTaxes(contractValue);
                hasDebts = false;
                hasDebtsToID = -1;
                debtValue = -1;
                remainedContractMonths -= 1;
            }
        }
    }

    /**
     * Metoda intoarce id-ul consumatorului.
     * @return
     */
    public Integer getID() {
        return id;
    }

    /**
     * Metoda intoarce bugetul initial al consumatorului.
     * @return
     */
    public long getInitialBudget() {
        return initialBudget;
    }

    /**
     * Metoda intoarce venitul lunar al consumatorului.
     * @return
     */
    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    /**
     * Metoda intoarce id-ul distribuitorului cu care are contract.
     * @return
     */
    public Integer getDistributorID() {
        return distributorID;
    }

    /**
     * Metoda intoarce true/false daca individul este falimentat.
     * @return
     */
    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    /**
     * Metoda intoarce true/false daca individul are datorii.
     * @return
     */
    public boolean hasDebts() {
        return hasDebts;
    }

    /**
     * Metoda intoarce id-ul distribuitorului catre care are datorii.
     * @return
     */
    public Integer getHasDebtsToID() {
        return hasDebtsToID;
    }

    /**
     * Metoda intoarce bugetul actual al consumatorului.
     * @return
     */
    public long getBudget() {
        return budget;
    }

    /**
     * Metoda intoarce true/false daca consumatorul are contract cu
     * vreun distribuitor.
     * @return
     */
    public boolean hasDistributor() {
        return hasDistributor;
    }

    /**
     * Metoda intoarce valoarea contractului pe care-l plateste acum.
     * @return
     */
    public long getContractCost() {
        return contractValue;
    }

    /**
     * Metoda seteaza valorile legate de distribuitor pe default,
     * semnificand ca, consumatorul nu mai are niciun contract.
     */
    public void setNoDistributor() {
        hasDistributor = false;
        hasDebts = false;
        isBankrupt = false;
        distributorID = -1;
        hasDebtsToID = -1;
        contractValue = -1;
        remainedContractMonths = -1;
        debtValue = -1;
    }

    /**
     * Metoda intoarce numarul de luni ramase din contract.
     * @return
     */
    public Integer getRemainedContractMonths() {
        return remainedContractMonths;
    }

    /**
     * Metoda seteaza daca un consumator are distribuitor sau nu.
     * @param hasDistributor
     */
    public void setHasDistributor(final boolean hasDistributor) {
        this.hasDistributor = hasDistributor;
    }
}
