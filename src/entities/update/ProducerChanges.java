package entities.update;

public final class ProducerChanges {
    private final Integer id;
    private final long energyPerDistributor;

    public ProducerChanges(final Integer id,
                           final long energyPerDistributor) {
        this.id = id;
        this.energyPerDistributor = energyPerDistributor;
    }

    public Integer getID() {
        return id;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }
}
