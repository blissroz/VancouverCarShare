package ca.blissroz.carshare;

public class ModoCarShare extends AbstractCarShareService {
    private static final String CAR_SHARE_NAME = "Modo";
    private static final double BASIC_COST_PER_HOUR = 9;
    private static final double BASIC_COST_PER_DAY = 72;
    private static final double BASIC_KM_INCLUDED = 250;
    private static final double PLUS_COST_PER_HOUR = 5;
    private static final double PLUS_COST_PER_DAY = 50;
    private static final double COST_PER_KM = 0.30;
    private static final double BASIC_HOURS_THRESHOLD_DAILY = BASIC_COST_PER_DAY / BASIC_COST_PER_HOUR;
    private static final double PLUS_HOURS_THRESHOLD_DAILY = PLUS_COST_PER_DAY / PLUS_COST_PER_HOUR;

    private boolean hasModoPlus;

    public ModoCarShare(boolean hasModoPlus) {
        this.hasModoPlus = hasModoPlus;
    }

    @Override
    public double getCost(final double distance,
                          final double time,
                          final String timeUnit,
                          final int numPeople) {
        return getCostOfTrip(distance, getTimeInHours(time, timeUnit));
    }

    private double getCostOfTrip(final double distance,
                                 final double time) {
        if (!hasModoPlus) {
            return getBasicCost(distance, time);
        } else {
            return Math.min(getBasicCost(distance, time), getPlusCost(distance, time));
        }
    }

    private double getBasicCost(final double distance,
                                final double time) {
        double days = (time % HOURS_PER_DAY >= BASIC_HOURS_THRESHOLD_DAILY) ?
                Math.ceil(time / HOURS_PER_DAY) : Math.floor(time / HOURS_PER_DAY);
        double hours = (time % HOURS_PER_DAY < BASIC_HOURS_THRESHOLD_DAILY) ?
                time % HOURS_PER_DAY : 0;
        double paidDistance = Math.max(distance - BASIC_KM_INCLUDED, 0);

        return (paidDistance * COST_PER_KM)
                + (days * BASIC_COST_PER_DAY)
                + (hours * BASIC_COST_PER_HOUR);
    }

    private double getPlusCost(final double distance,
                               final double time) {
        double days = (time % HOURS_PER_DAY >= PLUS_HOURS_THRESHOLD_DAILY) ?
                Math.ceil(time / HOURS_PER_DAY) : Math.floor(time / HOURS_PER_DAY);
        double hours = (time % HOURS_PER_DAY < PLUS_HOURS_THRESHOLD_DAILY) ?
                time % HOURS_PER_DAY : 0;

        return (distance * COST_PER_KM)
                + (days * PLUS_COST_PER_DAY)
                + (hours * PLUS_COST_PER_HOUR);
    }

    @Override
    public String getName() {
        return CAR_SHARE_NAME;
    }
}
