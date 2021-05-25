package entities.update;

import entities.Consumer;

import java.util.ArrayList;
import java.util.List;

public final class Update {
    private List<Consumer> newConsumers;
    private List<DistributorChanges> distributorChanges;
    private List<ProducerChanges> producerChanges;

    public Update() {
        this.newConsumers = new ArrayList<>();
        this.distributorChanges = new ArrayList<>();
        this.producerChanges = new ArrayList<>();
    }

    /**
     * @return schimbul costurilor distributorilor
     */
    public List<DistributorChanges> getDistributorChanges() {
        return distributorChanges;
    }

    /**
     * @return noul consumator
     */
    public List<Consumer> getNewConsumers() {
        return newConsumers;
    }

    /**
     * @return schimbul serviciilor producatorilor
     */
    public List<ProducerChanges> getProducerChanges() {
        return producerChanges;
    }

    /**
     * Suprascrierea metodei toString()
     * @return
     */
    @Override
    public String toString() {
        return "Update{"
                + "newConsumer=" + newConsumers
                + ", distributorChanges=" + distributorChanges
                + ", producerChanges=" + producerChanges
                + '}';
    }
}
