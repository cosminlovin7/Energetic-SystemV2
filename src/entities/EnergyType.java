package entities;

import utils.Constants;

/**
 * Types of energy produced by EnergyProducers
 */
public enum EnergyType {
    WIND("WIND", true),
    SOLAR("SOLAR", true),
    HYDRO("HYDRO", true),
    COAL("COAL", false),
    NUCLEAR("NUCLEAR", false);

    private final String label;

    private final boolean renewable;

    EnergyType(String label, boolean renewable) {
        this.label = label;
        this.renewable = renewable;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Metoda intoarce true/false daca energia este de reutilizabila
     * sau nu.
     * @param energyType
     * @return
     */
    public static boolean isRenewable(String energyType) {
        if (energyType.equalsIgnoreCase(Constants.WIND)
         || energyType.equalsIgnoreCase(Constants.SOLAR)
         || energyType.equalsIgnoreCase(Constants.HYDRO)) {
            return true;
        }
        return false;
    }
}
