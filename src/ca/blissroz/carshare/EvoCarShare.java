package ca.blissroz.carshare;

public class EvoCarShare extends AbstractCarShareService {
    private static final String CAR_SHARE_NAME = "Evo";

    private static final double PER_MINUTE = 0.41;
    private static final double PER_HOUR = 14.99;
    private static final double PER_DAY = 89.99;

    /**
     * In Evo, the cost is automatically the best one given your time and distance (they auto apply
     *  hourly rates, you do not select while booking)
     */
    public EvoCarShare() {}

    @Override
    public Result getCost(double distance, double time, String timeUnit, int numPeople) {
        double timeInHours = getTimeInHours(time, timeUnit);
        return new Result(getName() + " cost",
                dayCost(timeInHours));
    }

    private double dayCost(double time) {
        return Math.floor(time / HOURS_PER_DAY) * PER_DAY
                + Math.min(hourCost(time - Math.floor(time / HOURS_PER_DAY)), PER_DAY);
    }
    private double hourCost(double time) {
        return Math.floor(time) * PER_HOUR
                + Math.min(minsCost(time - Math.floor(time)), PER_HOUR);
    }

    private double minsCost(double time) {
        return time * MINUTES_PER_HOUR * PER_MINUTE;
    }

    @Override
    public String getName() {
        return CAR_SHARE_NAME;
    }
}
