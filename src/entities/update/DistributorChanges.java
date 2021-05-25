package entities.update;

public final class DistributorChanges {
    private Integer id;
    private long infrastructureCost;

    public DistributorChanges(final Integer id,
                      final long infrastructureCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
    }

    /**
     * @return id-ul distribuitorului ce trebuie updatat
     */
    public Integer getID() {
        return id;
    }
    /**
     * @return noul cost de infrastructura
     */
    public long getInfrastructureCost() {
        return infrastructureCost;
    }
}
