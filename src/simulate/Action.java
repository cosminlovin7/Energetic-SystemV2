package simulate;

import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import entities.update.DistributorChanges;
import entities.update.ProducerChanges;
import entities.update.Update;
import utils.SortDistributors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Action {
    private static final Action INSTANCE = new Action();
    private Action() { }
    public static Action getInstance() {
        return INSTANCE;
    }

    /**
     * Metoda updateaza entitatile consumatori si distribuitori, in functie de
     * update-urile din luna respectiva.
     * @param consumers
     * @param distributors
     * @param monthlyUpdates
     * @param currentMonth
     */
    public void updateEntities(final Map<Integer, Consumer> consumers,
                               final Map<Integer, Distributor> distributors,
                               final List<Update> monthlyUpdates,
                               final Integer currentMonth) {
        Update currentMonthUpdate = monthlyUpdates.get(currentMonth - 1);
        List<Consumer> newConsumers = currentMonthUpdate.getNewConsumers();
        List<DistributorChanges> distributorChanges = currentMonthUpdate.getDistributorChanges();

        for (Consumer newConsumer : newConsumers) {
            if (newConsumer.getID() > -1) {
                consumers.put(newConsumer.getID(), new Consumer(newConsumer.getID(),
                        newConsumer.getInitialBudget(),
                        newConsumer.getMonthlyIncome()));
            }
        }

        for (DistributorChanges distributorChange : distributorChanges) {
            if (distributorChange.getID() > -1) {
                Distributor distributorToModify = distributors.get(distributorChange.getID());
                distributorToModify
                        .setInfrastructureCost(distributorChange.getInfrastructureCost());
            }
        }
    }

    /**
     * Metoda updateaza valorile producatorilor in functie de update-urile din
     * luna respectiva.
     * @param currentMonth
     * @param distributors
     * @param producers
     * @param monthlyUpdates
     */
    public void updateProducers(final Integer currentMonth,
                                final Map<Integer, Distributor> distributors,
                                final Map<Integer, Producer> producers,
                                final List<Update> monthlyUpdates) {
        Update currentMonthUpdate = monthlyUpdates.get(currentMonth - 1);
        List<ProducerChanges> producerChanges = currentMonthUpdate.getProducerChanges();

        for (ProducerChanges producerChange : producerChanges) {
            if (producerChange.getID() > -1) {
                Producer producerToModify = producers.get(producerChange.getID());
                producerToModify
                        .setEnergyPerDistributor(producerChange.getEnergyPerDistributor());
                producerToModify
                        .notifyDistributors(distributors);
            }
        }
    }

    /**
     * Metoda updateaza statisticile lunare ale producatorilor.
     * @param producers
     */
    public void updateProducerStats(final Map<Integer, Producer> producers) {
        for (Map.Entry<Integer, Producer> producer : producers.entrySet()) {
            producer.getValue().updateMonthlyStats(Simulate.getInstance().getCurrentMonth());
        }
    }

    /**
     * Prin intermediul acestei metode distribuitorii isi incheie contracte
     * cu producatorii de energie.
     * @param distributors
     * @param producers
     */
    public void distributorsChooseProducers(final Map<Integer, Distributor> distributors,
                                            final Map<Integer, Producer> producers) {
        for (Map.Entry<Integer, Distributor> distributor : distributors.entrySet()) {
            if (distributor.getValue().hasToReconsiderProductionCost()) {
                distributor.getValue().removeProducers(producers);
                distributor.getValue().chooseProducer(producers);
            } else if (!distributor.getValue().hasProducers()) {
                distributor.getValue().chooseProducer(producers);
            }
        }
    }

    /**
     * Prin intermediul acestei metode fiecare distribuitor isi calculeaza valoarea
     * contractului pe care il ofera in luna respectiva.
     * @param distributors
     */
    public void distributorsCalculateContracts(final Map<Integer, Distributor> distributors) {
        for (Map.Entry<Integer, Distributor> distributor : distributors.entrySet()) {
            distributor.getValue().calculateContractValue();
        }
    }

    /**
     * Prin intermediul acestei metode consumatorii isi aleg distribuitori in functie
     * de cel mai mic contract existent pe piata.
     * @param consumers
     * @param distributors
     */
    public void consumersChooseDistributors(final Map<Integer, Consumer> consumers,
                                            final Map<Integer, Distributor> distributors) {
        Map<Integer, Distributor> sortedDistributors
                = SortDistributors.getInstance().sort(distributors);
        for (Map.Entry<Integer, Consumer> consumer : consumers.entrySet()) {
            if (!consumer.getValue().hasDistributor() && !consumer.getValue().getIsBankrupt()) {
                for (Map.Entry<Integer, Distributor> distributor : sortedDistributors.entrySet()) {
                    Distributor currentDistributor = distributor.getValue();
                    if (!currentDistributor.getIsBankrupt()) {
                        consumer.getValue().setDistributor(currentDistributor.getID(),
                                currentDistributor.getContractCost(),
                                currentDistributor.getContractLength());
                        currentDistributor.addConsumer(consumer.getValue().getID());
                        break;
                    }
                }
            }
        }
    }

    /**
     * Prin intermediul acestei metode, toti consumatorii isi primesc salariile.
     * @param consumers
     */
    public void consumersGetSalaries(final Map<Integer, Consumer> consumers) {
        for (Map.Entry<Integer, Consumer> consumer : consumers.entrySet()) {
            if (!consumer.getValue().getIsBankrupt()) {
                consumer.getValue().getSalary();
            }
        }
    }

    /**
     * Prin intermediul acestei metode toti consumatorii isi platesc taxele.
     * @param consumers
     * @param distributors
     */
    public void consumersPayTaxes(final Map<Integer, Consumer> consumers,
                                  final Map<Integer, Distributor> distributors) {
        for (Map.Entry<Integer, Consumer> consumer : consumers.entrySet()) {
            Consumer currentConsumer = consumer.getValue();
            if (!currentConsumer.getIsBankrupt()) {
                if (!currentConsumer.hasDebts()) {
                    Distributor distributor
                            = distributors.get(currentConsumer.getDistributorID());
                    currentConsumer.payTaxes(distributor);
                } else {
                    Distributor distributor
                            = distributors.get(currentConsumer.getDistributorID());
                    Distributor oldDistributor
                            = distributors.get(currentConsumer.getHasDebtsToID());
                    currentConsumer.payDebts(oldDistributor, distributor);
                }
            }
        }
    }

    /**
     * Prin intermediul acestei metode, toti distribuitorii isi platesc taxele.
     * @param distributors
     */
    public void distributorsPayTaxes(final Map<Integer, Distributor> distributors) {
        for (Map.Entry<Integer, Distributor> distributor : distributors.entrySet()) {
            distributor.getValue().payTaxes();
        }
    }

    /**
     * Prin intermediul acestei metode toti consumatorii care sunt bankrupt sau nu mai
     * au contract cu nimeni sunt eliminati din lista de consumatori a distribuitorului
     * cu care colaborau. De asemenea, daca un distribuitor ramane falimentat, el va
     * elibera toti consumatorii, acestia pierzand si datoriile pe care le aveau la el.
     * @param consumers
     * @param distributors
     */
    public void removeConsumers(final Map<Integer, Consumer> consumers,
                                final Map<Integer, Distributor> distributors) {
        for (Map.Entry<Integer, Distributor> distributor : distributors.entrySet()) {
            Distributor currentDistributor = distributor.getValue();
            if (currentDistributor.getIsBankrupt()) {
                if (currentDistributor.getConsumersID().size() != 0) {
                    for (Integer consumerID : currentDistributor.getConsumersID()) {
                        consumers.get(consumerID).setNoDistributor();
                    }
                }
            } else {
                List<Integer> removeHelper = new ArrayList<>(currentDistributor.getConsumersID());
                for (Integer consumerID : removeHelper) {
                    Consumer consumer = consumers.get(consumerID);
                    if (consumer.getIsBankrupt() || consumer.getRemainedContractMonths() == 0) {
                        currentDistributor.removeConsumer(consumerID);
                        consumer.setDistributor(-1, -1, -1);
                        consumer.setHasDistributor(false);
                        //consumer.setNoDistributor();
                    }
                }
            }
        }
    }
}
