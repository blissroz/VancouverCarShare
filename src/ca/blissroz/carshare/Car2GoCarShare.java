package ca.blissroz.carshare;

import java.util.HashMap;
import java.util.Map;


public class Car2GoCarShare extends AbstractCarShareService {
    private static final String CAR_SHARE_NAME = "Car2Go";
    private static final int NO_HOUR = 0, ONE_HOUR = 1, THREE_HOUR = 3, SIX_HOUR = 6;
    private static final int ONE_DAY = 1, TWO_DAY = 2, THREE_DAY = 3;

    private static final Map<Integer, Double> SMALL_CAR_COST_BY_HOUR = new HashMap<>() {{
        put(NO_HOUR, 0.32);
        put(ONE_HOUR, 13.0);
        put(THREE_HOUR, 35.0);
        put(SIX_HOUR, 49.0);
        put(HOURS_PER_DAY * ONE_DAY, 69.0);
        put(HOURS_PER_DAY * TWO_DAY, 129.0);
        put(HOURS_PER_DAY * THREE_DAY, 179.0);
    }};

    private static final double BIG_COST_PER_MINUTE = 0.45;
    private static final double BIG_COST_PER_HOUR = 17;
    private static final double BIG_COST_PER_3HOUR = 45;
    private static final double BIG_COST_PER_6HOUR = 69;
    private static final double BIG_COST_PER_DAY = 99;
    private static final double BIG_COST_PER_2DAY = 179;
    private static final double BIG_COST_PER_3DAY = 249;

    private static final double KM_INCLUDED_3DAY = 600;
    private static final double KM_INCLUDED_2DAY = 400;
    private static final double KM_INCLUDED_BASE = 200;
    private static final double COST_PER_KM = 0.45;

    public Car2GoCarShare() {}

    @Override
    public double getCost(final double distance,
                          final double time,
                          final String timeUnit,
                          final int numPeople) {
        return getSmallCarCost(distance, getTimeInHours(time, timeUnit));
    }

    private double getSmallCarCost(final double distance,
                                   final double time) {
        double cost = 0;
        long days = Math.round(Math.floor(time / HOURS_PER_DAY));
        double hours = time - (days * HOURS_PER_DAY);
        boolean addedDistance = false;

        if (days >= THREE_DAY) {
            cost += SMALL_CAR_COST_BY_HOUR.get(HOURS_PER_DAY * THREE_DAY) * (days / THREE_DAY);
            cost += Math.max(distance - KM_INCLUDED_3DAY, 0) * COST_PER_KM;
            addedDistance = true;
            days -= days / THREE_DAY;
        }

        if (days == TWO_DAY) {
            cost += SMALL_CAR_COST_BY_HOUR.get(HOURS_PER_DAY * TWO_DAY);
            cost += Math.max(distance - KM_INCLUDED_2DAY, 0) * COST_PER_KM;
            addedDistance = true;
        } else if (days == ONE_DAY) {
            cost += SMALL_CAR_COST_BY_HOUR.get(HOURS_PER_DAY);
        }

        if (hours >= SIX_HOUR) {
            cost += Math.min((SMALL_CAR_COST_BY_HOUR.get(SIX_HOUR) * hours / SIX_HOUR)
                    + (SMALL_CAR_COST_BY_HOUR.get(NO_HOUR) * hours % SIX_HOUR),
                    SMALL_CAR_COST_BY_HOUR.get(HOURS_PER_DAY));
        } else if (hours >= THREE_HOUR) {
            cost += (SMALL_CAR_COST_BY_HOUR.get(THREE_HOUR) * hours / THREE_HOUR)
                            + (SMALL_CAR_COST_BY_HOUR.get(NO_HOUR) * hours % THREE_HOUR);
        } else if (hours >= ONE_HOUR) {
            cost += (SMALL_CAR_COST_BY_HOUR.get(ONE_HOUR) * hours / ONE_HOUR)
                    + (SMALL_CAR_COST_BY_HOUR.get(NO_HOUR) * hours % ONE_HOUR);
        } else {
            cost += SMALL_CAR_COST_BY_HOUR.get(NO_HOUR) * hours;
        }

        if (!addedDistance) {
            cost += Math.max(distance - KM_INCLUDED_BASE, 0) * COST_PER_KM;
        }

        return cost;
    }

    @Override
    public String getName() {
        return CAR_SHARE_NAME;
    }
}
