package ca.blissroz.carshare.services;

import java.util.*;

public class Car2GoCarShare extends AbstractCarShareService {
    private static final String CAR_SHARE_NAME = "Car2Go";
    private static final int ONE_HOUR = 1, THREE_HOUR = 3, SIX_HOUR = 6;
    private static final int ONE_DAY = 1, TWO_DAY = 2, THREE_DAY = 3;
    private static final int SMALL_CAR_CAPACITY = 2;

    private static final Double SMALL_HOURLY_RATE = 0.32;
    private static final Double LARGE_HOURLY_RATE = 0.45;

    private static final Map<Integer, Double> SMALL_CAR_COST_BY_HOUR = new HashMap<>() {{
        put(ONE_HOUR, 13.0);
        put(THREE_HOUR, 35.0);
        put(SIX_HOUR, 49.0);
        put(HOURS_PER_DAY * ONE_DAY, 69.0);
        put(HOURS_PER_DAY * TWO_DAY, 129.0);
        put(HOURS_PER_DAY * THREE_DAY, 179.0);
    }};

    private static final Map<Integer, Double> LARGE_CAR_COST_BY_HOUR = new HashMap<>() {{
        put(ONE_HOUR, 17.0);
        put(THREE_HOUR, 45.0);
        put(SIX_HOUR, 69.0);
        put(HOURS_PER_DAY * ONE_DAY, 99.0);
        put(HOURS_PER_DAY * TWO_DAY, 179.0);
        put(HOURS_PER_DAY * THREE_DAY, 249.0);
    }};

    private static final double KM_INCLUDED_3DAY = 600;
    private static final double KM_INCLUDED_2DAY = 400;
    private static final double KM_INCLUDED_BASE = 200;
    private static final double COST_PER_KM = 0.45;

    /**
     * In Car2Go, you preselect the package such as 1 hr, 3 hr, etc. You cannot combine packages, and you
     * pay the per minute rate once the time from that package has expired. There is no other option.
     */
    public Car2GoCarShare() {
    }

    @Override
    public Result getCost(final double distance,
                          final double time,
                          final String timeUnit,
                          final int numPeople) {
        if (numPeople > SMALL_CAR_CAPACITY) {
            return getCost(distance, getTimeInHours(time, timeUnit), LARGE_CAR_COST_BY_HOUR, LARGE_HOURLY_RATE);
        } else {
            return getCost(distance, getTimeInHours(time, timeUnit), SMALL_CAR_COST_BY_HOUR, SMALL_HOURLY_RATE);
        }
    }

    /**
     * Note: currently, not optimized for distance. Yikes !
     *
     * @param distance in km
     * @param time     in hours
     * @param costs    associated with vehicle size
     * @return total cost of trip
     */
    private Result getCost(final double distance,
                           final double time,
                           final Map<Integer, Double> costs,
                           final Double perMinute) {
        TreeMap<Double, String> allCosts = new TreeMap<>();
        double baseDistanceCost = Math.max(distance - KM_INCLUDED_BASE, 0) * COST_PER_KM;
        allCosts.put(time * MINUTES_PER_HOUR * perMinute + baseDistanceCost, "Per minute rental cost");

        Double timeCost, distanceCost;
        Iterator it = costs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry cost = (Map.Entry) it.next();
            Double costForTime = (Double) cost.getValue();
            Integer amountOfTime = (Integer) cost.getKey();

            timeCost = costForTime + Math.max((time - amountOfTime) * MINUTES_PER_HOUR * perMinute, 0);
            if (amountOfTime == THREE_DAY * HOURS_PER_DAY) {
                distanceCost = Math.max(distance - KM_INCLUDED_3DAY, 0) * COST_PER_KM;
            } else if (amountOfTime == TWO_DAY * HOURS_PER_DAY) {
                distanceCost = Math.max(distance - KM_INCLUDED_2DAY, 0) * COST_PER_KM;
            } else {
                distanceCost = baseDistanceCost;
            }
            allCosts.put(timeCost + distanceCost, getName() + " " + amountOfTime + " hour rental cost");
        }

        Map.Entry<Double, String> cheapestOption = allCosts.firstEntry();
        return new Result(cheapestOption.getValue(), cheapestOption.getKey());
    }

    @Override
    public String getName() {
        return CAR_SHARE_NAME;
    }
}
